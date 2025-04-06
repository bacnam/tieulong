/*     */ package com.mchange.v2.c3p0;
/*     */ 
/*     */ import com.mchange.v2.c3p0.cfg.C3P0ConfigUtils;
/*     */ import com.mchange.v2.c3p0.impl.C3P0Defaults;
/*     */ import com.mchange.v2.c3p0.impl.IdentityTokenized;
/*     */ import com.mchange.v2.c3p0.impl.IdentityTokenizedCoalesceChecker;
/*     */ import com.mchange.v2.c3p0.management.ManagementCoordinator;
/*     */ import com.mchange.v2.c3p0.management.NullManagementCoordinator;
/*     */ import com.mchange.v2.coalesce.CoalesceChecker;
/*     */ import com.mchange.v2.coalesce.Coalescer;
/*     */ import com.mchange.v2.coalesce.CoalescerFactory;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import com.mchange.v2.sql.SqlUtils;
/*     */ import com.mchange.v2.util.DoubleWeakHashMap;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class C3P0Registry
/*     */ {
/*     */   private static final String MC_PARAM = "com.mchange.v2.c3p0.management.ManagementCoordinator";
/*  90 */   static final MLogger logger = MLog.getLogger(C3P0Registry.class);
/*     */ 
/*     */   
/*     */   static boolean banner_printed = false;
/*     */ 
/*     */   
/*     */   static boolean registry_mbean_registered = false;
/*     */ 
/*     */   
/*  99 */   private static CoalesceChecker CC = (CoalesceChecker)IdentityTokenizedCoalesceChecker.INSTANCE;
/*     */ 
/*     */ 
/*     */   
/* 103 */   private static Coalescer idtCoalescer = CoalescerFactory.createCoalescer(CC, true, false);
/*     */ 
/*     */   
/* 106 */   private static Map tokensToTokenized = (Map)new DoubleWeakHashMap();
/*     */ 
/*     */   
/* 109 */   private static HashSet unclosedPooledDataSources = new HashSet();
/*     */ 
/*     */   
/* 112 */   private static final Map classNamesToConnectionTesters = new HashMap<Object, Object>();
/*     */ 
/*     */   
/* 115 */   private static final Map classNamesToConnectionCustomizers = new HashMap<Object, Object>();
/*     */   
/*     */   private static ManagementCoordinator mc;
/*     */ 
/*     */   
/*     */   static {
/* 121 */     resetConnectionTesterCache();
/*     */     
/* 123 */     String userManagementCoordinator = C3P0ConfigUtils.getPropsFileConfigProperty("com.mchange.v2.c3p0.management.ManagementCoordinator");
/* 124 */     if (userManagementCoordinator != null) {
/*     */ 
/*     */       
/*     */       try {
/* 128 */         mc = (ManagementCoordinator)Class.forName(userManagementCoordinator).newInstance();
/*     */       }
/* 130 */       catch (Exception e) {
/*     */         
/* 132 */         if (logger.isLoggable(MLevel.WARNING)) {
/* 133 */           logger.log(MLevel.WARNING, "Could not instantiate user-specified ManagementCoordinator " + userManagementCoordinator + ". Using NullManagementCoordinator (c3p0 JMX management disabled!)", e);
/*     */         }
/*     */ 
/*     */         
/* 137 */         mc = (ManagementCoordinator)new NullManagementCoordinator();
/*     */       } 
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/* 144 */         Class.forName("java.lang.management.ManagementFactory");
/*     */         
/* 146 */         mc = (ManagementCoordinator)Class.forName("com.mchange.v2.c3p0.management.ActiveManagementCoordinator").newInstance();
/*     */       }
/* 148 */       catch (Exception e) {
/*     */         
/* 150 */         if (logger.isLoggable(MLevel.INFO)) {
/* 151 */           logger.log(MLevel.INFO, "jdk1.5 management interfaces unavailable... JMX support disabled.", e);
/*     */         }
/*     */         
/* 154 */         mc = (ManagementCoordinator)new NullManagementCoordinator();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void markConfigRefreshed() {
/* 161 */     resetConnectionTesterCache();
/*     */   }
/*     */   
/*     */   public static ConnectionTester getDefaultConnectionTester() {
/* 165 */     return getConnectionTester(C3P0Defaults.connectionTesterClassName());
/*     */   }
/*     */ 
/*     */   
/*     */   public static ConnectionTester getConnectionTester(String className) {
/*     */     try {
/* 171 */       synchronized (classNamesToConnectionTesters)
/*     */       {
/* 173 */         ConnectionTester out = (ConnectionTester)classNamesToConnectionTesters.get(className);
/* 174 */         if (out == null) {
/*     */           
/* 176 */           out = (ConnectionTester)Class.forName(className).newInstance();
/* 177 */           classNamesToConnectionTesters.put(className, out);
/*     */         } 
/* 179 */         return out;
/*     */       }
/*     */     
/* 182 */     } catch (Exception e) {
/*     */       
/* 184 */       if (logger.isLoggable(MLevel.WARNING)) {
/* 185 */         logger.log(MLevel.WARNING, "Could not create for find ConnectionTester with class name '" + className + "'. Using default.", e);
/*     */       }
/*     */ 
/*     */       
/* 189 */       return recreateDefaultConnectionTester();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static ConnectionTester recreateDefaultConnectionTester() {
/*     */     try {
/* 197 */       return (ConnectionTester)Class.forName(C3P0Defaults.connectionTesterClassName()).newInstance();
/* 198 */     } catch (Exception e) {
/* 199 */       throw new Error("Huh? We cannot instantiate the hard-coded, default ConnectionTester? We are very broken.", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void resetConnectionTesterCache() {
/* 204 */     synchronized (classNamesToConnectionTesters) {
/*     */       
/* 206 */       classNamesToConnectionTesters.clear();
/* 207 */       classNamesToConnectionTesters.put(C3P0Defaults.connectionTesterClassName(), recreateDefaultConnectionTester());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static ConnectionCustomizer getConnectionCustomizer(String className) throws SQLException {
/* 213 */     if (className == null || className.trim().equals("")) {
/* 214 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 219 */       synchronized (classNamesToConnectionCustomizers)
/*     */       {
/* 221 */         ConnectionCustomizer out = (ConnectionCustomizer)classNamesToConnectionCustomizers.get(className);
/* 222 */         if (out == null) {
/*     */           
/* 224 */           out = (ConnectionCustomizer)Class.forName(className).newInstance();
/* 225 */           classNamesToConnectionCustomizers.put(className, out);
/*     */         } 
/* 227 */         return out;
/*     */       }
/*     */     
/* 230 */     } catch (Exception e) {
/*     */       
/* 232 */       if (logger.isLoggable(MLevel.WARNING)) {
/* 233 */         logger.log(MLevel.WARNING, "Could not create for find ConnectionCustomizer with class name '" + className + "'.", e);
/*     */       }
/*     */ 
/*     */       
/* 237 */       throw SqlUtils.toSQLException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void banner() {
/* 245 */     if (!banner_printed) {
/*     */       
/* 247 */       if (logger.isLoggable(MLevel.INFO)) {
/* 248 */         logger.info("Initializing c3p0-0.9.5-pre7 [built 23-March-2014 06:21:47 -0700; debug? true; trace: 10]");
/*     */       }
/*     */ 
/*     */       
/* 252 */       banner_printed = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void attemptRegisterRegistryMBean() {
/* 259 */     if (!registry_mbean_registered) {
/*     */       
/* 261 */       mc.attemptManageC3P0Registry();
/* 262 */       registry_mbean_registered = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isIncorporated(IdentityTokenized idt) {
/* 268 */     return tokensToTokenized.keySet().contains(idt.getIdentityToken());
/*     */   }
/*     */ 
/*     */   
/*     */   private static void incorporate(IdentityTokenized idt) {
/* 273 */     tokensToTokenized.put(idt.getIdentityToken(), idt);
/* 274 */     if (idt instanceof PooledDataSource) {
/*     */       
/* 276 */       unclosedPooledDataSources.add(idt);
/* 277 */       mc.attemptManagePooledDataSource((PooledDataSource)idt);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized Map extensionsForToken(String pooledDataSourceIdentityToken) throws NoSuchElementException, IllegalArgumentException {
/* 284 */     Object o = tokensToTokenized.get(pooledDataSourceIdentityToken);
/* 285 */     if (o == null) throw new NoSuchElementException("No object is known to be identified by token '" + pooledDataSourceIdentityToken + "'. Either it is a bad token, or the object was no longer in use and has been garbage collected.");
/*     */ 
/*     */     
/* 288 */     if (!(o instanceof PooledDataSource)) {
/* 289 */       throw new IllegalArgumentException("The object '" + o + "', identified by token '" + pooledDataSourceIdentityToken + "', is not a PooledDataSource and therefore cannot have extensions.");
/*     */     }
/*     */     
/* 292 */     return ((PooledDataSource)o).getExtensions();
/*     */   }
/*     */ 
/*     */   
/*     */   public static synchronized IdentityTokenized reregister(IdentityTokenized idt) {
/* 297 */     if (idt instanceof PooledDataSource) {
/*     */       
/* 299 */       banner();
/* 300 */       attemptRegisterRegistryMBean();
/*     */     } 
/*     */     
/* 303 */     if (idt.getIdentityToken() == null) {
/* 304 */       throw new RuntimeException("[c3p0 issue] The identityToken of a registered object should be set prior to registration.");
/*     */     }
/* 306 */     IdentityTokenized coalesceCheck = (IdentityTokenized)idtCoalescer.coalesce(idt);
/*     */     
/* 308 */     if (!isIncorporated(coalesceCheck)) {
/* 309 */       incorporate(coalesceCheck);
/*     */     }
/* 311 */     return coalesceCheck;
/*     */   }
/*     */ 
/*     */   
/*     */   public static synchronized void markClosed(PooledDataSource pds) {
/* 316 */     unclosedPooledDataSources.remove(pds);
/* 317 */     mc.attemptUnmanagePooledDataSource(pds);
/* 318 */     if (unclosedPooledDataSources.isEmpty()) {
/*     */       
/* 320 */       mc.attemptUnmanageC3P0Registry();
/* 321 */       registry_mbean_registered = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static synchronized Set getPooledDataSources() {
/* 326 */     return (Set)unclosedPooledDataSources.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized Set pooledDataSourcesByName(String dataSourceName) {
/* 335 */     Set<PooledDataSource> out = new HashSet();
/* 336 */     for (Iterator<PooledDataSource> ii = unclosedPooledDataSources.iterator(); ii.hasNext(); ) {
/*     */       
/* 338 */       PooledDataSource pds = ii.next();
/* 339 */       if (pds.getDataSourceName().equals(dataSourceName))
/* 340 */         out.add(pds); 
/*     */     } 
/* 342 */     return out;
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
/*     */   public static synchronized PooledDataSource pooledDataSourceByName(String dataSourceName) {
/* 354 */     for (Iterator<PooledDataSource> ii = unclosedPooledDataSources.iterator(); ii.hasNext(); ) {
/*     */       
/* 356 */       PooledDataSource pds = ii.next();
/* 357 */       if (pds.getDataSourceName().equals(dataSourceName))
/* 358 */         return pds; 
/*     */     } 
/* 360 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static synchronized Set allIdentityTokens() {
/* 365 */     Set<?> out = Collections.unmodifiableSet(tokensToTokenized.keySet());
/*     */     
/* 367 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   public static synchronized Set allIdentityTokenized() {
/* 372 */     HashSet<?> out = new HashSet();
/* 373 */     out.addAll(tokensToTokenized.values());
/*     */     
/* 375 */     return Collections.unmodifiableSet(out);
/*     */   }
/*     */ 
/*     */   
/*     */   public static synchronized Set allPooledDataSources() {
/* 380 */     Set<?> out = Collections.unmodifiableSet(unclosedPooledDataSources);
/*     */     
/* 382 */     return out;
/*     */   }
/*     */   
/*     */   public static synchronized int getNumPooledDataSources() {
/* 386 */     return unclosedPooledDataSources.size();
/*     */   }
/*     */   
/*     */   public static synchronized int getNumPoolsAllDataSources() throws SQLException {
/* 390 */     int count = 0;
/* 391 */     for (Iterator<PooledDataSource> ii = unclosedPooledDataSources.iterator(); ii.hasNext(); ) {
/*     */       
/* 393 */       PooledDataSource pds = ii.next();
/* 394 */       count += pds.getNumUserPools();
/*     */     } 
/* 396 */     return count;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getNumThreadsAllThreadPools() throws SQLException {
/* 401 */     int count = 0;
/* 402 */     for (Iterator<PooledDataSource> ii = unclosedPooledDataSources.iterator(); ii.hasNext(); ) {
/*     */       
/* 404 */       PooledDataSource pds = ii.next();
/* 405 */       count += pds.getNumHelperThreads();
/*     */     } 
/* 407 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized Map getConfigExtensionsForPooledDataSource(String identityToken) throws SQLException {
/*     */     try {
/* 414 */       PooledDataSource pds = (PooledDataSource)tokensToTokenized.get(identityToken);
/* 415 */       if (pds == null)
/* 416 */         throw new SQLException("No DataSource or registered IdentityTokenized has identityToken '" + identityToken + "'."); 
/* 417 */       return pds.getExtensions();
/*     */     }
/* 419 */     catch (ClassCastException e) {
/*     */       
/* 421 */       if (logger.isLoggable(MLevel.WARNING)) {
/* 422 */         logger.log(MLevel.WARNING, "Tried to get config extensions for an entity that is not a PooledDataSource. (Extensions are available only on PooledDataSources.) Thowing SQLException.", e);
/*     */       }
/* 424 */       throw SqlUtils.toSQLException("Tried to get config extensions for an entity that is not a PooledDataSource. (Extensions are available only on PooledDataSources.)", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/C3P0Registry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */