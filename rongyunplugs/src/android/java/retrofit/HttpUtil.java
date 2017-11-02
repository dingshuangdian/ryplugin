package cordova.plugin.ismartnet.rongcloud.retrofit;

import android.content.Context;
import android.support.annotation.CheckResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cordova.plugin.ismartnet.rongcloud.retrofit.api.RetrofitService;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求封装类
 * Created by 耿 on 2016/6/28.
 */
public class HttpUtil {
  private static volatile HttpUtil mInstance;
  private static volatile RetrofitService mService;
  private Context mAppliactionContext;

  //构造函数私有，不允许外部调用
  private HttpUtil(RetrofitService mService, Context mAppliactionContext) {
    this.mService = mService;
    this.mAppliactionContext = mAppliactionContext;
  }

  @CheckResult
  public static RetrofitService getService() {
    if (mInstance == null) {
      throw new NullPointerException("HttpUtil has not be initialized");
    }
    return mService;
  }

  @CheckResult
  public static HttpUtil getmInstance() {
    if (mInstance == null) {
      throw new NullPointerException("HttpUtil has not be initialized");
    }
    return mInstance;
  }




  public static class SingletonBuilder {
    private Context appliactionContext;
    private String baseUrl;
    private List<String> servers = new ArrayList<>();
    private List<Converter.Factory> converterFactories = new ArrayList<>();
    OkHttpClient client;

    public SingletonBuilder(Context context, String baseUrl) {
      appliactionContext = context.getApplicationContext();
      this.baseUrl = baseUrl;
      client = OkhttpProvidede.okHttpClient(appliactionContext, baseUrl);
    }

    public SingletonBuilder client(OkHttpClient client) {
      this.client = client;
      return this;
    }


    public SingletonBuilder addServerUrl(String ipUrl) {
      this.servers.add(ipUrl);
      return this;
    }

    public SingletonBuilder serverUrls(List<String> servers) {
      this.servers = servers;
      return this;
    }

    public SingletonBuilder converterFactory(Converter.Factory factory) {
      this.converterFactories.add(factory);
      return this;
    }


    public HttpUtil build() {
      if (checkNULL(this.baseUrl)) {
        throw new NullPointerException("BASE_URL can not be null");
      }
      if (converterFactories.size() == 0) {
        converterFactories.add(GsonConverterFactory.create());
      }
      Retrofit.Builder builder = new Retrofit.Builder();

      for (Converter.Factory converterFactory : converterFactories) {
        builder.addConverterFactory(converterFactory);
      }

      Retrofit retrofit = builder
        .baseUrl(baseUrl)
        .client(client).build();
      RetrofitService retrofitHttpService =
        retrofit.create(RetrofitService.class);

      mInstance = new HttpUtil(retrofitHttpService, appliactionContext);
      return mInstance;
    }
  }

  // 判断是否NULL
  @CheckResult
  public static boolean checkNULL(String str) {
    return str == null || "null".equals(str) || "".equals(str);

  }


  static Map<String, Call> CALL_MAP = new HashMap<>();

  /*
  *添加某个请求
  *@author Administrator
  *@date 2016/10/12 11:00
  */
  public static synchronized void putCall(String url, Call call) {

    synchronized (CALL_MAP) {
      CALL_MAP.put(url, call);
    }
  }

  /*
  *取消某个界面都所有请求，或者是取消某个tag的所有请求
  * 如果要取消某个tag单独请求，tag需要转入tag+url
  *@author Administrator
  *@date 2016/10/12 10:57
  */
  public static synchronized void cancel(Object tag) {
    if (tag == null)
      return;
    List<String> list = new ArrayList<>();
    synchronized (CALL_MAP) {
      for (String key : CALL_MAP.keySet()) {
        if (key.startsWith(tag.toString())) {
          CALL_MAP.get(key).cancel();
          list.add(key);
        }
      }
    }
    for (String s : list) {
      removeCall(s);
    }

  }

  /*
  *移除某个请求
  *@author Administrator
  *@date 2016/10/12 10:58
  */
  public static synchronized void removeCall(String url) {
    synchronized (CALL_MAP) {
      for (String key : CALL_MAP.keySet()) {
        if (key.contains(url)) {
          url = key;
          break;
        }
      }
      CALL_MAP.remove(url);
    }
  }


}
