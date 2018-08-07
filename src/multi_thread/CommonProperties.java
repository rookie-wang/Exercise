package multi_thread;

//定义一些共有变量，可能会存在线程同步的问题
public class CommonProperties {

    public int nCount = 0;
    public volatile int vCount = 0;
}
