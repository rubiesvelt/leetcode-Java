public class DPStock {
    /*
     * 714. 买卖股票的最佳时机含手续费
     */
    public int maxProfit(int[] prices, int fee) {
        // 持有 0、未持有 1
        int n = prices.length;
        int[][] dp = new int[n][2];
        dp[0][0] = prices[0];
        // 持有 -> 未持有 收手续费；持有 -> 持有
        // 未持有 -> 持有；未持有 -> 未持有
        for (int i = 1; i < n; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] - prices[i]);
            dp[i][1] = Math.max(dp[i - 1][0] + prices[i] - fee, dp[i - 1][1]);
        }
        return dp[n - 1][1];
    }

    /*
     * 309. 最佳买卖股票时机含冷冻期
     *
     * [2,1,2,0,1]
     */
    public int maxProfit(int[] prices) {
        // 有两种状态，持有 0、可买 1、冷冻期 2
        int n = prices.length;
        int[][] dp = new int[n][3];
        dp[0][0] = -prices[0];
        // 持有 -> 持有；可买 -> 持有
        // 冷冻期 -> 可买；可买 -> 可买
        // 持有 -> 冷冻期
        for (int i = 1; i < n; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] - prices[i]);
            dp[i][1] = Math.max(dp[i - 1][1], dp[i - 1][2]);
            dp[i][2] = dp[i - 1][0] + prices[i];  // 卖出该产品
        }
        return Math.max(dp[n - 1][1], dp[n - 1][2]);
    }

    /*
     * 122. 买卖股票的最佳时机 II
     */
    public int maxProfit2(int[] prices) {
        int n = prices.length;
        int last = prices[0];
        int ans = 0;
        for (int i = 1; i < n; i++) {
            int now = prices[i];
            if (now > last) {
                ans += now - last;
            }
            last = now;
        }
        return ans;
    }

    /*
     * 121. 买卖股票的最佳时机
     */
    public int maxProfit1(int[] prices) {
        int min = 0x3f3f3f3f;  // min 为 u 之前最小的
        int ans = 0;
        for (int u : prices) {
            if (u < min) {
                min = u;
            } else {
                ans = Math.max(ans, u - min);
            }
        }
        return ans;
    }

}
