### Disruptor并发框架学习
    该项目是从公司实际开发项目中抽离出的disruptor并发核心框架结构
    其中涉及到disruptor框架的使用，disruptor两种工作模式handleEvents和
    handleEventsWithWorkPool,disruptor异常处理，进度监控(Progressor)，
    进度监控可以用于前端生成进度条
    
#### handleEvents and handleEventsWithWorkPool区别
##### handleEvents
    采用hanldeEvents处理多个任务时，如果注册了多个任务处理器，那么这些任务处理器
    会一次执行,（当然这些任务处理器要属于同一个工作组）disruptor.handleEventsWith(processors);

#### handleEventsWithWorkPool
    采用handleEventsWithWorkPool处理多个任务时，如果注册了多个任务处理器，那么这些
    disruptor会从分组中的这些处理器中选择其中一个执行，而不是像上面handleEvents那样
    每一个处理器都一次执行,（当然这些任务处理器要属于同一个工作组）disruptor.handleEventsWithWorkPool(processors);
    
>disruptor框架中多个线程之间数据的传递是通过定义的事件绑定数据传递的


#### 测试用例
    该项目中编写了一个非常简单的测试用例，模拟学生考试过程，从做选择题、填空题、解答题
    三种题型，这里的答题顺序为选择题->填空题->解答题->考试结束
    当然这些业务处理逻辑需要根据不同的场景进行编写，可以直接运行test/java/com/zheng/test/Application.java中的
    测试用例查看效果
