/*     */ package org.apache.http.impl.nio.reactor;
/*     */ 
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.util.Args;
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
/*     */ @NotThreadSafe
/*     */ public final class IOReactorConfig
/*     */   implements Cloneable
/*     */ {
/*  41 */   private static final int AVAIL_PROCS = Runtime.getRuntime().availableProcessors();
/*     */   
/*  43 */   public static final IOReactorConfig DEFAULT = (new Builder()).build();
/*     */   
/*     */   private long selectInterval;
/*     */   
/*     */   private long shutdownGracePeriod;
/*     */   
/*     */   private boolean interestOpQueued;
/*     */   private int ioThreadCount;
/*     */   private int soTimeout;
/*     */   private boolean soReuseAddress;
/*     */   private int soLinger;
/*     */   private boolean soKeepAlive;
/*     */   private boolean tcpNoDelay;
/*     */   private int connectTimeout;
/*     */   private int sndBufSize;
/*     */   private int rcvBufSize;
/*     */   private int backlogSize;
/*     */   
/*     */   @Deprecated
/*     */   public IOReactorConfig() {
/*  63 */     this.selectInterval = 1000L;
/*  64 */     this.shutdownGracePeriod = 500L;
/*  65 */     this.interestOpQueued = false;
/*  66 */     this.ioThreadCount = AVAIL_PROCS;
/*  67 */     this.soTimeout = 0;
/*  68 */     this.soReuseAddress = false;
/*  69 */     this.soLinger = -1;
/*  70 */     this.soKeepAlive = false;
/*  71 */     this.tcpNoDelay = true;
/*  72 */     this.connectTimeout = 0;
/*  73 */     this.sndBufSize = 0;
/*  74 */     this.rcvBufSize = 0;
/*  75 */     this.backlogSize = 0;
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
/*     */   IOReactorConfig(long selectInterval, long shutdownGracePeriod, boolean interestOpQueued, int ioThreadCount, int soTimeout, boolean soReuseAddress, int soLinger, boolean soKeepAlive, boolean tcpNoDelay, int connectTimeout, int sndBufSize, int rcvBufSize, int backlogSize) {
/*  93 */     this.selectInterval = selectInterval;
/*  94 */     this.shutdownGracePeriod = shutdownGracePeriod;
/*  95 */     this.interestOpQueued = interestOpQueued;
/*  96 */     this.ioThreadCount = ioThreadCount;
/*  97 */     this.soTimeout = soTimeout;
/*  98 */     this.soReuseAddress = soReuseAddress;
/*  99 */     this.soLinger = soLinger;
/* 100 */     this.soKeepAlive = soKeepAlive;
/* 101 */     this.tcpNoDelay = tcpNoDelay;
/* 102 */     this.connectTimeout = connectTimeout;
/* 103 */     this.sndBufSize = sndBufSize;
/* 104 */     this.rcvBufSize = rcvBufSize;
/* 105 */     this.backlogSize = backlogSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSelectInterval() {
/* 115 */     return this.selectInterval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setSelectInterval(long selectInterval) {
/* 123 */     Args.positive(selectInterval, "Select internal");
/* 124 */     this.selectInterval = selectInterval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getShutdownGracePeriod() {
/* 134 */     return this.shutdownGracePeriod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setShutdownGracePeriod(long gracePeriod) {
/* 142 */     Args.positive(gracePeriod, "Shutdown grace period");
/* 143 */     this.shutdownGracePeriod = gracePeriod;
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
/*     */   public boolean isInterestOpQueued() {
/* 158 */     return this.interestOpQueued;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setInterestOpQueued(boolean interestOpQueued) {
/* 166 */     this.interestOpQueued = interestOpQueued;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIoThreadCount() {
/* 175 */     return this.ioThreadCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setIoThreadCount(int ioThreadCount) {
/* 183 */     Args.positive(ioThreadCount, "I/O thread count");
/* 184 */     this.ioThreadCount = ioThreadCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSoTimeout() {
/* 195 */     return this.soTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setSoTimeout(int soTimeout) {
/* 203 */     this.soTimeout = soTimeout;
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
/*     */   public boolean isSoReuseAddress() {
/* 215 */     return this.soReuseAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setSoReuseAddress(boolean soReuseAddress) {
/* 223 */     this.soReuseAddress = soReuseAddress;
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
/*     */   public int getSoLinger() {
/* 235 */     return this.soLinger;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setSoLinger(int soLinger) {
/* 243 */     this.soLinger = soLinger;
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
/*     */   public boolean isSoKeepalive() {
/* 255 */     return this.soKeepAlive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setSoKeepalive(boolean soKeepAlive) {
/* 263 */     this.soKeepAlive = soKeepAlive;
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
/*     */   public boolean isTcpNoDelay() {
/* 275 */     return this.tcpNoDelay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setTcpNoDelay(boolean tcpNoDelay) {
/* 283 */     this.tcpNoDelay = tcpNoDelay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getConnectTimeout() {
/* 292 */     return this.connectTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setConnectTimeout(int connectTimeout) {
/* 300 */     this.connectTimeout = connectTimeout;
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
/*     */   public int getSndBufSize() {
/* 312 */     return this.sndBufSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setSndBufSize(int sndBufSize) {
/* 320 */     this.sndBufSize = sndBufSize;
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
/*     */   public int getRcvBufSize() {
/* 332 */     return this.rcvBufSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setRcvBufSize(int rcvBufSize) {
/* 340 */     this.rcvBufSize = rcvBufSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBacklogSize() {
/* 351 */     return this.backlogSize;
/*     */   }
/*     */ 
/*     */   
/*     */   protected IOReactorConfig clone() throws CloneNotSupportedException {
/* 356 */     return (IOReactorConfig)super.clone();
/*     */   }
/*     */   
/*     */   public static Builder custom() {
/* 360 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static Builder copy(IOReactorConfig config) {
/* 364 */     Args.notNull(config, "I/O reactor config");
/* 365 */     return (new Builder()).setSelectInterval(config.getSelectInterval()).setShutdownGracePeriod(config.getShutdownGracePeriod()).setInterestOpQueued(config.isInterestOpQueued()).setIoThreadCount(config.getIoThreadCount()).setSoTimeout(config.getSoTimeout()).setSoReuseAddress(config.isSoReuseAddress()).setSoLinger(config.getSoLinger()).setSoKeepAlive(config.isSoKeepalive()).setTcpNoDelay(config.isTcpNoDelay()).setConnectTimeout(config.getConnectTimeout()).setSndBufSize(config.getSndBufSize()).setRcvBufSize(config.getRcvBufSize()).setBacklogSize(config.getBacklogSize());
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
/*     */   public static class Builder
/*     */   {
/* 398 */     private long selectInterval = 1000L;
/* 399 */     private long shutdownGracePeriod = 500L;
/*     */     private boolean interestOpQueued = false;
/* 401 */     private int ioThreadCount = IOReactorConfig.AVAIL_PROCS;
/* 402 */     private int soTimeout = 0;
/*     */     private boolean soReuseAddress = false;
/* 404 */     private int soLinger = -1;
/*     */     private boolean soKeepAlive = false;
/*     */     private boolean tcpNoDelay = true;
/* 407 */     private int connectTimeout = 0;
/* 408 */     private int sndBufSize = 0;
/* 409 */     private int rcvBufSize = 0;
/* 410 */     private int backlogSize = 0;
/*     */ 
/*     */     
/*     */     public Builder setSelectInterval(long selectInterval) {
/* 414 */       this.selectInterval = selectInterval;
/* 415 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setShutdownGracePeriod(long shutdownGracePeriod) {
/* 419 */       this.shutdownGracePeriod = shutdownGracePeriod;
/* 420 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setInterestOpQueued(boolean interestOpQueued) {
/* 424 */       this.interestOpQueued = interestOpQueued;
/* 425 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setIoThreadCount(int ioThreadCount) {
/* 429 */       this.ioThreadCount = ioThreadCount;
/* 430 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSoTimeout(int soTimeout) {
/* 434 */       this.soTimeout = soTimeout;
/* 435 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSoReuseAddress(boolean soReuseAddress) {
/* 439 */       this.soReuseAddress = soReuseAddress;
/* 440 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSoLinger(int soLinger) {
/* 444 */       this.soLinger = soLinger;
/* 445 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSoKeepAlive(boolean soKeepAlive) {
/* 449 */       this.soKeepAlive = soKeepAlive;
/* 450 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setTcpNoDelay(boolean tcpNoDelay) {
/* 454 */       this.tcpNoDelay = tcpNoDelay;
/* 455 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setConnectTimeout(int connectTimeout) {
/* 459 */       this.connectTimeout = connectTimeout;
/* 460 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSndBufSize(int sndBufSize) {
/* 464 */       this.sndBufSize = sndBufSize;
/* 465 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setRcvBufSize(int rcvBufSize) {
/* 469 */       this.rcvBufSize = rcvBufSize;
/* 470 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setBacklogSize(int backlogSize) {
/* 474 */       this.backlogSize = backlogSize;
/* 475 */       return this;
/*     */     }
/*     */     
/*     */     public IOReactorConfig build() {
/* 479 */       return new IOReactorConfig(this.selectInterval, this.shutdownGracePeriod, this.interestOpQueued, this.ioThreadCount, this.soTimeout, this.soReuseAddress, this.soLinger, this.soKeepAlive, this.tcpNoDelay, this.connectTimeout, this.sndBufSize, this.rcvBufSize, this.backlogSize);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 489 */     StringBuilder builder = new StringBuilder();
/* 490 */     builder.append("[selectInterval=").append(this.selectInterval).append(", shutdownGracePeriod=").append(this.shutdownGracePeriod).append(", interestOpQueued=").append(this.interestOpQueued).append(", ioThreadCount=").append(this.ioThreadCount).append(", soTimeout=").append(this.soTimeout).append(", soReuseAddress=").append(this.soReuseAddress).append(", soLinger=").append(this.soLinger).append(", soKeepAlive=").append(this.soKeepAlive).append(", tcpNoDelay=").append(this.tcpNoDelay).append(", connectTimeout=").append(this.connectTimeout).append(", sndBufSize=").append(this.sndBufSize).append(", rcvBufSize=").append(this.rcvBufSize).append(", backlogSize=").append(this.backlogSize).append("]");
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
/* 504 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/reactor/IOReactorConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */