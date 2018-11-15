package bcaasc.io.ethdemo.http.retrofit;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/8/20
 * <p>
 * Http：Retrofit封裝网络请求
 */
public class RetrofitFactory {

    private static Retrofit instance;
    private static OkHttpClient client;


    private static void initClient() {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(3, TimeUnit.SECONDS)
                    .readTimeout(3, TimeUnit.SECONDS)
                    .writeTimeout(3, TimeUnit.SECONDS)
                    .addInterceptor(new OkHttpInterceptor())
                    .build();
        }
    }

    //正式
    private static String baseUrl = "https://mainnet.infura.io/";
    // 测试
    private static String testBaseUrl = "https://mainnet.infura.io/";

    /**
     * AN api
     */
    public static Retrofit getInstance() {
        initClient();
        instance = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(new StringConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//Observable，暂时没用
                .build();
        return instance;
    }


}
