/*     */ package com.mchange.v2.c3p0.impl;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
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
/*     */ public final class C3P0Defaults
/*     */ {
/*     */   private static final int MAX_STATEMENTS = 0;
/*     */   private static final int MAX_STATEMENTS_PER_CONNECTION = 0;
/*     */   private static final int INITIAL_POOL_SIZE = 3;
/*     */   private static final int MIN_POOL_SIZE = 3;
/*     */   private static final int MAX_POOL_SIZE = 15;
/*     */   private static final int IDLE_CONNECTION_TEST_PERIOD = 0;
/*     */   private static final int MAX_IDLE_TIME = 0;
/*     */   private static final int PROPERTY_CYCLE = 0;
/*     */   private static final int ACQUIRE_INCREMENT = 3;
/*     */   private static final int ACQUIRE_RETRY_ATTEMPTS = 30;
/*     */   private static final int ACQUIRE_RETRY_DELAY = 1000;
/*     */   private static final int CHECKOUT_TIMEOUT = 0;
/*     */   private static final int MAX_ADMINISTRATIVE_TASK_TIME = 0;
/*     */   private static final int MAX_IDLE_TIME_EXCESS_CONNECTIONS = 0;
/*     */   private static final int MAX_CONNECTION_AGE = 0;
/*     */   private static final int UNRETURNED_CONNECTION_TIMEOUT = 0;
/*     */   private static final int STATEMENT_CACHE_NUM_DEFERRED_CLOSE_THREADS = 0;
/*     */   private static final boolean BREAK_AFTER_ACQUIRE_FAILURE = false;
/*     */   private static final boolean TEST_CONNECTION_ON_CHECKOUT = false;
/*     */   private static final boolean TEST_CONNECTION_ON_CHECKIN = false;
/*     */   private static final boolean AUTO_COMMIT_ON_CLOSE = false;
/*     */   private static final boolean FORCE_IGNORE_UNRESOLVED_TXNS = false;
/*     */   private static final boolean USES_TRADITIONAL_REFLECTIVE_PROXIES = false;
/*     */   private static final boolean DEBUG_UNRETURNED_CONNECTION_STACK_TRACES = false;
/*     */   private static final boolean PRIVILEGE_SPAWNED_THREADS = false;
/*     */   private static final boolean FORCE_USE_NAMED_DRIVER_CLASS = false;
/*     */   private static final int NUM_HELPER_THREADS = 3;
/*  77 */   private static final String AUTOMATIC_TEST_TABLE = null;
/*  78 */   private static final String CONNECTION_CUSTOMIZER_CLASS_NAME = null;
/*     */   private static final String CONNECTION_TESTER_CLASS_NAME = "com.mchange.v2.c3p0.impl.DefaultConnectionTester";
/*     */   private static final String CONTEXT_CLASS_LOADER_SOURCE = "caller";
/*  81 */   private static final String DRIVER_CLASS = null;
/*  82 */   private static final String JDBC_URL = null;
/*  83 */   private static final String OVERRIDE_DEFAULT_USER = null;
/*  84 */   private static final String OVERRIDE_DEFAULT_PASSWORD = null;
/*  85 */   private static final String PASSWORD = null;
/*  86 */   private static final String PREFERRED_TEST_QUERY = null;
/*  87 */   private static final String FACTORY_CLASS_LOCATION = null;
/*  88 */   private static final String USER_OVERRIDES_AS_STRING = null;
/*  89 */   private static final String USER = null;
/*     */   
/*  91 */   private static final String DATA_SOURCE_NAME = null;
/*     */   
/*  93 */   private static final Map EXTENSIONS = Collections.emptyMap();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Set KNOWN_PROPERTIES;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 105 */     Method[] methods = C3P0Defaults.class.getMethods();
/* 106 */     Set<String> s = new HashSet();
/* 107 */     for (int i = 0, len = methods.length; i < len; i++) {
/*     */       
/* 109 */       Method m = methods[i];
/* 110 */       if (Modifier.isStatic(m.getModifiers()) && (m.getParameterTypes()).length == 0)
/* 111 */         s.add(m.getName()); 
/*     */     } 
/* 113 */     KNOWN_PROPERTIES = Collections.unmodifiableSet(s);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Set getKnownProperties(Object useless) {
/* 119 */     return KNOWN_PROPERTIES;
/*     */   }
/*     */   public static boolean isKnownProperty(String s) {
/* 122 */     return KNOWN_PROPERTIES.contains(s);
/*     */   }
/*     */   public static int maxStatements() {
/* 125 */     return 0;
/*     */   }
/*     */   public static int maxStatementsPerConnection() {
/* 128 */     return 0;
/*     */   }
/*     */   public static int initialPoolSize() {
/* 131 */     return 3;
/*     */   }
/*     */   public static int minPoolSize() {
/* 134 */     return 3;
/*     */   }
/*     */   public static int maxPoolSize() {
/* 137 */     return 15;
/*     */   }
/*     */   public static int idleConnectionTestPeriod() {
/* 140 */     return 0;
/*     */   }
/*     */   public static int maxIdleTime() {
/* 143 */     return 0;
/*     */   }
/*     */   public static int unreturnedConnectionTimeout() {
/* 146 */     return 0;
/*     */   }
/*     */   public static int propertyCycle() {
/* 149 */     return 0;
/*     */   }
/*     */   public static int acquireIncrement() {
/* 152 */     return 3;
/*     */   }
/*     */   public static int acquireRetryAttempts() {
/* 155 */     return 30;
/*     */   }
/*     */   public static int acquireRetryDelay() {
/* 158 */     return 1000;
/*     */   }
/*     */   public static int checkoutTimeout() {
/* 161 */     return 0;
/*     */   }
/*     */   public static int statementCacheNumDeferredCloseThreads() {
/* 164 */     return 0;
/*     */   }
/*     */   public static String connectionCustomizerClassName() {
/* 167 */     return CONNECTION_CUSTOMIZER_CLASS_NAME;
/*     */   }
/*     */   public static String contextClassLoaderSource() {
/* 170 */     return "caller";
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
/*     */   public static String connectionTesterClassName() {
/* 183 */     return "com.mchange.v2.c3p0.impl.DefaultConnectionTester";
/*     */   }
/*     */   public static String automaticTestTable() {
/* 186 */     return AUTOMATIC_TEST_TABLE;
/*     */   }
/*     */   public static String driverClass() {
/* 189 */     return DRIVER_CLASS;
/*     */   }
/*     */   public static boolean forceUseNamedDriverClass() {
/* 192 */     return false;
/*     */   }
/*     */   public static String jdbcUrl() {
/* 195 */     return JDBC_URL;
/*     */   }
/*     */   public static int numHelperThreads() {
/* 198 */     return 3;
/*     */   }
/*     */   public static boolean breakAfterAcquireFailure() {
/* 201 */     return false;
/*     */   }
/*     */   public static boolean testConnectionOnCheckout() {
/* 204 */     return false;
/*     */   }
/*     */   public static boolean testConnectionOnCheckin() {
/* 207 */     return false;
/*     */   }
/*     */   public static boolean autoCommitOnClose() {
/* 210 */     return false;
/*     */   }
/*     */   public static boolean forceIgnoreUnresolvedTransactions() {
/* 213 */     return false;
/*     */   }
/*     */   public static boolean debugUnreturnedConnectionStackTraces() {
/* 216 */     return false;
/*     */   }
/*     */   public static boolean usesTraditionalReflectiveProxies() {
/* 219 */     return false;
/*     */   }
/*     */   public static boolean privilegeSpawnedThreads() {
/* 222 */     return false;
/*     */   }
/*     */   public static String preferredTestQuery() {
/* 225 */     return PREFERRED_TEST_QUERY;
/*     */   }
/*     */   public static String userOverridesAsString() {
/* 228 */     return USER_OVERRIDES_AS_STRING;
/*     */   }
/*     */   public static String factoryClassLocation() {
/* 231 */     return FACTORY_CLASS_LOCATION;
/*     */   }
/*     */   public static String overrideDefaultUser() {
/* 234 */     return OVERRIDE_DEFAULT_USER;
/*     */   }
/*     */   public static String overrideDefaultPassword() {
/* 237 */     return OVERRIDE_DEFAULT_PASSWORD;
/*     */   }
/*     */   public static String user() {
/* 240 */     return USER;
/*     */   }
/*     */   public static String password() {
/* 243 */     return PASSWORD;
/*     */   }
/*     */   public static int maxAdministrativeTaskTime() {
/* 246 */     return 0;
/*     */   }
/*     */   public static int maxIdleTimeExcessConnections() {
/* 249 */     return 0;
/*     */   }
/*     */   public static int maxConnectionAge() {
/* 252 */     return 0;
/*     */   }
/*     */   public static String dataSourceName() {
/* 255 */     return DATA_SOURCE_NAME;
/*     */   }
/*     */   public static Map extensions() {
/* 258 */     return EXTENSIONS;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/C3P0Defaults.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */