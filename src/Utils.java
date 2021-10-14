public class Utils {

    public static class Pair<T1, T2> {
        public T1 fst;

        public T2 snd;

        public Pair(T1 fst, T2 snd) {
            this.fst = fst;
            this.snd = snd;
        }
    }

    /*
     * lower_bound()函数 用来求一个容器中，第一个 大于等于 所要查找的元素的地址
     * upper_bound()函数 用来求一个容器中，第一个 大于 所要查找的元素的地址
     *
     * 使用要求: 0 <= l <= r, l <= r <= nums.length()
     * 下标判断范围: [l, r)，t返回范围 [l, r]
     *
     * e.g.
     * nums = {1, 4, 4, 5}, t = 0 -> lower_bound = 0, upper_bound = 0
     * nums = {1, 4, 4, 5}, t = 1 -> lower_bound = 0, upper_bound = 1
     * nums = {1, 4, 4, 5}, t = 2 -> lower_bound = 1, upper_bound = 1
     * nums = {1, 4, 4, 5}, t = 4 -> lower_bound = 1, upper_bound = 3
     * nums = {1, 4, 4, 5}, t = 5 -> lower_bound = 3, upper_bound = 4
     * nums = {1, 4, 4, 5}, t = 6 -> lower_bound = 4, upper_bound = 4
     */
    public static int lower_bound(int[] nums, int l, int r, int t) {
        while (l < r) {
            int mid = (l + r) >> 1;
            if (nums[mid] >= t) {
                r = mid;
            } else {
                l = mid + 1;  // 如果 l = mid 与 mid = (l + r) >> 1 一起可能陷入死循环
            }
        }
        return l;
    }

    public static int upper_bound(int[] nums, int l, int r, int t) {
        while (l < r) {
            int mid = (l + r) >> 1;
            if (nums[mid] > t) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    // 快速幂，求a的b次方，结果对mod取余
    // 思路
    // a ^ 2x = (a ^ 2) ^ x
    // a ^ 2x+1 = (a ^ 2) ^ x * a
    // a ^ 19 = a * a * a * (a^8) * (a^8)
    public static long quickPow(long a, long b, long mod) {
        long ans = 1;
        while (b > 0) {
            if ((b & 1) == 1) {
                ans = a * ans % mod;
            }
            a = a * a % mod;
            b >>= 1;  // 相当于 b /= 2
        }
        return ans;
    }

    // greatest common divisor，最大公约数
    public static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    /**
     * 效果等同于 Integer.parseInt()
     * 将String转化成Int，支持负号
     *
     * @param s String 型数
     * @return 转成 int 后的结果
     */
    public static int stringToInt(String s) {
        boolean negative = s.charAt(0) == '-';
        int i = negative ? 1 : 0;
        int ret = 0;
        while (i < s.length()) {
            ret *= 10;
            ret += s.charAt(i++) - '0';
        }
        return negative ? -ret : ret;
    }

    // 7. 整数反转
    public int reverse(int x) {
        boolean flag = false;
        if (x < 0) {
            x = -x;
            flag = true;
        }
        int ans = 0;
        while (x > 0) {
            int tmp = x % 10;
            if (ans > 214748364 || (ans == 214748364 && tmp > 7)) {
                return 0;
            }
            ans *= 10;
            ans += tmp;
            x /= 10;
        }
        if (flag) {
            return -ans;
        }
        return ans;
    }
}
