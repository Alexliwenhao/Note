package cn.ghostcloud.cloud.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liwenhao
 * @since 2022/12/22 上午10:42
 */
@ConfigurationProperties("redis")
public class XRedisProperties {


    /**
     * 注册中心地址
     */
    private String host;
    /**
     * 注册中心端口
     */
    private int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
