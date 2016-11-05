package com.zheng.event;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 需要执行的任务接口
 * 将其封装成迭代器进行访问
 * Created by zhenglian on 2016/11/5.
 */
public abstract class EventTask<T> {
    protected int totalTaskNum; //总任务数
    protected int curIndex = 0; //当前执行任务的索引
    protected T cur = null; //当前任务数据对象
    protected List<T> data = Lists.newArrayList(); //任务数据列表

    public EventTask(List<T> data) {
        this.data = data;
        this.totalTaskNum = data.size();
    }

    /**
     * 是否存在下一个执行任务
     * @return
     */
    public boolean hasNext() {
        try {
            cur = data.get(curIndex++);
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    /**
     * 获取下一个任务数据对象
     * @return
     */
    public T next() {
        return cur;
    }

    /**
     * 获取任务总量
     * @return
     */
    public int getTotalTaskNum() {
        return totalTaskNum;
    }

    /**
     * 每个任务执行中需要传递的实际数据对象
     * 该对象由当前任务中的cur和不同任务需要的其他数据封装而成
     * @return
     */
    public abstract Object get();
}
