package com.zheng.test.processors;

import com.zheng.event.Processor;
import com.zheng.test.StudentTaskData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by zhenglian on 2016/11/5.
 */
public class ZgItemProcessor implements Processor<StudentTaskData>{
    private Logger logger = LoggerFactory.getLogger(ZgItemProcessor.class);

    public Class<StudentTaskData> getObjClazz() {
        return StudentTaskData.class;
    }

    public void process(StudentTaskData data) {
        Map<String, Object> student = data.getStudent();
        logger.info(student.get("username") + "开始做解答题...");
    }
}
