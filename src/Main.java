import beans.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // shi shi ji suan
        // max compute
        List<Integer> list = new ArrayList<>();
        Main main = new Main();
        char a = 'a';
        int i = 96;
        TreeNode tn4 = new TreeNode(4);
        TreeNode tn3 = new TreeNode(3);
        TreeNode tn2 = new TreeNode(2, null, tn4);
        TreeNode tn1 = new TreeNode(1, tn2, tn3);

        int[][] matrix = {{2, 1, 1}, {2, 3, 1}, {3, 4, 1}};
        int[][] matrix1 = {{1, 0, 0}, {0, 0, 1}, {1, 1, 0}};
        int[] dist = {2, 3, 3, 4, 4, 4, 5, 6, 7, 10};
        int[] speed = {5, 1, 5, 5, 1, 5, 3, 4, 5, 1, 4};
        char[][] matrix2 = {{'.', '.', 'W', '.', 'B', 'W', 'W', 'B'}, {'B', 'W', '.', 'W', '.', 'W', 'B', 'B'}, {'.', 'W', 'B', 'W', 'W', '.', 'W', 'W'}, {'W', 'W', '.', 'W', '.', '.', 'B', 'B'}, {'B', 'W', 'B', 'B', 'W', 'W', 'B', '.'}, {'W', '.', 'W', '.', '.', 'B', 'W', 'W'}, {'B', '.', 'B', 'B', '.', '.', 'B', 'B'}, {'.', 'W', '.', 'W', '.', 'W', '.', 'W'}};
        main.minSessions(dist, 12);
        return;
    }

    // 77. 组合
    List<List<Integer>> ans = new ArrayList<>();

    public List<List<Integer>> combine(int n, int k) {
        int[] num = new int[n];
        for(int i = 0;i<n;i++) {
            num[i] = i + 1;
        }
        List<Integer> current = new ArrayList<>();
        dfs77(num, k, 0, current);
        return ans;
    }

    public void dfs77(int[] num, int k, int index, List<Integer> current) {
        if(current.size() == k) {
            ans.add(new ArrayList<>(current));
            return;
        }

        for(int i = index;i < num.length;i++) {
            current.add(num[i]);
            dfs77(num, k, i + 1, current);
            current.remove(current.size() - 1);
        }
    }

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

    // 5856. 完成任务的最少工作时间段
    // 化为多次 0-1 背包问题
    public int minSessions(int[] oritasks, int sessionTime) {
        int cnt = 0;
        int n = oritasks.length;
        int left = oritasks.length;
        int[] tasks = new int[oritasks.length];
        Arrays.sort(oritasks);
        for (int i = 0; i < tasks.length; i++) {
            tasks[i] = oritasks[tasks.length - i - 1];
        }
        while (left > 0) {
            Block[] f = new Block[sessionTime + 1];  // f[i] 表示元素和不超过 i 的时候，背包中装的物品最大和
            for (int i = 0; i < sessionTime + 1; i++) {
                f[i] = new Block();
            }
            for (int i = 0; i < n; i++) {
                for (int j = sessionTime; j >= tasks[i]; j--) {
                    if (f[j].total >= f[j - tasks[i]].total + tasks[i]) {
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

    public String kthLargestNumber(String[] nums, int k) {
        Arrays.sort(nums, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int l1 = o1.length();
                int l2 = o2.length();
                if (l1 > l2) return 1;
                if (l1 < l2) return -1;
                for (int i = 0; i < l1; i++) {
                    char c1 = o1.charAt(i);
                    char c2 = o2.charAt(i);
                    if (c1 > c2) return 1;
                    if (c1 < c2) return -1;
                }
                return 0;
            }
        });
        return nums[nums.length - k];
    }

    public int minimumDifference(int[] nums, int k) {
        if (k == 1) {
            return 0;
        }
        Arrays.sort(nums);
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < nums.length; i++) {
            int j = i + k;
            if (j >= nums.length) {
                break;
            }
            if (min == 0) {
                return 0;
            }
            int diff = nums[j] - nums[i];
            min = Math.min(min, diff);
        }
        return min;
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
