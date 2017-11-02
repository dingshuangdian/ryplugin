package cordova.plugin.ismartnet.rongcloud.receiver;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.guoji.tpco.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cordova.plugin.chief.push.PushInit;
import cordova.plugin.ismartnet.rongcloud.App;
import cordova.plugin.ismartnet.rongcloud.RongCloudNav;
import io.rong.common.SystemUtils;
import io.rong.push.RongPushClient;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

public class SealNotificationReceiver extends PushMessageReceiver {
  private static String TAG = "NotificationReceiver";
  private RongCloudNav mPushInit;

  @Override
  public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {
    if (message.getConversationType().getName().equals("group")) {
      if (mPushInit == null) {
        mPushInit = new RongCloudNav();
      }
      Log.e(TAG, "Push消息");
      Map<String, String> extrasMap = new HashMap<>();
      //群id
      extrasMap.put("tId", message.getTargetId());
      extrasMap.put("tName", message.getTargetUserName());
      extrasMap.put("id", "");
      extrasMap.put("mId", message.getPushId() + "");
      extrasMap.put("fId", message.getSenderId());
      extrasMap.put("oName", message.getObjectName());
      extrasMap.put("cType", "GROUP");
      JSONObject extrasObject = getNotificationObject(message.getPushContent(), extrasMap);//转换数据类型
      mPushInit.obtainNotificationReceive(extrasObject);
      RongPushClient.sendNotification(context, message);
    }
    return true;
  }

  @Override
  public boolean onNotificationMessageClicked(final Context context, final PushNotificationMessage message) {
    Log.e(TAG, "点击通知");
    //上报点击消息  MiPushClient.reportMessageClicked();
    // Log.v(TAG, "处理数据");
   /* Intent mIntent = new Intent(context, MainActivity.class);
    mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(mIntent);*/
    if (mPushInit == null) {
      mPushInit = new RongCloudNav();
    }
    Map<String, String> extrasMap = new HashMap<>();
    //群id
    extrasMap.put("tId", message.getTargetId());
    extrasMap.put("tName", message.getTargetUserName());
    extrasMap.put("id", "");
    extrasMap.put("mId", message.getPushId() + "");
    extrasMap.put("fId", message.getSenderId());
    extrasMap.put("oName", message.getObjectName());
    extrasMap.put("cType", "GROUP");
    JSONObject extrasObject = getNotificationObject(message.getPushContent(), extrasMap);//转换数据类型
    //如果进程存在
    if (SystemUtils.isAppRunning(context, App.getCurProcessName(context))) {
      if (!SystemUtils.isInBackground(context)) {
        Log.e(TAG, "------------------------->前台存活");
        mPushInit.openNotificationReceive(extrasObject);
      } else {
        Log.e(TAG, "------------------------->后台存活");
        Log.e(TAG, message.getPushContent());
        Intent mIntent = new Intent(context, MainActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mIntent);
        mPushInit.openNotificationReceive(extrasObject);
      }
    } else {
      Log.e(TAG, "------------------------->重新启动");
      Intent mIntent = new Intent(context, MainActivity.class);
      mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(mIntent);
      mPushInit.openNotificationReceive(extrasObject);
    }
    return true;
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

}
