package cordova.plugin.ismartnet.rongcloud.retrofit.callback;

import cordova.plugin.ismartnet.rongcloud.App;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lvping on 2017/9/26.
 */

public abstract class MyCallBack<T> implements Callback<T> {
  private android.os.Handler handler = new android.os.Handler();

  @Override
  public void onResponse(Call<T> call, Response<T> response) {
    if (response.raw().code() == 200) {
      onSuccess(response);
    } else if (response.raw().code() == 401) {
      onFail("用户授权过期，请重新登陆");
      handler.postDelayed(new Runnable() {
        @Override
        public void run() {
          App.getInstance().exit();
        }
      }, 2000);
    }
  }

  @Override
  public void onFailure(Call<T> call, Throwable t) {
    onFail("网络连接超时，请稍后再试~！");
  }

  public abstract void onSuccess(Response<T> response);

  public abstract void onFail(String message);
}
