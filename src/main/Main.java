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
        TreeNode tn4 = new TreeNode(4);
        TreeNode tn3 = new TreeNode(3);
        TreeNode tn2 = new TreeNode(1, null, null);
        TreeNode tn1 = new TreeNode(-2, tn2, null);

        int[][] matrix = {{1, 0, 0}, {0, 1, 1}, {0, 1, 1}};
        int[][] matrix1 = {{1, 0, 0}, {0, 0, 1}, {1, 1, 0}};
        int[] dist = {2,4,6,4};
        int[] diff = {3, 3, 3};
        int[] speed = {5, 1, 5, 5, 1, 5, 3, 4, 5, 1, 4};
        char[][] matrix2 = {{'.', '.', 'W', '.', 'B', 'W', 'W', 'B'}, {'B', 'W', '.', 'W', '.', 'W', 'B', 'B'}, {'.', 'W', 'B', 'W', 'W', '.', 'W', 'W'}, {'W', 'W', '.', 'W', '.', '.', 'B', 'B'}, {'B', 'W', 'B', 'B', 'W', 'W', 'B', '.'}, {'W', '.', 'W', '.', '.', 'B', 'W', 'W'}, {'B', '.', 'B', 'B', '.', '.', 'B', 'B'}, {'.', 'W', '.', 'W', '.', 'W', '.', 'W'}};
        main.sumOfBeauties(dist);
        return;
    }

    public int sumOfBeauties(int[] nums) {
        int n = nums.length;
        // 下标 1 - (n - 2)
        // front all < now < all back   -> 2
        // front one < now < back one   -> 1
        // 记录前面的最小值，利用单调栈记录后面的
        Stack<Integer> stack = new Stack<>();  // 单调递增栈，存放下标
        int frontMax = nums[0];
        int[] acc = new int[n];
        for (int i = 1; i < n; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] >= nums[i]) {
                int index = stack.pop();
                if (index == i - 1) {
                    acc[index] = 0;
                } else {
                    acc[index] = 1;
                }
            }
            if (frontMax < nums[i]) {
                acc[i] = 2;
                stack.add(i);
                frontMax = nums[i];
            } else if (nums[i - 1] < nums[i]) {
                acc[i] = 1;
                stack.add(i);
            }
        }
        int ans = 0;
        for (int i = 1; i < n - 1; i++) {
            ans += acc[i];
        }
        return ans;
    }

    public int finalValueAfterOperations(String[] operations) {
        Set<String> down = new HashSet<>();
        Set<String> up = new HashSet<>();
        down.add("X--");
        down.add("--X");
        up.add("X++");
        up.add("++X");
        int ans = 0;
        for (String s : operations) {
            if (down.contains(s)) {
                ans--;
            }
            if (up.contains(s)) {
                ans++;
            }
        }
        return ans;
    }
}
