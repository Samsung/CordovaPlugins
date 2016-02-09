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

package com.samsung.spen;

import android.graphics.Color;

import com.samsung.android.sdk.pen.engine.SpenSurfaceView;
/**
 * A utility class used for static final variables. Not for Object creation.
 */
public class Utils {

    public static int mToolTypeSpen = SpenSurfaceView.TOOL_SPEN;
    public static int mToolTypeFinger = SpenSurfaceView.TOOL_FINGER;
    public final static int REQUEST_CODE_SELECT_IMAGE_BACKGROUND = 100;
    public static final int CONTEXT_MENU_DELETE_ID = 10;
    public static final int CONTEXT_MENU_TEXT_ID = 11;
    public static final int CONTEXT_MENU_SHAPE_ID = 12;
    public static final int CONTEXT_MENU_CUT_ID = 13;
    public static final int CONTEXT_MENU_COPY_ID = 14;
    public static final int CONTEXT_MENU_PASTE_ID = 15;
    public static final int SURFACE_INLINE = 200;
    public static final int SURFACE_POPUP = 201;
    protected static final int LIGHT_YELLOW = 0xFFFFF0A9;
    protected static final int LIGHT_SALMON = 0xFFFED8BC;
    protected static final int LIGHT_PINK = 0xFFFFC9E8;
    protected static final int LIGHT_PURPLE = 0xFFBDD6FC;
    protected static final int LIGHT_SKY_BLUE = 0xFFBAFBED;
    protected static final int LIGHT_GREEN = 0xFFB3FCB5;
    protected static final int SPRING_GREEN = 0xFFE4FAA8;
    protected static final int DIVINE_WHITE = 0xFFFFFFFF;
    public static final int RETURN_TYPE_IMAGE_DATA = 100;
    public static final int RETURN_TYPE_IMAGE_URI = 101;
    public static final int RETURN_TYPE_TEXT = 102;
    public static final int BACKGROUND_IMAGE_MODE_CENTER = 200;
    public static final int BACKGROUND_IMAGE_MODE_FIT = 201;
    public static final int BACKGROUND_IMAGE_MODE_STRETCH = 202;
    public static final int BACKGROUND_IMAGE_MODE_TILE = 203;
    public static final int IMAGE_URI_MODE_CENTER = 300;
    public static final int IMAGE_URI_MODE_FIT = 301;
    public static final int IMAGE_URI_MODE_STRETCH = 302;
    public static final int IMAGE_URI_MODE_TILE = 303;
    public static final int FLAG_PEN_SETTINGS = 1 << 0;
    public static final int FLAG_BACKGROUND = 1 << 1;
    public static final int FLAG_SELECTION = 1 << 2;
    public static final int FLAG_SHAPE_RECOGNITION = 1 << 3;
    public static final int FLAG_TEXT_RECOGNITION = 1 << 4;
    public static final int FLAG_EDIT = 1 << 5;
    public static final int FLAG_PEN = 1 << 6;
    public static final int FLAG_ERASER = 1 << 7;
    public static final int FLAG_UNDO_REDO = 1 << 8;
    public static final int FLAG_ADD_PAGE = 1 << 9;
    public static final int PEN_COLOR_BLACK = Color.rgb(0, 0, 0);
    public static final int PEN_COLOR_GREEN = Color.rgb(9, 119, 22);
    public static final int PEN_COLOR_BLUE = Color.rgb(64, 91, 193);
    public static final int PEN_COLOR_PURPLE = Color.rgb(124, 81, 161);
    public static final int PEN_COLOR_PINK = Color.rgb(239, 77, 156);
    public static final int PEN_COLOR_BROWN = Color.rgb(195, 56, 21);
    public static final int PEN_COLOR_ORANGE = Color.rgb(255, 142, 51);
    public static final int MIN_FLAGS_VALUE = 0;
    public static final int MAX_FLAGS_VALUE = 1023;
    public static final float PORTRAIT_X_FACTOR = 0.9f;
    public static final float PORTRAIT_Y_FACTOR = 0.75f;
    public static final float LANDSCAPE_X_FACTOR = 0.9F;
    public static final float LANDSCAPE_Y_FACTOR = 0.75f;
    public static final String SPEN = "SpenPlugin";
    /**
     * Util class. Not for object creation.
     */
    private Utils() {
    }

}