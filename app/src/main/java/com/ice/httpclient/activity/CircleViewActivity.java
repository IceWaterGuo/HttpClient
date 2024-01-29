package com.ice.httpclient.activity;

import android.widget.Button;

import com.ice.httpclient.R;
import com.ice.httpclient.widget.MyCircleProgressView;
import com.ice.net.base.activity.BaseActivity;

import kotlinx.coroutines.EventLoopImplBase;

/**
 * Desc:
 * Created by icewater on 2024/01/22.
 */
public class CircleViewActivity extends BaseActivity {

    private Button mSetProgress;
    private Button mResetProgress;
    private MyCircleProgressView mProgressView;

    @Override
    protected void setImmerse() {

    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_circle_view;
    }

    @Override
    public void initView() {
        mSetProgress = findViewById(R.id.set_progress);
        mResetProgress = findViewById(R.id.reset_progress);
        mProgressView = findViewById(R.id.progress_view);
    }

    @Override
    public void initListeners() {
        mSetProgress.setOnClickListener(v -> {
            mProgressView.setProgress("75");
        });
        mResetProgress.setOnClickListener(v -> {
            mProgressView.setProgress("0");
        });
    }
}
