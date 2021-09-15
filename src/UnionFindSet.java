import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnionFindSet {

    /**
     * 并查集模版 —— 数组形式
     */
    public static class ArrayUnionFindSet {
        int[] f;

        public ArrayUnionFindSet(int n) {
            f = new int[n];
            for (int i = 0; i < n; i++) {
                f[i] = i;
            }
        }

        public int getf(int[] f, int x) {
            if (f[x] == x) {
                return x;
            }
            int newf = getf(f, f[x]);
            f[x] = newf;  // 路径压缩
            return newf;
        }

        public void union(int x, int y) {
            int fa = getf(f, x);
            int fb = getf(f, y);
            f[fb] = fa;
        }
    }

    /**
     * 并查集模版 —— Map形式
     */
    public static class MapUnionFindSet {
        public Map<Integer, Integer> map = new HashMap<>();

        public void connect(int a, int b) {
            if (!map.containsKey(a)) {
                map.put(a, a);
            }
            if (!map.containsKey(b)) {
                map.put(b, b);
            }
            union(a, b);  // 2 -> 2, 10 -> 1
        }

        public boolean isConnect(int a, int b) {
            if (map.containsKey(a) && map.containsKey(b)) {
                int fa = getf(a);
                int fb = getf(b);
                return fa == fb;
            }
            return false;
        }

        public int getf(int a) {
            if (map.get(a).equals(a)) {
                return a;
            }
            int fa = getf(map.get(a));
            map.put(a, fa);
            return fa;
        }

        public void union(int a, int b) {
            int fa = getf(a);
            int fb = getf(b);
            map.put(fb, fa);
        }
    }

    // 765. 情侣牵手
    // 01, 23, 45 分组问题，考虑 id / 2 = 组号
    // 03, 26, 15, 47 —— 原
    // 0<-1, 1<-3, 0<-2, 2<-3 —— 分组
    // 0123, 0, 0, 0 —— 并查集
    public int minSwapsCouples(int[] row) {
        int n = row.length;
        int tot = n / 2;
        int[] f = new int[tot];  // 代表每一个组；数组形式的并查集，每个元素的父节点初始化为自己
        for (int i = 0; i < tot; i++) {
            f[i] = i;
        }

        for (int i = 0; i < n; i += 2) {
            int l = row[i] / 2;  // l 和 r 是组号
            int r = row[i + 1] / 2;
            union(f, l, r);  // 合并，r指向l
        }

        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < tot; i++) {  // 对于每一组
            int fx = getf(f, i);  // 看下哪一组跑偏了
            // map的getOrDefault用法，再也不用担心map初始化问题了
            map.put(fx, map.getOrDefault(fx, 0) + 1);
        }

        int ret = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            ret += entry.getValue() - 1;
        }
        return ret;
    }

    // find 操作，获取并查集的父节点（带路径压缩）
    public int getf(int[] f, int x) {
        if (f[x] == x) {
            return x;
        }
        int newf = getf(f, f[x]);
        f[x] = newf;  // 路径压缩
        return newf;
    }

    // union 操作
    public void union(int[] f, int x, int y) {
        int fx = getf(f, x);
        int fy = getf(f, y);
        f[fx] = fy;
    }


    // 399. 除法求值
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        int equationsSize = equations.size();
        // 定义并查集
        UnionFind unionFind = new UnionFind(2 * equationsSize);
        // 将变量的值与 id 进行映射，使得并查集的底层使用数组实现，方便编码
        Map<String, Integer> hashMap = new HashMap<>(2 * equationsSize);
        int id = 0;
        for (int i = 0; i < equationsSize; i++) {
            List<String> equation = equations.get(i);
            String var1 = equation.get(0);
            String var2 = equation.get(1);
            // 判断是否包含key，使用 hashMap.containsKey()
            if (!hashMap.containsKey(var1)) {
                hashMap.put(var1, id);
                id++;
            }
            if (!hashMap.containsKey(var2)) {
                hashMap.put(var2, id);
                id++;
            }
            unionFind.union(hashMap.get(var1), hashMap.get(var2), values[i]);
        }

        // 第 2 步：做查询
        int queriesSize = queries.size();
        double[] res = new double[queriesSize];
        for (int i = 0; i < queriesSize; i++) {
            String var1 = queries.get(i).get(0);
            String var2 = queries.get(i).get(1);

            Integer id1 = hashMap.get(var1);
            Integer id2 = hashMap.get(var2);

            if (id1 == null || id2 == null) {
                res[i] = -1.0d;
            } else {
                res[i] = unionFind.isConnected(id1, id2);
            }
        }
        return res;
    }

    // 带权重、自定义处理方法的并查集
    private static class UnionFind {

        // 父节点集合
        private final int[] parent;

        // 指向父节点的权值
        private final double[] weight;

        // 初始化共n个元素的并查集，每个节点的父节点初始设为该节点自己
        public UnionFind(int n) {
            this.parent = new int[n];
            this.weight = new double[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                weight[i] = 1.0d;
            }
        }

        // x 是 y 的 value 倍
        public void union(int x, int y, double value) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) {
                return;
            }
            parent[rootX] = rootY;
            // 关系式的推导请见「参考代码」下方的示意图
            weight[rootX] = weight[y] * value / weight[x];
        }

        // 核心，找到根节点，并对路径进行压缩  2——>4——>3——>1
        // 递归找到跟节点，并将 parent[x] 设置为该节点
        // 最终把一条线上的节点的父节点、weight值都修正过来
        public int find(int x) {
            if (x != parent[x]) {
                int origin = parent[x];
                parent[x] = find(parent[x]);
                weight[x] *= weight[origin];
            }
            return parent[x];
        }

        // 判断是否在同一并查集中，如果在、则返回权值计算结果，定制功能，普通并查集不需要
        public double isConnected(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) {
                return weight[x] / weight[y];
            } else {
                return -1.0d;
            }
        }
    }
}
