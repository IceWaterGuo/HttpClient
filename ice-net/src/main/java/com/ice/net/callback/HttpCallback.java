package com.ice.net.callback;

import com.google.gson.Gson;
import com.ice.net.base.presenter.BasePresenter;
import com.ice.net.http.ErrorType;
import com.ice.net.http.HttpClientException;
import com.ice.net.http.HttpUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Desc:
 * Created by icewater on 2021/03/08.
 */
public abstract class HttpCallback<ResultType> extends ErrorHandler implements Callback<ResultType> {
    private boolean mShowLoading;
    private boolean mShowRetry;
    private String mTitle;
    private WeakReference<BasePresenter> mPresenterWReference;

    public final void init(WeakReference<BasePresenter> presenterWReference, boolean showLoading, boolean showRetry, String title) {
        this.mPresenterWReference = presenterWReference;
        this.mShowLoading = showLoading;
        this.mShowRetry = showRetry;
        this.mTitle = title;
    }

    @Override
    public final void onSubscribe(Disposable disposable) {
        if (mPresenterWReference != null && mPresenterWReference.get() != null) {
            mPresenterWReference.get().addDisposable(disposable);
        }
        onStart();
    }

    @Override
    public final void onNext(ResponseBody responseBody) {
        parseResponse(responseBody);
    }

    @Override
    public final void onError(Throwable throwable) {
        if(mPresenterWReference != null && mPresenterWReference.get() != null){
            boolean netWorkAvailable = HttpUtils.isNetWorkAvailable(mPresenterWReference.get().mActivity.getApplicationContext());
            if(!netWorkAvailable){
                onHandleError("请检查你的网络设置",ErrorType.TYPE_NET_ERROR);
            }else {
                handleError(throwable);
            }
        }else {
            handleError(throwable);
        }

        onComplete();
    }

    @Override
    public final void onComplete() {
        if (mShowLoading && mPresenterWReference != null && mPresenterWReference.get() != null) {
            mPresenterWReference.get().mView.hideLoadingDialog();
        }
    }

    private void onStart() {
        //显示加载中
        if (mShowLoading && mPresenterWReference != null && mPresenterWReference.get() != null) {
            mPresenterWReference.get().mView.showLoadingDialog(mTitle);
        }
        //显示加载重试的加载中页面
        if (mShowRetry && mPresenterWReference != null && mPresenterWReference.get() != null) {
            mPresenterWReference.get().mView.showRetryLoading();
        }
    }

    private void parseResponse(ResponseBody responseBody) {
        if (responseBody == null) {
            handleError(new HttpClientException("服务器累趴下啦"));
            return;
        }

        Type genType = getClass().getGenericSuperclass();
        Type type = ((ParameterizedType) genType).getActualTypeArguments()[0];
        try {
            if (type == ResponseBody.class) {
                onHandleSuccess((ResultType) responseBody);
            } else if (type == String.class) {
                onHandleSuccess((ResultType) responseBody.string());
            } else {
                ResultType resultType = new Gson().fromJson(responseBody.string(), type);
                if (resultType == null) {
                    handleError(new HttpClientException("服务器数据异常"));
                } else {
                    onHandleSuccess(resultType);
                }
            }
        } catch (Exception e) {
            handleError(e);
        } finally {
            responseBody.close();
        }
    }

    @Override
    public final void onHandleError(String errString, ErrorType errorType) {
        if (mShowRetry) {
            switch (errorType) {
                case TYPE_ERROR:
                    if (mPresenterWReference != null && mPresenterWReference.get() != null) {
                        mPresenterWReference.get().mView.showRetryError(errString);
                    }
                    break;
                case TYPE_NET_ERROR:
                    if (mPresenterWReference != null && mPresenterWReference.get() != null) {
                        mPresenterWReference.get().mView.showRetryNetError();
                    }
                    break;
            }
        } else {
            onError(errString);
        }
    }

    public final void onHandleSuccess(ResultType resultType) {
        if (mShowRetry) {
            if (mPresenterWReference != null && mPresenterWReference.get() != null) {
                mPresenterWReference.get().mView.showRetryContent();
            }
        }
        onSuccess(resultType);
    }

    public abstract void onSuccess(ResultType resultType);

    public void onError(String msg){}
}
