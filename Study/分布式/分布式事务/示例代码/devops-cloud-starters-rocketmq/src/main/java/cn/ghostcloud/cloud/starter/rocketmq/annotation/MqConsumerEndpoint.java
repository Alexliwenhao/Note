package cn.ghostcloud.cloud.starter.rocketmq.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解在方法上， 监听mq消息
 * @author zyp
 * @since 2023-01-05 09:31
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MqConsumerEndpoint {
    /**
     * 当前方法支持的bizType数组
     */
    String[] value();
}
