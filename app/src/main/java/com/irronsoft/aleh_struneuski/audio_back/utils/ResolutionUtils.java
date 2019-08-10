package com.irronsoft.aleh_struneuski.audio_back.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public final class ResolutionUtils {

    private ResolutionUtils() {
    }

    public static int convertPercentToPixelHight(Context context, float percent) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float pxH = displayMetrics.heightPixels;

        float finalH = (pxH * percent) / 100;

        int f = Math.round(finalH);

        return f;
    }

    public static int convertPercentToPixelWidth(Context context, float percent) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float pxH = displayMetrics.widthPixels;

        float finalW = (pxH * percent) / 100;

        int f = Math.round(finalW);

        return f;
    }

}
