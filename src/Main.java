import beans.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        Main main = new Main();
        char a = 'a';
        int i = 96;
        TreeNode tn4 = new TreeNode(4);
        TreeNode tn3 = new TreeNode(3);
        TreeNode tn2 = new TreeNode(2, null, tn4);
        TreeNode tn1 = new TreeNode(1, tn2, tn3);

        int[][] matrix = {{2, 1, 1}, {2, 3, 1}, {3, 4, 1}};
        int[][] matrix1 = {{1, 0, 0}, {0, 0, 1}, {1, 1, 0}};
        int[] dist = {1, 0, 1};
        int[] diff = {0, 1, 0, 1, 0};
        int[] speed = {5, 1, 5, 5, 1, 5, 3, 4, 5, 1, 4};
        char[][] matrix2 = {{'.', '.', 'W', '.', 'B', 'W', 'W', 'B'}, {'B', 'W', '.', 'W', '.', 'W', 'B', 'B'}, {'.', 'W', 'B', 'W', 'W', '.', 'W', 'W'}, {'W', 'W', '.', 'W', '.', '.', 'B', 'B'}, {'B', 'W', 'B', 'B', 'W', 'W', 'B', '.'}, {'W', '.', 'W', '.', '.', 'B', 'W', 'W'}, {'B', '.', 'B', 'B', '.', '.', 'B', 'B'}, {'.', 'W', '.', 'W', '.', 'W', '.', 'W'}};
        main.waysToDecode("123");
        return;
    }

    /**
     * shopee 笔试二题
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
        return dfs(s, n, map, 0);
    }

    public int dfs(String s, int n, Map<Integer, Integer> map, int index) {
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
                ret = dfs(s, n, map, index + 2);
            } else if (t == 2 && nextT > 6) {
                ret = dfs(s, n, map, index + 1);
            } else {
                int ret1 = dfs(s, n, map, index + 1);
                int ret2 = dfs(s, n, map, index + 2);
                ret += ret1 + ret2;
            }
        } else {
            int ret1 = dfs(s, n, map, index + 1);
            ret += ret1;
        }
        map.put(index, ret);
        return ret;
    }

    /**
     * shopee 笔试一题
     * <p/>
     * <br/>数组array由0，1组成，求数组中最长的 "连续子序列"，使子序列中0和1的个数相等，返回其长度
     * <br/>e.g.
     * <br/>array = [0,1,0]
     * <br/>-> 2
     * <p/>
     *
     * @param array 数组
     * @return 最大
     */
    public int findMax(int[] array) {
        int n = array.length;
        int[] diff = new int[n + 1];  // diff records how much 1 more than 0
        if (n == 0) {
            return 0;
        }
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 0);
        int t;
        int max = 0;
        for (int i = 0; i < n; i++) {
            if (array[i] == 1) {
                t = diff[i] + 1;
                diff[i + 1] = t;
            } else {
                t = diff[i] - 1;
                diff[i + 1] = t;
            }
            if (map.containsKey(t)) {
                int t1 = map.get(t);
                max = Math.max(max, i + 1 - t1);
            } else {
                map.put(t, i + 1);
            }
        }
        return max;
    }
}
