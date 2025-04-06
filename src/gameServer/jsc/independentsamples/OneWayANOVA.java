/*     */ package jsc.independentsamples;
/*     */ 
/*     */ import jsc.datastructures.GroupedData;
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
/*     */ 
/*     */ public class OneWayANOVA
/*     */   implements SignificanceTest
/*     */ {
/*     */   private final int treatmentCount;
/*     */   private final int N;
/*     */   private final double F;
/*     */   private final double SP;
/*     */   private final double ess;
/*     */   private double rss;
/*     */   private final double tss;
/*     */   private final double ems;
/*     */   private final double rms;
/*     */   private final MeanVar[] mv;
/*     */   
/*     */   public OneWayANOVA(GroupedData paramGroupedData) {
/*  46 */     this.treatmentCount = paramGroupedData.getGroupCount();
/*  47 */     if (this.treatmentCount < 2)
/*  48 */       throw new IllegalArgumentException("Less than two samples."); 
/*  49 */     this.N = paramGroupedData.getN();
/*     */     
/*  51 */     this.mv = new MeanVar[this.treatmentCount];
/*     */ 
/*     */     
/*  54 */     MeanVar meanVar = new MeanVar(paramGroupedData.getData());
/*  55 */     double d = meanVar.getMean();
/*  56 */     this.tss = (this.N - 1.0D) * meanVar.getVariance();
/*     */ 
/*     */     
/*  59 */     this.rss = 0.0D;
/*  60 */     for (byte b = 0; b < this.treatmentCount; b++) {
/*     */       
/*  62 */       double[] arrayOfDouble = paramGroupedData.getData(b);
/*  63 */       this.mv[b] = new MeanVar(arrayOfDouble);
/*  64 */       for (byte b1 = 0; b1 < this.mv[b].getN(); b1++) {
/*     */         
/*  66 */         double d1 = arrayOfDouble[b1] - this.mv[b].getMean();
/*  67 */         this.rss += d1 * d1;
/*     */       } 
/*     */     } 
/*     */     
/*  71 */     this.ess = this.tss - this.rss;
/*     */ 
/*     */     
/*  74 */     this.ems = this.ess / (this.treatmentCount - 1);
/*  75 */     this.rms = this.rss / (this.N - this.treatmentCount);
/*  76 */     this.F = this.ems / this.rms;
/*     */ 
/*     */     
/*  79 */     this.SP = FishersF.upperTailProb(this.F, (this.treatmentCount - 1), (this.N - this.treatmentCount));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTreatmentCount() {
/*  87 */     return this.treatmentCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/*  94 */     return this.N;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSize(int paramInt) {
/* 102 */     return this.mv[paramInt].getN();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMean(int paramInt) {
/* 110 */     return this.mv[paramInt].getMean();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSd(int paramInt) {
/* 118 */     return this.mv[paramInt].getSd();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getEss() {
/* 126 */     return this.ess;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getEms() {
/* 133 */     return this.ems;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getRss() {
/* 140 */     return this.rss;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getRms() {
/* 148 */     return this.rms;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTss() {
/* 155 */     return this.tss;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSP() {
/* 162 */     return this.SP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTestStatistic() {
/* 169 */     return this.F;
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
/* 180 */       String[] arrayOfString = { "1", "1", "1", "1", "2", "2", "2", "2", "3", "3", "3", "3", "4", "4", "4", "4" };
/* 181 */       double[] arrayOfDouble = { 18.95D, 12.62D, 11.94D, 14.42D, 10.06D, 7.19D, 7.03D, 14.66D, 10.92D, 13.28D, 14.52D, 12.51D, 9.3D, 21.2D, 16.11D, 21.41D };
/*     */ 
/*     */       
/* 184 */       GroupedData groupedData = new GroupedData(arrayOfDouble, arrayOfString);
/* 185 */       OneWayANOVA oneWayANOVA = new OneWayANOVA(groupedData);
/* 186 */       System.out.println(" ESS = " + oneWayANOVA.getEss() + " EMS = " + oneWayANOVA.getEms());
/* 187 */       System.out.println(" RSS = " + oneWayANOVA.getRss() + " RMS = " + oneWayANOVA.getRms());
/* 188 */       System.out.println(" TSS = " + oneWayANOVA.getTss());
/* 189 */       for (byte b = 0; b < oneWayANOVA.getTreatmentCount(); b++)
/* 190 */         System.out.println(groupedData.getLabel(b) + "\tN = " + oneWayANOVA.getSize(b) + "\tMean = " + oneWayANOVA.getMean(b) + "\tsd = " + oneWayANOVA.getSd(b)); 
/* 191 */       System.out.println("F = " + oneWayANOVA.getTestStatistic() + " SP = " + oneWayANOVA.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/independentsamples/OneWayANOVA.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */