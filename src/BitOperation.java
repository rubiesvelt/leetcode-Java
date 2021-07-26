public class BitOperation {

    // 190. 颠倒二进制位
    public static int reverseBits(int n) {
        int rev = 0;
        for (int i = 0; i < 32 && n != 0; ++i) {
            rev |= (n & 1) << (31 - i);  // |是按位或，a |= b 的意思是 a = a | b
            n >>>= 1;  // >>>运算符所作的是无符号的位移处理，它不会将所处理的值的最高位视为正负符号，所以作位移处理时，会直接在空出的高位填入0
        }
        return rev;
    }

    // 338. 比特位计数
    public int[] countBits2(int num) {
        int[] res = new int[num + 1];
        for (int i = 0; i <= num; i++) {
            if (i % 2 == 1) {
                res[i] = res[i - 1] + 1;
            } else {
                res[i] = res[i / 2];
            }
        }
        return res;
    }

    // 461. 汉明距离
    // 位运算
    // 两个整数之间的汉明距离指的是这两个数字对应二进制位不同的位置的数目
    public int hammingDistance(int x, int y) {
        int n = x ^ y;  // 按位异或
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            if ((n & 1) == 1) {
                ans++;
            }
            n >>= 1;
        }
        return ans;
    }

    // 191
    public static int hammingWeight(long n) {
        int count = 0;
        for (int i = 0; i < 32; ++i) {
            count += n & 1;
            n >>= 1;
        }
        return count;
    }

    // 面试题 16.07
    public int maximum(int a, int b) {
        long x = (long) a - (long) b;
        int k = (int) (x >> 63);

        return (1 + k) * a - b * k;
    }

    /*
     * 最美子字符串的数目
     * "ccjjc"
     *
     * c -> 3
     * j -> 2
     */
    public long wonderfulSubstrings(String word) {
        /*
         * int state 作为前缀和，int为32位二进制，由于只有 a - j，我们只需要关注其 0-10 位即可
         * 1表示对应位出现了奇数次，0表示偶数次
         * e.g.
         * word = "cbba"
         * state =
         * 00000 00100 ->
         * 00000 00110 ->
         * 00000 00100 ->
         * 00000 00101
         * ...
         *
         * 当前状态为state，寻找与当前状态差1位的之前状态
         */
        int state = 0;
        // 用数组做 HashMap，下标为key（已经出现的状态），值为value（状态出现的次数）
        int[] cnt = new int[1 << 10];
        cnt[0] = 1;
        int ans = 0;
        for (char c : word.toCharArray()) {
            state ^= 1 << (c - 'a');
            // 当前状态为state，与当前状态奇偶性 "一位不差" 的，加上
            ans += cnt[state];
            // 当前状态为state，寻找与 当前状态 "差1位"的 之前状态，加上
            for (int i = 0; i < 10; i++) {
                ans += cnt[state ^ (1 << i)];
            }
            // 记录当前状态
            cnt[state]++;
        }
        return ans;
    }

    // 477. 汉明距离总和
    public static int totalHammingDistance(int[] nums) {
        int ans = 0;
        for (int i = 31; i >= 0; i--) {  // 从低位到高位也可
            int s0 = 0;
            int s1 = 0;
            for (int n : nums) {
                if (((n >> i) & 1) == 1) {  // 右移>>，左移<<，无符号右移>>>
                    s1++;
                } else {
                    s0++;
                }
            }
            ans += s0 * s1;
        }
        return ans;
    }

}
