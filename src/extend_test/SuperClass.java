package extend_test;

public class SuperClass {

    //父类静态变量和成员变量
    public static String sParentStrA = "parent";
    public String parentStrA;

    public SuperClass(){
        System.out.println("这是父类的无参构造函数");
       // this.parentStrA = "A";
    }

    public SuperClass(String a){
        System.out.println("这是父类的有参构造函数");
        this.parentStrA = a;
    }
}
