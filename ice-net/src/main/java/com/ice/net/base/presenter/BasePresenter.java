package com.ice.net.base.presenter;

import android.app.Activity;

import com.ice.net.base.view.BaseView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;


/**
 * Desc:
 * Created by icewater on 2021/03/08.
 */
public abstract class BasePresenter<V extends BaseView> {
    public V mView;
    public Activity mActivity;
    private final List<Disposable> mDisposableList = new ArrayList<>();

    public void onCreate(V view, Activity activity) {
        this.mView = view;
        this.mActivity = activity;
    }

    public void onDestroy() {
        release();
    }

    public void onLowMemory() {
        release();
    }

    private void release() {
        mView = null;
        mActivity = null;
        for (Disposable disposable : mDisposableList) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
        mDisposableList.clear();
    }

    public void addDisposable(Disposable d) {
        if (!mDisposableList.contains(d))
            mDisposableList.add(d);
    }
}
