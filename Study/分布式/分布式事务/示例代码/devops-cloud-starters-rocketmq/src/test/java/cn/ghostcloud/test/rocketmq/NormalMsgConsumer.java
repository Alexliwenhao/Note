package cn.ghostcloud.test.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author zyp
 * @since 2023-01-04 11:40
 */

public class NormalMsgConsumer {

    public static void main(String[] args) throws Exception {
        NormalMsgConsumer consumer = new NormalMsgConsumer();
        consumer.run();
    }


    public void run( ) throws Exception {
        DefaultLitePullConsumer consumer = new DefaultLitePullConsumer("consumer-test-group3");
        consumer.subscribe("test", "*");
        consumer.setPullBatchSize(8);
        consumer.setNamesrvAddr("192.168.204.128:9876");
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
