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

import java.io.IOException;

import org.apache.cordova.PluginResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.pen.Spen;
import com.samsung.android.sdk.pen.SpenSettingPenInfo;
import com.samsung.android.sdk.pen.document.SpenNoteDoc;
import com.samsung.android.sdk.pen.engine.SpenSurfaceView;
import com.samsung.android.sdk.pen.settingui.SpenSettingPenLayout;
import com.samsung.android.sdk.pen.settingui.SpenSettingSelectionLayout;
import com.samsung.spen.SpenException.SpenExceptionType;
/**
 * Class used for creating and handling popup surfaces.
 *
 */
public class SPenPopupSurface extends SpenSurface {

    private int mCurrentPageindex;
    private Dialog mDialog;
    private OrientationEventListener mOrientationListener;
    private RelativeLayout spenSurfaceViewContainer;
    private SpenSettingPenLayout mSpenSettingPenLayout;
    private SpenSettingSelectionLayout mSelectionSettingView;
    private AlertDialog mADiscardDialog;
    private Point mPageDocSizes = new Point();

    private static final String TAG = "SPenPopupSurface";

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int orientation = msg.getData().getInt("orientation");
//            handleOrientationChange(orientation);
        }
    };

    /**
     * Constructor for SPenPopupSurface
     * 
     * @param contextParams
     * @param option
     * 
     */
    public SPenPopupSurface(SpenContextParams contextParams,
            SpenTrayBarOptions option) {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside SPenPopupSurface");
        }
        mContextParams = contextParams;
        mOptions = option;
        createRelativeLayout(mContextParams.getSpenCustomDrawPlugin().cordova
                .getActivity());
    }


    /**
     * create RelativeLayout
     * 
     * @param context
     * 
     */
    
    private void createRelativeLayout(Context context) {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside initContextDetails");
        }
        if (mRelativeLayout == null) {
            mRelativeLayout = new RelativeLayout(context);
        }
        if (mRelativeLayout.getParent() != null) {
            ((ViewGroup) mRelativeLayout.getParent())
                    .removeView(mRelativeLayout);
        }
        mRelativeLayout.removeAllViews();
    }

    /**
     * create SPen surface
     * 
     * @return true if surface created otherwise false
     */
    boolean createSPenSurface() {
        Activity activity = mContextParams.getSpenCustomDrawPlugin().cordova
                .getActivity();
        boolean isSurface = true;
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside createSPenSurface");
        }
        if (mDialog == null) {
            mDialog = new Dialog(activity);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
            mDialog.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    if (mSpenSettingPenLayout != null) {
                        mSpenSettingPenLayout.setVisibility(View.GONE);
                    }
                    if (mSelectionSettingView != null) {
                        mSelectionSettingView.setVisibility(View.GONE);
                    }
                    mSpenSurfaceView.closeControl();
                }
            });
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                Log.d(TAG, "Dialog was null, creating new dialog. Dialog:"
                        + mDialog.toString());
            }
        }

        mRelativeLayout.setBackgroundColor(mOptions.getColor());
        // Adding top traybar buttons
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        int resId = activity.getResources().getIdentifier("spentraybar_top",
                "layout", activity.getPackageName());
        RelativeLayout traybar_top = (RelativeLayout) View.inflate(activity,
                resId, null);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mRelativeLayout.addView(traybar_top, lp);

        // Adding bottom traybar
        RelativeLayout traybar_bottom = null;
        if ((mOptions.getsPenFlags() & Utils.FLAG_ADD_PAGE) == Utils.FLAG_ADD_PAGE) {
            resId = activity.getResources().getIdentifier("spentraybar_bottom",
                    "layout", activity.getPackageName());
            traybar_bottom = (RelativeLayout) View.inflate(activity, resId,
                    null);
            lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mRelativeLayout.addView(traybar_bottom, lp);
        }

        final Point point = getWidthAndHeightForPageDoc();
        mPageDocSizes = point;

        try {
            Spen spenPackage = new Spen();
            boolean isStatic = false;
            resId = activity.getResources().getIdentifier("spen_static",
                    "bool", activity.getPackageName());
            try {
                if (resId != 0) {
                    isStatic = activity.getResources().getBoolean(resId);
                }
            } catch (Resources.NotFoundException re) {
                isStatic = false;
            }

            if (isStatic) {
                spenPackage.initialize(activity.getApplicationContext(), 5,
                        Spen.SPEN_STATIC_LIB_MODE);
            } else {
                spenPackage.initialize(activity.getApplicationContext());
            }

            mSpenSurfaceView = new SpenSurfaceView(
                    activity.getApplicationContext());
            lp = new RelativeLayout.LayoutParams(point.x, point.y);
            lp.addRule(RelativeLayout.BELOW, traybar_top.getId());

            if (traybar_bottom != null) {
                lp.addRule(RelativeLayout.ABOVE, traybar_bottom.getId());
            }

            spenSurfaceViewContainer = new RelativeLayout(activity);
            spenSurfaceViewContainer.addView(mSpenSurfaceView);
            mRelativeLayout.addView(spenSurfaceViewContainer, lp);
            int xValue = point.x;
            int yValue = point.y
                    - (int) (((float) 24) * activity.getResources()
                            .getDisplayMetrics().density);
            mSpenNoteDoc = new SpenNoteDoc(activity.getApplicationContext(),
                    xValue, yValue);
            mSpenPageDoc = mSpenNoteDoc.appendPage();
            mCurrentPageindex = mSpenNoteDoc.getPageIndexById(mSpenPageDoc
                    .getId());
            mSpenPageDoc.setBackgroundColor(mOptions.getColor());
            mSpenPageDoc.clearHistory();
            mSpenSurfaceView.setPageDoc(mSpenPageDoc, true);

            mPenInfo = new SpenSettingPenInfo();
            mPenInfo.size = PEN_SIZE;
            mPenInfo.color = Color.BLUE;
            mPenInfo.name = PEN_TYPE;
            if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                Log.d(TAG, "popup mPenInfo:" + mPenInfo.name);
            }
            mSpenSurfaceView.setPenSettingInfo(mPenInfo);

            LayoutParams p = new LayoutParams(point.x, point.y);

            // Adding Basic colors
            resId = activity.getResources().getIdentifier(
                    "spentraybar_basic_colors", "layout",
                    activity.getPackageName());
            LinearLayout basic_colors = (LinearLayout) View.inflate(activity,
                    resId, null);
            lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.BELOW, traybar_top.getId());
            mRelativeLayout.addView(basic_colors, lp);

            mDialog.setContentView(mRelativeLayout, p);
            setBgImage(mOptions);
            mDialog.show();
        } catch (SsdkUnsupportedException e) {
            Log.d(TAG, "failed initializing the spen package " + e.getMessage());
            e.printStackTrace();
            PluginResult pluginResult = new PluginResult(
                    PluginResult.Status.ERROR, e.getType());
            pluginResult.setKeepCallback(false);
            mContextParams.getCallbackContext().sendPluginResult(pluginResult);
            closeSurfaceControls();
            isSurface = false;
        } catch (IOException e) {
            Log.d(TAG, "cannot create new doc: " + e.getMessage());
            e.printStackTrace();
            SpenException.sendPluginResult(
                    SpenExceptionType.FAILED_CREATE_SURFACE,
                    mContextParams.getCallbackContext());
            closeSurfaceControls();
            isSurface = false;
        } catch (OutOfMemoryError e) {
            Runtime runtime = Runtime.getRuntime();
            Log.d(TAG, "Memory Usage, Memory Used: " + runtime.totalMemory()
                    + " , Max Memory: " + runtime.maxMemory());
            e.printStackTrace();
            closeSurfaceControls();
            SpenException.sendPluginResult(
                    SpenExceptionType.MAX_HEAP_MEMORY_REACHED,
                    mContextParams.getCallbackContext());
            isSurface = false;
        }

        mOrientationListener = new OrientationEventListener(
                activity.getApplicationContext()) {
            int orntation = Configuration.ORIENTATION_PORTRAIT;

            @Override
            public void onOrientationChanged(int orientation) {
                Activity activity = mContextParams.getSpenCustomDrawPlugin().cordova
                        .getActivity();
                if (orntation != activity.getResources().getConfiguration().orientation) {
                    if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                        Log.d(TAG, "onOrientationChanged() called");
                    }
                    orntation = activity.getResources().getConfiguration().orientation;
                    Message msgObj = handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putInt("orientation", orntation);
                    msgObj.setData(b);
                    handler.sendMessage(msgObj);
                }
            }
        };

        if (mOrientationListener.canDetectOrientation() == true) {
            if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                Log.d(TAG, "Can detect orientation");
            }
            mOrientationListener.enable();
        }
        return isSurface;
    }

    /**
     * Compare default Height and Width with Spen Page Doc size and return the minimum Height and Width
     * 
     * @return size Point
     * 
     */
    private Point getWidthAndHeightOnOrientation() {
        Point size = new Point();
        int defaultWidth = getDefaultWidth();
        if (mPageDocSizes.x > defaultWidth) {
            size.x = defaultWidth;
        } else {
            size.x = mPageDocSizes.x;
        }
        int defaultHeight = getDefaultHeight();
        if (mPageDocSizes.y > defaultHeight) {
            size.y = defaultHeight;
        } else {
            size.y = mPageDocSizes.y;
        }
        return size;
    }
    
    /**
     * Get Height and Width for Spen Page Doc
     * 
     * @return size of Spen Page Doc
     * 
     */
    private Point getWidthAndHeightForPageDoc() {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside getDimensionsforPageDoc");
        }
        Activity activity = mContextParams.getSpenCustomDrawPlugin().cordova
                .getActivity();
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "getDimensionsforPageDoc device dimensions are: "
                    + size.x + " " + size.y);
        }
        size = findWidthAndHeightForPageDoc(size);
        size.y = size.y
                + (int) (((float) 24) * activity.getResources()
                        .getDisplayMetrics().density);
        return size;
    }
    
    /**
     * Find Height and Width for Spen Page Doc
     * 
     * @params size
     *            Point
     * @return width and height of Page Doc
     * 
     */

    private Point findWidthAndHeightForPageDoc(Point size) {
        Activity activity = mContextParams.getSpenCustomDrawPlugin().cordova
                .getActivity();
        Context context = activity.getApplicationContext();
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "inside findWidthAndHeightForPageDoc");
        }
        int actualWidth = mOptions.getSurfacePosition().getWidth();
        int actualHeight = mOptions.getSurfacePosition().getHeight();
        boolean isWidthValid = mOptions.getSurfacePosition().isValidWidth(
                mOptions, context);
        boolean isHeightValid = mOptions.getSurfacePosition().isValidHeight(
                mOptions, context);
        int minWidth = mOptions.getMinWidth();
        int minHeight = mOptions.getMinHeight();
        int defaultWidth = SurfacePosition.getMaxWidth(context);
        int defaultHeight = SurfacePosition.getMaxHeight(context);

        ImageUriVO vo = getImageUriVO();
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "actual width and height: " + actualWidth + " "
                    + actualHeight);
            Log.d(TAG, "are width and height valid: " + isWidthValid + " "
                    + isHeightValid);
            Log.d(TAG, "min width and height: " + minWidth + " " + minHeight);
            Log.d(TAG, "default width and height: " + defaultWidth + " "
                    + defaultHeight);
            Log.d(TAG,
                    "ImageUri width and height: " + vo.getWidth() + " "
                            + vo.getHeight());
            Log.d(TAG, "ImageUri valid: " + vo.isValid());
        }
        // there are combination of 3 bits and a total of 8
        // possible cases
        // assume width is bit 2, height is bit 1, uri is bit 0
        if (isWidthValid & isHeightValid) { // covers 110 & 111
            // show surface with default width n height
            Log.d(TAG, "width and height are valid");
            size.x = actualWidth;
            size.y = actualHeight;
        } else if (isWidthValid & !isHeightValid) {
            size.x = actualWidth;
            if (vo.isValid()) { // covers 101
                Log.d(TAG, "width and image Uri are valid");
                // calculate height according to imageuri aspect ratio
                size.y = (vo.getHeight() * actualWidth) / vo.getWidth();
                if (size.y > defaultHeight) {
                    Log.d(TAG,
                            "height not satisfying constraints. defaulting..");
                    size.y = defaultHeight;
                } else if (size.y < minHeight) {
                    Log.d(TAG,
                            "height not satisfying constraints. Setting minimum height..");
                    size.y = minHeight;
                }
            } else { // covers 100
                Log.d(TAG, "only width is valid");
                // assign default height
                Log.d(TAG, "height not satisfying constraints. defaulting..");
                size.y = defaultHeight;
            }
        } else if (!isWidthValid & isHeightValid) {
            size.y = actualHeight;
            if (vo.isValid()) { // covers 011
                Log.d(TAG, "height and image uri are valid");
                // calculate width according to imageuri aspect ratio
                size.x = (vo.getWidth() * actualHeight) / vo.getHeight();
                if (size.x > defaultWidth) {
                    Log.d(TAG, "width not satisfying constraints. defaulting..");
                    size.x = defaultWidth;
                }
                if (size.x < minWidth) {
                    Log.d(TAG,
                            "width not satisfying constraints. Setting min width");
                    size.x = minWidth;
                }
            } else { // covers 010
                Log.d(TAG, "only height is valid");
                Log.d(TAG, "width not satisfying constraints. defaulting..");
                // assign default width
                size.x = defaultWidth;
            }
        } else if (vo.isValid()) { // covers 001
            Log.d(TAG, "only image uri valid");
            float deviceRatio = (float) defaultHeight / (float) defaultWidth;
            float imageRatio = (float) vo.getHeight() / (float) vo.getWidth();
            if (deviceRatio < imageRatio) {
                int height = defaultHeight < vo.getHeight()
                        ? defaultHeight
                        : vo.getHeight();
                if (height < minHeight) {
                    height = minHeight;
                }
                int width = (int) (height / imageRatio);
                if (width < minWidth) {
                    Log.d(TAG,
                            "width not satisfying constraints2. Setting min width");
                    width = minWidth;
                }
                size.y = height;
                size.x = width;
            } else {
                int width = defaultWidth < vo.getWidth() ? defaultWidth : vo
                        .getWidth();
                if (width < minWidth) {
                    width = minWidth;
                }
                int height = (int) (width * imageRatio);
                if (height < minHeight) {
                    height = minHeight;
                    Log.d(TAG,
                            "height not satisfying constraints2. Setting min height");
                }
                size.x = width;
                size.y = height;
            }
        } else { // covers 000
            Log.d(TAG, "all image uri, width & height are invalid");
            size.x = defaultWidth;
            size.y = defaultHeight;
        }
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "width and height are : " + size.x + " " + size.y);
        }
        return size;
    }

    /**
     * Calculate Default Height in portrait and landscape mode to avoid display issues
     * 
     * @return Height
     * 
     */
    private int getDefaultHeight() {
        Activity activity = mContextParams.getSpenCustomDrawPlugin().cordova
                .getActivity();
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        // restricting the max display to 90%
        // to avoid display issues
        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return (int) (Utils.PORTRAIT_Y_FACTOR * metrics.heightPixels);
        } else {
            return (int) (Utils.LANDSCAPE_Y_FACTOR * metrics.heightPixels);
        }
    }

    /**
     * Get Default Width
     * 
     * @return  width
     * 
     */
    public int getDefaultWidth() {
        Activity activity = mContextParams.getSpenCustomDrawPlugin().cordova
                .getActivity();
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        // restricting the max display to 90%
        // to avoid display issues
        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return (int) (Utils.PORTRAIT_X_FACTOR * metrics.widthPixels);
        } else {
            return (int) (Utils.LANDSCAPE_X_FACTOR * metrics.widthPixels);
        }
    }
    /**
     * Clear History of Spen Page Doc
     * 
     */
    void clearUndoRedo() {
        mSpenPageDoc.clearHistory();
    }

    /**
     * Open SPen Surface
     * 
     * @params options SpenTrayBarOptions
     * @params contextParams SpenContextParams
     */

    void openSpenSurface(SpenTrayBarOptions options,
            SpenContextParams contextParams) {
        mContextParams = contextParams;
        mOptions = options;
        setBgImage(mOptions);
        mDialog.show();
    }

    /**
     * Get Current Page Index
     * 
     *  @return mCurrentPageindex
     */
    int getCurrentPageindex() {
        return mCurrentPageindex;
    }

    /**
     * Set Current Page Index
     * 
     *  @params index
     *             mCurrentPageindex to be set
     * 
     */
    void setCurrentPageindex(int index) {
        mCurrentPageindex = index;
    }
    
    /**
     * 
     * Dismiss the dialog
     * 
     */

    void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
    /**
     * Handle Orientation
     * @params orientation
     */
    void handleOrientationChange(int orientation) {
        LayoutParams lp = null;
        if (mDialog != null) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
                mSpenSettingPenLayout.setPosition(0, 0);
                lp = new LayoutParams(getWidthAndHeightOnOrientation().x,
                        getWidthAndHeightOnOrientation().y);
                mDialog.setContentView(mRelativeLayout, lp);
                mDialog.show();
            } else {
                lp = new LayoutParams(getWidthAndHeightOnOrientation().x,
                        getWidthAndHeightOnOrientation().y);
                mDialog.setContentView(mRelativeLayout, lp);
            }
        }
        // dismissing and showing the discard dialog to
        // to align with the popup dialog
        if (mADiscardDialog != null && mADiscardDialog.isShowing()) {
            mADiscardDialog.dismiss();
            if (mDialog.isShowing()) {
                mADiscardDialog.show();
            }
        }
    }
    /**
     * remove surface and close controls
     *
     */
    public void removeSurface() {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d("TAG", "Inside removeSurface");
        }
        if (mDialog != null) {
            mDialog.dismiss();
        }
        mDialog = null;
        mSpenSettingPenLayout = null;
        mSelectionSettingView = null;
        closeSurfaceControls();
    }
    
    /**
     * Set SpenSettingLayout
     * 
     * @params penLayout SpenSettingPenLayout to be set
     */
    public void setSpenSettingPenLayout(SpenSettingPenLayout penLayout) {
        mSpenSettingPenLayout = penLayout;
    }
   
    /**
     * Set SpenSettingSelectionLayout
     * 
     * @params selectionLayout SpenSettingSelectionLayout to be set
     */
    public void SetSpenSettingSelectionLayout(
            SpenSettingSelectionLayout selectionLayout) {
        mSelectionSettingView = selectionLayout;
    }

    @Override
    void setAlertDialog(AlertDialog alertDialog) {
        mADiscardDialog = alertDialog;
    }
}