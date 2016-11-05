package com.zheng.event;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * 处理handleEvent/handleEventWithWorkerPool的处理器
 * 这里将两者的业务逻辑都统一在一个对象中
 * 他们之间的业务逻辑是一样的，只是disruptor执行的方式不一样而已
 *
 * Created by zhenglian on 2016/11/5.
 */
public class DisruptorEventHandler implements EventHandler<ValueEvent>, WorkHandler<ValueEvent> {
    private Processor processor = null;

    public DisruptorEventHandler(Processor processor) {
        this.processor = processor;
    }

    public void onEvent(ValueEvent event, long l, boolean b) throws Exception {
        onEvent(event);
    }

    public void onEvent(ValueEvent event) throws Exception {
        if(ValueEvent.class.equals(processor.getObjClazz())) { //这里表示是计数器任务，直接运行
            processor.process(event);
        }else {
            //如果报错了就不在往下执行
            if(event.isHasError()) {
                return;
            }

            processor.process(event.getObject()); //将事件中传递的数据对象往下传递执行
        }
    }
}
