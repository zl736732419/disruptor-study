package com.zheng;

import com.google.common.base.Throwables;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.Util;
import com.zheng.event.DisruptorExceptionHandler;
import com.zheng.event.ThreadExecutor;
import com.zheng.event.ValueEvent;
import com.zheng.event.ValueEventaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 任务执行器
 * Created by zhenglian on 2016/11/3.
 */
public class Performer implements Runnable{
    private Logger logger = LoggerFactory.getLogger(Performer.class);
    private Disruptor<ValueEvent> disruptor = null;

    private AtomicBoolean hasError = new AtomicBoolean(false); //标识disruptor处理过程中是否产生错误信息
    private AtomicBoolean hasWarn = new AtomicBoolean(false);

    public AtomicBoolean getHasError() {
        return hasError;
    }

    public AtomicBoolean getHasWarn() {
        return hasWarn;
    }

    public void run() {
        process();
    }

    public void process() {
        long start = System.currentTimeMillis();
        logger.debug("开始执行任务...");
        try {
            disruptor = new Disruptor<ValueEvent>(new ValueEventaFactory(), Util.ceilingNextPowerOfTwo(1024),
                    ThreadExecutor.createThreadFactory(), ProducerType.SINGLE, new YieldingWaitStrategy());
            //设置disruptor异常处理器
            disruptor.setDefaultExceptionHandler(new DisruptorExceptionHandler(this));







        }catch(Exception e) {
            logger.debug(e.getMessage());
            Throwables.propagate(e);
        } finally {
            disruptor.shutdown();
        }
        long end = System.currentTimeMillis();
        logger.debug("任务执行完毕，共耗时{}s", (end - start) / 3600);

    }
}
