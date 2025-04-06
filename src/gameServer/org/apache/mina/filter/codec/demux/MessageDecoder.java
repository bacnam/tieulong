/*    */ package org.apache.mina.filter.codec.demux;
/*    */ 
/*    */ import org.apache.mina.core.buffer.IoBuffer;
/*    */ import org.apache.mina.core.session.IoSession;
/*    */ import org.apache.mina.filter.codec.ProtocolDecoderOutput;
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
/*    */ public interface MessageDecoder
/*    */ {
/* 44 */   public static final MessageDecoderResult OK = MessageDecoderResult.OK;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public static final MessageDecoderResult NEED_DATA = MessageDecoderResult.NEED_DATA;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 58 */   public static final MessageDecoderResult NOT_OK = MessageDecoderResult.NOT_OK;
/*    */   
/*    */   MessageDecoderResult decodable(IoSession paramIoSession, IoBuffer paramIoBuffer);
/*    */   
/*    */   MessageDecoderResult decode(IoSession paramIoSession, IoBuffer paramIoBuffer, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
/*    */   
/*    */   void finishDecode(IoSession paramIoSession, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/demux/MessageDecoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */