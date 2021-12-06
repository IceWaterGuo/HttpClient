package com.ice.net.callback;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.text.TextUtils;

import com.ice.net.base.presenter.BasePresenter;
import com.ice.net.http.ErrorType;
import com.ice.net.http.FileUtils;
import com.ice.net.http.HttpUtils;
import com.ice.net.http.Progress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Desc:
 * Created by icewater on 2021/03/08.
 */
public abstract class FileCallback extends ErrorHandler implements Callback {

    private WeakReference<BasePresenter> mPresenterWReference;
    private String destFileName;//指定的文件的名字
    private String destFileDir;//指定的下载目录

    public FileCallback() {
    }

    public FileCallback(String destFileName) {
        this.destFileName = destFileName;
    }

    public FileCallback(String destFileName, String destFileDir) {
        this.destFileName = destFileName;
        this.destFileDir = destFileDir;
    }

    public void init(WeakReference<BasePresenter> presenterWReference, String url) {
        this.mPresenterWReference = presenterWReference;
        getFileName(url);
    }

    private void getFileName(String url) {
        if (TextUtils.isEmpty(destFileDir)) {
            this.destFileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";
        }
        if (TextUtils.isEmpty(destFileName)) {
            int index = url.lastIndexOf("/");
            if (index != -1) {
                destFileName = url.substring(index);
            } else {
                destFileName = url;
            }
        }
        FileUtils.createFolder(destFileDir);
        FileUtils.createFile(new File(destFileDir, destFileName));

    }

    @Override
    public void onSubscribe(Disposable disposable) {
        if (mPresenterWReference != null && mPresenterWReference.get() != null) {
            mPresenterWReference.get().addDisposable(disposable);
        }
        onStart();
    }

    @Override
    public void onNext(final ResponseBody responseBody) {
        handleResponse(responseBody);
    }

    @Override
    public void onError(Throwable e) {
        if(mPresenterWReference != null && mPresenterWReference.get() != null){
            boolean netWorkAvailable = HttpUtils.isNetWorkAvailable(mPresenterWReference.get().mActivity.getApplicationContext());
            if(!netWorkAvailable){
                onHandleError("请检查你的网络设置", ErrorType.TYPE_NET_ERROR);
            }else {
                handleError(e);
            }
        }else {
            handleError(e);
        }

        onDownloadComplete();
    }

    @Override
    public void onComplete() {
    }

    @SuppressLint("CheckResult")
    private void handleResponse(final ResponseBody responseBody) {
        Observable.create(new ObservableOnSubscribe<Progress>() {
            @Override
            public void subscribe(ObservableEmitter<Progress> emitter) throws Exception {

                Progress progress = new Progress();
                progress.totalSize = responseBody.contentLength();
                InputStream bodyStream = null;
                FileOutputStream fileOutputStream = null;
                try {
                    bodyStream = responseBody.byteStream();

                    fileOutputStream = new FileOutputStream(new File(destFileDir, destFileName));
                    byte[] buffer = new byte[8192];
                    int len = -1;
                    int currentLen = 0;
                    while ((len = bodyStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                        currentLen += len;
                        progress.currentSize = currentLen;
                        progress.fraction = currentLen *1.0f/progress.totalSize;
                        emitter.onNext(progress);
                    }
                    fileOutputStream.flush();
                }catch (Exception e){
                    emitter.onError(e);
                }finally {
                    try {
                        if(bodyStream != null){
                            bodyStream.close();
                        }
                        if(fileOutputStream != null){
                            fileOutputStream.close();
                        }
                        responseBody.close();
                    }catch (Exception e){
                        emitter.onError(e);
                    }
                    emitter.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Progress>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Progress progress) {
                        onDownloading(progress);
                    }

                    @Override
                    public void onError(Throwable e) {
                        handleError(e);
                    }

                    @Override
                    public void onComplete() {
                        onDownloadComplete();
                    }
                });
    }

    @Override
    protected void onHandleError(String errString, ErrorType errorType) {
        onError(errString);
    }

    public abstract void onStart();

    public abstract void onDownloading(Progress progress);

    public abstract void onDownloadComplete();

    public abstract void onError(String msg);
}
