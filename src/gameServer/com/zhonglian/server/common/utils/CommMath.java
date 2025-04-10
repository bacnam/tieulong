package com.zhonglian.server.common.utils;

import BaseCommon.CommLog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommMath
{
private static final String m_randomHexKeyBase = "0123456789abcdef";
private static final String m_randomAlphamericBase = "0123456789abcdefghijklmnopqrstuvwxyz";

public static float randomFloat() {
return randomInt(10000) / 10000.0F;
}

public static int randomInt(int iDelta) {
return Math.abs(Random.nextInt(iDelta + 1));
}

public static int randomInt(int min, int max) {
if (min == max) {
return min;
}
return randomInt(max - min) + min;
}

public static <T> T randomOne(List<T> data) {
int index = randomInt(data.size() - 1);
return data.get(index);
}

public static <T> List<T> getRandomListByList(List<T> _totalList, int cnt) {
List<T> src = new ArrayList<>(_totalList);
List<T> ret = new ArrayList<>();
if (src.size() == 0) {
return ret;
}

if (cnt > src.size()) {
CommLog.warn("getRandomListByList cnt > _totalList.size()");
}
while (cnt > 0 && src.size() > 0) {
cnt--;
int index = randomInt(src.size() - 1);
ret.add(src.remove(index));
} 

return ret;
}

public static List<Integer> getRandomListByWeightList(List<Integer> origin, List<Integer> weight, int count) {
if (origin.size() != weight.size()) {
CommLog.warn("getRandomListByWeightList valueArray.size != weightArray.size");
}

List<Integer> idx = getRandomIndexByRate(weight, count);
List<Integer> ret = new ArrayList<>(idx.size());
for (int i = 0; i < idx.size(); i++) {
ret.add(origin.get(((Integer)idx.get(i)).intValue()));
}
return ret;
}

public static List<Integer> getRepeatableRandList(List<Integer> origin, List<Integer> weight, int count) {
if (origin.size() != weight.size()) {
CommLog.warn("getRandomListByWeightList valueArray.size != weightArray.size");
}

List<Integer> ret = new ArrayList<>(count);
int index = 0;
while (count > 0) {
index = getRandomIndexByRate(weight);
ret.add(origin.get(index));
count--;
} 
return ret;
}

public static int getRandomRateByList(List<Integer> _rateList, int _totalRate) {
if (_totalRate == 0 && _rateList.size() > 0) {
return 0;
}

if (_totalRate == 0) {
return -1;
}

int rand = randomInt(_totalRate - 1) + 1;

for (int index = 0; index < _rateList.size(); index++) {
int curRate = ((Integer)_rateList.get(index)).intValue();
if (rand <= curRate) {
return index;
}
rand -= curRate;
} 
return -1;
}

public static <T> ArrayList<T> getRandomList(ArrayList<T> _src) {
ArrayList<T> temp = new ArrayList<>(_src);
Collections.shuffle(temp);
return temp;
}

public static <T> T getRandomValue(List<Integer> _rateList, List<T> _valueList) {
if (_rateList == null) {
CommLog.error("getRandomValueByRate. _rareList is null");
}

if (_valueList == null) {
CommLog.error("getRandomValueByRate. _valueList is null");
}

int index = getRandomIndexByRate(_rateList);

if (-1 == index) {
return null;
}
if (index >= _valueList.size()) {
if (_valueList.isEmpty()) {
return null;
}
return _valueList.get(_valueList.size() - 1);
} 

return _valueList.get(index);
}

public static int getRandomIndexByRate(List<Integer> _rateList) {
int rand = 0;

for (Integer rate : _rateList) {
rand += rate.intValue();
}

if (_rateList.size() > 0 && rand == 0) {
return randomInt(_rateList.size() - 1);
}

return getRandomRateByList(_rateList, rand);
}

public static List<Integer> getRandomIndexByRate(List<Integer> _rateList, int count) {
ArrayList<Integer> copy = new ArrayList<>(_rateList);
ArrayList<Integer> ret = new ArrayList<>();
int idx = 0;
while (count > 0) {
idx = getRandomIndexByRate(copy);
ret.add(Integer.valueOf(idx));
copy.set(idx, Integer.valueOf(0));
count--;
} 
return ret;
}

public static <T> List<T> getRandomListByCnt(List<T> src, int count) {
ArrayList<T> copy = new ArrayList<>(src);

int ct = count / src.size();
while (ct > 0) {
copy.addAll(src);
ct--;
} 

copy = getRandomList(copy);
return copy.subList(0, count);
}

public static String randomHex() {
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 32; i++) {
int iIndex = Random.nextInt("0123456789abcdef".length());
sb.append("0123456789abcdef".charAt(iIndex));
} 
return sb.toString();
}

public static String randString(int count) {
StringBuilder sb = new StringBuilder();
for (int i = 0; i < count; i++) {
int iIndex = Random.nextInt("0123456789abcdefghijklmnopqrstuvwxyz".length());
sb.append("0123456789abcdefghijklmnopqrstuvwxyz".charAt(iIndex));
} 

return sb.toString();
}
}

