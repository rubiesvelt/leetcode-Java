import java.util.Arrays;

public class Main3 {
    public static void main(String[] args) {
        int[] nums = new int[]{1, 2, 3, 4, 5, 10};
        int[][] requests = new int[][]{{1, 3}, {1, 1}, {0, 2}};
        maxChunksToSorted(nums);
        maxSumRangeQuery(nums, requests);
    }

    public static int maxChunksToSorted(int[] arr) {
        int res = 1;
        int max = -1;
        int min = 100;
        boolean flag = false;
        for (int i : arr) {
            if (i > max) {
                res++;
                max = i;
                flag = true;
            }
            if (i < min) {
                if (flag){
                    flag = false;
                    res--;
                    min = i;
                }
            }
        }
        return res;
    }

    public static int maxSumRangeQuery(int[] nums, int[][] requests) {
        final int MODULO = 1000000007;
        int numsSize = nums.length;
        int requestsSize = requests.length;
        int[] hashMap = new int[numsSize];
        // [0,1], [1,3]
        // [1,2,1,1,0]
        for (int i = 0; i < requestsSize; i++) {
            int start = requests[i][0];
            int end = requests[i][1];
            hashMap[start]++;
            if (end + 1 < numsSize) {
                hashMap[end + 1]--;
            }
        }
        for (int i = 1; i < numsSize; i++) {
            hashMap[i] = hashMap[i] + hashMap[i - 1];
        }
        Arrays.sort(nums);
        Arrays.sort(hashMap);
        long res = 0;
        for (int i = numsSize - 1; i > -1 && hashMap[i] > 0; i--) {
            res += (long) nums[i] * hashMap[i];
        }
        return (int) (res % MODULO);
    }
}