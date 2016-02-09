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

package com.samsung.multiwindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.net.Uri;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.multiwindow.SMultiWindow;
import com.samsung.android.sdk.multiwindow.SMultiWindowActivity;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class launches the Multiwindow supported apps in Freestyle and in Splitstyle.
 */
public class MultiWindow extends CordovaPlugin {

    private SMultiWindow mMultiWindow = null;
    private boolean pluginMetadata = false;
    private int zoneInfo = SMultiWindowActivity.ZONE_B;
    private boolean windowSupport = false;
    private String windowType = "";

    private static final String META_DATA = "com.samsung.cordova.multiwindow";
    public static final int INIT_SUCCESS = 10;
    public static final int ZONE_A = 1;
    public static final int ZONE_B = 2;
    public static final int ZONE_FULL = 4;
    public static final String[] URI_SCHEMA_SUPPORTED = {"http://", "https://",
        "content://","file://","ftp://","tel://","geo://"};
    private static final String TAG = "Multiwindow_Plugin";
    private static final String MULTIWINDOW ="MultiWindowPlugin";

    // action_main free style input options
    private enum mainFsOptions {
        WINDOW_TYPE, SCALE_INFO, PACKAGE_NAME, ACTIVITY_NAME
    }

    // action_main split style input options
    private enum mainSsOptions {
        WINDOW_TYPE, ZONE_INFO, PACKAGE_NAME, ACTIVITY_NAME
    }

    // action_view free style input options
    private enum viewFsOptions {
        WINDOW_TYPE, SCALE_INFO, DATA_URI
    }

    // action_view split style input options
    private enum viewSsOptions {
        WINDOW_TYPE, ZONE_INFO, DATA_URI
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    	
    	if (Log.isLoggable(MULTIWINDOW, Log.DEBUG)) {
            Log.d(TAG, "Initialize multiwindow");
        }
        super.initialize(cordova, webView);
        String packageName = cordova.getActivity().getPackageName();
        PackageManager pm = cordova.getActivity().getPackageManager();
        ApplicationInfo ai = null;
        try {
            ai = pm.getApplicationInfo(packageName,
                    PackageManager.GET_META_DATA);
            if (ai.metaData != null) {
                pluginMetadata = ai.metaData.getBoolean(META_DATA);
            }
        } catch (NameNotFoundException e) {
			Log.e("MultiWindow", e.getMessage());
        }
    }

    /**
     * This method will initialize Multiwindow on the device.
     * 
     */
    private int intializeMultiwindow() {
    	
    	if (Log.isLoggable(MULTIWINDOW, Log.DEBUG)) {
            Log.d(TAG, "Inside intializeMultiwindow,");
        }
        mMultiWindow = new SMultiWindow();

        try {
            mMultiWindow.initialize(this.cordova.getActivity());
        } catch (SsdkUnsupportedException e) {
            return e.getType();
        }

        return INIT_SUCCESS;
    }

    /**
     * Gets the window type received from JS
     * 
     * @return Window Type (splitstyle or freestyle)
     * 
     */
    private String getWindowType() {
        return windowType;
    }

    /**
     * Gets the zone info received from JS
     * 
     * @return Zone information (SMultiWindowActivity.ZONE_A or
     *         SMultiWindowActivity.ZONE_B or SMultiWindowActivity.ZONE_FULL )
     * 
     */
    private int getZoneInfo() {
        return zoneInfo;
    }

    /**
     * Executes the request and returns PluginResult.
     * 
     * @param action
     *            The action to be executed.
     * @param args
     *            JSONArray of arguments for the plugin.
     * @param callbackContext
     *            The callback id used when calling back into JavaScript.
     * @return A PluginResult object with a status and message.
     */
    public boolean execute(String action, final JSONArray args,
            final CallbackContext callbackContext) throws JSONException {

        int ret = -1;

        try {
            // window type argument index is same in all cases of
            // createmultiwindow
            windowType = args.getString(mainFsOptions.WINDOW_TYPE.ordinal());

            // Do not allow apis if metadata is missing
            if (!pluginMetadata) {
                callbackContext.error("METADATA_MISSING");
                Log.e("MultiWindow", "Metadata is missing");
                return false;
            }

            // Verify the multiwindow capability of the device
            ret = intializeMultiwindow();
            if (ret != INIT_SUCCESS) {

                switch (ret) {
                	
                	case SsdkUnsupportedException.VENDOR_NOT_SUPPORTED:
                		callbackContext.error("VENDOR_NOT_SUPPORTED");
                		break;
                	case SsdkUnsupportedException.DEVICE_NOT_SUPPORTED:
                		callbackContext.error("DEVICE_NOT_SUPPORTED");
                		break;
                	default:
                		callbackContext.error("MUTLI_WINDOW_INITIALIZATION_FAILED");
                		break;
                }

                return false;
            }
            
            if (Log.isLoggable(MULTIWINDOW, Log.DEBUG)) {
                Log.d(TAG, "Multiwindow initialization is successful"+ret);
            }

            // Check window support
            windowSupport = isMultiWindowSupported(windowType);
            if (windowType.equalsIgnoreCase("freestyle")) {

                if (!windowSupport) {
                    callbackContext.error("FREE_STYLE_NOT_SUPPORTED");
                }

            } else if (windowType.equalsIgnoreCase("splitstyle")) {

                if (!windowSupport) {
                    callbackContext.error("SPLIT_STYLE_NOT_SUPPORTED");
                }
            } else {

                callbackContext.error("INVALID_WINDOW_TYPE");
            }
            
            
            if (Log.isLoggable(MULTIWINDOW, Log.DEBUG)) {
                Log.d(TAG, "MultiWindow window type Supported:"+windowSupport);
            }

            // Get zone info in case of split style
            if (windowSupport
                    && (action.equals("action_main") || action
                            .equals("action_view"))) {

                if (windowType.equalsIgnoreCase("splitstyle")) {
                	
                    switch (args.optInt(mainSsOptions.ZONE_INFO.ordinal())) {
                    	case ZONE_A:
                    		Log.d(TAG, "Zone A selected");
                    		zoneInfo = SMultiWindowActivity.ZONE_A;
                    		break;
                    	case ZONE_B:
                    		Log.d(TAG, "Zone B selected");
                    		zoneInfo = SMultiWindowActivity.ZONE_B;
                    		break;
                    	case ZONE_FULL:
                    		Log.d(TAG, "Zone Full selected");
                    		zoneInfo = SMultiWindowActivity.ZONE_FULL;
                    		break;
                    	default:
                    		Log.d(TAG, "Zone is not selected");
                    		callbackContext.error("INVALID_ZONEINFO");
                    		return false;
                    }
                }
            }
        } catch (Exception e) {

            callbackContext.error(e.getMessage());
        }

        if (action.equals("action_main")) {

        	if (Log.isLoggable(MULTIWINDOW, Log.DEBUG)) {
                Log.d(TAG, "Action is action_main");
            }
            if (!windowSupport) {
                return false;
            }

            cordova.getThreadPool().execute(new Runnable() {

                public void run() {

                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    int packageNameIndex = 0;
                    int activityNameIndex = 0;
                    float scaleInfo = 0;
                    int zoneInfo = getZoneInfo();
                    String windowType = getWindowType();

                    if (windowType.equalsIgnoreCase("splitstyle")) {
                        packageNameIndex = mainSsOptions.PACKAGE_NAME.ordinal();
                        activityNameIndex = mainSsOptions.ACTIVITY_NAME
                                .ordinal();
                    } else {
                        packageNameIndex = mainFsOptions.PACKAGE_NAME.ordinal();
                        activityNameIndex = mainFsOptions.ACTIVITY_NAME
                                .ordinal();
                    }

                    try {
                        intent.setComponent(new ComponentName(args
                                .getString(packageNameIndex), args
                                .getString(activityNameIndex)));

                        if (windowType.equalsIgnoreCase("splitstyle")) {
                            SMultiWindowActivity.makeMultiWindowIntent(intent,
                                    zoneInfo);
                        } else {

                            scaleInfo = ((float) args
                                    .getDouble(mainFsOptions.SCALE_INFO
                                            .ordinal())) / 100;
                            if(scaleInfo < 0.6 || scaleInfo > 1.0) {
                                callbackContext.error("INVALID_SCALEINFO");
                                return;
                            }
                            SMultiWindowActivity.makeMultiWindowIntent(intent,
                                    scaleInfo);
                        }
                    } catch (Exception e) { // May be JSONException

                        callbackContext.error(e.getMessage());
                    }
                    try {
                        cordova.getActivity().startActivity(intent);
                    } catch (ActivityNotFoundException activityNotFound) {
                        callbackContext.error("ACTIVITY_NOT_FOUND");
                    }

                    callbackContext.sendPluginResult(new PluginResult(
                            PluginResult.Status.OK, 0));
                }
            });
            return true;

        } else if (action.equals("action_view")) {

        	if (Log.isLoggable(MULTIWINDOW, Log.DEBUG)) {
                Log.d(TAG, "Action is action_view");
            }
            if (!windowSupport) {
                return false;
            }

            cordova.getThreadPool().execute(new Runnable() {

                public void run() {

                    int dataUriIndex = 0;
                    float scaleInfo = 0;
                    Intent dataUrl = null;
                    String windowType = getWindowType();
                    int zoneInfo = getZoneInfo();

                    if (windowType.equalsIgnoreCase("splitstyle")) {
                        dataUriIndex = viewSsOptions.DATA_URI.ordinal();

                    } else {
                        dataUriIndex = viewFsOptions.DATA_URI.ordinal();
                    }
                    String dataUri = null;
                    try {
                        dataUri = args.getString(dataUriIndex);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(dataUri == null || dataUri.equals("") || dataUri.equals("null")) {
                        callbackContext.error("INVALID_DATA_URI");
                        return;
                    } else {
                        boolean isUriProper = false;
                        for(String schema: URI_SCHEMA_SUPPORTED) {
                            if(dataUri.startsWith(schema)) {
                                isUriProper = true;
                                break;
                            }
                        }
                        if(!isUriProper) {
                            callbackContext.error("INVALID_DATA_URI");
                            return;
                        }
                    }
                    try {
                        dataUrl = new Intent(Intent.ACTION_VIEW, Uri.parse(dataUri));

                        if (windowType.equalsIgnoreCase("splitstyle")) {
                            SMultiWindowActivity.makeMultiWindowIntent(dataUrl,
                                    zoneInfo);
                        } else {
                            scaleInfo = ((float) args
                                    .getDouble(viewFsOptions.SCALE_INFO
                                            .ordinal())) / 100;
                            if(scaleInfo < 0.6 || scaleInfo > 1.0) {
                                callbackContext.error("INVALID_SCALEINFO");
                                return;
                            }
                            SMultiWindowActivity.makeMultiWindowIntent(dataUrl,
                                    scaleInfo);
                        }
                    } catch (Exception e) { // May be JSONException

                        callbackContext.error(e.getMessage());
                    }

                    try {

                        if (dataUrl != null) {
                            cordova.getActivity().startActivity(dataUrl);
                        }

                    } catch (ActivityNotFoundException activityNotFound) {
                        callbackContext.error("ACTIVITY_NOT_FOUND");
                    }
                    callbackContext.sendPluginResult(new PluginResult(
                            PluginResult.Status.OK, 0));
                }
            });
            return true;

        } else if (action.equals("action_check")) {
        	
        	if (Log.isLoggable(MULTIWINDOW, Log.DEBUG)) {
                Log.d(TAG, "Action is action_check");
            }

            if (windowSupport) {

                callbackContext.success();
            }
            return true;

        } else if (action.equals("action_getapps")) {
        	
        	if (Log.isLoggable(MULTIWINDOW, Log.DEBUG)) {
                Log.d(TAG, "Action is action_getapps");
            }

            if (!windowSupport) {

                return false;
            }

            getMultiWindowApps(windowType, callbackContext);
            return true;

        }

        callbackContext.sendPluginResult(new PluginResult(
                PluginResult.Status.ERROR, 0));
        return false;
    }

    /**
     * Checks if the device supports Multiwindow feature of specified window
     * type.
     * 
     * @param windowType
     *            The window type freestyle or splitstyle.
     * @param callbackContext
     *            The callback id used when calling back into JavaScript.
     * @return true if window type is supported or false if windowType is not supported.
     * 
     */
    private boolean isMultiWindowSupported(final String windowType) {

    	
    	if (Log.isLoggable(MULTIWINDOW, Log.DEBUG)) {
            Log.d(TAG, "Inside isMultiWindowSupported");
        }
    	
        if (windowType.equalsIgnoreCase("freestyle")
                && mMultiWindow.isFeatureEnabled(SMultiWindow.FREE_STYLE)) {
            return true;
        } else if (windowType.equalsIgnoreCase("splitstyle")
                && mMultiWindow.isFeatureEnabled(SMultiWindow.MULTIWINDOW)) {
            return true;
        }

        return false;
    }

    /**
     * Get the MultiWindow enabled applications and activity names
     * 
     * @param windowType
     *            The window type freestyle or splitstyle.
     * @param callbackContext
     *            The callback id used when calling back into JavaScript.
     * 
     */
    private void getMultiWindowApps(final String windowType,
            final CallbackContext callbackContext) throws JSONException {

    	
    	if (Log.isLoggable(MULTIWINDOW, Log.DEBUG)) {
            Log.d(TAG, "Inside getMultiWindowApps");
        }
        cordova.getThreadPool().execute(new Runnable() {

            public void run() {

                JSONArray multiWindowApps = new JSONArray();
                Intent intent = new Intent(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_LAUNCHER);
                List<ResolveInfo> resolveInfos = cordova
                        .getActivity()
                        .getPackageManager()
                        .queryIntentActivities(
                                intent,
                                PackageManager.GET_RESOLVED_FILTER
                                        | PackageManager.GET_META_DATA);

                try {
                    // Get the multiwindow enabled applications

                    int index = 0;
                    for (ResolveInfo r : resolveInfos) {
                        if (r.activityInfo != null
                                && r.activityInfo.applicationInfo.metaData != null) {
                            if (r.activityInfo.applicationInfo.metaData
                                    .getBoolean("com.sec.android.support.multiwindow")
                                    || r.activityInfo.applicationInfo.metaData
                                            .getBoolean("com.samsung.android.sdk.multiwindow.enable")) {
                                JSONObject appInfo = new JSONObject();
                                boolean bUnSupportedMultiWinodw = false;
                                if (windowType.equalsIgnoreCase("splitstyle")) {
                                    if (r.activityInfo.metaData != null) {
                                        String activityWindowStyle = r.activityInfo.metaData
                                                .getString("com.sec.android.multiwindow.activity.STYLE");
                                        if (activityWindowStyle != null) {
                                            ArrayList<String> activityWindowStyles = new ArrayList<String>(
                                                    Arrays.asList(activityWindowStyle
                                                            .split("\\|")));
                                            if (!activityWindowStyles.isEmpty()) {
                                                if (activityWindowStyles
                                                        .contains("fullscreenOnly")) {
                                                    bUnSupportedMultiWinodw = true;
                                                }
                                            }
                                        }
                                    }
                                }

                                if (!bUnSupportedMultiWinodw
                                        || !windowType
                                                .equalsIgnoreCase("splitstyle")) {
                                    appInfo.put(
                                            "packageName",
                                            r.activityInfo.applicationInfo.packageName);
                                    appInfo.put("activity", r.activityInfo.name);
                                    multiWindowApps.put(index++, appInfo);
                                }
                            }
                        }
                    }
                    callbackContext.success(multiWindowApps);
                } catch (Exception e) {

                    callbackContext.error(e.getMessage());
                }

            }
        });
    }

}
