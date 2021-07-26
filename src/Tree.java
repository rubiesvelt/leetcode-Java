import beans.TreeNode;
import utils.Utils;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Tree {

    // 297. 二叉树的序列化与反序列化
    // BFS version
    public static String serialize2(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        Deque<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode tn = queue.poll();
            if (tn.val == -1001) {
                sb.append("null ");
                continue;
            }
            boolean b = tn.left == null ? queue.add(new TreeNode(-1001)) : queue.add(tn.left);
            b = tn.right == null ? queue.add(new TreeNode(-1001)) : queue.add(tn.right);
            sb.append(tn.val + " ");
        }
        return sb.toString();
    }

    // 1 2 3 null null 4 5 null null null null
    public static TreeNode deserialize2(String data) {
        String[] strings = data.split(" ");
        Deque<Integer> queue = new LinkedList();
        for (String str : strings) {
            if (!str.equals("null")) {
                queue.add(Integer.parseInt(str));
            } else {
                queue.add(-1001);
            }
        }
        TreeNode root = new TreeNode(queue.poll());
        Deque<TreeNode> bfsQueue = new LinkedList();  // bfs 需要一个装树节点的队列
        bfsQueue.add(root);
        while (!bfsQueue.isEmpty()) {
            TreeNode tn = bfsQueue.poll();
            Integer left = queue.poll();
            Integer right = queue.poll();
            if (left != -1001) {
                tn.left = new TreeNode(left);
                bfsQueue.add(tn.left);
            }
            if (right != -1001) {
                tn.right = new TreeNode(right);
                bfsQueue.add(tn.right);
            }
        }
        return root;
    }

    // DFS version
    // Encodes a tree to a single string.
    StringBuilder sb = new StringBuilder();

    public String serialize(TreeNode root) {
        dfs37(root);
        System.out.println(sb.toString());
        return sb.toString();
    }

    public void dfs37(TreeNode tn) {
        if (tn == null) {
            sb.append(". ");
            return;
        }
        sb.append(tn.val).append(" ");
        dfs37(tn.left);
        dfs37(tn.right);
    }

    // Decodes your encoded data to tree.
    // 12.4..3..
    public TreeNode deserialize(String data) {
        String[] ss = data.trim().split(" ");
        return dfs01(ss);
    }

    int index = 0;

    public TreeNode dfs01(String[] ss) {
        String s = ss[index++];
        if (s.equals(".")) {
            return null;
        }
        TreeNode tn = new TreeNode(Utils.stringToInt(s));
        tn.left = dfs01(ss);
        tn.right = dfs01(ss);
        return tn;
    }

    public static TreeNode getNode(int val) {
        if (val == -1) return null;
        else return new TreeNode(val, null, null);
    }

    // 173. 二叉搜索树迭代器
    // 解法一，直接遍历
    private int index1 = 0;
    private final List<Integer> arr = new ArrayList<>();

    public void BSTIterator1(TreeNode root) {
        inorderTraversal(root, arr);
    }

    public int next1() {
        return arr.get(index1++);
    }

    public boolean hasNext1() {
        return index1 < arr.size();
    }

    private void inorderTraversal(TreeNode root, List<Integer> arr) {  // 二叉树中序遍历
        if (root == null) {
            return;
        }
        inorderTraversal(root.left, arr);
        arr.add(root.val);
        inorderTraversal(root.right, arr);
    }

    // 解法二
    private TreeNode cur;
    private Deque<TreeNode> stack173;

    public void BSTIterator2(TreeNode root) {
        cur = root;
        stack173 = new LinkedList<TreeNode>();
    }

    public int next2() {  // 使用栈，动态中序遍历
        while (cur != null) {
            stack173.push(cur);
            cur = cur.left;
        }
        cur = stack173.pop();
        int ret = cur.val;
        cur = cur.right;
        return ret;
    }

    public boolean hasNext2() {
        return cur != null || !stack173.isEmpty();
    }

    // 563.二叉树的坡度
    public static int findTilt(TreeNode root) {
        reformat(root);
        return calSum(root);
    }

    public static int reformat(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = reformat(root.left);
        int right = reformat(root.right);
        int val = root.val;
        root.val = Math.abs(left - right);
        return left + right + val;
    }

    public static int calSum(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = calSum(root.left);
        int right = calSum(root.right);
        return left + right + root.val;
    }

    // 938. 二叉搜索树的范围和
    // 二叉搜索树，dfs
    int sum938 = 0;

    public int rangeSumBST(TreeNode root, int low, int high) {
        if (root == null) {
            return sum938;
        }
        if (root.val > high) {
            return sum938;
        }
        rangeSumBST(root.left, low, high);
        if (low <= root.val && root.val <= high) {
            sum938 += root.val;
        }
        rangeSumBST(root.right, low, high);
        return sum938;
    }


    // 897. 递增顺序搜索树
    // 二叉树，中序遍历
    static TreeNode tn = new TreeNode();
    static TreeNode next = tn;

    public static TreeNode increasingBST(TreeNode root) {
        if (root == null) {
            return null;
        }
        increasingBST(root.left);
        next.right = new TreeNode(root.val);  // 注意要new一个
        next = next.right;
        increasingBST(root.right);
        return tn.right;
    }

}
