package com.ice.net.callback;

import com.google.gson.JsonParseException;
import com.ice.net.http.ErrorType;
import com.ice.net.http.HttpClientException;

import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import retrofit2.HttpException;

/**
 * Desc:网络请求错误处理
 * Created by icewater on 2021/03/08.
 */
public abstract class ErrorHandler {
    /**
     * 处理错误信息
     *
     * @param throwable
     */
    public final void handleError(Throwable throwable) {
        String errString;
        ErrorType errorType;

        if (throwable instanceof SocketTimeoutException || throwable instanceof TimeoutException) {
            errString = "链接超时";
            errorType = ErrorType.TYPE_ERROR;
        } else if (throwable instanceof NoRouteToHostException) {
            errString = "服务器累趴下啦";
            errorType = ErrorType.TYPE_ERROR;
        } else if (throwable instanceof SocketException) {
            errString = "服务器累趴下啦";
            errorType = ErrorType.TYPE_ERROR;
        } else if (throwable instanceof UnknownHostException) {
            errString = "请检查网络设置";
            errorType = ErrorType.TYPE_ERROR;
        } else if (throwable instanceof ArithmeticException) {
            errString = "客户端异常";
            errorType = ErrorType.TYPE_ERROR;
        } else if (throwable instanceof NullPointerException) {
            errString = "客户端异常";
            errorType = ErrorType.TYPE_ERROR;
        } else if (throwable instanceof JsonParseException) {
            errString = "数据解析异常";
            errorType = ErrorType.TYPE_ERROR;
        } else if (throwable instanceof HttpClientException) {
            errString = throwable.getMessage();
            errorType = ErrorType.TYPE_ERROR;
        } else if (throwable instanceof HttpException) { // 网络错误
            HttpException httpEx = (HttpException) throwable;
            errorType = ErrorType.TYPE_ERROR;
            if (httpEx.getMessage().contains("timed out")) {
                errString = "连接超时";
            } else if (httpEx.code() == 500) {
                errString = "服务器累趴下啦";
            } else if (httpEx.code() == 0) {
                errString = "服务器累趴下啦";
            } else if (httpEx.code() == 401) {
                errString = "未授权";
            } else if (httpEx.code() == 403) {
                errString = "禁止访问";
            } else if (httpEx.code() == 404) {
                errString = "资源不存在";
            } else {
                //其他错误
                errString = "未知网络错误";
            }
        } else {
            errString = "服务器累趴下啦";
            errorType = ErrorType.TYPE_ERROR;
        }
        onHandleError(errString, errorType);
    }

    protected abstract void onHandleError(String errString, ErrorType errorType);
}
