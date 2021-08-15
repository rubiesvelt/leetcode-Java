import java.util.*;

/**
 * 区间DP —— 划分若干区间
 */
public class DPInterval {

    // 1959. K 次调整数组大小浪费的最小总空间
    // 区间dp
    //
    // 题目等价于：
    // 给定数组 nums 以及整数 k
    // 需要把数组完整地分成 k+1 段连续的子数组
    // 每一段的权值是"这一段的最大值乘以这一段的长度"
    // 求最小化总权值(减去原值之和)
    public int minSpaceWastedKResizing(int[] nums, int k) {
        int n = nums.length;
        int sum = 0;
        int[][] g = new int[n][n];  // g[i][j] 表示i到j区间内，这一段的最大值乘以这一段的长度
        for (int i = 0; i < n; i++) {
            int m = 0;
            for (int j = i; j < n; j++) {
                m = Math.max(m, nums[j]);
                g[i][j] = m * (j + 1 - i);
            }
            sum += nums[i];
        }

        int INF = 0x3f3f3f3f;  // 适用于，需要很大的数，但还没到顶，可以往上加的情景
        int[][] dp = new int[n][k + 2];  // dp[i][j] 表示将 nums[0..i] 分成 j 段的最小总权值
        // dp[i][j] =
        // min(
        //      g[0][i],
        //      dp[0][j - 1]+g[1][i],
        //      dp[1][j - 1]+g[2][i],
        //      ... ,
        //      dp[i - 1][j - 1]+g[i][i]
        // )
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i], INF);
        }

        for (int i = 0; i < n; i++) {
            for (int j = 1; j <= k + 1; j++) {
                for (int l = 0; l <= i; l++) {
                    if (l == 0) {
                        dp[i][j] = Math.min(dp[i][j], g[0][i]);
                    } else {
                        dp[i][j] = Math.min(dp[i][j], dp[l - 1][j - 1] + g[l][i]);
                    }
                }
            }
        }
        return dp[n - 1][k + 1] - sum;
    }
}
