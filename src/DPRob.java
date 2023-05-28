/**
 * DP —— 打家劫舍
 */
public class DPRob {

    // 198. House Robber
    // 打家劫舍
    // two adjacent houses can not be broken into on the same night
    public int rob1(int[] nums) {
        int n = nums.length;
        if (n == 1) {
            return nums[0];
        }
        if (n == 2) {
            return Math.max(nums[1], nums[0]);
        }
        int[] dp = new int[n];  // dp[i] 表示下标 [0, i] 内最高打劫钱财
        dp[0] = nums[0];
        dp[1] = Math.max(nums[1], nums[0]);
        for (int i = 2; i < n; i++) {
            dp[i] = Math.max(dp[i - 2] + nums[i], dp[i - 1]);  // 状态转移方程
        }
        return dp[n - 1];
    }
}
