package main;

import beans.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
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

    // 剑指 Offer 07. 重建二叉树
    // 给定先序遍历和中序遍历，重建二叉树
    // 利用原理,先序遍历的第一个节点就是根。在中序遍历中通过根 区分哪些是左子树的，哪些是右子树的
    // 左右子树，递归
    HashMap<Integer, Integer> map = new HashMap<>();  //标记中序遍历
    int[] preorder;  // 保留的先序遍历

    // preorder = [3, 9, 20, 15, 7]
    // inorder = [9, 3, 15, 20, 7]
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        this.preorder = preorder;
        for (int i = 0; i < preorder.length; i++) {
            map.put(inorder[i], i);
        }
        return recursive(0,0,inorder.length-1);
    }

    /**
     * @param pre_root_idx  先序遍历的索引
     * @param in_left_idx  中序遍历的索引
     * @param in_right_idx 中序遍历的索引
     */
    public TreeNode recursive(int pre_root_idx, int in_left_idx, int in_right_idx) {
        // 相等就是自己
        if (in_left_idx > in_right_idx) {
            return null;
        }
        // root_idx是在先序里面的
        TreeNode root = new TreeNode(preorder[pre_root_idx]);
        // 有了先序的，再根据先序的，在中序中获 当前根的索引
        int idx = map.get(preorder[pre_root_idx]);

        // 左子树的根节点就是 左子树的(前序遍历）第一个, 就是+1, 左边边界就是left, 右边边界是中间区分的idx-1
        root.left = recursive(pre_root_idx + 1, in_left_idx, idx - 1);

        // 由根节点在中序遍历的idx 区分成2段, idx 就是根

        // 右子树的根，就是右子树（前序遍历）的第一个, 就是当前根节点 加上左子树的数量
        // pre_root_idx 当前的根  左子树的长度 = 左子树的左边-右边 (idx-1 - in_left_idx +1) 。最后+1就是右子树的根了
        root.right = recursive(pre_root_idx + (idx-1 - in_left_idx +1)  + 1, idx + 1, in_right_idx);
        return root;
    }

    // 美团一面
    // 给定一个二叉树，请计算节点值之和最大的路径的节点值之和是多少
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
