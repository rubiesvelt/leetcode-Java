import java.util.LinkedList;
import java.util.Queue;

/**
 * DP —— 路径
 */
public class DPPath {

    /*
     * 63. 不同路径 II
     *
     * 深信服面试题. 机器人可移动的总方案数
     *
     * 给出一个二维数组 graph[][]，graph[i][j] = 0 表示畅通，graph[i][j] = 1 表示障碍
     * 机器人初始位置在图的左上角 (0, 0)，现需要移动到图的右下角，机器人只能往下或往右移动
     * 求有多少种不同的路线
     */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int n = obstacleGrid.length;
        int m = obstacleGrid[0].length;
        if (obstacleGrid[0][0] == 1 || obstacleGrid[n - 1][m - 1] == 1) return 0;
        int[][] dp = new int[n][m];  // dp[i][j] 表示到达 [i][j] 格子总路径数目
        dp[0][0] = 1;
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(0, 0));
        while (!queue.isEmpty()) {
            Point p = queue.poll();
            int i1 = p.i + 1;
            int j1 = p.j + 1;
            if (i1 < n && obstacleGrid[i1][p.j] == 0) {
                if (dp[i1][p.j] == 0) queue.add(new Point(i1, p.j));
                dp[i1][p.j] += dp[p.i][p.j];
            }
            if (j1 < m && obstacleGrid[p.i][j1] == 0) {
                if (dp[p.i][j1] == 0) queue.add(new Point(p.i, j1));
                dp[p.i][j1] += dp[p.i][p.j];
            }
        }
        return dp[n - 1][m - 1];
    }

    public static class Point {
        int i;
        int j;

        public Point(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    /*
     * 62. 不同路径
     */
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }
        for (int j = 0; j < n; j++) {
            dp[0][j] = 1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }
        return dp[m - 1][n - 1];
    }

    // 576. 出界的路径数
    // 记忆化搜索
    int MOD = (int) 1e9 + 7;
    int m, n, max;
    int[][] dirs = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};  // 四个方向
    int[][][] cache;

    public int findPaths(int m, int n, int max, int r, int c) {
        cache = new int[m][n][max + 1];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k <= max; k++) {
                    cache[i][j][k] = -1;
                }
            }
        }
        return dfs576(m, n, r, c, max);
    }

    int dfs576(int m, int n, int x, int y, int k) {
        if (x < 0 || x >= m || y < 0 || y >= n) return 1;
        if (k == 0) return 0;
        if (cache[x][y][k] != -1) return cache[x][y][k];
        int ans = 0;
        for (int[] d : dirs) {
            int nx = x + d[0], ny = y + d[1];
            ans += dfs576(m, n, nx, ny, k - 1);
            ans %= MOD;
        }
        cache[x][y][k] = ans;
        return ans;
    }

    // 动态规划
    // 路径dp
    // 此处将矩阵进行编码
    public int findPaths1(int _m, int _n, int _max, int r, int c) {
        m = _m;
        n = _n;
        max = _max;
        int[][] f = new int[m * n][max + 1];
        // 初始化边缘格子的路径数量
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0) add(i, j, f);
                if (j == 0) add(i, j, f);
                if (i == m - 1) add(i, j, f);
                if (j == n - 1) add(i, j, f);
            }
        }
        // 从小到大枚举「可移动步数」
        for (int k = 1; k <= max; k++) {
            // 枚举所有的「位置」
            for (int idx = 0; idx < m * n; idx++) {
                int[] info = parseIdx(idx);
                int x = info[0], y = info[1];
                for (int[] d : dirs) {
                    int nx = x + d[0], ny = y + d[1];
                    if (nx < 0 || nx >= m || ny < 0 || ny >= n) continue;
                    int nidx = getIdx(nx, ny);
                    f[idx][k] += f[nidx][k - 1];
                    f[idx][k] %= MOD;
                }
            }
        }
        return f[getIdx(r, c)][max];
    }

    void add(int x, int y, int[][] f) {
        for (int k = 1; k <= max; k++) {
            f[getIdx(x, y)][k]++;
        }
    }

    int getIdx(int x, int y) {
        return x * n + y;
    }

    int[] parseIdx(int idx) {
        return new int[]{idx / n, idx % n};
    }
}
