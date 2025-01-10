package com.alex.redis.exception;

/**
 * @author liwenhao
 * @date 2023/5/17 17:16
 */
public class LockException extends RuntimeException {

    public LockException(String msg) {
        super(msg);
    }
}
