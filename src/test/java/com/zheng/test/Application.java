package com.zheng.test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zheng.Performer;
import com.zheng.Progressor;
import com.zheng.cache.MemoryCache;
import com.zheng.test.processors.ChooseItemProcessor;
import com.zheng.test.processors.FillItemProcessor;
import com.zheng.test.processors.ZgItemProcessor;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by zhenglian on 2016/11/5.
 */
public class Application {
    private Logger logger = LoggerFactory.getLogger(Application.class);
    @Test
    public void test() {
        List<Map<String, Object>> students = loadStudents();
        if(students.size() <= 0) {
            logger.debug("当前没有可执行的任务...");
            return;
        }

        ExamTask task = new ExamTask(students);
        Performer performer = new Performer();
        performer.setTask(task).addProcessorPool(new ChooseItemProcessor(),new ChooseItemProcessor())
                .addProcessorPool(new FillItemProcessor(),new ChooseItemProcessor())
                .addProcessorPool(new ZgItemProcessor(), new ChooseItemProcessor());
        MemoryCache.getInstance().put(performer.getOnlyId(), new Progressor(0, task.getTotalTaskNum(), "准备执行任务..."));
        performer.process();
        Progressor progressor = (Progressor) MemoryCache.getInstance().get(performer.getOnlyId());
        System.out.println(progressor.getTotal() + "," + progressor.getComplated() + "," + progressor.isFinished());
        //初始化进度条
        logger.info("考试结束...");
    }

    /**
     * 采用handleEventsWith会将处理器分组中的多个处理器都执行，
     * 采用handleEventsWithWorkerPool则只会选择处理器分组中的某一个执行
     */
    @Test
    public void test2() {
        List<Map<String, Object>> students = loadStudents();
        if(students.size() <= 0) {
            logger.debug("当前没有可执行的任务...");
            return;
        }

        ExamTask task = new ExamTask(students);
        Performer performer = new Performer();
        performer.setTask(task).addProcessors(new ChooseItemProcessor(),new ChooseItemProcessor())
                .addProcessors(new FillItemProcessor(),new ChooseItemProcessor())
                .addProcessors(new ZgItemProcessor(), new ChooseItemProcessor());
        MemoryCache.getInstance().put(performer.getOnlyId(), new Progressor(0, task.getTotalTaskNum(), "准备执行任务..."));
        performer.process();
        //初始化进度条
        logger.info("考试结束...");
    }

    private List<Map<String,Object>> loadStudents() {
        List<Map<String, Object>> students = Lists.newArrayList();
        Map<String, Object> student = null;
        for(int i = 1; i <= 3; i++) {
            student = Maps.newHashMap();
            student.put("username", "name" + i);
            students.add(student);
        }
        return students;
    }
}
