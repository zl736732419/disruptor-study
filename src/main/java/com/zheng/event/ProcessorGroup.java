package com.zheng.event;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * disruptor通过handleEvents执行的处理器分组
 * Created by zhenglian on 2016/11/5.
 */
public class ProcessorGroup {

    private ImmutableList<Processor> processors;


    public ImmutableList<Processor> getProcessors() {
        return processors;
    }

    public ProcessorGroup setProcessors(ImmutableList<Processor> processors) {
        Preconditions.checkNotNull(processors, "数据处理不能空");
        this.processors = ImmutableList.copyOf(processors);
        return this;
    }

    public ProcessorGroup setProcessors(Processor... processors) {
        Preconditions.checkNotNull(processors, "数据处理不能空");
        this.processors = ImmutableList.copyOf(processors);
        return this;
    }
}