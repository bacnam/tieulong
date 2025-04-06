/*    */ package com.mchange.v2.c3p0;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import java.util.Map;
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
/*    */ public abstract class AbstractConnectionCustomizer
/*    */   implements ConnectionCustomizer
/*    */ {
/*    */   protected Map extensionsForToken(String parentDataSourceIdentityToken) {
/* 54 */     return C3P0Registry.extensionsForToken(parentDataSourceIdentityToken);
/*    */   }
/*    */   
/*    */   public void onAcquire(Connection c, String parentDataSourceIdentityToken) throws Exception {}
/*    */   
/*    */   public void onDestroy(Connection c, String parentDataSourceIdentityToken) throws Exception {}
/*    */   
/*    */   public void onCheckOut(Connection c, String parentDataSourceIdentityToken) throws Exception {}
/*    */   
/*    */   public void onCheckIn(Connection c, String parentDataSourceIdentityToken) throws Exception {}
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/AbstractConnectionCustomizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */