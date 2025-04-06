/*     */ package org.apache.mina.transport.vmpipe;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import org.apache.mina.core.filterchain.IoFilterChain;
/*     */ import org.apache.mina.core.future.ConnectFuture;
/*     */ import org.apache.mina.core.future.DefaultConnectFuture;
/*     */ import org.apache.mina.core.future.IoFuture;
/*     */ import org.apache.mina.core.future.IoFutureListener;
/*     */ import org.apache.mina.core.service.AbstractIoConnector;
/*     */ import org.apache.mina.core.service.IoService;
/*     */ import org.apache.mina.core.service.TransportMetadata;
/*     */ import org.apache.mina.core.session.IdleStatusChecker;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.session.IoSessionConfig;
/*     */ import org.apache.mina.core.session.IoSessionInitializer;
/*     */ import org.apache.mina.util.ExceptionMonitor;
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
/*     */ public final class VmPipeConnector
/*     */   extends AbstractIoConnector
/*     */ {
/*     */   private IdleStatusChecker idleChecker;
/*     */   
/*     */   public VmPipeConnector() {
/*  55 */     this((Executor)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VmPipeConnector(Executor executor) {
/*  62 */     super(new DefaultVmPipeSessionConfig(), executor);
/*  63 */     this.idleChecker = new IdleStatusChecker();
/*     */ 
/*     */     
/*  66 */     executeWorker((Runnable)this.idleChecker.getNotifyingTask(), "idleStatusChecker");
/*     */   }
/*     */   
/*     */   public TransportMetadata getTransportMetadata() {
/*  70 */     return VmPipeSession.METADATA;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VmPipeSessionConfig getSessionConfig() {
/*  77 */     return (VmPipeSessionConfig)this.sessionConfig;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ConnectFuture connect0(SocketAddress remoteAddress, SocketAddress localAddress, IoSessionInitializer<? extends ConnectFuture> sessionInitializer) {
/*     */     VmPipeAddress actualLocalAddress;
/*  83 */     VmPipe entry = VmPipeAcceptor.boundHandlers.get(remoteAddress);
/*  84 */     if (entry == null) {
/*  85 */       return DefaultConnectFuture.newFailedFuture(new IOException("Endpoint unavailable: " + remoteAddress));
/*     */     }
/*     */     
/*  88 */     DefaultConnectFuture future = new DefaultConnectFuture();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  93 */       actualLocalAddress = nextLocalAddress();
/*  94 */     } catch (IOException e) {
/*  95 */       return DefaultConnectFuture.newFailedFuture(e);
/*     */     } 
/*     */     
/*  98 */     VmPipeSession localSession = new VmPipeSession((IoService)this, getListeners(), actualLocalAddress, getHandler(), entry);
/*     */     
/* 100 */     initSession((IoSession)localSession, (IoFuture)future, sessionInitializer);
/*     */ 
/*     */     
/* 103 */     localSession.getCloseFuture().addListener(LOCAL_ADDRESS_RECLAIMER);
/*     */ 
/*     */     
/*     */     try {
/* 107 */       IoFilterChain filterChain = localSession.getFilterChain();
/* 108 */       getFilterChainBuilder().buildFilterChain(filterChain);
/*     */ 
/*     */       
/* 111 */       getListeners().fireSessionCreated((IoSession)localSession);
/* 112 */       this.idleChecker.addSession(localSession);
/* 113 */     } catch (Exception e) {
/* 114 */       future.setException(e);
/* 115 */       return (ConnectFuture)future;
/*     */     } 
/*     */ 
/*     */     
/* 119 */     VmPipeSession remoteSession = localSession.getRemoteSession();
/* 120 */     ((VmPipeAcceptor)remoteSession.getService()).doFinishSessionInitialization((IoSession)remoteSession, (IoFuture)null);
/*     */     try {
/* 122 */       IoFilterChain filterChain = remoteSession.getFilterChain();
/* 123 */       entry.getAcceptor().getFilterChainBuilder().buildFilterChain(filterChain);
/*     */ 
/*     */       
/* 126 */       entry.getListeners().fireSessionCreated((IoSession)remoteSession);
/* 127 */       this.idleChecker.addSession(remoteSession);
/* 128 */     } catch (Exception e) {
/* 129 */       ExceptionMonitor.getInstance().exceptionCaught(e);
/* 130 */       remoteSession.close(true);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 135 */     ((VmPipeFilterChain)localSession.getFilterChain()).start();
/* 136 */     ((VmPipeFilterChain)remoteSession.getFilterChain()).start();
/*     */     
/* 138 */     return (ConnectFuture)future;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void dispose0() throws Exception {
/* 144 */     this.idleChecker.getNotifyingTask().cancel();
/*     */   }
/*     */   
/* 147 */   private static final Set<VmPipeAddress> TAKEN_LOCAL_ADDRESSES = new HashSet<VmPipeAddress>();
/*     */   
/* 149 */   private static int nextLocalPort = -1;
/*     */   
/* 151 */   private static final IoFutureListener<IoFuture> LOCAL_ADDRESS_RECLAIMER = new LocalAddressReclaimer();
/*     */   
/*     */   private static VmPipeAddress nextLocalAddress() throws IOException {
/* 154 */     synchronized (TAKEN_LOCAL_ADDRESSES) {
/* 155 */       if (nextLocalPort >= 0) {
/* 156 */         nextLocalPort = -1;
/*     */       }
/* 158 */       for (int i = 0; i < Integer.MAX_VALUE; i++) {
/* 159 */         VmPipeAddress answer = new VmPipeAddress(nextLocalPort--);
/* 160 */         if (!TAKEN_LOCAL_ADDRESSES.contains(answer)) {
/* 161 */           TAKEN_LOCAL_ADDRESSES.add(answer);
/* 162 */           return answer;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 167 */     throw new IOException("Can't assign a local VM pipe port.");
/*     */   }
/*     */   
/*     */   private static class LocalAddressReclaimer implements IoFutureListener<IoFuture> {
/*     */     public void operationComplete(IoFuture future) {
/* 172 */       synchronized (VmPipeConnector.TAKEN_LOCAL_ADDRESSES) {
/* 173 */         VmPipeConnector.TAKEN_LOCAL_ADDRESSES.remove(future.getSession().getLocalAddress());
/*     */       } 
/*     */     }
/*     */     
/*     */     private LocalAddressReclaimer() {}
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/transport/vmpipe/VmPipeConnector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */