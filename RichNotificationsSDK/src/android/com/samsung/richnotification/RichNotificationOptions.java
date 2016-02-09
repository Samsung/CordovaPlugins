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

import android.util.Log;

import com.samsung.android.sdk.richnotification.SrnRichNotification.AlertType;
import com.samsung.android.sdk.richnotification.SrnRichNotification.PopupType;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * This class stores options for rich Notification.
 */
public class RichNotificationOptions {
    private static final String TAG = "RichNotificationOptions";

    RichNotificationOptions(JSONArray data) throws JSONException {
        // Fetch options from the data object
        this.uuid = data.optString(0);
        this.readoutTitle = data.optString(1);
        this.readout = data.optString(2);
        this.notificationTitle = data.optString(3);
        this.headerSizeType = data.optString(4);
        this.primarySubHeader = data.optString(5);
        this.primaryBody = data.optString(6);
        this.qrImage = data.optString(7);
        this.primaryBackgroundColor = data.optString(8);
        this.primaryBackgroundImage = data.optString(9);
        this.secondaryType = data.optString(10);
        this.secondarySubHeader = data.optString(11);
        this.secondaryContent = data.optJSONArray(12);
        this.secondaryBackgroundColor = data.optString(13);
        this.secondaryImage = data.optString(14);
        this.secondaryIcon1Path = data.optString(15);
        this.secondaryIcon1Text = data.optString(16);
        this.secondaryIcon2Path = data.optString(17);
        this.secondaryIcon2Text = data.optString(18);
        this.notificationIcon = data.optString(19);
        int alert = data.optInt(20);
        int popup = data.optInt(21);
        this.actions = data.optJSONArray(22);

        switch(alert) {
            case RichNotificationHelper.ALERT_TYPE_SILENCE:
                this.alertType = AlertType.SILENCE;
                break;
            case RichNotificationHelper.ALERT_TYPE_SOUND:
                this.alertType = AlertType.SOUND;
                break;
            case RichNotificationHelper.ALERT_TYPE_SOUND_AND_VIBR:
                this.alertType = AlertType.SOUND_AND_VIBRATION;
                break;
            case RichNotificationHelper.ALERT_TYPE_VIBR:
                this.alertType = AlertType.VIBRATION;
                break;
            default:
                this.alertType = AlertType.SOUND_AND_VIBRATION;
        }

        if (popup == RichNotificationHelper.POPUP_TYPE_NONE)
            this.popupType = PopupType.NONE;
        else
            this.popupType = PopupType.NORMAL;
    }

    public void printLogs() throws JSONException{
        // Print the values in the logs
        Log.d(TAG, "UUID: '" + this.uuid + "'");
        Log.d(TAG, "notificationIcon: '" + this.notificationIcon + "'");
        Log.d(TAG, "alertType: '" + this.alertType + "'");
        Log.d(TAG, "popupType: '" + this.popupType + "'");
        Log.d(TAG, "readoutTitle: '" + this.readoutTitle + "'");
        Log.d(TAG, "readout: '" + this.readout + "'");
        Log.d(TAG, "notificationTitle: '" + this.notificationTitle + "'");
        Log.d(TAG, "headerSizeType: '" + this.headerSizeType + "'");
        Log.d(TAG, "primarySubHeader: '" + this.primarySubHeader + "'");
        Log.d(TAG, "primaryBody: '" + this.primaryBody + "'");
        Log.d(TAG, "qrImage: '" + this.qrImage + "'");
        Log.d(TAG, "primaryBackgroundColor: '" + this.primaryBackgroundColor + "'");
        Log.d(TAG, "primaryBackgroundImage: '" + this.primaryBackgroundImage + "'");
        Log.d(TAG, "secondaryType: '" + this.secondaryType + "'");
        Log.d(TAG, "secondarySubHeader: '" + this.secondarySubHeader + "'");
        if (this.secondaryContent != null)
            Log.d(TAG, "secondaryContent: " + this.secondaryContent.toString(4));
        else
            Log.d(TAG, "secondaryContent: " + this.secondaryContent);
        Log.d(TAG, "secondaryBgColor: '" + this.secondaryBackgroundColor + "'");
        Log.d(TAG, "secondaryImage: '" + this.secondaryImage + "'");
        Log.d(TAG, "secondaryIcon1Path: '" + this.secondaryIcon1Path + "'");
        Log.d(TAG, "secondaryIcon1Text: '" + this.secondaryIcon1Text + "'");
        Log.d(TAG, "secondaryIcon2Path: '" + this.secondaryIcon2Path + "'");
        Log.d(TAG, "secondaryIcon2Text: '" + this.secondaryIcon2Text + "'");
        if (this.actions != null)
            Log.d(TAG, "actions: " + this.actions.toString(4));
        else
            Log.d(TAG, "actions: " + this.actions);
    }

    public String uuid;
    public String notificationIcon;
    public AlertType alertType;
    public PopupType popupType;
    public String readoutTitle;
    public String readout;
    public String notificationTitle;
    public String headerSizeType;
    public String primarySubHeader;
    public String primaryBody;
    public String qrImage;
    public String primaryBackgroundColor;
    public String primaryBackgroundImage;
    public String secondaryType;
    public String secondarySubHeader;
    public JSONArray secondaryContent;
    public String secondaryBackgroundColor;
    public String secondaryImage;
    public String secondaryIcon1Path;
    public String secondaryIcon1Text;
    public String secondaryIcon2Path;
    public String secondaryIcon2Text;
    public JSONArray actions;
}