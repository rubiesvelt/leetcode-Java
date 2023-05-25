import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class SimulateTreeSet {
    /*
     * 2070. Most Beautiful Item for Each Query
     */
    public int[] maximumBeauty(int[][] items, int[] queries) {
        Arrays.sort(items, (o1, o2) -> o1[0] - o2[0]);  // 价格从小到大
        int max = Integer.MIN_VALUE;

        TreeSet<Integer> treeSet = new TreeSet<>();  // TreeSet yyds!!
        Map<Integer, Integer> map = new HashMap<>();
        for (int[] item : items) {
            max = Math.max(item[1], max);
            treeSet.add(item[0]);
            map.put(item[0], max);
        }
        int n = queries.length;
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            Integer index = treeSet.floor(queries[i]);
            int score = 0;
            if (index != null) {
                score = map.get(index);
            }
            ans[i] = score;
        }
        return ans;
    }
}
