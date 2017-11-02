package cordova.plugin.ismartnet.rongcloud.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;


import cordova.plugin.ismartnet.rongcloud.App;
import cordova.plugin.ismartnet.rongcloud.retrofit.api.Api;
import cordova.plugin.ismartnet.rongcloud.utils.SharedPreferences;
import cordova.plugin.ismartnet.rongcloud.inter.CPa;
import cordova.plugin.ismartnet.rongcloud.utils.StringUtil;
import cordova.plugin.ismartnet.rongcloud.view.ActionBarView;

/**
 * Created by lvping on 2017/9/11.
 */

public abstract class RpBaseActivity extends FragmentActivity implements CPa{
  public Activity context;
  protected String userid;
  protected String username;
  protected String usericon;
  public ActionBarView actionBarView;
  public static String rongCloudToken;
  public float maxLimitMoney = 200.0F;

  public RpBaseActivity() {
  }

  protected void onCreate(Bundle var1) {
    this.requestWindowFeature(1);
    super.onCreate(var1);
    this.setContentView(this.getLayoutId());
    this.context = this;
    this.userid = this.getIntent().getStringExtra("user_id");
    this.username = this.getIntent().getStringExtra("user_name");
    this.usericon = this.getIntent().getStringExtra("user_icon");
    if(StringUtil.isNotEmpty(this.userid)) {
      SharedPreferences.getInstance(context).putStringValue(Api.MY_USER_ID,userid);
    }

    this.initView();
    this.initListener();
    this.initData(this.getIntent().getExtras());
    App.getInstance().addActivity(this);
  }
  public abstract int getLayoutId();
  public void initView() {
  }
  protected void initData(Bundle var1) {
  }

  public void onClick(int var1) {
  }
  public void initListener() {
  }
  public void onClick(View var1) {
    this.onClick(var1.getId());
  }
}
