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

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.samsung.android.sdk.SsdkUnsupportedException;

/**
 * public Exceptoin class used for exception handling.
 *
 */
public class SpenException extends Exception {

    private static final long serialVersionUID = 1L;

    enum SpenExceptionType {
        FAILED_CREATE_SURFACE,
        FAILED_SET_BACKGROUND_IMAGE,
        FAILED_RECOGNIZE_TEXT,
        FAILED_RECOGNIZE_SHAPE,
        FAILED_SAVING_IMAGE,
        ACTION_INVALID,
        SPEN_AND_HAND_SUPPORTED,
        ONLY_HAND_SUPPORTED,
        SPEN_NOT_SUPPORTED,
        VENDOR_NOT_SUPPORTED,
        DEVICE_NOT_SUPPORTED,
        LIBRARY_NOT_INSTALLED,
        LIBRARY_UPDATE_IS_REQUIRED,
        LIBRARY_UPDATE_IS_RECOMMENDED,
        INVALID_SURFACE_ID,
        SURFACE_ID_NOT_EXISTS,
        SURFACE_ID_ALREADY_EXISTS,
        INVALID_RETURN_TYPE,
        INVALID_SURFACE_TYPE,
        INVALID_FLAGS,
        INVALID_INLINE_CORDINATES,
        INVALID_DIMENSIONS,
        INLINE_SURFACE_LIMIT_REACHED,
        POPUP_SURFACE_LIMIT_REACHED,
        MAX_SURFACE_LIMIT_REACHED,
        MAX_HEAP_MEMORY_REACHED
    }

    /**
     * This method is used for sending the intermittent plugin results to the
     * application
     * 
     * @param type
     *            SpenExceptionType
     * @param callbackContext
     *            Context of callback
     */
    public static void sendPluginResult(SpenExceptionType type,
            CallbackContext callbackContext) {
        PluginResult pluginResult;
        switch (type) {

            case FAILED_RECOGNIZE_TEXT :
                pluginResult = new PluginResult(PluginResult.Status.ERROR,
                        SpenException.SpenExceptionType.FAILED_RECOGNIZE_TEXT
                                .toString());
                pluginResult.setKeepCallback(true);
                callbackContext.sendPluginResult(pluginResult);
                break;

            case FAILED_RECOGNIZE_SHAPE :
                pluginResult = new PluginResult(PluginResult.Status.ERROR,
                        SpenException.SpenExceptionType.FAILED_RECOGNIZE_SHAPE
                                .toString());
                pluginResult.setKeepCallback(true);
                callbackContext.sendPluginResult(pluginResult);
                break;

            case FAILED_SET_BACKGROUND_IMAGE:
                pluginResult = new PluginResult(PluginResult.Status.ERROR,
                        SpenException.SpenExceptionType.FAILED_SET_BACKGROUND_IMAGE.toString());
                pluginResult.setKeepCallback(true);
                callbackContext.sendPluginResult(pluginResult);
                break;

            case FAILED_CREATE_SURFACE :
                pluginResult = new PluginResult(PluginResult.Status.ERROR,
                        SpenException.SpenExceptionType.FAILED_CREATE_SURFACE
                                .toString());
                pluginResult.setKeepCallback(false);
                callbackContext.sendPluginResult(pluginResult);
                break;

            case SURFACE_ID_NOT_EXISTS :
                pluginResult = new PluginResult(PluginResult.Status.ERROR,
                        SpenExceptionType.SURFACE_ID_NOT_EXISTS.toString());
                pluginResult.setKeepCallback(false);
                callbackContext.sendPluginResult(pluginResult);
                break;

            case FAILED_SAVING_IMAGE :
                pluginResult = new PluginResult(PluginResult.Status.ERROR,
                        SpenExceptionType.FAILED_SAVING_IMAGE.toString());
                pluginResult.setKeepCallback(true);
                callbackContext.sendPluginResult(pluginResult);
                break;

            case INLINE_SURFACE_LIMIT_REACHED :
            case POPUP_SURFACE_LIMIT_REACHED :
            case MAX_HEAP_MEMORY_REACHED :
            case INVALID_SURFACE_ID :
            case INVALID_RETURN_TYPE :
            case INVALID_SURFACE_TYPE :
            case INVALID_FLAGS :
            case INVALID_INLINE_CORDINATES :
            case INVALID_DIMENSIONS :
            case MAX_SURFACE_LIMIT_REACHED :
            case SURFACE_ID_ALREADY_EXISTS :
                callbackContext.error(type.toString());
                break;
            default :
                break;
        }
    }

    /**
     * This method processes the exception that is caused while initializing the
     * Spen package and return the type of exception
     * 
     * @param e
     *            SsdkUnsupportedException
     * @return SpenExceptionType
     */
    public static SpenExceptionType processUnsupportedException(
            SsdkUnsupportedException e) {
        int errType = e.getType();
         
        if (errType == SsdkUnsupportedException.VENDOR_NOT_SUPPORTED) {
            // If the device is not a Samsung device
            return SpenExceptionType.VENDOR_NOT_SUPPORTED;
        } else if (errType == SsdkUnsupportedException.DEVICE_NOT_SUPPORTED) {
            //if the device does not support Pen.
            return SpenExceptionType.DEVICE_NOT_SUPPORTED;
        } else if (errType == SsdkUnsupportedException.LIBRARY_NOT_INSTALLED) {
            // If SpenSDK APK is not installed.
            return SpenExceptionType.LIBRARY_NOT_INSTALLED;
        } else if (errType == SsdkUnsupportedException.LIBRARY_UPDATE_IS_REQUIRED) {
            // SpenSDK APK must be updated.
            return SpenExceptionType.LIBRARY_UPDATE_IS_REQUIRED;
        } else if (errType == SsdkUnsupportedException.LIBRARY_UPDATE_IS_RECOMMENDED) {
            // Update of SpenSDK APK to an available new version is recommended.
            return SpenExceptionType.LIBRARY_UPDATE_IS_RECOMMENDED;
        }
        return SpenExceptionType.SPEN_NOT_SUPPORTED;
    }
}
