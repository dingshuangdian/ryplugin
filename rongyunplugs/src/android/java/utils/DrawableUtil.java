package cordova.plugin.ismartnet.rongcloud.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;

/**
 * Created by lvping on 2017/9/8.
 */

public class DrawableUtil {
  public DrawableUtil() {
  }

  public static void setDrawableRight(Drawable var0, TextView var1) {
    if(var0 != null) {
      var0.setBounds(0, 0, var0.getMinimumWidth(), var0.getMinimumHeight());
      var1.setCompoundDrawables((Drawable)null, (Drawable)null, var0, (Drawable)null);
    } else {
      var1.setCompoundDrawables((Drawable)null, (Drawable)null, (Drawable)null, (Drawable)null);
    }

  }

  public static void setClickable(View var0, boolean var1) {
    if(var1) {
      var0.getBackground().mutate().setAlpha(255);
      var0.setEnabled(true);
    } else {
      var0.getBackground().mutate().setAlpha(100);
      var0.setEnabled(false);
    }

  }

  public static void setRightDrawable(Context var0, TextView var1, int var2, boolean var3) {
    Drawable var4 = var0.getResources().getDrawable(var2);
    var4.setBounds(0, 0, var4.getMinimumWidth(), var4.getMinimumHeight());
    if(var3) {
      var1.setCompoundDrawables((Drawable)null, (Drawable)null, var4, (Drawable)null);
    } else {
      var1.setCompoundDrawables((Drawable)null, (Drawable)null, (Drawable)null, (Drawable)null);
    }

  }

  public static Drawable generateDrawable(Context var0, String var1, int var2) {
    GradientDrawable var3 = new GradientDrawable();
    if(StringUtil.isEmpty(var1)) {
      var3.setColor(-1);
    } else if(var1.startsWith("#")) {
      var3.setColor(Color.parseColor(var1));
    } else {
      var3.setColor(Color.parseColor("#" + var1));
    }

    var3.setCornerRadius((float)ScreenUtil.dp2px(var0, var2));
    return var3;
  }
}
