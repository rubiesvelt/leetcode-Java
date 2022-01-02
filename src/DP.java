import java.util.*;

public class DP {

    /**
     * 发生过的事情不会改变，但会对未来造成影响
     *
     * <p>
     * 经典动态规划
     * <p>
     * 最长递增子序列 LIS
     * 最长公共子序列 LCS
     * 单词拆分
     * 打家劫舍
     * 背包问题
     * 回文字串
     * 区间DP
     */

    /*
     * 343. 整数拆分
     */
    public int integerBreak(int n) {
        int[] f = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j < i; j++) {
                f[i] = Math.max(f[i], Math.max((i - j) * f[j], (i - j) * j));
            }
        }
        return f[n];
    }

    /*
     * 1567. 乘积为正数的最长子数组长度
     */
    public int getMaxLen(int[] nums) {
        int pos = 0;  // 选当前位 最长长度 乘积为正
        int neg = 0;  // 选当前位 最长长度 乘积为负
        int ans = 0;
        for (int n : nums) {
            if (n > 0) {
                pos++;
                if (neg > 0) neg++;
            } else if (n < 0) {
                int negBak = neg;
                neg = pos + 1;

                if (negBak > 0) {
                    pos = negBak + 1;
                } else {
                    pos = 0;
                }
            } else {
                pos = 0;
                neg = 0;
            }
            ans = Math.max(ans, pos);
        }
        return ans;
    }

    /*
     * 152. 乘积最大子数组
     */
    public int maxProduct(int[] nums) {
        int ans = Integer.MIN_VALUE;
        int n = nums.length;
        if (n == 1) return nums[0];
        int pos = -1;  // 选该位时 的正最大值
        int neg = 1;  // 选该位时 的负最小值
        int max = Integer.MIN_VALUE;
        for (int t : nums) {
            max = Math.max(max, t);
            if (t > 0) {
                if (pos != -1) pos *= t;
                else pos = t;

                if (neg != 1) neg *= t;
            } else if (t < 0) {
                int negBak = neg;

                if (pos != -1) neg = pos * t;
                else neg = t;

                if (negBak != 1) pos = negBak * t;
                else pos = -1;
            } else {
                pos = -1;
                neg = 1;
            }
            if (pos > 0) ans = Math.max(ans, pos);
        }
        if (ans < 0) {
            ans = max;
        }
        return ans;
    }

    /*
     * 918. 环形子数组的最大和
     */
    public int maxSubarraySumCircular(int[] nums) {
        int n = nums.length;
        int t = 0;
        int ans1 = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            if (t > 0) t += nums[i];
            else {
                t = nums[i];
            }
            ans1 = Math.max(ans1, t);
        }
        t = 0;
        int acc = 0;
        int u = Integer.MAX_VALUE;
        boolean hasZero = false;
        for (int i = 0; i < n; i++) {
            if (t < 0) t += nums[i];
            else {
                t = nums[i];
            }
            acc += nums[i];
            if (nums[i] == 0) hasZero = true;
            u = Math.min(u, t);
        }
        int ans2 = acc - u;
        if (ans2 == 0 && !hasZero) ans2 = Integer.MIN_VALUE;
        return Math.max(ans1, ans2);
    }

    /*
     * 53. 最大子序和
     */
    public int maxSubArray(int[] nums) {
        int n = nums.length;
        int t = 0;
        int ans = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            if (t > 0) t += nums[i];
            else {
                t = nums[i];
            }
            ans = Math.max(ans, t);
        }
        return ans;
    }

    /*
     * 55. 跳跃游戏 II
     * 给出 nums[] 数组，在 i 位置最多能向前走 nums[i] 距离
     * 初始在 i = 0 位置，最终都可以到达 i = n - 1 位置，求最终到达该位置时的 最小跳跃次数
     *
     * e.g.
     * [2,3,1,1,4]
     * -> 2   (2 -> 3 -> 4)
     *
     * [2,3,0,1,4]
     * -> 2   (2 -> 3 -> 4)
     *
     *
     */
    public int jump1(int[] nums) {
        int n = nums.length;
        if (n == 1) return 0;
        int arr = 0;
        int nextArr = 0;
        int step = 0;
        for (int i = 0; i < n; i++) {
            nextArr = Math.max(nextArr, i + nums[i]);
            if (nextArr >= n - 1) return step + 1;
            if (i == arr) {
                step++;
                arr = nextArr;
            }
        }
        return 0;
    }

    /*
     * 反复遍历的低效版本
     */
    public int jump0(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 0x3f3f3f3f);
        dp[0] = 0;
        int max = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < i + nums[i]; j++) {
                if (j >= n) break;
                dp[j] = Math.min(dp[j], dp[i] + 1);
            }
        }
        return dp[n - 1];
    }

    /*
     * 55. 跳跃游戏
     * 给出 nums[] 数组，在 i 位置最多能向前走 nums[i] 距离
     * 初始在 i = 0 位置，求最终是否能到达 i = n - 1 位置
     *
     * e.g.
     * nums = [2,3,1,1,4]
     * -> true
     *
     * nums = [3,2,1,0,4]
     * -> false
     */
    public boolean canJump(int[] nums) {
        int n = nums.length;
        int reachable = 0;
        int cur = 0;
        while (cur <= reachable) {
            reachable = Math.max(reachable, cur + nums[cur]);
            cur++;
            if (reachable >= n - 1) return true;
        }
        return false;
    }

    // 1937. 扣分后的最大得分
    // "左右"dp
    // 时间复杂度O(mn)
    public long maxPoints1(int[][] points) {
        int m = points.length;
        int n = points[0].length;
        long[] dp = new long[n];  // dp[] 存储到当前行的结果
        for (int i = 0; i < m; i++) {
            long[] buff = new long[n];  // buff 表示 下一行 可以从 上一行 收获的"最大增益"
            // 求增益，左扫描dp一遍，右扫描dp一遍，太秀了
            long lmax = 0;
            for (int j = 0; j < n; j++) {
                lmax = Math.max(lmax - 1, dp[j]);
                buff[j] = lmax;
            }
            long rmax = 0;
            for (int j = n - 1; j >= 0; j--) {
                rmax = Math.max(rmax - 1, dp[j]);
                buff[j] = Math.max(buff[j], rmax);
            }

            // 更新dp[]，上一行 -> 当前行
            for (int j = 0; j < n; j++) {
                dp[j] = buff[j] + points[i][j];
            }
        }
        long ans = 0;
        for (int j = 0; j < n; j++) {
            ans = Math.max(ans, dp[j]);
        }
        return ans;
    }

    // 朴素dp —— TLE
    // 时间复杂度O(m n^2)
    public long maxPoints0(int[][] points) {
        int m = points.length;
        int n = points[0].length;
        // dp[i][j]表示，若选取第i行第j列，所得到的最大结果是多少
        // dp[i + 1][j] = dp[i][x] - Math.abs(j - x) —— x 取 0 - n，取最大结果
        long[][] dp = new long[m][n];
        for (int i = 0; i < n; i++) {
            dp[0][i] = points[0][i];
        }
        for (int i = 1; i < m; i++) {
            for (int j = 0; j < n; j++) {
                long max = Long.MIN_VALUE;
                for (int k = 0; k < n; k++) {
                    long t = dp[i - 1][k] - Math.abs(j - k);
                    max = Math.max(max, t);
                }
                dp[i][j] = max + points[i][j];
            }
        }
        long ans = Long.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            ans = Math.max(ans, dp[m - 1][i]);
        }
        return ans;
    }

    // 记忆化搜索（TLE）
    public long maxPoints(int[][] points) {
        int m = points.length;
        int n = points[0].length;
        maxPointsDfs(points, m, n, 0, -1, 0);
        return ans1937;
    }

    long ans1937 = 0;

    // 记忆map
    Map<String, Long> map = new HashMap<>();

    // 记忆化搜索
    public void maxPointsDfs(int[][] points, int m, int n, int height, int index, long sum) {
        if (height == m) {
            ans1937 = Math.max(ans1937, sum);
            return;
        }
        int[] row = points[height];
        for (int i = 0; i < n; i++) {
            long subtract = index == -1 ? 0 : Math.abs(i - index);
            long t = sum + row[i] - subtract;

            // 记忆化
            String key = height + "_" + i;
            if (map.getOrDefault(key, Long.MIN_VALUE) >= t) {
                continue;
            }
            map.put(key, t);

            maxPointsDfs(points, m, n, height + 1, i, t);
        }
    }

    /*
     * LCP 19. 秋叶收藏集
     * leaves为小写字符 r 和 y 组成的字符串
     * 每一次操作可以将一个 r 变成 y，或者将一个 y 变成 r，
     * 现需要将收藏集中树叶的排列调整成「红、黄、红」三部分，求最少操作次数
     *
     * 动态规划：通过增加维度，来全面规划问题
     * 某一个状态的值，往往是问题的解
     * 都是有状态的
     * 某一个状态，可以参考前面的状态，叫做"状态转移"
     */
    public int minimumOperations(String leaves) {
        int n = leaves.length();
        int[][] f = new int[n][3];
        /*
         * f[i][j]表示：从头开始规整第 i 个元素 至 j 状态时，所需要调整的"最少"叶子数
         * 状态
         * j = 0 处于初级r状态
         * j = 1 处于中间y状态
         * j = 2 处于后段r状态
         * 状态转移方程：
         * f[i][0] = f[i-1][0] + 调整r
         * f[i][1] = min( f[i-1][0], f[i-1][1] ) + 调整y
         * f[i][2] = min( f[i-1][1], f[i-1][2] ) + 调整r
         *
         * e.g.
         *     r r r y y y r y r r y
         * [0] 0 0 0 1 2 3 3 4 4 x x   -> r... 看r
         * [1] x 1 1 0 0 0 1 1 2 3 x   -> r...y... 看y —— 如果是y，可以从左上 + 0，或者左 + 0；如果是r，可以从左上 + 1，或者左 + 1
         * [2] x x 1 2 1 1 0 1 1 1 2   -> r...y...r... 看r
         *
         */

        // 第一个元素一定为0
        f[0][0] = leaves.charAt(0) == 'y' ? 1 : 0;
        f[0][1] = f[0][2] = f[1][2] = Integer.MAX_VALUE;

        for (int i = 1; i < n; i++) {
            int isRed = leaves.charAt(i) == 'r' ? 1 : 0;
            int isYellow = leaves.charAt(i) == 'y' ? 1 : 0;
            f[i][0] = f[i - 1][0] + isYellow;
            f[i][1] = Math.min(f[i - 1][0], f[i - 1][1]) + isRed;
            if (i >= 2) {
                f[i][2] = Math.min(f[i - 1][1], f[i - 1][2]) + isYellow;
            }
        }
        return f[n - 1][2];
    }

    /*
     * 1269. 停在原地的方案数
     * 有一个长度为 arrLen 的数组，开始有一个指针在索引 0 处
     * 每一步操作中，你可以将指针向左或向右移动 1 步，或者停在原地，指针不能被移动到数组范围外
     * 给你两个整数 steps 和 arrLen ，请你计算并返回：在恰好执行 steps 次操作以后，指针仍然指向索引 0 处的方案数
     *
     * e.g.
     * steps = 3, arrLen = 2
     * -> 4
     * 3 步后，总共有 4 种不同的方法可以停在索引 0 处
     * 向右，向左，不动
     * 不动，向右，向左
     * 向右，不动，向左
     * 不动，不动，不动
     *
     * 动态规划
     */
    int MOD = (int) 1e9 + 7;

    public int numWays(int steps, int len) {
        int max = Math.min(steps / 2, len - 1);  // max 为可以到达的右边界
        int[][] dp = new int[steps + 1][max + 1];  // dp[i][j] 代表当前剩余操作数为 i，所在位置为 j 的所有方案数
        dp[steps][0] = 1;
        for (int i = steps - 1; i >= 0; i--) {
            int edge = Math.min(i, max);
            for (int j = 0; j <= edge; j++) {  // j从0往上吗，为什么不从高往低？也可以从高往低，因为本轮低结果只与上轮有关
                // 通过"留在原地"得到
                dp[i][j] = (dp[i][j] + dp[i + 1][j]) % MOD;
                // 从 j - 1 右移得到
                if (j - 1 >= 0) dp[i][j] = (dp[i][j] + dp[i + 1][j - 1]) % MOD;
                // 从 j + 1 左移得到
                if (j + 1 <= max) dp[i][j] = (dp[i][j] + dp[i + 1][j + 1]) % MOD;
            }
        }
        return dp[0][0];
    }

    /*
     * 377. 组合总和 Ⅳ
     * 给你一个由 不同 整数组成的数组 nums ，和一个目标整数 target
     * 请你从 nums 中找出并返回总和为 target 的元素组合的个数
     *
     * e.g.
     * nums = [1,2,3], target = 4
     * -> 7
     * 所有可能的组合为：
     * (1, 1, 1, 1)
     * (1, 1, 2)
     * (1, 2, 1)
     * (1, 3)
     * (2, 1, 1)
     * (2, 2)
     * (3, 1)
     * 注，顺序不同的序列被视作不同的组合
     *
     * 并非完全背包问题，易迷惑
     */
    public int combinationSum(int[] nums, int target) {
        /*
         * dp[i] 代表目标为 i 时使用 nums 中的数能组成的组合数的个数
         * dp[i] = dp[i - nums[0]] + dp[i - nums[1]] + dp[i - nums[2]] + ...
         *
         * 如 nums=[1, 3, 4], target = 7;
         * dp[7] = dp[6] + dp[4] + dp[3];
         * 其实就是说 7 的组合数可以由三部分组成, 1 和 dp[6]，3 和 dp[4], 4 和 dp[3];
         *
         * [1, 2]
         *
         * dp[2] ->
         * 1, 1
         * 2
         *
         * dp[3] ->
         * 1, 1, 1
         * 1, 2
         * 2, 1
         */
        int[] dp = new int[target + 1];
        // 是为了算上自己的情况，比如 dp[1] 可以由 dp[0] 和 1 这个数的这种情况组成
        dp[0] = 1;
        for (int i = 1; i <= target; i++) {
            for (int num : nums) {
                if (i >= num) {
                    dp[i] += dp[i - num];
                }
            }
        }
        return dp[target];
    }

    // 368. 最大整除子集
    // 动态规划
    public static List<Integer> largestDivisibleSubset(int[] nums) {
        Arrays.sort(nums);
        int[] dp = new int[nums.length];
        int[] g = new int[nums.length];
        dp[0] = 0;
        g[0] = -1;
        for (int i = 0; i < nums.length; i++) {
            int num = 0;
            int past = -1;
            for (int j = 0; j < i; j++) {
                if (nums[i] % nums[j] == 0) {
                    if (dp[j] > num) {
                        num = dp[j];
                        past = j;
                    }
                }
            }
            dp[i] = num + 1;
            g[i] = past;
        }
        int max = 0;
        int index = 0;
        for (int i = 0; i < dp.length; i++) {
            if (dp[i] > max) {
                max = dp[i];
                index = i;
            }
        }
        List<Integer> ans = new ArrayList<>();
        while (index > -1) {
            ans.add(nums[index]);
            index = g[index];
        }
        return ans;
    }

    // 264. 丑数Ⅱ
    // 动态规划，三指针
    // 丑数 就是只包含质因数 2、3 和/或 5 的正整数，返回第 n 个丑数
    // 1，2，3，4，5，6，8，9，10
    public int nthUglyNumber2(int n) {
        List<Long> list = new ArrayList<>();
        list.add(1L);
        int index_2 = 0;
        int index_3 = 0;
        int index_5 = 0;
        long min = 1;
        for (int i = 1; i < n; i++) {
            long min_2_3 = Math.min(2 * list.get(index_2), 3 * list.get(index_3));
            min = Math.min(min_2_3, 5 * list.get(index_5));
            list.add(min);
            if (min == 2 * list.get(index_2)) index_2++;
            if (min == 3 * list.get(index_3)) index_3++;  // 此处不使用 else if，可达到去重效果
            if (min == 5 * list.get(index_5)) index_5++;
        }
        return (int) min;
    }

    // 91. 解码方法
    // 动态规划
    public int numDecodings(String s) {
        int n = s.length();
        /*
         * 涉及到前两个状态的动态规划，可以添加哨兵，来简化逻辑
         * ' ' - '0' = -150
         */
        s = " " + s;
        char[] cs = s.toCharArray();  // String.toCharArray() 转换String到字符数组
        int[] f = new int[n + 1];
        f[0] = 1;
        for (int i = 1; i <= n; i++) {
            // a : 代表「当前位置」单独形成 item
            // b : 代表「当前位置」与「前一位置」共同形成 item
            int a = cs[i] - '0';
            int b = (cs[i - 1] - '0') * 10 + (cs[i] - '0');
            // 如果 a 属于有效值，那么 f[i] 可以由 f[i - 1] 转移过来
            if (1 <= a && a <= 9) f[i] = f[i - 1];
            // 如果 b 属于有效值，那么 f[i] 可以由 f[i - 2] 或者 f[i - 1] & f[i - 2] 转移过来
            if (10 <= b && b <= 26) f[i] += f[i - 2];
        }
        return f[n];
    }
}