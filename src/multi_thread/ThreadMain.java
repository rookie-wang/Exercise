package multi_thread;

import java.util.concurrent.*;


/**
 * 线程的销毁和创建的资源消耗，是指JAVA底层创建一个准备执行Thread对象的资源的开销
 * 所以可以定义一个五个线程的线程池，准备好五个线程来执行传入的线程对象，相当于每次
 * 过来得时候，直接用这五个线程的来执行，多余的就开始等待
 */
public class ThreadMain {

    public static void main(String[] args){
        ExecutorService executor = Executors.newFixedThreadPool(5);

        for(int i=0;i<15;i++){
            RunnableImpl myTask = new RunnableImpl(i);
            executor.execute(myTask);
        }
        executor.shutdown();
    }
}
