import java.util.HashMap;
import java.util.Map;

public class Hash {

    // 5803. 最长公共子路径
    // 二分 + 区间哈希
    long[] h = new long[100010];
    long[] p = new long[100010];
    int[][] paths;
    Map<Long, Integer> cnt = new HashMap<>();
    Map<Long, Integer> inner = new HashMap<>();

    public int longestCommonSubpath(int n, int[][] paths) {
        this.paths = paths;
        int l = 0;
        int r = 100010;
        for (int i = 0; i < paths.length; i++) {
            r = Math.min(r, paths[i].length);
        }
        while (l < r) {
            int mid = (l + r + 1) >> 1;
            if (check(mid))
                l = mid;
            else
                r = mid - 1;
        }
        return l;
    }

    private boolean check(int mid) {
        cnt.clear();  // 哈希值 -> 出现次数
        inner.clear();  // 哈希值 -> path下标
        p[0] = 1;
        for (int j = 0; j < paths.length; j++) {
            // 每一个path
            int n = paths[j].length;
            for (int i = 1; i <= n; i++) {
                p[i] = p[i - 1] * 133331;
                h[i] = h[i - 1] * 133331 + paths[j][i - 1];
            }
            for (int i = mid; i <= n; i++) {
                long val = get(i - mid + 1, i);  // [x, i]的哈希值
                if (!inner.containsKey(val) || inner.get(val) != j) {
                    inner.put(val, j);
                    int t = cnt.getOrDefault(val, 0) + 1;
                    if (t == paths.length) return true;
                    cnt.put(val, t);
                }
            }
        }
        return false;
    }

    public long get(int l, int r) {
        return h[r] - h[l - 1] * p[r - l + 1];
    }
}
