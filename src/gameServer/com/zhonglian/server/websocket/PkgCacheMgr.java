/*    */ package com.zhonglian.server.websocket;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import com.zhonglian.server.websocket.def.MessageType;
/*    */ import com.zhonglian.server.websocket.handler.MessageHeader;
/*    */ import java.util.LinkedList;
/*    */ import java.util.Queue;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PkgCacheMgr
/*    */ {
/* 19 */   private final int MaxCachedCnt = 30;
/*    */   
/* 21 */   private Queue<PkgRecv> recvs = new LinkedList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void cacheSent(MessageHeader header, String body) {
/* 27 */     String key = String.format("%s@%s", new Object[] { header.event, Short.valueOf(header.sequence) });
/* 28 */     for (PkgRecv recv : this.recvs) {
/* 29 */       if (recv.getKey().equals(key)) {
/* 30 */         recv.setResponse(header, body);
/*    */         return;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public synchronized PkgRecv fetchSentOrRegist(MessageHeader header) {
/* 37 */     if (header.messageType == MessageType.Notify) {
/* 38 */       return null;
/*    */     }
/* 40 */     String key = String.format("%s@%s", new Object[] { header.event, Short.valueOf(header.sequence) });
/* 41 */     for (PkgRecv recv : this.recvs) {
/* 42 */       if (recv.getKey().equals(key)) {
/* 43 */         return recv;
/*    */       }
/*    */     } 
/*    */     
/* 47 */     this.recvs.add(new PkgRecv(key, header));
/*    */     
/* 49 */     if (this.recvs.size() > 30) {
/* 50 */       ((PkgRecv)this.recvs.poll()).onRemove();
/*    */     }
/* 52 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public class PkgRecv
/*    */   {
/*    */     private String key;
/*    */     private MessageHeader header;
/*    */     private String body;
/*    */     
/*    */     public PkgRecv(String key, MessageHeader header) {
/* 63 */       this.key = key;
/* 64 */       this.header = header;
/*    */     }
/*    */     
/*    */     public void setResponse(MessageHeader header, String body) {
/* 68 */       this.header = header;
/* 69 */       this.body = body;
/*    */     }
/*    */     
/*    */     public String getKey() {
/* 73 */       return this.key;
/*    */     }
/*    */     
/*    */     public void onRemove() {
/* 77 */       if (this.body == null) {
/* 78 */         CommLog.error("PkgRecv not sendBack : {}", this.key);
/*    */       }
/*    */     }
/*    */     
/*    */     public MessageHeader getHeader() {
/* 83 */       return this.header;
/*    */     }
/*    */     
/*    */     public String getBody() {
/* 87 */       return this.body;
/*    */     }
/*    */     
/*    */     public String toString() {
/* 91 */       return this.key;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/PkgCacheMgr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */