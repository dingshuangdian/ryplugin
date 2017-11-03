package cordova.plugin.ismartnet.rongcloud.dao;

import android.content.Context;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import cordova.plugin.ismartnet.rongcloud.App;
import cordova.plugin.ismartnet.rongcloud.RongCloudNav;
import cordova.plugin.ismartnet.rongcloud.retrofit.api.Api;
import cordova.plugin.ismartnet.rongcloud.utils.SharedPreferences;
import io.rong.common.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.ConversationKey;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.MentionedInfo;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.RecallNotificationMessage;
/**
 * 融云相关监听 事件集合类
 * Created by AMing on 16/1/7.
 * Company RongCloud
 */
public class SealAppContext implements RongIMClient.OnReceiveMessageListener, RongIMClient.ConnectionStatusListener, RongIM.UserInfoProvider, RongIM.GroupInfoProvider, RongIM.GroupUserInfoProvider {
  private final static String TAG = "SealAppContext";
  private Context mContext;
  private static SealAppContext mRongCloudInstance;
  private static RongCloudNav rongCloudNav;

  public SealAppContext(Context mContext) {
    this.mContext = mContext;
    initListener();
  }

  /**
   * 初始化 RongCloud.
   *
   * @param context 上下文。
   */
  public static void init(Context context) {

    if (mRongCloudInstance == null) {
      synchronized (SealAppContext.class) {

        if (mRongCloudInstance == null) {
          mRongCloudInstance = new SealAppContext(context);
        }
        if (rongCloudNav == null) {
          rongCloudNav = new RongCloudNav();
        }
      }
    }

  }

  /**
   * 获取RongCloud 实例。
   *
   * @return RongCloud。
   */
  public static SealAppContext getInstance() {
    return mRongCloudInstance;
  }

  public Context getContext() {
    return mContext;
  }

  /**
   * init 后就能设置的监听
   */
  private void initListener() {
    RongIM.setOnReceiveMessageListener(this);
    RongIM.setUserInfoProvider(this, true);
    RongIM.setGroupInfoProvider(this, true);
  }

  @Override
  public boolean onReceived(Message message, int i) {
    if (message.getConversationType().getName().equals("group")) {
      Log.e("getConversationType()", message.getConversationType().getName());

      IContainerItemProvider.MessageProvider provider = RongContext.getInstance().getMessageTemplate(message.getContent().getClass());
      if (provider != null) {
        Spannable content = provider.getContentSummary(message.getContent());
        ConversationKey targetKey = ConversationKey.obtain(message.getTargetId(), message.getConversationType());
        if (targetKey == null) {
          RLog.e("RongNotificationManager", "onReceiveMessageFromApp targetKey is null");
        }

        RLog.i("RongNotificationManager", "onReceiveMessageFromApp start");
        if (content == null) {
          RLog.i("RongNotificationManager", "onReceiveMessageFromApp Content is null. Return directly.");
        } else {
          UserInfo userInfo;
          String notificationContent = null;
          String userName = null;
          userInfo = RongUserInfoManager.getInstance().getUserInfo(message.getSenderUserId());
          if (userInfo != null) {
            userName = userInfo.getName();
          } else {
            userName = message.getSenderUserId();
          }
          if (!TextUtils.isEmpty(userName)) {
            if (this.isMentionedMessage(message)) {
              if (TextUtils.isEmpty(message.getContent().getMentionedInfo().getMentionedContent())) {
                notificationContent = this.mContext.getString(io.rong.imkit.R.string.rc_message_content_mentioned) + userName + " : " + content.toString();
              } else {
                notificationContent = message.getContent().getMentionedInfo().getMentionedContent();
              }
            } else if (message.getContent() instanceof RecallNotificationMessage) {
              notificationContent = content.toString();
            } else {
              notificationContent = userName + " : " + content.toString();
            }
          }
          Map<String, String> extrasMap = new HashMap<>();
          //群id
          extrasMap.put("tId", message.getTargetId());
          extrasMap.put("id", "");
          extrasMap.put("mId", message.getMessageId() + "");
          extrasMap.put("fId", message.getSenderUserId());
          extrasMap.put("oName", message.getObjectName());
          extrasMap.put("cType", "GROUP");
          JSONObject extrasObject = getNotificationObject(notificationContent, extrasMap);//转换数据类型
          Log.e(TAG, message.getContent().toString());
          rongCloudNav.obtainNotificationReceive(extrasObject);
        }
      }
      return false;
    }
    return true;
  }


  public RongIMClient.ConnectCallback getConnectCallback(final String rcTokenId) {
    RongIMClient.ConnectCallback connectCallback = new RongIMClient.ConnectCallback() {
      @Override
      public void onTokenIncorrect() {
        Log.e(TAG, "ConnectCallback connect onTokenIncorrect");
      }

      @Override
      public void onSuccess(String s) {
        Log.e(TAG, "ConnectCallback connect onSuccess");
        SharedPreferences.getInstance(mContext).putStringValue(Api.RY_USER_ID, s);
        SharedPreferences.getInstance(mContext).putStringValue(Api.RY_USER_TOKEN, rcTokenId);

      }

      @Override
      public void onError(final RongIMClient.ErrorCode e) {
        Log.e(TAG, "ConnectCallback connect onError-ErrorCode=" + e);
      }
    };
    return connectCallback;
  }

  @Override
  public void onChanged(ConnectionStatus connectionStatus) {
    Log.d(TAG, "ConnectionStatus onChanged = " + connectionStatus.getMessage());
    if (connectionStatus.equals(ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {
      App.getInstance().exit();
    }
  }

  private boolean isMentionedMessage(Message message) {
    MentionedInfo mentionedInfo = message.getContent().getMentionedInfo();
    return mentionedInfo != null && (mentionedInfo.getType().equals(MentionedInfo.MentionedType.ALL) || mentionedInfo.getType().equals(MentionedInfo.MentionedType.PART) && mentionedInfo.getMentionedUserIdList() != null && mentionedInfo.getMentionedUserIdList().contains(RongIMClient.getInstance().getCurrentUserId()));
  }

  private static JSONObject getNotificationObject(String alert, Map<String, String> extras) {
    JSONObject data = new JSONObject();
    try {
      data.put("alert", alert);
      JSONObject jExtras = new JSONObject();
      for (Map.Entry<String, String> entry : extras.entrySet()) {
        jExtras.put(entry.getKey(), entry.getValue());
      }
      data.put("rc", jExtras);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return data;
  }

  @Override
  public UserInfo getUserInfo(String s) {
    return null;
  }

  @Override
  public Group getGroupInfo(String s) {
    return null;
  }

  @Override
  public GroupUserInfo getGroupUserInfo(String s, String s1) {
    return null;
  }
}





