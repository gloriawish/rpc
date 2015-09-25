package com.alibaba.middleware.race.rpc.demo.service;

/**
 * Created by huangsheng.hs on 2015/3/27.
 */
public class RaceException extends RuntimeException{
    private String flag = "race";
    public RaceException(String message){
        super(message);
    }
    public RaceException(Exception e){
        super(e);
    }
    public String getFlag(){
        return flag;
    }
}
