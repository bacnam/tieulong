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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IrregularFrequencyTable
/*     */   extends AbstractFrequencyTable
/*     */   implements DoubleFrequencyTable, Cloneable
/*     */ {
/*     */   double[] boundaries;
/*     */   
/*     */   public IrregularFrequencyTable(String paramString, double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) {
/*  27 */     this(paramString, paramArrayOfdouble1);
/*  28 */     for (byte b = 0; b < paramArrayOfdouble2.length; ) { addValue(paramArrayOfdouble2[b]); b++; }
/*  29 */      if (this.n < 1) throw new IllegalArgumentException("No data values.");
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
/*     */   public IrregularFrequencyTable(String paramString, double[] paramArrayOfdouble) {
/*  41 */     super(paramString);
/*  42 */     this.numberOfBins = paramArrayOfdouble.length - 1;
/*     */     
/*  44 */     this.boundaries = paramArrayOfdouble;
/*     */     
/*  46 */     this.n = 0;
/*  47 */     this.freq = new int[this.numberOfBins];
/*  48 */     for (byte b = 0; b < this.numberOfBins; ) { this.freq[b] = 0; b++; }
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
/*     */ 
/*     */   
/*     */   public IrregularFrequencyTable(String paramString, double[] paramArrayOfdouble, int[] paramArrayOfint) {
/*  64 */     this(paramString, paramArrayOfdouble);
/*     */     
/*  66 */     if (paramArrayOfint.length != this.numberOfBins) {
/*  67 */       throw new IllegalArgumentException("Number of frequencies should be one less than number of boundaries.");
/*     */     }
/*     */     
/*  70 */     for (byte b = 0; b < this.numberOfBins; ) { this.freq[b] = paramArrayOfint[b]; this.n += paramArrayOfint[b]; b++; }
/*  71 */      if (this.n < 1) throw new IllegalArgumentException("No data values.");
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
/*     */   public int addValue(double paramDouble) {
/*     */     int i;
/*  85 */     for (i = 0; i < this.numberOfBins; i++) {
/*  86 */       if (this.boundaries[i] <= paramDouble && paramDouble < this.boundaries[i + 1]) {
/*  87 */         this.n++; this.freq[i] = this.freq[i] + 1; return i;
/*     */       } 
/*  89 */     }  if (paramDouble == this.boundaries[this.numberOfBins]) {
/*     */       
/*  91 */       i = this.numberOfBins - 1;
/*  92 */       this.n++;
/*  93 */       this.freq[i] = this.freq[i] + 1;
/*  94 */       return i;
/*     */     } 
/*  96 */     return -1;
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
/*     */   public int addValues(double[] paramArrayOfdouble) {
/* 108 */     byte b1 = 0;
/* 109 */     for (byte b2 = 0; b2 < paramArrayOfdouble.length; b2++) {
/* 110 */       if (addValue(paramArrayOfdouble[b2]) >= 0) b1++; 
/* 111 */     }  return b1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getBoundary(int paramInt) {
/* 120 */     return this.boundaries[paramInt];
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/* 125 */     Object object = null; try {
/* 126 */       object = super.clone();
/*     */     } catch (CloneNotSupportedException cloneNotSupportedException) {
/* 128 */       System.out.println("IrregularFrequencyTable can't clone");
/* 129 */     }  return object;
/*     */   }
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
/* 141 */       double[] arrayOfDouble1 = { 72.2D, 64.0D, 53.4D, 76.8D, 86.3D, 58.1D, 63.2D, 73.1D, 78.0D, 44.3D, 85.1D, 66.6D, 80.4D, 76.0D, 68.8D, 76.8D, 58.9D, 58.1D, 74.9D, 72.2D, 73.1D, 39.3D, 52.8D, 54.2D, 65.3D, 74.0D, 63.2D, 64.7D, 68.8D, 85.1D, 62.2D, 76.0D, 70.5D, 48.9D, 78.0D, 66.6D, 58.1D, 32.5D, 63.2D, 64.0D, 68.8D, 65.3D, 71.9D, 72.2D, 63.2D, 72.2D, 70.5D, 80.4D, 45.4D, 59.6D };
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 146 */       double[] arrayOfDouble2 = { 0.0D, 40.0D, 50.0D, 55.0D, 60.0D, 65.0D, 70.0D, 75.0D, 100.0D };
/* 147 */       IrregularFrequencyTable irregularFrequencyTable = new IrregularFrequencyTable("Table", arrayOfDouble2, arrayOfDouble1);
/*     */ 
/*     */ 
/*     */       
/* 151 */       int[] arrayOfInt = { 2, 3, 3, 5, 8, 7, 11, 11 };
/*     */       
/* 153 */       System.out.println("Frequency table " + irregularFrequencyTable.getN() + " values");
/* 154 */       for (byte b = 0; b < irregularFrequencyTable.getNumberOfBins(); b++)
/* 155 */         System.out.println(irregularFrequencyTable.getBoundary(b) + " to " + irregularFrequencyTable.getBoundary(b + 1) + ", Freq = " + irregularFrequencyTable.getFrequency(b) + ", % = " + irregularFrequencyTable.getPercentage(b)); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/descriptive/IrregularFrequencyTable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */