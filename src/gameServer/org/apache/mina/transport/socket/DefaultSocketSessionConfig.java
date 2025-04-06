/*     */ package org.apache.mina.transport.socket;
/*     */ 
/*     */ import org.apache.mina.core.service.IoService;
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
/*     */ public class DefaultSocketSessionConfig
/*     */   extends AbstractSocketSessionConfig
/*     */ {
/*     */   private static boolean DEFAULT_REUSE_ADDRESS = false;
/*  32 */   private static int DEFAULT_TRAFFIC_CLASS = 0;
/*     */   
/*     */   private static boolean DEFAULT_KEEP_ALIVE = false;
/*     */   
/*     */   private static boolean DEFAULT_OOB_INLINE = false;
/*     */   
/*  38 */   private static int DEFAULT_SO_LINGER = -1;
/*     */ 
/*     */   
/*     */   private static boolean DEFAULT_TCP_NO_DELAY = false;
/*     */   
/*     */   protected IoService parent;
/*     */   
/*     */   private boolean defaultReuseAddress;
/*     */   
/*     */   private boolean reuseAddress;
/*     */   
/*  49 */   private int receiveBufferSize = -1;
/*     */ 
/*     */   
/*  52 */   private int sendBufferSize = -1;
/*     */   
/*  54 */   private int trafficClass = DEFAULT_TRAFFIC_CLASS;
/*     */   
/*  56 */   private boolean keepAlive = DEFAULT_KEEP_ALIVE;
/*     */   
/*  58 */   private boolean oobInline = DEFAULT_OOB_INLINE;
/*     */   
/*  60 */   private int soLinger = DEFAULT_SO_LINGER;
/*     */   
/*  62 */   private boolean tcpNoDelay = DEFAULT_TCP_NO_DELAY;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init(IoService parent) {
/*  72 */     this.parent = parent;
/*     */     
/*  74 */     if (parent instanceof SocketAcceptor) {
/*  75 */       this.defaultReuseAddress = true;
/*     */     } else {
/*  77 */       this.defaultReuseAddress = DEFAULT_REUSE_ADDRESS;
/*     */     } 
/*     */     
/*  80 */     this.reuseAddress = this.defaultReuseAddress;
/*     */   }
/*     */   
/*     */   public boolean isReuseAddress() {
/*  84 */     return this.reuseAddress;
/*     */   }
/*     */   
/*     */   public void setReuseAddress(boolean reuseAddress) {
/*  88 */     this.reuseAddress = reuseAddress;
/*     */   }
/*     */   
/*     */   public int getReceiveBufferSize() {
/*  92 */     return this.receiveBufferSize;
/*     */   }
/*     */   
/*     */   public void setReceiveBufferSize(int receiveBufferSize) {
/*  96 */     this.receiveBufferSize = receiveBufferSize;
/*     */   }
/*     */   
/*     */   public int getSendBufferSize() {
/* 100 */     return this.sendBufferSize;
/*     */   }
/*     */   
/*     */   public void setSendBufferSize(int sendBufferSize) {
/* 104 */     this.sendBufferSize = sendBufferSize;
/*     */   }
/*     */   
/*     */   public int getTrafficClass() {
/* 108 */     return this.trafficClass;
/*     */   }
/*     */   
/*     */   public void setTrafficClass(int trafficClass) {
/* 112 */     this.trafficClass = trafficClass;
/*     */   }
/*     */   
/*     */   public boolean isKeepAlive() {
/* 116 */     return this.keepAlive;
/*     */   }
/*     */   
/*     */   public void setKeepAlive(boolean keepAlive) {
/* 120 */     this.keepAlive = keepAlive;
/*     */   }
/*     */   
/*     */   public boolean isOobInline() {
/* 124 */     return this.oobInline;
/*     */   }
/*     */   
/*     */   public void setOobInline(boolean oobInline) {
/* 128 */     this.oobInline = oobInline;
/*     */   }
/*     */   
/*     */   public int getSoLinger() {
/* 132 */     return this.soLinger;
/*     */   }
/*     */   
/*     */   public void setSoLinger(int soLinger) {
/* 136 */     this.soLinger = soLinger;
/*     */   }
/*     */   
/*     */   public boolean isTcpNoDelay() {
/* 140 */     return this.tcpNoDelay;
/*     */   }
/*     */   
/*     */   public void setTcpNoDelay(boolean tcpNoDelay) {
/* 144 */     this.tcpNoDelay = tcpNoDelay;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isKeepAliveChanged() {
/* 149 */     return (this.keepAlive != DEFAULT_KEEP_ALIVE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isOobInlineChanged() {
/* 154 */     return (this.oobInline != DEFAULT_OOB_INLINE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isReceiveBufferSizeChanged() {
/* 159 */     return (this.receiveBufferSize != -1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isReuseAddressChanged() {
/* 164 */     return (this.reuseAddress != this.defaultReuseAddress);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSendBufferSizeChanged() {
/* 169 */     return (this.sendBufferSize != -1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSoLingerChanged() {
/* 174 */     return (this.soLinger != DEFAULT_SO_LINGER);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isTcpNoDelayChanged() {
/* 179 */     return (this.tcpNoDelay != DEFAULT_TCP_NO_DELAY);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isTrafficClassChanged() {
/* 184 */     return (this.trafficClass != DEFAULT_TRAFFIC_CLASS);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/transport/socket/DefaultSocketSessionConfig.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */