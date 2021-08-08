import beans.TreeNode;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.TreeSet;

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
        int[] speed = {5, 1, 5, 5, 1, 5, 3, 4, 5, 1, 4};
        char[][] matrix2 = {{'.', '.', 'W', '.', 'B', 'W', 'W', 'B'}, {'B', 'W', '.', 'W', '.', 'W', 'B', 'B'}, {'.', 'W', 'B', 'W', 'W', '.', 'W', 'W'}, {'W', 'W', '.', 'W', '.', '.', 'B', 'B'}, {'B', 'W', 'B', 'B', 'W', 'W', 'B', '.'}, {'W', '.', 'W', '.', '.', 'B', 'W', 'W'}, {'B', '.', 'B', 'B', '.', '.', 'B', 'B'}, {'.', 'W', '.', 'W', '.', 'W', '.', 'W'}};

        return;
    }

    // 5, 1, 5, 5, 1, 3, 4, (3, 3, 3) 5, 1, 4
    // 5 -> 3
    // 4 -> 4
    // 3 -> 6
    public int[] longestObstacleCourseAtEachPosition(int[] obstacles) {
        int n = obstacles.length;
        int[] mp = new int[10000008];
        int[] ans = new int[n];

        for (int i = 0; i < n; i++) {
            int t = obstacles[i];
            // t1 为小于等于 t 的第一个数
            int t2 = getMaxIndex(mp, obstacles, t, i);
            mp[t] = mp[t2] + 1;
            ans[i] = mp[t];
        }
        return ans;
    }

    public int getMaxIndex(int[] mp, int[] obstacles, int t1, int i) {
        int maxIndex = 0;
        int max = -1;
        // 从i往下找，obstacles[i]小于等于t1合理，寻找ans中最大的，返回其值
        while (i >= 0) {
            int now = obstacles[i];
            if (now > t1) {
                i--;
                continue;
            }
            if (mp[now] > max) {
                max = mp[now];
                maxIndex = now;
            }
            i--;
        }
        return maxIndex;
    }

    // [][][]
    // [[][[]]]
    // ]]][[[ -> []][[] -> [[][]]
    int rIndex;

    public int minSwaps(String s) {
        char[] cs = s.toCharArray();
        int n = s.length();
        int swap = 0;
        rIndex = s.length() - 1;
        // 从左到右找未配对的[
        int lNum = 0;
        for (int i = 0; i < n; i++) {
            if (cs[i] == '[') {
                lNum++;
                continue;
            }
            if (lNum == 0) {
                int t = findNextRight(cs);
                cs[t] = ']';
                cs[i] = '[';
                lNum++;
                swap++;
                continue;
            }
            lNum--;
        }
        return swap;
    }

    public int findNextRight(char[] cs) {
        while (rIndex > 0) {
            if (cs[rIndex] == '[') {
                rIndex--;
                return rIndex + 1;
            }
            rIndex--;
        }
        return 0;
    }

    public int minStoneSum(int[] piles, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>((o1, o2) -> o2 - o1);
        for (int num : piles) {
            pq.add(num);
        }
        while (k > 0) {
            int t = pq.poll();
            int t1 = t & 1;
            t >>= 1;
            pq.add(t + t1);
            k--;
        }
        int sum = 0;
        while (!pq.isEmpty()) {
            sum += pq.poll();
        }
        return sum;
    }

    public boolean isPrefixString(String s, String[] words) {
        for (String w : words) {
            if (!s.startsWith(w)) {
                return false;
            }
            s = s.substring(w.length());
            if (s.length() == 0) {
                return true;
            }
        }
        return false;
    }

    public long maxProduct(String s) {
        int n = s.length();
        boolean[][] g = new boolean[n][n];
        for (int i = 0; i < n; ++i) {
            Arrays.fill(g[i], true);
        }
        // 发现一个的时候，在之前的里面取最长的
        for (int i = n - 1; i >= 0; --i) {
            for (int j = i + 1; j < n; ++j) {
                g[i][j] = s.charAt(i) == s.charAt(j) && g[i + 1][j - 1];
            }
        }
        int[] longest = new int[n];
        Arrays.fill(longest, 1);
        for (int i = 1; i < n; i++) {  // 包含i的longest
            for (int j = 0; j <= i; j++) {
                if (g[j][i]) {
                    int cur = i - j + 1;
                    if (cur % 2 == 0) {
                        continue;
                    }
                    longest[i] = Math.max(longest[i - 1], cur);
                    break;
                }
                longest[i] = Math.max(longest[i - 1], longest[i]);
            }
        }
        long ans = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                if (g[j][i]) {
                    long cur = i - j + 1;
                    if (cur % 2 == 0) {
                        continue;
                    }
                    long bef;
                    if (j == 0) {
                        continue;
                    } else {
                        bef = longest[j - 1];
                    }
                    long l = cur * bef % 1000000007;
                    ans = Math.max(ans, l);
                }
            }
        }
        return ans;
    }

    public int minSpaceWastedKResizing(int[] nums, int k) {
        int n = nums.length;
        int[] currentMax = new int[n];
        int max = -1;
        for (int i = 0; i < n; i++) {
            max = Math.max(max, nums[i]);
            currentMax[i] = max;
        }
        // 消除k个阶梯
        return 0;

    }

    public boolean checkMove(char[][] board, int rMove, int cMove, char color) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                int[] direction = new int[]{i, j};
                if (isGood(board, rMove, cMove, color, direction)) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean isGood(char[][] board, int rMove, int cMove, char color, int[] direction) {
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

    public String makeFancyString(String s) {
        int n = s.length();
        int cnt = 1;
        int before = -1;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) - 'a' == before) {
                cnt++;
            } else {
                cnt = 1;
            }

            if (cnt < 3) {
                sb.append(s.charAt(i));
            }
            before = s.charAt(i) - 'a';
        }
        return sb.toString();
    }
}
