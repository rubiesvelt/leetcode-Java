import java.util.Arrays;
import java.util.TreeSet;

public class DoublePointers {

    // 1838. Frequency of the Most Frequent Element 最高频元素的频数
    // 滑动窗口
    public int maxFrequency(int[] nums, int k) {
        Arrays.sort(nums);
        int max = 0;
        int tempSum = 0;  // 记录当前 实体 的和
        int i = 0;
        // j goes forward, i follow up!
        for (int j = 0; j < nums.length; j++) {  // 这个循环，第二次来判断时候，即使没有进入，j 还是会 ++
            while (nums[j] * (j - i) - tempSum > k) {
                tempSum -= nums[i++];
            }
            tempSum += nums[j];
            // 到此处 由 i 到 j 都是合理状态
            max = Math.max(max, j - i + 1);
        }
        return max;
    }

    // LCP28. 采购方案
    // 小力将 N 个零件的报价存于数组 nums。小力预算为 target，假定小力仅购买两个零件，要求购买零件的花费不超过预算，请问他有多少种采购方案
    // 双指针
    public int purchasePlans2(int[] nums, int target) {
        Arrays.sort(nums);
        int left = 0;
        int right = nums.length - 1;
        long ans = 0;
        while (left < right) {
            if (nums[left] + nums[right] > target) {
                right--;
                continue;
            }
            ans += right - left;
            left++;
        }
        return (int) (ans % 1_000_000_007);
    }

    // 另一种方法，比双指针更快
    public int purchasePlans3(int[] nums, int target) {
        long res = 0;
        int[] cnt = new int[100010], pre = new int[100010];
        for (int i : nums) cnt[i]++;
        for (int i = 1; i < 100010; i++)
            pre[i] = pre[i - 1] + cnt[i];  // pre记录nums中小于等于i的数字个数
        for (int i : nums)
            if (target >= i)
                res += pre[target - i];
        for (int i : nums) if (2 * i <= target) res--;  // 删掉自己匹配自己的
        res >>= 1;  // 右移一位，相当于 /2，a匹配b和b匹配a只能算一个
        return (int) (res % 1_000_000_007);
    }

    // 220. 存在重复元素 III
    // 滑动窗口
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        TreeSet<Long> ts = new TreeSet<>();
        for (int i = 0; i < nums.length; i++) {
            long n = nums[i];
            // 没想到TreeSet还有 floor，ceiling 方法，方便定位
            Long f = ts.floor(n);  // may return null，throw NPE when unboxing
            Long c = ts.ceiling(n);
            if (f != null && n - f <= t) return true;
            if (c != null && c - n <= t) return true;
            ts.add(n);
            if (i >= k) ts.remove((long) nums[i - k]);
        }
        return false;
    }

    /*
     * 1004. 最大连续1的个数 III
     * 给定一个由若干 0 和 1 组成的数组 A，我们最多可以将 K 个值从 0 变成 1 。返回仅包含 1 的最长（连续）子数组的长度。
     *
     * left为左边界1
     * right为右边界1
     * lsum 为left左边的0个数
     * rsum 为right左边及right的0的个数
     * 左，见了0需要逃离
     * 右，见了0需要改变
     * */
    public int longestOnes(int[] A, int K) {
        int n = A.length;
        int left = 0, lsum = 0, rsum = 0;
        int ans = 0;
        for (int right = 0; right < n; right++) {
            rsum += 1 - A[right];  // rsum 包含当前right统计
            while (lsum < rsum - K) {
                lsum += 1 - A[left];
                ++left;  // lsum 统计完，left++，所以不包含当前right统计
            }
            ans = Math.max(ans, right - left + 1);  // right为最右1，left为最左1
        }
        return ans;
    }
}
