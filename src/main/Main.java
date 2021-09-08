package main;

import beans.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        Main main = new Main();
        char a = 'a';
        int i = 96;
        TreeNode tn4 = new TreeNode(4);
        TreeNode tn3 = new TreeNode(3);
        TreeNode tn2 = new TreeNode(1, null, null);
        TreeNode tn1 = new TreeNode(-2, tn2, null);

        int[][] matrix = {{1, 0, 0}, {0, 1, 1}, {0, 1, 1}};
        int[][] matrix1 = {{1, 0, 0}, {0, 0, 1}, {1, 1, 0}};
        int[] dist = {1, 0, 1};
        int[] diff = {0, 1, 0, 1, 0};
        int[] speed = {5, 1, 5, 5, 1, 5, 3, 4, 5, 1, 4};
        char[][] matrix2 = {{'.', '.', 'W', '.', 'B', 'W', 'W', 'B'}, {'B', 'W', '.', 'W', '.', 'W', 'B', 'B'}, {'.', 'W', 'B', 'W', 'W', '.', 'W', 'W'}, {'W', 'W', '.', 'W', '.', '.', 'B', 'B'}, {'B', 'W', 'B', 'B', 'W', 'W', 'B', '.'}, {'W', '.', 'W', '.', '.', 'B', 'W', 'W'}, {'B', '.', 'B', 'B', '.', '.', 'B', 'B'}, {'.', 'W', '.', 'W', '.', 'W', '.', 'W'}};
        main.maxPathSum(tn1);
        return;
    }

    // 美团一面
    // 给定一个二叉树，请计算节点值之和最大的路径的节点值之和是多少。
    // 这个路径的开始节点和结束节点可以是二叉树中的任意节点
    int ans = Integer.MIN_VALUE;

    public int maxPathSum(TreeNode root) {
        // write code here
        dfs(root);
        return ans;
    }

    public int dfs(TreeNode root) {
        // write code here
        if (root == null) {
            return 0;
        }
        int left = dfs(root.left);
        int right = dfs(root.right);
        int val = root.val;
        if (left > 0) {
            val += left;
        }
        if (right > 0) {
            val += right;
        }
        ans = Math.max(ans, val);
        return val;
    }
}
