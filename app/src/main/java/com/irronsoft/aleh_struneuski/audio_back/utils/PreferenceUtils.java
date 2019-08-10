package com.irronsoft.aleh_struneuski.audio_back.utils;

import android.content.Context;
import android.preference.PreferenceManager;

public class PreferenceUtils {

    public static final String LIMIT = "limit";
    public static final String ORDER = "order";

    public static final int ORDER_ADDED = 0, ORDER_NAME = 1, ORDER_ARTIST = 2, ORDER_ALBUM = 3, ORDER_LENGTH = 4, ORDER_RANDOM = 5;

    public static int getLimit(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(LIMIT, 0);
    }

    public static int getTrackOrder(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(ORDER, ORDER_ADDED);
    }

}

