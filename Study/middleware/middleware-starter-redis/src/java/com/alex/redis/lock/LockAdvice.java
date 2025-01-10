package com.alex.redis.lock;

import com.alex.redis.exception.LockException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author liwenhao
 * @date 2023/5/17 14:43
 */
public class LockAdvice implements MethodInterceptor {

    private RedissonClient redissonClient;


    public LockAdvice(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        RLock lock = null;
        try {
            Method method = methodInvocation.getMethod();
            //1.找到注解方法拿到数据
            MergedAnnotation<Lock> lockMergedAnnotation = MergedAnnotations.from(method).get(Lock.class);
            String value = lockMergedAnnotation.getString("value");
            long millise = lockMergedAnnotation.getLong("millise");
            //2.加锁
            lock = redissonClient.getLock(value);
            doLock(lock, millise);
            //3.执行
            return methodInvocation.proceed();
        } finally {
            //4.释放锁
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private void doLock(RLock lock, long millise) {
        try {
            boolean success = lock.tryLock(millise, TimeUnit.MILLISECONDS);
            if (!success) {
                throw new LockException("加锁失败");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
