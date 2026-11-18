package com.zhonglian.server.common.utils;

import java.util.List;

public class RquickSort {
    public int partition(List<Integer> list, int low, int high) {
        int small = low + 1;

        try {
            int pivot = Random.nextInt(high - low + 1 > 0 ? high - low + 1 : 1) + low;
            int temp = list.get(low);
            list.set(low, list.get(pivot));
            list.set(pivot, temp);
            pivot = low;

            for (int i = small; i <= high; i++) {
                if (list.get(i) < list.get(pivot)) {
                    temp = list.get(small);
                    list.set(small, list.get(i));
                    list.set(i, temp);
                    small++;
                }
            }

            temp = list.get(pivot);
            list.set(pivot, list.get(small - 1));
            list.set(small - 1, temp);
        } catch (Exception var8) {
            System.out.println(high - low);
        }

        return small - 1;
    }

    public void Rquicksort(List<Integer> list, int low, int high) {
        int pivot = this.partition(list, low, high);
        if (low < high) {
            this.Rquicksort(list, low, pivot);
            this.Rquicksort(list, pivot + 1, high);
        }
    }
}
