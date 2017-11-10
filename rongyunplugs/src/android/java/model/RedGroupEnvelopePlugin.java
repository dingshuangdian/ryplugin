package cordova.plugin.ismartnet.rongcloud.model;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import cordova.plugin.ismartnet.rongcloud.activity.SendGroupEnvelopesActivityRp;
import cordova.plugin.ismartnet.rongcloud.bean.CurrentUser;
import cordova.plugin.ismartnet.rongcloud.utils.ResourcesUtils;
import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;
/**
 * Created by lvping on 2017/9/15.
 */

public class RedGroupEnvelopePlugin implements IPluginModule{
  private Conversation.ConversationType conversationType;
  private String targetId;
  public RedGroupEnvelopePlugin(){

  }
  @Override
  public Drawable obtainDrawable(Context context) {
  return ContextCompat.getDrawable(context, ResourcesUtils.getDrawableId(context,"red_package_selector"));
  }

  @Override
  public String obtainTitle(Context context) {
    return "红包";
  }

  @Override
  public void onClick(Fragment fragment, RongExtension rongExtension) {
    this.conversationType = rongExtension.getConversationType();
    this.targetId = rongExtension.getTargetId();
    Intent var3 = new Intent(rongExtension.getContext(), SendGroupEnvelopesActivityRp.class);
    var3.putExtra("TargetId", rongExtension.getTargetId());
    var3.putExtra("user_id", CurrentUser.getUserId());
    var3.putExtra("user_name", CurrentUser.getName());
    var3.putExtra("user_icon", CurrentUser.getUserIcon());
    rongExtension.startActivityForPluginResult(var3, 51, this);

  }

  @Override
  public void onActivityResult(int i, int i1, Intent intent) {
   /* if(i1 == -1) {
      this.sendRpMessage(intent);
    }*/
  }
/*private void sendRpMessage(Intent var1) {
    String var2 = var1.getStringExtra("envelopesID");
    String var3 = var1.getStringExtra("envelopeMessage");
    String var4 = var1.getStringExtra("envelopeName");
    RedPacketMessage var5 = RedPacketMessage.obtain(var2, var4, var3, "[" + var4 + "] " + var3);
    if(RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
      RongIM.getInstance().getRongIMClient().sendMessage(this.conversationType, this.targetId, var5, "您收到了一条消息", (String)null, new RongIMClient.SendMessageCallback() {
        public void onError(Integer var1, RongIMClient.ErrorCode var2) {
          Log.e("group","send a group rp error");
        }

        public void onSuccess(Integer var1) {
          Log.e("group","send a group rp success");
        }
      });
    }
  }*/
}
