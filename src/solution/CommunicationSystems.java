package solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

public class CommunicationSystems {
    // 通信系统
    // add()
    // get()
    // x秒超时
    // 线程安全
    // batch size

    public CommunicationSystems(long timeoutSecond) {
        this.timeoutSecond = timeoutSecond;
    }

    public Queue<Integer> cache = new ConcurrentLinkedQueue<>();

    public long timeoutSecond;

    public void add(Integer element) {
        cache.add(element);
    }

    public List<Integer> get(int batchSize) {
        FutureTask<List<Integer>> future = new FutureTask<>(() -> innerGet(batchSize));
        try {
            return future.get(timeoutSecond, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            System.out.println("execution failed");
        } catch (InterruptedException e) {
            System.out.println("interrupted");
        } catch (TimeoutException e) {
            System.out.println("time out");
        }
        return new ArrayList<>();
    }

    public List<Integer> innerGet(int batchSize) {
        List<Integer> ret = new ArrayList<>();
        for (int i = 0; i < batchSize; i++) {
            if (cache.isEmpty()) {
                break;
            }
            ret.add(cache.poll());
        }
        return ret;
    }
}

