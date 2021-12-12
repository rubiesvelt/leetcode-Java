package solution;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class SORTracker {
    SortedSet<Item> set = new TreeSet<>((o1, o2) -> {
        if (o1.score != o2.score) {
            return o2.score - o1.score;  // 首先按 score 从大到小
        } else {
            return o1.name.compareTo(o2.name);  // score 相同时按 name 字典序 从小到大
        }
    });

    public SORTracker() {
    }

    public void add(String name, int score) {
        Item item = new Item();
        item.name = name;
        item.score = score;
        set.add(item);
    }

    int cnt = 0;

    public String get() {
        int cur = cnt;
        cnt++;
        Iterator<Item> it = set.iterator();
        while (cur > 0 && it.hasNext()) {
            it.next();
            cur--;
        }
        return it.next().name;
    }

    public static class Item {
        String name;
        int score;
    }
}
