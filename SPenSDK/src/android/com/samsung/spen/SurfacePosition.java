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

import android.content.Context;
import android.content.res.Configuration;

/**
 * Public class used to store the surface position details for inline and popup
 * surfaces.
 */
public class SurfacePosition {
    private int mWidth, mHeight, mXValue, mYValue;
    
    /**
     * Constructor for Inline surface
     * 
     * @param context
     *            Context of the application
     * @param width
     *            Width of the surface.
     * @param height
     *            Height of the surface.
     * @param x
     *            X-position of the surface.
     * @param y
     *            Y-position of the surface.
     */
    public SurfacePosition(Context context, int width, int height, int x, int y) {
        float density = context.getResources().getDisplayMetrics().density;
        mWidth = (int) (((float) width) * density);
        mHeight = (int) (((float) height) * density);
        mXValue = (int) (((float) x) * density);
        mYValue = (int) (((float) y) * density);
    }

    /**
     * Constructor for Popup surface.
     * 
     * @param context
     *            Context of the application
     * @param width
     *            Width of the surface.
     * @param height
     *            Height of the surface.
     */
    public SurfacePosition(Context context, int width, int height) {
        mWidth = width;
        mHeight = height;
        mXValue = 0;
        mYValue = 0;
    }

    /**
     * Get height of surface
     * 
     * @return height of surface
     */
    public int getHeight() {
        return mHeight;
    }

    /**
     * set height of surface
     */
    public void setHeight(int height) {
        mHeight = height;
    }

    /**
     * Get width of surface
     * 
     * @return width of surface
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * set the width of surface
     */
    public void setWidth(int width) {
        mWidth = width;
    }

    /**
     * Get xValue
     * 
     * @return the xValue
     */
    public int getxValue() {
        return mXValue;
    }

    /**
     * Get yValue
     * 
     * @return the yValue
     */
    public int getyValue() {
        return mYValue;
    }

    /**
     * Check if the parameters for surface are valid.
     * 
     * @param mOptions
     *            SpenTrayBarOptions.
     * @param context
     *            Context of the application.
     * @return true if parameters are valid, otherwise false.
     */
    public boolean isSurfaceValid(SpenTrayBarOptions mOptions, Context context) {

        if (isValidHeight(mOptions, context) && isValidWidth(mOptions, context)) {
            return true;
        }
        return false;
    }

    /**
     * Get the maximum height of the surface that can be created.
     * 
     * @param context
     *            Context of application.
     * @return Maximum possible height of the surface.
     */
    static public int getMaxHeight(Context context) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return (int) (Utils.PORTRAIT_Y_FACTOR * context.getResources()
                    .getDisplayMetrics().heightPixels);
        } else {
            return (int) (Utils.LANDSCAPE_Y_FACTOR * context.getResources()
                    .getDisplayMetrics().heightPixels);
        }
    }

    /**
     * Get the maximum width of the surface that can be created.
     * 
     * @param context
     *            Context of application.
     * @return maximum possible width of the surface.
     */
    static public int getMaxWidth(Context context) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return (int) (Utils.PORTRAIT_X_FACTOR * context.getResources()
                    .getDisplayMetrics().widthPixels);
        } else {
            return (int) (Utils.LANDSCAPE_X_FACTOR * context.getResources()
                    .getDisplayMetrics().widthPixels);
        }
    }
    /**
     * Check if the provided width of the surface is valid.
     * 
     * @param mOptions
     *            SpenTrayBarOptions.
     * @param context
     *            Application context.
     * @return true if width is valid else false.
     */
    boolean isValidWidth(SpenTrayBarOptions mOptions, Context context) {
        if (mWidth < mOptions.getMinWidth()
                || mWidth >= (long) Integer.MAX_VALUE
                || (mWidth > getMaxWidth(context))) {
            return false;
        }
        return true;
    }
    /**
     * Check if the provided height of the surface is valid.
     * 
     * @param mOptions
     *            SpenTrayBarOptions.
     * @param context
     *            Application context.
     * @return true if height is valid else false.
     */
    boolean isValidHeight(SpenTrayBarOptions mOptions, Context context) {
        if (mHeight < mOptions.getMinHeight()
                || mHeight >= (long) Integer.MAX_VALUE
                || (mHeight > getMaxHeight(context))) {
            return false;
        }
        return true;
    }
}
