/*     */ package jsc.independentsamples;
/*     */ 
/*     */ import jsc.distributions.Gamma;
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
/*     */ 
/*     */ public class RatioScaleCI
/*     */   extends MannWhitneyMedianDifferenceCI
/*     */ {
/*     */   public RatioScaleCI(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, double paramDouble, int paramInt) {
/*  44 */     super(Arrays.log(paramArrayOfdouble1), Arrays.log(paramArrayOfdouble2), paramDouble, paramInt);
/*  45 */     this.lowerLimit = Math.exp(this.lowerLimit);
/*  46 */     this.upperLimit = Math.exp(this.upperLimit);
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
/*     */   public RatioScaleCI(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, double paramDouble) {
/*  61 */     this(paramArrayOfdouble1, paramArrayOfdouble2, paramDouble, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getPointEstimate() {
/*  70 */     if (this.method == 3) {
/*  71 */       return Math.exp(this.dpoint);
/*     */     }
/*  73 */     return Math.exp(MannWhitneyMedianDifferenceCI.getPointEstimate(this.xA, this.xB));
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
/*     */   public static double getPointEstimate(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
/*  87 */     return Math.exp(MannWhitneyMedianDifferenceCI.getPointEstimate(Arrays.log(paramArrayOfdouble1), Arrays.log(paramArrayOfdouble2)));
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
/* 129 */       byte b2 = 25;
/* 130 */       int i = b2 + 1;
/* 131 */       double[] arrayOfDouble1 = new double[b2];
/* 132 */       double[] arrayOfDouble2 = new double[i];
/*     */ 
/*     */ 
/*     */       
/* 136 */       double d = 1.0D;
/* 137 */       Gamma gamma1 = new Gamma(d, 2.0D);
/* 138 */       Gamma gamma2 = new Gamma(d, 1.0D);
/* 139 */       gamma1.setSeed(123L);
/* 140 */       gamma2.setSeed(321L); byte b1;
/* 141 */       for (b1 = 0; b1 < b2; ) { arrayOfDouble1[b1] = gamma1.random(); b1++; }
/* 142 */        for (b1 = 0; b1 < i; ) { arrayOfDouble2[b1] = gamma2.random(); b1++; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 149 */       long l1 = System.currentTimeMillis();
/* 150 */       RatioScaleCI ratioScaleCI = new RatioScaleCI(arrayOfDouble1, arrayOfDouble2, 0.95D, 3);
/* 151 */       long l2 = System.currentTimeMillis();
/* 152 */       System.out.println("n = " + b2 + " Time = " + ((l2 - l1) / 1000L) + " secs");
/* 153 */       System.out.println("  Fast CI=[" + ratioScaleCI.getLowerLimit() + "," + ratioScaleCI.getUpperLimit() + "]" + " d = " + ratioScaleCI.getD() + " Point estimate = " + ratioScaleCI.getPointEstimate() + " Achieved conf = " + ratioScaleCI.getAchievedConfidence());
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 158 */       ratioScaleCI = new RatioScaleCI(arrayOfDouble1, arrayOfDouble2, 0.95D, 2);
/* 159 */       l2 = System.currentTimeMillis();
/* 160 */       System.out.println("n = " + b2 + " Time = " + ((l2 - l1) / 1000L) + " secs");
/* 161 */       System.out.println("Approx CI=[" + ratioScaleCI.getLowerLimit() + "," + ratioScaleCI.getUpperLimit() + "]" + " d = " + ratioScaleCI.getD() + " Point estimate = " + ratioScaleCI.getPointEstimate() + " Achieved conf = " + ratioScaleCI.getAchievedConfidence());
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 166 */       ratioScaleCI = new RatioScaleCI(arrayOfDouble1, arrayOfDouble2, 0.95D, 1);
/* 167 */       l2 = System.currentTimeMillis();
/* 168 */       System.out.println("n = " + b2 + " Time = " + ((l2 - l1) / 1000L) + " secs");
/* 169 */       System.out.println(" Exact CI=[" + ratioScaleCI.getLowerLimit() + "," + ratioScaleCI.getUpperLimit() + "]" + " d = " + ratioScaleCI.getD() + " Point estimate = " + ratioScaleCI.getPointEstimate() + " Achieved conf = " + ratioScaleCI.getAchievedConfidence());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/independentsamples/RatioScaleCI.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */