/*     */ package org.apache.mina.core.service;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
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
/*     */ public class IoServiceStatistics
/*     */ {
/*     */   private AbstractIoService service;
/*     */   private double readBytesThroughput;
/*     */   private double writtenBytesThroughput;
/*     */   private double readMessagesThroughput;
/*     */   private double writtenMessagesThroughput;
/*     */   private double largestReadBytesThroughput;
/*     */   private double largestWrittenBytesThroughput;
/*     */   private double largestReadMessagesThroughput;
/*     */   private double largestWrittenMessagesThroughput;
/*     */   private long readBytes;
/*     */   private long writtenBytes;
/*     */   private long readMessages;
/*     */   private long writtenMessages;
/*     */   private long lastReadTime;
/*     */   private long lastWriteTime;
/*     */   private long lastReadBytes;
/*     */   private long lastWrittenBytes;
/*     */   private long lastReadMessages;
/*     */   private long lastWrittenMessages;
/*     */   private long lastThroughputCalculationTime;
/*     */   private int scheduledWriteBytes;
/*     */   private int scheduledWriteMessages;
/*  93 */   private final AtomicInteger throughputCalculationInterval = new AtomicInteger(3);
/*     */   
/*  95 */   private final Lock throughputCalculationLock = new ReentrantLock();
/*     */   
/*     */   public IoServiceStatistics(AbstractIoService service) {
/*  98 */     this.service = service;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getLargestManagedSessionCount() {
/* 106 */     return this.service.getListeners().getLargestManagedSessionCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getCumulativeManagedSessionCount() {
/* 115 */     return this.service.getListeners().getCumulativeManagedSessionCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getLastIoTime() {
/* 123 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 126 */       return Math.max(this.lastReadTime, this.lastWriteTime);
/*     */     } finally {
/* 128 */       this.throughputCalculationLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getLastReadTime() {
/* 136 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 139 */       return this.lastReadTime;
/*     */     } finally {
/* 141 */       this.throughputCalculationLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getLastWriteTime() {
/* 149 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 152 */       return this.lastWriteTime;
/*     */     } finally {
/* 154 */       this.throughputCalculationLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getReadBytes() {
/* 162 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 165 */       return this.readBytes;
/*     */     } finally {
/* 167 */       this.throughputCalculationLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getWrittenBytes() {
/* 175 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 178 */       return this.writtenBytes;
/*     */     } finally {
/* 180 */       this.throughputCalculationLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getReadMessages() {
/* 188 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 191 */       return this.readMessages;
/*     */     } finally {
/* 193 */       this.throughputCalculationLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getWrittenMessages() {
/* 201 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 204 */       return this.writtenMessages;
/*     */     } finally {
/* 206 */       this.throughputCalculationLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getReadBytesThroughput() {
/* 214 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 217 */       resetThroughput();
/* 218 */       return this.readBytesThroughput;
/*     */     } finally {
/* 220 */       this.throughputCalculationLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getWrittenBytesThroughput() {
/* 228 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 231 */       resetThroughput();
/* 232 */       return this.writtenBytesThroughput;
/*     */     } finally {
/* 234 */       this.throughputCalculationLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getReadMessagesThroughput() {
/* 242 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 245 */       resetThroughput();
/* 246 */       return this.readMessagesThroughput;
/*     */     } finally {
/* 248 */       this.throughputCalculationLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getWrittenMessagesThroughput() {
/* 256 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 259 */       resetThroughput();
/* 260 */       return this.writtenMessagesThroughput;
/*     */     } finally {
/* 262 */       this.throughputCalculationLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getLargestReadBytesThroughput() {
/* 271 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 274 */       return this.largestReadBytesThroughput;
/*     */     } finally {
/* 276 */       this.throughputCalculationLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getLargestWrittenBytesThroughput() {
/* 285 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 288 */       return this.largestWrittenBytesThroughput;
/*     */     } finally {
/* 290 */       this.throughputCalculationLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getLargestReadMessagesThroughput() {
/* 299 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 302 */       return this.largestReadMessagesThroughput;
/*     */     } finally {
/* 304 */       this.throughputCalculationLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getLargestWrittenMessagesThroughput() {
/* 313 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 316 */       return this.largestWrittenMessagesThroughput;
/*     */     } finally {
/* 318 */       this.throughputCalculationLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getThroughputCalculationInterval() {
/* 327 */     return this.throughputCalculationInterval.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getThroughputCalculationIntervalInMillis() {
/* 335 */     return this.throughputCalculationInterval.get() * 1000L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setThroughputCalculationInterval(int throughputCalculationInterval) {
/* 343 */     if (throughputCalculationInterval < 0) {
/* 344 */       throw new IllegalArgumentException("throughputCalculationInterval: " + throughputCalculationInterval);
/*     */     }
/*     */     
/* 347 */     this.throughputCalculationInterval.set(throughputCalculationInterval);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void setLastReadTime(long lastReadTime) {
/* 357 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 360 */       this.lastReadTime = lastReadTime;
/*     */     } finally {
/* 362 */       this.throughputCalculationLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void setLastWriteTime(long lastWriteTime) {
/* 373 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 376 */       this.lastWriteTime = lastWriteTime;
/*     */     } finally {
/* 378 */       this.throughputCalculationLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void resetThroughput() {
/* 387 */     if (this.service.getManagedSessionCount() == 0) {
/* 388 */       this.readBytesThroughput = 0.0D;
/* 389 */       this.writtenBytesThroughput = 0.0D;
/* 390 */       this.readMessagesThroughput = 0.0D;
/* 391 */       this.writtenMessagesThroughput = 0.0D;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateThroughput(long currentTime) {
/* 399 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 402 */       int interval = (int)(currentTime - this.lastThroughputCalculationTime);
/* 403 */       long minInterval = getThroughputCalculationIntervalInMillis();
/*     */       
/* 405 */       if (minInterval == 0L || interval < minInterval) {
/*     */         return;
/*     */       }
/*     */       
/* 409 */       long readBytes = this.readBytes;
/* 410 */       long writtenBytes = this.writtenBytes;
/* 411 */       long readMessages = this.readMessages;
/* 412 */       long writtenMessages = this.writtenMessages;
/*     */       
/* 414 */       this.readBytesThroughput = (readBytes - this.lastReadBytes) * 1000.0D / interval;
/* 415 */       this.writtenBytesThroughput = (writtenBytes - this.lastWrittenBytes) * 1000.0D / interval;
/* 416 */       this.readMessagesThroughput = (readMessages - this.lastReadMessages) * 1000.0D / interval;
/* 417 */       this.writtenMessagesThroughput = (writtenMessages - this.lastWrittenMessages) * 1000.0D / interval;
/*     */       
/* 419 */       if (this.readBytesThroughput > this.largestReadBytesThroughput) {
/* 420 */         this.largestReadBytesThroughput = this.readBytesThroughput;
/*     */       }
/*     */       
/* 423 */       if (this.writtenBytesThroughput > this.largestWrittenBytesThroughput) {
/* 424 */         this.largestWrittenBytesThroughput = this.writtenBytesThroughput;
/*     */       }
/*     */       
/* 427 */       if (this.readMessagesThroughput > this.largestReadMessagesThroughput) {
/* 428 */         this.largestReadMessagesThroughput = this.readMessagesThroughput;
/*     */       }
/*     */       
/* 431 */       if (this.writtenMessagesThroughput > this.largestWrittenMessagesThroughput) {
/* 432 */         this.largestWrittenMessagesThroughput = this.writtenMessagesThroughput;
/*     */       }
/*     */       
/* 435 */       this.lastReadBytes = readBytes;
/* 436 */       this.lastWrittenBytes = writtenBytes;
/* 437 */       this.lastReadMessages = readMessages;
/* 438 */       this.lastWrittenMessages = writtenMessages;
/*     */       
/* 440 */       this.lastThroughputCalculationTime = currentTime;
/*     */     } finally {
/* 442 */       this.throughputCalculationLock.unlock();
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
/*     */ 
/*     */   
/*     */   public final void increaseReadBytes(long nbBytesRead, long currentTime) {
/* 456 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 459 */       this.readBytes += nbBytesRead;
/* 460 */       this.lastReadTime = currentTime;
/*     */     } finally {
/* 462 */       this.throughputCalculationLock.unlock();
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
/*     */   public final void increaseReadMessages(long currentTime) {
/* 474 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 477 */       this.readMessages++;
/* 478 */       this.lastReadTime = currentTime;
/*     */     } finally {
/* 480 */       this.throughputCalculationLock.unlock();
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
/*     */ 
/*     */   
/*     */   public final void increaseWrittenBytes(int nbBytesWritten, long currentTime) {
/* 494 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 497 */       this.writtenBytes += nbBytesWritten;
/* 498 */       this.lastWriteTime = currentTime;
/*     */     } finally {
/* 500 */       this.throughputCalculationLock.unlock();
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
/*     */   public final void increaseWrittenMessages(long currentTime) {
/* 512 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 515 */       this.writtenMessages++;
/* 516 */       this.lastWriteTime = currentTime;
/*     */     } finally {
/* 518 */       this.throughputCalculationLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getScheduledWriteBytes() {
/* 526 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 529 */       return this.scheduledWriteBytes;
/*     */     } finally {
/* 531 */       this.throughputCalculationLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void increaseScheduledWriteBytes(int increment) {
/* 539 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 542 */       this.scheduledWriteBytes += increment;
/*     */     } finally {
/* 544 */       this.throughputCalculationLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getScheduledWriteMessages() {
/* 552 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 555 */       return this.scheduledWriteMessages;
/*     */     } finally {
/* 557 */       this.throughputCalculationLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void increaseScheduledWriteMessages() {
/* 565 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 568 */       this.scheduledWriteMessages++;
/*     */     } finally {
/* 570 */       this.throughputCalculationLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void decreaseScheduledWriteMessages() {
/* 578 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 581 */       this.scheduledWriteMessages--;
/*     */     } finally {
/* 583 */       this.throughputCalculationLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setLastThroughputCalculationTime(long lastThroughputCalculationTime) {
/* 591 */     this.throughputCalculationLock.lock();
/*     */     
/*     */     try {
/* 594 */       this.lastThroughputCalculationTime = lastThroughputCalculationTime;
/*     */     } finally {
/* 596 */       this.throughputCalculationLock.unlock();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/service/IoServiceStatistics.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */