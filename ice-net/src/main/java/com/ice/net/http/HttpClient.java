package com.ice.net.http;


import com.ice.net.base.presenter.BasePresenter;
import com.ice.net.callback.Callback;
import com.ice.net.callback.FileCallback;
import com.ice.net.callback.HttpCallback;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Desc:
 * Created by icewater on 2021/03/08.
 */
public class HttpClient {
    private WeakReference<BasePresenter> presenterWReference;
    private boolean showLoading;
    private boolean showRetry;
    private String title;
    private HttpMethod httpMethod;
    private HashMap<String, Object> params;
    private HashMap<String, String> heads;
    private Object requestBean;
    private String url;
    private String baseUrl;
    private File uploadFile;

    private HttpClient(Builder builder) {
        this.presenterWReference = builder.presenterWReference;
        this.showLoading = builder.showLoading;
        this.showRetry = builder.showRetry;
        this.title = builder.title;
        this.httpMethod = builder.httpMethod;
        this.params = builder.params;
        this.heads = builder.heads;
        this.requestBean = builder.requestBean;
        this.url = builder.url;
        this.baseUrl = builder.baseUrl;
        this.uploadFile = builder.uploadFile;
    }

    public <T> void request(Callback<T> callback) {
        if (callback instanceof HttpCallback) {
            ((HttpCallback) callback).init(presenterWReference, showLoading, showRetry, title);
        } else if (callback instanceof FileCallback) {
            ((FileCallback) callback).init(presenterWReference, url);
        }
        Request.newRequest(httpMethod, params, heads, requestBean,
                url, baseUrl, uploadFile, callback);
    }


    public static Builder create(BasePresenter basePresenter) {
        return new Builder(basePresenter);
    }

    public static class Builder {
        private WeakReference<BasePresenter> presenterWReference;
        private boolean showLoading;
        private boolean showRetry;
        private String title;
        private HttpMethod httpMethod = HttpMethod.GET;
        private HashMap<String, Object> params = new HashMap<>();
        private HashMap<String, String> heads = new HashMap<>();
        private Object requestBean;
        private String url;
        private String baseUrl;
        private File uploadFile;

        public Builder(BasePresenter basePresenter) {
            presenterWReference = new WeakReference<>(basePresenter);
        }

        public Builder showLoading(boolean showLoading) {
            this.showLoading = showLoading;
            return this;
        }

        public Builder showRetry(boolean showRetry) {
            this.showRetry = showRetry;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder get() {
            this.httpMethod = HttpMethod.GET;
            return this;
        }

        public Builder post() {
            this.httpMethod = HttpMethod.POST;
            return this;
        }

        public Builder postJson() {
            this.httpMethod = HttpMethod.POST_JSON;
            return this;
        }

        public Builder putJson() {
            this.httpMethod = HttpMethod.PUT_JSON;
            return this;
        }

        public Builder put() {
            this.httpMethod = HttpMethod.PUT;
            return this;
        }

        public Builder delete() {
            this.httpMethod = HttpMethod.DELETE;
            return this;
        }

        public Builder upload(File uploadFile) {
            this.uploadFile = uploadFile;
            httpMethod = HttpMethod.UPLOAD;
            return this;
        }

        public Builder download() {
            this.httpMethod = HttpMethod.DOWNLOAD;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder mapParams(HashMap<String, Object> params) {
            this.params.putAll(params);
            return this;
        }

        public Builder params(Object requestBean) {
            this.requestBean = requestBean;
            return this;
        }

        public Builder requestParams(String key, Object value) {
            this.params.put(key, value);
            return this;
        }

        public Builder heads(String key, String value) {
            this.heads.put(key, value);
            return this;
        }

        public HttpClient builder() {
            checkup();
            return new HttpClient(this);
        }

        private void checkup() {

            if (url == null || url.length() == 0) {
                throw new IllegalArgumentException("url cannot be null");
            }

            if (showRetry && showLoading) {
                throw new IllegalArgumentException("showRetry and showLoading only one be true");
            }
        }
    }
}
