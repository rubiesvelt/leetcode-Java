import java.util.LinkedList;
import java.util.Queue;

public class BFS {

    /*
     * LCP 09. 最小跳跃次数
     * 给定一个数组 int[] jump，位于数组第i位置可以向前跳i格，或者跳到 下标小于 i 的任意一格，求最少跳几次能跳出数组？（i >= jump.length）
     * e.g.
     * jump = [2, 5, 1, 1, 1, 1]
     * -> 3
     *
     * BFS最短路径
     */
    public int minJump(int[] jump) {
        int n = jump.length;
        Queue<Pair> queue = new LinkedList<>();
        boolean[] used = new boolean[n];
        int current = 1;
        queue.add(new Pair(0, 0));
        used[0] = true;
        while (!queue.isEmpty()) {
            Pair p = queue.poll();  // queue.pop()
            int id = p.id;
            int cnt = p.cnt;
            int next = id + jump[id];
            if (next > n) {
                return cnt + 1;
            }
            if (!used[next]) {
                used[next] = true;
                queue.offer(new Pair(next, cnt + 1));
            }
            while (current < id) {
                used[current] = true;
                queue.offer(new Pair(current, cnt + 1));
                current++;
            }
        }
        return -1;
    }

    public static class Pair {
        int id;
        int cnt;

        public Pair(int id, int cnt) {
            this.id = id;
            this.cnt = cnt;
        }
    }
}
