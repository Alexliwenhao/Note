package cn.ghostcloud.cloud.starter.rocketmq.impl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zyp
 * @since 2023-01-04 16:32
 */
@ToString(callSuper = true)
public class MqTransactionResult<T> {
    @Getter
    private MqTransactionCallable<T> task;
    @Getter
    @Setter
    private T res;
    @Getter
    private Throwable exception;
    public MqTransactionResult(MqTransactionCallable<T> task) {
        this.task = task;
    }

    void setException(Throwable exception) {
        this.exception = exception;
    }
}
