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

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.pen.Spen;
import com.samsung.android.sdk.pen.SpenSettingPenInfo;
import com.samsung.android.sdk.pen.document.SpenNoteDoc;
import com.samsung.android.sdk.pen.engine.SpenSurfaceView;
import com.samsung.android.sdk.pen.settingui.SpenSettingPenLayout;
import com.samsung.android.sdk.pen.settingui.SpenSettingSelectionLayout;
import com.samsung.spen.SpenException.SpenExceptionType;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Class used for creating and handling inline surfaces.
 *
 */
class SPenInlineSurface extends SpenSurface {
    private static final String TAG = "SPenInlineSurface";
    private RelativeLayout spenSurfaceViewContainer;

    /**
     * Constructor for SPenInlineSurface
     * 
     * @param contextParams
     * @param options
     * 
     */
    public SPenInlineSurface(SpenContextParams contextParams,
            SpenTrayBarOptions options) {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside SPenInlineSurface Constructor");
        }
        mContextParams = contextParams;
        mOptions = options;
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
            Log.d(TAG, "Inside createRelativeLayout, creating Relative Layout");
        }
        if (mRelativeLayout == null) {
            mRelativeLayout = new RelativeLayout(context);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    mOptions.getSurfacePosition().getWidth(), mOptions
                            .getSurfacePosition().getHeight());
            mRelativeLayout.setLayoutParams(lp);
        }
        if (mRelativeLayout.getParent() != null) {
            ((ViewGroup) mRelativeLayout.getParent())
                    .removeView(mRelativeLayout);
        }
        mRelativeLayout.removeAllViews();
    }

    /**
     * create SPen surface
     */
    boolean createSPenSurface() {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside createSPenSurface, creating SpenSurface");
        }
        Activity activity = mContextParams.getSpenCustomDrawPlugin().cordova
                .getActivity();
        boolean isSurface = false;
        SurfacePosition surfacePosition = mOptions.getSurfacePosition();

        mRelativeLayout.setBackgroundColor(mOptions.getColor());
        // Adding top traybar
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        int resId = activity.getResources().getIdentifier("spentraybar_top",
                "layout", activity.getPackageName());
        RelativeLayout traybar_top = (RelativeLayout) View.inflate(activity,
                resId, null);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mRelativeLayout.addView(traybar_top, lp);

        Spen spenPackage = new Spen();

        boolean isStatic = false;
        resId = activity.getResources().getIdentifier("spen_static", "bool",
                activity.getPackageName());
        try {
            if (resId != 0) {
                isStatic = activity.getResources().getBoolean(resId);
            }
        } catch (Resources.NotFoundException re) {
            isStatic = false;
        }

        try {
            if (isStatic) {
                if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                    Log.d(TAG, "Initializing spen Statically");
                }
                spenPackage.initialize(activity.getApplicationContext(), 5,
                        Spen.SPEN_STATIC_LIB_MODE);
            } else {
                if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                    Log.d(TAG, "Initializing spen Dynamically");
                }
                spenPackage.initialize(activity.getApplicationContext());
            }

            mSpenSurfaceView = new SpenSurfaceView(
                    activity.getApplicationContext());

            // create layout for the surface
            lp = new RelativeLayout.LayoutParams(surfacePosition.getWidth(),
                    surfacePosition.getHeight());
            lp.addRule(RelativeLayout.BELOW, traybar_top.getId());
            ViewGroup viewGroup = (ViewGroup) mContextParams
                    .getSpenCustomDrawPlugin().webView;
            if (viewGroup != null) {
                spenSurfaceViewContainer = new RelativeLayout(activity);
                spenSurfaceViewContainer.addView(mSpenSurfaceView);
                mRelativeLayout.addView(spenSurfaceViewContainer, lp);

                // Adding Basic colors
                resId = activity.getResources().getIdentifier(
                        "spentraybar_basic_colors", "layout",
                        activity.getPackageName());
                LinearLayout basic_colors = (LinearLayout) View.inflate(
                        activity, resId, null);
                lp = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.BELOW, traybar_top.getId());
                mRelativeLayout.addView(basic_colors, lp);

                mRelativeLayout.setTranslationX(surfacePosition.getxValue());
                mRelativeLayout.setTranslationY(surfacePosition.getyValue());
                viewGroup.addView(mRelativeLayout);

                int widthValue = surfacePosition.getWidth();
                int heightValue = surfacePosition.getHeight()
                        - (int) (((float) 24) * activity.getResources()
                                .getDisplayMetrics().density);
                mSpenNoteDoc = new SpenNoteDoc(
                        activity.getApplicationContext(), widthValue,
                        heightValue);
                mSpenPageDoc = mSpenNoteDoc.appendPage();
                mSpenPageDoc.setBackgroundColor(mOptions.getColor());
                mSpenPageDoc.clearHistory();
                mSpenSurfaceView.setPageDoc(mSpenPageDoc, true);

                // set Pen Setting info
                mPenInfo = new SpenSettingPenInfo();
                mPenInfo.size = PEN_SIZE;
                mPenInfo.color = Color.BLUE;
                mPenInfo.name = PEN_TYPE;
                Log.d(TAG, "mPenInfo:" + mPenInfo.name);
                mSpenSurfaceView.setPenSettingInfo(mPenInfo);
                setBgImage(mOptions);
                isSurface = true;
            }
        } catch (SsdkUnsupportedException e) {
            Log.d(TAG, "failed initializing the spen package " + e.getMessage());
            e.printStackTrace();
            PluginResult pluginResult = new PluginResult(
                    PluginResult.Status.ERROR, e.getType());
            pluginResult.setKeepCallback(false);
            mContextParams.getCallbackContext().sendPluginResult(pluginResult);
            closeSurfaceControls();
        } catch (IOException e) {
            Log.d(TAG, "cannot create new doc: " + e.getMessage());
            e.printStackTrace();
            SpenException.sendPluginResult(
                    SpenExceptionType.FAILED_CREATE_SURFACE,
                    mContextParams.getCallbackContext());
            closeSurfaceControls();
        } catch (OutOfMemoryError e) {
            Runtime runtime = Runtime.getRuntime();
            Log.d(TAG, "Memory Usage, Memory Used: " + runtime.totalMemory()
                    + " , Max Memory: " + runtime.maxMemory());
            e.printStackTrace();
            closeSurfaceControls();
            SpenException.sendPluginResult(
                    SpenExceptionType.MAX_HEAP_MEMORY_REACHED,
                    mContextParams.getCallbackContext());
        }
        return isSurface;
    }

    /**
     * remove SPen surface
     */
    public void removeSurface() {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d("TAG", "Inside removeSurface, removing surface");
        }
        if (mRelativeLayout != null) {
            ViewGroup parentView = (ViewGroup) mRelativeLayout.getParent();
            if (parentView != null)
                parentView.removeView(mRelativeLayout);
            mContextParams.getSpenCustomDrawPlugin().deleteSpenSurface(
                    mOptions.getId());
        }
        closeSurfaceControls();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.samsung.spen.SpenSurface#openSpenSurface()
     */
    @Override
    void openSpenSurface(SpenTrayBarOptions options,
            SpenContextParams contextParams) {
        Activity activity = mContextParams.getSpenCustomDrawPlugin().cordova
                .getActivity();
        mOptions = options;
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d("TAG", "Inside openSpenSurface");
        }
        if (mRelativeLayout != null) {
            ViewGroup parentView = (ViewGroup) mRelativeLayout.getParent();
            if (parentView == null) {
                FrameLayout frameLayout = (FrameLayout) activity
                        .findViewById(android.R.id.content);
                if (frameLayout != null) {
                    ViewGroup viewGroup = (ViewGroup) ((ViewGroup) frameLayout
                            .getChildAt(0)).getChildAt(0);
                    viewGroup.addView(mRelativeLayout);
                    setBgImage(mOptions);
                }
            } else {
            }
        }
    }

    /**
     * Close Control  on scrolling
     */
    public void onScroll() {
        if (mSpenSurfaceView != null) {
            mSpenSurfaceView.closeControl();
        }
    }

    public void setSpenSettingPenLayout(SpenSettingPenLayout penLayout) {
        // Do nothing
    }

    public void SetSpenSettingSelectionLayout(
            SpenSettingSelectionLayout selectionLayout) {
        // Do nothing
    }

    @Override
    void setAlertDialog(AlertDialog alertDialog) {
        // DO Nothing
    }
}