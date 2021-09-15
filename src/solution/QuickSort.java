package solution;

public class QuickSort {

    public void quickSort(int[] num) {
        sort(num, 0, num.length - 1);
    }

    public void sort(int[] num, int left, int right) {
        int i, j, index;
        if (left > right) {
            return;
        }
        i = left;
        j = right;
        index = num[i];
        while (i < j) {
            while (i < j && num[j] >= index) {
                j--;
            }
            if (i < j) {
                num[i++] = num[j];
            }
            while (i < j && num[i] < index) {
                i++;
            }
            if (i < j) {
                num[j--] = num[i];
            }
        }
        num[i] = index;  // 此处，原来的 num[i] 已经被移到别处，或者 num[i] 就没变
        sort(num, left, i - 1);
        sort(num, i + 1, right);
    }
}
