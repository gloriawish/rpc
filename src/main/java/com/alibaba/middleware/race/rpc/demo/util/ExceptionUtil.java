package com.alibaba.middleware.race.rpc.demo.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by huangsheng.hs on 2015/5/18.
 */
public class ExceptionUtil {
    /**
     * 取得异常的stacktrace字符串。
     *
     * @param throwable 异常
     *
     * @return stacktrace字符串
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter buffer = new StringWriter();
        PrintWriter out = new PrintWriter(buffer);

        throwable.printStackTrace(out);
        out.flush();

        return buffer.toString();
    }
}
