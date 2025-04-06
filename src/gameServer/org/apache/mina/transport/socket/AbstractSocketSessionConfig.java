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
/*     */ public abstract class AbstractSocketSessionConfig
/*     */   extends AbstractIoSessionConfig
/*     */   implements SocketSessionConfig
/*     */ {
/*     */   protected final void doSetAll(IoSessionConfig config) {
/*  38 */     if (!(config instanceof SocketSessionConfig)) {
/*     */       return;
/*     */     }
/*     */     
/*  42 */     if (config instanceof AbstractSocketSessionConfig) {
/*     */       
/*  44 */       AbstractSocketSessionConfig cfg = (AbstractSocketSessionConfig)config;
/*  45 */       if (cfg.isKeepAliveChanged()) {
/*  46 */         setKeepAlive(cfg.isKeepAlive());
/*     */       }
/*  48 */       if (cfg.isOobInlineChanged()) {
/*  49 */         setOobInline(cfg.isOobInline());
/*     */       }
/*  51 */       if (cfg.isReceiveBufferSizeChanged()) {
/*  52 */         setReceiveBufferSize(cfg.getReceiveBufferSize());
/*     */       }
/*  54 */       if (cfg.isReuseAddressChanged()) {
/*  55 */         setReuseAddress(cfg.isReuseAddress());
/*     */       }
/*  57 */       if (cfg.isSendBufferSizeChanged()) {
/*  58 */         setSendBufferSize(cfg.getSendBufferSize());
/*     */       }
/*  60 */       if (cfg.isSoLingerChanged()) {
/*  61 */         setSoLinger(cfg.getSoLinger());
/*     */       }
/*  63 */       if (cfg.isTcpNoDelayChanged()) {
/*  64 */         setTcpNoDelay(cfg.isTcpNoDelay());
/*     */       }
/*  66 */       if (cfg.isTrafficClassChanged() && getTrafficClass() != cfg.getTrafficClass()) {
/*  67 */         setTrafficClass(cfg.getTrafficClass());
/*     */       }
/*     */     } else {
/*  70 */       SocketSessionConfig cfg = (SocketSessionConfig)config;
/*  71 */       setKeepAlive(cfg.isKeepAlive());
/*  72 */       setOobInline(cfg.isOobInline());
/*  73 */       setReceiveBufferSize(cfg.getReceiveBufferSize());
/*  74 */       setReuseAddress(cfg.isReuseAddress());
/*  75 */       setSendBufferSize(cfg.getSendBufferSize());
/*  76 */       setSoLinger(cfg.getSoLinger());
/*  77 */       setTcpNoDelay(cfg.isTcpNoDelay());
/*  78 */       if (getTrafficClass() != cfg.getTrafficClass()) {
/*  79 */         setTrafficClass(cfg.getTrafficClass());
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
/*     */   protected boolean isKeepAliveChanged() {
/*  92 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isOobInlineChanged() {
/* 103 */     return true;
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
/* 114 */     return true;
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
/* 125 */     return true;
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
/* 136 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isSoLingerChanged() {
/* 147 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isTcpNoDelayChanged() {
/* 158 */     return true;
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
/* 169 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/transport/socket/AbstractSocketSessionConfig.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */