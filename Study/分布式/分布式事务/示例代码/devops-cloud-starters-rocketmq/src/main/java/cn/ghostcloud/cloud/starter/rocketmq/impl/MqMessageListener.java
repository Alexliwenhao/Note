package cn.ghostcloud.cloud.starter.rocketmq.impl;

import cn.ghostcloud.cloud.starter.rocketmq.MqMessage;
import cn.ghostcloud.cloud.starter.rocketmq.annotation.MqConsumerEndpoint;
import cn.ghostcloud.cloud.starter.rocketmq.api.MqConsumer;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
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
 * @since 2023-01-05 10:26
 */
@Slf4j
public class MqMessageListener implements MessageListenerOrderly {
    @Autowired(required = false)
    private List<MqConsumer> mqConsumers;
    private Map<String, MqConsumerInvoker> mqConsumerInvokerMap = new HashMap<>();
    private final Class[] parameterTypes = new Class[]{String.class, String.class, String.class, MqMessage.class};
    @PostConstruct
    private void init() {
        if (mqConsumers != null) {
            initMqConsumer();
        }
    }

    private void initMqConsumer() {
        List<String> errors = new ArrayList<>();
        for (MqConsumer mqConsumer : mqConsumers) {
            Class<?> clas = ClassUtils.getUserClass(mqConsumer);
            ReflectionUtils.doWithMethods(clas, m -> {
                if (m.isSynthetic() || Modifier.isStatic(m.getModifiers())) {
                    return;
                }
                MqConsumerEndpoint annotation = AnnotationUtils.findAnnotation(m, MqConsumerEndpoint.class);
                if (annotation == null) {
                    return;
                }
                validateMethodDefinition(clas, m, errors);
                m.setAccessible(true);
                MqConsumerInvoker invoker = new MqConsumerInvoker(mqConsumer, m);
                for (String bizType : annotation.value()) {
                    MqConsumerInvoker replaced = mqConsumerInvokerMap.put(bizType, invoker);
                    if (replaced != null) {
                        throw new RuntimeException(String.format("MqConsumerEndpoint重复注册, bizType=%s, method1=%s," +
                                " method2=%s", bizType, replaced.getMethod(), invoker.getMethod()));
                    }
                    log.info("注册MqConsumerEndpoint, bean={}, method={}", clas.getSimpleName(), m.getName());
                }
            });
        }
        if (errors.size() > 0) {
            for (String error : errors) {
                log.error(error);
            }
            throw new RuntimeException("注册MqConsumerEndpoint, 失败，请参考日志进行修复");
        }
    }

    private void validateMethodDefinition(Class beanClass, Method method, List<String> errors) {
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != this.parameterTypes.length) {
            errors.add(
                    String.format("注册MqConsumerEndpoint, 失败. 参数数量错误，bean=%s, method=%s, cnt=%s, " +
                                    "requireCnt=%s",
                            beanClass.getSimpleName(), methodName, parameterTypes.length, this.parameterTypes.length));
            return;
        }
        int len = this.parameterTypes.length;
        for (int i = 0; i < len; i++) {
            if (parameterTypes[i] == this.parameterTypes[i]) {
                continue;
            }
            errors.add(
                    String.format("注册MqConsumerEndpoint, 失败. 参数类型错误，" +
                                    "bean=%s, method=%s, 请将第 %s 个参数类型修改为 %s, 当前类型 %s",
                            beanClass.getSimpleName(), methodName, i + 1, this.parameterTypes[i].getSimpleName(),
                            parameterTypes[i].getSimpleName()));
        }

        Class<?> returnType = method.getReturnType();
        if (returnType != void.class) {
            errors.add(
                    String.format("注册MqConsumerEndpoint, 失败. 返回值类型错误，" +
                                    "bean=%s, method=%s, 请将返回值类型修改为 %s, 当前类型 %s",
                            beanClass.getSimpleName(), methodName, void.class.getSimpleName(),
                            returnType.getSimpleName()));
        }
    }

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        for (MessageExt msg : msgs) {
            MqMessage message = JSON.parseObject(msg.getBody(), MqMessage.class);
            MqConsumerInvoker mqConsumerInvoker = mqConsumerInvokerMap.get(message.getBizType());
            if (mqConsumerInvoker == null) {
                log.info("不支持的消息类型, bizType={}", message.getBizType());
                return ConsumeOrderlyStatus.SUCCESS;
            }
            String tags = msg.getTags();
            String keys = msg.getKeys();

            try {
                mqConsumerInvoker.invoke(msg.getMsgId(), tags, keys, message);
            } catch (Throwable e) {
                log.error("消费消息失败, message=" + JSON.toJSONString(message), e);
                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }
        }
        return ConsumeOrderlyStatus.SUCCESS;
    }
}
