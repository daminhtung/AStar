package Ultis;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by FRAMGIA\nguyen.van.tung on 10/11/2016.
 */
public class Libs {
    private static Libs _libs;

    public static Libs getInstance() {
        if (_libs == null) _libs = new Libs();
        return _libs;
    }

    public int convertDpToPixel(int dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = dp * ((int) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
}
