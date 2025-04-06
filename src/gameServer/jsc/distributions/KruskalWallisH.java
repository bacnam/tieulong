/*     */ package jsc.distributions;
/*     */ 
/*     */ import jsc.combinatorics.MultiSetPermutation;
/*     */ import jsc.combinatorics.MultiSetPermutations;
/*     */ import jsc.descriptive.DoubleTally;
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
/*     */ public class KruskalWallisH
/*     */   extends Discrete
/*     */ {
/*  32 */   private double tolerance = 1.0E-14D;
/*     */ 
/*     */   
/*     */   private final int[] ns;
/*     */ 
/*     */   
/*     */   private final double Rbar;
/*     */ 
/*     */   
/*     */   private final double c;
/*     */ 
/*     */   
/*     */   private int[] ranks;
/*     */   
/*     */   private int[] count;
/*     */   
/*     */   private double[] R;
/*     */   
/*     */   private DoubleTally H;
/*     */ 
/*     */   
/*     */   public KruskalWallisH(int[] paramArrayOfint) {
/*  54 */     this.ns = paramArrayOfint;
/*  55 */     MultiSetPermutations multiSetPermutations = new MultiSetPermutations(paramArrayOfint);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  66 */     this.H = new DoubleTally(1500, 100, this.tolerance);
/*     */ 
/*     */ 
/*     */     
/*  70 */     int i = paramArrayOfint.length;
/*  71 */     this.R = new double[i];
/*     */     
/*  73 */     int j = (int)Arrays.sum(paramArrayOfint);
/*  74 */     this.ranks = new int[j];
/*  75 */     this.count = new int[i];
/*  76 */     this.c = 12.0D / j * (j + 1.0D);
/*  77 */     this.Rbar = 0.5D * (j + 1.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     while (multiSetPermutations.hasNext()) {
/*  90 */       double d = 0.0D;
/*     */       
/*  92 */       MultiSetPermutation multiSetPermutation = multiSetPermutations.nextPermutation();
/*     */       
/*  94 */       int[] arrayOfInt = multiSetPermutation.toIntArray();
/*     */       
/*  96 */       this.count[0] = 0; byte b1;
/*  97 */       for (b1 = 1; b1 < i; ) { this.count[b1] = this.count[b1 - 1] + this.ns[b1 - 1]; b1++; }
/*     */ 
/*     */ 
/*     */       
/* 101 */       for (b1 = 0; b1 < j; b1++) {
/*     */         
/* 103 */         int k = arrayOfInt[b1] - 1;
/* 104 */         this.ranks[this.count[k]] = b1 + 1;
/* 105 */         this.count[k] = this.count[k] + 1;
/*     */       } 
/*     */ 
/*     */       
/* 109 */       byte b2 = 0;
/* 110 */       for (b1 = 0; b1 < i; b1++) {
/*     */         
/* 112 */         this.R[b1] = 0.0D;
/* 113 */         for (byte b = 0; b < this.ns[b1]; ) { this.R[b1] = this.R[b1] + this.ranks[b2++]; b++; }
/*     */       
/*     */       } 
/*     */       
/* 117 */       for (b1 = 0; b1 < i; ) { this.R[b1] = this.R[b1] / this.ns[b1]; b1++; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 122 */       for (b1 = 0; b1 < i; b1++) {
/*     */         
/* 124 */         double d1 = this.R[b1] - this.Rbar;
/*     */         
/* 126 */         d += this.ns[b1] * d1 * d1;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 131 */       this.H.addValue(this.c * d);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     setDistribution(this.H);
/*     */ 
/*     */     
/* 150 */     this.H = null;
/* 151 */     this.R = null;
/*     */     
/* 153 */     this.ranks = null;
/* 154 */     this.count = null;
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
/*     */   public double criticalValue(double paramDouble) {
/* 169 */     if (paramDouble < 0.0D || paramDouble > 1.0D) throw new IllegalArgumentException("Invalid probability."); 
/* 170 */     double d = 0.0D;
/* 171 */     for (int i = this.valueCount - 1; i >= 0; i--) {
/*     */       
/* 173 */       d += this.probs[i];
/*     */       
/* 175 */       if (Math.abs(d - paramDouble) < 1.0E-16D) return this.values[i]; 
/* 176 */       if (d > paramDouble) return (i < this.valueCount - 1) ? this.values[i + 1] : -1.0D; 
/*     */     } 
/* 178 */     return this.minValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 183 */     StringBuffer stringBuffer = new StringBuffer();
/* 184 */     stringBuffer.append("Kruskal-Wallis H distribution; sample sizes");
/* 185 */     for (byte b = 0; b < this.valueCount; ) { stringBuffer.append(" " + this.ns[b]); b++; }
/* 186 */      return stringBuffer.toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 338 */       int[] arrayOfInt = { 4, 3, 2 };
/*     */       
/* 340 */       long l1 = System.currentTimeMillis();
/* 341 */       KruskalWallisH kruskalWallisH = new KruskalWallisH(arrayOfInt);
/* 342 */       long l2 = System.currentTimeMillis();
/* 343 */       System.out.println("Time = " + ((l2 - l1) / 1000L) + " secs");
/* 344 */       System.out.println("Kruskal-Wallis H distribution");
/* 345 */       double d = 0.0D;
/* 346 */       int i = kruskalWallisH.getValueCount();
/* 347 */       for (byte b = 0; b < i; b++) {
/*     */         
/* 349 */         System.out.println("P(X = " + kruskalWallisH.getValue(b) + ") = " + kruskalWallisH.getProb(b));
/* 350 */         d += kruskalWallisH.getProb(b);
/*     */       } 
/* 352 */       System.out.println("N = " + i + " sum = " + d + " Crit.value = " + kruskalWallisH.criticalValue(0.01D));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/KruskalWallisH.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */