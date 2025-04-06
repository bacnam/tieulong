/*     */ package jsc.combinatorics;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Random;
/*     */ import jsc.util.Arrays;
/*     */ import jsc.util.Maths;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultiSetPermutations
/*     */   implements Enumerator
/*     */ {
/*     */   private boolean hasNext;
/*     */   private Random rand;
/*     */   private long count;
/*     */   private int k;
/*     */   private int k1;
/*     */   private int N;
/*     */   private int[] randPerm;
/*     */   private int[] ns;
/*     */   private int[] r;
/*     */   private int[][] A;
/*     */   private final double permutationCount;
/*     */   
/*     */   public MultiSetPermutations(int paramInt) {
/*  85 */     this(Arrays.fill(paramInt, 1));
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
/*     */   public MultiSetPermutations(int[] paramArrayOfint) {
/* 102 */     this.k = paramArrayOfint.length;
/*     */     
/* 104 */     this.permutationCount = Maths.multinomialCoefficient(paramArrayOfint);
/*     */     
/* 106 */     this.ns = paramArrayOfint;
/* 107 */     this.r = new int[this.k + 1];
/* 108 */     System.arraycopy(paramArrayOfint, 0, this.r, 1, this.k);
/*     */     
/* 110 */     this.N = 0; byte b2;
/* 111 */     for (b2 = 1; b2 <= this.k; b2++) {
/*     */       
/* 113 */       if (this.r[b2] == 0)
/* 114 */         throw new IllegalArgumentException("Empty subset."); 
/* 115 */       this.N += this.r[b2];
/*     */     } 
/* 117 */     if (this.N < 2)
/* 118 */       throw new IllegalArgumentException("Less than 2 objects."); 
/* 119 */     this.A = new int[this.k + 1][this.N + 1];
/*     */ 
/*     */ 
/*     */     
/* 123 */     this.rand = new Random();
/*     */     
/* 125 */     this.randPerm = new int[this.N];
/*     */     
/* 127 */     byte b3 = 0;
/* 128 */     for (byte b1 = 0; b1 < this.k; b1++) {
/* 129 */       for (b2 = 0; b2 < paramArrayOfint[b1]; b2++) {
/* 130 */         this.randPerm[b3] = b1 + 1; b3++;
/*     */       } 
/* 132 */     }  reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double countSelections() {
/* 143 */     return this.permutationCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/* 152 */     return this.N;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private MultiSetPermutation getPermutation() {
/* 158 */     int[] arrayOfInt = new int[this.N];
/* 159 */     for (byte b = 1; b <= this.N; ) { arrayOfInt[b - 1] = this.A[this.k][b]; b++; }
/* 160 */      if (this.count > this.permutationCount) this.hasNext = false; 
/* 161 */     return new MultiSetPermutation(arrayOfInt, this.ns, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/* 170 */     return this.hasNext;
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
/*     */   private void moveMarks(int paramInt1, int paramInt2) {
/*     */     boolean bool;
/*     */     do {
/* 187 */       bool = false;
/* 188 */       for (byte b = 1; b <= paramInt2; b++) {
/*     */         
/* 190 */         int i = b + 1;
/* 191 */         if (this.A[paramInt1][b] == paramInt1 + 1 && this.A[paramInt1][i] == paramInt1)
/* 192 */         { this.A[paramInt1][b] = this.A[paramInt1][i];
/* 193 */           this.A[paramInt1][i] = this.A[paramInt1][b] + 1;
/* 194 */           bool = true; } 
/*     */       } 
/* 196 */     } while (bool);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Selection nextSelection() {
/* 207 */     return nextPermutation();
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
/*     */   public MultiSetPermutation nextPermutation() {
/* 235 */     int i = this.k - 1;
/* 236 */     int j = this.r[this.k] + this.r[i];
/* 237 */     if (this.count == 1L) {
/*     */       
/* 239 */       step5();
/*     */       
/* 241 */       this.count++;
/* 242 */       return getPermutation();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/*     */       byte b;
/*     */ 
/*     */       
/* 251 */       for (b = 1; b <= j - 1; ) {
/*     */         
/* 253 */         int k = b + 1;
/* 254 */         if (this.A[i][b] != i || this.A[i][k] != i + 1) { b++; continue; }
/* 255 */          int m = b - 2;
/* 256 */         this.A[i][b] = this.A[i][k];
/* 257 */         this.A[i][k] = this.A[i][b] - 1;
/*     */         
/* 259 */         if (m <= 0) {
/*     */           
/* 261 */           step5();
/*     */           
/* 263 */           this.count++;
/* 264 */           return getPermutation();
/*     */         } 
/*     */ 
/*     */         
/* 268 */         moveMarks(i, m);
/* 269 */         step5();
/*     */         
/* 271 */         this.count++;
/* 272 */         return getPermutation();
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 279 */       if (j != 1) {
/*     */         
/* 281 */         int k = j / 2;
/* 282 */         for (b = 1; b <= k; b++) {
/*     */           
/* 284 */           int n = j - b + 1;
/* 285 */           int m = this.A[i][b];
/* 286 */           this.A[i][b] = this.A[i][n];
/* 287 */           this.A[i][n] = m;
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 293 */       i--;
/* 294 */       if (i <= 0) throw new NoSuchElementException(); 
/* 295 */       j += this.r[i];
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Selection randomSelection() {
/* 328 */     return randomPermutation();
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
/*     */   public MultiSetPermutation randomPermutation() {
/* 341 */     for (byte b = 0; b < this.N; b++) {
/*     */       
/* 343 */       int i = b + this.rand.nextInt(this.N - b);
/*     */       
/* 345 */       int j = this.randPerm[i];
/* 346 */       this.randPerm[i] = this.randPerm[b];
/* 347 */       this.randPerm[b] = j;
/*     */     } 
/* 349 */     return new MultiSetPermutation(this.randPerm, this.ns, false);
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
/*     */   public void reset() {
/* 373 */     this.count = 1L;
/* 374 */     int i = this.N;
/* 375 */     this.k1 = this.k - 1;
/* 376 */     for (byte b = 1; b <= this.k1; b++) {
/*     */       
/* 378 */       int j = this.r[b]; int m;
/* 379 */       for (m = 1; m <= j; ) { this.A[b][m] = b; m++; }
/* 380 */        int k = this.r[b] + 1;
/* 381 */       for (m = k; m <= i; ) { this.A[b][m] = b + 1; m++; }
/* 382 */        i -= this.r[b];
/*     */     } 
/*     */     
/* 385 */     this.hasNext = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSeed(long paramLong) {
/* 396 */     this.rand.setSeed(paramLong);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void step5() {
/*     */     byte b;
/* 405 */     for (b = 1; b <= this.N; ) { this.A[this.k][b] = this.A[1][b]; b++; }
/*     */     
/* 407 */     if (this.k != 2)
/*     */     {
/* 409 */       for (byte b1 = 2; b1 <= this.k1; b1++) {
/*     */         
/* 411 */         byte b2 = 1;
/* 412 */         for (b = 1; b <= this.N; b++) {
/*     */           
/* 414 */           if (this.A[this.k][b] == b1) {
/* 415 */             this.A[this.k][b] = this.A[b1][b2];
/* 416 */             b2++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 429 */       int[] arrayOfInt = { 2, 2, 1 };
/*     */ 
/*     */ 
/*     */       
/* 433 */       MultiSetPermutations multiSetPermutations = new MultiSetPermutations(3);
/* 434 */       System.out.println("Number of permutations = " + multiSetPermutations.countSelections());
/*     */ 
/*     */ 
/*     */       
/* 438 */       System.out.println("All permutations");
/* 439 */       while (multiSetPermutations.hasNext()) {
/*     */         
/* 441 */         MultiSetPermutation multiSetPermutation = multiSetPermutations.nextPermutation();
/* 442 */         System.out.println(multiSetPermutation.toString());
/*     */       } 
/* 444 */       multiSetPermutations.reset();
/* 445 */       System.out.println("All permutations");
/* 446 */       while (multiSetPermutations.hasNext()) {
/*     */         
/* 448 */         MultiSetPermutation multiSetPermutation = multiSetPermutations.nextPermutation();
/* 449 */         System.out.println(multiSetPermutation.toString());
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/combinatorics/MultiSetPermutations.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */