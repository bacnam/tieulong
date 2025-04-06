/*     */ package jsc.relatedsamples;
/*     */ 
/*     */ import jsc.datastructures.MatchedData;
/*     */ import jsc.distributions.Normal;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PageTest
/*     */   implements SignificanceTest
/*     */ {
/*     */   int k;
/*     */   int n;
/*     */   double L;
/*     */   private double SP;
/*     */   private final MatchedData ranks;
/*     */   
/*     */   public PageTest(MatchedData paramMatchedData, String[] paramArrayOfString, double paramDouble) {
/*  54 */     this.L = 0.0D;
/*     */     
/*  56 */     this.n = paramMatchedData.getBlockCount();
/*  57 */     this.k = paramMatchedData.getTreatmentCount();
/*  58 */     if (this.k < 2)
/*  59 */       throw new IllegalArgumentException("Less than two samples."); 
/*  60 */     if (this.n < 2)
/*  61 */       throw new IllegalArgumentException("Less than two blocks."); 
/*  62 */     if (paramArrayOfString.length != this.k) {
/*  63 */       throw new IllegalArgumentException("Alternative array wrong length.");
/*     */     }
/*     */     
/*  66 */     this.ranks = paramMatchedData.copy();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  80 */     this.ranks.rankByBlocks(paramDouble);
/*  81 */     double[][] arrayOfDouble = this.ranks.getData();
/*     */     
/*  83 */     for (byte b = 0; b < this.k; b++) {
/*     */ 
/*     */       
/*  86 */       double d = 0.0D;
/*  87 */       for (byte b1 = 0; b1 < this.n; ) { d += arrayOfDouble[b1][b]; b1++; }
/*     */ 
/*     */       
/*  90 */       int i = paramMatchedData.indexOfTreatment(paramArrayOfString[b]);
/*  91 */       if (i < 0)
/*  92 */         throw new IllegalArgumentException("Invalid alternative treatment label."); 
/*  93 */       this.L += (1 + i) * d;
/*     */     } 
/*     */ 
/*     */     
/*  97 */     this.SP = approxSP(this.n, this.k, this.L);
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
/*     */   public PageTest(MatchedData paramMatchedData, String[] paramArrayOfString) {
/* 113 */     this(paramMatchedData, paramArrayOfString, 0.0D);
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
/*     */   public static double approxSP(int paramInt1, int paramInt2, double paramDouble) {
/* 130 */     if (paramInt2 < 2)
/* 131 */       throw new IllegalArgumentException("Less than two samples."); 
/* 132 */     if (paramInt1 < 2)
/* 133 */       throw new IllegalArgumentException("Less than two blocks."); 
/* 134 */     if (paramDouble < 0.0D) {
/* 135 */       throw new IllegalArgumentException("Invalid L value.");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 141 */     double d1 = paramInt2;
/* 142 */     double d2 = (paramDouble - 0.25D * paramInt1 * d1 * (d1 + 1.0D) * (d1 + 1.0D) - 0.5D) / Math.sqrt(paramInt1 * d1 * d1 * (d1 + 1.0D) * (d1 * d1 - 1.0D) / 144.0D);
/*     */     
/* 144 */     return Normal.standardTailProb(d2, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MatchedData getRanks() {
/* 154 */     return this.ranks;
/*     */   } public double getSP() {
/* 156 */     return this.SP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTestStatistic() {
/* 163 */     return this.L;
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
/* 174 */       String[] arrayOfString1 = { "1", "2", "3", "4", "5" };
/* 175 */       String[] arrayOfString2 = { "A", "B", "C", "D", "E", "F" };
/* 176 */       double[][] arrayOfDouble = { { 62.59D, 67.11D, 73.02D, 92.34D, 83.48D }, { 55.75D, 63.03D, 63.93D, 71.61D, 93.73D }, { 65.88D, 69.89D, 82.53D, 77.33D, 84.82D }, { 66.13D, 66.79D, 85.37D, 76.72D, 98.88D }, { 65.73D, 57.85D, 63.08D, 71.75D, 87.04D }, { 78.36D, 76.17D, 82.97D, 91.49D, 89.61D } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 182 */       String[] arrayOfString3 = { "1", "2", "3", "4", "5" };
/*     */       
/* 184 */       MatchedData matchedData = new MatchedData(arrayOfDouble, arrayOfString2, arrayOfString1);
/* 185 */       int i = matchedData.getTreatmentCount();
/* 186 */       int j = matchedData.getBlockCount();
/* 187 */       System.out.println("n = " + j + " k = " + i);
/* 188 */       PageTest pageTest = new PageTest(matchedData, arrayOfString3);
/* 189 */       double d = pageTest.getTestStatistic();
/* 190 */       System.out.print(pageTest.getRanks().toString());
/* 191 */       System.out.println("L = " + d + " Approx SP = " + PageTest.approxSP(j, i, d));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/relatedsamples/PageTest.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */