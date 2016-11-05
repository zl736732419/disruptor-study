package com.zheng.test;

import com.zheng.event.EventTask;

import java.util.List;
import java.util.Map;

/**
 * Created by zhenglian on 2016/11/5.
 */
public class ExamTask extends EventTask<Map<String, Object>>{
    public ExamTask(List<Map<String, Object>> students) {
        super(students);
    }

    public Object get() {
        return new StudentTaskData(cur);
    }
}
