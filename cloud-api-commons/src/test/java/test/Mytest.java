package test;


public class Mytest {
    public static void main(String[] args) {

        try {
//            ClassLoader classLoader = Class.forName("java.lang.String").getClassLoader();
            ClassLoader classLoader = Class.forName("test.Mytest").getClassLoader();
            System.out.println(classLoader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
