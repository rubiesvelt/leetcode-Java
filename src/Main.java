import beans.TreeNode;

import java.util.ArrayList;
import java.util.List;

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

        int[][] matrix = {{1,0,0}, {0,1,1}, {0,1,1}};
        int[][] matrix1 = {{1, 0, 0}, {0, 0, 1}, {1, 1, 0}};
        int[] dist = {1, 0, 1};
        int[] diff = {0, 1, 0, 1, 0};
        int[] speed = {5, 1, 5, 5, 1, 5, 3, 4, 5, 1, 4};
        char[][] matrix2 = {{'.', '.', 'W', '.', 'B', 'W', 'W', 'B'}, {'B', 'W', '.', 'W', '.', 'W', 'B', 'B'}, {'.', 'W', 'B', 'W', 'W', '.', 'W', 'W'}, {'W', 'W', '.', 'W', '.', '.', 'B', 'B'}, {'B', 'W', 'B', 'B', 'W', 'W', 'B', '.'}, {'W', '.', 'W', '.', '.', 'B', 'W', 'W'}, {'B', '.', 'B', 'B', '.', '.', 'B', 'B'}, {'.', 'W', '.', 'W', '.', 'W', '.', 'W'}};
        main.findFarmland(matrix);
        return;
    }


    public int[][] findFarmland(int[][] land) {
        List<int[]> farmLand = new ArrayList<>();
        int n = land.length;
        int m = land[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (isLeftUp(land, i, j)) {
                    int[] point = new int[4];
                    point[0] = i;
                    point[1] = j;
                    findRightDown(land, point);
                    farmLand.add(point);
                }
            }
        }
        int[][] ans = new int[farmLand.size()][];
        for (int i = 0; i < farmLand.size(); i++) {
            ans[i] = farmLand.get(i);
        }
        return ans;
    }

    public boolean isLeftUp(int[][] land, int x, int y) {
        int n = land.length;
        int m = land[0].length;
        if (land[x][y] == 0) {
            return false;
        }
        if ((x == 0 || land[x - 1][y] == 0) && (y == 0 || land[x][y - 1] == 0)) {
            return true;
        }
        return false;
    }

    public int[] findRightDown(int[][] land, int[] leftUp) {
        int n = land.length;
        int m = land[0].length;
        int x = leftUp[0];
        int y = leftUp[1];
        int i = x;
        int j = y;
        while (land[i][y] == 1) {
            if (i == n - 1) {
                i++;
                break;
            }
            i++;
        }
        while (land[x][j] == 1){
            if (j == m - 1) {
                j++;
                break;
            }
            j++;
        }
        leftUp[2] = i - 1;
        leftUp[3] = j - 1;
        return leftUp;
    }

    public int findMiddleIndex(int[] nums) {
        int n = nums.length;
        if (n == 1) {
            return 0;
        }
        int[] left = new int[n];
        int[] right = new int[n];

        int acc = 0;
        for (int i = n - 2; i >= 0; i--) {
            right[i] = right[i + 1] + nums[i + 1];
        }
        if (right[0] == 0) {
            return 0;
        }
        for (int i = 1; i < n; i++) {
            acc += nums[i - 1];
            if (acc == right[i]) {
                return i;
            }
        }
        return -1;
    }
}
