package extend_test;

public interface BaseInterface {
    //接口里面定义的变量默认都是public static final类型的，实现类可以直接使用
    //接口里面只能定义abstract方法
    String baseStrA = "str a of BaseInterface";

    void baseMethod();

    void sameMethod();
}
