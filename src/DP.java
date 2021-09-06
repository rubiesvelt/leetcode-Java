import java.util.*;

public class DP {

    /**
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

    // 940. 不同的子序列 II
    // 给定一个字符串 S，计算 S 的"不同"非空子序列的个数，
    // 如 "aba" -> 6 ("a", "b", "ab", "ba", "aa", "aba")
    public int distinctSubseqII(String S) {
        int MOD = 1_000_000_007;
        int N = S.length();
        int[] dp = new int[N + 1];  // dp[i]表示，考虑前i个数（下标 0 - i-1）不同非空子序列个数
        dp[0] = 1;
        int[] last = new int[26];  // last[i]（i为当前字符与'a'的差值）表示"上一次"以"该字符"结尾时，获得了多少增益
        Arrays.fill(last, -1);

        for (int i = 0; i < N; ++i) {
            int x = S.charAt(i) - 'a';
            dp[i + 1] = dp[i] * 2 % MOD;
            if (last[x] >= 0) {
                dp[i + 1] -= dp[last[x]];
            }
            dp[i + 1] %= MOD;
            last[x] = i;
        }

        dp[N]--;
        if (dp[N] < 0) dp[N] += MOD;
        return dp[N];
    }

    // 5857. 不同的好子序列数目
    // 给出一个以 0, 1 组成的String，求 "好子序列" 的数目。好子序列，即以1开头的子序列，如 "1", "101" 等
    public int numberOfUniqueGoodSubsequences(String binary) {
        int n = binary.length();
        int dp0 = 0, dp1 = 0;  // 以0，1开头的子序列的数目
        int has0 = 0;
        int MOD = (int) (1e9 + 7);
        // 0 0 0
        for (int i = n - 1; i >= 0; i--) {
            if (binary.charAt(i) == '0') {
                has0 = 1;
                dp0 = (dp0 + dp1 + 1) % MOD;
            } else {
                dp1 = (dp0 + dp1 + 1) % MOD;
            }
        }
        return dp1 + has0;
    }

    /*
     * 1986. 完成任务的最少工作时间段
     * 你被安排了 n 个任务。任务需要花费的时间用长度为 n 的整数数组tasks表示，第 i 个任务需要花费tasks[i]小时完成
     * 一个 工作时间段中，你可以 至多连续工作 sessionTime 个小时
     * 求最少需要多少时间段，能够完成所有任务
     *
     * [2,10,1,10,4,4,7,10,7,4,10,2] -> [10,10,10,10,7,7,4,4,4,2,2,1]
     * [13,14,14,1,1,2]
     * 15
     *
     * 答案为5 而不是6
     * 若第一次取 10 + 4 + 1 那后面 7 + 7就没法安排，多出个2
     *
     * 状压dp
     */
    public int minSessions(int[] tasks, int sessionTime) {
        int n = tasks.length;
        int m = 1 << n;
        final int INF = 20;
        int[] dp = new int[m];  // dp[i] 为选择状态为i(二进制)时，最小的分段数目
        Arrays.fill(dp, INF);

        // 预处理每个状态，合法状态预设为 1
        for (int i = 1; i < m; i++) {
            int state = i;
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
                // 111(3) 或 110(1) + 001(1)
                dp[i] = Math.min(dp[i], dp[j] + dp[i ^ j]);  // ^ 为 异或
            }
        }

        return dp[m - 1];
    }

     /*
     * 化为多次 0-1 背包问题 —— 不可取
     * 可以通过贪心得到最优解，但当背包中元素最多当时候，不能确认都装了哪些东西
     *
     * 先入为主策略
     * 15 -> 7,4,4
     * 14 -> 10,4
     *
     * 后来居上策略
     * 15 -> 10,2,2,1
     * 14 -> 10,2,2
     *
     * -> 到最后一个元素的时候，实行后来居上策略
     *
     * [2,10,1,10,4,4,7,10,7,4,10,2] -> [10,10,10,10,7,7,4,4,4,2,2,1]
     * 15
     * 答案为5 而不是6
     *
     * 完全没法控制 先入为主 或者 后来居上
     *
     *
     * [2,3,3,4,4,4,6,7,8,9,10]
     * 10, 3, 2
     * [3,4,4,4,6,7,8,9]
     * 9,6
     * 8,7
     * [3,4,4,4]
     *
     */
    public int minSessions1(int[] oritasks, int sessionTime) {
        int cnt = 0;
        int n = oritasks.length;
        int left = oritasks.length;
        int[] tasks = new int[oritasks.length];
        Arrays.sort(oritasks);
        for (int i = 0; i < tasks.length; i++) {
            tasks[i] = oritasks[tasks.length - i - 1];
        }
        while (left > 0) {
            Block[] f = new Block[sessionTime + 1];  // f[i] 表示元素和不超过 i 的时候，背包中装的物品最大和; f[i]永远小于i
            for (int i = 0; i < sessionTime + 1; i++) {
                f[i] = new Block();
            }
            for (int i = 0; i < n; i++) {
                if (i == n - 1) {
                    if (tasks[i] == 0) {
                        continue;
                    }
                    for (int j = sessionTime; j >= tasks[i]; j--) {
                        if (f[j].total > f[j - tasks[i]].total + tasks[i]) {  // 后来居上策略
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
                    if (f[j].total >= f[j - tasks[i]].total + tasks[i]) {  // 先入为主
                        continue;
                    }
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

    // 139. 单词拆分
    // 给定一个非空字符串 s 和一个包含非空单词的列表 wordDict，判定 s 是否可以被空格拆分为一个或多个在字典中出现的单词，单词可重复使用
    // 经典动态规划
    public boolean wordBreak(String s, List<String> wordDict) {
        Set<String> wordSet = new HashSet<>();  // wordDict允许复用，所以用Set去重
        int maxWordLength = 0;
        for (String str : wordDict) {
            wordSet.add(str);
            if (str.length() > maxWordLength) {
                maxWordLength = str.length();
            }
        }
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int i = 0; i <= s.length(); i++) {
            for (int j = (Math.max(i - maxWordLength, 0)); j < i; j++) {
                if (dp[j] && wordSet.contains(s.substring(j, i))) {  // substring(j, i)含j不含i
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[s.length()];
    }

    // BFS
    public static boolean wordBreakBfs(String s, List<String> wordDict) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);

        int slength = s.length();
        boolean[] visited = new boolean[slength + 1];

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int start = queue.poll();
                for (String word : wordDict) {
                    int nextStart = start + word.length();
                    if (nextStart > slength || visited[nextStart]) {
                        continue;
                    }
                    if (s.indexOf(word, start) == start) {
                        if (nextStart == slength) {
                            return true;
                        }
                        queue.add(nextStart);
                        visited[nextStart] = true;
                    }
                }
            }
        }
        return false;
    }

    // DFS
    public boolean wordBreakDfs(String s, List<String> wordDict) {
        boolean[] visited = new boolean[s.length() + 1];
        return dfsWordBreak(s, 0, wordDict, visited);
    }

    private boolean dfsWordBreak(String s, int start, List<String> wordDict, boolean[] visited) {
        for (String word : wordDict) {
            int nextStart = start + word.length();
            if (nextStart > s.length() || visited[nextStart]) {
                continue;
            }
            if (s.indexOf(word, start) == start) {  // index of returns the index within this string of the first occurrence of the specified substring, starting at the specified index.
                if (nextStart == s.length() || dfsWordBreak(s, nextStart, wordDict, visited)) {
                    return true;
                }
                visited[nextStart] = true;
            }
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
            long[] buff = new long[n];  // buff 表示下一行可以从上一行收获的"最大增益"
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


    // 300. 最长递增子序列 —— LIS问题

    /**
     * 最长公共子序列 —— LCS
     */

    // LCP 19. 秋叶收藏集
    /*
    动态规划：通过增加维度，来全面规划问题。
    某一个状态的值，往往是问题的解
    都是有状态的
    某一个状态，可以参考前面的状态，叫做"状态转移"
     */
    public int minimumOperations(String leaves) {
        int n = leaves.length();
        int[][] f = new int[n][3];
        // 第一个元素一定为0
        f[0][0] = leaves.charAt(0) == 'y' ? 1 : 0;
        f[0][1] = f[0][2] = f[1][2] = Integer.MAX_VALUE;

        /*
        f[i][j]表示：从头开始规整第i个元素至j状态时，所需要调整的“最少”叶子数
        状态
        j = 0 处于初级r状态
        j = 1 处于中间y状态
        j = 2 处于后段r状态
        状态转移方程：
        f[i][0] = f[i-1][0] + 调整r
        f[i][1] = min( f[i-1][0], f[i-1][1] ) + 调整y
        f[i][2] = min( f[i-1][1], f[i-1][2] ) + 调整r

            r r r y y y r y r r y
        [0] 0 0 0 1 2 3 3 4 4 x x
        [1] x 1 1 0 0 0 1 1 2 3 x
        [2] x x 1 2 1 1 0 1 1 1 2

         */
        for (int i = 1; i < n; ++i) {
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

    // 1269. 停在原地的方案数
    // 动态规划
    int mod = (int) 1e9 + 7;

    public int numWays(int steps, int len) {
        int max = Math.min(steps / 2, len - 1);
        // 定义 f[i][j] 代表当前剩余操作数为 i，所在位置为 j 的所有方案数
        int[][] f = new int[steps + 1][max + 1];
        f[steps][0] = 1;
        for (int i = steps - 1; i >= 0; i--) {
            int edge = Math.min(i, max);
            for (int j = 0; j <= edge; j++) {
                // 通过"留在原地"得到
                f[i][j] = (f[i][j] + f[i + 1][j]) % mod;
                // 从j - 1右移得到
                if (j - 1 >= 0) f[i][j] = (f[i][j] + f[i + 1][j - 1]) % mod;
                // 从j + 1左移得到
                if (j + 1 <= max) f[i][j] = (f[i][j] + f[i + 1][j + 1]) % mod;
            }
        }
        return f[0][0];
    }

    // 377. 组合总和 Ⅳ
    // 动态规划
    public int combinationSum4(int[] nums, int target) {
        // dfs会超时
        // 使用dp数组，dp[i]代表组合数为i时使用nums中的数能组成的组合数的个数
        // 别怪我写的这么完整
        // dp[i]=dp[i-nums[0]]+dp[i-nums[1]]+dp[i=nums[2]]+...
        // 举个例子比如nums=[1,3,4],target=7;
        // dp[7]=dp[6]+dp[4]+dp[3]
        // 其实就是说7的组合数可以由三部分组成，1和dp[6]，3和dp[4],4和dp[3];
        int[] dp = new int[target + 1];
        // 是为了算上自己的情况，比如dp[1]可以由dp[0]和1这个数的这种情况组成。
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
    // 丑数 就是只包含质因数 2、3 和/或 5 的正整数
    public static int nthUglyNumber2(int n) {
        List<Long> list = new ArrayList<>();
        list.add(1L);
        int index_2 = 0;
        int index_3 = 0;
        int index_5 = 0;
        long min = 1;
        for (int i = 1; i < n; i++) {
            long min_2_3 = Math.min(2 * list.get(index_2), 3 * list.get(index_3));
            min = Math.min(min_2_3, 5 * list.get(index_5));
            list.add(i, min);
            if (min == 2 * list.get(index_2)) index_2++;
            if (min == 3 * list.get(index_3)) index_3++;  // 此处不使用 else if，可达到去重效果
            if (min == 5 * list.get(index_5)) index_5++;
        }
        return (int) min;
    }

    // 91. 解码方法
    // 动态规划
    public static int numDecodings(String s) {
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
