package com.zheng.test;

import java.util.Map;

/**
 * Created by zhenglian on 2016/11/5.
 */
public class StudentTaskData {
    private Map<String, Object> student;

    public StudentTaskData(Map<String, Object> student) {
        this.student = student;
    }

    public Map<String, Object> getStudent() {
        return student;
    }

    public void setStudent(Map<String, Object> student) {
        this.student = student;
    }
}
