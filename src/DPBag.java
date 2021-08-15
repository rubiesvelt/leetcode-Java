import java.util.Arrays;

/**
 * DP —— 背包问题
 */
public class DPBag {

    /*
     * 1449. 数位成本和为目标值的最大数字
     * 动态规划：完全背包问题
     */
    public String largestNumber3(int[] cost, int t) {
        // f[j]表示当和为j时，需要多少个数字
        int[] f = new int[t + 1];
        // 这块需要填MIN_VALUE，因为会偶尔 +1 +1
        Arrays.fill(f, Integer.MIN_VALUE);
        f[0] = 0;
        for (int i = 0; i < 9; i++) {
            // 一个一个数字过
            int u = cost[i];
            for (int j = u; j <= t; j++) {
                // 由于f[j]和f[j - u]都参考的是上一个i的状态，所以这里必须倒序
                // —— 错，因为这是完全背包问题，可以重用，所以正序

                // 如 cost = [6,3,2], target = 12, u = 2
                // j = 4 时用 u 可以 在j = 2 基础上 + 1
                // j = 6 时用 u 也可以在j = 4 基础上继续用 u 来 +1，总共 +2
                // j = 8 ...
                f[j] = Math.max(f[j], f[j - u] + 1);
            }
        }
        if (f[t] < 0) return "0";
        String ans = "";
        int j = t;
        for (int i = 9; i > 0; i--) {
            // 判断，第i位可以作为ans中的下一位吗？由于只能 99887 或 99865 递减，所以不用发愁i又来了
            int u = cost[i - 1];
            while (j >= u && f[j] == f[j - u] + 1) {
                ans += i;
                j = j - u;
            }
        }
        return ans;
    }

    /*
     * 416. 分割等和子集
     * 动态规划：01背包问题
     */
    public boolean canPartition(int[] nums) {
        int sum = 0;
        for (int i : nums) {
            sum += i;
        }
        if ((sum & 1) == 1) {
            return false;
        }
        int count = sum >> 1;
        int n = nums.length;
        boolean[] f = new boolean[count + 1];
        f[0] = true;
        for (int num : nums) {
            // 每一个从后往前
            for (int j = count; j >= num; j--) {
                f[j] = f[j] || f[j - num];
                if (f[count]) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * 279. 完全平方数
     * 动态规划，完全背包问题
     *
     */
    public int numSquares(int n) {
        int[] f = new int[n + 1];
        Arrays.fill(f, 0x3f3f3f3f);
        f[0] = 0;
        for (int i = 1; i * i <= n; i++) {
            int k = i * i;
            for (int x = k; x <= n; x++) {
                f[x] = Math.min(f[x], f[x - k] + 1);
            }
        }
        return f[n];
    }

    /*
     * 动态规划，01背包问题
     */
    public int lastStoneWeightII(int[] ss) {
        int n = ss.length;
        int sum = 0;
        for (int i : ss) sum += i;
        int t = sum / 2;
        int[] f = new int[t + 1];
        for (int i = 1; i <= n; i++) {
            int x = ss[i - 1];
            for (int j = t; j >= x; j--) {
                f[j] = Math.max(f[j], f[j - x] + x);
            }
        }
        return Math.abs(sum - f[t] - f[t]);
    }

    // 5815. 扣分后的最大得分
    // 双重dp + 状态压缩成一维
    public long maxPoints2(int[][] points) {
        int m = points.length;
        int n = points[0].length;
        long[] dp = new long[n];
        for (int i = 0; i < m; i++) {
            long[] cur = new long[n];
            long lmax = 0;
            for (int j = 0; j < n; j++) {
                lmax = Math.max(lmax - 1, dp[j]);
                cur[j] = lmax;
            }
            long rmax = 0;
            for (int j = n - 1; j >= 0; j--) {
                rmax = Math.max(rmax - 1, dp[j]);
                cur[j] = Math.max(cur[j], rmax);
            }
            for (int j = 0; j < n; j++) {
                dp[j] = cur[j] + points[i][j];
            }
        }
        long ans = 0;
        for (int j = 0; j < n; j++) {
            ans = Math.max(ans, dp[j]);
        }
        return ans;
    }
}
