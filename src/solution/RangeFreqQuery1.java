package solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RangeFreqQuery1 {

    // 数字 -> 频率
    public List<Map<Integer, Integer>> mapList;

    public RangeFreqQuery1(int[] arr) {
        int n = arr.length;
        mapList = new ArrayList<>();
        Map<Integer, Integer> curMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int fre;
            if (curMap.containsKey(arr[i])) {
                fre = curMap.get(arr[i]) + 1;
            } else {
                fre = 1;
            }
            curMap.put(arr[i], fre);
            Map<Integer, Integer> newMap = new HashMap<>(curMap);
            mapList.add(i, newMap);
        }
    }

    public int query(int left, int right, int value) {
        Map<Integer, Integer> leftMap;
        if (left == 0) {
            leftMap = new HashMap<>();
        } else {
            leftMap = mapList.get(left - 1);
        }
        Map<Integer, Integer> rightMap = mapList.get(right);
        int lNum = leftMap.getOrDefault(value, 0);
        int rNum = rightMap.getOrDefault(value, 0);
        return rNum - lNum;
    }
}
