/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
exports.defineAutoTests = function() {
    var fail = function(done) {
        expect(true).toBe(false);
        done();
    };
    var pass = function(done) {
        expect(true).toBe(true);
        done();
    };

    describe('RichNotification isSupported', function() {
        it("method should exist", function() {
            expect(samsung.richnotification).toBeDefined();
            expect(samsung.richnotification.isSupported).toBeDefined();
            expect(typeof samsung.richnotification.isSupported).toBe('function');
        });

        it("method callback check", function(done) {
            var successrich = function() {
                pass(done);
            }

            var failurerich = function(msg) {
                pass(done);
            }
            samsung.richnotification.isSupported(successrich, failurerich);
        });
   });

    describe('RichNotification isConnected', function() {
        it("method should exist", function() {
            expect(samsung.richnotification).toBeDefined();
            expect(samsung.richnotification.isConnected).toBeDefined();
            expect(typeof samsung.richnotification.isConnected).toBe('function');
        });

        it("method callback check", function(done) {
            var successrich = function() {
                pass(done);
            }

            var failurerich = function(msg) {
                pass(done);
            }
            samsung.richnotification.isConnected(successrich, failurerich);
        });
    });

    describe('RichNotification registerEventListeners', function() {
        it("method should exist", function() {
            expect(samsung.richnotification).toBeDefined();
            expect(samsung.richnotification.registerEventListeners).toBeDefined();
            expect(typeof samsung.richnotification.registerEventListeners).toBe('function');
        });
    });

    describe('RichNotification send', function() {
        it("method should exist", function() {
            expect(samsung.richnotification).toBeDefined();
            expect(samsung.richnotification.send).toBeDefined();
            expect(typeof samsung.richnotification.send).toBe('function');
        });

        it("method callback check with valid entries", function(done) {
            var successrich = function() {
                pass(done);
            }

            var failurerich = function() {
                fail(done);
            }

            var options = {
                notificationIcon: "img/bike.jpg",
                notificationTitle: 'Valid entries test',
                headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
                primarySubHeader: 'Header size should be medium',
                primaryBody: 'The secondary template should be QR, i.e. listed format.',
                primaryQRImage: "img/qr.jpg",
                primaryBackgroundColor: "#abcdef",
                primaryBackgroundImage: "img/bg1.jpg",
                secondaryType: samsung.richnotification.SECONDARY_TYPE_QR,
                secondarySubHeader: 'Check if all the titles and bodies are visible:',
                secondaryBackgroundColor: "#555555",
                secondaryImage: "img/bg2.jpg",
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

            samsung.richnotification.send(options, successrich, failurerich);
        });
		
        it("Invalid Alert Type", function(done) {
            var successrich = function() {
                pass(done);
            }

            var failurerich = function() {
                fail(done);
            }

            var options = {
                notificationIcon: "img/bike.jpg",
                notificationTitle: 'Invalid Alert Type',
                alertType: samsung.richnotification.ALERT_TYPE_SILE,
                popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
            }

            samsung.richnotification.send(options, successrich, failurerich);
        });
		
	it("Invalid Notification Icon", function(done) {
            var successrich = function() {
                pass(done);
            }

            var failurerich = function() {
                fail(done);
            }

            var options = {
                notificationIcon: "img/bike.jg",
                notificationTitle: 'Invalid Notification Icon',
                alertType: samsung.richnotification.ALERT_TYPE_SILENCE,
                popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
            }

            samsung.richnotification.send(options, successrich, failurerich);
        });
		
        it("Invalid Popup Type", function(done) {
            var successrich = function() {
                pass(done);
            }

            var failurerich = function() {
                fail(done);
            }

            var options = {
                notificationIcon: "img/bike.jpg",
                notificationTitle: 'Invalid Popup Type',
                alertType: samsung.richnotification.ALERT_TYPE_SILENCE,
                popupType: samsung.richnotification.POPUP_TYPE_NORMAL100,
            }

            samsung.richnotification.send(options, successrich, failurerich);
        });
		
        it("Invalid Header Size", function(done) {
            var successrich = function() {
                pass(done);
            }

            var failurerich = function() {
                fail(done);
            }

            var options = {
                notificationIcon: "img/bike.jpg",
                notificationTitle: 'Invalid Header Size',
                alertType: samsung.richnotification.ALERT_TYPE_SILENCE,
                popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
                headerSizeType: 990,
            }

            samsung.richnotification.send(options, successrich, failurerich);
        });
		
        it("Invalid Primary Background Image", function(done) {
            var successrich = function() {
                pass(done);
            }

            var failurerich = function() {
                fail(done);
            }

            var options = {
                notificationIcon: "img/bike.jpg",
                notificationTitle: 'Invalid Primary Background Image',
                alertType: samsung.richnotification.ALERT_TYPE_SILENCE,
                popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
                headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
                primaryBackgroundImage: "img/noimage",
            }

            samsung.richnotification.send(options, successrich, failurerich);
        });
		
        it("Invalid Secondary Type", function(done) {
            var successrich = function() {
                pass(done);
            }

            var failurerich = function() {
                fail(done);
            }

            var options = {
                notificationIcon: "img/bike.jpg",
                notificationTitle: 'Invalid Secondary Type',
                alertType: samsung.richnotification.ALERT_TYPE_SILENCE,
                popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
                headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
                primaryBackgroundImage: "img/bg4.png",
                secondaryType: samsung.richnotification.SECONDARY_TYPE_SD,
            }

            samsung.richnotification.send(options, successrich, failurerich);
        });
		
        it("Invalid Secondary Image", function(done) {
            var successrich = function() {
                pass(done);
            }

            var failurerich = function() {
                fail(done);
            }

            var options = {
                notificationIcon: "img/bike.jpg",
                notificationTitle: 'Invalid Secondary Image',
                alertType: samsung.richnotification.ALERT_TYPE_SILENCE,
                popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
                headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
                primaryBackgroundImage: "img/bg4.png",
                secondaryType: samsung.richnotification.SECONDARY_TYPE_STD,
                secondaryImage: "wrongimage",
            }

            samsung.richnotification.send(options, successrich, failurerich);
        });
		
        it("Invalid Small Icon1 path", function(done) {
            var successrich = function() {
                pass(done);
            }

            var failurerich = function() {
                fail(done);
            }

            var options = {
                notificationIcon: "img/bike.jpg",
                notificationTitle: 'IInvalid Small Icon1 path',
                alertType: samsung.richnotification.ALERT_TYPE_SILENCE,
                popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
                headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
                primaryBackgroundImage: "img/bg4.png",
                secondaryType: samsung.richnotification.SECONDARY_TYPE_STD,
                secondaryImage: "img/bg3.png",
                smallIcon1Path: "wrongPath",
            }

            samsung.richnotification.send(options, successrich, failurerich);
        });
		
        it("Invalid Small Icon2 path", function(done) {
            var successrich = function() {
                pass(done);
            }

            var failurerich = function() {
                fail(done);
            }

            var options = {
                notificationIcon: "img/bike.jpg",
                notificationTitle: 'Invalid Small Icon2 path',
                alertType: samsung.richnotification.ALERT_TYPE_SILENCE,
                popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
                headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
                primaryBackgroundImage: "img/bg4.png",
                secondaryType: samsung.richnotification.SECONDARY_TYPE_STD,
                secondaryImage: "img/bg3.png",
                smallIcon1Path: "img/bg2.jpg",
                smallIcon1Text: "This is the Small Icon 1",
                smallIcon2Path: "im",
		smallIcon2Text: "This is the Small Icon 2",
            }

            samsung.richnotification.send(options, successrich, failurerich);
        });
		
        it("Empty Action Label for Call", function(done) {
            var successrich = function() {
                pass(done);
            }

            var failurerich = function() {
                fail(done);
            }

            var options = {
                notificationIcon: "img/bike.jpg",
                notificationTitle: 'Empty Action Label for Call',
                alertType: samsung.richnotification.ALERT_TYPE_SILENCE,
                popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
                headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
                primaryBackgroundImage: "img/bg4.png",
                secondaryType: samsung.richnotification.SECONDARY_TYPE_STD,
                secondaryImage: "img/bg3.png",
                smallIcon1Path: "img/bg2.jpg",
                smallIcon1Text: "This is the Small Icon 1",
                actions: [{type: samsung.richnotification.ACTION_TYPE_CALL,
                           dest: 9100091000}],
            }

            samsung.richnotification.send(options, successrich, failurerich);
        });
		
        it("Empty Action Label for SMS", function(done) {
            var successrich = function() {
                pass(done);
            }

            var failurerich = function() {
                fail(done);
            }

            var options = {
                notificationIcon: "img/bike.jpg",
                notificationTitle: 'Empty Action Label for SMS',
                alertType: samsung.richnotification.ALERT_TYPE_SILENCE,
                popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
                headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
                primaryBackgroundImage: "img/bg4.png",
                secondaryType: samsung.richnotification.SECONDARY_TYPE_STD,
                secondaryImage: "img/bg3.png",
                smallIcon1Path: "img/bg2.jpg",
                smallIcon1Text: "This is the Small Icon 1",
                actions: [{type: samsung.richnotification.ACTION_TYPE_SMS,
                           dest: 9100091000}],
            }

            samsung.richnotification.send(options, successrich, failurerich);
        });
		
        it("Empty Action Label for Email", function(done) {
            var successrich = function() {
                pass(done);
            }

            var failurerich = function() {
                fail(done);
            }

            var options = {
                notificationIcon: "img/bike.jpg",
                notificationTitle: 'Empty Action Label for Email',
                alertType: samsung.richnotification.ALERT_TYPE_SILENCE,
                popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
                headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
                primaryBackgroundImage: "img/bg4.png",
                secondaryType: samsung.richnotification.SECONDARY_TYPE_STD,
                secondaryImage: "img/bg3.png",
                smallIcon1Path: "img/bg2.jpg",
                smallIcon1Text: "This is the Small Icon 1",
                actions: [{type: samsung.richnotification.ACTION_TYPE_EMAIL,
                           dest: "abcd123@gmail.com",
                           toast: "Email will be sent from phone",
                           subject: "This is the Subject",
                           body: "This is the body of the Email",}],
            }

            samsung.richnotification.send(options, successrich, failurerich);
        });
		
        it("Empty Action Label for View", function(done) {
            var successrich = function() {
                pass(done);
            }

            var failurerich = function() {
                fail(done);
            }

            var options = {
                notificationIcon: "img/bike.jpg",
                notificationTitle: 'Empty Action Label for View',
                alertType: samsung.richnotification.ALERT_TYPE_SILENCE,
                popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
                headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
                primaryBackgroundImage: "img/bg4.png",
                secondaryType: samsung.richnotification.SECONDARY_TYPE_STD,
                secondaryImage: "img/bg3.png",
                smallIcon1Path: "img/bg2.jpg",
                smallIcon1Text: "This is the Small Icon 1",
                actions: [{type: samsung.richnotification.ACTION_TYPE_VIEW,
                           dest: "https://www.youtube.com",
                           toast: "check phone"}],
            }

            samsung.richnotification.send(options, successrich, failurerich);
        });
		
        it("Empty Action Label for Input Keyboard", function(done) {
            var successrich = function() {
                pass(done);
            }

            var failurerich = function() {
                fail(done);
            }

            var options = {
                notificationIcon: "img/bike.jpg",
                notificationTitle: 'Empty Action Label for Input Keyboard',
                alertType: samsung.richnotification.ALERT_TYPE_SILENCE,
                popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
                headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
                primaryBackgroundImage: "img/bg4.png",
                secondaryType: samsung.richnotification.SECONDARY_TYPE_STD,
                secondaryImage: "img/bg3.png",
                smallIcon1Path: "img/bg2.jpg",
                smallIcon1Text: "This is the Small Icon 1",
                actions: [{type: samsung.richnotification.ACTION_TYPE_INPUT_KEYBOARD,
                           keyboardType: samsung.richnotification.KEYBOARD_NORMAL,
                           actionID: "Normal-Keyboard",
                           charLimit: 25,
                           body: "Hi, How are you?",}],
            }

            samsung.richnotification.send(options, successrich, failurerich);
        });
		
        it("Empty Action Label for Input Single Select", function(done) {
            var successrich = function() {
                pass(done);
            }

            var failurerich = function() {
                fail(done);
            }

            var options = {
                notificationIcon: "img/bike.jpg",
                notificationTitle: 'Empty Action Label for Input Single Select',
                alertType: samsung.richnotification.ALERT_TYPE_SILENCE,
                popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
                headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
                primaryBackgroundImage: "img/bg4.png",
                secondaryType: samsung.richnotification.SECONDARY_TYPE_STD,
                secondaryImage: "img/bg3.png",
                smallIcon1Path: "img/bg2.jpg",
                smallIcon1Text: "This is the Small Icon 1",
                actions: [{type: samsung.richnotification.ACTION_TYPE_INPUT_SINGLE_SELECT,
                           actionID: "Single Select Value",
                           choices:[
                                    {    choiceLabel: "Lamborghini",
                                         choiceID:"Galardo" },
                                    {    choiceLabel: "Audi",
                                         choiceID:"A7"      },
                                    {    choiceLabel: "Volkswagon",
                                         choiceID:"Vento"   }]}],
            }

            samsung.richnotification.send(options, successrich, failurerich);
        });
		
        it("method callback check with invalid entries", function(done) {
            var successrich = function() {
                pass(done);
            }

            var failurerich = function() {
                fail(done);
            }

            var options = {
                notificationIcon: 1234,
                notificationTitle: "Invalid value test",
                headerSizeType: 0,
                primarySubHeader: 'Check the following:',
                primaryBody: 'The notification icon should be default (Cordova). The primary background should be transparent',
                primaryQRImage: "img/qr.jpg",
                primaryBackgroundColor: "",
                primaryBackgroundImage: "img/bg1.jpg",
                secondaryType: samsung.richnotification.SECONDARY_TYPE_QR,
                secondarySubHeader: 'The secondary background should be transparent. Check the following details:',
                secondaryBackgroundColor: "#abcdez",
                secondaryImage: "img/bg2.jpg",
                secondaryContent: [
                    {
                        titl: "Name",
                        body: "No title should be visible for this body"
                    },
                    {
                        title: "Body should not be seen for this title",
                        bod: "26 yrs"
                    },
                    {
                        title: "The word 'Bangalore' should follow",
                        body: "Bangalore"
                    },
                    {
                        title: "India",
                        body: "This should follow after 'India'"
                    }
                ]
            }

            samsung.richnotification.send(options, successrich, failurerich);
        });

        it("blank notification test (with null options)", function(done) {
            var successrich = function() {
                pass(done);
            }

            var failurerich = function() {
                fail(done);
            }

            samsung.richnotification.send(null, successrich, failurerich);
        });
    });
};

exports.defineManualTests = function(contentEl, richManualTest) {
    richManualTest('Event Listeners callback test', function() {
        var successRich = function(returnedObject) {
            alert(returnedObject.returnType + ": " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " +msg)
            console.log("error sending: " +msg);
        }
        var options = {
            notificationIcon: "img/bike.jpg",
            notificationTitle: 'Event Listeners callback test',
            headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
            primarySubHeader: 'Event Listeners enabled',
            primaryBody: 'You should get the event callbacks for all the notifications from now on.',
            primaryQRImage: "img/qr.jpg",
            primaryBackgroundColor: "",
            primaryBackgroundImage: "img/bg1.jpg",
            secondaryType: samsung.richnotification.SECONDARY_TYPE_NONE,
        }
        samsung.richnotification.registerEventListeners(successRich);
        samsung.richnotification.send(options, successRich, failureRich);
    });

    richManualTest('RichNotification not-supported test', function() {
        var successrich = function() {
            //fail(done);
        }

        var failurerich = function(msg) {
            console.log("RichNotification not supported: " + msg);
        }
        samsung.richnotification.isSupported(successrich,
                failurerich);
    });

    richManualTest('Host Actions test', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnType);

            if (returnedObject.returnType == samsung.richnotification.RETURN_TYPE_REMOTE_INPUT)
                alert("Action Id: " + returnedObject.actionID  + "  Return Value: " +returnedObject.returnValue);
            else
                alert("returnValue: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " +msg)
            console.log("error sending: " +msg);
        }
        var options = {
            notificationIcon: "img/bike.jpg",
            notificationTitle: 'Host Actions test',
            headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
            primarySubHeader: 'Call, SMS, View and Email',
            primaryBody: 'Check if all the host actions are working. Scroll down for descriptions',
            primaryQRImage: "img/qr.jpg",
            primaryBackgroundColor: "#abcdef",
            primaryBackgroundImage: "img/bg1.jpg",
            secondaryType: samsung.richnotification.SECONDARY_TYPE_QR,
            secondarySubHeader: 'The following actions are tested:',
            secondaryBackgroundColor: "#555555",
            secondaryImage: "img/bg2.jpg",
            secondaryContent: [
                {
                    title: "Call",
                    body: "Should start a call to 9876543210 on the host device"
                },
                {
                    title: "SMS",
                    body: "Should send an SMS to 9876543210"
                },
                {
                    title: "Open file",
                    body: "Will open the file: /storage/emulated/0/Download/file.txt"
                },
                {
                    title: "Open w3schools",
                    body: "Will open the site: www.w3schools.com"
                },
                {
                    title: "Send email",
                    body: "Will open an email client with a prefilled email"
                }
            ],
            actions: [
                {
                    type: samsung.richnotification.ACTION_TYPE_CALL,
                    actionLabel: "Call",
                    dest: "9876543210",
                    toast: "Calling "
                },
                {
                    type: samsung.richnotification.ACTION_TYPE_SMS,
                    actionLabel: "SMS",
                    dest: "9876543210",
                    toast: "SMS to "
                },
                {
                    type: samsung.richnotification.ACTION_TYPE_VIEW,
                    actionLabel: "Open file",
                    actionIcon: "img/tick.jpg",
                    dest: "file:///storage/emulated/0/Download/file.txt",
                    toast: "Check your phone!"
                },
                {
                    type: samsung.richnotification.ACTION_TYPE_VIEW,
                    actionLabel: "Open w3schools",
                    actionIcon: "/storage/emulated/0/Download/bg1.jpg",
                    dest: "http://www.w3schools.com/",
                    toast: "Browser will open on phone!"
                },
                {
                    type: samsung.richnotification.ACTION_TYPE_EMAIL,
                    actionLabel: "Send email",
                    toast: "Email client will open on phone",
                    dest: "usershp@gmail.com,abcd@gmail.com,hahah@yahoo.com",
                    subject: "This is subject",
                    body: "This is the body......."
                }
            ]
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });

    richManualTest('Remote Input Actions callback test', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnType);

            if (returnedObject.returnType == samsung.richnotification.RETURN_TYPE_REMOTE_INPUT)
                alert("Action Id: " + returnedObject.actionID  + "  Return Value: " +returnedObject.returnValue);
            else
                alert("returnValue: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
            console.log("error sending: " + msg);
        }
        var options = {
            notificationIcon: "img/bike.jpg",
            notificationTitle: 'Remote Input Actions test',
            headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
            primarySubHeader: 'Test the working of the input actions',
            primaryBody: 'Check all the input actions and check their returned choice IDs. Refer to the details below',
            primaryQRImage: "img/qr.jpg",
            primaryBackgroundColor: "#abcdef",
            primaryBackgroundImage: "img/bg1.jpg",
            secondaryType: samsung.richnotification.SECONDARY_TYPE_QR,
            secondarySubHeader: 'secondarySubHeader text',
            secondaryBackgroundColor: "#555555",
            secondaryImage: "img/bg2.jpg",
            secondaryContent: [
                {
                    title: "Comment",
                    body: "Keyboard action that returns a text to the host device. actionID: keys"
                },
                {
                    title: "Select any one",
                    body: "Select one of the 3 options and see their numeric vaules returned. actionID: singsel"
                },
                {
                    title: "Select more than one",
                    body: "This is a multi-select action that returns more than one choiceIDs. actionID: multisel"
                }
            ],
            actions: [
                {
                    type: samsung.richnotification.ACTION_TYPE_INPUT_KEYBOARD,
                    actionLabel: "Comment",
                    actionID: "keys",
                    actionIcon: "/storage/emulated/0/Download/bg1.jpg",
                    inputLabel: "Input text",
                    body: "123456",
                    charLimit: -4
                },
                {
                    type: samsung.richnotification.ACTION_TYPE_INPUT_SINGLE_SELECT,
                    actionLabel: "Select any one",
                    actionID: "singsel",
                    choices: [{choiceLabel: "one", choiceID: "1"},
                              {choiceLabel: "two", choiceID: "2"},
                              {choiceLabel: "three", choiceID: "3", choiceIcon: "/storage/emulated/0/Download/bday.jpg"}]
                },
                {
                    type: samsung.richnotification.ACTION_TYPE_INPUT_MULTI_SELECT,
                    actionID: "multisel",
                    actionLabel: "Select more than one",
                    actionIcon: "/storage/emulated/0/Download/bg1.jpg",
                    choices: [{choiceLabel: "one", choiceID: "1", choiceIcon: "/storage/emulated/0/Download/bday.jpg"},
                              {choiceLabel: "two", choiceID: "2", selected: true},
                              {choiceLabel: "three", choiceID: "3", selected: "True"}]
                }
            ]
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Notification Icon only', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
            console.log("error sending: " + msg);
        }
        var options = {
            notificationIcon: "img/bike.jpg",
            notificationTitle: 'Notification Icon only test',
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Notification with Alert type Sound', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
            console.log("error sending: " + msg);
        }
        var options = {
            notificationIcon: "img/bike.jpg",
            notificationTitle: 'Notification with Alert type Sound',
	    alertType: samsung.richnotification.ALERT_TYPE_SOUND,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Notification with Alert type Silence', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
            console.log("error sending: " + msg);
        }
        var options = {
            notificationIcon: "img/bike.jpg",
            notificationTitle: 'Notification with Alert type Silence',
	    alertType: samsung.richnotification.ALERT_TYPE_SILENCE,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Notification with Alert type Sound and Vibration', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/bike.jpg",
            notificationTitle: 'Notification with Alert type Sound and Vibration',
	    alertType: samsung.richnotification.ALERT_TYPE_SOUND_AND_VIBR,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Notification with Alert type Vibration Only', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/bike.jpg",
            notificationTitle: 'Notification with Alert type Sound and Vibration',
	    alertType: samsung.richnotification.ALERT_TYPE_VIBR,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Notification with Pop up type None', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/bike.jpg",
            notificationTitle: 'Notification with Pop up type None',
	    alertType: samsung.richnotification.ALERT_TYPE_SOUND,
	    popupType: samsung.richnotification.POPUP_TYPE_NONE,
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Notification with Pop up type Normal', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/bike.jpg",
            notificationTitle: 'Notification with Pop up type Normal',
	    alertType: samsung.richnotification.ALERT_TYPE_SOUND,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
	    primaryBackgroundColor: "#fedcba",
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Notification with Readout', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/bike.jpg",
            notificationTitle: 'Notification with Readout',
            readoutTitle: "This is the Readout Title",
            readout: "Test for Readout",
	    alertType: samsung.richnotification.ALERT_TYPE_SOUND,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
	    primaryBackgroundColor: "#fedcba",
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Notification with Header Size Small', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/car.png",
            notificationTitle: 'Notification with Header Size Small',
	    alertType: samsung.richnotification.ALERT_TYPE_SOUND,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
	    headerSizeType: samsung.richnotification.HEADER_TYPE_SMALL,
	    primaryBackgroundColor: "#abcdef",
	    primaryBackgroundImage: "img/venue.jpg",
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Notification with Header Size Medium', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/car.png",
            notificationTitle: 'Notification with Header Size Medium',
	    alertType: samsung.richnotification.ALERT_TYPE_VIBR,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
	    headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
	    primaryBackgroundColor: "#0047b2",
	    primaryBackgroundImage: "img/venue.jpg",
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Notification with Header Size Full', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/car.png",
            notificationTitle: 'Notification with Header Size Full',
	    alertType: samsung.richnotification.ALERT_TYPE_VIBR,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
	    headerSizeType: samsung.richnotification.HEADER_TYPE_FULL,
	    primaryBackgroundColor: "#003366",
	    primaryBackgroundImage: "img/venue.jpg",
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Notification with Header Size Large', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/car.png",
            notificationTitle: 'Notification with Header Size Large',
	    alertType: samsung.richnotification.ALERT_TYPE_VIBR,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
	    headerSizeType: samsung.richnotification.HEADER_TYPE_LARGE,
	    primaryBackgroundColor: "#00CC99",
	    primaryBackgroundImage: "img/venue.jpg",
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Notification with Header type QR', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/car.png",
            notificationTitle: 'Notification with Header type QR',
	    alertType: samsung.richnotification.ALERT_TYPE_VIBR,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
	    headerSizeType: samsung.richnotification.HEADER_TYPE_QR,
	    primaryBackgroundColor: "#00CC99",
	    primarySubHeader: "<b> Google.com </b>",
	    primaryQRImage: "img/qr_code.jpg",
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Primary SubHeader and Body', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/car.png",
            notificationTitle: 'Primary SubHeader and Body',
	    alertType: samsung.richnotification.ALERT_TYPE_VIBR,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
	    headerSizeType: samsung.richnotification.HEADER_TYPE_FULL,
	    primarySubHeader: "<b> New Movie Released </b>",
	    primaryBody: "<b>Brett Ratner</b>'s New Movie <b>Hercules</b> released",
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Primary QR Image', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/car.png",
            notificationTitle: 'Primary QR Image',
	    alertType: samsung.richnotification.ALERT_TYPE_VIBR,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
	    headerSizeType: samsung.richnotification.HEADER_TYPE_QR,
	    primaryBackgroundColor: "#6699FF",
	    primarySubHeader: "<b> Google </b>",
	    primaryQRImage: "img/qr_code.jpg",
	    primaryBody: "<b>Visit google.com",
	    primaryBackgroundImage: "img/bg1.jpg",
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Primary Template Example', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/car.png",
            notificationTitle: 'Primary Template Example',
	    alertType: samsung.richnotification.ALERT_TYPE_VIBR,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
	    headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
	    primaryBackgroundColor: "#6699FF",
	    primarySubHeader: "<b> New Movie Released </b>",
	    primaryQRImage: "img/qr_code.jpg",
	    primaryBody: "<b>Brett Ratner</b>'s New Movie <b>Hercules</b> released",
	    primaryBackgroundImage: "img/party.jpg",
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Secondary Type None', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/car.png",
            notificationTitle: 'Secondary Type None',
	    alertType: samsung.richnotification.ALERT_TYPE_VIBR,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
	    headerSizeType: samsung.richnotification.HEADER_TYPE_SMALL,
	    primaryBackgroundColor: "#6699FF",
	    primarySubHeader: "<b> New Movie Released </b>",
	    primaryQRImage: "img/qr_code.jpg",
	    primaryBody: "<b>Brett Ratner</b>'s New Movie <b>Hercules</b> released",
	    primaryBackgroundImage: "img/party.jpg",
	    secondaryType: samsung.richnotification.SECONDARY_TYPE_NONE,
	    secondarySubHeader: "<b>Release Date</b>: 25-07-2014",
	    secondaryBackgroundColor: "#123456",
	    secondaryImage: "img/bday.jpg",
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Secondary Type Standard', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/car.png",
            notificationTitle: 'Secondary Type Standard',
	    alertType: samsung.richnotification.ALERT_TYPE_VIBR,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
	    headerSizeType: samsung.richnotification.HEADER_TYPE_SMALL,
	    primaryBackgroundColor: "#6699FF",
	    primarySubHeader: "<b> New Movie Released </b>",
	    primaryQRImage: "img/qr_code.jpg",
	    primaryBody: "<b>Brett Ratner</b>'s New Movie <b>Hercules</b> released",
	    primaryBackgroundImage: "img/party.jpg",
	    secondaryType: samsung.richnotification.SECONDARY_TYPE_STD,
	    secondarySubHeader: "<b>Release Date</b>: 25-07-2014",
	    secondaryBackgroundColor: "#123456",
	    secondaryImage: "img/bday.jpg",
	    secondaryContent: [
                {
                    title: "Birthday Bash!!",
                    body: "You are cordially invited to Harry's birthday party at Leela Palace, at 7:30 PM"
                }],
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Secondary Type QR', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/car.png",
            notificationTitle: 'Secondary Type QR',
	    alertType: samsung.richnotification.ALERT_TYPE_VIBR,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
	    headerSizeType: samsung.richnotification.HEADER_TYPE_SMALL,
	    primaryBackgroundColor: "#6699FF",
	    primarySubHeader: "<b> New Movie Released </b>",
	    primaryQRImage: "img/qr_code.jpg",
	    primaryBody: "<b>Brett Ratner</b>'s New Movie <b>Hercules</b> released",
	    primaryBackgroundImage: "img/party.jpg",
	    secondaryType: samsung.richnotification.SECONDARY_TYPE_QR,
	    secondarySubHeader: "<b>Release Date</b>: 25-07-2014",
	    secondaryBackgroundColor: "#123456",
	    secondaryImage: "img/venue.jpg",
	    secondaryContent: [
                {
                    title: "Birthday Bash!!",
                    body: "You are cordially invited to Harry's birthday party at Leela Palace, at 7:30 PM"
                },
		{   
		    title:"Happy Birthday !!",
		    body:"Harry is now 30 yrs Old" }],
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Secondary Template Example', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/car.png",
            notificationTitle: 'Secondary Template Example',
	    alertType: samsung.richnotification.ALERT_TYPE_VIBR,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
	    headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
	    secondaryType: samsung.richnotification.SECONDARY_TYPE_QR,
	    secondarySubHeader: "<b>Release Date</b>: 25-07-2014",
	    secondaryBackgroundColor: "#123456",
	    secondaryImage: "img/movie.jpg",
	    secondaryContent: [
                {
                    title: "<b>Synopsis</b>",
                    body: "Having endured his legendary twelve labors, "
                    + "Hercules, the Greek demigod, has his life as a sword-for-hire "
                    + "tested when the King of Thrace and his daughter seek his aid "
                    + "in defeating a tyrannical warlord"
                }],
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Small Icon1 and Small Icon2', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/car.png",
            notificationTitle: 'Small Icon1 and Small Icon2',
	    alertType: samsung.richnotification.ALERT_TYPE_VIBR,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
	    headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
	    primarySubHeader: "<b> New Movie Released </b>",
	    primaryBody: "<b>Brett Ratner</b>'s New Movie <b>Hercules</b> released",
	    primaryBackgroundColor: '#DB476E',
	    primaryBackgroundImage: "img/movie.jpg",
	    secondaryType: samsung.richnotification.SECONDARY_TYPE_QR,
	    secondarySubHeader: "<b>Release Date</b>: 25-07-2014",
	    secondaryBackgroundColor: "#123456",
	    secondaryImage: "img/pvr.png",
	    secondaryContent: [
                {
                    title: "<b>Synopsis</b>",
                    body: "Having endured his legendary twelve labors, "
                    + "Hercules, the Greek demigod, has his life as a sword-for-hire "
                    + "tested when the King of Thrace and his daughter seek his aid "
                    + "in defeating a tyrannical warlord"
                }],
	    smallIcon1Path:	"img/like.png",
	    smallIcon1Text:	"876 votes",
	    smallIcon2Path:	"img/star_subtitle.png",
	    smallIcon2Text:	"6.1/10",
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Call Action', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/car.png",
            notificationTitle: 'Call Action',
	    alertType: samsung.richnotification.ALERT_TYPE_VIBR,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
	    headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
	    primarySubHeader: "<b> New Movie Released </b>",
	    primaryBody: "<b>Brett Ratner</b>'s New Movie <b>Hercules</b> released",
	    primaryBackgroundColor: '#DB476E',
	    primaryBackgroundImage: "img/movie.jpg",
	    secondaryType: samsung.richnotification.SECONDARY_TYPE_QR,
	    secondarySubHeader: "<b>Release Date</b>: 25-07-2014",
	    secondaryBackgroundColor: "#123456",
	    secondaryImage: "img/pvr.png",
	    secondaryContent: [
                {
                    title: "<b>Synopsis</b>",
                    body: "Having endured his legendary twelve labors, "
                    + "Hercules, the Greek demigod, has his life as a sword-for-hire "
                    + "tested when the King of Thrace and his daughter seek his aid "
                    + "in defeating a tyrannical warlord"
                }],
	    smallIcon1Path:	"img/like.png",
	    smallIcon1Text:	"876 votes",
	    smallIcon2Path:	"img/star_subtitle.png",
	    smallIcon2Text:	"6.1/10",
	    actions: [{ type: samsung.richnotification.ACTION_TYPE_CALL,
	                actionLabel: "Call",
			dest: 9100091000}],
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('SMS Action', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/car.png",
            notificationTitle: 'SMS Action',
	    alertType: samsung.richnotification.ALERT_TYPE_VIBR,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
	    headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
	    primarySubHeader: "<b> New Movie Released </b>",
	    primaryBody: "<b>Brett Ratner</b>'s New Movie <b>Hercules</b> released",
	    primaryBackgroundColor: '#DB476E',
	    primaryBackgroundImage: "img/movie.jpg",
	    secondaryType: samsung.richnotification.SECONDARY_TYPE_QR,
	    secondarySubHeader: "<b>Release Date</b>: 25-07-2014",
	    secondaryBackgroundColor: "#123456",
	    secondaryImage: "img/pvr.png",
	    secondaryContent: [
                {
                    title: "<b>Synopsis</b>",
                    body: "Having endured his legendary twelve labors, "
                    + "Hercules, the Greek demigod, has his life as a sword-for-hire "
                    + "tested when the King of Thrace and his daughter seek his aid "
                    + "in defeating a tyrannical warlord"
                }],
	    smallIcon1Path:	"img/like.png",
	    smallIcon1Text:	"876 votes",
	    smallIcon2Path:	"img/star_subtitle.png",
	    smallIcon2Text:	"6.1/10",
	    actions: [{type: samsung.richnotification.ACTION_TYPE_SMS,
	               actionLabel: "SMS",
	               dest: 9100091000}],
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('View Action', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/car.png",
            notificationTitle: 'View Action',
	    alertType: samsung.richnotification.ALERT_TYPE_VIBR,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
	    headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
	    primarySubHeader: "<b> New Movie Released </b>",
	    primaryBody: "<b>Brett Ratner</b>'s New Movie <b>Hercules</b> released",
	    primaryBackgroundColor: '#DB476E',
	    primaryBackgroundImage: "img/movie.jpg",
	    secondaryType: samsung.richnotification.SECONDARY_TYPE_QR,
	    secondarySubHeader: "<b>Release Date</b>: 25-07-2014",
	    secondaryBackgroundColor: "#123456",
	    secondaryImage: "img/pvr.png",
	    secondaryContent: [
                {
                    title: "<b>Synopsis</b>",
                    body: "Having endured his legendary twelve labors, "
                    + "Hercules, the Greek demigod, has his life as a sword-for-hire "
                    + "tested when the King of Thrace and his daughter seek his aid "
                    + "in defeating a tyrannical warlord"
                }],
	    smallIcon1Path:	"img/like.png",
	    smallIcon1Text:	"876 votes",
	    smallIcon2Path:	"img/star_subtitle.png",
	    smallIcon2Text:	"6.1/10",
	    actions: [{type: samsung.richnotification.ACTION_TYPE_VIEW,
	               actionLabel: "Watch Trailer on Youtube",
		       dest: "https://www.youtube.com/watch?v=1L41RWI1oUg",
		       toast: "Check phone"}],
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Email Action', function() {
        var successRich = function(returnedObject) {
            alert("returnType: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/car.png",
            notificationTitle: 'Email Action',
	    alertType: samsung.richnotification.ALERT_TYPE_VIBR,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
	    headerSizeType: samsung.richnotification.HEADER_TYPE_MEDIUM,
	    primarySubHeader: "<b> New Movie Released </b>",
	    primaryBody: "<b>Brett Ratner</b>'s New Movie <b>Hercules</b> released",
	    primaryBackgroundColor: '#DB476E',
	    primaryBackgroundImage: "img/movie.jpg",
	    secondaryType: samsung.richnotification.SECONDARY_TYPE_QR,
	    secondarySubHeader: "<b>Release Date</b>: 25-07-2014",
	    secondaryBackgroundColor: "#123456",
	    secondaryImage: "img/pvr.png",
	    secondaryContent: [
                {
                    title: "<b>Synopsis</b>",
                    body: "Having endured his legendary twelve labors, "
                    + "Hercules, the Greek demigod, has his life as a sword-for-hire "
                    + "tested when the King of Thrace and his daughter seek his aid "
                    + "in defeating a tyrannical warlord"
                }],
	    smallIcon1Path:	"img/like.png",
	    smallIcon1Text:	"876 votes",
	    smallIcon2Path:	"img/star_subtitle.png",
	    smallIcon2Text:	"6.1/10",
	    actions: [{type: samsung.richnotification.ACTION_TYPE_EMAIL,
	               actionLabel: "Email",
		       dest: "abcd123@gmail.com",
		       toast: "Email will be sent from phone",
		       subject: "This is the Subject",
		       body: "This is the body of the Email",}],
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Input Normal Keyboard Action', function() {
        var successRich = function(returnedObject) {
            alert("returnValue: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/car.png",
            notificationTitle: 'Input Normal Keyboard Action',
	    alertType: samsung.richnotification.ALERT_TYPE_VIBR,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
	    headerSizeType: samsung.richnotification.HEADER_TYPE_FULL,
	    primaryBackgroundColor: "#003366",
	    primaryBackgroundImage: "img/venue.jpg",
	    actions: [{type: samsung.richnotification.ACTION_TYPE_INPUT_KEYBOARD,
	               keyboardType: samsung.richnotification.KEYBOARD_NORMAL,
		       actionLabel: "Keyboard",
		       actionID: "Normal-Keyboard",
		       charLimit: 25,
		       body: "Hi, How are you?",}],
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Input Number Keyboard Action', function() {
        var successRich = function(returnedObject) {
            alert("returnValue: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/car.png",
            notificationTitle: 'Input Number Keyboard Action',
	    alertType: samsung.richnotification.ALERT_TYPE_VIBR,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
	    headerSizeType: samsung.richnotification.HEADER_TYPE_FULL,
	    primaryBackgroundColor: "#003366",
	    primaryBackgroundImage: "img/bg1.jpg",
	    actions: [{type: samsung.richnotification.ACTION_TYPE_INPUT_KEYBOARD,
	               keyboardType: samsung.richnotification.KEYBOARD_NUMBER,
		       actionLabel: "Keyboard",
		       actionID: "Normal-Keyboard",
		       charLimit: 25,
		       body: "My age is: ",}],
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Input Emoji Keyboard Action', function() {
        var successRich = function(returnedObject) {
            alert("returnValue: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/car.png",
            notificationTitle: 'Input Emoji Keyboard Action',
	    alertType: samsung.richnotification.ALERT_TYPE_VIBR,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
	    headerSizeType: samsung.richnotification.HEADER_TYPE_FULL,
	    primaryBackgroundColor: "#003366",
	    primaryBackgroundImage: "img/bg2.jpg",
	    actions: [{type: samsung.richnotification.ACTION_TYPE_INPUT_KEYBOARD,
	               keyboardType: samsung.richnotification.KEYBOARD_EMOJI,
		       actionLabel: "Keyboard",
		       actionID: "Normal-Keyboard",
		       charLimit: 25,
		       body: "Haha ",}],
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Single Select Action', function() {
        var successRich = function(returnedObject) {
            alert("returnValue: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/car.png",
            notificationTitle: 'Single Select Action',
	    alertType: samsung.richnotification.ALERT_TYPE_VIBR,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
	    headerSizeType: samsung.richnotification.HEADER_TYPE_FULL,
	    primaryBackgroundColor: "#003366",
	    primaryBackgroundImage: "img/bg1.jpg",
	    actions: [{type: samsung.richnotification.ACTION_TYPE_INPUT_SINGLE_SELECT,
	               actionLabel: "Single Select",
		       actionID: "Single Select Value",
		       choices:[
		           {    choiceLabel: "Lamborghini",
			        choiceID:"Galardo" },
			   {    choiceLabel: "Audi",
			        choiceID:"A7"      },
			   {    choiceLabel: "Volkswagon",
			        choiceID:"Vento"   }]},],
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
	
    richManualTest('Multiple Select Action', function() {
        var successRich = function(returnedObject) {
            alert("returnValue: " + returnedObject.returnValue);
        }
        var failureRich = function(msg) {
            alert("error callback: " + msg)
        }
        var options = {
            notificationIcon: "img/car.png",
            notificationTitle: 'Multiple Select Action',
	    alertType: samsung.richnotification.ALERT_TYPE_VIBR,
	    popupType: samsung.richnotification.POPUP_TYPE_NORMAL,
	    headerSizeType: samsung.richnotification.HEADER_TYPE_FULL,
	    primaryBackgroundColor: "#003366",
	    primaryBackgroundImage: "img/bg1.jpg",
	    actions: [{ type: samsung.richnotification.ACTION_TYPE_INPUT_MULTI_SELECT,
	                actionLabel: "Multiple Select",
			actionID: "Multiple Select Value",
			choices:[
			    {   choiceLabel: "Lamborghini",
			        choiceID:"Galardo" },
			    {   choiceLabel: "Audi",
			        choiceID:"A7"      },
			    {   choiceLabel: "Volkswagon",
			        choiceID:"Vento"   }]},],
        }
        samsung.richnotification.send(options, successRich, failureRich);
    });
}