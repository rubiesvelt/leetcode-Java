import beans.ListNode;
import beans.TreeNode;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Main {

    public static void main(String[] args) throws InterruptedException, ParseException {

        List<Integer> list = new ArrayList<>();
        list.sort((o1, o2) -> o1 - o2);
        int t = list.hashCode();
        char a = 'a';
        int i = 96;
        TreeNode tn5 = new TreeNode(5);
        TreeNode tn4 = new TreeNode(4);
        TreeNode tn3 = new TreeNode(3, null, null);
        TreeNode tn2 = new TreeNode(2, tn4, tn5);
        TreeNode tn1 = new TreeNode(1, tn2, tn3);

        String s = "***|**|*****|**||**|*";
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int[][] matrix1 = {{22, 44, 9}, {93, 96, 48}, {56, 90, 3}, {80, 92, 45}, {63, 73, 69}, {73, 96, 33}, {11, 23, 84}, {59, 72, 29}, {89, 100, 46}};
        int[] dist = {25, 11, 29, 6, 24, 4, 29, 18, 6, 13, 25, 30};
        int[] diff = {1, 2, 4};
        int[] speed = {28, 27, 13, 19, 23, 4, 29, 29, 7};
        char[][] matrix2 = {{'.', '.', 'W', '.', 'B', 'W', 'W', 'B'}, {'B', 'W', '.', 'W', '.', 'W', 'B', 'B'}, {'.', 'W', 'B', 'W', 'W', '.', 'W', 'W'}, {'W', 'W', '.', 'W', '.', '.', 'B', 'B'}, {'B', 'W', 'B', 'B', 'W', 'W', 'B', '.'}, {'W', '.', 'W', '.', '.', 'B', 'W', 'W'}, {'B', '.', 'B', 'B', '.', '.', 'B', 'B'}, {'.', 'W', '.', 'W', '.', 'W', '.', 'W'}};

        Main main = new Main();

        return;
    }

    public static List<Integer> contacts1(List<List<String>> queries) {
        List<String> names = new ArrayList<>();
        List<Integer> ans = new ArrayList<>();
        for(List<String> query : queries) {
            if (query.get(0).equals("add")) {
                names.add(query.get(1));
            } else if (query.get(0).equals("find")) {
                int num = 0;
                for (String n : names) {
                    if (n.startsWith(query.get(1))) {
                        num++;
                    }
                }
                ans.add(num);
            }
        }
        return ans;
    }

    public static List<Integer> contacts(List<List<String>> queries) {
        List<Integer> results = new ArrayList<Integer>();
        Map<String, Integer> namePartials = new HashMap<String, Integer>();

        for (List<String> list : queries) {
            String operation = list.get(0);
            String text = list.get(1);

            if (operation.equals("add")) {
                for (int i = 1; i <= text.length(); ++i) {
                    String partial = text.substring(0, i);
                    Integer count = namePartials.get(partial);
                    if (count == null) {
                        count = 0;
                    }
                    count++;
                    namePartials.put(partial, count);
                }
            } else if (operation.equals("find")) {
                Integer count = namePartials.get(text);
                if (count == null) {
                    count = 0;
                }
                results.add(count);
            } else {
                throw new RuntimeException("operation " + operation + " is not supported!");
            }
        }

        return results;
    }



    /*
     * 2071. Maximum Number of Tasks You Can Assign
     */
    public int maxTaskAssign(int[] tasks, int[] workers, int pills, int strength) {
        // 每个工人最多一个药丸，提高 strength 点力量
        // 目标完成最多的工作。
        // 优先完成使用体力最低的工作；不可能存在 使用体力高的工作被完成了，同时使用体力低的工作没有完成
        // tasks = [10,11,12,13,14,15], workers = [6,11,12,13], pills = 1, strength = 5
        // tasks = [5,5,8,9,9], workers = [1,2,4,6,6], pills = 1, strength = 5
        int m = tasks.length;
        int n = workers.length;
        int[][] dp = new int[m][pills];  // dp[i][j] 表示已完成前 m 个任务，此时用了 pills 个药丸
        return 0;
    }

    /*
     * 96. Unique Binary Search Trees
     * 1 -> 1
     * 2 -> 1 * 2 -> 2
     * 3 -> 2 * 2 + 1 -> 5
     * 4 -> (5 + 2) * 2 -> 14
     * 5
     * ->
     * 0,4 -> 14 * 2 = 28
     * 1,3 -> 5 * 2 = 10
     * 2,2 -> 2 * 2 = 4
     * -> 42
     */
    public int numTrees(int n) {
        List<Integer> list = new ArrayList<>();
        // list[i] means when there are i nodes, num of structures when using i as bottom
        list.add(1);
        int acc = 0;
        for (int i = 1; i <= n; i++) {
            acc = 0;
            int half = i / 2;
            int left = i % 2;
            for (int j = 1; j <= half; j++) {  // use j as bottom
                int l = list.get(j - 1);
                int r = list.get(i - j);
                acc += (l * r) * 2;
            }
            if (left == 1) {
                int l = list.get(half);
                acc += l * l;
            }
            list.add(acc);
        }
        return acc;
    }

    /*
     * 264. 丑数 II
     * 2,3,5
     * 4,3,5
     * 4,6,5
     * 8,6,5
     * 8,6,10
     * 8,12,10
     */
    public int nthUglyNumber(int n) {
        int a = 0;
        int b = 0;
        int c = 0;
        List<Long> list = new ArrayList<>();
        list.add(1L);
        long min = 1;
        for (int i = 1; i < n; i++) {
            long min_2_3 = Math.min(2 * list.get(a), 3 * list.get(b));
            min = Math.min(min_2_3, 5 * list.get(c));
            list.add(min);
            if (min == 2 * list.get(a)) a++;
            if (min == 3 * list.get(b)) b++;
            if (min == 5 * list.get(c)) c++;
        }
        return (int) min;
    }

    // 2059. 转化数字的最小运算数
    public int minimumOperations(int[] nums, int start, int goal) {
        Map<Integer, Integer> map = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();
        int move = 0;
        map.put(start, 0);
        queue.add(start);
        while (!queue.isEmpty()) {
            move++;
            List<Integer> list = new ArrayList<>();
            while (!queue.isEmpty()) {
                int t = queue.poll();
                for (int u : nums) {
                    int a1 = t + u;
                    int a2 = t - u;
                    int a3 = t ^ u;
                    if (a1 == goal || a2 == goal || a3 == goal) return move;
                    if (!map.containsKey(a1) && 0 <= a1 && a1 <= 1000) {
                        list.add(a1);
                        map.put(a1, move);
                    }
                    if (!map.containsKey(a2) && 0 <= a2 && a2 <= 1000) {
                        list.add(a2);
                        map.put(a2, move);
                    }
                    if (!map.containsKey(a3) && 0 <= a3 && a3 <= 1000) {
                        list.add(a3);
                        map.put(a3, move);
                    }
                }
            }
            queue.addAll(list);
        }
        return -1;
    }

    // 2058. 找出临界点之间的最小和最大距离
    public int[] nodesBetweenCriticalPoints(ListNode head) {
        int pre = head.val;
        head = head.next;
        if (head == null || head.next == null) return new int[]{-1, -1};
        int cur = head.val;
        int nxt = head.next.val;
        int index = 1;
        List<Integer> list = new ArrayList<>();
        while (head.next != null) {
            cur = head.val;
            nxt = head.next.val;
            if (pre < cur && cur > nxt) {
                list.add(index);
            } else if (pre > cur && cur < nxt) {
                list.add(index);
            }
            pre = cur;
            head = head.next;
            index++;
        }
        if (list.size() < 2) return new int[]{-1, -1};
        int min = 0x3f3f3f3f;
        for (int i = 1; i < list.size(); i++) {
            min = Math.min(min, list.get(i) - list.get(i - 1));
        }
        int[] ret = new int[]{min, list.get(list.size() - 1) - list.get(0)};
        return ret;
    }

}
