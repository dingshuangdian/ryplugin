package cordova.plugin.ismartnet.rongcloud.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.guoji.tpco.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import cordova.plugin.ismartnet.rongcloud.retrofit.callback.MyCallBack;
import cordova.plugin.ismartnet.rongcloud.retrofit.HttpUtil;
import cordova.plugin.ismartnet.rongcloud.retrofit.api.Api;
import cordova.plugin.ismartnet.rongcloud.bean.Receive;
import cordova.plugin.ismartnet.rongcloud.utils.DrawableUtil;
import cordova.plugin.ismartnet.rongcloud.utils.NoDoubleClickUtils;
import cordova.plugin.ismartnet.rongcloud.utils.RongGenerate;
import cordova.plugin.ismartnet.rongcloud.utils.SharedPreferences;
import cordova.plugin.ismartnet.rongcloud.utils.StringUtil;
import cordova.plugin.ismartnet.rongcloud.utils.ToastUtils;
import cordova.plugin.ismartnet.rongcloud.activity.RpDetailActivityRp;

import cordova.plugin.ismartnet.rongcloud.bean.CurrentUser;
import cordova.plugin.ismartnet.rongcloud.bean.SendUser;
import cordova.plugin.ismartnet.rongcloud.msg.RedPacketOpenedMessage;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by lvping on 2017/9/14.
 */

public class OpenRedFragment extends OpenRpBaseDialogFragment {
  public ImageView iv_close;
  public ImageView iv_header;
  public ImageView iv_open_rp;
  public TextView tv_name;
  public TextView tv_send_rp;
  public TextView tv_tip;
  public TextView tv_no_rp;
  public TextView tv_look_others;
  public String user_id;
  public String envelope_id;
  public String userName;
  public String envelopeStatus;
  public String bless;
  public boolean m = false;
  public boolean n = false;

  public OpenRedFragment() {
  }

  @Override
  public int getLayoutId() {
    return R.layout._dialog_open_rp;
  }

  @Override
  public void initView() {
    super.initView();
    iv_close = (ImageView) this.rootView.findViewById(R.id.iv_close);
    iv_header = (ImageView) this.rootView.findViewById(R.id.iv_header);
    iv_open_rp = (ImageView) this.rootView.findViewById(R.id.iv_open_rp);
    tv_name = (TextView) this.rootView.findViewById(R.id.tv_name);
    tv_send_rp = (TextView) this.rootView.findViewById(R.id.tv_send_rp);
    tv_tip = (TextView) this.rootView.findViewById(R.id.tv_tip);
    tv_no_rp = (TextView) this.rootView.findViewById(R.id.tv_no_rp);
    tv_look_others = (TextView) this.rootView.findViewById(R.id.tv_look_others);
  }

  @Override
  public void initListener() {
    super.initListener();
    iv_close.setOnClickListener(this);
    iv_open_rp.setOnClickListener(this);
    tv_look_others.setOnClickListener(this);
  }

  @Override
  protected void initData(Bundle var1) {
    super.initData(var1);
    if (var1 != null) {
      user_id = var1.getString("userId");
      envelope_id = var1.getString("envelopeId");
      envelopeStatus = var1.getString("envelopeStatus");
      userName = CurrentUser.getNameById(SendUser.sendUserId);
      bless = var1.getString("bless");
      if (StringUtil.isNotEmpty(CurrentUser.getUserIconById(SendUser.sendUserId))) {
        Picasso.with(this.fromActivity).load(CurrentUser.getUserIconById(SendUser.sendUserId)).into(iv_header);
      } else {
        Picasso.with(fromActivity).load(RongGenerate.generateDefaultAvatar(userName, SendUser.sendUserId)).into(iv_header);
      }
      this.tv_name.setText(userName);
      DrawableUtil.setRightDrawable(this.fromActivity, this.tv_name, R.drawable._ic_pin, true);
      if (envelopeStatus.equals("0")) {
        cView();
      } else if (envelopeStatus.equals("1")) {
        RpDetailActivityRp.intent(this.fromActivity, 1, SendUser.sendUserId, envelope_id, userName, CurrentUser.getUserIconById(SendUser.sendUserId), bless);
      } else if (envelopeStatus.equals("2")) {
        aView();
      }
    }
  }

  private void aView() {
    tv_no_rp.setVisibility(View.VISIBLE);
    tv_no_rp.setText(this.getActivity().getString(R.string.no_rp));
  }

  private void bView() {
    tv_tip.setVisibility(View.VISIBLE);
    tv_tip.setText(this.getActivity().getString(R.string.rp_send));
  }

  /**
   * 0未领
   */
  private void cView() {
    iv_open_rp.setVisibility(View.VISIBLE);
    tv_send_rp.setVisibility(View.VISIBLE);
    tv_tip.setVisibility(View.VISIBLE);
    tv_tip.setText(bless);
  }

  @Override
  public void onClick(int var1) {
    if (var1 == R.id.iv_close) {
      this.dismiss();
    } else if (var1 == R.id.iv_open_rp) {
      if (!this.m && !NoDoubleClickUtils.isDoubleClick()) {
        this.d();
      }
    } else if (var1 == R.id.tv_look_others) {
      RpDetailActivityRp.intent(this.fromActivity, 1, user_id, envelope_id, userName, CurrentUser.getNameById(SendUser.sendUserId), bless);
    }
  }

  public void d() {
    final ObjectAnimator var1 = ObjectAnimator.ofFloat(this.iv_open_rp, "RotationY", new float[]{0.0F, 180.0F});
    var1.setDuration(500L);
    var1.setRepeatCount(-1);
    var1.start();
    /**
     * 点击打开红包
     * 传参：
     * user_id
     * envelopeId
     * key
     * user_name
     * avatar
     * 返回RpInfoModel
     */
    Map<String, String> map = new HashMap<>();
    map.put("bonusId", envelope_id);
    map.put("tokenId", SharedPreferences.getInstance(this.fromActivity).getStringValue(Api.BD_TOKEN_ID));
    Call<Receive> receiveCall = HttpUtil.getService().getRed(SharedPreferences.getInstance(this.fromActivity).getStringValue(Api.RECEIVE_BONUS), map);
    receiveCall.enqueue(new MyCallBack<Receive>() {
      @Override
      public void onSuccess(Response<Receive> response) {
        /**
         * 0,未领 1，已领
         */
        if (response.body().isSuccess()) {
          if (response.body().getResult().equals("1")) {
            var1.cancel();
            sendMsg();
            RpDetailActivityRp.intent(fromActivity, 1, user_id, envelope_id, userName, CurrentUser.getNameById(SendUser.sendUserId), bless);
          } else {
            var1.cancel();
            aView();
          }
        }
      }
      @Override
      public void onFail(String message) {
        ToastUtils.show(fromActivity, message);
        var1.cancel();
      }
    });
  }

  private void sendMsg() {
    RedPacketOpenedMessage redPacketOpenedMessage;
    String[] user_id;
    if (StringUtil.isNotEmpty(CurrentUser.getUserId()) && CurrentUser.getUserId().equals(SendUser.sendUserId)) {
      redPacketOpenedMessage = RedPacketOpenedMessage.obtain(CurrentUser.getUserId(), userName, CurrentUser.getName());
      Log.e("redPacketOpenedMessage", "CurrentUser=SendUser");
      user_id = new String[]{SendUser.sendUserId};
      if (RongIM.getInstance() != null) {
        RongIM.getInstance().sendDirectionalMessage(SendUser.conversationType, SendUser.targetId, redPacketOpenedMessage, user_id, (String) null, (String) null, (IRongCallback.ISendMessageCallback) null);
      }
    } else {
      redPacketOpenedMessage = RedPacketOpenedMessage.obtain(CurrentUser.getUserId(), CurrentUser.getName(), CurrentUser.getNameById(SendUser.sendUserId));
      Log.e("redPacketOpenedMessage", "CurrentUser!=SendUser");
      user_id = new String[]{SendUser.sendUserId};
      if (RongIM.getInstance() != null) {
        RongIM.getInstance().sendDirectionalMessage(SendUser.conversationType, SendUser.targetId, redPacketOpenedMessage, user_id, (String) null, (String) null, (IRongCallback.ISendMessageCallback) null);
      }
      this.dismiss();
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    this.dismiss();
  }
}
