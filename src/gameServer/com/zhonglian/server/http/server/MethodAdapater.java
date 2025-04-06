/*    */ package com.zhonglian.server.http.server;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class MethodAdapater {
/*  7 */   private Map<String, HttpAdaperter> requests = new HashMap<>();
/*    */   
/*    */   public HttpAdaperter getAdaperter(String path) {
/* 10 */     return this.requests.get(path);
/*    */   }
/*    */   
/*    */   public void addAdapter(String path, HttpAdaperter httpAdaperter) {
/* 14 */     this.requests.put(path, httpAdaperter);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/http/server/MethodAdapater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */