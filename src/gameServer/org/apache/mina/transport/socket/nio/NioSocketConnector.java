/*     */ package org.apache.mina.transport.socket.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.Selector;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.Executor;
/*     */ import org.apache.mina.core.polling.AbstractPollingIoConnector;
/*     */ import org.apache.mina.core.service.IoProcessor;
/*     */ import org.apache.mina.core.service.IoService;
/*     */ import org.apache.mina.core.service.TransportMetadata;
/*     */ import org.apache.mina.core.session.AbstractIoSession;
/*     */ import org.apache.mina.core.session.IoSessionConfig;
/*     */ import org.apache.mina.transport.socket.DefaultSocketSessionConfig;
/*     */ import org.apache.mina.transport.socket.SocketConnector;
/*     */ import org.apache.mina.transport.socket.SocketSessionConfig;
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
/*     */ public final class NioSocketConnector
/*     */   extends AbstractPollingIoConnector<NioSession, SocketChannel>
/*     */   implements SocketConnector
/*     */ {
/*     */   private volatile Selector selector;
/*     */   
/*     */   public NioSocketConnector() {
/*  56 */     super((IoSessionConfig)new DefaultSocketSessionConfig(), NioProcessor.class);
/*  57 */     ((DefaultSocketSessionConfig)getSessionConfig()).init((IoService)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioSocketConnector(int processorCount) {
/*  67 */     super((IoSessionConfig)new DefaultSocketSessionConfig(), NioProcessor.class, processorCount);
/*  68 */     ((DefaultSocketSessionConfig)getSessionConfig()).init((IoService)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioSocketConnector(IoProcessor<NioSession> processor) {
/*  78 */     super((IoSessionConfig)new DefaultSocketSessionConfig(), processor);
/*  79 */     ((DefaultSocketSessionConfig)getSessionConfig()).init((IoService)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioSocketConnector(Executor executor, IoProcessor<NioSession> processor) {
/*  90 */     super((IoSessionConfig)new DefaultSocketSessionConfig(), executor, processor);
/*  91 */     ((DefaultSocketSessionConfig)getSessionConfig()).init((IoService)this);
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
/*     */ 
/*     */   
/*     */   public NioSocketConnector(Class<? extends IoProcessor<NioSession>> processorClass, int processorCount) {
/* 106 */     super((IoSessionConfig)new DefaultSocketSessionConfig(), processorClass, processorCount);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public NioSocketConnector(Class<? extends IoProcessor<NioSession>> processorClass) {
/* 122 */     super((IoSessionConfig)new DefaultSocketSessionConfig(), processorClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init() throws Exception {
/* 130 */     this.selector = Selector.open();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void destroy() throws Exception {
/* 138 */     if (this.selector != null) {
/* 139 */       this.selector.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TransportMetadata getTransportMetadata() {
/* 147 */     return NioSocketSession.METADATA;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SocketSessionConfig getSessionConfig() {
/* 154 */     return (SocketSessionConfig)this.sessionConfig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InetSocketAddress getDefaultRemoteAddress() {
/* 162 */     return (InetSocketAddress)super.getDefaultRemoteAddress();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultRemoteAddress(InetSocketAddress defaultRemoteAddress) {
/* 169 */     setDefaultRemoteAddress(defaultRemoteAddress);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Iterator<SocketChannel> allHandles() {
/* 177 */     return new SocketChannelIterator(this.selector.keys());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean connect(SocketChannel handle, SocketAddress remoteAddress) throws Exception {
/* 185 */     return handle.connect(remoteAddress);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractPollingIoConnector<NioSession, SocketChannel>.ConnectionRequest getConnectionRequest(SocketChannel handle) {
/* 193 */     SelectionKey key = handle.keyFor(this.selector);
/*     */     
/* 195 */     if (key == null || !key.isValid()) {
/* 196 */       return null;
/*     */     }
/*     */     
/* 199 */     return (AbstractPollingIoConnector.ConnectionRequest)key.attachment();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void close(SocketChannel handle) throws Exception {
/* 207 */     SelectionKey key = handle.keyFor(this.selector);
/*     */     
/* 209 */     if (key != null) {
/* 210 */       key.cancel();
/*     */     }
/*     */     
/* 213 */     handle.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean finishConnect(SocketChannel handle) throws Exception {
/* 221 */     if (handle.finishConnect()) {
/* 222 */       SelectionKey key = handle.keyFor(this.selector);
/*     */       
/* 224 */       if (key != null) {
/* 225 */         key.cancel();
/*     */       }
/*     */       
/* 228 */       return true;
/*     */     } 
/*     */     
/* 231 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SocketChannel newHandle(SocketAddress localAddress) throws Exception {
/* 239 */     SocketChannel ch = SocketChannel.open();
/*     */     
/* 241 */     int receiveBufferSize = getSessionConfig().getReceiveBufferSize();
/*     */     
/* 243 */     if (receiveBufferSize > 65535) {
/* 244 */       ch.socket().setReceiveBufferSize(receiveBufferSize);
/*     */     }
/*     */     
/* 247 */     if (localAddress != null) {
/*     */       try {
/* 249 */         ch.socket().bind(localAddress);
/* 250 */       } catch (IOException ioe) {
/*     */ 
/*     */         
/* 253 */         String newMessage = "Error while binding on " + localAddress + "\n" + "original message : " + ioe.getMessage();
/*     */         
/* 255 */         Exception e = new IOException(newMessage);
/* 256 */         e.initCause(ioe.getCause());
/*     */ 
/*     */         
/* 259 */         ch.close();
/* 260 */         throw ioe;
/*     */       } 
/*     */     }
/*     */     
/* 264 */     ch.configureBlocking(false);
/*     */     
/* 266 */     return ch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected NioSession newSession(IoProcessor<NioSession> processor, SocketChannel handle) {
/* 274 */     return new NioSocketSession((IoService)this, processor, handle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void register(SocketChannel handle, AbstractPollingIoConnector<NioSession, SocketChannel>.ConnectionRequest request) throws Exception {
/* 282 */     handle.register(this.selector, 8, request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int select(int timeout) throws Exception {
/* 290 */     return this.selector.select(timeout);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Iterator<SocketChannel> selectedHandles() {
/* 298 */     return new SocketChannelIterator(this.selector.selectedKeys());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void wakeup() {
/* 306 */     this.selector.wakeup();
/*     */   }
/*     */   
/*     */   private static class SocketChannelIterator
/*     */     implements Iterator<SocketChannel> {
/*     */     private final Iterator<SelectionKey> i;
/*     */     
/*     */     private SocketChannelIterator(Collection<SelectionKey> selectedKeys) {
/* 314 */       this.i = selectedKeys.iterator();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 321 */       return this.i.hasNext();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SocketChannel next() {
/* 328 */       SelectionKey key = this.i.next();
/* 329 */       return (SocketChannel)key.channel();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void remove() {
/* 336 */       this.i.remove();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/transport/socket/nio/NioSocketConnector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */