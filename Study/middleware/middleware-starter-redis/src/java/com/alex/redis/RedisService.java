package com.alex.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * @author liwenhao
 * @date 2023/5/17 17:29
 */
public class RedisService {

    private RedissonClient redissonClient;

    public RedisService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }


    /**
     * 获取锁
     * 执行任务
     * 释放锁
     */
    public <T> T doInLock(String redisKey, LockTask<T> lockTask) throws Throwable {
        RLock lock = redissonClient.getLock(redisKey);
        try {
            lock.lock();
            return lockTask.call();
        } finally {
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
