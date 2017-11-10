package cordova.plugin.ismartnet.rongcloud.utils;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import cordova.plugin.ismartnet.rongcloud.common.ChpC;

/**
 * Created by lvping on 2017/9/11.
 */

public class DialogDisplay {
  private static DialogDisplay instance = null;

  private DialogDisplay() {
  }

  public static DialogDisplay getInstance() {
    Class var0 = DialogDisplay.class;
    synchronized(DialogDisplay.class) {
      if(instance == null) {
        instance = new DialogDisplay();
      }
    }

    return instance;
  }


  public void dialogLoading(final Activity var1, final String var2) {
    ChpC.a(new Runnable() {
      public void run() {
        if(var1 != null) {
          FragmentManager var1x = var1.getFragmentManager();
          if(var1x != null) {
            LoadingDialogFragment var2x = (LoadingDialogFragment)var1x.findFragmentByTag(LoadingDialogFragment.class.getName());
            if(var2x == null) {
              var2x = LoadingDialogFragment.getInstance();
              var2x.showAllowingStateLoss(var1x);
              var1x.executePendingTransactions();
            }

            var2x.setMessage(var2);
          }
        }

      }
    });
  }

  public void dialogLoading(final Activity var1, final String var2, final LoadingDialogFragment.LoadingDialogListener var3) {
    ChpC.a();
    ChpC.a(new Runnable() {
      public void run() {
        if(var1 != null) {
          FragmentManager var1x = var1.getFragmentManager();
          if(var1x != null) {
            LoadingDialogFragment var2x = (LoadingDialogFragment)var1x.findFragmentByTag(LoadingDialogFragment.class.getName());
            if(var2x == null) {
              var2x = LoadingDialogFragment.getInstance(var3);
              var2x.showAllowingStateLoss(var1x);
              var1x.executePendingTransactions();
            }

            var2x.setMessage(var2);
          }
        }

      }
    });
  }

  public void dialogCloseLoading(final Activity var1) {
    ChpC.a(new Runnable() {
      public void run() {
        if(var1 != null) {
          FragmentManager var1x = var1.getFragmentManager();
          if(var1x != null) {
            LoadingDialogFragment var2 = (LoadingDialogFragment)var1x.findFragmentByTag(LoadingDialogFragment.class.getName());
            if(var2 != null) {
              var2.dismissAllowingStateLoss();
            }
          }
        }

      }
    });
  }
}
