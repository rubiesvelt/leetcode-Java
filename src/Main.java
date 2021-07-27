import beans.TreeNode;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        char a = 'a';
        int i = 96;
        TreeNode tn4 = new TreeNode(4);
        TreeNode tn3 = new TreeNode(3);
        TreeNode tn2 = new TreeNode(2, null, tn4);
        TreeNode tn1 = new TreeNode(1, tn2, tn3);

        int[][] matrix = {{1, 1, 0}, {1, 0, 1}, {0, 0, 1}};
        int[][] matrix1 = {{1, 0, 0}, {0, 0, 1}, {1, 1, 0}};
        int[] dist = {1, 10, 4, 4, 2, 7};
        int[] speed = {6};
    }
}
