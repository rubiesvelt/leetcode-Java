package solution;

public class DetectSquares {

    /*
     * ["DetectSquares","add","add","add","count","add","add","add","count","add","add","add","count","add","add","add",""""***count***"""","add","add","add","count","add","add","add","count","add","add","add","count","add","add","add","count","add","add","add","count","add","add","add","count","add","add","add","count"]
     * [[],[[419,351]],[[798,351]],[[798,730]],[[419,730]],[[998,1]],[[0,999]],[[998,999]],[[0,1]],[[226,561]],[[269,561]],[[226,604]],[[269,604]],[[200,274]],[[200,793]],[[719,793]],[[719,274]],[[995,99]],[[146,948]],[[146,99]],[[995,948]],[[420,16]],[[962,558]],[[420,558]],[[962,16]],[[217,833]],[[945,105]],[[217,105]],[[945,833]],[[26,977]],[[26,7]],[[996,7]],[[996,977]],[[96,38]],[[96,483]],[[541,483]],[[541,38]],[[38,924]],[[961,1]],[[961,924]],[[38,1]],[[438,609]],[[818,609]],[[818,229]],[[438,229]]]
     */
    public static void main(String[] args) {
        DetectSquares detectSquares = new DetectSquares();
        // [[200,274]],[[200,793]],[[719,793]],[[719,274]]
        int[] point1 = {1,1};
        int[] point2 = {200,793};
        int[] point3 = {719,793};
        int[] point4 = {11, 10};
        detectSquares.add(point1);
        // detectSquares.add(point2);
        // detectSquares.add(point3);
        // detectSquares.add(point1);
        // detectSquares.add(point2);
        detectSquares.count(new int[]{1,1});
    }
    int[][] board;

    public DetectSquares() {
        this.board = new int[1002][1002];
    }

    public void add(int[] point) {
        board[point[0]][point[1]]++;
    }

    public int count(int[] p1) {
        int x = p1[0];
        int y = p1[1];
        int n1 = 1;
        int ans = 0;
        for (int i = 0; i < 1002; i++) {
            if (y == i) {
                continue;
            }
            int n2;
            if (board[x][i] > 0) {
                n2 = board[x][i];
                int[] p2 = new int[]{x, i};
                int d = Math.abs(y - i);

                int[] p3_1 = new int[]{x + d, y};
                int[] p3_2 = new int[]{x - d, y};

                if (x + d < 1002) {
                    int n3 = board[x + d][y];
                    if (n3 > 0) {
                        int[] p4 = findNextPoint(p1, p2, p3_1);
                        int n4 = board[p4[0]][p4[1]];
                        ans += n1 * n2 * n3 * n4;
                    }
                }
                if (x - d >= 0) {
                    int n3 = board[x - d][y];
                    if (n3 > 0) {
                        int[] p4 = findNextPoint(p1, p2, p3_2);
                        int n4 = board[p4[0]][p4[1]];
                        ans += n1 * n2 * n3 * n4;
                    }
                }
            }
        }
        return ans;
    }

    public int[] findNextPoint(int[] point1, int[] point2, int[] point3) {
        // [0,0][0,1] -> [1,0]
        if (point1[0] == point2[0]) {
            return new int[]{point3[0], point3[1] + point2[1] - point1[1]};
        } else {
            return new int[]{point3[0] + point2[0] - point1[0], point3[1]};
        }
    }
}
