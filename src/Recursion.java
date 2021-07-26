import java.util.HashMap;
import java.util.Map;

public class Recursion {

    // 395
    public int longestSubstring(String s, int k) {
        int res = recursive(s, k);
        return res;
    }

    public int recursive(String s, int k) {
        if (k > s.length()) {
            return 0;
        }
        Map<Character, Integer> mp = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            Character a = s.charAt(i);
            if (!mp.containsKey(a)) {
                mp.put(a, 1);
            } else {
                Integer integer = mp.get(a);
                mp.put(a, integer + 1);
            }
        }
        int len = -1;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (mp.get(s.charAt(i)) >= k) {
                sb.append(s.charAt(i));
                if (i != s.length() - 1) {
                    continue;
                } else if (len == -1) {
                    continue;
                }
            }
            int subLen = recursive(sb.toString(), k);
            len = Math.max(len, subLen);
            sb.delete(0, s.length());  // 清除StringBuilder中全部元素
        }
        if (len == -1) {
            return s.length();
        }
        return len;
    }

    public int longestSubstring1(String s, int k) {
        int n = s.length();
        return dfsSubstring(s, 0, n - 1, k);
    }

    public int dfsSubstring(String s, int l, int r, int k) {
        int[] cnt = new int[26];  // 26个英文字母，数组初始元素都为0；用数组实现map
        for (int i = l; i <= r; i++) {
            cnt[s.charAt(i) - 'a']++;
        }

        char split = 0;
        for (int i = 0; i < 26; i++) {
            if (cnt[i] > 0 && cnt[i] < k) {
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
