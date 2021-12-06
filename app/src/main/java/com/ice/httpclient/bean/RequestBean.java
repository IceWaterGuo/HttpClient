package com.ice.httpclient.bean;

/**
 * Desc:
 * Created by icewater on 2021/12/06.
 */
public class RequestBean {
    public String appKey;
    public String area;

    public RequestBean(String appKey, String area) {
        this.appKey = appKey;
        this.area = area;
    }
}
