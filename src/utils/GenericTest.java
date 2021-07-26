package utils;


import java.util.ArrayList;
import java.util.List;

public class GenericTest<I, O> {

    Class<?> clazz;  // Class<?> clazz可以是任意类的Class对象

    // 根据擦除法，I和O永远是Object，但iClass, oClass对象可以为任意class
    Class<I> iClass;  // 表示这个可以代表 O 的 Class，可以实例化 O

    Class<O> oClass;  // 表示这个可以代表 I 的 Class，可以实例化 I

    /**
     * 返回O相关的Class
     *
     * @param oClazz 指定 O 相关的 Class，此处传入实现了泛型
     * @param i      Object 的子类型元素
     * @return 返回Object的子类
     * @throws InstantiationException e
     * @throws IllegalAccessException e
     */
    public O genericTest(Class<O> oClazz, I i) throws InstantiationException, IllegalAccessException {

        // 传入i为ArrayList后，I 仍为 Object，O 也为 Object
        O o = oClazz.newInstance();
        oClass = oClazz;  // java.lang.String

        Class<?> clz = i.getClass();  // .getClass()返回Class<?>, 实际为 ArrayList
        iClass = (Class<I>) clz;  // ArrayList

        // String —— 类名
        // String.class —— String类的Class对象
        // Class<String> —— String类的Class对象的类
        clazz = String.class;  // Class<?>
        boolean t = clazz.isInstance(o);

        Object object = new Object();
        I objI = (I) object;  // Object
        O ObjO = (O) object;  // Object

        Class<?> oClazz1 = Integer.class;  // Class对象可以通过
        oClass = (Class<O>) oClazz1;  // oClass = Integer.class

        return o;
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        GenericTest genericTest = new GenericTest<>();
        genericTest.genericTest(String.class, new ArrayList());
        Pair pair = new Pair(List.class, "aaa", Object.class);
        Object o1 = pair.getFirst();
        Object o2 = pair.getLast();
    }

    public static class Pair<T> {

        private final T first;

        private final T last;

        public Pair(T first, T last, Class<T> tClass) {
            this.first = first;
            this.last = last;
            if (!first.getClass().isAssignableFrom(tClass)) {
                System.out.println("what the hell");
            }
        }

        public T getFirst() {
            return first;
        }

        public T getLast() {
            return last;
        }
    }
}
