import beans.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        char a = 'a';
        int i = 96;
        TreeNode tn4 = new TreeNode(4);
        TreeNode tn3 = new TreeNode(3);
        TreeNode tn2 = new TreeNode(2, null, tn4);
        TreeNode tn1 = new TreeNode(1, tn2, tn3);

        int[][] matrix = {{2, 1, 1}, {2, 3, 1}, {3, 4, 1}};
        int[][] matrix1 = {{1, 0, 0}, {0, 0, 1}, {1, 1, 0}};
        int[] dist = {1, 10, 4, 4, 2, 7};
        int[] speed = {2, 2, 3, 4};
        main.triangleNumber(speed);
    }

    // 611. 有效三角形的个数
    public int triangleNumber1(int[] nums) {
        Arrays.sort(nums);
        int result = 0;
        for (int i = nums.length - 1; i >= 2; i--) {
            int k = 0;
            int j = i - 1;
            while (k < j) {
                // 选 c (num[i]) 和 b (num[j])，求 a (num[k - j], 可范围，批量求)
                // 满足该条件，说明从num[k]到num[j]的数都满足要求，结果直接加上j - k
                if (nums[k] + nums[j] > nums[i]) {
                    result += j - k;
                    j--;
                } else {
                    // 否则k自增，重新判断
                    k++;
                }
            }
        }
        return result;
    }

    int ans = 0;

    public int triangleNumber(int[] nums) {
        Arrays.sort(nums);
        dfs(nums, -1, new ArrayList<>());
        return ans;
    }

    // [2,2,3,4,5]
    public boolean dfs(int[] nums, int index, List<Integer> list) {
        if (list.size() == 3) {
            if (isTri(nums[list.get(0)], nums[list.get(1)], nums[list.get(2)])) {
                ans++;
                return true;
            }
            return false;
        }
        boolean ret = true;
        for (int i = index + 1; i < nums.length; i++) {
            if (!ret && list.size() == 2) {
                continue;
            }
            list.add(i);
            ret = dfs(nums, i, list);
            list.remove(list.size() - 1);
        }
        return true;
    }

    boolean isTri(int a, int b, int c) {
        return a + b > c;
    }
}
