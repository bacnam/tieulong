/*      */ package com.jolbox.bonecp;
/*      */ 
/*      */ import com.google.common.base.Objects;
/*      */ import com.jolbox.bonecp.hooks.ConnectionHook;
/*      */ import java.io.InputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Method;
/*      */ import java.net.URL;
/*      */ import java.util.Properties;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import javax.sql.DataSource;
/*      */ import javax.xml.parsers.DocumentBuilder;
/*      */ import javax.xml.parsers.DocumentBuilderFactory;
/*      */ import org.slf4j.Logger;
/*      */ import org.slf4j.LoggerFactory;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.NamedNodeMap;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ public class BoneCPConfig
/*      */   implements BoneCPConfigMBean, Cloneable, Serializable
/*      */ {
/*      */   private static final String USER = "user";
/*      */   private static final String PASSWORD = "password";
/*      */   private static final long serialVersionUID = 6090570773474131622L;
/*      */   private static final String CONFIG_TOSTRING = "JDBC URL = %s, Username = %s, partitions = %d, max (per partition) = %d, min (per partition) = %d, helper threads = %d, idle max age = %d min, idle test period = %d min";
/*      */   private static final String CONFIG_DS_TOSTRING = "JDBC URL = (via datasource bean), Username = (via datasource bean), partitions = %d, max (per partition) = %d, min (per partition) = %d, helper threads = %d, idle max age = %d min, idle test period = %d min";
/*   60 */   private static final Logger logger = LoggerFactory.getLogger(BoneCPConfig.class);
/*      */   
/*      */   private int minConnectionsPerPartition;
/*      */   
/*      */   private int maxConnectionsPerPartition;
/*      */   
/*   66 */   private int acquireIncrement = 2;
/*      */   
/*   68 */   private int partitionCount = 1;
/*      */   
/*      */   private String jdbcUrl;
/*      */   
/*      */   private String username;
/*      */   
/*      */   private String password;
/*      */   
/*   76 */   private long idleConnectionTestPeriodInSeconds = 14400L;
/*      */   
/*   78 */   private long idleMaxAgeInSeconds = 3600L;
/*      */   
/*      */   private String connectionTestStatement;
/*      */   
/*   82 */   private int statementsCacheSize = 0;
/*      */   
/*   84 */   private int statementsCachedPerConnection = 0;
/*      */   
/*   86 */   private int releaseHelperThreads = 3;
/*      */   
/*   88 */   private int statementReleaseHelperThreads = 0;
/*      */ 
/*      */   
/*      */   private ConnectionHook connectionHook;
/*      */ 
/*      */   
/*      */   private String initSQL;
/*      */   
/*      */   private boolean closeConnectionWatch;
/*      */   
/*      */   private boolean logStatementsEnabled;
/*      */   
/*  100 */   private long acquireRetryDelayInMs = 7000L;
/*      */   
/*  102 */   private int acquireRetryAttempts = 5;
/*      */   
/*      */   private boolean lazyInit;
/*      */   
/*      */   private boolean transactionRecoveryEnabled;
/*      */   
/*      */   private String connectionHookClassName;
/*      */   
/*  110 */   private ClassLoader classLoader = getClassLoader();
/*      */   
/*      */   private String poolName;
/*      */   
/*      */   private boolean disableJMX;
/*      */   
/*      */   private DataSource datasourceBean;
/*      */   
/*  118 */   private long queryExecuteTimeLimitInMs = 0L;
/*      */   
/*  120 */   private int poolAvailabilityThreshold = 20;
/*      */   
/*      */   private boolean disableConnectionTracking;
/*      */   
/*      */   private Properties driverProperties;
/*      */   
/*  126 */   private long connectionTimeoutInMs = 0L;
/*      */   
/*  128 */   private long closeConnectionWatchTimeoutInMs = 0L;
/*      */   
/*  130 */   private long maxConnectionAgeInSeconds = 0L;
/*      */   
/*      */   private String configFile;
/*      */   
/*      */   private String serviceOrder;
/*      */   
/*      */   private boolean statisticsEnabled;
/*      */   
/*      */   private Boolean defaultAutoCommit;
/*      */   
/*      */   private Boolean defaultReadOnly;
/*      */   
/*      */   private String defaultTransactionIsolation;
/*      */   
/*      */   private String defaultCatalog;
/*      */   
/*  146 */   private int defaultTransactionIsolationValue = -1;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean externalAuth;
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPoolName() {
/*  155 */     return this.poolName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPoolName(String poolName) {
/*  162 */     this.poolName = poolName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMinConnectionsPerPartition() {
/*  169 */     return this.minConnectionsPerPartition;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMinConnectionsPerPartition(int minConnectionsPerPartition) {
/*  178 */     this.minConnectionsPerPartition = minConnectionsPerPartition;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxConnectionsPerPartition() {
/*  185 */     return this.maxConnectionsPerPartition;
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
/*      */   public void setMaxConnectionsPerPartition(int maxConnectionsPerPartition) {
/*  197 */     this.maxConnectionsPerPartition = maxConnectionsPerPartition;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getAcquireIncrement() {
/*  204 */     return this.acquireIncrement;
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
/*      */   
/*      */   public void setAcquireIncrement(int acquireIncrement) {
/*  217 */     this.acquireIncrement = acquireIncrement;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getPartitionCount() {
/*  224 */     return this.partitionCount;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPartitionCount(int partitionCount) {
/*  241 */     this.partitionCount = partitionCount;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getJdbcUrl() {
/*  248 */     return this.jdbcUrl;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setJdbcUrl(String jdbcUrl) {
/*  257 */     this.jdbcUrl = jdbcUrl;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getUsername() {
/*  265 */     return this.username;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUsername(String username) {
/*  274 */     this.username = username;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPassword() {
/*  283 */     return this.password;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPassword(String password) {
/*  292 */     this.password = password;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public long getIdleConnectionTestPeriod() {
/*  303 */     logger.warn("Please use getIdleConnectionTestPeriodInMinutes in place of getIdleConnectionTestPeriod. This method has been deprecated.");
/*  304 */     return getIdleConnectionTestPeriodInMinutes();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setIdleConnectionTestPeriod(long idleConnectionTestPeriod) {
/*  315 */     logger.warn("Please use setIdleConnectionTestPeriodInMinutes in place of setIdleConnectionTestPeriod. This method has been deprecated.");
/*  316 */     setIdleConnectionTestPeriod(idleConnectionTestPeriod, TimeUnit.MINUTES);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getIdleConnectionTestPeriodInMinutes() {
/*  323 */     return TimeUnit.MINUTES.convert(this.idleConnectionTestPeriodInSeconds, TimeUnit.SECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getIdleConnectionTestPeriod(TimeUnit timeUnit) {
/*  333 */     return timeUnit.convert(this.idleConnectionTestPeriodInSeconds, TimeUnit.SECONDS);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIdleConnectionTestPeriodInMinutes(long idleConnectionTestPeriod) {
/*  349 */     setIdleConnectionTestPeriod(idleConnectionTestPeriod, TimeUnit.MINUTES);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIdleConnectionTestPeriodInSeconds(long idleConnectionTestPeriod) {
/*  365 */     setIdleConnectionTestPeriod(idleConnectionTestPeriod, TimeUnit.SECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIdleConnectionTestPeriod(long idleConnectionTestPeriod, TimeUnit timeUnit) {
/*  374 */     this.idleConnectionTestPeriodInSeconds = TimeUnit.SECONDS.convert(idleConnectionTestPeriod, timeUnit);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public long getIdleMaxAge() {
/*  383 */     logger.warn("Please use getIdleMaxAgeInMinutes in place of getIdleMaxAge. This method has been deprecated.");
/*  384 */     return getIdleMaxAgeInMinutes();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getIdleMaxAge(TimeUnit timeUnit) {
/*  394 */     return timeUnit.convert(this.idleMaxAgeInSeconds, TimeUnit.SECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getIdleMaxAgeInMinutes() {
/*  401 */     return TimeUnit.MINUTES.convert(this.idleMaxAgeInSeconds, TimeUnit.SECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setIdleMaxAge(long idleMaxAge) {
/*  412 */     logger.warn("Please use setIdleMaxAgeInMinutes in place of setIdleMaxAge. This method has been deprecated.");
/*  413 */     setIdleMaxAgeInMinutes(idleMaxAge);
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
/*      */   
/*      */   public void setIdleMaxAgeInMinutes(long idleMaxAge) {
/*  426 */     setIdleMaxAge(idleMaxAge, TimeUnit.MINUTES);
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
/*      */   
/*      */   public void setIdleMaxAgeInSeconds(long idleMaxAge) {
/*  439 */     setIdleMaxAge(idleMaxAge, TimeUnit.SECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIdleMaxAge(long idleMaxAge, TimeUnit timeUnit) {
/*  448 */     this.idleMaxAgeInSeconds = TimeUnit.SECONDS.convert(idleMaxAge, timeUnit);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getConnectionTestStatement() {
/*  457 */     return this.connectionTestStatement;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConnectionTestStatement(String connectionTestStatement) {
/*  475 */     this.connectionTestStatement = connectionTestStatement;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public int getPreparedStatementsCacheSize() {
/*  483 */     logger.warn("Please use getStatementsCacheSize in place of getPreparedStatementsCacheSize. This method has been deprecated.");
/*  484 */     return this.statementsCacheSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public int getPreparedStatementCacheSize() {
/*  492 */     logger.warn("Please use getStatementsCacheSize in place of getPreparedStatementCacheSize. This method has been deprecated.");
/*  493 */     return this.statementsCacheSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setPreparedStatementsCacheSize(int preparedStatementsCacheSize) {
/*  503 */     logger.warn("Please use setStatementsCacheSize in place of setPreparedStatementsCacheSize. This method has been deprecated.");
/*  504 */     this.statementsCacheSize = preparedStatementsCacheSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setStatementsCacheSize(int statementsCacheSize) {
/*  515 */     this.statementsCacheSize = statementsCacheSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getStatementsCacheSize() {
/*  522 */     return this.statementsCacheSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setStatementCacheSize(int statementsCacheSize) {
/*  534 */     logger.warn("Please use setStatementsCacheSize in place of setStatementCacheSize. This method has been deprecated.");
/*  535 */     this.statementsCacheSize = statementsCacheSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public int getStatementCacheSize() {
/*  543 */     logger.warn("Please use getStatementsCacheSize in place of getStatementCacheSize. This method has been deprecated.");
/*  544 */     return this.statementsCacheSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getReleaseHelperThreads() {
/*  552 */     return this.releaseHelperThreads;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setReleaseHelperThreads(int releaseHelperThreads) {
/*  573 */     this.releaseHelperThreads = releaseHelperThreads;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public int getStatementsCachedPerConnection() {
/*  581 */     return this.statementsCachedPerConnection;
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
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setStatementsCachedPerConnection(int statementsCachedPerConnection) {
/*  596 */     this.statementsCachedPerConnection = statementsCachedPerConnection;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConnectionHook getConnectionHook() {
/*  605 */     return this.connectionHook;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConnectionHook(ConnectionHook connectionHook) {
/*  616 */     this.connectionHook = connectionHook;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getInitSQL() {
/*  623 */     return this.initSQL;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInitSQL(String initSQL) {
/*  630 */     this.initSQL = initSQL;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCloseConnectionWatch() {
/*  639 */     return this.closeConnectionWatch;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCloseConnectionWatch(boolean closeConnectionWatch) {
/*  648 */     this.closeConnectionWatch = closeConnectionWatch;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLogStatementsEnabled() {
/*  655 */     return this.logStatementsEnabled;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLogStatementsEnabled(boolean logStatementsEnabled) {
/*  662 */     this.logStatementsEnabled = logStatementsEnabled;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public long getAcquireRetryDelay() {
/*  672 */     logger.warn("Please use getAcquireRetryDelayInMs in place of getAcquireRetryDelay. This method has been deprecated.");
/*  673 */     return this.acquireRetryDelayInMs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setAcquireRetryDelay(int acquireRetryDelayInMs) {
/*  683 */     logger.warn("Please use setAcquireRetryDelayInMs in place of setAcquireRetryDelay. This method has been deprecated.");
/*  684 */     this.acquireRetryDelayInMs = acquireRetryDelayInMs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getAcquireRetryDelayInMs() {
/*  691 */     return this.acquireRetryDelayInMs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getAcquireRetryDelay(TimeUnit timeUnit) {
/*  701 */     return timeUnit.convert(this.acquireRetryDelayInMs, TimeUnit.MILLISECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAcquireRetryDelayInMs(long acquireRetryDelay) {
/*  709 */     setAcquireRetryDelay(acquireRetryDelay, TimeUnit.MILLISECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAcquireRetryDelay(long acquireRetryDelay, TimeUnit timeUnit) {
/*  719 */     this.acquireRetryDelayInMs = TimeUnit.MILLISECONDS.convert(acquireRetryDelay, timeUnit);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLazyInit() {
/*  726 */     return this.lazyInit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLazyInit(boolean lazyInit) {
/*  733 */     this.lazyInit = lazyInit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTransactionRecoveryEnabled() {
/*  744 */     return this.transactionRecoveryEnabled;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTransactionRecoveryEnabled(boolean transactionRecoveryEnabled) {
/*  752 */     this.transactionRecoveryEnabled = transactionRecoveryEnabled;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getAcquireRetryAttempts() {
/*  759 */     return this.acquireRetryAttempts;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAcquireRetryAttempts(int acquireRetryAttempts) {
/*  766 */     this.acquireRetryAttempts = acquireRetryAttempts;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConnectionHookClassName(String connectionHookClassName) {
/*  773 */     this.connectionHookClassName = connectionHookClassName;
/*  774 */     if (connectionHookClassName != null) {
/*      */       
/*      */       try {
/*  777 */         Object hookClass = loadClass(connectionHookClassName).newInstance();
/*  778 */         this.connectionHook = (ConnectionHook)hookClass;
/*  779 */       } catch (Exception e) {
/*  780 */         logger.error("Unable to create an instance of the connection hook class (" + connectionHookClassName + ")");
/*  781 */         this.connectionHook = null;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getConnectionHookClassName() {
/*  790 */     return this.connectionHookClassName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDisableJMX() {
/*  798 */     return this.disableJMX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDisableJMX(boolean disableJMX) {
/*  805 */     this.disableJMX = disableJMX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DataSource getDatasourceBean() {
/*  812 */     return this.datasourceBean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDatasourceBean(DataSource datasourceBean) {
/*  819 */     this.datasourceBean = datasourceBean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public long getQueryExecuteTimeLimit() {
/*  829 */     logger.warn("Please use getQueryExecuteTimeLimitInMs in place of getQueryExecuteTimeLimit. This method has been deprecated.");
/*  830 */     return this.queryExecuteTimeLimitInMs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setQueryExecuteTimeLimit(int queryExecuteTimeLimit) {
/*  839 */     logger.warn("Please use setQueryExecuteTimeLimitInMs in place of setQueryExecuteTimeLimit. This method has been deprecated.");
/*  840 */     setQueryExecuteTimeLimit(queryExecuteTimeLimit, TimeUnit.MILLISECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getQueryExecuteTimeLimitInMs() {
/*  847 */     return this.queryExecuteTimeLimitInMs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getQueryExecuteTimeLimit(TimeUnit timeUnit) {
/*  857 */     return timeUnit.convert(this.queryExecuteTimeLimitInMs, TimeUnit.MILLISECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setQueryExecuteTimeLimitInMs(long queryExecuteTimeLimit) {
/*  865 */     setQueryExecuteTimeLimit(queryExecuteTimeLimit, TimeUnit.MILLISECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setQueryExecuteTimeLimit(long queryExecuteTimeLimit, TimeUnit timeUnit) {
/*  873 */     this.queryExecuteTimeLimitInMs = TimeUnit.MILLISECONDS.convert(queryExecuteTimeLimit, timeUnit);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getPoolAvailabilityThreshold() {
/*  882 */     return this.poolAvailabilityThreshold;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPoolAvailabilityThreshold(int poolAvailabilityThreshold) {
/*  900 */     this.poolAvailabilityThreshold = poolAvailabilityThreshold;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDisableConnectionTracking() {
/*  907 */     return this.disableConnectionTracking;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDisableConnectionTracking(boolean disableConnectionTracking) {
/*  917 */     this.disableConnectionTracking = disableConnectionTracking;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public long getConnectionTimeout() {
/*  927 */     logger.warn("Please use getConnectionTimeoutInMs in place of getConnectionTimeout. This method has been deprecated.");
/*  928 */     return this.connectionTimeoutInMs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setConnectionTimeout(long connectionTimeout) {
/*  938 */     logger.warn("Please use setConnectionTimeoutInMs in place of setConnectionTimeout. This method has been deprecated.");
/*  939 */     this.connectionTimeoutInMs = connectionTimeout;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getConnectionTimeoutInMs() {
/*  946 */     return this.connectionTimeoutInMs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getConnectionTimeout(TimeUnit timeUnit) {
/*  956 */     return timeUnit.convert(this.connectionTimeoutInMs, TimeUnit.MILLISECONDS);
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
/*      */   
/*      */   public void setConnectionTimeoutInMs(long connectionTimeoutinMs) {
/*  969 */     setConnectionTimeout(connectionTimeoutinMs, TimeUnit.MILLISECONDS);
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
/*      */   public void setConnectionTimeout(long connectionTimeout, TimeUnit timeUnit) {
/*  981 */     this.connectionTimeoutInMs = TimeUnit.MILLISECONDS.convert(connectionTimeout, timeUnit);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Properties getDriverProperties() {
/*  988 */     return this.driverProperties;
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDriverProperties(Properties driverProperties) {
/* 1004 */     if (driverProperties != null) {
/*      */ 
/*      */       
/* 1007 */       this.driverProperties = new Properties();
/* 1008 */       this.driverProperties.putAll(driverProperties);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public long getCloseConnectionWatchTimeout() {
/* 1018 */     logger.warn("Please use getCloseConnectionWatchTimeoutInMs in place of getCloseConnectionWatchTimeout. This method has been deprecated.");
/* 1019 */     return this.closeConnectionWatchTimeoutInMs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setCloseConnectionWatchTimeout(long closeConnectionWatchTimeout) {
/* 1028 */     logger.warn("Please use setCloseConnectionWatchTimeoutInMs in place of setCloseConnectionWatchTimeout. This method has been deprecated.");
/* 1029 */     setCloseConnectionWatchTimeoutInMs(closeConnectionWatchTimeout);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getCloseConnectionWatchTimeoutInMs() {
/* 1036 */     return this.closeConnectionWatchTimeoutInMs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getCloseConnectionWatchTimeout(TimeUnit timeUnit) {
/* 1046 */     return timeUnit.convert(this.closeConnectionWatchTimeoutInMs, TimeUnit.MILLISECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCloseConnectionWatchTimeoutInMs(long closeConnectionWatchTimeout) {
/* 1054 */     setCloseConnectionWatchTimeout(closeConnectionWatchTimeout, TimeUnit.MILLISECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCloseConnectionWatchTimeout(long closeConnectionWatchTimeout, TimeUnit timeUnit) {
/* 1062 */     this.closeConnectionWatchTimeoutInMs = TimeUnit.MILLISECONDS.convert(closeConnectionWatchTimeout, timeUnit);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getStatementReleaseHelperThreads() {
/* 1070 */     return this.statementReleaseHelperThreads;
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setStatementReleaseHelperThreads(int statementReleaseHelperThreads) {
/* 1086 */     this.statementReleaseHelperThreads = statementReleaseHelperThreads;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public long getMaxConnectionAge() {
/* 1098 */     logger.warn("Please use getMaxConnectionAgeInSeconds in place of getMaxConnectionAge. This method has been deprecated.");
/* 1099 */     return this.maxConnectionAgeInSeconds;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getMaxConnectionAgeInSeconds() {
/* 1107 */     return this.maxConnectionAgeInSeconds;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getMaxConnectionAge(TimeUnit timeUnit) {
/* 1117 */     return timeUnit.convert(this.maxConnectionAgeInSeconds, TimeUnit.SECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setMaxConnectionAge(long maxConnectionAgeInSeconds) {
/* 1129 */     logger.warn("Please use setmaxConnectionAgeInSecondsInSeconds in place of setMaxConnectionAge. This method has been deprecated.");
/* 1130 */     this.maxConnectionAgeInSeconds = maxConnectionAgeInSeconds;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaxConnectionAgeInSeconds(long maxConnectionAgeInSeconds) {
/* 1141 */     setMaxConnectionAge(maxConnectionAgeInSeconds, TimeUnit.SECONDS);
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
/*      */   public void setMaxConnectionAge(long maxConnectionAge, TimeUnit timeUnit) {
/* 1153 */     this.maxConnectionAgeInSeconds = TimeUnit.SECONDS.convert(maxConnectionAge, timeUnit);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getConfigFile() {
/* 1160 */     return this.configFile;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConfigFile(String configFile) {
/* 1169 */     this.configFile = configFile;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getServiceOrder() {
/* 1177 */     return this.serviceOrder;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setServiceOrder(String serviceOrder) {
/* 1185 */     this.serviceOrder = serviceOrder;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isStatisticsEnabled() {
/* 1193 */     return this.statisticsEnabled;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setStatisticsEnabled(boolean statisticsEnabled) {
/* 1202 */     this.statisticsEnabled = statisticsEnabled;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Boolean getDefaultAutoCommit() {
/* 1210 */     return this.defaultAutoCommit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDefaultAutoCommit(Boolean defaultAutoCommit) {
/* 1218 */     this.defaultAutoCommit = defaultAutoCommit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Boolean getDefaultReadOnly() {
/* 1226 */     return this.defaultReadOnly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDefaultReadOnly(Boolean defaultReadOnly) {
/* 1234 */     this.defaultReadOnly = defaultReadOnly;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDefaultCatalog() {
/* 1243 */     return this.defaultCatalog;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDefaultCatalog(String defaultCatalog) {
/* 1251 */     this.defaultCatalog = defaultCatalog;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDefaultTransactionIsolation() {
/* 1259 */     return this.defaultTransactionIsolation;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDefaultTransactionIsolation(String defaultTransactionIsolation) {
/* 1268 */     this.defaultTransactionIsolation = defaultTransactionIsolation;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int getDefaultTransactionIsolationValue() {
/* 1276 */     return this.defaultTransactionIsolationValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setDefaultTransactionIsolationValue(int defaultTransactionIsolationValue) {
/* 1284 */     this.defaultTransactionIsolationValue = defaultTransactionIsolationValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BoneCPConfig() {
/* 1295 */     loadProperties("/bonecp-default-config.xml");
/*      */     
/* 1297 */     loadProperties("/bonecp-config.xml");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BoneCPConfig(Properties props) throws Exception {
/* 1305 */     this();
/* 1306 */     setProperties(props);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BoneCPConfig(String sectionName) throws Exception {
/* 1315 */     this(BoneCPConfig.class.getResourceAsStream("/bonecp-config.xml"), sectionName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BoneCPConfig(InputStream xmlConfigFile, String sectionName) throws Exception {
/* 1324 */     this();
/* 1325 */     setXMLProperties(xmlConfigFile, sectionName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setXMLProperties(InputStream xmlConfigFile, String sectionName) throws Exception {
/* 1335 */     DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/*      */ 
/*      */     
/*      */     try {
/* 1339 */       DocumentBuilder db = dbf.newDocumentBuilder();
/* 1340 */       Document doc = db.parse(xmlConfigFile);
/* 1341 */       doc.getDocumentElement().normalize();
/*      */ 
/*      */       
/* 1344 */       Properties settings = parseXML(doc, null);
/* 1345 */       if (sectionName != null)
/*      */       {
/* 1347 */         settings.putAll(parseXML(doc, sectionName));
/*      */       }
/*      */       
/* 1350 */       setProperties(settings);
/*      */     }
/* 1352 */     catch (Exception e) {
/* 1353 */       throw e;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String lowerFirst(String name) {
/* 1363 */     return name.substring(0, 1).toLowerCase() + name.substring(1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setProperties(Properties props) throws Exception {
/* 1374 */     for (Method method : BoneCPConfig.class.getDeclaredMethods()) {
/* 1375 */       String tmp = null;
/* 1376 */       if (method.getName().startsWith("is")) {
/* 1377 */         tmp = lowerFirst(method.getName().substring(2));
/* 1378 */       } else if (method.getName().startsWith("set")) {
/* 1379 */         tmp = lowerFirst(method.getName().substring(3));
/*      */       } else {
/*      */         continue;
/*      */       } 
/*      */       
/* 1384 */       if ((method.getParameterTypes()).length == 1 && method.getParameterTypes()[0].equals(int.class)) {
/* 1385 */         String val = props.getProperty(tmp);
/* 1386 */         if (val == null) {
/* 1387 */           val = props.getProperty("bonecp." + tmp);
/*      */         }
/* 1389 */         if (val != null) {
/*      */           try {
/* 1391 */             method.invoke(this, new Object[] { Integer.valueOf(Integer.parseInt(val)) });
/* 1392 */           } catch (NumberFormatException e) {}
/*      */         
/*      */         }
/*      */       }
/* 1396 */       else if ((method.getParameterTypes()).length == 1 && method.getParameterTypes()[0].equals(long.class)) {
/* 1397 */         String val = props.getProperty(tmp);
/* 1398 */         if (val == null) {
/* 1399 */           val = props.getProperty("bonecp." + tmp);
/*      */         }
/* 1401 */         if (val != null) {
/*      */           try {
/* 1403 */             method.invoke(this, new Object[] { Long.valueOf(Long.parseLong(val)) });
/* 1404 */           } catch (NumberFormatException e) {}
/*      */         
/*      */         }
/*      */       }
/* 1408 */       else if ((method.getParameterTypes()).length == 1 && method.getParameterTypes()[0].equals(String.class)) {
/* 1409 */         String val = props.getProperty(tmp);
/* 1410 */         if (val == null) {
/* 1411 */           val = props.getProperty("bonecp." + tmp);
/*      */         }
/* 1413 */         if (val != null)
/* 1414 */           method.invoke(this, new Object[] { val }); 
/*      */       } 
/* 1416 */       if ((method.getParameterTypes()).length == 1 && method.getParameterTypes()[0].equals(boolean.class)) {
/* 1417 */         String val = props.getProperty(tmp);
/* 1418 */         if (val == null) {
/* 1419 */           val = props.getProperty("bonecp." + tmp);
/*      */         }
/* 1421 */         if (val != null) {
/* 1422 */           method.invoke(this, new Object[] { Boolean.valueOf(Boolean.parseBoolean(val)) });
/*      */         }
/*      */       } 
/*      */       continue;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Properties parseXML(Document doc, String sectionName) {
/* 1434 */     int found = -1;
/* 1435 */     Properties results = new Properties();
/* 1436 */     NodeList config = null;
/* 1437 */     if (sectionName == null) {
/* 1438 */       config = doc.getElementsByTagName("default-config");
/* 1439 */       found = 0;
/*      */     } else {
/* 1441 */       config = doc.getElementsByTagName("named-config");
/* 1442 */       if (config != null && config.getLength() > 0) {
/* 1443 */         for (int i = 0; i < config.getLength(); i++) {
/* 1444 */           Node node = config.item(i);
/* 1445 */           if (node.getNodeType() == 1) {
/* 1446 */             NamedNodeMap attributes = node.getAttributes();
/* 1447 */             if (attributes != null && attributes.getLength() > 0) {
/* 1448 */               Node name = attributes.getNamedItem("name");
/* 1449 */               if (name.getNodeValue().equalsIgnoreCase(sectionName)) {
/* 1450 */                 found = i;
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/* 1458 */       if (found == -1) {
/* 1459 */         config = null;
/* 1460 */         logger.warn("Did not find " + sectionName + " section in config file. Reverting to defaults.");
/*      */       } 
/*      */     } 
/*      */     
/* 1464 */     if (config != null && config.getLength() > 0) {
/* 1465 */       Node node = config.item(found);
/* 1466 */       if (node.getNodeType() == 1) {
/* 1467 */         Element elementEntry = (Element)node;
/* 1468 */         NodeList childNodeList = elementEntry.getChildNodes();
/* 1469 */         for (int j = 0; j < childNodeList.getLength(); j++) {
/* 1470 */           Node node_j = childNodeList.item(j);
/* 1471 */           if (node_j.getNodeType() == 1) {
/* 1472 */             Element piece = (Element)node_j;
/* 1473 */             NamedNodeMap attributes = piece.getAttributes();
/* 1474 */             if (attributes != null && attributes.getLength() > 0) {
/* 1475 */               results.put(attributes.item(0).getNodeValue(), piece.getTextContent());
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1482 */     return results;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isExternalAuth() {
/* 1490 */     return this.externalAuth;
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
/*      */   public void setExternalAuth(boolean externalAuth) {
/* 1502 */     this.externalAuth = externalAuth;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sanitize() {
/* 1510 */     if (this.configFile != null) {
/* 1511 */       loadProperties(this.configFile);
/*      */     }
/*      */     
/* 1514 */     if (this.poolAvailabilityThreshold < 0 || this.poolAvailabilityThreshold > 100) {
/* 1515 */       this.poolAvailabilityThreshold = 20;
/*      */     }
/*      */     
/* 1518 */     if (this.defaultTransactionIsolation != null) {
/* 1519 */       this.defaultTransactionIsolation = this.defaultTransactionIsolation.trim().toUpperCase();
/*      */       
/* 1521 */       if (this.defaultTransactionIsolation.equals("NONE")) {
/* 1522 */         this.defaultTransactionIsolationValue = 0;
/* 1523 */       } else if (this.defaultTransactionIsolation.equals("READ_COMMITTED") || this.defaultTransactionIsolation.equals("READ COMMITTED")) {
/* 1524 */         this.defaultTransactionIsolationValue = 2;
/* 1525 */       } else if (this.defaultTransactionIsolation.equals("REPEATABLE_READ") || this.defaultTransactionIsolation.equals("REPEATABLE READ")) {
/* 1526 */         this.defaultTransactionIsolationValue = 4;
/* 1527 */       } else if (this.defaultTransactionIsolation.equals("READ_UNCOMMITTED") || this.defaultTransactionIsolation.equals("READ UNCOMMITTED")) {
/* 1528 */         this.defaultTransactionIsolationValue = 1;
/* 1529 */       } else if (this.defaultTransactionIsolation.equals("SERIALIZABLE")) {
/* 1530 */         this.defaultTransactionIsolationValue = 8;
/*      */       } else {
/* 1532 */         logger.warn("Unrecognized defaultTransactionIsolation value. Using driver default.");
/* 1533 */         this.defaultTransactionIsolationValue = -1;
/*      */       } 
/*      */     } 
/* 1536 */     if (this.maxConnectionsPerPartition < 1) {
/* 1537 */       logger.warn("Max Connections < 1. Setting to 20");
/* 1538 */       this.maxConnectionsPerPartition = 20;
/*      */     } 
/* 1540 */     if (this.minConnectionsPerPartition < 0) {
/* 1541 */       logger.warn("Min Connections < 0. Setting to 1");
/* 1542 */       this.minConnectionsPerPartition = 1;
/*      */     } 
/*      */     
/* 1545 */     if (this.minConnectionsPerPartition > this.maxConnectionsPerPartition) {
/* 1546 */       logger.warn("Min Connections > max connections");
/* 1547 */       this.minConnectionsPerPartition = this.maxConnectionsPerPartition;
/*      */     } 
/* 1549 */     if (this.acquireIncrement <= 0) {
/* 1550 */       logger.warn("acquireIncrement <= 0. Setting to 1.");
/* 1551 */       this.acquireIncrement = 1;
/*      */     } 
/* 1553 */     if (this.partitionCount < 1) {
/* 1554 */       logger.warn("partitions < 1! Setting to 1");
/* 1555 */       this.partitionCount = 1;
/*      */     } 
/*      */     
/* 1558 */     if (this.releaseHelperThreads < 0) {
/* 1559 */       logger.warn("releaseHelperThreads < 0! Setting to 3");
/* 1560 */       this.releaseHelperThreads = 3;
/*      */     } 
/*      */     
/* 1563 */     if (this.statementReleaseHelperThreads < 0) {
/* 1564 */       logger.warn("statementReleaseHelperThreads < 0! Setting to 3");
/* 1565 */       this.statementReleaseHelperThreads = 3;
/*      */     } 
/*      */     
/* 1568 */     if (this.statementsCacheSize < 0) {
/* 1569 */       logger.warn("preparedStatementsCacheSize < 0! Setting to 0");
/* 1570 */       this.statementsCacheSize = 0;
/*      */     } 
/*      */     
/* 1573 */     if (this.acquireRetryDelayInMs <= 0L) {
/* 1574 */       this.acquireRetryDelayInMs = 1000L;
/*      */     }
/*      */     
/* 1577 */     if (!this.externalAuth && this.datasourceBean == null && this.driverProperties == null && (this.jdbcUrl == null || this.jdbcUrl.trim().equals("")))
/*      */     {
/* 1579 */       logger.warn("JDBC url was not set in config!");
/*      */     }
/*      */     
/* 1582 */     if (!this.externalAuth && this.datasourceBean == null && this.driverProperties == null && (this.username == null || this.username.trim().equals("")))
/*      */     {
/* 1584 */       logger.warn("JDBC username was not set in config!");
/*      */     }
/*      */     
/* 1587 */     if (!this.externalAuth && this.datasourceBean == null && this.driverProperties == null && this.password == null) {
/* 1588 */       logger.warn("JDBC password was not set in config!");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1593 */     if (!this.externalAuth && this.datasourceBean == null && this.driverProperties != null) {
/* 1594 */       if (this.driverProperties.get("user") == null && this.username == null) {
/* 1595 */         logger.warn("JDBC username not set in driver properties and not set in pool config either");
/* 1596 */       } else if (this.driverProperties.get("user") == null && this.username != null) {
/* 1597 */         logger.warn("JDBC username not set in driver properties, copying it from pool config");
/* 1598 */         this.driverProperties.setProperty("user", this.username);
/* 1599 */       } else if (this.username != null && !this.driverProperties.get("user").equals(this.username)) {
/* 1600 */         logger.warn("JDBC username set in driver properties does not match the one set in the pool config.  Overriding it with pool config.");
/* 1601 */         this.driverProperties.setProperty("user", this.username);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 1606 */     if (!this.externalAuth && this.datasourceBean == null && this.driverProperties != null) {
/* 1607 */       if (this.driverProperties.get("password") == null && this.password == null) {
/* 1608 */         logger.warn("JDBC password not set in driver properties and not set in pool config either");
/* 1609 */       } else if (this.driverProperties.get("password") == null && this.password != null) {
/* 1610 */         logger.warn("JDBC password not set in driver properties, copying it from pool config");
/* 1611 */         this.driverProperties.setProperty("password", this.password);
/* 1612 */       } else if (this.password != null && !this.driverProperties.get("password").equals(this.password)) {
/* 1613 */         logger.warn("JDBC password set in driver properties does not match the one set in the pool config. Overriding it with pool config.");
/* 1614 */         this.driverProperties.setProperty("password", this.password);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1620 */       this.username = this.driverProperties.getProperty("user");
/* 1621 */       this.password = this.driverProperties.getProperty("password");
/*      */     } 
/*      */     
/* 1624 */     if (this.username != null) {
/* 1625 */       this.username = this.username.trim();
/*      */     }
/* 1627 */     if (this.jdbcUrl != null) {
/* 1628 */       this.jdbcUrl = this.jdbcUrl.trim();
/*      */     }
/* 1630 */     if (this.password != null) {
/* 1631 */       this.password = this.password.trim();
/*      */     }
/*      */     
/* 1634 */     if (this.connectionTestStatement != null) {
/* 1635 */       this.connectionTestStatement = this.connectionTestStatement.trim();
/*      */     }
/*      */     
/* 1638 */     this.serviceOrder = (this.serviceOrder != null) ? this.serviceOrder.toUpperCase() : "FIFO";
/*      */     
/* 1640 */     if (!this.serviceOrder.equals("FIFO") && !this.serviceOrder.equals("LIFO")) {
/* 1641 */       logger.warn("Queue service order is not set to FIFO or LIFO. Defaulting to FIFO.");
/* 1642 */       this.serviceOrder = "FIFO";
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void loadProperties(String filename) {
/* 1653 */     ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
/* 1654 */     URL url = classLoader.getResource(filename);
/* 1655 */     if (url != null) {
/*      */       try {
/* 1657 */         setXMLProperties(url.openStream(), null);
/* 1658 */       } catch (Exception e) {}
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1666 */     String result = null;
/* 1667 */     if (this.datasourceBean != null) {
/* 1668 */       result = String.format("JDBC URL = (via datasource bean), Username = (via datasource bean), partitions = %d, max (per partition) = %d, min (per partition) = %d, helper threads = %d, idle max age = %d min, idle test period = %d min", new Object[] { Integer.valueOf(this.partitionCount), Integer.valueOf(this.maxConnectionsPerPartition), Integer.valueOf(this.minConnectionsPerPartition), Integer.valueOf(this.releaseHelperThreads), Long.valueOf(getIdleMaxAgeInMinutes()), Long.valueOf(getIdleConnectionTestPeriodInMinutes()) });
/*      */     }
/*      */     else {
/*      */       
/* 1672 */       result = String.format("JDBC URL = %s, Username = %s, partitions = %d, max (per partition) = %d, min (per partition) = %d, helper threads = %d, idle max age = %d min, idle test period = %d min", new Object[] { this.jdbcUrl, this.username, Integer.valueOf(this.partitionCount), Integer.valueOf(this.maxConnectionsPerPartition), Integer.valueOf(this.minConnectionsPerPartition), Integer.valueOf(this.releaseHelperThreads), Long.valueOf(getIdleMaxAgeInMinutes()), Long.valueOf(getIdleConnectionTestPeriodInMinutes()) });
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1678 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Class<?> loadClass(String clazz) throws ClassNotFoundException {
/* 1687 */     if (this.classLoader == null) {
/* 1688 */       return Class.forName(clazz);
/*      */     }
/*      */     
/* 1691 */     return Class.forName(clazz, true, this.classLoader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassLoader getClassLoader() {
/* 1698 */     return this.classLoader;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClassLoader(ClassLoader classLoader) {
/* 1705 */     this.classLoader = classLoader;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public BoneCPConfig clone() throws CloneNotSupportedException {
/* 1711 */     BoneCPConfig clone = (BoneCPConfig)super.clone();
/* 1712 */     Field[] fields = getClass().getDeclaredFields();
/* 1713 */     for (Field field : fields) {
/*      */       try {
/* 1715 */         field.set(clone, field.get(this));
/* 1716 */       } catch (Exception e) {}
/*      */     } 
/*      */ 
/*      */     
/* 1720 */     return clone;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasSameConfiguration(BoneCPConfig that) {
/* 1729 */     if (that != null && Objects.equal(Integer.valueOf(this.acquireIncrement), Integer.valueOf(that.getAcquireIncrement())) && Objects.equal(Long.valueOf(this.acquireRetryDelayInMs), Long.valueOf(that.getAcquireRetryDelayInMs())) && Objects.equal(Boolean.valueOf(this.closeConnectionWatch), Boolean.valueOf(that.isCloseConnectionWatch())) && Objects.equal(Boolean.valueOf(this.logStatementsEnabled), Boolean.valueOf(that.isLogStatementsEnabled())) && Objects.equal(this.connectionHook, that.getConnectionHook()) && Objects.equal(this.connectionTestStatement, that.getConnectionTestStatement()) && Objects.equal(Long.valueOf(this.idleConnectionTestPeriodInSeconds), Long.valueOf(that.getIdleConnectionTestPeriod(TimeUnit.SECONDS))) && Objects.equal(Long.valueOf(this.idleMaxAgeInSeconds), Long.valueOf(that.getIdleMaxAge(TimeUnit.SECONDS))) && Objects.equal(this.initSQL, that.getInitSQL()) && Objects.equal(this.jdbcUrl, that.getJdbcUrl()) && Objects.equal(Integer.valueOf(this.maxConnectionsPerPartition), Integer.valueOf(that.getMaxConnectionsPerPartition())) && Objects.equal(Integer.valueOf(this.minConnectionsPerPartition), Integer.valueOf(that.getMinConnectionsPerPartition())) && Objects.equal(Integer.valueOf(this.partitionCount), Integer.valueOf(that.getPartitionCount())) && Objects.equal(Integer.valueOf(this.releaseHelperThreads), Integer.valueOf(that.getReleaseHelperThreads())) && Objects.equal(Integer.valueOf(this.statementsCacheSize), Integer.valueOf(that.getStatementsCacheSize())) && Objects.equal(this.username, that.getUsername()) && Objects.equal(this.password, that.getPassword()) && Objects.equal(Boolean.valueOf(this.lazyInit), Boolean.valueOf(that.isLazyInit())) && Objects.equal(Boolean.valueOf(this.transactionRecoveryEnabled), Boolean.valueOf(that.isTransactionRecoveryEnabled())) && Objects.equal(Integer.valueOf(this.acquireRetryAttempts), Integer.valueOf(that.getAcquireRetryAttempts())) && Objects.equal(Integer.valueOf(this.statementReleaseHelperThreads), Integer.valueOf(that.getStatementReleaseHelperThreads())) && Objects.equal(Long.valueOf(this.closeConnectionWatchTimeoutInMs), Long.valueOf(that.getCloseConnectionWatchTimeout())) && Objects.equal(Long.valueOf(this.connectionTimeoutInMs), Long.valueOf(that.getConnectionTimeoutInMs())) && Objects.equal(this.datasourceBean, that.getDatasourceBean()) && Objects.equal(Long.valueOf(getQueryExecuteTimeLimitInMs()), Long.valueOf(that.getQueryExecuteTimeLimitInMs())) && Objects.equal(Integer.valueOf(this.poolAvailabilityThreshold), Integer.valueOf(that.getPoolAvailabilityThreshold())) && Objects.equal(this.poolName, that.getPoolName()) && Objects.equal(Boolean.valueOf(this.disableConnectionTracking), Boolean.valueOf(that.isDisableConnectionTracking())))
/*      */     {
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
/*      */       
/* 1759 */       return true;
/*      */     }
/*      */     
/* 1762 */     return false;
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/BoneCPConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */