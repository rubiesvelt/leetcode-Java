package solution;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 百度一面
 * 拓扑排序
 */
public class TaskOrder {

    // [[b,a], [c,a], [d,b], [d,c]]
    public String[] getOrder(int n, String[][] requests) {

        int[] cnt = new int[n];
        for (String[] r : requests) {
            cnt[r[0].charAt(0) - 'a']++;
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (cnt[i] == 0) {
                queue.add(i);
            }
        }

        String[] ans = new String[n];
        int index = 0;
        while (!queue.isEmpty()) {
            int t = queue.poll();
            char c = (char) ('a' + t);
            String s = String.valueOf(c);
            ans[index++] = s;
            for (String[] r : requests) {
                if (r[1].charAt(0) == c) {
                    cnt[r[0].charAt(0) - 'a']--;
                    if (cnt[r[0].charAt(0) - 'a'] == 0) {
                        queue.add(r[0].charAt(0) - 'a');
                    }
                }
            }
        }
        if (index == n) {
            return ans;
        }
        return new String[0];
    }
}
