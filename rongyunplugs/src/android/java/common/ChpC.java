package cordova.plugin.ismartnet.rongcloud.common;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lvping on 2017/9/11.
 */

public class ChpC {
  private static ChpC a = null;
  private ExecutorService b = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 + 1);

  private ChpC() {
  }

  public static ChpC a() {
    Class var0 = ChpC.class;
    synchronized(ChpC.class) {
      if(a == null) {
        a = new ChpC();
      }
    }

    return a;
  }

  public static void a(Runnable var0) {
    (new Handler(Looper.getMainLooper())).post(var0);
  }

  public void b(Runnable var1) {
    this.b.execute(var1);
  }
}
