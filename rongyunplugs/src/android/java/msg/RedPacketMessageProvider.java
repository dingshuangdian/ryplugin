package cordova.plugin.ismartnet.rongcloud.msg;

import android.app.Activity;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.guoji.tpco.R;

import cordova.plugin.ismartnet.rongcloud.common.RedClient;
import cordova.plugin.ismartnet.rongcloud.bean.CurrentUser;
import cordova.plugin.ismartnet.rongcloud.bean.SendUser;
import cordova.plugin.ismartnet.rongcloud.utils.StringUtil;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by lvping on 2017/9/11.
 */
@ProviderTag(
  messageContent = RedPacketMessage.class,
  showReadState = false
)
public class RedPacketMessageProvider extends IContainerItemProvider.MessageProvider<RedPacketMessage> {
  private Context context;

  public RedPacketMessageProvider() {
  }

  public void bindView(View var1, int var2, RedPacketMessage var3, UIMessage var4) {
    RedPacketMessageProvider.ViewHolder var5 = (RedPacketMessageProvider.ViewHolder) var1.getTag();
    if (var4.getMessageDirection() == Message.MessageDirection.SEND) {
      var5.bri_bg.setBackgroundResource(R.drawable._bg_from_hongbao);
      var5.tv_bri_target.setText("查看红包");
      var5.tv_bri_name.setPadding(28, 0, 0, 0);
    } else {
      var5.bri_bg.setBackgroundResource(R.drawable._bg_to_hongbao);
      var5.tv_bri_target.setText("领取红包");
      var5.tv_bri_name.setPadding(48, 0, 0, 0);
    }
    var5.tv_bri_mess.setText(var3.getContent());
    var5.tv_bri_name.setText("红包");
  }


  @Override
  public Spannable getContentSummary(RedPacketMessage var1) {
    return var1 != null && !StringUtil.isEmpty(var1.getContent().trim()) ? new SpannableString(var1.getContent()) : null;
  }

  public void onItemClick(View var1, int var2, final RedPacketMessage var3, final UIMessage var4) {
    SendUser.sendUserId=var4.getSenderUserId();
    Log.e("SendUser.sendUserId",SendUser.sendUserId);
    SendUser.conversationType=var4.getConversationType();
    Log.e("SendUser.conversation",SendUser.conversationType+"");
    SendUser.targetId=var4.getTargetId();
    Log.e("SendUser.targetId",var4.getTargetId());
    if(var4.getConversationType().getValue()== Conversation.ConversationType.GROUP.getValue()){
      RedClient.openGroupRp((Activity) RedPacketMessageProvider.this.context, CurrentUser.getUserId(),CurrentUser.getName(),CurrentUser.getUserIcon(),var3.getBribery_ID(),var3.getContent());
    }
  }
  public void onItemLongClick(View var1, int var2, RedPacketMessage var3, final UIMessage var4) {

  }

  @Override
  public View newView(Context var1, ViewGroup var2) {
    this.context = var1;
    View var3 = LayoutInflater.from(var1).inflate(R.layout._bribery_item, (ViewGroup) null);
    RedPacketMessageProvider.ViewHolder var4 = new RedPacketMessageProvider.ViewHolder();
    var4.layout = (RelativeLayout) var3.findViewById(R.id.layout);
    var4.tv_bri_mess = (TextView) var3.findViewById(R.id.tv_bri_mess);
    var4.tv_bri_target = (TextView) var3.findViewById(R.id.tv_bri_target);
    var4.tv_bri_name = (TextView) var3.findViewById(R.id.tv_bri_name);
    var4.bri_bg = (RelativeLayout) var3.findViewById(R.id.bri_bg);
    var3.setTag(var4);
    return var3;
  }
  class ViewHolder {
    RelativeLayout layout;
    RelativeLayout bri_bg;
    TextView tv_bri_mess;
    TextView tv_bri_target;
    TextView tv_bri_name;
    ViewHolder() {
    }
  }
}
