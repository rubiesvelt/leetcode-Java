public class Utils {

    public static class Pair<T1, T2> {
        public T1 fst;

        public T2 snd;

        public Pair(T1 fst, T2 snd) {
            this.fst = fst;
            this.snd = snd;
        }
    }

    // 二分 找到第一个 小于等于t 的元素，返回下标
    public static int lowerBound(int[] arr, int left, int right, int t) {
        while (left < right) {
            int mid = (right - left) / 2 + left;
            if (arr[mid] >= t) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        return left;
    }

    // 二分 找到第一个大于t的下标，就是 C++ 中的upper_bound函数
    public static int upperBound(int[] arr, int left, int right, int t) {
        while (left < right) {
            int mid = (right - left) / 2 + left;
            if (arr[mid] > t) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
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
