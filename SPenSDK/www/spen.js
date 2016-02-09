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

/**
 * Spen Object that is exported as module
 */
var spen = {};
/**
 * Returns byte array data after saving the surface.
 */
spen.RETURN_TYPE_IMAGE_DATA = 100;
/**
 * Returns Uri of the image after saving the surface.
 */
spen.RETURN_TYPE_IMAGE_URI = 101;
/**
 * Converts the surface content to text and returns it.
 */
spen.RETURN_TYPE_RECOGNIZED_TEXT = 102;

/**
 * Displays the background image in the center.
 */
spen.BACKGROUND_IMAGE_MODE_CENTER = 200;
/**
 * Displays the background image by fitting to the screen.
 */
spen.BACKGROUND_IMAGE_MODE_FIT = 201;
/**
 * Displays the background image by stretching (scaling) to surface’s width and height.
 */
spen.BACKGROUND_IMAGE_MODE_STRETCH = 202;
/**
 * Displays the background image as tiles.
 */
spen.BACKGROUND_IMAGE_MODE_TILE = 203;

/**
 * Displays the Uri image in the center.
 */
spen.IMAGE_URI_MODE_CENTER = 300;
/**
 * Displays the Uri image by fitting it to the screen.
 */
spen.IMAGE_URI_MODE_FIT = 301;
/**
 * Displays the Uri image by stretching (scaling) to surface's width and height.
 */
spen.IMAGE_URI_MODE_STRETCH = 302;
/**
 * Displays the Uri image as tiles.
 */
spen.IMAGE_URI_MODE_TILE = 303;

/**
 * Add this flag to enable pen settings option.
 */
spen.FLAG_PEN_SETTINGS = 1 << 0;
/**
 * Add this flag to get an option to choose the background image.
 */
spen.FLAG_BACKGROUND = 1 << 1;
/**
 * Add this flag to enable selection option.
 */
spen.FLAG_SELECTION = 1 << 2;
/**
 * Add this flag to enable recognize shape option.
 */
spen.FLAG_RECOGNIZE_SHAPE = 1 << 3;
/**
 * Add this flag to enable recognize text option.
 */
spen.FLAG_RECOGNIZE_TEXT = 1 << 4;
/**
 * Add this flag to edit the surface after saving.
 */
spen.FLAG_EDIT = 1 << 5;
/**
 * Add this flag to enable pen option.
 */
spen.FLAG_PEN = 1 << 6;
/**
 * Add this flag to enable eraser option.
 */
spen.FLAG_ERASER = 1 << 7;
/**
 * Add this flag to enable undo/redo option.
 */
spen.FLAG_UNDO_REDO = 1 << 8;

/**
 * This function checks if Spen or hand is supported on the device or not.
 * @param {function} success the callback function on success.
 * @param {function} fail the callback function on error.
 */
spen.isSupported = function (success, fail) {
    cordova.exec(success, fail, "SpenPlugin", "isSupported", []);
};

/**
 * This function will create or open the Spen surface (if already created) and launches it
 * the based on the coordinates given in the inlineOptions.
 *
 *
 * @param {function} success The callback function on success.
 * @param {function} fail The callback function on error.
 * @param {Object} inlineOptions The parameters required for creating Inline surface.
 *
 */
spen.launchSurfaceInline = function (inlineOptions, success, fail) {
    if (typeof inlineOptions.id === 'undefined' ||
            inlineOptions.id === "") {
        fail("INVALID_SURFACE_ID");
    } else if (typeof inlineOptions.sPenFlags === 'undefined') {
        fail("INVALID_FLAGS");
    } else if (typeof inlineOptions.returnType === 'undefined' ||
            inlineOptions.returnType === "") {
        fail("INVALID_RETURN_TYPE");
    } else if (typeof inlineOptions.elementCoordinates === 'undefined' ||
            typeof inlineOptions.elementCoordinates.left === 'undefined' ||
            typeof inlineOptions.elementCoordinates.top === 'undefined' ||
            typeof inlineOptions.elementCoordinates.width === 'undefined' ||
            typeof inlineOptions.elementCoordinates.height === 'undefined' ||
            inlineOptions.elementCoordinates.left === "" ||
            inlineOptions.elementCoordinates.top === "" ||
            inlineOptions.elementCoordinates.width === "" ||
            inlineOptions.elementCoordinates.height === ""
            ) {
        fail("INVALID_INLINE_CORDINATES");
    } else if (typeof inlineOptions.bodyRectangleCoordinates === 'undefined' ||
            typeof inlineOptions.bodyRectangleCoordinates.left === 'undefined' ||
            typeof inlineOptions.bodyRectangleCoordinates.top === 'undefined' ||
            inlineOptions.bodyRectangleCoordinates.left === "" ||
            inlineOptions.bodyRectangleCoordinates.top === ""
            ) {
        fail("INVALID_INLINE_CORDINATES");
    } else {
        cordova.exec(success, fail, "SpenPlugin", "launchSurfaceInline", [inlineOptions.id,
            inlineOptions.sPenFlags,
            inlineOptions.returnType,
            inlineOptions.backgroundColor,
            inlineOptions.bgImageScaleType,
            inlineOptions.imageUri,
            inlineOptions.imageUriScaleType,
            inlineOptions.elementCoordinates.left,
            inlineOptions.elementCoordinates.top,
            inlineOptions.elementCoordinates.width,
            inlineOptions.elementCoordinates.height,
            inlineOptions.bodyRectangleCoordinates.left,
            inlineOptions.bodyRectangleCoordinates.top
        ]);
    }
};

/**
 * This function creates or opens the Spen surface and launches it
 * in a dialog.
 *
 * @param {function} success The callback function on success.
 * @param {function} fail The callback function on error.
 * @param {Object} popupOptions The parameters required for creating Popup surface.
 *
 */
spen.launchSurfacePopup = function (popupOptions, success, fail) {
    if (typeof popupOptions.id === 'undefined' ||
            popupOptions.id === "") {
        fail("INVALID_SURFACE_ID");
    } else if (typeof popupOptions.sPenFlags === 'undefined') {
        fail("INVALID_FLAGS");
    } else if (typeof popupOptions.returnType === 'undefined' ||
            popupOptions.returnType === "") {
        fail("INVALID_RETURN_TYPE");
    } else {
        cordova.exec(success, fail, "SpenPlugin", "launchSurfacePopup", [popupOptions.id,
            popupOptions.sPenFlags,
            popupOptions.returnType,
            popupOptions.backgroundColor,
            popupOptions.bgImageScaleType,
            popupOptions.imageUri,
            popupOptions.imageUriScaleType,
            popupOptions.width,
            popupOptions.height]);
    }
};

/**
 * This function removes the inline surface based on id.
 *
 * @param {function} success The callback function on success.
 * @param {function} fail The callback function on error.
 * @param {Object} id The Id of the Inline surface that is to be reomved.
 *
 */
spen.removeSurfaceInline = function (id, success, fail) {
    if (typeof id === 'undefined' || id === "") {
        fail("INVALID_SURFACE_ID");
    } else {
        cordova.exec(success, fail, "SpenPlugin", "removeSurfaceInline", [id]);
    }
};

/**
 * This function removes the popup surface based on id.
 *
 * @param {function} success The callback function on success.
 * @param {function} fail The callback function on error.
 * @param {Object} id The Id of the Popup surface that is to be reomved.
 *
 */
spen.removeSurfacePopup = function (id, success, fail) {
    if (typeof id === 'undefined' || id === "") {
        fail("INVALID_SURFACE_ID");
    } else {
        cordova.exec(success, fail, "SpenPlugin", "removeSurfacePopup", [id]);
    }
};

module.exports = spen;