package cn.ghostcloud.cloud.starter.rocketmq.impl;

import cn.ghostcloud.cloud.base.http.exception.ExPrinter;
import cn.ghostcloud.cloud.starter.rocketmq.MqMessage;
import cn.ghostcloud.cloud.starter.rocketmq.annotation.MqTransactionListenerEndpoint;
import cn.ghostcloud.cloud.starter.rocketmq.api.MqTransactionListener;
import cn.ghostcloud.cloud.starter.rocketmq.exception.MqException;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zyp
 * @since 2023-01-04 16:28
 */
@Slf4j
public class MqTransactionListenerImpl implements TransactionListener {
    @Autowired(required = false)
    private List<MqTransactionListener> mqTransactionListeners;

    private Map<String, MqTransactionListenerInvoker> mqTrxListenerInvokerMap = new HashMap<>();
    private final Class[] parameterTypes = new Class[]{String.class, String.class, String.class, MqMessage.class};

    @PostConstruct
    private void init() {
        if (mqTransactionListeners != null) {
            initMqTransactionListener();
        }
    }


    private void initMqTransactionListener() {
        List<String> errors = new ArrayList<>();
        for (MqTransactionListener mqTransactionListener : mqTransactionListeners) {
            Class<?> clas = ClassUtils.getUserClass(mqTransactionListener);
            ReflectionUtils.doWithMethods(clas, m -> {
                if (m.isSynthetic() || Modifier.isStatic(m.getModifiers())) {
                    return;
                }
                MqTransactionListenerEndpoint annotation =
                        AnnotationUtils.findAnnotation(m, MqTransactionListenerEndpoint.class);
                if (annotation == null) {
                    return;
                }
                validateMethodDefinition(clas, m, errors);
                m.setAccessible(true);
                MqTransactionListenerInvoker invoker = new MqTransactionListenerInvoker(mqTransactionListener, m);
                for (String bizType : annotation.value()) {
                    MqTransactionListenerInvoker replaced = mqTrxListenerInvokerMap.put(bizType, invoker);
                    if (replaced != null) {
                        throw new RuntimeException(
                                String.format("MqTransactionListenerEndpoint重复注册, bizType=%s, method1=%s," +
                                        " method2=%s", bizType, replaced.getMethod(), invoker.getMethod()));
                    }
                    log.info("注册MqTransactionListenerEndpoint, bean={}, method={}",
                            clas.getSimpleName(), m.getName());
                }
            });
        }
        if (errors.size() > 0) {
            for (String error : errors) {
                log.error(error);
            }
            throw new RuntimeException("注册MqTransactionListenerEndpoint, 失败，请参考日志进行修复");
        }
    }

    private void validateMethodDefinition(Class beanClass, Method method, List<String> errors) {
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != this.parameterTypes.length) {
            errors.add(String.format("注册MqTransactionListenerEndpoint, 失败. 参数数量错误，bean=%s, method=%s, cnt=%s, " +
                            "requireCnt=%s",
                    beanClass.getSimpleName(), methodName, parameterTypes.length, this.parameterTypes.length));
            return;
        }
        int len = this.parameterTypes.length;
        for (int i = 0; i < len; i++) {
            if (parameterTypes[i] == this.parameterTypes[i]) {
                continue;
            }
            errors.add(String.format("注册MqTransactionListenerEndpoint, 失败. 参数类型错误，" +
                            "bean=%s, method=%s, 请将第 %s 个参数类型修改为 %s, 当前类型 %s",
                    beanClass.getSimpleName(), methodName, i + 1, this.parameterTypes[i].getSimpleName(),
                    parameterTypes[i].getSimpleName()));
        }
        Class<?> returnType = method.getReturnType();
        if (returnType != LocalTransactionState.class) {
            errors.add(
                    String.format("注册MqConsumerEndpoint, 失败. 返回值类型错误，" +
                                    "bean=%s, method=%s, 请将返回值类型修改为 %s, 当前类型 %s",
                            beanClass.getSimpleName(), methodName, LocalTransactionState.class.getSimpleName(),
                            returnType.getSimpleName()));
        }
    }

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        MqTransactionResult wrapper = (MqTransactionResult) arg;
        Object res = null;
        try {
            res = wrapper.getTask()
                    .execute();
        } catch (Throwable e) {
            wrapper.setException(e);
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new MqException("发送mq事物消息失败", e);
        }
        wrapper.setRes(res);
        return LocalTransactionState.COMMIT_MESSAGE;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        MqMessage message = JSON.parseObject(msg.getBody(), MqMessage.class);
        String bizId = message.getBizId();
        String bizType = message.getBizType();
        String trxId = msg.getMsgId();
        LocalTransactionState state;
        MqTransactionListenerInvoker mqTransactionListenerInvoker = mqTrxListenerInvokerMap.get(bizType);
        if (mqTransactionListenerInvoker == null) {
            log.error("找不到 MqTransactionListenerEndpoint, bizType={}", bizType);
            state = LocalTransactionState.UNKNOW;
        } else {
            try {
                state = mqTransactionListenerInvoker.invoke(trxId, msg.getTags(), msg.getKeys(), message);
            } catch (Throwable e) {
                ExPrinter.printStackTrace(e);
                state = LocalTransactionState.UNKNOW;
            }
        }
        log.info("事物消息反查结果, trxId={}, bizType={}, bizId={}, state={}",
                trxId, bizType, bizId, state);
        return state;
    }
}
