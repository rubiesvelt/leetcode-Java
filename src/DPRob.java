/**
 * DP —— 打家劫舍
 */
public class DPRob {

    // 213. 打家劫舍 Ⅱ
    public int rob(int[] nums) {
        if (nums.length == 1) {
            return nums[0];
        }
        if (nums.length == 2) {
            return Math.max(nums[0], nums[1]);
        }
        int[] nums1 = new int[nums.length - 1];
        int[] nums2 = new int[nums.length - 1];
        int index1 = 0;
        int index2 = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i != nums.length - 1) nums1[index1++] = nums[i];
            if (i != 0) nums2[index2++] = nums[i];
        }
        nums = nums1;
        if (nums.length == 1) {
            return nums[0];
        }
        if (nums.length == 2) {
            return Math.max(nums[0], nums[1]);
        }
        int[] res = new int[nums.length];
        res[0] = nums[0];
        res[1] = Math.max(nums[0], nums[1]);
        for (int i = 2; i < nums.length; i++) {
            res[i] = Math.max(res[i - 1], res[i - 2] + nums[i]);
        }
        int ans1 = res[res.length - 1];
        nums = nums2;
        if (nums.length == 1) {
            return nums[0];
        }
        if (nums.length == 2) {
            return Math.max(nums[0], nums[1]);
        }
        res = new int[nums.length];
        res[0] = nums[0];
        res[1] = Math.max(nums[0], nums[1]);
        for (int i = 2; i < nums.length; i++) {
            res[i] = Math.max(res[i - 1], res[i - 2] + nums[i]);
        }
        int ans2 = res[res.length - 1];
        return Math.max(ans1, ans2);
    }
}
