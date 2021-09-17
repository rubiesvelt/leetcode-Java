package main;

import beans.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        List<Integer> list = new ArrayList<>();
        Main main = new Main();
        char a = 'a';
        int i = 96;
        TreeNode tn4 = new TreeNode(4);
        TreeNode tn3 = new TreeNode(3);
        TreeNode tn2 = new TreeNode(1, null, null);
        TreeNode tn1 = new TreeNode(-2, tn2, null);

        int[][] matrix = {{1, 0, 0}, {0, 1, 1}, {0, 1, 1}};
        int[][] matrix1 = {{1, 0, 0}, {0, 0, 1}, {1, 1, 0}};
        int[] dist = {1, 0, 1};
        int[] diff = {3,3,3};
        int[] speed = {5, 1, 5, 5, 1, 5, 3, 4, 5, 1, 4};
        char[][] matrix2 = {{'.', '.', 'W', '.', 'B', 'W', 'W', 'B'}, {'B', 'W', '.', 'W', '.', 'W', 'B', 'B'}, {'.', 'W', 'B', 'W', 'W', '.', 'W', 'W'}, {'W', 'W', '.', 'W', '.', '.', 'B', 'B'}, {'B', 'W', 'B', 'B', 'W', 'W', 'B', '.'}, {'W', '.', 'W', '.', '.', 'B', 'W', 'W'}, {'B', '.', 'B', 'B', '.', '.', 'B', 'B'}, {'.', 'W', '.', 'W', '.', 'W', '.', 'W'}};

        fishThread.start();
        catThread.start();
        dogThread.start();
        return;
    }

    // 3 threads,  output 100 times ,
    // array := ["fish", "cat", "dog"]
    // business logic : output one animal
    // in order:
    // 0 fish
    // 0 cat
    // 0 dog
    // 1 fish
    // 1 cat
    // 1 dog
    // ...
    // 99 fish
    // 99 cat
    // 99 dog

    // 线程轮流获得时间片
    public static FishThread fishThread = new FishThread();
    public static CatThread catThread = new CatThread();
    public static DogThread dogThread = new DogThread();

    public static final Object lock = new Object();

    public static int line = 0;
    public static int cnt = 1;

    // 12 23 13
    public static class FishThread extends Thread {
        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    if (cnt % 3 != 1) {
                        continue;
                    }
                    cnt++;
                    System.out.println(line + " " + "fish");
                    if (line == 99) {
                        lock.notifyAll();
                        return;
                    }
                    lock.notifyAll();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static class CatThread extends Thread {
        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    if (cnt % 3 != 2) {
                        continue;
                    }
                    cnt++;
                    System.out.println(line + " " + "cat");

                    if (line == 99) {
                        lock.notifyAll();
                        return;
                    }

                    lock.notifyAll();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static class DogThread extends Thread {
        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    if (cnt % 3 != 0) {
                        continue;
                    }
                    cnt++;
                    System.out.println(line + " " + "dog");
                    line++;

                    if (line == 100) {
                        lock.notifyAll();
                        return;
                    }
                    lock.notifyAll();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    // 任务系统，一些机器执行任务，复杂任务 多台机器做；实现任务系统，大任务拆分小任务；小任务output agg

    // 一个已经排序的数组，给一个数字，找出数字在数组中的 起始点 和 终止点
    // 1 2 3 3 3 4
    public int[] findStartEnd(int[] nums, int k) {
        int l = 0;
        int r = nums.length - 1;
        int[] ans = new int[2];

        boolean found = false;
        // find left
        while (l < r) {
            int m = (l + r) / 2;
            if (nums[m] > k) {
                r = m;
            } else if (nums[m] < k) {
                l = m + 1;
            } else {
                if (m - 1 < 0) {
                    ans[0] = m;
                    found = true;
                    break;
                }
                if (nums[m - 1] < nums[m]) {
                    ans[0] = m;
                    found = true;
                    break;
                }
                r = m;
            }
        }

        if (!found) {
            return ans;
        }

        // find right
        // 1 2 2 3 3 3
        l = 0;
        r = nums.length - 1;
        while (l < r) {
            int m = (l + r) / 2;
            if (nums[m] > k) {
                r = m;
            } else if (nums[m] < k) {
                l = m + 1;
            } else {
                if (m + 1 == nums.length) {
                    ans[1] = m;
                    break;
                }
                if (nums[m + 1] > nums[m]) {
                    ans[1] = m;
                    break;
                }
                l = m + 1;
            }
        }
        if (nums[l] == k && l == nums.length - 1) {
            ans[1] = l;
        }
        return ans;
    }
}
