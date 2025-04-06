/*     */ package jsc.combinatorics;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Random;
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
/*     */ public class Permutations
/*     */   implements Enumerator
/*     */ {
/*     */   private boolean hasNext;
/*     */   private long count;
/*     */   private Random rand;
/*     */   private boolean firstCall;
/*     */   private int n;
/*     */   private int[] pi;
/*     */   private int[] rho;
/*     */   private int[] randPerm;
/*     */   private final double permutationCount;
/*     */   
/*     */   public Permutations(int paramInt) {
/*  53 */     this.n = paramInt;
/*     */     
/*  55 */     this.permutationCount = Maths.factorial(paramInt);
/*     */ 
/*     */     
/*  58 */     if (paramInt < 1)
/*  59 */       throw new IllegalArgumentException("Less than one object."); 
/*  60 */     this.pi = new int[paramInt + 1];
/*  61 */     this.rho = new int[paramInt + 1];
/*     */     
/*  63 */     this.rand = new Random();
/*  64 */     this.randPerm = new int[paramInt];
/*  65 */     for (byte b = 0; b < paramInt; ) { this.randPerm[b] = b + 1; b++; }
/*  66 */      reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double countSelections() {
/*  75 */     return this.permutationCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/*  82 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Permutation getPermutation() {
/*  88 */     int[] arrayOfInt = new int[this.n];
/*  89 */     for (byte b = 1; b <= this.n; ) { arrayOfInt[b - 1] = this.pi[b]; b++; }
/*  90 */      if (this.count > this.permutationCount) this.hasNext = false; 
/*  91 */     return new Permutation(arrayOfInt, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/* 100 */     return this.hasNext;
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
/*     */   public Permutation nextPermutation() {
/* 117 */     this.pi[0] = 0;
/* 118 */     if (this.firstCall) {
/*     */ 
/*     */       
/* 121 */       this.firstCall = false;
/* 122 */       this.count++;
/* 123 */       return getPermutation();
/*     */     } 
/* 125 */     int i = this.n - 1;
/* 126 */     for (; this.pi[i + 1] < this.pi[i]; i--);
/* 127 */     if (i == 0) throw new NoSuchElementException(); 
/* 128 */     int j = this.n;
/* 129 */     for (; this.pi[j] < this.pi[i]; j--);
/* 130 */     int k = this.pi[j];
/* 131 */     this.pi[j] = this.pi[i];
/* 132 */     this.pi[i] = k; int m;
/* 133 */     for (m = i + 1; m <= this.n; ) { this.rho[m] = this.pi[m]; m++; }
/* 134 */      for (m = i + 1; m <= this.n; ) { this.pi[m] = this.rho[this.n + i + 1 - m]; m++; }
/* 135 */      this.count++;
/* 136 */     return getPermutation();
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
/* 147 */     return nextPermutation();
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
/*     */   public Permutation randomPermutation() {
/* 159 */     for (byte b = 0; b < this.n; b++) {
/*     */       
/* 161 */       int i = b + this.rand.nextInt(this.n - b);
/*     */       
/* 163 */       int j = this.randPerm[i];
/* 164 */       this.randPerm[i] = this.randPerm[b];
/* 165 */       this.randPerm[b] = j;
/*     */     } 
/* 167 */     return new Permutation(this.randPerm, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Selection randomSelection() {
/* 175 */     return randomPermutation();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 184 */     for (byte b = 1; b <= this.n; ) { this.pi[b] = b; b++; }
/* 185 */      this.firstCall = true;
/* 186 */     this.count = 1L;
/* 187 */     this.hasNext = true;
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
/*     */   public void setSeed(long paramLong) {
/* 243 */     this.rand.setSeed(paramLong);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 252 */       byte b = 3;
/* 253 */       Permutations permutations = new Permutations(b);
/* 254 */       int i = (int)permutations.countSelections();
/* 255 */       System.out.println("Number of permutations = " + i);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 265 */       System.out.println("All permutations");
/* 266 */       while (permutations.hasNext()) {
/*     */         
/* 268 */         Permutation permutation = permutations.nextPermutation();
/* 269 */         System.out.println(permutation.toString());
/*     */       } 
/* 271 */       permutations.reset();
/* 272 */       System.out.println("All permutations");
/* 273 */       while (permutations.hasNext()) {
/*     */         
/* 275 */         Permutation permutation = permutations.nextPermutation();
/* 276 */         System.out.println(permutation.toString());
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/combinatorics/Permutations.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */