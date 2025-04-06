/*     */ package jsc.combinatorics;
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
/*     */ public class MultiSetPermutation
/*     */   implements Selection
/*     */ {
/*     */   private int[] p;
/*     */   private int[] subsetSizes;
/*     */   
/*     */   MultiSetPermutation(int[] paramArrayOfint1, int[] paramArrayOfint2, boolean paramBoolean) {
/*  33 */     this.p = paramArrayOfint1;
/*  34 */     this.subsetSizes = paramArrayOfint2;
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
/*     */   public MultiSetPermutation(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  51 */     int i = paramArrayOfint1.length;
/*  52 */     int j = paramArrayOfint2.length;
/*  53 */     int[] arrayOfInt = new int[j];
/*     */     
/*     */     byte b;
/*  56 */     for (b = 0; b < j; ) { arrayOfInt[b] = 0; b++; }
/*  57 */      for (b = 0; b < i; b++) {
/*     */       
/*  59 */       int k = paramArrayOfint1[b];
/*  60 */       if (k < 1 || k > j)
/*  61 */         throw new IllegalArgumentException("Multi-set permutation array contains incorrect values."); 
/*  62 */       arrayOfInt[k - 1] = arrayOfInt[k - 1] + 1;
/*     */     } 
/*  64 */     for (b = 0; b < j; b++) {
/*     */       
/*  66 */       if (arrayOfInt[b] != paramArrayOfint2[b]) {
/*  67 */         throw new IllegalArgumentException("Multi-set permutation array contains incorrect values.");
/*     */       }
/*     */     } 
/*  70 */     this.p = paramArrayOfint1;
/*  71 */     this.subsetSizes = paramArrayOfint2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSubsetCount() {
/*  79 */     return this.subsetSizes.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getSubsetSizes() {
/*  86 */     return this.subsetSizes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/*  93 */     return this.p.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] toIntArray() {
/* 100 */     return this.p;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 104 */     StringBuffer stringBuffer = new StringBuffer();
/* 105 */     stringBuffer.append("MultiSetPermutation:");
/* 106 */     for (byte b = 0; b < this.p.length; ) { stringBuffer.append(" " + this.p[b]); b++; }
/* 107 */      return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 115 */       int[] arrayOfInt1 = { 2, 1, 3 };
/* 116 */       int[] arrayOfInt2 = { 2, 3, 1, 3, 1, 3 };
/* 117 */       MultiSetPermutation multiSetPermutation = new MultiSetPermutation(arrayOfInt2, arrayOfInt1);
/* 118 */       System.out.println(multiSetPermutation.toString());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/combinatorics/MultiSetPermutation.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */