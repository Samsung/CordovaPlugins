/*
 * Copyright (c) 2015 Samsung Electronics, Co. Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.samsung.spen;

import java.io.File;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.pen.Spen;
import com.samsung.spen.SpenException.SpenExceptionType;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * This class is responsible for launching both popup and Inline surface.
 */
public class SpenPlugin extends CordovaPlugin {
    private static final int ID = 0;
    private static final int SPEN_FLAGS = 1;
    private static final int RETURN_TYPE = 2;
    private static final int BACKGROUND_COLOR = 3;
    private static final int BACKGROUND_IMAGE_SCALE_TYPE = 4;
    private static final int IMAGE_PATH = 5;
    private static final int IMAGE_URI_SCALE_TYPE = 6;
    private static final int RECTANGLE_X_VALUE = 7;
    private static final int RECTANGLE_Y_VALUE = 8;
    private static final int POPUP_WIDTH = 7;
    private static final int POPUP_HEIGHT = 8;
    private static final int WIDTH = 9;
    private static final int HEIGHT = 10;
    private static final int BODY_RECTANGLE_X_VALUE = 11;
    private static final int BODY_RECTANGLE_Y_VALUE = 12;
    private static final int SPEN_AND_HAND_SUPPORTED = 100;
    private static final int ONLY_HAND_SUPPORTED = 101;
    private static final int SPEN_INITILIZATION_ERROR = 102;
    private static final int MAX_ID_LENGTH = 100;
    private static final String META_DATA = "com.samsung.cordova.spen";
    private static final String TAG = "SpenPlugin";
    private Boolean pluginMetadata = null;
    private Boolean isStatic = null;
    private Activity mActivity;
    private SpenContextParams mContextParams;
    private String mCurrentWorkingId;
    private SpenSurfaceViews mSpenSurfaceViews = new SpenSurfaceViews();
    private SpenSurfaceViews mSpenSurfaceViewsPopup = new SpenSurfaceViews();
    private int mCurrentSurfaceType;
    private int surfaceCount = 0;
    private int mSpenState;

    /** 
     * 
     * Set Id and Syrface Type
     * 
     * @params Id 
     *           String
     * @params surfaceType
     *           int
     * 
     */
    void setIdAndSurfaceType(String id, int surfaceType) {
        mCurrentWorkingId = id;
        mCurrentSurfaceType = surfaceType;
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        init();
    }

    private void init() {
        if (isStatic == null || pluginMetadata == null) {
            mActivity = cordova.getActivity();
            String mPackageName = mActivity.getPackageName();
            PackageManager pm = mActivity.getPackageManager();
            try {
                ApplicationInfo ai = pm.getApplicationInfo(mPackageName,
                        PackageManager.GET_META_DATA);
                if (ai.metaData != null) {
                    pluginMetadata = ai.metaData.getBoolean(META_DATA);
                } else {
                    pluginMetadata = false;
                }
            } catch (NameNotFoundException e) {
                pluginMetadata = false;
            }

            isStatic = false;
            int resId = mActivity.getResources().getIdentifier("spen_static",
                    "bool", mActivity.getPackageName());
            try {
                if (resId != 0) {
                    isStatic = mActivity.getResources().getBoolean(resId);
                }
            } catch (Resources.NotFoundException re) {
                isStatic = false;
            }
            if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                Log.d(TAG, "Static is " + isStatic);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.cordova.CordovaPlugin#onActivityResult(int, int,
     * android.content.Intent)
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside onActivityResult");
            Log.d(TAG, "Intent: " + intent + ", requestCode: " + requestCode);
        }
        if (requestCode == Utils.REQUEST_CODE_SELECT_IMAGE_BACKGROUND) {
            if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                Log.d(TAG, "on background image selected");
            }
            if (mCurrentSurfaceType == Utils.SURFACE_INLINE) {
                mSpenSurfaceViews.getSurfaceView(mCurrentWorkingId)
                        .setasBgImage(intent);
            } else {
                Message msgObj = setBgHandler.obtainMessage();
                Bundle b = new Bundle();
                b.putParcelable("intent", intent);
                msgObj.setData(b);
                setBgHandler.sendMessage(msgObj);
            }
        } else {
            if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                Log.d(TAG, "request code is not matching");
            }
        }
    }

    private Handler setBgHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                Log.d(TAG, "inside setBgHandler");
            }
            Intent intent = (Intent) msg.getData().getParcelable("intent");
            mSpenSurfaceViewsPopup.getSurfaceView(mCurrentWorkingId)
                    .setasBgImage(intent);
        }
    };

    private void initContextDetails(CallbackContext callbackContext) {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside initContextDetails");
        }
        if (mActivity == null) {
            mActivity = this.cordova.getActivity();
        }
        if (mContextParams == null) {
            mContextParams = new SpenContextParams();
            mContextParams.setSpenCustomDrawPlugin(this);
        }
        mContextParams.setCallbackContext(callbackContext);
    }

    @Override
    public boolean execute(String action, JSONArray args,
            CallbackContext callbackContext) throws JSONException {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside execute");
        }

        // sometimes, init is not called from initialize
        init();
        // Do not allow apis if metadata is missing
        if (!pluginMetadata) {
            callbackContext.error("METADATA_MISSING");
            Log.e(TAG, "Metadata is missing");
            return false;
        }

        initContextDetails(callbackContext);
        final CallbackContext finalCallbackContext = callbackContext;
        mSpenState = isSpenFeatureEnabled(mActivity.getApplicationContext(),
                callbackContext);
        if (action.equals("isSupported")) {
            if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                Log.d(TAG, "Inside isSpenSupported");
            }
            if (mSpenState == SPEN_AND_HAND_SUPPORTED) {
                callbackContext
                        .success(SpenExceptionType.SPEN_AND_HAND_SUPPORTED
                                .toString());
                return true;
            } else if (mSpenState == ONLY_HAND_SUPPORTED) {
                callbackContext.success(SpenExceptionType.ONLY_HAND_SUPPORTED
                        .toString());
                // handled this in isSpenFeatureEnabled itself as it common
                // for many cases.
            }
        } else if (action.equals("launchSurfaceInline")) {
            if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                Log.d(TAG, "creating the spen surface inline");
            }
            SpenTrayBarOptions options = createTrayBarOptions(args,
                    Utils.SURFACE_INLINE, callbackContext);
            final SpenTrayBarOptions inlineOptions = options;
            if (inlineOptions != null && mSpenState != SPEN_INITILIZATION_ERROR) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String id = inlineOptions.getId();
                        if (mSpenSurfaceViews.getSurfaceView(id) == null) {
                            if (surfaceCount >= SpenSurfaceViews.MAX_SURFACE_COUNT) {
                                SpenException
                                        .sendPluginResult(
                                                SpenExceptionType.MAX_SURFACE_LIMIT_REACHED,
                                                finalCallbackContext);
                            } else {
                                SPenSurfaceWithTrayBar mPaintViewTrayBar = null;
                                mPaintViewTrayBar = new SPenSurfaceWithTrayBar(
                                        mContextParams, inlineOptions);
                                mSpenSurfaceViews.addSurfaceView(id,
                                        mPaintViewTrayBar);
                                surfaceCount++;
                                if (!mSpenSurfaceViews.getSurfaceView(id)
                                        .createSPenSurfaceWithTrayBar()) {
                                    mSpenSurfaceViews.removeSurfaceView(id);
                                    surfaceCount--;
                                    SpenException
                                            .sendPluginResult(
                                                    SpenExceptionType.FAILED_CREATE_SURFACE,
                                                    finalCallbackContext);
                                } else {
                                    // finalCallbackContext.success();
                                    PluginResult pluginResult = new PluginResult(
                                            PluginResult.Status.OK, "");
                                    pluginResult.setKeepCallback(true);
                                    finalCallbackContext
                                            .sendPluginResult(pluginResult);
                                }
                            }
                        } else {
                            SpenException
                                    .sendPluginResult(
                                            SpenExceptionType.SURFACE_ID_ALREADY_EXISTS,
                                            finalCallbackContext);
                        }
                    }
                });
            }
        } else if (action.equals("launchSurfacePopup")) {
            if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                Log.d(TAG, "creating the spen surface popup");
            }
            SpenTrayBarOptions options = createTrayBarOptions(args,
                    Utils.SURFACE_POPUP, callbackContext);
            if (options != null && mSpenState != SPEN_INITILIZATION_ERROR) {
                String id = options.getId();
                if (mSpenSurfaceViewsPopup.getSurfaceView(id) == null) {
                    if (surfaceCount >= SpenSurfaceViews.MAX_SURFACE_COUNT) {
                        SpenException.sendPluginResult(
                                SpenExceptionType.MAX_SURFACE_LIMIT_REACHED,
                                finalCallbackContext);
                    } else {
                        SPenSurfaceWithTrayBar mPaintViewTrayBar = null;
                        mPaintViewTrayBar = new SPenSurfaceWithTrayBar(
                                mContextParams, options);
                        mSpenSurfaceViewsPopup.addSurfaceView(id,
                                mPaintViewTrayBar);
                        surfaceCount++;
                        if (!mSpenSurfaceViewsPopup.getSurfaceView(id)
                                .createSPenSurfaceWithTrayBar()) {
                            mSpenSurfaceViewsPopup.removeSurfaceView(id);
                            surfaceCount--;
                            SpenException.sendPluginResult(
                                    SpenExceptionType.FAILED_CREATE_SURFACE,
                                    finalCallbackContext);
                        } else {
                            PluginResult pluginResult = new PluginResult(
                                    PluginResult.Status.OK, "");
                            pluginResult.setKeepCallback(true);
                            finalCallbackContext.sendPluginResult(pluginResult);
                        }
                    }
                } else {
                    mSpenSurfaceViewsPopup.getSurfaceView(id)
                            .changeSPenTrayBarOptions(options, mContextParams);
                    mSpenSurfaceViewsPopup.getSurfaceView(id)
                            .openSPenSurfaceWithTrayBar();
                }
            }
        } else if (action.equals("removeSurfaceInline")) {
            if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                Log.d(TAG, "removing SpenSurface Inline");
            }

            String tempId = args.getString(ID);
            if (tempId != null) {
                tempId = tempId.trim();
                if (tempId.length() > MAX_ID_LENGTH) {
                    tempId = tempId.substring(0, MAX_ID_LENGTH);
                }
            }

            final String id = tempId;
            if (id == null || id.equals("") || id.equals("null")) {
                SpenException.sendPluginResult(
                        SpenExceptionType.INVALID_SURFACE_ID, callbackContext);
                return false;
            }

            if (mSpenState != SPEN_INITILIZATION_ERROR) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mSpenSurfaceViews.getSurfaceView(id) != null) {
                            surfaceCount--;
                            mSpenSurfaceViews.getSurfaceView(id)
                                    .removeSurface();
                            mSpenSurfaceViews.removeSurfaceView(id);
                            finalCallbackContext.success(id);
                        } else {
                            SpenException.sendPluginResult(
                                    SpenExceptionType.SURFACE_ID_NOT_EXISTS,
                                    finalCallbackContext);
                        }
                    }
                });
            }
        } else if (action.equals("removeSurfacePopup")) {
            if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                Log.d(TAG, "removing SpenSurface Popup");
            }
            String tempId = args.getString(ID);
            if (tempId != null) {
                tempId = tempId.trim();
                if (tempId.length() > MAX_ID_LENGTH) {
                    tempId = tempId.substring(0, MAX_ID_LENGTH);
                }
            }

            final String id = tempId;

            if (id == null || id.equals("") || id.equals("null")) {
                SpenException.sendPluginResult(
                        SpenExceptionType.INVALID_SURFACE_ID, callbackContext);
                return false;
            }

            if (mSpenSurfaceViewsPopup.getSurfaceView(id) != null) {
                surfaceCount--;
                mSpenSurfaceViewsPopup.getSurfaceView(id).removeSurface();
                mSpenSurfaceViewsPopup.removeSurfaceView(id);
                finalCallbackContext.success(id);
            } else {
                SpenException.sendPluginResult(
                        SpenExceptionType.SURFACE_ID_NOT_EXISTS,
                        finalCallbackContext);
            }
        } else {
            callbackContext.error(SpenExceptionType.ACTION_INVALID.toString());
            return false;
        }
        return true;
    }
    /**
     * 
     * @param args
     *                 JSON array of options sent from the script.
     * @param surfaceType
     *                int
     * @param callbackContext
     *                CallbackContext
     * @return	options
     *                SpenTrayBarOptions
     * @throws JSONException
     */
    private SpenTrayBarOptions createTrayBarOptions(JSONArray args,
            int surfaceType, CallbackContext callbackContext)
            throws JSONException {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside createTrayBarOptions");
        }

        String tempId = args.getString(ID);
        if (tempId != null) {
            tempId = tempId.trim();
            if (tempId.length() > MAX_ID_LENGTH) {
                tempId = tempId.substring(0, MAX_ID_LENGTH);
            }
        }

        final String id = tempId;
        if (id == null || id.equals("") || id.equals("null")
                || !id.matches("^[ !#-)+-.0-9;=@-Z^-{}~]+$")) {
            SpenException.sendPluginResult(
                    SpenExceptionType.INVALID_SURFACE_ID, callbackContext);
            return null;
        }

        int sPenFlags = args.optInt(SPEN_FLAGS, Integer.MIN_VALUE);
        if (sPenFlags == Integer.MIN_VALUE || sPenFlags > Utils.MAX_FLAGS_VALUE
                || sPenFlags < Utils.MIN_FLAGS_VALUE) {
            SpenException.sendPluginResult(SpenExceptionType.INVALID_FLAGS,
                    callbackContext);
            return null;
        }

        int returnType = args.optInt(RETURN_TYPE);
        if (returnType != Utils.RETURN_TYPE_IMAGE_DATA
                && returnType != Utils.RETURN_TYPE_IMAGE_URI
                && returnType != Utils.RETURN_TYPE_TEXT) {

            SpenException.sendPluginResult(
                    SpenExceptionType.INVALID_RETURN_TYPE, callbackContext);
            return null;
        }

        String backgroundColor = args.getString(BACKGROUND_COLOR);

        String imagePath = args.getString(IMAGE_PATH);
        if (imagePath.equals("") || imagePath.equals("null")) {
            imagePath = null;
        } else {
            imagePath = Uri.decode(imagePath);
            String truncatedPath = truncateQueryPart(imagePath);
            File file = new File(truncatedPath);
            if (file.exists()) {
                imagePath = truncatedPath;
            }
        }

        int bgImageScaleType = args.optInt(BACKGROUND_IMAGE_SCALE_TYPE);
        if (bgImageScaleType != Utils.BACKGROUND_IMAGE_MODE_CENTER
                && bgImageScaleType != Utils.BACKGROUND_IMAGE_MODE_FIT
                && bgImageScaleType != Utils.BACKGROUND_IMAGE_MODE_STRETCH
                && bgImageScaleType != Utils.BACKGROUND_IMAGE_MODE_TILE) {
            bgImageScaleType = Utils.BACKGROUND_IMAGE_MODE_FIT;
        }

        int imageUriScaleType = args.optInt(IMAGE_URI_SCALE_TYPE);
        if (imageUriScaleType != Utils.IMAGE_URI_MODE_CENTER
                && imageUriScaleType != Utils.IMAGE_URI_MODE_FIT
                && imageUriScaleType != Utils.IMAGE_URI_MODE_TILE
                && imageUriScaleType != Utils.IMAGE_URI_MODE_STRETCH) {
            imageUriScaleType = Utils.IMAGE_URI_MODE_FIT;
        }

        if (surfaceType == Utils.SURFACE_INLINE) {
            if ((sPenFlags & Utils.FLAG_ADD_PAGE) == Utils.FLAG_ADD_PAGE) {
                if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                    Log.d(TAG, "Add Page is not supported in Inline");
                }
                sPenFlags = sPenFlags & ~Utils.FLAG_ADD_PAGE;
            }
        } else if (surfaceType == Utils.SURFACE_POPUP) {

            if ((sPenFlags & Utils.FLAG_EDIT) == Utils.FLAG_EDIT) {
                if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                    Log.d(TAG, "Edit Page is not supported in Popup");
                }
                sPenFlags = sPenFlags & ~Utils.FLAG_EDIT;
            }

            if ((sPenFlags & Utils.FLAG_PEN) == Utils.FLAG_PEN) {
                if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                    Log.d(TAG,
                            "Pen option is provided by default, is not configurable in Popup");
                }
                sPenFlags = sPenFlags & ~Utils.FLAG_PEN;

            }

            if ((sPenFlags & Utils.FLAG_ERASER) == Utils.FLAG_ERASER) {
                if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                    Log.d(TAG,
                            "Eraser option is provided by default, is not configurable in Popup");
                }
                sPenFlags = sPenFlags & ~Utils.FLAG_ERASER;

            }

            if ((sPenFlags & Utils.FLAG_UNDO_REDO) == Utils.FLAG_UNDO_REDO) {
                if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                    Log.d(TAG,
                            "Undo Redo option is provided by default, is not configurable in Popup");
                }
                sPenFlags = sPenFlags & ~Utils.FLAG_UNDO_REDO;

            }
        } else {
            SpenException.sendPluginResult(
                    SpenExceptionType.INVALID_SURFACE_TYPE, callbackContext);
            return null;
        }

        if ((sPenFlags & Utils.FLAG_TEXT_RECOGNITION) == Utils.FLAG_TEXT_RECOGNITION
                || (sPenFlags & Utils.FLAG_SHAPE_RECOGNITION) == Utils.FLAG_SHAPE_RECOGNITION) {
            sPenFlags = sPenFlags | Utils.FLAG_SELECTION;
        }

        SpenTrayBarOptions options = new SpenTrayBarOptions(sPenFlags);
        options.setId(id);
        options.setIsfeatureEnabled(mSpenState == SPEN_AND_HAND_SUPPORTED
                ? true
                : false);
        options.setColor(backgroundColor);
        options.setBgImageScaleType(bgImageScaleType);
        options.setImageUriScaleType(imageUriScaleType);
        options.setReturnType(returnType);
        options.setSurfaceType(surfaceType);
        options.setImagePath(imagePath);
        options.setDensity(mActivity.getApplicationContext().getResources()
                .getDisplayMetrics().density);

        if (surfaceType == Utils.SURFACE_INLINE) {
            long xRect = 0, yRect = 0, width = 0, height = 0, xBodyRect = 0, yBodyRect = 0;
            if (args.isNull(RECTANGLE_X_VALUE)
                    || args.isNull(RECTANGLE_Y_VALUE) || args.isNull(WIDTH)
                    || args.isNull(HEIGHT)) {
                SpenException.sendPluginResult(
                        SpenExceptionType.INVALID_INLINE_CORDINATES,
                        callbackContext);
                return null;
            } else {
                xRect = args.optLong(RECTANGLE_X_VALUE, Integer.MIN_VALUE);
                yRect = args.optLong(RECTANGLE_Y_VALUE, Integer.MIN_VALUE);
                width = args.optLong(WIDTH, Integer.MIN_VALUE);
                height = args.optLong(HEIGHT, Integer.MIN_VALUE);
                xBodyRect = args.optLong(BODY_RECTANGLE_X_VALUE,
                        Integer.MIN_VALUE);
                yBodyRect = args.optLong(BODY_RECTANGLE_Y_VALUE,
                        Integer.MAX_VALUE);
                if (xRect == Integer.MIN_VALUE || yRect == Integer.MIN_VALUE
                        || width == Integer.MIN_VALUE
                        || height == Integer.MIN_VALUE
                        || xBodyRect == Integer.MIN_VALUE
                        || yBodyRect == Integer.MIN_VALUE
                        || xRect > (long) Integer.MAX_VALUE
                        || yRect > (long) Integer.MAX_VALUE
                        || width > (long) Integer.MAX_VALUE
                        || height > (long) Integer.MAX_VALUE
                        || xBodyRect > (long) Integer.MAX_VALUE
                        || yBodyRect > (long) Integer.MAX_VALUE) {
                    SpenException.sendPluginResult(
                            SpenExceptionType.INVALID_INLINE_CORDINATES,
                            callbackContext);
                    return null;
                }
            }
            SurfacePosition surfacePosition = new SurfacePosition(
                    mActivity.getApplicationContext(), (int) width,
                    (int) height, (int) xRect - (int) xBodyRect, (int) yRect
                            - (int) yBodyRect);
            if (!surfacePosition.isSurfaceValid(options,
                    mActivity.getApplicationContext())) {
                SpenException.sendPluginResult(
                        SpenExceptionType.INVALID_INLINE_CORDINATES,
                        callbackContext);
                return null;
            }
            options.setSurfacePosition(surfacePosition);
        } else if (surfaceType == Utils.SURFACE_POPUP) {
            long popupWidth = 0, popupHeight = 0;
            popupWidth = args.optLong(POPUP_WIDTH, Integer.MIN_VALUE);
            popupHeight = args.optLong(POPUP_HEIGHT, Integer.MIN_VALUE);
            SurfacePosition surfacePosition = new SurfacePosition(
                    mActivity.getApplicationContext(), (int) popupWidth,
                    (int) popupHeight);
            options.setSurfacePosition(surfacePosition);
        }
        return options;
    }

    /**
     * truncates the part query part if the image has it Ex:-
     * /sdcard/hello.png?123 returns /sdcard/hello.png
     * 
     * @param imagePath
     *                String
     * @return imagePath
     */
    private String truncateQueryPart(String imagePath) {
        int qMarkIndex = imagePath.lastIndexOf('?');
        if (qMarkIndex != -1) {
            imagePath = imagePath.substring(0, qMarkIndex);
        }
        return imagePath;
    }

    /**
     * Delete SpenSurface
     * 
     * @param id
     *           String
     */
    void deleteSpenSurface(String id) {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside deleteSpenSurface");
        }
        if (mSpenSurfaceViews.getSurfaceView(id) != null) {
            mSpenSurfaceViews.removeSurfaceView(id);
        }
    }

    /**
     * checks if the Spen feature is enabled or not. Send the result as
     * SPEN_SUPPORTED if the Spen is supported otherwise the corresponding error
     * message.
     * 
     * @param context
     *                Context
     * @param callbackContext
     *                CallbackContext
     * @return spenState
     */
    private int isSpenFeatureEnabled(Context context,
            CallbackContext callbackContext) {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "inside isSpenFeatureEnabled");
        }
        int spenState = SPEN_INITILIZATION_ERROR;
        Spen spenPackage = new Spen();
        try {
            if (isStatic) {
                spenPackage.initialize(context, 5, Spen.SPEN_STATIC_LIB_MODE);
            } else {
                spenPackage.initialize(context);
            }
            if (spenPackage.isFeatureEnabled(Spen.DEVICE_PEN)) {
                spenState = SPEN_AND_HAND_SUPPORTED;
            } else {
                spenState = ONLY_HAND_SUPPORTED;
            }
        } catch (SsdkUnsupportedException e) {
            Log.d(TAG, "failed initializing the spen package " + e.getMessage());
            e.printStackTrace();
            // if the spen sdk version name (dynamic sdk) is lesser than
            // the jar version name (which is inlcuded in the spen plugin
            // then LIBRARY_UPDATE_IS_REQUIRED should be thrown.
            // Current, Spen SDK not handled it properly.
            SpenExceptionType errorType = null;
            boolean isExceptionTypeFound = false;
            if (spenPackage != null && !isStatic) {
                String dynamicSDKPkgName = Spen.SPEN_NATIVE_PACKAGE_NAME;
                try {
                    PackageInfo packageInfo = mActivity.getPackageManager()
                            .getPackageInfo(dynamicSDKPkgName, 0);
                    if (packageInfo != null) {
                        String dynamicSDKVersionName = packageInfo.versionName
                                .replace(".", "");
                        String pluginJarVersionName = spenPackage
                                .getVersionName().replace(".", "");
                        if (dynamicSDKVersionName
                                .compareTo(pluginJarVersionName) < 0) {
                            errorType = SpenExceptionType.LIBRARY_UPDATE_IS_REQUIRED;
                            isExceptionTypeFound = true;
                        }
                    }
                } catch (NameNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
            if (!isExceptionTypeFound) {
                errorType = SpenException.processUnsupportedException(e);
            }
            PluginResult pluginResult = new PluginResult(
                    PluginResult.Status.ERROR, errorType.toString());
            pluginResult.setKeepCallback(false);
            callbackContext.sendPluginResult(pluginResult);
        }
        spenPackage = null;
        return spenState;
    }
}
