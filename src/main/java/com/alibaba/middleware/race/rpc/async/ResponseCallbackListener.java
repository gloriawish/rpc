package com.alibaba.middleware.race.rpc.async;

/**
 * Created by huangsheng.hs on 2015/3/27.
 */
public interface ResponseCallbackListener {
    void onResponse(Object response);
    void onTimeout();
    void onException(Exception e);
}
