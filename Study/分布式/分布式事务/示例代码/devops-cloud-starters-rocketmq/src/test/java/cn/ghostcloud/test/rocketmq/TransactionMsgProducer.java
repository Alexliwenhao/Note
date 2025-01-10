package cn.ghostcloud.test.rocketmq;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.beans.Transient;
import java.nio.charset.StandardCharsets;
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

public class TransactionMsgProducer {
    public static void main(String[] args) throws Exception {
        TransactionMsgProducer producer = new TransactionMsgProducer();
        producer.run();
    }


    public void run() throws Exception {
        TransactionListener transactionListener = new TransactionListenerImpl();
        TransactionMQProducer producer = new TransactionMQProducer("producer-trx-group");
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 4, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1024));
        producer.setExecutorService(threadPoolExecutor);
        producer.setTransactionListener(transactionListener);
        producer.setNamesrvAddr("10.0.3.4:9876");
        producer.start();
        for (int i = 0; i < 10; i++) {
            Message message = new Message("test-trx-topic",
                    (System.currentTimeMillis() + "-" + i).getBytes(StandardCharsets.UTF_8));
            String uuid = UUID.randomUUID().toString();
            message.setTransactionId(uuid);
            message.putUserProperty("trx_id", uuid);
            // ====
            SendResult send = producer.sendMessageInTransaction(message, null);
            // ====
            System.out.println(send);
            Thread.sleep(5);
        }
        Thread.sleep(60000);
        producer.shutdown();
    }

    @Transient
    public void doBiz(Object arg) {

    }


    public static class TransactionListenerImpl implements TransactionListener {
        private AtomicLong transactionIndex = new AtomicLong(0);
        private Map<String, Integer> localTransactionMap = new ConcurrentHashMap<>();

        @Override
        public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
            long idx = transactionIndex.incrementAndGet();
            int status = (int) (idx % 3);
            localTransactionMap.put(msg.getTransactionId(), status);
            LocalTransactionState state = LocalTransactionState.values()[status];
            System.out.printf("事物 %s 状态 %s%n", msg.getTransactionId(), state);
            return state;
        }

        @Override
        public LocalTransactionState checkLocalTransaction(MessageExt msg) {
            Integer status = localTransactionMap.get(msg.getTransactionId());
            if (status == null) {
                System.out.printf("%s 本地无记录， 回滚\n", msg.getTransactionId());
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }
            LocalTransactionState state = LocalTransactionState.values()[status];
            if (state == LocalTransactionState.UNKNOW) {
                System.out.printf("%s 本地事物状态 UNKNOW, 提交消息\n", msg.getTransactionId());
                return LocalTransactionState.COMMIT_MESSAGE;
            }
            System.out.printf("%s 本地事物状态 %s\n", msg.getTransactionId(), state);
            return state;
        }
    }
}
