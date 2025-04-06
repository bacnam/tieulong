/*    */ package org.apache.mina.filter.codec.serialization;
/*    */ 
/*    */ import java.io.NotSerializableException;
/*    */ import org.apache.mina.core.buffer.IoBuffer;
/*    */ import org.apache.mina.core.session.IoSession;
/*    */ import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
/*    */ import org.apache.mina.filter.codec.ProtocolEncoderOutput;
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
/*    */ public class ObjectSerializationEncoder
/*    */   extends ProtocolEncoderAdapter
/*    */ {
/* 38 */   private int maxObjectSize = Integer.MAX_VALUE;
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
/*    */   public int getMaxObjectSize() {
/* 54 */     return this.maxObjectSize;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setMaxObjectSize(int maxObjectSize) {
/* 64 */     if (maxObjectSize <= 0) {
/* 65 */       throw new IllegalArgumentException("maxObjectSize: " + maxObjectSize);
/*    */     }
/*    */     
/* 68 */     this.maxObjectSize = maxObjectSize;
/*    */   }
/*    */   
/*    */   public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
/* 72 */     if (!(message instanceof java.io.Serializable)) {
/* 73 */       throw new NotSerializableException();
/*    */     }
/*    */     
/* 76 */     IoBuffer buf = IoBuffer.allocate(64);
/* 77 */     buf.setAutoExpand(true);
/* 78 */     buf.putObject(message);
/*    */     
/* 80 */     int objectSize = buf.position() - 4;
/* 81 */     if (objectSize > this.maxObjectSize) {
/* 82 */       throw new IllegalArgumentException("The encoded object is too big: " + objectSize + " (> " + this.maxObjectSize + ')');
/*    */     }
/*    */ 
/*    */     
/* 86 */     buf.flip();
/* 87 */     out.write(buf);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/serialization/ObjectSerializationEncoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */