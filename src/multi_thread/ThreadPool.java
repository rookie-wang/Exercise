package multi_thread;


import java.util.concurrent.ConcurrentHashMap;

//线程池的实现 - 线程池式为了减小创建和销毁线程的开销以及控制线程的创建数量
public class ThreadPool {

    //线程池
    private ConcurrentHashMap<String,RunnableImpl> pool;

    //定义线程池的初始化大小
    private static final int initSize = 5;
    //定义线程池的最大大小
    private static final int maxSize = 5;

    //单例模式构造线程池

    public RunnableImpl getThread(String strDeal){
        //如果线程池已经达到最大容量，返回Null
        return null;
    }

}
