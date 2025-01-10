package cn.ghostcloud.cloud.starter.rocketmq.exception;

/**
 * @author zyp
 * @since 2023-01-04 16:54
 */
public class MqException extends RuntimeException {
    public MqException() {
        super();
    }

    public MqException(String message) {
        super(message);
    }

    public MqException(String message, Throwable cause) {
        super(message, cause);
    }

    public MqException(Throwable cause) {
        super(cause);
    }

    protected MqException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
