package solution.cache;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

class LFUCache {

    Map<Integer, Node> cache;  // 存储缓存的内容
    Map<Integer, LinkedHashSet<Integer>> freqMap;  // 存储每个频次对应的双向链表
    int size;
    int capacity;
    int min;  // 存储当前最小频次

    public LFUCache(int capacity) {
        cache = new HashMap<>(capacity);
        freqMap = new HashMap<>();
        this.capacity = capacity;
    }

    public int get(int key) {
        Node node = cache.get(key);
        if (node == null) {
            return -1;
        }
        freqInc(node);
        return node.value;
    }

    public void put(int key, int value) {
        if (capacity == 0) {
            return;
        }
        Node node = cache.get(key);
        if (node != null) {
            node.value = value;
            freqInc(node);
        } else {
            if (size == capacity) {
                int deadNode = removeNode();
                cache.remove(deadNode);
                size--;
            }
            Node newNode = new Node(key, value);
            cache.put(key, newNode);
            addNode(newNode);
            size++;
        }
    }

    void freqInc(Node node) {
        // 从原freq对应的链表里移除, 并更新min
        int freq = node.freq;
        LinkedHashSet<Integer> set = freqMap.get(freq);
        set.remove(node.key);
        if (freq == min && set.size() == 0) {
            min = freq + 1;
        }
        // 加入新freq对应的链表
        node.freq++;
        LinkedHashSet<Integer> newSet = freqMap.get(freq + 1);
        if (newSet == null) {
            newSet = new LinkedHashSet<>();
            freqMap.put(freq + 1, newSet);
        }
        newSet.add(node.key);
    }

    void addNode(Node node) {
        LinkedHashSet<Integer> set = freqMap.get(1);
        if (set == null) {
            set = new LinkedHashSet<>();
            freqMap.put(1, set);
        }
        set.add(node.key);
        min = 1;
    }

    Integer removeNode() {
        LinkedHashSet<Integer> set = freqMap.get(min);
        Integer deadNode = set.iterator().next();  // 此处，先添加的先 get 到，后添加的后 get 到，类似队列
        set.remove(deadNode);
        return deadNode;
    }

    public static class Node {
        int key;  // 需要往 freq set 中放的
        int value;  // 原本的职责
        int freq = 1;  // 频率，freqInc()中会用到

        public Node() {}

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
}
