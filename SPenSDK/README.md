# Cordova Plugin for Pen

The Pen Cordova Plugin provides set of JavaScript APIs to implement the S pen
features. This plugin can be used on Samsung devices only.

## Permissions
The Pen Plugin uses the following permission:

`android.permission.WRITE_EXTERNAL_STORAGE`

`com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY`

When Pen Plugin is added to the application project the permissions get added to the project by default.

## Installation
To add the plugin to your application

    cordova plugin add com.samsung.spen

## Supported Platform

- Android

## APIs

- samsung.spen.isSupported
- samsung.spen.launchSurfaceInline
- samsung.spen.launchSurfacePopup
- samsung.spen.removeSurfaceInline
- samsung.spen.removeSurfacePopup

## samsung.spen.isSupported

This method is used to check whether the device supports the Pen feature or not. Before launching popup view or inline view, call this method to verify if the device supports the S Pen feature.

### Synopsis

void isSupported (successCallback, errorCallback)

### Parameters

-  __successCallback__ success the callback function on success.
-  __errorCallback__ fail the callback function on error.

### Example

    <script type="text/javascript">
        function isSPenSupported() {
            samsung.spen.isSupported(successCallback, errorCallback);
        }
        //Called if SPen supported on the target device
        function successCallback(successMessage)    {
            alert(successMessage);
        }
        //Called if SPen not supported on the target device
        function errorCallback(errorMessage){
            alert(errorMessage);
        }
    </script>

## samsung.spen.launchSurfaceInline

This method launches the S pen inline view in a native window. The S pen surface is embedded
into the defined web element, and so it allows to use S pen as inline mode. The maximum number
of surfaces that can be created including both inline and popup are 5.

### Synopsis

void launchSurfaceInline (inlineViewOptions, successCallback, errorCallback)

### Parameters

-  __inlineViewOptions__ The parameters required for creating Inline surface.
-  __successCallback__ success the callback function on success.
-  __errorCallback__ fail the callback function on error.

### Example

    <script type="text/javascript">

        function launchSurfaceInline() {
            var inlineOptions = {};
            inlineOptions.id = "inlineId";
            inlineOptions.bodyRectangleCoordinates = document.body.getBoundingClientRect();

            //inline-image is the element in html page on which spen surface will be superimposed

            inlineOptions.elementCoordinates = document.getElementById("inline-
                                               image").getBoundingClientRect();
            inlineOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_URI;
            inlineOptions.backgroundColor = "#F3F781";
            inlineOptions.sPenFlags = samsung.spen.FLAG_EDIT | samsung.spen.FLAG_UNDO_REDO
            | samsung.spen.FLAG_PEN_SETTINGS;
            // bgImageScaleType is useful only if sPenFlags has
            //Samsung.spen.BACKGROUND.
            inlineOptions.bgImageScaleType =  samsung.spen.BACKGROUND_IMAGE_MODE_STRETCH;
            inlineOptions.imageUri =  "file:///data/data/com.samsung.demoapp/files/sample.png";

            //Other possible imageUri are
            // "/storage/emulated/0/Samsung/Image/sample.jpg"
            // "/storage/extSdCard/Image/sample.png"
            // "content://media/external/images/media/494

            inlineOptions.imageUriScaleType = samsung.spen.IMAGE_URI_MODE_STRETCH;

            samsung.spen.launchSurfaceInline(inlineOptions, successCallback, errorCallback);
        }

        function successCallback(imageUri) {
            if (imagedata) {
                document.getElementById("inline-image").value = imageUri;
            }
        }

        function errorCallback(msg) {
                alert(msg);
        }

    </script>



## samsung.spen.launchSurfacePopup

This method creates a S pen surface view in a native window (pop-up mode). It can also be launched
in customized manner, with sub-properties like pen settings, eraser settings, color options, text
recognition, shape recognition options, and so on. The maximum number of surfaces that can be created
including both inline and popup are 5.

### Synopsis

void launchSurfacePopup (popupViewOptions, successCallback,errorCallback)

### Parameters

-  __popupViewOptions__ The parameters required for creating Popup surface.
-  __successCallback__ success the callback function on success.
-  __errorCallback__ fail the callback function on error.

### Example

    <script type="text/javascript">

        //user defines this function in his application.
        function launchSurfacePopup(noteId, retType) {
            var popupOptions = {};
            popupOptions.id = "popupId";
            popupOptions.sPenFlags = samsung.spen.FLAG_PEN_SETTINGS
                | samsung.spen.FLAG_BACKGROUND | samsung.spen.FLAG_SELECTION;
            popupOptions.backgroundColor = "#F3F781";
            popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_DATA;
            samsung.spen.launchSurfacePopup(popupOptions,successCallback,
                                            errorCallback);
        }

        //return value is string of Base64 encoded image
        function successCallback(imageData){
            if (imagedata) {
                // popup-image is an image holder in html
                document.getElementById("popup-image").src = "data:image/jpg;base64,"
                        + imagedata;
            }
        }

        function errorCallback(errorMessage){
            alert(errorMessage);
        }
    </script>



## samsung.spen.removeSurfaceInline

This method is used to remove the inline surface that is launched by calling the launchSurfaceInline() API method.

### Synopsis

void removeSurfaceInline (id, successCallback, errorCallback)

### Parameters

-  __id__ The Id of the Inline surface that is to be removed.
-  __successCallback__ success the callback function on success.
-  __errorCallback__ fail the callback function on error.

### Example

    <script type="text/javascript">

        removeInlineSurface: function() {
            var successCallback = function(id) {
                console.log("Inline surface successfully removed, id:"+ id);
            }

            var errorCallback = function(msg) {
                alert("Error callback: " + msg);
            }
            samsungspen.removeSurfaceInline(inlineId, successCallback, errorCallback);
        }
    </script>


## samsung.spen.removeSurfacePopup

This method is used to remove the popup surface that is launched by calling the launchSurfacePopup().

### Synopsis

void removeSurfacePopup (id, successCallback, errorCallback)

### Parameters

-  __id__ The id of the popup surface that is to be removed.
-  __successCallback__ success the callback function on success.
-  __errorCallback__ fail the callback function on error.

### Example

    <script type="text/javascript">

        removePopupSurface: function() {
            var successCallback = function(id) {
                console.log("Popup surface successfully removed, id:"+ id);
            }

            var errorCallback = function(msg) {
                alert("Error callback: " + msg);
            }
            samsungspen.removeSurfacePopup(id, successCallback, errorCallback);
        }
    </script>

### Restrictions

The Samsung SPen Cordova Plugin is based on Samsung Pen SDK Version 4.0.0. You can use this plugin only
on Samsung Android (API level 14 or higher) devices. All the restrictions of the Samsung Pen SDK are also
applicable for the Samsung SPen Cordova Plugin.