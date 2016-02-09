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

import java.util.HashMap;
import java.util.Map;
/**
 * Class that holds all the surface views.
 *
 */
public class SpenSurfaceViews {
    public static final int MAX_INLINE_SURFACE_COUNT = 3;
    public static final int MAX_POPUP_SURFACE_COUNT = 2;
    public static final int MAX_SURFACE_COUNT = 5;
    Map<String, SPenSurfaceWithTrayBar> mSpenSurfaceViews = new HashMap<String, SPenSurfaceWithTrayBar>(
            3);

    
    /**
     * Put SPenSurfaceWithTrayBar in Map
     * 
     */
    void addSurfaceView(String id, SPenSurfaceWithTrayBar surfaceWithTrayBar) {
        mSpenSurfaceViews.put(id, surfaceWithTrayBar);
    }

    /**
     * Get SPenSurfaceWithTrayBar
     * 
     * @params id String
     * @return SPenSurfaceWithTrayBar
     * 
     */
    SPenSurfaceWithTrayBar getSurfaceView(String id) {
        return mSpenSurfaceViews.get(id);
    }

    /**
     * Remove SPenSurfaceWithTrayBar from Map
     * 
     * @params id String
     * 
     */
    void removeSurfaceView(String id) {
        mSpenSurfaceViews.remove(id);
    }

    /**
     * Get Size of Map
     * 
     * @return Size int
     * 
     */
    public int getSurfaceCount() {
        return mSpenSurfaceViews.size();
    }
}
