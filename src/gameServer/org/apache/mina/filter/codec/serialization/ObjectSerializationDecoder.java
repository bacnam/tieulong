/*    */ package org.apache.mina.filter.codec.serialization;
/*    */ 
/*    */ import org.apache.mina.core.buffer.IoBuffer;
/*    */ import org.apache.mina.core.session.IoSession;
/*    */ import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
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
/*    */ public class ObjectSerializationDecoder
/*    */   extends CumulativeProtocolDecoder
/*    */ {
/*    */   private final ClassLoader classLoader;
/* 40 */   private int maxObjectSize = 1048576;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ObjectSerializationDecoder() {
/* 47 */     this(Thread.currentThread().getContextClassLoader());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ObjectSerializationDecoder(ClassLoader classLoader) {
/* 54 */     if (classLoader == null) {
/* 55 */       throw new IllegalArgumentException("classLoader");
/*    */     }
/* 57 */     this.classLoader = classLoader;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getMaxObjectSize() {
/* 67 */     return this.maxObjectSize;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setMaxObjectSize(int maxObjectSize) {
/* 77 */     if (maxObjectSize <= 0) {
/* 78 */       throw new IllegalArgumentException("maxObjectSize: " + maxObjectSize);
/*    */     }
/*    */     
/* 81 */     this.maxObjectSize = maxObjectSize;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
/* 86 */     if (!in.prefixedDataAvailable(4, this.maxObjectSize)) {
/* 87 */       return false;
/*    */     }
/*    */     
/* 90 */     out.write(in.getObject(this.classLoader));
/* 91 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/serialization/ObjectSerializationDecoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */