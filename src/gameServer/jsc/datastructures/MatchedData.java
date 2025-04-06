/*     */ package jsc.datastructures;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import jsc.util.Arrays;
/*     */ import jsc.util.Rank;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MatchedData
/*     */   implements Cloneable
/*     */ {
/*     */   private int n;
/*     */   private int blockCount;
/*     */   private int treatmentCount;
/*     */   private Vector blockLabels;
/*     */   private Vector treatmentLabels;
/*     */   private double[][] data;
/*     */   
/*     */   public MatchedData(double[] paramArrayOfdouble, String[] paramArrayOfString1, String[] paramArrayOfString2) {
/*  64 */     this.n = paramArrayOfdouble.length;
/*  65 */     if (this.n < 1)
/*  66 */       throw new IllegalArgumentException("No data."); 
/*  67 */     if (this.n != paramArrayOfString1.length || this.n != paramArrayOfString2.length) {
/*  68 */       throw new IllegalArgumentException("Arrays not equal length.");
/*     */     }
/*     */     
/*  71 */     this.blockLabels = Sort.getLabels(paramArrayOfString1);
/*  72 */     this.treatmentLabels = Sort.getLabels(paramArrayOfString2);
/*     */     
/*  74 */     this.blockCount = this.blockLabels.size();
/*  75 */     this.treatmentCount = this.treatmentLabels.size();
/*  76 */     this.data = new double[this.blockCount][this.treatmentCount];
/*  77 */     for (byte b = 0; b < this.n; b++) {
/*  78 */       this.data[this.blockLabels.indexOf(paramArrayOfString1[b])][this.treatmentLabels.indexOf(paramArrayOfString2[b])] = paramArrayOfdouble[b];
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
/*     */   public MatchedData(double[] paramArrayOfdouble, int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  93 */     this(paramArrayOfdouble, Arrays.toStringArray(paramArrayOfint1), Arrays.toStringArray(paramArrayOfint2));
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
/*     */   public MatchedData(double[][] paramArrayOfdouble, String[] paramArrayOfString1, String[] paramArrayOfString2) {
/* 114 */     this.blockCount = paramArrayOfString1.length;
/* 115 */     this.treatmentCount = paramArrayOfString2.length;
/*     */ 
/*     */     
/* 118 */     if (this.blockCount < 1)
/* 119 */       throw new IllegalArgumentException("No blocks labels."); 
/* 120 */     if (this.treatmentCount < 1)
/* 121 */       throw new IllegalArgumentException("No treatment labels."); 
/* 122 */     if (this.blockCount != paramArrayOfdouble.length)
/* 123 */       throw new IllegalArgumentException("Number of block labels and data values do not match.");  byte b;
/* 124 */     for (b = 0; b < this.blockCount; b++) {
/* 125 */       if ((paramArrayOfdouble[b]).length != this.treatmentCount)
/* 126 */         throw new IllegalArgumentException("Number of treatment labels and data values do not match."); 
/*     */     } 
/* 128 */     this.blockLabels = new Vector(this.blockCount);
/* 129 */     this.treatmentLabels = new Vector(this.treatmentCount);
/* 130 */     for (b = 0; b < this.blockCount; ) { this.blockLabels.add(paramArrayOfString1[b]); b++; }
/* 131 */      for (b = 0; b < this.treatmentCount; ) { this.treatmentLabels.add(paramArrayOfString2[b]); b++; }
/*     */     
/* 133 */     this.data = paramArrayOfdouble;
/* 134 */     this.n = this.blockCount * this.treatmentCount;
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
/*     */   public MatchedData(double[][] paramArrayOfdouble, int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 154 */     this(paramArrayOfdouble, Arrays.toStringArray(paramArrayOfint1), Arrays.toStringArray(paramArrayOfint2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MatchedData(double[][] paramArrayOfdouble) {
/* 165 */     this(paramArrayOfdouble, Arrays.sequence(0, paramArrayOfdouble.length - 1), Arrays.sequence(0, (paramArrayOfdouble[0]).length - 1));
/*     */   } public Object clone() {
/* 167 */     return copy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MatchedData copy() {
/* 177 */     String[] arrayOfString1 = new String[this.blockCount];
/* 178 */     String[] arrayOfString2 = new String[this.treatmentCount];
/* 179 */     double[][] arrayOfDouble = new double[this.blockCount][this.treatmentCount];
/*     */     byte b;
/* 181 */     for (b = 0; b < this.blockCount; ) { arrayOfString1[b] = this.blockLabels.get(b); b++; }
/* 182 */      for (b = 0; b < this.treatmentCount; ) { arrayOfString2[b] = this.treatmentLabels.get(b); b++; }
/* 183 */      for (b = 0; b < this.blockCount; b++) {
/* 184 */       for (byte b1 = 0; b1 < this.treatmentCount; b1++)
/* 185 */         arrayOfDouble[b][b1] = this.data[b][b1]; 
/*     */     } 
/* 187 */     return new MatchedData(arrayOfDouble, arrayOfString1, arrayOfString2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[][] getData() {
/* 197 */     return this.data;
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
/*     */   public double getDatum(int paramInt1, int paramInt2) {
/* 212 */     return this.data[paramInt1][paramInt2];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/* 219 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBlockCount() {
/* 226 */     return this.blockCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] getBlockData(String paramString) {
/* 237 */     int i = this.blockLabels.indexOf(paramString);
/* 238 */     if (i < 0) return null; 
/* 239 */     return this.data[i];
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
/*     */   public double[] getBlockData(int paramInt) {
/* 251 */     return this.data[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBlockLabel(int paramInt) {
/* 262 */     return this.blockLabels.get(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector getBlockLabels() {
/* 269 */     return this.blockLabels;
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
/*     */   public double getBlockMean(int paramInt) {
/* 281 */     double d = 0.0D;
/* 282 */     for (byte b = 0; b < this.treatmentCount; ) { d += this.data[paramInt][b]; b++; }
/* 283 */      return d / this.treatmentCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] getBlockPackedCopy() {
/* 292 */     double[] arrayOfDouble = new double[this.blockCount * this.treatmentCount];
/* 293 */     for (byte b = 0; b < this.blockCount; b++) {
/* 294 */       for (byte b1 = 0; b1 < this.treatmentCount; b1++) {
/* 295 */         arrayOfDouble[b * this.treatmentCount + b1] = this.data[b][b1];
/*     */       }
/*     */     } 
/* 298 */     return arrayOfDouble;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasBlockLabel(String paramString) {
/* 307 */     return this.blockLabels.contains(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOfBlock(String paramString) {
/* 316 */     return this.blockLabels.indexOf(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTreatmentCount() {
/* 323 */     return this.treatmentCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] getTreatmentData(String paramString) {
/* 334 */     int i = this.treatmentLabels.indexOf(paramString);
/* 335 */     if (i < 0) return null; 
/* 336 */     return getTreatmentData(i);
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
/*     */   public double[] getTreatmentData(int paramInt) {
/* 350 */     double[] arrayOfDouble = new double[this.blockCount];
/* 351 */     for (byte b = 0; b < this.blockCount; ) { arrayOfDouble[b] = this.data[b][paramInt]; b++; }
/* 352 */      return arrayOfDouble;
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
/*     */   public String getTreatmentLabel(int paramInt) {
/* 364 */     return this.treatmentLabels.get(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector getTreatmentLabels() {
/* 371 */     return this.treatmentLabels;
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
/*     */   public double getTreatmentMean(int paramInt) {
/* 383 */     double d = 0.0D;
/* 384 */     for (byte b = 0; b < this.blockCount; ) { d += this.data[b][paramInt]; b++; }
/* 385 */      return d / this.blockCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] getTreatmentPackedCopy() {
/* 394 */     double[] arrayOfDouble = new double[this.blockCount * this.treatmentCount];
/* 395 */     for (byte b = 0; b < this.blockCount; b++) {
/* 396 */       for (byte b1 = 0; b1 < this.treatmentCount; b1++) {
/* 397 */         arrayOfDouble[b + b1 * this.blockCount] = this.data[b][b1];
/*     */       }
/*     */     } 
/* 400 */     return arrayOfDouble;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasTreatmentLabel(String paramString) {
/* 409 */     return this.treatmentLabels.contains(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOfTreatment(String paramString) {
/* 418 */     return this.treatmentLabels.indexOf(paramString);
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
/*     */   public int rankByBlocks(double paramDouble) {
/* 439 */     int i = 0;
/*     */     
/* 441 */     for (byte b = 0; b < this.blockCount; b++) {
/*     */       
/* 443 */       Rank rank = new Rank(getBlockData(b), paramDouble);
/* 444 */       i += rank.getCorrectionFactor1();
/* 445 */       for (byte b1 = 0; b1 < this.treatmentCount; b1++)
/* 446 */         this.data[b][b1] = rank.getRank(b1); 
/*     */     } 
/* 448 */     return i;
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
/*     */   public double sweepByBlocks() {
/* 463 */     double d = 0.0D;
/* 464 */     for (byte b = 0; b < this.blockCount; b++) {
/*     */       
/* 466 */       double d1 = getBlockMean(b);
/* 467 */       for (byte b1 = 0; b1 < this.treatmentCount; b1++) {
/*     */         
/* 469 */         this.data[b][b1] = this.data[b][b1] - d1;
/* 470 */         d += this.data[b][b1] * this.data[b][b1];
/*     */       } 
/*     */     } 
/* 473 */     return d;
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
/*     */   public double sweepByTreatments() {
/* 488 */     double d = 0.0D;
/* 489 */     for (byte b = 0; b < this.treatmentCount; b++) {
/*     */       
/* 491 */       double d1 = getTreatmentMean(b);
/* 492 */       for (byte b1 = 0; b1 < this.blockCount; b1++) {
/*     */         
/* 494 */         this.data[b1][b] = this.data[b1][b] - d1;
/* 495 */         d += this.data[b1][b] * this.data[b1][b];
/*     */       } 
/*     */     } 
/* 498 */     return d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 509 */     StringBuffer stringBuffer = new StringBuffer();
/* 510 */     stringBuffer.append("\nMatched data\n"); byte b1;
/* 511 */     for (b1 = 0; b1 < this.treatmentCount; ) { stringBuffer.append("\t" + getTreatmentLabel(b1)); b1++; }
/* 512 */      for (byte b2 = 0; b2 < this.blockCount; b2++) {
/*     */       
/* 514 */       stringBuffer.append("\n" + getBlockLabel(b2));
/* 515 */       for (b1 = 0; b1 < this.treatmentCount; ) { stringBuffer.append("\t" + this.data[b2][b1]); b1++; }
/*     */     
/* 517 */     }  stringBuffer.append("\n");
/* 518 */     return stringBuffer.toString();
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 553 */       double[][] arrayOfDouble = { { 3.93D, 3.99D, 4.08D }, { 3.78D, 3.96D, 3.94D }, { 3.88D, 3.96D, 4.02D }, { 3.93D, 4.03D, 4.06D }, { 3.84D, 4.1D, 3.94D }, { 3.75D, 4.02D, 4.09D }, { 3.98D, 4.06D, 4.17D }, { 3.84D, 3.92D, 4.12D } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 562 */       MatchedData matchedData = new MatchedData(arrayOfDouble);
/*     */       
/* 564 */       double d = matchedData.sweepByBlocks();
/* 565 */       System.out.print(matchedData.toString());
/* 566 */       System.out.print("rss = " + d);
/* 567 */       d = matchedData.sweepByTreatments();
/* 568 */       System.out.print(matchedData.toString());
/* 569 */       System.out.print("rss = " + d);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/datastructures/MatchedData.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */