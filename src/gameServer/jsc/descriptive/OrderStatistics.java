/*     */ package jsc.descriptive;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class OrderStatistics
/*     */   implements Cloneable
/*     */ {
/*     */   private final int n;
/*     */   private double maximum;
/*     */   private double minimum;
/*     */   private final double median;
/*     */   private final double lowerQuartile;
/*     */   private final double upperQuartile;
/*     */   
/*     */   public OrderStatistics(double[] paramArrayOfdouble) {
/*  39 */     this.n = paramArrayOfdouble.length;
/*  40 */     if (this.n < 1) {
/*  41 */       throw new IllegalArgumentException("No values");
/*     */     }
/*  43 */     this.minimum = paramArrayOfdouble[0];
/*  44 */     this.maximum = paramArrayOfdouble[this.n - 1];
/*  45 */     if (this.n == 1) {
/*  46 */       this.lowerQuartile = paramArrayOfdouble[0]; this.upperQuartile = paramArrayOfdouble[0]; this.median = paramArrayOfdouble[0];
/*  47 */     } else if (this.n == 2) {
/*     */       
/*  49 */       if (paramArrayOfdouble[0] > paramArrayOfdouble[1]) {
/*     */         
/*  51 */         paramArrayOfdouble[0] = this.maximum;
/*  52 */         paramArrayOfdouble[1] = this.minimum;
/*  53 */         this.minimum = paramArrayOfdouble[0];
/*  54 */         this.maximum = paramArrayOfdouble[this.n - 1];
/*     */       } 
/*  56 */       this.lowerQuartile = paramArrayOfdouble[0]; this.upperQuartile = paramArrayOfdouble[1]; this.median = (paramArrayOfdouble[0] + paramArrayOfdouble[1]) / 2.0D;
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/*  63 */       Arrays.sort(paramArrayOfdouble);
/*  64 */       this.minimum = paramArrayOfdouble[0];
/*  65 */       this.maximum = paramArrayOfdouble[this.n - 1];
/*     */ 
/*     */ 
/*     */       
/*  69 */       int j = (this.n + 1) % 4;
/*  70 */       if (j != 0) {
/*     */         
/*  72 */         int k = (this.n + 1) / 4 - 1;
/*  73 */         this.lowerQuartile = paramArrayOfdouble[k] + j * (paramArrayOfdouble[k + 1] - paramArrayOfdouble[k]) / 4.0D;
/*     */       } else {
/*     */         
/*  76 */         this.lowerQuartile = paramArrayOfdouble[(this.n + 1) / 4 - 1];
/*     */       } 
/*     */       
/*  79 */       j = 3 * (this.n + 1) % 4;
/*  80 */       if (j != 0) {
/*     */         
/*  82 */         int k = 3 * (this.n + 1) / 4 - 1;
/*  83 */         this.upperQuartile = paramArrayOfdouble[k] + j * (paramArrayOfdouble[k + 1] - paramArrayOfdouble[k]) / 4.0D;
/*     */       } else {
/*     */         
/*  86 */         this.upperQuartile = paramArrayOfdouble[3 * (this.n + 1) / 4 - 1];
/*     */       } 
/*     */       
/*  89 */       int i = this.n / 2;
/*  90 */       this.median = (this.n % 2 != 0) ? paramArrayOfdouble[j] : (0.5D * (paramArrayOfdouble[i - 1] + paramArrayOfdouble[j]));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/* 101 */     Object object = null; try {
/* 102 */       object = super.clone();
/*     */     } catch (CloneNotSupportedException cloneNotSupportedException) {
/* 104 */       System.out.println("OrderStatistics can't clone");
/* 105 */     }  return object;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getLowerQuartile() {
/* 171 */     return this.lowerQuartile;
/*     */   }
/*     */   public double getMaximum() {
/* 174 */     return this.maximum;
/*     */   }
/*     */   public double getMedian() {
/* 177 */     return this.median;
/*     */   }
/*     */   public double getMinimum() {
/* 180 */     return this.minimum;
/*     */   }
/*     */   public int getN() {
/* 183 */     return this.n;
/*     */   }
/*     */   public double getUpperQuartile() {
/* 186 */     return this.upperQuartile;
/*     */   }
/*     */   public double getInterquartileRange() {
/* 189 */     return this.upperQuartile - this.lowerQuartile;
/*     */   }
/*     */   public double getRange() {
/* 192 */     return this.maximum - this.minimum;
/*     */   }
/*     */   
/*     */   private static void swap(double paramDouble1, double paramDouble2) {
/* 196 */     double d = paramDouble1;
/* 197 */     paramDouble1 = paramDouble2; paramDouble2 = d;
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
/* 208 */       double[] arrayOfDouble = { 73.0D, 62.7D, 59.3D, 68.2D, 63.9D };
/*     */ 
/*     */       
/* 211 */       byte b = 2;
/*     */ 
/*     */ 
/*     */       
/* 215 */       OrderStatistics orderStatistics = new OrderStatistics(arrayOfDouble);
/* 216 */       System.out.println("N = " + orderStatistics.getN());
/* 217 */       System.out.println("Median = " + orderStatistics.getMedian());
/* 218 */       System.out.println("Minimum = " + orderStatistics.getMinimum());
/* 219 */       System.out.println("Maximum = " + orderStatistics.getMaximum());
/* 220 */       System.out.println("Lower quartile = " + orderStatistics.getLowerQuartile());
/* 221 */       System.out.println("Upper quartile = " + orderStatistics.getUpperQuartile());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/descriptive/OrderStatistics.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */