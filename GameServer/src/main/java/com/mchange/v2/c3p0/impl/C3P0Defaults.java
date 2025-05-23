package com.mchange.v2.c3p0.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class C3P0Defaults
{
private static final int MAX_STATEMENTS = 0;
private static final int MAX_STATEMENTS_PER_CONNECTION = 0;
private static final int INITIAL_POOL_SIZE = 3;
private static final int MIN_POOL_SIZE = 3;
private static final int MAX_POOL_SIZE = 15;
private static final int IDLE_CONNECTION_TEST_PERIOD = 0;
private static final int MAX_IDLE_TIME = 0;
private static final int PROPERTY_CYCLE = 0;
private static final int ACQUIRE_INCREMENT = 3;
private static final int ACQUIRE_RETRY_ATTEMPTS = 30;
private static final int ACQUIRE_RETRY_DELAY = 1000;
private static final int CHECKOUT_TIMEOUT = 0;
private static final int MAX_ADMINISTRATIVE_TASK_TIME = 0;
private static final int MAX_IDLE_TIME_EXCESS_CONNECTIONS = 0;
private static final int MAX_CONNECTION_AGE = 0;
private static final int UNRETURNED_CONNECTION_TIMEOUT = 0;
private static final int STATEMENT_CACHE_NUM_DEFERRED_CLOSE_THREADS = 0;
private static final boolean BREAK_AFTER_ACQUIRE_FAILURE = false;
private static final boolean TEST_CONNECTION_ON_CHECKOUT = false;
private static final boolean TEST_CONNECTION_ON_CHECKIN = false;
private static final boolean AUTO_COMMIT_ON_CLOSE = false;
private static final boolean FORCE_IGNORE_UNRESOLVED_TXNS = false;
private static final boolean USES_TRADITIONAL_REFLECTIVE_PROXIES = false;
private static final boolean DEBUG_UNRETURNED_CONNECTION_STACK_TRACES = false;
private static final boolean PRIVILEGE_SPAWNED_THREADS = false;
private static final boolean FORCE_USE_NAMED_DRIVER_CLASS = false;
private static final int NUM_HELPER_THREADS = 3;
private static final String AUTOMATIC_TEST_TABLE = null;
private static final String CONNECTION_CUSTOMIZER_CLASS_NAME = null;
private static final String CONNECTION_TESTER_CLASS_NAME = "com.mchange.v2.c3p0.impl.DefaultConnectionTester";
private static final String CONTEXT_CLASS_LOADER_SOURCE = "caller";
private static final String DRIVER_CLASS = null;
private static final String JDBC_URL = null;
private static final String OVERRIDE_DEFAULT_USER = null;
private static final String OVERRIDE_DEFAULT_PASSWORD = null;
private static final String PASSWORD = null;
private static final String PREFERRED_TEST_QUERY = null;
private static final String FACTORY_CLASS_LOCATION = null;
private static final String USER_OVERRIDES_AS_STRING = null;
private static final String USER = null;

private static final String DATA_SOURCE_NAME = null;

private static final Map EXTENSIONS = Collections.emptyMap();

private static final Set KNOWN_PROPERTIES;

static {
Method[] methods = C3P0Defaults.class.getMethods();
Set<String> s = new HashSet();
for (int i = 0, len = methods.length; i < len; i++) {

Method m = methods[i];
if (Modifier.isStatic(m.getModifiers()) && (m.getParameterTypes()).length == 0)
s.add(m.getName()); 
} 
KNOWN_PROPERTIES = Collections.unmodifiableSet(s);
}

public static Set getKnownProperties(Object useless) {
return KNOWN_PROPERTIES;
}
public static boolean isKnownProperty(String s) {
return KNOWN_PROPERTIES.contains(s);
}
public static int maxStatements() {
return 0;
}
public static int maxStatementsPerConnection() {
return 0;
}
public static int initialPoolSize() {
return 3;
}
public static int minPoolSize() {
return 3;
}
public static int maxPoolSize() {
return 15;
}
public static int idleConnectionTestPeriod() {
return 0;
}
public static int maxIdleTime() {
return 0;
}
public static int unreturnedConnectionTimeout() {
return 0;
}
public static int propertyCycle() {
return 0;
}
public static int acquireIncrement() {
return 3;
}
public static int acquireRetryAttempts() {
return 30;
}
public static int acquireRetryDelay() {
return 1000;
}
public static int checkoutTimeout() {
return 0;
}
public static int statementCacheNumDeferredCloseThreads() {
return 0;
}
public static String connectionCustomizerClassName() {
return CONNECTION_CUSTOMIZER_CLASS_NAME;
}
public static String contextClassLoaderSource() {
return "caller";
}

public static String connectionTesterClassName() {
return "com.mchange.v2.c3p0.impl.DefaultConnectionTester";
}
public static String automaticTestTable() {
return AUTOMATIC_TEST_TABLE;
}
public static String driverClass() {
return DRIVER_CLASS;
}
public static boolean forceUseNamedDriverClass() {
return false;
}
public static String jdbcUrl() {
return JDBC_URL;
}
public static int numHelperThreads() {
return 3;
}
public static boolean breakAfterAcquireFailure() {
return false;
}
public static boolean testConnectionOnCheckout() {
return false;
}
public static boolean testConnectionOnCheckin() {
return false;
}
public static boolean autoCommitOnClose() {
return false;
}
public static boolean forceIgnoreUnresolvedTransactions() {
return false;
}
public static boolean debugUnreturnedConnectionStackTraces() {
return false;
}
public static boolean usesTraditionalReflectiveProxies() {
return false;
}
public static boolean privilegeSpawnedThreads() {
return false;
}
public static String preferredTestQuery() {
return PREFERRED_TEST_QUERY;
}
public static String userOverridesAsString() {
return USER_OVERRIDES_AS_STRING;
}
public static String factoryClassLocation() {
return FACTORY_CLASS_LOCATION;
}
public static String overrideDefaultUser() {
return OVERRIDE_DEFAULT_USER;
}
public static String overrideDefaultPassword() {
return OVERRIDE_DEFAULT_PASSWORD;
}
public static String user() {
return USER;
}
public static String password() {
return PASSWORD;
}
public static int maxAdministrativeTaskTime() {
return 0;
}
public static int maxIdleTimeExcessConnections() {
return 0;
}
public static int maxConnectionAge() {
return 0;
}
public static String dataSourceName() {
return DATA_SOURCE_NAME;
}
public static Map extensions() {
return EXTENSIONS;
}
}

