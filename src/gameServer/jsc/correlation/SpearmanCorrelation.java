/*     */ package jsc.correlation;
/*     */ 
/*     */ import jsc.datastructures.PairedData;
/*     */ import jsc.distributions.HotellingPabstS;
/*     */ import jsc.tests.H1;
/*     */ import jsc.tests.SignificanceTest;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpearmanCorrelation
/*     */   implements SignificanceTest
/*     */ {
/*     */   static final int N_EXACT = 10;
/*     */   private final int n;
/*     */   private final double r;
/*     */   private double S;
/*     */   private double SP;
/*     */   
/*     */   public SpearmanCorrelation(PairedData paramPairedData, H1 paramH1, double paramDouble, boolean paramBoolean) {
/*  48 */     this.n = paramPairedData.getN();
/*     */ 
/*     */     
/*  51 */     Rank rank1 = new Rank(paramPairedData.getX(), paramDouble);
/*  52 */     Rank rank2 = new Rank(paramPairedData.getY(), paramDouble);
/*  53 */     double[] arrayOfDouble1 = rank1.getRanks();
/*  54 */     double[] arrayOfDouble2 = rank2.getRanks();
/*     */ 
/*     */ 
/*     */     
/*  58 */     this.S = 0.0D;
/*     */     
/*  60 */     for (byte b = 0; b < this.n; b++)
/*  61 */       this.S += (arrayOfDouble1[b] - arrayOfDouble2[b]) * (arrayOfDouble1[b] - arrayOfDouble2[b]); 
/*  62 */     double d1 = (this.n * this.n * this.n - this.n - rank1.getCorrectionFactor1());
/*  63 */     double d2 = (this.n * this.n * this.n - this.n - rank2.getCorrectionFactor1());
/*  64 */     this.r = (d1 + d2 - this.S * 12.0D) / 2.0D * Math.sqrt(d1 * d2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  73 */     if (paramH1 == H1.LESS_THAN) {
/*     */       
/*  75 */       this.SP = HotellingPabstS.upperTailProb(this.n, (int)Math.round(this.S), paramBoolean);
/*  76 */     } else if (paramH1 == H1.GREATER_THAN) {
/*     */       
/*  78 */       this.SP = HotellingPabstS.lowerTailProb(this.n, (int)Math.round(this.S), paramBoolean);
/*     */     
/*     */     }
/*  81 */     else if (this.r < 0.0D) {
/*  82 */       this.SP = 2.0D * HotellingPabstS.upperTailProb(this.n, (int)Math.round(this.S), paramBoolean);
/*  83 */     } else if (this.r > 0.0D) {
/*  84 */       this.SP = 2.0D * HotellingPabstS.lowerTailProb(this.n, (int)Math.round(this.S), paramBoolean);
/*     */     } else {
/*  86 */       this.SP = 1.0D;
/*     */     } 
/*  88 */     if (this.SP > 1.0D) this.SP = 1.0D;
/*     */   
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
/*     */   public SpearmanCorrelation(PairedData paramPairedData, H1 paramH1, double paramDouble) {
/* 102 */     this(paramPairedData, paramH1, paramDouble, (paramPairedData.getN() > 10));
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
/*     */   public SpearmanCorrelation(PairedData paramPairedData, H1 paramH1) {
/* 114 */     this(paramPairedData, paramH1, 0.0D, (paramPairedData.getN() > 10));
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
/*     */   public SpearmanCorrelation(PairedData paramPairedData) {
/* 126 */     this(paramPairedData, H1.NOT_EQUAL, 0.0D, (paramPairedData.getN() > 10));
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
/*     */   public int getN() {
/* 169 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getR() {
/* 176 */     return this.r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getS() {
/* 184 */     return this.S;
/*     */   } public double getSP() {
/* 186 */     return this.SP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTestStatistic() {
/* 193 */     return this.r;
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 258 */       double[] arrayOfDouble1 = { 1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D, 7.0D };
/* 259 */       double[] arrayOfDouble2 = { 124.0D, 117.0D, 117.0D, 120.0D, 120.0D, 114.0D, 114.0D };
/* 260 */       double[] arrayOfDouble3 = { 131.0D, 125.0D, 116.0D, 113.0D, 124.0D, 118.0D, 102.0D };
/* 261 */       double[] arrayOfDouble4 = { 100.0D, 97.0D, 103.0D, 108.0D, 106.0D, 95.0D, 96.0D };
/*     */       
/* 263 */       SpearmanCorrelation spearmanCorrelation = new SpearmanCorrelation(new PairedData(arrayOfDouble1, arrayOfDouble2), H1.LESS_THAN);
/* 264 */       System.out.println("n = " + spearmanCorrelation.getN() + " r = " + spearmanCorrelation.getR() + " SP = " + spearmanCorrelation.getSP());
/* 265 */       spearmanCorrelation = new SpearmanCorrelation(new PairedData(arrayOfDouble1, arrayOfDouble3), H1.LESS_THAN);
/* 266 */       System.out.println("n = " + spearmanCorrelation.getN() + " r = " + spearmanCorrelation.getR() + " SP = " + spearmanCorrelation.getSP());
/* 267 */       spearmanCorrelation = new SpearmanCorrelation(new PairedData(arrayOfDouble1, arrayOfDouble4), H1.LESS_THAN);
/* 268 */       System.out.println("n = " + spearmanCorrelation.getN() + " r = " + spearmanCorrelation.getR() + " SP = " + spearmanCorrelation.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/correlation/SpearmanCorrelation.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */