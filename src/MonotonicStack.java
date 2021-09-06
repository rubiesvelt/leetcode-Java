import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

public class MonotonicStack {

    /**
     * 典型应用：
     * 下一个更大的元素 —— 单调递减栈
     * 下一个更小的元素 —— 单调递增栈
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

    // 1856. 子数组最小乘积的最大值
    // 一个数组的 最小乘积 定义为这个数组中 最小值 乘以 数组的 和
    // 比方说，数组 [3,2,5] （最小值是 2）的最小乘积为 2 * (3+2+5) = 2 * 10 = 20
    // 求 子数组最小乘积的最大值
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