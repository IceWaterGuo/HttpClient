package com.ice.httpclient.view;

import com.ice.net.base.view.BaseView;
import com.ice.net.http.Progress;

/**
 * Desc:
 * Created by icewater on 2021/12/06.
 */
public interface HttpView extends BaseView {
    void onProgress(Progress progress);

    void getDataSuccess(String data);

    void getDataFail(String msg);
}
