/*     */ package org.apache.mina.transport.vmpipe;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import org.apache.mina.core.future.IoFuture;
/*     */ import org.apache.mina.core.service.AbstractIoAcceptor;
/*     */ import org.apache.mina.core.service.TransportMetadata;
/*     */ import org.apache.mina.core.session.IdleStatusChecker;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.session.IoSessionConfig;
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
/*     */ public final class VmPipeAcceptor
/*     */   extends AbstractIoAcceptor
/*     */ {
/*     */   private IdleStatusChecker idleChecker;
/*  49 */   static final Map<VmPipeAddress, VmPipe> boundHandlers = new HashMap<VmPipeAddress, VmPipe>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VmPipeAcceptor() {
/*  55 */     this((Executor)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VmPipeAcceptor(Executor executor) {
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
/*     */   public VmPipeAddress getLocalAddress() {
/*  82 */     return (VmPipeAddress)super.getLocalAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   public VmPipeAddress getDefaultLocalAddress() {
/*  87 */     return (VmPipeAddress)super.getDefaultLocalAddress();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultLocalAddress(VmPipeAddress localAddress) {
/*  94 */     setDefaultLocalAddress(localAddress);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void dispose0() throws Exception {
/* 100 */     this.idleChecker.getNotifyingTask().cancel();
/* 101 */     unbind();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Set<SocketAddress> bindInternal(List<? extends SocketAddress> localAddresses) throws IOException {
/* 106 */     Set<SocketAddress> newLocalAddresses = new HashSet<SocketAddress>();
/*     */     
/* 108 */     synchronized (boundHandlers) {
/* 109 */       for (SocketAddress a : localAddresses) {
/* 110 */         VmPipeAddress localAddress = (VmPipeAddress)a;
/* 111 */         if (localAddress == null || localAddress.getPort() == 0) {
/* 112 */           localAddress = null;
/* 113 */           for (int i = 10000; i < Integer.MAX_VALUE; i++) {
/* 114 */             VmPipeAddress newLocalAddress = new VmPipeAddress(i);
/* 115 */             if (!boundHandlers.containsKey(newLocalAddress) && !newLocalAddresses.contains(newLocalAddress)) {
/* 116 */               localAddress = newLocalAddress;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/* 121 */           if (localAddress == null)
/* 122 */             throw new IOException("No port available."); 
/*     */         } else {
/* 124 */           if (localAddress.getPort() < 0)
/* 125 */             throw new IOException("Bind port number must be 0 or above."); 
/* 126 */           if (boundHandlers.containsKey(localAddress)) {
/* 127 */             throw new IOException("Address already bound: " + localAddress);
/*     */           }
/*     */         } 
/* 130 */         newLocalAddresses.add(localAddress);
/*     */       } 
/*     */       
/* 133 */       for (SocketAddress a : newLocalAddresses) {
/* 134 */         VmPipeAddress localAddress = (VmPipeAddress)a;
/* 135 */         if (!boundHandlers.containsKey(localAddress)) {
/* 136 */           boundHandlers.put(localAddress, new VmPipe(this, localAddress, getHandler(), getListeners())); continue;
/*     */         } 
/* 138 */         for (SocketAddress a2 : newLocalAddresses) {
/* 139 */           boundHandlers.remove(a2);
/*     */         }
/* 141 */         throw new IOException("Duplicate local address: " + a);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 146 */     return newLocalAddresses;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void unbind0(List<? extends SocketAddress> localAddresses) {
/* 151 */     synchronized (boundHandlers) {
/* 152 */       for (SocketAddress a : localAddresses) {
/* 153 */         boundHandlers.remove(a);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public IoSession newSession(SocketAddress remoteAddress, SocketAddress localAddress) {
/* 159 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   void doFinishSessionInitialization(IoSession session, IoFuture future) {
/* 163 */     initSession(session, future, null);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/transport/vmpipe/VmPipeAcceptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */