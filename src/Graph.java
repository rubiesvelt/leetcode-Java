import java.util.*;

public class Graph {

    // TODO 拓扑排序，最小生成树，最短路径

    // 802. 找到最终的安全状态
    // DFS，拓扑排序，均可

    // 拓扑排序
    // 拓扑排序求的是"入度"为0的点，我们需要求从"出度"为0的点，及其相关点，所以我们使用"反图"
    public List<Integer> eventualSafeNodes1(int[][] graph) {
        int n = graph.length;
        // 反图，邻接表存储
        List<List<Integer>> new_graph = new ArrayList<>();
        // 节点入度，拓扑排序使用
        int[] Indeg = new int[n];

        for(int i = 0; i < n; i++) {
            new_graph.add(new ArrayList<>());
        }
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < graph[i].length; j++) {
                new_graph.get(graph[i][j]).add(i);
            }
            // 原数组记录的节点出度，在反图中就是入度
            Indeg[i] = graph[i].length;
        }

        // 拓扑排序队列
        Queue<Integer> q = new LinkedList<>();

        // 首先将入度为 0 的点存入队列
        for(int i = 0; i < n; i++) {
            if(Indeg[i] == 0) {
                q.offer(i);
            }
        }

        while(!q.isEmpty()) {
            // 每次弹出队头元素
            int cur = q.poll();
            for(int x : new_graph.get(cur)) {
                // 将以其为起点的有向边删除，更新终点入度
                Indeg[x]--;
                if(Indeg[x] == 0) q.offer(x);
            }
        }

        // 最终入度（原图中出度）为 0 的所有点均为安全点
        List<Integer> ret = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            if(Indeg[i] == 0) ret.add(i);
        }
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
        ans.sort(Comparator.comparingInt(o -> o));
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

    // 547. 省份数量
    // 找图中连通块数量
    public int findCircleNum(int[][] isConnected) {
        int res = 0;
        int[] isVisited = new int[isConnected.length];
        for (int i = 0; i < isConnected.length; i++) {
            if (isVisited[i] == 1) {
                continue;
            }
            dfs1(isConnected, isVisited, i);
            res++;
        }
        return res;
    }

    public void dfs1(int[][] isConnected, int[] isVisited, int u) {
        isVisited[u] = 1;
        for (int i = 0; i < isConnected.length; i++) {
            if (isVisited[i] == 0 && isConnected[u][i] == 1) {
                dfs1(isConnected, isVisited, i);
            }
        }
    }

    /*
     * 1857. 有向图中最大颜色值
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
        int[] indeg = new int[n];
        for (int[] edge : edges) {
            indeg[edge[1]]++;
            g.get(edge[0]).add(edge[1]);
        }
        // 邻接表、入度均为拓朴排序所需要

        // 记录拓扑排序中遇到节点个数
        int found = 0;
        int[][] f = new int[n][26];  // dp
        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (indeg[i] == 0) q.offer(i);
        }

        while (!q.isEmpty()) {
            found++;
            int u = q.poll();
            // 将节点对应颜色加1
            f[u][colors.charAt(u) - 'a']++;
            // 枚举 u 后所有结点 v
            for (int v : g.get(u)) {
                indeg[v]--;
                // 将 f(v,c) 更新为其与 f(u,c) 的较大值
                for (int c = 0; c < 26; ++c) {
                    f[v][c] = Math.max(f[v][c], f[u][c]);
                }
                if (indeg[v] == 0) q.offer(v);
            }
        }

        if (found != n) return -1;

        int ans = 0;
        for (int i = 0; i < n; ++i) {
            ans = Math.max(ans, Arrays.stream(f[i]).max().getAsInt());
        }
        return ans;
    }

    // 210
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
                dfs(i);
            }
        }
        if (!valid) {
            return new int[0];
        }
        // 如果没有环，那么就有拓扑排序
        return result;
    }

    public void dfs(int u) {
        // 将节点标记为「搜索中」
        visited[u] = 1;
        // 搜索其相邻节点
        // 只要发现有环，立刻停止搜索
        for (int v : edges.get(u)) {
            // 如果「未搜索」那么搜索相邻节点
            if (visited[v] == 0) {
                dfs(v);
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
