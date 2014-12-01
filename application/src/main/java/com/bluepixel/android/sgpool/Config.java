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

package com.bluepixel.android.sgpool;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class Config {
    // General configuration

    // Is this an internal dogfood build?
    public static final boolean IS_DOGFOOD_BUILD = false;



    // Conference hashtag
    public static final String CONFERENCE_HASHTAG = "#io14";

    // Patterns that, when absent from a hashtag, will trigger the addition of the
    // CONFERENCE_HASHTAG on sharing snippets. Ex: "#Android" will be shared as "#io14 #Android",
    // but "#iohunt" won't be modified.
    public static final String CONFERENCE_HASHTAG_PREFIX = "#io";

    // Hard-coded conference dates. This is hardcoded here instead of extracted from the conference
    // data to avoid the Schedule UI breaking if some session is incorrectly set to a wrong date.
    public static final int CONFERENCE_YEAR = 2014;


    public static final TimeZone CONFERENCE_TIMEZONE = TimeZone.getTimeZone("America/Los_Angeles");


    // shorthand for some units of time
    public static final long SECOND_MILLIS = 1000;
    public static final long MINUTE_MILLIS = 60 * SECOND_MILLIS;
    public static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
    public static final long DAY_MILLIS = 24 * HOUR_MILLIS;

    // OAuth 2.0 related config
    public static final String APP_NAME = "GoogleIO-Android";
    public static final String API_KEY = "";

    // GCM config
    public static final String GCM_SERVER_PROD_URL = "";
    public static final String GCM_SERVER_URL = "";

    // the GCM sender ID is the ID of the app in Google Cloud Console
    public static final String GCM_SENDER_ID = "";

    // The registration api KEY in the gcm server (configured in the GCM
    // server's AuthHelper.java file)
    public static final String GCM_API_KEY = "";



    // Known session tags that induce special behaviors
    public interface Tags {
        // tag that indicates a session is a live session
        public static final String SESSIONS = "TYPE_SESSIONS";

        // the tag category that we use to group sessions together when displaying them
        public static final String SESSION_GROUPING_TAG_CATEGORY = "TYPE";

        // tag categories
        public static final String CATEGORY_THEME = "THEME";
        public static final String CATEGORY_TOPIC = "TOPIC";
        public static final String CATEGORY_TYPE = "TYPE";

        public static final Map<String, Integer> CATEGORY_DISPLAY_ORDERS
                = new HashMap<String, Integer>();

        public static final String SPECIAL_KEYNOTE = "FLAG_KEYNOTE";

        public static final String[] EXPLORE_CATEGORIES =
                { CATEGORY_THEME, CATEGORY_TOPIC, CATEGORY_TYPE };


    }

    static {
        Tags.CATEGORY_DISPLAY_ORDERS.put(Tags.CATEGORY_THEME, 0);
        Tags.CATEGORY_DISPLAY_ORDERS.put(Tags.CATEGORY_TOPIC, 1);
        Tags.CATEGORY_DISPLAY_ORDERS.put(Tags.CATEGORY_TYPE, 2);
    }

    private static String piece(String s, char start, char end) {
        int startIndex = s.indexOf(start), endIndex = s.indexOf(end);
        return s.substring(startIndex + 1, endIndex);
    }

    private static String piece(String s, char start) {
        int startIndex = s.indexOf(start);
        return s.substring(startIndex + 1);
    }

    private static String rep(String s, String orig, String replacement) {
        return s.replaceAll(orig, replacement);
    }
}
