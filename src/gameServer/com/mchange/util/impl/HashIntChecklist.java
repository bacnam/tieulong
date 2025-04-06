/*    */ package com.mchange.util.impl;
/*    */ 
/*    */ import com.mchange.util.IntChecklist;
/*    */ import com.mchange.util.IntEnumeration;
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
/*    */ public class HashIntChecklist
/*    */   implements IntChecklist
/*    */ {
/* 43 */   private static final Object DUMMY = new Object();
/*    */   
/* 45 */   IntObjectHash ioh = new IntObjectHash();
/*    */   
/*    */   public void check(int paramInt) {
/* 48 */     this.ioh.put(paramInt, DUMMY);
/*    */   }
/*    */   public void uncheck(int paramInt) {
/* 51 */     this.ioh.remove(paramInt);
/*    */   }
/*    */   public boolean isChecked(int paramInt) {
/* 54 */     return this.ioh.containsInt(paramInt);
/*    */   }
/*    */   public void clear() {
/* 57 */     this.ioh.clear();
/*    */   }
/*    */   public int countChecked() {
/* 60 */     return this.ioh.getSize();
/*    */   }
/*    */   
/*    */   public int[] getChecked() {
/* 64 */     synchronized (this.ioh) {
/*    */       
/* 66 */       int[] arrayOfInt = new int[this.ioh.getSize()];
/* 67 */       IntEnumeration intEnumeration = this.ioh.ints();
/* 68 */       for (byte b = 0; intEnumeration.hasMoreInts(); ) { arrayOfInt[b] = intEnumeration.nextInt(); b++; }
/* 69 */        return arrayOfInt;
/*    */     } 
/*    */   }
/*    */   
/*    */   public IntEnumeration checked() {
/* 74 */     return this.ioh.ints();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/impl/HashIntChecklist.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */