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

    /**
     * 今天我们介绍了关于 ZK 持久化的知识：
     * ZK 会持久化到磁盘的文件有两种：log 和 snapshot
     * log 负责记录每一个写请求
     * snapshot 负责对当前整个内存数据进行快照
     * 恢复数据的时候，会先读取最新的 snapshot 文件
     * 然后在根据 snapshot 最大的 zxid 去搜索符合条件的 log 文件，再通过逐条读取写请求来恢复剩余的数据
     *
     *
     *
     * 相比于别人，你的优势是什么？
     * - 优势：技术充满兴趣，能够沉下心搞懂一件东西；做事情比较有目标感，目标明确
     *
     *
     * 你的缺点是什么？
     * - 缺点：喜欢独处，希望有一些独处的空间。
     *
     *
     * 为什么选择腾讯公司？
     * - 腾讯公司是互联网巨头之一，业务覆盖生活的方方面面，能接触互联网最前沿的技术；
     * 腾讯比较年轻，开放，包容的公司，里面有很多很厉害的人；
     * 腾讯对员工的关怀方面做的比较好
     *
     *
     * 对行业（腾讯游戏）发展的看法？
     * - 腾讯游戏做的比较好，无论在手游上还是端游，我自己也经常玩腾讯的游戏。
     *   游戏从电脑游戏切换到手机游戏，后续可能有更丰富的形式；
     *   游戏的内容会做的更加多样化，能够更加吸引玩家，丰富大家的业余生活。
     *   丰富大家业余生活，使大家学到更多的东西，为大家生活增添色彩，增添灵感和想象力
     *
     *
     * 为什么离职？
     * - 原部门业务稳定，技术栈有限，对大数据的应用也是有限的。希望在腾讯游戏能学到更多东西
     *
     *
     * 职业生涯的规划？
     * - 在腾讯能得到更好的发展，和女朋友同时在找工作，去过深圳，感觉比较好干净，开放，包容 ，如果有机会希望能在深圳定居
     *
     *
     * 你和职业生涯下一步的差距在哪里？
     * - 经验的积累，技术的提升，在技术深度方面有提升空间，了解业界最新技术，向前沿看齐
     *
     *
     * 你有什么问题想要了解的？
     * - 绩效的考核机制；
     * - 腾讯游戏有没有海外业务，海外出差支持的机会？
     *
     *
     * 人生理想
     * - 多体验，遇见更多的人，更多的事，探索更深入的技术
     */

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
        return recursive(0, 0, inorder.length - 1);
    }

    /**
     * @param preRootIndex 根节点索引
     * @param InLeftIndex  左边（到头）索引
     * @param InRightIndex 右边（到头）索引
     */
    public TreeNode recursive(int preRootIndex, int InLeftIndex, int InRightIndex) {
        // 相等就是自己
        if (InLeftIndex > InRightIndex) {
            return null;
        }
        // root_idx是在先序里面的
        TreeNode root = new TreeNode(preorder[preRootIndex]);
        // 有了先序的，再根据先序的，在中序中获 当前根的索引
        int idx = map.get(preorder[preRootIndex]);

        // 左子树的根节点就是 左子树的(前序遍历）第一个, 就是+1, 左边边界就是left, 右边边界是中间区分的idx-1
        root.left = recursive(preRootIndex + 1, InLeftIndex, idx - 1);

        // 由根节点在中序遍历的idx 区分成2段, idx 就是根

        // 右子树的根，就是右子树（前序遍历）的第一个, 就是当前根节点 加上左子树的数量
        // preRootIndex 当前的根  左子树的长度 = 左子树的左边-右边 (idx-1 - InLeftIndex +1) 。最后+1就是右子树的根了
        root.right = recursive(preRootIndex + (idx - InLeftIndex) + 1, idx + 1, InRightIndex);
        return root;
    }

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
