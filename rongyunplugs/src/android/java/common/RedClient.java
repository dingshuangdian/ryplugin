package cordova.plugin.ismartnet.rongcloud.common;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import cordova.plugin.ismartnet.rongcloud.retrofit.callback.MyCallBack;
import cordova.plugin.ismartnet.rongcloud.retrofit.HttpUtil;
import cordova.plugin.ismartnet.rongcloud.retrofit.api.Api;
import cordova.plugin.ismartnet.rongcloud.bean.Receive;
import cordova.plugin.ismartnet.rongcloud.utils.DialogDisplay;
import cordova.plugin.ismartnet.rongcloud.utils.LoadingDialogFragment;
import cordova.plugin.ismartnet.rongcloud.utils.ResourcesUtils;
import cordova.plugin.ismartnet.rongcloud.utils.SharedPreferences;
import cordova.plugin.ismartnet.rongcloud.utils.ToastUtils;
import cordova.plugin.ismartnet.rongcloud.fragment.OpenRedFragment;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by lvping on 2017/9/13.
 */

public class RedClient {
  private static RedClient.RedPa mloadListener = new RedClient.RedPa();
  private static ArrayList<String> redPacketIds = new ArrayList();

  public RedClient() {
  }

  /**
   * var0上下文环境
   * var1当前user_id
   * var2当前user_name
   * var3当前user_icon
   * var4红包id
   * var5祝福语
   */
  public static void openGroupRp(Activity var0, String var1, String var2, String var3, String var4, String var5) {
    openRedPacket(var0, 1, var1, var2, var3, var4, var5);
  }

  private static void openRedPacket(final Activity var0, int var1, final String var2, final String var3, String var4, final String var5, final String var6) {
    DialogDisplay.getInstance().dialogLoading(var0, var0.getString(ResourcesUtils.getStringId(var0,"waiting")), mloadListener);
    Map<String, String> map = new HashMap<>();
    map.put("bonusId", var5);
    map.put("tokenId", SharedPreferences.getInstance(var0).getStringValue(Api.BD_TOKEN_ID));
    Call<Receive> receiveCall = HttpUtil.getService().getReceive(SharedPreferences.getInstance(var0).getStringValue(Api.IS_RECEIVE_BONUS), map);
    receiveCall.enqueue(new MyCallBack<Receive>() {
      @Override
      public void onSuccess(Response<Receive> response) {
        if (response.body().isSuccess()) {
          OpenRedFragment openRedFragment = new OpenRedFragment();
          Bundle bundle = new Bundle();
          bundle.putString("userId", var2);
          bundle.putString("envelopeId", var5);
          bundle.putString("userName", var3);
          bundle.putString("envelopeStatus", response.body().getResult());
          bundle.putString("bless", var6);
          openRedFragment.setArguments(bundle);
          FragmentTransaction fragmentTransaction = var0.getFragmentManager().beginTransaction();
          fragmentTransaction.add(openRedFragment, "open_rp");
          fragmentTransaction.commitAllowingStateLoss();
          DialogDisplay.getInstance().dialogCloseLoading(var0);
        }
      }

      @Override
      public void onFail(String message) {
        DialogDisplay.getInstance().dialogCloseLoading(var0);
        ToastUtils.show(var0, message);
      }
    });

    /**
     * 获取群体红包信息
     * 传参：
     * 1、int var1(0||1判断是请求私人还是群体包) user_id  user_name user_icon 红包id
     */
    /**
     * HasMap<String,String>map=new HasMap<>();
     * map.put("var1",var1(0或者1));
     * map.put("user_id",var2);
     * map.put("user_name",var3);
     * map.put("packageId",var5);
     * map.put("user_icon",var4);
     * 获取红包状态(存储到RpInfoModel)
     *
     * envelopeStatus(0,1,2,3)
     * 0,1可领，2过期，3领完
     */
    /**
     * 根据红包状态跳转到红包fragment页面
     * new b(var0, var6, var2, var3, var1);
     * var0:上下文环境(a)
     * var6:packageId(b)
     * var2:user_id(c)
     * var3:user_name(d)
     * var1:0或1标识(e)
     *
     * DialogDisplay.getInstance().dialogCloseLoading(this.a);
     *如果没领取过的(envelopeStatus,红包状态)
     * HashMap<String,String>map=new HasMap<>();
     * map.put("")
     */
  }


  private static class RedPa implements LoadingDialogFragment.LoadingDialogListener {
    private RedPa() {
    }

    public void onCancel() {
      RedClient.redPacketIds.clear();
    }
  }
}
