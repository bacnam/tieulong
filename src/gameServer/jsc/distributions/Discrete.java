/*     */ package jsc.distributions;
/*     */ 
/*     */ import jsc.descriptive.DoubleTally;
/*     */ import jsc.goodnessfit.ChiSquaredFitTest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Discrete
/*     */   extends AbstractDistribution
/*     */ {
/*     */   protected double minValue;
/*     */   protected double maxValue;
/*     */   protected int valueCount;
/*     */   protected double[] values;
/*     */   protected double[] probs;
/*     */   
/*     */   public Discrete() {
/*  37 */     this.valueCount = 0;
/*  38 */     this.values = new double[0];
/*  39 */     this.probs = new double[0];
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
/*     */   public Discrete(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
/*  52 */     this(paramArrayOfdouble1, paramArrayOfdouble2, false, 1.0E-6D);
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
/*     */   public Discrete(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, boolean paramBoolean, double paramDouble) {
/*  72 */     setDistribution(paramArrayOfdouble1, paramArrayOfdouble2, paramBoolean, paramDouble);
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
/*     */   public Discrete(double[] paramArrayOfdouble, double paramDouble) {
/*  87 */     setDistribution(paramArrayOfdouble, paramDouble);
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
/*     */   public Discrete(double[] paramArrayOfdouble) {
/*  99 */     setDistribution(paramArrayOfdouble, 0.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Discrete(DoubleTally paramDoubleTally) {
/* 109 */     setDistribution(paramDoubleTally);
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
/*     */   public double cdf(double paramDouble) {
/* 121 */     if (paramDouble < this.values[0] || paramDouble > this.values[this.valueCount - 1])
/*     */     {
/* 123 */       throw new IllegalArgumentException("Invalid variate-value."); } 
/* 124 */     double d = 0.0D;
/* 125 */     for (byte b = 0; b < this.valueCount && 
/* 126 */       this.values[b] <= paramDouble; b++) {
/* 127 */       d += this.probs[b];
/*     */     }
/*     */ 
/*     */     
/* 131 */     if (d < 0.0D) return 0.0D; 
/* 132 */     if (d > 1.0D) return 1.0D; 
/* 133 */     return d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMaxValue() {
/* 141 */     return this.maxValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMinValue() {
/* 148 */     return this.minValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getProb(int paramInt) {
/* 157 */     return this.probs[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getValue(int paramInt) {
/* 166 */     return this.values[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getValueCount() {
/* 173 */     return this.valueCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double inverseCdf(double paramDouble) {
/* 184 */     if (paramDouble < 0.0D || paramDouble > 1.0D)
/* 185 */       throw new IllegalArgumentException("Invalid probability."); 
/* 186 */     double d = 0.0D;
/*     */     
/* 188 */     if (paramDouble == 1.0D) return this.values[this.valueCount - 1]; 
/*     */     byte b;
/* 190 */     for (b = 0; b < this.valueCount; b++) {
/*     */       
/* 192 */       d += this.probs[b];
/* 193 */       if (d >= paramDouble)
/*     */         break; 
/* 195 */     }  return this.values[b];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDiscrete() {
/* 203 */     return true;
/*     */   }
/*     */   
/*     */   public double mean() {
/* 207 */     double d = 0.0D;
/* 208 */     for (byte b = 0; b < this.valueCount; b++)
/* 209 */       d += this.values[b] * this.probs[b]; 
/* 210 */     return d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double moment(int paramInt) {
/* 219 */     return moment(paramInt, 0.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double moment(int paramInt, double paramDouble) {
/* 230 */     if (paramInt < 0)
/* 231 */       throw new IllegalArgumentException("Invalid moment order."); 
/* 232 */     double d = 0.0D;
/* 233 */     for (byte b = 0; b < this.valueCount; b++)
/* 234 */       d += Math.pow(this.values[b] - paramDouble, paramInt) * this.probs[b]; 
/* 235 */     return d;
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
/*     */   public double pdf(double paramDouble) {
/* 248 */     for (byte b = 0; b < this.valueCount; b++) {
/* 249 */       if (paramDouble == this.values[b]) return this.probs[b]; 
/* 250 */     }  throw new IllegalArgumentException("Invalid variate-value.");
/*     */   }
/*     */ 
/*     */   
/*     */   public double random() {
/* 255 */     double d1 = 0.0D;
/* 256 */     double d2 = this.rand.nextDouble();
/* 257 */     for (byte b = 0; b < this.valueCount; b++) {
/*     */       
/* 259 */       d1 += this.probs[b];
/* 260 */       if (d2 < d1) return this.values[b]; 
/*     */     } 
/* 262 */     return this.values[this.valueCount - 1];
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
/*     */   public void setDistribution(double[] paramArrayOfdouble, double paramDouble) {
/* 279 */     setDistribution(new DoubleTally(paramArrayOfdouble, paramDouble));
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
/*     */   public void setDistribution(DoubleTally paramDoubleTally) {
/* 291 */     this.valueCount = paramDoubleTally.getValueCount();
/* 292 */     this.values = new double[this.valueCount];
/* 293 */     this.probs = new double[this.valueCount];
/* 294 */     for (byte b = 0; b < this.valueCount; b++) {
/*     */       
/* 296 */       this.values[b] = paramDoubleTally.getValue(b);
/* 297 */       this.probs[b] = paramDoubleTally.getProportion(b);
/*     */     } 
/* 299 */     this.minValue = paramDoubleTally.getMin();
/* 300 */     this.maxValue = paramDoubleTally.getMax();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDistribution(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, boolean paramBoolean, double paramDouble) {
/* 361 */     this.valueCount = paramArrayOfdouble1.length;
/* 362 */     if (this.valueCount < 1 || this.valueCount != paramArrayOfdouble2.length)
/* 363 */       throw new IllegalArgumentException("Invalid distribution parameter."); 
/* 364 */     this.values = new double[this.valueCount];
/* 365 */     this.probs = new double[this.valueCount];
/* 366 */     System.arraycopy(paramArrayOfdouble1, 0, this.values, 0, this.valueCount);
/* 367 */     System.arraycopy(paramArrayOfdouble2, 0, this.probs, 0, this.valueCount);
/*     */ 
/*     */     
/* 370 */     double d1 = Double.NEGATIVE_INFINITY;
/* 371 */     double d2 = 0.0D;
/*     */     byte b;
/* 373 */     for (b = 0; b < this.valueCount; b++) {
/*     */       
/* 375 */       if (this.values[b] > d1) {
/* 376 */         d1 = this.values[b];
/*     */       } else {
/* 378 */         throw new IllegalArgumentException("Invalid values value: " + this.values[b]);
/* 379 */       }  if (this.probs[b] < 0.0D || this.probs[b] > 1.0D)
/* 380 */         throw new IllegalArgumentException("Invalid probability: " + this.probs[b]); 
/* 381 */       d2 += this.probs[b];
/*     */     } 
/* 383 */     if ((!paramBoolean && Math.abs(1.0D - d2) > paramDouble) || d2 <= 0.0D) {
/* 384 */       throw new IllegalArgumentException("Probabilities sum to " + d2);
/*     */     }
/* 386 */     if (paramBoolean && Math.abs(1.0D - d2) > paramDouble)
/* 387 */       for (b = 0; b < this.valueCount; ) { this.probs[b] = this.probs[b] / d2; b++; }
/*     */        
/* 389 */     this.minValue = this.values[0];
/* 390 */     this.maxValue = this.values[this.valueCount - 1];
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 395 */     StringBuffer stringBuffer = new StringBuffer();
/* 396 */     stringBuffer.append("Discrete distribution\nx\tp");
/* 397 */     for (byte b = 0; b < this.valueCount; b++)
/* 398 */       stringBuffer.append("\n" + this.values[b] + "\t" + this.probs[b]); 
/* 399 */     return stringBuffer.toString();
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
/*     */   public double upperTailProb(double paramDouble) {
/* 414 */     if (paramDouble < this.values[0] || paramDouble > this.values[this.valueCount - 1])
/* 415 */       throw new IllegalArgumentException("Invalid variate-value."); 
/* 416 */     double d = 0.0D;
/* 417 */     for (int i = this.valueCount - 1; i >= 0 && 
/* 418 */       this.values[i] >= paramDouble; i--) {
/* 419 */       d += this.probs[i];
/*     */     }
/*     */     
/* 422 */     return d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double variance() {
/* 432 */     return moment(2, mean());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 443 */       double d1 = 10.0D;
/* 444 */       double d2 = 0.5D;
/* 445 */       double[] arrayOfDouble1 = { 0.1D, 0.2D, 0.2D, 0.3D, 0.1D, 0.1D };
/* 446 */       double[] arrayOfDouble2 = { -2.0D, -1.0D, 0.0D, 2.0D, 5.0D, 10.0D };
/* 447 */       Discrete discrete = new Discrete(arrayOfDouble2, arrayOfDouble1);
/* 448 */       System.out.println(discrete.toString());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 466 */       char c = '✐';
/* 467 */       double[] arrayOfDouble3 = new double[c];
/* 468 */       for (byte b = 0; b < c; ) { arrayOfDouble3[b] = discrete.random(); b++; }
/* 469 */        ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new DoubleTally(arrayOfDouble3), discrete, 0);
/* 470 */       System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
/* 471 */       System.out.println("m = " + c + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/Discrete.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */