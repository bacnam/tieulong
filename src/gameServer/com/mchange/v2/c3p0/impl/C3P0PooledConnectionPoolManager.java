/*      */ package com.mchange.v2.c3p0.impl;
/*      */ 
/*      */ import com.mchange.v1.db.sql.ConnectionUtils;
/*      */ import com.mchange.v1.db.sql.ResultSetUtils;
/*      */ import com.mchange.v1.db.sql.StatementUtils;
/*      */ import com.mchange.v1.lang.BooleanUtils;
/*      */ import com.mchange.v2.async.AsynchronousRunner;
/*      */ import com.mchange.v2.async.ThreadPoolAsynchronousRunner;
/*      */ import com.mchange.v2.c3p0.C3P0Registry;
/*      */ import com.mchange.v2.c3p0.ConnectionCustomizer;
/*      */ import com.mchange.v2.c3p0.ConnectionTester;
/*      */ import com.mchange.v2.c3p0.cfg.C3P0Config;
/*      */ import com.mchange.v2.c3p0.cfg.C3P0ConfigUtils;
/*      */ import com.mchange.v2.coalesce.CoalesceChecker;
/*      */ import com.mchange.v2.coalesce.Coalescer;
/*      */ import com.mchange.v2.coalesce.CoalescerFactory;
/*      */ import com.mchange.v2.log.MLevel;
/*      */ import com.mchange.v2.log.MLog;
/*      */ import com.mchange.v2.log.MLogger;
/*      */ import com.mchange.v2.resourcepool.BasicResourcePoolFactory;
/*      */ import com.mchange.v2.resourcepool.ResourcePoolFactory;
/*      */ import com.mchange.v2.sql.SqlUtils;
/*      */ import java.beans.BeanInfo;
/*      */ import java.beans.Introspector;
/*      */ import java.beans.PropertyDescriptor;
/*      */ import java.lang.reflect.Method;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.sql.Connection;
/*      */ import java.sql.DatabaseMetaData;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.Timer;
/*      */ import javax.sql.ConnectionPoolDataSource;
/*      */ import javax.sql.PooledConnection;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class C3P0PooledConnectionPoolManager
/*      */ {
/*   60 */   private static final MLogger logger = MLog.getLogger(C3P0PooledConnectionPoolManager.class);
/*      */   
/*      */   private static final boolean POOL_EVENT_SUPPORT = false;
/*      */   
/*   64 */   private static final CoalesceChecker COALESCE_CHECKER = IdentityTokenizedCoalesceChecker.INSTANCE;
/*      */ 
/*      */   
/*   67 */   static final Coalescer COALESCER = CoalescerFactory.createCoalescer(COALESCE_CHECKER, true, false);
/*      */   
/*      */   static final int DFLT_NUM_TASK_THREADS_PER_DATA_SOURCE = 3;
/*      */   
/*      */   ThreadPoolAsynchronousRunner taskRunner;
/*      */   
/*      */   ThreadPoolAsynchronousRunner deferredStatementDestroyer;
/*      */   
/*      */   Timer timer;
/*      */   
/*      */   ResourcePoolFactory rpfact;
/*      */   
/*      */   Map authsToPools;
/*      */   
/*      */   final ConnectionPoolDataSource cpds;
/*      */   
/*      */   final Map propNamesToReadMethods;
/*      */   final Map flatPropertyOverrides;
/*      */   final Map userOverrides;
/*      */   final DbAuth defaultAuth;
/*      */   final String parentDataSourceIdentityToken;
/*      */   final String parentDataSourceName;
/*   89 */   int num_task_threads = 3;
/*      */ 
/*      */ 
/*      */   
/*      */   public int getThreadPoolSize() {
/*   94 */     return this.taskRunner.getThreadCount();
/*      */   }
/*      */   public int getThreadPoolNumActiveThreads() {
/*   97 */     return this.taskRunner.getActiveCount();
/*      */   }
/*      */   public int getThreadPoolNumIdleThreads() {
/*  100 */     return this.taskRunner.getIdleCount();
/*      */   }
/*      */   public int getThreadPoolNumTasksPending() {
/*  103 */     return this.taskRunner.getPendingTaskCount();
/*      */   }
/*      */   public String getThreadPoolStackTraces() {
/*  106 */     return this.taskRunner.getStackTraces();
/*      */   }
/*      */   public String getThreadPoolStatus() {
/*  109 */     return this.taskRunner.getStatus();
/*      */   }
/*      */   public int getStatementDestroyerNumThreads() {
/*  112 */     return (this.deferredStatementDestroyer != null) ? this.deferredStatementDestroyer.getThreadCount() : -1;
/*      */   }
/*      */   public int getStatementDestroyerNumActiveThreads() {
/*  115 */     return (this.deferredStatementDestroyer != null) ? this.deferredStatementDestroyer.getActiveCount() : -1;
/*      */   }
/*      */   public int getStatementDestroyerNumIdleThreads() {
/*  118 */     return (this.deferredStatementDestroyer != null) ? this.deferredStatementDestroyer.getIdleCount() : -1;
/*      */   }
/*      */   public int getStatementDestroyerNumTasksPending() {
/*  121 */     return (this.deferredStatementDestroyer != null) ? this.deferredStatementDestroyer.getPendingTaskCount() : -1;
/*      */   }
/*      */   public String getStatementDestroyerStackTraces() {
/*  124 */     return (this.deferredStatementDestroyer != null) ? this.deferredStatementDestroyer.getStackTraces() : null;
/*      */   }
/*      */   public String getStatementDestroyerStatus() {
/*  127 */     return (this.deferredStatementDestroyer != null) ? this.deferredStatementDestroyer.getStatus() : null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private ThreadPoolAsynchronousRunner createTaskRunner(int num_threads, int matt, Timer timer, String threadLabel) {
/*  133 */     ThreadPoolAsynchronousRunner out = null;
/*  134 */     if (matt > 0) {
/*      */       
/*  136 */       int matt_ms = matt * 1000;
/*  137 */       out = new ThreadPoolAsynchronousRunner(num_threads, true, matt_ms, matt_ms * 3, matt_ms * 6, timer, threadLabel);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  149 */       out = new ThreadPoolAsynchronousRunner(num_threads, true, timer, threadLabel);
/*      */     } 
/*  151 */     return out;
/*      */   }
/*      */ 
/*      */   
/*      */   private String idString() {
/*  156 */     StringBuffer sb = new StringBuffer(512);
/*  157 */     sb.append("C3P0PooledConnectionPoolManager");
/*  158 */     sb.append('[');
/*  159 */     sb.append("identityToken->");
/*  160 */     sb.append(this.parentDataSourceIdentityToken);
/*  161 */     if (this.parentDataSourceIdentityToken == null || !this.parentDataSourceIdentityToken.equals(this.parentDataSourceName)) {
/*      */       
/*  163 */       sb.append(", dataSourceName->");
/*  164 */       sb.append(this.parentDataSourceName);
/*      */     } 
/*  166 */     sb.append(']');
/*  167 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private void maybePrivilegedPoolsInit(boolean privilege_spawned_threads) {
/*  172 */     if (privilege_spawned_threads) {
/*      */       
/*  174 */       PrivilegedAction<Void> privilegedPoolsInit = new PrivilegedAction<Void>()
/*      */         {
/*      */           public Void run()
/*      */           {
/*  178 */             C3P0PooledConnectionPoolManager.this._poolsInit();
/*  179 */             return null;
/*      */           }
/*      */         };
/*  182 */       AccessController.doPrivileged(privilegedPoolsInit);
/*      */     } else {
/*      */       
/*  185 */       _poolsInit();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void poolsInit() {
/*  191 */     final boolean privilege_spawned_threads = getPrivilegeSpawnedThreads();
/*  192 */     String contextClassLoaderSource = getContextClassLoaderSource();
/*      */     class ContextClassLoaderPoolsInitThread
/*      */       extends Thread
/*      */     {
/*      */       ContextClassLoaderPoolsInitThread(ClassLoader ccl)
/*      */       {
/*  198 */         setContextClassLoader(ccl);
/*      */       }
/*      */       public void run() {
/*  201 */         C3P0PooledConnectionPoolManager.this.maybePrivilegedPoolsInit(privilege_spawned_threads);
/*      */       }
/*      */     };
/*      */     
/*      */     try {
/*  206 */       if ("library".equalsIgnoreCase(contextClassLoaderSource))
/*      */       {
/*  208 */         Thread t = new ContextClassLoaderPoolsInitThread(getClass().getClassLoader());
/*  209 */         t.start();
/*  210 */         t.join();
/*      */       }
/*  212 */       else if ("none".equalsIgnoreCase(contextClassLoaderSource))
/*      */       {
/*  214 */         Thread t = new ContextClassLoaderPoolsInitThread(this, null, privilege_spawned_threads);
/*  215 */         t.start();
/*  216 */         t.join();
/*      */       }
/*      */       else
/*      */       {
/*  220 */         if (logger.isLoggable(MLevel.WARNING) && !"caller".equalsIgnoreCase(contextClassLoaderSource))
/*  221 */           logger.log(MLevel.WARNING, "Unknown contextClassLoaderSource: " + contextClassLoaderSource + " -- should be 'caller', 'library', or 'none'. Using default value 'caller'."); 
/*  222 */         maybePrivilegedPoolsInit(privilege_spawned_threads);
/*      */       }
/*      */     
/*  225 */     } catch (InterruptedException e) {
/*      */       
/*  227 */       if (logger.isLoggable(MLevel.SEVERE)) {
/*  228 */         logger.log(MLevel.SEVERE, "Unexpected interruption while trying to initialize DataSource Thread resources [ poolsInit() ].", e);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private synchronized void _poolsInit() {
/*  234 */     String idStr = idString();
/*      */     
/*  236 */     this.timer = new Timer(idStr + "-AdminTaskTimer", true);
/*      */     
/*  238 */     int matt = getMaxAdministrativeTaskTime();
/*      */     
/*  240 */     this.taskRunner = createTaskRunner(this.num_task_threads, matt, this.timer, idStr + "-HelperThread");
/*      */ 
/*      */ 
/*      */     
/*  244 */     int num_deferred_close_threads = getStatementCacheNumDeferredCloseThreads();
/*      */     
/*  246 */     if (num_deferred_close_threads > 0) {
/*  247 */       this.deferredStatementDestroyer = createTaskRunner(num_deferred_close_threads, matt, this.timer, idStr + "-DeferredStatementDestroyerThread");
/*      */     } else {
/*  249 */       this.deferredStatementDestroyer = null;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  254 */     this.rpfact = (ResourcePoolFactory)BasicResourcePoolFactory.createNoEventSupportInstance((AsynchronousRunner)this.taskRunner, this.timer);
/*      */     
/*  256 */     this.authsToPools = new HashMap<Object, Object>();
/*      */   }
/*      */   
/*      */   private void poolsDestroy() {
/*  260 */     poolsDestroy(true);
/*      */   }
/*      */ 
/*      */   
/*      */   private synchronized void poolsDestroy(boolean close_outstanding_connections) {
/*  265 */     for (Iterator<C3P0PooledConnectionPool> ii = this.authsToPools.values().iterator(); ii.hasNext();) {
/*      */       
/*      */       try {
/*  268 */         ((C3P0PooledConnectionPool)ii.next()).close(close_outstanding_connections);
/*  269 */       } catch (Exception e) {
/*      */ 
/*      */         
/*  272 */         logger.log(MLevel.WARNING, "An Exception occurred while trying to clean up a pool!", e);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  277 */     this.taskRunner.close(true);
/*      */ 
/*      */     
/*  280 */     if (this.deferredStatementDestroyer != null) {
/*  281 */       this.deferredStatementDestroyer.close(false);
/*      */     }
/*  283 */     this.timer.cancel();
/*      */     
/*  285 */     this.taskRunner = null;
/*  286 */     this.timer = null;
/*  287 */     this.rpfact = null;
/*  288 */     this.authsToPools = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public C3P0PooledConnectionPoolManager(ConnectionPoolDataSource cpds, Map flatPropertyOverrides, Map forceUserOverrides, int num_task_threads, String parentDataSourceIdentityToken, String parentDataSourceName) throws SQLException {
/*      */     try {
/*  301 */       this.cpds = cpds;
/*  302 */       this.flatPropertyOverrides = flatPropertyOverrides;
/*  303 */       this.num_task_threads = num_task_threads;
/*  304 */       this.parentDataSourceIdentityToken = parentDataSourceIdentityToken;
/*  305 */       this.parentDataSourceName = parentDataSourceName;
/*      */       
/*  307 */       DbAuth auth = null;
/*      */       
/*  309 */       if (flatPropertyOverrides != null) {
/*      */         
/*  311 */         String overrideUser = (String)flatPropertyOverrides.get("overrideDefaultUser");
/*  312 */         String overridePassword = (String)flatPropertyOverrides.get("overrideDefaultPassword");
/*      */         
/*  314 */         if (overrideUser == null) {
/*      */           
/*  316 */           overrideUser = (String)flatPropertyOverrides.get("user");
/*  317 */           overridePassword = (String)flatPropertyOverrides.get("password");
/*      */         } 
/*      */         
/*  320 */         if (overrideUser != null) {
/*  321 */           auth = new DbAuth(overrideUser, overridePassword);
/*      */         }
/*      */       } 
/*  324 */       if (auth == null) {
/*  325 */         auth = C3P0ImplUtils.findAuth(cpds);
/*      */       }
/*  327 */       this.defaultAuth = auth;
/*      */       
/*  329 */       Map<Object, Object> tmp = new HashMap<Object, Object>();
/*  330 */       BeanInfo bi = Introspector.getBeanInfo(cpds.getClass());
/*  331 */       PropertyDescriptor[] pds = bi.getPropertyDescriptors();
/*  332 */       PropertyDescriptor pd = null;
/*  333 */       for (int i = 0, len = pds.length; i < len; i++) {
/*      */         
/*  335 */         pd = pds[i];
/*      */         
/*  337 */         String name = pd.getName();
/*  338 */         Method m = pd.getReadMethod();
/*      */         
/*  340 */         if (m != null)
/*  341 */           tmp.put(name, m); 
/*      */       } 
/*  343 */       this.propNamesToReadMethods = tmp;
/*      */       
/*  345 */       if (forceUserOverrides == null) {
/*      */         
/*  347 */         Method uom = (Method)this.propNamesToReadMethods.get("userOverridesAsString");
/*  348 */         if (uom != null) {
/*      */           
/*  350 */           String uoas = (String)uom.invoke(cpds, null);
/*      */           
/*  352 */           Map uo = C3P0ImplUtils.parseUserOverridesAsString(uoas);
/*  353 */           this.userOverrides = uo;
/*      */         } else {
/*      */           
/*  356 */           this.userOverrides = Collections.EMPTY_MAP;
/*      */         } 
/*      */       } else {
/*  359 */         this.userOverrides = forceUserOverrides;
/*      */       } 
/*  361 */       poolsInit();
/*      */     }
/*  363 */     catch (Exception e) {
/*      */ 
/*      */       
/*  366 */       logger.log(MLevel.FINE, null, e);
/*      */       
/*  368 */       throw SqlUtils.toSQLException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized C3P0PooledConnectionPool getPool(String username, String password, boolean create) throws SQLException {
/*  374 */     if (create) {
/*  375 */       return getPool(username, password);
/*      */     }
/*      */     
/*  378 */     DbAuth checkAuth = new DbAuth(username, password);
/*  379 */     C3P0PooledConnectionPool out = (C3P0PooledConnectionPool)this.authsToPools.get(checkAuth);
/*  380 */     if (out == null) {
/*  381 */       throw new SQLException("No pool has been initialized for databse user '" + username + "' with the specified password.");
/*      */     }
/*  383 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public C3P0PooledConnectionPool getPool(String username, String password) throws SQLException {
/*  389 */     return getPool(new DbAuth(username, password));
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized C3P0PooledConnectionPool getPool(DbAuth auth) throws SQLException {
/*  394 */     C3P0PooledConnectionPool out = (C3P0PooledConnectionPool)this.authsToPools.get(auth);
/*  395 */     if (out == null) {
/*      */       
/*  397 */       out = createPooledConnectionPool(auth);
/*  398 */       this.authsToPools.put(auth, out);
/*      */       
/*  400 */       if (logger.isLoggable(MLevel.FINE))
/*  401 */         logger.log(MLevel.FINE, "Created new pool for auth, username (masked): '" + auth.getMaskedUserString() + "'."); 
/*      */     } 
/*  403 */     return out;
/*      */   }
/*      */   
/*      */   public synchronized Set getManagedAuths() {
/*  407 */     return Collections.unmodifiableSet(this.authsToPools.keySet());
/*      */   }
/*      */   public synchronized int getNumManagedAuths() {
/*  410 */     return this.authsToPools.size();
/*      */   }
/*      */   
/*      */   public C3P0PooledConnectionPool getPool() throws SQLException {
/*  414 */     return getPool(this.defaultAuth);
/*      */   }
/*      */   
/*      */   public synchronized int getNumIdleConnectionsAllAuths() throws SQLException {
/*  418 */     int out = 0;
/*  419 */     for (Iterator<C3P0PooledConnectionPool> ii = this.authsToPools.values().iterator(); ii.hasNext();)
/*  420 */       out += ((C3P0PooledConnectionPool)ii.next()).getNumIdleConnections(); 
/*  421 */     return out;
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getNumBusyConnectionsAllAuths() throws SQLException {
/*  426 */     int out = 0;
/*  427 */     for (Iterator<C3P0PooledConnectionPool> ii = this.authsToPools.values().iterator(); ii.hasNext();)
/*  428 */       out += ((C3P0PooledConnectionPool)ii.next()).getNumBusyConnections(); 
/*  429 */     return out;
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getNumConnectionsAllAuths() throws SQLException {
/*  434 */     int out = 0;
/*  435 */     for (Iterator<C3P0PooledConnectionPool> ii = this.authsToPools.values().iterator(); ii.hasNext();)
/*  436 */       out += ((C3P0PooledConnectionPool)ii.next()).getNumConnections(); 
/*  437 */     return out;
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getNumUnclosedOrphanedConnectionsAllAuths() throws SQLException {
/*  442 */     int out = 0;
/*  443 */     for (Iterator<C3P0PooledConnectionPool> ii = this.authsToPools.values().iterator(); ii.hasNext();)
/*  444 */       out += ((C3P0PooledConnectionPool)ii.next()).getNumUnclosedOrphanedConnections(); 
/*  445 */     return out;
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getStatementCacheNumStatementsAllUsers() throws SQLException {
/*  450 */     int out = 0;
/*  451 */     for (Iterator<C3P0PooledConnectionPool> ii = this.authsToPools.values().iterator(); ii.hasNext();)
/*  452 */       out += ((C3P0PooledConnectionPool)ii.next()).getStatementCacheNumStatements(); 
/*  453 */     return out;
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getStatementCacheNumCheckedOutStatementsAllUsers() throws SQLException {
/*  458 */     int out = 0;
/*  459 */     for (Iterator<C3P0PooledConnectionPool> ii = this.authsToPools.values().iterator(); ii.hasNext();)
/*  460 */       out += ((C3P0PooledConnectionPool)ii.next()).getStatementCacheNumCheckedOut(); 
/*  461 */     return out;
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getStatementCacheNumConnectionsWithCachedStatementsAllUsers() throws SQLException {
/*  466 */     int out = 0;
/*  467 */     for (Iterator<C3P0PooledConnectionPool> ii = this.authsToPools.values().iterator(); ii.hasNext();)
/*  468 */       out += ((C3P0PooledConnectionPool)ii.next()).getStatementCacheNumConnectionsWithCachedStatements(); 
/*  469 */     return out;
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getStatementDestroyerNumConnectionsInUseAllUsers() throws SQLException {
/*  474 */     if (this.deferredStatementDestroyer != null) {
/*      */       
/*  476 */       int out = 0;
/*  477 */       for (Iterator<C3P0PooledConnectionPool> ii = this.authsToPools.values().iterator(); ii.hasNext();)
/*  478 */         out += ((C3P0PooledConnectionPool)ii.next()).getStatementDestroyerNumConnectionsInUse(); 
/*  479 */       return out;
/*      */     } 
/*      */     
/*  482 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getStatementDestroyerNumConnectionsWithDeferredDestroyStatementsAllUsers() throws SQLException {
/*  487 */     if (this.deferredStatementDestroyer != null) {
/*      */       
/*  489 */       int out = 0;
/*  490 */       for (Iterator<C3P0PooledConnectionPool> ii = this.authsToPools.values().iterator(); ii.hasNext();)
/*  491 */         out += ((C3P0PooledConnectionPool)ii.next()).getStatementDestroyerNumConnectionsWithDeferredDestroyStatements(); 
/*  492 */       return out;
/*      */     } 
/*      */     
/*  495 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getStatementDestroyerNumDeferredDestroyStatementsAllUsers() throws SQLException {
/*  500 */     if (this.deferredStatementDestroyer != null) {
/*      */       
/*  502 */       int out = 0;
/*  503 */       for (Iterator<C3P0PooledConnectionPool> ii = this.authsToPools.values().iterator(); ii.hasNext();)
/*  504 */         out += ((C3P0PooledConnectionPool)ii.next()).getStatementDestroyerNumDeferredDestroyStatements(); 
/*  505 */       return out;
/*      */     } 
/*      */     
/*  508 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void softResetAllAuths() throws SQLException {
/*  513 */     for (Iterator<C3P0PooledConnectionPool> ii = this.authsToPools.values().iterator(); ii.hasNext();)
/*  514 */       ((C3P0PooledConnectionPool)ii.next()).reset(); 
/*      */   }
/*      */   
/*      */   public void close() {
/*  518 */     close(true);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void close(boolean close_outstanding_connections) {
/*  523 */     if (this.authsToPools != null) {
/*  524 */       poolsDestroy(close_outstanding_connections);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected synchronized void finalize() {
/*  530 */     close();
/*      */   }
/*      */ 
/*      */   
/*      */   private Object getObject(String propName, String userName) {
/*  535 */     Object out = null;
/*      */ 
/*      */     
/*  538 */     if (userName != null) {
/*  539 */       out = C3P0ConfigUtils.extractUserOverride(propName, userName, this.userOverrides);
/*      */     }
/*  541 */     if (out == null && this.flatPropertyOverrides != null) {
/*  542 */       out = this.flatPropertyOverrides.get(propName);
/*      */     }
/*      */ 
/*      */     
/*  546 */     if (out == null) {
/*      */       
/*      */       try {
/*      */         
/*  550 */         Method m = (Method)this.propNamesToReadMethods.get(propName);
/*  551 */         if (m != null) {
/*      */           
/*  553 */           Object readProp = m.invoke(this.cpds, null);
/*  554 */           if (readProp != null) {
/*  555 */             out = readProp.toString();
/*      */           }
/*      */         } 
/*  558 */       } catch (Exception e) {
/*      */         
/*  560 */         if (logger.isLoggable(MLevel.WARNING)) {
/*  561 */           logger.log(MLevel.WARNING, "An exception occurred while trying to read property '" + propName + "' from ConnectionPoolDataSource: " + this.cpds + ". Default config value will be used.", e);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  572 */     if (out == null) {
/*  573 */       out = C3P0Config.getUnspecifiedUserProperty(propName, null);
/*      */     }
/*  575 */     return out;
/*      */   }
/*      */ 
/*      */   
/*      */   private String getString(String propName, String userName) {
/*  580 */     Object o = getObject(propName, userName);
/*  581 */     return (o == null) ? null : o.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private int getInt(String propName, String userName) throws Exception {
/*  586 */     Object o = getObject(propName, userName);
/*  587 */     if (o instanceof Integer)
/*  588 */       return ((Integer)o).intValue(); 
/*  589 */     if (o instanceof String) {
/*  590 */       return Integer.parseInt((String)o);
/*      */     }
/*  592 */     throw new Exception("Unexpected object found for putative int property '" + propName + "': " + o);
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean getBoolean(String propName, String userName) throws Exception {
/*  597 */     Object o = getObject(propName, userName);
/*  598 */     if (o instanceof Boolean)
/*  599 */       return ((Boolean)o).booleanValue(); 
/*  600 */     if (o instanceof String) {
/*  601 */       return BooleanUtils.parseBoolean((String)o);
/*      */     }
/*  603 */     throw new Exception("Unexpected object found for putative boolean property '" + propName + "': " + o);
/*      */   }
/*      */   
/*      */   public String getAutomaticTestTable(String userName) {
/*  607 */     return getString("automaticTestTable", userName);
/*      */   }
/*      */   public String getPreferredTestQuery(String userName) {
/*  610 */     return getString("preferredTestQuery", userName);
/*      */   }
/*      */   
/*      */   private int getInitialPoolSize(String userName) {
/*      */     try {
/*  615 */       return getInt("initialPoolSize", userName);
/*  616 */     } catch (Exception e) {
/*      */       
/*  618 */       if (logger.isLoggable(MLevel.FINE))
/*  619 */         logger.log(MLevel.FINE, "Could not fetch int property", e); 
/*  620 */       return C3P0Defaults.initialPoolSize();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMinPoolSize(String userName) {
/*      */     try {
/*  627 */       return getInt("minPoolSize", userName);
/*  628 */     } catch (Exception e) {
/*      */       
/*  630 */       if (logger.isLoggable(MLevel.FINE))
/*  631 */         logger.log(MLevel.FINE, "Could not fetch int property", e); 
/*  632 */       return C3P0Defaults.minPoolSize();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private int getMaxPoolSize(String userName) {
/*      */     try {
/*  639 */       return getInt("maxPoolSize", userName);
/*  640 */     } catch (Exception e) {
/*      */       
/*  642 */       if (logger.isLoggable(MLevel.FINE))
/*  643 */         logger.log(MLevel.FINE, "Could not fetch int property", e); 
/*  644 */       return C3P0Defaults.maxPoolSize();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private int getMaxStatements(String userName) {
/*      */     try {
/*  651 */       return getInt("maxStatements", userName);
/*  652 */     } catch (Exception e) {
/*      */       
/*  654 */       if (logger.isLoggable(MLevel.FINE))
/*  655 */         logger.log(MLevel.FINE, "Could not fetch int property", e); 
/*  656 */       return C3P0Defaults.maxStatements();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private int getMaxStatementsPerConnection(String userName) {
/*      */     try {
/*  663 */       return getInt("maxStatementsPerConnection", userName);
/*  664 */     } catch (Exception e) {
/*      */       
/*  666 */       if (logger.isLoggable(MLevel.FINE))
/*  667 */         logger.log(MLevel.FINE, "Could not fetch int property", e); 
/*  668 */       return C3P0Defaults.maxStatementsPerConnection();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private int getAcquireIncrement(String userName) {
/*      */     try {
/*  675 */       return getInt("acquireIncrement", userName);
/*  676 */     } catch (Exception e) {
/*      */       
/*  678 */       if (logger.isLoggable(MLevel.FINE))
/*  679 */         logger.log(MLevel.FINE, "Could not fetch int property", e); 
/*  680 */       return C3P0Defaults.acquireIncrement();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private int getAcquireRetryAttempts(String userName) {
/*      */     try {
/*  687 */       return getInt("acquireRetryAttempts", userName);
/*  688 */     } catch (Exception e) {
/*      */       
/*  690 */       if (logger.isLoggable(MLevel.FINE))
/*  691 */         logger.log(MLevel.FINE, "Could not fetch int property", e); 
/*  692 */       return C3P0Defaults.acquireRetryAttempts();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private int getAcquireRetryDelay(String userName) {
/*      */     try {
/*  699 */       return getInt("acquireRetryDelay", userName);
/*  700 */     } catch (Exception e) {
/*      */       
/*  702 */       if (logger.isLoggable(MLevel.FINE))
/*  703 */         logger.log(MLevel.FINE, "Could not fetch int property", e); 
/*  704 */       return C3P0Defaults.acquireRetryDelay();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean getBreakAfterAcquireFailure(String userName) {
/*      */     try {
/*  711 */       return getBoolean("breakAfterAcquireFailure", userName);
/*  712 */     } catch (Exception e) {
/*      */       
/*  714 */       if (logger.isLoggable(MLevel.FINE))
/*  715 */         logger.log(MLevel.FINE, "Could not fetch boolean property", e); 
/*  716 */       return C3P0Defaults.breakAfterAcquireFailure();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private int getCheckoutTimeout(String userName) {
/*      */     try {
/*  723 */       return getInt("checkoutTimeout", userName);
/*  724 */     } catch (Exception e) {
/*      */       
/*  726 */       if (logger.isLoggable(MLevel.FINE))
/*  727 */         logger.log(MLevel.FINE, "Could not fetch int property", e); 
/*  728 */       return C3P0Defaults.checkoutTimeout();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private int getIdleConnectionTestPeriod(String userName) {
/*      */     try {
/*  735 */       return getInt("idleConnectionTestPeriod", userName);
/*  736 */     } catch (Exception e) {
/*      */       
/*  738 */       if (logger.isLoggable(MLevel.FINE))
/*  739 */         logger.log(MLevel.FINE, "Could not fetch int property", e); 
/*  740 */       return C3P0Defaults.idleConnectionTestPeriod();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private int getMaxIdleTime(String userName) {
/*      */     try {
/*  747 */       return getInt("maxIdleTime", userName);
/*  748 */     } catch (Exception e) {
/*      */       
/*  750 */       if (logger.isLoggable(MLevel.FINE))
/*  751 */         logger.log(MLevel.FINE, "Could not fetch int property", e); 
/*  752 */       return C3P0Defaults.maxIdleTime();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private int getUnreturnedConnectionTimeout(String userName) {
/*      */     try {
/*  759 */       return getInt("unreturnedConnectionTimeout", userName);
/*  760 */     } catch (Exception e) {
/*      */       
/*  762 */       if (logger.isLoggable(MLevel.FINE))
/*  763 */         logger.log(MLevel.FINE, "Could not fetch int property", e); 
/*  764 */       return C3P0Defaults.unreturnedConnectionTimeout();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean getTestConnectionOnCheckout(String userName) {
/*      */     try {
/*  771 */       return getBoolean("testConnectionOnCheckout", userName);
/*  772 */     } catch (Exception e) {
/*      */       
/*  774 */       if (logger.isLoggable(MLevel.FINE))
/*  775 */         logger.log(MLevel.FINE, "Could not fetch boolean property", e); 
/*  776 */       return C3P0Defaults.testConnectionOnCheckout();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean getTestConnectionOnCheckin(String userName) {
/*      */     try {
/*  783 */       return getBoolean("testConnectionOnCheckin", userName);
/*  784 */     } catch (Exception e) {
/*      */       
/*  786 */       if (logger.isLoggable(MLevel.FINE))
/*  787 */         logger.log(MLevel.FINE, "Could not fetch boolean property", e); 
/*  788 */       return C3P0Defaults.testConnectionOnCheckin();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean getDebugUnreturnedConnectionStackTraces(String userName) {
/*      */     try {
/*  795 */       return getBoolean("debugUnreturnedConnectionStackTraces", userName);
/*  796 */     } catch (Exception e) {
/*      */       
/*  798 */       if (logger.isLoggable(MLevel.FINE))
/*  799 */         logger.log(MLevel.FINE, "Could not fetch boolean property", e); 
/*  800 */       return C3P0Defaults.debugUnreturnedConnectionStackTraces();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private String getConnectionTesterClassName(String userName) {
/*  806 */     return getString("connectionTesterClassName", userName);
/*      */   }
/*      */   private ConnectionTester getConnectionTester(String userName) {
/*  809 */     return C3P0Registry.getConnectionTester(getConnectionTesterClassName(userName));
/*      */   }
/*      */   private String getConnectionCustomizerClassName(String userName) {
/*  812 */     return getString("connectionCustomizerClassName", userName);
/*      */   }
/*      */   private ConnectionCustomizer getConnectionCustomizer(String userName) throws SQLException {
/*  815 */     return C3P0Registry.getConnectionCustomizer(getConnectionCustomizerClassName(userName));
/*      */   }
/*      */   
/*      */   private int getMaxIdleTimeExcessConnections(String userName) {
/*      */     try {
/*  820 */       return getInt("maxIdleTimeExcessConnections", userName);
/*  821 */     } catch (Exception e) {
/*      */       
/*  823 */       if (logger.isLoggable(MLevel.FINE))
/*  824 */         logger.log(MLevel.FINE, "Could not fetch int property", e); 
/*  825 */       return C3P0Defaults.maxIdleTimeExcessConnections();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private int getMaxConnectionAge(String userName) {
/*      */     try {
/*  832 */       return getInt("maxConnectionAge", userName);
/*  833 */     } catch (Exception e) {
/*      */       
/*  835 */       if (logger.isLoggable(MLevel.FINE))
/*  836 */         logger.log(MLevel.FINE, "Could not fetch int property", e); 
/*  837 */       return C3P0Defaults.maxConnectionAge();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private int getPropertyCycle(String userName) {
/*      */     try {
/*  844 */       return getInt("propertyCycle", userName);
/*  845 */     } catch (Exception e) {
/*      */       
/*  847 */       if (logger.isLoggable(MLevel.FINE))
/*  848 */         logger.log(MLevel.FINE, "Could not fetch int property", e); 
/*  849 */       return C3P0Defaults.propertyCycle();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String getContextClassLoaderSource() {
/*      */     try {
/*  857 */       return getString("contextClassLoaderSource", null);
/*  858 */     } catch (Exception e) {
/*      */       
/*  860 */       if (logger.isLoggable(MLevel.FINE))
/*  861 */         logger.log(MLevel.FINE, "Could not fetch String property", e); 
/*  862 */       return C3P0Defaults.contextClassLoaderSource();
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean getPrivilegeSpawnedThreads() {
/*      */     try {
/*  868 */       return getBoolean("privilegeSpawnedThreads", null);
/*  869 */     } catch (Exception e) {
/*      */       
/*  871 */       if (logger.isLoggable(MLevel.FINE))
/*  872 */         logger.log(MLevel.FINE, "Could not fetch boolean property", e); 
/*  873 */       return C3P0Defaults.privilegeSpawnedThreads();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private int getMaxAdministrativeTaskTime() {
/*      */     try {
/*  880 */       return getInt("maxAdministrativeTaskTime", null);
/*  881 */     } catch (Exception e) {
/*      */       
/*  883 */       if (logger.isLoggable(MLevel.FINE))
/*  884 */         logger.log(MLevel.FINE, "Could not fetch int property", e); 
/*  885 */       return C3P0Defaults.maxAdministrativeTaskTime();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private int getStatementCacheNumDeferredCloseThreads() {
/*      */     try {
/*  892 */       return getInt("statementCacheNumDeferredCloseThreads", null);
/*  893 */     } catch (Exception e) {
/*      */       
/*  895 */       if (logger.isLoggable(MLevel.FINE))
/*  896 */         logger.log(MLevel.FINE, "Could not fetch int property", e); 
/*  897 */       return C3P0Defaults.statementCacheNumDeferredCloseThreads();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private C3P0PooledConnectionPool createPooledConnectionPool(DbAuth auth) throws SQLException {
/*  904 */     String realTestQuery, userName = auth.getUser();
/*  905 */     String automaticTestTable = getAutomaticTestTable(userName);
/*      */ 
/*      */     
/*  908 */     if (automaticTestTable != null) {
/*      */       
/*  910 */       realTestQuery = initializeAutomaticTestTable(automaticTestTable, auth);
/*  911 */       if (getPreferredTestQuery(userName) != null)
/*      */       {
/*  913 */         if (logger.isLoggable(MLevel.WARNING))
/*      */         {
/*  915 */           logger.logp(MLevel.WARNING, C3P0PooledConnectionPoolManager.class.getName(), "createPooledConnectionPool", "[c3p0] Both automaticTestTable and preferredTestQuery have been set! Using automaticTestTable, and ignoring preferredTestQuery. Real test query is ''{0}''.", realTestQuery);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  946 */       if (!this.defaultAuth.equals(auth)) {
/*  947 */         ensureFirstConnectionAcquisition(auth);
/*      */       }
/*  949 */       realTestQuery = getPreferredTestQuery(userName);
/*      */     } 
/*      */     
/*  952 */     C3P0PooledConnectionPool out = new C3P0PooledConnectionPool(this.cpds, auth, getMinPoolSize(userName), getMaxPoolSize(userName), getInitialPoolSize(userName), getAcquireIncrement(userName), getAcquireRetryAttempts(userName), getAcquireRetryDelay(userName), getBreakAfterAcquireFailure(userName), getCheckoutTimeout(userName), getIdleConnectionTestPeriod(userName), getMaxIdleTime(userName), getMaxIdleTimeExcessConnections(userName), getMaxConnectionAge(userName), getPropertyCycle(userName), getUnreturnedConnectionTimeout(userName), getDebugUnreturnedConnectionStackTraces(userName), getTestConnectionOnCheckout(userName), getTestConnectionOnCheckin(userName), getMaxStatements(userName), getMaxStatementsPerConnection(userName), getConnectionTester(userName), getConnectionCustomizer(userName), realTestQuery, this.rpfact, this.taskRunner, this.deferredStatementDestroyer, this.parentDataSourceIdentityToken);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  980 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String initializeAutomaticTestTable(String automaticTestTable, DbAuth auth) throws SQLException {
/*  987 */     PooledConnection throwawayPooledConnection = auth.equals(this.defaultAuth) ? this.cpds.getPooledConnection() : this.cpds.getPooledConnection(auth.getUser(), auth.getPassword());
/*      */ 
/*      */     
/*  990 */     Connection c = null;
/*  991 */     PreparedStatement testStmt = null;
/*  992 */     PreparedStatement createStmt = null;
/*  993 */     ResultSet mdrs = null;
/*  994 */     ResultSet rs = null;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1000 */       c = throwawayPooledConnection.getConnection();
/*      */       
/* 1002 */       DatabaseMetaData dmd = c.getMetaData();
/* 1003 */       String q = dmd.getIdentifierQuoteString();
/* 1004 */       String quotedTableName = q + automaticTestTable + q;
/* 1005 */       String out = "SELECT * FROM " + quotedTableName;
/* 1006 */       mdrs = dmd.getTables(null, null, automaticTestTable, new String[] { "TABLE" });
/* 1007 */       boolean exists = mdrs.next();
/*      */ 
/*      */ 
/*      */       
/* 1011 */       if (exists) {
/*      */         
/* 1013 */         testStmt = c.prepareStatement(out);
/* 1014 */         rs = testStmt.executeQuery();
/* 1015 */         boolean has_rows = rs.next();
/* 1016 */         if (has_rows) {
/* 1017 */           throw new SQLException("automatic test table '" + automaticTestTable + "' contains rows, and it should not! Please set this " + "parameter to the name of a table c3p0 can create on its own, " + "that is not used elsewhere in the database!");
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1024 */         String createSql = "CREATE TABLE " + quotedTableName + " ( a CHAR(1) )";
/*      */         
/*      */         try {
/* 1027 */           createStmt = c.prepareStatement(createSql);
/* 1028 */           createStmt.executeUpdate();
/*      */         }
/* 1030 */         catch (SQLException e) {
/*      */           
/* 1032 */           if (logger.isLoggable(MLevel.WARNING)) {
/* 1033 */             logger.log(MLevel.WARNING, "An attempt to create an automatic test table failed. Create SQL: " + createSql, e);
/*      */           }
/*      */ 
/*      */           
/* 1037 */           throw e;
/*      */         } 
/*      */       } 
/* 1040 */       return out;
/*      */     }
/*      */     finally {
/*      */       
/* 1044 */       ResultSetUtils.attemptClose(mdrs);
/* 1045 */       ResultSetUtils.attemptClose(rs);
/* 1046 */       StatementUtils.attemptClose(testStmt);
/* 1047 */       StatementUtils.attemptClose(createStmt);
/* 1048 */       ConnectionUtils.attemptClose(c); try {
/* 1049 */         if (throwawayPooledConnection != null) throwawayPooledConnection.close(); 
/* 1050 */       } catch (Exception e) {
/*      */ 
/*      */         
/* 1053 */         logger.log(MLevel.WARNING, "A PooledConnection failed to close.", e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void ensureFirstConnectionAcquisition(DbAuth auth) throws SQLException {
/* 1060 */     PooledConnection throwawayPooledConnection = auth.equals(this.defaultAuth) ? this.cpds.getPooledConnection() : this.cpds.getPooledConnection(auth.getUser(), auth.getPassword());
/*      */ 
/*      */     
/* 1063 */     Connection c = null;
/*      */     
/*      */     try {
/* 1066 */       c = throwawayPooledConnection.getConnection();
/*      */     }
/*      */     finally {
/*      */       
/* 1070 */       ConnectionUtils.attemptClose(c); try {
/* 1071 */         if (throwawayPooledConnection != null) throwawayPooledConnection.close(); 
/* 1072 */       } catch (Exception e) {
/*      */ 
/*      */         
/* 1075 */         logger.log(MLevel.WARNING, "A PooledConnection failed to close.", e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/C3P0PooledConnectionPoolManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */