/*     */ package com.jolbox.bonecp;
/*     */ 
/*     */ import com.google.common.base.FinalizableReferenceQueue;
/*     */ import com.google.common.base.FinalizableWeakReference;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.concurrent.ArrayBlockingQueue;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import jsr166y.LinkedTransferQueue;
/*     */ import jsr166y.TransferQueue;
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
/*     */ public class ConnectionPartition
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7864443421028454573L;
/*  65 */   static Logger logger = LoggerFactory.getLogger(ConnectionPartition.class);
/*     */   
/*     */   private TransferQueue<ConnectionHandle> freeConnections;
/*     */   
/*     */   private final int acquireIncrement;
/*     */   
/*     */   private final int minConnections;
/*     */   
/*     */   private final int maxConnections;
/*     */   
/*  75 */   protected ReentrantReadWriteLock statsLock = new ReentrantReadWriteLock();
/*     */   
/*  77 */   private int createdConnections = 0;
/*     */ 
/*     */   
/*     */   private final String url;
/*     */ 
/*     */   
/*     */   private final String username;
/*     */   
/*     */   private final String password;
/*     */   
/*     */   private volatile boolean unableToCreateMoreTransactions = false;
/*     */   
/*     */   private LinkedTransferQueue<ConnectionHandle> connectionsPendingRelease;
/*     */   
/*     */   private boolean disableTracking;
/*     */   
/*  93 */   private BlockingQueue<Object> poolWatchThreadSignalQueue = new ArrayBlockingQueue(1);
/*     */ 
/*     */   
/*     */   private long queryExecuteTimeLimitInNanoSeconds;
/*     */ 
/*     */   
/*     */   private String poolName;
/*     */ 
/*     */   
/*     */   protected BoneCP pool;
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockingQueue<Object> getPoolWatchThreadSignalQueue() {
/* 107 */     return this.poolWatchThreadSignalQueue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateCreatedConnections(int increment) {
/*     */     try {
/* 116 */       this.statsLock.writeLock().lock();
/* 117 */       this.createdConnections += increment;
/*     */     }
/*     */     finally {
/*     */       
/* 121 */       this.statsLock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addFreeConnection(ConnectionHandle connectionHandle) throws SQLException {
/* 132 */     connectionHandle.setOriginatingPartition(this);
/*     */ 
/*     */     
/* 135 */     updateCreatedConnections(1);
/* 136 */     if (!this.disableTracking) {
/* 137 */       trackConnectionFinalizer(connectionHandle);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 142 */     if (!this.freeConnections.offer(connectionHandle)) {
/*     */       
/* 144 */       updateCreatedConnections(-1);
/*     */       
/* 146 */       if (!this.disableTracking) {
/* 147 */         this.pool.getFinalizableRefs().remove(connectionHandle.getInternalConnection());
/*     */       }
/*     */       
/* 150 */       connectionHandle.internalClose();
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
/*     */   protected void trackConnectionFinalizer(ConnectionHandle connectionHandle) {
/* 164 */     if (!this.disableTracking) {
/*     */       
/* 166 */       Connection con = connectionHandle.getInternalConnection();
/* 167 */       if (con != null && con instanceof Proxy && Proxy.getInvocationHandler(con) instanceof MemorizeTransactionProxy) {
/*     */         
/*     */         try {
/*     */           
/* 171 */           con = (Connection)Proxy.getInvocationHandler(con).invoke(con, ConnectionHandle.class.getMethod("getProxyTarget", new Class[0]), null);
/* 172 */         } catch (Throwable t) {
/* 173 */           logger.error("Error while attempting to track internal db connection", t);
/*     */         } 
/*     */       }
/* 176 */       final Connection internalDBConnection = con;
/* 177 */       final BoneCP pool = connectionHandle.getPool();
/* 178 */       connectionHandle.getPool().getFinalizableRefs().put(internalDBConnection, new FinalizableWeakReference<ConnectionHandle>(connectionHandle, connectionHandle.getPool().getFinalizableRefQueue())
/*     */           {
/*     */             public void finalizeReferent() {
/*     */               try {
/* 182 */                 pool.getFinalizableRefs().remove(internalDBConnection);
/* 183 */                 if (internalDBConnection != null && !internalDBConnection.isClosed()) {
/*     */                   
/* 185 */                   ConnectionPartition.logger.warn("BoneCP detected an unclosed connection " + ConnectionPartition.this.poolName + "and will now attempt to close it for you. " + "You should be closing this connection in your application - enable connectionWatch for additional debugging assistance.");
/*     */                   
/* 187 */                   internalDBConnection.close();
/* 188 */                   ConnectionPartition.this.updateCreatedConnections(-1);
/*     */                 } 
/* 190 */               } catch (Throwable t) {
/* 191 */                 ConnectionPartition.logger.error("Error while closing off internal db connection", t);
/*     */               } 
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TransferQueue<ConnectionHandle> getFreeConnections() {
/* 202 */     return this.freeConnections;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setFreeConnections(TransferQueue<ConnectionHandle> freeConnections) {
/* 209 */     this.freeConnections = freeConnections;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConnectionPartition(BoneCP pool) {
/* 219 */     BoneCPConfig config = pool.getConfig();
/* 220 */     this.minConnections = config.getMinConnectionsPerPartition();
/* 221 */     this.maxConnections = config.getMaxConnectionsPerPartition();
/* 222 */     this.acquireIncrement = config.getAcquireIncrement();
/* 223 */     this.url = config.getJdbcUrl();
/* 224 */     this.username = config.getUsername();
/* 225 */     this.password = config.getPassword();
/* 226 */     this.poolName = (config.getPoolName() != null) ? ("(in pool '" + config.getPoolName() + "') ") : "";
/* 227 */     this.pool = pool;
/*     */     
/* 229 */     this.connectionsPendingRelease = new LinkedTransferQueue();
/* 230 */     this.disableTracking = config.isDisableConnectionTracking();
/* 231 */     this.queryExecuteTimeLimitInNanoSeconds = TimeUnit.NANOSECONDS.convert(config.getQueryExecuteTimeLimitInMs(), TimeUnit.MILLISECONDS);
/*     */     
/* 233 */     int helperThreads = config.getReleaseHelperThreads();
/* 234 */     for (int i = 0; i < helperThreads; i++)
/*     */     {
/* 236 */       pool.getReleaseHelper().execute(new ConnectionReleaseHelperThread((BlockingQueue<ConnectionHandle>)this.connectionsPendingRelease, pool));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getAcquireIncrement() {
/* 244 */     return this.acquireIncrement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getMinConnections() {
/* 251 */     return this.minConnections;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getMaxConnections() {
/* 259 */     return this.maxConnections;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getCreatedConnections() {
/*     */     try {
/* 267 */       this.statsLock.readLock().lock();
/* 268 */       return this.createdConnections;
/*     */     } finally {
/* 270 */       this.statsLock.readLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUrl() {
/* 278 */     return this.url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUsername() {
/* 286 */     return this.username;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getPassword() {
/* 294 */     return this.password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isUnableToCreateMoreTransactions() {
/* 304 */     return this.unableToCreateMoreTransactions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setUnableToCreateMoreTransactions(boolean unableToCreateMoreTransactions) {
/* 314 */     this.unableToCreateMoreTransactions = unableToCreateMoreTransactions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected LinkedTransferQueue<ConnectionHandle> getConnectionsPendingRelease() {
/* 324 */     return this.connectionsPendingRelease;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getAvailableConnections() {
/* 332 */     return this.freeConnections.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRemainingCapacity() {
/* 339 */     return this.freeConnections.remainingCapacity();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long getQueryExecuteTimeLimitinNanoSeconds() {
/* 346 */     return this.queryExecuteTimeLimitInNanoSeconds;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/ConnectionPartition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */