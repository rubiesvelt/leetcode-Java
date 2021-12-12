import beans.TreeNode;

import java.util.*;

public class DFS {

    /**
     * dfs 随想
     *     
     * 首先需要想清楚遍历过程
     * 1. 如何构建 & 遍历树？二叉树 —— 左右子树递归；非二叉树 —— 数组形式？其他数据结构形式？怎么表示这个树？for循环子节点进行递归
     * 2. 边界条件？一般是达到树叶；二叉树 —— 节点为null；非二叉树 —— 到达边界条件；数组实现一般 height == size()，其他实现
     * 3. 如何得到结果？
     * 4. 如何进行剪枝？—— 大多时候剪枝是为了得到正确结果，而不是提高性能，所以可以归于 构建树 部分<p>
     *
     * 函数传递和保存在函数外一样吗
     * 1. 函数传递对象，与保存在函数外，完全一样
     * 2. 函数传递不可变类型，此时dfs函数传递，并不会破坏传递对象
     * 
     * 
     * dfs常见场景？
     * 遍历 爆搜
     * 
     * 常见数据结构？
     * 树
     * 字符串
     * 数组
     * 
     * 常见解决方案？
     * 树的遍历
     * 用/不用（删除无效的括号）
     * 选这个/选那个（排列组合）
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

    /*
     * 5936. 引爆最多的炸弹
     * 构建图，从图的每一个顶点开启爆搜
     */
    public int maximumDetonation(int[][] bombs) {
        List<List<Integer>> graph = new ArrayList<>();
        int n = bombs.length;
        for (int i = 0; i < n; i++) {
            List<Integer> list = new ArrayList<>();
            int a1 = bombs[i][0];
            int b1 = bombs[i][1];
            int r = bombs[i][2];
            for (int j = 0; j < n; j++) {
                if (i == j) continue;
                int a2 = bombs[j][0];
                int b2 = bombs[j][1];
                if (reach(a1, b1, a2, b2, r)) {
                    list.add(j);
                }
            }
            graph.add(i, list);
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            Set<Integer> visited = new HashSet<>();
            int now = dfs(i, visited, graph);
            ans = Math.max(ans, now);
        }
        return ans;
    }

    public int dfs(int cur, Set<Integer> visited, List<List<Integer>> graph) {
        visited.add(cur);
        List<Integer> list = graph.get(cur);
        for (int next : list) {
            if (visited.contains(next)) continue;
            dfs(next, visited, graph);
        }
        return visited.size();
    }

    public boolean reach(int a1, int b1, int a2, int b2, int r) {
        long a12 = a1 - a2;
        long b12 = b1 - b2;
        long x2 = a12 * a12;
        long y2 = b12 * b12;
        long r2 = (long) r * (long) r;
        return r2 >= x2 + y2;
    }

    /*
     * 301. 删除无效的括号
     * 给你一个由若干括号和字母组成的字符串 s ，删除 最小数量 的无效括号，使得输入的字符串有效。
     * 返回所有可能的结果。答案可以按 任意顺序 返回。
     *
     * e.g.
     * s = "()()()((("
     * -> ["(())()","()()()"]
     *
     * s = "(a)())()"
     * -> ["(a())()","(a)()()"]
     *
     * s = ")("
     * -> [""]
     *
     * 括号问题
     *
     * 删除最小数量的无用括号 —— 可求解 需要删掉的 左右括号数量
     * 针对s中每个括号 '(', ')' 可以有 选/不选 两种 —— 可进行 dfs
     * 从前到后，'(' 加 1 分，')' 减 1 分，分值不可少于 0 ，也不可大于 min('('数，')'数) —— 保证方案合理 & 剪枝
     * 到最终形态，左括号数量 等于 右括号数量 —— 判定最终方案合理性
     */
    Set<String> ans301 = new HashSet<>();

    public List<String> removeInvalidParentheses(String s) {
        int n = s.length();
        int l = 0;  // l 与 r 表示分别需要去掉多少左，右括号
        int r = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') {
                l++;
            } else if (c == ')') {
                if (l > 0) l--;
                else r++;
            }
        }
        int len = n - l - r;  // len 表示剩下的长度
        int c1 = 0, c2 = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') c1++;
            if (c == ')') c2++;
        }
        int max = Math.min(c1, c2);
        dfs301(0, "", l, r, 0, len, max, s);
        return new ArrayList<>(ans301);
    }

    public void dfs301(int u, String cur, int l, int r, int score, int len, int max, String s) {
        if (l < 0 || r < 0 || cur.length() > len || score > max || score < 0) {
            return;
        }
        if (u == s.length()) {
            if (cur.length() == len && l == 0 && r == 0) {
                ans301.add(cur);
            }
            return;
        }
        if (s.charAt(u) == '(') {
            dfs301(u + 1, cur + '(', l, r, score + 1, len, max, s);  // 使用此括号
            dfs301(u + 1, cur, l - 1, r, score, len, max, s);  // 不使用
        } else if (s.charAt(u) == ')') {
            dfs301(u + 1, cur + ')', l, r, score - 1, len, max, s);
            dfs301(u + 1, cur, l, r - 1, score, len, max, s);
        } else {
            dfs301(u + 1, cur + s.charAt(u), l, r, score, len, max, s);  // 遇到非括号，直接使用
        }
    }

    /**
     * shopee 笔试二题<br/>
     * 回溯
     * <p/>给出 由数字组成的，编码后的 字符串s，求字符串解码的方案数目
     * <br/>编码规则
     * <br/>a -> 1
     * <br/>b -> 2
     * <br/>...
     * <br/>z -> 26
     * <br/>
     * e.g.
     * s = "12" -> 2 (can be "a, b" or "l")
     * <p/>
     *
     * @param s 编码后的 由数字组成的 字符串
     * @return 解码的方案数目
     */
    public int waysToDecode(String s) {
        int n = s.length();
        Map<Integer, Integer> map = new HashMap<>();  // record index -> totalNum
        return dfs2(s, n, map, 0);
    }

    public int dfs2(String s, int n, Map<Integer, Integer> map, int index) {
        if (map.containsKey(index)) {
            return map.get(index);
        }
        if (index == n - 1) {
            return 1;
        } if (index == n) {
            return 1;
        } if (index > n) {
            return 0;
        }

        char c = s.charAt(index);
        int t = c - '0';
        int ret = 0;
        if (t == 1 || t == 2) {
            int nextT = s.charAt(index + 1) - '0';
            if (nextT == 0) {
                ret = dfs2(s, n, map, index + 2);
            } else if (t == 2 && nextT > 6) {
                ret = dfs2(s, n, map, index + 1);
            } else {
                int ret1 = dfs2(s, n, map, index + 1);
                int ret2 = dfs2(s, n, map, index + 2);
                ret += ret1 + ret2;
            }
        } else {
            int ret1 = dfs2(s, n, map, index + 1);
            ret += ret1;
        }
        map.put(index, ret);
        return ret;
    }

    // 可信 3
    // 回溯算法
    int ans3 = Integer.MAX_VALUE;

    /*
     * 有2 * num个表演队进行演出，每一个节目需要两个表演队配合完成，每一个表演队 只能 且 必须 表演一场节目<br/>
     * 现给出节目列表，列表中每个元素中的三个数，分别为 {1队，2队，成本}<br/>
     * e.g. {0,1,250} 代表一场节目由0号和1号表演队完成，花费250<br/>
     * 可从节目列表中选取若干节目，现求如何编排节目，能使 最高节目花费 最少，求最少的最高节目花费的值<p/>
     *
     * e.g.<br/>
     * num：2<br/>
     * program：{{0,1,250},{0,3,10},{1,2,25},{1,3,80},{2,3,90}}<br/>
     * 结果：25<p/>
     *
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
