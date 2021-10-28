import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DFSPermutations {

    /*
     * 1947. 最大兼容性评分和
     * m 个学生，m 个导师，每个人做 n 道题，题的答案可为 0 或 1，记录在 students[][] 和 mentors[][] 中
     * 如果 学生 和 导师 对于同一题的答案一致，则其兼容性 + 1；如学生 [1, 1, 0] 和导师 [1, 0, 0] 的兼容性为 2
     * 现将学生分配给导师，求最大兼容性平分和
     *
     * 回溯
     * 全排列
     *
     * 排列 Anm 表示从 n 个元素中取出 m 个元素，"并将其按不同顺序排列" 的方案总数
     * Anm = n(n - 1)(n - 2)...(n - m + 1) = n! / (n - m)!
     *
     * 组合 Cnm 表示从 n 个元素中取出 m 个元素，的方案总数
     * Cnm = Anm / m! = n! / m!(n - m)!
     * Cnm = Cn(n - m), n >= m
     */
    public int maxCompatibilitySum(int[][] students, int[][] mentors) {
        int n = students.length;
        int m = students[0].length;
        int[][] preSum = new int[n][n];  // 预处理；preSum[i][j] 为学生 i 与导师 j 的兼容性
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
        dfs1947(n, 0, 0, used, preSum);
        return ans1947;
    }

    int ans1947 = 0;

    // 将老师全排列，和学生去匹配（将学生全排列也可以）
    public void dfs1947(int n, int sum, int cnt, boolean[] used, int[][] preSum) {
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
            dfs1947(n, sum, cnt + 1, used, preSum);
            sum -= preSum[cnt][i];
            used[i] = false;
        }
    }

    // 77. 组合
    // 给定两个整数 n 和 k，返回范围 [1, n] 中所有可能的 k 个数的组合
    // 回溯
    List<List<Integer>> ans = new ArrayList<>();

    public List<List<Integer>> combine(int n, int k) {
        int[] num = new int[n];
        for (int i = 0; i < n; i++) {
            num[i] = i + 1;
        }
        List<Integer> current = new ArrayList<>();
        dfs77(num, k, 0, current);
        return ans;
    }

    public void dfs77(int[] num, int k, int index, List<Integer> current) {
        if (current.size() == k) {
            ans.add(new ArrayList<>(current));
            return;
        }

        for (int i = index; i < num.length; i++) {
            current.add(num[i]);
            dfs77(num, k, i + 1, current);
            current.remove(current.size() - 1);
        }
    }

    // 08.08. 有重复字符串的排列组合
    // e.g.
    // S = "qqe"
    // -> ["eqq", "qeq", "qqe"]
    public String[] permutation(String S) {
        List<String> res = new ArrayList<>();
        int len = S.length();
        if (len == 0) return new String[0];
        boolean[] used = new boolean[len];
        char[] sChar = S.toCharArray();

        StringBuilder path = new StringBuilder(len);

        // 排序是为了后面的剪枝
        Arrays.sort(sChar);

        dfs08(path, 0, used, res, sChar, len);
        return res.toArray(new String[0]);
    }

    /**
     * @param res   结果集
     * @param sChar 输入字符数组
     * @param len   字符数组长度
     * @param path  根结点到任意结点的路径
     * @param u     当前树的深度
     * @param used  使用标记数组
     */
    public void dfs08(StringBuilder path, int u, boolean[] used, List<String> res, char[] sChar, int len) {
        // 到达叶子结点
        if (u == len) {
            res.add(path.toString());
            return;
            // 递归出口
        }
        for (int i = 0; i < len; i++) {
            if (!used[i]) {
                // 根据已排序字符数组剪枝，跳过a1 a2 ... 和 a2 a1 ...
                if (i > 0 && sChar[i] == sChar[i - 1] && !used[i - 1]) {
                    continue;
                }
                path.append(sChar[i]);
                used[i] = true;  // 标记选择
                dfs08(path, u + 1, used, res, sChar, len);
                path.deleteCharAt(u);
                used[i] = false;  // 撤销选择
            }
        }
    }
}
