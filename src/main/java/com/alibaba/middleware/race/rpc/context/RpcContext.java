package com.alibaba.middleware.race.rpc.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangsheng.hs on 2015/4/8.
 */
public class RpcContext {
    //TODO how can I get props as a provider? tip:ThreadLocal
    public static Map<String,Object> props = new HashMap<String, Object>();

    public static void addProp(String key ,Object value){
        props.put(key,value);
    }

    public static Object getProp(String key){
        return props.get(key);
    }

    public static Map<String,Object> getProps(){
       return Collections.unmodifiableMap(props);
    }
}
