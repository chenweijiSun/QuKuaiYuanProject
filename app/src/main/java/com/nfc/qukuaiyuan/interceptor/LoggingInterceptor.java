package com.nfc.qukuaiyuan.interceptor;

import com.nfc.qukuaiyuan.utils.jutils.JUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by cwj on 2018/3/22.
 */

public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        JUtils.Log(String.format("发送请求 %s on %s%n%s", request.url(), chain.connection(), request.headers()));
        return chain.proceed(request);
    }
}
