package com.ice.net.http;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;

/**
 * Desc:
 * Created by icewater on 2021/03/08.
 */
public class HttpConfig {

    private HttpConfig(Builder builder) {
        RetrofitCreator.getInstance().initBuilder(builder);
    }

    public static Builder create(){
        return new Builder();
    }

    public static class Builder{
        protected static final List<Interceptor> INTERCEPTORS = new ArrayList<>();
        protected String BASE_URL;
        protected long readTimeout = 60;//读取超时时间
        protected TimeUnit readTimeUnit = TimeUnit.SECONDS;
        protected long writeTimeout = 60;//写入超时时间
        protected TimeUnit writeTimeUnit = TimeUnit.SECONDS;
        protected long connTimeout = 60;//连接超时时间
        protected TimeUnit connTimeUnit = TimeUnit.SECONDS;

        public Builder baseUrl(String baseUrl) {
            if (TextUtils.isEmpty(baseUrl)) {
                throw new IllegalArgumentException("baseUrl cannot be null");
            }
            BASE_URL = baseUrl;
            return this;
        }

        public Builder readTimeout(long readTimeout, TimeUnit timeUnit) {
            this.readTimeout = readTimeout;
            this.readTimeUnit = timeUnit;
            return this;
        }

        public Builder writeTimeout(long writeTimeout, TimeUnit timeUnit) {
            this.writeTimeout = writeTimeout;
            this.writeTimeUnit = timeUnit;
            return this;
        }

        public Builder connTimeout(long connTimeout, TimeUnit timeUnit) {
            this.connTimeout = connTimeout;
            this.connTimeUnit = timeUnit;
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor) {
            INTERCEPTORS.add(interceptor);
            return this;
        }

        public void builder(){
            new HttpConfig(this);
        }
    }
}
