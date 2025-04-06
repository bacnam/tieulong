/*    */ package com.mchange.v2.c3p0.impl;
/*    */ 
/*    */ import com.mchange.v2.c3p0.C3P0Registry;
/*    */ import java.io.ObjectStreamException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class IdentityTokenResolvable
/*    */   extends AbstractIdentityTokenized
/*    */ {
/*    */   public static Object doResolve(IdentityTokenized itd) {
/* 58 */     return C3P0Registry.reregister(itd);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Object readResolve() throws ObjectStreamException {
/* 63 */     Object out = doResolve(this);
/* 64 */     verifyResolve(out);
/*    */ 
/*    */     
/* 67 */     return out;
/*    */   }
/*    */   
/*    */   protected void verifyResolve(Object o) throws ObjectStreamException {}
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/IdentityTokenResolvable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */