import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * DP —— 背包问题
 */
public class DPBag {

    /**
     * --------------------------------------- 01背包 ---------------------------------------
     *
     * 0-1 背包问题选取的物品的容积总量 不能超过 规定的总量，物品不可重复选
     *
     */

    /*
     * 416. 分割等和子集
     * 给你一个 只包含正整数 的 非空 数组 nums ，请你判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等
     *
     * 动态规划：01背包问题
     * 选取的数字之和需要 恰好等于 规定的和的一半。
     */
    // 二维版
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
        // 二维
        boolean[][] dp = new boolean[n][count + 1];  // dp[i][j]表示从数组的 [0, i] 这个子区间内挑选一些正整数，每个数只能用一次，使得这些数的和恰好等于 j
        // 状态转移方程
        // dp[i][j] =
        // dp[i - 1][j] 或 dp[i - 1][j - nums[i]]
        // true   (nums[i] == j, 即 dp[i][num[i]])
        if (nums[0] <= count) {
            dp[0][nums[0]] = true;
        }
        if (count == nums[0]) {
            return true;
        }
        for (int i = 1; i < n; i++) {
            if (nums[i] <= count) {
                dp[i][nums[i]] = true;
            }
            for (int j = 0; j <= count; j++) {  // 由于是"岔开的"所以 正序遍历 倒序遍历 没有区别; 需要遍历 0 -> count 全部，因为没有留下的状态
                if (j >= nums[i]) {
                    dp[i][j] = dp[i][j] || dp[i - 1][j - nums[i]] || dp[i - 1][j];
                } else {
                    dp[i][j] = dp[i][j] || dp[i - 1][j];
                }
                if (dp[i][count]) {
                    return true;
                }
            }
        }
        return false;
    }

    // 一维版
    public boolean canPartition1(int[] nums) {
        int sum = 0;
        for (int i : nums) {
            sum += i;
        }
        if ((sum & 1) == 1) {
            return false;
        }
        int count = sum >> 1;
        // 压缩成一维
        boolean[] f = new boolean[count + 1];  // f[i]表示数组是否可以和为i
        f[0] = true;
        for (int num : nums) {
            for (int j = count; j >= num; j--) {  // 不能借鉴本轮的，所以从后往前遍历; 且只遍历 count -> num 部分，留下的也是有效状态 —— 跟最长递增子序列相似
                f[j] = f[j] || f[j - num];
                if (f[count]) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * 1049. 最后一块石头的重量 II
     *
     * 每一回合，从中选出任意两块石头，然后将它们一起粉碎，有以下两种情况：
     * 1. 如果 x == y，那么两块石头都会被完全粉碎
     * 2. 如果 x != y，那么重量为 x 的石头将会完全粉碎，而重量为 y 的石头新重量为 y-x
     * 最后，最多只会剩下一块 石头。返回此石头 最小的可能重量 。如果没有石头剩下，就返回 0
     *
     * 动态规划，01背包问题
     *
     * 转换为
     * —> 为 stones 中的每个数字添加 +/-，使得形成的「计算表达式」结果绝对值最小
     * -> 在 stones 中选出若干元素，使这些元素之和最大，但不可大于 sum/2  —— 标准的背包问题
     */
    public int lastStoneWeightII(int[] stones) {
        int n = stones.length;
        int sum = 0;
        for (int i : stones) {
            sum += i;
        }
        int t = sum / 2;
        // dp[i][j] 表示前 i 个物品，凑成总和不超过 j 的最大价值
        int[] f = new int[t + 1];  // f[i] 表示凑成总和不超过i的最大价值
        for (int x : stones) {
            for (int j = t; j >= x; j--) {
                f[j] = Math.max(f[j], f[j - x] + x);
            }
        }
        return Math.abs(sum - f[t] - f[t]);
    }

    // 494. 目标和
    // 01背包问题
    // 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个 表达式，返回可以通过上述方法构造的、运算结果等于 target 的不同 表达式 的数目
    // 动态规划
    public int findTargetSumWays(int[] nums, int target) {
        int n = nums.length;
        int s = 0;
        for (int i : nums) s += Math.abs(i);
        if (target > s || (s - target) % 2 != 0) {
            return 0;
        }
        int m = (s - target) / 2;

        int[][] f = new int[n + 1][m + 1];  // f[i][j] 为从数组nums中 0 - i 的元素进行 累加 可得到 j 的方法数量
        f[0][0] = 1;
        for (int i = 1; i <= n; i++) {
            int x = nums[i - 1];
            for (int j = 0; j <= m; j++) {
                f[i][j] += f[i - 1][j];
                if (j >= x) {
                    f[i][j] += f[i - 1][j - x];
                }
            }
        }
        return f[n][m];
    }


    // 记忆化搜索
    public int findTargetSumWays1(int[] nums, int t) {
        return dfs1(nums, t, 0, 0);
    }

    Map<String, Integer> map = new HashMap<>();

    int dfs1(int[] nums, int t, int u, int cur) {  // u为当前下标，cur为当前和
        if (map.containsKey(u + "_" + cur)) {
            return map.get(u + "_" + cur);
        }
        if (u == nums.length) {
            return cur == t ? 1 : 0;
        }
        int left = dfs1(nums, t, u + 1, cur + nums[u]);
        int right = dfs1(nums, t, u + 1, cur - nums[u]);
        String s = u + "_" + cur;
        map.put(s, right + left);
        return left + right;
    }

    // dfs 爆搜
    public int findTargetSumWays2(int[] nums, int t) {
        return dfs2(nums, t, 0, 0);
    }

    int dfs2(int[] nums, int t, int u, int cur) {
        if (u == nums.length) {
            return cur == t ? 1 : 0;
        }
        int left = dfs2(nums, t, u + 1, cur + nums[u]);
        int right = dfs2(nums, t, u + 1, cur - nums[u]);
        return left + right;
    }

    /**
     * --------------------------------------- 完全背包问题 ---------------------------------------
     */

    /*
     * 279. 完全平方数
     * 动态规划，完全背包问题
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
}
