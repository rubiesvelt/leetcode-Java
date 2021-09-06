import java.util.*;


public class Simulate {

    // 179. 最大数
    // 给定一组非负整数 nums，重新排列每个数的顺序（每个数不可拆分）使之组成一个最大的整数
    public String largestNumber(int[] nums) {
        int n = nums.length;
        String[] strNums = new String[n];

        for (int i = 0;i<n;i++) {
            strNums[i] = String.valueOf(nums[i]);
        }

        Arrays.sort(strNums, (o1, o2) -> {
            // 比较 o1o2 和 o2o1 大小
            String s1 = o1 + o2;
            String s2 = o2 + o1;
            return s2.compareTo(s1);
        });
        if (strNums[0].equals("0")) {
            return "0";
        }
        StringBuilder ret = new StringBuilder();
        for (String num : strNums) {
            ret.append(num);
        }
        return ret.toString();
    }

    public String largestNumber1(int[] nums) {
        int n = nums.length;
        String[] strNums = new String[n];
        // 转换成包装类型，以便传入 Comparator 对象（此处为 lambda 表达式）
        Integer[] numsArr = new Integer[n];
        for (int i = 0; i < n; i++) {
            numsArr[i] = nums[i];
        }

        Arrays.sort(numsArr, (x, y) -> {
            long sx = 10, sy = 10;
            while (sx <= x) {
                sx *= 10;
            }
            while (sy <= y) {
                sy *= 10;
            }
            return (int) (-sy * x - y + sx * y + x);
        });

        if (numsArr[0] == 0) {
            return "0";
        }
        StringBuilder ret = new StringBuilder();
        for (int num : numsArr) {
            ret.append(num);
        }
        return ret.toString();
    }

    // 233. 数字 1 的个数
    // 给定一个整数 n，计算所有小于等于 n 的非负整数中数字 1 出现的个数
    // 按位模拟
    public int countDigitOne(int n) {
        String s = String.valueOf(n);
        int m = s.length();
        if (m == 1) {
            return n > 0 ? 1 : 0;
        }
        // 计算第 i 位前缀代表的数值，和后缀代表的数值
        // 例如 12345 则有
        // ps[2] = 12
        // ss[2] = 45
        int[] ps = new int[m];  // 前缀大小
        int[] ss = new int[m];  // 后缀大小
        ss[0] = Integer.parseInt(s.substring(1));
        for (int i = 1; i < m - 1; i++) {
            ps[i] = Integer.parseInt(s.substring(0, i));
            ss[i] = Integer.parseInt(s.substring(i + 1));
        }
        ps[m - 1] = Integer.parseInt(s.substring(0, m - 1));

        int ans = 0;
        for (int i = 0; i < m; i++) {  // 第i位为1有多少情况
            /*
             * x 为当前位数值，len 为当前位后面长度为多少
             * 如 n = 12345, i = 2
             * prefix = 12
             * suffix = 45
             * 12 3 45
             */
            int x = s.charAt(i) - '0';
            int len = m - i - 1;
            int prefix = ps[i];
            int suffix = ss[i];
            int tot = 0;
            tot += prefix * Math.pow(10, len);  // 0 -> 12 000 区间内 第2位出现次数 12 * 100
            if (x == 0) {  // 当前位为0什么都不做
            } else if (x == 1) {  // 当前位为1加上 suffix + 1
                tot += suffix + 1;
            } else {  // 当前位大于1 "加满"
                tot += Math.pow(10, len);  // 12 000 -> 12 345 区间内 第2位出现1的次数 100
            }
            ans += tot;
        }
        return ans;
    }

    /*
     * 1953. 你可以工作的最大周数
     * 给定一个数组，下标i的工作有多少件
     * 每周都得工作，连续两周不能做相同工作
     * 求不违反上面规则的情况下你 最多 能工作多少周
     *
     * 能工作多少周，瓶颈在任务最多的那个项目
     */
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
    // 使用Set去重并没有变快，能用数组不要用结构体
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
     * 给你一个数组 points，其中 points[i] = [xi, yi] 表示 X-Y 平面上的一个点，求最多有多少个点在同一条直线上。
     */
    public int maxPoints(int[][] ps) {
        int n = ps.length;
        int ans = 1;
        // 从一个点出发，遍历其他点，用HashMap统计最多的斜率
        for (int i = 0; i < n; i++) {
            Map<String, Integer> map = new HashMap<>();
            // 由当前点 i 发出的直线所经过的最多点数量
            int max = 0;
            int x1 = ps[i][0];
            int y1 = ps[i][1];
            for (int j = i + 1; j < n; j++) {  // 确实，从i + 1开始就行，时间复杂度 n^2 -> n log n
                int x2 = ps[j][0];
                int y2 = ps[j][1];
                int a = x1 - x2;
                int b = y1 - y2;
                // 可保证正负性
                int k = Utils.gcd(a, b);
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
