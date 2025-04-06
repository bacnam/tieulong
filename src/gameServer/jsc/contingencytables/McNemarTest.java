/*     */ package jsc.contingencytables;
/*     */ 
/*     */ import jsc.distributions.ChiSquared;
/*     */ import jsc.tests.SignificanceTest;
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
/*     */ public class McNemarTest
/*     */   implements SignificanceTest
/*     */ {
/*     */   private double chiSquared;
/*     */   private double SP;
/*     */   
/*     */   public McNemarTest(ContingencyTable2x2 paramContingencyTable2x2) {
/*  39 */     this(paramContingencyTable2x2, true);
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
/*     */   public McNemarTest(ContingencyTable2x2 paramContingencyTable2x2, boolean paramBoolean) {
/*  56 */     int i = paramContingencyTable2x2.getFrequency(0, 1);
/*  57 */     int j = paramContingencyTable2x2.getFrequency(1, 0);
/*  58 */     if (i + j == 0) {
/*  59 */       throw new IllegalArgumentException("Frequencies are zero.");
/*     */     }
/*  61 */     if (paramBoolean) {
/*     */       
/*  63 */       double d = (Math.abs(i - j) - 1);
/*  64 */       this.chiSquared = d * d / (i + j);
/*     */     }
/*     */     else {
/*     */       
/*  68 */       double d = (i - j);
/*  69 */       this.chiSquared = d * d / (i + j);
/*     */     } 
/*     */     
/*  72 */     this.SP = ChiSquared.upperTailProb(this.chiSquared, 1.0D);
/*     */   }
/*     */   public double getSP() {
/*  75 */     return this.SP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTestStatistic() {
/*  82 */     return this.chiSquared;
/*     */   }
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
/*  94 */       String[] arrayOfString1 = { "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "C", "C", "C", "C", "C", "C", "C" };
/*  95 */       String[] arrayOfString2 = { "A", "A", "A", "A", "C", "C", "C", "C", "C", "C", "C", "C", "C", "C", "C", "C", "C", "C", "C", "C", "C", "A", "A", "A", "A" };
/*     */       
/*  97 */       byte b1 = 2;
/*  98 */       ContingencyTable2x2[] arrayOfContingencyTable2x2 = new ContingencyTable2x2[b1];
/*  99 */       arrayOfContingencyTable2x2[0] = new ContingencyTable2x2(14, 5, 2, 2);
/*     */       
/* 101 */       arrayOfContingencyTable2x2[1] = new ContingencyTable2x2(arrayOfString1, arrayOfString2);
/* 102 */       for (byte b2 = 0; b2 < b1; b2++) {
/*     */         
/* 104 */         System.out.println(arrayOfContingencyTable2x2[b2].toString());
/* 105 */         McNemarTest mcNemarTest = new McNemarTest(arrayOfContingencyTable2x2[b2]);
/* 106 */         System.out.println("Chi-squared = " + mcNemarTest.getTestStatistic() + " SP = " + mcNemarTest.getSP());
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/contingencytables/McNemarTest.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */