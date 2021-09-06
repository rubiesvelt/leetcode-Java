import java.util.*;

public class Matrix {

    // 1992. 找到所有的农场组
    public int[][] findFarmland(int[][] land) {
        List<int[]> res = new ArrayList<>();
        int m = land.length, n = land[0].length;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (land[i][j] == 0) {
                    continue;
                }
                int row = i, col = j;
                // 向下探索矩形农场组行
                while (row + 1 < m && land[row + 1][j] == 1) row++;
                // 向右探索矩形农场组的列
                while (col + 1 < n && land[i][col + 1] == 1) col++;

                res.add(new int[]{i, j, row, col});

                // 探索到的矩形农场组中所有块"置为0"，避免后续的遍历 —— 聪明方法
                for (int x = i; x <= row; x++) {
                    for (int y = j; y <= col; y++) {
                        land[x][y] = 0;
                    }
                }
            }
        }
        return res.toArray(new int[0][]);
    }

    // 1958. 检查操作是否合法
    public boolean checkMove(char[][] board, int rMove, int cMove, char color) {
        for (int i = -1; i <= 1; i++) {  // 使用i, j模拟出8个方向
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                int[] direction = new int[]{i, j};
                if (isGoodMove(board, rMove, cMove, color, direction)) {
                    return true;
                }
            }
        }
        return false;
    }

    // 检查向单个方向的一次操作是否合法
    boolean isGoodMove(char[][] board, int rMove, int cMove, char color, int[] direction) {
        char anotherColor;
        if (color == 'W') {
            anotherColor = 'B';
        } else {
            anotherColor = 'W';
        }
        int rNext = rMove;
        int cNext = cMove;
        boolean start = false;
        while (true) {
            rNext = rNext + direction[0];
            cNext = cNext + direction[1];
            if (rNext > 7 || cNext > 7 || rNext < 0 || cNext < 0) {
                return false;
            }
            if (board[rNext][cNext] == '.') {
                return false;
            }
            if (!start && board[rNext][cNext] == color) {
                return false;
            }
            if (board[rNext][cNext] == anotherColor && !start) {
                start = true;
                continue;
            }
            if (board[rNext][cNext] == color) {
                return true;
            }
        }
    }

    // 1878. 矩阵中最大的三个菱形和
    // 求矩阵中最大的三个互不相同的菱形和
    public int[] getBiggestThree(int[][] grid) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                set.addAll(calculateOne(grid, i, j));
            }
        }
        if (set.size() < 3) {
            int[] ans = new int[set.size()];
            int index = 0;
            for (Integer integer : set) {
                ans[index++] = integer;
            }
            if (set.size() == 1) return ans;
            if (ans[0] < ans[1]) {
                int t = ans[0];
                ans[0] = ans[1];
                ans[1] = t;
            }
            return ans;
        }
        List<Integer> list = new ArrayList<>(set);
        list.sort((o1, o2) -> o2 - o1);
        int[] ans = new int[3];
        for (int i = 0; i < 3; i++) {
            ans[i] = list.get(i);
        }
        return ans;
    }

    // 计算以i, j为中心的所有菱形和
    public List<Integer> calculateOne(int[][] grid, int i, int j) {
        List<Integer> ans = new ArrayList<>();
        ans.add(grid[i][j]);
        int m = grid.length;
        int n = grid[0].length;
        int imin = Math.min(i, m - i - 1);
        int jmin = Math.min(j, n - 1 - j);
        int rmax = Math.min(imin, jmin);  // 最大半径
        int r = 1;
        while (r <= rmax) {
            int count = r;
            int t = 0;
            int p = i - count;
            int q = j;
            while (count-- > 0) {
                t += grid[p++][q++];
            }
            count = r;
            while (count-- > 0) {
                t += grid[p++][q--];
            }
            count = r;
            while (count-- > 0) {
                t += grid[p--][q--];
            }
            count = r;
            while (count-- > 0) {
                t += grid[p--][q++];
            }
            ans.add(t);
            r++;
        }
        return ans;
    }

    /*
     * 1905. 统计子岛屿
     * 联通块问题
     */
    int subIsland = 0;
    int m5971 = 0;
    int n5971 = 0;
    boolean[][] islandVisited;

    public int countSubIslands(int[][] grid1, int[][] grid2) {
        m5971 = grid1.length;
        n5971 = grid1[0].length;
        islandVisited = new boolean[m5971][n5971];
        for (int i = 0; i < m5971; i++) {
            for (int j = 0; j < n5971; j++) {
                if (grid2[i][j] == 1) handleIsland(grid1, grid2, i, j);
            }
        }
        return subIsland;
    }

    public void handleIsland(int[][] grid1, int[][] grid2, int iPoint, int jPoint) {
        // 先将所有添加到 List，如果flag = true则 ++， = false 则不加，然后将陆地变成水
        boolean flag = true;
        Queue<Point> queue = new ArrayDeque<>();
        queue.add(new Point(iPoint, jPoint));
        while (!queue.isEmpty()) {
            Point p = queue.poll();
            if (grid1[p.i][p.j] == 0) flag = false;
            grid2[p.i][p.j] = 0;
            // 可能添加重复元素
            if (p.i - 1 >= 0 && grid2[p.i - 1][p.j] == 1 && !islandVisited[p.i - 1][p.j]) {
                queue.add(new Point(p.i - 1, p.j));
                islandVisited[p.i - 1][p.j] = true;
            }
            if (p.i + 1 < m5971 && grid2[p.i + 1][p.j] == 1 && !islandVisited[p.i + 1][p.j]) {
                queue.add(new Point(p.i + 1, p.j));
                islandVisited[p.i + 1][p.j] = true;
            }
            if (p.j - 1 >= 0 && grid2[p.i][p.j - 1] == 1 && !islandVisited[p.i][p.j - 1]) {
                queue.add(new Point(p.i, p.j - 1));
                islandVisited[p.i][p.j - 1] = true;
            }
            if (p.j + 1 < n5971 && grid2[p.i][p.j + 1] == 1 && !islandVisited[p.i - 1][p.j + 1]) {
                queue.add(new Point(p.i, p.j + 1));
                islandVisited[p.i][p.j + 1] = true;
            }
        }
        if (flag) subIsland++;
    }

    public static class Point {
        int i;
        int j;

        Point(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    // 1914. 循环轮转矩阵
    // 一圈一圈的，每一圈往前移动k个元素
    // 矩阵中带方向带模拟
    public int[][] rotateGrid(int[][] grid, int k) {
        int m = grid.length, n = grid[0].length;
        int circle = Math.min(m, n) / 2;
        // 当前层
        for (int i = 0; i < circle; i++) {
            // 当前层需要移动的步数 = k % 当前层元素总数
            // 当前层元素总数
            // = 2 * (行数 + 列数) - 4
            // = 2 * (m - 2i + n - 2i) - 4
            // = 2 * (m + n) - 4 - 8 * i
            int round = k % (2 * (m + n) - 4 - 8 * i);
            while (round-- > 0) {
                int x = i, y = i;
                // e -> s -> w -> n
                char forward = 'e';
                int init = grid[i][i];
                while (true) {
                    if (forward == 's') {
                        if (x == m - i - 1) forward = 'w';
                        else grid[x][y] = grid[++x][y];
                    }
                    if (forward == 'e') {
                        if (y == n - i - 1) forward = 's';
                        else grid[x][y] = grid[x][++y];
                    }
                    if (forward == 'n') {
                        if (x == i) {
                            grid[x + 1][y] = init;
                            break;
                        } else grid[x][y] = grid[--x][y];
                    }
                    if (forward == 'w') {
                        if (y == i) forward = 'n';
                        else grid[x][y] = grid[x][--y];
                    }
                }
            }
        }
        return grid;
    }

    // LCP29. 乐团站位
    // 也是螺旋矩阵，1-9 螺旋排列，求[xPos, yPos]的数
    public int orchestraLayout(int num, int xPos, int yPos) {
        int[][] resArray = generateMatrixSpan(num);
        return resArray[xPos][yPos];
    }

    public int[][] generateMatrixSpan(int n) {
        int numEle = n * n;
        int[][] resArray = new int[n][n];
        int index = 1;
        int element = 1;
        int left = 0;
        int right = n - 1;
        int top = 0;
        int bottom = n - 1;
        while (element <= numEle) {
            for (int i = left; i <= right; i++) {
                resArray[top][i] = index++;
                if (index == 10) {
                    index = 1;
                }
                element++;
            }
            top++;
            for (int i = top; i <= bottom; i++) {
                resArray[i][right] = index++;
                if (index == 10) {
                    index = 1;
                }
                element++;
            }
            right--;
            for (int i = right; i >= left; i--) {
                resArray[bottom][i] = index++;
                if (index == 10) {
                    index = 1;
                }
                element++;
            }
            bottom--;
            for (int i = bottom; i >= top; i--) {
                resArray[i][left] = index++;
                if (index == 10) {
                    index = 1;
                }
                element++;
            }
            left++;
        }
        return resArray;
    }

    // 54. 螺旋矩阵
    // 按螺旋矩阵的顺序返回矩阵中的元素
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> list = new ArrayList<>();
        if (matrix.length == 0) {
            return null;
        }
        int cnt = matrix.length * matrix[0].length;
        int bottom = 0;
        int top = matrix[0].length - 1;
        int left = 0;
        int right = matrix.length - 1;
        while (true) {
            int size = list.size();  // 不可采取此种比较，会导致元素遍历完后，若非正方形，则回头遍历
            for (int i = bottom; i <= top; i++) {
                list.add(matrix[left][i]);
                cnt--;
            }
            if (cnt <= 0) {
                break;
            }
            left++;
            for (int j = left; j <= right; j++) {
                list.add(matrix[j][top]);
                cnt--;
            }
            if (cnt <= 0) {
                break;
            }
            top--;
            for (int i = top; i >= bottom; i--) {
                list.add(matrix[right][i]);
                cnt--;
            }
            if (cnt <= 0) {
                break;
            }
            right--;
            for (int j = right; j >= left; j--) {
                list.add(matrix[j][bottom]);
                cnt--;
            }
            if (cnt <= 0) {
                break;
            }
            bottom++;
        }
        return list;
    }

    private List<Integer> spiralOrder2(int[][] matrix) {
        LinkedList<Integer> result = new LinkedList<>();
        if (matrix == null || matrix.length == 0) return result;
        int left = 0;
        int right = matrix[0].length - 1;
        int top = 0;
        int bottom = matrix.length - 1;
        int numEle = matrix.length * matrix[0].length;
        while (numEle >= 1) {
            for (int i = left; i <= right && numEle >= 1; i++) {
                result.add(matrix[top][i]);
                numEle--;
            }
            top++;
            for (int i = top; i <= bottom && numEle >= 1; i++) {
                result.add(matrix[i][right]);
                numEle--;
            }
            right--;
            for (int i = right; i >= left && numEle >= 1; i--) {
                result.add(matrix[bottom][i]);
                numEle--;
            }
            bottom--;
            for (int i = bottom; i >= top && numEle >= 1; i--) {
                result.add(matrix[i][left]);
                numEle--;
            }
            left++;
        }
        return result;
    }

    // 59. 螺旋矩阵Ⅱ
    // 给定一个n，生成螺旋矩阵
    public int[][] generateMatrix(int n) {
        int numEle = n * n;
        int[][] resArray = new int[n][n];
        int element = 1;
        int left = 0;
        int right = n - 1;
        int top = 0;
        int bottom = n - 1;
        while (element <= numEle) {
            for (int i = left; i <= right; i++) {
                resArray[top][i] = element;
                element++;
            }
            top++;
            for (int i = top; i <= bottom; i++) {
                resArray[i][right] = element;
                element++;
            }
            right--;
            for (int i = right; i >= left; i--) {
                resArray[bottom][i] = element;
                element++;
            }
            bottom--;
            for (int i = bottom; i >= top; i--) {
                resArray[i][left] = element;
                element++;
            }
            left++;
        }
        return resArray;
    }
}
