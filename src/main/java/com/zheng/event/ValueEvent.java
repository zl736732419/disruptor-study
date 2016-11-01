package com.zheng.event;

/**
 * disruptor处理事件对象
 * 该事件对象中携带多个线程中传递的数据对象
 * Created by zhenglian on 2016/11/1.
 */
public class ValueEvent {
    private Object object; //传递的数据对象
    private String message; //在任务传递过程中的一些信息，比如错误信息，警告信息等
    private boolean hasError = Boolean.FALSE; // 是否在执行过程中有错误
    private boolean hasWarn = Boolean.FALSE; //是否在执行过程中有警告信息
    private int index; //当前执行的任务索引号

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public boolean isHasWarn() {
        return hasWarn;
    }

    public void setHasWarn(boolean hasWarn) {
        this.hasWarn = hasWarn;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void copy(ValueEvent from) {
        this.object = from.object;
        this.index = from.index;
        this.hasError = false;
        this.hasWarn = false;
        message = "";
    }
}
