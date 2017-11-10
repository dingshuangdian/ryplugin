package cordova.plugin.ismartnet.rongcloud.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import cordova.plugin.ismartnet.rongcloud.msg.VideoMessage;
import cordova.plugin.ismartnet.rongcloud.msg.VideoMessageProvider;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by Lvping on 2017/11/5.
 */

public class VideoExtensionModule implements IExtensionModule {
  private Context context;

  @Override
  public void onInit(String s) {
    RongIM.registerMessageType(VideoMessage.class);
    RongIM.registerMessageTemplate(new VideoMessageProvider());
  }
  @Override
  public void onConnect(String s) {

  }

  @Override
  public void onAttachedToExtension(RongExtension rongExtension) {

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
      if (conversationType.equals(Conversation.ConversationType.GROUP)) {
        VideoGroupPlugin videoGroupPlugin = new VideoGroupPlugin();
        arrayList.add(videoGroupPlugin);

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
