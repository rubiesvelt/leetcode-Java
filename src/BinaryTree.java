import beans.TreeNode;
import utils.Utils;

import java.util.*;

public class BinaryTree {

    // 863. 二叉树中所有距离为 K 的结点
    // 求二叉树中距离 target 为 K 的结点；树建图，后从target遍历图
    Map<Integer, TreeNode> parents863 = new HashMap<>();
    List<Integer> ans863 = new ArrayList<>();

    public List<Integer> distanceK(TreeNode root, TreeNode target, int k) {
        // 从 root 出发 DFS，记录每个结点的父结点；BFS也可
        // 将图转化为邻接表，此接邻表前两条边连在节点上，第三条边存在Map里
        findParents(root);

        // 从 target 出发 DFS，寻找所有深度为 k 的结点
        findAns(target, null, 0, k);

        return ans863;
    }

    public void findParents(TreeNode node) {
        if (node.left != null) {
            parents863.put(node.left.val, node);
            findParents(node.left);
        }
        if (node.right != null) {
            parents863.put(node.right.val, node);
            findParents(node.right);
        }
    }

    // 遍历邻接表，from防回头
    public void findAns(TreeNode node, TreeNode from, int depth, int k) {
        if (node == null) {
            return;
        }
        if (depth == k) {
            ans863.add(node.val);
            return;
        }

        // 此邻接表前两条边连在节点上，第三条边存在Map里
        if (node.left != from) {
            findAns(node.left, node, depth + 1, k);
        }
        if (node.right != from) {
            findAns(node.right, node, depth + 1, k);
        }
        if (parents863.get(node.val) != from) {
            findAns(parents863.get(node.val), node, depth + 1, k);
        }
    }

    // 链式前向星
    // 链式前向星是一个存图方法，是 邻接表 和 邻接矩阵 之间一个折中的方案
    // 稀疏图比 邻接矩阵 省空间，比 邻接表 容易创建
    // 根据数据范围最多有 501 个点，每个点最多有 2 条无向边（两个子节点）
    int N = 510, M = N * 4;
    int[] he = new int[N];  // key: 节点值; value: index
    int[] e = new int[M];   // key: id; value: 指向的节点值
    int[] ne = new int[M];  // key: id; value: next id

    int idx;

    // 添加一条a到b的边
    void add(int a, int b) {
        e[idx] = b;
        ne[idx] = he[a];
        he[a] = idx++;
    }

    // 遍历一个节点指向的下一节点
    void edgeForNode(int a) {
        for (int i = he[a]; i != -1; i++) {  // he[] 初始化为 -1
            int j = e[i];  // 下一节点值
        }
    }

    boolean[] vis = new boolean[N];

    public List<Integer> distanceK1(TreeNode root, TreeNode t, int k) {
        List<Integer> ans = new ArrayList<>();
        Arrays.fill(he, -1);
        dfs(root);
        Deque<Integer> d = new ArrayDeque<>();
        d.addLast(t.val);
        vis[t.val] = true;
        while (!d.isEmpty() && k >= 0) {
            int size = d.size();
            while (size-- > 0) {
                int poll = d.pollFirst();
                if (k == 0) {
                    ans.add(poll);
                    continue;
                }
                for (int i = he[poll]; i != -1; i = ne[i]) {
                    int j = e[i];
                    if (!vis[j]) {
                        d.addLast(j);
                        vis[j] = true;
                    }
                }
            }
            k--;
        }
        return ans;
    }

    void dfs(TreeNode root) {
        if (root == null) return;
        if (root.left != null) {
            add(root.val, root.left.val);
            add(root.left.val, root.val);
            dfs(root.left);
        }
        if (root.right != null) {
            add(root.val, root.right.val);
            add(root.right.val, root.val);
            dfs(root.right);
        }
    }

    // 1104. 二叉树寻路
    public List<Integer> pathInZigZagTree(int label) {
        List<Integer> ans = new ArrayList<>();
        while (label > 0) {
            ans.add(label);
            label /= 2;
        }
        Collections.reverse(ans);
        for (int i = 0; i < ans.size(); i++) {
            int depth = i + 1;
            if ((depth & 1) == (ans.size() & 1)) {
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
