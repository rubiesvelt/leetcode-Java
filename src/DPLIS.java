import java.util.Arrays;

/**
 * DP —— 最长递增子串
 */
public class DPLIS {

    // 300. 最长递增子序列
    // 严格递增
    // 有两种解法，一种 n方 的朴素解法，一种像如下这样 nlogn 解法
    public int lengthOfLIS(int[] nums) {
        int n = nums.length;
        int[] f = new int[n];  // f[i]表示 最长递增子序列 长度 为i时，该序列结尾元素的大小（f[]递增）
        int ans = -1;
        int len = 0;
        for (int x : nums) {
            int t = Utils.lower_bound(f, 0, len, x);  // 找到第一个大于等于 x 的元素 的下标; t + 1 为必选 x 时，最长递增子序列的长度
            f[t] = x;
            ans = Math.max(ans, t + 1);
            if (t == len) {
                len++;
            }
        }
        return ans;
    }

    // 1964. 找出到每个位置为止最长的有效障碍赛跑路线
    // 求解最长"不递减"子序列长度，对数组中的每一项
    // 必须 nlogn 解法，否则超时
    public int[] longestObstacleCourseAtEachPosition1(int[] obstacles) {
        int n = obstacles.length;
        int[] arr = new int[n];  // 用做状态传递的数组
        int[] res = new int[n];  // 存储结果
        int len = 0, idx = 0;
        for (int x : obstacles) {
            int t = Utils.upper_bound(arr, 0, len, x);  // t + 1 为必选 x 时，最长不递减子序列的长度
            arr[t] = x;
            res[idx] = t + 1;
            if (t == len) {
                len++;
            }
            idx++;
        }

        return res;
    }

    // 普通dp方法，复杂度 n^2（笨比）
    public int[] longestObstacleCourseAtEachPosition(int[] obstacles) {
        int n = obstacles.length;
        int[] mp = new int[10000008];
        int[] ans = new int[n];

        for (int i = 0; i < n; i++) {
            int t = obstacles[i];
            // t1 为下标小于等于 i ，且元素值 obstacles[i] 小于等于 t 的元素中 mp[] 最大的
            int t1 = getMaxIndex(mp, obstacles, t, i);
            mp[t] = mp[t1] + 1;
            ans[i] = mp[t];
        }
        return ans;
    }

    public int getMaxIndex(int[] mp, int[] obstacles, int t1, int i) {
        int maxIndex = 0;
        int max = -1;
        // 从i往下找，obstacles[i]小于等于t1合理，寻找ans中最大的，返回其值
        while (i >= 0) {
            int now = obstacles[i];
            if (now > t1) {
                i--;
                continue;
            }
            if (mp[now] > max) {
                max = mp[now];
                maxIndex = now;
            }
            i--;
        }
        return maxIndex;
    }

    // 354. 俄罗斯套娃信封问题
    // 动态规划，寻找最长递增子序列
    public static int maxEnvelopes(int[][] envelopes) {
        if (envelopes.length == 0) {
            return 0;
        }
        int n = envelopes.length;
        // 二维数组排序，从小到大，用前减后
        Arrays.sort(envelopes, (o1, o2) -> {
            if (o1[0] != o2[0]) {
                return o1[0] - o2[0];
            } else {
                return o2[1] - o1[1];
            }
        });

        int[] f = new int[n];
        int ans = 1;
        for (int i = 0; i < n; i++) {
            f[i] = 1;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (envelopes[i][1] > envelopes[j][1]) {
                    f[i] = Math.max(f[i], f[j] + 1);
                    ans = Math.max(f[i], ans);
                }
            }
        }
        return ans;
    }

}
