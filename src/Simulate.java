import java.util.*;

import static utils.Utils.gcd;

public class Simulate {

    // 1337. 矩阵中战斗力最弱的 K 行
    // 按战斗力从小到大排序，战斗力一样的按下标从小到大排序
    public int[] kWeakestRows(int[][] mat, int k) {
        int n = mat.length;
        int m = mat[0].length;
        int[] sum = new int[n];
        for (int i = 0; i < n; i++) {
            int cnt = 0;
            for (int j = 0; j < m; j++) {
                if (mat[i][j] != 1) break;
                cnt++;
            }
            sum[i] = cnt * 100 + i;  // 用一个数组存储战斗力（高位）和下标（低位），直接排序即可
        }
        Arrays.sort(sum);
        int[] ans = new int[k];
        for (int i = 0; i < k; i++) {
            ans[i] = sum[i] % 100;  // 取低位获得下标
        }
        return ans;
    }

    // 5831. 你可以工作的最大周数
    // 给定一个数组，下标i的工作有多少件
    // 每周都得工作，连续两周不能做相同工作
    // 求不违反上面规则的情况下你 最多 能工作多少周
    public long numberOfWeeks(int[] milestones) {
        long sum = 0;
        long max = 0;
        for (int n : milestones) {
            sum += n;
            max = Math.max(n, max);
        }
        if (sum >= 2 * max) {
            return sum;
        }
        return (sum - max) * 2L + 1;
    }

    // 5187. 收集足够苹果的最小花园周长
    public long minimumPerimeter(long neededApples) {
        long sum = 0;
        long k = 1;
        while (true) {
            sum += 12 * k * k;
            if (sum >= neededApples) {
                return 8 * k;
            }
            k++;
        }
    }

    // 1942. 最小未被占据椅子的编号
    public int smallestChair(int[][] times, int targetFriend) {
        int[] t = times[targetFriend];
        Arrays.sort(times, (o1, o2) -> o1[0] - o2[0]);
        int[] chairs = new int[10002];  // 记录椅子使用结束的时间
        Arrays.fill(chairs, 1);
        for (int[] time : times) {
            int chair = getNext(chairs, time[0]);
            if (time[0] == t[0]) {
                return chair;
            }
            chairs[chair] = time[1];
        }
        return -1;
    }

    public int getNext(int[] chairs, int arr) {
        for (int i = 0; i < chairs.length; i++) {
            if (chairs[i] <= arr) {
                return i;
            }
        }
        return 10001;
    }

    // 1818. 绝对差值和
    // 使用Set去重并没有变快
    public int minAbsoluteSumDiff(int[] nums1, int[] nums2) {
        int n = nums1.length;
        long ans = 0;
        long max = 0;
        TreeSet<Integer> set = new TreeSet<>();
        for (int i : nums1) {
            set.add(i);
        }
        for (int i = 0; i < n; i++) {
            int abs = Math.abs(nums1[i] - nums2[i]);
            ans += abs;
            if (max > abs) {
                continue;
            }
            long tmpMax = 0;
            Integer h = set.floor(nums2[i]);
            Integer l = set.ceiling(nums2[i]);
            if (h != null) tmpMax = Math.max(tmpMax, abs - Math.abs(nums2[i] - h));
            if (l != null) tmpMax = Math.max(tmpMax, abs - Math.abs(nums2[i] - l));
            if (tmpMax > max) max = tmpMax;
        }
        return (int) ((ans - max) % 1000000007);
    }

    public int minAbsoluteSumDiff1(int[] nums1, int[] nums2) {
        int n = nums1.length;
        long ans = 0;
        int max = 0;
        for (int i = 0; i < n; i++) {
            int abs = Math.abs(nums1[i] - nums2[i]);
            ans += abs;
            if (max >= abs) {
                continue;
            }
            int tmpMax = 0;
            for (int k : nums1) {
                int t = Math.abs(nums2[i] - k);
                if (abs - t > tmpMax) {
                    tmpMax = abs - t;
                }
            }
            max = Math.max(tmpMax, max);
        }
        return (int) ((ans - max) % 1000000007);
    }

    // 5809. 长度为 3 的不同回文子序列
    // dfs 可以摘出所有长度为 3 的序列，但解决此题明显不需要这样做，站在更高层次，根据题目给的限制来看，这种做法合适。
    public int countPalindromicSubsequence1(String s) {
        int ans = 0;
        for (int i = 0; i < 26; i++) {
            Set<Character> set = new HashSet<>();
            char target = (char) ('a' + i);
            int l = 0;
            int r = s.length() - 1;
            while (l < s.length() - 2 && s.charAt(l) != target) {
                l++;
            }
            while (r > 1 && s.charAt(r) != target) {
                r--;
            }
            if (s.charAt(l) == target && s.charAt(r) == target) {
                for (int k = l + 1; k < r; k++) {
                    set.add(s.charAt(k));
                }
                ans += set.size();
            }
        }
        return ans;
    }

    // dfs做法(没必要)
    int ansPal = 0;

    public int countPalindromicSubsequence(String s) {
        char[] t = new char[3];
        dfsSearch(s, 0, t, 0);
        return ansPal;
    }

    public void dfsSearch(String s, int index, char[] t, int tIndex) {
        if (tIndex == 3) {
            if (t[0] == t[2]) {
                ansPal++;
            }
            return;
        }
        // 去重，跳过value相同的子节点的遍历
        Set<Character> set = new HashSet<>();
        for (int i = index; i < s.length(); i++) {
            if (set.contains(s.charAt(i))) {
                continue;
            }
            set.add(s.charAt(i));
            t[tIndex] = s.charAt(i);
            dfsSearch(s, i + 1, t, tIndex + 1);
        }
    }

    public String frequencySort(String s) {
        Map<Character, Integer> map = new HashMap<>();
        int n = s.length();
        // 建立 key -> 次数的 map
        for (int i = 0; i < n; i++) {
            if (map.containsKey(s.charAt(i))) {
                int t = map.get(s.charAt(i)) + 1;
                map.put(s.charAt(i), t);
                continue;
            }
            map.put(s.charAt(i), 1);
        }
        // 建立 次数 -> key 的数组
        List<Character>[] array = new List[n + 1];
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            if (array[entry.getValue()] == null) {
                array[entry.getValue()] = new ArrayList<Character>(entry.getKey());
            }
            array[entry.getValue()].add(entry.getKey());
        }
        // 构建
        StringBuilder sb = new StringBuilder();
        for (int i = n; i > 0; i--) {
            if (array[i] == null) continue;
            List<Character> list = array[i];
            for (char c : list) {
                for (int j = 0; j < i; j++) {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
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

    // 554. 砖墙
    public int leastBricks(List<List<Integer>> wall) {
        Map<Integer, Integer> map = new HashMap<>();  // 使用数组内存越界，考虑使用 map
        for (List<Integer> line : wall) {
            int index = 0;
            for (int i : line) {
                index = index + i;
                map.put(index, map.getOrDefault(index, 0) + 1);  // 使用map.getOrDefault()
            }
            map.remove(index);
        }
        int max = 0;
        for (int i : map.values()) {  // 遍历map.values()
            max = Math.max(max, i);
        }
        return wall.size() - max;
    }

    /*
     * 149. 直线上最多的点数
     *
     * 给出一些点，找出在一条线上点最多的点
     */
    public int maxPoints(int[][] ps) {
        int n = ps.length;
        int ans = 1;
        // 从一个点出发，遍历其他点，用HashMap统计最多的斜率
        for (int i = 0; i < n; i++) {
            Map<String, Integer> map = new HashMap<>();
            // 由当前点 i 发出的直线所经过的最多点数量
            int max = 0;
            int x1 = ps[i][0], y1 = ps[i][1];
            for (int j = i + 1; j < n; j++) {
                int x2 = ps[j][0], y2 = ps[j][1];
                int a = x1 - x2, b = y1 - y2;
                // 可保证正负性
                int k = gcd(a, b);
                String key = (a / k) + "_" + (b / k);
                map.put(key, map.getOrDefault(key, 0) + 1);
                max = Math.max(max, map.get(key));
            }
            ans = Math.max(ans, max + 1);
        }
        return ans;
    }


    // LCP30. 魔塔游戏
    public int magicTower(int[] nums) {
        List<Integer> lastList = new ArrayList<>();  // 直接累加即可，不必放入lastList
        PriorityQueue<Integer> negativeBuffer = new PriorityQueue<>();  // 使用PriorityQueue数据结构
        long hp = 1;  // 必须用long，否则会溢出
        for (int num : nums) {
            hp += num;
            if (num < 0) {
                negativeBuffer.add(num);
            }
            if (hp <= 0) {
                int min = negativeBuffer.poll();
                lastList.add(min);
                // min < nums[i], min - nums[i] < 0
                hp = hp - min;
            }
        }
        for (Integer n : lastList) {
            hp += n;
            if (hp <= 0) {
                return -1;
            }
        }
        return lastList.size();
    }

    // 706. 设计哈希映射(HashMap)
    public static class Node {
        int key, value;
        Node next;
        boolean isDeleted;

        Node(int _key, int _value) {
            key = _key;
            value = _value;
        }
    }

    // 冲突时的偏移量
    int OFFSET = 1;
    Node[] nodes = new Node[10009];

    public void put(int key, int value) {
        int idx = getIndex(key);
        Node node = nodes[idx];
        if (node != null) {
            node.value = value;
            node.isDeleted = false;
        } else {
            node = new Node(key, value);
            nodes[idx] = node;
        }
    }

    public void remove1(int key) {
        Node node = nodes[getIndex(key)];
        if (node != null) node.isDeleted = true;
    }

    public int get(int key) {
        Node node = nodes[getIndex(key)];
        if (node == null) return -1;
        return node.isDeleted ? -1 : node.value;
    }

    // 当 map 中没有 key 的时候，getIndex 总是返回一个空位置
    // 当 map 中包含 key 的时候，getIndex 总是返回 key 所在的位置
    // 开放寻址法
    int getIndex(int key) {
        int hash = Integer.hashCode(key);
        hash ^= (hash >>> 16);
        int n = nodes.length;
        int idx = hash % n;
        while (nodes[idx] != null && nodes[idx].key != key) {
            hash += OFFSET;
            idx = hash % n;
        }
        return idx;
    }

    // 705. 计哈希集合
    private static final int BASE = 769;
    private LinkedList[] data;  // Java原生链表的列表

    /**
     * Initialize your data structure here.
     */
    public void MyHashSet() {
        data = new LinkedList[BASE];
        for (int i = 0; i < BASE; ++i) {
            data[i] = new LinkedList<Integer>();  // Java原生链表
        }
    }

    public void add(int key) {
        int h = hash(key);
        Iterator<Integer> iterator = data[h].iterator();
        while (iterator.hasNext()) {
            Integer element = iterator.next();
            if (element == key) {
                return;
            }
        }
        data[h].offerLast(key);
    }

    public void remove(int key) {
        int h = hash(key);
        Iterator<Integer> iterator = data[h].iterator();
        while (iterator.hasNext()) {
            Integer element = iterator.next();
            if (element == key) {
                data[h].remove(element);
                return;
            }
        }
    }

    /**
     * Returns true if this set contains the specified element
     */
    public boolean contains(int key) {
        int h = hash(key);
        Iterator<Integer> iterator = data[h].iterator();
        while (iterator.hasNext()) {
            Integer element = iterator.next();
            if (element == key) {
                return true;
            }
        }
        return false;
    }

    private static int hash(int key) {
        return key % BASE;
    }

    // 227 基本计算器Ⅱ
    public int calculate4(String s) {
        Deque<Integer> stack = new LinkedList<Integer>();
        char preSign = '+';
        int num = 0;
        int n = s.length();
        for (int i = 0; i < n; ++i) {
            if (Character.isDigit(s.charAt(i))) {
                num = num * 10 + s.charAt(i) - '0';
            }
            if (!Character.isDigit(s.charAt(i)) && s.charAt(i) != ' ' || i == n - 1) {
                switch (preSign) {
                    case '+':
                        stack.push(num);
                        break;
                    case '-':
                        stack.push(-num);
                        break;
                    case '*':
                        stack.push(stack.pop() * num);
                        break;
                    default:
                        stack.push(stack.pop() / num);
                }
                preSign = s.charAt(i);
                num = 0;
            }
        }
        int ans = 0;
        while (!stack.isEmpty()) {
            ans += stack.pop();
        }
        return ans;
    }

    // 224 基本计算器
    public static int calculate2(String s) {
        Deque<Integer> ops = new LinkedList<Integer>();
        ops.push(1);
        int sign = 1;
        int ret = 0;
        int n = s.length();
        int i = 0;
        while (i < n) {
            if (s.charAt(i) == ' ') {
                i++;
            } else if (s.charAt(i) == '+') {
                sign = ops.peek();
                i++;
            } else if (s.charAt(i) == '-') {
                sign = -ops.peek();
                i++;
            } else if (s.charAt(i) == '(') {
                ops.push(sign);
                i++;
            } else if (s.charAt(i) == ')') {
                ops.pop();
                i++;
            } else {
                long num = 0;
                while (i < n && Character.isDigit(s.charAt(i))) {
                    num = num * 10 + s.charAt(i) - '0';  // char和int互相转化
                    i++;
                }
                ret += sign * num;
            }
        }
        return ret;
    }


    // 剑指 09
    public Stack<Integer> addStack = new Stack<>();
    public Stack<Integer> delStack = new Stack<>();

    public void appendTail(int value) {
        addStack.add(value);
    }

    public int deleteHead() {
        if (delStack.isEmpty()) {
            if (addStack.isEmpty()) {
                return -1;
            }
            while (!addStack.isEmpty()) {
                delStack.add(addStack.pop());
            }
        }
        return delStack.pop();
    }

    // 12. 整数转罗马数字
    // 罗马数字只是简单的堆砌，没有像普通数字每一位之间有倍数关系
    // 罗马数字大的字符会在小的字符左边，当小的字符在大的字符左边时，就是4(IV), 9(IX), 40(XL), 90(XC), 400(CD), 900(CM)等等
    public String intToRoman(int num) {
        int[] c = new int[]{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] r = new String[]{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuilder ans = new StringBuilder();

        for (int i = 0; i < 13; i++) {
            while (num >= c[i]) {
                num -= c[i];
                ans.append(r[i]);
            }
        }
        return ans.toString();
    }

    // 13. 罗马数字转整数
    public int romanToInt(String s) {
        int sum = 0;
        HashMap<Character, Integer> map = new HashMap<>();
        map.put('I', 1);
        map.put('V', 5);
        map.put('X', 10);
        map.put('L', 50);
        map.put('C', 100);
        map.put('D', 500);
        map.put('M', 1000);
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            // 如果小的数字在大的左边
            if (i < chars.length - 1 && map.get(chars[i]) < map.get(chars[i + 1])) {
                sum -= map.get(chars[i]);
            } else sum += map.get(chars[i]);
        }
        return sum;
    }
}
