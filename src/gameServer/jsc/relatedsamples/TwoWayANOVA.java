/*     */ package jsc.relatedsamples;
/*     */ 
/*     */ import jsc.datastructures.MatchedData;
/*     */ import jsc.descriptive.MeanVar;
/*     */ import jsc.distributions.FishersF;
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
/*     */ public class TwoWayANOVA
/*     */   implements SignificanceTest
/*     */ {
/*     */   private final int N;
/*     */   private final int treatmentCount;
/*     */   private final int blockCount;
/*     */   private final double F;
/*     */   private final double SP;
/*     */   private double tess;
/*     */   private double bess;
/*     */   private final double tems;
/*     */   private final double bems;
/*     */   private double tss;
/*     */   private final double rss;
/*     */   private final double rms;
/*     */   private final MatchedData resids;
/*     */   
/*     */   public TwoWayANOVA(MatchedData paramMatchedData) {
/*  48 */     this.treatmentCount = paramMatchedData.getTreatmentCount();
/*  49 */     this.blockCount = paramMatchedData.getBlockCount();
/*  50 */     if (this.treatmentCount < 2)
/*  51 */       throw new IllegalArgumentException("Less than two treatments."); 
/*  52 */     if (this.blockCount < 2)
/*  53 */       throw new IllegalArgumentException("Less than two blocks."); 
/*  54 */     this.N = paramMatchedData.getN();
/*     */     
/*  56 */     this.resids = paramMatchedData.copy();
/*     */ 
/*     */     
/*  59 */     MeanVar meanVar = new MeanVar(paramMatchedData.getBlockPackedCopy());
/*  60 */     this.tss = (this.N - 1.0D) * meanVar.getVariance();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  66 */     double d = this.resids.sweepByBlocks();
/*  67 */     System.out.println("brss = " + d);
/*  68 */     this.bess = this.tss - d;
/*     */ 
/*     */     
/*  71 */     this.rss = this.resids.sweepByTreatments();
/*  72 */     System.out.println("rss = " + this.rss);
/*  73 */     this.tess = d - this.rss;
/*     */ 
/*     */     
/*  76 */     this.bems = this.bess / (this.blockCount - 1);
/*  77 */     this.tems = this.tess / (this.treatmentCount - 1);
/*  78 */     this.rms = this.rss / ((this.blockCount - 1) * (this.treatmentCount - 1));
/*     */ 
/*     */     
/*  81 */     this.F = this.tems / this.rms;
/*  82 */     this.SP = FishersF.upperTailProb(this.F, (this.treatmentCount - 1), ((this.treatmentCount - 1) * (this.blockCount - 1)));
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/* 152 */     return this.N;
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
/*     */   public int getBlockCount() {
/* 175 */     return this.blockCount;
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
/*     */   public double getBlockEss() {
/* 197 */     return this.bess;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getBlockEms() {
/* 204 */     return this.bems;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTreatmentCount() {
/* 211 */     return this.treatmentCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTreatmentEss() {
/* 219 */     return this.tess;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTreatmentEms() {
/* 226 */     return this.tems;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getRss() {
/* 233 */     return this.rss;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getRms() {
/* 241 */     return this.rms;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTss() {
/* 248 */     return this.tss;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MatchedData getResiduals() {
/* 257 */     return this.resids;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSP() {
/* 264 */     return this.SP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTestStatistic() {
/* 271 */     return this.F;
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
/* 282 */       double[][] arrayOfDouble = { { 3.93D, 3.99D, 4.08D }, { 3.78D, 3.96D, 3.94D }, { 3.88D, 3.96D, 4.02D }, { 3.93D, 4.03D, 4.06D }, { 3.84D, 4.1D, 3.94D }, { 3.75D, 4.02D, 4.09D }, { 3.98D, 4.06D, 4.17D }, { 3.84D, 3.92D, 4.12D } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 291 */       MatchedData matchedData = new MatchedData(arrayOfDouble);
/* 292 */       TwoWayANOVA twoWayANOVA = new TwoWayANOVA(matchedData);
/* 293 */       System.out.print(twoWayANOVA.getResiduals().toString());
/* 294 */       System.out.println("Treatment SS = " + twoWayANOVA.getTreatmentEss() + " MS = " + twoWayANOVA.getTreatmentEms());
/* 295 */       System.out.println("    Block SS = " + twoWayANOVA.getBlockEss() + " MS = " + twoWayANOVA.getBlockEms());
/* 296 */       System.out.println("         RSS = " + twoWayANOVA.getRss() + " RMS = " + twoWayANOVA.getRms());
/* 297 */       System.out.println("         TSS = " + twoWayANOVA.getTss());
/* 298 */       System.out.println("F = " + twoWayANOVA.getTestStatistic() + " SP = " + twoWayANOVA.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/relatedsamples/TwoWayANOVA.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */