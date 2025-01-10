package cn.ghostcloud.cloud.starter.rocketmq.impl;

import cn.ghostcloud.cloud.starter.rocketmq.MqMessage;
import lombok.Getter;
import lombok.ToString;
import org.apache.rocketmq.client.producer.LocalTransactionState;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author zyp
 * @since 2023-01-05 09:44
 */
@Getter
@ToString
public class MqTransactionListenerInvoker {
    private Object bean;
    private Method method;

    public MqTransactionListenerInvoker(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
    }

    public LocalTransactionState invoke(String msgId, String tag, String key, MqMessage message) throws Throwable {
        try {
            return (LocalTransactionState) method.invoke(bean, msgId, tag, key, message);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
