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

    /**
     * dp 单位转换为 px.
     */
    public static int dp2px(Context context, float dpValue){
        return (int)(dpValue * (context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    /**
     * px 单位转换为 dp.
     */
    public static int px2dp(Context context, float pxValue){
        return (int)(pxValue / (context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    /**
     * sp 单位转换为 px.
     */
    public static int sp2Px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px 单位转换为 sp.
     */
    public static float px2Sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }
}
