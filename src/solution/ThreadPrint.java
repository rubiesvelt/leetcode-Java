package solution;

public class ThreadPrint {

    public static void main(String[] args) {
        fishThread.start();
        catThread.start();
        dogThread.start();
    }

    // shopee 一面
    // 3 threads,  output 100 times
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
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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
}
