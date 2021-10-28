import java.util.*;

public class DFSSubSet {

    // 698. 划分为k个相等的子集
    // 是否能将nums数组划分为k个元素和相等的子集？
    // nums = [1,1,1,1,2,2,2,2], k = 4
    // 回溯算法
    public boolean canPartitionKSubsets(int[] cp, int k) {
        Integer[] nums = new Integer[cp.length];
        int index = 0;
        for (int i : cp) {
            nums[index++] = i;
        }
        Arrays.sort(nums, (o1, o2) -> o2 - o1);
        int sum = 0;
        for (int n : nums) {
            sum += n;
        }
        if (sum % k != 0) {
            return false;
        }
        int t = sum / k;
        used = new boolean[nums.length];
        dfs698(nums, t, -1, new ArrayList<>(), 0);
        for (boolean b : used) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

    boolean[] used;

    // [4, 3, 2, 3, 5, 2, 1], k = 4
    public boolean dfs698(Integer[] num, int t, int index, List<Integer> list, int sum) {
        if (sum == t) {
            for (int i : list) {
                used[i] = true;
            }
            return true;
        }
        if (sum > t) {
            return false;
        }
        for (int i = index + 1; i < num.length; i++) {
            if (used[i]) {
                continue;
            }
            // 5, 4, 4, 3, 2
            if (i > index + 1 && !used[i - 1] && num[i].equals(num[i - 1])) {  // 剪枝，如果该元素前一个一样的不能用，那这个也不能用
                continue;
            }
            sum += num[i];
            list.add(i);
            boolean ret = dfs698(num, t, i, list, sum);
            sum -= num[i];
            list.remove(list.size() - 1);
            if (ret && !list.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // 78. 子集
    // 给你一个整数数组 nums ，数组中的元素"互不相同" 。返回该数组所有可能的子集
    // 经典回溯
    List<List<Integer>> ans78 = new ArrayList<>();

    public List<List<Integer>> subsets(int[] nums) {
        List<Integer> current = new ArrayList<>();
        dfs78(nums, -1, current);
        return ans78;
    }

    public void dfs78(int[] nums, int start, List<Integer> current) {
        ans78.add(new ArrayList<>(current));

        for (int i = start + 1; i < nums.length; i++) {
            current.add(nums[i]);
            dfs78(nums, i, current);
            current.remove(current.size() - 1);  // List要用.size()，数组要用.length
        }
    }

    // 90. 子集 II
    // 给你一个整数数组 nums ，其中可能包含重复元素，请你返回该数组所有可能的子集
    // dfs, 回溯算法
    // 回溯算法，回溯一棵树，就是回溯一棵树的一个个从根到叶的路径，使用List记录路径元素
    List<List<Integer>> ans90 = new ArrayList<>();

    public List<List<Integer>> subsetsWithDup(int[] nums) {
        Arrays.sort(nums);
        dfs90(nums, 0, new ArrayList<>());
        return ans90;
    }

    public void dfs90(int[] nums, int index, List<Integer> t) {
        ans90.add(new ArrayList<>(t));
        // 节点的父节点相同，叫同一层

        // 1. 递归边界
        if (index == nums.length) {
            return;
        }

        // 现在位于父节点，向下各个子节点
        // 2. 在进行下层子节点遍历之前，的"初始化"代码，只会执行一次
        Set<Integer> set = new HashSet<>();

        for (int i = index; i < nums.length; i++) {  // 每一层中的元素；从下一个开始，而不是从最开头开始 —— 构建树的技巧

            // 3. 进行向下各个子节点遍历过程中，处理每个具体元素的代码
            if (set.contains(nums[i])) {  // 根据要求剪枝
                continue;
            }
            set.add(nums[i]);
            t.add(nums[i]);

            // 4. 进入下一层
            dfs90(nums, i + 1, t);  // 找到子节点 nums[i + 1] 进入下一层
            t.remove(t.size() - 1);  // 回溯
        }
    }
}
