/*    */ package com.zhonglian.server.websocket.handler;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ 
/*    */ public abstract class IBaseHandler
/*    */ {
/*  7 */   private String event = "";
/*    */   
/*    */   public IBaseHandler() {
/*    */     try {
/* 11 */       this.event = getClass().getName().replaceAll("^.*\\.handler\\.", "").toLowerCase();
/* 12 */     } catch (Exception e) {
/* 13 */       CommLog.error("error handler name for {}", getClass().getName());
/*    */     } 
/*    */   }
/*    */   
/*    */   public IBaseHandler(String opName) {
/* 18 */     this.event = opName;
/*    */   }
/*    */   
/*    */   public String getEvent() {
/* 22 */     return this.event;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/handler/IBaseHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */