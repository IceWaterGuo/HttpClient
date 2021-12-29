package com.ice.httpclient;

import android.annotation.SuppressLint;

import com.ice.httpclient.utils.AppLog;
import com.ice.net.BaseApplication;
import com.ice.net.http.HttpConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Desc:
 * Created by icewater on 2021/12/29.
 */
public class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        init(this);
    }

    private void init(MyApplication application) {
        AppLog.setDEBUG(true);
        initHttpClient();
    }

    /**
     * 初始化网络框架
     */
    private void initHttpClient() {
        HttpConfig.create()
                .baseUrl("https://www.jianshu.com/")
                .addInterceptor(new LoggingInterceptor())
                .builder();
    }

    static class LoggingInterceptor implements Interceptor {
        @SuppressLint("DefaultLocale")
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            AppLog.e(String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            AppLog.e(String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            return response;
        }
    }

}
