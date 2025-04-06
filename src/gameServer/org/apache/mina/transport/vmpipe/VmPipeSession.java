/*     */ package org.apache.mina.transport.vmpipe;
/*     */ 
/*     */ import java.net.SocketAddress;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.mina.core.filterchain.IoFilterChain;
/*     */ import org.apache.mina.core.service.DefaultTransportMetadata;
/*     */ import org.apache.mina.core.service.IoHandler;
/*     */ import org.apache.mina.core.service.IoProcessor;
/*     */ import org.apache.mina.core.service.IoService;
/*     */ import org.apache.mina.core.service.IoServiceListenerSupport;
/*     */ import org.apache.mina.core.service.TransportMetadata;
/*     */ import org.apache.mina.core.session.AbstractIoSession;
/*     */ import org.apache.mina.core.session.IoSessionConfig;
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
/*     */ class VmPipeSession
/*     */   extends AbstractIoSession
/*     */ {
/*  45 */   static final TransportMetadata METADATA = (TransportMetadata)new DefaultTransportMetadata("mina", "vmpipe", false, false, VmPipeAddress.class, VmPipeSessionConfig.class, new Class[] { Object.class });
/*     */ 
/*     */   
/*     */   private final IoServiceListenerSupport serviceListeners;
/*     */ 
/*     */   
/*     */   private final VmPipeAddress localAddress;
/*     */ 
/*     */   
/*     */   private final VmPipeAddress remoteAddress;
/*     */ 
/*     */   
/*     */   private final VmPipeAddress serviceAddress;
/*     */   
/*     */   private final VmPipeFilterChain filterChain;
/*     */   
/*     */   private final VmPipeSession remoteSession;
/*     */   
/*     */   private final Lock lock;
/*     */   
/*     */   final BlockingQueue<Object> receivedMessageQueue;
/*     */ 
/*     */   
/*     */   VmPipeSession(IoService service, IoServiceListenerSupport serviceListeners, VmPipeAddress localAddress, IoHandler handler, VmPipe remoteEntry) {
/*  69 */     super(service);
/*  70 */     this.config = new DefaultVmPipeSessionConfig();
/*  71 */     this.serviceListeners = serviceListeners;
/*  72 */     this.lock = new ReentrantLock();
/*  73 */     this.localAddress = localAddress;
/*  74 */     this.remoteAddress = this.serviceAddress = remoteEntry.getAddress();
/*  75 */     this.filterChain = new VmPipeFilterChain(this);
/*  76 */     this.receivedMessageQueue = new LinkedBlockingQueue();
/*     */     
/*  78 */     this.remoteSession = new VmPipeSession(this, remoteEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private VmPipeSession(VmPipeSession remoteSession, VmPipe entry) {
/*  85 */     super((IoService)entry.getAcceptor());
/*  86 */     this.config = new DefaultVmPipeSessionConfig();
/*  87 */     this.serviceListeners = entry.getListeners();
/*  88 */     this.lock = remoteSession.lock;
/*  89 */     this.localAddress = this.serviceAddress = remoteSession.remoteAddress;
/*  90 */     this.remoteAddress = remoteSession.localAddress;
/*  91 */     this.filterChain = new VmPipeFilterChain(this);
/*  92 */     this.remoteSession = remoteSession;
/*  93 */     this.receivedMessageQueue = new LinkedBlockingQueue();
/*     */   }
/*     */ 
/*     */   
/*     */   public IoProcessor<VmPipeSession> getProcessor() {
/*  98 */     return this.filterChain.getProcessor();
/*     */   }
/*     */   
/*     */   IoServiceListenerSupport getServiceListeners() {
/* 102 */     return this.serviceListeners;
/*     */   }
/*     */   
/*     */   public VmPipeSessionConfig getConfig() {
/* 106 */     return (VmPipeSessionConfig)this.config;
/*     */   }
/*     */   
/*     */   public IoFilterChain getFilterChain() {
/* 110 */     return (IoFilterChain)this.filterChain;
/*     */   }
/*     */   
/*     */   public VmPipeSession getRemoteSession() {
/* 114 */     return this.remoteSession;
/*     */   }
/*     */   
/*     */   public TransportMetadata getTransportMetadata() {
/* 118 */     return METADATA;
/*     */   }
/*     */   
/*     */   public VmPipeAddress getRemoteAddress() {
/* 122 */     return this.remoteAddress;
/*     */   }
/*     */   
/*     */   public VmPipeAddress getLocalAddress() {
/* 126 */     return this.localAddress;
/*     */   }
/*     */ 
/*     */   
/*     */   public VmPipeAddress getServiceAddress() {
/* 131 */     return this.serviceAddress;
/*     */   }
/*     */   
/*     */   void increaseWrittenBytes0(int increment, long currentTime) {
/* 135 */     increaseWrittenBytes(increment, currentTime);
/*     */   }
/*     */   
/*     */   WriteRequestQueue getWriteRequestQueue0() {
/* 139 */     return getWriteRequestQueue();
/*     */   }
/*     */   
/*     */   Lock getLock() {
/* 143 */     return this.lock;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/transport/vmpipe/VmPipeSession.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */