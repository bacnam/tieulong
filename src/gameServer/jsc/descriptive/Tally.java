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
/*     */ public class Tally
/*     */   implements Cloneable
/*     */ {
/*     */   private int n;
/*     */   private int min;
/*     */   private int max;
/*     */   private int k;
/*     */   private int[] freq;
/*     */   
/*     */   public Tally(int[] paramArrayOfint) {
/*  28 */     this.min = Integer.MAX_VALUE;
/*  29 */     this.max = Integer.MIN_VALUE; byte b;
/*  30 */     for (b = 0; b < paramArrayOfint.length; b++) {
/*     */       
/*  32 */       if (paramArrayOfint[b] < this.min) this.min = paramArrayOfint[b]; 
/*  33 */       if (paramArrayOfint[b] > this.max) this.max = paramArrayOfint[b];
/*     */     
/*     */     } 
/*  36 */     this.k = 1 + this.max - this.min;
/*     */     
/*  38 */     this.freq = new int[this.k];
/*  39 */     clearData();
/*  40 */     for (b = 0; b < paramArrayOfint.length; ) { addValue(paramArrayOfint[b]); b++; }
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
/*     */   public Tally(int paramInt1, int paramInt2) {
/*  55 */     if (paramInt2 <= paramInt1) {
/*  56 */       throw new IllegalArgumentException("Invalid bins");
/*     */     }
/*  58 */     this.k = 1 + paramInt2 - paramInt1;
/*  59 */     this.min = paramInt1;
/*  60 */     this.max = paramInt2;
/*     */     
/*  62 */     this.freq = new int[this.k];
/*  63 */     clearData();
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
/*     */   public Tally(int paramInt1, int paramInt2, int[] paramArrayOfint) {
/*  75 */     this(paramInt1, paramInt2);
/*     */ 
/*     */     
/*  78 */     for (byte b = 0; b < paramArrayOfint.length; ) { addValue(paramArrayOfint[b]); b++; }
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
/*     */   
/*     */   public Tally(int paramInt, int[] paramArrayOfint) {
/*  95 */     this.k = paramArrayOfint.length;
/*  96 */     this.min = paramInt;
/*  97 */     this.max = paramInt + this.k - 1;
/*  98 */     this.freq = new int[this.k];
/*  99 */     this.n = 0;
/* 100 */     for (byte b = 0; b < this.k; ) { this.freq[b] = paramArrayOfint[b]; this.n += paramArrayOfint[b]; b++; }
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
/*     */   public int addValue(int paramInt) {
/* 112 */     if (paramInt >= this.min && paramInt <= this.max) {
/*     */       
/* 114 */       this.n++;
/* 115 */       int i = paramInt - this.min;
/* 116 */       this.freq[i] = this.freq[i] + 1;
/* 117 */       return i;
/*     */     } 
/*     */     
/* 120 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearData() {
/* 126 */     this.n = 0;
/* 127 */     for (byte b = 0; b < this.k; ) { this.freq[b] = 0; b++; }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/* 133 */     Object object = null; try {
/* 134 */       object = super.clone();
/*     */     } catch (CloneNotSupportedException cloneNotSupportedException) {
/* 136 */       System.out.println("Tally can't clone");
/* 137 */     }  return object;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBinValue(int paramInt) {
/* 146 */     return this.min + paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getFrequencies() {
/* 153 */     return this.freq;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFrequency(int paramInt) {
/* 161 */     return this.freq[paramInt];
/*     */   }
/*     */   public int getMax() {
/* 164 */     return this.max;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxFreq() {
/* 169 */     int i = 0;
/* 170 */     for (byte b = 0; b < this.k; b++) {
/* 171 */       if (this.freq[b] > i) i = this.freq[b]; 
/* 172 */     }  return i;
/*     */   }
/*     */   
/*     */   public int getMin() {
/* 176 */     return this.min;
/*     */   }
/*     */   public int getNumberOfBins() {
/* 179 */     return this.k;
/*     */   }
/*     */   public int getN() {
/* 182 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getPercentage(int paramInt) {
/* 191 */     return (this.n > 0) ? (100.0D * this.freq[paramInt] / this.n) : 0.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getProportion(int paramInt) {
/* 200 */     return (this.n > 0) ? (this.freq[paramInt] / this.n) : 0.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOf(int paramInt) {
/* 211 */     if (paramInt >= this.min && paramInt <= this.max) {
/* 212 */       return paramInt - this.min;
/*     */     }
/* 214 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setData(int[] paramArrayOfint) {
/* 225 */     clearData();
/* 226 */     for (byte b = 0; b < paramArrayOfint.length; ) { addValue(paramArrayOfint[b]); b++; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 237 */       int[] arrayOfInt = { 4, 5, 8, 9, 3, 2, 5, 4, 6, 7, 8, 9, 2, 1, 0, -3, -2, 0, -6, -3 };
/* 238 */       Tally tally = new Tally(arrayOfInt);
/*     */       
/* 240 */       System.out.println("Tally " + tally.getN() + " values, " + "Min = " + tally.getMin() + ", Max = " + tally.getMax());
/*     */       byte b;
/* 242 */       for (b = 0; b < tally.getNumberOfBins(); b++) {
/* 243 */         System.out.println(tally.getBinValue(b) + ", Freq = " + tally.getFrequency(b) + ", % = " + tally.getPercentage(b));
/*     */       }
/* 245 */       for (b = 0; b < arrayOfInt.length; b++)
/* 246 */         System.out.println("Index of " + arrayOfInt[b] + " is " + tally.indexOf(arrayOfInt[b])); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/descriptive/Tally.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */