/**
 * DP —— 最长公共子串
 */
public class DPLCS {

    /*
     * 1143. 最长公共子序列 —— LCS问题
     * 经典动态规划
     * dp[i][j] =
     * {
     *     dp[i - 1][j - 1] + 1;  （当text1.charAt(i) == text2.charAt(j)时）
     *     Math.max(dp[i - 1][j], dp[i][j - 1]);  （当text1.charAt(i) != text2.charAt(j)时）
     * }
     */
    public int longestCommonSubsequence(String text1, String text2) {
        int length1 = text1.length();
        int length2 = text2.length();
        int[][] dp = new int[length1 + 1][length2 + 1];
        for (int i = 1; i < length1 + 1; i++) {
            char c1 = text1.charAt(i - 1);
            for (int j = 1; j < length2 + 1; j++) {
                char c2 = text2.charAt(j - 1);
                if (c1 == c2) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[length1][length2];
    }

    // 1035. 不相交的线
    // 动态规划，与 1143. 最长公共子序列 雷同 —— LCS问题
    public int maxUncrossedLines(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            int num1 = nums1[i - 1];
            for (int j = 1; j <= n; j++) {
                int num2 = nums2[j - 1];
                if (num1 == num2) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    // dp[i - 1][j]，不用模式串
                    // dp[i][j - 1]，不用当前串
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[m][n];
    }

}
