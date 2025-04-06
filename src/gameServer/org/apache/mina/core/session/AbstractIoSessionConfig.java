/*     */ package org.apache.mina.core.session;
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
/*     */ public abstract class AbstractIoSessionConfig
/*     */   implements IoSessionConfig
/*     */ {
/*  29 */   private int minReadBufferSize = 64;
/*     */   
/*  31 */   private int readBufferSize = 2048;
/*     */   
/*  33 */   private int maxReadBufferSize = 65536;
/*     */   
/*     */   private int idleTimeForRead;
/*     */   
/*     */   private int idleTimeForWrite;
/*     */   
/*     */   private int idleTimeForBoth;
/*     */   
/*  41 */   private int writeTimeout = 60;
/*     */   
/*     */   private boolean useReadOperation;
/*     */   
/*  45 */   private int throughputCalculationInterval = 3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setAll(IoSessionConfig config) {
/*  55 */     if (config == null) {
/*  56 */       throw new IllegalArgumentException("config");
/*     */     }
/*     */     
/*  59 */     setReadBufferSize(config.getReadBufferSize());
/*  60 */     setMinReadBufferSize(config.getMinReadBufferSize());
/*  61 */     setMaxReadBufferSize(config.getMaxReadBufferSize());
/*  62 */     setIdleTime(IdleStatus.BOTH_IDLE, config.getIdleTime(IdleStatus.BOTH_IDLE));
/*  63 */     setIdleTime(IdleStatus.READER_IDLE, config.getIdleTime(IdleStatus.READER_IDLE));
/*  64 */     setIdleTime(IdleStatus.WRITER_IDLE, config.getIdleTime(IdleStatus.WRITER_IDLE));
/*  65 */     setWriteTimeout(config.getWriteTimeout());
/*  66 */     setUseReadOperation(config.isUseReadOperation());
/*  67 */     setThroughputCalculationInterval(config.getThroughputCalculationInterval());
/*     */     
/*  69 */     doSetAll(config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void doSetAll(IoSessionConfig paramIoSessionConfig);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getReadBufferSize() {
/*  82 */     return this.readBufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadBufferSize(int readBufferSize) {
/*  89 */     if (readBufferSize <= 0) {
/*  90 */       throw new IllegalArgumentException("readBufferSize: " + readBufferSize + " (expected: 1+)");
/*     */     }
/*  92 */     this.readBufferSize = readBufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMinReadBufferSize() {
/*  99 */     return this.minReadBufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMinReadBufferSize(int minReadBufferSize) {
/* 106 */     if (minReadBufferSize <= 0) {
/* 107 */       throw new IllegalArgumentException("minReadBufferSize: " + minReadBufferSize + " (expected: 1+)");
/*     */     }
/* 109 */     if (minReadBufferSize > this.maxReadBufferSize) {
/* 110 */       throw new IllegalArgumentException("minReadBufferSize: " + minReadBufferSize + " (expected: smaller than " + this.maxReadBufferSize + ')');
/*     */     }
/*     */ 
/*     */     
/* 114 */     this.minReadBufferSize = minReadBufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxReadBufferSize() {
/* 121 */     return this.maxReadBufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxReadBufferSize(int maxReadBufferSize) {
/* 128 */     if (maxReadBufferSize <= 0) {
/* 129 */       throw new IllegalArgumentException("maxReadBufferSize: " + maxReadBufferSize + " (expected: 1+)");
/*     */     }
/*     */     
/* 132 */     if (maxReadBufferSize < this.minReadBufferSize) {
/* 133 */       throw new IllegalArgumentException("maxReadBufferSize: " + maxReadBufferSize + " (expected: greater than " + this.minReadBufferSize + ')');
/*     */     }
/*     */ 
/*     */     
/* 137 */     this.maxReadBufferSize = maxReadBufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIdleTime(IdleStatus status) {
/* 144 */     if (status == IdleStatus.BOTH_IDLE) {
/* 145 */       return this.idleTimeForBoth;
/*     */     }
/*     */     
/* 148 */     if (status == IdleStatus.READER_IDLE) {
/* 149 */       return this.idleTimeForRead;
/*     */     }
/*     */     
/* 152 */     if (status == IdleStatus.WRITER_IDLE) {
/* 153 */       return this.idleTimeForWrite;
/*     */     }
/*     */     
/* 156 */     throw new IllegalArgumentException("Unknown idle status: " + status);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIdleTimeInMillis(IdleStatus status) {
/* 163 */     return getIdleTime(status) * 1000L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIdleTime(IdleStatus status, int idleTime) {
/* 170 */     if (idleTime < 0) {
/* 171 */       throw new IllegalArgumentException("Illegal idle time: " + idleTime);
/*     */     }
/*     */     
/* 174 */     if (status == IdleStatus.BOTH_IDLE) {
/* 175 */       this.idleTimeForBoth = idleTime;
/* 176 */     } else if (status == IdleStatus.READER_IDLE) {
/* 177 */       this.idleTimeForRead = idleTime;
/* 178 */     } else if (status == IdleStatus.WRITER_IDLE) {
/* 179 */       this.idleTimeForWrite = idleTime;
/*     */     } else {
/* 181 */       throw new IllegalArgumentException("Unknown idle status: " + status);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getBothIdleTime() {
/* 189 */     return getIdleTime(IdleStatus.BOTH_IDLE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getBothIdleTimeInMillis() {
/* 196 */     return getIdleTimeInMillis(IdleStatus.BOTH_IDLE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getReaderIdleTime() {
/* 203 */     return getIdleTime(IdleStatus.READER_IDLE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getReaderIdleTimeInMillis() {
/* 210 */     return getIdleTimeInMillis(IdleStatus.READER_IDLE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getWriterIdleTime() {
/* 217 */     return getIdleTime(IdleStatus.WRITER_IDLE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getWriterIdleTimeInMillis() {
/* 224 */     return getIdleTimeInMillis(IdleStatus.WRITER_IDLE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBothIdleTime(int idleTime) {
/* 231 */     setIdleTime(IdleStatus.BOTH_IDLE, idleTime);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReaderIdleTime(int idleTime) {
/* 238 */     setIdleTime(IdleStatus.READER_IDLE, idleTime);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWriterIdleTime(int idleTime) {
/* 245 */     setIdleTime(IdleStatus.WRITER_IDLE, idleTime);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWriteTimeout() {
/* 252 */     return this.writeTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getWriteTimeoutInMillis() {
/* 259 */     return this.writeTimeout * 1000L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWriteTimeout(int writeTimeout) {
/* 266 */     if (writeTimeout < 0) {
/* 267 */       throw new IllegalArgumentException("Illegal write timeout: " + writeTimeout);
/*     */     }
/* 269 */     this.writeTimeout = writeTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUseReadOperation() {
/* 276 */     return this.useReadOperation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseReadOperation(boolean useReadOperation) {
/* 283 */     this.useReadOperation = useReadOperation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getThroughputCalculationInterval() {
/* 290 */     return this.throughputCalculationInterval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThroughputCalculationInterval(int throughputCalculationInterval) {
/* 297 */     if (throughputCalculationInterval < 0) {
/* 298 */       throw new IllegalArgumentException("throughputCalculationInterval: " + throughputCalculationInterval);
/*     */     }
/*     */     
/* 301 */     this.throughputCalculationInterval = throughputCalculationInterval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getThroughputCalculationIntervalInMillis() {
/* 308 */     return this.throughputCalculationInterval * 1000L;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/session/AbstractIoSessionConfig.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */