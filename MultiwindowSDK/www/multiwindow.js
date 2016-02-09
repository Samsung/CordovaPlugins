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
 * multiwindow object that is exported as module.
 */
var multiwindow = {};

/**
 * Starts an activity in ZONE A which is top portion of a split window.
 */
multiwindow.SPLITWINDOW_ZONE_A = 1;
/**
 * Starts an activity in ZONE B which is bottom portion of a split window.
 */
multiwindow.SPLITWINDOW_ZONE_B = 1 << 1;
/**
 * Starts an activity in ZONE FULL which is full screen.
 */
multiwindow.SPLITWINDOW_ZONE_FULL = 1 << 2;

/**
 * Checks MultiWindow is supported on the device or not.
 *
 * @param {string} windowType The window type which can be freestyle or splitstyle.
 * @param {function} successCallback The callback function on success.
 * @param {function} errorCallback The callback function on error.
 */
multiwindow.isSupported = function(windowType, successCallback,
        errorCallback) {

    var validWindow = false;

    if(typeof windowType === 'undefined') {
        errorCallback("INVALID_WINDOW_TYPE");
        return;
    }

    if(windowType === "freestyle") {
        validWindow = true;
    } else if( windowType === "splitstyle") {
        validWindow = true;
    }

    if(!validWindow) {
        errorCallback("INVALID_WINDOW_TYPE");
    } else {
        cordova.exec(successCallback, errorCallback, "MultiWindow", "action_check", [ windowType ]);
    }
};

/**
 * Gets the MultiWindow enabled packages and their activity for given window type.
 *
 * @param {string} windowType The window type which can be freestyle or splitstyle.
 * @param {function} successCallback The callback function on success.
 * @param {function} errorCallback The callback function on error.
 */
multiwindow.getMultiWindowApps = function(windowType, successCallback,
        errorCallback) {

    var validWindow = false;

    if(typeof windowType === 'undefined') {
        errorCallback("INVALID_WINDOW_TYPE");
        return;
    }

    if(windowType === "freestyle") {
        validWindow = true;
    } else if( windowType === "splitstyle") {
        validWindow = true;
    }

    if(!validWindow) {
        errorCallback("INVALID_WINDOW_TYPE");
    } else {
        cordova.exec(successCallback, errorCallback, "MultiWindow", "action_getapps", [ windowType ]);
    }
};

/**
 * Creates and launches MultiWindow
 *
 * @param {object} inputOptions Contains the MultiWindow parameters like window type, data URI or package and activity name.
 * @param {function} successCallback The callback function on success.
 * @param {function} errorCallback The callback function on error.
 */
multiwindow.createMultiWindow = function(inputOptions, successCallback,
        errorCallback) {

    var validWindow = false;

    if(typeof inputOptions.windowType === 'undefined') {
        errorCallback("INVALID_WINDOW_TYPE");
        return;
    }

    if(typeof inputOptions.action === 'undefined') {
        errorCallback("INVALID_ACTION");
        return; 
    }

    if(inputOptions.windowType === "freestyle") {
        validWindow = true;
    } else if( inputOptions.windowType === "splitstyle") {
        validWindow = true;
    }

    if(!validWindow) {
        errorCallback("INVALID_WINDOW_TYPE");
        return;
    }

    switch(inputOptions.action) {
        case 'action_main':
            if(typeof inputOptions.packageName === 'undefined' || typeof inputOptions.activity === 'undefined') {
                errorCallback("INVALID_PACKAGE_OR_ACTIVITY");
                return;
            }

            if(inputOptions.windowType === 'freestyle') {
                if(typeof inputOptions.scaleInfo === 'undefined' || isNaN(inputOptions.scaleInfo)) {
                    errorCallback("INVALID_SCALEINFO");
                    return;
                }
                cordova.exec(successCallback, errorCallback, "MultiWindow", "action_main",
                [ inputOptions.windowType,inputOptions.scaleInfo,inputOptions.packageName,inputOptions.activity ]);
            } else if(inputOptions.windowType === 'splitstyle') {
                if(typeof inputOptions.zoneInfo === 'undefined' || isNaN(inputOptions.zoneInfo)) {
                    errorCallback("INVALID_ZONEINFO");
                    return;
                }
                cordova.exec(successCallback, errorCallback, "MultiWindow", "action_main",
                [ inputOptions.windowType,inputOptions.zoneInfo,inputOptions.packageName,inputOptions.activity ]);
            }
            break;
        case 'action_view':
            if(typeof inputOptions.dataUri === 'undefined') {
                    errorCallback("INVALID_DATA_URI");
                    return;
            }

            if(inputOptions.windowType === 'freestyle') {
                if(typeof inputOptions.scaleInfo === 'undefined' || isNaN(inputOptions.scaleInfo)) {
                    errorCallback("INVALID_SCALEINFO");
                    return;
                }
                cordova.exec(successCallback, errorCallback, "MultiWindow", "action_view",
                [ inputOptions.windowType,inputOptions.scaleInfo,inputOptions.dataUri ]);
            } else if(inputOptions.windowType === 'splitstyle') {
                if(typeof inputOptions.zoneInfo === 'undefined' || isNaN(inputOptions.zoneInfo)) {
                    errorCallback("INVALID_ZONEINFO");
                    return;
                }
                cordova.exec(successCallback, errorCallback, "MultiWindow", "action_view",
                [ inputOptions.windowType,inputOptions.zoneInfo,inputOptions.dataUri ]);
            }
            break;
        default:
        errorCallback("INVALID_ACTION");
        return;
    }
};

module.exports = multiwindow;