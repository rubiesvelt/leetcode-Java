package solution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*
 * 1707. 与数组中元素的最大异或值
 * 是 421. 数组中两个数的最大异或值 的升级版
 *
 * 重点：
 * 这种提前给定了所有询问的题目，我们可以运用"离线思想"，即"调整询问的回答顺序"进行求解
 */
public class MaximizeXor {
    // 用数组实现的Trie树，每个idx赋予一个新节点
    static int N = (int)1e5 * 32;
    // 此处如果使用实例字段，则每跑一个用例，都会被实例化一次，会超时
    static int[][] trie = new int[N][2];
    static int idx = 0;

    // 每跑一个数据，会被实例化一次，每次实例化的时候被调用，做"清理工作"
    // 使用静态字段时候需要注意
    public MaximizeXor() {
        for (int i = 0; i <= idx; i++) {
            Arrays.fill(trie[i], 0);
        }
        idx = 0;
    }
    void add(int x) {
        int p = 0;
        for (int i = 31; i >= 0; i--) {
            int u = (x >> i) & 1;
            if (trie[p][u] == 0) trie[p][u] = ++idx;
            p = trie[p][u];
        }
    }
    int getVal(int x) {
        int ans = 0;
        int p = 0;
        for (int i = 31; i >= 0; i--) {
            int a = (x >> i) & 1, b = 1 - a;
            if (trie[p][b] != 0) {
                p = trie[p][b];
                ans = ans | (b << i);
            } else {
                p = trie[p][a];
                ans = ans | (a << i);
            }
        }
        return ans ^ x;
    }
    public int[] maximizeXor(int[] nums, int[][] qs) {
        int m = nums.length, n = qs.length;

        // 巧妙的运用HashMap将原顺序保存下来，容易还原
        Map<int[], Integer> map = new HashMap<>();
        for (int i = 0; i < n; i++) map.put(qs[i], i);

        Arrays.sort(nums);
        Arrays.sort(qs, (a, b)->a[1]-b[1]);

        int[] ans = new int[n];
        int loc = 0; // 数组 num 的下标
        for (int[] q : qs) {
            int x = q[0], limit = q[1];
            // 将小于等于 limit 的数存入 Trie
            while (loc < m && nums[loc] <= limit) {
                add(nums[loc++]);
            }
            if (loc == 0) {
                ans[map.get(q)] = -1;
            } else {
                ans[map.get(q)] = getVal(x);
            }
        }
        return ans;
    }
}
