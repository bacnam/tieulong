/*     */ package jsc.combinatorics;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Random;
/*     */ import jsc.util.BitVector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GrayCode
/*     */   implements Enumerator
/*     */ {
/*     */   private int k;
/*     */   private Random rand;
/*     */   private boolean firstCall;
/*     */   private boolean finalExit;
/*     */   private int n;
/*     */   private int[] a;
/*     */   private final double subsetCount;
/*     */   
/*     */   public GrayCode(int paramInt) {
/*  54 */     this.n = paramInt;
/*     */     
/*  56 */     this.subsetCount = Math.pow(2.0D, paramInt);
/*     */ 
/*     */     
/*  59 */     if (paramInt < 1)
/*  60 */       throw new IllegalArgumentException("n < 1."); 
/*  61 */     this.a = new int[paramInt + 1];
/*     */     
/*  63 */     this.rand = new Random();
/*     */ 
/*     */     
/*  66 */     reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double countSelections() {
/*  75 */     return this.subsetCount;
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
/*     */   private BitVector getBitVector() {
/*  88 */     BitVector bitVector = new BitVector(this.n);
/*  89 */     for (byte b = 1; b <= this.n; ) { bitVector.set(b - 1, (this.a[b] == 1)); b++; }
/*  90 */      return bitVector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/*  98 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/* 106 */     return !this.finalExit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Selection nextSelection() {
/* 116 */     return (Selection)nextBitVector();
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
/*     */   public BitVector nextBitVector() {
/* 129 */     if (this.finalExit) throw new NoSuchElementException();
/*     */ 
/*     */     
/* 132 */     this.a[0] = 0;
/* 133 */     if (this.firstCall) {
/*     */       
/* 135 */       this.firstCall = false;
/* 136 */       return getBitVector();
/*     */     } 
/*     */     
/* 139 */     int i = this.k % 2;
/* 140 */     byte b = 1;
/* 141 */     if (i != 0) {
/*     */       
/* 143 */       do { b++; } while (this.a[b - 1] != 1);
/*     */     }
/*     */     
/* 146 */     this.a[b] = 1 - this.a[b];
/* 147 */     this.k = this.k + 2 * this.a[b] - 1;
/* 148 */     if (this.k == this.a[this.n]) this.finalExit = true; 
/* 149 */     return getBitVector();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Selection randomSelection() {
/* 157 */     return (Selection)randomBitVector();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BitVector randomBitVector() {
/* 166 */     BitVector bitVector = new BitVector(this.n);
/* 167 */     for (byte b = 0; b < this.n; ) { bitVector.set(b, this.rand.nextBoolean()); b++; }
/* 168 */      return bitVector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 177 */     for (byte b = 1; b <= this.n; ) { this.a[b] = 0; b++; }
/* 178 */      this.firstCall = true;
/* 179 */     this.finalExit = false;
/* 180 */     this.k = 0;
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
/*     */   public void setSeed(long paramLong) {
/* 209 */     this.rand.setSeed(paramLong);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 217 */       byte b1 = 4;
/* 218 */       GrayCode grayCode = new GrayCode(b1);
/* 219 */       int i = (int)grayCode.countSelections();
/* 220 */       System.out.println("Number of bit vectors = " + i);
/*     */ 
/*     */ 
/*     */       
/* 224 */       while (grayCode.hasNext()) {
/*     */         
/* 226 */         BitVector bitVector = (BitVector)grayCode.nextSelection();
/* 227 */         System.out.println(bitVector.toString());
/*     */       } 
/*     */       
/* 230 */       System.out.println("Random Gray codes");
/* 231 */       for (byte b2 = 0; b2 < 10; b2++) {
/*     */         
/* 233 */         BitVector bitVector = (BitVector)grayCode.randomSelection();
/* 234 */         System.out.println(bitVector.toString());
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/combinatorics/GrayCode.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */