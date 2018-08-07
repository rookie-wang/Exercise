package multi_thread;

import java.util.stream.IntStream;

public class RunnableImpl implements Runnable {
    //定义几个变量。分别标识线程，以及线程需要处理的资源
    public String UUID;//线程的唯一标识

    public String dealStr; //需要处理资源的标识


    @Override
    public void run() {
        //获取需要标识的资源
        //处理需要标识的资源
        //销毁标识
        IntStream.range(0,5).forEach(i->{
            System.out.println("thread of runnable "+i);
        });
    }
}
