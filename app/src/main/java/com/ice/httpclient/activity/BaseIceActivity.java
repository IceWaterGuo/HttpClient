package com.ice.httpclient.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.ice.httpclient.R;
import com.ice.net.base.activity.MVPActivity;
import com.ice.net.base.presenter.BasePresenter;
import com.ice.net.base.view.BaseView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Desc:
 * Created by icewater on 2021/12/06.
 */
public abstract class BaseIceActivity<V extends BaseView, P extends BasePresenter<V>> extends MVPActivity<V, P>{

    //测试base类中使用ButterKnife
    @BindView(R.id.tv_text)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void setImmerse() {

    }

    public void showRetryLoading() {

    }

    public void showRetryError(String errorMsg) {

    }

    public void showRetryNetError() {

    }

    public void showRetryEmpty() {

    }

    public void showRetryContent() {

    }
}
