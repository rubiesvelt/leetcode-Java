public class DPBit {

    /**
     * 数位DP
     */

    // 526. 优美的排列
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
    public int countArrangement3(int n) {
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
