/*    */ package com.mchange.v2.c3p0.impl;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractIdentityTokenized
/*    */   implements IdentityTokenized
/*    */ {
/*    */   public boolean equals(Object o) {
/* 48 */     if (this == o) {
/* 49 */       return true;
/*    */     }
/* 51 */     if (o instanceof IdentityTokenized) {
/* 52 */       return getIdentityToken().equals(((IdentityTokenized)o).getIdentityToken());
/*    */     }
/* 54 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 58 */     return getIdentityToken().hashCode() ^ 0xFFFFFFFF;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/AbstractIdentityTokenized.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */