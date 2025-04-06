/*    */ package com.mchange.v2.c3p0.impl;
/*    */ 
/*    */ import com.mchange.v2.coalesce.CoalesceChecker;
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
/*    */ public final class IdentityTokenizedCoalesceChecker
/*    */   implements CoalesceChecker
/*    */ {
/* 42 */   public static IdentityTokenizedCoalesceChecker INSTANCE = new IdentityTokenizedCoalesceChecker();
/*    */ 
/*    */   
/*    */   public boolean checkCoalesce(Object a, Object b) {
/* 46 */     IdentityTokenized aa = (IdentityTokenized)a;
/* 47 */     IdentityTokenized bb = (IdentityTokenized)b;
/*    */     
/* 49 */     String ta = aa.getIdentityToken();
/* 50 */     String tb = bb.getIdentityToken();
/*    */     
/* 52 */     if (ta == null || tb == null) {
/* 53 */       throw new NullPointerException("[c3p0 bug] An IdentityTokenized object has no identity token set?!?! " + ((ta == null) ? ta : tb));
/*    */     }
/* 55 */     return ta.equals(tb);
/*    */   }
/*    */ 
/*    */   
/*    */   public int coalesceHash(Object a) {
/* 60 */     String t = ((IdentityTokenized)a).getIdentityToken();
/* 61 */     return (t != null) ? t.hashCode() : 0;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/IdentityTokenizedCoalesceChecker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */