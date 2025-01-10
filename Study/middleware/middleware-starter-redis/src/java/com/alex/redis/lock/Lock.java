package com.alex.redis.lock;

/**
 * @author liwenhao
 * @date 2023/5/17 16:29
 */
public @interface Lock {

    /**
     * 锁
     */
    String value();

    /**
     * 超时时间
     */
    long millise() default  -1;
}
