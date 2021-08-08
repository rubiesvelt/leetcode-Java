import java.util.ArrayList;
import java.util.List;

public class Mathematics {

    // 1943. 描述绘画结果
    // 扫描线，差分
    public List<List<Long>> splitPainting(int[][] segments) {
        boolean[] startPoints = new boolean[100006];
        long[] acc = new long[100006];
        for (int[] seg : segments) {
            acc[seg[0]] += seg[2];
            acc[seg[1]] -= seg[2];
            startPoints[seg[0]] = true;
        }
        List<List<Long>> ans = new ArrayList<>();
        long sum = 0;
        int pre = 0;
        for (int i = 0; i < acc.length; i++) {
            if (acc[i] == 0 && !startPoints[i]) continue;
            if (sum == 0) {
                sum = acc[i];
                pre = i;
                continue;
            }
            List<Long> l = new ArrayList<>();
            l.add((long) pre);
            l.add((long) i);
            l.add(sum);
            ans.add(l);
            pre = i;
            sum += acc[i];
        }
        return ans;
    }

    // 995. K 连续位的最小翻转次数
    // 差分数组
    public int minKBitFlips(int[] A, int K) {
        int n = A.length;
        int[] diff = new int[n + 1];  // 差分数组
        int ans = 0;
        int revCnt = 0;
        for (int i = 0; i < n; i++) {
            revCnt += diff[i];  // 当前项目被翻转了revCnt次
            if ((A[i] + revCnt) % 2 == 0) {  // 照现在这样，A[i]将会是0
                if (i + K > n) {
                    return -1;
                }
                ans++;
                revCnt++;  // 这俩配合，相当于将当前 至 后K个元素反转
                diff[i + K]++;  // 此处++和--效果一样
            }
        }
        return ans;
    }

    // 1922. 统计好数字的数目
    // 快速幂
    // 取模应该写在外面，并使用同一mod，否则类型转换会出现意想不到的错误
    long mod = (long) (1e9 + 7);

    public int countGoodNumbers1(long n) {
        // 直接用快速幂
        return (int) (Utils.quickPow(5, (n + 1) / 2, 1000000007) * Utils.quickPow(4, n / 2, 1000000007) % mod);
    }

    public int countGoodNumbers(long n) {
        if (n == 1) return 5;
        long a = n / 2;
        long b = n % 2;
        // 求 20 的 a 次方 与 1e9 求余的结果
        // (a * b) % q = (a % q * b % q) % q
        long mod = (long) (1e9 + 7);
        long ans = 1;
        long base = 20;
        while (a > 0) {
            // 多余的那个
            long t = a & 1;
            if (t == 1) ans = ans * base % mod;
            // other *= Math.pow(base, t) % mod;
            // 正式的
            a /= 2;
            base = base * base % mod;
        }
        // int t = (int) (Math.pow(5, b) * (other % mod) * (base % mod) % mod);
        return (int) (ans * Math.pow(5, b) % mod);
    }

    /*
     * 810. 黑板异或游戏
     * 博弈论
     * 经典场景：n = 101，两个人依次操作，每个人可以选 1-9 中一个数从n中减去，减到0者胜；先手必胜
     */
    public boolean xorGame(int[] nums) {
        int sum = 0;
        for (int i : nums) sum ^= i;
        /*
         * 如果数组异或和是0，则 先手必胜
         * 如果数组异或和不为0，但序列长度为偶数，那么最终会出现 后手必败
         */
        return sum == 0 || nums.length % 2 == 0;
    }

    // 137. 只出现一次的数字 II
    // 有限状态自动机
    public int singleNumber(int[] nums) {
        int ones = 0, twos = 0;
        for (int num : nums) {
            /*
             * 每一位
             * 第一次出现 two = 0, one = 1
             * 第二次出现 two = 1, one = 0
             * 第三次出现 two = 0, one = 0
             */
            ones = ones ^ num & ~twos;
            twos = twos ^ num & ~ones;
        }
        return ones;
    }


    // 面试题 17.10. 主要元素
    // 数组中大于一半的元素叫主要元素
    // 摩尔计数
    public int majorityElement(int[] nums) {
        int x = -1;
        int cnt = 0;
        for (int n : nums) {
            if (cnt == 0) {
                x = n;
                cnt = 1;
            } else {
                if (n == x) cnt++;
                else cnt -= 1;
            }
        }
        cnt = 0;
        for (int n : nums) {
            if (n == x) cnt++;
        }
        return cnt > nums.length / 2 ? x : -1;
    }

    // 633. 平方数之和
    // 平方开方
    public boolean judgeSquareSum(int c) {
        int i = 0;
        while (i * i <= c / 2) {
            if (isSqrt(c - i * i)) {
                return true;
            }
            i++;
        }
        return false;
    }

    public static boolean isSqrt(double n) {
        double d = Math.sqrt(n);
        return d % 1 == 0;
    }
}
