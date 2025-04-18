package com.mchange.v2.c3p0;

import com.mchange.v2.c3p0.cfg.C3P0ConfigUtils;
import com.mchange.v2.c3p0.impl.C3P0Defaults;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import java.util.Properties;

public final class PoolConfig
{
static final MLogger logger = MLog.getLogger(PoolConfig.class); public static final String INITIAL_POOL_SIZE = "c3p0.initialPoolSize"; public static final String MIN_POOL_SIZE = "c3p0.minPoolSize"; public static final String MAX_POOL_SIZE = "c3p0.maxPoolSize"; public static final String IDLE_CONNECTION_TEST_PERIOD = "c3p0.idleConnectionTestPeriod"; public static final String MAX_IDLE_TIME = "c3p0.maxIdleTime"; public static final String PROPERTY_CYCLE = "c3p0.propertyCycle"; public static final String MAX_STATEMENTS = "c3p0.maxStatements"; public static final String MAX_STATEMENTS_PER_CONNECTION = "c3p0.maxStatementsPerConnection"; public static final String CHECKOUT_TIMEOUT = "c3p0.checkoutTimeout"; public static final String ACQUIRE_INCREMENT = "c3p0.acquireIncrement"; public static final String ACQUIRE_RETRY_ATTEMPTS = "c3p0.acquireRetryAttempts"; public static final String ACQUIRE_RETRY_DELAY = "c3p0.acquireRetryDelay"; public static final String BREAK_AFTER_ACQUIRE_FAILURE = "c3p0.breakAfterAcquireFailure"; public static final String USES_TRADITIONAL_REFLECTIVE_PROXIES = "c3p0.usesTraditionalReflectiveProxies"; public static final String TEST_CONNECTION_ON_CHECKOUT = "c3p0.testConnectionOnCheckout"; public static final String TEST_CONNECTION_ON_CHECKIN = "c3p0.testConnectionOnCheckin"; public static final String CONNECTION_TESTER_CLASS_NAME = "c3p0.connectionTesterClassName"; public static final String AUTOMATIC_TEST_TABLE = "c3p0.automaticTestTable"; public static final String AUTO_COMMIT_ON_CLOSE = "c3p0.autoCommitOnClose"; public static final String FORCE_IGNORE_UNRESOLVED_TRANSACTIONS = "c3p0.forceIgnoreUnresolvedTransactions"; public static final String NUM_HELPER_THREADS = "c3p0.numHelperThreads"; public static final String PREFERRED_TEST_QUERY = "c3p0.preferredTestQuery"; public static final String FACTORY_CLASS_LOCATION = "c3p0.factoryClassLocation"; static final PoolConfig DEFAULTS;
static {
Properties rsrcProps = C3P0ConfigUtils.findResourceProperties();
PoolConfig rsrcDefaults = extractConfig(rsrcProps, null);

try {
properties1 = System.getProperties();
} catch (SecurityException e) {

if (logger.isLoggable(MLevel.WARNING)) {
logger.log(MLevel.WARNING, "Read of system Properties blocked -- ignoring any c3p0 configuration via System properties! (But any configuration via a c3p0.properties file is still okay!)", e);
}

properties1 = new Properties();
} 
DEFAULTS = extractConfig(properties1, rsrcDefaults);
} int maxStatements; int maxStatementsPerConnection; int initialPoolSize; int minPoolSize; int maxPoolSize; int idleConnectionTestPeriod; int maxIdleTime; int propertyCycle; int checkoutTimeout; int acquireIncrement; int acquireRetryAttempts; int acquireRetryDelay; boolean breakAfterAcquireFailure; boolean testConnectionOnCheckout; boolean testConnectionOnCheckin; boolean autoCommitOnClose; boolean forceIgnoreUnresolvedTransactions; boolean usesTraditionalReflectiveProxies; String connectionTesterClassName; String automaticTestTable; int numHelperThreads; String preferredTestQuery; String factoryClassLocation; static {
Properties properties1;
} public static int defaultNumHelperThreads() {
return DEFAULTS.getNumHelperThreads();
}
public static String defaultPreferredTestQuery() {
return DEFAULTS.getPreferredTestQuery();
}
public static String defaultFactoryClassLocation() {
return DEFAULTS.getFactoryClassLocation();
}
public static int defaultMaxStatements() {
return DEFAULTS.getMaxStatements();
}
public static int defaultMaxStatementsPerConnection() {
return DEFAULTS.getMaxStatementsPerConnection();
}
public static int defaultInitialPoolSize() {
return DEFAULTS.getInitialPoolSize();
}
public static int defaultMinPoolSize() {
return DEFAULTS.getMinPoolSize();
}
public static int defaultMaxPoolSize() {
return DEFAULTS.getMaxPoolSize();
}
public static int defaultIdleConnectionTestPeriod() {
return DEFAULTS.getIdleConnectionTestPeriod();
}
public static int defaultMaxIdleTime() {
return DEFAULTS.getMaxIdleTime();
}
public static int defaultPropertyCycle() {
return DEFAULTS.getPropertyCycle();
}
public static int defaultCheckoutTimeout() {
return DEFAULTS.getCheckoutTimeout();
}
public static int defaultAcquireIncrement() {
return DEFAULTS.getAcquireIncrement();
}
public static int defaultAcquireRetryAttempts() {
return DEFAULTS.getAcquireRetryAttempts();
}
public static int defaultAcquireRetryDelay() {
return DEFAULTS.getAcquireRetryDelay();
}
public static boolean defaultBreakAfterAcquireFailure() {
return DEFAULTS.isBreakAfterAcquireFailure();
}
public static String defaultConnectionTesterClassName() {
return DEFAULTS.getConnectionTesterClassName();
}
public static String defaultAutomaticTestTable() {
return DEFAULTS.getAutomaticTestTable();
}
public static boolean defaultTestConnectionOnCheckout() {
return DEFAULTS.isTestConnectionOnCheckout();
}
public static boolean defaultTestConnectionOnCheckin() {
return DEFAULTS.isTestConnectionOnCheckin();
}
public static boolean defaultAutoCommitOnClose() {
return DEFAULTS.isAutoCommitOnClose();
}
public static boolean defaultForceIgnoreUnresolvedTransactions() {
return DEFAULTS.isAutoCommitOnClose();
}
public static boolean defaultUsesTraditionalReflectiveProxies() {
return DEFAULTS.isUsesTraditionalReflectiveProxies();
}

private PoolConfig(Properties props, boolean init) throws NumberFormatException {
if (init)
extractConfig(this, props, DEFAULTS); 
}

public PoolConfig(Properties props) throws NumberFormatException {
this(props, true);
}
public PoolConfig() throws NumberFormatException {
this(null, true);
}
public int getNumHelperThreads() {
return this.numHelperThreads;
}
public String getPreferredTestQuery() {
return this.preferredTestQuery;
}
public String getFactoryClassLocation() {
return this.factoryClassLocation;
}
public int getMaxStatements() {
return this.maxStatements;
}
public int getMaxStatementsPerConnection() {
return this.maxStatementsPerConnection;
}
public int getInitialPoolSize() {
return this.initialPoolSize;
}
public int getMinPoolSize() {
return this.minPoolSize;
}
public int getMaxPoolSize() {
return this.maxPoolSize;
}
public int getIdleConnectionTestPeriod() {
return this.idleConnectionTestPeriod;
}
public int getMaxIdleTime() {
return this.maxIdleTime;
}
public int getPropertyCycle() {
return this.propertyCycle;
}
public int getAcquireIncrement() {
return this.acquireIncrement;
}
public int getCheckoutTimeout() {
return this.checkoutTimeout;
}
public int getAcquireRetryAttempts() {
return this.acquireRetryAttempts;
}
public int getAcquireRetryDelay() {
return this.acquireRetryDelay;
}
public boolean isBreakAfterAcquireFailure() {
return this.breakAfterAcquireFailure;
}
public boolean isUsesTraditionalReflectiveProxies() {
return this.usesTraditionalReflectiveProxies;
}
public String getConnectionTesterClassName() {
return this.connectionTesterClassName;
}
public String getAutomaticTestTable() {
return this.automaticTestTable;
}

public boolean getTestConnectionOnCheckout() {
return this.testConnectionOnCheckout;
}
public boolean isTestConnectionOnCheckout() {
return getTestConnectionOnCheckout();
}
public boolean isTestConnectionOnCheckin() {
return this.testConnectionOnCheckin;
}
public boolean isAutoCommitOnClose() {
return this.autoCommitOnClose;
}
public boolean isForceIgnoreUnresolvedTransactions() {
return this.forceIgnoreUnresolvedTransactions;
}
public void setNumHelperThreads(int numHelperThreads) {
this.numHelperThreads = numHelperThreads;
}
public void setPreferredTestQuery(String preferredTestQuery) {
this.preferredTestQuery = preferredTestQuery;
}
public void setFactoryClassLocation(String factoryClassLocation) {
this.factoryClassLocation = factoryClassLocation;
}
public void setMaxStatements(int maxStatements) {
this.maxStatements = maxStatements;
}
public void setMaxStatementsPerConnection(int maxStatementsPerConnection) {
this.maxStatementsPerConnection = maxStatementsPerConnection;
}
public void setInitialPoolSize(int initialPoolSize) {
this.initialPoolSize = initialPoolSize;
}
public void setMinPoolSize(int minPoolSize) {
this.minPoolSize = minPoolSize;
}
public void setMaxPoolSize(int maxPoolSize) {
this.maxPoolSize = maxPoolSize;
}
public void setIdleConnectionTestPeriod(int idleConnectionTestPeriod) {
this.idleConnectionTestPeriod = idleConnectionTestPeriod;
}
public void setMaxIdleTime(int maxIdleTime) {
this.maxIdleTime = maxIdleTime;
}
public void setPropertyCycle(int propertyCycle) {
this.propertyCycle = propertyCycle;
}
public void setCheckoutTimeout(int checkoutTimeout) {
this.checkoutTimeout = checkoutTimeout;
}
public void setAcquireIncrement(int acquireIncrement) {
this.acquireIncrement = acquireIncrement;
}
public void setAcquireRetryAttempts(int acquireRetryAttempts) {
this.acquireRetryAttempts = acquireRetryAttempts;
}
public void setAcquireRetryDelay(int acquireRetryDelay) {
this.acquireRetryDelay = acquireRetryDelay;
}
public void setConnectionTesterClassName(String connectionTesterClassName) {
this.connectionTesterClassName = connectionTesterClassName;
}
public void setAutomaticTestTable(String automaticTestTable) {
this.automaticTestTable = automaticTestTable;
}
public void setBreakAfterAcquireFailure(boolean breakAfterAcquireFailure) {
this.breakAfterAcquireFailure = breakAfterAcquireFailure;
}
public void setUsesTraditionalReflectiveProxies(boolean usesTraditionalReflectiveProxies) {
this.usesTraditionalReflectiveProxies = usesTraditionalReflectiveProxies;
}
public void setTestConnectionOnCheckout(boolean testConnectionOnCheckout) {
this.testConnectionOnCheckout = testConnectionOnCheckout;
}
public void setTestConnectionOnCheckin(boolean testConnectionOnCheckin) {
this.testConnectionOnCheckin = testConnectionOnCheckin;
}
public void setAutoCommitOnClose(boolean autoCommitOnClose) {
this.autoCommitOnClose = autoCommitOnClose;
}
public void setForceIgnoreUnresolvedTransactions(boolean forceIgnoreUnresolvedTransactions) {
this.forceIgnoreUnresolvedTransactions = forceIgnoreUnresolvedTransactions;
}

private static PoolConfig extractConfig(Properties props, PoolConfig defaults) throws NumberFormatException {
PoolConfig pcfg = new PoolConfig(null, false);
extractConfig(pcfg, props, defaults);
return pcfg;
}

private static void extractConfig(PoolConfig pcfg, Properties props, PoolConfig defaults) throws NumberFormatException {
String maxStatementsStr = null;
String maxStatementsPerConnectionStr = null;
String initialPoolSizeStr = null;
String minPoolSizeStr = null;
String maxPoolSizeStr = null;
String idleConnectionTestPeriodStr = null;
String maxIdleTimeStr = null;
String propertyCycleStr = null;
String checkoutTimeoutStr = null;
String acquireIncrementStr = null;
String acquireRetryAttemptsStr = null;
String acquireRetryDelayStr = null;
String breakAfterAcquireFailureStr = null;
String usesTraditionalReflectiveProxiesStr = null;
String testConnectionOnCheckoutStr = null;
String testConnectionOnCheckinStr = null;
String autoCommitOnCloseStr = null;
String forceIgnoreUnresolvedTransactionsStr = null;
String connectionTesterClassName = null;
String automaticTestTable = null;
String numHelperThreadsStr = null;
String preferredTestQuery = null;
String factoryClassLocation = null;

if (props != null) {

maxStatementsStr = props.getProperty("c3p0.maxStatements");
maxStatementsPerConnectionStr = props.getProperty("c3p0.maxStatementsPerConnection");
initialPoolSizeStr = props.getProperty("c3p0.initialPoolSize");
minPoolSizeStr = props.getProperty("c3p0.minPoolSize");
maxPoolSizeStr = props.getProperty("c3p0.maxPoolSize");
idleConnectionTestPeriodStr = props.getProperty("c3p0.idleConnectionTestPeriod");
maxIdleTimeStr = props.getProperty("c3p0.maxIdleTime");
propertyCycleStr = props.getProperty("c3p0.propertyCycle");
checkoutTimeoutStr = props.getProperty("c3p0.checkoutTimeout");
acquireIncrementStr = props.getProperty("c3p0.acquireIncrement");
acquireRetryAttemptsStr = props.getProperty("c3p0.acquireRetryAttempts");
acquireRetryDelayStr = props.getProperty("c3p0.acquireRetryDelay");
breakAfterAcquireFailureStr = props.getProperty("c3p0.breakAfterAcquireFailure");
usesTraditionalReflectiveProxiesStr = props.getProperty("c3p0.usesTraditionalReflectiveProxies");
testConnectionOnCheckoutStr = props.getProperty("c3p0.testConnectionOnCheckout");
testConnectionOnCheckinStr = props.getProperty("c3p0.testConnectionOnCheckin");
autoCommitOnCloseStr = props.getProperty("c3p0.autoCommitOnClose");
forceIgnoreUnresolvedTransactionsStr = props.getProperty("c3p0.forceIgnoreUnresolvedTransactions");
connectionTesterClassName = props.getProperty("c3p0.connectionTesterClassName");
automaticTestTable = props.getProperty("c3p0.automaticTestTable");
numHelperThreadsStr = props.getProperty("c3p0.numHelperThreads");
preferredTestQuery = props.getProperty("c3p0.preferredTestQuery");
factoryClassLocation = props.getProperty("c3p0.factoryClassLocation");
} 

if (maxStatementsStr != null) {
pcfg.setMaxStatements(Integer.parseInt(maxStatementsStr.trim()));
} else if (defaults != null) {
pcfg.setMaxStatements(defaults.getMaxStatements());
} else {
pcfg.setMaxStatements(C3P0Defaults.maxStatements());
} 

if (maxStatementsPerConnectionStr != null) {
pcfg.setMaxStatementsPerConnection(Integer.parseInt(maxStatementsPerConnectionStr.trim()));
} else if (defaults != null) {
pcfg.setMaxStatementsPerConnection(defaults.getMaxStatementsPerConnection());
} else {
pcfg.setMaxStatementsPerConnection(C3P0Defaults.maxStatementsPerConnection());
} 

if (initialPoolSizeStr != null) {
pcfg.setInitialPoolSize(Integer.parseInt(initialPoolSizeStr.trim()));
} else if (defaults != null) {
pcfg.setInitialPoolSize(defaults.getInitialPoolSize());
} else {
pcfg.setInitialPoolSize(C3P0Defaults.initialPoolSize());
} 

if (minPoolSizeStr != null) {
pcfg.setMinPoolSize(Integer.parseInt(minPoolSizeStr.trim()));
} else if (defaults != null) {
pcfg.setMinPoolSize(defaults.getMinPoolSize());
} else {
pcfg.setMinPoolSize(C3P0Defaults.minPoolSize());
} 

if (maxPoolSizeStr != null) {
pcfg.setMaxPoolSize(Integer.parseInt(maxPoolSizeStr.trim()));
} else if (defaults != null) {
pcfg.setMaxPoolSize(defaults.getMaxPoolSize());
} else {
pcfg.setMaxPoolSize(C3P0Defaults.maxPoolSize());
} 

if (idleConnectionTestPeriodStr != null) {
pcfg.setIdleConnectionTestPeriod(Integer.parseInt(idleConnectionTestPeriodStr.trim()));
} else if (defaults != null) {
pcfg.setIdleConnectionTestPeriod(defaults.getIdleConnectionTestPeriod());
} else {
pcfg.setIdleConnectionTestPeriod(C3P0Defaults.idleConnectionTestPeriod());
} 

if (maxIdleTimeStr != null) {
pcfg.setMaxIdleTime(Integer.parseInt(maxIdleTimeStr.trim()));
} else if (defaults != null) {
pcfg.setMaxIdleTime(defaults.getMaxIdleTime());
} else {
pcfg.setMaxIdleTime(C3P0Defaults.maxIdleTime());
} 

if (propertyCycleStr != null) {
pcfg.setPropertyCycle(Integer.parseInt(propertyCycleStr.trim()));
} else if (defaults != null) {
pcfg.setPropertyCycle(defaults.getPropertyCycle());
} else {
pcfg.setPropertyCycle(C3P0Defaults.propertyCycle());
} 

if (checkoutTimeoutStr != null) {
pcfg.setCheckoutTimeout(Integer.parseInt(checkoutTimeoutStr.trim()));
} else if (defaults != null) {
pcfg.setCheckoutTimeout(defaults.getCheckoutTimeout());
} else {
pcfg.setCheckoutTimeout(C3P0Defaults.checkoutTimeout());
} 

if (acquireIncrementStr != null) {
pcfg.setAcquireIncrement(Integer.parseInt(acquireIncrementStr.trim()));
} else if (defaults != null) {
pcfg.setAcquireIncrement(defaults.getAcquireIncrement());
} else {
pcfg.setAcquireIncrement(C3P0Defaults.acquireIncrement());
} 

if (acquireRetryAttemptsStr != null) {
pcfg.setAcquireRetryAttempts(Integer.parseInt(acquireRetryAttemptsStr.trim()));
} else if (defaults != null) {
pcfg.setAcquireRetryAttempts(defaults.getAcquireRetryAttempts());
} else {
pcfg.setAcquireRetryAttempts(C3P0Defaults.acquireRetryAttempts());
} 

if (acquireRetryDelayStr != null) {
pcfg.setAcquireRetryDelay(Integer.parseInt(acquireRetryDelayStr.trim()));
} else if (defaults != null) {
pcfg.setAcquireRetryDelay(defaults.getAcquireRetryDelay());
} else {
pcfg.setAcquireRetryDelay(C3P0Defaults.acquireRetryDelay());
} 

if (breakAfterAcquireFailureStr != null) {
pcfg.setBreakAfterAcquireFailure(Boolean.valueOf(breakAfterAcquireFailureStr.trim()).booleanValue());
} else if (defaults != null) {
pcfg.setBreakAfterAcquireFailure(defaults.isBreakAfterAcquireFailure());
} else {
pcfg.setBreakAfterAcquireFailure(C3P0Defaults.breakAfterAcquireFailure());
} 

if (usesTraditionalReflectiveProxiesStr != null) {
pcfg.setUsesTraditionalReflectiveProxies(Boolean.valueOf(usesTraditionalReflectiveProxiesStr.trim()).booleanValue());
} else if (defaults != null) {
pcfg.setUsesTraditionalReflectiveProxies(defaults.isUsesTraditionalReflectiveProxies());
} else {
pcfg.setUsesTraditionalReflectiveProxies(C3P0Defaults.usesTraditionalReflectiveProxies());
} 

if (testConnectionOnCheckoutStr != null) {
pcfg.setTestConnectionOnCheckout(Boolean.valueOf(testConnectionOnCheckoutStr.trim()).booleanValue());
} else if (defaults != null) {
pcfg.setTestConnectionOnCheckout(defaults.isTestConnectionOnCheckout());
} else {
pcfg.setTestConnectionOnCheckout(C3P0Defaults.testConnectionOnCheckout());
} 

if (testConnectionOnCheckinStr != null) {
pcfg.setTestConnectionOnCheckin(Boolean.valueOf(testConnectionOnCheckinStr.trim()).booleanValue());
} else if (defaults != null) {
pcfg.setTestConnectionOnCheckin(defaults.isTestConnectionOnCheckin());
} else {
pcfg.setTestConnectionOnCheckin(C3P0Defaults.testConnectionOnCheckin());
} 

if (autoCommitOnCloseStr != null) {
pcfg.setAutoCommitOnClose(Boolean.valueOf(autoCommitOnCloseStr.trim()).booleanValue());
} else if (defaults != null) {
pcfg.setAutoCommitOnClose(defaults.isAutoCommitOnClose());
} else {
pcfg.setAutoCommitOnClose(C3P0Defaults.autoCommitOnClose());
} 

if (forceIgnoreUnresolvedTransactionsStr != null) {
pcfg.setForceIgnoreUnresolvedTransactions(Boolean.valueOf(forceIgnoreUnresolvedTransactionsStr.trim()).booleanValue());
} else if (defaults != null) {
pcfg.setForceIgnoreUnresolvedTransactions(defaults.isForceIgnoreUnresolvedTransactions());
} else {
pcfg.setForceIgnoreUnresolvedTransactions(C3P0Defaults.forceIgnoreUnresolvedTransactions());
} 

if (connectionTesterClassName != null) {
pcfg.setConnectionTesterClassName(connectionTesterClassName.trim());
} else if (defaults != null) {
pcfg.setConnectionTesterClassName(defaults.getConnectionTesterClassName());
} else {
pcfg.setConnectionTesterClassName(C3P0Defaults.connectionTesterClassName());
} 

if (automaticTestTable != null) {
pcfg.setAutomaticTestTable(automaticTestTable.trim());
} else if (defaults != null) {
pcfg.setAutomaticTestTable(defaults.getAutomaticTestTable());
} else {
pcfg.setAutomaticTestTable(C3P0Defaults.automaticTestTable());
} 

if (numHelperThreadsStr != null) {
pcfg.setNumHelperThreads(Integer.parseInt(numHelperThreadsStr.trim()));
} else if (defaults != null) {
pcfg.setNumHelperThreads(defaults.getNumHelperThreads());
} else {
pcfg.setNumHelperThreads(C3P0Defaults.numHelperThreads());
} 

if (preferredTestQuery != null) {
pcfg.setPreferredTestQuery(preferredTestQuery.trim());
} else if (defaults != null) {
pcfg.setPreferredTestQuery(defaults.getPreferredTestQuery());
} else {
pcfg.setPreferredTestQuery(C3P0Defaults.preferredTestQuery());
} 

if (factoryClassLocation != null) {
pcfg.setFactoryClassLocation(factoryClassLocation.trim());
} else if (defaults != null) {
pcfg.setFactoryClassLocation(defaults.getFactoryClassLocation());
} else {
pcfg.setFactoryClassLocation(C3P0Defaults.factoryClassLocation());
} 
}
}

