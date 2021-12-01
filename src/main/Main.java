package main;

import beans.ListNode;
import beans.TreeNode;

import java.util.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        List<Integer> list = new ArrayList<>();
        int t = list.hashCode();
        Main main = new Main();
        char a = 'a';
        int i = 96;
        TreeNode tn5 = new TreeNode(5);
        TreeNode tn4 = new TreeNode(4);
        TreeNode tn3 = new TreeNode(3, tn5, null);
        TreeNode tn2 = new TreeNode(2, tn4, null);
        TreeNode tn1 = new TreeNode(1, tn2, tn3);

        String s = "***|**|*****|**||**|*";
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int[][] matrix1 = {{22, 44, 9}, {93, 96, 48}, {56, 90, 3}, {80, 92, 45}, {63, 73, 69}, {73, 96, 33}, {11, 23, 84}, {59, 72, 29}, {89, 100, 46}};
        int[] dist = {25, 11, 29, 6, 24, 4, 29, 18, 6, 13, 25, 30};
        int[] diff = {10, 9, 2, 5, 3, 7, 101, 18};
        int[] speed = {-5, -2, 5, 6, -2, -7, 0, 2, 8};
        char[][] matrix2 = {{'.', '.', 'W', '.', 'B', 'W', 'W', 'B'}, {'B', 'W', '.', 'W', '.', 'W', 'B', 'B'}, {'.', 'W', 'B', 'W', 'W', '.', 'W', 'W'}, {'W', 'W', '.', 'W', '.', '.', 'B', 'B'}, {'B', 'W', 'B', 'B', 'W', 'W', 'B', '.'}, {'W', '.', 'W', '.', '.', 'B', 'W', 'W'}, {'B', '.', 'B', 'B', '.', '.', 'B', 'B'}, {'.', 'W', '.', 'W', '.', 'W', '.', 'W'}};

        main.kMirror(2, 5);
        return;
    }

    /*
     * 279. 完全平方数
     * 典型完全背包的方法
     */
    public int numSquares(int n) {
        int[] f = new int[n + 1];
        Arrays.fill(f, 0x3f3f3f3f);
        f[0] = 0;
        for (int i = 1; i * i <= n; i++) {  // 背包问题先过 东西
            int k = i * i;
            // 完全背包，可复用，所以从小到大
            for (int x = k; x <= n; x++) {  // 再过 总和
                f[x] = Math.min(f[x], f[x - k] + 1);
                // 如果f[x]已经越界，就没必要判断f[x]后面的了
            }
        }
        return f[n];
    }

    /*
     * 非典型完全背包的方法
     */
    public int numSquares0(int n) {
        int[] f = new int[n + 1];
        Arrays.fill(f, 0x3f3f3f3f);
        f[0] = 0;
        for (int i = 1; i <= n; i++) {  // 先过总和
            for (int j = 1; j <= n; j++) {  // 再过东西
                int t = j * j;
                if (t > i) break;
                f[i] = Math.min(f[i], f[i - t] + 1);
            }
        }
        return f[n];
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
     * 并非 完全背包问题，易迷惑
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
        for (int i = 1; i <= target; i++) {  // 先过总和
            for (int num : nums) {  // 再过东西
                if (i >= num) {
                    dp[i] += dp[i - num];
                }
            }
        }
        return dp[target];
    }

    /*
     * 518. 零钱兑换 II
     *
     * 给定零钱 coins[] 和目标和 amount，零钱可以重用，求凑成 amount 有多少种零钱方案
     *
     * 典型的 完全背包问题
     * 一维
     */
    public int change(int amount, int[] coins) {
        int[] f = new int[amount + 1];  // f[i] 表示和为 i 时的方案数
        f[0] = 1;
        for (int coin : coins) {  // 先过 东西，再过 总和
            for (int j = coin; j <= amount; j++) {  // 完全背包，从前往后
                f[j] = f[j] + f[j - coin];
            }
        }
        return f[amount];
    }

    // 二维
    public int change0(int amount, int[] coins) {
        int n = coins.length;
        // dp[i][j] 为前i个数组成j的组合数
        int[][] dp = new int[n][amount + 1];
        /*
         * dp[i][j]
         * =
         * dp[i - 1][j] +
         * dp[i - 1][j - coins[i]] +
         * dp[i - 1][j - 2 * coins[i]] +
         * ... (使用第i个数时能有多少种方法和为j)
         */
        for (int i = 0; i <= amount; i++) {
            if (i % coins[0] == 0) dp[0][i] = 1;
        }
        for (int i = 0; i < n; i++) {
            dp[i][0] = 1;
        }
        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= amount; j++) {
                int coin = coins[i];
                int cur = j;
                while (cur >= 0) {
                    dp[i][j] = dp[i - 1][cur];
                    cur -= coin;
                }
            }
        }
        return dp[n - 1][amount];
    }

    /*
     * 322. 零钱兑换
     */
    public int coinChange(int[] coins, int amount) {
        if (amount == 0) return 0;
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, 0x3f3f3f3f);
        for (int coin : coins) {
            if (coin > amount) continue;
            if (coin == amount) return 1;
            dp[coin] = 1;
        }
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin >= i) continue;
                dp[i] = Math.min(dp[coin] + dp[i - coin], dp[i]);
            }
        }
        return dp[amount] == 0x3f3f3f3f ? -1 : dp[amount];
    }

    /*
     * 2081. k 镜像数字的和
     */
    int numLeft;

    public long kMirror(int k, int n) {
        int ans = 0;
        numLeft = n;
        // 0  ->  k - 1
        Queue<String> queue = new LinkedList<>();
        for (int i = 1; i < k; i++) {
            String s = "";
            s += i;
            queue.add(s);
        }
        while (!queue.isEmpty()) {
            List<String> strings = new ArrayList<>();
            String cur = queue.poll();
            strings.add(cur);
            int curLen = cur.length();
            while (!queue.isEmpty() && queue.peek().length() == curLen) {
                String s = queue.poll();
                strings.add(s);
            }
            long t = findMirror(k, strings);
            if (t > 0) {
                ans += t;
                if (numLeft <= 0) {
                    return ans;
                }
            }
            for (String s : strings) {
                for (int i = 0; i < k; i++) {
                    String s1 = s + i;
                    queue.add(s1);
                }
            }
        }
        return 0;
    }

    public long findMirror(int k, List<String> strs) {
        List<String> st = new ArrayList<>();
        List<String> lg = new ArrayList<>();
        for (String str : strs) {
            int n = str.length();
            char c = str.charAt(n - 1);
            String s1 = str.substring(0, n - 1) + c + reverse(str.substring(0, n - 1));
            String s2 = str + reverse(str);
            st.add(s1);
            lg.add(s2);
        }
        long sum = 0;
        for (String s : st) {
            long t1 = calculate(k, s);
            if (t1 > 0) {
                numLeft--;
                sum += t1;
                if (numLeft == 0) {
                    return sum;
                }
            }
        }
        for (String s : lg) {
            long t1 = calculate(k, s);
            if (t1 > 0) {
                numLeft--;
                sum += t1;
                if (numLeft == 0) {
                    return sum;
                }
            }
        }
        return sum;
    }

    public String reverse(String s) {
        String ret = "";
        int n = s.length();
        for (int i = n - 1; i >= 0; i--) {
            ret += s.charAt(i);
        }
        return ret;
    }

    public long calculate(int k, String str) {
        int n = str.length();
        long res = 0;
        for (int i = 0; i < n; i++) {
            res *= k;
            res += str.charAt(i) - '0';
        }
        String strRes = "" + res;
        char[] cs = strRes.toCharArray();
        int l = 0;
        int r = strRes.length() - 1;
        while (l < r) {
            if (cs[l] != cs[r]) return 0;
            l++;
            r--;
        }
        return res;
    }

    /*
     * 72. 编辑距离
     *
     * word1 -> word2
     */
    public int minDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m + 1][n + 1];
        // 如果 dp[i][j] 从 dp[i-1][j-1], dp[i-1][j], dp[i][j-1] 推导
        // dp[i][j] 表示 word1 从 0 到 i，word2 到j 的子集需要的变化次数
        char[] cs1 = word1.toCharArray();
        char[] cs2 = word2.toCharArray();
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (cs1[i - 1] == cs2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // dp[i][j] 可从之前的状态，通过
                    // 添加 dp[i][j-1]
                    // 修改 dp[i-1][j-1]
                    // 删除 dp[i-1][j] 得到
                    int min = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]);
                    dp[i][j] = min + 1;
                }
            }
        }
        return dp[m][n];
    }

    /*
     * 1143. 最长公共子序列
     */
    public int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();
        int[][] dp = new int[m + 1][n + 1];
        // 如果 dp[i][j] 从 dp[i-1][j-1], dp[i-1][j], dp[i][j-1] 推导
        char[] cs1 = text1.toCharArray();
        char[] cs2 = text2.toCharArray();
        for (int i = 1; i < m + 1; i++) {
            for (int j = 1; j < n + 1; j++) {
                if (cs1[i - 1] == cs2[j - 1]) dp[i][j] = dp[i - 1][j - 1] + 1;
                else dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }
        return dp[m][n];
    }

    /*
     * 392. 判断子序列
     */
    public boolean isSubsequence(String s, String t) {
        int m = s.length();
        if (m == 0) return true;
        int n = t.length();
        int cur = 0;
        for (int i = 0; i < n; i++) {
            if (s.charAt(cur) == t.charAt(i)) {
                cur++;
            }
            if (cur == m) {
                return true;
            }
        }
        return false;
    }

    /*
     * 376. 摆动序列
     *
     * [1,17,5,10,13,15,10,5,16,8]
     * 当人上面, 当人下面
     * 1 -> 1,1
     * 17 -> 2,1
     * 5 -> 2,3
     * 10 -> 4,3
     * 13 -> 4,3
     * 15 -> 4,3
     * 10 -> 4,5
     * 5 -> 2,5
     * 16 -> 6,3
     * 8 -> 6,7
     */
    public int wiggleMaxLength(int[] nums) {
        int n = nums.length;
        if (n < 2) {
            return n;
        }
        int up = 1;
        int down = 1;
        for (int i = 1; i < n; i++) {
            if (nums[i] > nums[i - 1]) {
                up = down + 1;
            }
            if (nums[i] < nums[i - 1]) {
                down = up + 1;
            }
        }
        return Math.max(up, down);
    }

    /*
     * 5. 最长回文子串
     */
    // 动态规划
    public String longestPalindrome(String s) {
        int n = s.length();
        int max = -1;
        int l = 0, r = 0;
        boolean[][] dp = new boolean[n][n];  // dp[i][j] 为 [i, j) 子串 是否为回文字串
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i], true);
        }
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {  // i = n - 1 的时候，j 不会进入循环
                dp[i][j] = s.charAt(i) == s.charAt(j) && dp[i + 1][j - 1];  // 此处不用担心 i + 1越界，因为到不了这块
                if (dp[i][j]) {
                    if (j - i > max) {
                        max = j - i;
                        l = i;
                        r = j;
                    }
                }
            }
        }
        return s.substring(l, r + 1);
    }

    // 中心扩展法
    public String longestPalindrome1(String s) {
        if (s == null || s.length() < 1) return "";
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            int len1 = expandAroundCenter(s, i, i);  // aba
            int len2 = expandAroundCenter(s, i, i + 1);  // abba
            int len = Math.max(len1, len2);
            if (len > end - start) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        return s.substring(start, end + 1);
    }

    private int expandAroundCenter(String s, int left, int right) {
        int L = left, R = right;
        while (L >= 0 && R < s.length() && s.charAt(L) == s.charAt(R)) {
            L--;
            R++;
        }
        return R - L - 1;
    }

    /*
     * 221. 最大正方形
     */
    public int maximalSquare(char[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int ans = 0;
        for (int i = 0; i < m; i++) {
            if (matrix[i][0] == '1') {
                ans = 1;
                break;
            }
        }
        for (int j = 0; j < n; j++) {
            if (matrix[0][j] == '1') {
                ans = 1;
                break;
            }
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][j] == '0') continue;
                int a = matrix[i - 1][j - 1] - '0';
                int b = matrix[i - 1][j] - '0';
                int c = matrix[i][j - 1] - '0';
                int min = Math.min(a, b);
                min = Math.min(min, c);
                matrix[i][j] = (char) ('0' + min + 1);
                ans = Math.max(ans, matrix[i][j] - '0');
            }
        }
        return ans * ans;
    }

    /*
     * 64. 最小路径和
     */
    public int minPathSum(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        for (int i = 1; i < m; i++) {
            grid[i][0] += grid[i - 1][0];
        }
        for (int i = 1; i < n; i++) {
            grid[0][i] += grid[0][i - 1];
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                int min = Math.min(grid[i - 1][j], grid[i][j - 1]);
                grid[i][j] += min;
            }
        }
        return grid[m - 1][n - 1];
    }

    public String decodeCiphertext(String encodedText, int rows) {
        if (rows == 1) return encodedText;
        int n = encodedText.length();
        int col = n / rows;
        char[][] metric = new char[rows][col];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < col; j++) {
                metric[i][j] = encodedText.charAt(index);
                index++;
            }
        }
        int cur = 0;
        int k = col - rows + 2;
        String ans = "";
        while (cur < k) {
            for (int i = 0; i < rows; i++) {
                if (cur + i < col) ans += metric[i][cur + i];
            }
            cur++;
        }
        int q = ans.length() - 1;
        while (q >= 0 && ans.charAt(q) == ' ') {
            q--;
        }
        String ret = ans.substring(0, q + 1);
        return ret;
    }

    public int maxTaskAssign(int[] tasks, int[] workers, int pills, int strength) {
        // 每个工人最多一个药丸，提高 strength 点力量
        // 目标完成最多的工作。
        // 优先完成使用体力最低的工作；不可能存在 使用体力高的工作被完成了，同时使用体力低的工作没有完成
        // tasks = [10,11,12,13,14,15], workers = [6,11,12,13], pills = 1, strength = 5
        // tasks = [5,5,8,9,9], workers = [1,2,4,6,6], pills = 1, strength = 5
        int m = tasks.length;
        int n = workers.length;
        int[][] dp = new int[m][pills];  // dp[i][j] 表示已完成前 m 个任务，此时用了 pills 个药丸
        return 0;
    }

    public int[] maximumBeauty(int[][] items, int[] queries) {
        Arrays.sort(items, (o1, o2) -> o1[0] - o2[0]);  // 价格从小到大
        int max = Integer.MIN_VALUE;

        TreeSet<Integer> treeSet = new TreeSet<>();  // TreeSet yyds!!
        Map<Integer, Integer> map = new HashMap<>();
        for (int[] item : items) {
            max = Math.max(item[1], max);
            treeSet.add(item[0]);
            map.put(item[0], max);
        }
        int n = queries.length;
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            Integer index = treeSet.floor(queries[i]);
            int score = 0;
            if (index != null) {
                score = map.get(index);
            }
            ans[i] = score;
        }
        return ans;
    }

    /*
     * 63. 不同路径 II
     *
     * 深信服面试题. 机器人可移动的总方案数
     *
     * 给出一个二维数组 graph[][]，graph[i][j] = 0 表示畅通，graph[i][j] = 1 表示障碍
     * 机器人初始位置在图的左上角 (0, 0)，现需要移动到图的右下角，机器人只能往下或往右移动
     * 求有多少种不同的路线
     */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int n = obstacleGrid.length;
        int m = obstacleGrid[0].length;
        if (obstacleGrid[0][0] == 1 || obstacleGrid[n - 1][m - 1] == 1) return 0;
        int[][] dp = new int[n][m];  // dp[i][j] 表示到达 [i][j] 格子总路径数目
        dp[0][0] = 1;
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(0, 0));
        while (!queue.isEmpty()) {
            Point p = queue.poll();
            int i1 = p.i + 1;
            int j1 = p.j + 1;
            if (i1 < n && obstacleGrid[i1][p.j] == 0) {
                if (dp[i1][p.j] == 0) queue.add(new Point(i1, p.j));
                dp[i1][p.j] += dp[p.i][p.j];
            }
            if (j1 < m && obstacleGrid[p.i][j1] == 0) {
                if (dp[p.i][j1] == 0) queue.add(new Point(p.i, j1));
                dp[p.i][j1] += dp[p.i][p.j];
            }
        }
        return dp[n - 1][m - 1];
    }

    public static class Point {
        int i;
        int j;

        public Point(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    /*
     * 62. 不同路径
     */
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }
        for (int j = 0; j < n; j++) {
            dp[0][j] = 1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }
        return dp[m - 1][n - 1];
    }

    /*
     * 1314. 矩阵区域和
     */
    public int[][] matrixBlockSum(int[][] mat, int k) {
        int m = mat.length;
        int n = mat[0].length;
        int[][] preSum = new int[m + 1][n + 1];
        for (int i = 1; i < m + 1; i++) {
            for (int j = 1; j < n + 1; j++) {
                preSum[i][j] = mat[i - 1][j - 1] + preSum[i - 1][j] + preSum[i][j - 1] - preSum[i - 1][j - 1];
            }
        }
        int[][] ans = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int i0 = Math.max(i - k, 0);
                int j0 = Math.max(j - k, 0);
                int i1 = Math.min(1 + i + k, m);
                int j1 = Math.min(1 + j + k, n);
                ans[i][j] = preSum[i1][j1] + preSum[i0][j0] - preSum[i1][j0] - preSum[i0][j1];
            }
        }
        return ans;
    }

    /*
     * 120. 三角形最小路径和
     */
    public int minimumTotal(List<List<Integer>> triangle) {
        int n = triangle.size();
        List<Integer> buff = new ArrayList<>();
        buff.add(0);
        for (int i = 0; i < n; i++) {
            List<Integer> cur = triangle.get(i);
            List<Integer> next = new ArrayList<>();
            // 第i层，有i + 1个元素
            for (int j = 0; j < cur.size(); j++) {
                int l = 0x3f3f3f3f;  // j - 1
                int r = 0x3f3f3f3f;  // j
                if (j - 1 >= 0) l = buff.get(j - 1);
                if (j < buff.size()) r = buff.get(j);
                int t = Math.min(l, r) + cur.get(j);
                next.add(t);
            }
            buff = next;
        }
        int ans = 0x3f3f3f3f;
        for (int t : buff) {
            ans = Math.min(ans, t);
        }
        return ans;
    }

    /*
     * 931. 下降路径最小和
     */
    public int minFallingPathSum(int[][] matrix) {
        int m = matrix[0].length;
        int[] buff = new int[m];
        for (int[] row : matrix) {
            int[] cur = new int[m];
            for (int i = 0; i < m; i++) {
                cur[i] = buff[i] + row[i];
            }
            for (int i = 0; i < m; i++) {
                int l = 0x3f3f3f3f;
                int r = 0x3f3f3f3f;
                if (i > 0) l = cur[i - 1];
                if (i < m - 1) r = cur[i + 1];
                int min_l_r = Math.min(l, r);
                int min = Math.min(min_l_r, cur[i]);
                buff[i] = min;
            }
        }
        int ans = 0x3f3f3f3f;
        for (int i = 0; i < m; i++) {
            ans = Math.min(ans, buff[i]);
        }
        return ans;
    }

    public List<Integer> getRow(int rowIndex) {
        List<Integer> past = new ArrayList<>();
        past.add(1);
        List<Integer> cur = new ArrayList<>();
        cur.add(1);
        for (int i = 0; i < rowIndex; i++) {
            cur = new ArrayList<>();
            cur.add(1);
            for (int j = 1; j < past.size(); j++) {
                cur.add(past.get(j - 1) + past.get(j));
            }
            cur.add(1);
            past = cur;
        }
        return cur;
    }

    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> first = new ArrayList<>();
        first.add(1);
        ans.add(first);
        for (int i = 1; i < numRows; i++) {
            List<Integer> past = ans.get(i - 1);
            List<Integer> cur = new ArrayList<>();
            cur.add(1);
            for (int j = 1; j < past.size(); j++) {
                cur.add(past.get(j - 1) + past.get(j));
            }
            cur.add(1);
            ans.add(cur);
        }
        return ans;
    }

    /*
     * 96. 不同的二叉搜索树
     * 1 -> 1
     * 2 -> 1 * 2 -> 2
     * 3 -> 2 * 2 + 1 -> 5
     * 4 -> (5 + 2) * 2 -> 14
     * 5
     * ->
     * 0,4 -> 14 * 2 = 28
     * 1,3 -> 5 * 2 = 10
     * 2,2 -> 2 * 2 = 4
     * -> 42
     */
    public int numTrees(int n) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        int acc = 0;
        for (int i = 1; i <= n; i++) {
            acc = 0;
            int half = i / 2;
            int left = i % 2;
            for (int j = 1; j <= half; j++) {  // 以 j 为底
                int l = list.get(j - 1);
                int r = list.get(i - j);
                acc += (l * r) * 2;
            }
            if (left == 1) {
                int l = list.get(half);
                acc += l * l;
            }
            list.add(acc);
        }
        return acc;
    }

    /*
     * 264. 丑数 II
     * 2,3,5
     * 4,3,5
     * 4,6,5
     * 8,6,5
     * 8,6,10
     * 8,12,10
     */
    public int nthUglyNumber(int n) {
        int a = 0;
        int b = 0;
        int c = 0;
        List<Long> list = new ArrayList<>();
        list.add(1L);
        long min = 1;
        for (int i = 1; i < n; i++) {
            long min_2_3 = Math.min(2 * list.get(a), 3 * list.get(b));
            min = Math.min(min_2_3, 5 * list.get(c));
            list.add(min);
            if (min == 2 * list.get(a)) a++;
            if (min == 3 * list.get(b)) b++;
            if (min == 5 * list.get(c)) c++;
        }
        return (int) min;
    }

    /*
     * 91. 解码方法
     */
    public int numDecodings(String s) {
        // 1 -> 0-9
        // 2 -> 0-6
        int n = s.length();
        if (s.charAt(0) == '0') return 0;
        if (n == 1) return 1;
        char[] cs = s.toCharArray();
        int[] dp = new int[n];
        dp[0] = 1;
        dp[1] = 1;
        if (cs[0] == '1' || (cs[0] == '2' && '0' <= cs[1] && cs[1] <= '6')) {
            if (cs[1] != '0') dp[1] = 2;
        }
        if (cs[1] == '0' && cs[0] > '2') {
            return 0;
        }

        for (int i = 2; i < n; i++) {
            char c = cs[i];
            char p = cs[i - 1];

            if (c == '0') {
                if (p == '1' || p == '2') {
                    dp[i] = dp[i - 2];
                    continue;
                } else {
                    return 0;
                }
            }

            if (p == '1') {
                dp[i] = dp[i - 1] + dp[i - 2];
            } else if (p == '2' && '0' <= c && c <= '6') {
                dp[i] = dp[i - 1] + dp[i - 2];
            } else {
                dp[i] = dp[i - 1];
            }
        }
        return dp[n - 1];
    }

    /*
     * 5921. 最大化一张图中的路径价值
     */
    int ans = 0;

    public int maximalPathQuality(int[] values, int[][] edges, int maxTime) {
        int n = values.length;
        int[][] graph = new int[n][n];
        for (int[] edge : edges) {
            graph[edge[0]][edge[1]] = edge[2];
            graph[edge[1]][edge[0]] = edge[2];
        }
        boolean[] visited = new boolean[n];
        visited[0] = true;
        dfs(0, values[0], 0, n, visited, values, graph, maxTime);
        return ans;
    }

    public void dfs(int cur, int curSum, int curTime, int n, boolean[] visited, int[] values, int[][] graph, int maxTime) {
        if (cur == 0) {
            ans = Math.max(ans, curSum);
        }
        for (int i = 0; i < n; i++) {
            if (i == cur) continue;
            if (graph[cur][i] == 0) continue;

            int time = graph[cur][i];
            if (curTime + time > maxTime) continue;
            if (i != 0 && curTime + 2 * time > maxTime) continue;

            int nextSum = curSum;
            if (!visited[i]) {
                nextSum += values[i];
                visited[i] = true;
                dfs(i, nextSum, curTime + time, n, visited, values, graph, maxTime);
                visited[i] = false;
            } else {
                dfs(i, nextSum, curTime + time, n, visited, values, graph, maxTime);
            }
        }
    }

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

    /*
     * 1014. 最佳观光组合
     */
    public int maxScoreSightseeingPair(int[] values) {
        int n = values.length;
        int max = values[0];
        int ans = -1;
        for (int i = 1; i < n; i++) {
            max--;
            ans = Math.max(ans, max + values[i]);
            max = Math.max(max, values[i]);
        }
        return ans;
    }

    // 5916. 转化数字的最小运算数
    public int minimumOperations(int[] nums, int start, int goal) {
        Map<Integer, Integer> map = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();
        int move = 0;
        map.put(start, 0);
        queue.add(start);
        while (!queue.isEmpty()) {
            move++;
            List<Integer> list = new ArrayList<>();
            while (!queue.isEmpty()) {
                int t = queue.poll();
                for (int u : nums) {
                    int a1 = t + u;
                    int a2 = t - u;
                    int a3 = t ^ u;
                    if (a1 == goal || a2 == goal || a3 == goal) return move;
                    if (!map.containsKey(a1) && 0 <= a1 && a1 <= 1000) {
                        list.add(a1);
                        map.put(a1, move);
                    }
                    if (!map.containsKey(a2) && 0 <= a2 && a2 <= 1000) {
                        list.add(a2);
                        map.put(a2, move);
                    }
                    if (!map.containsKey(a3) && 0 <= a3 && a3 <= 1000) {
                        list.add(a3);
                        map.put(a3, move);
                    }
                }
            }
            queue.addAll(list);
        }
        return -1;
    }

    // 2058. 找出临界点之间的最小和最大距离
    public int[] nodesBetweenCriticalPoints(ListNode head) {
        int pre = head.val;
        head = head.next;
        if (head == null || head.next == null) return new int[]{-1, -1};
        int cur = head.val;
        int nxt = head.next.val;
        int index = 1;
        List<Integer> list = new ArrayList<>();
        while (head.next != null) {
            cur = head.val;
            nxt = head.next.val;
            if (pre < cur && cur > nxt) {
                list.add(index);
            } else if (pre > cur && cur < nxt) {
                list.add(index);
            }
            pre = cur;
            head = head.next;
            index++;
        }
        if (list.size() < 2) return new int[]{-1, -1};
        int min = 0x3f3f3f3f;
        for (int i = 1; i < list.size(); i++) {
            min = Math.min(min, list.get(i) - list.get(i - 1));
        }
        int[] ret = new int[]{min, list.get(list.size() - 1) - list.get(0)};
        return ret;
    }

    // 2055. 蜡烛之间的盘子
    public int[] platesBetweenCandles(String s, int[][] queries) {
        int n = s.length();
        int[] cnt = new int[n];  // cnt[i] —— i 为 '|' 且作为右端点的时候
        int inc = 0;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '*') inc++;
            else {
                cnt[i] = inc;
            }
        }
        int[] left = new int[n];  // left[i] —— i 作为左端点的时候，需要扣除多少
        int cur = 0;
        // "|||*|"  由左向右找到第一个 |
        // 0 0 0 1 1
        for (int i = n - 1; i >= 0; i--) {
            if (s.charAt(i) == '|') {
                cur = cnt[i];
            }
            left[i] = cur;
        }

        int[] right = new int[n];  // right[i] —— i 作为右端点的时候，能获得多少
        cur = 0;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '|') {
                cur = cnt[i];
            }
            right[i] = cur;
        }
        int m = queries.length;
        int[] ans = new int[m];
        for (int i = 0; i < m; i++) {
            int[] query = queries[i];
            int t = getAns(query, left, right);
            if (t < 0) t = 0;
            ans[i] = t;
        }
        return ans;
    }

    public int getAns(int[] query, int[] left, int[] right) {
        int l = query[0];
        int r = query[1];
        return right[r] - left[l];
    }

    public int maxTwoEvents0(int[][] events) {
        int n = events.length;
        // 找到开始时间 >= xxx 的最大点数
        int max = -1;
        for (int i = 0; i < n; i++) {
            max = Math.max(max, events[i][2]);
            for (int j = 0; j < n; j++) {
                if (j == i) continue;
                if (events[j][0] > events[i][1]) {
                    max = Math.max(max, events[i][2] + events[j][2]);
                }
            }
        }
        return max;
    }

    public String kthDistinct(String[] arr, int k) {
        Set<String> set1 = new HashSet<>();
        Set<String> set2 = new HashSet<>();
        for (String s : arr) {
            set1.add(s);
            if (set1.contains(s)) set2.add(s);
        }
        for (String s : arr) {
            if (set2.contains(s)) continue;
            k--;
            if (k == 0) return s;
        }
        return "";
    }

    /*
     * 260. 只出现一次的数字 III
     * 给出 nums[] 数组，数组中每个元素都出现两次，但其中有两个数只出现过一次，返回这两个 只出现一次的数字
     */
    public int[] singleNumber(int[] nums) {
        int u = 0;
        int n = nums.length;
        for (int num : nums) {
            u ^= num;
        }
        int[] nums0 = new int[n];
        int[] nums1 = new int[n];
        int p0 = 0, p1 = 0;
        int dis = 0;
        for (int i = 0; i < 32; i++) {
            int t = u >> i & 1;
            if (t == 1) {
                dis = i;
                break;
            }
        }
        for (int num : nums) {
            if ((num >> dis & 1) == 0) nums0[p0++] = num;
            if ((num >> dis & 1) == 1) nums1[p1++] = num;
        }

        int r0 = 0, r1 = 0;
        for (int num : nums0) r0 ^= num;
        for (int num : nums1) r1 ^= num;
        return new int[]{r0, r1};
    }

    /*
     * [2,3,4] -> 6
     * [2,2,3,3,3,4] -> 9
     *
     */
    public int deleteAndEarn(int[] nums) {
        int[] cnt = new int[1001];
        int n = nums.length;
        int max = 0;
        for (int i = 0; i < n; i++) {
            cnt[nums[i]]++;
            max = Math.max(max, nums[i]);
        }
        int[] dp = new int[max + 1];
        dp[1] = cnt[1];
        for (int i = 2; i <= max; i++) {
            if (cnt[i] == 0) dp[i] = dp[i - 1];
            else dp[i] = Math.max(dp[i - 2] + i * cnt[i], dp[i - 1]);
        }
        return dp[max];
    }

    public boolean isSelfCrossing(int[] d) {
        int n = d.length;
        if (n < 4) return false;
        for (int i = 3; i < n; i++) {
            if (d[i] >= d[i - 2] && d[i - 1] <= d[i - 3]) return true;
            if (i >= 4 && d[i - 1] == d[i - 3] && d[i] + d[i - 4] >= d[i - 2]) return true;
            if (i >= 5 && d[i - 1] <= d[i - 3] && d[i - 2] > d[i - 4] && d[i] + d[i - 4] >= d[i - 2] && d[i - 1] + d[i - 5] >= d[i - 3])
                return true;
        }
        return false;
    }

    // 0, 1, 1, 2
    public int fib(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        int a = 1;
        int b = 0;
        while (n > 1) {
            int t = a;
            a = a + b;
            b = t;
            n--;
        }
        return a;
    }

    /*
     * 638. 大礼包
     * <p>
     * [0,0,0]
     * [[1,1,0,4],[2,2,1,9]]
     * [1,1,1]
     */
    public int shoppingOffers(List<Integer> price, List<List<Integer>> special, List<Integer> needs) {
        int n = price.size();
        List<List<Integer>> goodSpecial = new ArrayList<>();
        // 筛选 能够优惠 且 可以选择 的大礼包
        for (List<Integer> sp : special) {
            int total = 0;  // 大礼包中元素的原价
            for (int i = 0; i < n; i++) {
                if (sp.get(i) > needs.get(i)) {
                    total = 0x3f3f3f3f;
                    break;
                }
                total += sp.get(i) * price.get(i);
            }
            if (total <= sp.get(n)) {
                continue;
            }
            goodSpecial.add(sp);
        }

        dfs1(0, needs, goodSpecial, price, n);
        return ans638;
    }

    int ans638 = 0x3f3f3f3f;

    public void dfs1(int total, List<Integer> needs, List<List<Integer>> special, List<Integer> price, int n) {
        boolean hasSpe = false;
        for (List<Integer> spe : special) {
            if (goodSpe(spe, needs)) {
                hasSpe = true;
                total += spe.get(n);
                useSpe(spe, needs);
                dfs1(total, needs, special, price, n);
                total -= spe.get(n);
                releaseSpe(spe, needs);
            }
        }
        if (!hasSpe) {
            for (int i = 0; i < n; i++) {
                total += needs.get(i) * price.get(i);
            }
            ans638 = Math.min(ans638, total);
        }
    }

    public boolean goodSpe(List<Integer> spe, List<Integer> needs) {
        for (int i = 0; i < needs.size(); i++) {
            if (spe.get(i) > needs.get(i)) {
                return false;
            }
        }
        return true;
    }

    public void useSpe(List<Integer> spe, List<Integer> needs) {
        for (int i = 0; i < needs.size(); i++) {
            int t = needs.get(i) - spe.get(i);
            needs.remove(i);
            needs.add(i, t);
        }
    }

    public void releaseSpe(List<Integer> spe, List<Integer> needs) {
        for (int i = 0; i < needs.size(); i++) {
            int t = needs.get(i) + spe.get(i);
            needs.remove(i);
            needs.add(i, t);
        }
    }
}
