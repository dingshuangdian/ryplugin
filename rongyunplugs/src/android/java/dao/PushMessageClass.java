package cordova.plugin.ismartnet.rongcloud.dao;

import android.app.Activity;
import android.util.Log;

import org.apache.cordova.CordovaWebView;
import org.json.JSONObject;


import cordova.plugin.chief.push.PushInit;
import cordova.plugin.ismartnet.rongcloud.RongCloudNav;

/**
 * Created by lvping on 2017/10/31.
 */

public class PushMessageClass {
  private static final  String TAG = "PushMessageClass";

  //接受通知
  public static void ReceiveNotice(Activity mActivity, final RongCloudNav instance, final CordovaWebView webView, JSONObject extrasJson){
    Log.d(TAG," --------->  ReceiveNotice");
    if (webView == null){
      return;
    }
    String format = "window.plugins.pushInit.receiveNotificationInAndroidCallback(%s);";
    final String js = String.format(format, extrasJson.toString());
    Log.e("js",js);
    mActivity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        webView.loadUrl("javascript:" + js);
      }
    });

  }
  //消息透传
  public static void receiveMessage(Activity mActivity, final RongCloudNav instance,final CordovaWebView webView, JSONObject extrasJson){
    Log.d(TAG," --------->  receiveMessage");
    if (webView == null){
      return;
    }
    String format = "window.plugins.pushInit.messageTransmissionInAndroidCallback(%s);";
    final String js = String.format(format, extrasJson.toString());
    mActivity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        webView.loadUrl("javascript:" + js);
      }
    });

  }
  //打开通知
  public static void openReceiveNotice(Activity mActivity, final RongCloudNav instance, final CordovaWebView webView, JSONObject extrasJson){

    Log.d(TAG," --------->  openReceiveNotice");
    if (webView == null){
      Log.d(TAG," --------->  openReceiveNotice");
      return;
    }
    Log.d(TAG," --------->  openReceiveNotice 222："+extrasJson);

    String format = "window.plugins.pushInit.openNotificationInAndroidCallback(%s);";
    final String js = String.format(format, extrasJson.toString());
    mActivity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        webView.loadUrl("javascript:" + js);
      }
    });

  }
}
