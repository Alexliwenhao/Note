package cn.ghostcloud.cloud.starter.rocketmq;

import lombok.*;

/**
 * @author zyp
 * @since 2023-01-04 17:11
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MqMessage {
    private String bizType;
    private String bizId;
    private String payload;
}
