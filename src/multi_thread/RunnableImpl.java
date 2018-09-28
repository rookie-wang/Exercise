package multi_thread;

public class RunnableImpl implements Runnable {

    /**
     *     标记当前线程的标识，用于执行不同的任务
     */
    private int taskNum;

    public RunnableImpl(int taskNum){
        this.taskNum = taskNum;
    }
    @Override
    public void run() {
        System.out.println("正在执行task "+taskNum);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("task "+taskNum+"执行完毕");
    }
}
