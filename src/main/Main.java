package main;

import beans.TreeNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        List<Integer> list = new ArrayList<>();
        int t = list.hashCode();

        Main main = new Main();
        char a = 'a';
        int i = 96;
        TreeNode tn5 = new TreeNode(5);
        TreeNode tn4 = new TreeNode(4);
        TreeNode tn3 = new TreeNode(3, tn5, null);
        TreeNode tn2 = new TreeNode(2, tn4, null);
        TreeNode tn1 = new TreeNode(1, tn2, tn3);

        int[][] matrix = {{1, 2}};
        int[][] matrix1 = {{1, 0, 0}, {0, 0, 1}, {1, 1, 0}};
        int[] dist = {-1, 2, 0};
        int[] diff = {3, 3, 3};
        int[] speed = {5, 1, 5, 5, 1, 5, 3, 4, 5, 1, 4};
        char[][] matrix2 = {{'.', '.', 'W', '.', 'B', 'W', 'W', 'B'}, {'B', 'W', '.', 'W', '.', 'W', 'B', 'B'}, {'.', 'W', 'B', 'W', 'W', '.', 'W', 'W'}, {'W', 'W', '.', 'W', '.', '.', 'B', 'B'}, {'B', 'W', 'B', 'B', 'W', 'W', 'B', '.'}, {'W', '.', 'W', '.', '.', 'B', 'W', 'W'}, {'B', '.', 'B', 'B', '.', '.', 'B', 'B'}, {'.', 'W', '.', 'W', '.', 'W', '.', 'W'}};
        return;
    }

    /*
     * 638. 大礼包
     * <p>
     * [0,0,0]
     * [[1,1,0,4],[2,2,1,9]]
     * [1,1,1]
     */
    public int shoppingOffers(List<Integer> price, List<List<Integer>> special, List<Integer> needs) {
        int n = price.size();
        List<List<Integer>> goodSpecial = new ArrayList<>();
        // 筛选 能够优惠 且 可以选择 的大礼包
        for (List<Integer> sp : special) {
            int total = 0;  // 大礼包中元素的原价
            for (int i = 0; i < n; i++) {
                if (sp.get(i) > needs.get(i)) {
                    total = 0x3f3f3f3f;
                    break;
                }
                total += sp.get(i) * price.get(i);
            }
            if (total <= sp.get(n)) {
                continue;
            }
            goodSpecial.add(sp);
        }

        dfs(0, needs, goodSpecial, price, n);
        return ans638;
    }

    int ans638 = 0x3f3f3f3f;

    public void dfs(int total, List<Integer> needs, List<List<Integer>> special, List<Integer> price, int n) {
        boolean hasSpe = false;
        for (List<Integer> spe : special) {
            if (goodSpe(spe, needs)) {
                hasSpe = true;
                total += spe.get(n);
                useSpe(spe, needs);
                dfs(total, needs, special, price, n);
                total -= spe.get(n);
                releaseSpe(spe, needs);
            }
        }
        if (!hasSpe) {
            for (int i = 0; i < n; i++) {
                total += needs.get(i) * price.get(i);
            }
            ans638 = Math.min(ans638, total);
        }
    }

    public boolean goodSpe(List<Integer> spe, List<Integer> needs) {
        for (int i = 0; i < needs.size(); i++) {
            if (spe.get(i) > needs.get(i)) {
                return false;
            }
        }
        return true;
    }

    public void useSpe(List<Integer> spe, List<Integer> needs) {
        for (int i = 0; i < needs.size(); i++) {
            int t = needs.get(i) - spe.get(i);
            needs.remove(i);
            needs.add(i, t);
        }
    }

    public void releaseSpe(List<Integer> spe, List<Integer> needs) {
        for (int i = 0; i < needs.size(); i++) {
            int t = needs.get(i) + spe.get(i);
            needs.remove(i);
            needs.add(i, t);
        }
    }
}
