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
 * Richnotification object that is exported as module.
 */
var RichNotification = {};

// Error messages
/**
 * Vendor is not Samsung.
 */
RichNotification.VENDOR_NOT_SUPPORTED = "VENDOR_NOT_SUPPORTED";
/**
 * Feature not supported on this device.
 */
RichNotification.DEVICE_NOT_SUPPORTED = "DEVICE_NOT_SUPPORTED";
/**
 * SDK library is not installed.
 */
RichNotification.LIBRARY_NOT_INSTALLED = "LIBRARY_NOT_INSTALLED";
/**
 * SDK library update is recommended.
 */
RichNotification.LIBRARY_UPDATE_IS_RECOMMENDED = "LIBRARY_UPDATE_IS_RECOMMENDED";
/**
 * SDK library must be updated.
 */
RichNotification.LIBRARY_UPDATE_IS_REQUIRED = "LIBRARY_UPDATE_IS_REQUIRED";
/**
 * Notification manager not started yet.
 */
RichNotification.NOTIFICATION_MANAGER_NOT_STARTED = "NOTIFICATION_MANAGER_NOT_STARTED";
/**
 * UUID supplied is not valid.
 */
RichNotification.INVALID_UUID = "INVALID_UUID";
/**
 * Gear not connected to host device.
 */
RichNotification.DEVICE_NOT_CONNECTED = "DEVICE_NOT_CONNECTED";
/**
 * Permission is denied.
 */
RichNotification.PERMISSION_DENIED = "PERMISSION_DENIED";

// Notification header types
/**
 * Creates small header template.
 */
RichNotification.HEADER_TYPE_SMALL = "HEADER_TYPE_SMALL";
/**
 * Creates medium header template.
 */
RichNotification.HEADER_TYPE_MEDIUM = "HEADER_TYPE_MEDIUM";
/**
 * Creates large header template.
 */
RichNotification.HEADER_TYPE_LARGE = "HEADER_TYPE_LARGE";
/**
 * Creates full header template.
 */
RichNotification.HEADER_TYPE_FULL = "HEADER_TYPE_FULL";
/**
 * Creates a QR template which can be scanned to get the information.
 */
RichNotification.HEADER_TYPE_QR = "HEADER_TYPE_QR";

//Secondary template types
/**
 * The secondary title and body pair is not displayed.
 */
RichNotification.SECONDARY_TYPE_NONE = "SECONDARY_TYPE_NONE";
/**
 * Displays a pair of secondary title and body.
 */
RichNotification.SECONDARY_TYPE_STD = "SECONDARY_TYPE_STD";
/**
 * Displays multiple pairs of Secondary title and body.
 */
RichNotification.SECONDARY_TYPE_QR = "SECONDARY_TYPE_QR";

// Alert types cannot override the Gear settings.
/**
 * Silent Notification without any sound or vibration.
 */
RichNotification.ALERT_TYPE_SILENCE = 101;
/**
 * Notification with Sound only, no Vibration.
 */
RichNotification.ALERT_TYPE_SOUND = 102;
/**
 * Notification with both sound and vibration.
 */
RichNotification.ALERT_TYPE_SOUND_AND_VIBR = 103;
/**
 * Notification with Vibration only.
 */
RichNotification.ALERT_TYPE_VIBR = 104;

// Popup type
/**
 * Displays notification with no popup.
 */
RichNotification.POPUP_TYPE_NONE = 201;
/**
 * Displays notification with popup.
 */
RichNotification.POPUP_TYPE_NORMAL = 202;

// Action types
/**
 * Place a call on the wearable.
 */
RichNotification.ACTION_TYPE_CALL = 301;
/**
 * Send SMS from the Gear.
 */
RichNotification.ACTION_TYPE_SMS = 302;
/**
 * Initiate an email from the Gear.
 */
RichNotification.ACTION_TYPE_EMAIL = 303;
/**
 * Initiate a view from the Gear and view it on the phone.
 */
RichNotification.ACTION_TYPE_VIEW = 304;
/**
 * Input with the keyboard on the Gear.
 */
RichNotification.ACTION_TYPE_INPUT_KEYBOARD = 305;
/**
 * An input mode where the user can pick a single item from the list.
 */
RichNotification.ACTION_TYPE_INPUT_SINGLE_SELECT = 306;
/**
 * An input mode where the user can pick multiple items from the list.
 */
RichNotification.ACTION_TYPE_INPUT_MULTI_SELECT = 307;

// Keyboard Type
/**
 * Displays Normal keyboard (QWERTY Keyboard).
 */
RichNotification.KEYBOARD_NORMAL = 308;
/**
 * Displays Keyboard that has Numeric values only.
 */
RichNotification.KEYBOARD_NUMBER = 309;
/**
 * Displays Keyboard that has Graphic Symbols.
 */
RichNotification.KEYBOARD_EMOJI = 310;

// send API success return types
/**
 * An input action that does not allow user input on the wearable.
 */
RichNotification.RETURN_TYPE_NOTIFICATION_SENT = "RETURN_TYPE_NOTIFICATION_SENT";
/**
 * An input action that allows user input on the wearable.
 */
RichNotification.RETURN_TYPE_NOTIFICATION_READ = "RETURN_TYPE_NOTIFICATION_READ";
/**
 * Indicates the notification is removed from the Gear.
 */
RichNotification.RETURN_TYPE_NOTIFICATION_REMOVED = "RETURN_TYPE_NOTIFICATION_REMOVED";
/**
 * An input action that allows user input on the wearable.
 */
RichNotification.RETURN_TYPE_REMOTE_INPUT = "RETURN_TYPE_REMOTE_INPUT";

/**
 * This function checks if Rich Notification is supported on the device or not.
 * @param {function} success the callback function on success.
 * @param {function} fail the callback function on error.
 */ 
RichNotification.isSupported = function(success, fail) {
    cordova.exec(success, fail, "RichNotificationPlugin", "isSupported", []);
};

/**
 * This function checks if the Gear device is connected to the device or not.
 * @param {function} success the callback function on success.
 * @param {function} fail the callback function on error.
 */ 
RichNotification.isConnected = function(success, fail) {
    cordova.exec(success, fail, "RichNotificationPlugin", "isConnected", []);
};

/**
 * This function checks if the device is registered to the event listeners or not.
 * @param {function} success the callback function on success.
 * @param {function} fail the callback function on error.
 */
RichNotification.registerEventListeners = function(success, fail) {
    cordova.exec(success, fail, "RichNotificationPlugin", "registerEventListeners", []);
};

/**
 * This function will send a new notification or will update the notification sent.
 *
 * @param {function} success The callback function on success.
 * @param {function} fail The callback function on error.
 * @param {Object} options The parameters required for sending the notification.
 *
 */
RichNotification.send = function(options, success, fail) {
    // Handling undefined options case
    var opts = options;
    if (options == undefined) {
        console.log('notificationOptions is undefined. Creating a default.');
        opts = {};
    }

    var DEFAULT_PRIMARY_HEADER = RichNotification.HEADER_TYPE_SMALL;
    var DEFAULT_SECONDARY_HEADER = RichNotification.SECONDARY_TYPE_NONE;
    var DEFAULT_ALERT_TYPE = RichNotification.ALERT_TYPE_SOUND_AND_VIBR;
    var DEFAULT_POPUP_TYPE = RichNotification.POPUP_TYPE_NORMAL;

    args = [typeof opts.uuid === 'string' ?  opts.uuid : "",
            typeof opts.readoutTitle === 'string' ?  opts.readoutTitle : "",
            typeof opts.readout === 'string' ? opts.readout : "",
            typeof opts.notificationTitle === 'string' ? opts.notificationTitle : "",
            typeof opts.headerSizeType === undefined ? DEFAULT_PRIMARY_HEADER : opts.headerSizeType,
            typeof opts.primarySubHeader === 'string' ? opts.primarySubHeader : "",
            typeof opts.primaryBody === 'string' ? opts.primaryBody : "",
            typeof opts.primaryQRImage === 'string' ? opts.primaryQRImage : "",
            typeof opts.primaryBackgroundColor === 'string' ? opts.primaryBackgroundColor : "",
            typeof opts.primaryBackgroundImage === 'string' ? opts.primaryBackgroundImage : "",
            typeof opts.secondaryType === undefined ? DEFAULT_SECONDARY_HEADER : opts.secondaryType,
            typeof opts.secondarySubHeader === 'string' ? opts.secondarySubHeader : "",
            Object.prototype.toString.call(opts.secondaryContent) === '[object Array]' ? opts.secondaryContent : "",
            typeof opts.secondaryBackgroundColor === 'string' ? opts.secondaryBackgroundColor : "",
            typeof opts.secondaryImage === 'string' ? opts.secondaryImage : "",
            typeof opts.smallIcon1Path === 'string' ? opts.smallIcon1Path : "",
            typeof opts.smallIcon1Text === 'string' ? opts.smallIcon1Text : "",
            typeof opts.smallIcon2Path === 'string' ? opts.smallIcon2Path : "",
            typeof opts.smallIcon2Text === 'string' ? opts.smallIcon2Text : "",
            typeof opts.notificationIcon === 'string' ? opts.notificationIcon : "",
            typeof opts.alertType === 'number' ? opts.alertType : DEFAULT_ALERT_TYPE,
            typeof opts.popupType === 'number' ? opts.popupType : DEFAULT_POPUP_TYPE,
            Object.prototype.toString.call(opts.actions) === '[object Array]' ? opts.actions : null];

    cordova.exec(success, fail, "RichNotificationPlugin", "send", args);
};

/**
 * This function will delete a specific notification or will delete all the notification sent 
 * in a particular session.
 *
 * @param {function} success The callback function on success.
 * @param {function} fail The callback function on error.
 * @param {string} uuid The parameter required for dismissing the notification.
 *
 */
RichNotification.dismiss = function(uuid, success, fail) {
    cordova.exec(success, fail, "RichNotificationPlugin", "dismiss", [uuid]);
};

module.exports = RichNotification;
