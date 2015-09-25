package com.alibaba.middleware.race.rpc.demo.service;

import com.alibaba.middleware.race.rpc.aop.ConsumerHook;
import com.alibaba.middleware.race.rpc.context.RpcContext;
import com.alibaba.middleware.race.rpc.model.RpcRequest;

/**
 * Created by huangsheng.hs on 2015/5/7.
 */
public class RaceConsumerHook implements ConsumerHook{
    @Override
    public void before(RpcRequest request) {
        RpcContext.addProp("hook key","this is pass by hook");
    }

    @Override
    public void after(RpcRequest request) {
        System.out.println("I have finished Rpc calling.");
    }
}
