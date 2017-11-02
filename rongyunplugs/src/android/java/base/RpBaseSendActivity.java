package cordova.plugin.ismartnet.rongcloud.base;

import android.content.Intent;
import android.os.Bundle;
import cordova.plugin.ismartnet.rongcloud.utils.StringUtil;

/**
 * Created by lvping on 2017/9/11.
 */

public class RpBaseSendActivity extends RpBaseActivity {
  private String envelopesID = "";
  private String envelopeMessage = "";

  public RpBaseSendActivity() {
  }

  protected void onCreate(Bundle var1) {
    super.onCreate(var1);
    if (StringUtil.isEmpty(rongCloudToken)) {
    }
  }

  /**
   *
   * @param var1 envelopesID
   * @param var2 envelopeMessage
   * @param var3 amount
   */
/*  public void jumpPayTypeActivity(String var1,String var2,String var3){
    this.jumpPayTypeActivity(var1, var2,var3,"1");
  }
  public void jumpPayTypeActivity(String var1,String var2,String var3,String var4) {
    //this.envelopesID=var1;
    this.envelopeMessage = var1;
    Intent var7 = new Intent(this, PayTypeActivity.class);
    var7.putExtra("amount", var2);
    this.startActivityForResult(var7, 999);
  }*/
  protected void onActivityResult(int var1, int var2, Intent var3) {
    if (var1 == 999) {
      if (var2 == -1) {
        Intent var4 = new Intent();
        //var4.putExtra("envelopesID",this.envelopesID);
        var4.putExtra("envelopeMessage", this.envelopeMessage);
        var4.putExtra("envelopeName", "红包");
        this.setResult(-1, var4);
        this.finish();
      } else if (var2 == 0) {
        ;
      }
    }

  }

  public static boolean isNumber(String var0) {
    return !StringUtil.isEmpty(var0) ? var0.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$") : false;
  }

  @Override
  public int getLayoutId() {
    return 0;
  }
}
