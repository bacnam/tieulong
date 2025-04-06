/*     */ package org.apache.mina.transport.socket.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.DatagramChannel;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import org.apache.mina.core.polling.AbstractPollingIoConnector;
/*     */ import org.apache.mina.core.service.IoProcessor;
/*     */ import org.apache.mina.core.service.IoService;
/*     */ import org.apache.mina.core.service.TransportMetadata;
/*     */ import org.apache.mina.core.session.AbstractIoSession;
/*     */ import org.apache.mina.core.session.IoSessionConfig;
/*     */ import org.apache.mina.transport.socket.DatagramConnector;
/*     */ import org.apache.mina.transport.socket.DatagramSessionConfig;
/*     */ import org.apache.mina.transport.socket.DefaultDatagramSessionConfig;
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
/*     */ public final class NioDatagramConnector
/*     */   extends AbstractPollingIoConnector<NioSession, DatagramChannel>
/*     */   implements DatagramConnector
/*     */ {
/*     */   public NioDatagramConnector() {
/*  51 */     super((IoSessionConfig)new DefaultDatagramSessionConfig(), NioProcessor.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioDatagramConnector(int processorCount) {
/*  58 */     super((IoSessionConfig)new DefaultDatagramSessionConfig(), NioProcessor.class, processorCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioDatagramConnector(IoProcessor<NioSession> processor) {
/*  65 */     super((IoSessionConfig)new DefaultDatagramSessionConfig(), processor);
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
/*     */   public NioDatagramConnector(Class<? extends IoProcessor<NioSession>> processorClass, int processorCount) {
/*  80 */     super((IoSessionConfig)new DefaultDatagramSessionConfig(), processorClass, processorCount);
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
/*     */   public NioDatagramConnector(Class<? extends IoProcessor<NioSession>> processorClass) {
/*  96 */     super((IoSessionConfig)new DefaultDatagramSessionConfig(), processorClass);
/*     */   }
/*     */   
/*     */   public TransportMetadata getTransportMetadata() {
/* 100 */     return NioDatagramSession.METADATA;
/*     */   }
/*     */   
/*     */   public DatagramSessionConfig getSessionConfig() {
/* 104 */     return (DatagramSessionConfig)this.sessionConfig;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress getDefaultRemoteAddress() {
/* 109 */     return (InetSocketAddress)super.getDefaultRemoteAddress();
/*     */   }
/*     */   
/*     */   public void setDefaultRemoteAddress(InetSocketAddress defaultRemoteAddress) {
/* 113 */     setDefaultRemoteAddress(defaultRemoteAddress);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init() throws Exception {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected DatagramChannel newHandle(SocketAddress localAddress) throws Exception {
/* 123 */     DatagramChannel ch = DatagramChannel.open();
/*     */     
/*     */     try {
/* 126 */       if (localAddress != null) {
/*     */         try {
/* 128 */           ch.socket().bind(localAddress);
/* 129 */           setDefaultLocalAddress(localAddress);
/* 130 */         } catch (IOException ioe) {
/*     */ 
/*     */           
/* 133 */           String newMessage = "Error while binding on " + localAddress + "\n" + "original message : " + ioe.getMessage();
/*     */           
/* 135 */           Exception e = new IOException(newMessage);
/* 136 */           e.initCause(ioe.getCause());
/*     */ 
/*     */           
/* 139 */           ch.close();
/*     */           
/* 141 */           throw e;
/*     */         } 
/*     */       }
/*     */       
/* 145 */       return ch;
/* 146 */     } catch (Exception e) {
/*     */ 
/*     */       
/* 149 */       ch.close();
/* 150 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean connect(DatagramChannel handle, SocketAddress remoteAddress) throws Exception {
/* 156 */     handle.connect(remoteAddress);
/* 157 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected NioSession newSession(IoProcessor<NioSession> processor, DatagramChannel handle) {
/* 162 */     NioSession session = new NioDatagramSession((IoService)this, handle, processor);
/* 163 */     session.getConfig().setAll((IoSessionConfig)getSessionConfig());
/* 164 */     return session;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void close(DatagramChannel handle) throws Exception {
/* 169 */     handle.disconnect();
/* 170 */     handle.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Iterator<DatagramChannel> allHandles() {
/* 177 */     return Collections.EMPTY_LIST.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractPollingIoConnector<NioSession, DatagramChannel>.ConnectionRequest getConnectionRequest(DatagramChannel handle) {
/* 182 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void destroy() throws Exception {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean finishConnect(DatagramChannel handle) throws Exception {
/* 192 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void register(DatagramChannel handle, AbstractPollingIoConnector<NioSession, DatagramChannel>.ConnectionRequest request) throws Exception {
/* 197 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int select(int timeout) throws Exception {
/* 202 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Iterator<DatagramChannel> selectedHandles() {
/* 208 */     return Collections.EMPTY_LIST.iterator();
/*     */   }
/*     */   
/*     */   protected void wakeup() {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/transport/socket/nio/NioDatagramConnector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */