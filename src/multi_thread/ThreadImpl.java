package multi_thread;

public class ThreadImpl extends Thread {

    //定义几个变量。分别标识线程，以及线程需要处理的资源
    public String UUID;//线程的唯一标识

    public String dealStr; //需要处理资源的标识

    public void run(){
        for(int i=0;i<5;i++) System.out.println("thread of thread " + i);
    }
}
