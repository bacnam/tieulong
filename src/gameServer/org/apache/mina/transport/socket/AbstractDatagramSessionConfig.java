/*     */ package org.apache.mina.transport.socket;
/*     */ 
/*     */ import org.apache.mina.core.session.AbstractIoSessionConfig;
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
/*     */ 
/*     */ public abstract class AbstractDatagramSessionConfig
/*     */   extends AbstractIoSessionConfig
/*     */   implements DatagramSessionConfig
/*     */ {
/*     */   private static final boolean DEFAULT_CLOSE_ON_PORT_UNREACHABLE = true;
/*     */   private boolean closeOnPortUnreachable = true;
/*     */   
/*     */   protected void doSetAll(IoSessionConfig config) {
/*  42 */     if (!(config instanceof DatagramSessionConfig)) {
/*     */       return;
/*     */     }
/*     */     
/*  46 */     if (config instanceof AbstractDatagramSessionConfig) {
/*     */       
/*  48 */       AbstractDatagramSessionConfig cfg = (AbstractDatagramSessionConfig)config;
/*  49 */       if (cfg.isBroadcastChanged()) {
/*  50 */         setBroadcast(cfg.isBroadcast());
/*     */       }
/*  52 */       if (cfg.isReceiveBufferSizeChanged()) {
/*  53 */         setReceiveBufferSize(cfg.getReceiveBufferSize());
/*     */       }
/*  55 */       if (cfg.isReuseAddressChanged()) {
/*  56 */         setReuseAddress(cfg.isReuseAddress());
/*     */       }
/*  58 */       if (cfg.isSendBufferSizeChanged()) {
/*  59 */         setSendBufferSize(cfg.getSendBufferSize());
/*     */       }
/*  61 */       if (cfg.isTrafficClassChanged() && getTrafficClass() != cfg.getTrafficClass()) {
/*  62 */         setTrafficClass(cfg.getTrafficClass());
/*     */       }
/*     */     } else {
/*  65 */       DatagramSessionConfig cfg = (DatagramSessionConfig)config;
/*  66 */       setBroadcast(cfg.isBroadcast());
/*  67 */       setReceiveBufferSize(cfg.getReceiveBufferSize());
/*  68 */       setReuseAddress(cfg.isReuseAddress());
/*  69 */       setSendBufferSize(cfg.getSendBufferSize());
/*  70 */       if (getTrafficClass() != cfg.getTrafficClass()) {
/*  71 */         setTrafficClass(cfg.getTrafficClass());
/*     */       }
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
/*     */   protected boolean isBroadcastChanged() {
/*  84 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isReceiveBufferSizeChanged() {
/*  95 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isReuseAddressChanged() {
/* 106 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isSendBufferSizeChanged() {
/* 117 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isTrafficClassChanged() {
/* 128 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCloseOnPortUnreachable() {
/* 135 */     return this.closeOnPortUnreachable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCloseOnPortUnreachable(boolean closeOnPortUnreachable) {
/* 142 */     this.closeOnPortUnreachable = closeOnPortUnreachable;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/transport/socket/AbstractDatagramSessionConfig.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */