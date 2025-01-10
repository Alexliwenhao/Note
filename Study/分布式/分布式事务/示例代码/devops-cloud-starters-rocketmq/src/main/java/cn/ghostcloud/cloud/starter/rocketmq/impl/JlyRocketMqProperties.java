package cn.ghostcloud.cloud.starter.rocketmq.impl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zyp
 * @since 2023-01-04 16:02
 */
@Getter
@Setter
@ToString(callSuper = true)
@ConfigurationProperties(prefix = "ghostcloud.rocketmq")
public class JlyRocketMqProperties {
    private Boolean enable = false;
    /**
     * rocketmq的namesrv地址
     *
     * @author zhengyongpan
     * @since 2023-01-04 16:04
     */
    private String namesrvAddr;
    private int consumerPullBatchSize = 16;
    private int consumerClientCallbackExecutorThreads = 4;

    private String topic = "devops-normal";
    private String consumerSubExpression = "*";

    private String topicTrx = "devops-trx";
    private String consumerSubExpressionTrx = "*";

}
