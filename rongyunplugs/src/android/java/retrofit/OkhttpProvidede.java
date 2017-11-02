package cordova.plugin.ismartnet.rongcloud.retrofit;

import android.content.Context;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;


public class OkhttpProvidede {
    static OkHttpClient okHttpClient;

    public static OkHttpClient okHttpClient(final Context context, String BASE_URL) {
        if (okHttpClient == null) {
            synchronized (OkhttpProvidede.class) {
                if (okHttpClient == null) {
                    OkHttpClient client = new OkHttpClient.Builder()
                            .retryOnConnectionFailure(true)
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(12, TimeUnit.SECONDS)
                            .writeTimeout(12, TimeUnit.SECONDS)
                            .build();
                    okHttpClient = client;
                }

            }

        }
        return okHttpClient;
    }
}
