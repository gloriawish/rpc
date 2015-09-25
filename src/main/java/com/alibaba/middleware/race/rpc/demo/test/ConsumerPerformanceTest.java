package com.alibaba.middleware.race.rpc.demo.test;

import com.alibaba.middleware.race.rpc.demo.builder.ConsumerBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by huangsheng.hs on 2015/5/19.
 */
public class ConsumerPerformanceTest extends ConsumerTest{
    private static AtomicLong callAmount = new AtomicLong(0L);
    private static OutputStream outputStream;
    private static Method performanceTestMethod;
    private static ConsumerBuilder consumerBuilder;
    private static CountDownLatch countDownLatch;
    public static void main(String[] args) throws IOException {
        try {
            consumerBuilder = new ConsumerBuilder();
            outputStream = getPerformanceOutputStream();
            performanceTestMethod = consumerBuilder.getClass().getDeclaredMethod("pressureTest",null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int coreCount = Runtime.getRuntime().availableProcessors() * 2;
        countDownLatch = new CountDownLatch(coreCount);
        long startTime = System.currentTimeMillis();

        final ExecutorService executor = Executors.newFixedThreadPool(coreCount);
        for(int i = 0 ; i < coreCount ; i++){
            executor.execute(new Runnable() {
                public void run() {
                    while(callAmount.get() < 100000){
                        try {
                            if((Boolean)performanceTestMethod.invoke(consumerBuilder,null))
                                callAmount.incrementAndGet();
                            else
                                continue;
                        } catch (Exception e) {
                            continue;
                        }
                    }
                    countDownLatch.countDown();
                }
            });
        }
        try {
            countDownLatch.await(300, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            //DO Nothing
        }
        if(callAmount.intValue() < 100000)
            outputStream.write("Doesn't finish all invoking.".getBytes());
        else {
            long endTime = System.currentTimeMillis();
            Float tps = (float) callAmount.get() / (float) (endTime - startTime) * 1000F;
            StringBuilder sb = new StringBuilder();
            sb.append("tps:").append(tps);
            outputStream.write(sb.toString().getBytes());
            outputStream.close();
        }
        System.exit(1);
    }
}
