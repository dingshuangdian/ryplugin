package cordova.plugin.ismartnet.rongcloud.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.guoji.tpco.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cordova.plugin.ismartnet.rongcloud.App;
import cordova.plugin.ismartnet.rongcloud.base.RpBaseActivity;
import cordova.plugin.ismartnet.rongcloud.retrofit.callback.MyCallBack;
import cordova.plugin.ismartnet.rongcloud.retrofit.HttpUtil;
import cordova.plugin.ismartnet.rongcloud.retrofit.api.Api;
import cordova.plugin.ismartnet.rongcloud.utils.DialogDisplay;
import cordova.plugin.ismartnet.rongcloud.utils.DrawableUtil;
import cordova.plugin.ismartnet.rongcloud.utils.RongGenerate;
import cordova.plugin.ismartnet.rongcloud.utils.SharedPreferences;
import cordova.plugin.ismartnet.rongcloud.utils.StringUtil;
import cordova.plugin.ismartnet.rongcloud.utils.ToastUtils;
import cordova.plugin.ismartnet.rongcloud.adapter.DetailBaseAdapter;
import cordova.plugin.ismartnet.rongcloud.adapter.cAdapter;
import cordova.plugin.ismartnet.rongcloud.common.IsTrue;
import cordova.plugin.ismartnet.rongcloud.bean.CurrentUser;
import cordova.plugin.ismartnet.rongcloud.bean.SendUser;
import cordova.plugin.ismartnet.rongcloud.model.RpItemModel;
import cordova.plugin.ismartnet.rongcloud.view.ActionBarView;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by lvping on 2017/9/18.
 */

public class RpDetailActivityRp extends RpBaseActivity implements AbsListView.OnScrollListener {
  private ListView listView;
  private ImageView iv_avatar;

  private TextView tv_look_rp_history;
  private TextView tv_username;
  private TextView tv_bless;
  private TextView tv_rec_amount;
  private TextView tv_rp_num;
  private RpDetailActivityRp.RpDetailAdapter rpDetailAdapter;
  private int mListViewHeight;
  private int page = 2;
  private int pageCount;
  private int flag = 0;
  private boolean isScroll = false;
  private IsTrue buttomBean;
  private List<Object> rpItemModelList;
  private HashMap<String, Object> tempMap = App.getInstance().getTempMap();
  private String userId;
  private String thirdToken;
  private String rpId;
  private String userName;
  private String bless;
  private String userIcon;
  private int hasLeft;

  public static void intent(Activity var0, int var1, String var2, String var3, String var4, String var5, String var6) {
    Intent var7 = new Intent(var0, RpDetailActivityRp.class);
    Bundle var8 = new Bundle();
    var8.putInt("fromKey", var1);
    var8.putString("userId", var2);
    var8.putString("envelope_id", var3);
    var8.putString("userName", var4);
    var8.putString("userIcon", var5);
    var8.putString("bless", var6);
    var7.putExtras(var8);
    var0.startActivity(var7);
  }

  @Override
  public void initView() {
    this.actionBarView = (ActionBarView) this.findViewById(R.id.actionbar);
    this.listView = (ListView) this.findViewById(R.id.listView);
    this.tv_look_rp_history = (TextView) this.findViewById(R.id.tv_look_rp_history);
    View var1 = View.inflate(this, R.layout._header_rp_detail, (ViewGroup) null);
    this.iv_avatar = (ImageView) var1.findViewById(R.id.iv_avatar);
    //发包人
    this.tv_username = (TextView) var1.findViewById(R.id.tv_username);
    //祝福语
    this.tv_bless = (TextView) var1.findViewById(R.id.tv_bless);
    //领取的金额
    this.tv_rec_amount = (TextView) var1.findViewById(R.id.tv_rec_amount);
    //领取的数量
    this.tv_rp_num = (TextView) var1.findViewById(R.id.tv_rp_num);
    this.listView.addHeaderView(var1);

  }

  @Override
  protected void initData(Bundle var1) {
    super.initData(var1);
    if (var1 != null) {
      int var2 = var1.getInt("fromKey");
      this.userId = var1.getString("userId");
      this.rpId = var1.getString("envelope_id");
      this.userName = var1.getString("userName");
      this.bless = var1.getString("bless");
      this.actionBarView.setTitle("红包详情");
      this.rpItemModelList = new ArrayList();
      this.rpDetailAdapter = new RpDetailActivityRp.RpDetailAdapter(this, this.rpItemModelList);
      this.listView.setAdapter(this.rpDetailAdapter);
      if (var2 == 0) {
      } else {
        this.userName = var1.getString("userName");
        this.userIcon = var1.getString("userIcon");
      }
      getReceive();
    }
  }

  private void getReceive() {
    DialogDisplay.getInstance().dialogLoading(this.context, this.getString(R.string.loading));
    Map<String, String> map = new HashMap<>();
    map.put("bonusId", rpId);
    map.put("tokenId", SharedPreferences.getInstance(RpDetailActivityRp.this).getStringValue(Api.BD_TOKEN_ID));
    Call<RpItemModel> rpItemModelCall = HttpUtil.getService().getRpItem(SharedPreferences.getInstance(RpDetailActivityRp.this).getStringValue(Api.QRYRECEIVE_BONUS), map);
    rpItemModelCall.enqueue(new MyCallBack<RpItemModel>() {
      @Override
      public void onSuccess(Response<RpItemModel> response) {
        DialogDisplay.getInstance().dialogCloseLoading(RpDetailActivityRp.this.context);
        if (response.body().isSuccess()) {
          showHeader(response.body());
          if (response.body().getResult().getReceiveNum() > 0) {
            tempMap.put(Api.RPMODEL, response.body());
            RpDetailActivityRp.this.rpItemModelList.addAll(response.body().getResult().getRecord());
            RpDetailActivityRp.this.rpDetailAdapter.notifyDataSetChanged();
          }
        }
      }
      @Override
      public void onFail(String message) {
        DialogDisplay.getInstance().dialogCloseLoading(RpDetailActivityRp.this);
        ToastUtils.show(RpDetailActivityRp.this, message);
      }
    });
  }

  private void showHeader(RpItemModel rpItemModel) {
    if (StringUtil.isEmptyAndNull(CurrentUser.getUserIconById(SendUser.sendUserId))) {
      Picasso.with(RpDetailActivityRp.this).load(RongGenerate.generateDefaultAvatar(CurrentUser.getNameById(SendUser.sendUserId), SendUser.sendUserId)).into(iv_avatar);
    } else {
      Picasso.with(RpDetailActivityRp.this).load(CurrentUser.getUserIconById(SendUser.sendUserId)).into(iv_avatar);
    }
    this.tv_username.setText(userName + "的红包");
    this.tv_bless.setText(bless);
    if (rpItemModel.getResult().getIssueTypeId() == 2) {
      DrawableUtil.setRightDrawable(this.context, this.tv_username, R.drawable._ic_pin, true);
    } else {
      DrawableUtil.setRightDrawable(this.context, this.tv_username, R.drawable._ic_pin, false);
    }
    if (!rpItemModel.getResult().getIsReceive().equals("false")) {
      for (int i = 0; i < rpItemModel.getResult().getRecord().size(); i++) {
        String user_id = (rpItemModel.getResult().getRecord().get(i).getUserId());
        if (user_id.equals(CurrentUser.getUserId())) {

          SpannableString var2 = new SpannableString(StringUtil.formatMoney(rpItemModel.getResult().getRecord().get(i).getBonusAmount()) + "元");
          var2.setSpan(new AbsoluteSizeSpan(100), 0, var2.length() - 1, 18);
          this.tv_rec_amount.setText(var2);
        }
      }
    } else {
      this.tv_rec_amount.setVisibility(View.GONE);
    }
    if (rpItemModel.getResult().getReceiveNum() == rpItemModel.getResult().getSplitNum()) {
      this.tv_rp_num.setText(String.format(this.getString(R.string.self_rp_no_left), new Object[]{Integer.valueOf(String.valueOf(rpItemModel.getResult().getReceiveNum())), Integer.valueOf(rpItemModel.getResult().getSplitNum()), StringUtil.formatMoney(rpItemModel.getResult().getOrderAmount())}));
    } else {
      this.tv_rp_num.setText(String.format(this.getString(R.string.self_rp_has_left), new Object[]{Integer.valueOf(String.valueOf(rpItemModel.getResult().getReceiveNum())), Integer.valueOf(rpItemModel.getResult().getSplitNum()), StringUtil.formatMoney(rpItemModel.getResult().getReceiveAmount()), rpItemModel.getResult().getOrderAmount()}));
    }
  }

  @Override
  public void initListener() {
    super.initListener();
    this.actionBarView.getIvBack().setOnClickListener(this);
    this.tv_look_rp_history.setOnClickListener(this);
    this.listView.setOnScrollListener(this);
    this.listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @SuppressLint({"NewApi"})
      public void onGlobalLayout() {
        RpDetailActivityRp.this.mListViewHeight = RpDetailActivityRp.this.listView.getHeight();
        if (Build.VERSION.SDK_INT > 16) {
          RpDetailActivityRp.this.listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        } else {
          RpDetailActivityRp.this.listView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
      }
    });
  }

  @Override
  public void onScrollStateChanged(AbsListView view, int scrollState) {
    if (scrollState == 0) {
      this.isScroll = false;
    } else {
      this.isScroll = true;
    }
  }

  @Override
  public void onScroll(AbsListView var1, int var2, int var3, int var4) {
    if (var2 + var3 == var4 && this.isScroll) {
      View var5 = this.listView.getChildAt(this.listView.getChildCount() - 1);
      if (var5 != null && var5.getBottom() == this.mListViewHeight) {
        if (this.page <= this.pageCount) {
          //this.loadNextPage();
        } else if (this.buttomBean != null && this.buttomBean.IsTrue) {
          this.buttomBean.IsTrue = false;
          this.rpDetailAdapter.notifyDataSetChanged();
        }
      }
    }
  }

  @Override
  public int getLayoutId() {
    return R.layout._activity_rp_detail;
  }

  class RpDetailAdapter extends DetailBaseAdapter<Object> {
    public RpDetailAdapter(Context var1, List<Object> var2) {
      super(var1, var2);
    }

    public int getItemViewType(int var1) {
      return RpDetailActivityRp.this.rpItemModelList.get(var1) instanceof IsTrue ? 1 : 0;
    }

    public int getViewTypeCount() {
      return 2;
    }

    public View getView(int var1, View var2, ViewGroup var3) {
      TextView var8;
      RpItemModel.ResultBean.RecordBean var11 = (RpItemModel.ResultBean.RecordBean) RpDetailActivityRp.this.rpItemModelList.get(var1);
      RpItemModel rpItemModel = (RpItemModel) tempMap.get(Api.RPMODEL);
      cAdapter var12 = cAdapter.a(RpDetailActivityRp.this.context, var2, var3, R.layout._item_rp_detail, var1);
      //领取人名称
      TextView var13 = (TextView) var12.a(R.id.tv_name);
      SimpleDraweeView iv_header = var12.a(R.id.iv_header);
      if (!StringUtil.isEmptyAndNull(var11.getHeadImg())) {
        iv_header.setImageURI(Uri.parse(var11.getHeadImg()));
      } else {
        //iv_header.setImageURI(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getResources().getResourcePackageName(R.drawable.de_default_portrait) + "/" + getResources().getResourceTypeName(R.drawable.de_default_portrait) + "/" + getResources().getResourceEntryName(R.drawable.de_default_portrait)));
        iv_header.setImageURI(RongGenerate.generateDefaultAvatar(var11.getUserName(), var11.getUserId()));
      }
      //领取时间
      var8 = (TextView) var12.a(R.id.tv_time);
      //领取金额
      TextView var14 = (TextView) var12.a(R.id.tv_amount);
      //手气最佳
      TextView var10 = (TextView) var12.a(R.id.tv_best);
      var13.setText(var11.getUserName());
      var8.setText(var11.getReceiveTime());
      var14.setText(StringUtil.formatMoney(var11.getBonusAmount()) + "元");
      if (rpItemModel.getResult().getReceiveNum() == rpItemModel.getResult().getSplitNum()) {
        float max = Float.parseFloat(rpItemModel.getResult().getRecord().get(0).getBonusAmount());
        if (rpItemModel.getResult().getSplitNum() == 1) {
          var10.setVisibility(View.VISIBLE);
        } else {
          for (int i = 1; i < rpItemModel.getResult().getRecord().size(); i++) {
            if (max < Float.parseFloat(rpItemModel.getResult().getRecord().get(i).getBonusAmount())) {
              max = Float.parseFloat(rpItemModel.getResult().getRecord().get(i).getBonusAmount());
              rpItemModel.getResult().getRecord().get(i).setFlag(1);
            } else {
              rpItemModel.getResult().getRecord().get(0).setFlag(1);
            }
          }
        }
      }
      if (var11.getFlag() == 1) {
        var10.setVisibility(View.VISIBLE);
      }
      return var12.a();
    }
  }

  @Override
  public void onClick(int var1) {
    super.onClick(var1);
    if (var1 == R.id.iv_back) {
      this.finish();
    }
  }
}
