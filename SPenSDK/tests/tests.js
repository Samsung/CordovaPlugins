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
exports.defineAutoTests = function () {
    var fail = function (done) {
        expect(true).toBe(false);
        done();
    };
    var pass = function (done) {
        expect(true).toBe(true);
        done();
    };

    describe('SPen isSupported', function () {
        beforeEach(function () {
            jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
        });
        it("isSupported method should exist", function () {
            expect(samsung.spen).toBeDefined();
            expect(samsung.spen.isSupported).toBeDefined();
            expect(typeof samsung.spen.isSupported).toBe('function');
        });

        it("SPEN_AND_HAND_SUPPORTED or ONLY_HAND_SUPPORTED",
                function (done) {
                    var successSpen = function (msg) {
                        //console.log("spen isSupported success check: " + msg);
                        if (msg === "SPEN_AND_HAND_SUPPORTED") {
                            pass(done);
                        } else if (msg === "ONLY_HAND_SUPPORTED") {
                            pass(done);
                        } else {
                            fail(done);
                        }
                    }

                    var failureSpen = function (msg) {
                        //console.log("spen isSupported fail check: " + msg);
                        if (msg === "VENDOR_NOT_SUPPORTED") {
                            pass(done);
                        } else if (msg === "DEVICE_NOT_SUPPORTED") {
                            pass(done);
                        } else if (msg === "LIBRARY_NOT_INSTALLED") {
                            pass(done);
                        }  else if (msg === "LIBRARY_UPDATE_IS_REQUIRED") {
                            pass(done);
                        }  else if (msg === "LIBRARY_UPDATE_IS_RECOMMENDED") {
                            pass(done);
                        } else {
                            fail(done);
                        }
                    }
                    samsung.spen.isSupported(successSpen,
                            failureSpen);
                });
    });

    describe('Spen createSurfacePopup', function () {
        beforeEach(function () {
            jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
        });
        it('method should exist', function () {
            expect(samsung.spen).toBeDefined();
            expect(samsung.spen.launchSurfacePopup).toBeDefined();
            expect(typeof samsung.spen.launchSurfacePopup).toBe('function');
        });

        //empty surface id case
        it("throws INVALID_SURFACE_ID", function (done) {
            var successSpen = function () {
                fail(done);
            }

            var failureSpen = function (msg) {
                if (msg === "INVALID_SURFACE_ID") {
                    //console.log("surface id is empty: " + msg);
                    pass(done);
                } else {
                    fail(done);
                }
            }
            var popupOptions = {};
            var flagPopup = 0;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN;
            flagPopup = flagPopup | samsung.spen.FLAG_ERASER;
            flagPopup = flagPopup | samsung.spen.FLAG_UNDO_REDO;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN_SETTINGS;
            flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
            flagPopup = flagPopup | samsung.spen.FLAG_EDIT;
            flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_SHAPE;
            flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_TEXT;
            popupOptions.id = "  ";
            popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_URI;
            popupOptions.sPenFlags = flagPopup;
            popupOptions.backgroundColor = "#FFF333";
            popupOptions.imageUri = "";
            samsung.spen.removeSurfacePopup("popupId", function () {
            }, function () {
            });
            samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
        });
        //id more than 100 chars
        it("id length greater than 100 chars", function (done) {
            var successSpen = function () {
            };

            var failureSpen = function (msg) {
                fail(done);
            };
            var popupOptions = {};
            popupOptions.id = "com.samsung.android.sdk.pen.document.SpenObject" +
                    "Stroke@1,com.samsung.android.sdk.pen.document.SpenObjectStroke@2";
            var successSpenMain = function (data) {
                if (data === popupOptions.id.substr(0, 100)) {
                    pass(done);
                }
                else {
                    fail(done);
                }
            };

            var failureSpenMain = function (msg) {
                fail(done);
            };
            var flagPopup = 0;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN;
            flagPopup = flagPopup | samsung.spen.FLAG_ERASER;
            flagPopup = flagPopup | samsung.spen.FLAG_UNDO_REDO;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN_SETTINGS;
            flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
            flagPopup = flagPopup | samsung.spen.FLAG_EDIT;
            flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_SHAPE;
            popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_URI;
            popupOptions.sPenFlags = flagPopup;
            popupOptions.backgroundColor = "#FFF333";
            popupOptions.imageUri = "";
            samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
            samsung.spen.removeSurfacePopup(popupOptions.id, successSpenMain, failureSpenMain);

        });
        
        // width and height are negative
        
        it("width and height are negative ", function (done) {
            var successSpen = function () {
            pass(done);
            samsung.spen.removeSurfacePopup("popupId", function () {
            }, function () {
            });
            };

            var failureSpen = function (msg) {
                fail(done);
            };
            var popupOptions = {};
           
            popupOptions.width = -400;
            popupOptions.height = -400;
            var flagPopup = 0;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN;
            flagPopup = flagPopup | samsung.spen.FLAG_ERASER;
            flagPopup = flagPopup | samsung.spen.FLAG_UNDO_REDO;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN_SETTINGS;
            flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
            flagPopup = flagPopup | samsung.spen.FLAG_EDIT;
            flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_SHAPE;
            popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_URI;
            popupOptions.sPenFlags = flagPopup;
            popupOptions.id = "popupId";
            popupOptions.backgroundColor = "#FFF333";
            popupOptions.imageUri = "";
            samsung.spen.removeSurfacePopup("popupId", function () {
            }, function () {
            });
            samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
            
        });
        
        // width and height are strings
        
        it("width and height are strings ", function (done) {
            var successSpen = function () {
        pass(done);
        samsung.spen.removeSurfacePopup("popupId", function () {
            }, function () {
            });
            };

            var failureSpen = function (msg) {
                fail(done);
            };
            var popupOptions = {};
           
            popupOptions.width = 'abc';
            popupOptions.height = 'xyz';
            var flagPopup = 0;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN;
            flagPopup = flagPopup | samsung.spen.FLAG_ERASER;
            flagPopup = flagPopup | samsung.spen.FLAG_UNDO_REDO;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN_SETTINGS;
            flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
            flagPopup = flagPopup | samsung.spen.FLAG_EDIT;
            flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_SHAPE;
            popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_URI;
            popupOptions.sPenFlags = flagPopup;
            popupOptions.id = "popupId";
            popupOptions.backgroundColor = "#FFF333";
            popupOptions.imageUri = "";
            samsung.spen.removeSurfacePopup("popupId", function () {
            }, function () {
            });
            samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
        });
        
        it("width and height are large numbers ", function (done) {
            var successSpen = function () {
        pass(done);
        samsung.spen.removeSurfacePopup("popupId", function () {
                 }, function () {
                 });
            };

            var failureSpen = function (msg) {
                fail(done);
            };
            var popupOptions = {};
           
            popupOptions.width = 100000;
            popupOptions.height = 100000;
            var flagPopup = 0;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN;
            flagPopup = flagPopup | samsung.spen.FLAG_ERASER;
            flagPopup = flagPopup | samsung.spen.FLAG_UNDO_REDO;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN_SETTINGS;
            flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
            flagPopup = flagPopup | samsung.spen.FLAG_EDIT;
            flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_SHAPE;
            popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_URI;
            popupOptions.sPenFlags = flagPopup;
            popupOptions.id = "popupId";
            popupOptions.backgroundColor = "#FFF333";
            popupOptions.imageUri = "";
            samsung.spen.removeSurfacePopup("popupId", function () {
            }, function () {
            });
            samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
           
        });
        
        it("width and height are decimal numbers ", function (done) {
            var successSpen = function () {
            	pass(done);
            	 samsung.spen.removeSurfacePopup("popupId", function () {
                 }, function () {
                 });
            };

            var failureSpen = function (msg) {
                fail(done);
            };
            var popupOptions = {};
           
            popupOptions.width = 10.90;
            popupOptions.height = 230.95;
            var flagPopup = 0;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN;
            flagPopup = flagPopup | samsung.spen.FLAG_ERASER;
            flagPopup = flagPopup | samsung.spen.FLAG_UNDO_REDO;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN_SETTINGS;
            flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
            flagPopup = flagPopup | samsung.spen.FLAG_EDIT;
            flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_SHAPE;
            popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_URI;
            popupOptions.sPenFlags = flagPopup;
            popupOptions.id = "popupId";
            popupOptions.backgroundColor = "#FFF333";
            popupOptions.imageUri = "";
            samsung.spen.removeSurfacePopup("popupId", function () {
            }, function () {
            });
            samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
           
        });
        
        //background image scale type is CENTER
        
        it("background image scale type is CENTER", function (done) {
            var successSpen = function () {
        pass(done);
        samsung.spen.removeSurfacePopup("popupId", function () {
                 }, function () {
                 });
            };

            var failureSpen = function (msg) {
                fail(done);
            };
            var popupOptions = {};
           
            var flagPopup = 0;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN;
            flagPopup = flagPopup | samsung.spen.FLAG_ERASER;
            flagPopup = flagPopup | samsung.spen.FLAG_UNDO_REDO;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN_SETTINGS;
            flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
            flagPopup = flagPopup | samsung.spen.FLAG_EDIT;
            flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_SHAPE;
            popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_URI;
            popupOptions.sPenFlags = flagPopup;
            popupOptions.id = "popupId";
            popupOptions.backgroundColor = "#FFF333";
            popupOptions.bgImageScaleType = samsung.spen.BACKGROUND_IMAGE_MODE_CENTER;
            popupOptions.imageUri = "";
            samsung.spen.removeSurfacePopup("popupId", function () {
            }, function () {
            });
            samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
        });
        
        //background image scale type is FIT
        
        it("background image scale type is FIT", function (done) {
            var successSpen = function () {
            pass(done);
            samsung.spen.removeSurfacePopup("popupId", function () {
                 }, function () {
                 });
            };

            var failureSpen = function (msg) {
                fail(done);
            };
            var popupOptions = {};
           
            var flagPopup = 0;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN;
            flagPopup = flagPopup | samsung.spen.FLAG_ERASER;
            flagPopup = flagPopup | samsung.spen.FLAG_UNDO_REDO;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN_SETTINGS;
            flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
            flagPopup = flagPopup | samsung.spen.FLAG_EDIT;
            flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_SHAPE;
            popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_URI;
            popupOptions.sPenFlags = flagPopup;
            popupOptions.id = "popupId";
            popupOptions.backgroundColor = "#FFF333";
            popupOptions.bgImageScaleType = samsung.spen.BACKGROUND_IMAGE_MODE_FIT;
            popupOptions.imageUri = "";
            samsung.spen.removeSurfacePopup("popupId", function () {
            }, function () {
            });
            samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
        });
        
        //background image scale type is STRETCH
        
        it("background image scale type is STRETCH", function (done) {
            var successSpen = function () {
            pass(done);
            samsung.spen.removeSurfacePopup("popupId", function () {
                 }, function () {
                 });
            };

            var failureSpen = function (msg) {
                fail(done);
            };
            var popupOptions = {};
           
            var flagPopup = 0;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN;
            flagPopup = flagPopup | samsung.spen.FLAG_ERASER;
            flagPopup = flagPopup | samsung.spen.FLAG_UNDO_REDO;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN_SETTINGS;
            flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
            flagPopup = flagPopup | samsung.spen.FLAG_EDIT;
            flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_SHAPE;
            popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_URI;
            popupOptions.sPenFlags = flagPopup;
            popupOptions.id = "popupId";
            popupOptions.backgroundColor = "#FFF333";
            popupOptions.bgImageScaleType = samsung.spen.BACKGROUND_IMAGE_MODE_STRETCH;
            popupOptions.imageUri = "";
            samsung.spen.removeSurfacePopup("popupId", function () {
            }, function () {
            });
            samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
        });
        
        //background image scale type is STRETCH
        
        it("background image scale type is TILE", function (done) {
            var successSpen = function () {
            pass(done);
            samsung.spen.removeSurfacePopup("popupId", function () {
                 }, function () {
                 });
            };

            var failureSpen = function (msg) {
                fail(done);
            };
            var popupOptions = {};
           
            var flagPopup = 0;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN;
            flagPopup = flagPopup | samsung.spen.FLAG_ERASER;
            flagPopup = flagPopup | samsung.spen.FLAG_UNDO_REDO;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN_SETTINGS;
            flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
            flagPopup = flagPopup | samsung.spen.FLAG_EDIT;
            flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_SHAPE;
            popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_URI;
            popupOptions.sPenFlags = flagPopup;
            popupOptions.id = "popupId";
            popupOptions.backgroundColor = "#FFF333";
            popupOptions.bgImageScaleType = samsung.spen.BACKGROUND_IMAGE_MODE_TILE;
            popupOptions.imageUri = "";
            samsung.spen.removeSurfacePopup("popupId", function () {
            }, function () {
            });
            samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
        });

        //INVALID_RETURN_TYPE case
        it("throws INVALID_RETURN_TYPE", function (done) {
            var successSpen = function () {
                fail(done);
            }

            var failureSpen = function (msg) {
                if (msg === "INVALID_RETURN_TYPE") {
                    //console.log("return type is invalid: " + msg);
                    pass(done);
                } else {
                    fail(done);
                }
            }
            var popupOptions = {};
            var flagPopup = 0;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN;
            flagPopup = flagPopup | samsung.spen.FLAG_ERASER;
            flagPopup = flagPopup | samsung.spen.FLAG_UNDO_REDO;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN_SETTINGS;
            flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
            flagPopup = flagPopup | samsung.spen.FLAG_EDIT;
            flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_SHAPE;
            popupOptions.id = "popupId";
            popupOptions.returnType = "s";
            popupOptions.sPenFlags = flagPopup;
            popupOptions.backgroundColor = "#FFF333";
            popupOptions.imageUri = "";
            samsung.spen.removeSurfacePopup("popupId", function () {
            }, function () {
            });
            samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);

        });

        //INVALID_FLAGS case
        it("throws INVALID_FLAGS", function (done) {
            var successSpen = function () {
                fail(done);
            }

            var failureSpen = function (msg) {
                if (msg === "INVALID_FLAGS") {
                    //console.log("return type is invalid: " + msg);
                    pass(done);
                } else {
                    fail(done);
                }
            }
            var popupOptions = {};
            var flagPopup = -1;
            popupOptions.id = "popupId";
            popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_DATA;
            popupOptions.sPenFlags = flagPopup;
            popupOptions.backgroundColor = "#FFF333";
            popupOptions.imageUri = "";
            samsung.spen.removeSurfacePopup("popupId", function () {
            }, function () {
            });
            samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);

        });

        it("throws MAX_SURFACE_LIMIT_REACHED", function (done) {

            var successSpen = function () {
            };
            var successSpenMain = function () {
                fail(done);
            };

            var failureSpen = function (msg) {
                console.log("error launching popup : " + msg);
                fail(done);
            };
            var failureSpenMain = function (msg) {
                if (msg === "MAX_SURFACE_LIMIT_REACHED") {
                    pass(done);
                    samsung.spen.removeSurfacePopup("popupId1", function () {
                    }, function () {
                    });
                    samsung.spen.removeSurfacePopup("popupId2", function () {
                    }, function () {
                    });
                    samsung.spen.removeSurfacePopup("popupId3", function () {
                    }, function () {
                    });
                    samsung.spen.removeSurfacePopup("popupId4", function () {
                    }, function () {
                    });
                    samsung.spen.removeSurfacePopup("popupId5", function () {
                    }, function () {
                    });
                } else {
                    fail(done);
                }
            };
            var popupOptions = {};
            var flagPopup = 0;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN;
            flagPopup = flagPopup | samsung.spen.FLAG_ERASER;
            flagPopup = flagPopup | samsung.spen.FLAG_UNDO_REDO;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN_SETTINGS;
            flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
            flagPopup = flagPopup | samsung.spen.FLAG_EDIT;
            flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_SHAPE;
            popupOptions.id = "popupId1";
            popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_DATA;
            popupOptions.sPenFlags = flagPopup;
            popupOptions.backgroundColor = "FFF333";
            popupOptions.imageUri = "";
            samsung.spen.removeSurfacePopup("popupId", function () {
            }, function () {
            });
            samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
            popupOptions.id = "popupId2";
            samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
            popupOptions.id = "popupId3";
            samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
            popupOptions.id = "popupId4";
            samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
            popupOptions.id = "popupId5";
            samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
            popupOptions.id = "popupId6";
            samsung.spen.launchSurfacePopup(popupOptions, successSpenMain, failureSpenMain);

        });
    });

    describe('Spen removeSurfacePopup', function () {
        beforeEach(function () {
            jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
        });
        it('method should exist', function () {
            expect(samsung.spen).toBeDefined();
            expect(samsung.spen.removeSurfacePopup).toBeDefined();
            expect(typeof samsung.spen.removeSurfacePopup).toBe('function');
        });

        it('throws INVALID_SURFACE_ID', function (done) {
            var successSpen = function () {
                fail(done);
            }
            var failureSpen = function (msg) {
                if (msg === "INVALID_SURFACE_ID") {
                    //console.log("surface id is empty: " + msg);
                    pass(done);
                } else {
                    fail(done);
                }
            }
            var popupId = " ";
            samsung.spen.removeSurfacePopup(popupId, successSpen, failureSpen);
        });

        it('throws SURFACE_ID_NOT_EXISTS', function (done) {
            var successSpen = function () {
                fail(done);
            }
            var failureSpen = function (msg) {
                if (msg === "SURFACE_ID_NOT_EXISTS") {
                    //console.log("surface id not exists: " + msg);
                    pass(done);
                } else {
                    fail(done);
                }
            }
            var popupId = "popupId";
            samsung.spen.removeSurfacePopup(popupId, successSpen, failureSpen);
        });

    });
    describe('Spen createSurfaceInline', function () {
        beforeEach(function () {
            jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
        });
        it('method should exist', function () {
            expect(samsung.spen).toBeDefined();
            expect(samsung.spen.launchSurfaceInline).toBeDefined();
            expect(typeof samsung.spen.launchSurfaceInline).toBe('function');
        });

        it("throws INVALID_SURFACE_ID", function (done) {
            var successSpen = function () {
                fail(done);
            };

            var failureSpen = function (msg) {
                if (msg === "INVALID_SURFACE_ID") {
                    //console.log("surface id is empty: " + msg);
                    pass(done);
                } else {
                    fail(done);
                }
            };
            var flagsInline = 0;
            var inlineOptions = {};
            inlineOptions.elementCoordinates = {};
            inlineOptions.bodyRectangleCoordinates = {};
            inlineOptions.elementCoordinates.left = 10;
            inlineOptions.elementCoordinates.top = 400;
            inlineOptions.elementCoordinates.width = 300;
            inlineOptions.elementCoordinates.height = 200;
            inlineOptions.bodyRectangleCoordinates.left = 0;
            inlineOptions.bodyRectangleCoordinates.top = 0;
            inlineOptions.id = "";
            inlineOptions.returnType = 100;
            inlineOptions.backgroundColor = "#FFF333";
            inlineOptions.imageUri = "";
            inlineOptions.sPenFlags = flagsInline;
            samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
        });
        //id more than 100 chars
        it("id length greater than 100 chars", function (done) {
            var successSpen = function () {
            };

            var failureSpen = function (msg) {
                fail(done);
            };
            var inlineOptions = {};
            inlineOptions.id = "com.samsung.android.sdk.pen.document.SpenObject" +
                    "Stroke@1,com.samsung.android.sdk.pen.document.SpenObjectStroke@2";
            var successSpenMain = function (data) {
                if (data === inlineOptions.id.substr(0, 100)) {
                    pass(done);
                }
                else {
                    fail(done);
                }
            };

            var failureSpenMain = function (msg) {
                fail(done);
            };
            var flagsInline = 0;
            inlineOptions.elementCoordinates = {};
            inlineOptions.bodyRectangleCoordinates = {};
            inlineOptions.elementCoordinates.left = 10;
            inlineOptions.elementCoordinates.top = 400;
            inlineOptions.elementCoordinates.width = 300;
            inlineOptions.elementCoordinates.height = 200;
            inlineOptions.bodyRectangleCoordinates.left = 0;
            inlineOptions.bodyRectangleCoordinates.top = 0;
            inlineOptions.returnType = 100;
            inlineOptions.backgroundColor = "#FFF333";
            inlineOptions.imageUri = "";
            inlineOptions.sPenFlags = flagsInline;
            samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
            samsung.spen.removeSurfaceInline(inlineOptions.id, successSpenMain, failureSpenMain);

        });
        it("throws INVALID_RETURN_TYPE", function (done) {
            var successSpen = function () {
                fail(done);
            }

            var failureSpen = function (msg) {
                if (msg === "INVALID_RETURN_TYPE") {
                    // console.log("return type is invalid: " + msg);
                    pass(done);
                } else {
                    fail(done);
                }
            }
            var flagsInline = 0;
            var inlineOptions = {};
            inlineOptions.elementCoordinates = {};
            inlineOptions.bodyRectangleCoordinates = {};
            inlineOptions.elementCoordinates.left = 10;
            inlineOptions.elementCoordinates.top = 400;
            inlineOptions.elementCoordinates.width = 300;
            inlineOptions.elementCoordinates.height = 200;
            inlineOptions.bodyRectangleCoordinates.left = 0;
            inlineOptions.bodyRectangleCoordinates.top = 0;
            inlineOptions.id = "popupId";
            inlineOptions.returnType = 500;
            inlineOptions.backgroundColor = "#FFF333";
            inlineOptions.imageUri = "";
            inlineOptions.sPenFlags = flagsInline;
            samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);

        });
        //INVALID_FLAGS case
        it("throws INVALID_FLAGS", function (done) {
            var successSpen = function () {
                fail(done);
            }

            var failureSpen = function (msg) {
                if (msg === "INVALID_FLAGS") {
                    //console.log("return type is invalid: " + msg);
                    pass(done);
                } else {
                    fail(done);
                }
            }
            var flagsInline = -1;
            var inlineOptions = {};
            inlineOptions.elementCoordinates = {};
            inlineOptions.bodyRectangleCoordinates = {};
            inlineOptions.elementCoordinates.left = 10;
            inlineOptions.elementCoordinates.top = 400;
            inlineOptions.elementCoordinates.width = 300;
            inlineOptions.elementCoordinates.height = 200;
            inlineOptions.bodyRectangleCoordinates.left = 0;
            inlineOptions.bodyRectangleCoordinates.top = 0;
            inlineOptions.id = "popupId";
            inlineOptions.returnType = 100;
            inlineOptions.backgroundColor = "#FFF333";
            inlineOptions.imageUri = "";
            inlineOptions.sPenFlags = flagsInline;
            samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);

        });
        //INVALID_Cordinates case
        it("throws INVALID_INLINE_CORDINATES", function (done) {
            var failCount = 0;
            var successSpen = function () {
                fail(done);
            };

            var failureSpen = function (msg) {
                if (msg === "INVALID_INLINE_CORDINATES") {
                    failCount++;
                } else {
                    fail(done);
                }
            };
            var failureSpen1 = function (msg) {
                if (msg === "INVALID_INLINE_CORDINATES") {
                    failCount++;
                    if (failCount === 6) {
                        //console.log("cordinates are invalid: " + msg);
                        pass(done);
                    } else {
                        fail(done);
                    }
                } else {
                    fail(done);
                }
            };
            var flagsInline = 0;
            var inlineOptions = {};
            inlineOptions.elementCoordinates = {};
            inlineOptions.bodyRectangleCoordinates = {};
            inlineOptions.elementCoordinates.left = 1000000000000000000;
            inlineOptions.elementCoordinates.top = 400;
            inlineOptions.elementCoordinates.width = 300;
            inlineOptions.elementCoordinates.height = 200;
            inlineOptions.bodyRectangleCoordinates.left = 0;
            inlineOptions.bodyRectangleCoordinates.top = 0;
            inlineOptions.id = "popupId";
            inlineOptions.returnType = 101;
            inlineOptions.backgroundColor = "#FFF333";
            inlineOptions.imageUri = "";
            inlineOptions.sPenFlags = flagsInline;
            samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
            inlineOptions.elementCoordinates.left = 10;
            inlineOptions.elementCoordinates.top = 4000000000000000000;
            samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
            inlineOptions.elementCoordinates.top = 400;
            inlineOptions.elementCoordinates.width = 3000000000000000000;
            samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
            inlineOptions.elementCoordinates.width = 300;
            inlineOptions.elementCoordinates.height = 2000000000000000000;
            samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
            inlineOptions.elementCoordinates.height = 200;
            inlineOptions.bodyRectangleCoordinates.left = 2000000000000000000;
            samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
            inlineOptions.bodyRectangleCoordinates.left = 0;
            inlineOptions.bodyRectangleCoordinates.top = 2000000000000000000;
            samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen1);
        });
        it("throws SURFACE_ID_ALREADY_EXISTS", function (done) {

            var successSpen = function () {
            };
            var failureSpen = function (msg) {
                fail(done);
            }
            var successSpenMain = function () {
                fail(done);
            };

            var failureSpenMain = function (msg) {
                if (msg === "SURFACE_ID_ALREADY_EXISTS") {
                    //console.log("return type is invalid: " + msg);
                    samsung.spen.removeSurfaceInline("popupId", function () {
                    }, function () {
                    });
                    pass(done);
                } else {
                    fail(done);
                }
            };
            var flagsInline = 0;
            var inlineOptions = {};
            inlineOptions.elementCoordinates = {};
            inlineOptions.bodyRectangleCoordinates = {};
            inlineOptions.elementCoordinates.left = 10;
            inlineOptions.elementCoordinates.top = 400;
            inlineOptions.elementCoordinates.width = 300;
            inlineOptions.elementCoordinates.height = 200;
            inlineOptions.bodyRectangleCoordinates.left = 0;
            inlineOptions.bodyRectangleCoordinates.top = 0;
            inlineOptions.id = "popupId";
            inlineOptions.returnType = 100;
            inlineOptions.backgroundColor = "#FFF333";
            inlineOptions.imageUri = "";
            inlineOptions.sPenFlags = flagsInline;
            samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
            samsung.spen.launchSurfaceInline(inlineOptions, successSpenMain, failureSpenMain);

        });
        it("throws MAX_SURFACE_LIMIT_REACHED", function (done) {
            var successSpen = function () {
            };
            var successSpenMain = function () {
                console.log("error, launched more surface than max limit");
                fail(done);
            };

            var failureSpen = function (msg) {
                console.log("error launching popup : " + msg);
                fail(done);
            };
            var failureSpenMain = function (msg) {
                if (msg === "MAX_SURFACE_LIMIT_REACHED") {
                    pass(done);
                    samsung.spen.removeSurfaceInline("popupId1", function () {
                    }, function () {
                    });
                    samsung.spen.removeSurfaceInline("popupId2", function () {
                    }, function () {
                    });
                    samsung.spen.removeSurfaceInline("popupId3", function () {
                    }, function () {
                    });
                    samsung.spen.removeSurfaceInline("popupId4", function () {
                    }, function () {
                    });
                    samsung.spen.removeSurfaceInline("popupId5", function () {
                    }, function () {
                    });
                } else {
                    fail(done);
                }
            };
            var flagsInline = 0;
            var inlineOptions = {};
            inlineOptions.elementCoordinates = {};
            inlineOptions.bodyRectangleCoordinates = {};
            inlineOptions.elementCoordinates.left = 10;
            inlineOptions.elementCoordinates.top = 400;
            inlineOptions.elementCoordinates.width = 300;
            inlineOptions.elementCoordinates.height = 200;
            inlineOptions.bodyRectangleCoordinates.left = 0;
            inlineOptions.bodyRectangleCoordinates.top = 0;
            inlineOptions.id = "popupId1";
            inlineOptions.returnType = 100;
            inlineOptions.backgroundColor = "#FFF333";
            inlineOptions.imageUri = "";
            inlineOptions.sPenFlags = flagsInline;
            samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
            inlineOptions.id = "popupId2";
            samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
            inlineOptions.id = "popupId3";
            samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
            inlineOptions.id = "popupId4";
            samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
            inlineOptions.id = "popupId5";
            samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
            inlineOptions.id = "popupId6";
            samsung.spen.launchSurfaceInline(inlineOptions, successSpenMain, failureSpenMain);

        });
    });
    describe('Spen removeSurfaceInline', function () {
        beforeEach(function () {
            jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
        });
        it('method should exist', function () {
            expect(samsung.spen).toBeDefined();
            expect(samsung.spen.removeSurfaceInline).toBeDefined();
            expect(typeof samsung.spen.removeSurfaceInline).toBe('function');
        });

        it('throws INVALID_SURFACE_ID', function (done) {
            var successSpen = function () {
                fail(done);
            }
            var failureSpen = function (msg) {
                if (msg === "INVALID_SURFACE_ID") {
                    //console.log("surface id is empty: " + msg);
                    pass(done);
                } else {
                    fail(done);
                }
            }
            var popupId = " ";
            samsung.spen.removeSurfaceInline(popupId, successSpen, failureSpen);
        });

        it('throws SURFACE_ID_NOT_EXISTS', function (done) {
            var successSpen = function () {
                fail(done);
            }
            var failureSpen = function (msg) {
                if (msg === "SURFACE_ID_NOT_EXISTS") {
                    //console.log("surface id not exists: " + msg);
                    pass(done);
                } else {
                    fail(done);
                }
            }
            var popupId = "popupId";
            samsung.spen.removeSurfaceInline(popupId, successSpen, failureSpen);
        });

    });
    describe('Spen popup and Inline combined tests', function () {
        beforeEach(function () {
            jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
        });
        it("throws MAX_SURFACE_LIMIT_REACHED", function (done) {
            var successSpen = function () {
            };
            var successSpenMain = function () {
                fail(done);
            };

            var failureSpen = function (msg) {
                console.log("error launching surfaces : " + msg);
                fail(done);
            };
            var failureSpenMain = function (msg) {
                if (msg === "MAX_SURFACE_LIMIT_REACHED") {
                    pass(done);
                    samsung.spen.removeSurfacePopup("popupId1", function () {
                    }, function () {
                    });
                    samsung.spen.removeSurfacePopup("popupId2", function () {
                    }, function () {
                    });
                    samsung.spen.removeSurfacePopup("popupId3", function () {
                    }, function () {
                    });
                    samsung.spen.removeSurfaceInline("popupId4", function () {
                    }, function () {
                    });
                    samsung.spen.removeSurfaceInline("popupId5", function () {
                    }, function () {
                    });
                } else {
                    fail(done);
                }
            };
            var popupOptions = {};
            var flagPopup = 0;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN;
            flagPopup = flagPopup | samsung.spen.FLAG_ERASER;
            flagPopup = flagPopup | samsung.spen.FLAG_UNDO_REDO;
            flagPopup = flagPopup | samsung.spen.FLAG_PEN_SETTINGS;
            flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
            flagPopup = flagPopup | samsung.spen.FLAG_EDIT;
            flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_SHAPE;
            popupOptions.id = "popupId1";
            popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_DATA;
            popupOptions.sPenFlags = flagPopup;
            popupOptions.backgroundColor = "#FFF333";
            popupOptions.imageUri = "";
            samsung.spen.removeSurfacePopup("popupId", function () {
            }, function () {
            });
            samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
            popupOptions.id = "popupId2";
            samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
            popupOptions.id = "popupId3";
            samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);

            var flagsInline = 0;
            var inlineOptions = {};
            inlineOptions.elementCoordinates = {};
            inlineOptions.bodyRectangleCoordinates = {};
            inlineOptions.elementCoordinates.left = 10;
            inlineOptions.elementCoordinates.top = 400;
            inlineOptions.elementCoordinates.width = 300;
            inlineOptions.elementCoordinates.height = 200;
            inlineOptions.bodyRectangleCoordinates.left = 0;
            inlineOptions.bodyRectangleCoordinates.top = 0;
            inlineOptions.id = "popupId4";
            inlineOptions.returnType = 100;
            inlineOptions.backgroundColor = "#FFF333";
            inlineOptions.imageUri = "";
            inlineOptions.sPenFlags = flagsInline;
            samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
            inlineOptions.id = "popupId5";
            samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
            inlineOptions.id = "popupId6";
            samsung.spen.launchSurfaceInline(inlineOptions, successSpenMain, failureSpenMain);

        });
    });
};

exports.defineManualTests = function (contentEl, launchPopupSurface) {

    launchPopupSurface('Launch Popup: default flags', function () {
        contentEl.innerHTML = '</br></br></br>';
        var successSpen = function (data) {
            contentEl.innerHTML = '<div class = nontranslate>' +
                    '<img id="imageData" height="400" width = "300"/>' +
                    '</div>';
            if (data !== "") {
                document.getElementById("imageData").src = "data:image/jpg;base64," + data;
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg);
            console.log("error launching spen: " + msg);
        };
        var popupOptions = {};
        var flags = 0;
        popupOptions.id = "popupId";
        popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_DATA;
        popupOptions.backgroundColor = "#FFF333";
        popupOptions.imageUri = "";
        popupOptions.sPenFlags = flags;
        samsung.spen.removeSurfacePopup("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
    });

    launchPopupSurface('Launch Popup: default + only Pen Settings', function () {
        contentEl.innerHTML = '</br></br></br>';
        var successSpen = function (data) {
            contentEl.innerHTML = '<div class = nontranslate>' +
                    '<img id="imageData" height="400" width = "300"/>' +
                    '</div>';
            if (data !== "") {
                document.getElementById("imageData").src = "data:image/jpg;base64," + data;
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg)
            console.log("error launching spen: " + msg);
        };
        var popupOptions = {};
        var flagPopup = 0;
        flagPopup = flagPopup | samsung.spen.FLAG_PEN_SETTINGS;
        popupOptions.id = "popupId";
        popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_DATA;
        popupOptions.backgroundColor = "#FFF333";
        popupOptions.imageUri = "";
        popupOptions.sPenFlags = flagPopup;
        samsung.spen.removeSurfacePopup("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
    });
    launchPopupSurface('Launch Popup: default + only background', function () {
        contentEl.innerHTML = '</br></br></br>';
        var successSpen = function (data) {
            contentEl.innerHTML = '<div class = nontranslate>' +
                    '<img id="imageData" height="400" width = "300"/>' +
                    '</div>';
            if (data !== "") {
                document.getElementById("imageData").src = "data:image/jpg;base64," + data;
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg)
            console.log("error launching spen: " + msg);
        };
        var popupOptions = {};
        var flagPopup = 0;
        flagPopup = flagPopup | samsung.spen.FLAG_BACKGROUND;
        popupOptions.id = "popupId";
        popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_DATA;
        popupOptions.backgroundColor = "#FFF333";
        popupOptions.imageUri = "";
        popupOptions.sPenFlags = flagPopup;
        samsung.spen.removeSurfacePopup("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
    });
    launchPopupSurface('Launch Popup: default + only selection', function () {
        contentEl.innerHTML = '</br></br></br>';
        var successSpen = function (data) {
            contentEl.innerHTML = '<div class = nontranslate>' +
                    '<img id="imageData" height="400" width = "300"/>' +
                    '</div>';
            if (data !== "") {
                document.getElementById("imageData").src = "data:image/jpg;base64," + data;
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg)
            console.log("error launching spen: " + msg);
        };
        var popupOptions = {};
        var flagPopup = 0;
        flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
        popupOptions.id = "popupId";
        popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_DATA;
        popupOptions.backgroundColor = "#FFF333";
        popupOptions.imageUri = "";
        popupOptions.sPenFlags = flagPopup;
        samsung.spen.removeSurfacePopup("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
    });
    launchPopupSurface('Launch Popup: default + only recognize text', function () {
        contentEl.innerHTML = '</br></br></br>';
        var successSpen = function (data) {
            contentEl.innerHTML = '<div class = nontranslate>' +
                    '<img id="imageData" height="400" width = "300"/>' +
                    '</div>';
            if (data !== "") {
                document.getElementById("imageData").src = "data:image/jpg;base64," + data;
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg)
            console.log("error launching spen: " + msg);
        };
        var popupOptions = {};
        var flagPopup = 0;
        flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
        popupOptions.id = "popupId";
        popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_DATA;
        popupOptions.backgroundColor = "#FFF333";
        popupOptions.imageUri = "";
        popupOptions.sPenFlags = flagPopup;
        samsung.spen.removeSurfacePopup("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
    });
    launchPopupSurface('Launch Popup: default + only recognize shape', function () {
        contentEl.innerHTML = '</br></br></br>';
        var successSpen = function (data) {
            contentEl.innerHTML = '<div class = nontranslate>' +
                    '<img id="imageData" height="400" width = "300"/>' +
                    '</div>';
            if (data !== "") {
                document.getElementById("imageData").src = "data:image/jpg;base64," + data;
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg)
            console.log("error launching spen: " + msg);
        };
        var popupOptions = {};
        var flagPopup = 0;
        flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
        flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_SHAPE;
        popupOptions.id = "popupId";
        popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_DATA;
        popupOptions.backgroundColor = "#FFF333";
        popupOptions.imageUri = "";
        popupOptions.sPenFlags = flagPopup;
        samsung.spen.removeSurfacePopup("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
    });

    launchPopupSurface('Launch Popup: all flags', function () {
        contentEl.innerHTML = '</br></br></br>';
        var successSpen = function (data) {
            contentEl.innerHTML = '<div class = nontranslate>' +
                    '<img id="imageData" height="400" width = "300"/>' +
                    '</div>';
            if (data !== "") {
                document.getElementById("imageData").src = "data:image/jpg;base64," + data;
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg);
            console.log("error launching spen: " + msg);
        };
        var popupOptions = {};
        var flagPopup = 0;
        flagPopup = flagPopup | samsung.spen.FLAG_PEN_SETTINGS;
        flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
        flagPopup = flagPopup | samsung.spen.FLAG_BACKGROUND;
        flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_SHAPE;
        popupOptions.id = "popupId";
        popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_DATA;
        popupOptions.backgroundColor = "#FFF333";
        popupOptions.imageUri = "";
        popupOptions.sPenFlags = flagPopup;
        samsung.spen.removeSurfacePopup("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
    });

    launchPopupSurface('Launch Popup: all flags + color "xyz"', function () {
        contentEl.innerHTML = '</br></br></br>';
        var successSpen = function (data) {
            contentEl.innerHTML = '<div class = nontranslate>' +
                    '<img id="imageData" height="400" width = "300"/>' +
                    '</div>';
            if (data !== "") {
                document.getElementById("imageData").src = "data:image/jpg;base64," + data;
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg);
            console.log("error launching spen: " + msg);
        };
        var popupOptions = {};
        var flagPopup = 0;
        flagPopup = flagPopup | samsung.spen.FLAG_PEN_SETTINGS;
        flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
        flagPopup = flagPopup | samsung.spen.FLAG_BACKGROUND;
        flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_SHAPE;
        popupOptions.id = "popupId";
        popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_DATA;
        popupOptions.backgroundColor = "xyz";
        popupOptions.imageUri = "";
        popupOptions.sPenFlags = flagPopup;
        samsung.spen.removeSurfacePopup("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
    });
    launchPopupSurface('Launch Popup: all flags + Open imageUri', function () {
        contentEl.innerHTML = '</br></br></br>';
        var successSpen = function (data) {
            contentEl.innerHTML = '<div class = nontranslate>' +
                    '<img id="imageData" height="400" width = "300"/>' +
                    '</div>';
            if (data !== "") {
                document.getElementById("imageData").src = "data:image/jpg;base64," + data;
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg)
            console.log("error launching spen: " + msg);
        }
        var popupOptions = {};
        var flagPopup = 0;
        flagPopup = flagPopup | samsung.spen.FLAG_PEN_SETTINGS;
        flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
        flagPopup = flagPopup | samsung.spen.FLAG_BACKGROUND;
        flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_SHAPE;
        popupOptions.id = "popupId";
        popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_DATA;
        popupOptions.backgroundColor = "#FFF333";
        popupOptions.imageUri = "file:///data/data/com.samsung.cordova.demo/files/popupId.png";
        popupOptions.sPenFlags = flagPopup;
        samsung.spen.removeSurfacePopup("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
    });
    launchPopupSurface('Launch Popup: all flags + Open imageUri with scale type as center', function () {
        contentEl.innerHTML = '</br></br></br>';
        var successSpen = function (data) {
            contentEl.innerHTML = '<div class = nontranslate>' +
                    '<img id="imageData" height="400" width = "300"/>' +
                    '</div>';
            if (data !== "") {
                document.getElementById("imageData").src = "data:image/jpg;base64," + data;
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg)
            console.log("error launching spen: " + msg);
        }
        var popupOptions = {};
        var flagPopup = 0;
        flagPopup = flagPopup | samsung.spen.FLAG_PEN_SETTINGS;
        flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
        flagPopup = flagPopup | samsung.spen.FLAG_BACKGROUND;
        flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_SHAPE;
        popupOptions.id = "popupId";
        popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_DATA;
        popupOptions.backgroundColor = "#FFF333";
        popupOptions.imageUri = "file:///data/data/com.samsung.cordova.demo/files/popupId.png";
        popupOptions.imageUriScaleType = samsung.spen.IMAGE_URI_MODE_CENTER;
        popupOptions.sPenFlags = flagPopup;
        samsung.spen.removeSurfacePopup("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
    });
    
    launchPopupSurface('Launch Popup: all flags + Open imageUri with scale type as fit', function () {
        contentEl.innerHTML = '</br></br></br>';
        var successSpen = function (data) {
            contentEl.innerHTML = '<div class = nontranslate>' +
                    '<img id="imageData" height="400" width = "300"/>' +
                    '</div>';
            if (data !== "") {
                document.getElementById("imageData").src = "data:image/jpg;base64," + data;
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg)
            console.log("error launching spen: " + msg);
        }
        var popupOptions = {};
        var flagPopup = 0;
        flagPopup = flagPopup | samsung.spen.FLAG_PEN_SETTINGS;
        flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
        flagPopup = flagPopup | samsung.spen.FLAG_BACKGROUND;
        flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_SHAPE;
        popupOptions.id = "popupId";
        popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_DATA;
        popupOptions.backgroundColor = "#FFF333";
        popupOptions.imageUri = "file:///data/data/com.samsung.cordova.demo/files/popupId.png";
        popupOptions.imageUriScaleType = samsung.spen.IMAGE_URI_MODE_FIT;
        popupOptions.sPenFlags = flagPopup;
        samsung.spen.removeSurfacePopup("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
    });
    
    launchPopupSurface('Launch Popup: all flags + Open imageUri with scale type as stretch', function () {
        contentEl.innerHTML = '</br></br></br>';
        var successSpen = function (data) {
            contentEl.innerHTML = '<div class = nontranslate>' +
                    '<img id="imageData" height="400" width = "300"/>' +
                    '</div>';
            if (data !== "") {
                document.getElementById("imageData").src = "data:image/jpg;base64," + data;
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg)
            console.log("error launching spen: " + msg);
        }
        var popupOptions = {};
        var flagPopup = 0;
        flagPopup = flagPopup | samsung.spen.FLAG_PEN_SETTINGS;
        flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
        flagPopup = flagPopup | samsung.spen.FLAG_BACKGROUND;
        flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_SHAPE;
        popupOptions.id = "popupId";
        popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_DATA;
        popupOptions.backgroundColor = "#FFF333";
        popupOptions.imageUri = "file:///data/data/com.samsung.cordova.demo/files/popupId.png";
		popupOptions.imageUriScaleType = samsung.spen.IMAGE_URI_MODE_STRETCH;
		popupOptions.sPenFlags = flagPopup;
        samsung.spen.removeSurfacePopup("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
    });
    
    launchPopupSurface('Launch Popup: all flags + Open imageUri with scale type as tile', function () {
        contentEl.innerHTML = '</br></br></br>';
        var successSpen = function (data) {
            contentEl.innerHTML = '<div class = nontranslate>' +
                    '<img id="imageData" height="400" width = "300"/>' +
                    '</div>';
            if (data !== "") {
                document.getElementById("imageData").src = "data:image/jpg;base64," + data;
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg)
            console.log("error launching spen: " + msg);
        }
        var popupOptions = {};
        var flagPopup = 0;
        flagPopup = flagPopup | samsung.spen.FLAG_PEN_SETTINGS;
        flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
        flagPopup = flagPopup | samsung.spen.FLAG_BACKGROUND;
        flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_SHAPE;
        popupOptions.id = "popupId";
        popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_DATA;
        popupOptions.backgroundColor = "#FFF333";
        popupOptions.imageUri = "file:///data/data/com.samsung.cordova.demo/files/popupId.png";
		popupOptions.imageUriScaleType = samsung.spen.IMAGE_URI_MODE_TILE;
		popupOptions.sPenFlags = flagPopup;
        samsung.spen.removeSurfacePopup("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
    });
    launchPopupSurface('Launch Popup: all flags + Return type Recognize text', function () {
        contentEl.innerHTML = '</br></br></br>';
        var successSpen = function (data) {
            alert(data);
            contentEl.innerHTML = '<div class="ui-block-a">' +
                    '<label for="recognizedText">Recognized Text </label>' +
                    '<input type="text" name="recognizedText" id="recognizedText">' +
                    '</div>';
            document.getElementById("recognizedText").value = data;
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg);
            console.log("error launching spen: " + msg);
        };
        var popupOptions = {};
        var flagPopup = 0;
        popupOptions.id = "popupId";
        flagPopup = flagPopup | samsung.spen.FLAG_PEN_SETTINGS;
        flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
        flagPopup = flagPopup | samsung.spen.FLAG_BACKGROUND;
        flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_SHAPE;
        popupOptions.backgroundColor = "#FFF333";
        popupOptions.imageUri = "";
        popupOptions.sPenFlags = flagPopup;
        samsung.spen.removeSurfacePopup("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
    });

    launchPopupSurface('Launch Popup: all flags + Return type Image Uri', function () {
        contentEl.innerHTML = '</br></br></br>';
        var successSpen = function (data) {
            contentEl.innerHTML = '<div class="ui-block-a">' +
                    '<label for="imageUri">Image Uri </label>' +
                    '<input type="text" name="imageUri" id="imageUri" width = 300>' +
                    '</div>';
            document.getElementById("imageUri").value = data;
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg);
            console.log("error launching spen: " + msg);
        };
        var popupOptions = {};
        var flagPopup = 0;
        popupOptions.id = "popupId";
        flagPopup = flagPopup | samsung.spen.FLAG_PEN_SETTINGS;
        flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
        flagPopup = flagPopup | samsung.spen.FLAG_BACKGROUND;
        flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_SHAPE;
        popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_URI;
        popupOptions.backgroundColor = "#FFF333";
        popupOptions.imageUri = "";
        popupOptions.sPenFlags = flagPopup;
        samsung.spen.removeSurfacePopup("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
    });

    launchPopupSurface('Relaunch Popup: relaunch with all options', function () {
        contentEl.innerHTML = '</br></br></br>';
        var successSpen = function (data) {
            //alert("removed surface :" + data);
            contentEl.innerHTML = '<div class="ui-block-a">' +
                    '<label for="imageUri">Image Uri </label>' +
                    '<input type="text" name="imageUri" id="imageUri" width = 300>' +
                    '</div>';
            document.getElementById("imageUri").value = data;
        };
        var failureSpen = function (msg) {
            //alert("error callback: " + msg);
            console.log("error launching spen: " + msg);
        };
        var popupOptions = {};
        var flagPopup = 0;
        popupOptions.id = "popupId";
        flagPopup = flagPopup | samsung.spen.FLAG_PEN_SETTINGS;
        flagPopup = flagPopup | samsung.spen.FLAG_SELECTION;
        flagPopup = flagPopup | samsung.spen.FLAG_BACKGROUND;
        flagPopup = flagPopup | samsung.spen.FLAG_RECOGNIZE_SHAPE;
        popupOptions.returnType = samsung.spen.RETURN_TYPE_IMAGE_URI;
        popupOptions.backgroundColor = "ffF333";
        popupOptions.imageUri = "";
        popupOptions.sPenFlags = flagPopup;
        samsung.spen.launchSurfacePopup(popupOptions, successSpen, failureSpen);
    });
    launchPopupSurface('Remove Popup: remove popup surfaces', function () {
        contentEl.innerHTML = '</br></br></br>';
        var successSpen = function (data) {
            alert("removed surface :" + data);
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg);
            console.log("error launching spen: " + msg);
        };

        samsung.spen.removeSurfacePopup("popupId", successSpen, failureSpen);
    });
    launchPopupSurface('Launch Inline: default flags', function () {
        contentEl.innerHTML = '<div class = nontranslate>' +
                '<img id="inlineimage" height="200" width = "300"/>' +
                '</div>';
        var successSpen = function (data) {
            if (data !== "") {
                document.getElementById("inlineimage").src = "data:image/jpg;base64," + data;
                samsung.spen.removeSurfaceInline("popupId", function () {
                }, function () {
                });
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg);
            console.log("error launching spen: " + msg);
        };
        var flagsInline = 0;
        var inlineOptions = {};
        inlineOptions.elementCoordinates = {};
        inlineOptions.bodyRectangleCoordinates = {};
        inlineOptions.elementCoordinates.left = 10;
        inlineOptions.elementCoordinates.top = 400;
        inlineOptions.elementCoordinates.width = 300;
        inlineOptions.elementCoordinates.height = 200;
        inlineOptions.bodyRectangleCoordinates.left = 0;
        inlineOptions.bodyRectangleCoordinates.top = 0;
        inlineOptions.id = "popupId";
        inlineOptions.returnType = 100;
        inlineOptions.backgroundColor = "#FFF333";
        inlineOptions.imageUri = "";
        inlineOptions.sPenFlags = flagsInline;
        samsung.spen.removeSurfaceInline("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
    });
    launchPopupSurface('Launch Inline: default + Edit', function () {
        contentEl.innerHTML = '<div class = nontranslate>' +
                '<img id="inlineimage" height="200" width = "300"/>' +
                '</div>';
        var successSpen = function (data) {
            if (data !== "") {
                document.getElementById("inlineimage").src = "data:image/jpg;base64," + data;
                //samsung.spen.removeSurfaceInline("popupId", function(){},function(){});
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg);
            console.log("error launching spen: " + msg);
        };
        var flagsInline = 0;
        flagsInline = flagsInline | samsung.spen.FLAG_EDIT;
        var inlineOptions = {};
        inlineOptions.elementCoordinates = {};
        inlineOptions.bodyRectangleCoordinates = {};
        inlineOptions.elementCoordinates.left = 10;
        inlineOptions.elementCoordinates.top = 400;
        inlineOptions.elementCoordinates.width = 300;
        inlineOptions.elementCoordinates.height = 200;
        inlineOptions.bodyRectangleCoordinates.left = 0;
        inlineOptions.bodyRectangleCoordinates.top = 0;
        inlineOptions.id = "popupId";
        inlineOptions.returnType = 100;
        inlineOptions.backgroundColor = "#FFF333";
        inlineOptions.imageUri = "";
        inlineOptions.sPenFlags = flagsInline;
        samsung.spen.removeSurfaceInline("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
    });
    launchPopupSurface('Launch Inline: default + Edit + only pen', function () {
        contentEl.innerHTML = '<div class = nontranslate>' +
                '<img id="inlineimage" height="200" width = "300"/>' +
                '</div>';
        var successSpen = function (data) {
            if (data !== "") {
                document.getElementById("inlineimage").src = "data:image/jpg;base64," + data;
                //samsung.spen.removeSurfaceInline("popupId", function(){},function(){});
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg);
            console.log("error launching spen: " + msg);
        };
        var flagsInline = 0;
        flagsInline = flagsInline | samsung.spen.FLAG_EDIT;
        flagsInline = flagsInline | samsung.spen.FLAG_PEN;
        var inlineOptions = {};
        inlineOptions.elementCoordinates = {};
        inlineOptions.bodyRectangleCoordinates = {};
        inlineOptions.elementCoordinates.left = 10;
        inlineOptions.elementCoordinates.top = 400;
        inlineOptions.elementCoordinates.width = 300;
        inlineOptions.elementCoordinates.height = 200;
        inlineOptions.bodyRectangleCoordinates.left = 0;
        inlineOptions.bodyRectangleCoordinates.top = 0;
        inlineOptions.id = "popupId";
        inlineOptions.returnType = 100;
        inlineOptions.backgroundColor = "#FFF333";
        inlineOptions.imageUri = "";
        inlineOptions.sPenFlags = flagsInline;
        samsung.spen.removeSurfaceInline("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
    });
    
    launchPopupSurface('Launch Inline: default + Edit + only pen settings', function () {
        contentEl.innerHTML = '<div class = nontranslate>' +
                '<img id="inlineimage" height="200" width = "300"/>' +
                '</div>';
        var successSpen = function (data) {
            if (data !== "") {
                document.getElementById("inlineimage").src = "data:image/jpg;base64," + data;
                //samsung.spen.removeSurfaceInline("popupId", function(){},function(){});
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg);
            console.log("error launching spen: " + msg);
        };
        var flagsInline = 0;
        flagsInline = flagsInline | samsung.spen.FLAG_EDIT;
        flagsInline = flagsInline | samsung.spen.FLAG_PEN;
        flagsInline = flagsInline | samsung.spen.FLAG_PEN_SETTINGS;
        var inlineOptions = {};
        inlineOptions.elementCoordinates = {};
        inlineOptions.bodyRectangleCoordinates = {};
        inlineOptions.elementCoordinates.left = 10;
        inlineOptions.elementCoordinates.top = 400;
        inlineOptions.elementCoordinates.width = 300;
        inlineOptions.elementCoordinates.height = 200;
        inlineOptions.bodyRectangleCoordinates.left = 0;
        inlineOptions.bodyRectangleCoordinates.top = 0;
        inlineOptions.id = "popupId";
        inlineOptions.returnType = 100;
        inlineOptions.backgroundColor = "#FFF333";
        inlineOptions.imageUri = "";
        inlineOptions.sPenFlags = flagsInline;
        samsung.spen.removeSurfaceInline("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
    });
    
    launchPopupSurface('Launch Inline: default + Edit + only Eraser', function () {
        contentEl.innerHTML = '<div class = nontranslate>' +
                '<img id="inlineimage" height="200" width = "300" left = "20"/>' +
                '</div>';
        var successSpen = function (data) {
            if (data !== "") {
                document.getElementById("inlineimage").src = "data:image/jpg;base64," + data;
                //samsung.spen.removeSurfaceInline("popupId", function(){},function(){});
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg);
            console.log("error launching spen: " + msg);
        };
        var flagsInline = 0;
        flagsInline = flagsInline | samsung.spen.FLAG_EDIT;
        flagsInline = flagsInline | samsung.spen.FLAG_ERASER;
        var inlineOptions = {};
        inlineOptions.elementCoordinates = {};
        inlineOptions.bodyRectangleCoordinates = {};
        inlineOptions.elementCoordinates.left = 10;
        inlineOptions.elementCoordinates.top = 400;
        inlineOptions.elementCoordinates.width = 300;
        inlineOptions.elementCoordinates.height = 200;
        inlineOptions.bodyRectangleCoordinates.left = 0;
        inlineOptions.bodyRectangleCoordinates.top = 0;
        inlineOptions.id = "popupId";
        inlineOptions.returnType = 100;
        inlineOptions.backgroundColor = "#FFF333";
        inlineOptions.imageUri = "";
        inlineOptions.sPenFlags = flagsInline;
        samsung.spen.removeSurfaceInline("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
    });
    launchPopupSurface('Launch Inline: default + Edit + only Selection', function () {
        contentEl.innerHTML = '<div class = nontranslate>' +
                '<img id="inlineimage" height="200" width = "300"/>' +
                '</div>';
        var successSpen = function (data) {
            if (data !== "") {
                document.getElementById("inlineimage").src = "data:image/jpg;base64," + data;
                //samsung.spen.removeSurfaceInline("popupId", function(){},function(){});
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg);
            console.log("error launching spen: " + msg);
        };
        var flagsInline = 0;
        flagsInline = flagsInline | samsung.spen.FLAG_EDIT;
        flagsInline = flagsInline | samsung.spen.FLAG_SELECTION;
        var inlineOptions = {};
        inlineOptions.elementCoordinates = {};
        inlineOptions.bodyRectangleCoordinates = {};
        inlineOptions.elementCoordinates.left = 10;
        inlineOptions.elementCoordinates.top = 400;
        inlineOptions.elementCoordinates.width = 300;
        inlineOptions.elementCoordinates.height = 200;
        inlineOptions.bodyRectangleCoordinates.left = 0;
        inlineOptions.bodyRectangleCoordinates.top = 0;
        inlineOptions.id = "popupId";
        inlineOptions.returnType = 100;
        inlineOptions.backgroundColor = "#FFF333";
        inlineOptions.imageUri = "";
        inlineOptions.sPenFlags = flagsInline;
        samsung.spen.removeSurfaceInline("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
    });
    launchPopupSurface('Launch Inline: default + Edit + only Background', function () {
        contentEl.innerHTML = '<div class = nontranslate>' +
                '<img id="inlineimage" height="200" width = "300"/>' +
                '</div>';
        var successSpen = function (data) {
            if (data !== "") {
                document.getElementById("inlineimage").src = "data:image/jpg;base64," + data;
                //samsung.spen.removeSurfaceInline("popupId", function(){},function(){});
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg);
            console.log("error launching spen: " + msg);
        };
        var flagsInline = 0;
        flagsInline = flagsInline | samsung.spen.FLAG_EDIT;
        flagsInline = flagsInline | samsung.spen.FLAG_BACKGROUND;
        var inlineOptions = {};
        inlineOptions.elementCoordinates = {};
        inlineOptions.bodyRectangleCoordinates = {};
        inlineOptions.elementCoordinates.left = 10;
        inlineOptions.elementCoordinates.top = 400;
        inlineOptions.elementCoordinates.width = 300;
        inlineOptions.elementCoordinates.height = 200;
        inlineOptions.bodyRectangleCoordinates.left = 0;
        inlineOptions.bodyRectangleCoordinates.top = 0;
        inlineOptions.id = "popupId";
        inlineOptions.returnType = 100;
        inlineOptions.backgroundColor = "#FFF333";
        inlineOptions.imageUri = "";
        inlineOptions.sPenFlags = flagsInline;
        samsung.spen.removeSurfaceInline("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
    });
    launchPopupSurface('Launch Inline: default + Edit + undo Redo', function () {
        contentEl.innerHTML = '<div class = nontranslate>' +
                '<img id="inlineimage" height="200" width = "300"/>' +
                '</div>';
        var successSpen = function (data) {
            if (data !== "") {
                document.getElementById("inlineimage").src = "data:image/jpg;base64," + data;
                //samsung.spen.removeSurfaceInline("popupId", function(){},function(){});
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg);
            console.log("error launching spen: " + msg);
        };
        var flagsInline = 0;
        flagsInline = flagsInline | samsung.spen.FLAG_EDIT;
        flagsInline = flagsInline | samsung.spen.FLAG_UNDO_REDO;
        var inlineOptions = {};
        inlineOptions.elementCoordinates = {};
        inlineOptions.bodyRectangleCoordinates = {};
        inlineOptions.elementCoordinates.left = 10;
        inlineOptions.elementCoordinates.top = 400;
        inlineOptions.elementCoordinates.width = 300;
        inlineOptions.elementCoordinates.height = 200;
        inlineOptions.bodyRectangleCoordinates.left = 0;
        inlineOptions.bodyRectangleCoordinates.top = 0;
        inlineOptions.id = "popupId";
        inlineOptions.returnType = 100;
        inlineOptions.backgroundColor = "#FFF333";
        inlineOptions.imageUri = "";
        inlineOptions.sPenFlags = flagsInline;
        samsung.spen.removeSurfaceInline("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
    });
    launchPopupSurface('Launch Inline: default + Edit + only recognize text', function () {
        contentEl.innerHTML = '<div class = nontranslate>' +
                '<img id="inlineimage" height="200" width = "300"/>' +
                '</div>';
        var successSpen = function (data) {
            if (data !== "") {
                document.getElementById("inlineimage").src = "data:image/jpg;base64," + data;
                //samsung.spen.removeSurfaceInline("popupId", function(){},function(){});
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg);
            console.log("error launching spen: " + msg);
        };
        var flagsInline = 0;
        flagsInline = flagsInline | samsung.spen.FLAG_EDIT;
        flagsInline = flagsInline | samsung.spen.FLAG_SELECTION;
        var inlineOptions = {};
        inlineOptions.elementCoordinates = {};
        inlineOptions.bodyRectangleCoordinates = {};
        inlineOptions.elementCoordinates.left = 10;
        inlineOptions.elementCoordinates.top = 400;
        inlineOptions.elementCoordinates.width = 300;
        inlineOptions.elementCoordinates.height = 200;
        inlineOptions.bodyRectangleCoordinates.left = 0;
        inlineOptions.bodyRectangleCoordinates.top = 0;
        inlineOptions.id = "popupId";
        inlineOptions.returnType = 100;
        inlineOptions.backgroundColor = "#FFF333";
        inlineOptions.imageUri = "";
        inlineOptions.sPenFlags = flagsInline;
        samsung.spen.removeSurfaceInline("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
    });
    launchPopupSurface('Launch Inline: default + Edit + only recognize shape', function () {
        contentEl.innerHTML = '<div class = nontranslate>' +
                '<img id="inlineimage" height="200" width = "300"/>' +
                '</div>';
        var successSpen = function (data) {
            if (data !== "") {
                document.getElementById("inlineimage").src = "data:image/jpg;base64," + data;
                //samsung.spen.removeSurfaceInline("popupId", function(){},function(){});
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg);
            console.log("error launching spen: " + msg);
        };
        var flagsInline = 0;
        flagsInline = flagsInline | samsung.spen.FLAG_EDIT;
        flagsInline = flagsInline | samsung.spen.FLAG_SELECTION;
        flagsInline = flagsInline | samsung.spen.FLAG_RECOGNIZE_SHAPE;
        var inlineOptions = {};
        inlineOptions.elementCoordinates = {};
        inlineOptions.bodyRectangleCoordinates = {};
        inlineOptions.elementCoordinates.left = 10;
        inlineOptions.elementCoordinates.top = 400;
        inlineOptions.elementCoordinates.width = 300;
        inlineOptions.elementCoordinates.height = 200;
        inlineOptions.bodyRectangleCoordinates.left = 0;
        inlineOptions.bodyRectangleCoordinates.top = 0;
        inlineOptions.id = "popupId";
        inlineOptions.returnType = 100;
        inlineOptions.backgroundColor = "#FFF333";
        inlineOptions.imageUri = "";
        inlineOptions.sPenFlags = flagsInline;
        samsung.spen.removeSurfaceInline("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
    });
    launchPopupSurface('Launch Inline: all options', function () {
        contentEl.innerHTML = '<div class = nontranslate>' +
                '<img id="inlineimage" height="200" width = "300"/>' +
                '</div>';
        var successSpen = function (data) {
            if (data !== "") {
                document.getElementById("inlineimage").src = "data:image/jpg;base64," + data;
                //samsung.spen.removeSurfaceInline("popupId", function(){},function(){});
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg);
            console.log("error launching spen: " + msg);
        };
        var flagsInline = 0;
        flagsInline = flagsInline | samsung.spen.FLAG_EDIT;
        flagsInline = flagsInline | samsung.spen.FLAG_BACKGROUND;
        flagsInline = flagsInline | samsung.spen.FLAG_ERASER;
        flagsInline = flagsInline | samsung.spen.FLAG_PEN;
        flagsInline = flagsInline | samsung.spen.FLAG_PEN_SETTINGS;
        flagsInline = flagsInline | samsung.spen.FLAG_RECOGNIZE_SHAPE;
        flagsInline = flagsInline | samsung.spen.FLAG_UNDO_REDO;
        flagsInline = flagsInline | samsung.spen.FLAG_SELECTION;
        var inlineOptions = {};
        inlineOptions.elementCoordinates = {};
        inlineOptions.bodyRectangleCoordinates = {};
        inlineOptions.elementCoordinates.left = 10;
        inlineOptions.elementCoordinates.top = 400;
        inlineOptions.elementCoordinates.width = 300;
        inlineOptions.elementCoordinates.height = 200;
        inlineOptions.bodyRectangleCoordinates.left = 0;
        inlineOptions.bodyRectangleCoordinates.top = 0;
        inlineOptions.id = "popupId";
        inlineOptions.returnType = 100;
        inlineOptions.backgroundColor = "#FFF333";
        inlineOptions.imageUri = "";
        inlineOptions.sPenFlags = flagsInline;
        samsung.spen.removeSurfaceInline("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
    });
    launchPopupSurface('Launch Inline: all options+ color "xyz"', function () {
        contentEl.innerHTML = '<div class = nontranslate>' +
                '<img id="inlineimage" height="200" width = "300"/>' +
                '</div>';
        var successSpen = function (data) {
            if (data !== "") {
                document.getElementById("inlineimage").src = "data:image/jpg;base64," + data;
                //samsung.spen.removeSurfaceInline("popupId", function(){},function(){});
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg);
            console.log("error launching spen: " + msg);
        };
        var flagsInline = 0;
        flagsInline = flagsInline | samsung.spen.FLAG_EDIT;
        flagsInline = flagsInline | samsung.spen.FLAG_BACKGROUND;
        flagsInline = flagsInline | samsung.spen.FLAG_ERASER;
        flagsInline = flagsInline | samsung.spen.FLAG_PEN;
        flagsInline = flagsInline | samsung.spen.FLAG_PEN_SETTINGS;
        flagsInline = flagsInline | samsung.spen.FLAG_RECOGNIZE_SHAPE;
        flagsInline = flagsInline | samsung.spen.FLAG_UNDO_REDO;
        flagsInline = flagsInline | samsung.spen.FLAG_SELECTION;
        var inlineOptions = {};
        inlineOptions.elementCoordinates = {};
        inlineOptions.bodyRectangleCoordinates = {};
        inlineOptions.elementCoordinates.left = 10;
        inlineOptions.elementCoordinates.top = 400;
        inlineOptions.elementCoordinates.width = 300;
        inlineOptions.elementCoordinates.height = 200;
        inlineOptions.bodyRectangleCoordinates.left = 0;
        inlineOptions.bodyRectangleCoordinates.top = 0;
        inlineOptions.id = "popupId";
        inlineOptions.returnType = 100;
        inlineOptions.backgroundColor = "xyz";
        inlineOptions.imageUri = "";
        inlineOptions.sPenFlags = flagsInline;
        samsung.spen.removeSurfaceInline("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
    });
    launchPopupSurface('Launch Inline: all options + load image uri', function () {
        contentEl.innerHTML = '<div class = nontranslate>' +
                '<img id="inlineimage" height="200" width = "300"/>' +
                '</div>';
        var successSpen = function (data) {
            if (data !== "") {
                document.getElementById("inlineimage").src = "data:image/jpg;base64," + data;
                //samsung.spen.removeSurfaceInline("popupId", function(){},function(){});
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg);
            console.log("error launching spen: " + msg);
        };
        var flagsInline = 0;
        flagsInline = flagsInline | samsung.spen.FLAG_EDIT;
        flagsInline = flagsInline | samsung.spen.FLAG_BACKGROUND;
        flagsInline = flagsInline | samsung.spen.FLAG_ERASER;
        flagsInline = flagsInline | samsung.spen.FLAG_PEN;
        flagsInline = flagsInline | samsung.spen.FLAG_PEN_SETTINGS;
        flagsInline = flagsInline | samsung.spen.FLAG_RECOGNIZE_SHAPE;
        flagsInline = flagsInline | samsung.spen.FLAG_RECOGNIZE_TEXT;
        flagsInline = flagsInline | samsung.spen.FLAG_UNDO_REDO;
        flagsInline = flagsInline | samsung.spen.FLAG_SELECTION;
        var inlineOptions = {};
        inlineOptions.elementCoordinates = {};
        inlineOptions.bodyRectangleCoordinates = {};
        inlineOptions.elementCoordinates.left = 10;
        inlineOptions.elementCoordinates.top = 400;
        inlineOptions.elementCoordinates.width = 300;
        inlineOptions.elementCoordinates.height = 200;
        inlineOptions.bodyRectangleCoordinates.left = 0;
        inlineOptions.bodyRectangleCoordinates.top = 0;
        inlineOptions.id = "popupId";
        inlineOptions.returnType = 100;
        inlineOptions.backgroundColor = "#FFF333";
        inlineOptions.imageUri = "file:///data/data/com.samsung.cordova.demo/files/popupId.png";
        inlineOptions.sPenFlags = flagsInline;
        samsung.spen.removeSurfaceInline("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
    });
    launchPopupSurface('Launch Inline: all options + return type image uri', function () {
        var successSpen = function (data) {
            if (data !== "") {
                contentEl.innerHTML = '<div class="ui-block-a">' +
                        '<label for="imageUri">Image Uri </label>' +
                        '<input type="text" name="imageUri" id="imageUri" width = 300>' +
                        '</div>';
                document.getElementById("imageUri").value = data;
                //samsung.spen.removeSurfaceInline("popupId", function(){},function(){});
            }
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg);
            console.log("error launching spen: " + msg);
        };
        var flagsInline = 0;
        flagsInline = flagsInline | samsung.spen.FLAG_EDIT;
        flagsInline = flagsInline | samsung.spen.FLAG_BACKGROUND;
        flagsInline = flagsInline | samsung.spen.FLAG_ERASER;
        flagsInline = flagsInline | samsung.spen.FLAG_PEN;
        flagsInline = flagsInline | samsung.spen.FLAG_PEN_SETTINGS;
        flagsInline = flagsInline | samsung.spen.FLAG_RECOGNIZE_SHAPE;
        flagsInline = flagsInline | samsung.spen.FLAG_RECOGNIZE_TEXT;
        flagsInline = flagsInline | samsung.spen.FLAG_UNDO_REDO;
        flagsInline = flagsInline | samsung.spen.FLAG_SELECTION;
        var inlineOptions = {};
        inlineOptions.elementCoordinates = {};
        inlineOptions.bodyRectangleCoordinates = {};
        inlineOptions.elementCoordinates.left = 10;
        inlineOptions.elementCoordinates.top = 400;
        inlineOptions.elementCoordinates.width = 300;
        inlineOptions.elementCoordinates.height = 200;
        inlineOptions.bodyRectangleCoordinates.left = 0;
        inlineOptions.bodyRectangleCoordinates.top = 0;
        inlineOptions.id = "popupId";
        inlineOptions.returnType = 101;
        inlineOptions.backgroundColor = "#FFF333";
        inlineOptions.imageUri = "";
        inlineOptions.sPenFlags = flagsInline;
        samsung.spen.removeSurfaceInline("popupId", function () {
        }, function () {
        });
        samsung.spen.launchSurfaceInline(inlineOptions, successSpen, failureSpen);
    });
    launchPopupSurface('Remove Inline: remove Inline surface', function () {
        contentEl.innerHTML = '</br></br></br></br>';
        var successSpen = function (data) {
            //console.log("removed id: " + data)
        };
        var failureSpen = function (msg) {
            alert("error callback: " + msg);
            console.log("error removing : " + msg);
        };
        samsung.spen.removeSurfaceInline("popupId", successSpen, failureSpen);
    });
};
