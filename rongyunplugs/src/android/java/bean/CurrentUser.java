package cordova.plugin.ismartnet.rongcloud.bean;

import cordova.plugin.ismartnet.rongcloud.utils.StringUtil;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Created by lvping on 2017/9/11.
 */

public class CurrentUser {
  public CurrentUser() {
}

  public static String getUserId() {
    return RongIMClient.getInstance().getCurrentUserId();
  }

  public static String getName() {
    UserInfo var0 = RongUserInfoManager.getInstance().getUserInfo(getUserId());
    return var0 != null?(var0.getName() != null?var0.getName():""):"";
  }

  public static String getUserIcon() {
    UserInfo var0 = RongUserInfoManager.getInstance().getUserInfo(getUserId());
    return var0 != null?(var0.getPortraitUri().toString() != null?var0.getPortraitUri().toString():""):"";
  }

  public static String getNameById(String var0) {
    if(StringUtil.isEmpty(var0)) {
      return "";
    } else if(RongUserInfoManager.getInstance() == null) {
      return "";
    } else {
      UserInfo var1 = RongUserInfoManager.getInstance().getUserInfo(var0);
      return var1 != null?(var1.getName() != null?var1.getName():""):"";
    }
  }

  public static String getUserIconById(String var0) {
    if(StringUtil.isEmpty(var0)) {
      return "";
    } else if(RongUserInfoManager.getInstance() == null) {
      return "";
    } else {
      UserInfo var1 = RongUserInfoManager.getInstance().getUserInfo(var0);
      return var1 != null?(var1.getPortraitUri().toString() != null?var1.getPortraitUri().toString():""):"";
    }
  }
}
