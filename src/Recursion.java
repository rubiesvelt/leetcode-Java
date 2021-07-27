import java.util.HashMap;
import java.util.Map;

public class Recursion {

    // 395. 至少有 K 个重复字符的最长子串
    // 分治
    public int longestSubstring(String s, int k) {
        return dfs(s, k);
    }

    public int dfs(String s, int k) {
        if (k > s.length()) {
            return 0;
        }
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            Character a = s.charAt(i);
            if (!map.containsKey(a)) {
                map.put(a, 1);
            } else {
                Integer integer = map.get(a);
                map.put(a, integer + 1);
            }
        }
        int ans = -1;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (map.get(s.charAt(i)) >= k) {
                sb.append(s.charAt(i));
                if (i != s.length() - 1) {
                    continue;
                } else if (ans == -1) {
                    continue;
                }
            }
            // 遇见小于k的，split点
            // 全局的split点一定要分开，后续递归的split点一定也要分开
            int subLen = dfs(sb.toString(), k);
            ans = Math.max(ans, subLen);
            sb.delete(0, s.length());  // 清除StringBuilder中全部元素
        }
        if (ans == -1) {
            return s.length();
        }
        return ans;
    }

    public int longestSubstring1(String s, int k) {
        int n = s.length();
        return dfsSubstring(s, 0, n - 1, k);
    }

    // 用左右端点 l 和 r 做递归，上面的直接用字符串（或子串）s做递归
    public int dfsSubstring(String s, int l, int r, int k) {
        int[] cnt = new int[26];  // 26个英文字母，数组初始元素都为0；用数组实现map
        for (int i = l; i <= r; i++) {
            cnt[s.charAt(i) - 'a']++;
        }

        char split = 0;  // 第一个断点字母
        for (int i = 0; i < 26; i++) {
            if (cnt[i] > 0 && cnt[i] < k) {  // 找到整个串中的断点
                split = (char) (i + 'a');
                break;
            }
        }
        if (split == 0) {  // 递归边界，数组中没有少于k个的
            return r - l + 1;
        }

        int i = l;
        int ret = 0;
        while (i <= r) {
            while (i <= r && s.charAt(i) == split) {
                i++;
            }
            if (i > r) {
                break;
            }
            int start = i;
            while (i <= r && s.charAt(i) != split) {
                i++;
            }

            int length = dfsSubstring(s, start, i - 1, k);
            ret = Math.max(ret, length);
        }
        return ret;
    }

}
