/*     */ package org.apache.mina.transport.socket.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.Selector;
/*     */ import java.nio.channels.ServerSocketChannel;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.Executor;
/*     */ import org.apache.mina.core.polling.AbstractPollingIoAcceptor;
/*     */ import org.apache.mina.core.service.IoProcessor;
/*     */ import org.apache.mina.core.service.IoService;
/*     */ import org.apache.mina.core.service.TransportMetadata;
/*     */ import org.apache.mina.core.session.AbstractIoSession;
/*     */ import org.apache.mina.core.session.IoSessionConfig;
/*     */ import org.apache.mina.transport.socket.DefaultSocketSessionConfig;
/*     */ import org.apache.mina.transport.socket.SocketAcceptor;
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
/*     */ public final class NioSocketAcceptor
/*     */   extends AbstractPollingIoAcceptor<NioSession, ServerSocketChannel>
/*     */   implements SocketAcceptor
/*     */ {
/*     */   private volatile Selector selector;
/*  55 */   private volatile SelectorProvider selectorProvider = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioSocketAcceptor() {
/*  61 */     super((IoSessionConfig)new DefaultSocketSessionConfig(), NioProcessor.class);
/*  62 */     ((DefaultSocketSessionConfig)getSessionConfig()).init((IoService)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioSocketAcceptor(int processorCount) {
/*  73 */     super((IoSessionConfig)new DefaultSocketSessionConfig(), NioProcessor.class, processorCount);
/*  74 */     ((DefaultSocketSessionConfig)getSessionConfig()).init((IoService)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioSocketAcceptor(IoProcessor<NioSession> processor) {
/*  84 */     super((IoSessionConfig)new DefaultSocketSessionConfig(), processor);
/*  85 */     ((DefaultSocketSessionConfig)getSessionConfig()).init((IoService)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioSocketAcceptor(Executor executor, IoProcessor<NioSession> processor) {
/*  96 */     super((IoSessionConfig)new DefaultSocketSessionConfig(), executor, processor);
/*  97 */     ((DefaultSocketSessionConfig)getSessionConfig()).init((IoService)this);
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
/*     */   public NioSocketAcceptor(int processorCount, SelectorProvider selectorProvider) {
/* 110 */     super((IoSessionConfig)new DefaultSocketSessionConfig(), NioProcessor.class, processorCount, selectorProvider);
/* 111 */     ((DefaultSocketSessionConfig)getSessionConfig()).init((IoService)this);
/* 112 */     this.selectorProvider = selectorProvider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init() throws Exception {
/* 120 */     this.selector = Selector.open();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init(SelectorProvider selectorProvider) throws Exception {
/* 128 */     this.selectorProvider = selectorProvider;
/*     */     
/* 130 */     if (selectorProvider == null) {
/* 131 */       this.selector = Selector.open();
/*     */     } else {
/* 133 */       this.selector = selectorProvider.openSelector();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void destroy() throws Exception {
/* 142 */     if (this.selector != null) {
/* 143 */       this.selector.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TransportMetadata getTransportMetadata() {
/* 151 */     return NioSocketSession.METADATA;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InetSocketAddress getLocalAddress() {
/* 159 */     return (InetSocketAddress)super.getLocalAddress();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InetSocketAddress getDefaultLocalAddress() {
/* 167 */     return (InetSocketAddress)super.getDefaultLocalAddress();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultLocalAddress(InetSocketAddress localAddress) {
/* 174 */     setDefaultLocalAddress(localAddress);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected NioSession accept(IoProcessor<NioSession> processor, ServerSocketChannel handle) throws Exception {
/* 183 */     SelectionKey key = null;
/*     */     
/* 185 */     if (handle != null) {
/* 186 */       key = handle.keyFor(this.selector);
/*     */     }
/*     */     
/* 189 */     if (key == null || !key.isValid() || !key.isAcceptable()) {
/* 190 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 194 */     SocketChannel ch = handle.accept();
/*     */     
/* 196 */     if (ch == null) {
/* 197 */       return null;
/*     */     }
/*     */     
/* 200 */     return new NioSocketSession((IoService)this, processor, ch);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ServerSocketChannel open(SocketAddress localAddress) throws Exception {
/* 210 */     ServerSocketChannel channel = null;
/*     */     
/* 212 */     if (this.selectorProvider != null) {
/* 213 */       channel = this.selectorProvider.openServerSocketChannel();
/*     */     } else {
/* 215 */       channel = ServerSocketChannel.open();
/*     */     } 
/*     */     
/* 218 */     boolean success = false;
/*     */ 
/*     */     
/*     */     try {
/* 222 */       channel.configureBlocking(false);
/*     */ 
/*     */       
/* 225 */       ServerSocket socket = channel.socket();
/*     */ 
/*     */       
/* 228 */       socket.setReuseAddress(isReuseAddress());
/*     */ 
/*     */       
/*     */       try {
/* 232 */         socket.bind(localAddress, getBacklog());
/* 233 */       } catch (IOException ioe) {
/*     */ 
/*     */         
/* 236 */         String newMessage = "Error while binding on " + localAddress + "\n" + "original message : " + ioe.getMessage();
/*     */         
/* 238 */         Exception e = new IOException(newMessage);
/* 239 */         e.initCause(ioe.getCause());
/*     */ 
/*     */         
/* 242 */         channel.close();
/*     */         
/* 244 */         throw e;
/*     */       } 
/*     */ 
/*     */       
/* 248 */       channel.register(this.selector, 16);
/* 249 */       success = true;
/*     */     } finally {
/* 251 */       if (!success) {
/* 252 */         close(channel);
/*     */       }
/*     */     } 
/* 255 */     return channel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SocketAddress localAddress(ServerSocketChannel handle) throws Exception {
/* 263 */     return handle.socket().getLocalSocketAddress();
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
/*     */ 
/*     */   
/*     */   protected int select() throws Exception {
/* 281 */     return this.selector.select();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Iterator<ServerSocketChannel> selectedHandles() {
/* 289 */     return new ServerSocketChannelIterator(this.selector.selectedKeys());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void close(ServerSocketChannel handle) throws Exception {
/* 297 */     SelectionKey key = handle.keyFor(this.selector);
/*     */     
/* 299 */     if (key != null) {
/* 300 */       key.cancel();
/*     */     }
/*     */     
/* 303 */     handle.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void wakeup() {
/* 311 */     this.selector.wakeup();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ServerSocketChannelIterator
/*     */     implements Iterator<ServerSocketChannel>
/*     */   {
/*     */     private final Iterator<SelectionKey> iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private ServerSocketChannelIterator(Collection<SelectionKey> selectedKeys) {
/* 329 */       this.iterator = selectedKeys.iterator();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 338 */       return this.iterator.hasNext();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ServerSocketChannel next() {
/* 348 */       SelectionKey key = this.iterator.next();
/*     */       
/* 350 */       if (key.isValid() && key.isAcceptable()) {
/* 351 */         return (ServerSocketChannel)key.channel();
/*     */       }
/*     */       
/* 354 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void remove() {
/* 361 */       this.iterator.remove();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/transport/socket/nio/NioSocketAcceptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */