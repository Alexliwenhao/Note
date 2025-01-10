package com.alex.redis;

/**
 * @author liwenhao
 * @date 2023/5/17 17:28
 */
@FunctionalInterface
public interface LockTask<T> {
    T call() throws Throwable;
}
