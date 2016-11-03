package com.zheng.event;

import com.lmax.disruptor.EventFactory;

/**
 * 构建事件对象的工厂
 * Created by zhenglian on 2016/11/3.
 */
public class ValueEventaFactory implements EventFactory<ValueEvent> {
    public ValueEvent newInstance() {
        return new ValueEvent();
    }
}
