package cordova.plugin.ismartnet.rongcloud.utils;

/**
 * Created by lvping on 2017/9/8.
 */

public class NoDoubleClickUtils {
  private static long lastClickTime;
  private static final int SPACE_TIME = 500;

  public NoDoubleClickUtils() {
  }

  public static void initLastClickTime() {
    lastClickTime = 0L;
  }

  public static synchronized boolean isDoubleClick() {
    long var0 = System.currentTimeMillis();
    boolean var2;
    if(var0 - lastClickTime > 500L) {
      var2 = false;
    } else {
      var2 = true;
    }

    lastClickTime = var0;
    return var2;
  }
}
