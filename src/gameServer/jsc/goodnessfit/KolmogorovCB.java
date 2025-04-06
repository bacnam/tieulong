/*     */ package jsc.goodnessfit;
/*     */ 
/*     */ import jsc.ci.ConfidenceBand;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class KolmogorovCB
/*     */   implements ConfidenceBand
/*     */ {
/*     */   static final double TOLERANCE = 5.0E-15D;
/*     */   private int n;
/*     */   private SampleDistributionFunction sdf;
/*     */   private boolean approx;
/*     */   private double confidenceCoeff;
/*     */   private double criticalValue;
/*     */   
/*     */   public KolmogorovCB(SampleDistributionFunction paramSampleDistributionFunction, double paramDouble, boolean paramBoolean) {
/*  56 */     this.approx = paramBoolean;
/*  57 */     this.sdf = paramSampleDistributionFunction;
/*  58 */     this.n = paramSampleDistributionFunction.getN();
/*  59 */     setConfidenceCoeff(paramDouble);
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
/*     */   public KolmogorovCB(double[] paramArrayOfdouble, double paramDouble, boolean paramBoolean) {
/*  72 */     this(new SampleDistributionFunction(paramArrayOfdouble), paramDouble, paramBoolean);
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
/*     */   public KolmogorovCB(double[] paramArrayOfdouble) {
/*  84 */     this(paramArrayOfdouble, 0.95D, (paramArrayOfdouble.length <= 100));
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
/*     */   public static double approxCriticalValue(int paramInt, double paramDouble) {
/* 104 */     if (paramInt < 1) throw new IllegalArgumentException("n < 1."); 
/* 105 */     if (paramDouble < 0.0D || paramDouble > 1.0D) throw new IllegalArgumentException("Invalid alpha."); 
/* 106 */     if (paramDouble == 0.0D) return 1.0D; 
/* 107 */     if (paramDouble == 1.0D) return 0.0D; 
/* 108 */     if (paramInt == 1) return 1.0D - paramDouble; 
/* 109 */     double d1 = Math.sqrt(Math.log(1.0D / paramDouble) / (2 * paramInt));
/* 110 */     double d4 = Maths.log10(paramDouble);
/* 111 */     double d3 = 0.09037D * Math.pow(-d4, 1.5D) + 0.01515D * d4 * d4 - 0.11143D;
/* 112 */     double d2 = d1 - 0.16693D / paramInt - d3 / Math.pow(paramInt, 1.5D);
/* 113 */     if (d2 < 0.0D) return 0.0D; 
/* 114 */     if (d2 > 1.0D) return 1.0D; 
/* 115 */     return d2;
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
/*     */   public static double exactCriticalValue(int paramInt, double paramDouble) {
/* 136 */     double d4, d2 = paramInt;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     if (paramInt < 1) throw new IllegalArgumentException("n < 1."); 
/* 143 */     if (paramDouble < 0.0D || paramDouble > 1.0D) throw new IllegalArgumentException("Invalid alpha."); 
/* 144 */     if (paramDouble == 0.0D) return 1.0D; 
/* 145 */     if (paramDouble == 0.0D) return 0.0D; 
/* 146 */     if (paramInt == 1) return 1.0D - paramDouble; 
/* 147 */     double d1 = Maths.logFactorial(paramInt);
/*     */ 
/*     */     
/* 150 */     double d3 = approxCriticalValue(paramInt, paramDouble);
/*     */ 
/*     */ 
/*     */     
/* 154 */     if (d3 == 1.0D) { d3 = 0.5D; }
/* 155 */     else if (d3 == 0.0D) { d3 = (paramDouble > 0.99D) ? (1.0D - paramDouble) : 0.5D; }
/*     */      do {
/* 157 */       double d5 = 0.0D, d6 = 0.0D;
/* 158 */       for (byte b = 0; b < paramInt * (1.0D - d3) && b < paramInt; b++) {
/*     */         
/* 160 */         double d10 = b / d2;
/* 161 */         double d9 = Math.exp(d1 - Maths.logFactorial(b) - Maths.logFactorial((paramInt - b)) + (paramInt - b) * Math.log(1.0D - d3 - d10) + (b - 1) * Math.log(d3 + d10));
/*     */ 
/*     */         
/* 164 */         d5 += d9;
/* 165 */         d6 += (1.0D / d3 + (b - 1.0D) / (d3 + d10) - (paramInt - b) / (1.0D - d3 - d10)) * d9;
/*     */       } 
/*     */       
/* 168 */       double d7 = paramDouble - d3 * d5;
/* 169 */       double d8 = -d3 * d6;
/* 170 */       if (d8 == 0.0D) {
/* 171 */         throw new ArithmeticException("Zero derivative.");
/*     */       }
/* 173 */       d4 = d3;
/* 174 */       d3 -= d7 / d8;
/* 175 */       if (d3 < 0.0D || d3 > 1.0D) return d4;
/*     */     
/* 177 */     } while (Math.abs(d3 - d4) >= 5.0E-15D);
/*     */ 
/*     */     
/* 180 */     if (d3 < 0.0D) return 0.0D; 
/* 181 */     if (d3 > 1.0D) return 1.0D; 
/* 182 */     return d3;
/*     */   }
/*     */   public double getConfidenceCoeff() {
/* 185 */     return this.confidenceCoeff;
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
/*     */   public void setConfidenceCoeff(double paramDouble) {
/* 197 */     if (paramDouble <= 0.0D || paramDouble >= 1.0D)
/* 198 */       throw new IllegalArgumentException("Invalid confidence coefficient."); 
/* 199 */     this.confidenceCoeff = paramDouble;
/* 200 */     double d = 0.5D - 0.5D * paramDouble;
/* 201 */     if (this.approx) {
/* 202 */       this.criticalValue = approxCriticalValue(this.n, d);
/*     */     } else {
/* 204 */       this.criticalValue = exactCriticalValue(this.n, d);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getCriticalValue() {
/* 213 */     return this.criticalValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getLowerLimit(int paramInt) {
/* 218 */     return this.sdf.getOrderedS(paramInt) - this.criticalValue;
/*     */   }
/*     */   
/*     */   public double getUpperLimit(int paramInt) {
/* 222 */     return this.sdf.getOrderedS(paramInt) + this.criticalValue;
/*     */   }
/*     */   public int getN() {
/* 225 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SampleDistributionFunction getSdf() {
/* 232 */     return this.sdf;
/*     */   } public double getX(int paramInt) {
/* 234 */     return this.sdf.getOrderedX(paramInt);
/*     */   }
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
/* 247 */       double[] arrayOfDouble = { 13.3D, 14.6D, 13.6D, 17.2D, 14.1D, 10.6D, 15.9D, 14.7D, 14.2D, 14.0D, 17.4D, 15.6D, 8.2D, 13.8D, 15.4D, 16.3D, 17.7D, 15.0D, 13.4D, 13.4D, 16.0D, 13.3D, 14.9D, 12.9D, 14.0D, 16.2D, 11.5D, 10.4D, 12.6D, 18.1D };
/*     */ 
/*     */       
/* 250 */       KolmogorovCB kolmogorovCB = new KolmogorovCB(arrayOfDouble);
/*     */       
/* 252 */       for (byte b = 0; b < kolmogorovCB.getN(); b++)
/* 253 */         System.out.println("x = " + kolmogorovCB.getX(b) + " [" + kolmogorovCB.getLowerLimit(b) + ", " + kolmogorovCB.getUpperLimit(b) + "]"); 
/* 254 */       System.out.println("n = " + kolmogorovCB.getN() + " D = " + kolmogorovCB.getCriticalValue());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/goodnessfit/KolmogorovCB.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */