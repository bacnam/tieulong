/*    */ package org.apache.mina.transport.socket.nio;
/*    */ 
/*    */ import java.nio.channels.ByteChannel;
/*    */ import java.nio.channels.Channel;
/*    */ import java.nio.channels.SelectionKey;
/*    */ import org.apache.mina.core.filterchain.DefaultIoFilterChain;
/*    */ import org.apache.mina.core.filterchain.IoFilterChain;
/*    */ import org.apache.mina.core.service.IoProcessor;
/*    */ import org.apache.mina.core.service.IoService;
/*    */ import org.apache.mina.core.session.AbstractIoSession;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class NioSession
/*    */   extends AbstractIoSession
/*    */ {
/*    */   protected final IoProcessor<NioSession> processor;
/*    */   protected final Channel channel;
/*    */   protected SelectionKey key;
/*    */   private final IoFilterChain filterChain;
/*    */   
/*    */   protected NioSession(IoProcessor<NioSession> processor, IoService service, Channel channel) {
/* 60 */     super(service);
/* 61 */     this.channel = channel;
/* 62 */     this.processor = processor;
/* 63 */     this.filterChain = (IoFilterChain)new DefaultIoFilterChain(this);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   abstract ByteChannel getChannel();
/*    */ 
/*    */   
/*    */   public IoFilterChain getFilterChain() {
/* 72 */     return this.filterChain;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   SelectionKey getSelectionKey() {
/* 79 */     return this.key;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void setSelectionKey(SelectionKey key) {
/* 88 */     this.key = key;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public IoProcessor<NioSession> getProcessor() {
/* 95 */     return this.processor;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/transport/socket/nio/NioSession.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */