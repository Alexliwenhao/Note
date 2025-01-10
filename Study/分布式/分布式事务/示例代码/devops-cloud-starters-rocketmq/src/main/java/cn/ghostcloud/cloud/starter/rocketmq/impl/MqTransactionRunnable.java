package cn.ghostcloud.cloud.starter.rocketmq.impl;

/**
 * @author zyp
 * @since 2023-01-04 16:32
 */
@FunctionalInterface
public interface MqTransactionRunnable extends MqTransactionCallable<Object> {
    public void run() throws Throwable;

    @Override
    default Object execute() throws Throwable {
        run();
        return null;
    }
}
