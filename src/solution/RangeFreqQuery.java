package solution;

import java.util.ArrayList;
import java.util.List;

public class RangeFreqQuery {

    List<Integer>[] lists;

    public RangeFreqQuery(int[] arr) {
        lists = new List[1002];
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            int u = arr[i];
            if (lists[u] == null) {
                List<Integer> curList = new ArrayList<>();
                curList.add(i);
                lists[u] = curList;
            } else {
                List<Integer> curList = lists[u];
                curList.add(i);
            }
        }
    }

    public int query(int left, int right, int value) {
        List<Integer> curList = lists[value];
        if (curList == null) return 0;
        // 在curList中找到 left, right 的下标
        int l;
        if (left == 0) {
            l = 0;
        } else {
            l = binarySearch(left - 1, curList);
        }
        int r = binarySearch(right, curList);
        return r - l;
    }

    public int binarySearch(int v, List<Integer> list) {
        int n = list.size();
        int l = 0;
        int r = n;
        while (l < r) {
            int m = (l + r) >> 1;
            int cur = list.get(m);
            if (cur <= v) {
                l = m + 1;
            } else {
                r = m;
            }
        }
        return l;
    }

    public static void main(String[] args) {
        int[] dis = {12, 33, 4, 56, 22, 2, 34, 33, 22, 12, 34, 56};
        RangeFreqQuery rangeFreqQuery = new RangeFreqQuery(dis);
        rangeFreqQuery.query(1,2,4);
        rangeFreqQuery.query(0,11,3);
    }
}
