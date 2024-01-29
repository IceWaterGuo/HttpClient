package com.ice.httpclient.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.ice.httpclient.R;
import com.ice.net.base.activity.BaseActivity;

public class MainActivity extends BaseActivity {

    private Button mBtHttp;
    private Button mBtView;

    @Override
    protected void setImmerse() {

    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mBtHttp = findViewById(R.id.bt_http);
        mBtView = findViewById(R.id.bt_view);
    }

    @Override
    public void initListeners() {
        mBtHttp.setOnClickListener(v -> {
            startActivity(new Intent(this, HttpActivity.class));
        });
        mBtView.setOnClickListener(v -> {
            startActivity(new Intent(this, CircleViewActivity.class));
        });
    }

}