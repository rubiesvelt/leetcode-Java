public class BitOperation {

    /*
     * 2044. 统计按位或能得到最大值的子集数目
     * 给出数组 nums[] 求其 按位或 能得到最大值 的子集 的数目
     * e.g.
     * nums = [1,2,3,5]
     *
     * 按位或最大值 7
     * 如下子集 进行按位或 可得到
     * [2,5]
     * [3,5]
     * [1,2,5]
     * [1,3,5]
     * [2,3,5]
     * [1,2,3,5]
     * 共 6 个
     * -> 6
     */
    public int countMaxOrSubsets1(int[] nums) {
        int n = nums.length;
        int max = 0;
        for (int num : nums) {
            max |= num;
        }
        int ans = 0;
        // 使用 n位 代表所有选项；1 << n 即 2 ^ n；共有 2 ^ n 个组合 (从 0 到 (2 ^ n) - 1)
        for (int i = 0; i < (1 << n); i++) {  // 使用 状态压缩 获取数组元素的全组合
            int res = 0;
            for (int j = 0; j < n; j++) {
                if (((i >> j) & 1) == 1) {
                    res |= nums[j];
                }
            }
            if (res == max) {
                ans++;
            }
        }
        return ans;
    }

    /*
     * dfs遍历所有子集
     */
    public int countMaxOrSubsets(int[] nums) {
        int n = nums.length;
        int max = 0;  // 按位或的最大值
        for (int num : nums) {
            max |= num;
        }
        // 从 nums[] 中选 i 个
        for (int i = 1; i < n; i++) {
            dfs(nums, -1, 0, 0, i, max);
        }
        return ans;
    }

    int ans = 1;

    public void dfs(int[] nums, int index, int current, int sum, int total, int max) {
        /*
         * 可变
         * index 当前元素下标
         * current 当前 总和
         * sum 当前元素总数
         *
         * 不可变
         * total 要求元素总数
         * max 目标和
         */
        if (sum == total) {
            if (max == current) {
                ans++;
            }
            return;
        }
        for (int i = index + 1; i < nums.length; i++) {  // i为下一个目标
            int t = current | nums[i];
            dfs(nums, i, t, sum + 1, total, max);
        }
    }

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
