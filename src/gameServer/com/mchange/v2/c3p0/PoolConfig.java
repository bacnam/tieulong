/*     */ package com.mchange.v2.c3p0;
/*     */ 
/*     */ import com.mchange.v2.c3p0.cfg.C3P0ConfigUtils;
/*     */ import com.mchange.v2.c3p0.impl.C3P0Defaults;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PoolConfig
/*     */ {
/* 104 */   static final MLogger logger = MLog.getLogger(PoolConfig.class); public static final String INITIAL_POOL_SIZE = "c3p0.initialPoolSize"; public static final String MIN_POOL_SIZE = "c3p0.minPoolSize"; public static final String MAX_POOL_SIZE = "c3p0.maxPoolSize"; public static final String IDLE_CONNECTION_TEST_PERIOD = "c3p0.idleConnectionTestPeriod"; public static final String MAX_IDLE_TIME = "c3p0.maxIdleTime"; public static final String PROPERTY_CYCLE = "c3p0.propertyCycle"; public static final String MAX_STATEMENTS = "c3p0.maxStatements"; public static final String MAX_STATEMENTS_PER_CONNECTION = "c3p0.maxStatementsPerConnection"; public static final String CHECKOUT_TIMEOUT = "c3p0.checkoutTimeout"; public static final String ACQUIRE_INCREMENT = "c3p0.acquireIncrement"; public static final String ACQUIRE_RETRY_ATTEMPTS = "c3p0.acquireRetryAttempts"; public static final String ACQUIRE_RETRY_DELAY = "c3p0.acquireRetryDelay"; public static final String BREAK_AFTER_ACQUIRE_FAILURE = "c3p0.breakAfterAcquireFailure"; public static final String USES_TRADITIONAL_REFLECTIVE_PROXIES = "c3p0.usesTraditionalReflectiveProxies"; public static final String TEST_CONNECTION_ON_CHECKOUT = "c3p0.testConnectionOnCheckout"; public static final String TEST_CONNECTION_ON_CHECKIN = "c3p0.testConnectionOnCheckin"; public static final String CONNECTION_TESTER_CLASS_NAME = "c3p0.connectionTesterClassName"; public static final String AUTOMATIC_TEST_TABLE = "c3p0.automaticTestTable"; public static final String AUTO_COMMIT_ON_CLOSE = "c3p0.autoCommitOnClose"; public static final String FORCE_IGNORE_UNRESOLVED_TRANSACTIONS = "c3p0.forceIgnoreUnresolvedTransactions"; public static final String NUM_HELPER_THREADS = "c3p0.numHelperThreads"; public static final String PREFERRED_TEST_QUERY = "c3p0.preferredTestQuery"; public static final String FACTORY_CLASS_LOCATION = "c3p0.factoryClassLocation"; static final PoolConfig DEFAULTS;
/*     */   static {
/* 106 */     Properties rsrcProps = C3P0ConfigUtils.findResourceProperties();
/* 107 */     PoolConfig rsrcDefaults = extractConfig(rsrcProps, null);
/*     */ 
/*     */     
/*     */     try {
/* 111 */       properties1 = System.getProperties();
/* 112 */     } catch (SecurityException e) {
/*     */       
/* 114 */       if (logger.isLoggable(MLevel.WARNING)) {
/* 115 */         logger.log(MLevel.WARNING, "Read of system Properties blocked -- ignoring any c3p0 configuration via System properties! (But any configuration via a c3p0.properties file is still okay!)", e);
/*     */       }
/*     */ 
/*     */       
/* 119 */       properties1 = new Properties();
/*     */     } 
/* 121 */     DEFAULTS = extractConfig(properties1, rsrcDefaults);
/*     */   } int maxStatements; int maxStatementsPerConnection; int initialPoolSize; int minPoolSize; int maxPoolSize; int idleConnectionTestPeriod; int maxIdleTime; int propertyCycle; int checkoutTimeout; int acquireIncrement; int acquireRetryAttempts; int acquireRetryDelay; boolean breakAfterAcquireFailure; boolean testConnectionOnCheckout; boolean testConnectionOnCheckin; boolean autoCommitOnClose; boolean forceIgnoreUnresolvedTransactions; boolean usesTraditionalReflectiveProxies; String connectionTesterClassName; String automaticTestTable; int numHelperThreads; String preferredTestQuery; String factoryClassLocation; static {
/*     */     Properties properties1;
/*     */   } public static int defaultNumHelperThreads() {
/* 125 */     return DEFAULTS.getNumHelperThreads();
/*     */   }
/*     */   public static String defaultPreferredTestQuery() {
/* 128 */     return DEFAULTS.getPreferredTestQuery();
/*     */   }
/*     */   public static String defaultFactoryClassLocation() {
/* 131 */     return DEFAULTS.getFactoryClassLocation();
/*     */   }
/*     */   public static int defaultMaxStatements() {
/* 134 */     return DEFAULTS.getMaxStatements();
/*     */   }
/*     */   public static int defaultMaxStatementsPerConnection() {
/* 137 */     return DEFAULTS.getMaxStatementsPerConnection();
/*     */   }
/*     */   public static int defaultInitialPoolSize() {
/* 140 */     return DEFAULTS.getInitialPoolSize();
/*     */   }
/*     */   public static int defaultMinPoolSize() {
/* 143 */     return DEFAULTS.getMinPoolSize();
/*     */   }
/*     */   public static int defaultMaxPoolSize() {
/* 146 */     return DEFAULTS.getMaxPoolSize();
/*     */   }
/*     */   public static int defaultIdleConnectionTestPeriod() {
/* 149 */     return DEFAULTS.getIdleConnectionTestPeriod();
/*     */   }
/*     */   public static int defaultMaxIdleTime() {
/* 152 */     return DEFAULTS.getMaxIdleTime();
/*     */   }
/*     */   public static int defaultPropertyCycle() {
/* 155 */     return DEFAULTS.getPropertyCycle();
/*     */   }
/*     */   public static int defaultCheckoutTimeout() {
/* 158 */     return DEFAULTS.getCheckoutTimeout();
/*     */   }
/*     */   public static int defaultAcquireIncrement() {
/* 161 */     return DEFAULTS.getAcquireIncrement();
/*     */   }
/*     */   public static int defaultAcquireRetryAttempts() {
/* 164 */     return DEFAULTS.getAcquireRetryAttempts();
/*     */   }
/*     */   public static int defaultAcquireRetryDelay() {
/* 167 */     return DEFAULTS.getAcquireRetryDelay();
/*     */   }
/*     */   public static boolean defaultBreakAfterAcquireFailure() {
/* 170 */     return DEFAULTS.isBreakAfterAcquireFailure();
/*     */   }
/*     */   public static String defaultConnectionTesterClassName() {
/* 173 */     return DEFAULTS.getConnectionTesterClassName();
/*     */   }
/*     */   public static String defaultAutomaticTestTable() {
/* 176 */     return DEFAULTS.getAutomaticTestTable();
/*     */   }
/*     */   public static boolean defaultTestConnectionOnCheckout() {
/* 179 */     return DEFAULTS.isTestConnectionOnCheckout();
/*     */   }
/*     */   public static boolean defaultTestConnectionOnCheckin() {
/* 182 */     return DEFAULTS.isTestConnectionOnCheckin();
/*     */   }
/*     */   public static boolean defaultAutoCommitOnClose() {
/* 185 */     return DEFAULTS.isAutoCommitOnClose();
/*     */   }
/*     */   public static boolean defaultForceIgnoreUnresolvedTransactions() {
/* 188 */     return DEFAULTS.isAutoCommitOnClose();
/*     */   }
/*     */   public static boolean defaultUsesTraditionalReflectiveProxies() {
/* 191 */     return DEFAULTS.isUsesTraditionalReflectiveProxies();
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
/*     */   private PoolConfig(Properties props, boolean init) throws NumberFormatException {
/* 220 */     if (init)
/* 221 */       extractConfig(this, props, DEFAULTS); 
/*     */   }
/*     */   
/*     */   public PoolConfig(Properties props) throws NumberFormatException {
/* 225 */     this(props, true);
/*     */   }
/*     */   public PoolConfig() throws NumberFormatException {
/* 228 */     this(null, true);
/*     */   }
/*     */   public int getNumHelperThreads() {
/* 231 */     return this.numHelperThreads;
/*     */   }
/*     */   public String getPreferredTestQuery() {
/* 234 */     return this.preferredTestQuery;
/*     */   }
/*     */   public String getFactoryClassLocation() {
/* 237 */     return this.factoryClassLocation;
/*     */   }
/*     */   public int getMaxStatements() {
/* 240 */     return this.maxStatements;
/*     */   }
/*     */   public int getMaxStatementsPerConnection() {
/* 243 */     return this.maxStatementsPerConnection;
/*     */   }
/*     */   public int getInitialPoolSize() {
/* 246 */     return this.initialPoolSize;
/*     */   }
/*     */   public int getMinPoolSize() {
/* 249 */     return this.minPoolSize;
/*     */   }
/*     */   public int getMaxPoolSize() {
/* 252 */     return this.maxPoolSize;
/*     */   }
/*     */   public int getIdleConnectionTestPeriod() {
/* 255 */     return this.idleConnectionTestPeriod;
/*     */   }
/*     */   public int getMaxIdleTime() {
/* 258 */     return this.maxIdleTime;
/*     */   }
/*     */   public int getPropertyCycle() {
/* 261 */     return this.propertyCycle;
/*     */   }
/*     */   public int getAcquireIncrement() {
/* 264 */     return this.acquireIncrement;
/*     */   }
/*     */   public int getCheckoutTimeout() {
/* 267 */     return this.checkoutTimeout;
/*     */   }
/*     */   public int getAcquireRetryAttempts() {
/* 270 */     return this.acquireRetryAttempts;
/*     */   }
/*     */   public int getAcquireRetryDelay() {
/* 273 */     return this.acquireRetryDelay;
/*     */   }
/*     */   public boolean isBreakAfterAcquireFailure() {
/* 276 */     return this.breakAfterAcquireFailure;
/*     */   }
/*     */   public boolean isUsesTraditionalReflectiveProxies() {
/* 279 */     return this.usesTraditionalReflectiveProxies;
/*     */   }
/*     */   public String getConnectionTesterClassName() {
/* 282 */     return this.connectionTesterClassName;
/*     */   }
/*     */   public String getAutomaticTestTable() {
/* 285 */     return this.automaticTestTable;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getTestConnectionOnCheckout() {
/* 291 */     return this.testConnectionOnCheckout;
/*     */   }
/*     */   public boolean isTestConnectionOnCheckout() {
/* 294 */     return getTestConnectionOnCheckout();
/*     */   }
/*     */   public boolean isTestConnectionOnCheckin() {
/* 297 */     return this.testConnectionOnCheckin;
/*     */   }
/*     */   public boolean isAutoCommitOnClose() {
/* 300 */     return this.autoCommitOnClose;
/*     */   }
/*     */   public boolean isForceIgnoreUnresolvedTransactions() {
/* 303 */     return this.forceIgnoreUnresolvedTransactions;
/*     */   }
/*     */   public void setNumHelperThreads(int numHelperThreads) {
/* 306 */     this.numHelperThreads = numHelperThreads;
/*     */   }
/*     */   public void setPreferredTestQuery(String preferredTestQuery) {
/* 309 */     this.preferredTestQuery = preferredTestQuery;
/*     */   }
/*     */   public void setFactoryClassLocation(String factoryClassLocation) {
/* 312 */     this.factoryClassLocation = factoryClassLocation;
/*     */   }
/*     */   public void setMaxStatements(int maxStatements) {
/* 315 */     this.maxStatements = maxStatements;
/*     */   }
/*     */   public void setMaxStatementsPerConnection(int maxStatementsPerConnection) {
/* 318 */     this.maxStatementsPerConnection = maxStatementsPerConnection;
/*     */   }
/*     */   public void setInitialPoolSize(int initialPoolSize) {
/* 321 */     this.initialPoolSize = initialPoolSize;
/*     */   }
/*     */   public void setMinPoolSize(int minPoolSize) {
/* 324 */     this.minPoolSize = minPoolSize;
/*     */   }
/*     */   public void setMaxPoolSize(int maxPoolSize) {
/* 327 */     this.maxPoolSize = maxPoolSize;
/*     */   }
/*     */   public void setIdleConnectionTestPeriod(int idleConnectionTestPeriod) {
/* 330 */     this.idleConnectionTestPeriod = idleConnectionTestPeriod;
/*     */   }
/*     */   public void setMaxIdleTime(int maxIdleTime) {
/* 333 */     this.maxIdleTime = maxIdleTime;
/*     */   }
/*     */   public void setPropertyCycle(int propertyCycle) {
/* 336 */     this.propertyCycle = propertyCycle;
/*     */   }
/*     */   public void setCheckoutTimeout(int checkoutTimeout) {
/* 339 */     this.checkoutTimeout = checkoutTimeout;
/*     */   }
/*     */   public void setAcquireIncrement(int acquireIncrement) {
/* 342 */     this.acquireIncrement = acquireIncrement;
/*     */   }
/*     */   public void setAcquireRetryAttempts(int acquireRetryAttempts) {
/* 345 */     this.acquireRetryAttempts = acquireRetryAttempts;
/*     */   }
/*     */   public void setAcquireRetryDelay(int acquireRetryDelay) {
/* 348 */     this.acquireRetryDelay = acquireRetryDelay;
/*     */   }
/*     */   public void setConnectionTesterClassName(String connectionTesterClassName) {
/* 351 */     this.connectionTesterClassName = connectionTesterClassName;
/*     */   }
/*     */   public void setAutomaticTestTable(String automaticTestTable) {
/* 354 */     this.automaticTestTable = automaticTestTable;
/*     */   }
/*     */   public void setBreakAfterAcquireFailure(boolean breakAfterAcquireFailure) {
/* 357 */     this.breakAfterAcquireFailure = breakAfterAcquireFailure;
/*     */   }
/*     */   public void setUsesTraditionalReflectiveProxies(boolean usesTraditionalReflectiveProxies) {
/* 360 */     this.usesTraditionalReflectiveProxies = usesTraditionalReflectiveProxies;
/*     */   }
/*     */   public void setTestConnectionOnCheckout(boolean testConnectionOnCheckout) {
/* 363 */     this.testConnectionOnCheckout = testConnectionOnCheckout;
/*     */   }
/*     */   public void setTestConnectionOnCheckin(boolean testConnectionOnCheckin) {
/* 366 */     this.testConnectionOnCheckin = testConnectionOnCheckin;
/*     */   }
/*     */   public void setAutoCommitOnClose(boolean autoCommitOnClose) {
/* 369 */     this.autoCommitOnClose = autoCommitOnClose;
/*     */   }
/*     */   public void setForceIgnoreUnresolvedTransactions(boolean forceIgnoreUnresolvedTransactions) {
/* 372 */     this.forceIgnoreUnresolvedTransactions = forceIgnoreUnresolvedTransactions;
/*     */   }
/*     */   
/*     */   private static PoolConfig extractConfig(Properties props, PoolConfig defaults) throws NumberFormatException {
/* 376 */     PoolConfig pcfg = new PoolConfig(null, false);
/* 377 */     extractConfig(pcfg, props, defaults);
/* 378 */     return pcfg;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void extractConfig(PoolConfig pcfg, Properties props, PoolConfig defaults) throws NumberFormatException {
/* 383 */     String maxStatementsStr = null;
/* 384 */     String maxStatementsPerConnectionStr = null;
/* 385 */     String initialPoolSizeStr = null;
/* 386 */     String minPoolSizeStr = null;
/* 387 */     String maxPoolSizeStr = null;
/* 388 */     String idleConnectionTestPeriodStr = null;
/* 389 */     String maxIdleTimeStr = null;
/* 390 */     String propertyCycleStr = null;
/* 391 */     String checkoutTimeoutStr = null;
/* 392 */     String acquireIncrementStr = null;
/* 393 */     String acquireRetryAttemptsStr = null;
/* 394 */     String acquireRetryDelayStr = null;
/* 395 */     String breakAfterAcquireFailureStr = null;
/* 396 */     String usesTraditionalReflectiveProxiesStr = null;
/* 397 */     String testConnectionOnCheckoutStr = null;
/* 398 */     String testConnectionOnCheckinStr = null;
/* 399 */     String autoCommitOnCloseStr = null;
/* 400 */     String forceIgnoreUnresolvedTransactionsStr = null;
/* 401 */     String connectionTesterClassName = null;
/* 402 */     String automaticTestTable = null;
/* 403 */     String numHelperThreadsStr = null;
/* 404 */     String preferredTestQuery = null;
/* 405 */     String factoryClassLocation = null;
/*     */     
/* 407 */     if (props != null) {
/*     */       
/* 409 */       maxStatementsStr = props.getProperty("c3p0.maxStatements");
/* 410 */       maxStatementsPerConnectionStr = props.getProperty("c3p0.maxStatementsPerConnection");
/* 411 */       initialPoolSizeStr = props.getProperty("c3p0.initialPoolSize");
/* 412 */       minPoolSizeStr = props.getProperty("c3p0.minPoolSize");
/* 413 */       maxPoolSizeStr = props.getProperty("c3p0.maxPoolSize");
/* 414 */       idleConnectionTestPeriodStr = props.getProperty("c3p0.idleConnectionTestPeriod");
/* 415 */       maxIdleTimeStr = props.getProperty("c3p0.maxIdleTime");
/* 416 */       propertyCycleStr = props.getProperty("c3p0.propertyCycle");
/* 417 */       checkoutTimeoutStr = props.getProperty("c3p0.checkoutTimeout");
/* 418 */       acquireIncrementStr = props.getProperty("c3p0.acquireIncrement");
/* 419 */       acquireRetryAttemptsStr = props.getProperty("c3p0.acquireRetryAttempts");
/* 420 */       acquireRetryDelayStr = props.getProperty("c3p0.acquireRetryDelay");
/* 421 */       breakAfterAcquireFailureStr = props.getProperty("c3p0.breakAfterAcquireFailure");
/* 422 */       usesTraditionalReflectiveProxiesStr = props.getProperty("c3p0.usesTraditionalReflectiveProxies");
/* 423 */       testConnectionOnCheckoutStr = props.getProperty("c3p0.testConnectionOnCheckout");
/* 424 */       testConnectionOnCheckinStr = props.getProperty("c3p0.testConnectionOnCheckin");
/* 425 */       autoCommitOnCloseStr = props.getProperty("c3p0.autoCommitOnClose");
/* 426 */       forceIgnoreUnresolvedTransactionsStr = props.getProperty("c3p0.forceIgnoreUnresolvedTransactions");
/* 427 */       connectionTesterClassName = props.getProperty("c3p0.connectionTesterClassName");
/* 428 */       automaticTestTable = props.getProperty("c3p0.automaticTestTable");
/* 429 */       numHelperThreadsStr = props.getProperty("c3p0.numHelperThreads");
/* 430 */       preferredTestQuery = props.getProperty("c3p0.preferredTestQuery");
/* 431 */       factoryClassLocation = props.getProperty("c3p0.factoryClassLocation");
/*     */     } 
/*     */ 
/*     */     
/* 435 */     if (maxStatementsStr != null) {
/* 436 */       pcfg.setMaxStatements(Integer.parseInt(maxStatementsStr.trim()));
/* 437 */     } else if (defaults != null) {
/* 438 */       pcfg.setMaxStatements(defaults.getMaxStatements());
/*     */     } else {
/* 440 */       pcfg.setMaxStatements(C3P0Defaults.maxStatements());
/*     */     } 
/*     */     
/* 443 */     if (maxStatementsPerConnectionStr != null) {
/* 444 */       pcfg.setMaxStatementsPerConnection(Integer.parseInt(maxStatementsPerConnectionStr.trim()));
/* 445 */     } else if (defaults != null) {
/* 446 */       pcfg.setMaxStatementsPerConnection(defaults.getMaxStatementsPerConnection());
/*     */     } else {
/* 448 */       pcfg.setMaxStatementsPerConnection(C3P0Defaults.maxStatementsPerConnection());
/*     */     } 
/*     */     
/* 451 */     if (initialPoolSizeStr != null) {
/* 452 */       pcfg.setInitialPoolSize(Integer.parseInt(initialPoolSizeStr.trim()));
/* 453 */     } else if (defaults != null) {
/* 454 */       pcfg.setInitialPoolSize(defaults.getInitialPoolSize());
/*     */     } else {
/* 456 */       pcfg.setInitialPoolSize(C3P0Defaults.initialPoolSize());
/*     */     } 
/*     */     
/* 459 */     if (minPoolSizeStr != null) {
/* 460 */       pcfg.setMinPoolSize(Integer.parseInt(minPoolSizeStr.trim()));
/* 461 */     } else if (defaults != null) {
/* 462 */       pcfg.setMinPoolSize(defaults.getMinPoolSize());
/*     */     } else {
/* 464 */       pcfg.setMinPoolSize(C3P0Defaults.minPoolSize());
/*     */     } 
/*     */     
/* 467 */     if (maxPoolSizeStr != null) {
/* 468 */       pcfg.setMaxPoolSize(Integer.parseInt(maxPoolSizeStr.trim()));
/* 469 */     } else if (defaults != null) {
/* 470 */       pcfg.setMaxPoolSize(defaults.getMaxPoolSize());
/*     */     } else {
/* 472 */       pcfg.setMaxPoolSize(C3P0Defaults.maxPoolSize());
/*     */     } 
/*     */     
/* 475 */     if (idleConnectionTestPeriodStr != null) {
/* 476 */       pcfg.setIdleConnectionTestPeriod(Integer.parseInt(idleConnectionTestPeriodStr.trim()));
/* 477 */     } else if (defaults != null) {
/* 478 */       pcfg.setIdleConnectionTestPeriod(defaults.getIdleConnectionTestPeriod());
/*     */     } else {
/* 480 */       pcfg.setIdleConnectionTestPeriod(C3P0Defaults.idleConnectionTestPeriod());
/*     */     } 
/*     */     
/* 483 */     if (maxIdleTimeStr != null) {
/* 484 */       pcfg.setMaxIdleTime(Integer.parseInt(maxIdleTimeStr.trim()));
/* 485 */     } else if (defaults != null) {
/* 486 */       pcfg.setMaxIdleTime(defaults.getMaxIdleTime());
/*     */     } else {
/* 488 */       pcfg.setMaxIdleTime(C3P0Defaults.maxIdleTime());
/*     */     } 
/*     */     
/* 491 */     if (propertyCycleStr != null) {
/* 492 */       pcfg.setPropertyCycle(Integer.parseInt(propertyCycleStr.trim()));
/* 493 */     } else if (defaults != null) {
/* 494 */       pcfg.setPropertyCycle(defaults.getPropertyCycle());
/*     */     } else {
/* 496 */       pcfg.setPropertyCycle(C3P0Defaults.propertyCycle());
/*     */     } 
/*     */     
/* 499 */     if (checkoutTimeoutStr != null) {
/* 500 */       pcfg.setCheckoutTimeout(Integer.parseInt(checkoutTimeoutStr.trim()));
/* 501 */     } else if (defaults != null) {
/* 502 */       pcfg.setCheckoutTimeout(defaults.getCheckoutTimeout());
/*     */     } else {
/* 504 */       pcfg.setCheckoutTimeout(C3P0Defaults.checkoutTimeout());
/*     */     } 
/*     */     
/* 507 */     if (acquireIncrementStr != null) {
/* 508 */       pcfg.setAcquireIncrement(Integer.parseInt(acquireIncrementStr.trim()));
/* 509 */     } else if (defaults != null) {
/* 510 */       pcfg.setAcquireIncrement(defaults.getAcquireIncrement());
/*     */     } else {
/* 512 */       pcfg.setAcquireIncrement(C3P0Defaults.acquireIncrement());
/*     */     } 
/*     */     
/* 515 */     if (acquireRetryAttemptsStr != null) {
/* 516 */       pcfg.setAcquireRetryAttempts(Integer.parseInt(acquireRetryAttemptsStr.trim()));
/* 517 */     } else if (defaults != null) {
/* 518 */       pcfg.setAcquireRetryAttempts(defaults.getAcquireRetryAttempts());
/*     */     } else {
/* 520 */       pcfg.setAcquireRetryAttempts(C3P0Defaults.acquireRetryAttempts());
/*     */     } 
/*     */     
/* 523 */     if (acquireRetryDelayStr != null) {
/* 524 */       pcfg.setAcquireRetryDelay(Integer.parseInt(acquireRetryDelayStr.trim()));
/* 525 */     } else if (defaults != null) {
/* 526 */       pcfg.setAcquireRetryDelay(defaults.getAcquireRetryDelay());
/*     */     } else {
/* 528 */       pcfg.setAcquireRetryDelay(C3P0Defaults.acquireRetryDelay());
/*     */     } 
/*     */     
/* 531 */     if (breakAfterAcquireFailureStr != null) {
/* 532 */       pcfg.setBreakAfterAcquireFailure(Boolean.valueOf(breakAfterAcquireFailureStr.trim()).booleanValue());
/* 533 */     } else if (defaults != null) {
/* 534 */       pcfg.setBreakAfterAcquireFailure(defaults.isBreakAfterAcquireFailure());
/*     */     } else {
/* 536 */       pcfg.setBreakAfterAcquireFailure(C3P0Defaults.breakAfterAcquireFailure());
/*     */     } 
/*     */     
/* 539 */     if (usesTraditionalReflectiveProxiesStr != null) {
/* 540 */       pcfg.setUsesTraditionalReflectiveProxies(Boolean.valueOf(usesTraditionalReflectiveProxiesStr.trim()).booleanValue());
/* 541 */     } else if (defaults != null) {
/* 542 */       pcfg.setUsesTraditionalReflectiveProxies(defaults.isUsesTraditionalReflectiveProxies());
/*     */     } else {
/* 544 */       pcfg.setUsesTraditionalReflectiveProxies(C3P0Defaults.usesTraditionalReflectiveProxies());
/*     */     } 
/*     */     
/* 547 */     if (testConnectionOnCheckoutStr != null) {
/* 548 */       pcfg.setTestConnectionOnCheckout(Boolean.valueOf(testConnectionOnCheckoutStr.trim()).booleanValue());
/* 549 */     } else if (defaults != null) {
/* 550 */       pcfg.setTestConnectionOnCheckout(defaults.isTestConnectionOnCheckout());
/*     */     } else {
/* 552 */       pcfg.setTestConnectionOnCheckout(C3P0Defaults.testConnectionOnCheckout());
/*     */     } 
/*     */     
/* 555 */     if (testConnectionOnCheckinStr != null) {
/* 556 */       pcfg.setTestConnectionOnCheckin(Boolean.valueOf(testConnectionOnCheckinStr.trim()).booleanValue());
/* 557 */     } else if (defaults != null) {
/* 558 */       pcfg.setTestConnectionOnCheckin(defaults.isTestConnectionOnCheckin());
/*     */     } else {
/* 560 */       pcfg.setTestConnectionOnCheckin(C3P0Defaults.testConnectionOnCheckin());
/*     */     } 
/*     */     
/* 563 */     if (autoCommitOnCloseStr != null) {
/* 564 */       pcfg.setAutoCommitOnClose(Boolean.valueOf(autoCommitOnCloseStr.trim()).booleanValue());
/* 565 */     } else if (defaults != null) {
/* 566 */       pcfg.setAutoCommitOnClose(defaults.isAutoCommitOnClose());
/*     */     } else {
/* 568 */       pcfg.setAutoCommitOnClose(C3P0Defaults.autoCommitOnClose());
/*     */     } 
/*     */     
/* 571 */     if (forceIgnoreUnresolvedTransactionsStr != null) {
/* 572 */       pcfg.setForceIgnoreUnresolvedTransactions(Boolean.valueOf(forceIgnoreUnresolvedTransactionsStr.trim()).booleanValue());
/* 573 */     } else if (defaults != null) {
/* 574 */       pcfg.setForceIgnoreUnresolvedTransactions(defaults.isForceIgnoreUnresolvedTransactions());
/*     */     } else {
/* 576 */       pcfg.setForceIgnoreUnresolvedTransactions(C3P0Defaults.forceIgnoreUnresolvedTransactions());
/*     */     } 
/*     */     
/* 579 */     if (connectionTesterClassName != null) {
/* 580 */       pcfg.setConnectionTesterClassName(connectionTesterClassName.trim());
/* 581 */     } else if (defaults != null) {
/* 582 */       pcfg.setConnectionTesterClassName(defaults.getConnectionTesterClassName());
/*     */     } else {
/* 584 */       pcfg.setConnectionTesterClassName(C3P0Defaults.connectionTesterClassName());
/*     */     } 
/*     */     
/* 587 */     if (automaticTestTable != null) {
/* 588 */       pcfg.setAutomaticTestTable(automaticTestTable.trim());
/* 589 */     } else if (defaults != null) {
/* 590 */       pcfg.setAutomaticTestTable(defaults.getAutomaticTestTable());
/*     */     } else {
/* 592 */       pcfg.setAutomaticTestTable(C3P0Defaults.automaticTestTable());
/*     */     } 
/*     */     
/* 595 */     if (numHelperThreadsStr != null) {
/* 596 */       pcfg.setNumHelperThreads(Integer.parseInt(numHelperThreadsStr.trim()));
/* 597 */     } else if (defaults != null) {
/* 598 */       pcfg.setNumHelperThreads(defaults.getNumHelperThreads());
/*     */     } else {
/* 600 */       pcfg.setNumHelperThreads(C3P0Defaults.numHelperThreads());
/*     */     } 
/*     */     
/* 603 */     if (preferredTestQuery != null) {
/* 604 */       pcfg.setPreferredTestQuery(preferredTestQuery.trim());
/* 605 */     } else if (defaults != null) {
/* 606 */       pcfg.setPreferredTestQuery(defaults.getPreferredTestQuery());
/*     */     } else {
/* 608 */       pcfg.setPreferredTestQuery(C3P0Defaults.preferredTestQuery());
/*     */     } 
/*     */     
/* 611 */     if (factoryClassLocation != null) {
/* 612 */       pcfg.setFactoryClassLocation(factoryClassLocation.trim());
/* 613 */     } else if (defaults != null) {
/* 614 */       pcfg.setFactoryClassLocation(defaults.getFactoryClassLocation());
/*     */     } else {
/* 616 */       pcfg.setFactoryClassLocation(C3P0Defaults.factoryClassLocation());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/PoolConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */