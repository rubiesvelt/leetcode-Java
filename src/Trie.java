public class Trie {

    // 421. 数组中两个数的最大异或值
    // Trie树匹配；位运算
    public static class TrieNode {
        public TrieNode[] next = new TrieNode[2];
    }

    public TrieNode root421 = new TrieNode();

    /*
     * 构建 1 + 32 层的前缀树
     * 每一条路径都是 1 + 32
     */
    public void add421(int n) {
        TrieNode r = root421;
        for (int i = 31; i >= 0; i--) {
            int t = (n >> i) & 1;
            if (r.next[t] == null) {
                r.next[t] = new TrieNode();
            }
            r = r.next[t];
        }
    }

    /*
     * 获取树中与其异或结果最大的元素
     * 从 高位 往 低位 匹配，每一位尽量匹配相反，最后匹配到一个完整的
     */
    public int getValue421(int n) {
        TrieNode r = root421;
        int ans = 0;
        for (int i = 31; i >= 0; i--) {
            int a = (n >> i) & 1;  // a是n的第i位，重要！！！**求数字n的 从小到大 第i位**
            int b = 1 - a;
            if (r.next[b] != null) {  // 匹配上了，这一位是n的相反位，就他了
                ans |= (b << i);  // 将ans的从右到左第i位置为b
                // ans = ans * 2 + 1;  // 如果是这样就是获取树中最大异或结果
                r = r.next[b];
            } else {  // 没匹配上，这一位和n的一样
                ans |= (a << i);  // 将ans的从右到左第i位置为a
                // ans = ans * 2;
                r = r.next[a];
            }
        }
        return ans;
    }

    public int findMaximumXOR(int[] nums) {
        int ans = 0;
        for (int i : nums) {
            add421(i);
            int j = getValue421(i);
            ans = Math.max(ans, i ^ j);
        }
        return ans;
    }
}
