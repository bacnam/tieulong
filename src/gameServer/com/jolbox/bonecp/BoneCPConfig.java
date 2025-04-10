package com.jolbox.bonecp;

import com.google.common.base.Objects;
import com.jolbox.bonecp.hooks.ConnectionHook;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BoneCPConfig
implements BoneCPConfigMBean, Cloneable, Serializable
{
private static final String USER = "user";
private static final String PASSWORD = "password";
private static final long serialVersionUID = 6090570773474131622L;
private static final String CONFIG_TOSTRING = "JDBC URL = %s, Username = %s, partitions = %d, max (per partition) = %d, min (per partition) = %d, helper threads = %d, idle max age = %d min, idle test period = %d min";
private static final String CONFIG_DS_TOSTRING = "JDBC URL = (via datasource bean), Username = (via datasource bean), partitions = %d, max (per partition) = %d, min (per partition) = %d, helper threads = %d, idle max age = %d min, idle test period = %d min";
private static final Logger logger = LoggerFactory.getLogger(BoneCPConfig.class);

private int minConnectionsPerPartition;

private int maxConnectionsPerPartition;

private int acquireIncrement = 2;

private int partitionCount = 1;

private String jdbcUrl;

private String username;

private String password;

private long idleConnectionTestPeriodInSeconds = 14400L;

private long idleMaxAgeInSeconds = 3600L;

private String connectionTestStatement;

private int statementsCacheSize = 0;

private int statementsCachedPerConnection = 0;

private int releaseHelperThreads = 3;

private int statementReleaseHelperThreads = 0;

private ConnectionHook connectionHook;

private String initSQL;

private boolean closeConnectionWatch;

private boolean logStatementsEnabled;

private long acquireRetryDelayInMs = 7000L;

private int acquireRetryAttempts = 5;

private boolean lazyInit;

private boolean transactionRecoveryEnabled;

private String connectionHookClassName;

private ClassLoader classLoader = getClassLoader();

private String poolName;

private boolean disableJMX;

private DataSource datasourceBean;

private long queryExecuteTimeLimitInMs = 0L;

private int poolAvailabilityThreshold = 20;

private boolean disableConnectionTracking;

private Properties driverProperties;

private long connectionTimeoutInMs = 0L;

private long closeConnectionWatchTimeoutInMs = 0L;

private long maxConnectionAgeInSeconds = 0L;

private String configFile;

private String serviceOrder;

private boolean statisticsEnabled;

private Boolean defaultAutoCommit;

private Boolean defaultReadOnly;

private String defaultTransactionIsolation;

private String defaultCatalog;

private int defaultTransactionIsolationValue = -1;

private boolean externalAuth;

public String getPoolName() {
return this.poolName;
}

public void setPoolName(String poolName) {
this.poolName = poolName;
}

public int getMinConnectionsPerPartition() {
return this.minConnectionsPerPartition;
}

public void setMinConnectionsPerPartition(int minConnectionsPerPartition) {
this.minConnectionsPerPartition = minConnectionsPerPartition;
}

public int getMaxConnectionsPerPartition() {
return this.maxConnectionsPerPartition;
}

public void setMaxConnectionsPerPartition(int maxConnectionsPerPartition) {
this.maxConnectionsPerPartition = maxConnectionsPerPartition;
}

public int getAcquireIncrement() {
return this.acquireIncrement;
}

public void setAcquireIncrement(int acquireIncrement) {
this.acquireIncrement = acquireIncrement;
}

public int getPartitionCount() {
return this.partitionCount;
}

public void setPartitionCount(int partitionCount) {
this.partitionCount = partitionCount;
}

public String getJdbcUrl() {
return this.jdbcUrl;
}

public void setJdbcUrl(String jdbcUrl) {
this.jdbcUrl = jdbcUrl;
}

public String getUsername() {
return this.username;
}

public void setUsername(String username) {
this.username = username;
}

public String getPassword() {
return this.password;
}

public void setPassword(String password) {
this.password = password;
}

@Deprecated
public long getIdleConnectionTestPeriod() {
logger.warn("Please use getIdleConnectionTestPeriodInMinutes in place of getIdleConnectionTestPeriod. This method has been deprecated.");
return getIdleConnectionTestPeriodInMinutes();
}

@Deprecated
public void setIdleConnectionTestPeriod(long idleConnectionTestPeriod) {
logger.warn("Please use setIdleConnectionTestPeriodInMinutes in place of setIdleConnectionTestPeriod. This method has been deprecated.");
setIdleConnectionTestPeriod(idleConnectionTestPeriod, TimeUnit.MINUTES);
}

public long getIdleConnectionTestPeriodInMinutes() {
return TimeUnit.MINUTES.convert(this.idleConnectionTestPeriodInSeconds, TimeUnit.SECONDS);
}

public long getIdleConnectionTestPeriod(TimeUnit timeUnit) {
return timeUnit.convert(this.idleConnectionTestPeriodInSeconds, TimeUnit.SECONDS);
}

public void setIdleConnectionTestPeriodInMinutes(long idleConnectionTestPeriod) {
setIdleConnectionTestPeriod(idleConnectionTestPeriod, TimeUnit.MINUTES);
}

public void setIdleConnectionTestPeriodInSeconds(long idleConnectionTestPeriod) {
setIdleConnectionTestPeriod(idleConnectionTestPeriod, TimeUnit.SECONDS);
}

public void setIdleConnectionTestPeriod(long idleConnectionTestPeriod, TimeUnit timeUnit) {
this.idleConnectionTestPeriodInSeconds = TimeUnit.SECONDS.convert(idleConnectionTestPeriod, timeUnit);
}

@Deprecated
public long getIdleMaxAge() {
logger.warn("Please use getIdleMaxAgeInMinutes in place of getIdleMaxAge. This method has been deprecated.");
return getIdleMaxAgeInMinutes();
}

public long getIdleMaxAge(TimeUnit timeUnit) {
return timeUnit.convert(this.idleMaxAgeInSeconds, TimeUnit.SECONDS);
}

public long getIdleMaxAgeInMinutes() {
return TimeUnit.MINUTES.convert(this.idleMaxAgeInSeconds, TimeUnit.SECONDS);
}

@Deprecated
public void setIdleMaxAge(long idleMaxAge) {
logger.warn("Please use setIdleMaxAgeInMinutes in place of setIdleMaxAge. This method has been deprecated.");
setIdleMaxAgeInMinutes(idleMaxAge);
}

public void setIdleMaxAgeInMinutes(long idleMaxAge) {
setIdleMaxAge(idleMaxAge, TimeUnit.MINUTES);
}

public void setIdleMaxAgeInSeconds(long idleMaxAge) {
setIdleMaxAge(idleMaxAge, TimeUnit.SECONDS);
}

public void setIdleMaxAge(long idleMaxAge, TimeUnit timeUnit) {
this.idleMaxAgeInSeconds = TimeUnit.SECONDS.convert(idleMaxAge, timeUnit);
}

public String getConnectionTestStatement() {
return this.connectionTestStatement;
}

public void setConnectionTestStatement(String connectionTestStatement) {
this.connectionTestStatement = connectionTestStatement;
}

@Deprecated
public int getPreparedStatementsCacheSize() {
logger.warn("Please use getStatementsCacheSize in place of getPreparedStatementsCacheSize. This method has been deprecated.");
return this.statementsCacheSize;
}

@Deprecated
public int getPreparedStatementCacheSize() {
logger.warn("Please use getStatementsCacheSize in place of getPreparedStatementCacheSize. This method has been deprecated.");
return this.statementsCacheSize;
}

@Deprecated
public void setPreparedStatementsCacheSize(int preparedStatementsCacheSize) {
logger.warn("Please use setStatementsCacheSize in place of setPreparedStatementsCacheSize. This method has been deprecated.");
this.statementsCacheSize = preparedStatementsCacheSize;
}

public void setStatementsCacheSize(int statementsCacheSize) {
this.statementsCacheSize = statementsCacheSize;
}

public int getStatementsCacheSize() {
return this.statementsCacheSize;
}

@Deprecated
public void setStatementCacheSize(int statementsCacheSize) {
logger.warn("Please use setStatementsCacheSize in place of setStatementCacheSize. This method has been deprecated.");
this.statementsCacheSize = statementsCacheSize;
}

@Deprecated
public int getStatementCacheSize() {
logger.warn("Please use getStatementsCacheSize in place of getStatementCacheSize. This method has been deprecated.");
return this.statementsCacheSize;
}

public int getReleaseHelperThreads() {
return this.releaseHelperThreads;
}

public void setReleaseHelperThreads(int releaseHelperThreads) {
this.releaseHelperThreads = releaseHelperThreads;
}

@Deprecated
public int getStatementsCachedPerConnection() {
return this.statementsCachedPerConnection;
}

@Deprecated
public void setStatementsCachedPerConnection(int statementsCachedPerConnection) {
this.statementsCachedPerConnection = statementsCachedPerConnection;
}

public ConnectionHook getConnectionHook() {
return this.connectionHook;
}

public void setConnectionHook(ConnectionHook connectionHook) {
this.connectionHook = connectionHook;
}

public String getInitSQL() {
return this.initSQL;
}

public void setInitSQL(String initSQL) {
this.initSQL = initSQL;
}

public boolean isCloseConnectionWatch() {
return this.closeConnectionWatch;
}

public void setCloseConnectionWatch(boolean closeConnectionWatch) {
this.closeConnectionWatch = closeConnectionWatch;
}

public boolean isLogStatementsEnabled() {
return this.logStatementsEnabled;
}

public void setLogStatementsEnabled(boolean logStatementsEnabled) {
this.logStatementsEnabled = logStatementsEnabled;
}

@Deprecated
public long getAcquireRetryDelay() {
logger.warn("Please use getAcquireRetryDelayInMs in place of getAcquireRetryDelay. This method has been deprecated.");
return this.acquireRetryDelayInMs;
}

@Deprecated
public void setAcquireRetryDelay(int acquireRetryDelayInMs) {
logger.warn("Please use setAcquireRetryDelayInMs in place of setAcquireRetryDelay. This method has been deprecated.");
this.acquireRetryDelayInMs = acquireRetryDelayInMs;
}

public long getAcquireRetryDelayInMs() {
return this.acquireRetryDelayInMs;
}

public long getAcquireRetryDelay(TimeUnit timeUnit) {
return timeUnit.convert(this.acquireRetryDelayInMs, TimeUnit.MILLISECONDS);
}

public void setAcquireRetryDelayInMs(long acquireRetryDelay) {
setAcquireRetryDelay(acquireRetryDelay, TimeUnit.MILLISECONDS);
}

public void setAcquireRetryDelay(long acquireRetryDelay, TimeUnit timeUnit) {
this.acquireRetryDelayInMs = TimeUnit.MILLISECONDS.convert(acquireRetryDelay, timeUnit);
}

public boolean isLazyInit() {
return this.lazyInit;
}

public void setLazyInit(boolean lazyInit) {
this.lazyInit = lazyInit;
}

public boolean isTransactionRecoveryEnabled() {
return this.transactionRecoveryEnabled;
}

public void setTransactionRecoveryEnabled(boolean transactionRecoveryEnabled) {
this.transactionRecoveryEnabled = transactionRecoveryEnabled;
}

public int getAcquireRetryAttempts() {
return this.acquireRetryAttempts;
}

public void setAcquireRetryAttempts(int acquireRetryAttempts) {
this.acquireRetryAttempts = acquireRetryAttempts;
}

public void setConnectionHookClassName(String connectionHookClassName) {
this.connectionHookClassName = connectionHookClassName;
if (connectionHookClassName != null) {

try {
Object hookClass = loadClass(connectionHookClassName).newInstance();
this.connectionHook = (ConnectionHook)hookClass;
} catch (Exception e) {
logger.error("Unable to create an instance of the connection hook class (" + connectionHookClassName + ")");
this.connectionHook = null;
} 
}
}

public String getConnectionHookClassName() {
return this.connectionHookClassName;
}

public boolean isDisableJMX() {
return this.disableJMX;
}

public void setDisableJMX(boolean disableJMX) {
this.disableJMX = disableJMX;
}

public DataSource getDatasourceBean() {
return this.datasourceBean;
}

public void setDatasourceBean(DataSource datasourceBean) {
this.datasourceBean = datasourceBean;
}

@Deprecated
public long getQueryExecuteTimeLimit() {
logger.warn("Please use getQueryExecuteTimeLimitInMs in place of getQueryExecuteTimeLimit. This method has been deprecated.");
return this.queryExecuteTimeLimitInMs;
}

@Deprecated
public void setQueryExecuteTimeLimit(int queryExecuteTimeLimit) {
logger.warn("Please use setQueryExecuteTimeLimitInMs in place of setQueryExecuteTimeLimit. This method has been deprecated.");
setQueryExecuteTimeLimit(queryExecuteTimeLimit, TimeUnit.MILLISECONDS);
}

public long getQueryExecuteTimeLimitInMs() {
return this.queryExecuteTimeLimitInMs;
}

public long getQueryExecuteTimeLimit(TimeUnit timeUnit) {
return timeUnit.convert(this.queryExecuteTimeLimitInMs, TimeUnit.MILLISECONDS);
}

public void setQueryExecuteTimeLimitInMs(long queryExecuteTimeLimit) {
setQueryExecuteTimeLimit(queryExecuteTimeLimit, TimeUnit.MILLISECONDS);
}

public void setQueryExecuteTimeLimit(long queryExecuteTimeLimit, TimeUnit timeUnit) {
this.queryExecuteTimeLimitInMs = TimeUnit.MILLISECONDS.convert(queryExecuteTimeLimit, timeUnit);
}

public int getPoolAvailabilityThreshold() {
return this.poolAvailabilityThreshold;
}

public void setPoolAvailabilityThreshold(int poolAvailabilityThreshold) {
this.poolAvailabilityThreshold = poolAvailabilityThreshold;
}

public boolean isDisableConnectionTracking() {
return this.disableConnectionTracking;
}

public void setDisableConnectionTracking(boolean disableConnectionTracking) {
this.disableConnectionTracking = disableConnectionTracking;
}

@Deprecated
public long getConnectionTimeout() {
logger.warn("Please use getConnectionTimeoutInMs in place of getConnectionTimeout. This method has been deprecated.");
return this.connectionTimeoutInMs;
}

@Deprecated
public void setConnectionTimeout(long connectionTimeout) {
logger.warn("Please use setConnectionTimeoutInMs in place of setConnectionTimeout. This method has been deprecated.");
this.connectionTimeoutInMs = connectionTimeout;
}

public long getConnectionTimeoutInMs() {
return this.connectionTimeoutInMs;
}

public long getConnectionTimeout(TimeUnit timeUnit) {
return timeUnit.convert(this.connectionTimeoutInMs, TimeUnit.MILLISECONDS);
}

public void setConnectionTimeoutInMs(long connectionTimeoutinMs) {
setConnectionTimeout(connectionTimeoutinMs, TimeUnit.MILLISECONDS);
}

public void setConnectionTimeout(long connectionTimeout, TimeUnit timeUnit) {
this.connectionTimeoutInMs = TimeUnit.MILLISECONDS.convert(connectionTimeout, timeUnit);
}

public Properties getDriverProperties() {
return this.driverProperties;
}

public void setDriverProperties(Properties driverProperties) {
if (driverProperties != null) {

this.driverProperties = new Properties();
this.driverProperties.putAll(driverProperties);
} 
}

@Deprecated
public long getCloseConnectionWatchTimeout() {
logger.warn("Please use getCloseConnectionWatchTimeoutInMs in place of getCloseConnectionWatchTimeout. This method has been deprecated.");
return this.closeConnectionWatchTimeoutInMs;
}

@Deprecated
public void setCloseConnectionWatchTimeout(long closeConnectionWatchTimeout) {
logger.warn("Please use setCloseConnectionWatchTimeoutInMs in place of setCloseConnectionWatchTimeout. This method has been deprecated.");
setCloseConnectionWatchTimeoutInMs(closeConnectionWatchTimeout);
}

public long getCloseConnectionWatchTimeoutInMs() {
return this.closeConnectionWatchTimeoutInMs;
}

public long getCloseConnectionWatchTimeout(TimeUnit timeUnit) {
return timeUnit.convert(this.closeConnectionWatchTimeoutInMs, TimeUnit.MILLISECONDS);
}

public void setCloseConnectionWatchTimeoutInMs(long closeConnectionWatchTimeout) {
setCloseConnectionWatchTimeout(closeConnectionWatchTimeout, TimeUnit.MILLISECONDS);
}

public void setCloseConnectionWatchTimeout(long closeConnectionWatchTimeout, TimeUnit timeUnit) {
this.closeConnectionWatchTimeoutInMs = TimeUnit.MILLISECONDS.convert(closeConnectionWatchTimeout, timeUnit);
}

public int getStatementReleaseHelperThreads() {
return this.statementReleaseHelperThreads;
}

public void setStatementReleaseHelperThreads(int statementReleaseHelperThreads) {
this.statementReleaseHelperThreads = statementReleaseHelperThreads;
}

@Deprecated
public long getMaxConnectionAge() {
logger.warn("Please use getMaxConnectionAgeInSeconds in place of getMaxConnectionAge. This method has been deprecated.");
return this.maxConnectionAgeInSeconds;
}

public long getMaxConnectionAgeInSeconds() {
return this.maxConnectionAgeInSeconds;
}

public long getMaxConnectionAge(TimeUnit timeUnit) {
return timeUnit.convert(this.maxConnectionAgeInSeconds, TimeUnit.SECONDS);
}

@Deprecated
public void setMaxConnectionAge(long maxConnectionAgeInSeconds) {
logger.warn("Please use setmaxConnectionAgeInSecondsInSeconds in place of setMaxConnectionAge. This method has been deprecated.");
this.maxConnectionAgeInSeconds = maxConnectionAgeInSeconds;
}

public void setMaxConnectionAgeInSeconds(long maxConnectionAgeInSeconds) {
setMaxConnectionAge(maxConnectionAgeInSeconds, TimeUnit.SECONDS);
}

public void setMaxConnectionAge(long maxConnectionAge, TimeUnit timeUnit) {
this.maxConnectionAgeInSeconds = TimeUnit.SECONDS.convert(maxConnectionAge, timeUnit);
}

public String getConfigFile() {
return this.configFile;
}

public void setConfigFile(String configFile) {
this.configFile = configFile;
}

public String getServiceOrder() {
return this.serviceOrder;
}

public void setServiceOrder(String serviceOrder) {
this.serviceOrder = serviceOrder;
}

public boolean isStatisticsEnabled() {
return this.statisticsEnabled;
}

public void setStatisticsEnabled(boolean statisticsEnabled) {
this.statisticsEnabled = statisticsEnabled;
}

public Boolean getDefaultAutoCommit() {
return this.defaultAutoCommit;
}

public void setDefaultAutoCommit(Boolean defaultAutoCommit) {
this.defaultAutoCommit = defaultAutoCommit;
}

public Boolean getDefaultReadOnly() {
return this.defaultReadOnly;
}

public void setDefaultReadOnly(Boolean defaultReadOnly) {
this.defaultReadOnly = defaultReadOnly;
}

public String getDefaultCatalog() {
return this.defaultCatalog;
}

public void setDefaultCatalog(String defaultCatalog) {
this.defaultCatalog = defaultCatalog;
}

public String getDefaultTransactionIsolation() {
return this.defaultTransactionIsolation;
}

public void setDefaultTransactionIsolation(String defaultTransactionIsolation) {
this.defaultTransactionIsolation = defaultTransactionIsolation;
}

protected int getDefaultTransactionIsolationValue() {
return this.defaultTransactionIsolationValue;
}

protected void setDefaultTransactionIsolationValue(int defaultTransactionIsolationValue) {
this.defaultTransactionIsolationValue = defaultTransactionIsolationValue;
}

public BoneCPConfig() {
loadProperties("/bonecp-default-config.xml");

loadProperties("/bonecp-config.xml");
}

public BoneCPConfig(Properties props) throws Exception {
this();
setProperties(props);
}

public BoneCPConfig(String sectionName) throws Exception {
this(BoneCPConfig.class.getResourceAsStream("/bonecp-config.xml"), sectionName);
}

public BoneCPConfig(InputStream xmlConfigFile, String sectionName) throws Exception {
this();
setXMLProperties(xmlConfigFile, sectionName);
}

private void setXMLProperties(InputStream xmlConfigFile, String sectionName) throws Exception {
DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

try {
DocumentBuilder db = dbf.newDocumentBuilder();
Document doc = db.parse(xmlConfigFile);
doc.getDocumentElement().normalize();

Properties settings = parseXML(doc, null);
if (sectionName != null)
{
settings.putAll(parseXML(doc, sectionName));
}

setProperties(settings);
}
catch (Exception e) {
throw e;
} 
}

private String lowerFirst(String name) {
return name.substring(0, 1).toLowerCase() + name.substring(1);
}

public void setProperties(Properties props) throws Exception {
for (Method method : BoneCPConfig.class.getDeclaredMethods()) {
String tmp = null;
if (method.getName().startsWith("is")) {
tmp = lowerFirst(method.getName().substring(2));
} else if (method.getName().startsWith("set")) {
tmp = lowerFirst(method.getName().substring(3));
} else {
continue;
} 

if ((method.getParameterTypes()).length == 1 && method.getParameterTypes()[0].equals(int.class)) {
String val = props.getProperty(tmp);
if (val == null) {
val = props.getProperty("bonecp." + tmp);
}
if (val != null) {
try {
method.invoke(this, new Object[] { Integer.valueOf(Integer.parseInt(val)) });
} catch (NumberFormatException e) {}

}
}
else if ((method.getParameterTypes()).length == 1 && method.getParameterTypes()[0].equals(long.class)) {
String val = props.getProperty(tmp);
if (val == null) {
val = props.getProperty("bonecp." + tmp);
}
if (val != null) {
try {
method.invoke(this, new Object[] { Long.valueOf(Long.parseLong(val)) });
} catch (NumberFormatException e) {}

}
}
else if ((method.getParameterTypes()).length == 1 && method.getParameterTypes()[0].equals(String.class)) {
String val = props.getProperty(tmp);
if (val == null) {
val = props.getProperty("bonecp." + tmp);
}
if (val != null)
method.invoke(this, new Object[] { val }); 
} 
if ((method.getParameterTypes()).length == 1 && method.getParameterTypes()[0].equals(boolean.class)) {
String val = props.getProperty(tmp);
if (val == null) {
val = props.getProperty("bonecp." + tmp);
}
if (val != null) {
method.invoke(this, new Object[] { Boolean.valueOf(Boolean.parseBoolean(val)) });
}
} 
continue;
} 
}

private Properties parseXML(Document doc, String sectionName) {
int found = -1;
Properties results = new Properties();
NodeList config = null;
if (sectionName == null) {
config = doc.getElementsByTagName("default-config");
found = 0;
} else {
config = doc.getElementsByTagName("named-config");
if (config != null && config.getLength() > 0) {
for (int i = 0; i < config.getLength(); i++) {
Node node = config.item(i);
if (node.getNodeType() == 1) {
NamedNodeMap attributes = node.getAttributes();
if (attributes != null && attributes.getLength() > 0) {
Node name = attributes.getNamedItem("name");
if (name.getNodeValue().equalsIgnoreCase(sectionName)) {
found = i;

break;
} 
} 
} 
} 
}
if (found == -1) {
config = null;
logger.warn("Did not find " + sectionName + " section in config file. Reverting to defaults.");
} 
} 

if (config != null && config.getLength() > 0) {
Node node = config.item(found);
if (node.getNodeType() == 1) {
Element elementEntry = (Element)node;
NodeList childNodeList = elementEntry.getChildNodes();
for (int j = 0; j < childNodeList.getLength(); j++) {
Node node_j = childNodeList.item(j);
if (node_j.getNodeType() == 1) {
Element piece = (Element)node_j;
NamedNodeMap attributes = piece.getAttributes();
if (attributes != null && attributes.getLength() > 0) {
results.put(attributes.item(0).getNodeValue(), piece.getTextContent());
}
} 
} 
} 
} 

return results;
}

public boolean isExternalAuth() {
return this.externalAuth;
}

public void setExternalAuth(boolean externalAuth) {
this.externalAuth = externalAuth;
}

public void sanitize() {
if (this.configFile != null) {
loadProperties(this.configFile);
}

if (this.poolAvailabilityThreshold < 0 || this.poolAvailabilityThreshold > 100) {
this.poolAvailabilityThreshold = 20;
}

if (this.defaultTransactionIsolation != null) {
this.defaultTransactionIsolation = this.defaultTransactionIsolation.trim().toUpperCase();

if (this.defaultTransactionIsolation.equals("NONE")) {
this.defaultTransactionIsolationValue = 0;
} else if (this.defaultTransactionIsolation.equals("READ_COMMITTED") || this.defaultTransactionIsolation.equals("READ COMMITTED")) {
this.defaultTransactionIsolationValue = 2;
} else if (this.defaultTransactionIsolation.equals("REPEATABLE_READ") || this.defaultTransactionIsolation.equals("REPEATABLE READ")) {
this.defaultTransactionIsolationValue = 4;
} else if (this.defaultTransactionIsolation.equals("READ_UNCOMMITTED") || this.defaultTransactionIsolation.equals("READ UNCOMMITTED")) {
this.defaultTransactionIsolationValue = 1;
} else if (this.defaultTransactionIsolation.equals("SERIALIZABLE")) {
this.defaultTransactionIsolationValue = 8;
} else {
logger.warn("Unrecognized defaultTransactionIsolation value. Using driver default.");
this.defaultTransactionIsolationValue = -1;
} 
} 
if (this.maxConnectionsPerPartition < 1) {
logger.warn("Max Connections < 1. Setting to 20");
this.maxConnectionsPerPartition = 20;
} 
if (this.minConnectionsPerPartition < 0) {
logger.warn("Min Connections < 0. Setting to 1");
this.minConnectionsPerPartition = 1;
} 

if (this.minConnectionsPerPartition > this.maxConnectionsPerPartition) {
logger.warn("Min Connections > max connections");
this.minConnectionsPerPartition = this.maxConnectionsPerPartition;
} 
if (this.acquireIncrement <= 0) {
logger.warn("acquireIncrement <= 0. Setting to 1.");
this.acquireIncrement = 1;
} 
if (this.partitionCount < 1) {
logger.warn("partitions < 1! Setting to 1");
this.partitionCount = 1;
} 

if (this.releaseHelperThreads < 0) {
logger.warn("releaseHelperThreads < 0! Setting to 3");
this.releaseHelperThreads = 3;
} 

if (this.statementReleaseHelperThreads < 0) {
logger.warn("statementReleaseHelperThreads < 0! Setting to 3");
this.statementReleaseHelperThreads = 3;
} 

if (this.statementsCacheSize < 0) {
logger.warn("preparedStatementsCacheSize < 0! Setting to 0");
this.statementsCacheSize = 0;
} 

if (this.acquireRetryDelayInMs <= 0L) {
this.acquireRetryDelayInMs = 1000L;
}

if (!this.externalAuth && this.datasourceBean == null && this.driverProperties == null && (this.jdbcUrl == null || this.jdbcUrl.trim().equals("")))
{
logger.warn("JDBC url was not set in config!");
}

if (!this.externalAuth && this.datasourceBean == null && this.driverProperties == null && (this.username == null || this.username.trim().equals("")))
{
logger.warn("JDBC username was not set in config!");
}

if (!this.externalAuth && this.datasourceBean == null && this.driverProperties == null && this.password == null) {
logger.warn("JDBC password was not set in config!");
}

if (!this.externalAuth && this.datasourceBean == null && this.driverProperties != null) {
if (this.driverProperties.get("user") == null && this.username == null) {
logger.warn("JDBC username not set in driver properties and not set in pool config either");
} else if (this.driverProperties.get("user") == null && this.username != null) {
logger.warn("JDBC username not set in driver properties, copying it from pool config");
this.driverProperties.setProperty("user", this.username);
} else if (this.username != null && !this.driverProperties.get("user").equals(this.username)) {
logger.warn("JDBC username set in driver properties does not match the one set in the pool config.  Overriding it with pool config.");
this.driverProperties.setProperty("user", this.username);
} 
}

if (!this.externalAuth && this.datasourceBean == null && this.driverProperties != null) {
if (this.driverProperties.get("password") == null && this.password == null) {
logger.warn("JDBC password not set in driver properties and not set in pool config either");
} else if (this.driverProperties.get("password") == null && this.password != null) {
logger.warn("JDBC password not set in driver properties, copying it from pool config");
this.driverProperties.setProperty("password", this.password);
} else if (this.password != null && !this.driverProperties.get("password").equals(this.password)) {
logger.warn("JDBC password set in driver properties does not match the one set in the pool config. Overriding it with pool config.");
this.driverProperties.setProperty("password", this.password);
} 

this.username = this.driverProperties.getProperty("user");
this.password = this.driverProperties.getProperty("password");
} 

if (this.username != null) {
this.username = this.username.trim();
}
if (this.jdbcUrl != null) {
this.jdbcUrl = this.jdbcUrl.trim();
}
if (this.password != null) {
this.password = this.password.trim();
}

if (this.connectionTestStatement != null) {
this.connectionTestStatement = this.connectionTestStatement.trim();
}

this.serviceOrder = (this.serviceOrder != null) ? this.serviceOrder.toUpperCase() : "FIFO";

if (!this.serviceOrder.equals("FIFO") && !this.serviceOrder.equals("LIFO")) {
logger.warn("Queue service order is not set to FIFO or LIFO. Defaulting to FIFO.");
this.serviceOrder = "FIFO";
} 
}

protected void loadProperties(String filename) {
ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
URL url = classLoader.getResource(filename);
if (url != null) {
try {
setXMLProperties(url.openStream(), null);
} catch (Exception e) {}
}
}

public String toString() {
String result = null;
if (this.datasourceBean != null) {
result = String.format("JDBC URL = (via datasource bean), Username = (via datasource bean), partitions = %d, max (per partition) = %d, min (per partition) = %d, helper threads = %d, idle max age = %d min, idle test period = %d min", new Object[] { Integer.valueOf(this.partitionCount), Integer.valueOf(this.maxConnectionsPerPartition), Integer.valueOf(this.minConnectionsPerPartition), Integer.valueOf(this.releaseHelperThreads), Long.valueOf(getIdleMaxAgeInMinutes()), Long.valueOf(getIdleConnectionTestPeriodInMinutes()) });
}
else {

result = String.format("JDBC URL = %s, Username = %s, partitions = %d, max (per partition) = %d, min (per partition) = %d, helper threads = %d, idle max age = %d min, idle test period = %d min", new Object[] { this.jdbcUrl, this.username, Integer.valueOf(this.partitionCount), Integer.valueOf(this.maxConnectionsPerPartition), Integer.valueOf(this.minConnectionsPerPartition), Integer.valueOf(this.releaseHelperThreads), Long.valueOf(getIdleMaxAgeInMinutes()), Long.valueOf(getIdleConnectionTestPeriodInMinutes()) });
} 

return result;
}

protected Class<?> loadClass(String clazz) throws ClassNotFoundException {
if (this.classLoader == null) {
return Class.forName(clazz);
}

return Class.forName(clazz, true, this.classLoader);
}

public ClassLoader getClassLoader() {
return this.classLoader;
}

public void setClassLoader(ClassLoader classLoader) {
this.classLoader = classLoader;
}

public BoneCPConfig clone() throws CloneNotSupportedException {
BoneCPConfig clone = (BoneCPConfig)super.clone();
Field[] fields = getClass().getDeclaredFields();
for (Field field : fields) {
try {
field.set(clone, field.get(this));
} catch (Exception e) {}
} 

return clone;
}

public boolean hasSameConfiguration(BoneCPConfig that) {
if (that != null && Objects.equal(Integer.valueOf(this.acquireIncrement), Integer.valueOf(that.getAcquireIncrement())) && Objects.equal(Long.valueOf(this.acquireRetryDelayInMs), Long.valueOf(that.getAcquireRetryDelayInMs())) && Objects.equal(Boolean.valueOf(this.closeConnectionWatch), Boolean.valueOf(that.isCloseConnectionWatch())) && Objects.equal(Boolean.valueOf(this.logStatementsEnabled), Boolean.valueOf(that.isLogStatementsEnabled())) && Objects.equal(this.connectionHook, that.getConnectionHook()) && Objects.equal(this.connectionTestStatement, that.getConnectionTestStatement()) && Objects.equal(Long.valueOf(this.idleConnectionTestPeriodInSeconds), Long.valueOf(that.getIdleConnectionTestPeriod(TimeUnit.SECONDS))) && Objects.equal(Long.valueOf(this.idleMaxAgeInSeconds), Long.valueOf(that.getIdleMaxAge(TimeUnit.SECONDS))) && Objects.equal(this.initSQL, that.getInitSQL()) && Objects.equal(this.jdbcUrl, that.getJdbcUrl()) && Objects.equal(Integer.valueOf(this.maxConnectionsPerPartition), Integer.valueOf(that.getMaxConnectionsPerPartition())) && Objects.equal(Integer.valueOf(this.minConnectionsPerPartition), Integer.valueOf(that.getMinConnectionsPerPartition())) && Objects.equal(Integer.valueOf(this.partitionCount), Integer.valueOf(that.getPartitionCount())) && Objects.equal(Integer.valueOf(this.releaseHelperThreads), Integer.valueOf(that.getReleaseHelperThreads())) && Objects.equal(Integer.valueOf(this.statementsCacheSize), Integer.valueOf(that.getStatementsCacheSize())) && Objects.equal(this.username, that.getUsername()) && Objects.equal(this.password, that.getPassword()) && Objects.equal(Boolean.valueOf(this.lazyInit), Boolean.valueOf(that.isLazyInit())) && Objects.equal(Boolean.valueOf(this.transactionRecoveryEnabled), Boolean.valueOf(that.isTransactionRecoveryEnabled())) && Objects.equal(Integer.valueOf(this.acquireRetryAttempts), Integer.valueOf(that.getAcquireRetryAttempts())) && Objects.equal(Integer.valueOf(this.statementReleaseHelperThreads), Integer.valueOf(that.getStatementReleaseHelperThreads())) && Objects.equal(Long.valueOf(this.closeConnectionWatchTimeoutInMs), Long.valueOf(that.getCloseConnectionWatchTimeout())) && Objects.equal(Long.valueOf(this.connectionTimeoutInMs), Long.valueOf(that.getConnectionTimeoutInMs())) && Objects.equal(this.datasourceBean, that.getDatasourceBean()) && Objects.equal(Long.valueOf(getQueryExecuteTimeLimitInMs()), Long.valueOf(that.getQueryExecuteTimeLimitInMs())) && Objects.equal(Integer.valueOf(this.poolAvailabilityThreshold), Integer.valueOf(that.getPoolAvailabilityThreshold())) && Objects.equal(this.poolName, that.getPoolName()) && Objects.equal(Boolean.valueOf(this.disableConnectionTracking), Boolean.valueOf(that.isDisableConnectionTracking())))
{

return true;
}

return false;
}
}

