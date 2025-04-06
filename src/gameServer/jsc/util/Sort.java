/*     */ package jsc.util;
/*     */ 
/*     */ import java.util.Vector;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Sort
/*     */ {
/*     */   public static Vector getLabels(String[] paramArrayOfString) {
/*  32 */     int i = paramArrayOfString.length;
/*     */ 
/*     */     
/*  35 */     String[] arrayOfString = new String[i];
/*  36 */     System.arraycopy(paramArrayOfString, 0, arrayOfString, 0, i);
/*     */     
/*  38 */     sortStrings(arrayOfString, null, 0, i - 1, true);
/*  39 */     Vector vector = new Vector();
/*  40 */     for (byte b = 0; b < i; b++) {
/*  41 */       if (!vector.contains(arrayOfString[b])) vector.addElement(arrayOfString[b]); 
/*  42 */     }  return vector;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sort(double[] paramArrayOfdouble, int[] paramArrayOfint, int paramInt1, int paramInt2, boolean paramBoolean) {
/*  84 */     if (paramArrayOfdouble == null || paramArrayOfdouble.length < 2) {
/*     */       return;
/*     */     }
/*     */     
/*  88 */     int i = paramInt1, j = paramInt2;
/*  89 */     Double double_ = new Double(paramArrayOfdouble[(paramInt1 + paramInt2) / 2]);
/*     */     while (true) {
/*  91 */       if (paramBoolean) {
/*  92 */         for (; i < paramInt2 && double_.compareTo(new Double(paramArrayOfdouble[i])) > 0; i++);
/*  93 */         for (; j > paramInt1 && double_.compareTo(new Double(paramArrayOfdouble[j])) < 0; j--);
/*     */       } else {
/*  95 */         for (; i < paramInt2 && double_.compareTo(new Double(paramArrayOfdouble[i])) < 0; i++);
/*  96 */         for (; j > paramInt1 && double_.compareTo(new Double(paramArrayOfdouble[j])) > 0; j--);
/*     */       } 
/*  98 */       if (i < j) {
/*  99 */         double d = paramArrayOfdouble[i]; paramArrayOfdouble[i] = paramArrayOfdouble[j]; paramArrayOfdouble[j] = d;
/* 100 */         if (paramArrayOfint != null) { int k = paramArrayOfint[i]; paramArrayOfint[i] = paramArrayOfint[j]; paramArrayOfint[j] = k; }
/*     */       
/* 102 */       }  if (i <= j) { i++; j--; }
/* 103 */        if (i > j) {
/* 104 */         if (paramInt1 < j) sort(paramArrayOfdouble, paramArrayOfint, paramInt1, j, paramBoolean); 
/* 105 */         if (i < paramInt2) sort(paramArrayOfdouble, paramArrayOfint, i, paramInt2, paramBoolean);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
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
/*     */   
/*     */   public static void sort(Double[] paramArrayOfDouble, int[] paramArrayOfint, int paramInt1, int paramInt2, boolean paramBoolean) {
/* 123 */     if (paramArrayOfDouble == null || paramArrayOfDouble.length < 2) {
/*     */       return;
/*     */     }
/*     */     
/* 127 */     int i = paramInt1, j = paramInt2;
/* 128 */     Double double_ = paramArrayOfDouble[(paramInt1 + paramInt2) / 2];
/*     */     while (true) {
/* 130 */       if (paramBoolean) {
/* 131 */         for (; i < paramInt2 && double_.compareTo(paramArrayOfDouble[i]) > 0; i++);
/* 132 */         for (; j > paramInt1 && double_.compareTo(paramArrayOfDouble[j]) < 0; j--);
/*     */       } else {
/* 134 */         for (; i < paramInt2 && double_.compareTo(paramArrayOfDouble[i]) < 0; i++);
/* 135 */         for (; j > paramInt1 && double_.compareTo(paramArrayOfDouble[j]) > 0; j--);
/*     */       } 
/* 137 */       if (i < j) {
/* 138 */         Double double_1 = paramArrayOfDouble[i]; paramArrayOfDouble[i] = paramArrayOfDouble[j]; paramArrayOfDouble[j] = double_1;
/* 139 */         if (paramArrayOfint != null) { int k = paramArrayOfint[i]; paramArrayOfint[i] = paramArrayOfint[j]; paramArrayOfint[j] = k; }
/*     */       
/* 141 */       }  if (i <= j) { i++; j--; }
/* 142 */        if (i > j) {
/* 143 */         if (paramInt1 < j) sort(paramArrayOfDouble, paramArrayOfint, paramInt1, j, paramBoolean); 
/* 144 */         if (i < paramInt2) sort(paramArrayOfDouble, paramArrayOfint, i, paramInt2, paramBoolean);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDoubles(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, int paramInt1, int paramInt2, boolean paramBoolean) {
/* 165 */     if (paramArrayOfdouble1 == null || paramArrayOfdouble1.length < 2) {
/*     */       return;
/*     */     }
/*     */     
/* 169 */     int i = paramInt1, j = paramInt2;
/* 170 */     Double double_ = new Double(paramArrayOfdouble1[(paramInt1 + paramInt2) / 2]);
/*     */     while (true) {
/* 172 */       if (paramBoolean) {
/* 173 */         for (; i < paramInt2 && double_.compareTo(new Double(paramArrayOfdouble1[i])) > 0; i++);
/* 174 */         for (; j > paramInt1 && double_.compareTo(new Double(paramArrayOfdouble1[j])) < 0; j--);
/*     */       } else {
/* 176 */         for (; i < paramInt2 && double_.compareTo(new Double(paramArrayOfdouble1[i])) < 0; i++);
/* 177 */         for (; j > paramInt1 && double_.compareTo(new Double(paramArrayOfdouble1[j])) > 0; j--);
/*     */       } 
/* 179 */       if (i < j) {
/* 180 */         double d = paramArrayOfdouble1[i]; paramArrayOfdouble1[i] = paramArrayOfdouble1[j]; paramArrayOfdouble1[j] = d;
/* 181 */         if (paramArrayOfdouble2 != null) { d = paramArrayOfdouble2[i]; paramArrayOfdouble2[i] = paramArrayOfdouble2[j]; paramArrayOfdouble2[j] = d; }
/*     */       
/* 183 */       }  if (i <= j) { i++; j--; }
/* 184 */        if (i > j) {
/* 185 */         if (paramInt1 < j) sortDoubles(paramArrayOfdouble1, paramArrayOfdouble2, paramInt1, j, paramBoolean); 
/* 186 */         if (i < paramInt2) sortDoubles(paramArrayOfdouble1, paramArrayOfdouble2, i, paramInt2, paramBoolean);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortInts(int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt1, int paramInt2, boolean paramBoolean) {
/* 207 */     if (paramArrayOfint1 == null || paramArrayOfint1.length < 2) {
/*     */       return;
/*     */     }
/*     */     
/* 211 */     int i = paramInt1, j = paramInt2;
/* 212 */     Integer integer = new Integer(paramArrayOfint1[(paramInt1 + paramInt2) / 2]);
/*     */     while (true) {
/* 214 */       if (paramBoolean) {
/* 215 */         for (; i < paramInt2 && integer.compareTo(new Integer(paramArrayOfint1[i])) > 0; i++);
/* 216 */         for (; j > paramInt1 && integer.compareTo(new Integer(paramArrayOfint1[j])) < 0; j--);
/*     */       } else {
/* 218 */         for (; i < paramInt2 && integer.compareTo(new Integer(paramArrayOfint1[i])) < 0; i++);
/* 219 */         for (; j > paramInt1 && integer.compareTo(new Integer(paramArrayOfint1[j])) > 0; j--);
/*     */       } 
/* 221 */       if (i < j) {
/* 222 */         int k = paramArrayOfint1[i]; paramArrayOfint1[i] = paramArrayOfint1[j]; paramArrayOfint1[j] = k;
/* 223 */         if (paramArrayOfint2 != null) { int m = paramArrayOfint2[i]; paramArrayOfint2[i] = paramArrayOfint2[j]; paramArrayOfint2[j] = m; }
/*     */       
/* 225 */       }  if (i <= j) { i++; j--; }
/* 226 */        if (i > j) {
/* 227 */         if (paramInt1 < j) sortInts(paramArrayOfint1, paramArrayOfint2, paramInt1, j, paramBoolean); 
/* 228 */         if (i < paramInt2) sortInts(paramArrayOfint1, paramArrayOfint2, i, paramInt2, paramBoolean);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
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
/*     */ 
/*     */   
/*     */   public static void sortStrings(String[] paramArrayOfString1, String[] paramArrayOfString2, int paramInt1, int paramInt2, boolean paramBoolean) {
/* 247 */     if (paramArrayOfString1 == null || paramArrayOfString1.length < 2) {
/*     */       return;
/*     */     }
/*     */     
/* 251 */     int i = paramInt1, j = paramInt2;
/* 252 */     String str = paramArrayOfString1[(paramInt1 + paramInt2) / 2];
/*     */     while (true) {
/* 254 */       if (paramBoolean) {
/* 255 */         for (; i < paramInt2 && compareStrings(str, paramArrayOfString1[i]) > 0; i++);
/* 256 */         for (; j > paramInt1 && compareStrings(str, paramArrayOfString1[j]) < 0; j--);
/*     */       } else {
/* 258 */         for (; i < paramInt2 && compareStrings(str, paramArrayOfString1[i]) < 0; i++);
/* 259 */         for (; j > paramInt1 && compareStrings(str, paramArrayOfString1[j]) > 0; j--);
/*     */       } 
/* 261 */       if (i < j) {
/* 262 */         String str1 = paramArrayOfString1[i]; paramArrayOfString1[i] = paramArrayOfString1[j]; paramArrayOfString1[j] = str1;
/* 263 */         if (paramArrayOfString2 != null) { str1 = paramArrayOfString2[i]; paramArrayOfString2[i] = paramArrayOfString2[j]; paramArrayOfString2[j] = str1; }
/*     */       
/* 265 */       }  if (i <= j) { i++; j--; }
/* 266 */        if (i > j) {
/* 267 */         if (paramInt1 < j) sortStrings(paramArrayOfString1, paramArrayOfString2, paramInt1, j, paramBoolean); 
/* 268 */         if (i < paramInt2) sortStrings(paramArrayOfString1, paramArrayOfString2, i, paramInt2, paramBoolean);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int compare(Object paramObject1, Object paramObject2, boolean paramBoolean) {
/* 291 */     if (paramObject1 != null && paramObject2 == null) return paramBoolean ? -1 : 1; 
/* 292 */     if (paramObject1 == null && paramObject2 == null) return 0; 
/* 293 */     if (paramObject1 == null && paramObject2 != null) return paramBoolean ? 1 : -1; 
/* 294 */     if (paramObject1 instanceof Double && paramObject2 instanceof Double) {
/* 295 */       Double double_1 = (Double)paramObject1, double_2 = (Double)paramObject2; return double_1.compareTo(double_2);
/* 296 */     }  if (paramObject1 instanceof Integer && paramObject2 instanceof Integer) {
/* 297 */       Integer integer1 = (Integer)paramObject1, integer2 = (Integer)paramObject2; return integer1.compareTo(integer2);
/* 298 */     }  return compareStrings(paramObject1.toString(), paramObject2.toString());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int compareStrings(String paramString1, String paramString2) {
/*     */     try {
/* 321 */       Double double_1 = new Double(Double.parseDouble(paramString1));
/* 322 */       Double double_2 = new Double(Double.parseDouble(paramString2));
/* 323 */       return double_1.compareTo(double_2);
/*     */     } catch (NumberFormatException numberFormatException) {
/*     */       
/* 326 */       return paramString1.compareToIgnoreCase(paramString2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 336 */       double[] arrayOfDouble = { 7.0D, 3.0D, -4.0D, 3.0D, 1.0D, 8.0D, 2.0D, 1.0D, 6.0D, -5.0D, -2.0D, 0.0D, 7.0D };
/* 337 */       Sort.sortDoubles(arrayOfDouble, null, 0, arrayOfDouble.length - 1, true); byte b;
/* 338 */       for (b = 0; b < arrayOfDouble.length; b++)
/* 339 */         System.out.println(arrayOfDouble[b] + ", "); 
/* 340 */       String[] arrayOfString = { "X-ray", "1211", "0", "Golf", "99", "x-ray", "111", "Hotel", "Alpha", "Zero", "25", "Juliette", "£", "Foxtrot", "Tango", "Romeo", "1111", "Bravo", "November", "Charlie", "*", "-10", "Victor", "10", "romeo", "12", "11", "121", "1", "-100", "-1", "-0" };
/*     */ 
/*     */       
/* 343 */       Sort.sortStrings(arrayOfString, null, 0, arrayOfString.length - 1, true);
/* 344 */       for (b = 0; b < arrayOfString.length; b++)
/* 345 */         System.out.println(arrayOfString[b] + ", "); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/util/Sort.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */