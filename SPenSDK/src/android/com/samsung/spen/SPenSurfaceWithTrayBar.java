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

import android.content.Intent;
import android.util.Log;
/**
 * Class for handling the functionality of Both Inline and Popup surfaces.
 *
 */
class SPenSurfaceWithTrayBar {

    private SpenContextParams mContextParams;
    private SpenSurface mSpenSurface;
    private SpenTrayBar mSpenTrayBar;
    private int mSurfaceType;
    private SpenTrayBarOptions mOptions;
    private static final String TAG = "SPenSurfaceWithTrayBar";

    /**
     * 
     * Constructor of SPenSurfaceWithTrayBar
     * 
     * @param contextParams
     *            SpenContextParams
     * @param options
     *            SpenTrayBarOptions
     */
    public SPenSurfaceWithTrayBar(SpenContextParams contextParams,
            SpenTrayBarOptions options) {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside SPenSurfaceWithTrayBar Constructor");
        }
        mContextParams = contextParams;
        mSurfaceType = options.getSurfaceType();
        mOptions = options;
    }

    /**
     * Create Spen Surface With TrayBar
     * 
     * @return isSurface boolean
     * 
     */
    boolean createSPenSurfaceWithTrayBar() {
        boolean isSurface = false;
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside createSPenSurfaceWithTrayBar");
        }

        if (mSurfaceType == Utils.SURFACE_INLINE) {
            mSpenSurface = new SPenInlineSurface(mContextParams, mOptions);
        } else if (mSurfaceType == Utils.SURFACE_POPUP) {
            mSpenSurface = new SPenPopupSurface(mContextParams, mOptions);
        }

        isSurface = mSpenSurface.createSPenSurface();
        if (isSurface) {
            mSpenTrayBar = new SpenTrayBar(mContextParams, mSpenSurface,
                    mOptions);
            mSpenTrayBar.createTrayBar();
            setSettingsLayoutForPopup();
        } else {
            mSpenSurface.removeSurface();
        }
        return isSurface;
    }

    /**
     * Change Spen TrayBar Options
     * 
     */
    void changeSPenTrayBarOptions(SpenTrayBarOptions options,
            SpenContextParams contextParams) {
        mContextParams = contextParams;
        mOptions = options;
    }

    /**
     * Open Spen Surafce With Traybar
     * 
     */
    void openSPenSurfaceWithTrayBar() {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside openSPenSurfaceWithTrayBar");
        }
        if (mSpenTrayBar != null) {
            mSpenTrayBar.changeContextParams(mContextParams);
            mSpenTrayBar.changeButtonsOnSurfaceView(mOptions);
            mSpenTrayBar.changeSurfaceColor(mOptions);
        }
        if (mSpenSurface != null) {
            mSpenSurface.openSpenSurface(mOptions, mContextParams);
        }
    }

    /**
     * Set Background Image
     * 
     * @params intent Intent
     */
    void setasBgImage(Intent intent) {
        mSpenSurface.setasBgImage(intent);
    }

    /**
     * Remove Surafce and TrayBar
     * 
     */
    public void removeSurface() {
        if (mSpenTrayBar != null) {
            mSpenTrayBar.removeTrayBar();
        }
        if (mSpenSurface != null) {
            mSpenSurface.removeSurface();
        }
    }
    /**
     * Handle On Scroll
     * 
     */
    public void onScroll() {
        if (mSpenTrayBar != null && mSpenTrayBar.mButtonDone != null) {
            mSpenTrayBar.mButtonDone.callOnClick();
        }
        if (mSpenSurface != null) {
            ((SPenInlineSurface) mSpenSurface).onScroll();
        }
    }
    /**
     * Set Setting Layout for Popup
     * 
     */
    public void setSettingsLayoutForPopup() {
        if (mSurfaceType == Utils.SURFACE_POPUP) {
            mSpenSurface.setSpenSettingPenLayout(mSpenTrayBar
                    .getSpenSettingPenLayout());
            mSpenSurface.SetSpenSettingSelectionLayout(mSpenTrayBar
                    .getSpenSelectionSettingLayout());
        }
    }
}
