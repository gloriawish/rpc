package com.alibaba.middleware.race.rpc.demo.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by huangsheng.hs on 2015/5/19.
 */
public class ConsumerTest {
    protected static OutputStream getFunctionalOutputStream() throws FileNotFoundException {
        File file = new File("function.log");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        return fileOutputStream;
    }

    protected static OutputStream getPerformanceOutputStream() throws FileNotFoundException {
        File file = new File("performance.log");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        return fileOutputStream;
    }
}
