package solution;

/*
 * 304. 二维区域和检索 - 矩阵不可变
 */
public class NumMatrix {

    int[][] sum;

    /*
     * 数组前缀和模板
     * sum 数组相比于 matrix 数组，每个点都向右下方移动了一格
     */
    public NumMatrix(int[][] matrix) {
        int n = matrix.length, m = matrix[0].length;
        sum = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                sum[i][j] = sum[i - 1][j] + sum[i][j - 1] - sum[i - 1][j - 1] + matrix[i - 1][j - 1];
            }
        }
    }

    /*
     * 左上角为(row1, col1)
     * 右下角为(row2, col2)
     */
    public int sumRegion(int row1, int col1, int row2, int col2) {
        return sum[row2 + 1][col2 + 1] + sum[row1][col1] - sum[row1][col2 + 1] - sum[row2 + 1][col1];
    }
}
