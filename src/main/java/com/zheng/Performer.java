package com.zheng;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.Util;
import com.zheng.cache.MemoryCache;
import com.zheng.event.*;
import com.zheng.utils.OnlyIdBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 任务执行器
 * Created by zhenglian on 2016/11/3.
 */
public class Performer implements Runnable{
    private Logger logger = LoggerFactory.getLogger(Performer.class);
    private Disruptor<ValueEvent> disruptor = null;
    private List<ProcessorGroup> processorGroups = Lists.newArrayList();
    private AtomicBoolean hasError = new AtomicBoolean(false); //标识disruptor处理过程中是否产生错误信息
    private AtomicBoolean hasWarn = new AtomicBoolean(false);
    private EventTask task = null;
    private String onlyId = OnlyIdBuilder.build();

    public EventTask getTask() {
        return task;
    }

    public Performer setTask(EventTask task) {
        this.task = task;
        return this;
    }

    /**
     * 通过handleEvents处理
     * @param processors
     * @return
     */
    public  Performer addProcessors(Processor... processors) {
        processorGroups.add(new ProcessorGroup().setProcessors(processors));
        return this;
    }

    /**
     * 通过handleEventsWithWorkerPool处理
     * @param processors
     * @return
     */
    public Performer addProcessorPool(Processor... processors) {
        processorGroups.add(new ProcessorGroupPool().setProcessors(processors));
        return this;
    }

    public AtomicBoolean getHasError() {
        return hasError;
    }

    public AtomicBoolean getHasWarn() {
        return hasWarn;
    }

    public Disruptor<ValueEvent> getDisruptor() {
        return disruptor;
    }

    public void setDisruptor(Disruptor<ValueEvent> disruptor) {
        this.disruptor = disruptor;
    }

    public List<ProcessorGroup> getProcessorGroups() {
        return processorGroups;
    }

    public void setHasError(AtomicBoolean hasError) {
        this.hasError = hasError;
    }

    public void setHasWarn(AtomicBoolean hasWarn) {
        this.hasWarn = hasWarn;
    }

    public String getOnlyId() {
        return onlyId;
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

            //设置处理器
            EventHandlerGroup<ValueEvent> handlerGroup = createEventHandlerGroup();
            //创建计数任务
            CountDownLatch latch = new CountDownLatch(task.getTotalTaskNum());
            createTaskCountHandler(handlerGroup, latch);
            //启动disruptor,准备接受任务
            disruptor.start();

            //开始执行任务
            doTask();

            //等待任务执行完成之后再执行后续操作
            latch.await();
        }catch(Exception e) {
            logger.debug(e.getMessage());
            Throwables.propagate(e);
        } finally {
            disruptor.shutdown();
        }
        long end = System.currentTimeMillis();
        logger.debug("任务执行完毕，共耗时{}s", (end - start) / 3600);
    }

    /**
     * 开始执行任务
     */
    private void doTask() {
        int index = 1;//当前任务索引
        while(task.hasNext()) {
            ValueEvent event = new ValueEvent();
            event.setMessage("");
            event.setObject(task.get());
            event.setIndex(index++);
            disruptor.publishEvent(new DisruptorEventTranslator(event));
        }
    }

    /**
     * 创建计数器任务，用于计数当前执行的任务数
     * @param handlerGroup
     */
    private void createTaskCountHandler(EventHandlerGroup<ValueEvent> handlerGroup, final CountDownLatch latch) {
        DisruptorEventHandler handler = new DisruptorEventHandler(new Processor<ValueEvent>() {
            public Class getObjClazz() {
                return ValueEvent.class; //计数器任务标识
            }

            public void process(ValueEvent event) {
                //这里-1是countDown在后面操作导致
                //这里开始更新进度条
                Progressor progressor = (Progressor) MemoryCache.getInstance().get(onlyId);
                if(progressor == null) {
                    return;
                }

                progressor.setComplated(progressor.getComplated() + 1);
                logger.info("处理完毕: " + progressor.getComplated());

                if(event.isHasError()) {
                    hasError.compareAndSet(false, true);
                    progressor.setHasError(true);
                    progressor.getErrorMessages().add(event.getMessage());
                }

                if(event.isHasWarn()) {
                    hasWarn.compareAndSet(false, true);
                    progressor.setHasWarn(true);
                    progressor.getWarnMessages().add(event.getMessage());
                }

                MemoryCache.getInstance().put(onlyId, new Progressor(progressor.getComplated(),
                        progressor.getTotal(), progressor.getText()));

                latch.countDown();
                logger.info("当前已经完成{},还有{}个任务...........", event.getIndex(), latch.getCount());
            }
        });

        if(handlerGroup != null) {
            handlerGroup.handleEventsWith(handler);
        }else {
            disruptor.handleEventsWith(handler);
        }
    }

    /**
     * 处理任务
     * @return 返回任务处理分组，主要是为了后续在任务当前任务组中添加计数器任务
     */
    private EventHandlerGroup<ValueEvent> createEventHandlerGroup() {
        EventHandlerGroup<ValueEvent> handlerGroup = null;
        for(ProcessorGroup group : processorGroups) {
            if(group instanceof ProcessorGroupPool) {
                handlerGroup = createWorkerPoolHandlerGroup(group.getProcessors(), handlerGroup);
            }else {
                handlerGroup = createHandleEventHandlerGroup(group.getProcessors(), handlerGroup);
            }
        }
        return handlerGroup;
    }

    private EventHandlerGroup<ValueEvent> createHandleEventHandlerGroup(ImmutableList<Processor> processors, EventHandlerGroup<ValueEvent> handlerGroup) {
        if(handlerGroup == null) {
            return disruptor.handleEventsWith(toArray(processors));
        }else {
            return handlerGroup.handleEventsWith(toArray(processors));
        }
    }

    private DisruptorEventHandler[] toArray(List<Processor> processors) {
        List<DisruptorEventHandler> handlers = Lists.newArrayList();
        for(Processor processor : processors) {
            handlers.add(new DisruptorEventHandler(processor));
        }

        return handlers.toArray(new DisruptorEventHandler[processors.size()]);
    }

    private EventHandlerGroup<ValueEvent> createWorkerPoolHandlerGroup(ImmutableList<Processor> processors, EventHandlerGroup<ValueEvent> handlerGroup) {
        if(handlerGroup == null) {
            return disruptor.handleEventsWithWorkerPool(toArray(processors));
        }else {
            return handlerGroup.handleEventsWithWorkerPool(toArray(processors));
        }
    }
}
