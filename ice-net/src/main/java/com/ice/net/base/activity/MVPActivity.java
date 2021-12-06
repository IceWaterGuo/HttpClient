package com.ice.net.base.activity;

import android.os.Bundle;

import com.ice.net.base.presenter.BasePresenter;
import com.ice.net.base.view.BaseView;

/**
 * Desc:
 * Created by icewater on 2021/03/08.
 */
public abstract class MVPActivity<V extends BaseView, P extends BasePresenter<V>> extends BaseActivity {

    public P mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPresenter = onCreatePresenter();
        if (mPresenter != null) {
            mPresenter.onCreate((V) this, this);
        }
        super.onCreate(savedInstanceState);
    }

    protected abstract P onCreatePresenter();

    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        if (mPresenter != null) {
            mPresenter.onLowMemory();
        }
        super.onLowMemory();
    }
}
