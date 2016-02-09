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

package com.samsung.richnotification;


import java.util.UUID;
import java.util.List;
import java.lang.IllegalArgumentException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.util.Log;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.richnotification.Srn;
import com.samsung.android.sdk.richnotification.SrnRichNotificationManager;
import com.samsung.android.sdk.richnotification.SrnRichNotificationManager.ErrorType;
import com.samsung.android.sdk.richnotification.SrnRichNotificationManager.EventListener;
import com.samsung.android.sdk.richnotification.SrnImageAsset;
import com.samsung.android.sdk.richnotification.SrnRichNotification;
import com.samsung.android.sdk.richnotification.templates.SrnPrimaryTemplate;
import com.samsung.android.sdk.richnotification.templates.SrnSecondaryTemplate;
import com.samsung.android.sdk.richnotification.SrnAction;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is responsible for sending rich Notification to the Gear.
 */
public class RichNotification extends CordovaPlugin implements EventListener {

    private SrnRichNotificationManager mRichNotificationManager;
    private Context mContext;
    private boolean mIsRichSupported = false;
    private CallbackContext mCallbackContext = null;
    private boolean pluginMetadata = false;

    private static final String META_DATA = "com.samsung.cordova.richnotification";
    private static final String TAG = "RichNotification";
    private static final String RICHNOTI = "RichNotiPlugin";

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        String mPackageName = cordova.getActivity().getPackageName();
        PackageManager pm = cordova.getActivity().getPackageManager();
        try {
            ApplicationInfo ai = pm.getApplicationInfo(mPackageName, PackageManager.GET_META_DATA);
            if (ai.metaData != null) {
                pluginMetadata = ai.metaData.getBoolean(META_DATA);
            }
        } catch (NameNotFoundException e) {
        }
        IntentFilter filter = new IntentFilter("com.samsung.cordova.richnotification.remote_input_receiver");
        if(richRemoteInputReceiver != null) {
            cordova.getActivity().getApplicationContext().registerReceiver(richRemoteInputReceiver, filter);
        }
    }

	/**
     * This method will initialize RichNotification on the device.
     *
     */
    private boolean initRichNotification (CallbackContext callbackContext) {
    	if(Log.isLoggable(RICHNOTI, Log.DEBUG))
        	Log.d(TAG, "Initializing RichNotification...");

        // This function takes care of error callbacks
        if (!isRichNotificationSupported(callbackContext)) {
            return false;
        }
        else if (mRichNotificationManager == null) {
            mRichNotificationManager = new SrnRichNotificationManager(mContext);
            mRichNotificationManager.start();
            mRichNotificationManager.registerRichNotificationListener(this);
        }

        boolean isConnected = mRichNotificationManager.isConnected();
        if (!isConnected) {
            Log.e(TAG, "Cannot send RichNotification. Device not connected.");
            callbackContext.error(RichNotificationHelper.DEVICE_NOT_CONNECTED);
            return false;
        }
        else {
        	if(Log.isLoggable(RICHNOTI, Log.DEBUG))
            	Log.d(TAG, "Initialization complete. Device is connected.");
        }

        return true;
    }

    /**
     * Checks if the device supports RichNotification feature.
     *
     * @param callbackContext
     *            The callback id used when calling back into JavaScript.
     * @return true if the RichNotification is supported or false if not supported.
     *
     */
    private boolean isRichNotificationSupported (CallbackContext callbackContext) {
        if (mIsRichSupported)
            return true;

        if(Log.isLoggable(RICHNOTI, Log.DEBUG))
        	Log.d(TAG, "Checking RichNotification support...");
        Srn srn = new Srn();
        try {
            // Initialize an instance of Srn.
            srn.initialize(mContext);
        } catch (SsdkUnsupportedException e) {
            // Error handling
            if (e.getType() == SsdkUnsupportedException.VENDOR_NOT_SUPPORTED) {
                Log.e(TAG, "Initialization error. Vendor is not Samsung.");
                callbackContext.error(RichNotificationHelper.VENDOR_NOT_SUPPORTED);
            }
            else if (e.getType() == SsdkUnsupportedException.DEVICE_NOT_SUPPORTED) {
                Log.e(TAG, "Initialization error. Device not supported.");
                callbackContext.error(RichNotificationHelper.DEVICE_NOT_SUPPORTED);
            }
            else if (e.getType() == SsdkUnsupportedException.LIBRARY_NOT_INSTALLED) {
                Log.e(TAG, "Initialization error. Device not supported.");
                callbackContext.error(RichNotificationHelper.LIBRARY_NOT_INSTALLED);
            }
            else if (e.getType() == SsdkUnsupportedException.LIBRARY_UPDATE_IS_RECOMMENDED) {
                Log.e(TAG, "Initialization error. Device not supported.");
                callbackContext.error(RichNotificationHelper.LIBRARY_UPDATE_IS_RECOMMENDED);
            }
            else if (e.getType() == SsdkUnsupportedException.LIBRARY_UPDATE_IS_REQUIRED) {
                Log.e(TAG, "Initialization error. Device not supported.");
                callbackContext.error(RichNotificationHelper.LIBRARY_UPDATE_IS_REQUIRED);
            }
            else {
                Log.e(TAG, "Initialization error.");
                callbackContext.error("Initialization error");
            }
            return false;
        }
        mIsRichSupported = true;
        return true;
    }

    /**
     * Checks if the device is registered to the event listeners.
     *
     * @param callbackContext
     *            The callback id used when calling back into JavaScript.
     * @return true if the registration was successful or false if the registration was unsuccessful.
     *
     */
    private boolean registerRichNotificationEventListeners(CallbackContext callbackContext) {
        // This function takes care of error callbacks
        if (!isRichNotificationSupported(callbackContext)) {
            return false;
        }
        mCallbackContext = callbackContext;
        return true;
    }

    /**
     * This function will send a new notification or will update the notification sent.
     *
     * @param data
	 *            The JSONArray with parameters required for sending the notification.
     * @param callbackContext
     *            The callback id used when calling back into JavaScript.
     * @return true if the notification is sent or false if the notification is not sent.
     *
     */
    private boolean sendRichNotification(JSONArray data, CallbackContext callbackContext)
                    throws JSONException {
        RichNotificationOptions options = null;
        SrnPrimaryTemplate primaryTemplate = null;
        SrnSecondaryTemplate secondaryTemplate = null;

        // This function takes care of error callbacks
        if(!initRichNotification(callbackContext)){
            Log.e(TAG, "Initialization failed.");
            return false;
        }

        if(Log.isLoggable(RICHNOTI, Log.DEBUG))
        	Log.d(TAG, "Creating the notification.");

        // Fetch options from the data object
        options = new RichNotificationOptions(data);
        options.printLogs();

        SrnRichNotification noti = null;
        UUID uuid = null;

        try {
            uuid = UUID.fromString(options.uuid);
        } catch(IllegalArgumentException invalidUUID) {
            Log.e(TAG, "Invalid UUID supplied. Creating a new notification.");
            options.uuid = null;
        }

        if (options.uuid == null)
            noti = new SrnRichNotification(mContext);
        else
            noti = new SrnRichNotification(mContext, uuid);

        // Set notification icon
        if (!options.notificationIcon.isEmpty()) {
            Bitmap notiIconBit = RichNotificationHelper.getIconBitmap(mContext, "file://"
                                                                        + options.notificationIcon);
            SrnImageAsset notiIconAsst = new SrnImageAsset(mContext, "notiIcon", notiIconBit);
            noti.setIcon(notiIconAsst);
        }

        noti.setAlertType(options.alertType, options.popupType);

        noti.setReadout(options.readoutTitle, options.readout);
        noti.setTitle(options.notificationTitle);

        primaryTemplate = RichNotificationHelper.createPrimaryTemplate(mContext, options);
        secondaryTemplate = RichNotificationHelper.createSecondaryTemplate(mContext, options);

        noti.setPrimaryTemplate(primaryTemplate);

        if (secondaryTemplate != null)
            noti.setSecondaryTemplate(secondaryTemplate);

        // Action related
        List<SrnAction> actionList = RichNotificationHelper.createActions(mContext,
                                                                          callbackContext,
                                                                          options);
        if (actionList != null) {
            noti.addActions(actionList);
        }
        else {
        	if(Log.isLoggable(RICHNOTI, Log.DEBUG))
            	Log.d(TAG, "Actions not defined");
        }

        try {
            options.uuid = mRichNotificationManager.notify(noti).toString();

            JSONObject successMsg = new JSONObject();
            successMsg.put("returnType", RichNotificationHelper.RETURN_TYPE_NOTIFICATION_SENT);
            successMsg.put("returnValue", options.uuid);

            if(Log.isLoggable(RICHNOTI, Log.DEBUG))
            	Log.d(TAG, "Notification sent. UUID: " + options.uuid);
            PluginResult sentResult = new PluginResult(PluginResult.Status.OK, successMsg);
            sentResult.setKeepCallback(true);
            callbackContext.sendPluginResult(sentResult);
            return true;
        } catch(SecurityException secEx) {
            Log.e(TAG, "Permission denied");
            callbackContext.error(RichNotificationHelper.PERMISSION_DENIED);
            return false;
        }
    }

    /**
     * This function will delete a specific notification or will delete all the notification sent 
     * in a particular session.
     *
     * @param uuid
	 *            The string parameter required for dismissing the notification.
     * @param callbackContext
     *            The callback id used when calling back into JavaScript.
     * @return true if the notification is dismissed or falses if the notification is not dismissed.
     *
     */
    private boolean dismissRichNotification(String uuid, CallbackContext callbackContext) {
        try {
            if (!isRichNotificationSupported(callbackContext)) {
                return false;
            }
            else if (mRichNotificationManager == null) {
                callbackContext.error(RichNotificationHelper.NOTIFICATION_MANAGER_NOT_STARTED);
                return false;
            }
            else if (!mRichNotificationManager.isConnected()) {
                callbackContext.error(RichNotificationHelper.DEVICE_NOT_CONNECTED);
                return false;
            }
            else if (uuid == null || uuid.isEmpty()) {
                callbackContext.error(RichNotificationHelper.INVALID_UUID);
                return false;
            }
            else if (uuid.equalsIgnoreCase("all")) {
                mRichNotificationManager.dismissAll();
            }
            else {
                UUID dismissID = UUID.fromString(uuid);
                mRichNotificationManager.dismiss(dismissID);
            }
        } catch(IllegalArgumentException invalidUUID) {
            Log.e(TAG, "Invalid UUID: " + uuid);
            callbackContext.error(RichNotificationHelper.INVALID_UUID);
            return false;
        }

        callbackContext.success();
        return true;
    }

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action
     *            The action to execute.
     * @param data
     *            JSONArray of arguments for the plugin.
     * @param callbackContext
     *            The callback id used when calling back into JavaScript.
     * @return A PluginResult object with a status and message.
     */
    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext)
                    throws JSONException {

    	if(Log.isLoggable(RICHNOTI, Log.DEBUG))
        	Log.d(TAG, "execute(): callbackID - " + callbackContext.getCallbackId());
        mContext = this.cordova.getActivity();

        // Do not allow apis if metadata is missing
        if (!pluginMetadata) {
            callbackContext.error("METADATA_MISSING");
            Log.e(TAG, "Metadata is missing");
            return false;
        }
        
        if (action.equals("isSupported")) {
            if(isRichNotificationSupported(callbackContext)){
                callbackContext.success();
                return true;
            }
            return false;
        }

        else if (action.equals("registerEventListeners")) {
            return registerRichNotificationEventListeners(callbackContext);
        }

        else if (action.equals("send")) {
            return sendRichNotification(data, callbackContext);
        }

        else if (action.equals("dismiss")) {
            String uuid = data.getString(0); // There will be just one array-element i.e. the UUID
            return dismissRichNotification(uuid, callbackContext);
        }

        else if (action.equals("isConnected")) {
            // initRichNotification will throw the DEVICE_NOT_CONNECTED error
            if (!initRichNotification(callbackContext)) {
                Log.e(TAG, "Initialization failed.");
                return false;
            }
            else {
            	if(Log.isLoggable(RICHNOTI, Log.DEBUG))
                	Log.d(TAG, "Connected to gear");
                callbackContext.success();
                return true;
            }
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(richRemoteInputReceiver != null) {
            try {
                this.cordova.getActivity().unregisterReceiver(richRemoteInputReceiver);
                richRemoteInputReceiver = null;    
            } catch(IllegalArgumentException e) {
                //not an error case
            	if(Log.isLoggable(RICHNOTI, Log.DEBUG))
                	Log.d(TAG, "unregistering receiver in onDestroy: " +e.getMessage());
                e.printStackTrace();
            }
        }
    };

    BroadcastReceiver richRemoteInputReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
        	if(Log.isLoggable(RICHNOTI, Log.DEBUG))
            	Log.d(TAG, "RemoteInputReceiver: Received input from gear");

            String callbackID = intent.getStringExtra("callbackID");
            String actionID = intent.getStringExtra("actionID");
            if(Log.isLoggable(RICHNOTI, Log.DEBUG))
            	Log.d(TAG, "onNewIntent: actionID - " + actionID);
            String inputActionData = intent.getStringExtra("extra_action_data");

            JSONObject gearInputReturn = new JSONObject();

            try {
                gearInputReturn.put("returnType", RichNotificationHelper.RETURN_TYPE_REMOTE_INPUT);
                gearInputReturn.put("returnValue", inputActionData);
                gearInputReturn.put("actionID", actionID);
            } catch (JSONException jse) {
                Log.d(TAG, "Gear input invalid");
                return;
            }
            if(Log.isLoggable(RICHNOTI, Log.DEBUG))
            	Log.d(TAG, "Data to be sent to JS: " + inputActionData);
            PluginResult result = new PluginResult(PluginResult.Status.OK, gearInputReturn);
            result.setKeepCallback(true);
            RichNotification.this.webView.sendPluginResult(result, callbackID);
        }
    };

    @Override
    public void onError(UUID arg0, ErrorType arg1) {
    }

    @Override
	//On RichNotification Read in the gear
    public void onRead(UUID arg0) {
        if (mCallbackContext == null)
            return;
        try {
            JSONObject eventMsg = new JSONObject();
            eventMsg.put("returnType", RichNotificationHelper.RETURN_TYPE_NOTIFICATION_READ);
            eventMsg.put("returnValue", arg0.toString());
            PluginResult sentResult = new PluginResult(PluginResult.Status.OK, eventMsg);
            sentResult.setKeepCallback(true);
            mCallbackContext.sendPluginResult(sentResult);
        } catch (JSONException jse) {
        	if(Log.isLoggable(RICHNOTI, Log.DEBUG))
            	Log.d(TAG, "Gear input invalid");
            return;
        }
    }

    @Override
	//On RichNotification Removed from the gear
    public void onRemoved(UUID arg0) {
        if (mCallbackContext == null)
            return;
        try {
            JSONObject eventMsg = new JSONObject();
            eventMsg.put("returnType", RichNotificationHelper.RETURN_TYPE_NOTIFICATION_REMOVED);
            eventMsg.put("returnValue", arg0.toString());
            PluginResult sentResult = new PluginResult(PluginResult.Status.OK, eventMsg);
            sentResult.setKeepCallback(true);
            mCallbackContext.sendPluginResult(sentResult);
        } catch (JSONException jse) {
        	if(Log.isLoggable(RICHNOTI, Log.DEBUG))
            	Log.d(TAG, "Gear input invalid");
            return;
        }
    }
}
