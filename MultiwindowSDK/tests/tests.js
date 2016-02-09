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

    describe('MultiWindow isSupported', function() {
        it("method should exist", function() {
            expect(samsung.multiwindow).toBeDefined();
            expect(samsung.multiwindow.isSupported).toBeDefined();
            expect(typeof samsung.multiwindow.isSupported).toBe('function');
        });

        it("method callback check", function(done) {
            var successmw = function() {
                pass(done);
            }

            var failuremw = function(msg) {
                pass(done);
            }
            samsung.multiwindow.isSupported("freestyle", successmw, failuremw);
        });

        it("splitstyle method callback check",
                function(done) {
                    var successmw = function() {
                        pass(done);
                    }

                    var failuremw = function(msg) {
                        console.log("splitstyle method callback check " + msg);
                        if (msg === "SPLIT_STYLE_NOT_SUPPORTED") {
                            pass(done);
                        } else if (msg === "DEVICE_NOT_SUPPORTED") {
                            pass(done);
                        } else {
                            fail(done);
                        }
                    }
                    samsung.multiwindow.isSupported("splitstyle", successmw,
                            failuremw);
                });

        it("freestyle method callback check", function(done) {
            var successmw = function() {
                pass(done);
            }

            var failuremw = function(msg) {
                console.log("freestyle method callback check " + msg);
                if (msg === "FREE_STYLE_NOT_SUPPORTED") {
                    pass(done);
                } else if (msg === "DEVICE_NOT_SUPPORTED") {
                    pass(done);
                } else {
                    fail(done);
                }
            }
            samsung.multiwindow.isSupported("freestyle", successmw, failuremw);
        });

        it("INVALID_WINDOW_TYPE", function(done) {
            // console.log(device.manufacturer);

            var successmw = function() {
                fail(done);
            }

            var failuremw = function(msg) {
                if (msg === "INVALID_WINDOW_TYPE") {
                    pass(done);
                } else {
                    fail(done);
                }

            }
            samsung.multiwindow.isSupported("nonfreestyle", successmw,
                    failuremw);

        });
    });

    describe('MultiWindow createMultiWindow', function() {
        it("method should exist", function() {
            expect(samsung.multiwindow).toBeDefined();
            expect(samsung.multiwindow.createMultiWindow).toBeDefined();
            expect(typeof samsung.multiwindow.createMultiWindow).toBe(
                    'function');
        });

        it("INVALID_ACTION undefined", function(done) {
            // if action is missing, this error is thrown
            var successmw = function() {
                fail(done);
            }

            var failuremw = function(msg) {
                if (msg === "INVALID_ACTION") {
                    pass(done);
                } else {
                    fail(done);
                }
            }
            var inputOptions = {};
            inputOptions.windowType = "freestyle";
            inputOptions.scaleInfo = 60;
            // TODO: fill the right contents, right now this works, but
            // ideally correct input shld be given
            inputOptions.packageName = "dummy";
            inputOptions.activity = "dummy";

            samsung.multiwindow.createMultiWindow(inputOptions, successmw,
                    failuremw);
        });
        it("INVALID_ACTION wrong", function(done) {
            // if action is missing, this error is thrown
            var successmw = function() {
                fail(done);
            }

            var failuremw = function(msg) {
                if (msg === "INVALID_ACTION") {
                    pass(done);
                } else {
                    fail(done);
                }
            }
            var inputOptions = {};
            inputOptions.windowType = "freestyle";
            inputOptions.action = "action";
            inputOptions.scaleInfo = 60;
            // TODO: fill the right contents, right now this works, but
            // ideally correct input shld be given
            inputOptions.packageName = "dummy";
            inputOptions.activity = "dummy";

            samsung.multiwindow.createMultiWindow(inputOptions, successmw,
                    failuremw);
        });
        it("INVALID_WINDOW_TYPE undefined", function(done) {
            // if action is missing, this error is thrown
            var successmw = function() {
                fail(done);
            }

            var failuremw = function(msg) {
                if (msg === "INVALID_WINDOW_TYPE") {
                    pass(done);
                } else {
                    fail(done);
                }
            }
            var inputOptions = {};
            inputOptions.action = "action_main";
            inputOptions.scaleInfo = 60;
            // TODO: fill the right contents, right now this works, but
            // ideally correct input shld be given
            inputOptions.packageName = "dummy";
            inputOptions.activity = "dummy";
            samsung.multiwindow.createMultiWindow(inputOptions, successmw,
                    failuremw);
        });
        it("INVALID_WINDOW_TYPE wrong", function(done) {
            // if action is missing, this error is thrown
            var successmw = function() {
                fail(done);
            }

            var failuremw = function(msg) {
                if (msg === "INVALID_WINDOW_TYPE") {
                    pass(done);
                } else {
                    fail(done);
                }
            }
            var inputOptions = {};
            inputOptions.action = "action_main";
            inputOptions.windowType = "style";
            inputOptions.scaleInfo = 60;
            // TODO: fill the right contents, right now this works, but
            // ideally correct input shld be given
            inputOptions.packageName = "dummy";
            inputOptions.activity = "dummy";
            samsung.multiwindow.createMultiWindow(inputOptions, successmw,
                    failuremw);
        });
        it("INVALID_PACKAGE_OR_ACTIVITY undefined", function(done) {
            // if action is missing, this error is thrown
            var successmw = function() {
                fail(done);
            }

            var failuremw = function(msg) {
                if (msg === "INVALID_PACKAGE_OR_ACTIVITY") {
                    pass(done);
                } else {
                    fail(done);
                }
            }
            var inputOptions = {};
            inputOptions.action = "action_main";
            inputOptions.windowType = "freestyle";
            inputOptions.scaleInfo = 60;
            samsung.multiwindow.createMultiWindow(inputOptions, successmw,
                    failuremw);
        });
        it("INVALID_PACKAGE_OR_ACTIVITY package undefined", function(done) {
            // if action is missing, this error is thrown
            var successmw = function() {
                fail(done);
            }

            var failuremw = function(msg) {
                if (msg === "INVALID_PACKAGE_OR_ACTIVITY") {
                    pass(done);
                } else {
                    fail(done);
                }
            }
            var inputOptions = {};
            inputOptions.action = "action_main";
            inputOptions.windowType = "splitstyle";
            inputOptions.zoneInfo = samsung.multiwindow.SPLITWINDOW_ZONE_A;
            inputOptions.activity = "dummy";
            samsung.multiwindow.createMultiWindow(inputOptions, successmw,
                    failuremw);
        });
        it("INVALID_PACKAGE_OR_ACTIVITY activity undefined", function(done) {
            // if action is missing, this error is thrown
            var successmw = function() {
                fail(done);
            }

            var failuremw = function(msg) {
                if (msg === "INVALID_PACKAGE_OR_ACTIVITY") {
                    pass(done);
                } else {
                    fail(done);
                }
            }
            var inputOptions = {};
            inputOptions.action = "action_main";
            inputOptions.windowType = "splitstyle";
            inputOptions.packageName = "dummy";
            inputOptions.zoneInfo = samsung.multiwindow.SPLITWINDOW_ZONE_A;
            samsung.multiwindow.createMultiWindow(inputOptions, successmw,
                    failuremw);
        });
        it("ACTIVITY_NOT_FOUND", function(done) {
            // if action is missing, this error is thrown
            var successmw = function() {
                fail(done);
            }

            var failuremw = function(msg) {
                if (msg === "ACTIVITY_NOT_FOUND") {
                    pass(done);
                } else if (msg === "DEVICE_NOT_SUPPORTED") {
                    console.log("ACTIVITY_NOT_FOUND " + msg);
                    pass(done);
                } else {
                    console.log("ACTIVITY_NOT_FOUND " + msg);
                    fail(done);
                }
            }
            var inputOptions = {};
            inputOptions.action = "action_main";
            inputOptions.windowType = "splitstyle";
            inputOptions.packageName = "dummy";
            inputOptions.activity = "dummy";
            inputOptions.zoneInfo = samsung.multiwindow.SPLITWINDOW_ZONE_A;

            samsung.multiwindow.createMultiWindow(inputOptions, successmw,
                    failuremw);

        });
        it("INVALID_SCALEINFO", function(done) {
            // if action is missing, this error is thrown
            var successmw = function() {
                fail(done);
            }

            var failuremw = function(msg) {
                if (msg === "INVALID_SCALEINFO") {
                    pass(done);
                } else {
                    console.log("INVALID_SCALEINFO " + msg);
                    fail(done);
                }
            }
            var inputOptions = {};
            inputOptions.action = "action_main";
            inputOptions.windowType = "freestyle";
            inputOptions.packageName = "dummy";
            inputOptions.activity = "dummy";

            samsung.multiwindow.createMultiWindow(inputOptions, successmw,
                    failuremw);
        });
        it("INVALID_ZONEINFO", function(done) {
            // if action is missing, this error is thrown
            var successmw = function() {
                fail(done);
            }

            var failuremw = function(msg) {
                if (msg === "INVALID_ZONEINFO") {
                    pass(done);
                } else {
                    console.log(msg);
                    fail(done);
                }
            }
            var inputOptions = {};
            inputOptions.action = "action_main";
            inputOptions.windowType = "splitstyle";
            inputOptions.packageName = "dummy";
            inputOptions.activity = "dummy";

            samsung.multiwindow.createMultiWindow(inputOptions, successmw,
                    failuremw);
        });
        it("INVALID_DATA_URI", function(done) {
            // if action is missing, this error is thrown
            var successmw = function() {
                fail(done);
            }

            var failuremw = function(msg) {
                if (msg === "INVALID_DATA_URI") {
                    pass(done);
                } else {
                    console.log(msg);
                    fail(done);
                }
            }
            var inputOptions = {};
            inputOptions.action = "action_view";
            inputOptions.windowType = "splitstyle";
            inputOptions.packageName = "dummy";
            inputOptions.activity = "dummy";

            samsung.multiwindow.createMultiWindow(inputOptions, successmw,
                    failuremw);
        });
    });
    describe('MultiWindow getMultiWindowApps', function() {
        it("method should exist", function() {
            expect(samsung.multiwindow).toBeDefined();
            expect(samsung.multiwindow.getMultiWindowApps).toBeDefined();
            expect(typeof samsung.multiwindow.getMultiWindowApps).toBe(
                    'function');
        });

        it("method callback check", function(done) {
            // if action is missing, this error is thrown
            var successmw = function() {
                pass(done);
            }

            var failuremw = function(msg) {
                pass(done);
            }

            samsung.multiwindow.getMultiWindowApps("freestyle", successmw,
                    failuremw);
        });
        it("freestyle callback check", function(done) {
            // if action is missing, this error is thrown
            var successmw = function(appsList) {
                var count = 0;
                var packagesarray = [];
                var activitiesarray = [];
                for ( var key in appsList) {
                    if (appsList.hasOwnProperty(key)) {
                        packagesarray[count] = appsList[key]["package"];
                        activitiesarray[count] = appsList[key]["activity"];
                        count++;
                    }
                }

                if (count > 0) {
                    pass(done);
                } else {
                    fail(done);
                }
            }

            var failuremw = function(msg) {
                console.log("freestyle callback check " + msg);
                if (msg === "DEVICE_NOT_SUPPORTED") {
                    pass(done);
                } else if (msg === "FREE_STYLE_NOT_SUPPORTED") {
                    pass(done);
                } else {
                    console.log(msg);
                    fail(done);
                }
            }

            samsung.multiwindow.getMultiWindowApps("freestyle", successmw,
                    failuremw);
        });
        it("splitstyle callback check", function(done) {
            // if action is missing, this error is thrown
            var successmw = function(appsList) {
                var count = 0;
                var packagesarray = [];
                var activitiesarray = [];

                for ( var key in appsList) {
                    if (appsList.hasOwnProperty(key)) {
                        packagesarray[count] = appsList[key]["package"];
                        activitiesarray[count] = appsList[key]["activity"];
                        count++;
                    }
                }

                if (count > 0) {
                    pass(done);
                } else {
                    fail(done);
                }
            }

            var failuremw = function(msg) {
                console.log("splitstyle callback check " + msg);
                if (msg === "DEVICE_NOT_SUPPORTED") {
                    pass(done);
                } else if (msg === "SPLIT_STYLE_NOT_SUPPORTED") {
                    pass(done);
                } else {
                    console.log(msg);
                    fail(done);
                }
            }

            samsung.multiwindow.getMultiWindowApps("splitstyle", successmw,
                    failuremw);
        });
        it("INVALID_WINDOW_TYPE", function(done) {
            // if action is missing, this error is thrown
            var successmw = function() {
                fail(done);
            }

            var failuremw = function(msg) {
                if (msg === "INVALID_WINDOW_TYPE") {
                    pass(done);
                } else {
                    console.log(msg);
                    fail(done)
                }
            }

            samsung.multiwindow.getMultiWindowApps("nostyle", successmw,
                    failuremw);
        });
    });

};

exports.defineManualTests = function(contentEl, createActionButton) {

    function freestyleMain(scale) {
        var successget = function(appsList) {
            var count = 0;
            var packagesarray = [];
            var activitiesarray = [];

            for ( var key in appsList) {
                if (appsList.hasOwnProperty(key)) {
                    packagesarray[count] = appsList[key]["packageName"];
                    activitiesarray[count] = appsList[key]["activity"];
                    count++;
                }
            }
            
            var successmw = function() {               
            }

            var failuremw = function(msg) {
                console.log("freestyle ACTION_MAIN " + msg);      
            }
            var index = Math.floor((Math.random() * count));

            //now create multiwindow with appslist
            var inputOptions = {};
            inputOptions.action = "action_main";
            inputOptions.windowType = "freestyle";
            inputOptions.scaleInfo = scale;            
            inputOptions.packageName = packagesarray[index];
            inputOptions.activity = activitiesarray[index];

            samsung.multiwindow.createMultiWindow(inputOptions, successmw,
                    failuremw);
        }

        var failureget = function(msg) {
            console.log("freestyle ACTION_MAIN " + msg);      
        }
        samsung.multiwindow.getMultiWindowApps("freestyle", successget,
                failureget);

    }
    function freestyleView(scale) {
        var successget = function(appsList) {
            var count = 0;
            var packagesarray = [];
            var activitiesarray = [];

            for ( var key in appsList) {
                if (appsList.hasOwnProperty(key)) {
                    packagesarray[count] = appsList[key]["packageName"];
                    activitiesarray[count] = appsList[key]["activity"];
                    count++;
                }
            }
            
            var successmw = function() {
            }

            var failuremw = function(msg) {
                console.log("freestyle ACTION_MAIN " + msg);      
            }
            var index = Math.floor((Math.random() * count));

            //now create multiwindow with appslist
            var inputOptions = {};
            inputOptions.action = "action_view";
            inputOptions.windowType = "freestyle";
            inputOptions.scaleInfo = scale;            
            inputOptions.dataUri = "http://www.samsungknox.com/en";

            samsung.multiwindow.createMultiWindow(inputOptions, successmw,
                    failuremw);
        }

        var failureget = function(msg) {
            console.log("freestyle ACTION_MAIN " + msg);      
        }
        samsung.multiwindow.getMultiWindowApps("freestyle", successget,
                failureget);

    }
    function splitstyleMain(zone) {
        var successget = function(appsList) {
            var count = 0;
            var packagesarray = [];
            var activitiesarray = [];

            for ( var key in appsList) {
                if (appsList.hasOwnProperty(key)) {
                    packagesarray[count] = appsList[key]["packageName"];
                    activitiesarray[count] = appsList[key]["activity"];
                    count++;
                }
            }
            
            var successmw = function() {               
            }

            var failuremw = function(msg) {
                console.log("splitstyle ACTION_MAIN " + msg);      
            }
            var index = Math.floor((Math.random() * count));

            //now create multiwindow with appslist
            var inputOptions = {};
            inputOptions.action = "action_main";
            inputOptions.windowType = "splitstyle";
            inputOptions.zoneInfo = zone;            
            inputOptions.packageName = packagesarray[index];
            inputOptions.activity = activitiesarray[index];

            samsung.multiwindow.createMultiWindow(inputOptions, successmw,
                    failuremw);
        }

        var failureget = function(msg) {
            console.log("splitstyle ACTION_MAIN " + msg);      
        }
        samsung.multiwindow.getMultiWindowApps("splitstyle", successget,
                failureget);

    }
    
    function splitstyleView(zone) {
        var successget = function(appsList) {
            var count = 0;
            var packagesarray = [];
            var activitiesarray = [];

            for ( var key in appsList) {
                if (appsList.hasOwnProperty(key)) {
                    packagesarray[count] = appsList[key]["packageName"];
                    activitiesarray[count] = appsList[key]["activity"];
                    count++;
                }
            }
            
            var successmw = function() {               
            }

            var failuremw = function(msg) {
                console.log("splitstyle ACTION_MAIN " + msg);      
            }
            var index = Math.floor((Math.random() * count));

            //now create multiwindow with appslist
            var inputOptions = {};
            inputOptions.action = "action_view";
            inputOptions.windowType = "splitstyle";
            inputOptions.zoneInfo = zone;            
            inputOptions.dataUri = "http://www.samsungknox.com/en";

            samsung.multiwindow.createMultiWindow(inputOptions, successmw,
                    failuremw);
        }

        var failureget = function(msg) {
            console.log("splitstyle ACTION_MAIN " + msg);      
        }
        samsung.multiwindow.getMultiWindowApps("splitstyle", successget,
                failureget);

    }
    createActionButton('freestyle, ACTION_MAIN scale 60', function() {
        freestyleMain(60);        
    });
    
    createActionButton('freestyle, ACTION_MAIN scale 100', function() {
        freestyleMain(100);
    });
    
    createActionButton('freestyle, ACTION_MAIN scale 10', function() {
        freestyleMain(10);
    });
    createActionButton('freestyle, ACTION_MAIN scale 1000', function() {
        freestyleMain(1000);

    });
    createActionButton('freestyle, ACTION_MAIN scale invalid', function() {
        freestyleMain("invalid");
    });
    

    createActionButton('splitstyle, ACTION_MAIN zone A', function() {
        splitstyleMain(samsung.multiwindow.SPLITWINDOW_ZONE_A);
    });
    createActionButton('splitstyle, ACTION_MAIN zone B', function() {
        splitstyleMain(samsung.multiwindow.SPLITWINDOW_ZONE_B);
    });
    createActionButton('splitstyle, ACTION_MAIN full', function() {
        splitstyleMain(samsung.multiwindow.SPLITWINDOW_ZONE_FULL);
    });
    createActionButton('splitstyle, ACTION_MAIN invalid number', function() {
        splitstyleMain(10);
    });
    createActionButton('splitstyle, ACTION_MAIN invalid string', function() {
        splitstyleMain("invalid");
    });
    
    createActionButton('splitstyle, ACTION_VIEW zone A', function() {
        splitstyleView(samsung.multiwindow.SPLITWINDOW_ZONE_A);
    });
    createActionButton('splitstyle, ACTION_VIEW zone B', function() {
        splitstyleView(samsung.multiwindow.SPLITWINDOW_ZONE_B);
    });
    createActionButton('splitstyle, ACTION_VIEW full', function() {
        splitstyleView(samsung.multiwindow.SPLITWINDOW_ZONE_FULL);
    });
    createActionButton('splitstyle, ACTION_VIEW invalid number', function() {
        splitstyleView(10);
    });
    createActionButton('splitstyle, ACTION_VIEW invalid string', function() {
        splitstyleView("invalid");
    });
    
    createActionButton('freestyle, ACTION_VIEW scale 60', function() {
        freestyleView(60);        
    });
    
    createActionButton('freestyle, ACTION_VIEW scale 100', function() {
        freestyleView(100);
    });
    
    createActionButton('freestyle, ACTION_VIEW scale 10', function() {
        freestyleView(10);
    });
    createActionButton('freestyle, ACTION_VIEW scale 1000', function() {
        freestyleView(1000);

    });
    createActionButton('freestyle, ACTION_VIEW scale invalid', function() {
        freestyleView("invalid");
    });

};