import beans.TreeNode;

import java.util.*;

public class BFS {

    /*
     * Level Order Traversal
     * 层序遍历 = BFS
     */
    public List<Integer> levelOrder(TreeNode root) {
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        List<Integer> ans = new ArrayList<>();
        while (!q.isEmpty()) {
            TreeNode n = q.poll();
            ans.add(n.val);
            if (n.left != null) q.add(n.left);
            if (n.right != null) q.add(n.right);
        }
        Collections.reverse(ans);
        return ans;

    }

    /*
     * 2045. 到达目的地的第二短时间
     * 一个城市是有 n 个节点的双向连通图，以 edges[][] 表示，如[[1,2][2,3]]表示 1，2 和 2，3 节点之间有路
     * 节点间路径每次通过耗时为 time，
     * 城市中红绿灯在 time = 0 时为绿灯，每隔change时间改变一次
     * 求节点 1 到节点 n 需要的 第二短时间
     *
     * BFS最短路
     */
    public int secondMinimum(int n, int[][] edges, int time, int change) {
        List<Integer>[] graph = new List[n];
        for (int i = 0; i < n; i++) graph[i] = new ArrayList<>();
        for (int[] edge : edges) {
            graph[edge[0] - 1].add(edge[1] - 1);
            graph[edge[1] - 1].add(edge[0] - 1);
        }
        int[] visited = new int[n];
        Arrays.fill(visited, 2);
        visited[0]--;
        int t = 0;  // 时间
        boolean isFirst = true;  // 是否为最短路径
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(0);
        while (!queue.isEmpty()) {
            int k = t / change;  // 处理红绿灯时间
            if (k % 2 == 1) {
                t += change - t % change;
            }
            t += time;
            int size = queue.size();
            int[] used = new int[n];  // 此处必须用 数组 + get，不能用List + contains 否则超时！
            while (size > 0) {
                int z = queue.poll();
                size--;
                List<Integer> list = graph[z];
                for (Integer x : list) {
                    if (visited[x] <= 0 || used[x] != 0) {
                        continue;
                    }
                    visited[x]--;
                    used[x] = 1;
                    queue.offer(x);
                    if (x == n - 1) {
                        if (isFirst) {
                            isFirst = false;
                            continue;
                        }
                        return t;
                    }
                }
            }
        }
        return -1;
    }

    /*
     * LCP 09. 最小跳跃次数
     * 给定一个数组 int[] jump，位于数组第i位置可以向前跳i格，或者跳到 下标小于 i 的任意一格
     * 小球初始位于 0 位置，求最少跳几次能跳出数组（i >= jump.length）
     * e.g.
     * jump = [2, 5, 1, 1, 1, 1]
     * -> 3
     *
     * BFS最短路径
     */
    public int minJump(int[] jump) {
        int n = jump.length;
        Queue<Pair> queue = new LinkedList<>();
        boolean[] used = new boolean[n];
        int current = 1;  // 此处 current 采取向上漫的方式，而不是回头找
        queue.add(new Pair(0, 0));
        used[0] = true;
        while (!queue.isEmpty()) {
            Pair p = queue.poll();  // queue.pop()
            int id = p.id;
            int cnt = p.cnt;
            int next = id + jump[id];
            if (next > n) {
                return cnt + 1;
            }
            if (!used[next]) {
                used[next] = true;
                queue.offer(new Pair(next, cnt + 1));
            }
            while (current < id) {
                used[current] = true;
                queue.offer(new Pair(current, cnt + 1));
                current++;
            }
        }
        return -1;
    }

    public static class Pair {
        int id;
        int cnt;

        public Pair(int id, int cnt) {
            this.id = id;
            this.cnt = cnt;
        }
    }
}
