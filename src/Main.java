import beans.TreeNode;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        char a = 'a';
        int i = 96;
        TreeNode tn4 = new TreeNode(4);
        TreeNode tn3 = new TreeNode(3);
        TreeNode tn2 = new TreeNode(2, null, tn4);
        TreeNode tn1 = new TreeNode(1, tn2, tn3);

        int[][] matrix = {{2, 1, 1}, {2, 3, 1}, {3, 4, 1}};
        int[][] matrix1 = {{1, 0, 0}, {0, 0, 1}, {1, 1, 0}};
        int[] dist = {1, 2, 3, 4};
        int[] speed = {5, 1, 5, 5, 1, 5, 3, 4, 5, 1, 4};
        char[][] matrix2 = {{'.', '.', 'W', '.', 'B', 'W', 'W', 'B'}, {'B', 'W', '.', 'W', '.', 'W', 'B', 'B'}, {'.', 'W', 'B', 'W', 'W', '.', 'W', 'W'}, {'W', 'W', '.', 'W', '.', '.', 'B', 'B'}, {'B', 'W', 'B', 'B', 'W', 'W', 'B', '.'}, {'W', '.', 'W', '.', '.', 'B', 'W', 'W'}, {'B', '.', 'B', 'B', '.', '.', 'B', 'B'}, {'.', 'W', '.', 'W', '.', 'W', '.', 'W'}};
        return;
    }

    // 526. 优美的排列
    public int countArrangement(int n) {
        return dfs(n, 1, new boolean[n + 1]);  // i表示第i个位置
    }

    private int dfs(int n, int i, boolean[] visited) {
        if (i > n) {
            return 1;  // 到头来返回1
        }

        int ans = 0;
        for (int num = 1; num <= n; num++) {
            if (!visited[num] && (num % i == 0 || i % num == 0)) {
                visited[num] = true;
                ans += dfs(n, i + 1, visited);
                visited[num] = false;
            }
        }

        return ans;
    }

    // 1960. 两个回文子字符串长度的最大乘积
    public long maxProduct(String s) {
        int n = s.length();
        boolean[][] g = new boolean[n][n];
        for (int i = 0; i < n; ++i) {
            Arrays.fill(g[i], true);
        }
        // 发现一个的时候，在之前的里面取最长的
        for (int i = n - 1; i >= 0; --i) {
            for (int j = i + 1; j < n; ++j) {
                g[i][j] = s.charAt(i) == s.charAt(j) && g[i + 1][j - 1];
            }
        }
        int[] longest = new int[n];
        Arrays.fill(longest, 1);
        for (int i = 1; i < n; i++) {  // 包含i的longest
            for (int j = 0; j <= i; j++) {
                if (g[j][i]) {
                    int cur = i - j + 1;
                    if (cur % 2 == 0) {
                        continue;
                    }
                    longest[i] = Math.max(longest[i - 1], cur);
                    break;
                }
                longest[i] = Math.max(longest[i - 1], longest[i]);
            }
        }
        long ans = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                if (g[j][i]) {
                    long cur = i - j + 1;
                    if (cur % 2 == 0) {
                        continue;
                    }
                    long bef;
                    if (j == 0) {
                        continue;
                    } else {
                        bef = longest[j - 1];
                    }
                    long l = cur * bef % 1000000007;
                    ans = Math.max(ans, l);
                }
            }
        }
        return ans;
    }
}
