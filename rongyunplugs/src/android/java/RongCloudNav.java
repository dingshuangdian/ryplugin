package cordova.plugin.ismartnet.rongcloud;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import cordova.plugin.chief.push.MobilePhoneType;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import cordova.plugin.ismartnet.rongcloud.bean.GroupMsg;
import cordova.plugin.ismartnet.rongcloud.bean.Permiss;
import cordova.plugin.ismartnet.rongcloud.dao.PushMessageClass;
import cordova.plugin.ismartnet.rongcloud.dao.SealAppContext;
import cordova.plugin.ismartnet.rongcloud.dao.UserInfoManager;
import cordova.plugin.ismartnet.rongcloud.model.RedExtensionModule;
import cordova.plugin.ismartnet.rongcloud.retrofit.api.Api;
import cordova.plugin.ismartnet.rongcloud.retrofit.callback.MyCallBack;
import cordova.plugin.ismartnet.rongcloud.utils.LoadDialog;
import cordova.plugin.ismartnet.rongcloud.utils.RongGenerate;
import cordova.plugin.ismartnet.rongcloud.utils.StringUtil;
import cordova.plugin.ismartnet.rongcloud.utils.ToastUtils;
import cordova.plugin.ismartnet.rongcloud.retrofit.HttpUtil;
import cordova.plugin.ismartnet.rongcloud.utils.SharedPreferences;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * This class echoes a string called from JavaScript.
 */
public class RongCloudNav extends CordovaPlugin {
  private final String TAG = "RongCloudLibPlugin";
  private static Activity cordovaActivity;
  private static Context mContext;
  private android.os.Handler handler;
  private Boolean showAnnouncement = true;
  private Boolean showRedPackage = false;
  private String rcTokenId;
  public static RongCloudNav instance;
  public static CordovaWebView cordovaWebView; //异步线程池
  private ExecutorService threadPool = Executors.newFixedThreadPool(1);
  //手机结果类型
  public static String phoneType = null;


  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
    cordovaActivity = cordova.getActivity();
    this.cordovaWebView = webView;
    this.mContext = cordova.getActivity().getApplicationContext();
    handler = new Handler(Looper.getMainLooper());

    webView.getContext();
  }

  public RongCloudNav() {
    instance = this;
  }

  @Override
  public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (action.equals("connectWithToken")) {
      cordova.getThreadPool().execute(new Runnable() {
        @Override
        public void run() {
          connect(args, callbackContext);
        }
      });
    }

    if (action.equals("pushConversionView")) {
          getGroupChat(args, callbackContext);

    }
    if (action.equals("startWithHost")) {
      getUrl(args, callbackContext);
      new HttpUtil.SingletonBuilder(cordovaActivity, SharedPreferences.getInstance(cordovaActivity).getStringValue(Api.CHEPU_URL))
        .converterFactory(GsonConverterFactory.create())
        .build();
    }
    if (action.equals("disconnect")) {
      RongIM.getInstance().disconnect();
      SharedPreferences.getInstance(cordovaActivity).putStringValue(Api.RY_USER_TOKEN, "");
    }
    threadPool.execute(new Runnable() {
      @Override
      public void run() {
        try {
           phoneType = MobilePhoneType.getType(cordovaActivity);

          Method method = RongCloudNav.class.getDeclaredMethod(action, JSONArray.class, CallbackContext.class, String.class, CordovaInterface.class);
          method.invoke(RongCloudNav.this, args, callbackContext, phoneType, cordova);

        } catch (NoSuchMethodException noSuchMethodException) {
          Log.d(TAG, noSuchMethodException.toString());
        } catch (Exception e) {
          Log.d(TAG, e.toString());
        }
      }
    });
    return true;
  }

  private void getUrl(JSONArray args, CallbackContext callbackContext) {
    JSONObject jsonObject = null;
    try {
      jsonObject = args.getJSONObject(0);
      String getUserInfo = jsonObject.optString("getUserInfo");
      String[] url = getUserInfo.split("TPS", 2);
      SharedPreferences.getInstance(cordovaActivity).putStringValue(Api.CHEPU_URL, url[0]);
      SharedPreferences.getInstance(cordovaActivity).putStringValue(Api.GETUSER_INFO, "TPS" + url[1]);
      Log.e("split", url[0] + "TPS" + url[1]);
      String getGroupInfo = jsonObject.optString("getGroupInfo");
      String[] url1 = getGroupInfo.split("TPS", 2);
      SharedPreferences.getInstance(cordovaActivity).putStringValue(Api.GETGROUP_INFO, "TPS" + url1[1]);
      Log.e("split", url[0] + "TPS" + url1[1]);
      String groupChat = jsonObject.optString("groupChat");
      String[] url2 = groupChat.split("TPS", 2);
      SharedPreferences.getInstance(cordovaActivity).putStringValue(Api.GROUP_CHAT, "TPS" + url2[1]);
      Log.e("split", url[0] + "TPS" + url2[1]);
      String createBonus = jsonObject.optString("createBonus");
      String[] url3 = createBonus.split("TPS", 2);
      SharedPreferences.getInstance(cordovaActivity).putStringValue(Api.CREATE_BONUS, "TPS" + url3[1]);
      Log.e("split", url[0] + "TPS" + url3[1]);
      String receiveBonus = jsonObject.optString("receiveBonus");
      String[] url4 = receiveBonus.split("TPS", 2);
      SharedPreferences.getInstance(cordovaActivity).putStringValue(Api.RECEIVE_BONUS, "TPS" + url4[1]);
      Log.e("split", url[0] + "TPS" + url4[1]);
      String isReceiveBonus = jsonObject.optString("isReceiveBonus");
      String[] url5 = isReceiveBonus.split("TPS", 2);
      SharedPreferences.getInstance(cordovaActivity).putStringValue(Api.IS_RECEIVE_BONUS, "TPS" + url5[1]);
      Log.e("split", url[0] + "TPS" + url5[1]);
      String qryReceiveBonusRecord = jsonObject.optString("qryReceiveBonusRecord");
      String[] url6 = qryReceiveBonusRecord.split("TPS", 2);
      SharedPreferences.getInstance(cordovaActivity).putStringValue(Api.QRYRECEIVE_BONUS, "TPS" + url6[1]);
      Log.e("split", url[0] + "TPS" + url6[1]);
      String admin = jsonObject.optString("getBuserAuth");
      String[] url7 = admin.split("TPS", 2);
      SharedPreferences.getInstance(cordovaActivity).putStringValue(Api.ADMIN, "TPS" + url7[1]);
      Log.e("split", url[0] + "TPS" + url7[1]);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  /**
   * 连接融云服务器
   *
   * @param
   * @param callbackContext
   */
  void connect(final JSONArray args, final CallbackContext callbackContext) {
    try {
      JSONObject jsonObject = args.getJSONObject(0);
      //本地服务器Token
      String tokenId = jsonObject.optString("tokenId");
      SharedPreferences.getInstance(cordovaActivity).putStringValue(Api.BD_TOKEN_ID, tokenId);
      Log.e("tokenId", tokenId);
      //融云服务器Token
      rcTokenId = jsonObject.optString("RCtokenId");
      Log.e("rcTokenId", rcTokenId);
      if (!TextUtils.isEmpty(rcTokenId)) {
        RongIM.connect(rcTokenId, SealAppContext.getInstance().getConnectCallback(rcTokenId));
        handler.postDelayed(new Runnable() {
          @Override
          public void run() {
            getMyUserInfo();
           getGroupMsg();
          }
        }, 800);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public void disconnect(final JSONArray args, final CallbackContext context) {
    RongIM.getInstance().disconnect();
  }

  /**
   * 连接成功获取用户信息
   */
  public void getMyUserInfo() {
    RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
      @Override
      public UserInfo getUserInfo(String userid) {
        UserInfoManager.getInstance().getUserInfo(userid);
        return null;
      }
    }, true);
  }

  /**
   * 获取群信息
   */
  private void getGroupMsg() {
    RongIM.setGroupInfoProvider(new RongIM.GroupInfoProvider() {
      @Override
      public Group getGroupInfo(final String groupId) {
        UserInfoManager.getInstance().getGroupInfo(groupId);
        return null;
      }
    }, true);
  }
/*  private void getGroupMembers(){
    RongIM.getInstance().setGroupMembersProvider(new RongIM.IGroupMembersProvider() {
      @Override
      public void getGroupMembers(String s, RongIM.IGroupMemberCallback iGroupMemberCallback) {
        UserInfoManager.getInstance().getGroupMember(s);
      }
    });
  }*/

/*  */

  /**
   * 获取群组成员信息
   */
  private void getGroupMembers(String s) {
    LoadDialog.show(cordovaActivity);
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("tokenId", SharedPreferences.getInstance(cordovaActivity).getStringValue(Api.BD_TOKEN_ID));
    map.put("taskId", s);
    Call<GroupMsg> groupMsgCall = HttpUtil.getService().getGroupMembers(SharedPreferences.getInstance(cordovaActivity).getStringValue(Api.GROUP_CHAT), map);
    groupMsgCall.enqueue(new MyCallBack<GroupMsg>() {
      @Override
      public void onSuccess(Response<GroupMsg> response) {
        //List<UserInfo> userInfos = new ArrayList<>();
        if (response.body().getResult() != null) {
          for (GroupMsg.ResultBean groupMember : response.body().getResult()) {
            if (groupMember != null) {
              if (StringUtil.isEmptyAndNull(groupMember.getHeadImg())) {
                groupMember.setHeadImg(RongGenerate.generateDefaultAvatar(groupMember.getUName(), groupMember.getUId()));
              }
              String userIconn = groupMember.getHeadImg();
              UserInfo userInfo = new UserInfo(groupMember.getUId(), groupMember.getUName(), Uri.parse(userIconn));
              //userInfos.add(userInfo);
              RongIM.getInstance().refreshUserInfoCache(userInfo);
            }
          }
        }
        LoadDialog.dismiss(cordovaActivity);
      }

      @Override
      public void onFail(String message) {
        LoadDialog.dismiss(cordovaActivity);
        ToastUtils.show(cordovaActivity, message);
      }
    });
  }

  private void getPermiss(String targetId) {

    LoadDialog.show(cordovaActivity);
    HashMap<String, String> map = new HashMap<>(3);
    map.put("tokenId", SharedPreferences.getInstance(cordovaActivity).getStringValue(Api.BD_TOKEN_ID));
    map.put("taskId", targetId);
    map.put("bUserId", SharedPreferences.getInstance(cordovaActivity).getStringValue(Api.RY_USER_ID));
    Log.e("bUserId", SharedPreferences.getInstance(cordovaActivity).getStringValue(Api.RY_USER_ID));
    Call<Permiss> permissCall = HttpUtil.getService().getPermiss(SharedPreferences.getInstance(cordovaActivity).getStringValue(Api.ADMIN), map);
    permissCall.enqueue(new MyCallBack<Permiss>() {
      @Override
      public void onSuccess(Response<Permiss> response) {
        if (response.body().getResult().getBonusAuth().equals("0")) {
          SharedPreferences.getInstance(cordovaActivity).putBooleanValue(Api.SHOWREDPACKAGE, showRedPackage);
        } else if (response.body().getResult().getBonusAuth().equals("1")) {
          showRedPackage = true;
          SharedPreferences.getInstance(cordovaActivity).putBooleanValue(Api.SHOWREDPACKAGE, showRedPackage);
        }
        if (response.body().getResult().getAnnouncementAuth().equals("0")) {
          showAnnouncement = false;
          SharedPreferences.getInstance(cordovaActivity).putBooleanValue(Api.SHOWANNOUNCEMENT, showAnnouncement);
        } else {
          showAnnouncement = true;
          SharedPreferences.getInstance(cordovaActivity).putBooleanValue(Api.SHOWANNOUNCEMENT, showAnnouncement);
        }
      }

      @Override
      public void onFail(String message) {
        LoadDialog.dismiss(cordovaActivity);
        ToastUtils.show(cordovaActivity, message);
      }
    });
  }
  /**
   * 启动群聊界面
   *
   * @param args
   * @param callbackContext
   */
  public void getGroupChat(final JSONArray args, final CallbackContext callbackContext) {
    try {
      JSONObject jsonObject = args.getJSONObject(0);
      String targetId = jsonObject.optString("targetId");
      Log.e("targetId", targetId);
      String conversationTitle = jsonObject.optString("conversationTitle");
      getGroupMembers(targetId);
      getPermiss(targetId);
      PushMessageClass.OpenChat(cordovaActivity, instance, cordovaWebView,targetId,conversationTitle);
      //RongIM.getInstance().startConversation(mContext, Conversation.ConversationType.GROUP, targetId, conversationTitle);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
  /**
   * 接收通知
   *
   * @param extrasJson
   */
  public void obtainNotificationReceive(JSONObject extrasJson) {
    if (instance == null) {
      return;
    }
    PushMessageClass.receiveMessage(cordovaActivity, instance, cordovaWebView, extrasJson);
  }
  /**
   * 打开通知
   *
   * @param extrasJson
   */
  public void openNotificationReceive(JSONObject extrasJson) {
    if (instance == null) {
      return;
    }
    PushMessageClass.openReceiveNotice(cordovaActivity, instance, cordovaWebView, extrasJson);

  }
  @Override
  public void onDestroy() {
    super.onDestroy();
    cordovaActivity = null;
    instance = null;
    RongExtensionManager.getInstance().unregisterExtensionModule(new RedExtensionModule(cordovaActivity));
  }
  @Override
  public void onPause(boolean multitasking) {
    super.onPause(multitasking);
    LoadDialog.dismiss(cordovaActivity);
  }
}
