package com.ice.net.base.view;

/**
 * Desc:
 * Created by icewater on 2021/03/08.
 */
public interface BaseView {

    /**
     * 显示对话框
     */
    void showToast(String msg);

    /**
     * 显示加载中dialog
     *
     * @param title
     */
    void showLoadingDialog(String title);

    /**
     * 隐藏加载中dialog
     */
    void hideLoadingDialog();

    /**
     * 显示加载重试的加载中
     */
    void showRetryLoading();

    /**
     * 显示一般错误页面
     */
    void showRetryError(String errorMsg);

    /**
     * 显示网络错误页面
     */
    void showRetryNetError();

    /**
     * 显示数据为空的页面
     */
    void showRetryEmpty();

    /**
     * 显示加载成功页面
     */
    void showRetryContent();
}
