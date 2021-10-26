import java.util.ArrayList;
import java.util.List;

public class MoleCount {

    /**
     * 摩尔投票法基本思想：
     * 每一轮投票过程中，从数组中找出一对不同的元素，将其从数组中删除。
     * 这样不断的删除直到无法再进行投票，如果数组为空，则没有任何元素出现的次数超过该数组长度的一半。
     * 如果只存在一种元素，那么这个元素则可能为目标元素。
     */

    /**
     * 229. 求众数 II
     * 数组大小为 n 求出现次数 > n/3 的元素
     * 你能实现 时间复杂度O(n)，空间复杂度O(1) 的解法吗
     *
     * 摩尔计数
     */
    public List<Integer> majorityElement229(int[] nums) {
        int x = 0;
        int y = 0;
        int cx = 0;
        int cy = 0;
        for (int n : nums) {
            if ((cx == 0 || n == x) && n != y) {
                cx++;
                x = n;
            } else if (cy == 0 || n == y) {
                cy++;
                y = n;
            } else {
                cx--;
                cy--;
            }
        }
        int th = nums.length / 3 + 1;
        List<Integer> ans = new ArrayList<>();
        cx = 0;
        for (int n : nums) {
            if (n == x) cx++;
        }
        if (cx >= th) ans.add(x);
        if (x == y) {
            return ans;
        }
        cy = 0;
        for (int n : nums) {
            if (n == y) cy++;
        }
        if (cy >= th) ans.add(y);

        return ans;
    }

    // 面试题 17.10. 主要元素
    // 数组中大于一半的元素叫主要元素
    // 摩尔计数
    public int majorityElement(int[] nums) {
        int x = -1;
        int cnt = 0;
        for (int n : nums) {
            if (cnt == 0) {
                x = n;
                cnt = 1;
            } else {
                if (n == x) cnt++;
                else cnt -= 1;
            }
        }
        cnt = 0;
        for (int n : nums) {
            if (n == x) cnt++;
        }
        return cnt > nums.length / 2 ? x : -1;
    }
}
