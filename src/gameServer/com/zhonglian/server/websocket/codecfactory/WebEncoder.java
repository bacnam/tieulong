/*    */ package com.zhonglian.server.websocket.codecfactory;
/*    */ 
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.nio.ByteBuffer;
/*    */ import org.apache.mina.core.buffer.IoBuffer;
/*    */ import org.apache.mina.core.session.IoSession;
/*    */ import org.apache.mina.filter.codec.ProtocolEncoder;
/*    */ import org.apache.mina.filter.codec.ProtocolEncoderOutput;
/*    */ 
/*    */ 
/*    */ public class WebEncoder
/*    */   implements ProtocolEncoder
/*    */ {
/*    */   public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
/* 15 */     byte[] _protocol = null;
/* 16 */     if (message instanceof String) {
/* 17 */       _protocol = ((String)message).getBytes("UTF-8");
/* 18 */     } else if (message instanceof ByteBuffer) {
/* 19 */       _protocol = encode(((ByteBuffer)message).array());
/*    */     } 
/* 21 */     out.write(IoBuffer.wrap(_protocol));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static byte[] encode(byte[] msgByte) throws UnsupportedEncodingException {
/* 27 */     int masking_key_startIndex = 2;
/*    */ 
/*    */     
/* 30 */     if (msgByte.length <= 125) {
/* 31 */       masking_key_startIndex = 2;
/* 32 */     } else if (msgByte.length > 65536) {
/* 33 */       masking_key_startIndex = 10;
/* 34 */     } else if (msgByte.length > 125) {
/* 35 */       masking_key_startIndex = 4;
/*    */     } 
/*    */ 
/*    */     
/* 39 */     byte[] result = new byte[msgByte.length + masking_key_startIndex];
/*    */ 
/*    */ 
/*    */     
/* 43 */     result[0] = -126;
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 48 */     if (msgByte.length <= 125) {
/* 49 */       result[1] = (byte)msgByte.length;
/* 50 */     } else if (msgByte.length > 65536) {
/* 51 */       result[1] = Byte.MAX_VALUE;
/* 52 */     } else if (msgByte.length > 125) {
/* 53 */       result[1] = 126;
/* 54 */       result[2] = (byte)(msgByte.length >> 8);
/* 55 */       result[3] = (byte)(msgByte.length % 256);
/*    */     } 
/*    */ 
/*    */     
/* 59 */     for (int i = 0; i < msgByte.length; i++) {
/* 60 */       result[i + masking_key_startIndex] = msgByte[i];
/*    */     }
/*    */     
/* 63 */     return result;
/*    */   }
/*    */   
/*    */   public void dispose(IoSession session) throws Exception {}
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/codecfactory/WebEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */