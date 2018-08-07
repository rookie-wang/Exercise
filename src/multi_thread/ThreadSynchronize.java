package multi_thread;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//验证线程同步的三种方式
public class ThreadSynchronize implements Runnable {
    public  static int total = 20;

    public static final String lock = "sold_lock";

    private static Lock rLock = new ReentrantLock();

    @Override
    public void run() {
        synchronizeMethod();
       // lockMethod();
    }

    //通过Synchronize的方式来同步,锁都是针对于对象而言的，Synchronize枷锁的对象应该是同一个
    private void synchronizeMethod(){
        while (total>0){
            synchronized (lock) {
                if(total>0){
                    System.out.println(Thread.currentThread().getName() + " sold " + total);
                    total--;
                }else {
                    System.out.println(Thread.currentThread().getName()+" empty");
                }
            }
            //睡眠0.1秒，更好的展示多线程
            try{
                Thread.sleep(100);
            }catch (Exception e){

            }
        }
    }

    //通过JDK1.5以后的重入锁实现线程同步，多个线程用的锁应该是同一个，所以lock应该是静态变量或者是同一个对象的lock变量
    private void lockMethod(){

        while (total>0){
            try{

                rLock.lock();
                {
                    if(total>0){
                        System.out.println(Thread.currentThread().getName() + " sold " + total);
                        total--;
                    }else {
                        System.out.println(Thread.currentThread().getName()+" empty");
                    }
                }

            }catch (Exception e){

            }finally {
                rLock.unlock();
            }

            //睡眠0.1秒，更好的展示多线程
            try{
                Thread.sleep(100);
            }catch (Exception e){

            }
        }
    }

    //通过ThreadLocal来同步

    //关于volatile：
    //1. volatile保证了变量修改后的可见性，当修改一个变量后，不放在缓存中，而是直接反映在主存中，读取也是一样
    //2. 虽然保证类变量的可见性，但是无法保证操作的原子性，所以在修饰一个整数，然后多线程对他相加的时候，结果不是预期的结果
    //3. volatile在一定场景下可以替代synchronize，但是无法完全替代，Synchronize可以防止多个线程同时执行一段代码，所以是有牺牲效率为代价的
    //A. 标记量 B. Double  Check 单例模式中的双重检查模式（懒加载的模式下，可能会存在多线程获取实例的时候，重复初始化的问题）
}
