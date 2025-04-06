/*     */ package jsc.goodnessfit;
/*     */ 
/*     */ import jsc.util.Arrays;
/*     */ import jsc.util.Sort;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SampleDistributionFunction
/*     */ {
/*     */   private int n;
/*     */   private double[] x;
/*     */   private double[] S;
/*     */   private double[] orderedX;
/*     */   private double[] orderedS;
/*     */   
/*     */   public SampleDistributionFunction(double[] paramArrayOfdouble) {
/*  50 */     this.n = paramArrayOfdouble.length;
/*  51 */     if (this.n < 2) {
/*  52 */       throw new IllegalArgumentException("Less than 2 observations.");
/*     */     }
/*  54 */     this.x = paramArrayOfdouble;
/*     */     
/*  56 */     this.orderedX = new double[this.n];
/*  57 */     this.S = new double[this.n];
/*  58 */     this.orderedS = new double[this.n];
/*  59 */     int[] arrayOfInt = Arrays.sequence(this.n);
/*  60 */     System.arraycopy(paramArrayOfdouble, 0, this.orderedX, 0, this.n);
/*     */ 
/*     */     
/*  63 */     Sort.sort(this.orderedX, arrayOfInt, 0, this.n - 1, true);
/*     */     
/*  65 */     this.orderedS[this.n - 1] = 1.0D; int i;
/*  66 */     for (i = this.n - 2; i >= 0; i--) {
/*  67 */       if (this.orderedX[i] == this.orderedX[i + 1]) {
/*  68 */         this.orderedS[i] = this.orderedS[i + 1];
/*     */       } else {
/*  70 */         this.orderedS[i] = (1.0D + i) / this.n;
/*     */       } 
/*     */     } 
/*  73 */     for (i = 0; i < this.n; ) { this.S[arrayOfInt[i]] = this.orderedS[i]; i++; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMinX() {
/*  83 */     return this.orderedX[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMaxX() {
/*  90 */     return this.orderedX[this.n - 1];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/*  97 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getS(int paramInt) {
/* 106 */     return this.S[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getX(int paramInt) {
/* 114 */     return this.x[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getOrderedS(int paramInt) {
/* 122 */     return this.orderedS[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getOrderedX(int paramInt) {
/* 131 */     return this.orderedX[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] getS() {
/* 139 */     return this.S;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] getX() {
/* 146 */     return this.x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] getOrderedS() {
/* 153 */     return this.orderedS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] getOrderedX() {
/* 161 */     return this.orderedX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 171 */     StringBuffer stringBuffer = new StringBuffer();
/* 172 */     stringBuffer.append("\nSample distribution function\n");
/* 173 */     stringBuffer.append("x\tS(x)");
/* 174 */     for (byte b = 0; b < getN(); b++)
/* 175 */       stringBuffer.append("\n" + getOrderedX(b) + "\t" + getOrderedS(b)); 
/* 176 */     stringBuffer.append("\n");
/* 177 */     return stringBuffer.toString();
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
/* 219 */       double[] arrayOfDouble = { 72.2D, 64.0D, 53.4D, 76.8D, 86.3D, 58.1D, 63.2D, 73.1D, 78.0D, 44.3D, 85.1D, 66.6D, 80.4D, 76.0D, 68.8D, 76.8D, 58.9D, 58.1D, 74.9D, 72.2D, 73.1D, 39.3D, 52.8D, 54.2D, 65.3D, 74.0D, 63.2D, 64.7D, 68.8D, 85.1D, 62.2D, 76.0D, 70.5D, 48.9D, 78.0D, 66.6D, 58.1D, 32.5D, 63.2D, 64.0D, 68.8D, 65.3D, 71.9D, 72.2D, 63.2D, 72.2D, 70.5D, 80.4D, 45.4D, 59.6D };
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 224 */       SampleDistributionFunction sampleDistributionFunction = new SampleDistributionFunction(arrayOfDouble);
/* 225 */       System.out.println(sampleDistributionFunction.toString());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/goodnessfit/SampleDistributionFunction.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */