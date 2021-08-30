import java.util.Arrays;
import java.util.Comparator;

public class Stringling {

    // 1985. 找出数组中的第 K 大整数
    // 找出数组中的第 K 大整数，数字以字符串形式给出
    // 字符串表示数字 的比较排序
    public String kthLargestNumber(String[] nums, int k) {
        Arrays.sort(nums, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int l1 = o1.length();
                int l2 = o2.length();
                if (l1 > l2) return 1;
                if (l1 < l2) return -1;
                for (int i = 0; i < l1; i++) {
                    char c1 = o1.charAt(i);
                    char c2 = o2.charAt(i);
                    if (c1 > c2) return 1;
                    if (c1 < c2) return -1;
                }
                return 0;
            }
        });
        return nums[nums.length - k];
    }

    // 1963. 使字符串平衡的最小交换次数
    // [][][]
    // [[][[]]]
    // ]]][[[ -> []][[] -> [[][]]
    public int minSwaps(String s) {
        char[] cs = s.toCharArray();  // 涉及String的位操作，先转为char[]
        int n = s.length();
        int swap = 0;
        int rIndex = s.length() - 1;
        // 从左到右找未配对的']'
        int lNum = 0;
        for (int i = 0; i < n; i++) {
            if (cs[i] == '[') {
                lNum++;
                continue;
            }
            if (lNum == 0) {  // 找到未配对的 ']'
                int t = findNextRight(cs, rIndex);  // 与从右到左找到第一个 '[' 交换
                cs[t] = ']';
                cs[i] = '[';
                lNum++;
                swap++;
                continue;
            }
            lNum--;
        }
        return swap;
    }

    public int findNextRight(char[] cs, int rIndex) {
        while (rIndex > 0) {
            if (cs[rIndex] == '[') {
                rIndex--;
                return rIndex + 1;
            }
            rIndex--;
        }
        return 0;
    }
}
