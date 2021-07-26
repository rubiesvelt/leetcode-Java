package solution;

public class LargestMagicSquare {

    public static void main(String[] args) {
        LargestMagicSquare largestMagicSquare = new LargestMagicSquare();
        int[][] grid = {{7, 1, 4, 5, 6}, {2, 5, 1, 6, 4}, {1, 5, 4, 3, 2}, {1, 2, 7, 3, 4}};
        largestMagicSquare.largestMagicSquare(grid);
    }

    // 5202. 最大的幻方
    int[][] row;
    int[][] col;
    int[][] lr;
    int[][] rl;

    public int largestMagicSquare(int[][] grid) {
        // m行n列
        int m = grid.length;
        int n = grid[0].length;
        row = new int[m][n];
        col = new int[m][n];
        lr = new int[m][n];
        rl = new int[m][n];
        for (int i = 0; i < m; i++) {
            int acc = 0;
            for (int j = 0; j < n; j++) {
                acc += grid[i][j];
                row[i][j] = acc;
            }
        }
        for (int j = 0; j < n; j++) {
            int acc = 0;
            for (int i = 0; i < m; i++) {
                acc += grid[i][j];
                col[i][j] = acc;
            }
        }
        for (int i = 0; i < m; i++) {
            int i1 = i;
            int j1 = 0;
            int acc = 0;
            while (i1 < m && j1 < n) {
                acc += grid[i1][j1];
                lr[i1][j1] = acc;
                i1++;
                j1++;
            }
        }
        for (int j = 1; j < n; j++) {
            int i1 = 0;
            int j1 = j;
            int acc = 0;
            while (i1 < m && j1 < n) {
                acc += grid[i1][j1];
                lr[i1][j1] = acc;
                i1++;
                j1++;
            }
        }
        for (int i = 0; i < m; i++) {
            int i1 = i;
            int j1 = n - 1;
            int acc = 0;
            while (i1 < m && j1 >= 0) {
                acc += grid[i1][j1];
                rl[i1][j1] = acc;
                i1++;
                j1--;
            }
        }
        for (int j = n - 2; j >= 0; j--) {
            int i1 = 0;
            int j1 = j;
            int acc = 0;
            while (i1 < m && j1 >= 0) {
                acc += grid[i1][j1];
                rl[i1][j1] = acc;
                i1++;
                j1--;
            }
        }
        int r = Math.min(m, n);
        while (r > 1) {
            for (int i = 0; i - 1 + r < m; i++) {
                for (int j = 0; j - 1 + r < n; j++) {
                    if (isCube(i, j, r)) {
                        return r;
                    }
                }
            }
            r--;
        }
        return 1;
    }

    public boolean isCube(int i, int j, int l) {
        int base = j == 0 ? row[i][j - 1 + l] : row[i][j - 1 + l] - row[i][j - 1];
        // 行
        int count = 0;
        while (count < l) {
            int t;
            if (j == 0) {
                t = row[i + count][l - 1];
            } else {
                t = row[i + count][j - 1 + l] - row[i + count][j - 1];
            }
            if (t != base) return false;
            count++;
        }
        // 列
        count = 0;
        while (count < l) {
            int t;
            if (i == 0) {
                t = col[l - 1][j + count];
            } else {
                t = col[i - 1 + l][j + count] - col[i - 1][j + count];
            }
            if (t != base) return false;
            count++;
        }
        // lr
        int t = i > 0 && j > 0 ? lr[i - 1 + l][j - 1 + l] - lr[i - 1][j - 1] : lr[i - 1 + l][j - 1 + l];
        if (t != base) return false;
        // rl
        int t1 = i > 0 && j + l < rl[0].length ? rl[i - 1 + l][j] - rl[i - 1][j + l] : rl[i - 1 + l][j];
        if (t1 != base) return false;
        return true;
    }
}
