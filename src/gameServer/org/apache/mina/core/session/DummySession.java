/*     */ package org.apache.mina.core.session;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import org.apache.mina.core.file.FileRegion;
/*     */ import org.apache.mina.core.filterchain.DefaultIoFilterChain;
/*     */ import org.apache.mina.core.filterchain.IoFilterChain;
/*     */ import org.apache.mina.core.service.AbstractIoAcceptor;
/*     */ import org.apache.mina.core.service.DefaultTransportMetadata;
/*     */ import org.apache.mina.core.service.IoHandler;
/*     */ import org.apache.mina.core.service.IoHandlerAdapter;
/*     */ import org.apache.mina.core.service.IoProcessor;
/*     */ import org.apache.mina.core.service.IoService;
/*     */ import org.apache.mina.core.service.TransportMetadata;
/*     */ import org.apache.mina.core.write.WriteRequest;
/*     */ import org.apache.mina.core.write.WriteRequestQueue;
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
/*     */ public class DummySession
/*     */   extends AbstractIoSession
/*     */ {
/*  57 */   private static final TransportMetadata TRANSPORT_METADATA = (TransportMetadata)new DefaultTransportMetadata("mina", "dummy", false, false, SocketAddress.class, IoSessionConfig.class, new Class[] { Object.class });
/*     */ 
/*     */   
/*  60 */   private static final SocketAddress ANONYMOUS_ADDRESS = new SocketAddress()
/*     */     {
/*     */       private static final long serialVersionUID = -496112902353454179L;
/*     */       
/*     */       public String toString() {
/*  65 */         return "?";
/*     */       }
/*     */     };
/*     */   
/*     */   private volatile IoService service;
/*     */   
/*  71 */   private volatile IoSessionConfig config = new AbstractIoSessionConfig()
/*     */     {
/*     */       protected void doSetAll(IoSessionConfig config) {}
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*  78 */   private final IoFilterChain filterChain = (IoFilterChain)new DefaultIoFilterChain(this);
/*     */   
/*     */   private final IoProcessor<IoSession> processor;
/*     */   
/*  82 */   private volatile IoHandler handler = (IoHandler)new IoHandlerAdapter();
/*     */   
/*  84 */   private volatile SocketAddress localAddress = ANONYMOUS_ADDRESS;
/*     */   
/*  86 */   private volatile SocketAddress remoteAddress = ANONYMOUS_ADDRESS;
/*     */   
/*  88 */   private volatile TransportMetadata transportMetadata = TRANSPORT_METADATA;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DummySession() {
/*  94 */     super((IoService)new AbstractIoAcceptor(new AbstractIoSessionConfig() { protected void doSetAll(IoSessionConfig config) {} }, new Executor()
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             public void execute(Runnable command) {}
/*     */           })
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           protected Set<SocketAddress> bindInternal(List<? extends SocketAddress> localAddresses) throws Exception
/*     */           {
/* 111 */             throw new UnsupportedOperationException();
/*     */           }
/*     */ 
/*     */           
/*     */           protected void unbind0(List<? extends SocketAddress> localAddresses) throws Exception {
/* 116 */             throw new UnsupportedOperationException();
/*     */           }
/*     */           
/*     */           public IoSession newSession(SocketAddress remoteAddress, SocketAddress localAddress) {
/* 120 */             throw new UnsupportedOperationException();
/*     */           }
/*     */           
/*     */           public TransportMetadata getTransportMetadata() {
/* 124 */             return DummySession.TRANSPORT_METADATA;
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           protected void dispose0() throws Exception {}
/*     */ 
/*     */ 
/*     */           
/*     */           public IoSessionConfig getSessionConfig() {
/* 135 */             return this.sessionConfig;
/*     */           }
/*     */         });
/*     */     
/* 139 */     this.processor = new IoProcessor<IoSession>()
/*     */       {
/*     */         public void add(IoSession session) {}
/*     */ 
/*     */         
/*     */         public void flush(IoSession session) {
/* 145 */           DummySession s = (DummySession)session;
/* 146 */           WriteRequest req = s.getWriteRequestQueue().poll(session);
/*     */ 
/*     */ 
/*     */           
/* 150 */           if (req != null) {
/* 151 */             Object m = req.getMessage();
/* 152 */             if (m instanceof FileRegion) {
/* 153 */               FileRegion file = (FileRegion)m;
/*     */               try {
/* 155 */                 file.getFileChannel().position(file.getPosition() + file.getRemainingBytes());
/* 156 */                 file.update(file.getRemainingBytes());
/* 157 */               } catch (IOException e) {
/* 158 */                 s.getFilterChain().fireExceptionCaught(e);
/*     */               } 
/*     */             } 
/* 161 */             DummySession.this.getFilterChain().fireMessageSent(req);
/*     */           } 
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void write(IoSession session, WriteRequest writeRequest) {
/* 169 */           WriteRequestQueue writeRequestQueue = session.getWriteRequestQueue();
/*     */           
/* 171 */           writeRequestQueue.offer(session, writeRequest);
/*     */           
/* 173 */           if (!session.isWriteSuspended()) {
/* 174 */             flush(session);
/*     */           }
/*     */         }
/*     */         
/*     */         public void remove(IoSession session) {
/* 179 */           if (!session.getCloseFuture().isClosed()) {
/* 180 */             session.getFilterChain().fireSessionClosed();
/*     */           }
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void updateTrafficControl(IoSession session) {}
/*     */ 
/*     */         
/*     */         public void dispose() {}
/*     */ 
/*     */         
/*     */         public boolean isDisposed() {
/* 193 */           return false;
/*     */         }
/*     */         
/*     */         public boolean isDisposing() {
/* 197 */           return false;
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 202 */     this.service = super.getService();
/*     */     
/*     */     try {
/* 205 */       IoSessionDataStructureFactory factory = new DefaultIoSessionDataStructureFactory();
/* 206 */       setAttributeMap(factory.getAttributeMap(this));
/* 207 */       setWriteRequestQueue(factory.getWriteRequestQueue(this));
/* 208 */     } catch (Exception e) {
/* 209 */       throw new InternalError();
/*     */     } 
/*     */   }
/*     */   
/*     */   public IoSessionConfig getConfig() {
/* 214 */     return this.config;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConfig(IoSessionConfig config) {
/* 221 */     if (config == null) {
/* 222 */       throw new IllegalArgumentException("config");
/*     */     }
/*     */     
/* 225 */     this.config = config;
/*     */   }
/*     */   
/*     */   public IoFilterChain getFilterChain() {
/* 229 */     return this.filterChain;
/*     */   }
/*     */   
/*     */   public IoHandler getHandler() {
/* 233 */     return this.handler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHandler(IoHandler handler) {
/* 240 */     if (handler == null) {
/* 241 */       throw new IllegalArgumentException("handler");
/*     */     }
/*     */     
/* 244 */     this.handler = handler;
/*     */   }
/*     */   
/*     */   public SocketAddress getLocalAddress() {
/* 248 */     return this.localAddress;
/*     */   }
/*     */   
/*     */   public SocketAddress getRemoteAddress() {
/* 252 */     return this.remoteAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocalAddress(SocketAddress localAddress) {
/* 260 */     if (localAddress == null) {
/* 261 */       throw new IllegalArgumentException("localAddress");
/*     */     }
/*     */     
/* 264 */     this.localAddress = localAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoteAddress(SocketAddress remoteAddress) {
/* 271 */     if (remoteAddress == null) {
/* 272 */       throw new IllegalArgumentException("remoteAddress");
/*     */     }
/*     */     
/* 275 */     this.remoteAddress = remoteAddress;
/*     */   }
/*     */   
/*     */   public IoService getService() {
/* 279 */     return this.service;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setService(IoService service) {
/* 286 */     if (service == null) {
/* 287 */       throw new IllegalArgumentException("service");
/*     */     }
/*     */     
/* 290 */     this.service = service;
/*     */   }
/*     */ 
/*     */   
/*     */   public final IoProcessor<IoSession> getProcessor() {
/* 295 */     return this.processor;
/*     */   }
/*     */   
/*     */   public TransportMetadata getTransportMetadata() {
/* 299 */     return this.transportMetadata;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTransportMetadata(TransportMetadata transportMetadata) {
/* 306 */     if (transportMetadata == null) {
/* 307 */       throw new IllegalArgumentException("transportMetadata");
/*     */     }
/*     */     
/* 310 */     this.transportMetadata = transportMetadata;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScheduledWriteBytes(int byteCount) {
/* 315 */     super.setScheduledWriteBytes(byteCount);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScheduledWriteMessages(int messages) {
/* 320 */     super.setScheduledWriteMessages(messages);
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
/*     */   public void updateThroughput(boolean force) {
/* 332 */     updateThroughput(System.currentTimeMillis(), force);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/session/DummySession.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */