/*     */ package jsc.descriptive;
/*     */ 
/*     */ import jsc.util.Scale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FrequencyTable
/*     */   extends AbstractFrequencyTable
/*     */   implements DoubleFrequencyTable, Cloneable
/*     */ {
/*     */   Scale scale;
/*     */   
/*     */   public FrequencyTable(String paramString, int paramInt, double[] paramArrayOfdouble) {
/*  36 */     this(paramString, paramInt, paramArrayOfdouble, false);
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
/*     */   public FrequencyTable(String paramString, int paramInt, double[] paramArrayOfdouble, boolean paramBoolean) {
/*  51 */     super(paramString);
/*  52 */     this.numberOfBins = paramInt;
/*     */ 
/*     */ 
/*     */     
/*  56 */     double d1 = Double.POSITIVE_INFINITY;
/*  57 */     double d2 = Double.NEGATIVE_INFINITY; byte b;
/*  58 */     for (b = 0; b < paramArrayOfdouble.length; b++) {
/*     */       
/*  60 */       if (paramArrayOfdouble[b] < d1) d1 = paramArrayOfdouble[b]; 
/*  61 */       if (paramArrayOfdouble[b] > d2) d2 = paramArrayOfdouble[b];
/*     */     
/*     */     } 
/*     */     
/*  65 */     this.scale = new Scale(d1, d2, paramInt + 1, true, false);
/*     */     
/*  67 */     if (this.scale.getStep() == 0.0D) {
/*  68 */       throw new IllegalArgumentException("Constant data");
/*     */     }
/*  70 */     if (paramBoolean) {
/*     */       
/*  72 */       double d = 0.5D * this.scale.getStep();
/*  73 */       this.scale = new Scale(this.scale.getFirstTickValue() - d, this.scale.getLastTickValue() + d, paramInt, false, false);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  78 */     this.n = 0;
/*  79 */     this.freq = new int[paramInt];
/*  80 */     for (b = 0; b < paramInt; ) { this.freq[b] = 0; b++; }
/*  81 */      for (b = 0; b < paramArrayOfdouble.length; ) { addValue(paramArrayOfdouble[b]); b++; }
/*  82 */      if (this.n < 1) {
/*  83 */       throw new IllegalArgumentException("No data values.");
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
/*     */   public FrequencyTable(String paramString, double paramDouble1, double paramDouble2, double paramDouble3) {
/*  99 */     super(paramString);
/* 100 */     if (paramDouble3 <= 0.0D || paramDouble2 <= paramDouble1) {
/* 101 */       throw new IllegalArgumentException("Invalid bins");
/*     */     }
/*     */     
/* 104 */     this.numberOfBins = (int)Math.ceil((paramDouble2 - paramDouble1) / paramDouble3);
/*     */ 
/*     */     
/* 107 */     this.scale = new Scale(paramDouble1, paramDouble2, this.numberOfBins + 1, false, false);
/*     */     
/* 109 */     this.n = 0;
/* 110 */     this.freq = new int[this.numberOfBins];
/* 111 */     for (byte b = 0; b < this.numberOfBins; ) { this.freq[b] = 0; b++; }
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
/*     */   
/*     */   public FrequencyTable(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, double[] paramArrayOfdouble) {
/* 129 */     super(paramString);
/* 130 */     if (paramDouble3 <= 0.0D || paramDouble2 <= paramDouble1) {
/* 131 */       throw new IllegalArgumentException("Invalid bins");
/*     */     }
/*     */     
/* 134 */     this.numberOfBins = (int)Math.ceil((paramDouble2 - paramDouble1) / paramDouble3);
/*     */ 
/*     */     
/* 137 */     this.scale = new Scale(paramDouble1, paramDouble2, this.numberOfBins + 1, false, false);
/*     */ 
/*     */     
/* 140 */     this.n = 0;
/* 141 */     this.freq = new int[this.numberOfBins]; byte b;
/* 142 */     for (b = 0; b < this.numberOfBins; ) { this.freq[b] = 0; b++; }
/* 143 */      for (b = 0; b < paramArrayOfdouble.length; ) { addValue(paramArrayOfdouble[b]); b++; }
/* 144 */      if (this.n < 1) {
/* 145 */       throw new IllegalArgumentException("No data values.");
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
/*     */   public FrequencyTable(String paramString, double paramDouble1, double paramDouble2, int[] paramArrayOfint) {
/* 163 */     super(paramString);
/* 164 */     if (paramDouble2 <= paramDouble1) {
/* 165 */       throw new IllegalArgumentException("Invalid bins");
/*     */     }
/*     */     
/* 168 */     this.numberOfBins = paramArrayOfint.length;
/*     */ 
/*     */     
/* 171 */     this.scale = new Scale(paramDouble1, paramDouble2, this.numberOfBins + 1, false);
/*     */ 
/*     */     
/* 174 */     this.n = 0;
/* 175 */     this.freq = new int[this.numberOfBins];
/* 176 */     for (byte b = 0; b < this.numberOfBins; ) { this.freq[b] = paramArrayOfint[b]; this.n += paramArrayOfint[b]; b++; }
/* 177 */      if (this.n < 1) {
/* 178 */       throw new IllegalArgumentException("No data values.");
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
/*     */   public int addValue(double paramDouble) {
/* 194 */     if (paramDouble >= this.scale.getFirstTickValue() && paramDouble <= this.scale.getLastTickValue()) {
/*     */       
/* 196 */       this.n++;
/*     */       
/* 198 */       int i = (int)Math.floor((paramDouble - this.scale.getFirstTickValue()) / this.scale.getStep());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 205 */       if (i < 0) { i = 0; }
/* 206 */       else if (i >= this.numberOfBins) { i = this.numberOfBins - 1; }
/*     */ 
/*     */       
/* 209 */       this.freq[i] = this.freq[i] + 1;
/*     */       
/* 211 */       return i;
/*     */     } 
/* 213 */     return -1;
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
/* 225 */     byte b1 = 0;
/* 226 */     for (byte b2 = 0; b2 < paramArrayOfdouble.length; b2++) {
/* 227 */       if (addValue(paramArrayOfdouble[b2]) >= 0) b1++; 
/* 228 */     }  return b1;
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
/*     */   public double getBinWidth() {
/* 242 */     return this.scale.getStep();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getBoundary(int paramInt) {
/* 250 */     return this.scale.getTickValue(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/* 255 */     Object object = null; try {
/* 256 */       object = super.clone();
/*     */     } catch (CloneNotSupportedException cloneNotSupportedException) {
/* 258 */       System.out.println("FrequencyTable can't clone");
/* 259 */     }  return object;
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
/*     */   public double getMaximumNormalizedFreq() {
/* 271 */     int i = getMaximumFreq();
/* 272 */     if (i > 0) {
/*     */       
/* 274 */       double d = this.n * this.scale.getStep();
/* 275 */       if (d > 0.0D) return i / d; 
/*     */     } 
/* 277 */     return 1.0D;
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
/*     */   public double getNormalizedFrequency(int paramInt) {
/* 290 */     double d = this.n * this.scale.getStep();
/* 291 */     if (d > 0.0D) {
/* 292 */       return this.freq[paramInt] / d;
/*     */     }
/* 294 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public Scale getScale() {
/* 298 */     return this.scale;
/*     */   }
/*     */   public double getScaleMax() {
/* 301 */     return this.scale.getLastTickValue();
/*     */   }
/*     */   public double getScaleMin() {
/* 304 */     return this.scale.getFirstTickValue();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 308 */     return "FrequencyTable: " + super.toString() + " " + this.scale.toString();
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 322 */       double[] arrayOfDouble = { 39.0D, 41.0D, 41.0D, 41.0D, 41.0D, 43.0D, 43.0D, 45.0D, 45.0D, 48.0D, 69.0D, 83.0D, 83.0D, 83.0D, 83.0D, 91.0D, 179.0D, 238.0D, 241.0D, 253.0D, 47.0D, 274.0D, 280.0D, 394.0D, 501.0D, 503.0D, 509.0D, 511.0D, 513.0D, 515.0D, 518.0D, 518.0D, 520.0D, 522.0D, 522.0D, 522.0D, 525.0D, 527.0D, 527.0D, 527.0D, 529.0D, 529.0D, 531.0D, 531.0D, 533.0D, 535.0D, 538.0D, 538.0D, 541.0D, 543.0D, 545.0D, 547.0D, 547.0D, 549.0D, 549.0D, 549.0D, 549.0D, 551.0D, 553.0D, 555.0D, 555.0D, 555.0D, 557.0D, 560.0D, 560.0D, 562.0D, 564.0D, 568.0D, 570.0D, 572.0D, 574.0D, 576.0D, 579.0D, 581.0D, 583.0D, 585.0D, 578.0D, 578.0D, 589.0D, 590.0D, 592.0D, 592.0D, 596.0D, 598.0D, 598.0D, 598.0D, 301.0D, 601.0D, 606.0D, 609.0D, 611.0D, 615.0D, 617.0D, 617.0D, 619.0D, 621.0D, 623.0D, 623.0D, 626.0D, 626.0D, 628.0D, 630.0D, 630.0D, 630.0D, 630.0D, 630.0D, 632.0D, 634.0D, 636.0D, 638.0D, 640.0D, 640.0D, 642.0D, 644.0D, 644.0D, 646.0D, 648.0D, 648.0D, 648.0D, 650.0D, 652.0D, 655.0D, 657.0D, 659.0D, 661.0D, 661.0D, 663.0D, 665.0D, 670.0D, 673.0D, 675.0D, 677.0D, 679.0D, 681.0D, 683.0D, 909.0D, 911.0D, 907.0D, 555.0D, 555.0D, 692.0D, 694.0D, 657.0D };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 330 */       FrequencyTable frequencyTable = new FrequencyTable("Table", 9, arrayOfDouble, false);
/*     */       
/* 332 */       System.out.println("Frequency table " + frequencyTable.getN() + " values");
/* 333 */       for (byte b = 0; b < frequencyTable.getNumberOfBins(); b++)
/* 334 */         System.out.println(frequencyTable.getBoundary(b) + " to " + frequencyTable.getBoundary(b + 1) + ", Freq = " + frequencyTable.getFrequency(b) + ", % = " + frequencyTable.getPercentage(b)); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/descriptive/FrequencyTable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */