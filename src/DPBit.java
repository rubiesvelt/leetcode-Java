import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DPBit {

    /**
     * 数位DP
     */

    /*
     * 1986. 完成任务的最少工作时间段
     * 你被安排了 n 个任务。任务需要花费的时间用长度为 n 的整数数组 tasks 表示，第 i 个任务需要花费 tasks[i] 小时完成
     * 一个 工作时间段中，你可以 至多连续工作 sessionTime 个小时
     * 求最少需要多少时间段，能够完成所有任务
     * 条件：
     * 1 <= n <= 14
     * 1 <= tasks[i] <= 10
     * 1 <= tasks[i] <= 10
     *
     * e.g.
     * tasks = [10,9,8,7,6,4,4,4,3,3,2]
     * sessionTime = 15
     * -> 答案为4 而不是5
     * 分组如下
     * 10 3 2
     * 9 6
     * 8 7
     * 4 4 4 3
     *
     * 状压dp
     */
    public int minSessions(int[] tasks, int sessionTime) {
        int n = tasks.length;
        int m = 1 << n;  // n位，数位dp
        final int INF = 20;
        int[] dp = new int[m];  // dp[i] 为选择状态为i(二进制)时，最小的分段数目
        Arrays.fill(dp, INF);

        // 罗列 全不选 - 全选的所有状态，合法状态预设为 1
        // 例如总共6个task，全不选 000000，全选 111111
        for (int i = 1; i < m; i++) {
            int state = i;  // 表示一个状态
            int idx = 0;
            int spend = 0;
            while (state > 0) {
                int bit = state & 1;
                if (bit == 1) {
                    spend += tasks[idx];
                }
                state >>= 1;
                idx++;
            }
            if (spend <= sessionTime) {
                dp[i] = 1;
            }
        }

        // 对每个状态枚举子集，跳过已经有最优解的状态
        for (int i = 1; i < m; i++) {
            if (dp[i] == 1) {
                continue;
            }
            for (int j = i; j > 0; j = (j - 1) & i) {  // j永远是i的子集
                /*
                 * 111
                 * 110 + 001
                 * 101 + 010
                 * 100 + 011
                 * 011 + 100
                 * 010 + 101
                 * 001 + 110
                 *
                 * 111(3) 或 110(1) + 001(1)
                 */
                dp[i] = Math.min(dp[i], dp[j] + dp[i ^ j]);  // ^ 为 异或; 此处只能异或，不能直接使用 ~ 取反，因为 ~ 是全部取反
            }
        }
        return dp[m - 1];
    }

    /*
     * 化为多次 0-1 背包问题 —— 不可取
     * 可以通过贪心得到最优解，但当背包中元素最多的时候，不能确认都装了哪些东西
     *
     * 错误情景！！！
     * [10,9,8,7,6,4,4,4,3,3,2]
     * -> 4
     *
     * 10 3 2
     * 9 6
     * 8 7
     * 4 4 4 3
     *
     * 答案为4不为5
     *
     * [10,10,10,10,7,7,4,4,4,2,2,1]
     *
     */
    public int minSessions1(int[] oritasks, int sessionTime) {
        int cnt = 0;
        int n = oritasks.length;
        int left = oritasks.length;
        int[] tasks = new int[n];
        Arrays.sort(oritasks);  // oritasks[]从小到大排序
        for (int i = 0; i < n; i++) {
            tasks[i] = oritasks[n - i - 1];  // tasks[]从大到小排序
        }
        while (left > 0) {
            Block[] f = new Block[sessionTime + 1];  // f[i] 表示元素和不超过 i 的时候，背包中装的物品最大和; f[i]永远小于i
            for (int i = 0; i < sessionTime + 1; i++) {
                f[i] = new Block();
            }
            // 每来一个元素，从高到低刷新 动态规划数组
            for (int i = 0; i < n; i++) {
                if (i == n - 1) {  // 最后一个元素
                    if (tasks[i] == 0) {
                        continue;
                    }
                    for (int j = sessionTime; j >= tasks[i]; j--) {
                        if (f[j].total > f[j - tasks[i]].total + tasks[i]) {  // 原来的 > 新的 则跳过 即 新的 == 原来的 进行更新（后来居上）
                            continue;
                        }
                        Block block = f[j];
                        block.total = f[j - tasks[i]].total + tasks[i];
                        block.used = new ArrayList<>(f[j - tasks[i]].used);
                        block.used.add(i);
                    }
                    continue;
                }
                if (tasks[i] == 0) {
                    continue;
                }
                for (int j = sessionTime; j >= tasks[i]; j--) {
                    if (f[j].total >= f[j - tasks[i]].total + tasks[i]) {  // 原来的 >= 新的 则跳过 即 新的 == 原来的 不进行更新（先入为主）
                        continue;
                    }
                    // 进行更新，把 f[j] 更新成新的
                    Block block = f[j];
                    block.total = f[j - tasks[i]].total + tasks[i];
                    block.used = new ArrayList<>(f[j - tasks[i]].used);
                    block.used.add(i);
                }
            }
            List<Integer> used = f[sessionTime].used;
            for (int t : used) {
                tasks[t] = 0;
                left--;
            }
            cnt++;
        }
        return cnt;
    }

    public static class Block {
        int total = 0;
        List<Integer> used = new ArrayList<>();
    }

    // 526. 优美的排列
    // 一个数组，包含1-n的整数，每一位都满足以下条件时，称为优美的排列
    // 1. 第 i 位的数字能被 i 整除
    // 2. i 能被第 i 位上的数字整除
    // 现在给定一个整数 N，请问可以构造多少个优美的排列？

    // 爆搜
    public int countArrangement(int n) {
        return dfs526(n, 1, new boolean[n + 1]);
    }

    private int dfs526(int n, int i, boolean[] visited) {
        if (i > n) {
            return 1;
        }

        int ans = 0;
        for (int num = 1; num <= n; num++) {
            if (!visited[num] && (num % i == 0 || i % num == 0)) {
                visited[num] = true;
                ans += dfs526(n, i + 1, visited);
                visited[num] = false;
            }
        }

        return ans;
    }

    // 记忆化搜索
    public int countArrangement1(int n) {
        int[][] mem = new int[n + 1][1 << n];  // 记忆化数组
        return dfs527(n, 1, 0, mem);
    }

    private int dfs527(int n, int i, int visited, int[][] memo) {
        if (i > n) {
            return 1;
        }

        if (memo[i][visited] != 0) {
            return memo[i][visited];
        }

        int ans = 0;
        for (int num = 1; num <= n; num++) {
            if (((1 << (num - 1)) & visited) == 0 && (num % i == 0 || i % num == 0)) {
                ans += dfs527(n, i + 1, (1 << (num - 1)) | visited, memo);
            }
        }

        memo[i][visited] = ans;

        return ans;
    }

    // 动态规划
    // dp[i][visited] += ∑(dp[i-1][visited打掉一个1])
    public int countArrangement2(int n) {
        // visited的最终状态为 (1 << n) - 1
        int mask = 1 << n;

        // dp[i][visited] 表示填入第 i 个数且访问状态为 visited 时的方案数（填完之后是 visited）
        // dp[i][visited] += ∑(dp[i-1][visited打掉一个1])
        int[][] dp = new int[n + 1][mask];

        // 初始值，与DFS中所有数都填完了返回1是一样的原理
        dp[0][0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int visited = 0; visited < mask; visited++) {
                // 只有 i 与 visited 中1的位数相等时才需要计算，因为填写第 1 个数时，不可能已经访问了两个数
                if (Integer.bitCount(visited) == i) {
                    for (int num = 1; num <= n; num++) {
                        // 第一个条件：与DFS相反，这里表示本次填入了这个数
                        if (((1 << (num - 1)) & visited) != 0 && (num % i == 0 || i % num == 0)) {
                            // i - 1位置没有放入num
                            // 1 << (num - 1) 表示第 num 位是1，取反就是这位是0，其他都是1
                            // 再与 visited 与运算表示打掉 visited 这位的 1
                            dp[i][visited] += dp[i - 1][visited & (~(1 << (num - 1)))];
                        }
                    }
                }
            }
        }
        return dp[n][mask - 1];
    }
}
