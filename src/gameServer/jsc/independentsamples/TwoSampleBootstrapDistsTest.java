/*     */ package jsc.independentsamples;
/*     */ 
/*     */ import jsc.distributions.Tail;
/*     */ import jsc.event.StatisticListener;
/*     */ import jsc.tests.H1;
/*     */ import jsc.util.Arrays;
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
/*     */ public class TwoSampleBootstrapDistsTest
/*     */   extends TwoSampleBootstrapTest
/*     */ {
/*     */   private double[] xA;
/*     */   private double[] xB;
/*     */   private double[] x;
/*     */   
/*     */   public TwoSampleBootstrapDistsTest(TwoSampleStatistic paramTwoSampleStatistic, Tail paramTail, int paramInt) {
/*  49 */     this(paramTwoSampleStatistic, paramTail, paramInt, (StatisticListener)null);
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
/*     */   public TwoSampleBootstrapDistsTest(TwoSampleStatistic paramTwoSampleStatistic, Tail paramTail, int paramInt, StatisticListener paramStatisticListener) {
/*  61 */     super(paramTwoSampleStatistic, paramTail, paramStatisticListener);
/*     */     
/*  63 */     this.x = Arrays.append(paramTwoSampleStatistic.getSampleA(), paramTwoSampleStatistic.getSampleB());
/*  64 */     this.xA = new double[this.nA];
/*  65 */     this.xB = new double[this.nB];
/*     */ 
/*     */ 
/*     */     
/*  69 */     calculateSP(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected double bootstrapSample() {
/*     */     byte b;
/*  77 */     for (b = 0; b < this.nA; ) { this.xA[b] = this.x[this.rand.nextInt(this.N)]; b++; }
/*  78 */      for (b = 0; b < this.nB; ) { this.xB[b] = this.x[this.rand.nextInt(this.N)]; b++; }
/*     */     
/*  80 */     return this.t.resampleStatistic(this.xA, this.xB);
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 100 */       double[] arrayOfDouble1 = { 94.0D, 197.0D, 16.0D, 38.0D, 99.0D, 141.0D, 23.0D };
/* 101 */       double[] arrayOfDouble2 = { 52.0D, 104.0D, 146.0D, 10.0D, 50.0D, 31.0D, 40.0D, 27.0D, 46.0D };
/* 102 */       TwoSampleTtest twoSampleTtest = new TwoSampleTtest(arrayOfDouble1, arrayOfDouble2, H1.GREATER_THAN, true);
/*     */ 
/*     */ 
/*     */       
/* 106 */       long l1 = System.currentTimeMillis();
/* 107 */       TwoSampleBootstrapDistsTest twoSampleBootstrapDistsTest = new TwoSampleBootstrapDistsTest(twoSampleTtest, Tail.UPPER, 5000000);
/* 108 */       long l2 = System.currentTimeMillis();
/* 109 */       System.out.println("Time = " + ((l2 - l1) / 1000L) + " secs");
/* 110 */       System.out.println("T = " + twoSampleTtest.getTestStatistic() + " SP = " + twoSampleTtest.getSP());
/* 111 */       System.out.println("Bootstrap SP = " + twoSampleBootstrapDistsTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/independentsamples/TwoSampleBootstrapDistsTest.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */