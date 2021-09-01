import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

public class MonotonicStack {

    /**
     * 典型应用：下一个更大的元素
     */

    // 1944. 队列中可以看到的人数
    public int[] canSeePersonsCount(int[] heights) {
        Stack<Integer> stack = new Stack<>();  // 单调递减栈；曾在他上面的，还有把他挤出去的，就是他能看到的
        int n = heights.length;
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            int t = heights[i];
            while (!stack.isEmpty() && t > heights[stack.peek()]) {
                int index = stack.pop();
                ans[index]++;
            }
            if (!stack.isEmpty()) {
                int index = stack.peek();
                ans[index]++;
            }
            stack.add(i);
        }
        return ans;
    }

    // 面试题 17.21 直方图的水量
    // 给定一个直方图(也称柱状图)，假设有人从上面源源不断地倒水，最后直方图能存多少水量
    public int trap(int[] height) {
        Deque<Integer> stack = new LinkedList<>();  // 单调递减栈，存下标
        int length = height.length;
        int ans = 0;
        for (int i = 0; i < length; ++i) {
            while (!stack.isEmpty() && height[i] > height[stack.peek()]) {
                int top = stack.pop();
                if (stack.isEmpty()) {  // 调用stack.pop(), stack.peek()之前一定判空
                    break;
                }
                int j = stack.peek();
                ans += (i - j - 1) * (Math.min(height[i], height[j]) - height[top]);
            }
            stack.push(i);
        }
        return ans;
    }

    // 456. 132模式
    // 给你一个整数数组 nums ，数组中共有 n 个整数。132 模式的子序列 由三个整数 nums[i]、nums[j] 和 nums[k] 组成，并同时满足：i < j < k 和 nums[i] < nums[k] < nums[j] 。
    // 如果 nums 中存在 132 模式的子序列 ，返回 true ；否则，返回 false 。
    public boolean find132pattern(int[] nums) {
        int n = nums.length;
        int last = Integer.MIN_VALUE;  // 132中的2
        Stack<Integer> sta = new Stack<>();  // 单调递减栈，栈顶是132中的3
        if (nums.length < 3)
            return false;
        for (int i = n - 1; i >= 0; i--) {
            if (nums[i] < last)  // 若出现132中的1则返回正确值
                return true;
            // 若当前值大于或等于2则更新2（2为栈中小于当前值的最大元素）
            while (!sta.isEmpty() && sta.peek() < nums[i]) {
                last = sta.pop();
            }
            // 将当前值压入栈中
            sta.push(nums[i]);
        }
        return false;
    }


    // 503. 下一个更大元素 II
    public int[] nextGreaterElements1(int[] nums) {
        int n = nums.length;
        int[] ret = new int[n];
        Arrays.fill(ret, -1);
        // 单调栈，栈中元素都是从底单调递减的
        Deque<Integer> stack = new LinkedList<Integer>();
        for (int i = 0; i < n * 2 - 1; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] < nums[i % n]) {
                ret[stack.pop()] = nums[i % n];
            }
            stack.push(i % n);
        }
        return ret;
    }


    // 1475. 商品折扣后的最终价格
    // 单调栈，栈的使用
    // hook没有动态，单调栈动态了，hook方法基准没有移动，是不可行的
    public int[] finalPrices(int[] prices) {
        int len = prices.length;
        Stack<Integer> stack = new Stack<>();  // 单调递减栈

        for (int i = 0; i < len; i++) {
            while (!stack.isEmpty() && prices[stack.peek()] >= prices[i]) {
                int index = stack.pop();    // java 的pop可以直接获取顶元素就不用像c++ 一样先top再pop了
                prices[index] -= prices[i];
            }
            stack.push(i);
        }
        return prices;
    }
}