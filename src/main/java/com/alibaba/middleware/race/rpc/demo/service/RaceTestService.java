package com.alibaba.middleware.race.rpc.demo.service;

import java.util.Map;

/**
 * Created by huangsheng.hs on 2015/3/26.
 */
public interface RaceTestService {
    public Map<String,Object> getMap();
    public String getString();
    public RaceDO getDO();
    public boolean longTimeMethod();
    public Integer throwException() throws RaceException;
}
