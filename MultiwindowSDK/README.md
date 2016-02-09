# Cordova Plugin for Multiwindow

The MultiWindow Cordova Plugin provides set of JavaScript APIs to implement the MultiWindow features.
This plugin can be used on Samsung devices only.

## Permissions
The MultiWindow Plugin uses the following permission:
 
`com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY`

When the Multiwindow Plugin is added to the application project the permission gets added to the project by default.

## Installation
To add the plugin to your application

    cordova plugin add com.samsung.multiwindow
    
## Supported Platform

- Android

## APIs

- samsung.multiwindow.isSupported
- samsung.multiwindow.createMultiWindow
- samsung.multiwindow.getMultiWindowApps

## samsung.multiwindow.isSupported

This method is used to check whether the device supports the MultiWindow feature or not. 
This method can also be used to check whether the device supports free style or split style 
MultiWindow. It is recommended to call this method before launching a MultiWindow.

### Synopsis

void isSupported (windowType, successCallback, errorCallback)

### Parameters

-  __windowType__ windowType This parameter checks whether the device supports free style or split style MultiWindow.
                    The parameter can take "freestyle" or "splitstyle" as a value. 
-  __successCallback__ success the callback function on success.
-  __errorCallback__ fail the callback function on error.

### Example

    <script type="text/javascript">
    
        function isSplitStyleSupported() {
            var successCallback = function() {
                alert("This device supports splitstyle");
            }
            var errorCallback = function(msg) {
                alert("This device does not support splitstyle");
            }
            samsung.multiwindow.isSupported("splitstyle", successCallback, errorCallback);
        }
    
        function isFreeStyleSupported() {
            var successCallback = function() {
                alert("This device supports freestyle");
            }
            var errorCallback = function(msg) {
                alert("This device does not support freestyle");
            }
            samsung.multiwindow.isSupported("freestyle", successCallback, errorCallback);
        }
        
    </script>

    
## samsung.multiwindow.createMultiWindow

This method is used to start an activity with free style or split style MultiWindow.

To start an activity with MultiWindow (free style or split style), an app which is holding 
this activity should be MultiWindow enabled. Also, the MultiWindow style of the application 
which holds this activity should not be “fullscreenOnly”. If the MultiWindow style of the 
application is “fullscreenOnly” then the activity will be launched in full window. 

To start an activity with split style MultiWindow, in addition to the above constraints, 
app which is starting an activity using createMultiWindow() method must be a MultiWindow enabled app.


### Synopsis

void createMultiWindow (inputOptions, successCallback, errorCallback) 

### Parameters

-  __inputOptions__ The parameters required for creating Multiwindow.
-  __successCallback__ success the callback function on success.
-  __errorCallback__ fail the callback function on error.

### Example

    <script type="text/javascript">
    
        function launchMultiwindow() {
            var successCallback = function() {
            }
            var errorCallback = function(msg) {
                alert(msg);
            }
       
           var inputOptions = {};
            inputOptions.windowType = “freestyle”;
            inputOptions.action = "action_main"
            inputOptions.scaleInfo = 60;
            inputOptions.packageName = <packagename>;
            inputOptions.activity = <activityname>;
            samsung.multiwindow.createMultiWindow(inputOptions, successCallback, errorCallback);
        }
    </script>


    
    
## samsung.multiwindow.getMultiWindowApps

This method is used to get the list of MultiWindow enabled applications and their main activity 
based on window type value passed. The window type can be free style or split style. If a window 
type is not supported, corresponding error message is returned.

### Synopsis

void getMultiWindowApps (windowType, successCallback, errorCallback) 

### Parameters

-  __windowType__ windowType This parameter is used to find splitstyle or freestyle supported MultiWindow 
                  enabled applications. 
-  __successCallback__ success the callback function on success.
-  __errorCallback__ fail the callback function on error.

### Example

    <script type="text/javascript">
    
        function getPackages() {

            var successCallback = function(appsList) {
                var mActivity, mPackage;

                for ( var key in appsList) {
                    if (appsList.hasOwnProperty(key)) {
                              //use below values in action_main for activity and  
                              //package
                        mPackage = appsList[key]["packageName"];
                        mActivity = appsList[key]["activity"];
                    }
                }
             }

            var errorCallback = function(msg) {
                alert(msg);
            }
            samsung.multiwindow.getMultiWindowApps("splitstyle", successCallback, errorCallback);
        }    
    </script>

    
### Restrictions

The Samsung MultiWindow Cordova Plugin is based on Samsung MultiWindow SDK Version 1.2.3. You can use this 
plugin only on Samsung Android (API level 16 or higher) devices that support MultiWindow. All the restrictions 
of the Samsung MultiWindow SDK are also applicable for the Samsung MultiWindow Cordova Plugin.