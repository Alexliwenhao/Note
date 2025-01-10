package cn.ghostcloud.test.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.beans.Transient;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 事物消息
 *
 * @author zyp
 * @since 2023-01-04 11:40
 */

public class TransactionMsgConsumer {
    public static void main(String[] args) throws Exception {
        TransactionMsgConsumer consumer = new TransactionMsgConsumer();
        consumer.run();
    }

    public void run( ) throws Exception {
        DefaultLitePullConsumer consumer = new DefaultLitePullConsumer("consumer-test-group");
        consumer.subscribe("test-trx-topic", "*");
        consumer.setPullBatchSize(8);
        consumer.setNamesrvAddr("10.0.3.4:9876");
        consumer.start();
        while (true) {
            List<MessageExt> poll = consumer.poll();
            if (poll != null) {
                for (MessageExt messageExt : poll) {
                    System.out.println(messageExt);
                    System.out.println(new String(messageExt.getBody(), StandardCharsets.UTF_8));
                }
            }
            System.out.println("sleep");
        }
    }
}
