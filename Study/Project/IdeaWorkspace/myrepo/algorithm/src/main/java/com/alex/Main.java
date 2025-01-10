package com.alex;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author 杨幸
 * @date 2023/4/20 20:56
 * @copyright 成都精灵云科技有限公司
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 10, TimeUnit.DAYS, new ArrayBlockingQueue<>(4),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread();
                        t.setDaemon(true);
                        return t;
                    }
                });
        executor.execute(() -> {

        });
        new Thread(() -> {
            System.out.println(0);
            executor.execute(() -> {
                System.out.println(111);
                sleep(36000);
            });
            System.out.println(2);
        }).start();
        sleep(10000);
        executor.shutdown();
        System.out.println("线程池已关闭");
    }

    private static final void sleep(long t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
 