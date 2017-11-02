package cordova.plugin.ismartnet.rongcloud.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lvping on 2017/9/18.
 */

public class cAdapter {
  private SparseArray<View> a;
  private int b;
  private View c;

  private cAdapter(Context var1, ViewGroup var2, int var3, int var4) {
    this.b = var4;
    this.a = new SparseArray();
    this.c = LayoutInflater.from(var1).inflate(var3, var2, false);
    this.c.setTag(this);
  }

  public static cAdapter a(Context var0, View var1, ViewGroup var2, int var3, int var4) {
    if(var1 == null) {
      return new cAdapter(var0, var2, var3, var4);
    } else {
      cAdapter var5 = (cAdapter)var1.getTag();
      var5.b = var4;
      return var5;
    }
  }

  public <T extends View> T a(int var1) {
    View var2 = (View)this.a.get(var1);
    if(var2 == null) {
      var2 = this.c.findViewById(var1);
      this.a.put(var1, var2);
    }

    return (T) var2;
  }

  public View a() {
    return this.c;
  }
}
