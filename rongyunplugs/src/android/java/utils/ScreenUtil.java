package cordova.plugin.ismartnet.rongcloud.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by lvping on 2017/9/8.
 */

public class ScreenUtil {
  public ScreenUtil() {
  }

  public static float sp2Px(Context var0, int var1) {
    return TypedValue.applyDimension(2, (float)var1, var0.getResources().getDisplayMetrics());
  }

  public static int sp2px(Context var0, float var1) {
    float var2 = var0.getResources().getDisplayMetrics().scaledDensity;
    return (int)(var1 * var2 + 0.5F);
  }

  public static float px2Sp(int var0, Context var1) {
    return TypedValue.applyDimension(0, (float)var0, var1.getResources().getDisplayMetrics());
  }

  public static int px2dip(Context var0, float var1) {
    float var2 = var0.getResources().getDisplayMetrics().density;
    return (int)(var1 / var2 + 0.5F);
  }

  public static int dip2px(Context var0, float var1) {
    float var2 = var0.getResources().getDisplayMetrics().density;
    return (int)(var1 * var2 + 0.5F);
  }

  public static int dp2px(Context var0, int var1) {
    return (int)TypedValue.applyDimension(1, (float)var1, var0.getResources().getDisplayMetrics());
  }

  public static int dpToPx(Resources var0, int var1) {
    return (int)TypedValue.applyDimension(0, (float)var1, var0.getDisplayMetrics());
  }

  public static int getScreenHeight(Context var0) {
    return var0.getResources().getDisplayMetrics().heightPixels;
  }

  public static int getScreenWidth(Context var0) {
    return var0.getResources().getDisplayMetrics().widthPixels;
  }
}
