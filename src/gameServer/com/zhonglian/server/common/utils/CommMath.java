/*     */ package com.zhonglian.server.common.utils;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommMath
/*     */ {
/*     */   private static final String m_randomHexKeyBase = "0123456789abcdef";
/*     */   private static final String m_randomAlphamericBase = "0123456789abcdefghijklmnopqrstuvwxyz";
/*     */   
/*     */   public static float randomFloat() {
/*  20 */     return randomInt(10000) / 10000.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int randomInt(int iDelta) {
/*  30 */     return Math.abs(Random.nextInt(iDelta + 1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int randomInt(int min, int max) {
/*  39 */     if (min == max) {
/*  40 */       return min;
/*     */     }
/*  42 */     return randomInt(max - min) + min;
/*     */   }
/*     */   
/*     */   public static <T> T randomOne(List<T> data) {
/*  46 */     int index = randomInt(data.size() - 1);
/*  47 */     return data.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> List<T> getRandomListByList(List<T> _totalList, int cnt) {
/*  58 */     List<T> src = new ArrayList<>(_totalList);
/*  59 */     List<T> ret = new ArrayList<>();
/*  60 */     if (src.size() == 0) {
/*  61 */       return ret;
/*     */     }
/*     */     
/*  64 */     if (cnt > src.size()) {
/*  65 */       CommLog.warn("getRandomListByList cnt > _totalList.size()");
/*     */     }
/*  67 */     while (cnt > 0 && src.size() > 0) {
/*  68 */       cnt--;
/*  69 */       int index = randomInt(src.size() - 1);
/*  70 */       ret.add(src.remove(index));
/*     */     } 
/*     */     
/*  73 */     return ret;
/*     */   }
/*     */   
/*     */   public static List<Integer> getRandomListByWeightList(List<Integer> origin, List<Integer> weight, int count) {
/*  77 */     if (origin.size() != weight.size()) {
/*  78 */       CommLog.warn("getRandomListByWeightList valueArray.size != weightArray.size");
/*     */     }
/*     */     
/*  81 */     List<Integer> idx = getRandomIndexByRate(weight, count);
/*  82 */     List<Integer> ret = new ArrayList<>(idx.size());
/*  83 */     for (int i = 0; i < idx.size(); i++) {
/*  84 */       ret.add(origin.get(((Integer)idx.get(i)).intValue()));
/*     */     }
/*  86 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Integer> getRepeatableRandList(List<Integer> origin, List<Integer> weight, int count) {
/*  98 */     if (origin.size() != weight.size()) {
/*  99 */       CommLog.warn("getRandomListByWeightList valueArray.size != weightArray.size");
/*     */     }
/*     */     
/* 102 */     List<Integer> ret = new ArrayList<>(count);
/* 103 */     int index = 0;
/* 104 */     while (count > 0) {
/* 105 */       index = getRandomIndexByRate(weight);
/* 106 */       ret.add(origin.get(index));
/* 107 */       count--;
/*     */     } 
/* 109 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getRandomRateByList(List<Integer> _rateList, int _totalRate) {
/* 120 */     if (_totalRate == 0 && _rateList.size() > 0) {
/* 121 */       return 0;
/*     */     }
/*     */     
/* 124 */     if (_totalRate == 0) {
/* 125 */       return -1;
/*     */     }
/*     */     
/* 128 */     int rand = randomInt(_totalRate - 1) + 1;
/*     */     
/* 130 */     for (int index = 0; index < _rateList.size(); index++) {
/* 131 */       int curRate = ((Integer)_rateList.get(index)).intValue();
/* 132 */       if (rand <= curRate) {
/* 133 */         return index;
/*     */       }
/* 135 */       rand -= curRate;
/*     */     } 
/* 137 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> ArrayList<T> getRandomList(ArrayList<T> _src) {
/* 148 */     ArrayList<T> temp = new ArrayList<>(_src);
/* 149 */     Collections.shuffle(temp);
/* 150 */     return temp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T getRandomValue(List<Integer> _rateList, List<T> _valueList) {
/* 163 */     if (_rateList == null) {
/* 164 */       CommLog.error("getRandomValueByRate. _rareList is null");
/*     */     }
/*     */     
/* 167 */     if (_valueList == null) {
/* 168 */       CommLog.error("getRandomValueByRate. _valueList is null");
/*     */     }
/*     */     
/* 171 */     int index = getRandomIndexByRate(_rateList);
/*     */     
/* 173 */     if (-1 == index) {
/* 174 */       return null;
/*     */     }
/* 176 */     if (index >= _valueList.size()) {
/* 177 */       if (_valueList.isEmpty()) {
/* 178 */         return null;
/*     */       }
/* 180 */       return _valueList.get(_valueList.size() - 1);
/*     */     } 
/*     */     
/* 183 */     return _valueList.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getRandomIndexByRate(List<Integer> _rateList) {
/* 193 */     int rand = 0;
/*     */     
/* 195 */     for (Integer rate : _rateList) {
/* 196 */       rand += rate.intValue();
/*     */     }
/*     */     
/* 199 */     if (_rateList.size() > 0 && rand == 0) {
/* 200 */       return randomInt(_rateList.size() - 1);
/*     */     }
/*     */     
/* 203 */     return getRandomRateByList(_rateList, rand);
/*     */   }
/*     */   
/*     */   public static List<Integer> getRandomIndexByRate(List<Integer> _rateList, int count) {
/* 207 */     ArrayList<Integer> copy = new ArrayList<>(_rateList);
/* 208 */     ArrayList<Integer> ret = new ArrayList<>();
/* 209 */     int idx = 0;
/* 210 */     while (count > 0) {
/* 211 */       idx = getRandomIndexByRate(copy);
/* 212 */       ret.add(Integer.valueOf(idx));
/* 213 */       copy.set(idx, Integer.valueOf(0));
/* 214 */       count--;
/*     */     } 
/* 216 */     return ret;
/*     */   }
/*     */   
/*     */   public static <T> List<T> getRandomListByCnt(List<T> src, int count) {
/* 220 */     ArrayList<T> copy = new ArrayList<>(src);
/*     */     
/* 222 */     int ct = count / src.size();
/* 223 */     while (ct > 0) {
/* 224 */       copy.addAll(src);
/* 225 */       ct--;
/*     */     } 
/*     */     
/* 228 */     copy = getRandomList(copy);
/* 229 */     return copy.subList(0, count);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String randomHex() {
/* 238 */     StringBuilder sb = new StringBuilder();
/* 239 */     for (int i = 0; i < 32; i++) {
/* 240 */       int iIndex = Random.nextInt("0123456789abcdef".length());
/* 241 */       sb.append("0123456789abcdef".charAt(iIndex));
/*     */     } 
/* 243 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String randString(int count) {
/* 253 */     StringBuilder sb = new StringBuilder();
/* 254 */     for (int i = 0; i < count; i++) {
/* 255 */       int iIndex = Random.nextInt("0123456789abcdefghijklmnopqrstuvwxyz".length());
/* 256 */       sb.append("0123456789abcdefghijklmnopqrstuvwxyz".charAt(iIndex));
/*     */     } 
/*     */     
/* 259 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/CommMath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */