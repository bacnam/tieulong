/*     */ package jsc.correlation;
/*     */ 
/*     */ import jsc.ci.ConfidenceInterval;
/*     */ import jsc.datastructures.PairedData;
/*     */ import jsc.distributions.Normal;
/*     */ import jsc.distributions.StudentsT;
/*     */ import jsc.onesample.Ztest;
/*     */ import jsc.tests.H1;
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
/*     */ public class LinearCorrelation
/*     */   implements SignificanceTest, ConfidenceInterval
/*     */ {
/*     */   private final int n;
/*     */   private final double r;
/*     */   private double confidenceCoeff;
/*     */   private double lowerLimit;
/*     */   private double upperLimit;
/*     */   private double SP;
/*     */   
/*     */   public LinearCorrelation(PairedData paramPairedData, double paramDouble1, H1 paramH1, double paramDouble2) {
/*  47 */     this.n = paramPairedData.getN();
/*  48 */     this.r = correlationCoeff(paramPairedData);
/*     */     
/*  50 */     if (paramDouble1 == 0.0D) {
/*     */ 
/*     */       
/*  53 */       double d1, d2 = 0.0D;
/*  54 */       if (this.r == 1.0D) {
/*  55 */         d1 = Double.POSITIVE_INFINITY;
/*  56 */       } else if (this.r == -1.0D) {
/*  57 */         d1 = Double.NEGATIVE_INFINITY;
/*     */       } else {
/*     */         
/*  60 */         d1 = this.r * Math.sqrt((this.n - 2.0D) / (1.0D - this.r * this.r));
/*  61 */         d2 = StudentsT.tailProb(d1, (this.n - 2));
/*     */       } 
/*     */       
/*  64 */       if (paramH1 == H1.NOT_EQUAL) {
/*  65 */         this.SP = d2 + d2;
/*  66 */       } else if (paramH1 == H1.LESS_THAN) {
/*  67 */         this.SP = (d1 < 0.0D) ? d2 : (1.0D - d2);
/*     */       } else {
/*  69 */         this.SP = (d1 > 0.0D) ? d2 : (1.0D - d2);
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/*  75 */       if (paramDouble1 <= -1.0D || paramDouble1 >= 1.0D) {
/*  76 */         throw new IllegalArgumentException("Invalid null hypothesis.");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  83 */       if (Math.abs(this.r) < 1.0D) {
/*     */         
/*  85 */         if (this.n < 4)
/*  86 */           throw new IllegalArgumentException("Need more than 3 observations to use Fisher's z transformation."); 
/*  87 */         double d = 0.5D * Math.sqrt(this.n - 3.0D) * Math.log((1.0D + this.r) / (1.0D - this.r) * (1.0D + paramDouble1) / (1.0D - paramDouble1));
/*     */         
/*  89 */         this.SP = Ztest.getSP(d, paramH1);
/*     */       } else {
/*     */         double d;
/*     */         
/*  93 */         if (this.r == 1.0D) {
/*  94 */           d = Double.POSITIVE_INFINITY;
/*     */         } else {
/*  96 */           d = Double.NEGATIVE_INFINITY;
/*  97 */         }  if (paramH1 == H1.NOT_EQUAL) {
/*  98 */           this.SP = 0.0D;
/*  99 */         } else if (paramH1 == H1.LESS_THAN) {
/* 100 */           this.SP = (d < 0.0D) ? 0.0D : 1.0D;
/*     */         } else {
/* 102 */           this.SP = (d > 0.0D) ? 0.0D : 1.0D;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 108 */     setConfidenceCoeff(paramDouble2);
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
/*     */   public LinearCorrelation(PairedData paramPairedData, double paramDouble, H1 paramH1) {
/* 122 */     this(paramPairedData, paramDouble, paramH1, 0.95D);
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
/*     */   public LinearCorrelation(PairedData paramPairedData, double paramDouble) {
/* 135 */     this(paramPairedData, paramDouble, H1.NOT_EQUAL, 0.95D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinearCorrelation(PairedData paramPairedData) {
/* 146 */     this(paramPairedData, 0.0D, H1.NOT_EQUAL, 0.95D);
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
/*     */   public static double correlationCoeff(PairedData paramPairedData) {
/* 159 */     int i = paramPairedData.getN();
/* 160 */     double[] arrayOfDouble1 = paramPairedData.getX();
/* 161 */     double[] arrayOfDouble2 = paramPairedData.getY();
/*     */ 
/*     */ 
/*     */     
/* 165 */     double d1 = 0.0D, d2 = 0.0D, d3 = 0.0D, d4 = 0.0D, d5 = 0.0D;
/*     */     byte b;
/* 167 */     for (b = 0; b < i; b++) {
/* 168 */       d5 += arrayOfDouble1[b]; d4 += arrayOfDouble2[b];
/* 169 */     }  d5 /= i;
/* 170 */     d4 /= i;
/* 171 */     for (b = 0; b < i; b++) {
/*     */       
/* 173 */       double d6 = arrayOfDouble1[b] - d5;
/* 174 */       double d7 = arrayOfDouble2[b] - d4;
/* 175 */       d3 += d6 * d6;
/* 176 */       d1 += d7 * d7;
/* 177 */       d2 += d6 * d7;
/*     */     } 
/*     */ 
/*     */     
/* 181 */     if (d3 <= 0.0D)
/* 182 */       throw new IllegalArgumentException("X data are constant."); 
/* 183 */     if (d1 <= 0.0D)
/* 184 */       throw new IllegalArgumentException("Y data are constant."); 
/* 185 */     return d2 / Math.sqrt(d3 * d1);
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
/*     */   public int getN() {
/* 206 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getR() {
/* 213 */     return this.r;
/*     */   } public double getSP() {
/* 215 */     return this.SP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTestStatistic() {
/* 222 */     return this.r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getLowerLimit() {
/* 229 */     return this.lowerLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getUpperLimit() {
/* 236 */     return this.upperLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getConfidenceCoeff() {
/* 243 */     return this.confidenceCoeff;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConfidenceCoeff(double paramDouble) {
/* 254 */     if (paramDouble < 0.0D || paramDouble > 1.0D)
/* 255 */       throw new IllegalArgumentException("Invalid confidence coefficient."); 
/* 256 */     this.confidenceCoeff = paramDouble;
/* 257 */     double d1 = 0.5D * Math.log((1.0D + this.r) / (1.0D - this.r));
/* 258 */     Normal normal = new Normal();
/* 259 */     double d2 = 1.0D - paramDouble;
/* 260 */     double d3 = normal.inverseCdf(1.0D - 0.5D * d2);
/* 261 */     if (this.n < 4)
/* 262 */       throw new IllegalArgumentException("Need more than 3 observations to calculate confidence interval."); 
/* 263 */     double d4 = d3 / Math.sqrt(this.n - 3.0D);
/*     */     
/* 265 */     double d5 = Math.exp(2.0D * (d1 - d4));
/* 266 */     double d6 = Math.exp(2.0D * (d1 + d4));
/* 267 */     this.lowerLimit = (d5 - 1.0D) / (d5 + 1.0D);
/* 268 */     this.upperLimit = (d6 - 1.0D) / (d6 + 1.0D);
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 282 */       double[] arrayOfDouble1 = { 8.000001D, 8.000003D, 8.000002D, 8.000004D, 8.000005D };
/* 283 */       double[] arrayOfDouble2 = { 8.0D, 9.0D, 10.0D, 11.0D, 12.0D };
/*     */       
/* 285 */       double[] arrayOfDouble3 = { 0.8D, 1.7D, 2.4D, 0.9D, 1.2D, 1.6D, 1.7D, 2.9D };
/* 286 */       double[] arrayOfDouble4 = { 1.3D, 3.3D, 3.8D, 1.1D, 2.4D, 3.1D, 3.5D, 3.9D };
/*     */       
/* 288 */       LinearCorrelation linearCorrelation = new LinearCorrelation(new PairedData(arrayOfDouble3, arrayOfDouble4), 0.0D, H1.GREATER_THAN, 0.95D);
/* 289 */       System.out.println("n = " + linearCorrelation.getN() + " r = " + linearCorrelation.getR() + " SP = " + linearCorrelation.getSP());
/* 290 */       System.out.println("CI = [" + linearCorrelation.getLowerLimit() + ", " + linearCorrelation.getUpperLimit() + "]");
/* 291 */       linearCorrelation = new LinearCorrelation(new PairedData(arrayOfDouble3, arrayOfDouble4), 0.0D, H1.NOT_EQUAL, 0.95D);
/* 292 */       System.out.println("n = " + linearCorrelation.getN() + " r = " + linearCorrelation.getR() + " SP = " + linearCorrelation.getSP());
/* 293 */       System.out.println("CI = [" + linearCorrelation.getLowerLimit() + ", " + linearCorrelation.getUpperLimit() + "]");
/*     */       
/* 295 */       double[] arrayOfDouble5 = { 8.2D, 9.6D, 7.0D, 9.4D, 10.9D, 7.1D, 9.0D, 6.6D, 8.4D, 10.5D };
/* 296 */       double[] arrayOfDouble6 = { 8.7D, 9.6D, 6.9D, 8.5D, 11.3D, 7.6D, 9.2D, 6.3D, 8.4D, 12.3D };
/* 297 */       linearCorrelation = new LinearCorrelation(new PairedData(arrayOfDouble5, arrayOfDouble6), 0.0D, H1.NOT_EQUAL, 0.95D);
/* 298 */       System.out.println("n = " + linearCorrelation.getN() + " r = " + linearCorrelation.getR() + " SP = " + linearCorrelation.getSP());
/* 299 */       System.out.println("CI = [" + linearCorrelation.getLowerLimit() + ", " + linearCorrelation.getUpperLimit() + "]");
/*     */       
/* 301 */       double[] arrayOfDouble7 = { 0.0D, 4.0D, 6.0D, 8.0D, 12.0D, 14.0D, 16.0D, 22.0D, 26.0D };
/* 302 */       double[] arrayOfDouble8 = { 11.0D, 13.0D, 8.0D, 4.0D, 7.0D, 6.0D, 3.0D, 2.0D, 0.0D };
/* 303 */       linearCorrelation = new LinearCorrelation(new PairedData(arrayOfDouble7, arrayOfDouble8), 0.0D, H1.NOT_EQUAL, 0.99D);
/* 304 */       System.out.println("n = " + linearCorrelation.getN() + " r = " + linearCorrelation.getR() + " SP = " + linearCorrelation.getSP());
/* 305 */       System.out.println("CI = [" + linearCorrelation.getLowerLimit() + ", " + linearCorrelation.getUpperLimit() + "]");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/correlation/LinearCorrelation.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */