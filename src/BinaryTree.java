import beans.TreeNode;

import java.util.*;

public class BinaryTree {

    /*
     * shopee 二面. z 字形遍历二叉树
     * 二叉树层序遍历，从根开始，第一层从左到右，第二层从右到左...求最终结果序列
     */
    public static class TreeNodeWrapper {
        public TreeNode treeNode;
        public int level;

        public TreeNodeWrapper(TreeNode treeNode, int level) {
            this.treeNode = treeNode;
            this.level = level;
        }
    }

    public List<Integer> traverse(TreeNode root) {

        Queue<TreeNodeWrapper> q = new LinkedList<>();
        q.offer(new TreeNodeWrapper(root, 0));
        List<List<Integer>> lists = new ArrayList<>();

        while (!q.isEmpty()) {
            TreeNodeWrapper wrapper = q.poll();
            TreeNode tn = wrapper.treeNode;
            int level = wrapper.level;
            int value = tn.val;
            if (level == lists.size()) {
                List<Integer> list = new ArrayList<>();
                list.add(value);
                lists.add(list);
            } else {
                List<Integer> list = lists.get(level);
                list.add(value);
            }
            if (tn.left != null) q.offer(new TreeNodeWrapper(tn.left, level + 1));
            if (tn.right != null) q.offer(new TreeNodeWrapper(tn.right, level + 1));
        }

        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            List<Integer> list = lists.get(i);
            if (i % 2 == 1) {
                Collections.reverse(list);
            }
            ans.addAll(list);
        }
        return ans;
    }

    /*
     * 美团一面. 寻找二叉树最大路径
     * 给定一个二叉树，请计算节点值之和最大的路径的节点值之和是多少
     * 这个路径的开始节点和结束节点可以是二叉树中的任意节点
     *
     * 后序遍历
     */
    int ans = Integer.MIN_VALUE;

    public int maxPathSum(TreeNode root) {
        dfsmt(root);
        return ans;
    }

    public int dfsmt(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = dfsmt(root.left);
        int right = dfsmt(root.right);
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

    // 863. 二叉树中所有距离为 K 的结点
    // 求二叉树中距离 target 为 K 的结点，树上的每个结点都具有唯一的值
    //
    // 树建图，后从target遍历图
    List<Integer> ans863 = new ArrayList<>();

    public List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
        // 从 root 出发 DFS，记录每个结点的父结点；BFS也可
        // 将图转化为邻接表，此接邻表前两条边连在节点上，第三条边存在Map里
        Map<Integer, TreeNode> parents = new HashMap<>();
        findParents(root, parents);

        // 从 target 出发 DFS，寻找所有深度为 k 的结点
        findAns(target, parents, null, 0, k);
        return ans863;
    }

    public void findParents(TreeNode node, Map<Integer, TreeNode> parents) {
        if (node.left != null) {
            parents.put(node.left.val, node);
            findParents(node.left, parents);
        }
        if (node.right != null) {
            parents.put(node.right.val, node);
            findParents(node.right, parents);
        }
    }

    // 遍历邻接表，from 防回头
    public void findAns(TreeNode node, Map<Integer, TreeNode> parents, TreeNode from, int depth, int k) {
        if (node == null) {
            return;
        }
        if (depth == k) {
            ans863.add(node.val);
            return;
        }

        // 此邻接表前两条边连在节点上，第三条边存在Map里
        if (node.left != from) {
            findAns(node.left, parents, node, depth + 1, k);
        }
        if (node.right != from) {
            findAns(node.right, parents, node, depth + 1, k);
        }
        if (parents.get(node.val) != from) {
            findAns(parents.get(node.val), parents, node, depth + 1, k);
        }
    }

    // 1104. 二叉树寻路
    // 二叉树
    // 奇数行（即，第一行、第三行、第五行……）中，按从左到右的顺序进行标记
    // 偶数行（即，第二行、第四行、第六行……）中，按从右到左的顺序进行标记
    // 给定一个数 label 求 从底到顶的路径
    public List<Integer> pathInZigZagTree(int label) {
        List<Integer> ans = new ArrayList<>();
        while (label > 0) {
            ans.add(label);  // 1, 3, 7, 14
            label /= 2;
        }
        Collections.reverse(ans);
        for (int i = 0; i < ans.size(); i++) {
            int depth = i + 1;
            if ((depth & 1) == (ans.size() & 1)) {  // 由于最后一位不需要反转，所以前面相邻的位需要反转
                continue;
            }
            int n = ans.get(i);
            int left = (int) Math.pow(2, i);
            int right = 2 * left - 1;
            n = right + left - n;
            ans.set(i, n);
        }
        return ans;
    }

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

    // 173. 二叉搜索树迭代器
    // 解法一，中序遍历，先将结果存在 arr 中
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

    // 解法二，使用栈，动态中序遍历
    private TreeNode cur;
    private Deque<TreeNode> stack173;

    public void BSTIterator2(TreeNode root) {
        cur = root;
        stack173 = new LinkedList<>();
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
    public int findTilt(TreeNode root) {
        reformat(root);
        return calSum(root);
    }

    public int reformat(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = reformat(root.left);
        int right = reformat(root.right);
        int val = root.val;
        root.val = Math.abs(left - right);
        return left + right + val;
    }

    public int calSum(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = calSum(root.left);
        int right = calSum(root.right);
        return left + right + root.val;
    }

    // 938. 二叉搜索树的范围和
    // 给定二叉搜索树的根结点 root，返回值位于范围 [low, high] 之间的所有结点的值的和
    // 二叉搜索树，dfs
    int sum938 = 0;

    public int rangeSumBST(TreeNode root, int low, int high) {
        if (root == null) {
            return 0;
        }

        if (root.val > low) {
            rangeSumBST(root.left, low, high);
        }
        if (low <= root.val && root.val <= high) {
            sum938 += root.val;
        }
        if (root.val < high) {
            rangeSumBST(root.right, low, high);
        }
        return sum938;
    }


    // 897. 递增顺序搜索树
    // 把一个二叉搜索树"捋直"，使一个个节点向右递增
    // 二叉树，中序遍历
    TreeNode tn = new TreeNode();  // 所有的祖先
    TreeNode next = tn;

    public TreeNode increasingBST(TreeNode root) {
        if (root == null) {
            return null;
        }
        increasingBST(root.left);

        next.right = new TreeNode(root.val);  // 注意要new一个
        next = next.right;

        increasingBST(root.right);
        return tn.right;
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
}
