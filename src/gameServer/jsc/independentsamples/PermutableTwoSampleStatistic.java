/*    */ package jsc.independentsamples;
/*    */ 
/*    */ import jsc.combinatorics.Selection;
/*    */ import jsc.statistics.PermutableStatistic;
/*    */ import jsc.util.Arrays;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class PermutableTwoSampleStatistic
/*    */   implements PermutableStatistic
/*    */ {
/*    */   protected int N;
/*    */   protected double[] originalSample;
/*    */   protected double[] permutedSampleA;
/*    */   protected double[] permutedSampleB;
/*    */   
/*    */   public PermutableTwoSampleStatistic(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
/* 42 */     this.originalSample = Arrays.append(paramArrayOfdouble2, paramArrayOfdouble1);
/* 43 */     this.N = this.originalSample.length;
/* 44 */     this.permutedSampleA = new double[paramArrayOfdouble1.length];
/* 45 */     this.permutedSampleB = new double[paramArrayOfdouble2.length];
/*    */   }
/*    */   public int getN() {
/* 48 */     return this.N;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double permuteStatistic(Selection paramSelection) {
/* 75 */     byte b1 = 0;
/* 76 */     byte b2 = 0;
/* 77 */     int[] arrayOfInt = paramSelection.toIntArray();
/*    */     
/* 79 */     for (byte b3 = 0; b3 < this.N; b3++) {
/*    */       
/* 81 */       if (arrayOfInt[b3] == 1) {
/* 82 */         this.permutedSampleA[b1] = this.originalSample[b3]; b1++;
/* 83 */       } else if (arrayOfInt[b3] == 2) {
/* 84 */         this.permutedSampleB[b2] = this.originalSample[b3]; b2++;
/*    */       } else {
/* 86 */         throw new IllegalArgumentException("Invalid permutation.");
/*    */       } 
/*    */     } 
/*    */     
/* 90 */     return resampleStatistic(this.permutedSampleA, this.permutedSampleB);
/*    */   }
/*    */   
/*    */   public abstract double resampleStatistic(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2);
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/independentsamples/PermutableTwoSampleStatistic.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */