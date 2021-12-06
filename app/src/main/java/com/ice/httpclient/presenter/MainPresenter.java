package com.ice.httpclient.presenter;

import android.util.Log;

import com.ice.httpclient.bean.BaseBean;
import com.ice.httpclient.bean.NewsBean;
import com.ice.httpclient.bean.RequestBean;
import com.ice.httpclient.bean.WeatherBean;
import com.ice.httpclient.view.MainView;
import com.ice.net.base.presenter.BasePresenter;
import com.ice.net.callback.FileCallback;
import com.ice.net.callback.HttpCallback;
import com.ice.net.http.HttpClient;
import com.ice.net.http.Progress;

import java.util.HashMap;

/**
 * Desc:
 * Created by icewater on 2021/12/06.
 */
public class MainPresenter extends BasePresenter<MainView> {
    public void testGet() {
        //        HashMap<String, Object> params = new HashMap<>();
        //        params.put("appKey", "1328ffff76a74d2987914a0de08b9f44");
        //        params.put("category", "要闻");
        //        params.put("updateTime", "2018-06-21 00:00:00");
        HttpClient.create(this)
                //                .params(params)
                .requestParams("appKey", "1328ffff76a74d2987914a0de08b9f44")
                .requestParams("category", "要闻")
                .requestParams("updateTime", "2018-06-21 00:00:00")
                .heads("a", "aa")
                .showLoading(true)
                .url("api/news/list")
                .builder()
                .request(new HttpCallback<BaseBean<NewsBean>>() {
                    @Override
                    public void onSuccess(BaseBean<NewsBean> newsBeanBaseBean) {
                        mView.getDataSuccess(newsBeanBaseBean.toString());
                    }

                    @Override
                    public void onError(String msg) {
                        mView.getDataFail(msg);
                    }
                });

    }

    public void testPost() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("appKey", "b5baa6d5add44cc3a6f9bd7596953669");
        params.put("area", "苏州");

        HttpClient.create(this)
                .params(params)
                .showLoading(true)
                .url("api/weather/area")
                .heads("a", "aa")
                .heads("b", "bb")
                .post()
                .builder()
                .request(new HttpCallback<BaseBean<WeatherBean>>() {
                    @Override
                    public void onSuccess(BaseBean<WeatherBean> weatherBeanBaseBean) {
                        mView.getDataSuccess(weatherBeanBaseBean.toString());
                    }

                    @Override
                    public void onError(String msg) {
                        mView.getDataFail(msg);
                    }
                });
    }

    /**
     * FileCallback
     */
    public void testDownLoad() {
        //http://app.mi.com/download/427632
        HttpClient.create(this)
                .download()
                .url("software/mobile/MockMobile_v3.4.0.0.apk")
                .baseUrl("http://cdn.mockplus.cn/")
                .builder()
                .request(new FileCallback() {

                    @Override
                    public void onStart() {
                        Log.e("399", "onStart");
                    }

                    @Override
                    public void onDownloadComplete() {
                        Log.e("399", "onDownloadComplete" + " thread: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("399", "onError" + " thread: " + Thread.currentThread().getName() + "  msg:" + msg);
                    }

                    @Override
                    public void onDownloading(Progress progress) {
                        mView.onProgress(progress);
                    }
                });
    }

    public void testPostJson() {
        RequestBean requestBean = new RequestBean("b5baa6d5add44cc3a6f9bd7596953669", "苏州");
        HttpClient.create(this)
                .params(requestBean)
                .postJson()
                .url("api/weather/area")
                .showLoading(true)
                .builder()
                .request(new HttpCallback<String>() {
                    @Override
                    public void onSuccess(String s) {
                        mView.getDataSuccess(s);
                    }

                    @Override
                    public void onError(String msg) {
                        mView.getDataFail(msg);
                    }
                });
    }

    public void testUpload() {
//        HttpClient.create(this)
//                .url("")
//                .upload(new File(""))
//                .builder()
//                .request(new HttpCallback<String>() {
//                    @Override
//                    public void onSuccess(String s) {
//
//                    }
//
//                    @Override
//                    public void onError(String msg) {
//
//                    }
//                });

    }

}
