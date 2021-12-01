package solution;

public class Robot {

    int actWidth;
    int actHeight;
    int round;
    String dir = "East";
    int[] cur = new int[]{0,0};
    boolean moved = false;

    public Robot(int width, int height) {
        this.actWidth = width - 1;
        this.actHeight = height - 1;
        this.round = 2 * actWidth + 2 * actHeight;
    }

    public void move(int num) {
        int dis = num % round;
        if (dis == 0 && !moved) {
            dir = "South";
        }
        moved = true;
        while (dis > 0) {
            if (dir == "East") {
                if (cur[0] == actWidth) {
                    dir = "North";
                    cur[1]++;
                } else {
                    cur[0]++;
                }
            }
            else if (dir == "North") {
                if (cur[1] == actHeight) {
                    dir = "West";
                    cur[0]--;
                } else {
                    cur[1]++;
                }
            }
            else if (dir == "West") {
                if (cur[0] == 0) {
                    dir = "South";
                    cur[1]--;
                } else {
                    cur[0]--;
                }
            }
            else if (dir == "South") {
                if (cur[1] == 0) {
                    dir = "East";
                    cur[0]++;
                } else {
                    cur[1]--;
                }
            }
            dis--;
        }
    }

    public int[] getPos() {
        return cur;
    }

    public String getDir() {
        if (!moved) return "East";
        return dir;
    }

    public static void main(String[] args) {
        Robot robot = new Robot(20,14);
        robot.move(32);
        robot.move(18);
        robot.move(18);
        robot.move(1);
        robot.move(4);
    }
}
