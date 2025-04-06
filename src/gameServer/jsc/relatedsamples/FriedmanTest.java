/*     */ package jsc.relatedsamples;
/*     */ 
/*     */ import jsc.datastructures.MatchedData;
/*     */ import jsc.distributions.Beta;
/*     */ import jsc.distributions.ChiSquared;
/*     */ import jsc.distributions.FriedmanM;
/*     */ import jsc.tests.SignificanceTest;
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
/*     */ public class FriedmanTest
/*     */   implements SignificanceTest
/*     */ {
/*     */   int k;
/*     */   int n;
/*     */   double C;
/*     */   double M;
/*     */   double S;
/*     */   double W;
/*     */   private double SP;
/*     */   private final MatchedData ranks;
/*     */   
/*     */   public FriedmanTest(MatchedData paramMatchedData, double paramDouble, boolean paramBoolean) {
/*  61 */     int i = 0;
/*     */ 
/*     */     
/*  64 */     double d1 = 0.0D;
/*     */     
/*  66 */     this.S = 0.0D;
/*     */     
/*  68 */     this.n = paramMatchedData.getBlockCount();
/*  69 */     this.k = paramMatchedData.getTreatmentCount();
/*  70 */     if (this.k < 2)
/*  71 */       throw new IllegalArgumentException("Less than two samples."); 
/*  72 */     if (this.n < 2) {
/*  73 */       throw new IllegalArgumentException("Less than two blocks.");
/*     */     }
/*     */     
/*  76 */     this.ranks = paramMatchedData.copy();
/*  77 */     double d2 = 0.5D * (this.k + 1.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     i = this.ranks.rankByBlocks(paramDouble);
/*  92 */     double[][] arrayOfDouble = this.ranks.getData();
/*     */ 
/*     */     
/*  95 */     for (byte b = 0; b < this.k; b++) {
/*     */ 
/*     */       
/*  98 */       d1 = 0.0D;
/*  99 */       for (byte b1 = 0; b1 < this.n; ) { d1 += arrayOfDouble[b1][b]; b1++; }
/* 100 */        d1 /= this.n;
/*     */       
/* 102 */       this.S += (d1 - d2) * (d1 - d2);
/*     */     } 
/*     */     
/* 105 */     double d3 = (this.k * (this.k + 1));
/* 106 */     double d4 = (this.k * (this.k * this.k - 1));
/*     */     
/* 108 */     this.C = 1.0D - i / this.n * d4;
/* 109 */     this.M = 12.0D * this.n * this.S / d3 / this.C;
/* 110 */     this.W = this.M / this.n * (this.k - 1.0D);
/*     */ 
/*     */     
/* 113 */     if (paramBoolean) {
/* 114 */       this.SP = betaApproxSP(this.n, this.k, this.S, this.C);
/*     */     
/*     */     }
/* 117 */     else if ((this.k == 2 && this.n < 25) || (this.k == 3 && this.n < 11) || (this.k == 4 && this.n < 7) || (this.k == 5 && this.n < 5) || (this.k == 6 && this.n < 4) || (this.k == 7 && this.n < 3) || (this.k == 8 && this.n < 3) || (this.k == 9 && this.n < 3) || (this.k == 10 && this.n < 3)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 126 */       this.SP = exactSP(this.n, this.k, this.M);
/*     */     } else {
/* 128 */       this.SP = betaApproxSP(this.n, this.k, this.S, this.C);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FriedmanTest(MatchedData paramMatchedData) {
/* 175 */     this(paramMatchedData, 0.0D, false);
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
/*     */   public static double betaApproxSP(int paramInt1, int paramInt2, double paramDouble1, double paramDouble2) {
/* 197 */     if (paramInt2 < 2)
/* 198 */       throw new IllegalArgumentException("Less than two samples."); 
/* 199 */     if (paramInt1 < 2)
/* 200 */       throw new IllegalArgumentException("Less than two blocks."); 
/* 201 */     if (paramDouble1 < 0.0D)
/* 202 */       throw new IllegalArgumentException("Invalid S value."); 
/* 203 */     if (paramDouble2 <= 0.0D || paramDouble2 > 1.0D)
/* 204 */       throw new IllegalArgumentException("Invalid correction factor for ties."); 
/* 205 */     double d1 = 0.5D * (paramInt2 - 1.0D) - 1.0D / paramInt1;
/* 206 */     double d2 = (paramInt1 - 1.0D) * d1;
/* 207 */     double d3 = (paramInt1 * paramInt1) * paramDouble1;
/* 208 */     double d4 = paramDouble2 * 12.0D * (d3 - 1.0D) / ((paramInt1 * paramInt1 * paramInt2) * ((paramInt2 * paramInt2) - 1.0D) + 2.0D);
/*     */ 
/*     */     
/*     */     try {
/* 212 */       return 1.0D - Beta.incompleteBeta(d4, d1, d2, Maths.lnB(d1, d2));
/*     */     } catch (IllegalArgumentException illegalArgumentException) {
/* 214 */       throw new IllegalArgumentException("Cannot calculate beta approximation.");
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
/*     */   public static double chiSquaredApproxSP(int paramInt, double paramDouble) {
/* 229 */     if (paramInt < 2)
/* 230 */       throw new IllegalArgumentException("Less than two samples."); 
/* 231 */     if (paramDouble < 0.0D)
/* 232 */       throw new IllegalArgumentException("Invalid M value."); 
/* 233 */     ChiSquared chiSquared = new ChiSquared((paramInt - 1));
/* 234 */     return 1.0D - chiSquared.cdf(paramDouble);
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
/*     */   public static double exactSP(int paramInt1, int paramInt2, double paramDouble) {
/* 256 */     if (paramInt2 < 2)
/* 257 */       throw new IllegalArgumentException("Less than two samples."); 
/* 258 */     if (paramInt1 < 2)
/* 259 */       throw new IllegalArgumentException("Less than two blocks."); 
/* 260 */     if (paramDouble < 0.0D)
/* 261 */       throw new IllegalArgumentException("Invalid M value."); 
/* 262 */     FriedmanM friedmanM = new FriedmanM(paramInt1, paramInt2);
/* 263 */     return 1.0D - friedmanM.cdf(paramDouble);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getC() {
/* 272 */     return this.C;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MatchedData getRanks() {
/* 281 */     return this.ranks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getS() {
/* 290 */     return this.S;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSP() {
/* 297 */     return this.SP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTestStatistic() {
/* 304 */     return this.M;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getW() {
/* 313 */     return this.W;
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 376 */       String[] arrayOfString1 = { "1", "2", "3" };
/* 377 */       String[] arrayOfString2 = { "A", "B", "C", "D" };
/* 378 */       double[][] arrayOfDouble = { { 8.5D, 8.9D, 8.8D, 8.8D }, { 8.2D, 8.4D, 8.2D, 8.2D }, { 8.9D, 9.1D, 9.1D, 8.9D } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 394 */       MatchedData matchedData = new MatchedData(arrayOfDouble, arrayOfString1, arrayOfString2);
/* 395 */       int i = matchedData.getTreatmentCount();
/* 396 */       int j = matchedData.getBlockCount();
/* 397 */       System.out.println("n = " + j + " k = " + i);
/* 398 */       FriedmanTest friedmanTest = new FriedmanTest(matchedData, 0.0D, false);
/* 399 */       System.out.print(friedmanTest.getRanks().toString());
/* 400 */       double d1 = friedmanTest.getTestStatistic();
/* 401 */       double d2 = friedmanTest.getS();
/* 402 */       double d3 = friedmanTest.getC();
/* 403 */       System.out.println("S = " + friedmanTest.getS() + " M = " + d1 + " W = " + friedmanTest.getW());
/* 404 */       System.out.println("Chi-squared approx SP = " + FriedmanTest.chiSquaredApproxSP(i, d1));
/* 405 */       System.out.println("                   SP = " + friedmanTest.getSP());
/* 406 */       System.out.println("       Beta approx SP = " + FriedmanTest.betaApproxSP(j, i, d2, d3));
/* 407 */       System.out.println("       Exact(test) SP = " + FriedmanTest.exactSP(j, i, d1));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/relatedsamples/FriedmanTest.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */