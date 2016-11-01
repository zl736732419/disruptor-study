###Disruptor并发框架学习
    该项目是从公司实际开发项目中抽离出的disruptor并发核心框架结构
    其中涉及到disruptor框架的使用，disruptor两种工作模式handleEvents和
    handleEventsWithWorkPool,disruptor异常处理，进度监控(Progressor)，
    进度监控可以用于前端生成进度条
    
####handleEvents and handleEventsWithWorkPool区别
#####handleEvents
    采用hanldeEvents处理多个任务时，如果注册了多个任务处理器，那么这些任务处理器
    会一次执行,（当然这些任务处理器要属于同一个工作组）disruptor.handleEventsWith(processors);

####handleEventsWithWorkPool
    采用handleEventsWithWorkPool处理多个任务时，如果注册了多个任务处理器，那么这些
    disruptor会从分组中的这些处理器中选择其中一个执行，而不是像上面handleEvents那样
    每一个处理器都一次执行,（当然这些任务处理器要属于同一个工作组）disruptor.handleEventsWithWorkPool(processors);
    
