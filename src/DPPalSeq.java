import java.util.Arrays;

/**
 * DP —— 回文字串
 */
public class DPPalSeq {

    // 516. 最长回文子序列
    // 区间dp
    // 给定一个字符串，求 最长的，回文的，子序列(从原序列去除任意个字符，字符顺序不变)的长度
    public int longestPalindromeSubseq(String s) {
        int n = s.length();
        int[][] f = new int[n][n];
        // f[j][i] 为下标j到i之间的子串中的最长回文数
        // 状态转移方程
        // f[j][i] = f[j + 1][i - 1] + 2             —— s.charAt(i) == s.charAt(j)
        // f[j][i] = max(f[j + 1][i], f[j][i - 1])   —— s.charAt(i) != s.charAt(j)

        // i从后往前，j从i往后
        for (int i = n - 1; i >= 0; i--) {
            f[i][i] = 1;
            for (int j = i + 1; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    f[i][j] = f[i + 1][j - 1] + 2;  // j = i + 1 时候达到"无人区"，0 + 2 = 2
                } else {
                    f[i][j] = Math.max(f[i + 1][j], f[i][j - 1]);
                }
            }
        }
        // 或者j从前往后，i从j - 1往前
        return f[0][n - 1];
    }

    // 132. 分割回文串 II
    // 给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是回文，求最少分割次数
    public int minCut(String s) {
        int n = s.length();
        boolean[][] g = new boolean[n][n];
        for (int i = 0; i < n; ++i) {
            Arrays.fill(g[i], true);  // i + 1 = j时达到"无人区"，无人区应为 true
        }
        for (int i = n - 1; i >= 0; --i) {
            for (int j = i + 1; j < n; ++j) {
                g[i][j] = s.charAt(i) == s.charAt(j) && g[i + 1][j - 1];
            }
        }

        int[] f = new int[n];
        Arrays.fill(f, Integer.MAX_VALUE);
        // 动态规划
        // aabbc
        for (int i = 0; i < n; i++) {
            if (g[0][i]) {
                f[i] = 0;  // 切0刀
            } else {
                for (int j = 0; j < i; j++) {
                    if (g[j + 1][i]) {
                        f[i] = Math.min(f[i], f[j] + 1);  // 多切一刀
                    }
                }
            }
        }
        return f[n - 1];
    }
}
