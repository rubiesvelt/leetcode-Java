package main;

import beans.TreeNode;

import java.util.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        List<Integer> list = new ArrayList<>();
        int t = list.hashCode();

        Main main = new Main();
        char a = 'a';
        int i = 96;
        TreeNode tn5 = new TreeNode(5);
        TreeNode tn4 = new TreeNode(4);
        TreeNode tn3 = new TreeNode(3, tn5, null);
        TreeNode tn2 = new TreeNode(2, tn4, null);
        TreeNode tn1 = new TreeNode(1, tn2, tn3);

        int[][] matrix = {{1, 0, 0}, {0, 1, 1}, {0, 1, 1}};
        int[][] matrix1 = {{1, 0, 0}, {0, 0, 1}, {1, 1, 0}};
        int[] dist = {2, 4, 6, 4};
        int[] diff = {3, 3, 3};
        int[] speed = {5, 1, 5, 5, 1, 5, 3, 4, 5, 1, 4};
        char[][] matrix2 = {{'.', '.', 'W', '.', 'B', 'W', 'W', 'B'}, {'B', 'W', '.', 'W', '.', 'W', 'B', 'B'}, {'.', 'W', 'B', 'W', 'W', '.', 'W', 'W'}, {'W', 'W', '.', 'W', '.', '.', 'B', 'B'}, {'B', 'W', 'B', 'B', 'W', 'W', 'B', '.'}, {'W', '.', 'W', '.', '.', 'B', 'W', 'W'}, {'B', '.', 'B', 'B', '.', '.', 'B', 'B'}, {'.', 'W', '.', 'W', '.', 'W', '.', 'W'}};

        return;
    }

    /*
     * 5904. 统计按位或能得到最大值的子集数目
     * 给出数组 nums[] 求其 按位或 能得到最大值 的子集 的数目
     * e.g.
     * nums = [1,2,3,5]
     *
     * 按位或最大值 7
     * 如下子集 进行按位或 可得到
     * [2,5]
     * [3,5]
     * [1,2,5]
     * [1,3,5]
     * [2,3,5]
     * [1,2,3,5]
     * 共 6 个
     * -> 6
     */
    public int countMaxOrSubsets1(int[] nums) {
        int n = nums.length;
        int max = 0;
        for (int num : nums) {
            max |= num;
        }
        int ans = 0;
        // n位 代表所有选项，从 0 到 2 ^ n
        for (int i = 0; i < (1 << n); i++) {  // 对于每一个子集
            int res = 0;
            for (int j = 0; j < n; j++) {
                if (((i >> j) & 1) == 1) {
                    res |= nums[j];
                }
            }
            if (res == max) {
                ans++;
            }
        }
        return ans;
    }

    /*
     * dfs遍历所有子集
     */
    public int countMaxOrSubsets(int[] nums) {
        int n = nums.length;
        int max = 0;  // 按位或的最大值
        for (int num : nums) {
            max |= num;
        }
        // 从 nums[] 中选 i 个
        for (int i = 1; i < n; i++) {
            dfs(nums, -1, 0, 0, i, max);
        }
        return ans;
    }

    int ans = 1;

    public void dfs(int[] nums, int index, int current, int sum, int total, int max) {
        /*
         * 可变
         * index 当前元素下标
         * current 当前 总和
         * sum 当前元素总数
         *
         * 不可变
         * total 要求元素总数
         * max 目标和
         */
        if (sum == total) {
            if (max == current) {
                ans++;
            }
            return;
        }
        for (int i = index + 1; i < nums.length; i++) {  // i为下一个目标
            int t = current | nums[i];
            dfs(nums, i, t, sum + 1, total, max);
        }
    }
}
