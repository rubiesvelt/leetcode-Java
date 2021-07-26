import java.util.TreeMap;

public class Array {

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
