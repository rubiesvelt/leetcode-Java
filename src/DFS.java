import beans.TreeNode;

import java.util.*;

public class DFS {

    /**
     * dfs 随想<p>
     * 首先需要想清楚遍历过程<br>
     * 1. 如何构建 & 遍历树？二叉树 —— 左右子树递归；非二叉树 —— 数组形式？其他数据结构形式？怎么表示这个树？for循环子节点进行递归<br>
     * 2. 边界条件？一般是达到树叶；二叉树 —— 节点为null；非二叉树 —— 到达边界条件；数组实现一般 height == size()，其他实现<br>
     * 3. 如何得到结果？<br>
     * 4. 如何进行剪枝？—— 大多时候剪枝是为了得到正确结果，而不是提高性能，所以可以归于 构建树 部分<p>
     *
     * 函数传递和保存在函数外一样吗<br>
     * 1. 函数传递对象，与保存在函数外，完全一样<br>
     * 2. 函数传递不可变类型，此时dfs函数传递，并不会破坏传递对象
     */

    // sb存储先序遍历
    public void dfsThink(TreeNode root, StringBuilder sb) {
        if (root == null) {
            return;
        }
        // 现在位于父节点，向下各个子节点
        sb.append(root.val);

        dfsThink(root.left, sb);
        dfsThink(root.right, sb);
    }

    // 回溯
    public void dfsBackTrackBinary(TreeNode root, List<TreeNode> t) {
        if (root == null) {
            return;
        }
        // 现在位于父节点，向下各个子节点
        t.add(root);  // 回溯

        dfsBackTrackBinary(root.left, t);
        dfsBackTrackBinary(root.right, t);

        t.remove(t.size() - 1);  // 回溯
    }

    // 回溯
    public boolean dfsBackTrackArray(int[] nums, int index, List<Integer> t) {
        // 递归边界
        if (index == nums.length) {
            return true;
        }
        for (int i = index; i < nums.length; i++) {  // 每一层中的元素；从下一个开始，而不是从最开头开始
            t.add(nums[i]);
            boolean b = dfsBackTrackArray(nums, i + 1, t);
            t.remove(t.size() - 1);
            if (b) {  // 返回值的妙用 —— 一条完整路径达成后，不再使用路径上的节点，则使用返回值来回溯
                return true;
            }
        }
        // fall through
        return false;
    }

    // 记忆化搜索 —— dp的前身
    Map<String, Long> map = new HashMap<>();

    public void memorySearch(int[][] points, int m, int n, int height, long sum) {
        if (height == m) {
            return;
        }
        int[] row = points[height];
        for (int i = 0; i < n; i++) {
            long t = sum + row[i];

            // 记忆化
            String key = height + "_" + i;
            if (map.getOrDefault(key, Long.MIN_VALUE) >= t) {
                continue;
            }
            map.put(key, t);

            memorySearch(points, m, n, height + 1, t);
        }
    }

    // 1947. 最大兼容性评分和
    // 回溯
    // 全排列

    // 排列 Anm 表示从n个元素中取出 m 个元素，"并将其按不同顺序排列" 的方案总数
    // Anm = n(n - 1)(n - 2)...(n - m + 1) = n! / (n - m)!

    // 组合 Cnm 表示从n个元素中取出 m 个元素，的方案总数
    // Cnm = Anm / m! = n! / m!(n - m)!
    // Cnm = Cn(n - m), n >= m
    public int maxCompatibilitySum(int[][] students, int[][] mentors) {
        int n = students.length;
        int m = students[0].length;
        int[][] preSum = new int[n][n];
        for (int i = 0; i < n; i++) {
            int[] student = students[i];
            for (int j = 0; j < n; j++) {
                int[] mentor = mentors[j];
                int t = 0;
                for (int k = 0; k < m; k++) {
                    if (student[k] == mentor[k]) t++;
                }
                preSum[i][j] = t;
            }
        }
        boolean[] used = new boolean[n];  // 使用过的老师
        dfs1947(preSum, n, 0, 0, used);
        return ans1947;
    }

    int ans1947 = 0;

    // 通过回溯来构造全排列
    public void dfs1947(int[][] preSum, int n, int sum, int cnt, boolean[] used) {
        if (cnt == n) {  // 第cnt个学生
            ans1947 = Math.max(ans1947, sum);
            return;
        }
        for (int i = 0; i < n; i++) {  // 第i个老师
            if (used[i]) {
                continue;
            }
            sum += preSum[cnt][i];
            used[i] = true;
            dfs1947(preSum, n, sum, cnt + 1, used);
            sum -= preSum[cnt][i];
            used[i] = false;
        }
    }

    // 可信 3
    // 回溯算法
    int ans3 = Integer.MAX_VALUE;

    /**
     * 有2 * num个表演队进行演出，每一个节目需要两个表演队配合完成，每一个表演队 只能 且 必须 表演一场节目
     * <br/>现给出节目列表，列表中每个元素中的三个数，分别为 {1队，2队，成本}
     * <br/>e.g. {0,1,250} 代表一场节目由0号和1号表演队完成，花费250
     * <br/>可从节目列表中选取若干节目，现求如何编排节目，能使 最高节目花费 最少，求最少的最高节目花费的值
     * <p/>
     * e.g.
     * <br/>num：2
     * <br/>program：{{0,1,250},{0,3,10},{1,2,25},{1,3,80},{2,3,90}}
     * <br/>结果：25
     * <p/>
     * @param num     数目
     * @param program 节目列表
     * @return 最低的 最高节目花费
     */
    public int cooperativePerformance(int num, int[][] program) {
        dfs3(program, num, program.length, -1, new HashSet<>(), -1);
        return ans3;
    }

    public void dfs3(int[][] program, int num, int n, int u, Set<Integer> used, int max) {
        if (max >= ans3) {  // 剪枝
            return;
        }
        if (used.size() == num * 2) {
            ans3 = max;
            return;
        }
        for (int i = u + 1; i < n; i++) {
            int[] show = program[i];
            if (used.contains(show[0]) || used.contains(show[1])) {
                continue;
            }
            used.add(show[0]);
            used.add(show[1]);
            int cost = show[2];
            dfs3(program, num, n, i, used, Math.max(max, cost));
            used.remove(show[0]);  // 回溯
            used.remove(show[1]);
        }
    }

    // 698. 划分为k个相等的子集
    // nums = [1,1,1,1,2,2,2,2], k = 4 是否能将nums数组划分为k个元素和相等的子集？
    // 回溯算法
    public boolean canPartitionKSubsets(int[] cp, int k) {
        Integer[] nums = new Integer[cp.length];
        int index = 0;
        for (int i : cp) {
            nums[index++] = i;
        }
        Arrays.sort(nums, (o1, o2) -> o2 - o1);
        int sum = 0;
        for (int n : nums) {
            sum += n;
        }
        if (sum % k != 0) {
            return false;
        }
        int t = sum / k;
        used = new boolean[nums.length];
        dfs698(nums, t, -1, new ArrayList<>(), 0);
        for (boolean b : used) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

    boolean[] used;

    // [4, 3, 2, 3, 5, 2, 1], k = 4
    public boolean dfs698(Integer[] num, int t, int index, List<Integer> list, int sum) {
        if (sum == t) {
            for (int i : list) {
                used[i] = true;
            }
            return true;
        }
        if (sum > t) {
            return false;
        }
        for (int i = index + 1; i < num.length; i++) {
            if (used[i]) {
                continue;
            }
            if (i > index + 1 && !used[i - 1] && num[i].equals(num[i - 1])) {
                continue;
            }
            sum += num[i];
            list.add(i);
            boolean ret = dfs698(num, t, i, list, sum);
            sum -= num[i];
            list.remove(list.size() - 1);
            if (ret && !list.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // 78. 子集
    // 给你一个整数数组 nums ，数组中的元素"互不相同" 。返回该数组所有可能的子集
    // 经典回溯
    List<List<Integer>> ans = new ArrayList<>();

    public List<List<Integer>> subsets(int[] nums) {
        List<Integer> current = new ArrayList<>();
        dfs78(nums, -1, current);
        return ans;
    }

    public void dfs78(int[] nums, int start, List<Integer> current) {
        ans.add(new ArrayList<>(current));

        for (int i = start + 1; i < nums.length; i++) {
            current.add(nums[i]);
            dfs78(nums, i, current);
            current.remove(current.size() - 1);  // List要用.size()，数组要用.length
        }
    }

    // 90. 子集 II
    // 给你一个整数数组 nums ，其中可能包含重复元素，请你返回该数组所有可能的子集
    // dfs, 回溯算法
    // 回溯算法，回溯一棵树，就是回溯一棵树的一个个从根到叶的路径，使用List记录路径元素
    List<List<Integer>> ans90 = new ArrayList<>();

    public List<List<Integer>> subsetsWithDup(int[] nums) {
        Arrays.sort(nums);
        dfs90(nums, 0, new ArrayList<>());
        return ans90;
    }

    public void dfs90(int[] nums, int index, List<Integer> t) {
        ans90.add(new ArrayList<>(t));
        // 节点的父节点相同，叫同一层

        // 1. 递归边界
        if (index == nums.length) {
            return;
        }

        // 现在位于父节点，向下各个子节点
        // 2. 在进行下层子节点遍历之前，的"初始化"代码，只会执行一次
        Set<Integer> set = new HashSet<>();

        for (int i = index; i < nums.length; i++) {  // 每一层中的元素；从下一个开始，而不是从最开头开始 —— 构建树的技巧

            // 3. 进行向下各个子节点遍历过程中，处理每个具体元素的代码
            if (set.contains(nums[i])) {  // 根据要求剪枝
                continue;
            }
            set.add(nums[i]);
            t.add(nums[i]);

            // 4. 进入下一层
            dfs90(nums, i + 1, t);  // 找到子节点 nums[i + 1] 进入下一层
            t.remove(t.size() - 1);  // 回溯
        }
    }

    // 可信 2.
    // 求二叉树节点值之和，平分路径节点除外
    // dfs，回溯算法
    public int bisectTreePath(TreeNode root) {
        List<TreeNode> list = new ArrayList<>();
        dfs2(root, list);
        for (TreeNode tn : mark) {
            sum -= tn.val;
        }
        return sum;
    }

    Set<TreeNode> mark = new HashSet<>();
    int sum = 0;

    public void dfs2(TreeNode root, List<TreeNode> list) {
        if (root == null) {
            return;
        }
        sum += root.val;
        list.add(root);  // 回溯
        if (root.left == null && root.right == null) {
            handleTnList(list);
        }

        // 进入下一层
        dfs2(root.left, list);
        dfs2(root.right, list);
        list.remove(list.size() - 1);  // 回溯
    }

    public void handleTnList(List<TreeNode> list) {
        int sum = 0;
        int acc = 0;
        for (TreeNode tn : list) {
            sum += tn.val;
        }
        for (TreeNode tn : list) {
            if (sum - tn.val == acc) {
                mark.add(tn);
            }
            sum -= tn.val;
            acc += tn.val;
        }
    }

    // LCP 07. 传递信息
    int ans07 = 0;
    int last;

    public int numWays(int n, int[][] relation, int k) {
        last = n;
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int[] re : relation) {
            if (!map.containsKey(re[0])) {
                List<Integer> list = new ArrayList<>();
                list.add(re[1]);
                map.put(re[0], list);
                continue;
            }
            List<Integer> list = map.get(re[0]);
            list.add(re[1]);
            map.put(re[0], list);
        }
        dfs07(0, 0, k, map);
        return ans07;
    }

    public void dfs07(int n, int i, int k, Map<Integer, List<Integer>> map) {
        if (i == k) {
            if (n == last - 1) {
                ans07++;
            }
            return;
        }
        List<Integer> list = map.get(n);
        if (list == null) return;
        for (int t : list) {
            dfs07(t, i + 1, k, map);
        }
    }

    // 08.08
    public String[] permutation(String S) {
        List<String> res = new ArrayList<String>();
        int len = S.length();
        if (len == 0) return new String[0];
        boolean[] used = new boolean[len];
        char[] sChar = S.toCharArray();

        StringBuilder path = new StringBuilder(len);

        // 排序是为了后面的剪枝
        Arrays.sort(sChar);

        dfs(res, sChar, len, path, 0, used);
        return res.toArray(new String[0]);
    }

    /**
     * @param res   结果集
     * @param sChar 输入字符数组
     * @param len   字符数组长度
     * @param path  根结点到任意结点的路径
     * @param depth 当前树的深度
     * @param used  使用标记数组
     */
    private void dfs(List<String> res
            , char[] sChar
            , int len
            , StringBuilder path
            , int depth
            , boolean[] used) {
        // 到达叶子结点
        if (depth == len) {
            res.add(path.toString());
            return;
            // 递归出口
        }
        // 命运的车轮滚到哪里了？
        // 模拟
        // sChar = aabc aacb abac abca acab acba baac baca bcaa caab caba cbaa
        for (int i = 0; i < len; i++) {
            if (!used[i]) {
                // 根据已排序字符数组剪枝，跳过a1 a2 ... 和 a2 a1 ...
                if (i > 0 && sChar[i] == sChar[i - 1] && !used[i - 1]) {
                    continue;
                }
                path.append(sChar[i]);
                used[i] = true; // 标记选择
                dfs(res, sChar, len, path, depth + 1, used);
                path.deleteCharAt(depth);
                used[i] = false; // 撤销选择
            }
        }
    }

    // 993. 二叉树的堂兄弟节点
    public boolean isCousins(TreeNode root, int x, int y) {
        xVal993 = x;
        yVal993 = y;
        dfs993(root, root, 0);
        return xDepth == yDepth && xParent != yParent;
    }

    public int xVal993;
    public int yVal993;
    public int xDepth;
    public int yDepth;
    public int xParent;
    public int yParent;

    public void dfs993(TreeNode root, TreeNode parent, int depth) {
        if (root == null) return;
        if (root.val == xVal993) {
            xDepth = depth;
            xParent = parent.val;
        } else if (root.val == yVal993) {
            yDepth = depth;
            yParent = parent.val;
        }
        dfs993(root.left, root, depth + 1);
        dfs993(root.right, root, depth + 1);
    }

    // 1723. 完成所有工作的最短时间
    // dfs
    int[] jobs;
    int n1723, k;
    int ans1723 = 0x3f3f3f3f;

    public int minimumTimeRequired(int[] _jobs, int _k) {
        jobs = _jobs;
        n1723 = jobs.length;
        k = _k;
        int[] sum = new int[k];
        dfs1723(0, 0, sum, 0);
        return ans1723;
    }

    /**
     * 回溯 + 剪枝
     *
     * @param u    当前处理到那个 job
     * @param used 当前分配给了多少个工人了
     * @param sum  工人的分配情况。例如：sum[0] = x 代表 0 号工人工作量为 x
     * @param max  当前的「最大工作时间」
     */
    void dfs1723(int u, int used, int[] sum, int max) {
        if (max >= ans1723) return;
        if (u == n1723) {
            ans1723 = max;
            return;
        }
        /*
         * 剪枝 —— 优先分配给「空闲工人」
         * 第一份工作必定分给第一个人 只扫过一半的树、更好的利用剪枝条件
         */
        if (used < k) {
            sum[used] = jobs[u];
            dfs1723(u + 1, used + 1, sum, Math.max(sum[used], max));
            sum[used] = 0;  // 回溯
        }
        // 当 used = k 进入此处时，意味对子树的完全遍历
        // 当 used < k 进入此处
        // i 号工人做 u 号工作
        for (int i = 0; i < used; i++) {
            sum[i] += jobs[u];
            dfs1723(u + 1, used, sum, Math.max(sum[i], max));
            sum[i] -= jobs[u];
        }
    }

    // 690. 员工的重要性
    // dfs
    int ans690 = 0;
    Map<Integer, Employee> map690 = new HashMap<>();  // 与其每次查找，不如一次直接先加载到map里

    public int getImportance(List<Employee> employees, int id) {
        for (Employee employee : employees) {
            map690.put(employee.id, employee);
        }
        return dfs690(employees, id);
    }

    public int dfs690(List<Employee> employees, int id) {
        Employee current = map690.get(id);
        ans690 += current.importance;
        for (int i : current.subordinates) {
            dfs690(employees, i);
        }
        return ans690;
    }

    static class Employee {
        public int id;
        public int importance;
        public List<Integer> subordinates;
    }
}
