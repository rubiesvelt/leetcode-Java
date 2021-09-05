import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

public class Graph {

    // TODO 拓扑排序，最小生成树，最短路径

    /**
     * --------------------------------------- 拓扑排序 ---------------------------------------
     * <p>
     * 拓扑排序，需要:
     * <br/>原图，
     * <br/>节点入度数组，
     * <br/>拓扑排序队列
     */

    /*
     * 1857. 有向图中最大颜色值
     * 给你一个字符串colors ，其中colors[i]是小写英文字母，表示图中第 i个节点的 颜色（下标从 0开始）
     * 同时给你一个二维数组edges，其中edges[j] = [aj, bj]表示从节点aj到节点bj有一条有向边
     * 求给定图中有效路径里面的 最大颜色值。如果图中含有环，请返回 -1
     *
     * 拓朴排序，根据拓朴序dp
     * 拓朴排序，适用于"有向图"；入度为0的进入队列，然后不断拆边，直到所有入度为0都进入队列
     */
    public int largestPathValue1(String colors, int[][] edges) {
        int n = colors.length();
        // 邻接表，描述节点间的邻接关系
        // 1 -> 2, 3
        // 2 -> 3, 4, 5
        List<List<Integer>> g = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            g.add(new ArrayList<>());
        }
        // 统计节点入度
        int[] inDegree = new int[n];
        for (int[] edge : edges) {
            inDegree[edge[1]]++;
            g.get(edge[0]).add(edge[1]);
        }
        // 邻接表、入度均为拓朴排序所需要

        // 记录拓扑排序中遇到节点个数
        int found = 0;
        int[][] dp = new int[n][26];  // dp[i][j] 表示在i号节点，j号颜色(x - 'a')，所能得到的 串联起来的 最大出现次数
        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) q.offer(i);
        }

        while (!q.isEmpty()) {
            found++;
            int u = q.poll();
            // 将节点对应颜色加1
            dp[u][colors.charAt(u) - 'a']++;
            // 枚举 u 后所有结点 v
            for (int v : g.get(u)) {
                inDegree[v]--;
                // 将 dp(v,c) 更新为其与 dp(u,c) 的较大值
                for (int c = 0; c < 26; ++c) {
                    dp[v][c] = Math.max(dp[v][c], dp[u][c]);
                }
                if (inDegree[v] == 0) q.offer(v);
            }
        }

        if (found != n) return -1;
        int ans = 0;
        for (int i = 0; i < n; ++i) {
            ans = Math.max(ans, Arrays.stream(dp[i]).max().getAsInt());
        }
        return ans;
    }

    // 802. 找到最终的安全状态
    // 给出一个有向图graph
    // 对于一个起始节点，如果从该节点出发，无论每一步选择沿哪条有向边行走，最后必然在有限步内到达终点，则将该起始节点称作是 安全 的
    // DFS，拓扑排序，均可

    /*
     * 所有在环上，有路径通向环的点，都是不安全点
     * 求不在环上，且没有路径通向环的点
     * 拓扑排序，拆不掉 环上 及 环上元素通往的点，故我们使用"反图"做拓扑排序，除去拆不掉的点，剩下点都是安全点
     *
     */
    public List<Integer> eventualSafeNodes1(int[][] graph) {
        int n = graph.length;
        // 反图，邻接表存储
        List<List<Integer>> reverseGraph = new ArrayList<>();
        // 节点入度，拓扑排序使用
        int[] inDegree = new int[n];

        for (int i = 0; i < n; i++) {
            reverseGraph.add(new ArrayList<>());
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                reverseGraph.get(graph[i][j]).add(i);
            }
            // 原数组记录的节点出度，在反图中就是入度
            inDegree[i] = graph[i].length;
        }

        // 拓扑排序队列
        Queue<Integer> q = new LinkedList<>();

        // 首先将入度为 0 的点存入队列
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                q.offer(i);
            }
        }

        while (!q.isEmpty()) {
            // 每次弹出队头元素
            int cur = q.poll();
            for (int x : reverseGraph.get(cur)) {
                // 将以其为起点的有向边删除，更新终点入度
                inDegree[x]--;
                if (inDegree[x] == 0) q.offer(x);
            }
        }

        // 最终入度（原图中出度）为 0 的所有点均为安全点。由于要求升序排列，所以整这么一出
        List<Integer> ret = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) ret.add(i);
        }

        TreeMap treeMap;
        String s;
        ConcurrentSkipListSet set;
        return ret;
    }

    // DFS版本
    Set<Integer> good = new HashSet<>();
    Set<Integer> used = new HashSet<>();

    public List<Integer> eventualSafeNodes(int[][] graph) {
        int n = graph.length;
        for (int i = 0; i < n; i++) {
            if (used.contains(i)) {
                continue;
            }
            dfs802(graph, i, new HashSet<>());
        }
        List<Integer> ans = new ArrayList<>(good);
        ans.sort((o1, o2) -> o1 - o2);
        return ans;
    }

    // DFS + 回溯
    public boolean dfs802(int[][] graph, int now, Set<Integer> current) {
        int[] edges = graph[now];
        used.add(now);
        if (current.isEmpty()) current.add(now);  // 此处，只为添加开头的元素
        if (edges.length == 0) {  // 此处也是开头元素的特判
            good.add(now);
            return true;
        }
        for (int next : edges) {
            if (current.contains(next)) {
                return false;
            }
            if (used.contains(next)) {
                if (!good.contains(next)) {
                    return false;
                }
                continue;
            }
            current.add(next);
            boolean b = dfs802(graph, next, current);
            current.remove(next);
            if (!b) {
                return false;
            }
            good.add(next);
        }
        good.add(now);
        return true;
    }

    // 785. 判断二分图
    // 染色法
    // 二分图：可以将图中节点分为两组，使图中所有边所连的节点，分别属于两组
    public boolean isBipartite(int[][] graph) {
        int n = graph.length;
        int[] visited = new int[n];
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (visited[i] != 0) {
                continue;
            }
            queue.offer(i);
            visited[i] = 1;  // 不光是visited数组，还染成 1 或 -1
            while (!queue.isEmpty()) {
                int j = queue.poll();
                for (int k : graph[j]) {
                    if (visited[k] == visited[j]) {
                        return false;
                    }
                    if (visited[k] == 0) {
                        visited[k] = -visited[j];
                        queue.add(k);
                    }
                }
            }
        }
        return true;
    }

    /*
     * 547. 省份数量
     * 给你一个 n x n 的矩阵 isConnected ，其中 isConnected[i][j] = 1 表示第 i 个城市和第 j 个城市直接相连
     * 而 isConnected[i][j] = 0 表示二者不直接相连
     * 返回矩阵中 省份 的数量
     * 找图中连通块数量
     */
    public int findCircleNum(int[][] isConnected) {
        int res = 0;
        int[] isVisited = new int[isConnected.length];
        for (int i = 0; i < isConnected.length; i++) {
            if (isVisited[i] == 1) {
                continue;
            }
            dfs547(isConnected, isVisited, i);
            res++;
        }
        return res;
    }

    public void dfs547(int[][] isConnected, int[] isVisited, int u) {
        isVisited[u] = 1;
        for (int i = 0; i < isConnected.length; i++) {
            if (isVisited[i] == 0 && isConnected[u][i] == 1) {
                dfs547(isConnected, isVisited, i);
            }
        }
    }

    // 210. 课程表 II
    // 存储有向图
    List<List<Integer>> edges;
    // 标记每个节点的状态：0=未搜索，1=搜索中，2=已完成
    int[] visited;
    // 用数组来模拟栈，下标 n-1 为栈底，0 为栈顶
    int[] result;
    // 判断有向图中是否有环
    boolean valid = true;
    // 栈下标
    int index;

    public int[] findOrder(int numCourses, int[][] prerequisites) {
        edges = new ArrayList<List<Integer>>();
        for (int i = 0; i < numCourses; ++i) {
            edges.add(new ArrayList<Integer>());
        }
        visited = new int[numCourses];
        result = new int[numCourses];
        index = numCourses - 1;
        for (int[] info : prerequisites) {
            edges.get(info[1]).add(info[0]);
        }
        // 每次挑选一个「未搜索」的节点，开始进行深度优先搜索
        for (int i = 0; i < numCourses && valid; ++i) {
            if (visited[i] == 0) {
                dfs210(i);
            }
        }
        if (!valid) {
            return new int[0];
        }
        // 如果没有环，那么就有拓扑排序
        return result;
    }

    public void dfs210(int u) {
        // 将节点标记为「搜索中」
        visited[u] = 1;
        // 搜索其相邻节点
        // 只要发现有环，立刻停止搜索
        for (int v : edges.get(u)) {
            // 如果「未搜索」那么搜索相邻节点
            if (visited[v] == 0) {
                dfs210(v);
                if (!valid) {
                    return;
                }
            }
            // 如果「搜索中」说明找到了环
            else if (visited[v] == 1) {
                valid = false;
                return;
            }
        }
        // 将节点标记为「已完成」
        visited[u] = 2;
        // 将节点入栈
        result[index--] = u;
    }
}
