/*     */ package jsc.independentsamples;
/*     */ 
/*     */ import jsc.event.StatisticListener;
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
/*     */ public class TwoSampleBootstrapMeansTest
/*     */   extends TwoSampleBootstrapTest
/*     */ {
/*     */   private double[] rA;
/*     */   private double[] rB;
/*     */   private final double[] zA;
/*     */   private final double[] zB;
/*     */   
/*     */   public TwoSampleBootstrapMeansTest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, int paramInt) {
/*  42 */     this(paramArrayOfdouble1, paramArrayOfdouble2, H1.NOT_EQUAL, paramInt, (StatisticListener)null);
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
/*     */   public TwoSampleBootstrapMeansTest(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, H1 paramH1, int paramInt, StatisticListener paramStatisticListener) {
/*  55 */     super(new TwoSampleTtest(paramArrayOfdouble1, paramArrayOfdouble2, paramH1, false), H1.toTail(paramH1), paramStatisticListener);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  61 */     double d1 = 0.0D, d2 = 0.0D, d3 = 0.0D; byte b;
/*  62 */     for (b = 0; b < this.nA; ) { d1 += paramArrayOfdouble1[b]; d3 += paramArrayOfdouble1[b]; b++; }
/*  63 */      d1 /= this.nA;
/*  64 */     for (b = 0; b < this.nB; ) { d2 += paramArrayOfdouble2[b]; d3 += paramArrayOfdouble2[b]; b++; }
/*  65 */      d2 /= this.nB;
/*  66 */     d3 /= this.N;
/*     */     
/*  68 */     this.rA = new double[this.nA];
/*  69 */     this.rB = new double[this.nB];
/*  70 */     this.zA = new double[this.nA];
/*  71 */     this.zB = new double[this.nB];
/*     */     
/*  73 */     for (b = 0; b < this.nA; ) { this.zA[b] = paramArrayOfdouble1[b] - d1 + d3; b++; }
/*  74 */      for (b = 0; b < this.nB; ) { this.zB[b] = paramArrayOfdouble2[b] - d2 + d3; b++; }
/*     */ 
/*     */ 
/*     */     
/*  78 */     calculateSP(paramInt);
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
/*     */   protected double bootstrapSample() {
/*     */     byte b;
/*  95 */     for (b = 0; b < this.nA; ) { this.rA[b] = this.zA[this.rand.nextInt(this.nA)]; b++; }
/*  96 */      for (b = 0; b < this.nB; ) { this.rB[b] = this.zB[this.rand.nextInt(this.nB)]; b++; }
/*  97 */      return this.t.resampleStatistic(this.rA, this.rB);
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
/* 117 */       double[] arrayOfDouble1 = { 94.0D, 197.0D, 16.0D, 38.0D, 99.0D, 141.0D, 23.0D };
/* 118 */       double[] arrayOfDouble2 = { 52.0D, 104.0D, 146.0D, 10.0D, 50.0D, 31.0D, 40.0D, 27.0D, 46.0D };
/* 119 */       H1 h1 = H1.NOT_EQUAL;
/* 120 */       TwoSampleTtest twoSampleTtest = new TwoSampleTtest(arrayOfDouble1, arrayOfDouble2, h1, false);
/* 121 */       System.out.println("T = " + twoSampleTtest.getTestStatistic() + " SP = " + twoSampleTtest.getSP());
/*     */ 
/*     */ 
/*     */       
/* 125 */       long l1 = System.currentTimeMillis();
/* 126 */       TwoSampleBootstrapMeansTest twoSampleBootstrapMeansTest = new TwoSampleBootstrapMeansTest(arrayOfDouble1, arrayOfDouble2, h1, 5000000, null);
/* 127 */       long l2 = System.currentTimeMillis();
/* 128 */       System.out.println("Time = " + ((l2 - l1) / 1000L) + " secs");
/* 129 */       System.out.println("Bootstrap SP = " + twoSampleBootstrapMeansTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/independentsamples/TwoSampleBootstrapMeansTest.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */