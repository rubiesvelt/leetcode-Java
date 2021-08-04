import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SimulateThreeTuple {

    // 611. 有效三角形的个数
    // 给定一个数组，统计其中可以组成三角形三条边的三元组个数
    // 回溯，暴力枚举会超时，需要观察场景采取合适策略
    public int triangleNumber(int[] nums) {
        Arrays.sort(nums);
        int result = 0;
        for (int i = nums.length - 1; i >= 2; i--) {
            int k = 0;
            int j = i - 1;
            while (k < j) {
                // 三角形三条边从短到长分别是 a, b, c
                // 选 c (num[i]) 和 b (num[j])，求 a (num[k - j], 可范围，批量求)
                if (nums[k] + nums[j] > nums[i]) {  // 满足该条件，说明从num[k]到num[j]的数都满足要求
                    result += j - k;
                    j--;
                } else {
                    // 否则k自增，重新判断
                    k++;
                }
            }
        }
        return result;
    }

    // 5809. 长度为 3 的不同回文子序列
    // dfs 可以摘出所有长度为 3 的序列，但解决此题明显不需要这样做，站在更高层次，根据题目给的限制来看，这种做法合适。
    public int countPalindromicSubsequence(String s) {
        int ans = 0;
        for (int i = 0; i < 26; i++) {
            Set<Character> set = new HashSet<>();
            char target = (char) ('a' + i);
            int l = 0;
            int r = s.length() - 1;
            while (l < s.length() - 2 && s.charAt(l) != target) {
                l++;
            }
            while (r > 1 && s.charAt(r) != target) {
                r--;
            }
            if (s.charAt(l) == target && s.charAt(r) == target) {
                for (int k = l + 1; k < r; k++) {
                    set.add(s.charAt(k));
                }
                ans += set.size();
            }
        }
        return ans;
    }
}
