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

        int[][] matrix = {{0, 1, 250}, {0, 3, 10}, {1, 2, 25}, {1, 3, 80}, {2, 3, 90}};
        int[] dist = {1, 10, 4, 4, 2, 7};
        int[] speed = {6};
    }
}
