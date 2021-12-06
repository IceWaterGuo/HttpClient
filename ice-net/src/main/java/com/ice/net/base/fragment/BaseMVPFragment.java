package com.ice.net.base.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.ice.net.base.presenter.BasePresenter;
import com.ice.net.base.view.BaseView;


/**
 * Desc:
 * Created by icewater on 2021/03/08.
 */
public abstract class BaseMVPFragment<V extends BaseView, P extends BasePresenter<V>> extends BaseFragment {

    public P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = onCreatePresenter();
        if (mPresenter != null) {
            mPresenter.onCreate((V) this, getActivity());
        }
        setHasOptionsMenu(true);
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
