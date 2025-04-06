/*     */ package org.apache.mina.core.service;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.nio.channels.spi.SelectorProvider;
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import org.apache.mina.core.RuntimeIoException;
/*     */ import org.apache.mina.core.session.AbstractIoSession;
/*     */ import org.apache.mina.core.session.AttributeKey;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.write.WriteRequest;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleIoProcessorPool<S extends AbstractIoSession>
/*     */   implements IoProcessor<S>
/*     */ {
/*  82 */   private static final Logger LOGGER = LoggerFactory.getLogger(SimpleIoProcessorPool.class);
/*     */ 
/*     */   
/*  85 */   private static final int DEFAULT_SIZE = Runtime.getRuntime().availableProcessors() + 1;
/*     */ 
/*     */   
/*  88 */   private static final AttributeKey PROCESSOR = new AttributeKey(SimpleIoProcessorPool.class, "processor");
/*     */ 
/*     */   
/*     */   private final IoProcessor<S>[] pool;
/*     */ 
/*     */   
/*     */   private final Executor executor;
/*     */ 
/*     */   
/*     */   private final boolean createdExecutor;
/*     */ 
/*     */   
/* 100 */   private final Object disposalLock = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean disposing;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean disposed;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleIoProcessorPool(Class<? extends IoProcessor<S>> processorType) {
/* 115 */     this(processorType, null, DEFAULT_SIZE, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleIoProcessorPool(Class<? extends IoProcessor<S>> processorType, int size) {
/* 126 */     this(processorType, null, size, null);
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
/*     */   public SimpleIoProcessorPool(Class<? extends IoProcessor<S>> processorType, int size, SelectorProvider selectorProvider) {
/* 138 */     this(processorType, null, size, selectorProvider);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleIoProcessorPool(Class<? extends IoProcessor<S>> processorType, Executor executor) {
/* 148 */     this(processorType, executor, DEFAULT_SIZE, null);
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
/*     */   public SimpleIoProcessorPool(Class<? extends IoProcessor<S>> processorType, Executor executor, int size, SelectorProvider selectorProvider) {
/* 160 */     if (processorType == null) {
/* 161 */       throw new IllegalArgumentException("processorType");
/*     */     }
/*     */     
/* 164 */     if (size <= 0) {
/* 165 */       throw new IllegalArgumentException("size: " + size + " (expected: positive integer)");
/*     */     }
/*     */ 
/*     */     
/* 169 */     this.createdExecutor = (executor == null);
/*     */     
/* 171 */     if (this.createdExecutor) {
/* 172 */       this.executor = Executors.newCachedThreadPool();
/*     */       
/* 174 */       ((ThreadPoolExecutor)this.executor).setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
/*     */     } else {
/* 176 */       this.executor = executor;
/*     */     } 
/*     */     
/* 179 */     this.pool = (IoProcessor<S>[])new IoProcessor[size];
/*     */     
/* 181 */     boolean success = false;
/* 182 */     Constructor<? extends IoProcessor<S>> processorConstructor = null;
/* 183 */     boolean usesExecutorArg = true;
/*     */ 
/*     */     
/*     */     try {
/*     */       try {
/*     */         try {
/* 189 */           processorConstructor = processorType.getConstructor(new Class[] { ExecutorService.class });
/* 190 */           this.pool[0] = processorConstructor.newInstance(new Object[] { this.executor });
/* 191 */         } catch (NoSuchMethodException e1) {
/*     */           
/*     */           try {
/* 194 */             if (selectorProvider == null) {
/* 195 */               processorConstructor = processorType.getConstructor(new Class[] { Executor.class });
/* 196 */               this.pool[0] = processorConstructor.newInstance(new Object[] { this.executor });
/*     */             } else {
/* 198 */               processorConstructor = processorType.getConstructor(new Class[] { Executor.class, SelectorProvider.class });
/* 199 */               this.pool[0] = processorConstructor.newInstance(new Object[] { this.executor, selectorProvider });
/*     */             } 
/* 201 */           } catch (NoSuchMethodException e2) {
/*     */             
/*     */             try {
/* 204 */               processorConstructor = processorType.getConstructor(new Class[0]);
/* 205 */               usesExecutorArg = false;
/* 206 */               this.pool[0] = processorConstructor.newInstance(new Object[0]);
/* 207 */             } catch (NoSuchMethodException e3) {}
/*     */           }
/*     */         
/*     */         }
/*     */       
/* 212 */       } catch (RuntimeException re) {
/* 213 */         LOGGER.error("Cannot create an IoProcessor :{}", re.getMessage());
/* 214 */         throw re;
/* 215 */       } catch (Exception e) {
/* 216 */         String msg = "Failed to create a new instance of " + processorType.getName() + ":" + e.getMessage();
/* 217 */         LOGGER.error(msg, e);
/* 218 */         throw new RuntimeIoException(msg, e);
/*     */       } 
/*     */       
/* 221 */       if (processorConstructor == null) {
/*     */         
/* 223 */         String msg = String.valueOf(processorType) + " must have a public constructor with one " + ExecutorService.class.getSimpleName() + " parameter, a public constructor with one " + Executor.class.getSimpleName() + " parameter or a public default constructor.";
/*     */ 
/*     */         
/* 226 */         LOGGER.error(msg);
/* 227 */         throw new IllegalArgumentException(msg);
/*     */       } 
/*     */ 
/*     */       
/* 231 */       for (int i = 1; i < this.pool.length; i++) {
/*     */         try {
/* 233 */           if (usesExecutorArg) {
/* 234 */             if (selectorProvider == null) {
/* 235 */               this.pool[i] = processorConstructor.newInstance(new Object[] { this.executor });
/*     */             } else {
/* 237 */               this.pool[i] = processorConstructor.newInstance(new Object[] { this.executor, selectorProvider });
/*     */             } 
/*     */           } else {
/* 240 */             this.pool[i] = processorConstructor.newInstance(new Object[0]);
/*     */           } 
/* 242 */         } catch (Exception e) {}
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 247 */       success = true;
/*     */     } finally {
/* 249 */       if (!success) {
/* 250 */         dispose();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void add(S session) {
/* 259 */     getProcessor(session).add(session);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void flush(S session) {
/* 266 */     getProcessor(session).flush(session);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void write(S session, WriteRequest writeRequest) {
/* 273 */     getProcessor(session).write(session, writeRequest);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void remove(S session) {
/* 280 */     getProcessor(session).remove(session);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void updateTrafficControl(S session) {
/* 287 */     getProcessor(session).updateTrafficControl(session);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDisposed() {
/* 294 */     return this.disposed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDisposing() {
/* 301 */     return this.disposing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void dispose() {
/* 308 */     if (this.disposed) {
/*     */       return;
/*     */     }
/*     */     
/* 312 */     synchronized (this.disposalLock) {
/* 313 */       if (!this.disposing) {
/* 314 */         this.disposing = true;
/*     */         
/* 316 */         for (IoProcessor<S> ioProcessor : this.pool) {
/* 317 */           if (ioProcessor != null)
/*     */           {
/*     */ 
/*     */ 
/*     */             
/* 322 */             if (!ioProcessor.isDisposing())
/*     */               
/*     */               try {
/*     */ 
/*     */                 
/* 327 */                 ioProcessor.dispose();
/* 328 */               } catch (Exception e) {
/* 329 */                 LOGGER.warn("Failed to dispose the {} IoProcessor.", ioProcessor.getClass().getSimpleName(), e);
/*     */               }  
/*     */           }
/*     */         } 
/* 333 */         if (this.createdExecutor) {
/* 334 */           ((ExecutorService)this.executor).shutdown();
/*     */         }
/*     */       } 
/*     */       
/* 338 */       Arrays.fill((Object[])this.pool, (Object)null);
/* 339 */       this.disposed = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private IoProcessor<S> getProcessor(S session) {
/* 349 */     IoProcessor<S> processor = (IoProcessor<S>)session.getAttribute(PROCESSOR);
/*     */     
/* 351 */     if (processor == null) {
/* 352 */       if (this.disposed || this.disposing) {
/* 353 */         throw new IllegalStateException("A disposed processor cannot be accessed.");
/*     */       }
/*     */       
/* 356 */       processor = this.pool[Math.abs((int)session.getId()) % this.pool.length];
/*     */       
/* 358 */       if (processor == null) {
/* 359 */         throw new IllegalStateException("A disposed processor cannot be accessed.");
/*     */       }
/*     */       
/* 362 */       session.setAttributeIfAbsent(PROCESSOR, processor);
/*     */     } 
/*     */     
/* 365 */     return processor;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/service/SimpleIoProcessorPool.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */