package com.ice.httpclient.activity;

import android.view.View;

import com.ice.httpclient.R;
import com.ice.httpclient.presenter.MainPresenter;
import com.ice.httpclient.view.MainView;
import com.ice.httpclient.widget.ColorfulProgressBar;
import com.ice.net.http.Progress;

import butterknife.BindView;

public class MainActivity extends BaseIceActivity<MainView, MainPresenter> implements MainView{

    @BindView(R.id.colorful_progressbar)
    ColorfulProgressBar mColorfulProgressBar;

    @Override
    protected MainPresenter onCreatePresenter() {
        return new MainPresenter();
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {

    }

    public void testGet(View view) {
        mPresenter.testGet();
    }

    public void testPost(View view){
        mPresenter.testPost();
    }
    public void testPostJson(View view){
        mPresenter.testPostJson();
    }

    //申请读写权限（此处省略）
    public void testDownload(View view){
        mPresenter.testDownLoad();
    }
    public void testUpload(View view){
        mPresenter.testUpload();
    }

    @Override
    public void onProgress(Progress progress) {
        mColorfulProgressBar.setMax(progress.totalSize);
        mColorfulProgressBar.setProgress(progress.currentSize);
    }

    @Override
    public void getDataSuccess(String data) {
        mTextView.setText(data);
    }

    @Override
    public void getDataFail(String msg) {
        mTextView.setText(msg);
    }


}