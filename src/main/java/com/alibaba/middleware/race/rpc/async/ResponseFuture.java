package com.alibaba.middleware.race.rpc.async;

import com.alibaba.middleware.race.rpc.model.RpcResponse;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by huangsheng.hs on 2015/3/27.
 */
public class ResponseFuture {
    public static ThreadLocal<Future<Object>> futureThreadLocal = new ThreadLocal<Future<Object>>();

    public static Object getResponse(long timeout) throws InterruptedException {
        if (null == futureThreadLocal.get()) {
            throw new RuntimeException("Thread [" + Thread.currentThread() + "] have not set the response future!");
        }

        try {
            RpcResponse response = (RpcResponse) (futureThreadLocal.get().get(timeout, TimeUnit.MILLISECONDS));
            if (response.isError()) {
                throw new RuntimeException(response.getErrorMsg());
            }
            return response.getAppResponse();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException("Time out", e);
        }
    }

    public static void setFuture(Future<Object> future){
        futureThreadLocal.set(future);
    }
}
