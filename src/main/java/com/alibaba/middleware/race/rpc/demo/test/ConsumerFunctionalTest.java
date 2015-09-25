package com.alibaba.middleware.race.rpc.demo.test;

import com.alibaba.middleware.race.rpc.demo.builder.ConsumerBuilder;
import com.alibaba.middleware.race.rpc.demo.util.ExceptionUtil;

import java.io.*;
import java.lang.reflect.Method;

/**
 * Created by huangsheng.hs on 2015/5/18.
 */
public class ConsumerFunctionalTest extends ConsumerTest{
    public static void main(String[] args) {
        try {
            OutputStream outputStream = getFunctionalOutputStream();
            ConsumerBuilder consumerBuilder = new ConsumerBuilder();
            Method[] methods = consumerBuilder.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().startsWith("test")) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(method.getName()).append(":");
                    try {
                        method.invoke(consumerBuilder, null);
                        sb.append("pass").append("\r\n");
                    } catch (Exception e) {
                        sb.append(ExceptionUtil.getStackTrace(e)).append("\r\n");
                    }
                    outputStream.write(sb.toString().getBytes());
                }
            }
            outputStream.close();
        }catch (Exception e){
           //Do Nothing
        }

        // some thread may be not daemon
        System.exit(1);
    }
}
