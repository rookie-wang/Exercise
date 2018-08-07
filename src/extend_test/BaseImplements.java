package extend_test;

public class BaseImplements  implements ChildInterface{
    @Override
    public void childMethod() {

    }

    @Override
    public void baseMethod() {

    }

    @Override
    public void sameMethod() {

    }

    //实验证明
    public static void main(String[] args){
        ChildInterface ci = new BaseImplements();
        BaseInterface bi = new BaseImplements();

        System.out.println(ci.baseStrA);
        System.out.println(bi.baseStrA);
    }
}
