/*     */ package jsc.util;
/*     */ 
/*     */ import java.text.NumberFormat;
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
/*     */ public class Arrays
/*     */ {
/*     */   public static double[] append(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
/*  30 */     int i = paramArrayOfdouble1.length;
/*  31 */     int j = paramArrayOfdouble2.length;
/*  32 */     double[] arrayOfDouble = new double[i + j];
/*     */     byte b;
/*  34 */     for (b = 0; b < j; ) { arrayOfDouble[b] = paramArrayOfdouble2[b]; b++; }
/*  35 */      for (b = 0; b < i; ) { arrayOfDouble[b + j] = paramArrayOfdouble1[b]; b++; }
/*  36 */      return arrayOfDouble;
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
/*     */   public static int[] fill(int paramInt1, int paramInt2) {
/*  48 */     int[] arrayOfInt = new int[paramInt1];
/*     */     
/*  50 */     java.util.Arrays.fill(arrayOfInt, paramInt2);
/*  51 */     return arrayOfInt;
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
/*     */   public static boolean isConstant(int[] paramArrayOfint) {
/*  63 */     int i = paramArrayOfint[0];
/*  64 */     for (byte b = 1; b < paramArrayOfint.length; ) { if (paramArrayOfint[b] != i) return false;  b++; }
/*  65 */      return true;
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
/*     */   public static boolean isConstant(double[] paramArrayOfdouble) {
/*  77 */     double d = paramArrayOfdouble[0];
/*  78 */     for (byte b = 1; b < paramArrayOfdouble.length; ) { if (paramArrayOfdouble[b] != d) return false;  b++; }
/*  79 */      return true;
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
/*     */   public static boolean isIntegers(double[] paramArrayOfdouble) {
/*  91 */     for (byte b = 0; b < paramArrayOfdouble.length; ) { if (Math.rint(paramArrayOfdouble[b]) != paramArrayOfdouble[b]) return false;  b++; }
/*  92 */      return true;
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
/*     */   public static double[] log(double[] paramArrayOfdouble) {
/* 105 */     int i = paramArrayOfdouble.length;
/* 106 */     double[] arrayOfDouble = new double[i];
/* 107 */     for (byte b = 0; b < i; b++) {
/*     */       
/* 109 */       if (paramArrayOfdouble[b] <= 0.0D)
/* 110 */         throw new IllegalArgumentException("Element " + b + " not greater than zero."); 
/* 111 */       arrayOfDouble[b] = Math.log(paramArrayOfdouble[b]);
/*     */     } 
/* 113 */     return arrayOfDouble;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double max(double[] paramArrayOfdouble) {
/* 124 */     double d = paramArrayOfdouble[0];
/* 125 */     for (byte b = 1; b < paramArrayOfdouble.length; b++) {
/* 126 */       if (paramArrayOfdouble[b] > d) d = paramArrayOfdouble[b]; 
/* 127 */     }  return d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double min(double[] paramArrayOfdouble) {
/* 138 */     double d = paramArrayOfdouble[0];
/* 139 */     for (byte b = 1; b < paramArrayOfdouble.length; b++) {
/* 140 */       if (paramArrayOfdouble[b] < d) d = paramArrayOfdouble[b]; 
/* 141 */     }  return d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long product(int[] paramArrayOfint) {
/* 152 */     long l = paramArrayOfint[0];
/* 153 */     for (byte b = 1; b < paramArrayOfint.length; ) { l *= paramArrayOfint[b]; b++; }
/* 154 */      return l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] sequence(int paramInt) {
/* 163 */     return sequence(0, paramInt - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] sequence(int paramInt1, int paramInt2) {
/* 174 */     int[] arrayOfInt = new int[paramInt2 - paramInt1 + 1];
/* 175 */     for (int i = paramInt1; i <= paramInt2; ) { arrayOfInt[i - paramInt1] = i; i++; }
/* 176 */      return arrayOfInt;
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
/*     */   public static double[] subtract(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
/* 190 */     int i = paramArrayOfdouble1.length;
/* 191 */     if (i != paramArrayOfdouble2.length)
/* 192 */       throw new IllegalArgumentException("Arrays not equal length."); 
/* 193 */     double[] arrayOfDouble = new double[i];
/* 194 */     for (byte b = 0; b < i; ) { arrayOfDouble[b] = paramArrayOfdouble1[b] - paramArrayOfdouble2[b]; b++; }
/* 195 */      return arrayOfDouble;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long sum(int[] paramArrayOfint) {
/* 206 */     long l = paramArrayOfint[0];
/* 207 */     for (byte b = 1; b < paramArrayOfint.length; ) { l += paramArrayOfint[b]; b++; }
/* 208 */      return l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double sum(double[] paramArrayOfdouble) {
/* 219 */     double d = paramArrayOfdouble[0];
/* 220 */     for (byte b = 1; b < paramArrayOfdouble.length; ) { d += paramArrayOfdouble[b]; b++; }
/* 221 */      return d;
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
/*     */   public static String[] toStringArray(int[] paramArrayOfint) {
/* 233 */     String[] arrayOfString = new String[paramArrayOfint.length];
/*     */     
/* 235 */     NumberFormat numberFormat = NumberFormat.getNumberInstance();
/* 236 */     for (byte b = 0; b < paramArrayOfint.length; b++)
/* 237 */       arrayOfString[b] = numberFormat.format(paramArrayOfint[b]); 
/* 238 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] toStringArray(Vector paramVector) {
/* 249 */     String[] arrayOfString = new String[paramVector.size()];
/* 250 */     return (String[])paramVector.toArray((Object[])arrayOfString);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/util/Arrays.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */