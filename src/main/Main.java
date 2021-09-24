package main;

import beans.TreeNode;

import java.util.*;

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

        int[][] matrix = {{1, 0, 0}, {0, 1, 1}, {0, 1, 1}};
        int[][] matrix1 = {{1, 0, 0}, {0, 0, 1}, {1, 1, 0}};
        int[] dist = {2, 4, 6, 4};
        int[] diff = {3, 3, 3};
        int[] speed = {5, 1, 5, 5, 1, 5, 3, 4, 5, 1, 4};
        char[][] matrix2 = {{'.', '.', 'W', '.', 'B', 'W', 'W', 'B'}, {'B', 'W', '.', 'W', '.', 'W', 'B', 'B'}, {'.', 'W', 'B', 'W', 'W', '.', 'W', 'W'}, {'W', 'W', '.', 'W', '.', '.', 'B', 'B'}, {'B', 'W', 'B', 'B', 'W', 'W', 'B', '.'}, {'W', '.', 'W', '.', '.', 'B', 'W', 'W'}, {'B', '.', 'B', 'B', '.', '.', 'B', 'B'}, {'.', 'W', '.', 'W', '.', 'W', '.', 'W'}};

        return;
    }
}
