package com.github.airsaid.androidwidget.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * @author airsaid
 */
public class DimenUtils {

    private DimenUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static int dp2px(Context context, float dpValue){
        return (int)(dpValue * (context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int dp2px(Context context, int dpValue){
        return (int)(dpValue * (context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dp(Context context, float pxValue){
        return (int)(pxValue / (context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int sp2Px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    public static float px2Sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

}
