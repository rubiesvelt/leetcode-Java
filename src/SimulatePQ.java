import main.Main;

import java.util.*;

public class SimulatePQ {

    // 2054. 两个最好的不重叠活动
    public int maxTwoEvents(int[][] events) {
        int n = events.length;
        Arrays.sort(events, (o1, o2) -> o1[0] - o2[0]);
        PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2) -> o1[1] - o2[1]);
        int maxScore = 0;
        int max = 0;
        int ans = 0;
        for (int i = 0; i < n; i++) {
            int start = events[i][0];
            int score = events[i][2];

            maxScore = Math.max(score, maxScore);
            // 从pq中取出所有 endTime >= start 的元素
            while (!pq.isEmpty()) {
                int[] cur = pq.peek();
                if (cur[1] >= start) break;
                pq.poll();
                max = Math.max(max, cur[2]);
            }
            ans = Math.max(ans, max + score);
            pq.add(events[i]);
        }

        if (ans < maxScore) ans = maxScore;
        return ans;
    }

    // 743. 网络延迟时间
    public int networkDelayTime(int[][] times, int n, int k) {
        Set<Integer> used = new HashSet<>();
        PriorityQueue<Pair<Integer, Integer>> queue = new PriorityQueue<>((o1, o2) -> o1.second - o2.second);
        queue.add(new Pair<>(k, 0));
        int sum = 0;
        while (true) {
            Pair<Integer, Integer> now = null;
            if (!queue.isEmpty()) {
                now = queue.poll();
                if (used.contains(now.first)) {
                    continue;
                }
            }
            if (now == null) {
                return used.size() < n || sum == 0 ? -1 : sum;
            }
            used.add(now.first);
            sum = now.second;
            for (int[] it : times) {
                if (it[0] == now.first) {
                    if (used.contains(it[1])) {
                        continue;
                    }
                    queue.add(new Pair<>(it[1], now.second + it[2]));
                }
            }
        }
    }

    // id, 距离
    public static class Pair<T1, T2> {
        T1 first;
        T2 second;

        public Pair(T1 first, T2 second) {
            this.first = first;
            this.second = second;
        }
    }


    // 1882. 使用服务器处理任务
    public int[] assignTasks(int[] servers, int[] tasks) {
        // 定义优先队列sq1（存入int[]{服务器标号，权重}），并重写Comparator（权重从小到大，标号从小到大）
        PriorityQueue<int[]> sq1 = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                if (a[1] == b[1]) {
                    return a[0] - b[0];
                }
                return a[1] - b[1];
            }
        });
        // 定义优先队列sq2（存入int[]{服务器标号，权重，服务器完成工作时间}，并重写Comparator（完成工作时间从小到大）
        PriorityQueue<int[]> sq2 = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return a[2] - b[2];
            }
        });
        for (int i = 0; i < servers.length; i++) {
            sq1.offer(new int[]{i, servers[i]});
        }
        int n = tasks.length;
        int[] res = new int[n];
        int r = 0;
        Deque<Integer> lst = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            // 检测sq2中是否有 完成时间小于<=当前时间的服务器，若有，则从sq2中取出该服务器并加入sq1中
            while (!sq2.isEmpty() && sq2.peek()[2] <= i) {
                int[] tas = sq2.poll();
                sq1.offer(new int[]{tas[0], tas[1]});
            }
            // 任务从尾部进入队列
            lst.offerLast(tasks[i]);
            // 当sq1和lst都不为空时，说明有任务可以加到服务器中，此时从lst头部取出任务，并从sq1中取出服务器，两者结合后添加到sq2中
            // 题目原话：如果同一时刻存在多台空闲服务器，可以同时将多项任务分别分配给它们。（很重要）
            while (!sq1.isEmpty() && !lst.isEmpty()) {
                int[] ser = sq1.poll();
                res[r++] = ser[0];
                sq2.offer(new int[]{ser[0], ser[1], i + lst.pollFirst()});
            }
        }
        int t = n;
        //若是lst依旧不为空，说明服务器资源已满，需要等待
        while (!lst.isEmpty()) {
            //因此我们取出完成时间最小的所有服务器（多个服务器可能会同时解放）
            if (!sq2.isEmpty()) {
                //需把时间t置为服务器解放时间（很多超时是因为t逐一累加）
                t = sq2.peek()[2];
                while (!sq2.isEmpty() && sq2.peek()[2] == t) {
                    int[] tas = sq2.poll();
                    sq1.offer(new int[]{tas[0], tas[1]});
                }
            }
            //仿照上面把任务添加进空闲服务器
            while (!sq1.isEmpty() && !lst.isEmpty()) {
                int[] ser = sq1.poll();
                res[r++] = ser[0];
                sq2.offer(new int[]{ser[0], ser[1], t + lst.pollFirst()});
            }
        }
        return res;
    }

    // 1834. 单线程 CPU
    // 模拟过程的问题，一定要从实际出发，设计合理的逻辑。
    public int[] getOrder(int[][] tasks) {
        CpuTask[] cpuTasks = new CpuTask[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            cpuTasks[i] = new CpuTask(tasks[i][0], tasks[i][1], i);
        }
        Arrays.sort(cpuTasks, Comparator.comparingInt(o -> o.start));
        PriorityQueue<CpuTask> pq = new PriorityQueue<>((o1, o2) -> {
            if (o1.cost != o2.cost) {
                return o1.cost - o2.cost;
            }
            if (o1.index != o2.index) {
                return o1.index - o2.index;
            }
            return 0;
        });
        int now = 0;
        int[] ans = new int[tasks.length];
        int i = 0;  // cpuTasks的指针
        int p = 0;  // ans的指针

        while (i < tasks.length) {
            // 将start在now之前的元素入队
            while (i < tasks.length && cpuTasks[i].start <= now) {  // 此时包含now，意味时间等于now到达的也参队列中的排序
                pq.offer(cpuTasks[i]);
                i++;
            }
            // 如果pq为空，则当前CPU空闲，将最近的任务入队
            if (pq.isEmpty()) {
                int start = cpuTasks[i].start;
                while (i < tasks.length && cpuTasks[i].start == start) {
                    pq.offer(cpuTasks[i++]);
                }
                now = start;
            }
            // 此时pq不为空
            CpuTask task = pq.poll();
            now += task.cost;
            ans[p++] = task.index;
        }
        while (!pq.isEmpty()) {
            CpuTask next = pq.poll();
            ans[p++] = next.index;
        }
        return ans;
    }

    public static class CpuTask {
        int start;
        int cost;
        int index;

        public CpuTask(int start, int cost, int index) {
            this.start = start;
            this.cost = cost;
            this.index = index;
        }
    }
}
