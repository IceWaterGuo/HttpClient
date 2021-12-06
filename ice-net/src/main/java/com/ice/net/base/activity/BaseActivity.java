package com.ice.net.base.activity;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hjq.toast.ToastUtils;
import com.ice.net.manager.ActivityCollector;

/**
 * Desc:activity基类
 * Created by icewater on 2021/03/08.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.INSTANCE.addActivity(this);
        setContentView(setLayoutResID());//设置布局文件
        bindView();
        setImmerse();
        fillData();
        initToolbar();
        initView();
        initListeners();
        initData();
    }

    protected abstract void setImmerse();


    public void bindView() {

    }

    public void initToolbar() {

    }


    /**
     * 此方法的目的是子类使用此方法findViewById不再需要强转，注意：接受类型一定不要写错
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T> T findView(int id) {
        T view = (T) findViewById(id);
        return view;
    }

    /**
     * 初始化Listener,需要实现是覆盖
     */
    public void initListeners() {

    }

    public void fillData() {

    }

    /**
     * 设置布局文件
     *
     * @return 布局文件ID
     */
    public abstract int setLayoutResID();


    /**
     * 初始化view,必须实现
     */
    public abstract void initView();


    /**
     * 初始化数据
     */
    public void initData() {

    }

    /**
     * 打印吐司
     *
     * @param msg
     */
    public void showToast(String msg) {
        ToastUtils.show(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.INSTANCE.removeActivity(this);
    }

    /**
     * 显示加载进度条
     *
     * @param title
     */
    public void showLoadingDialog(String title) {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(title);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideLoadingDialog() {

    }

    ;
}
