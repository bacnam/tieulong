/*    */ package jsc.combinatorics;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Permutation
/*    */   implements Selection
/*    */ {
/*    */   private int[] p;
/*    */   
/*    */   Permutation(int[] paramArrayOfint, boolean paramBoolean) {
/* 27 */     this.p = paramArrayOfint;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Permutation(int[] paramArrayOfint) {
/* 39 */     int i = paramArrayOfint.length;
/*    */ 
/*    */     
/* 42 */     for (byte b = 1; b <= i; b++) {
/*    */       
/* 44 */       byte b1 = 0; while (true) { if (b1 >= i)
/* 45 */           throw new IllegalArgumentException("Permutation array contains incorrect values.");  if (paramArrayOfint[b1] == b)
/*    */           break;  b1++; }
/*    */     
/* 48 */     }  this.p = paramArrayOfint;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int length() {
/* 56 */     return this.p.length;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int[] toIntArray() {
/* 63 */     return this.p;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 67 */     StringBuffer stringBuffer = new StringBuffer();
/* 68 */     stringBuffer.append("Permutation:");
/* 69 */     for (byte b = 0; b < this.p.length; ) { stringBuffer.append(" " + this.p[b]); b++; }
/* 70 */      return stringBuffer.toString();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/combinatorics/Permutation.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */