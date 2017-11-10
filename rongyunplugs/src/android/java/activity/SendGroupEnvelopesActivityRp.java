package cordova.plugin.ismartnet.rongcloud.activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import cordova.plugin.ismartnet.rongcloud.base.RpBaseSendActivity;
import cordova.plugin.ismartnet.rongcloud.retrofit.callback.MyCallBack;
import cordova.plugin.ismartnet.rongcloud.retrofit.HttpUtil;
import cordova.plugin.ismartnet.rongcloud.retrofit.api.Api;
import cordova.plugin.ismartnet.rongcloud.bean.Order;
import cordova.plugin.ismartnet.rongcloud.utils.DialogDisplay;
import cordova.plugin.ismartnet.rongcloud.utils.DrawableUtil;
import cordova.plugin.ismartnet.rongcloud.utils.KeyboardUtil;
import cordova.plugin.ismartnet.rongcloud.utils.NoDoubleClickUtils;
import cordova.plugin.ismartnet.rongcloud.utils.ResourcesUtils;
import cordova.plugin.ismartnet.rongcloud.utils.SharedPreferences;
import cordova.plugin.ismartnet.rongcloud.utils.StringUtil;
import cordova.plugin.ismartnet.rongcloud.utils.ToastUtils;
import cordova.plugin.ismartnet.rongcloud.view.ActionBarView;
import cordova.plugin.ismartnet.rongcloud.view.MyTextWatcher;
import cordova.plugin.ismartnet.rongcloud.view.NoUnderClickableSpan;
import retrofit2.Call;
import retrofit2.Response;
/**
 * Created by lvping on 2017/9/14.
 */

public class SendGroupEnvelopesActivityRp extends RpBaseSendActivity {
  String TargetId = null;
  //普通为1，拼手气为2
  private int ENVELOPES_TYPE = 2;
  private ActionBarView actionBarView;
  private TextView pop_message;
  private LinearLayout ll_peak_num_layout;
  private EditText et_peak_num;
  private TextView tv_group_member_num;
  private LinearLayout ll_peak_amount_layout;
  private TextView tv_peak_amount_icon;
  private EditText et_peak_amount;
  private TextView tv_peak_type;
  private EditText et_peak_message;
  private TextView tv_amount_for_show;
  private Button btn_putin;
  private String summary = "恭喜发财，大吉大利！";
  private int maxCount = 100;
  private String envelopeName = "云红包";
  private NoUnderClickableSpan peakTypeClick = new NoUnderClickableSpan() {
    public void onClick(View var1) {
      if (SendGroupEnvelopesActivityRp.this.ENVELOPES_TYPE == 2) {
        SendGroupEnvelopesActivityRp.this.ENVELOPES_TYPE = 1;
      } else if (SendGroupEnvelopesActivityRp.this.ENVELOPES_TYPE == 1) {
        SendGroupEnvelopesActivityRp.this.ENVELOPES_TYPE = 2;
      }

      SendGroupEnvelopesActivityRp.this.setPeakTypeStyle();
      SendGroupEnvelopesActivityRp.this.checkType();
    }
  };
  private int NUMBER = -1;
  private float AMOUNTMONEY = -1.0F;

  public SendGroupEnvelopesActivityRp() {
  }

  public void initView() {
    actionBarView = (ActionBarView) findViewById(ResourcesUtils.getId(this,"actionbar"));
    pop_message = (TextView) findViewById(ResourcesUtils.getId(this,"pop_message"));
    ll_peak_num_layout = (LinearLayout) findViewById(ResourcesUtils.getId(this,"ll_peak_num_layout"));
    et_peak_num = (EditText) findViewById(ResourcesUtils.getId(this,"et_peak_num"));
    this.et_peak_num.setFocusable(true);
    this.et_peak_num.setFocusableInTouchMode(true);
    this.et_peak_num.requestFocus();
    this.et_peak_num.setSelection(this.et_peak_num.getText().length());
    this.et_peak_num.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
    tv_group_member_num = (TextView) findViewById(ResourcesUtils.getId(this,"tv_group_member_num"));
    ll_peak_amount_layout = (LinearLayout) findViewById(ResourcesUtils.getId(this,"ll_peak_amount_layout"));
    tv_peak_amount_icon = (TextView) findViewById(ResourcesUtils.getId(this,"tv_peak_amount_icon"));
    et_peak_amount = (EditText) findViewById(ResourcesUtils.getId(this,"et_peak_amount"));
    tv_peak_type = (TextView) findViewById(ResourcesUtils.getId(this,"tv_peak_type"));
    et_peak_message = (EditText) findViewById(ResourcesUtils.getId(this,"et_peak_message"));
    tv_amount_for_show = (TextView) findViewById(ResourcesUtils.getId(this,"tv_amount_for_show"));
    btn_putin = (Button) findViewById(ResourcesUtils.getId(this,"btn_putin"));
    KeyboardUtil.popInputMethod(this.et_peak_num);
    this.setDefaultView();
  }

  public void initListener() {
    actionBarView.getIvBack().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        KeyboardUtil.hideKeyboard(SendGroupEnvelopesActivityRp.this);
        SendGroupEnvelopesActivityRp.this.finish();
      }
    });
    this.et_peak_num.addTextChangedListener(new MyTextWatcher() {
      public void afterTextChanged(Editable var1) {
        SendGroupEnvelopesActivityRp.this.NUMBER = SendGroupEnvelopesActivityRp.this.checkNum();
        if (SendGroupEnvelopesActivityRp.this.NUMBER != -1) {
          SendGroupEnvelopesActivityRp.this.AMOUNTMONEY = SendGroupEnvelopesActivityRp.this.checkAmount();
        }

        SendGroupEnvelopesActivityRp.this.checkForAllAmount();
        SendGroupEnvelopesActivityRp.this.checkForButton();
      }
    });
    this.et_peak_amount.addTextChangedListener(new MyTextWatcher() {
      public void afterTextChanged(Editable var1) {
        SendGroupEnvelopesActivityRp.this.NUMBER = SendGroupEnvelopesActivityRp.this.checkNum();
        if (SendGroupEnvelopesActivityRp.this.NUMBER != -1) {
          SendGroupEnvelopesActivityRp.this.AMOUNTMONEY = SendGroupEnvelopesActivityRp.this.checkAmount();
        }

        SendGroupEnvelopesActivityRp.this.checkForAllAmount();
        SendGroupEnvelopesActivityRp.this.checkForButton();
      }
    });
    this.btn_putin.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {

        if (!NoDoubleClickUtils.isDoubleClick()) {
          String var1 = et_peak_amount.getText().toString();
          if (!StringUtil.isEmpty(var1)) {
            SendGroupEnvelopesActivityRp.this.requestInfo();
          } else {
            ToastUtils.show(SendGroupEnvelopesActivityRp.this, "请输入正确金额");
            ll_peak_amount_layout.setBackgroundResource(ResourcesUtils.getDrawableId(SendGroupEnvelopesActivityRp.this,"_bg_white_round"));
            DrawableUtil.setClickable(btn_putin, true);
          }
        }
      }
    });
  }

  private void requestInfo() {
    KeyboardUtil.hideKeyboard(this);
    DialogDisplay.getInstance().dialogLoading(SendGroupEnvelopesActivityRp.this, "请稍后...");
    String taskId = SendGroupEnvelopesActivityRp.this.getIntent().getStringExtra("TargetId");
    final String et_peak_message = StringUtil.isEmpty(SendGroupEnvelopesActivityRp.this.et_peak_message.getText().toString().trim()) ? SendGroupEnvelopesActivityRp.this.et_peak_message.getHint().toString().trim() : SendGroupEnvelopesActivityRp.this.et_peak_message.getText().toString().trim();
    SharedPreferences.getInstance(SendGroupEnvelopesActivityRp.this).putStringValue(Api.MEMO, et_peak_message);
    String et_peak_amount = SendGroupEnvelopesActivityRp.this.tv_amount_for_show.getText().toString();
    String et_peak_num = SendGroupEnvelopesActivityRp.this.et_peak_num.getText().toString();
    String issueTypeId = SendGroupEnvelopesActivityRp.this.ENVELOPES_TYPE + "";
    SharedPreferences.getInstance(SendGroupEnvelopesActivityRp.this).putStringValue(Api.ENVELOPES_TYPE, issueTypeId);
    Map<String, String> map = new HashMap<String, String>();
    map.put("taskId", taskId);
    map.put("memo", et_peak_message);
    map.put("orderAmount", et_peak_amount);
    map.put("splitNum", et_peak_num);
    map.put("issueTypeId", issueTypeId);
    map.put("tokenId", SharedPreferences.getInstance(SendGroupEnvelopesActivityRp.this).getStringValue(Api.BD_TOKEN_ID));
    Call<Order>orderCall= HttpUtil.getService().createBonus(SharedPreferences.getInstance(SendGroupEnvelopesActivityRp.this).getStringValue(Api.CREATE_BONUS), map);

    orderCall.enqueue(new MyCallBack<Order>() {
      @Override
      public void onSuccess(Response<Order> response) {
        if (response.body().isSuccess()) {
          finish();
        }else{
          DialogDisplay.getInstance().dialogCloseLoading(SendGroupEnvelopesActivityRp.this);
          ToastUtils.show(SendGroupEnvelopesActivityRp.this,"钱包余额不足，请先充值~");
        }
      }
      @Override
      public void onFail(String message) {
        DialogDisplay.getInstance().dialogCloseLoading(SendGroupEnvelopesActivityRp.this);
        ToastUtils.show(SendGroupEnvelopesActivityRp.this,message);
      }
    });
  }

  private void setDefaultView() {
    this.pop_message.setVisibility(View.INVISIBLE);
    this.pop_message.getBackground().mutate().setAlpha(80);
    this.setPeakTypeStyle();
    this.tv_amount_for_show.setText("0.00");
    DrawableUtil.setClickable(this.btn_putin, false);
  }

  private void setPeakTypeStyle() {
    String var1 = "";
    if (this.ENVELOPES_TYPE == 2) {
      DrawableUtil.setDrawableRight(this.getResources().getDrawable(ResourcesUtils.getDrawableId(SendGroupEnvelopesActivityRp.this,"_ic_pin")), this.tv_peak_amount_icon);
      this.tv_peak_amount_icon.setText("总金额");
      var1 = "当前为拼手气红包，改为普通红包";
    } else if (this.ENVELOPES_TYPE == 1) {
      DrawableUtil.setDrawableRight((Drawable) null, this.tv_peak_amount_icon);
      this.tv_peak_amount_icon.setText("单个金额");
      var1 = "当前为普通红包，改为拼手气红包";
    }

    int var2 = var1.indexOf("，") + 1;
    if (var2 >= 1) {
      SpannableString var3 = new SpannableString(var1);
      var3.setSpan(this.peakTypeClick, var2, var1.length(), 33);
      var3.setSpan(new ForegroundColorSpan(this.getResources().getColor(ResourcesUtils.getColorId(SendGroupEnvelopesActivityRp.this,"blue"))), var2, var1.length(), 33);
      this.tv_peak_type.setText(var3);
      this.tv_peak_type.setMovementMethod(LinkMovementMethod.getInstance());
    }
  }

  private void checkType() {
    String var1;
    if (this.ENVELOPES_TYPE == 2) {
      var1 = this.tv_amount_for_show.getText().toString();
      this.et_peak_amount.setText(var1);
    } else if (this.ENVELOPES_TYPE == 1) {
      var1 = this.et_peak_amount.getText().toString();
      String var2 = this.et_peak_num.getText().toString();
      if (var1 != null && !var1.isEmpty() && var2 != null && !var2.isEmpty()) {
        BigDecimal var3 = new BigDecimal(var1);
        BigDecimal var4 = new BigDecimal(var2);
        if (var4.floatValue() > 0.0F) {
          BigDecimal var5 = var3.divide(var4, 3, 5);
          this.et_peak_amount.setText(StringUtil.formatMoney(var5.doubleValue()));
        }
      }
    }
  }

  private int checkNum() {
    String var1 = this.et_peak_num.getText().toString();
    if (!StringUtil.isEmpty(var1)) {
      if (isNumber(var1)) {
        BigDecimal var2 = new BigDecimal(var1);
        int var3 = var2.intValue();
        if (var3 == 0) {
          this.showTips("至少需要设置1个红包");
          this.ll_peak_num_layout.setBackgroundResource(ResourcesUtils.getDrawableId(SendGroupEnvelopesActivityRp.this,"_bg_white_round_stroke"));
          return -1;
        } else if (var3 > this.maxCount) {
          this.showTips("一次最多可发" + this.maxCount + "个红包");
          this.ll_peak_num_layout.setBackgroundResource(ResourcesUtils.getDrawableId(SendGroupEnvelopesActivityRp.this,"_bg_white_round_stroke"));
          return -1;
        } else {
          this.ll_peak_num_layout.setBackgroundResource(ResourcesUtils.getDrawableId(SendGroupEnvelopesActivityRp.this,"_bg_white_round"));
          this.closeTips();
          return var3;
        }
      } else {
        this.showTips("请输入正确个数");
        this.ll_peak_num_layout.setBackgroundResource(ResourcesUtils.getDrawableId(SendGroupEnvelopesActivityRp.this,"_bg_white_round_stroke"));
        return -1;
      }
    } else {
      this.closeTips();
      this.ll_peak_num_layout.setBackgroundResource(ResourcesUtils.getDrawableId(SendGroupEnvelopesActivityRp.this,"_bg_white_round"));
      return -1;
    }
  }

  private float checkAmount() {
    String var1 = this.et_peak_amount.getText().toString();
    String var8 = this.et_peak_num.getText().toString();
    int var6=1;
    if (!StringUtil.isEmpty(var8)) {
      if (isNumber(var8)) {
        BigDecimal var5 = new BigDecimal(var8);
        var6 = var5.intValue();
      }
    }
    if (!StringUtil.isEmpty(var1)) {
      if (!var1.startsWith(".")) {
        BigDecimal var2 = new BigDecimal(var1);
        float var3 = var2.floatValue();
        if (var3 == 0.0F) {
          this.ll_peak_amount_layout.setBackgroundResource(ResourcesUtils.getDrawableId(SendGroupEnvelopesActivityRp.this,"_bg_white_round"));
          this.closeTips();
          return -1.0F;
        } else if (var3/var6 < 0.01F) {
          this.showTips("单个红包金额不可低于0.01元");
          this.ll_peak_amount_layout.setBackgroundResource(ResourcesUtils.getDrawableId(SendGroupEnvelopesActivityRp.this,"_bg_white_round_stroke"));
          return -1.0F;
        } else if (this.ENVELOPES_TYPE == 2) {
          if (var3/var6> this.maxLimitMoney) {
            this.showTips("单个红包金额不可超过" + StringUtil.formatMoney((double) this.maxLimitMoney) + "元");
            this.ll_peak_amount_layout.setBackgroundResource(ResourcesUtils.getDrawableId(SendGroupEnvelopesActivityRp.this,"_bg_white_round_stroke"));
            return -1.0F;
          } else {
            this.ll_peak_amount_layout.setBackgroundResource(ResourcesUtils.getDrawableId(SendGroupEnvelopesActivityRp.this,"_bg_white_round"));
            this.closeTips();
            return var3;
          }
        } else if (this.ENVELOPES_TYPE == 1) {
          String var4 = this.et_peak_num.getText().toString();
          if (var4 != null && !var4.isEmpty()) {
            float var5 = 0.0F;
            var5 = StringUtil.formatMoneyDouble(var4);
            if (var3 > this.maxLimitMoney) {
              this.showTips("单个红包金额不可超过" + this.maxLimitMoney + "元");
              this.ll_peak_amount_layout.setBackgroundResource(ResourcesUtils.getDrawableId(SendGroupEnvelopesActivityRp.this,"_bg_white_round_stroke"));
              return -1.0F;
            } else {
              this.ll_peak_amount_layout.setBackgroundResource(ResourcesUtils.getDrawableId(SendGroupEnvelopesActivityRp.this,"_bg_white_round"));
              this.closeTips();
              return var3;
            }
          } else if (var3 > this.maxLimitMoney) {
            this.showTips("单个红包金额不可超过" + this.maxLimitMoney + "元");
            this.ll_peak_amount_layout.setBackgroundResource(ResourcesUtils.getDrawableId(SendGroupEnvelopesActivityRp.this,"_bg_white_round_stroke"));
            return -1.0F;
          } else {
            this.closeTips();
            this.ll_peak_amount_layout.setBackgroundResource(ResourcesUtils.getDrawableId(SendGroupEnvelopesActivityRp.this,"_bg_white_round"));
            return var3;
          }
        } else {
          return -1.0F;
        }
      } else {
        this.showTips("请输入正确金额");
        this.ll_peak_amount_layout.setBackgroundResource(ResourcesUtils.getDrawableId(SendGroupEnvelopesActivityRp.this,"_bg_white_round_stroke"));
        return -1.0F;
      }
    } else {
      this.closeTips();
      this.ll_peak_amount_layout.setBackgroundResource(ResourcesUtils.getDrawableId(SendGroupEnvelopesActivityRp.this,"_bg_white_round"));
      return -1.0F;
    }
  }

  private void showTips(String var1) {
    this.pop_message.setText(var1);
    this.pop_message.setVisibility(View.VISIBLE);
  }

  private void closeTips() {
    this.pop_message.setText("");
    this.pop_message.setVisibility(View.INVISIBLE);
  }

  private void checkForAllAmount() {
    int var1 = -1;
    String var2 = this.et_peak_num.getText().toString();
    if (!StringUtil.isEmpty(var2) && isNumber(var2)) {
      BigDecimal var3 = new BigDecimal(var2);
      var1 = var3.intValue();
    }

    float var7 = 0.0F;
    String var4 = this.et_peak_amount.getText().toString();
    BigDecimal var5;
    if (!StringUtil.isEmpty(var4) && !var4.startsWith(".")) {
      var5 = new BigDecimal(var4);
      var7 = var5.floatValue();
    }

    if (var1 > 0 && var7 > 0.0F) {
      if (this.ENVELOPES_TYPE == 2) {
        var5 = new BigDecimal((double) var7);
        var5 = var5.setScale(2, 5);
        this.tv_amount_for_show.setText(var5 + "");
      } else if (this.ENVELOPES_TYPE == 1) {
        var5 = new BigDecimal((double) var7);
        BigDecimal var6 = var5.multiply(new BigDecimal(var1));
        var6 = var6.setScale(2, 5);
        this.tv_amount_for_show.setText(var6 + "");
      }
    } else {
      this.tv_amount_for_show.setText("0.00");
    }

  }

  private void checkForButton() {
    DrawableUtil.setClickable(this.btn_putin, false);
    if (this.NUMBER > 0 && this.AMOUNTMONEY > 0.0F) {
      DrawableUtil.setClickable(this.btn_putin, true);
    }

  }

  @Override
  protected void initData(Bundle var1) {
    super.initData(var1);
    this.TargetId = this.getIntent().getStringExtra("TargetId");
    this.rongCloundNumber();
  }

  private void rongCloundNumber() {
    String num = SharedPreferences.getInstance(SendGroupEnvelopesActivityRp.this).getStringValue(Api.GROUPNUM);
    if (!StringUtil.isEmptyAndNull(num)) {
      SendGroupEnvelopesActivityRp.this.tv_group_member_num.setVisibility(View.VISIBLE);
      String var2 = SendGroupEnvelopesActivityRp.this.getResources().getString(ResourcesUtils.getStringId(SendGroupEnvelopesActivityRp.this,"group_number"));
      String var3 = String.format(var2, new Object[]{Integer.valueOf(num)});
      SendGroupEnvelopesActivityRp.this.tv_group_member_num.setText(var3);
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    this.et_peak_message.addTextChangedListener(new MyTextWatcher() {
      public void afterTextChanged(Editable var1) {
        super.afterTextChanged(var1);
        if (!TextUtils.isEmpty(var1.toString())) {
          if (var1.toString().length() == 25) {
            ToastUtils.show(SendGroupEnvelopesActivityRp.this, "红包祝福语最多输入25个字噢！");
          }
        }
      }
    });
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    KeyboardUtil.hideKeyboard(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    DialogDisplay.getInstance().dialogCloseLoading(SendGroupEnvelopesActivityRp.this.context);
  }

  @Override
  public int getLayoutId() {
    return ResourcesUtils.getLayoutId(SendGroupEnvelopesActivityRp.this,"_activity_send_group_peak");
  }
}
