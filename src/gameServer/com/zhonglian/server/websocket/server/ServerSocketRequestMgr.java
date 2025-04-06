/*    */ package com.zhonglian.server.websocket.server;
/*    */ 
/*    */ import BaseThread.BaseMutexObject;
/*    */ import com.zhonglian.server.websocket.handler.response.ResponseHandler;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public class ServerSocketRequestMgr
/*    */ {
/* 13 */   private static short MAX_REQUEST_LENGTH = 16383;
/*    */   
/*    */   private final ServerSession session;
/*    */   private String opcode;
/* 17 */   private short sequence = 0;
/* 18 */   private BaseMutexObject seqMutex = new BaseMutexObject();
/* 19 */   private Map<Short, ServerSocketRequest> requests = new HashMap<>();
/*    */   
/*    */   public ServerSocketRequestMgr(ServerSession session, String operation) {
/* 22 */     this.session = session;
/* 23 */     this.opcode = operation;
/* 24 */     this.seqMutex.reduceMutexLevel(10);
/*    */   }
/*    */   
/*    */   public ServerSocketRequest popRequest(short sequence) {
/* 28 */     ServerSocketRequest request = null;
/*    */     try {
/* 30 */       this.seqMutex.lock();
/* 31 */       request = this.requests.remove(Short.valueOf(sequence));
/*    */     } finally {
/* 33 */       this.seqMutex.unlock();
/*    */     } 
/* 35 */     return request;
/*    */   }
/*    */   
/*    */   public ServerSocketRequest genRequest(ResponseHandler handler) {
/* 39 */     if (this.requests.size() >= MAX_REQUEST_LENGTH) {
/* 40 */       return null;
/*    */     }
/* 42 */     ServerSocketRequest request = null;
/* 43 */     this.seqMutex.lock();
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
/*    */ 
/*    */ 
/*    */   
/*    */   public void checkTimeoutRequest() {
/* 58 */     List<Short> timeoutList = new ArrayList<>();
/* 59 */     this.seqMutex.lock();
/* 60 */     List<ServerSocketRequest> requestLists = new ArrayList<>(this.requests.values());
/* 61 */     this.seqMutex.unlock();
/* 62 */     for (ServerSocketRequest request : requestLists) {
/* 63 */       if (request.isTimeout()) {
/* 64 */         timeoutList.add(Short.valueOf(request.getSequence()));
/*    */       }
/*    */     } 
/* 67 */     for (Short req : timeoutList) {
/* 68 */       ServerSocketRequest request = popRequest(req.shortValue());
/*    */       
/* 70 */       if (request != null)
/* 71 */         request.expired(); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/server/ServerSocketRequestMgr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */