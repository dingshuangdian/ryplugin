package cordova.plugin.ismartnet.rongcloud.view;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by lvping on 2017/9/14.
 */

public class NoUnderClickableSpan extends ClickableSpan{
  public NoUnderClickableSpan() {
  }
  @Override
  public void onClick(View widget) {

  }
  public void updateDrawState(TextPaint var1) {
    super.updateDrawState(var1);
    var1.setUnderlineText(false);
    var1.setColor(-65536);
  }
}
