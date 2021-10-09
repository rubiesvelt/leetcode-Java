import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * DP —— 背包问题
 */
public class DPBag {

    /**
     * --------------------------------------- 01背包 ---------------------------------------<p>
     *
     * 0-1 背包问题，不可重复选<br>
     *
     * 选取物品总量 不能超过 规定数量，问最多物品数<br>
     * 选取物品总量 恰好等于 规定数量，问最小物品数<br>
     * 选取物品总量 恰好等于 规定数量，问可行性<br>
     * 选取物品总量 恰好等于 规定数量，问方案总数<br>
     * 选取物品总量 恰好等于 规定数量，问最小物品数量
     */

    /*
     * 416. 分割等和子集
     * 给你一个 只包含正整数 的 非空 数组 nums ，请你判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等
     *
     * 动态规划：01背包问题
     * 选取的数字之和需要 恰好等于 规定的和的一半
     *
     * 问题转化为，选取xxx个元素，使这些元素和为count，求是否可行
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
     * -> 在 stones 中选出若干元素，使这些元素之和最大，但不可大于 sum/2，求最大和
     *
     * 问题转化为，选取xxx个元素，使这些元素之和最大，但不能大于 count，求最大和
     */
    public int lastStoneWeightII(int[] stones) {
        int sum = 0;
        for (int i : stones) {
            sum += i;
        }
        int t = sum / 2;
        // dp[i][j] 表示前 i 个物品，凑成总和不超过 j 的最大价值
        int[] f = new int[t + 1];  // f[i] 表示凑成总和不超过i的最大价值，即f[i]不大于i
        for (int x : stones) {
            for (int j = t; j >= x; j--) {
                f[j] = Math.max(f[j], f[j - x] + x);
            }
        }
        return Math.abs(sum - f[t] - f[t]);
    }

    /*
     * 494. 目标和
     * 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个 表达式，返回可以通过上述方法构造的、运算结果等于 target 的不同 表达式 的数目
     * 01背包问题
     * 问题转化为：
     * 选出 xxx 个元素，使这些元素的和为 m，求方案总数
     */
    // 二维
    public int findTargetSumWays(int[] nums, int target) {
        int n = nums.length;
        int sum = 0;
        for (int i : nums) sum += Math.abs(i);
        if (target > sum || (sum - target) % 2 != 0) {
            return 0;
        }
        int m = (sum - target) / 2;

        // 问题转化为：选出 xxx 个元素，不可重复选，使这些元素的和为 m，求方案总数
        int[][] dp = new int[n + 1][m + 1];  // dp[i][j] 为从数组nums中 0 - i 的元素进行 累加 可得到 j 的方法数量
        dp[0][0] = 1;
        for (int i = 1; i <= n; i++) {
            int x = nums[i - 1];
            for (int j = 0; j <= m; j++) {
                dp[i][j] += dp[i - 1][j];
                if (j >= x) {
                    dp[i][j] += dp[i - 1][j - x];
                }
            }
        }
        return dp[n][m];
    }

    // 一维
    public int findTargetSumWays0(int[] nums, int target) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        if (target > sum || (target + sum) % 2 == 1) {
            return 0;
        }
        int m = (sum - target) / 2;
        int[] f = new int[m + 1];
        f[0] = 1;
        for (int num : nums) {
            for (int j = m; j >= num; j--) {
                f[j] = f[j] + f[j - num];
            }
        }
        return f[m];
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
     * --------------------------------------- 完全背包问题 ---------------------------------------<p>
     *
     * 完全背包问题，元素允许重用<br>
     */

    /*
     * 279. 完全平方数
     * 给定正整数 n，找到若干个完全平方数（比如 1, 4, 9, 16, ...）使得它们的和等于 n。你需要让组成和的完全平方数的个数最少，求最少数目
     * 动态规划，完全背包问题
     *
     * 问题转化为：
     * 从数组中选出 xxx 个元素，可重复选，使这些元素的和为 n，最少选多少元素，求数量
     */
    public int numSquares(int n) {
        int[] f = new int[n + 1];  // f[i] 表示和为i的情况下，最小选多少元素
        Arrays.fill(f, 0x3f3f3f3f);
        f[0] = 0;
        for (int i = 1; i * i <= n; i++) {
            int k = i * i;
            for (int x = k; x <= n; x++) {
                f[x] = Math.min(f[x], f[x - k] + 1);  // 需要参考本轮结果，所以"从前往后"遍历！元素的重用也体现在此处！此处就是 0-1 背包与完全背包的区别！
            }
        }
        return f[n];
    }

    /*
     * 1449. 数位成本和为目标值的最大数字
     * 从数组中选出 xxx 个"数位(1-9)"（可以重复选），每个"数位 i + 1"有自己的代价 cost[i]，代价之和需要等于target。现要求这些数位组成的数最大，求最大数
     *
     * e.g.
     * cost = [4,3,2,5,6,7,2,5,5], target = 9
     * -> 7772
     * 解释：
     * 1 -> 4
     * 2 -> 3
     * 3 -> 2
     * ...
     *
     * 数位7的成本是2，数位2的成本是3，故我们可选 2, 7, 7, 7 总成本 = 2 * 3 + 3 = 9；拼凑成的最大数字为"7772"
     * "977" 也是满足要求的数字，但 "7772" 是较大的数字
     *
     * 动态规划，完全背包问题
     *
     * 问题转化为：
     * 从9个数位中选出xx个，使代价恰等于target；在符合要求的方案中，求数位组成的最大的数
     */
    public String largestNumber3(int[] cost, int target) {
        int[] f = new int[target + 1];  // f[i]表示当和为i时，需要多少个数字
        // 这块需要填MIN_VALUE，因为会偶尔 +1 +1
        Arrays.fill(f, Integer.MIN_VALUE);
        f[0] = 0;
        for (int i = 0; i < 9; i++) {
            // 一个一个数字过
            int u = cost[i];
            for (int j = u; j <= target; j++) {  // 完全背包问题，可以重用，故正序
                f[j] = Math.max(f[j], f[j - u] + 1);
            }
        }
        if (f[target] < 0) return "0";
        StringBuilder sb = new StringBuilder();
        int j = target;
        for (int i = 9; i > 0; i--) {
            // 判断，第i位可以作为ans中的下一位吗？由于只能 99887 或 99865 递减，所以不用发愁i又来了
            int u = cost[i - 1];
            while (j >= u && f[j] == f[j - u] + 1) {  // 秀儿；此处为true时，表示f[j]这个状态，可以从f[j - u]通过 使用第i位 转移过来
                sb.append(i);
                j = j - u;
            }
        }
        return sb.toString();
    }
}
