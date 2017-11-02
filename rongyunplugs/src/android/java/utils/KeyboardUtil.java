package cordova.plugin.ismartnet.rongcloud.utils;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import java.util.Timer;
import java.util.TimerTask;
/**
 * Created by lvping on 2017/9/8.
 */
public class KeyboardUtil {
  public KeyboardUtil() {
  }
  public static void showKeyboard(Activity var0) {
    if(null != var0) {
      InputMethodManager var1 = (InputMethodManager)var0.getSystemService(Context.INPUT_METHOD_SERVICE);
      var1.toggleSoftInput(0, 2);
    }
  }
  public static void hideKeyboard(Activity var0) {
    if(null != var0) {
      try {
        View var1 = var0.getWindow().peekDecorView();
        if(var1 != null && var1.getWindowToken() != null) {
          InputMethodManager var2 = (InputMethodManager)var0.getSystemService(Context.INPUT_METHOD_SERVICE);
          var2.hideSoftInputFromWindow(var1.getWindowToken(), 0);
        }
      } catch (Exception var3) {
      }
    }
  }

  public static final void popInputMethod(final EditText var0) {
    var0.setFocusableInTouchMode(true);
    var0.requestFocus();
    Timer var1 = new Timer();
    var1.schedule(new TimerTask() {
      public void run() {
        InputMethodManager var1 = (InputMethodManager)var0.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        var1.showSoftInput(var0, 0);
      }
    }, 500L);
  }
}
