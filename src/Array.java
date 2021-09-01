import java.util.TreeMap;

public class Array {

    // 413. 等差数列划分
    // 将一个数组划分成几个子数组，使每个都是等差数列
    // 1,2,4,6
    public int numberOfArithmeticSlices(int[] nums) {
        int n = nums.length;
        if (n < 3) {
            return 0;
        }
        int delta = nums[1] - nums[0];
        int addition = 0;
        int ans = 0;
        for (int i = 2; i < n; i++) {
            if (nums[i] - nums[i - 1] == delta) {
                ans += ++addition;  // 有技巧
            } else {
                addition = 0;
                delta = nums[i] - nums[i - 1];
            }
        }
        return ans;
    }

    // 581. 最短无序连续子数组
    public int findUnsortedSubarray(int[] arr) {
        if(arr == null || arr.length < 2){
            return 0;
        }
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        int R = 0;
        int L = 0;
        // 从小到大，找到违背的
        for (int i = 0; i < arr.length; i++) {
            if(max > arr[i]) {
                R = i;
            }
            max = Math.max(max, arr[i]);
        }
        // 从大到小，找到违背的
        for (int i = arr.length - 1; i >= 0; i--) {
            if(min < arr[i]) {
                L = i;
            }
            min = Math.min(min, arr[i]);
        }
        return R == L ? 0 : R - L + 1;
    }

    // 1438. 绝对差不超过限制的最长连续子数组
    public static int longestSubarray(int[] nums, int limit) {
        int res = 0;
        for (int i = 0; i < nums.length; i++) {
            int arrayMin = nums[i];
            int arrayMax = nums[i];
            int index = 0;
            int distinct = 0;
            while (distinct <= limit) {
                index++;
                if (i + index == nums.length) {
                    break;
                }
                if (nums[i + index] < arrayMin) {
                    arrayMin = nums[i + index];
                    distinct = arrayMax - arrayMin;
                } else if (nums[i + index] > arrayMax) {
                    arrayMax = nums[i + index];
                    distinct = arrayMax - arrayMin;
                }
            }
            res = Math.max(index, res);
        }
        return res;
    }

    public static int longestSubarray2(int[] nums, int limit) {
        // TreeMap的key有序，可以使用 map.lastKey()，map.firstKet()
        TreeMap<Integer, Integer> map = new TreeMap<Integer, Integer>();
        int n = nums.length;
        int left = 0, right = 0;
        int ret = 0;
        while (right < n) {
            map.put(nums[right], map.getOrDefault(nums[right], 0) + 1);
            while (map.lastKey() - map.firstKey() > limit) {
                map.put(nums[left], map.get(nums[left]) - 1);  // 左边向前一位，莫搞混nums[left]和lastKey()
                if (map.get(nums[left]) == 0) {
                    map.remove(nums[left]);
                }
                left++;
            }
            ret = Math.max(ret, right - left + 1);
            right++;
        }
        return ret;
    }

}
