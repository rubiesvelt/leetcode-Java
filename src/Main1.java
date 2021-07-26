import java.util.HashMap;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class Main1<T> {
    T o1;
    T o2;

    public Main1(T t1, T t2) {
        o1 = t1;
        o2 = t2;
    }

    public static <W> void print(W wa) {
        wa.toString();
    }

    HashMap hashMap = new HashMap();
    Hashtable hashtable = new Hashtable();  // 线程安全
    ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();  // 线程安全，不允许有null的key、value
    TreeMap treeMap = new TreeMap();  // key自带排序，线程不安全，不允许有null的key、value


    int i = 9082045;
    char ch = 65535;  // 正确，范围 0-65535（0xffff），小于0大于65535编译错误
    byte b = 127;  // byte范围在-128到127

    char c = '\uff12';  // 正确，存储ASCII码，0-0xffff
    char c2 = '\n';  // 正确
    char a = 'A';  // 'A'的ASCII码为65，'a'的为97
}
