package com.alibaba.middleware.race.rpc.demo.service;


import com.alibaba.middleware.race.rpc.async.ResponseCallbackListener;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by huangsheng.hs on 2015/3/28.
 */
public class RaceServiceListener implements ResponseCallbackListener {
    private CountDownLatch latch = new CountDownLatch(1);
    private Object response;

    public Object getResponse() throws InterruptedException {
        latch.await(10, TimeUnit.SECONDS);
        if(response == null)
            throw new RuntimeException("The response doesn't come back.");
        return response;
    }
    @Override
    public void onResponse(Object response) {
        System.out.println("This method is call when response arrived");
        this.response = response;
        latch.countDown();
    }

    @Override
    public void onTimeout() {
        throw new RuntimeException("This call has taken time more than timeout value");
    }

    @Override
    public void onException(Exception e) {
        throw new RuntimeException(e);
    }
}
