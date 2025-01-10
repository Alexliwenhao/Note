package cn.ghostcloud.test.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;

/**
 * @author zyp
 * @since 2023-01-04 11:40
 */

public class NormalMsgProducer {
    public static void main(String[] args) throws Exception {
        NormalMsgProducer producer = new NormalMsgProducer();
        producer.run();
    }


    public void run() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("test-group");
        producer.setNamesrvAddr("192.168.204.128:9876");
        producer.start();
        for (int i = 0; i < 10; i++) {
            Message message = new Message("test",
                    (System.currentTimeMillis() + "-" + i).getBytes(StandardCharsets.UTF_8));
            SendResult send = producer.send(message);
            System.out.println(send);
            Thread.sleep(5);
        }
        producer.shutdown();
    }
}
