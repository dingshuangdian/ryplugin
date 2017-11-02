package cordova.plugin.ismartnet.rongcloud.model;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.RongExtension;
import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imkit.plugin.DefaultLocationPlugin;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.ImagePlugin;
import io.rong.imkit.widget.provider.FilePlugin;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by lvping on 2017/10/13.
 */

public class MyExtensionModule extends DefaultExtensionModule {

  @Override
  public void onInit(String appKey) {
    super.onInit(appKey);
  }

  @Override
  public void onDisconnect() {
    super.onDisconnect();
  }

  @Override
  public void onConnect(String token) {
    super.onConnect(token);
  }

  @Override
  public void onAttachedToExtension(RongExtension extension) {
    super.onAttachedToExtension(extension);
  }

  @Override
  public void onDetachedFromExtension() {
    super.onDetachedFromExtension();
  }

  @Override
  public void onReceivedMessage(Message message) {
    super.onReceivedMessage(message);
  }

  @Override
  public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
    List<IPluginModule> pluginModules = super.getPluginModules(conversationType);
    if (pluginModules != null) {
      for (IPluginModule module : pluginModules) {
        if (module instanceof FilePlugin) {
          pluginModules.remove(module);
          break;
        }
      }
    }
    return pluginModules;
  }
  @Override
  public List<IEmoticonTab> getEmoticonTabs() {
    return super.getEmoticonTabs();
  }
}
