package com.ice.net.http;

import com.ice.net.callback.Callback;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Desc:
 * Created by icewater on 2021/03/08.
 */
public class HttpObserver implements Observer<ResponseBody> {

    private Callback callback;

    public HttpObserver(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (callback != null) {
            callback.onSubscribe(d);
        }
    }

    @Override
    public void onNext(ResponseBody responseBody) {
        if (callback != null)
            callback.onNext(responseBody);
    }

    @Override
    public void onError(Throwable e) {
        if (callback != null)
            callback.onError(e);
    }

    @Override
    public void onComplete() {
        if (callback != null) {
            callback.onComplete();
        }
    }
}
