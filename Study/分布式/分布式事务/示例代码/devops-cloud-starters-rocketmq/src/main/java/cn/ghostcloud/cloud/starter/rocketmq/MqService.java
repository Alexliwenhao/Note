package cn.ghostcloud.cloud.starter.rocketmq;

import cn.ghostcloud.cloud.starter.rocketmq.exception.MqException;
import cn.ghostcloud.cloud.starter.rocketmq.impl.JlyRocketMqProperties;
import cn.ghostcloud.cloud.starter.rocketmq.impl.MqTransactionCallable;
import cn.ghostcloud.cloud.starter.rocketmq.impl.MqTransactionResult;
import cn.ghostcloud.cloud.starter.rocketmq.impl.MqTransactionRunnable;
import com.alibaba.fastjson.JSON;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author zyp
 * @since 2023-01-04 16:07
 */
@Slf4j
public class MqService {

    @Resource
    private JlyRocketMqProperties jlyRocketMqProperties;
    @Resource
    private MQProducer mqProducer;
    @Resource
    private MQProducer mqProducerTrx;
    @Value("${spring.application.name}")
    private String appName;

    public String getProducerGroup() {
        return "devops-producer-" + appName;
    }

    public String getConsumerGroup() {
        return "devops-consumer-" + appName;
    }

    /**
     * 发送普通消息
     *
     * @param tag     tag 可为null
     * @param key     key 可为null
     * @param bizType bizType 业务类型，必传
     * @param bizId   bizId 可为null
     * @param payload payload 消息体，可为null
     * @return SendResult 发送结果
     * @author zhengyongpan
     * @since 2023-01-05 10:46
     */
    public SendResult sendMsg(@Nullable String tag, @Nullable String key,
                              @NonNull String bizType, @Nullable String bizId,
                              @Nullable String payload) {
        try {
            Message msg = makeMessage(tag, key, bizType, bizId, payload);
            SendResult sendResult = mqProducer.send(msg);
            log.info("mq消息发送成功, msg={}, res={}", JSON.toJSONString(msg), JSON.toJSONString(sendResult));
            return sendResult;
        } catch (Throwable x) {
            throw new MqException("发送mq消息失败", x);
        }
    }

    /**
     * 发送普通消息
     *
     * @param bizType bizType 业务类型，必传
     * @param bizId   bizId 可为null
     * @param payload payload 消息体，可为null
     * @return SendResult 发送结果
     * @author zhengyongpan
     * @since 2023-01-05 10:46
     */
    public SendResult sendMsg(@NonNull String bizType, @Nullable String bizId,
                              @Nullable String payload) {
        return sendMsg(null, null, bizType, bizId, payload);
    }


    public <T> T sendTrxMsg(@NonNull String bizType, @Nullable String bizId,
                            @Nullable String payload,
                            @NonNull MqTransactionCallable<T> task) throws Throwable {
        return sendTrxMsg(null, null, bizType, bizId, payload, task);
    }

    public void sendTrxMsg(@NonNull String bizType, @Nullable String bizId,
                           @Nullable String payload,
                           @NonNull MqTransactionRunnable task) throws Throwable {
        sendTrxMsg(null, null, bizType, bizId, payload, task);
    }

    /**
     * 发送事物消息
     *
     * @param tag     tag   可为null
     * @param key     key   可为null
     * @param bizType bizType   业务类型，必传
     * @param bizId   bizId 可为null
     * @param payload payload   可为null
     * @param task    task  本地事物
     * @return T 本地事物返回值
     * @author zhengyongpan
     * @since 2023-01-05 10:52
     */
    public <T> T sendTrxMsg(@Nullable String tag, @Nullable String key,
                            @NonNull String bizType, @Nullable String bizId,
                            @Nullable String payload,
                            @NonNull MqTransactionCallable<T> task) throws Throwable {
        Message msg = makeTrxMessage(tag, key, bizType, bizId, payload);
        MqTransactionResult<T> result = new MqTransactionResult<>(task);
        try {
            TransactionSendResult transactionSendResult = mqProducerTrx.sendMessageInTransaction(msg, result);
            log.info("事物消息执行结果: trxId={}, trxState={}, msg={}", transactionSendResult.getMsgId(),
                    transactionSendResult.getLocalTransactionState(), JSON.toJSONString(msg));
            if (result.getException() != null) {
                throw result.getException();
            }
            return result.getRes();
        } catch (Throwable x) {
            log.info("事物消息执行失败: ex={}", x.getClass().getSimpleName());
            throw x;
        }
    }


    /**
     * 发送事物消息
     *
     * @param tag     tag   可为null
     * @param key     key   可为null
     * @param bizType bizType   业务类型，必传
     * @param bizId   bizId 可为null
     * @param payload payload   可为null
     * @param task    task  本地事物
     * @return T 本地事物返回值
     * @author zhengyongpan
     * @since 2023-01-05 10:52
     */
    public void sendTrxMsg(@Nullable String tag, @Nullable String key,
                           @NonNull String bizType, @Nullable String bizId,
                           @Nullable String payload,
                           @NonNull MqTransactionRunnable task) throws Throwable {
        Message msg = makeTrxMessage(tag, key, bizType, bizId, payload);
        MqTransactionResult<Object> result = new MqTransactionResult<>(task);
        try {
            TransactionSendResult transactionSendResult = mqProducerTrx.sendMessageInTransaction(msg, result);
            log.info("事物消息执行结果: trxId={}, trxState={}, msg={}", transactionSendResult.getMsgId(),
                    transactionSendResult.getLocalTransactionState(), JSON.toJSONString(msg));
            if (result.getException() != null) {
                throw result.getException();
            }
        } catch (Throwable x) {
            log.info("事物消息执行失败: ex={}", x.getClass().getSimpleName());
            throw x;
        }
    }

    private Message makeMessage(String topic, String tag, String key, String bizType, String bizId,
                                String payload) {
        MqMessage message = new MqMessage();
        message.setBizType(bizType);
        message.setBizId(bizId);
        message.setPayload(payload);
        Message msg = new Message(topic, tag, JSON.toJSONString(message).getBytes(StandardCharsets.UTF_8));
        if (key != null) {
            msg.setKeys(key);
        }
        return msg;
    }

    private Message makeMessage(String tag, String key, String bizType, String bizId, String payload) {
        return makeMessage(jlyRocketMqProperties.getTopic(), tag, key, bizType, bizId, payload);
    }

    private Message makeTrxMessage(String tag, String key, String bizType, String bizId, String payload) {
        return makeMessage(jlyRocketMqProperties.getTopicTrx(), tag, key, bizType, bizId, payload);
    }



}
