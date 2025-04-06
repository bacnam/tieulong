/*     */ package jsc.distributions;
/*     */ 
/*     */ import jsc.combinatorics.Permutation;
/*     */ import jsc.combinatorics.Permutations;
/*     */ import jsc.descriptive.DoubleTally;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RankSumOfSquares
/*     */   extends Discrete
/*     */ {
/*     */   protected int n;
/*     */   protected int k;
/*  43 */   private double tolerance = 1.0E-14D;
/*     */ 
/*     */ 
/*     */   
/*     */   private double Rbar;
/*     */ 
/*     */ 
/*     */   
/*     */   private int[] perm;
/*     */ 
/*     */ 
/*     */   
/*     */   private int[][] R;
/*     */ 
/*     */ 
/*     */   
/*     */   private DoubleTally S;
/*     */ 
/*     */   
/*     */   private Permutations[] perms;
/*     */ 
/*     */ 
/*     */   
/*     */   public RankSumOfSquares(int paramInt1, int paramInt2) {
/*  67 */     this.n = paramInt1;
/*  68 */     this.k = paramInt2;
/*  69 */     if (paramInt1 < 2)
/*  70 */       throw new IllegalArgumentException("Less than two rankings."); 
/*  71 */     if (paramInt2 < 2) {
/*  72 */       throw new IllegalArgumentException("Less than two objects.");
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
/*  83 */     this.S = new DoubleTally(166, 10, this.tolerance);
/*     */     
/*  85 */     this.R = new int[paramInt1][paramInt2];
/*  86 */     this.perm = new int[paramInt2];
/*  87 */     this.Rbar = 0.5D * paramInt1 * (paramInt2 + 1.0D);
/*     */ 
/*     */ 
/*     */     
/*  91 */     byte b2 = 0;
/*  92 */     for (byte b1 = 0; b1 < paramInt2; ) { this.R[0][b1] = b1 + 1; b1++; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     this.perms = new Permutations[paramInt1];
/* 124 */     for (b2 = 0; b2 < paramInt1; ) { this.perms[b2] = new Permutations(paramInt2); b2++; }
/*     */     
/* 126 */     recurse(1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 134 */     setDistribution(this.S);
/*     */ 
/*     */     
/* 137 */     this.S = null;
/* 138 */     this.R = null;
/* 139 */     this.perm = null;
/* 140 */     this.perms = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void recurse(int paramInt) {
/* 150 */     while (this.perms[paramInt].hasNext()) {
/*     */       
/* 152 */       Permutation permutation = this.perms[paramInt].nextPermutation();
/*     */       
/* 154 */       this.perm = permutation.toIntArray();
/* 155 */       for (byte b = 0; b < this.k; ) { this.R[paramInt][b] = this.perm[b]; b++; }
/* 156 */        if (paramInt == this.n - 1) {
/*     */         
/* 158 */         this.S.addValue(getS());
/*     */         continue;
/*     */       } 
/* 161 */       recurse(paramInt + 1);
/* 162 */       this.perms[paramInt + 1].reset();
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
/*     */   public double criticalValue(double paramDouble) {
/* 179 */     if (paramDouble < 0.0D || paramDouble > 1.0D) throw new IllegalArgumentException("Invalid probability."); 
/* 180 */     double d = 0.0D;
/* 181 */     for (int i = this.valueCount - 1; i >= 0; i--) {
/*     */       
/* 183 */       d += this.probs[i];
/* 184 */       if (d == paramDouble) return this.values[i];
/*     */       
/* 186 */       if (d > paramDouble) return (i < this.valueCount - 1) ? this.values[i + 1] : -1.0D; 
/*     */     } 
/* 188 */     return this.minValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double getS() {
/* 195 */     double d1 = 0.0D;
/* 196 */     double d2 = 0.0D;
/*     */ 
/*     */ 
/*     */     
/* 200 */     for (byte b = 0; b < this.k; b++) {
/*     */ 
/*     */       
/* 203 */       d1 = 0.0D;
/* 204 */       for (byte b1 = 0; b1 < this.n; ) { d1 += this.R[b1][b]; b1++; }
/* 205 */        double d = d1 - this.Rbar;
/* 206 */       d2 += d * d;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 211 */     return d2;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 215 */     return new String("Rank sum of squares distribution: " + this.n + " blocks, " + this.k + " treatments.");
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
/* 227 */       byte b = -2; byte b1 = 2;
/* 228 */       System.out.println("n = " + b + " m = " + b1);
/*     */       
/* 230 */       long l1 = System.currentTimeMillis();
/* 231 */       RankSumOfSquares rankSumOfSquares = new RankSumOfSquares(b1, b);
/* 232 */       long l2 = System.currentTimeMillis();
/* 233 */       System.out.println("Time = " + ((l2 - l1) / 1000L) + " secs");
/* 234 */       for (byte b2 = 0; b2 < rankSumOfSquares.getValueCount(); b2++) {
/*     */         
/* 236 */         int i = (int)rankSumOfSquares.getValue(b2);
/* 237 */         System.out.println("S = " + i + " P = " + rankSumOfSquares.upperTailProb(i));
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/RankSumOfSquares.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */