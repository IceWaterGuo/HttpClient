package com.ice.net.callback;

import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Desc:回调接口
 * Created by icewater on 2021/03/08.
 */
public interface Callback<T> {
    void onSubscribe(Disposable disposable);
    void onNext(ResponseBody responseBody);
    void onError(Throwable e);
    void onComplete();
}
