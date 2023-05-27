public class BinarySearch {

    /*
     * 连续的一段
     * 前一段可以，后一段不行，则可使用二分
     *
     * 注意二分边界：
     * - 我们需要的是什么？
     * - 我们需要的东西最大最小取值到哪里？
     */

    /*
     * 典型二分
     * num sort from smallest to largest
     * lower_bound —— find the first element that bigger or equal than (not less than) t
     */
    public static int lower_bound(int[] nums, int l, int r, int t) {
        while (l < r) {
            int mid = (l + r) >> 1;
            if (nums[mid] >= t) {  // although there is a match that nums[mid] == t, we still try to find the nearest
                r = mid;
            } else {
                l = mid + 1;  // if we use l = mid, may cause endless loop —— Since "mid = (l + r) >> 1" is rounded down，it will cause mid = l when r is 1 bigger than l, at this time if we use l = mid would cause endless loop
            }
        }
        return l;  // 最终状态，l 等于 r，并且停留位置是 "r侧第一个满足条件的"
    }

    /*
     * 2064. 分配给商店的最多商品的最小值
     * n 个商店，quantities[] 记录每一种产品的数量，每个商店只能分 一种 产品，要求 分到产品最多的商店 产品数最少 求该数
     *
     * 从 base 向上二分查找，而不是扩大商店动态分配 —— 不要将问题复杂化
     *
     * e.g.
     * n = 6, quantities = [11, 6]
     * -> 3 —— (6 个商店，分配方案 [2, 3, 3, 3, 3, 3])
     *
     */
    public int minimizedMaximum(int n, int[] quantities) {
        int sum = 0;
        int max = 0;
        int len = quantities.length;
        for (int q : quantities) {
            sum += q;
            max = Math.max(max, q);
        }
        int base = sum / n;
        if (sum % n > 0) base++;
        // 使用二分
        int l = base;
        int r = max;
        while (l < r) {
            int m = (l + r) >> 1;
            if (canBaseAfford(m, len, n, quantities)) {
                r = m;
            } else {
                l = m + 1;
            }
        }
        return l;
    }

    boolean canBaseAfford(int base, int left, int n, int[] quantities) {
        int sum = 0;
        // left 剪枝
        for (int q : quantities) {
            int cnt = q / base;
            if (q % base > 0) cnt++;
            sum += cnt;
            if (sum > n - left) return false;
            left--;
        }
        return true;
    }

    /**
     * 可信 1
     * <br/>二分
     * <br/>现给出展厅列表，下标0，1...代表0号，1号...展厅，数组的值为展厅中来参观的人数，总参观人数为 nums[0] + nums[1] + ... + nums[n - 1]
     * <br/>但本次参观最多容纳人数为cnt，如果总共来参观的人数大于cnt，我们需要对人数进行限流，给每个展厅进入人数设置一个上限
     * <br/>每个展厅最多容纳上限个人，总人数不可超过cnt
     * <p/>
     * e.g.
     * <br/>{1, 4, 2, 5, 5, 1, 6}, 13 -> 2
     * <br/>{1, 1, 1, 1, 1, 1, 25}, 13 -> 7
     * <p/>
     *
     * @param nums 展厅列表数组
     * @param cnt  本次参观最多容纳人数为cnt
     * @return 展厅进入人数上限的最大值
     */
    public int manageTourists(int[] nums, int cnt) {
        long sum = 0;
        for (int t : nums) {
            sum += t;
        }
        if (sum <= cnt) {
            return -1;
        }
        // 限制每个展厅的人数 limit，使 "实际到达" 的总人数不超过cnt
        int l = 1;  // l取最小，可能取到l
        int r = cnt + 1;  // 最大能取到r ; 我们需要的结果是 r - 1; r - 1 也可以取到 cnt; 所以 r 取到 cnt + 1
        while (l < r) {
            int m = (l + r) >> 1;
            if (isCapable2(nums, m, cnt)) {  // 限定每个展厅人数为 m 时候，可以将总人数限制在 cnt 以内，故尝试将m调大
                l = m + 1;
            } else {
                r = m;
            }
        }
        // 从小到大，从 能够限制 到 不能限制，l是第一个 不能限制 的；我们求的是最大的 能够限制 的
        return l - 1;
    }

    public boolean isCapable2(int[] nums, long limit, long cnt) {
        long sum = 0;
        for (int n : nums) {
            long t = Math.min(n, limit);
            sum += t;
            if (sum > cnt) {
                return false;  // 按这个限制，进入人数就超过cnt了，不合适
            }
        }
        return true;  // 完全OK
    }

    // 74. 搜索二维矩阵
    public boolean searchMatrix(int[][] matrix, int target) {
        int rowIndex = binarySearchFirstColumn(matrix, target);
        if (rowIndex < 0) {
            return false;
        }
        return binarySearchRow(matrix[rowIndex], target);
    }

    // 二分查找到比target小的数字
    public int binarySearchFirstColumn(int[][] matrix, int target) {
        int l = -1, r = matrix.length - 1;
        while (l < r) {
            int m = (r - l + 1) / 2 + l;
            if (matrix[m][0] <= target) {
                l = m;
            } else {
                r = m - 1;
            }
        }
        return l;
    }

    // 二分查找，查找存不存在
    public boolean binarySearchRow(int[] row, int target) {
        int l = 0, r = row.length - 1;
        while (l <= r) {
            int m = (r - l) / 2 + l;
            if (row[m] == target) {  // 找到一个点，可以明确是否是想要的值
                return true;
            } else if (row[m] > target) {
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return false;
    }

    // 278. First Bad Version
    /*
     * 区间二分
     * 1. 写 l < r，条件 r = m, l = m + 1
     * 2. 考虑会不会真正结果在l左或r右
     */
    // g b b
    public int firstBadVersion(int n) {
        int l = 1;
        int r = n;
        while (l < r) {
            int m = r + l >> 1;
            if (isBadVersion(m)) r = m;
            else l = m + 1;
        }
        return l;  // 此处，return l 或 r 都可以，因为 l == r
    }

    public boolean isBadVersion(int version) {
        short s = 1;
        s = (short) (s + 1);
        byte b = 10;
        return true;
    }

    /*
     * 1898. 可移除字符的最大数目
     * 区间二分
     *
     * s = "abcbddddd", p = "abcd", removable = [3,1,0]
     * 移除哪几个后，p仍是s子序列
     *
     */
    public int maximumRemovals(String s, String p, int[] removable) {
        char[] pc = p.toCharArray();
        int l = 0;
        // 特别注意，递归结果，（第一个坏的地方），可能超过数组右边界，所以这里设 r = removable.length;
        int r = removable.length;
        while (l < r) {
            int m = (l + r) / 2;
            char[] sc = s.toCharArray();
            for (int i = 0; i <= m; i++) {
                sc[removable[i]] = '.';
            }
            // 可行，l = m + 1
            if (isSubSeq(sc, pc)) {
                l = m + 1;
            } else r = m;
        }

        return l;
    }

    public boolean isSubSeq(char[] sc, char[] pc) {
        int j = 0;
        for (int i = 0; i < sc.length; i++) {
            // 优化1
            if (sc.length - i < pc.length - j) return false;
            if (sc[i] == pc[j]) {
                j++;
                if (j == pc.length) return true;
            }
        }
        return false;
    }

    // 1011. 在 D 天内送达包裹的能力
    // 区间二分，寻找最接近的；找到一个点后，并不能知道是不是结果，需要二分逼近
    // 二分查找 与 区间二分
    public int shipWithinDays(int[] weights, int D) {
        int max = 0;
        int sum = 0;
        for (int i : weights) {
            sum += i;
            max = Math.max(i, max);
        }
        // 船的载重肯定在 平均重量 和 最大重量 之间
        int l = max;
        int r = sum;
        while (l < r) {
            int m = (l + r) >> 1;  // 二分查找，先确定 m
            if (isCapable(weights, m, D)) {
                // 考虑刚好命中结果的场景，应当走这里
                // 我们目的是找到符合中的最小的，所以符合的话 r = m，r 永远符合要求
                r = m;
            } else {
                l = m + 1;
            }
        }
        return r;
    }

    // 如果船载重为capacity，能否在D天内装完
    public boolean isCapable(int[] weights, int capacity, int D) {
        int sum = 0;
        int round = 1;
        for (int i : weights) {
            // 写在上面的话，如果sum + 最后一个 i > capacity 了，就会失误的判断为可行
            if (sum + i > capacity) {
                round++;
                sum = i;
                if (round > D) {  // 已经开始了下一轮
                    return false;
                }
                continue;
            }
            sum += i;
        }
        return true;
    }
}
