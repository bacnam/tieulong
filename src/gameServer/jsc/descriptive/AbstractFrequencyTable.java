/*     */ package jsc.descriptive;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractFrequencyTable
/*     */ {
/*     */   int n;
/*     */   int numberOfBins;
/*     */   int[] freq;
/*     */   private String name;
/*     */   
/*     */   public AbstractFrequencyTable(String paramString) {
/*  25 */     this.n = 0; this.numberOfBins = 0; this.name = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearData() {
/*  32 */     this.n = 0;
/*  33 */     for (byte b = 0; b < this.numberOfBins; ) { this.freq[b] = 0; b++; }
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
/*     */   public int getCumulativeFrequency(int paramInt) {
/*  45 */     int i = 0;
/*  46 */     for (byte b = 0; b <= paramInt; ) { i += this.freq[b]; b++; }
/*  47 */      return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getCumulativePercentage(int paramInt) {
/*  58 */     return 100.0D * getCumulativeProportion(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getCumulativeProportion(int paramInt) {
/*  68 */     return (this.n > 0) ? (getCumulativeFrequency(paramInt) / this.n) : 0.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getFrequencies() {
/*  75 */     return this.freq;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFrequency(int paramInt) {
/*  84 */     return this.freq[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaximumFreq() {
/*  93 */     int i = 0;
/*  94 */     for (byte b = 0; b < this.numberOfBins; b++) {
/*  95 */       if (this.freq[b] > i) i = this.freq[b]; 
/*  96 */     }  return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMaximumProportion() {
/* 105 */     return (this.n > 0) ? (getMaximumFreq() / this.n) : 0.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumberOfBins() {
/* 112 */     return this.numberOfBins;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/* 119 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 126 */     return this.name;
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
/*     */   public double getPercentage(int paramInt) {
/* 147 */     return 100.0D * getProportion(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getProportion(int paramInt) {
/* 157 */     return (this.n > 0) ? (this.freq[paramInt] / this.n) : 0.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String paramString) {
/* 164 */     this.name = paramString;
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
/* 175 */     StringBuffer stringBuffer = new StringBuffer("AbstractFrequencyTable: " + this.name + " from " + this.n + " observations, " + this.numberOfBins + " bins with frequencies:");
/*     */ 
/*     */     
/* 178 */     for (byte b = 0; b < this.numberOfBins; ) { stringBuffer.append(" "); stringBuffer.append(this.freq[b]); b++; }
/* 179 */      return stringBuffer.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/descriptive/AbstractFrequencyTable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */