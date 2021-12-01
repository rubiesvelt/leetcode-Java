import java.util.Arrays;

/**
 * DP —— 回文字串
 */
public class DPPalSeq {

    // 132. 分割回文串 II
    // 给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是回文，求最少分割次数
    public int minCut(String s) {
        int n = s.length();

        // 动态规划求解回文子串
        boolean[][] dp = new boolean[n][n];  // dp[i][j] == true 时表示子串 [i, j) 为回文字串
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i], true);
        }
        for (int i = n - 1; i >= 0; i--) {  // 由于 dp[i][j] 的状态与左下方 dp[i + 1][j - 1] 相关，故 i 从大往小
            for (int j = i + 1; j < n; j++) {  // i = n - 1 的时候 j = n，不会进入循环
                dp[i][j] = s.charAt(i) == s.charAt(j) && dp[i + 1][j - 1];  // j = i + 1 时达到"无人区"，无人区应为 true
            }
        }

        int[] f = new int[n];  // f[i]表示子串 [0, i] 之间需要切多少刀
        Arrays.fill(f, Integer.MAX_VALUE);
        // 动态规划
        // aabbc
        for (int i = 0; i < n; i++) {  // 扩展，i 从 0 向上，j 从 0 向 i
            if (dp[0][i]) {
                f[i] = 0;  // 切 0 刀
            } else {
                for (int j = 0; j < i; j++) {
                    if (dp[j + 1][i]) {
                        f[i] = Math.min(f[i], f[j] + 1);  // 多切一刀
                    }
                }
            }
        }
        return f[n - 1];
    }

    // 516. 最长回文子序列
    // 区间dp
    // 给定一个字符串，求 最长的，回文的，子序列(从原序列去除任意个字符，字符顺序不变)的长度
    public int longestPalindromeSubseq(String s) {
        int n = s.length();
        int[][] dp = new int[n][n];
        // dp[i][j] 为下标i到j之间的子串中的最长回文数
        // 状态转移方程
        // dp[i][j] = dp[i + 1][j - 1] + 2             —— s.charAt(i) == s.charAt(j)
        // dp[i][j] = max(dp[i + 1][j], dp[i][j - 1])   —— s.charAt(i) != s.charAt(j)

        // i从后往前，j从i往后
        for (int i = n - 1; i >= 0; i--) {
            dp[i][i] = 1;
            for (int j = i + 1; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = dp[i + 1][j - 1] + 2;  // j = i + 1 时候达到"无人区"，0 + 2 = 2; 只有上半区和中线是有人区
                } else {
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }
        // 或者j从前往后，i从j - 1往前
        return dp[0][n - 1];
    }

    // 1960. 两个回文子字符串长度的最大乘积
    // 只能用马拉车做
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
