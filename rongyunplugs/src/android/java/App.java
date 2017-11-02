package cordova.plugin.ismartnet.rongcloud;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cordova.plugin.ismartnet.rongcloud.dao.SealAppContext;
import cordova.plugin.ismartnet.rongcloud.dao.UserInfoManager;
import cordova.plugin.ismartnet.rongcloud.model.MyExtensionModule;
import cordova.plugin.ismartnet.rongcloud.retrofit.api.Api;
import cordova.plugin.ismartnet.rongcloud.utils.SharedPreferences;


import cordova.plugin.ismartnet.rongcloud.model.RedExtensionModule;
import cordova.plugin.ismartnet.rongcloud.utils.StringUtil;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.push.RongPushClient;

/**
 * Created by lvping on 2017/8/31.
 */

public class App extends MultiDexApplication {
  private List<Activity> mList = new LinkedList<Activity>();
  private static App mInstance;
  private HashMap<String, Object> tempMap = new HashMap<String, Object>();

  public HashMap<String, Object> getTempMap() {
    return tempMap;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    MultiDex.install(this);
    Fresco.initialize(this);
    UserInfoManager.init(this);
    UserInfoManager.getInstance().initDB();
    //openSealDBIfHasCachedToken();
    mInstance = this;
    if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
      /**
       * 注意：
       *
       * IMKit SDK调用第一步 初始化
       *
       * context上下文
       *
       * 只有两个进程需要初始化，主进程和 push 进程
       */
      //RongPushClient.registerHWPush(this);
      RongPushClient.registerMiPush(this, "2882303761517622042", "5721762239042");
      //MiPushClient.registerPush(this, "2882303761517622042", "5721762239042");
      RongIM.setServerInfo("nav.cn.ronghub.com", "up.qbox.me");
      RongIM.init(this);
      if (StringUtil.isNotEmptyAndNull(SharedPreferences.getInstance(mInstance).getStringValue(Api.RY_USER_TOKEN))) {
        RongIM.connect(SharedPreferences.getInstance(mInstance).getStringValue(Api.RY_USER_TOKEN), SealAppContext.getInstance().getConnectCallback(SharedPreferences.getInstance(mInstance).getStringValue(Api.RY_USER_TOKEN)));
      }
      List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
      IExtensionModule defaultModule = null;
      if (moduleList != null) {
        for (IExtensionModule module : moduleList) {
          if (module instanceof DefaultExtensionModule) {
            defaultModule = module;
            break;
          }
        }
        if (defaultModule != null) {
          RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
          RongExtensionManager.getInstance().registerExtensionModule(new MyExtensionModule());
        }
      }
      RongExtensionManager.getInstance().registerExtensionModule(new RedExtensionModule(mInstance));
    }
  }

  public static App getInstance() {
    return mInstance;
  }

  private void openSealDBIfHasCachedToken() {

    String cachedToken = SharedPreferences.getInstance(this).getStringValue(Api.RY_USER_TOKEN);
    if (!TextUtils.isEmpty(cachedToken)) {
      String current = getCurProcessName(this);
      String mainProcessName = getPackageName();
      if (mainProcessName.equals(current)) {
        UserInfoManager.getInstance().openDB();
      }
    }
  }

  public static String getCurProcessName(Context context) {
    int pid = android.os.Process.myPid();
    ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
      if (appProcess.pid == pid) {
        return appProcess.processName;
      }
    }
    return null;
  }

  public void addActivity(Activity activity) {
    mList.add(activity);
  }

  //关闭每一个list内的activity
  public void exit() {
    try {
      for (Activity activity : mList) {
        if (activity != null)
          activity.finish();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
