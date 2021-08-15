public class Stringling {

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
