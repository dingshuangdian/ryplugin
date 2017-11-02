package cordova.plugin.ismartnet.rongcloud.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cordova.plugin.ismartnet.rongcloud.bean.CurrentUser;
import cordova.plugin.ismartnet.rongcloud.msg.RedPacketMessage;
import cordova.plugin.ismartnet.rongcloud.msg.RedPacketMessageProvider;
import cordova.plugin.ismartnet.rongcloud.msg.RedPacketOpenMessageProvider;
import cordova.plugin.ismartnet.rongcloud.msg.RedPacketOpenedMessage;
import cordova.plugin.ismartnet.rongcloud.retrofit.api.Api;
import cordova.plugin.ismartnet.rongcloud.utils.SharedPreferences;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.widget.provider.FilePlugin;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by lvping on 2017/9/8.
 */

public class RedExtensionModule implements IExtensionModule {
  private String lastName;
  private String lastAvatar;
  private String targetId;
  private Context context;

  public RedExtensionModule(Context mContext) {
    context = mContext;
  }

  @Override
  public void onInit(String s) {
    RongIM.registerMessageType(RedPacketMessage.class);
    RongIM.registerMessageType(RedPacketOpenedMessage.class);
    RongIM.getInstance().registerMessageTemplate(new RedPacketMessageProvider());
    RongIM.getInstance().registerMessageTemplate(new RedPacketOpenMessageProvider());
    //EventBus.getDefault().register(this);
  }

  @Override
  public void onConnect(String s) {

  }

  @Override
  public void onAttachedToExtension(RongExtension rongExtension) {
    this.targetId = rongExtension.getTargetId();

  }

  @Override
  public void onDetachedFromExtension() {

  }

  @Override
  public void onReceivedMessage(Message message) {

  }

  @Override
  public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
    if (conversationType == null) {
      return null;
    } else {
      ArrayList arrayList = new ArrayList();
      if (conversationType.equals(Conversation.ConversationType.GROUP) && SharedPreferences.getInstance(context).getBooleanValue(Api.SHOWREDPACKAGE)) {
        RedGroupEnvelopePlugin redGroupEnvelopePlugin = new RedGroupEnvelopePlugin();
        arrayList.add(redGroupEnvelopePlugin);

      }
      return arrayList;
    }
  }

  @Override
  public List<IEmoticonTab> getEmoticonTabs() {

    return null;
  }

  @Override
  public void onDisconnect() {

  }
}
