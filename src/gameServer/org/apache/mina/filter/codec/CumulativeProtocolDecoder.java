/*     */ package org.apache.mina.filter.codec;
/*     */ 
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.session.AttributeKey;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CumulativeProtocolDecoder
/*     */   extends ProtocolDecoderAdapter
/*     */ {
/* 105 */   private final AttributeKey BUFFER = new AttributeKey(getClass(), "buffer");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
/* 124 */     if (!session.getTransportMetadata().hasFragmentation()) { do {  }
/* 125 */       while (in.hasRemaining() && 
/* 126 */         doDecode(session, in, out));
/*     */ 
/*     */ 
/*     */       
/*     */       return; }
/*     */ 
/*     */ 
/*     */     
/* 134 */     boolean usingSessionBuffer = true;
/* 135 */     IoBuffer buf = (IoBuffer)session.getAttribute(this.BUFFER);
/*     */ 
/*     */     
/* 138 */     if (buf != null) {
/* 139 */       boolean appended = false;
/*     */       
/* 141 */       if (buf.isAutoExpand()) {
/*     */         try {
/* 143 */           buf.put(in);
/* 144 */           appended = true;
/* 145 */         } catch (IllegalStateException e) {
/*     */ 
/*     */         
/* 148 */         } catch (IndexOutOfBoundsException e) {}
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 153 */       if (appended) {
/* 154 */         buf.flip();
/*     */       }
/*     */       else {
/*     */         
/* 158 */         buf.flip();
/* 159 */         IoBuffer newBuf = IoBuffer.allocate(buf.remaining() + in.remaining()).setAutoExpand(true);
/* 160 */         newBuf.order(buf.order());
/* 161 */         newBuf.put(buf);
/* 162 */         newBuf.put(in);
/* 163 */         newBuf.flip();
/* 164 */         buf = newBuf;
/*     */ 
/*     */         
/* 167 */         session.setAttribute(this.BUFFER, buf);
/*     */       } 
/*     */     } else {
/* 170 */       buf = in;
/* 171 */       usingSessionBuffer = false;
/*     */     } 
/*     */     
/*     */     while (true) {
/* 175 */       int oldPos = buf.position();
/* 176 */       boolean decoded = doDecode(session, buf, out);
/* 177 */       if (decoded) {
/* 178 */         if (buf.position() == oldPos) {
/* 179 */           throw new IllegalStateException("doDecode() can't return true when buffer is not consumed.");
/*     */         }
/*     */         
/* 182 */         if (!buf.hasRemaining()) {
/*     */           break;
/*     */         }
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/*     */       break;
/*     */     } 
/*     */     
/* 193 */     if (buf.hasRemaining()) {
/* 194 */       if (usingSessionBuffer && buf.isAutoExpand()) {
/* 195 */         buf.compact();
/*     */       } else {
/* 197 */         storeRemainingInSession(buf, session);
/*     */       }
/*     */     
/* 200 */     } else if (usingSessionBuffer) {
/* 201 */       removeSessionBuffer(session);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean doDecode(IoSession paramIoSession, IoBuffer paramIoBuffer, ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose(IoSession session) throws Exception {
/* 226 */     removeSessionBuffer(session);
/*     */   }
/*     */   
/*     */   private void removeSessionBuffer(IoSession session) {
/* 230 */     session.removeAttribute(this.BUFFER);
/*     */   }
/*     */   
/*     */   private void storeRemainingInSession(IoBuffer buf, IoSession session) {
/* 234 */     IoBuffer remainingBuf = IoBuffer.allocate(buf.capacity()).setAutoExpand(true);
/*     */     
/* 236 */     remainingBuf.order(buf.order());
/* 237 */     remainingBuf.put(buf);
/*     */     
/* 239 */     session.setAttribute(this.BUFFER, remainingBuf);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/CumulativeProtocolDecoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */