/*     */ package jsc.onesample;
/*     */ 
/*     */ import jsc.combinatorics.Enumerator;
/*     */ import jsc.combinatorics.GrayCode;
/*     */ import jsc.combinatorics.Selection;
/*     */ import jsc.datastructures.PairedData;
/*     */ import jsc.descriptive.MeanVar;
/*     */ import jsc.statistics.PermutableStatistic;
/*     */ import jsc.tests.H1;
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
/*     */ public class PairedTtest
/*     */   extends Ttest
/*     */   implements PermutableStatistic
/*     */ {
/*     */   double rootN;
/*     */   double[] diffs;
/*     */   double[] permutedDiffs;
/*     */   
/*     */   public PairedTtest(PairedData paramPairedData, H1 paramH1) {
/*  56 */     super(paramPairedData.differences(), 0.0D, paramH1);
/*     */ 
/*     */     
/*  59 */     this.rootN = Math.sqrt(this.n);
/*  60 */     this.diffs = paramPairedData.differences();
/*  61 */     this.permutedDiffs = new double[this.n];
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
/*     */   public PairedTtest(PairedData paramPairedData) {
/*  88 */     this(paramPairedData, H1.NOT_EQUAL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumerator getEnumerator() {
/*  96 */     return (Enumerator)new GrayCode(this.n);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double permuteStatistic(Selection paramSelection) {
/* 107 */     int[] arrayOfInt = paramSelection.toIntArray();
/*     */     
/* 109 */     for (byte b = 0; b < this.n; b++) {
/*     */       
/* 111 */       if (arrayOfInt[b] == 0) {
/* 112 */         this.permutedDiffs[b] = this.diffs[b];
/*     */       } else {
/* 114 */         this.permutedDiffs[b] = -this.diffs[b];
/*     */       } 
/*     */     } 
/* 117 */     MeanVar meanVar = new MeanVar(this.permutedDiffs);
/* 118 */     return meanVar.getMean() / meanVar.getSd() / this.rootN;
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 160 */       double[] arrayOfDouble1 = { 70.0D, 80.0D, 62.0D, 50.0D, 70.0D, 30.0D, 49.0D, 60.0D };
/* 161 */       double[] arrayOfDouble2 = { 75.0D, 82.0D, 65.0D, 58.0D, 68.0D, 41.0D, 55.0D, 67.0D };
/* 162 */       PairedData pairedData = new PairedData(arrayOfDouble1, arrayOfDouble2);
/* 163 */       PairedTtest pairedTtest1 = new PairedTtest(pairedData);
/* 164 */       System.out.println("H1: means not equal: t = " + pairedTtest1.getTestStatistic() + " SP = " + pairedTtest1.getSP());
/* 165 */       PairedTtest pairedTtest2 = new PairedTtest(pairedData, H1.LESS_THAN);
/* 166 */       System.out.println("H1: mean A < mean B: t = " + pairedTtest2.getTestStatistic() + " SP = " + pairedTtest2.getSP());
/* 167 */       PairedTtest pairedTtest3 = new PairedTtest(pairedData, H1.GREATER_THAN);
/* 168 */       System.out.println("H1: mean A > mean B: t = " + pairedTtest3.getTestStatistic() + " SP = " + pairedTtest3.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/onesample/PairedTtest.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */