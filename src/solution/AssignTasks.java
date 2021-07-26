package solution;

import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * 5774. 使用服务器处理任务
 *
 * 优先队列需要自定义 Comparator，否则毫无意义
 * 定义两个优先队列
 * 一个用来存空闲的服务器
 * 一个用来存正在处理的服务器
 * 再来一个普通队列存tasks
 * 然后一个一个task开始处理
 *
 * task在有限时间内全加入队列，然后只用处理积压的task
 */
public class AssignTasks {

    public int[] assignTasks(int[] servers, int[] tasks) {
        //定义优先队列sq1（存入int[]{服务器标号 ， 权重}），并重写Comparator（权重从小到大，标号从小到大）
        PriorityQueue<int[]> sq1 = new PriorityQueue<int[]>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                if (a[1] == b[1]) {
                    return a[0] - b[0];
                }
                return a[1] - b[1];
            }
        });
        //定义优先队列sq2（存入int[]{服务器标号 ， 权重 ， 服务器完成工作时间}），并重写Comparator（完成工作时间从小到大）
        PriorityQueue<int[]> sq2 = new PriorityQueue<int[]>(new Comparator<int[]>() {
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
            // 检测sq2中是否有 完成时间 <= 当前时间 的服务器，若有，则从sq2中取出该服务器并加入sq1中
            // 当前时间为i
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
        // 若是lst依旧不为空，说明服务器资源已满，需要等待
        while (!lst.isEmpty()) {
            if (!sq2.isEmpty()) {
                // 找到下一台服务器解放的时间 t，而没必要累加
                t = sq2.peek()[2];
                while (!sq2.isEmpty() && sq2.peek()[2] == t) {
                    int[] tas = sq2.poll();
                    sq1.offer(new int[]{tas[0], tas[1]});
                }
            }
            // 仿照上面把任务添加进空闲服务器
            while (!sq1.isEmpty() && !lst.isEmpty()) {
                int[] ser = sq1.poll();
                res[r++] = ser[0];
                sq2.offer(new int[]{ser[0], ser[1], t + lst.pollFirst()});
            }
        }
        return res;
    }
}
