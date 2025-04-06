/*     */ package jsc.contingencytables;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import jsc.descriptive.CategoricalTally;
/*     */ import jsc.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContingencyTable
/*     */ {
/*     */   private int N;
/*     */   private int rowCount;
/*     */   private int colCount;
/*     */   private Vector rowLabels;
/*     */   private Vector colLabels;
/*     */   private int[][] O;
/*     */   
/*     */   public ContingencyTable(String[] paramArrayOfString1, String[] paramArrayOfString2) {
/*  42 */     this.N = paramArrayOfString1.length;
/*  43 */     if (this.N != paramArrayOfString2.length) {
/*  44 */       throw new IllegalArgumentException("Data arrays do not match.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  49 */     CategoricalTally categoricalTally1 = new CategoricalTally("", paramArrayOfString1);
/*  50 */     CategoricalTally categoricalTally2 = new CategoricalTally("", paramArrayOfString2);
/*     */     
/*  52 */     this.rowCount = categoricalTally1.getNumberOfBins();
/*  53 */     this.colCount = categoricalTally2.getNumberOfBins();
/*     */     
/*  55 */     this.rowLabels = new Vector(this.rowCount);
/*  56 */     this.colLabels = new Vector(this.colCount); byte b;
/*  57 */     for (b = 0; b < this.rowCount; ) { this.rowLabels.add(categoricalTally1.getLabel(b)); b++; }
/*  58 */      for (b = 0; b < this.colCount; ) { this.colLabels.add(categoricalTally2.getLabel(b)); b++; }
/*     */     
/*  60 */     this.O = new int[this.rowCount][this.colCount];
/*     */ 
/*     */     
/*  63 */     for (b = 0; b < this.N; b++) {
/*  64 */       this.O[categoricalTally1.indexOf(paramArrayOfString1[b])][categoricalTally2.indexOf(paramArrayOfString2[b])] = this.O[categoricalTally1.indexOf(paramArrayOfString1[b])][categoricalTally2.indexOf(paramArrayOfString2[b])] + 1;
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
/*     */   public ContingencyTable(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  76 */     this(Arrays.toStringArray(paramArrayOfint1), Arrays.toStringArray(paramArrayOfint2));
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
/*     */   public ContingencyTable(String[] paramArrayOfString1, String[] paramArrayOfString2, int[][] paramArrayOfint) {
/*  98 */     this.rowCount = paramArrayOfString1.length;
/*  99 */     this.colCount = paramArrayOfString2.length;
/*     */ 
/*     */     
/* 102 */     if (this.rowCount < 1)
/* 103 */       throw new IllegalArgumentException("No row labels."); 
/* 104 */     if (this.colCount < 1)
/* 105 */       throw new IllegalArgumentException("No column labels."); 
/* 106 */     if (this.rowCount != paramArrayOfint.length)
/* 107 */       throw new IllegalArgumentException("Number of row labels do not match frequencies.");  byte b;
/* 108 */     for (b = 0; b < this.rowCount; b++) {
/* 109 */       if ((paramArrayOfint[b]).length != this.colCount)
/* 110 */         throw new IllegalArgumentException("Number of column labels do not match frequencies."); 
/*     */     } 
/* 112 */     this.rowLabels = new Vector(this.rowCount);
/* 113 */     this.colLabels = new Vector(this.colCount);
/* 114 */     for (b = 0; b < this.rowCount; ) { this.rowLabels.add(paramArrayOfString1[b]); b++; }
/* 115 */      for (b = 0; b < this.colCount; ) { this.colLabels.add(paramArrayOfString2[b]); b++; }
/*     */ 
/*     */ 
/*     */     
/* 119 */     this.O = new int[this.rowCount][this.colCount];
/* 120 */     this.N = 0;
/* 121 */     for (b = 0; b < this.rowCount; b++) {
/* 122 */       for (byte b1 = 0; b1 < this.colCount; b1++) {
/*     */         
/* 124 */         if (paramArrayOfint[b][b1] < 0)
/* 125 */           throw new IllegalArgumentException("Negative frequency."); 
/* 126 */         this.N += paramArrayOfint[b][b1];
/* 127 */         this.O[b][b1] = paramArrayOfint[b][b1];
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
/*     */   public ContingencyTable(int[] paramArrayOfint1, int[] paramArrayOfint2, int[][] paramArrayOfint) {
/* 148 */     this(Arrays.toStringArray(paramArrayOfint1), Arrays.toStringArray(paramArrayOfint2), paramArrayOfint);
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
/*     */   public ContingencyTable(int[][] paramArrayOfint) {
/* 160 */     this(Arrays.sequence(0, paramArrayOfint.length - 1), Arrays.sequence(0, (paramArrayOfint[0]).length - 1), paramArrayOfint);
/*     */   } public Object clone() {
/* 162 */     return copy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContingencyTable copy() {
/* 172 */     String[] arrayOfString1 = new String[this.rowCount];
/* 173 */     String[] arrayOfString2 = new String[this.colCount];
/* 174 */     int[][] arrayOfInt = new int[this.rowCount][this.colCount];
/*     */     byte b;
/* 176 */     for (b = 0; b < this.rowCount; ) { arrayOfString1[b] = this.rowLabels.get(b); b++; }
/* 177 */      for (b = 0; b < this.colCount; ) { arrayOfString2[b] = this.colLabels.get(b); b++; }
/*     */     
/* 179 */     for (b = 0; b < this.rowCount; b++) {
/* 180 */       for (byte b1 = 0; b1 < this.colCount; b1++)
/* 181 */         arrayOfInt[b][b1] = this.O[b][b1]; 
/*     */     } 
/* 183 */     return new ContingencyTable(arrayOfString1, arrayOfString2, arrayOfInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[][] getFrequencies() {
/* 191 */     return this.O;
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
/*     */   public int getFrequency(int paramInt1, int paramInt2) {
/* 206 */     return this.O[paramInt1][paramInt2];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/* 213 */     return this.N;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRowCount() {
/* 220 */     return this.rowCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getRowData(String paramString) {
/* 231 */     int i = this.rowLabels.indexOf(paramString);
/* 232 */     if (i < 0) return null; 
/* 233 */     return this.O[i];
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
/*     */   public int[] getRowData(int paramInt) {
/* 245 */     return this.O[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRowLabel(int paramInt) {
/* 256 */     return this.rowLabels.get(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector getRowLabels() {
/* 263 */     return this.rowLabels;
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
/*     */   public int[] getRowTotals() {
/* 280 */     int[] arrayOfInt = new int[this.rowCount];
/* 281 */     for (byte b = 0; b < this.rowCount; b++) {
/*     */       
/* 283 */       arrayOfInt[b] = 0;
/* 284 */       for (byte b1 = 0; b1 < this.colCount; ) { arrayOfInt[b] = arrayOfInt[b] + this.O[b][b1]; b1++; }
/*     */     
/* 286 */     }  return arrayOfInt;
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
/*     */   public boolean hasRowLabel(String paramString) {
/* 302 */     return this.rowLabels.contains(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOfRow(String paramString) {
/* 311 */     return this.rowLabels.indexOf(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumnCount() {
/* 318 */     return this.colCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getColumnData(String paramString) {
/* 329 */     int i = this.colLabels.indexOf(paramString);
/* 330 */     if (i < 0) return null; 
/* 331 */     return getColumnData(i);
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
/*     */   public int[] getColumnData(int paramInt) {
/* 345 */     int[] arrayOfInt = new int[this.rowCount];
/* 346 */     for (byte b = 0; b < this.rowCount; ) { arrayOfInt[b] = this.O[b][paramInt]; b++; }
/* 347 */      return arrayOfInt;
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
/*     */   public String getColumnLabel(int paramInt) {
/* 359 */     return this.colLabels.get(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector getColumnLabels() {
/* 366 */     return this.colLabels;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getColumnTotals() {
/* 376 */     int[] arrayOfInt = new int[this.colCount];
/* 377 */     for (byte b = 0; b < this.colCount; b++) {
/*     */       
/* 379 */       arrayOfInt[b] = 0;
/* 380 */       for (byte b1 = 0; b1 < this.rowCount; ) { arrayOfInt[b] = arrayOfInt[b] + this.O[b1][b]; b1++; }
/*     */     
/* 382 */     }  return arrayOfInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasColumnLabel(String paramString) {
/* 391 */     return this.colLabels.contains(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOfColumn(String paramString) {
/* 400 */     return this.colLabels.indexOf(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 410 */     StringBuffer stringBuffer = new StringBuffer();
/* 411 */     stringBuffer.append("\nContingency table\n"); byte b1;
/* 412 */     for (b1 = 0; b1 < this.colCount; ) { stringBuffer.append("\t" + getColumnLabel(b1)); b1++; }
/* 413 */      for (byte b2 = 0; b2 < this.rowCount; b2++) {
/*     */       
/* 415 */       stringBuffer.append("\n" + getRowLabel(b2));
/* 416 */       for (b1 = 0; b1 < this.colCount; ) { stringBuffer.append("\t" + this.O[b2][b1]); b1++; }
/*     */     
/* 418 */     }  stringBuffer.append("\n");
/* 419 */     return stringBuffer.toString();
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
/* 430 */       String[] arrayOfString1 = { "Improved", "Same or worse" };
/* 431 */       String[] arrayOfString2 = { "Placebo", "Drug 1", "Drug 2", "Drug 3", "Drug 4", "Drug 5" };
/* 432 */       int[][] arrayOfInt = { { 8, 12, 21, 15, 14, 19 }, { 22, 18, 9, 15, 16, 11 } };
/* 433 */       ContingencyTable contingencyTable1 = new ContingencyTable(arrayOfString1, arrayOfString2, arrayOfInt);
/* 434 */       System.out.println(contingencyTable1.toString());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 475 */       int[] arrayOfInt1 = { 2, 1, 2, 2, 2, 3, 3, 2, 1, 1, 2, 3, 2, 3, 2, 3, 1, 2, 3, 2 };
/* 476 */       int[] arrayOfInt2 = { 8, 8, 9, 8, 9, 9, 9, 8, 8, 9, 8, 8, 9, 8, 8, 9, 9, 8, 8, 9 };
/* 477 */       contingencyTable1 = new ContingencyTable(arrayOfInt1, arrayOfInt2);
/* 478 */       System.out.println(contingencyTable1.toString());
/* 479 */       ContingencyTable contingencyTable2 = contingencyTable1.copy();
/* 480 */       System.out.println(contingencyTable2.toString());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/contingencytables/ContingencyTable.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */