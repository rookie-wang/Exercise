package extend_test;

public class ChildClass extends SuperClass {

    //静态成员与实例成员
    public static String sParentStrA = "child";
    public String parentStrA;

    //如果基类的构造函数没有无参的，那么子类里面必须定义一个构造函数并且显式的调用父类构造函数
    //因为在子类在实例化的时候，会隐式的调用父类的无参构造函数
    public ChildClass(String a){
        //会隐式的调用父类的无参构造方法
        System.out.println("子类的有参构造方法");
        this.parentStrA = a;

    }

    public ChildClass(){
        //会隐式的调用父类的无参构造方法
        System.out.println("子类的无参构造方法");
        this.parentStrA="B";
    }

    public static void main(String[] args){
        //验证强类型转换下，同名的成员便来和实例变量的值
        //实验证明，如果基类跟子类有相同名字的实例变量，跟类变量，子类向上转换的时候，使用的还是自己的属性

        SuperClass sc = new ChildClass();
        System.out.println(sc.parentStrA);
        System.out.println(sc.sParentStrA);
    }

}

