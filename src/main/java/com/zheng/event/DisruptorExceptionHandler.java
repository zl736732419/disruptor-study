package com.zheng.event;

import com.lmax.disruptor.ExceptionHandler;
import com.zheng.Performer;
import com.zheng.utils.ThrowableParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * Created by zhenglian on 2016/11/3.
 */
public class DisruptorExceptionHandler implements ExceptionHandler<ValueEvent> {
    private Logger logger = LoggerFactory.getLogger(DisruptorExceptionHandler.class);
    private Performer performer = null;

    public DisruptorExceptionHandler(Performer performer) {
        this.performer = performer;
    }

    public void handleEventException(Throwable throwable, long l, ValueEvent valueEvent) {
        performer.getHasError().compareAndSet(false, true);
        String message = MessageFormat.format("处理{0}数据出错", valueEvent.getObject().toString());
        logger.debug("错误：" + ThrowableParser.toString(throwable));
        logger.debug(message);

        valueEvent.setHasError(true);
        valueEvent.setMessage(message);
    }

    public void handleOnStartException(Throwable throwable) {
        logger.debug("启动disruptor出错");
        logger.debug(ThrowableParser.toString(throwable));
    }

    public void handleOnShutdownException(Throwable throwable) {
        logger.debug("关闭disruptor出错");
        logger.debug(ThrowableParser.toString(throwable));
    }
}
