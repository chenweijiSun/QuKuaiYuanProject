package com.nfc.qukuaiyuan.http;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nfc.qukuaiyuan.BuildConfig;
import com.nfc.qukuaiyuan.Constant;
import com.nfc.qukuaiyuan.interceptor.LoggingInterceptor;
import com.nfc.qukuaiyuan.utils.jutils.JUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * Created by cwj on 16/7/17.
 */
public class HttpClient {
    private static final Object monitor = new Object();
    private static IDreamRetrofit fruitRetrofit;
    private static Retrofit retrofit;
    private static OkHttpClient mOkHttpClient;
    //短缓存有效期为1秒钟
    public static final int CACHE_STALE_SHORT = 1;
    //长缓存有效期为7天
    public static final int CACHE_STALE_LONG = 60 * 60 * 24 * 7;

    private HttpClient() {

    }

    static {
        initOKHttpClient();
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }


    public static IDreamRetrofit getGankRetrofitInstance() {
        synchronized (monitor) {
            if (fruitRetrofit == null) {
                fruitRetrofit = retrofit.create(IDreamRetrofit.class);
            }
            return fruitRetrofit;
        }
    }

    private static void initOKHttpClient() {

        if (mOkHttpClient == null) {
            synchronized (monitor) {
                // 指定缓存路径,缓存大小100Mb
                OkHttpClient.Builder builder = new OkHttpClient.Builder()
                        .retryOnConnectionFailure(true)
                        .connectTimeout(20, TimeUnit.SECONDS);

                if (BuildConfig.DEBUG) {
                    //okhttp输出日志
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
                            JUtils.Log("收到响应: " + message);
                        }
                    });
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    builder.addInterceptor(interceptor);
                    builder.addInterceptor(new LoggingInterceptor());
                }
                mOkHttpClient = builder.build();

            }
        }
    }
}
