/*     */ package jsc.contingencytables;
/*     */ 
/*     */ import jsc.distributions.ChiSquared;
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
/*     */ public class ChiSquaredTest
/*     */   implements SignificanceTest
/*     */ {
/*     */   private boolean smallE = false;
/*     */   private int df;
/*     */   double chiSquared;
/*     */   private double SP;
/*     */   private double[][] E;
/*     */   private double[][] resids;
/*     */   private ContingencyTable tableCopy;
/*     */   
/*     */   public ChiSquaredTest(ContingencyTable paramContingencyTable) {
/*  48 */     this.tableCopy = paramContingencyTable.copy();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  53 */     int i = paramContingencyTable.getColumnCount();
/*  54 */     if (i < 2) throw new IllegalArgumentException("Less than 2 columns."); 
/*  55 */     int j = paramContingencyTable.getRowCount();
/*  56 */     if (j < 2) throw new IllegalArgumentException("Less than 2 rows.");
/*     */     
/*  58 */     int[] arrayOfInt1 = paramContingencyTable.getColumnTotals();
/*  59 */     int[] arrayOfInt2 = paramContingencyTable.getRowTotals();
/*  60 */     double d1 = paramContingencyTable.getN();
/*     */     
/*  62 */     this.df = (j - 1) * (i - 1);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  67 */     double d2 = varpear(d1, j, i, arrayOfInt2, arrayOfInt1);
/*     */     
/*  69 */     this.E = new double[j][i];
/*  70 */     this.resids = new double[j][i];
/*  71 */     int[][] arrayOfInt = paramContingencyTable.getFrequencies();
/*     */ 
/*     */     
/*  74 */     this.chiSquared = 0.0D;
/*  75 */     for (byte b = 0; b < j; b++) {
/*  76 */       for (byte b1 = 0; b1 < i; b1++) {
/*     */         
/*  78 */         this.E[b][b1] = (arrayOfInt2[b] * arrayOfInt1[b1]) / d1;
/*  79 */         if (this.E[b][b1] <= 0.0D)
/*  80 */           throw new IllegalArgumentException("An expected frequency is zero."); 
/*  81 */         if (this.E[b][b1] < 5.0D && d2 >= (this.df + this.df)) this.smallE = true; 
/*  82 */         this.resids[b][b1] = (arrayOfInt[b][b1] - this.E[b][b1]) * (arrayOfInt[b][b1] - this.E[b][b1]) / this.E[b][b1];
/*  83 */         this.chiSquared += this.resids[b][b1];
/*     */       } 
/*     */     } 
/*  86 */     this.SP = ChiSquared.upperTailProb(this.chiSquared, this.df);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContingencyTable getContingencyTable() {
/*  94 */     return this.tableCopy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/* 102 */     return this.tableCopy.getN();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRowCount() {
/* 110 */     return this.tableCopy.getRowCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getRowTotals() {
/* 118 */     return this.tableCopy.getRowTotals();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumnCount() {
/* 126 */     return this.tableCopy.getColumnCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getColumnTotals() {
/* 134 */     return this.tableCopy.getColumnTotals();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[][] getExpectedFrequencies() {
/* 141 */     return this.E;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getExpectedFrequency(int paramInt1, int paramInt2) {
/* 152 */     return this.E[paramInt1][paramInt2];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[][] getObservedFrequencies() {
/* 159 */     return this.tableCopy.getFrequencies();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getObservedFrequency(int paramInt1, int paramInt2) {
/* 170 */     return this.tableCopy.getFrequency(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[][] getResiduals() {
/* 177 */     return this.resids;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getResidual(int paramInt1, int paramInt2) {
/* 188 */     return this.resids[paramInt1][paramInt2];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDegreesOfFreedom() {
/* 195 */     return this.df;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSP() {
/* 202 */     return this.SP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTestStatistic() {
/* 209 */     return this.chiSquared;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasSmallExpectedFrequency() {
/* 220 */     return this.smallE;
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
/*     */   private double varpear(double paramDouble, int paramInt1, int paramInt2, int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 240 */     double d5 = 0.0D;
/*     */     
/* 242 */     if (paramDouble < 4.0D) return Double.MAX_VALUE;
/*     */     
/* 244 */     double d1 = (paramDouble - paramInt1) * (paramInt1 - 1.0D) / (paramDouble - 1.0D);
/* 245 */     double d3 = (paramDouble - paramInt2) * (paramInt2 - 1.0D) / (paramDouble - 1.0D); byte b;
/* 246 */     for (b = 0; b < paramInt1; b++) {
/* 247 */       if (paramArrayOfint1[b] <= 0.0D) {
/* 248 */         return Double.MAX_VALUE;
/*     */       }
/* 250 */       d5 += 1.0D / paramArrayOfint1[b];
/* 251 */     }  double d2 = (d5 - (paramInt1 * paramInt1) / paramDouble) * paramDouble / (paramDouble - 2.0D);
/* 252 */     d5 = 0.0D;
/* 253 */     for (b = 0; b < paramInt2; b++) {
/* 254 */       if (paramArrayOfint2[b] <= 0.0D) {
/* 255 */         return Double.MAX_VALUE;
/*     */       }
/* 257 */       d5 += 1.0D / paramArrayOfint2[b];
/* 258 */     }  double d4 = (d5 - (paramInt2 * paramInt2) / paramDouble) * paramDouble / (paramDouble - 2.0D);
/*     */     
/* 260 */     return 2.0D * paramDouble / (paramDouble - 3.0D) * (d1 - d2) * (d3 - d4) + paramDouble * paramDouble / (paramDouble - 1.0D) * d2 * d4;
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 289 */       String[] arrayOfString1 = { "Improved", "Same or worse" };
/* 290 */       String[] arrayOfString2 = { "Placebo", "Drug 1", "Drug 2", "Drug 3", "Drug 4", "Drug 5" };
/* 291 */       int[][] arrayOfInt = { { 8, 12, 21, 15, 14, 19 }, { 22, 18, 9, 15, 16, 11 } };
/* 292 */       ContingencyTable contingencyTable = new ContingencyTable(arrayOfString1, arrayOfString2, arrayOfInt);
/* 293 */       System.out.println(contingencyTable.toString());
/* 294 */       ChiSquaredTest chiSquaredTest = new ChiSquaredTest(contingencyTable);
/* 295 */       System.out.println("Chi-squared = " + chiSquaredTest.getTestStatistic() + " SP = " + chiSquaredTest.getSP());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 327 */       int[] arrayOfInt1 = { 2, 1, 2, 2, 2, 3, 3, 2, 1, 1, 2, 3, 2, 3, 2, 3, 1, 2, 3, 2 };
/* 328 */       int[] arrayOfInt2 = { 8, 8, 9, 8, 9, 9, 9, 8, 8, 9, 8, 8, 9, 8, 8, 9, 9, 8, 8, 9 };
/* 329 */       contingencyTable = new ContingencyTable(arrayOfInt1, arrayOfInt2);
/* 330 */       System.out.println(contingencyTable.toString());
/* 331 */       chiSquaredTest = new ChiSquaredTest(contingencyTable);
/* 332 */       System.out.println("Chi-squared = " + chiSquaredTest.getTestStatistic() + " SP = " + chiSquaredTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/contingencytables/ChiSquaredTest.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */