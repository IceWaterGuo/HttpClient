package com.ice.httpclient.rxbus;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * Desc:RxBus管理订阅工具类
 * Created by icewater on 2024/01/04.
 */
public class RxBusUtil {
    private CompositeDisposable compositeDisposable;

    public RxBusUtil() {
        compositeDisposable = new CompositeDisposable();
    }

    /**
     * 加入 compositeDisposable 管理
     */
    public void add(Disposable disposable) {
        if (disposable != null) {
            compositeDisposable.add(disposable);
        }
    }

    /**
     * 已经加入 compositeDisposable 中的 disposable 如此取消订阅
     */
    private boolean remove(Disposable disposable) {
        if (compositeDisposable != null) {
            return compositeDisposable.remove(disposable);
        } else {
            return false;
        }
    }

    /**
     * 取消所有订阅
     */
    public void clear() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
            compositeDisposable = null;
        }
    }

    /**
     * 没有 加入 compositeDisposable 中的 disposable 如此取消
     */
    public void disposable(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
