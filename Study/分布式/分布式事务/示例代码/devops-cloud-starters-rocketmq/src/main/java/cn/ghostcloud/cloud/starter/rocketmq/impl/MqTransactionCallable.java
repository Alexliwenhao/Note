package cn.ghostcloud.cloud.starter.rocketmq.impl;

/**
 * @author zyp
 * @since 2023-01-04 16:32
 */
@FunctionalInterface
public interface MqTransactionCallable<T> {
    public T execute() throws Throwable;


}
