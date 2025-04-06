/*    */ package com.zhonglian.server.websocket.handler;
/*    */ 
/*    */ import com.zhonglian.server.websocket.def.MessageType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MessageHeader
/*    */ {
/*    */   public MessageType messageType;
/*    */   public byte srcType;
/*    */   public long srcId;
/*    */   public byte descType;
/*    */   public long descId;
/*    */   public String event;
/*    */   public short sequence;
/*    */   public short errcode;
/*    */   
/*    */   public MessageHeader genResponseHeader(short errorCode) {
/* 20 */     MessageHeader header = new MessageHeader();
/* 21 */     header.messageType = MessageType.Response;
/*    */     
/* 23 */     header.srcType = this.descType;
/* 24 */     header.srcId = this.descId;
/* 25 */     header.descType = this.srcType;
/* 26 */     header.descId = this.srcId;
/*    */     
/* 28 */     header.event = this.event;
/* 29 */     header.sequence = this.sequence;
/* 30 */     header.errcode = errorCode;
/* 31 */     return header;
/*    */   }
/*    */   
/*    */   public MessageHeader genResponseHeader() {
/* 35 */     return genResponseHeader((short)0);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/handler/MessageHeader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */