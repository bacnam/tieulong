/*    */ package com.zhonglian.server.websocket.def;
/*    */ 
/*    */ public enum TerminalType {
/*  4 */   None(0),
/*  5 */   Client(1),
/*  6 */   GameServer(2),
/*  7 */   ZoneServer(3),
/*  8 */   WorldServer(4);
/*    */   
/*    */   private byte value;
/*    */ 
/*    */   
/*    */   TerminalType(int value) {
/* 14 */     this.value = (byte)value;
/*    */   }
/*    */   
/*    */   public byte value() {
/* 18 */     return this.value;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/def/TerminalType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */