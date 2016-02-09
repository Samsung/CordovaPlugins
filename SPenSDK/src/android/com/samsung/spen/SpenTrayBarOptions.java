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

import com.samsung.android.sdk.pen.document.SpenPageDoc;

import android.graphics.Color;
import android.util.Log;
/**
 * Public class to store the Traybar options provided from JavaScript.
 */
public class SpenTrayBarOptions {
    private String mId;
    private String mImagePath;
    private int mColor;
    private int mReturnType;
    private int mSPenFlags;
    private int mSurfaceType;
    private float mDensity;
    private int mBgImageScaleType;
    private int mImageUriScaleType;
    private SurfacePosition mSurfacePosition;
    private static final String TAG = "SpenTrayBarOptions";
    private boolean mIsSpenFeatureEnabled = false;

    /**
     * Contructor which sets the spen flags provided from the JavaScript.
     * 
     * @param sPenFlags
     *            
     */
    SpenTrayBarOptions(int sPenFlags) {
        mSPenFlags = sPenFlags;
    }

    /**
     * Get Spen Flags.
     * 
     * @return Integer having spen Flags.
     */
    public int getsPenFlags() {
        return mSPenFlags;
    }

    /**
     * Set the pixel density of the device.
     * 
     * @param density
     *            Density of the device to be set
     */
    public void setDensity(float density) {
        mDensity = density;
    }

    /**
     * Set Spen Flags.
     * 
     * @param sPenFlags
     *            sPenFlags to be set.
     */
    public void setsPenFlags(int sPenFlags) {
        mSPenFlags = sPenFlags;
    }

    /**
     * Remove Spen Flag.
     * 
     * @param flag
     *            Spen flag to be removed.
     */
    public void removeFlag(int flag) {
        mSPenFlags = mSPenFlags & (~flag);
    }

    /**
     * Add Spen Flag.
     * 
     * @param flag
     *           Spen flag to be added.
     */
    public void addFlag(int flag) {
        mSPenFlags = mSPenFlags & flag;
    }

    /**
     * Set surface type
     * 
     * @param surfaceType
     *            The mSurfaceType to be set
     */
    public void setSurfaceType(int surfaceType) {
        mSurfaceType = surfaceType;
    }

    /**
     * Get surface typre
     * 
     * @return the surfaceType
     */
    public int getSurfaceType() {
        return mSurfaceType;
    }

    /**
     * Set surafce position
     * 
     * @param surfacePosition
     *            The mSurfacePosition to be set
     */
    public void setSurfacePosition(SurfacePosition surfacePosition) {
        mSurfacePosition = surfacePosition;
    }

    /**
     * Get surafce position
     * 
     * @return surfacePosition
     */
    public SurfacePosition getSurfacePosition() {
        return mSurfacePosition;
    }

    /**
     * Set mReturnType
     * 
     * @param returnType
     *            the mReturnType to be set
     */
    public void setReturnType(int returnType) {
        mReturnType = returnType;
    }

    /**
     * @return the returnType
     */
    public int getReturnType() {
        return mReturnType;
    }

    /**
     * Get image path
     * 
     * @return the mImagePath
     */
    public String getImagePath() {
        return mImagePath;
    }

    /**
     * Set image path
     * 
     * @param imagePath
     *            Image path to be set
     */
    public void setImagePath(String imagePath) {
        mImagePath = imagePath;
    }
    
    /**
     * Set id
     * 
     * @param id
     *            The id to be set
     */
    public void setId(String id) {
        mId = id;
    }
    
    /**
     * Get id
     * 
     * @return the id
     */
    public String getId() {
        return mId;
    }

    /**
     * Check the color string and set the color. On error 0xFFFFFFFF is set as
     * default color.
     * 
     * @param color
     *            The color to be set
     */
    public void setColor(String color) {

        try {
            mColor = Color.parseColor(color);
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "Error parsing the background color."
                    + " Defaulting to White color");
            mColor = 0xFFFFFFFF;
        }
    }

    /**
     * Get color
     * 
     * @return the color
     */
    public int getColor() {
        return mColor;
    }

    /**
     * Set mIsSpenFeatureEnabled
     * 
     * @param enabled
     *            set mIsSpenFeatureEnabled true or false
     */
    public void setIsfeatureEnabled(boolean enabled) {
        mIsSpenFeatureEnabled = enabled;
    }
    /**
     * Get IsFeatureEnabled
     * 
     * @return mIsSpenFeatureEnabled
     */
    public boolean getIsFeatureEnabled() {
        return mIsSpenFeatureEnabled;
    }
    /**
     * Get scaleType
     * 
     * @return scaleType
     */
    public int getBgImageScaleType() {
        int scaleType = SpenPageDoc.BACKGROUND_IMAGE_MODE_FIT;
        if (mBgImageScaleType == Utils.BACKGROUND_IMAGE_MODE_CENTER) {
            scaleType = SpenPageDoc.BACKGROUND_IMAGE_MODE_CENTER;
        } else if (mBgImageScaleType == Utils.BACKGROUND_IMAGE_MODE_STRETCH) {
            scaleType = SpenPageDoc.BACKGROUND_IMAGE_MODE_STRETCH;
        } else if (mBgImageScaleType == Utils.BACKGROUND_IMAGE_MODE_TILE) {
            scaleType = SpenPageDoc.BACKGROUND_IMAGE_MODE_TILE;
        }
        return scaleType;
    }


    /**
     * Set mBgImageScaleType
     * 
     * @param bgImageScaleType
     *            mBgImageScaleType to be set
     */
    public void setBgImageScaleType(int bgImageScaleType) {
        this.mBgImageScaleType = bgImageScaleType;
    }

    /**
     * @return the mImageUriScaleType
     */
    public int getImageUriScaleType() {
        return mImageUriScaleType;
    }

    /**
     * @param mImageUriScaleType
     *            the mImageUriScaleType to set
     */
    public void setImageUriScaleType(int imageUriScaleType) {
        this.mImageUriScaleType = imageUriScaleType;
    }
    /**
     * return minHeight
     */
    int getMinHeight() {
        int minHeight = 24 * 2; // 24dpi and min two times the height of traybar
        return minHeight * (int) (mDensity);
    }

    /**
     * 
     * return minWidth based on SurfaceType and SPenFlags
     */
    int getMinWidth() {

        int minWidth = 28 * 3; // 28dpi and there are 3 default options in
                               // inline type
        if (mSurfaceType == Utils.SURFACE_POPUP) {
            minWidth = 28 * 7; // 28dpi and there are 7 default options in popup
                               // type
        }

        if ((getsPenFlags() & Utils.FLAG_BACKGROUND) == Utils.FLAG_BACKGROUND) {
            minWidth += 28;
        }

        if ((getsPenFlags() & Utils.FLAG_SELECTION) == Utils.FLAG_SELECTION) {
            minWidth += 28;
        }

        if ((getsPenFlags() & Utils.FLAG_PEN) == Utils.FLAG_PEN) {
            minWidth += 28;
        }

        if ((getsPenFlags() & Utils.FLAG_UNDO_REDO) == Utils.FLAG_UNDO_REDO) {
            minWidth += 56;
        }

        if ((getsPenFlags() & Utils.FLAG_ERASER) == Utils.FLAG_ERASER) {
            minWidth += 28;
        }

        return minWidth * (int) (mDensity);
    }
}
