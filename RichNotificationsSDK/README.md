# Cordova Plugin for RichNotification

The Samsung RichNotification Cordova Plugin provides set of JavaScript APIs to implement the RichNotification features. 
This plugin can be used on Samsung devices only.

## Permissions
The RichNotification Plugin uses following permission:

`com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY`

`com.samsung.wmanager.ENABLE_NOTIFICATION`

`android.permission.CALL_PHONE`

`android.permission.SEND_SMS`

`android.permission.READ_EXTERNAL_STORAGE`

When RichNotification Plugin is added to the application project the permissions get added to the project by default.

## Installation
To add the plugin to your application

    cordova plugin add com.samsung.richnotification
    
## Supported Platform

- Android

## APIs

- samsung.richnotification.isSupported
- samsung.richnotification.isConnected
- samsung.richnotification.registerEventListeners
- samsung.richnotification.send
- samsung.richnotification.dismiss

## samsung.richnotification.isSupported

This method checks if the RichNotification service is supported on the host device. It is recommended to call this
method before sending a RichNotification.

### Synopsis

void isSupported (successCallback, errorCallback)

### Parameters

-  __successCallback__ success the callback function on success.
-  __errorCallback__ fail the callback function on error.

### Example

    <script type="text/javascript">
        function checkRichNotiSupport(){
            var successCallback = function() {
                alert("Service is supported.");
            }

            var errorCallback = function(msg) {
                alert("Service not supported: " + msg);
            }
            samsung.richnotification.isSupported(successCallback, errorCallback);
        }
    </script>

    
## samsung.richnotification.isConnected

This method is used to check if the host device is connected to the Gear. It is recommended to call this 
method before sending the RichNotification.

### Synopsis

void isConnected (successCallback, errorCallback)

### Parameters

-  __successCallback__ success the callback function on success.
-  __errorCallback__ fail the callback function on error.

### Example

    <script type="text/javascript">
        function checkDeviceConnected(){
                var successCallback = function () {
                alert("True");
            }

            var errorCallback = function (msg) {
                alert("False: "+ msg);
            }
            samsung.richnotification.isConnected(successCallback, errorCallback);
        }
    </script>


    
    
## samsung.richnotification.registerEventListeners

This method is used to enable event listener for receiving onRead() and onRemove() 
callbacks from the Gear device, when a notification on the Gear is read or removed, respectively.

### Synopsis

void registerEventListeners (eventCallback)

### Parameters

-  __eventCallback__ This callback is invoked if a notification is read, or removed from the Gear device. It returns a JavaScript object. 

### Example

    <script type="text/javascript">
        function checkEventListeners(){
            var eventRich = function(returnedObject) {
                alert(returnedObject.returnType + ": " + returnedObject.returnValue);
            }
            var successCallback = function(returnedObject) {
                alert(returnedObject.returnType + ": " + returnedObject.returnValue);
            }
            var failureCallback = function(msg) {
                alert("error callback: " +msg)
            }
            var options = {
                notificationIcon: "img/bike.jpg",
                notificationTitle: 'Event Listeners callback test',
                headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
                primarySubHeader: 'Event Listeners enabled',
                primaryQRImage: "img/qr.jpg",
                primaryBackgroundColor: "",
                primaryBackgroundImage: "img/bg1.jpg",
                secondaryType: samsung.richnotification.SECONDARY_TYPE_NONE,
            }
            samsung.richnotification.registerEventListeners(eventRich);
            // Read/Remove the following notification on the Gear to check the 
            //callback
            samsung.richnotification.send(options, successCallback, failureCallback);
        }
    </script>

    
    
    
## samsung.richnotification.send

This method is used to send the rich notification to the Gear device. There are several types of 
notifications that can be defined using the parameters described below. The notification also 
supports basic actions such as Call, SMS, Email and View. These actions are selected on the Gear 
but are triggered on the host device.

### Synopsis

void send (notificationOptions, successCallback, errorCallback)

### Parameters

-  __notificationOptions__ The options required to send the RichNotification.
-  __successCallback__ success the callback function on success.
-  __errorCallback__ fail the callback function on error.

### Example

    <script type="text/javascript">

        function sendNotification(){
            var successCallback = function(returnedObject) {
                alert(returnedObject.returnType + ": " +          
                returnedObject.returnValue);
            }

            var errorCallback = function(msg) {
                alert("error callback: " + msg)
            }

            var options = {
                notificationIcon: "img/icon.png",
                notificationTitle: 'Valid entries test',
                headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
                primarySubHeader: 'Header size should be medium',
                primaryBody: 'The secondary template can be QR or STD in listed format.',
                primaryQRImage: "img/qr.jpg",
                primaryBackgroundColor: "#abcdef",
                primaryBackgroundImage: "/storage/extSdCard/Image/qr_primary.png",
                secondaryType: samsung.richnotification.SECONDARY_TYPE_QR,
                secondarySubHeader: 'Check if all the titles and bodies are visible:',
                secondaryBackgroundColor: "#555555",
                secondaryImage: "img/qr_secondary.png",
                secondaryContent: [
                    {
                        title: "Name",
                        body: "Rahul"
                    },
                    {
                        title: "Age",
                        body: "26 yrs"
                    },
                    {
                        title: "Place",
                        body: "Bangalore"
                    },
                    {
                        title: "Nationality",
                        body: "Indian"
                    }
                ]
            }
            samsung.richnotification.send(options, successCallback, errorCallback);
        }
    </script>



## samsung.richnotification.dismiss

This method is used to dismiss notifications.

### Synopsis

void dismiss (uuid, successCallback, errorCallback) 

### Parameters

-  __uuid__ The uuid of the notification to be dismissed.
-  __successCallback__ success the callback function on success.
-  __errorCallback__ fail the callback function on error.

### Example

    <script type="text/javascript">
        function dismissNotification(){
            var successCallback = function () {
                alert("Rich Notification Dismissed");
            }

            var errorCallback = function (msg) {
                alert("Error: " + msg);
            }

            var richUUID = document.getElementById("uuid").value;

            samsung.richnotification.dismiss(richUUID, successCallback, errorCallback);
        }
    </script>

    
### Restrictions

The Samsung RichNotification Cordova Plugin is based on Samsung Rich Notifications SDK Version 
1.0.0. You can use this plugin only on Samsung Android (API level 19 or higher) devices compatible
with Gear S. All the restrictions of the Samsung RichNotification SDK are also applicable for the 
Samsung RichNotification Cordova Plugin.