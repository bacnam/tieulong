/*     */ package jsc.onesample;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import jsc.ci.AbstractConfidenceInterval;
/*     */ import jsc.datastructures.PairedData;
/*     */ import jsc.distributions.Binomial;
/*     */ import jsc.util.Maths;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SignMedianCI
/*     */   extends AbstractConfidenceInterval
/*     */ {
/*     */   int d;
/*     */   double achievedConfidence;
/*     */   double median;
/*     */   
/*     */   public SignMedianCI(double[] paramArrayOfdouble, double paramDouble) {
/*  48 */     super(paramDouble);
/*     */     
/*  50 */     int i = paramArrayOfdouble.length;
/*  51 */     if (i < 2) {
/*  52 */       throw new IllegalArgumentException("Less than two values.");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  59 */     Binomial binomial = new Binomial(i, 0.5D);
/*  60 */     this.d = (int)binomial.inverseCdf(0.5D - paramDouble / 2.0D);
/*     */ 
/*     */ 
/*     */     
/*  64 */     if (this.d < 1 || this.d > i)
/*  65 */       throw new IllegalArgumentException("Cannot calculate interval."); 
/*  66 */     double d = binomial.cdf((this.d - 1));
/*     */     
/*  68 */     double[] arrayOfDouble = new double[i];
/*  69 */     System.arraycopy(paramArrayOfdouble, 0, arrayOfDouble, 0, i);
/*  70 */     Arrays.sort(arrayOfDouble);
/*  71 */     this.lowerLimit = arrayOfDouble[this.d - 1];
/*  72 */     this.upperLimit = arrayOfDouble[i - this.d];
/*     */     
/*  74 */     int j = i / 2;
/*  75 */     if (i % 2 == 0) {
/*  76 */       this.median = (arrayOfDouble[j - 1] + arrayOfDouble[j]) / 2.0D;
/*     */     } else {
/*  78 */       this.median = arrayOfDouble[j];
/*  79 */     }  this.achievedConfidence = 1.0D - 2.0D * d;
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
/*     */   public SignMedianCI(PairedData paramPairedData, double paramDouble) {
/*  99 */     this(paramPairedData.differences(), paramDouble);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getAchievedConfidence() {
/* 110 */     return this.achievedConfidence;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getD() {
/* 118 */     return this.d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMedian() {
/* 125 */     return this.median;
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 143 */       long l1 = System.currentTimeMillis();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 150 */       double[] arrayOfDouble1 = { 5.0D, 3.0D, -1.0D, 14.0D, 7.0D, -4.0D, 11.0D, 10.0D };
/*     */       
/* 152 */       int i = arrayOfDouble1.length;
/* 153 */       SignMedianCI signMedianCI = new SignMedianCI(arrayOfDouble1, 0.92D);
/* 154 */       long l2 = System.currentTimeMillis();
/* 155 */       System.out.println("n = " + i + " Time = " + ((l2 - l1) / 1000L) + " secs");
/* 156 */       System.out.println("CI = [" + signMedianCI.getLowerLimit() + ", " + signMedianCI.getUpperLimit() + "]" + " d = " + signMedianCI.getD() + " Median = " + signMedianCI.getMedian() + " Achieved confidence = " + signMedianCI.getAchievedConfidence());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 165 */       double[] arrayOfDouble2 = { 51.0D, 48.0D, 52.0D, 62.0D, 64.0D, 51.0D, 55.0D, 60.0D };
/* 166 */       double[] arrayOfDouble3 = { 46.0D, 45.0D, 53.0D, 48.0D, 57.0D, 55.0D, 44.0D, 50.0D };
/* 167 */       double[] arrayOfDouble4 = { 0.89D, 0.9D, 0.92D, 0.94D, 0.95D, 0.96D, 0.98D, 0.99D };
/* 168 */       PairedData pairedData = new PairedData(arrayOfDouble2, arrayOfDouble3);
/* 169 */       System.out.println("************* Suntan lotions: n = 8 *******************"); byte b;
/* 170 */       for (b = 0; b < arrayOfDouble4.length; b++) {
/*     */         
/* 172 */         System.out.println("Nominal confidence coeff. = " + arrayOfDouble4[b]);
/* 173 */         signMedianCI = new SignMedianCI(pairedData, arrayOfDouble4[b]);
/* 174 */         System.out.println("Exact CI=[" + signMedianCI.getLowerLimit() + "," + signMedianCI.getUpperLimit() + "]" + " d=" + signMedianCI.getD() + " Achieved conf.=" + Maths.round(signMedianCI.getAchievedConfidence(), 3));
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 181 */       double[] arrayOfDouble5 = { 54.5D, 70.6D, 85.6D, 78.2D, 69.6D, 73.1D, 97.5D, 85.6D, 74.9D, 86.8D, 53.6D, 89.4D };
/* 182 */       double[] arrayOfDouble6 = { 55.5D, 72.9D, 84.8D, 78.3D, 71.6D, 74.0D, 97.2D, 88.0D, 74.4D, 89.3D, 52.3D, 91.5D };
/* 183 */       pairedData = new PairedData(arrayOfDouble5, arrayOfDouble6);
/* 184 */       System.out.println("*************** Weight change data: n = 12 ***************");
/* 185 */       for (b = 0; b < arrayOfDouble4.length; b++) {
/*     */         
/* 187 */         System.out.println("Nominal confidence coeff. = " + arrayOfDouble4[b]);
/* 188 */         signMedianCI = new SignMedianCI(pairedData, arrayOfDouble4[b]);
/* 189 */         System.out.println(" Exact CI=[" + signMedianCI.getLowerLimit() + "," + signMedianCI.getUpperLimit() + "]" + " d=" + signMedianCI.getD() + " Achieved conf.=" + Maths.round(signMedianCI.getAchievedConfidence(), 3));
/*     */         
/* 191 */         signMedianCI = new SignMedianCI(pairedData, arrayOfDouble4[b]);
/* 192 */         System.out.println("Approx CI=[" + signMedianCI.getLowerLimit() + "," + signMedianCI.getUpperLimit() + "]" + " d=" + signMedianCI.getD() + " Achieved conf.=" + Maths.round(signMedianCI.getAchievedConfidence(), 3));
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/onesample/SignMedianCI.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */