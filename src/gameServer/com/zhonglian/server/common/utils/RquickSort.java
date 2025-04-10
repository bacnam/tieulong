package com.zhonglian.server.common.utils;

import java.util.List;

public class RquickSort {
public int partition(List<Integer> list, int low, int high) {
int small = low + 1;
try {
int pivot = Random.nextInt((high - low + 1 > 0) ? (high - low + 1) : 1) + low;
int temp = ((Integer)list.get(low)).intValue();
list.set(low, list.get(pivot));
list.set(pivot, Integer.valueOf(temp));
pivot = low;
for (int i = small; i <= high; i++) {
if (((Integer)list.get(i)).intValue() < ((Integer)list.get(pivot)).intValue()) {
temp = ((Integer)list.get(small)).intValue();
list.set(small, list.get(i));
list.set(i, Integer.valueOf(temp));
small++;
} 
} 

temp = ((Integer)list.get(pivot)).intValue();
list.set(pivot, list.get(small - 1));
list.set(small - 1, Integer.valueOf(temp));
} catch (Exception e) {
System.out.println(high - low);
} 
return small - 1;
}

public void Rquicksort(List<Integer> list, int low, int high) {
int pivot = partition(list, low, high);
if (low < high) {
Rquicksort(list, low, pivot);
Rquicksort(list, pivot + 1, high);
} 
}
}

