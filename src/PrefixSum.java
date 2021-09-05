import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class PrefixSum {

    /**
     * 典型应用：求数组 nums[i] - nums[j] 区间的和
     */

    // 560. 和为K的子数组
    // 前缀和 + hashMap 模板题目
    public int subarraySum1(int[] nums, int k) {
        // 前缀和 -> 出现次数
        // 由于nums的元素可为负数，故前缀和不是单调递增的
        Map<Integer, Integer> map = new HashMap<>();
        int count = 0;
        int sum = 0;
        map.put(0, 1);
        for (int num : nums) {
            sum += num;
            int t = sum - k;
            count += map.getOrDefault(t, 0);
            map.put(sum, map.getOrDefault(sum, 0) + 1);
        }
        return count;
    }

    // 930. 和相同的二元子数组
    // 前缀和 + hashMap
    // 子数组，必须连续，考虑前缀和
    // 子集，不需要连续
    public int numSubarraysWithSum(int[] nums, int goal) {
        int n = nums.length;
        Map<Integer, Integer> map = new HashMap<>();  // 记录 前缀和 -> 出现次数
        map.put(0, 1);
        int ans = 0;
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += nums[i];
            int t = sum - goal;
            ans += map.getOrDefault(t, 0);
            map.put(sum, map.getOrDefault(sum, 0) + 1);
        }
        return ans;
    }

    // 前缀和模板题目
    public int subarraySum(int[] nums, int k) {
        int n = nums.length;
        int[] pre = new int[n + 1];
        pre[0] = 0;
        for (int i = 0; i < n; i++) {
            pre[i + 1] = pre[i] + nums[i];
        }
        int ans = 0;
        for (int i = 0; i < n + 1; i++) {
            for (int j = i + 1; j < n + 1; j++) {
                if (pre[j] - pre[i] == k) ans++;
            }
        }
        return ans;
    }

    // 1074. 元素和为目标值的子矩阵数量
    // 二维数组前缀和
    public int numSubmatrixSumTarget(int[][] mat, int t) {
        // 二维数组前缀和标准模板
        int n = mat.length;
        int m = mat[0].length;
        int[][] sum = new int[n + 1][m + 1];  // 前缀和数组
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                sum[i][j] = sum[i - 1][j] + sum[i][j - 1] - sum[i - 1][j - 1] + mat[i - 1][j - 1];
            }
        }

        int ans = 0;
        for (int top = 1; top <= n; top++) {
            for (int bot = top; bot <= n; bot++) {
                int cur;
                Map<Integer, Integer> map = new HashMap<>();
                // 使用map记录出现过的值的和，看起来是合理的
                for (int r = 1; r <= m; r++) {
                    cur = sum[bot][r] - sum[top - 1][r];
                    if (cur == t) {  // 从0开始，到r，和恰好是t
                        ans++;
                    }
                    /*
                     * 涉及前缀和，和为x的子序列
                     */
                    if (map.containsKey(cur - t)) {  // 从0开始，到r，和为cur，但之前有和为 cur - t 的，此时就凑成一对
                        ans += map.get(cur - t);
                    }
                    map.put(cur, map.getOrDefault(cur, 0) + 1);
                }
            }
        }
        return ans;
    }

    // 1442. 形成两个异或相等数组的三元组数目
    // 区间异或，利用异或数组前缀和
    public int countTriplets1(int[] arr) {
        int len = arr.length;
        int ans = 0;
        // 异或数组前缀和
        int[] pre = new int[len];
        pre[0] = arr[0];
        for (int i = 1; i < len; i++) {
            pre[i] = pre[i - 1] ^ arr[i];
        }
        for (int i = 0; i < len - 1; i++) {
            for (int k = i; k < len; k++) {
                if (i == 0) {
                    if (pre[k] == 0) ans += k;  // 前缀和第0个元素往往需要特判
                    continue;
                }
                if (pre[i - 1] == pre[k]) {
                    ans += (k - i);
                }
            }
        }
        return ans;
    }

    // 1310. 子数组异或查询
    // 异或运算中使用数组前缀和
    public int[] xorQueries(int[] arr, int[][] qs) {
        int n = arr.length;
        int m = qs.length;
        // 前缀和，带补位0的
        int[] sum = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            sum[i] = sum[i - 1] ^ arr[i - 1];
        }
        int[] ans = new int[m];
        for (int i = 0; i < m; i++) {
            int l = qs[i][0] + 1;
            int r = qs[i][1] + 1;
            ans[i] = sum[r] ^ sum[l - 1];
        }
        return ans;
    }

    // 5752. 子数组最小乘积的最大值
    // 单调栈
    public int maxSumMinProduct(int[] nums) {
        int n = nums.length;

        // 数组前缀和
        long[] pre = new long[n + 1];  // 存储下标“之前”的元素和
        pre[0] = nums[0];
        for (int i = 1; i <= n; i++) {
            pre[i] = pre[i - 1] + nums[i - 1];
        }

        // 单递增调栈
        Stack<Integer> stack = new Stack<>();
        // 求元素右边第一个比其小的
        int[] rightLower = new int[n];
        Arrays.fill(rightLower, n);  // 默认为n，即没发现
        for (int i = 0; i < n; i++) {
            // 单调递增栈
            while (!stack.isEmpty() && nums[stack.peek()] > nums[i]) {
                int t = stack.pop();
                rightLower[t] = i;
            }
            stack.push(i);
        }

        // 求元素左边第一个比其小的
        int[] leftLower = new int[n];
        Arrays.fill(rightLower, -1);  // 默认为-1，即没发现
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && nums[stack.peek()] > nums[i]) {
                int t = stack.pop();
                leftLower[t] = i;
            }
            stack.push(i);
        }

        // 在前缀和及单调栈基础上，求最终解
        long ans = 0;
        for (int i = 0; i < n; i++) {
            int r = rightLower[i];
            int l = leftLower[i] + 1;
            long t = pre[r] - pre[l];
            ans = Math.max(ans, t * nums[i]);
        }
        long mod = (long) 1e9 + 7;  // 注意，取模时的定义方法
        return (int) (ans % mod);
    }
}
