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
        int[] diff = {1, 2, 1, 3, 2, 5};
        int[] speed = {28, 27, 13, 19, 23, 4, 29, 29, 7};
        char[][] matrix2 = {{'.', '.', 'W', '.', 'B', 'W', 'W', 'B'}, {'B', 'W', '.', 'W', '.', 'W', 'B', 'B'}, {'.', 'W', 'B', 'W', 'W', '.', 'W', 'W'}, {'W', 'W', '.', 'W', '.', '.', 'B', 'B'}, {'B', 'W', 'B', 'B', 'W', 'W', 'B', '.'}, {'W', '.', 'W', '.', '.', 'B', 'W', 'W'}, {'B', '.', 'B', 'B', '.', '.', 'B', 'B'}, {'.', 'W', '.', 'W', '.', 'W', '.', 'W'}};

        return;
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

    // 2059. 转化数字的最小运算数
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

}
