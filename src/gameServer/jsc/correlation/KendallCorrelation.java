/*     */ package jsc.correlation;
/*     */ 
/*     */ import jsc.datastructures.PairedData;
/*     */ import jsc.distributions.Normal;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class KendallCorrelation
/*     */   implements SignificanceTest
/*     */ {
/*     */   private final int n;
/*     */   private final double r;
/*     */   private int T;
/*     */   private double SP;
/*     */   
/*     */   public KendallCorrelation(PairedData paramPairedData, H1 paramH1, double paramDouble) {
/*  45 */     byte b1 = 0, b2 = 0;
/*     */ 
/*     */ 
/*     */     
/*  49 */     this.n = paramPairedData.getN();
/*  50 */     double[] arrayOfDouble1 = paramPairedData.getX();
/*  51 */     double[] arrayOfDouble2 = paramPairedData.getY();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  56 */     this.T = 0;
/*  57 */     for (byte b3 = 0; b3 < this.n - 1; b3++) {
/*  58 */       for (int i = b3 + 1; i < this.n; i++) {
/*     */         
/*  60 */         double d3 = arrayOfDouble1[b3] - arrayOfDouble1[i];
/*  61 */         double d2 = arrayOfDouble2[b3] - arrayOfDouble2[i];
/*  62 */         if (Math.abs(d3) <= paramDouble) d3 = 0.0D; 
/*  63 */         if (Math.abs(d2) <= paramDouble) d2 = 0.0D; 
/*  64 */         double d1 = d3 * d2;
/*  65 */         if (d1 != 0.0D) {
/*     */           
/*  67 */           b2++; b1++;
/*  68 */           if (d1 > 0.0D) {
/*  69 */             this.T++;
/*     */           } else {
/*  71 */             this.T--;
/*     */           } 
/*     */         } else {
/*     */           
/*  75 */           if (d3 != 0.0D) b2++; 
/*  76 */           if (d2 != 0.0D) b1++; 
/*     */         } 
/*     */       } 
/*  79 */     }  this.r = this.T / Math.sqrt(b2) * Math.sqrt(b1);
/*     */ 
/*     */ 
/*     */     
/*  83 */     if (paramH1 == H1.LESS_THAN) {
/*  84 */       this.SP = lowerTailProb(this.n, this.T);
/*  85 */     } else if (paramH1 == H1.GREATER_THAN) {
/*  86 */       this.SP = upperTailProb(this.n, this.T);
/*     */     
/*     */     }
/*  89 */     else if (this.r < 0.0D) {
/*  90 */       this.SP = 2.0D * lowerTailProb(this.n, this.T);
/*  91 */     } else if (this.r > 0.0D) {
/*  92 */       this.SP = 2.0D * upperTailProb(this.n, this.T);
/*     */     } else {
/*  94 */       this.SP = 1.0D;
/*     */     } 
/*  96 */     if (this.SP > 1.0D) this.SP = 1.0D;
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KendallCorrelation(PairedData paramPairedData, H1 paramH1) {
/* 107 */     this(paramPairedData, paramH1, 0.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KendallCorrelation(PairedData paramPairedData) {
/* 117 */     this(paramPairedData, H1.NOT_EQUAL, 0.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/* 124 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getR() {
/* 131 */     return this.r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSP() {
/* 138 */     return this.SP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getT() {
/* 145 */     return this.T;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTestStatistic() {
/* 152 */     return this.r;
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
/*     */   public static double lowerTailProb(int paramInt1, int paramInt2) {
/* 165 */     return 1.0D - upperTailProb(paramInt1, paramInt2 + 2);
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
/*     */   public static double upperTailProb(int paramInt1, int paramInt2) {
/* 184 */     double d = 1.0D;
/* 185 */     int[][] arrayOfInt = new int[3][16];
/* 186 */     double[] arrayOfDouble = new double[16];
/*     */ 
/*     */     
/* 189 */     if (paramInt1 < 1)
/* 190 */       throw new IllegalArgumentException("n < 1."); 
/* 191 */     int i1 = paramInt1 * (paramInt1 - 1) / 2 - Math.abs(paramInt2);
/*     */     
/* 193 */     if (i1 == 0 && paramInt2 <= 0) return d; 
/* 194 */     if (paramInt1 > 8) {
/*     */ 
/*     */       
/* 197 */       double d3 = (paramInt2 - 1) / Math.sqrt((6 + paramInt1 * (5 - paramInt1 * (3 + 2 * paramInt1))) / -18.0D);
/*     */       
/* 199 */       arrayOfDouble[1] = d3;
/* 200 */       arrayOfInt[1][1] = (int)d3;
/* 201 */       arrayOfDouble[2] = d3 * d3 - 1.0D;
/* 202 */       for (byte b = 3; b <= 15; b++) {
/* 203 */         arrayOfDouble[b] = d3 * arrayOfDouble[b - 1] - (b - 1) * arrayOfDouble[b - 2];
/*     */       }
/*     */       
/* 206 */       double d1 = 1.0D / paramInt1;
/* 207 */       double d2 = d1 * (arrayOfDouble[3] * (-0.09D + d1 * (0.045D + d1 * (-0.5325D + d1 * 0.506D))) + d1 * (arrayOfDouble[5] * (0.036735D + d1 * (-0.036735D + d1 * 0.3214D)) + arrayOfDouble[7] * (0.00405D + d1 * (-0.023336D + d1 * 0.07787D)) + d1 * (arrayOfDouble[9] * (-0.0033061D - d1 * 0.0065166D) + arrayOfDouble[11] * (-1.215E-4D + d1 * 0.0025927D) + d1 * (arrayOfDouble[13] * 1.4878E-4D + arrayOfDouble[15] * 2.7338E-6D))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 216 */       d = Normal.standardTailProb(d3, true) + d2 * 0.398942D * Math.exp(-0.5D * d3 * d3);
/* 217 */       if (d < 0.0D) d = 0.0D; 
/* 218 */       if (d > 1.0D) d = 1.0D; 
/* 219 */       return d;
/*     */     } 
/*     */ 
/*     */     
/* 223 */     if (paramInt2 < 0) i1 -= 2; 
/* 224 */     int k = i1 / 2 + 1;
/* 225 */     arrayOfInt[1][1] = 1; arrayOfDouble[1] = 1.0D;
/* 226 */     arrayOfInt[2][1] = 1;
/* 227 */     if (k >= 2)
/* 228 */       for (byte b = 2; b <= k; ) { arrayOfInt[1][b] = 0; arrayOfInt[2][b] = 0; b++; }
/* 229 */         int j = 1, i = 1; i1 = 1; int m = 1;
/* 230 */     int n = 2;
/*     */     
/*     */     while (true) {
/* 233 */       if (i == paramInt1) {
/*     */         
/* 235 */         int i3 = 0;
/* 236 */         for (i = 1; i <= k; ) { i3 += arrayOfInt[n][i]; i++; }
/* 237 */          d = i3 / i1;
/* 238 */         if (paramInt2 < 0) d = 1.0D - d; 
/* 239 */         return d;
/*     */       } 
/* 241 */       j += i;
/* 242 */       i++;
/* 243 */       i1 *= i;
/* 244 */       m = 3 - m;
/* 245 */       n = 3 - n;
/* 246 */       byte b1 = 1;
/* 247 */       byte b2 = 0;
/* 248 */       int i2 = Math.min(k, j);
/*     */ 
/*     */       
/* 251 */       b1++;
/* 252 */       while (b1 <= i2) {
/*     */         
/* 254 */         arrayOfInt[n][b1] = arrayOfInt[n][b1 - 1] + arrayOfInt[m][b1];
/* 255 */         if (b1 <= i)
/* 256 */           continue;  b2++;
/* 257 */         arrayOfInt[n][b1] = arrayOfInt[n][b1] - arrayOfInt[m][b2];
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 286 */       double[] arrayOfDouble1 = { 6.0D, 3.0D, 2.0D, 5.0D, 1.0D, 7.0D, 4.0D };
/* 287 */       double[] arrayOfDouble2 = { 4.0D, 5.0D, 1.0D, 7.0D, 3.0D, 6.0D, 2.0D };
/*     */ 
/*     */       
/* 290 */       KendallCorrelation kendallCorrelation = new KendallCorrelation(new PairedData(arrayOfDouble1, arrayOfDouble2), H1.LESS_THAN, 0.0D);
/* 291 */       System.out.println("n = " + kendallCorrelation.getN() + " T = " + kendallCorrelation.getT() + " r = " + kendallCorrelation.getR() + " SP = " + kendallCorrelation.getSP());
/* 292 */       kendallCorrelation = new KendallCorrelation(new PairedData(arrayOfDouble1, arrayOfDouble2), H1.NOT_EQUAL, 0.0D);
/* 293 */       System.out.println("n = " + kendallCorrelation.getN() + " T = " + kendallCorrelation.getT() + " r = " + kendallCorrelation.getR() + " SP = " + kendallCorrelation.getSP());
/* 294 */       kendallCorrelation = new KendallCorrelation(new PairedData(arrayOfDouble1, arrayOfDouble2), H1.GREATER_THAN, 0.0D);
/* 295 */       System.out.println("n = " + kendallCorrelation.getN() + " T = " + kendallCorrelation.getT() + " r = " + kendallCorrelation.getR() + " SP = " + kendallCorrelation.getSP());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 305 */       byte b1 = 8;
/* 306 */       for (byte b2 = 0; b2 <= 28; b2++)
/* 307 */         System.out.println("n = " + b1 + " S = " + b2 + " p = " + KendallCorrelation.upperTailProb(b1, b2)); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/correlation/KendallCorrelation.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */