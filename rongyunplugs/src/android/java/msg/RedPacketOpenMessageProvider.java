package cordova.plugin.ismartnet.rongcloud.msg;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guoji.tpco.R;

import cordova.plugin.ismartnet.rongcloud.bean.CurrentUser;
import cordova.plugin.ismartnet.rongcloud.utils.StringUtil;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;

/**
 * Created by lvping on 2017/9/14.
 */
@ProviderTag(
  messageContent = RedPacketOpenedMessage.class,
  showPortrait = false,
  centerInHorizontal = true,
  showSummaryWithName = false
)
public class RedPacketOpenMessageProvider extends IContainerItemProvider.MessageProvider<RedPacketOpenedMessage> {
  public RedPacketOpenMessageProvider() {
  }

  public void bindView(View var1, int var2, RedPacketOpenedMessage var3, UIMessage var4) {
    RedPacketOpenMessageProvider.ViewHolder var5 = (RedPacketOpenMessageProvider.ViewHolder) var1.getTag();
    if (var3 != null) {
      //当前id
      String var6 = CurrentUser.getUserId();
      Log.e("SendUser.sendUserId", var6);
      //接收人id
      String var7 = var3.getReceiveUserId();
      Log.e("var7", var7);
      //发红包人名称
      String var8 = var3.getSendUserName();
      if(StringUtil.isEmptyAndNull(var8)){
        var8=var6;
      }
      Log.e("var8", var8);
      //接收人名称
      String var9 = var3.getReceiveUserName();
      if(StringUtil.isEmptyAndNull(var8)){
        var9=var7;
      }
      Log.e("var9", var9);
     if(TextUtils.isEmpty(var6) || TextUtils.isEmpty(var7)) {
        Log.i("JrmfRedPacketOpened", "idddd不能为空!!!");
        return ;
      }
      String tipMsg;
      SpannableString var11;
      if (var6.equals(var7)) {
        tipMsg = "你领取了" + var8 + "的红包";
      } else {
        tipMsg = var9 + "领取了你的红包";

      }
      var11 = new SpannableString(tipMsg);
      var11.setSpan(new ForegroundColorSpan(Color.parseColor("#fa9d3b")), tipMsg.length() - 2, tipMsg.length(), 33);
      var5.packet_message.setText(var11);
    }
  }

  public Spannable getContentSummary(RedPacketOpenedMessage var1) {
    if (var1 != null) {
      //红包人id
      String var6 = CurrentUser.getUserId();
      Log.e("SendUser.sendUserId", var6);
      //接收人id
      String var7 = var1.getReceiveUserId();
      //发红包人名称
      String var8 = var1.getSendUserName();
      if(StringUtil.isEmptyAndNull(var8)){
        var8=var6;
      }
      //接收人名称
      String var9 = var1.getReceiveUserName();
      if(StringUtil.isEmptyAndNull(var8)){
        var9=var7;
      }
      if(TextUtils.isEmpty(var6) || TextUtils.isEmpty(var7)) {
        Log.i("JrmfRedPacketOpened", "id不能为空!!!");
        return null;
      }
      String tipMsg;
      if (var6.equals(var7)) {
        tipMsg = "你领取了" + var8 + "的红包";
      } else {
        tipMsg = var9 + "领取了你的红包";

      }
      return new SpannableString(tipMsg);

    }
    return null;
  }

  public void onItemClick(View var1, int var2, RedPacketOpenedMessage var3, UIMessage var4) {

  }

  public void onItemLongClick(View var1, int var2, RedPacketOpenedMessage var3, UIMessage var4) {
  }

  public View newView(Context var1, ViewGroup var2) {
    View var3 = LayoutInflater.from(var1).inflate(R.layout._open_packet, (ViewGroup) null);
    RedPacketOpenMessageProvider.ViewHolder var4 = new RedPacketOpenMessageProvider.ViewHolder();
    var4.packet_message = (TextView) var3.findViewById(R.id.packet_message);
    var3.setTag(var4);
    return var3;
  }

  class ViewHolder {
    TextView packet_message;

    ViewHolder() {
    }
  }
}
