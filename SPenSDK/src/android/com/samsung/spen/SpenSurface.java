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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RelativeLayout;
import com.samsung.android.sdk.pen.SpenSettingPenInfo;
import com.samsung.android.sdk.pen.document.SpenNoteDoc;
import com.samsung.android.sdk.pen.document.SpenPageDoc;
import com.samsung.android.sdk.pen.engine.SpenSurfaceView;
import com.samsung.android.sdk.pen.settingui.SpenSettingPenLayout;
import com.samsung.android.sdk.pen.settingui.SpenSettingSelectionLayout;
/**
 * Abstract class used by SpenInline and SpenPopup Surfaces.
 *
 */
public abstract class SpenSurface {
    static final String PEN_TYPE = "com.samsung.android.sdk.pen.pen.preload.InkPen";
    static final int PEN_SIZE = 10;
    protected RelativeLayout mRelativeLayout;
    protected SpenSettingPenInfo mPenInfo;
    protected SpenSurfaceView mSpenSurfaceView;
    protected SpenNoteDoc mSpenNoteDoc;
    protected SpenPageDoc mSpenPageDoc;
    protected SpenContextParams mContextParams;
    protected SpenTrayBarOptions mOptions;
    protected ImageUriVO mImageUriVO;
    private static final String TAG = "SpenSurface";

    abstract boolean createSPenSurface();
    
    abstract void openSpenSurface(SpenTrayBarOptions options,
            SpenContextParams contextParams);

    /**
     * Get SPen Surface View
     * 
     * @return mSpenSurfaceView SpenSurfaceView
     * 
     */
    protected SpenSurfaceView getSpenSurfaceView() {
        return mSpenSurfaceView;
    }

    /**
     * Get SPen Page Doc
     * 
     * @return mSpenPageDoc SpenPageDoc
     * 
     */
    protected SpenPageDoc getSpenPageDoc() {
        return mSpenPageDoc;
    }

    /**
     * Get SPen Setting Page Info
     * 
     * @return mPenInfo SpenSettingPenInfo
     * 
     */
    protected SpenSettingPenInfo getSpenInfo() {
        return mPenInfo;
    }

    /**
     * Get RelativeLayout
     * 
     * @return mRelativeLayout RelativeLayout
     * 
     */
    protected RelativeLayout getRelativeLayout() {
        return mRelativeLayout;
    }

    /**
     * Get SPen Note Doc
     * 
     * @return mSpenNoteDoc SpenNoteDoc
     * 
     */
    protected SpenNoteDoc getSpenNoteDoc() {
        return mSpenNoteDoc;
    }

    /**
     * 
     * Set Color Info
     * 
     * @params color int
     * 
     */
    void setColorInfo(int color) {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside setColorInfo, color is : " + color);
        }
        mPenInfo.color = color;
        mSpenSurfaceView.setPenSettingInfo(mPenInfo);
    }

    /**
     * sets the background image from the URI present in the intent
     * 
     * @param intent
     */
    void setasBgImage(Intent intent) {
        Activity activity = mContextParams.getSpenCustomDrawPlugin().cordova
                .getActivity();
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside setasBgImage");
        }
        if (intent != null) {
            Uri imageFileUri = intent.getData();
            if (imageFileUri != null) {
                Cursor cursor = activity
                        .getApplicationContext()
                        .getContentResolver()
                        .query(Uri.parse(imageFileUri.toString()), null, null,
                                null, null);
                String imagePath = null;
                if (cursor != null && cursor.moveToNext()) {
                    try {
                        imagePath = cursor.getString(cursor
                                .getColumnIndex(MediaStore.MediaColumns.DATA));
                        mSpenPageDoc.setBackgroundImage(imagePath);
                        mSpenPageDoc.setBackgroundImageMode(mOptions
                                .getBgImageScaleType());
                        mSpenSurfaceView.update();
                    } catch (IllegalArgumentException e) {
                        Log.d(TAG, "error while setting the background image: "
                                + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                        Log.d(TAG, "cursor is null or doesn't hold image");
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else {
            if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                Log.d(TAG, "Intent is null. Cannot set the background image.");
            }
        }
    }

    /**
     * Set Background Image
     * 
     * @param mOptions
     *            SpenTrayBar options
     */
    void setBgImage(SpenTrayBarOptions mOptions) {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "inside setBgImage");
        }
        String imagePath = mOptions.getImagePath();
        if (imagePath != null) {
            int scaleType = mOptions.getImageUriScaleType();
            int scale = SpenPageDoc.BACKGROUND_IMAGE_MODE_STRETCH;

            if (scaleType == Utils.IMAGE_URI_MODE_CENTER) {
                scale = SpenPageDoc.BACKGROUND_IMAGE_MODE_CENTER;
            } else if (scaleType == Utils.IMAGE_URI_MODE_FIT) {
                scale = SpenPageDoc.BACKGROUND_IMAGE_MODE_FIT;
            } else if (scaleType == Utils.IMAGE_URI_MODE_TILE) {
                scale = SpenPageDoc.BACKGROUND_IMAGE_MODE_TILE;
            }

            Activity activity = mContextParams.getSpenCustomDrawPlugin().cordova
                    .getActivity();
            Context context = activity.getApplicationContext();
            if (imagePath.startsWith("file:///")) {
                imagePath = imagePath.replaceFirst("file:///", "/");
                if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                    Log.d(TAG, "image path starts with file://");
                }
            } else if (imagePath.startsWith("content://")) {
                if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                    Log.d(TAG, "image path starts with content://");
                }
                Cursor cursor = context.getContentResolver().query(
                        Uri.parse(imagePath), null, null, null, null);
                if (cursor != null && cursor.moveToNext()) {
                    imagePath = cursor.getString(cursor
                            .getColumnIndex(MediaStore.MediaColumns.DATA));
                } else {
                    imagePath = null;
                    if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                        Log.d(TAG, "cursor is null or doesn't hold image");
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
            if (imagePath != null) {
                try {
                    mSpenPageDoc.setBackgroundImageMode(scale);
                    mSpenPageDoc.setBackgroundImage(imagePath);
                    mSpenPageDoc.clearHistory();
                    mSpenSurfaceView.update();
                } catch (IllegalArgumentException e) {
                    Log.d(TAG,
                            "error while setting the background image: "
                                    + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Inner Class ImageUriVO
     * 
     */
    public class ImageUriVO {
        private int width = 0;
        private int height = 0;
        private boolean isValid = false;
        private String imagePath = null;
        public int getWidth() {
            return width;
        }
        public int getHeight() {
            return height;
        }
        public boolean isValid() {
            return isValid;
        }
        public void setHeight(int height) {
            this.height = height;
        }
        public void setValid(boolean isValid) {
            this.isValid = isValid;
        }
        public void setWidth(int width) {
            this.width = width;
        }
        public String getImagePath() {
            return imagePath;
        }
        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }
    }


    /**
     * Get ImageUriVO
     * 
     * @return imageUriVO ImageUriVO
     * 
     */
    public ImageUriVO getImageUriVO() {
        String imagePath = mOptions.getImagePath();
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "image path is: " + imagePath);
        }
        ImageUriVO imageUriVO = new ImageUriVO();
        if (imagePath != null) {
            if (imagePath.startsWith("file:///")) {
                imagePath = imagePath.replaceFirst("file:///", "/");
                if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                    Log.d(TAG, "image path starts with file://");
                }
            } else if (imagePath.startsWith("content://")) {
                if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                    Log.d(TAG, "image path starts with content://");
                }
                Cursor cursor = mContextParams.getSpenCustomDrawPlugin().cordova
                        .getActivity().getApplicationContext()
                        .getContentResolver()
                        .query(Uri.parse(imagePath), null, null, null, null);
                if (cursor != null && cursor.moveToNext()) {
                    imagePath = cursor.getString(cursor
                            .getColumnIndex(MediaStore.MediaColumns.DATA));
                } else {
                    imagePath = null;
                    if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
                        Log.d(TAG, "cursor is null or doesn't hold image");
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imagePath, bitmapOptions);
            if (bitmapOptions.outWidth > 0 && bitmapOptions.outHeight > 0) {
                float imgWidth = 0;
                float imgHeight = 0;
                imgWidth = bitmapOptions.outWidth;
                imgHeight = bitmapOptions.outHeight;
                imageUriVO.setWidth((int) imgWidth);
                imageUriVO.setHeight((int) imgHeight);
                imageUriVO.setImagePath(imagePath);
                if (imgWidth > 0 && imgHeight > 0) {
                    imageUriVO.setValid(true);
                }
            } else {
                Log.d(TAG, "Image or ImagePath is invalid");
            }
        }
        mImageUriVO = imageUriVO;
        return imageUriVO;
    }


    /**
     * Close Surfae Controls
     * 
     */
    void closeSurfaceControls() {
        if (Log.isLoggable(Utils.SPEN, Log.DEBUG)) {
            Log.d(TAG, "Inside closeSurfaceControls");
        }
        if (mSpenSurfaceView != null) {
            mSpenSurfaceView.closeControl();
            mSpenSurfaceView.close();
            mSpenSurfaceView = null;
        }
        if (mSpenNoteDoc != null) {
            try {
                mSpenNoteDoc.close();
            } catch (IOException e) {
                Log.d(TAG,
                        "exception while close surface controls: "
                                + e.getMessage());
                e.printStackTrace();
            }
            mSpenNoteDoc = null;
        }
    }

    abstract void removeSurface();
    abstract void setSpenSettingPenLayout(SpenSettingPenLayout penLayout);
    abstract void SetSpenSettingSelectionLayout(
            SpenSettingSelectionLayout selectionLayout);
    abstract void setAlertDialog(AlertDialog alertDialog);
}
