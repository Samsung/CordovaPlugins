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

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.samsung.android.sdk.richnotification.SrnAction;
import com.samsung.android.sdk.richnotification.SrnAction.CallbackIntent;
import com.samsung.android.sdk.richnotification.actions.SrnHostAction;
import com.samsung.android.sdk.richnotification.actions.SrnRemoteBuiltInAction;
import com.samsung.android.sdk.richnotification.actions.SrnRemoteBuiltInAction.OperationType;
import com.samsung.android.sdk.richnotification.actions.SrnRemoteInputAction;
import com.samsung.android.sdk.richnotification.actions.SrnRemoteInputAction.InputModeFactory;
import com.samsung.android.sdk.richnotification.actions.SrnRemoteInputAction.KeyboardInputMode;
import com.samsung.android.sdk.richnotification.actions.SrnRemoteInputAction.KeyboardInputMode.KeyboardType;
import com.samsung.android.sdk.richnotification.actions.SrnRemoteInputAction.MultiSelectInputMode;
import com.samsung.android.sdk.richnotification.actions.SrnRemoteInputAction.SingleSelectInputMode;
import com.samsung.android.sdk.richnotification.SrnImageAsset;
import com.samsung.android.sdk.richnotification.templates.SrnPrimaryTemplate;
import com.samsung.android.sdk.richnotification.templates.SrnSecondaryTemplate;
import com.samsung.android.sdk.richnotification.templates.SrnLargeHeaderTemplate;
import com.samsung.android.sdk.richnotification.templates.SrnQRTemplate;
import com.samsung.android.sdk.richnotification.templates.SrnQRSecondaryTemplate;
import com.samsung.android.sdk.richnotification.templates.SrnStandardSecondaryTemplate;
import com.samsung.android.sdk.richnotification.templates.SrnStandardTemplate;
import com.samsung.android.sdk.richnotification.templates.SrnStandardTemplate.HeaderSizeType;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.apache.cordova.CallbackContext;

/**
 * This is a helper class, which has few helper methods to crete template, create action, get uri etc.
 */
public class RichNotificationHelper {
    // Error messages
    static final String VENDOR_NOT_SUPPORTED = "VENDOR_NOT_SUPPORTED";
    static final String DEVICE_NOT_SUPPORTED = "DEVICE_NOT_SUPPORTED";
    static final String LIBRARY_NOT_INSTALLED = "LIBRARY_NOT_INSTALLED";
    static final String LIBRARY_UPDATE_IS_RECOMMENDED = "LIBRARY_UPDATE_IS_RECOMMENDED";
    static final String LIBRARY_UPDATE_IS_REQUIRED = "LIBRARY_UPDATE_IS_REQUIRED";
    static final String NOTIFICATION_MANAGER_NOT_STARTED = "NOTIFICATION_MANAGER_NOT_STARTED";
    static final String INVALID_UUID = "INVALID_UUID";
    static final String DEVICE_NOT_CONNECTED = "DEVICE_NOT_CONNECTED";
    static final String PERMISSION_DENIED = "PERMISSION_DENIED";

    // Header types
    static final String HEADER_TYPE_SMALL = "HEADER_TYPE_SMALL";
    static final String HEADER_TYPE_MEDIUM = "HEADER_TYPE_MEDIUM";
    static final String HEADER_TYPE_LARGE = "HEADER_TYPE_LARGE";
    static final String HEADER_TYPE_FULL = "HEADER_TYPE_FULL";
    static final String HEADER_TYPE_QR = "HEADER_TYPE_QR";

    // Secondary template types
    static final String SECONDARY_TYPE_NONE = "SECONDARY_TYPE_NONE";
    static final String SECONDARY_TYPE_STD = "SECONDARY_TYPE_STD";
    static final String SECONDARY_TYPE_QR = "SECONDARY_TYPE_QR";

    // Alert Types
    static final int ALERT_TYPE_SILENCE = 101;
    static final int ALERT_TYPE_SOUND = 102;
    static final int ALERT_TYPE_SOUND_AND_VIBR = 103;
    static final int ALERT_TYPE_VIBR = 104;

    // Popup types
    static final int POPUP_TYPE_NONE = 201;
    static final int POPUP_TYPE_NORMAL = 202;

    // Action types
    static final int ACTION_TYPE_CALL = 301;
    static final int ACTION_TYPE_SMS = 302;
    static final int ACTION_TYPE_EMAIL = 303;
    static final int ACTION_TYPE_VIEW = 304;
    static final int ACTION_TYPE_INPUT_KEYBOARD = 305;
    static final int ACTION_TYPE_INPUT_SINGLE_SELECT = 306;
    static final int ACTION_TYPE_INPUT_MULTI_SELECT = 307;

    // Keyboard Type
    static final int KEYBOARD_NORMAL = 308;
    static final int KEYBOARD_NUMBER = 309;
    static final int KEYBOARD_EMOJI = 310;

    // sendRichNotification success return types
    static final String RETURN_TYPE_NOTIFICATION_SENT = "RETURN_TYPE_NOTIFICATION_SENT";
    static final String RETURN_TYPE_REMOTE_INPUT = "RETURN_TYPE_REMOTE_INPUT";
    static final String RETURN_TYPE_NOTIFICATION_READ = "RETURN_TYPE_NOTIFICATION_READ";
    static final String RETURN_TYPE_NOTIFICATION_REMOVED = "RETURN_TYPE_NOTIFICATION_REMOVED";

    // Other constants
    private static final String STORAGE_FOLDER = "/localnotification";
    private static final String EMPTY_STRING = "";
    private static final String TAG = "RichNotificationHelper";

	//Creates Primary template of the Rich Notification.
    public static SrnPrimaryTemplate createPrimaryTemplate(Context mContext,
                                                           RichNotificationOptions options) {
        SrnPrimaryTemplate primaryTemplate = null;
        SrnStandardTemplate stdPrimaryTemplate = null;
        SrnLargeHeaderTemplate largeTemplate = null;
        SrnQRTemplate qrPrimaryTemplate = null;

        // Code duplication in case of SMALL, MEDIUM, FULL_SCREEN and default
        if (options.headerSizeType.equals(RichNotificationHelper.HEADER_TYPE_SMALL)) {
            stdPrimaryTemplate = new SrnStandardTemplate(HeaderSizeType.SMALL);
            stdPrimaryTemplate.setSubHeader(options.primarySubHeader);
            stdPrimaryTemplate.setBody(options.primaryBody);
            primaryTemplate = stdPrimaryTemplate;
        }
        else if (options.headerSizeType.equals(RichNotificationHelper.HEADER_TYPE_MEDIUM)) {
            stdPrimaryTemplate = new SrnStandardTemplate(HeaderSizeType.MEDIUM);
            stdPrimaryTemplate.setSubHeader(options.primarySubHeader);
            stdPrimaryTemplate.setBody(options.primaryBody);
            primaryTemplate = stdPrimaryTemplate;
        }
        else if (options.headerSizeType.equals(RichNotificationHelper.HEADER_TYPE_FULL)) {
            stdPrimaryTemplate = new SrnStandardTemplate(HeaderSizeType.FULL_SCREEN);
            stdPrimaryTemplate.setSubHeader(options.primarySubHeader);
            stdPrimaryTemplate.setBody(options.primaryBody);
            primaryTemplate = stdPrimaryTemplate;
        }
        else if (options.headerSizeType.equals(RichNotificationHelper.HEADER_TYPE_LARGE)) {
            largeTemplate = new SrnLargeHeaderTemplate();
            primaryTemplate = largeTemplate;
        }
        else if (options.headerSizeType.equals(RichNotificationHelper.HEADER_TYPE_QR)) {
            qrPrimaryTemplate = new SrnQRTemplate();
            Bitmap imageBitmap = getIconBitmap(mContext, "file://" + options.qrImage);
            SrnImageAsset imageAsset = new SrnImageAsset(mContext, "QR Image", imageBitmap);
            qrPrimaryTemplate.setImage(imageAsset);
            qrPrimaryTemplate.setSubHeader(options.primarySubHeader);
            primaryTemplate = qrPrimaryTemplate;
        }
        else {
            Log.e(TAG, "Header type invalid. Creating SMALL header.");
            stdPrimaryTemplate = new SrnStandardTemplate();
            stdPrimaryTemplate.setSubHeader(options.primarySubHeader);
            stdPrimaryTemplate.setBody(options.primaryBody);
            primaryTemplate = stdPrimaryTemplate;
        }
        // Set the background image of the primary template
        if (!options.primaryBackgroundImage.equals("")) {
            Bitmap priBgBit = getIconBitmap(mContext, "file://" + options.primaryBackgroundImage);
            SrnImageAsset priBgAsst = new SrnImageAsset(mContext, "PrimaryBG", priBgBit);
            primaryTemplate.setBackgroundImage(priBgAsst);
        }
        // Set the background color of the primary template
        if (!options.primaryBackgroundColor.isEmpty()) {
            try {
                int color = Color.parseColor(options.primaryBackgroundColor);
                primaryTemplate.setBackgroundColor(color);
            } catch(IllegalArgumentException illArgEx) {
                Log.e(TAG, "Invalid color string for Primary Background");
            }
        }
        else {
            Log.e(TAG, "Empty string for Primary Background");
        }

        return primaryTemplate;
    }

	//Creates Secondary template of the Rich Notification.
    public static SrnSecondaryTemplate createSecondaryTemplate(Context mContext,
                                                               RichNotificationOptions options)
                                        throws JSONException {
        SrnSecondaryTemplate secondaryTemplate = null;
		//In case of Seconday Type None
        if (options.secondaryType.equalsIgnoreCase(RichNotificationHelper.SECONDARY_TYPE_NONE)) {
            return null;
        }//In case of Seconday Type Standard
        else if (options.secondaryType.equalsIgnoreCase(RichNotificationHelper.SECONDARY_TYPE_STD)) {
            SrnStandardSecondaryTemplate stdSecondary = new SrnStandardSecondaryTemplate();
            stdSecondary.setSubHeader(options.secondarySubHeader);

            if (options.secondaryContent != null) {
                JSONObject content = options.secondaryContent.optJSONObject(0);
                if (content != null) {
                    stdSecondary.setTitle(content.optString("title"));
                    stdSecondary.setBody(content.optString("body"));
                }
            }

            // Set SmallIcons
            Bitmap icon1 = null;
            if (!options.secondaryIcon1Path.equals("")) {
                icon1 = getIconBitmap(mContext, "file://" + options.secondaryIcon1Path);
            }
            SrnImageAsset smallIcon1 = new SrnImageAsset(mContext, "smallIcon1", icon1);
            stdSecondary.setSmallIcon1(smallIcon1, options.secondaryIcon1Text);
            
            Bitmap icon2 = null;
            if (!options.secondaryIcon2Path.equals("")) {
                icon2 = getIconBitmap(mContext, "file://" + options.secondaryIcon2Path);
            }
            SrnImageAsset smallIcon2 = new SrnImageAsset(mContext, "smallIcon2", icon2);
            stdSecondary.setSmallIcon2(smallIcon2, options.secondaryIcon2Text);

            // Set Image
            if (!options.secondaryImage.equals("")) {
                Bitmap secBgBit = getIconBitmap(mContext, "file://" + options.secondaryImage);
                SrnImageAsset secBgAsst = new SrnImageAsset(mContext, "SecondaryBG", secBgBit);
                stdSecondary.setImage(secBgAsst);
            }

            secondaryTemplate = stdSecondary;
            
        } //In case of Seconday Type QR
        else if (options.secondaryType.equalsIgnoreCase(RichNotificationHelper.SECONDARY_TYPE_QR)) {
            SrnQRSecondaryTemplate qrSecondary = new SrnQRSecondaryTemplate();

            if (options.secondaryContent != null) {
                for (int i = 0; i < options.secondaryContent.length(); i++) {
                    JSONObject content = options.secondaryContent.optJSONObject(i);
                    if (content == null)
                        continue;
                    qrSecondary.addListItem(content.optString("title"), content.optString("body"));
                }
            }

            // Set SmallIcons
            Bitmap icon1 = null;
            if (!options.secondaryIcon1Path.equals("")) {
                icon1 = getIconBitmap(mContext, "file://" + options.secondaryIcon1Path);
            }
            SrnImageAsset smallIcon1 = new SrnImageAsset(mContext, "smallIcon1", icon1);
            qrSecondary.setSmallIcon1(smallIcon1, options.secondaryIcon1Text);
            
            Bitmap icon2 = null;
            if (!options.secondaryIcon2Path.equals("")) {
                icon2 = getIconBitmap(mContext, "file://" + options.secondaryIcon2Path);
            }
            SrnImageAsset smallIcon2 = new SrnImageAsset(mContext, "smallIcon2", icon2);
            qrSecondary.setSmallIcon2(smallIcon2, options.secondaryIcon2Text);

            // Set Image
            if (!options.secondaryImage.equals("")) {
                Bitmap secBgBit = getIconBitmap(mContext, "file://" + options.secondaryImage);
                SrnImageAsset secBgAsst = new SrnImageAsset(mContext, "SecondaryBG", secBgBit);
                qrSecondary.setImage(secBgAsst);
            }

            secondaryTemplate = qrSecondary;
        }
        else {
            return null;
        }

        // Set the background color of the secondary template
        if (!options.secondaryBackgroundColor.isEmpty()) {
            try {
                int color = Color.parseColor(options.secondaryBackgroundColor);
                secondaryTemplate.setBackgroundColor(color);
            } catch(IllegalArgumentException illArgEx) {
                Log.e(TAG, "Invalid color string for Secondary Background");
            }
        }
        else {
            Log.e(TAG, "Empty string for Secondary Background");
        }

        return secondaryTemplate;
    }

    // Actions for the Rich Notifications.
    public static List<SrnAction> createActions(Context mContext,
                                                CallbackContext callbackContext,
                                                RichNotificationOptions options)
                                    throws JSONException {
        ArrayList<SrnAction> actionsList = new ArrayList<SrnAction>();
        JSONArray actions = options.actions;
        if (actions == null)
            return null;

        SrnAction action = null;
        for (int i = 0; i < actions.length(); i++) {
            JSONObject act = actions.optJSONObject(i);
            if (act == null)
                continue;

            String actionLabel = act.optString("actionLabel", EMPTY_STRING);
            if (actionLabel.isEmpty())
                continue;

            Bitmap actionIcon = getIconBitmap(mContext, "file://" + act.optString("actionIcon"));
            SrnImageAsset actionImg = new SrnImageAsset(mContext, actionLabel, actionIcon);

            int actionType = act.optInt("type");
            switch (actionType) {
            
                case ACTION_TYPE_CALL:
                    SrnRemoteBuiltInAction call = new SrnRemoteBuiltInAction(actionLabel,
                                                                 OperationType.CALL);
                    call.setData(Uri.parse(act.optString("dest")));
                    action = call;
                    break;
                case ACTION_TYPE_SMS:
                    SrnRemoteBuiltInAction sms = new SrnRemoteBuiltInAction(actionLabel,
                                                                 OperationType.SMS);
                    sms.setData(Uri.fromParts("sms", act.optString("dest"), null));
                    action = sms;
                    break;
                case ACTION_TYPE_EMAIL:
                    Log.d(TAG, "Email to: '" + act.optString("dest") + "'");
                    Log.d(TAG, "Subject: '" + act.optString("subject") + "'");
                    Log.d(TAG, "Body: '" + act.optString("body") + "'");

                    SrnHostAction email = new SrnHostAction(actionLabel);
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    String uriText = "mailto:" + act.optString("dest")
                                        + "?subject=" + Uri.encode(act.optString("subject"))
                                     + "&body=" + Uri.encode(act.optString("body"));
                    Uri uri = Uri.parse(uriText);
                    emailIntent.setData(uri);
                    email.setCallbackIntent(CallbackIntent.getActivityCallback(emailIntent));
                    email.setToast(act.optString("toast"));
                    email.setIcon(actionImg);
                    action = email;
                    break;
                case ACTION_TYPE_VIEW:
                    SrnHostAction view = new SrnHostAction(actionLabel);
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                    String urlText = act.optString("dest");
                    Uri url = Uri.parse(urlText);
                    viewIntent.setData(url);
                    view.setCallbackIntent(CallbackIntent.getActivityCallback(viewIntent));
                    view.setToast(act.optString("toast"));
                    view.setIcon(actionImg);
                    action = view;
                    break;
                case ACTION_TYPE_INPUT_KEYBOARD:
                case ACTION_TYPE_INPUT_SINGLE_SELECT:
                case ACTION_TYPE_INPUT_MULTI_SELECT:
                    SrnRemoteInputAction input = getRemoteInputAction(mContext, act);
                    if (input == null) {
                        continue;
                    }

                    Intent inputIntent = new Intent("com.samsung.cordova.richnotification.remote_input_receiver");
                    inputIntent.putExtra("callbackID", callbackContext.getCallbackId());
                    String actionID = act.optString("actionID", EMPTY_STRING);
                    if (actionID.isEmpty()) {
                        continue;
                    }
                    else {
                        inputIntent.putExtra("actionID", actionID);
                    }
                    input.setCallbackIntent(CallbackIntent.getBroadcastCallback(inputIntent));
                    input.setIcon(actionImg);
                    action = input;
                    break;
                default:
                    Log.e(TAG, "Invalid action type: "+ actionType);
                    continue;
            }

            Log.d(TAG, "Action type created: " + actionType);
            actionsList.add(action);
        }

        return actionsList;
    }

    public static Bitmap getIconBitmap(Context mContext, String path) {
        Bitmap bmp;

        try{
            Uri uri = getUri(mContext, path);
            if (uri == null)
                return null;
            else
                uri = Uri.parse(uri.toString());
            bmp = getIconFromUri(mContext, uri);
        } catch (IOException e) {
            bmp = null;
        }

        return bmp;
    }

    // Private helper functions
    private static SrnRemoteInputAction getRemoteInputAction (Context mContext, JSONObject action)
                                        throws JSONException {
        SrnRemoteInputAction inputAction = null;
        String actionLabel = action.optString("actionLabel");
        if (actionLabel == null || actionLabel.isEmpty())
            return null;

        inputAction = new SrnRemoteInputAction(actionLabel);

        int inputType = action.optInt("type");
        switch (inputType) {
            case ACTION_TYPE_INPUT_KEYBOARD:
                KeyboardInputMode kbInput = InputModeFactory.createKeyboardInputMode();
                String prefillString = action.optString("body");
                int charLimit = action.optInt("charLimit", 0);
                if (charLimit > 0 && charLimit <= prefillString.length()) {
                    kbInput.setCharacterLimit(charLimit);
                    kbInput.setPrefillString(prefillString.substring(0, charLimit));
                }
                else if (charLimit > 0 && charLimit > prefillString.length()) {
                    kbInput.setCharacterLimit(charLimit);
                    kbInput.setPrefillString(prefillString);
                }
                else {
                    kbInput.setPrefillString(prefillString);
                }
                int keyboardType = action.optInt("keyboardType",  KEYBOARD_NORMAL);
                kbInput.setKeyboardType(getKeyboardType(keyboardType));
                inputAction.setRequestedInputMode(kbInput);
                break;
            case ACTION_TYPE_INPUT_SINGLE_SELECT:
            case ACTION_TYPE_INPUT_MULTI_SELECT:
                SingleSelectInputMode single = InputModeFactory.createSingleSelectInputMode();
                MultiSelectInputMode multi = InputModeFactory.createMultiSelectInputMode();
                JSONArray choices = action.optJSONArray("choices");
                Log.d(TAG, "Choices: " + choices);
                if (choices == null || choices.length() == 0)
                    return null;

                for (int index = 0; index < choices.length(); index++) {
                    JSONObject choice = choices.optJSONObject(index);
                    Log.d(TAG, "Choice: " + choice);
                    if (choice == null)
                        continue;

                    String choiceLabel = choice.optString("choiceLabel", null);
                    String choiceID = choice.optString("choiceID", null);
                    if (choiceLabel == null || choiceID == null)
                        continue;

                    Bitmap chIco = getIconBitmap(mContext, "file://" + choice.optString("choiceIcon"));
                    Log.d(TAG, "chIco for '" + choiceLabel + "'' : " + chIco);
                    SrnImageAsset choiceImg = new SrnImageAsset(mContext, choiceLabel, chIco);

                    boolean selected = choice.optBoolean("selected");
                    if (inputType == ACTION_TYPE_INPUT_SINGLE_SELECT) {
                        single.addChoice(choiceLabel, choiceID, choiceImg);
                        inputAction.setRequestedInputMode(single);
                    }
                    else {
                        multi.addChoice(choiceLabel, choiceID, choiceImg, selected);
                        inputAction.setRequestedInputMode(multi);
                    }
                }

                break;
            default:
                Log.d(TAG, "Invalid input type. Hence, ignoring.");
                return null;
        }
        return inputAction;
    }
    private static Uri getUri(Context mContext, String path) {
        if (path.startsWith("file:///")) {
            return getUriFromPath(path);
        }
        else if (path.startsWith("file://")) {
            return getUriFromAsset(mContext, path);
        }
        else {
            return null;
        }
    }

    private static Uri getUriFromPath(String path) {
        String absPath = path.replaceFirst("file://", "");
        File file = new File(absPath);

        if (!file.exists()) {
            Log.e(TAG, "File not found: " + file.getAbsolutePath());
            return Uri.EMPTY;
        }

        return Uri.fromFile(file);
    }

    private static Uri getUriFromAsset(Context mContext, String path) {
        File dir = mContext.getExternalCacheDir();

        if (dir == null) {
            Log.e(TAG, "Missing external cache dir");
            return Uri.EMPTY;
        }
        String resPath  = path.replaceFirst("file:/", "www");
        String fileName = resPath.substring(resPath.lastIndexOf('/') + 1);
        String storage  = dir.toString() + STORAGE_FOLDER;

        if (fileName == null || fileName.isEmpty()) {
            Log.e(TAG, "Filename is missing");
            return Uri.EMPTY;
        }

        File file = new File(storage, fileName);
        FileOutputStream outStream = null;
        InputStream inputStream = null;

        try {
            File fileStorage = new File(storage);
            if (!fileStorage.mkdir())
                Log.e(TAG, "Storage directory could not be created: " + storage);

            AssetManager assets = mContext.getAssets();
            outStream = new FileOutputStream(file);
            inputStream = assets.open(resPath);

            copyFile(inputStream, outStream);
            outStream.flush();
            outStream.close();
            return Uri.fromFile(file);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: assets/" + resPath);
        } catch (IOException ioe) {
            Log.e(TAG, "IOException occured");
        } catch (SecurityException secEx) {
            Log.e(TAG, "SecurityException: directory creation denied");
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outStream != null) {
                    outStream.flush();
                    outStream.close();
                }
            } catch (IOException ioe) {
                Log.e(TAG, "IOException occured while closing/flushing streams");
            }
        }
        return Uri.EMPTY;
    }

    private static Bitmap getIconFromUri(Context mContext, Uri uri) throws IOException {
        InputStream input = mContext.getContentResolver().openInputStream(uri);
        return BitmapFactory.decodeStream(input);
    }
    
    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;

        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

	// Get the type of the Keyboard from the user.
    private static KeyboardType getKeyboardType(int keyboardType) {
        if(keyboardType == KEYBOARD_NORMAL) {
            return KeyboardType.NORMAL;
        } else if(keyboardType == KEYBOARD_NUMBER) {
            return KeyboardType.NUMBER;
        } else if(keyboardType == KEYBOARD_EMOJI) {
            return KeyboardType.EMOJI;
        } else {
            return KeyboardType.NORMAL;
        }
    }
}