/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bluepixel.android.sgpool.util;

import android.content.Context;
import android.net.wifi.WifiManager;

import static com.bluepixel.android.sgpool.util.LogUtils.makeLogTag;

public class WiFiUtils {


    private static final String TAG = makeLogTag(WiFiUtils.class);

    public static boolean isWiFiEnabled(final Context context) {
        final WifiManager wifiManager =
                (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }




}
