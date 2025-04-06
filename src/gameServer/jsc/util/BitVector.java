/*     */ package jsc.util;
/*     */ 
/*     */ import java.util.BitSet;
/*     */ import jsc.combinatorics.Selection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BitVector
/*     */   extends BitSet
/*     */   implements Selection
/*     */ {
/*     */   private int n;
/*     */   
/*     */   public BitVector(int paramInt) {
/*  26 */     super(paramInt); this.n = paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BitVector(boolean[] paramArrayOfboolean) {
/*  35 */     this(paramArrayOfboolean.length);
/*  36 */     for (byte b = 0; b < this.n; ) { set(b, paramArrayOfboolean[b]); b++; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BitVector(int[] paramArrayOfint) {
/*  47 */     this(paramArrayOfint.length);
/*  48 */     for (byte b = 0; b < this.n; b++) {
/*     */       
/*  50 */       if (paramArrayOfint[b] == 0) { set(b, false); }
/*  51 */       else if (paramArrayOfint[b] == 1) { set(b, true); }
/*  52 */       else { throw new IllegalArgumentException("Array must contain only 0 or 1."); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/*  61 */     return this.n;
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
/*     */   public boolean[] toBooleanArray() {
/*  78 */     boolean[] arrayOfBoolean = new boolean[this.n];
/*  79 */     for (byte b = 0; b < this.n; ) { arrayOfBoolean[b] = get(b); b++; }
/*  80 */      return arrayOfBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] toIntArray() {
/*  90 */     int[] arrayOfInt = new int[this.n];
/*  91 */     for (byte b = 0; b < this.n; ) { arrayOfInt[b] = get(b) ? 1 : 0; b++; }
/*  92 */      return arrayOfInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 102 */     StringBuffer stringBuffer = new StringBuffer();
/*     */     
/* 104 */     for (byte b = 0; b < this.n; ) { stringBuffer.append(get(b) ? 1 : 0); b++; }
/* 105 */      return stringBuffer.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/util/BitVector.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */