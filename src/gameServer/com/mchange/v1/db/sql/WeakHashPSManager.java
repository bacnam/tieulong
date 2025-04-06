/*    */ package com.mchange.v1.db.sql;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.WeakHashMap;
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
/*    */ public class WeakHashPSManager
/*    */   implements PSManager
/*    */ {
/* 49 */   WeakHashMap wmap = new WeakHashMap<Object, Object>();
/*    */ 
/*    */   
/*    */   public PreparedStatement getPS(Connection paramConnection, String paramString) {
/* 53 */     Map map = (Map)this.wmap.get(paramConnection);
/* 54 */     return (map == null) ? null : (PreparedStatement)map.get(paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   public void putPS(Connection paramConnection, String paramString, PreparedStatement paramPreparedStatement) {
/* 59 */     Map<Object, Object> map = (Map)this.wmap.get(paramConnection);
/* 60 */     if (map == null) {
/*    */       
/* 62 */       map = new HashMap<Object, Object>();
/* 63 */       this.wmap.put(paramConnection, map);
/*    */     } 
/* 65 */     map.put(paramString, paramPreparedStatement);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/WeakHashPSManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */