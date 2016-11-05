package com.zheng.event;

/**
 * Created by zhenglian on 2016/11/5.
 */
public interface Processor<T> {
    Class<T> getObjClazz();
    void process(T data);
}
