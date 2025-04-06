/*     */ package com.jolbox.bonecp;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.FinalizableReferenceQueue;
/*     */ import com.jolbox.bonecp.hooks.AcquireFailConfig;
/*     */ import java.io.Serializable;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.ref.Reference;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
/*     */ import javax.sql.DataSource;
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
/*     */ public class BoneCP
/*     */   implements Serializable
/*     */ {
/*     */   private static final String THREAD_CLOSE_CONNECTION_WARNING = "Thread close connection monitoring has been enabled. This will negatively impact on your performance. Only enable this option for debugging purposes!";
/*     */   private static final long serialVersionUID = -8386816681977604817L;
/*     */   private static final String ERROR_TEST_CONNECTION = "Unable to open a test connection to the given database. JDBC url = %s, username = %s. Terminating connection pool. Original Exception: %s";
/*     */   private static final String SHUTDOWN_LOCATION_TRACE = "Attempting to obtain a connection from a pool that has already been shutdown. \nStack trace of location where pool was shutdown follows:\n";
/*     */   private static final String UNCLOSED_EXCEPTION_MESSAGE = "Connection obtained from thread [%s] was never closed. \nStack trace of location where connection was obtained follows:\n";
/*     */   public static final String MBEAN_CONFIG = "com.jolbox.bonecp:type=BoneCPConfig";
/*     */   public static final String MBEAN_BONECP = "com.jolbox.bonecp:type=BoneCP";
/*  77 */   private static final String[] METADATATABLE = new String[] { "TABLE" };
/*     */ 
/*     */   
/*     */   private static final String KEEPALIVEMETADATA = "BONECPKEEPALIVE";
/*     */ 
/*     */   
/*     */   protected final int poolAvailabilityThreshold;
/*     */ 
/*     */   
/*     */   private int partitionCount;
/*     */ 
/*     */   
/*     */   private ConnectionPartition[] partitions;
/*     */ 
/*     */   
/*     */   private ScheduledExecutorService keepAliveScheduler;
/*     */   
/*     */   private ScheduledExecutorService maxAliveScheduler;
/*     */   
/*     */   private ExecutorService connectionsScheduler;
/*     */   
/*     */   private BoneCPConfig config;
/*     */   
/*     */   private boolean releaseHelperThreadsConfigured;
/*     */   
/*     */   private ExecutorService releaseHelper;
/*     */   
/*     */   private ExecutorService statementCloseHelperExecutor;
/*     */   
/*     */   private ExecutorService asyncExecutor;
/*     */   
/* 108 */   private static Logger logger = LoggerFactory.getLogger(BoneCP.class);
/*     */   
/*     */   private MBeanServer mbs;
/*     */   
/* 112 */   protected Lock terminationLock = new ReentrantLock();
/*     */ 
/*     */   
/*     */   protected boolean closeConnectionWatch = false;
/*     */ 
/*     */   
/*     */   private ExecutorService closeConnectionExecutor;
/*     */   
/*     */   protected volatile boolean poolShuttingDown;
/*     */   
/*     */   private String shutdownStackTrace;
/*     */   
/* 124 */   private final Map<Connection, Reference<ConnectionHandle>> finalizableRefs = new ConcurrentHashMap<Connection, Reference<ConnectionHandle>>();
/*     */   
/*     */   private FinalizableReferenceQueue finalizableRefQueue;
/*     */   
/*     */   private long connectionTimeoutInMs;
/*     */   
/*     */   private long closeConnectionWatchTimeoutInMs;
/*     */   
/*     */   private boolean statementReleaseHelperThreadsConfigured;
/*     */   
/*     */   private LinkedTransferQueue<StatementHandle> statementsPendingRelease;
/*     */   
/*     */   private boolean statisticsEnabled;
/*     */   
/* 138 */   private Statistics statistics = new Statistics(this);
/*     */ 
/*     */   
/*     */   private Boolean defaultReadOnly;
/*     */ 
/*     */   
/*     */   private String defaultCatalog;
/*     */   
/*     */   private int defaultTransactionIsolationValue;
/*     */   
/*     */   private Boolean defaultAutoCommit;
/*     */   
/*     */   @VisibleForTesting
/*     */   protected boolean externalAuth;
/*     */ 
/*     */   
/*     */   public synchronized void shutdown() {
/* 155 */     if (!this.poolShuttingDown) {
/* 156 */       logger.info("Shutting down connection pool...");
/* 157 */       this.poolShuttingDown = true;
/*     */       
/* 159 */       this.shutdownStackTrace = captureStackTrace("Attempting to obtain a connection from a pool that has already been shutdown. \nStack trace of location where pool was shutdown follows:\n");
/* 160 */       this.keepAliveScheduler.shutdownNow();
/* 161 */       this.maxAliveScheduler.shutdownNow();
/* 162 */       this.connectionsScheduler.shutdownNow();
/*     */       
/* 164 */       if (this.releaseHelperThreadsConfigured) {
/* 165 */         this.releaseHelper.shutdownNow();
/*     */       }
/* 167 */       if (this.statementReleaseHelperThreadsConfigured) {
/* 168 */         this.statementCloseHelperExecutor.shutdownNow();
/*     */       }
/* 170 */       if (this.asyncExecutor != null) {
/* 171 */         this.asyncExecutor.shutdownNow();
/*     */       }
/* 173 */       if (this.closeConnectionExecutor != null) {
/* 174 */         this.closeConnectionExecutor.shutdownNow();
/*     */       }
/* 176 */       terminateAllConnections();
/* 177 */       logger.info("Connection pool has been shutdown.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 183 */     shutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void terminateAllConnections() {
/* 188 */     this.terminationLock.lock();
/*     */     
/*     */     try {
/* 191 */       for (int i = 0; i < this.partitionCount; i++) {
/*     */         ConnectionHandle conn;
/* 193 */         while ((conn = (ConnectionHandle)this.partitions[i].getFreeConnections().poll()) != null) {
/* 194 */           postDestroyConnection(conn);
/* 195 */           conn.setInReplayMode(true);
/*     */           try {
/* 197 */             conn.internalClose();
/* 198 */           } catch (SQLException e) {
/* 199 */             logger.error("Error in attempting to close connection", e);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } finally {
/*     */       
/* 205 */       this.terminationLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void postDestroyConnection(ConnectionHandle handle) {
/* 213 */     ConnectionPartition partition = handle.getOriginatingPartition();
/*     */     
/* 215 */     if (this.finalizableRefQueue != null) {
/* 216 */       this.finalizableRefs.remove(handle.getInternalConnection());
/*     */     }
/*     */ 
/*     */     
/* 220 */     partition.updateCreatedConnections(-1);
/* 221 */     partition.setUnableToCreateMoreTransactions(false);
/*     */ 
/*     */ 
/*     */     
/* 225 */     if (handle.getConnectionHook() != null) {
/* 226 */       handle.getConnectionHook().onDestroy(handle);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Connection obtainRawInternalConnection() throws SQLException {
/* 237 */     Connection result = null;
/*     */     
/* 239 */     DataSource datasourceBean = this.config.getDatasourceBean();
/* 240 */     String url = this.config.getJdbcUrl();
/* 241 */     String username = this.config.getUsername();
/* 242 */     String password = this.config.getPassword();
/* 243 */     Properties props = this.config.getDriverProperties();
/*     */     
/* 245 */     if (this.externalAuth && props == null) {
/* 246 */       props = new Properties();
/*     */     }
/*     */     
/* 249 */     if (datasourceBean != null) {
/* 250 */       return (username == null) ? datasourceBean.getConnection() : datasourceBean.getConnection(username, password);
/*     */     }
/*     */     
/* 253 */     if (props != null) {
/* 254 */       result = DriverManager.getConnection(url, props);
/*     */     } else {
/* 256 */       result = DriverManager.getConnection(url, username, password);
/*     */     } 
/*     */     
/* 259 */     if (this.defaultAutoCommit != null) {
/* 260 */       result.setAutoCommit(this.defaultAutoCommit.booleanValue());
/*     */     }
/* 262 */     if (this.defaultReadOnly != null) {
/* 263 */       result.setReadOnly(this.defaultReadOnly.booleanValue());
/*     */     }
/* 265 */     if (this.defaultCatalog != null) {
/* 266 */       result.setCatalog(this.defaultCatalog);
/*     */     }
/* 268 */     if (this.defaultTransactionIsolationValue != -1) {
/* 269 */       result.setTransactionIsolation(this.defaultTransactionIsolationValue);
/*     */     }
/*     */     
/* 272 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoneCP(BoneCPConfig config) throws SQLException {
/* 281 */     this.config = config;
/* 282 */     config.sanitize();
/*     */     
/* 284 */     this.statisticsEnabled = config.isStatisticsEnabled();
/* 285 */     this.closeConnectionWatchTimeoutInMs = config.getCloseConnectionWatchTimeoutInMs();
/* 286 */     this.poolAvailabilityThreshold = config.getPoolAvailabilityThreshold();
/* 287 */     this.connectionTimeoutInMs = config.getConnectionTimeoutInMs();
/* 288 */     this.externalAuth = config.isExternalAuth();
/*     */     
/* 290 */     if (this.connectionTimeoutInMs == 0L) {
/* 291 */       this.connectionTimeoutInMs = Long.MAX_VALUE;
/*     */     }
/* 293 */     this.defaultReadOnly = config.getDefaultReadOnly();
/* 294 */     this.defaultCatalog = config.getDefaultCatalog();
/* 295 */     this.defaultTransactionIsolationValue = config.getDefaultTransactionIsolationValue();
/* 296 */     this.defaultAutoCommit = config.getDefaultAutoCommit();
/*     */     
/* 298 */     AcquireFailConfig acquireConfig = new AcquireFailConfig();
/* 299 */     acquireConfig.setAcquireRetryAttempts(new AtomicInteger(0));
/* 300 */     acquireConfig.setAcquireRetryDelayInMs(0L);
/* 301 */     acquireConfig.setLogMessage("Failed to obtain initial connection");
/*     */     
/* 303 */     if (!config.isLazyInit()) {
/*     */       try {
/* 305 */         Connection sanityConnection = obtainRawInternalConnection();
/* 306 */         sanityConnection.close();
/* 307 */       } catch (Exception e) {
/* 308 */         if (config.getConnectionHook() != null) {
/* 309 */           config.getConnectionHook().onAcquireFail(e, acquireConfig);
/*     */         }
/*     */         
/* 312 */         throw new SQLException(String.format("Unable to open a test connection to the given database. JDBC url = %s, username = %s. Terminating connection pool. Original Exception: %s", new Object[] { config.getJdbcUrl(), config.getUsername(), PoolUtil.stringifyException(e) }), e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 320 */     if (!config.isDisableConnectionTracking()) {
/* 321 */       this.finalizableRefQueue = new FinalizableReferenceQueue();
/*     */     }
/*     */     
/* 324 */     this.asyncExecutor = Executors.newCachedThreadPool();
/* 325 */     int helperThreads = config.getReleaseHelperThreads();
/* 326 */     this.releaseHelperThreadsConfigured = (helperThreads > 0);
/*     */     
/* 328 */     this.statementReleaseHelperThreadsConfigured = (config.getStatementReleaseHelperThreads() > 0);
/* 329 */     this.config = config;
/* 330 */     this.partitions = new ConnectionPartition[config.getPartitionCount()];
/* 331 */     String suffix = "";
/*     */     
/* 333 */     if (config.getPoolName() != null) {
/* 334 */       suffix = "-" + config.getPoolName();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 339 */     if (this.releaseHelperThreadsConfigured) {
/* 340 */       this.releaseHelper = Executors.newFixedThreadPool(helperThreads * config.getPartitionCount(), new CustomThreadFactory("BoneCP-release-thread-helper-thread" + suffix, true));
/*     */     }
/* 342 */     this.keepAliveScheduler = Executors.newScheduledThreadPool(config.getPartitionCount(), new CustomThreadFactory("BoneCP-keep-alive-scheduler" + suffix, true));
/* 343 */     this.maxAliveScheduler = Executors.newScheduledThreadPool(config.getPartitionCount(), new CustomThreadFactory("BoneCP-max-alive-scheduler" + suffix, true));
/* 344 */     this.connectionsScheduler = Executors.newFixedThreadPool(config.getPartitionCount(), new CustomThreadFactory("BoneCP-pool-watch-thread" + suffix, true));
/*     */     
/* 346 */     this.partitionCount = config.getPartitionCount();
/* 347 */     this.closeConnectionWatch = config.isCloseConnectionWatch();
/* 348 */     boolean queueLIFO = (config.getServiceOrder() != null && config.getServiceOrder().equalsIgnoreCase("LIFO"));
/* 349 */     if (this.closeConnectionWatch) {
/* 350 */       logger.warn("Thread close connection monitoring has been enabled. This will negatively impact on your performance. Only enable this option for debugging purposes!");
/* 351 */       this.closeConnectionExecutor = Executors.newCachedThreadPool(new CustomThreadFactory("BoneCP-connection-watch-thread" + suffix, true));
/*     */     } 
/*     */     
/* 354 */     for (int p = 0; p < config.getPartitionCount(); p++) {
/*     */       TransferQueue<ConnectionHandle> connectionHandles;
/* 356 */       ConnectionPartition connectionPartition = new ConnectionPartition(this);
/* 357 */       this.partitions[p] = connectionPartition;
/*     */       
/* 359 */       if (config.getMaxConnectionsPerPartition() == config.getMinConnectionsPerPartition()) {
/*     */ 
/*     */         
/* 362 */         connectionHandles = queueLIFO ? new LIFOQueue<ConnectionHandle>() : (TransferQueue<ConnectionHandle>)new LinkedTransferQueue();
/*     */       } else {
/* 364 */         connectionHandles = queueLIFO ? new LIFOQueue<ConnectionHandle>(this.config.getMaxConnectionsPerPartition()) : (TransferQueue<ConnectionHandle>)new BoundedLinkedTransferQueue(this.config.getMaxConnectionsPerPartition());
/*     */       } 
/*     */       
/* 367 */       this.partitions[p].setFreeConnections(connectionHandles);
/*     */       
/* 369 */       if (!config.isLazyInit()) {
/* 370 */         for (int i = 0; i < config.getMinConnectionsPerPartition(); i++) {
/* 371 */           ConnectionHandle handle = new ConnectionHandle(config.getJdbcUrl(), config.getUsername(), config.getPassword(), this);
/* 372 */           this.partitions[p].addFreeConnection(handle);
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 378 */       if (config.getIdleConnectionTestPeriodInMinutes() > 0L || config.getIdleMaxAgeInMinutes() > 0L) {
/*     */         
/* 380 */         Runnable connectionTester = new ConnectionTesterThread(connectionPartition, this.keepAliveScheduler, this, config.getIdleMaxAge(TimeUnit.MILLISECONDS), config.getIdleConnectionTestPeriod(TimeUnit.MILLISECONDS), queueLIFO);
/* 381 */         long delayInMinutes = config.getIdleConnectionTestPeriodInMinutes();
/* 382 */         if (delayInMinutes == 0L) {
/* 383 */           delayInMinutes = config.getIdleMaxAgeInMinutes();
/*     */         }
/* 385 */         if (config.getIdleMaxAgeInMinutes() != 0L && config.getIdleConnectionTestPeriodInMinutes() != 0L && config.getIdleMaxAgeInMinutes() < delayInMinutes) {
/* 386 */           delayInMinutes = config.getIdleMaxAgeInMinutes();
/*     */         }
/* 388 */         this.keepAliveScheduler.schedule(connectionTester, delayInMinutes, TimeUnit.MINUTES);
/*     */       } 
/*     */       
/* 391 */       if (config.getMaxConnectionAgeInSeconds() > 0L) {
/* 392 */         Runnable connectionMaxAgeTester = new ConnectionMaxAgeThread(connectionPartition, this.maxAliveScheduler, this, config.getMaxConnectionAge(TimeUnit.MILLISECONDS), queueLIFO);
/* 393 */         this.maxAliveScheduler.schedule(connectionMaxAgeTester, config.getMaxConnectionAgeInSeconds(), TimeUnit.SECONDS);
/*     */       } 
/*     */       
/* 396 */       this.connectionsScheduler.execute(new PoolWatchThread(connectionPartition, this));
/*     */     } 
/*     */     
/* 399 */     initStmtReleaseHelper(suffix);
/*     */     
/* 401 */     if (!this.config.isDisableJMX()) {
/* 402 */       initJMX();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initStmtReleaseHelper(String suffix) {
/* 413 */     this.statementsPendingRelease = new BoundedLinkedTransferQueue<StatementHandle>(this.config.getMaxConnectionsPerPartition() * 3);
/* 414 */     int statementReleaseHelperThreads = this.config.getStatementReleaseHelperThreads();
/*     */     
/* 416 */     if (statementReleaseHelperThreads > 0) {
/* 417 */       setStatementCloseHelperExecutor(Executors.newFixedThreadPool(statementReleaseHelperThreads, new CustomThreadFactory("BoneCP-statement-close-helper-thread" + suffix, true)));
/*     */       
/* 419 */       for (int i = 0; i < statementReleaseHelperThreads; i++)
/*     */       {
/* 421 */         getStatementCloseHelperExecutor().execute(new StatementReleaseHelperThread((BlockingQueue<StatementHandle>)this.statementsPendingRelease, this));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initJMX() {
/* 430 */     if (this.mbs == null) {
/* 431 */       this.mbs = ManagementFactory.getPlatformMBeanServer();
/*     */     }
/*     */     try {
/* 434 */       String suffix = "";
/*     */       
/* 436 */       if (this.config.getPoolName() != null) {
/* 437 */         suffix = "-" + this.config.getPoolName();
/*     */       }
/*     */       
/* 440 */       ObjectName name = new ObjectName("com.jolbox.bonecp:type=BoneCP" + suffix);
/* 441 */       ObjectName configname = new ObjectName("com.jolbox.bonecp:type=BoneCPConfig" + suffix);
/*     */ 
/*     */       
/* 444 */       if (!this.mbs.isRegistered(name)) {
/* 445 */         this.mbs.registerMBean(this.statistics, name);
/*     */       }
/* 447 */       if (!this.mbs.isRegistered(configname)) {
/* 448 */         this.mbs.registerMBean(this.config, configname);
/*     */       }
/* 450 */     } catch (Exception e) {
/* 451 */       logger.error("Unable to start JMX", e);
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
/*     */   public Connection getConnection() throws SQLException {
/* 464 */     long statsObtainTime = 0L;
/*     */     
/* 466 */     if (this.poolShuttingDown) {
/* 467 */       throw new SQLException(this.shutdownStackTrace);
/*     */     }
/*     */     
/* 470 */     int partition = (int)(Thread.currentThread().getId() % this.partitionCount);
/* 471 */     ConnectionPartition connectionPartition = this.partitions[partition];
/*     */     
/* 473 */     if (this.statisticsEnabled) {
/* 474 */       statsObtainTime = System.nanoTime();
/* 475 */       this.statistics.incrementConnectionsRequested();
/*     */     } 
/* 477 */     ConnectionHandle result = (ConnectionHandle)connectionPartition.getFreeConnections().poll();
/*     */     
/* 479 */     if (result == null)
/*     */     {
/* 481 */       for (int i = 0; i < this.partitionCount; i++) {
/* 482 */         if (i != partition) {
/*     */ 
/*     */           
/* 485 */           result = (ConnectionHandle)this.partitions[i].getFreeConnections().poll();
/* 486 */           connectionPartition = this.partitions[i];
/* 487 */           if (result != null) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/* 493 */     if (!connectionPartition.isUnableToCreateMoreTransactions()) {
/* 494 */       maybeSignalForMoreConnections(connectionPartition);
/*     */     }
/*     */ 
/*     */     
/* 498 */     if (result == null) {
/*     */       try {
/* 500 */         result = (ConnectionHandle)connectionPartition.getFreeConnections().poll(this.connectionTimeoutInMs, TimeUnit.MILLISECONDS);
/* 501 */         if (result == null)
/*     */         {
/* 503 */           throw new SQLException("Timed out waiting for a free available connection.", "08001");
/*     */         }
/*     */       }
/* 506 */       catch (InterruptedException e) {
/* 507 */         throw new SQLException(e.getMessage());
/*     */       } 
/*     */     }
/* 510 */     result.renewConnection();
/*     */ 
/*     */     
/* 513 */     if (result.getConnectionHook() != null) {
/* 514 */       result.getConnectionHook().onCheckOut(result);
/*     */     }
/*     */     
/* 517 */     if (this.closeConnectionWatch) {
/* 518 */       watchConnection(result);
/*     */     }
/*     */     
/* 521 */     if (this.statisticsEnabled) {
/* 522 */       this.statistics.addCumulativeConnectionWaitTime(System.nanoTime() - statsObtainTime);
/*     */     }
/* 524 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void watchConnection(ConnectionHandle connectionHandle) {
/* 532 */     String message = captureStackTrace("Connection obtained from thread [%s] was never closed. \nStack trace of location where connection was obtained follows:\n");
/* 533 */     this.closeConnectionExecutor.submit(new CloseThreadMonitor(Thread.currentThread(), connectionHandle, message, this.closeConnectionWatchTimeoutInMs));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String captureStackTrace(String message) {
/* 542 */     StringBuilder stringBuilder = new StringBuilder(String.format(message, new Object[] { Thread.currentThread().getName() }));
/* 543 */     StackTraceElement[] trace = Thread.currentThread().getStackTrace();
/* 544 */     for (int i = 0; i < trace.length; i++) {
/* 545 */       stringBuilder.append(" " + trace[i] + "\r\n");
/*     */     }
/*     */     
/* 548 */     stringBuilder.append("");
/*     */     
/* 550 */     return stringBuilder.toString();
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
/*     */   public Future<Connection> getAsyncConnection() {
/* 564 */     return this.asyncExecutor.submit(new Callable<Connection>()
/*     */         {
/*     */           public Connection call() throws Exception {
/* 567 */             return BoneCP.this.getConnection();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void maybeSignalForMoreConnections(ConnectionPartition connectionPartition) {
/* 577 */     if (!connectionPartition.isUnableToCreateMoreTransactions() && !this.poolShuttingDown && connectionPartition.getAvailableConnections() * 100 / connectionPartition.getMaxConnections() <= this.poolAvailabilityThreshold)
/*     */     {
/* 579 */       connectionPartition.getPoolWatchThreadSignalQueue().offer(new Object());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void releaseConnection(Connection connection) throws SQLException {
/* 590 */     ConnectionHandle handle = (ConnectionHandle)connection;
/*     */ 
/*     */     
/* 593 */     if (handle.getConnectionHook() != null) {
/* 594 */       handle.getConnectionHook().onCheckIn(handle);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 599 */     if (!this.poolShuttingDown && this.releaseHelperThreadsConfigured) {
/* 600 */       if (!handle.getOriginatingPartition().getConnectionsPendingRelease().tryTransfer(handle)) {
/* 601 */         handle.getOriginatingPartition().getConnectionsPendingRelease().put(handle);
/*     */       }
/*     */     } else {
/* 604 */       internalReleaseConnection(handle);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void internalReleaseConnection(ConnectionHandle connectionHandle) throws SQLException {
/* 613 */     connectionHandle.clearStatementCaches(false);
/*     */     
/* 615 */     if (connectionHandle.getReplayLog() != null) {
/* 616 */       connectionHandle.getReplayLog().clear();
/* 617 */       connectionHandle.recoveryResult.getReplaceTarget().clear();
/*     */     } 
/*     */     
/* 620 */     if (connectionHandle.isExpired() || (!this.poolShuttingDown && connectionHandle.isPossiblyBroken() && !isConnectionHandleAlive(connectionHandle))) {
/*     */ 
/*     */       
/* 623 */       ConnectionPartition connectionPartition = connectionHandle.getOriginatingPartition();
/* 624 */       maybeSignalForMoreConnections(connectionPartition);
/*     */       
/* 626 */       postDestroyConnection(connectionHandle);
/* 627 */       connectionHandle.clearStatementCaches(true);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 632 */     connectionHandle.setConnectionLastUsedInMs(System.currentTimeMillis());
/* 633 */     if (!this.poolShuttingDown) {
/*     */       
/* 635 */       putConnectionBackInPartition(connectionHandle);
/*     */     } else {
/* 637 */       connectionHandle.internalClose();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void putConnectionBackInPartition(ConnectionHandle connectionHandle) throws SQLException {
/* 648 */     TransferQueue<ConnectionHandle> queue = connectionHandle.getOriginatingPartition().getFreeConnections();
/*     */     
/* 650 */     if (!queue.tryTransfer(connectionHandle) && 
/* 651 */       !queue.offer(connectionHandle)) {
/* 652 */       connectionHandle.internalClose();
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
/*     */   public boolean isConnectionHandleAlive(ConnectionHandle connection) {
/* 665 */     Statement stmt = null;
/* 666 */     boolean result = false;
/* 667 */     boolean logicallyClosed = connection.logicallyClosed;
/*     */     try {
/* 669 */       if (logicallyClosed) {
/* 670 */         connection.logicallyClosed = false;
/*     */       }
/* 672 */       String testStatement = this.config.getConnectionTestStatement();
/* 673 */       ResultSet rs = null;
/*     */       
/* 675 */       if (testStatement == null) {
/*     */         
/* 677 */         rs = connection.getMetaData().getTables(null, null, "BONECPKEEPALIVE", METADATATABLE);
/*     */       } else {
/* 679 */         stmt = connection.createStatement();
/* 680 */         stmt.execute(testStatement);
/*     */       } 
/*     */ 
/*     */       
/* 684 */       if (rs != null) {
/* 685 */         rs.close();
/*     */       }
/*     */       
/* 688 */       result = true;
/* 689 */     } catch (SQLException e) {
/*     */       
/* 691 */       result = false;
/*     */     } finally {
/* 693 */       connection.logicallyClosed = logicallyClosed;
/* 694 */       connection.setConnectionLastResetInMs(System.currentTimeMillis());
/* 695 */       result = closeStatement(stmt, result);
/*     */     } 
/* 697 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean closeStatement(Statement stmt, boolean result) {
/* 706 */     if (stmt != null) {
/*     */       try {
/* 708 */         stmt.close();
/* 709 */       } catch (SQLException e) {
/* 710 */         return false;
/*     */       } 
/*     */     }
/* 713 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTotalLeased() {
/* 720 */     int total = 0;
/* 721 */     for (int i = 0; i < this.partitionCount; i++) {
/* 722 */       total += this.partitions[i].getCreatedConnections() - this.partitions[i].getAvailableConnections();
/*     */     }
/* 724 */     return total;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTotalFree() {
/* 732 */     int total = 0;
/* 733 */     for (int i = 0; i < this.partitionCount; i++) {
/* 734 */       total += this.partitions[i].getAvailableConnections();
/*     */     }
/* 736 */     return total;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTotalCreatedConnections() {
/* 745 */     int total = 0;
/* 746 */     for (int i = 0; i < this.partitionCount; i++) {
/* 747 */       total += this.partitions[i].getCreatedConnections();
/*     */     }
/* 749 */     return total;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoneCPConfig getConfig() {
/* 759 */     return this.config;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ExecutorService getReleaseHelper() {
/* 766 */     return this.releaseHelper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setReleaseHelper(ExecutorService releaseHelper) {
/* 773 */     this.releaseHelper = releaseHelper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<Connection, Reference<ConnectionHandle>> getFinalizableRefs() {
/* 780 */     return this.finalizableRefs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FinalizableReferenceQueue getFinalizableRefQueue() {
/* 787 */     return this.finalizableRefQueue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ExecutorService getStatementCloseHelperExecutor() {
/* 795 */     return this.statementCloseHelperExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setStatementCloseHelperExecutor(ExecutorService statementCloseHelper) {
/* 804 */     this.statementCloseHelperExecutor = statementCloseHelper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isReleaseHelperThreadsConfigured() {
/* 812 */     return this.releaseHelperThreadsConfigured;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isStatementReleaseHelperThreadsConfigured() {
/* 820 */     return this.statementReleaseHelperThreadsConfigured;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected LinkedTransferQueue<StatementHandle> getStatementsPendingRelease() {
/* 828 */     return this.statementsPendingRelease;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statistics getStatistics() {
/* 836 */     return this.statistics;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/BoneCP.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */