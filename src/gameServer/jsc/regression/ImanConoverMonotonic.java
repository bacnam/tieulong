/*     */ package jsc.regression;
/*     */ 
/*     */ import jsc.datastructures.PairedData;
/*     */ import jsc.util.Rank;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ImanConoverMonotonic
/*     */ {
/*     */   private final int n;
/*     */   private final double a;
/*     */   private final double b;
/*     */   private final double[] x;
/*     */   private final double[] y;
/*     */   private final double[] py;
/*     */   private final Rank rankX;
/*     */   private final Rank rankY;
/*     */   
/*     */   public ImanConoverMonotonic(PairedData paramPairedData) {
/*  41 */     this.n = paramPairedData.getN();
/*     */ 
/*     */     
/*  44 */     this.x = new double[this.n];
/*  45 */     this.y = new double[this.n];
/*  46 */     this.py = new double[this.n];
/*  47 */     System.arraycopy(paramPairedData.getX(), 0, this.x, 0, this.n);
/*  48 */     System.arraycopy(paramPairedData.getY(), 0, this.y, 0, this.n);
/*     */     
/*  50 */     this.rankX = new Rank(this.x, 0.0D);
/*  51 */     this.rankY = new Rank(this.y, 0.0D);
/*  52 */     double[] arrayOfDouble1 = this.rankX.getRanks();
/*  53 */     double[] arrayOfDouble2 = this.rankY.getRanks();
/*  54 */     int[] arrayOfInt = this.rankY.getSortIndexes();
/*     */ 
/*     */ 
/*     */     
/*  58 */     PearsonCorrelation pearsonCorrelation = new PearsonCorrelation(new PairedData(arrayOfDouble1, arrayOfDouble2));
/*  59 */     this.b = pearsonCorrelation.getR();
/*  60 */     this.a = 0.5D * (this.n + 1.0D) * (1.0D - this.b);
/*     */ 
/*     */     
/*  63 */     for (byte b = 0; b < this.n; b++) {
/*     */ 
/*     */       
/*  66 */       double d = this.b * arrayOfDouble1[b] + this.a;
/*     */ 
/*     */       
/*  69 */       this.py[b] = interpol(this.n, d, arrayOfDouble2, this.y, arrayOfInt);
/*     */     } 
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
/*     */   public int getN() {
/*  82 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] getPredY() {
/*  90 */     return this.py;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getPredY(double paramDouble) {
/* 101 */     double d1 = interpol(this.n, paramDouble, this.x, this.rankX.getRanks(), this.rankX.getSortIndexes());
/* 102 */     double d2 = this.b * d1 + this.a;
/* 103 */     return interpol(this.n, d2, this.rankY.getRanks(), this.y, this.rankY.getSortIndexes());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rank getRankX() {
/* 111 */     return this.rankX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rank getRankY() {
/* 118 */     return this.rankY;
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
/*     */   public double getSortedPredY(int paramInt) {
/* 135 */     return this.py[this.rankX.getSortIndex(paramInt)];
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
/*     */   public double getSortedResidual(int paramInt) {
/* 147 */     int i = this.rankX.getSortIndex(paramInt);
/* 148 */     return this.y[i] - this.py[i];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSortedX(int paramInt) {
/* 159 */     return this.x[this.rankX.getSortIndex(paramInt)];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] getX() {
/* 166 */     return this.x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] getY() {
/* 173 */     return this.y;
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
/*     */   public double getSpearmanCoeff() {
/* 193 */     return this.b;
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
/*     */   double interpol(int paramInt, double paramDouble, double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, int[] paramArrayOfint) {
/* 214 */     if (paramInt < 1) {
/* 215 */       throw new IllegalArgumentException("No data");
/*     */     }
/* 217 */     int i = paramArrayOfint[0], j = paramArrayOfint[paramInt - 1];
/* 218 */     if (paramDouble < paramArrayOfdouble1[i])
/* 219 */       return paramArrayOfdouble2[i]; 
/* 220 */     if (paramDouble > paramArrayOfdouble1[j])
/* 221 */       return paramArrayOfdouble2[j]; 
/* 222 */     if (paramDouble == paramArrayOfdouble1[i]) {
/* 223 */       return paramArrayOfdouble2[i];
/*     */     }
/* 225 */     for (byte b = 1; b < paramInt; b++) {
/*     */       
/* 227 */       i = paramArrayOfint[b - 1];
/* 228 */       j = paramArrayOfint[b];
/* 229 */       if (paramArrayOfdouble1[i] > paramArrayOfdouble1[j])
/*     */       {
/* 231 */         throw new IllegalArgumentException("X-values not in ascending order."); } 
/* 232 */       if (paramDouble <= paramArrayOfdouble1[j])
/*     */       {
/* 234 */         return paramArrayOfdouble2[i] + (paramArrayOfdouble2[j] - paramArrayOfdouble2[i]) * (paramDouble - paramArrayOfdouble1[i]) / (paramArrayOfdouble1[j] - paramArrayOfdouble1[i]);
/*     */       }
/*     */     } 
/* 237 */     return paramArrayOfdouble2[j];
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
/* 250 */       double[] arrayOfDouble1 = { 104.0D, 161.0D, 156.0D, 96.0D, 149.0D, 143.0D, 113.0D, 142.0D, 115.0D, 175.0D, 135.0D, 145.0D, 137.0D, 151.0D, 126.0D };
/* 251 */       double[] arrayOfDouble2 = { 25.0D, 47.0D, 40.0D, 17.0D, 49.0D, 39.0D, 33.0D, 30.0D, 31.0D, 44.0D, 34.0D, 43.0D, 35.0D, 42.0D, 36.0D };
/* 252 */       ImanConoverMonotonic imanConoverMonotonic = new ImanConoverMonotonic(new PairedData(arrayOfDouble1, arrayOfDouble2));
/*     */       
/* 254 */       System.out.println("n = " + imanConoverMonotonic.getN());
/* 255 */       System.out.println("Spearman coeff = " + imanConoverMonotonic.getSpearmanCoeff());
/* 256 */       for (byte b = 0; b < imanConoverMonotonic.getN(); b++)
/*     */       {
/*     */         
/* 259 */         System.out.println("X = " + imanConoverMonotonic.getSortedX(b) + " Y = " + imanConoverMonotonic.getSortedPredY(b));
/*     */       }
/* 261 */       double d = 120.0D;
/* 262 */       System.out.println("Estimated Y for x = " + d + " is " + imanConoverMonotonic.getPredY(d));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/regression/ImanConoverMonotonic.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */