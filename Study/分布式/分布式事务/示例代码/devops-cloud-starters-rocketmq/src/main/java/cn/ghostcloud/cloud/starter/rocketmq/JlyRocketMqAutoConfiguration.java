package cn.ghostcloud.cloud.starter.rocketmq;

import cn.ghostcloud.cloud.base.threadpool.BeExecutorService;
import cn.ghostcloud.cloud.base.threadpool.NamedThreadFactory;
import cn.ghostcloud.cloud.base.threadpool.ThreadPoolUtils;
import cn.ghostcloud.cloud.starter.rocketmq.impl.JlyRocketMqProperties;
import cn.ghostcloud.cloud.starter.rocketmq.impl.MqMessageListener;
import cn.ghostcloud.cloud.starter.rocketmq.impl.MqTransactionListenerImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zyp
 * @since 2023-01-04 16:02
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(JlyRocketMqProperties.class)
@ConditionalOnProperty(value = "ghostcloud.rocketmq.enable", havingValue = "true", matchIfMissing = false)
public class JlyRocketMqAutoConfiguration {
    private static final String LOCALHOST;

    static {
        try {
            LOCALHOST = InetAddress.getLocalHost()
                    .getHostAddress();
            log.info("本机ip: {}", LOCALHOST);
        } catch (UnknownHostException e) {
            throw new RuntimeException("获取本机ip失败", e);
        }
    }

    private BeExecutorService mqExecutor = ThreadPoolUtils.newThreadPool(4, 8,
            60, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1024), new NamedThreadFactory("mq"),
            new ThreadPoolExecutor.CallerRunsPolicy());
    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.profiles.active}")
    private String profile;

    @Bean
    public MqService mqService() {
        return new MqService();
    }


    @Bean
    public TransactionListener mqTransactionListener() {
        return new MqTransactionListenerImpl();
    }

    @Bean
    public MqMessageListener messageListener() {
        return new MqMessageListener();
    }

    @Bean(destroyMethod = "shutdown")
    public MQProducer mqProducer(JlyRocketMqProperties jlyRocketMqProperties) throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("devops-producer-" + appName);
        producer.setNamesrvAddr(jlyRocketMqProperties.getNamesrvAddr());
        producer.setDefaultTopicQueueNums(1);
        withNamespace(producer);
        producer.start();
        producer.setCallbackExecutor(mqExecutor);
        producer.setAsyncSenderExecutor(mqExecutor);
        return producer;
    }


    @Bean(destroyMethod = "shutdown")
    public MQPushConsumer mqPushConsumer(JlyRocketMqProperties jlyRocketMqProperties,
                                         MqMessageListener messageListener) throws MQClientException {
        DefaultMQPushConsumer consumer =
                new DefaultMQPushConsumer("devops-consumer-" + appName);
        consumer.setNamesrvAddr(jlyRocketMqProperties.getNamesrvAddr());
        withNamespace(consumer);
        consumer.setPullBatchSize(1);
        consumer.setClientCallbackExecutorThreads(1);
        consumer.subscribe(jlyRocketMqProperties.getTopic(),
                jlyRocketMqProperties.getConsumerSubExpression());
        consumer.registerMessageListener(messageListener);
        consumer.start();
        return consumer;
    }

    @Bean(destroyMethod = "shutdown")
    public MQProducer mqProducerTrx(JlyRocketMqProperties jlyRocketMqProperties,
                                    @Qualifier("mqTransactionListener") TransactionListener mqTransactionListener)
            throws Exception {
        TransactionMQProducer producer = new TransactionMQProducer("devops-producer-trx-" + appName);
        producer.setTransactionListener(mqTransactionListener);
        producer.setNamesrvAddr(jlyRocketMqProperties.getNamesrvAddr());
        withNamespace(producer);
        producer.setDefaultTopicQueueNums(1);
        producer.setExecutorService(mqExecutor);
        producer.start();
        return producer;
    }

    @Bean(destroyMethod = "shutdown")
    public MQPushConsumer mqPushConsumerTrx(JlyRocketMqProperties jlyRocketMqProperties,
                                            MqMessageListener messageListener) throws MQClientException {
        DefaultMQPushConsumer consumer =
                new DefaultMQPushConsumer("devops-consumer-trx-" + appName);
        consumer.setNamesrvAddr(jlyRocketMqProperties.getNamesrvAddr());
        withNamespace(consumer);
        consumer.setPullBatchSize(1);
        consumer.setClientCallbackExecutorThreads(1);
        consumer.subscribe(jlyRocketMqProperties.getTopicTrx(),
                jlyRocketMqProperties.getConsumerSubExpressionTrx());
        consumer.registerMessageListener(messageListener);
        consumer.start();
        return consumer;
    }

    private void withNamespace(DefaultMQPushConsumer consumer) {
        if ("local".equals(profile)) {
            log.info("设置mq的namespace. consumer={}", consumer);
            consumer.setNamespace(getNamespace());
        }
    }

    private void withNamespace(TransactionMQProducer producer) {
        if ("local".equals(profile)) {
            log.info("设置mq的namespace. producer={}", producer);
            producer.setNamespace(getNamespace());
        }
    }

    private void withNamespace(DefaultMQProducer producer) {
        if ("local".equals(profile)) {
            log.info("设置mq的namespace. producer={}", producer);
            producer.setNamespace(getNamespace());
        }
    }

    private String getNamespace() {
        return LOCALHOST.replaceAll("[.:]", "_");
    }
}
