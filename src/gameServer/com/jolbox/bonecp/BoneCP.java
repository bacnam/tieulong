package com.jolbox.bonecp;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.FinalizableReferenceQueue;
import com.jolbox.bonecp.hooks.AcquireFailConfig;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.ref.Reference;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.sql.DataSource;
import jsr166y.LinkedTransferQueue;
import jsr166y.TransferQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoneCP
implements Serializable
{
private static final String THREAD_CLOSE_CONNECTION_WARNING = "Thread close connection monitoring has been enabled. This will negatively impact on your performance. Only enable this option for debugging purposes!";
private static final long serialVersionUID = -8386816681977604817L;
private static final String ERROR_TEST_CONNECTION = "Unable to open a test connection to the given database. JDBC url = %s, username = %s. Terminating connection pool. Original Exception: %s";
private static final String SHUTDOWN_LOCATION_TRACE = "Attempting to obtain a connection from a pool that has already been shutdown. \nStack trace of location where pool was shutdown follows:\n";
private static final String UNCLOSED_EXCEPTION_MESSAGE = "Connection obtained from thread [%s] was never closed. \nStack trace of location where connection was obtained follows:\n";
public static final String MBEAN_CONFIG = "com.jolbox.bonecp:type=BoneCPConfig";
public static final String MBEAN_BONECP = "com.jolbox.bonecp:type=BoneCP";
private static final String[] METADATATABLE = new String[] { "TABLE" };

private static final String KEEPALIVEMETADATA = "BONECPKEEPALIVE";

protected final int poolAvailabilityThreshold;

private int partitionCount;

private ConnectionPartition[] partitions;

private ScheduledExecutorService keepAliveScheduler;

private ScheduledExecutorService maxAliveScheduler;

private ExecutorService connectionsScheduler;

private BoneCPConfig config;

private boolean releaseHelperThreadsConfigured;

private ExecutorService releaseHelper;

private ExecutorService statementCloseHelperExecutor;

private ExecutorService asyncExecutor;

private static Logger logger = LoggerFactory.getLogger(BoneCP.class);

private MBeanServer mbs;

protected Lock terminationLock = new ReentrantLock();

protected boolean closeConnectionWatch = false;

private ExecutorService closeConnectionExecutor;

protected volatile boolean poolShuttingDown;

private String shutdownStackTrace;

private final Map<Connection, Reference<ConnectionHandle>> finalizableRefs = new ConcurrentHashMap<Connection, Reference<ConnectionHandle>>();

private FinalizableReferenceQueue finalizableRefQueue;

private long connectionTimeoutInMs;

private long closeConnectionWatchTimeoutInMs;

private boolean statementReleaseHelperThreadsConfigured;

private LinkedTransferQueue<StatementHandle> statementsPendingRelease;

private boolean statisticsEnabled;

private Statistics statistics = new Statistics(this);

private Boolean defaultReadOnly;

private String defaultCatalog;

private int defaultTransactionIsolationValue;

private Boolean defaultAutoCommit;

@VisibleForTesting
protected boolean externalAuth;

public synchronized void shutdown() {
if (!this.poolShuttingDown) {
logger.info("Shutting down connection pool...");
this.poolShuttingDown = true;

this.shutdownStackTrace = captureStackTrace("Attempting to obtain a connection from a pool that has already been shutdown. \nStack trace of location where pool was shutdown follows:\n");
this.keepAliveScheduler.shutdownNow();
this.maxAliveScheduler.shutdownNow();
this.connectionsScheduler.shutdownNow();

if (this.releaseHelperThreadsConfigured) {
this.releaseHelper.shutdownNow();
}
if (this.statementReleaseHelperThreadsConfigured) {
this.statementCloseHelperExecutor.shutdownNow();
}
if (this.asyncExecutor != null) {
this.asyncExecutor.shutdownNow();
}
if (this.closeConnectionExecutor != null) {
this.closeConnectionExecutor.shutdownNow();
}
terminateAllConnections();
logger.info("Connection pool has been shutdown.");
} 
}

public void close() {
shutdown();
}

protected void terminateAllConnections() {
this.terminationLock.lock();

try {
for (int i = 0; i < this.partitionCount; i++) {
ConnectionHandle conn;
while ((conn = (ConnectionHandle)this.partitions[i].getFreeConnections().poll()) != null) {
postDestroyConnection(conn);
conn.setInReplayMode(true);
try {
conn.internalClose();
} catch (SQLException e) {
logger.error("Error in attempting to close connection", e);
} 
} 
} 
} finally {

this.terminationLock.unlock();
} 
}

protected void postDestroyConnection(ConnectionHandle handle) {
ConnectionPartition partition = handle.getOriginatingPartition();

if (this.finalizableRefQueue != null) {
this.finalizableRefs.remove(handle.getInternalConnection());
}

partition.updateCreatedConnections(-1);
partition.setUnableToCreateMoreTransactions(false);

if (handle.getConnectionHook() != null) {
handle.getConnectionHook().onDestroy(handle);
}
}

protected Connection obtainRawInternalConnection() throws SQLException {
Connection result = null;

DataSource datasourceBean = this.config.getDatasourceBean();
String url = this.config.getJdbcUrl();
String username = this.config.getUsername();
String password = this.config.getPassword();
Properties props = this.config.getDriverProperties();

if (this.externalAuth && props == null) {
props = new Properties();
}

if (datasourceBean != null) {
return (username == null) ? datasourceBean.getConnection() : datasourceBean.getConnection(username, password);
}

if (props != null) {
result = DriverManager.getConnection(url, props);
} else {
result = DriverManager.getConnection(url, username, password);
} 

if (this.defaultAutoCommit != null) {
result.setAutoCommit(this.defaultAutoCommit.booleanValue());
}
if (this.defaultReadOnly != null) {
result.setReadOnly(this.defaultReadOnly.booleanValue());
}
if (this.defaultCatalog != null) {
result.setCatalog(this.defaultCatalog);
}
if (this.defaultTransactionIsolationValue != -1) {
result.setTransactionIsolation(this.defaultTransactionIsolationValue);
}

return result;
}

public BoneCP(BoneCPConfig config) throws SQLException {
this.config = config;
config.sanitize();

this.statisticsEnabled = config.isStatisticsEnabled();
this.closeConnectionWatchTimeoutInMs = config.getCloseConnectionWatchTimeoutInMs();
this.poolAvailabilityThreshold = config.getPoolAvailabilityThreshold();
this.connectionTimeoutInMs = config.getConnectionTimeoutInMs();
this.externalAuth = config.isExternalAuth();

if (this.connectionTimeoutInMs == 0L) {
this.connectionTimeoutInMs = Long.MAX_VALUE;
}
this.defaultReadOnly = config.getDefaultReadOnly();
this.defaultCatalog = config.getDefaultCatalog();
this.defaultTransactionIsolationValue = config.getDefaultTransactionIsolationValue();
this.defaultAutoCommit = config.getDefaultAutoCommit();

AcquireFailConfig acquireConfig = new AcquireFailConfig();
acquireConfig.setAcquireRetryAttempts(new AtomicInteger(0));
acquireConfig.setAcquireRetryDelayInMs(0L);
acquireConfig.setLogMessage("Failed to obtain initial connection");

if (!config.isLazyInit()) {
try {
Connection sanityConnection = obtainRawInternalConnection();
sanityConnection.close();
} catch (Exception e) {
if (config.getConnectionHook() != null) {
config.getConnectionHook().onAcquireFail(e, acquireConfig);
}

throw new SQLException(String.format("Unable to open a test connection to the given database. JDBC url = %s, username = %s. Terminating connection pool. Original Exception: %s", new Object[] { config.getJdbcUrl(), config.getUsername(), PoolUtil.stringifyException(e) }), e);
} 
}

if (!config.isDisableConnectionTracking()) {
this.finalizableRefQueue = new FinalizableReferenceQueue();
}

this.asyncExecutor = Executors.newCachedThreadPool();
int helperThreads = config.getReleaseHelperThreads();
this.releaseHelperThreadsConfigured = (helperThreads > 0);

this.statementReleaseHelperThreadsConfigured = (config.getStatementReleaseHelperThreads() > 0);
this.config = config;
this.partitions = new ConnectionPartition[config.getPartitionCount()];
String suffix = "";

if (config.getPoolName() != null) {
suffix = "-" + config.getPoolName();
}

if (this.releaseHelperThreadsConfigured) {
this.releaseHelper = Executors.newFixedThreadPool(helperThreads * config.getPartitionCount(), new CustomThreadFactory("BoneCP-release-thread-helper-thread" + suffix, true));
}
this.keepAliveScheduler = Executors.newScheduledThreadPool(config.getPartitionCount(), new CustomThreadFactory("BoneCP-keep-alive-scheduler" + suffix, true));
this.maxAliveScheduler = Executors.newScheduledThreadPool(config.getPartitionCount(), new CustomThreadFactory("BoneCP-max-alive-scheduler" + suffix, true));
this.connectionsScheduler = Executors.newFixedThreadPool(config.getPartitionCount(), new CustomThreadFactory("BoneCP-pool-watch-thread" + suffix, true));

this.partitionCount = config.getPartitionCount();
this.closeConnectionWatch = config.isCloseConnectionWatch();
boolean queueLIFO = (config.getServiceOrder() != null && config.getServiceOrder().equalsIgnoreCase("LIFO"));
if (this.closeConnectionWatch) {
logger.warn("Thread close connection monitoring has been enabled. This will negatively impact on your performance. Only enable this option for debugging purposes!");
this.closeConnectionExecutor = Executors.newCachedThreadPool(new CustomThreadFactory("BoneCP-connection-watch-thread" + suffix, true));
} 

for (int p = 0; p < config.getPartitionCount(); p++) {
TransferQueue<ConnectionHandle> connectionHandles;
ConnectionPartition connectionPartition = new ConnectionPartition(this);
this.partitions[p] = connectionPartition;

if (config.getMaxConnectionsPerPartition() == config.getMinConnectionsPerPartition()) {

connectionHandles = queueLIFO ? new LIFOQueue<ConnectionHandle>() : (TransferQueue<ConnectionHandle>)new LinkedTransferQueue();
} else {
connectionHandles = queueLIFO ? new LIFOQueue<ConnectionHandle>(this.config.getMaxConnectionsPerPartition()) : (TransferQueue<ConnectionHandle>)new BoundedLinkedTransferQueue(this.config.getMaxConnectionsPerPartition());
} 

this.partitions[p].setFreeConnections(connectionHandles);

if (!config.isLazyInit()) {
for (int i = 0; i < config.getMinConnectionsPerPartition(); i++) {
ConnectionHandle handle = new ConnectionHandle(config.getJdbcUrl(), config.getUsername(), config.getPassword(), this);
this.partitions[p].addFreeConnection(handle);
} 
}

if (config.getIdleConnectionTestPeriodInMinutes() > 0L || config.getIdleMaxAgeInMinutes() > 0L) {

Runnable connectionTester = new ConnectionTesterThread(connectionPartition, this.keepAliveScheduler, this, config.getIdleMaxAge(TimeUnit.MILLISECONDS), config.getIdleConnectionTestPeriod(TimeUnit.MILLISECONDS), queueLIFO);
long delayInMinutes = config.getIdleConnectionTestPeriodInMinutes();
if (delayInMinutes == 0L) {
delayInMinutes = config.getIdleMaxAgeInMinutes();
}
if (config.getIdleMaxAgeInMinutes() != 0L && config.getIdleConnectionTestPeriodInMinutes() != 0L && config.getIdleMaxAgeInMinutes() < delayInMinutes) {
delayInMinutes = config.getIdleMaxAgeInMinutes();
}
this.keepAliveScheduler.schedule(connectionTester, delayInMinutes, TimeUnit.MINUTES);
} 

if (config.getMaxConnectionAgeInSeconds() > 0L) {
Runnable connectionMaxAgeTester = new ConnectionMaxAgeThread(connectionPartition, this.maxAliveScheduler, this, config.getMaxConnectionAge(TimeUnit.MILLISECONDS), queueLIFO);
this.maxAliveScheduler.schedule(connectionMaxAgeTester, config.getMaxConnectionAgeInSeconds(), TimeUnit.SECONDS);
} 

this.connectionsScheduler.execute(new PoolWatchThread(connectionPartition, this));
} 

initStmtReleaseHelper(suffix);

if (!this.config.isDisableJMX()) {
initJMX();
}
}

protected void initStmtReleaseHelper(String suffix) {
this.statementsPendingRelease = new BoundedLinkedTransferQueue<StatementHandle>(this.config.getMaxConnectionsPerPartition() * 3);
int statementReleaseHelperThreads = this.config.getStatementReleaseHelperThreads();

if (statementReleaseHelperThreads > 0) {
setStatementCloseHelperExecutor(Executors.newFixedThreadPool(statementReleaseHelperThreads, new CustomThreadFactory("BoneCP-statement-close-helper-thread" + suffix, true)));

for (int i = 0; i < statementReleaseHelperThreads; i++)
{
getStatementCloseHelperExecutor().execute(new StatementReleaseHelperThread((BlockingQueue<StatementHandle>)this.statementsPendingRelease, this));
}
} 
}

protected void initJMX() {
if (this.mbs == null) {
this.mbs = ManagementFactory.getPlatformMBeanServer();
}
try {
String suffix = "";

if (this.config.getPoolName() != null) {
suffix = "-" + this.config.getPoolName();
}

ObjectName name = new ObjectName("com.jolbox.bonecp:type=BoneCP" + suffix);
ObjectName configname = new ObjectName("com.jolbox.bonecp:type=BoneCPConfig" + suffix);

if (!this.mbs.isRegistered(name)) {
this.mbs.registerMBean(this.statistics, name);
}
if (!this.mbs.isRegistered(configname)) {
this.mbs.registerMBean(this.config, configname);
}
} catch (Exception e) {
logger.error("Unable to start JMX", e);
} 
}

public Connection getConnection() throws SQLException {
long statsObtainTime = 0L;

if (this.poolShuttingDown) {
throw new SQLException(this.shutdownStackTrace);
}

int partition = (int)(Thread.currentThread().getId() % this.partitionCount);
ConnectionPartition connectionPartition = this.partitions[partition];

if (this.statisticsEnabled) {
statsObtainTime = System.nanoTime();
this.statistics.incrementConnectionsRequested();
} 
ConnectionHandle result = (ConnectionHandle)connectionPartition.getFreeConnections().poll();

if (result == null)
{
for (int i = 0; i < this.partitionCount; i++) {
if (i != partition) {

result = (ConnectionHandle)this.partitions[i].getFreeConnections().poll();
connectionPartition = this.partitions[i];
if (result != null) {
break;
}
} 
} 
}
if (!connectionPartition.isUnableToCreateMoreTransactions()) {
maybeSignalForMoreConnections(connectionPartition);
}

if (result == null) {
try {
result = (ConnectionHandle)connectionPartition.getFreeConnections().poll(this.connectionTimeoutInMs, TimeUnit.MILLISECONDS);
if (result == null)
{
throw new SQLException("Timed out waiting for a free available connection.", "08001");
}
}
catch (InterruptedException e) {
throw new SQLException(e.getMessage());
} 
}
result.renewConnection();

if (result.getConnectionHook() != null) {
result.getConnectionHook().onCheckOut(result);
}

if (this.closeConnectionWatch) {
watchConnection(result);
}

if (this.statisticsEnabled) {
this.statistics.addCumulativeConnectionWaitTime(System.nanoTime() - statsObtainTime);
}
return result;
}

private void watchConnection(ConnectionHandle connectionHandle) {
String message = captureStackTrace("Connection obtained from thread [%s] was never closed. \nStack trace of location where connection was obtained follows:\n");
this.closeConnectionExecutor.submit(new CloseThreadMonitor(Thread.currentThread(), connectionHandle, message, this.closeConnectionWatchTimeoutInMs));
}

protected String captureStackTrace(String message) {
StringBuilder stringBuilder = new StringBuilder(String.format(message, new Object[] { Thread.currentThread().getName() }));
StackTraceElement[] trace = Thread.currentThread().getStackTrace();
for (int i = 0; i < trace.length; i++) {
stringBuilder.append(" " + trace[i] + "\r\n");
}

stringBuilder.append("");

return stringBuilder.toString();
}

public Future<Connection> getAsyncConnection() {
return this.asyncExecutor.submit(new Callable<Connection>()
{
public Connection call() throws Exception {
return BoneCP.this.getConnection();
}
});
}

private void maybeSignalForMoreConnections(ConnectionPartition connectionPartition) {
if (!connectionPartition.isUnableToCreateMoreTransactions() && !this.poolShuttingDown && connectionPartition.getAvailableConnections() * 100 / connectionPartition.getMaxConnections() <= this.poolAvailabilityThreshold)
{
connectionPartition.getPoolWatchThreadSignalQueue().offer(new Object());
}
}

protected void releaseConnection(Connection connection) throws SQLException {
ConnectionHandle handle = (ConnectionHandle)connection;

if (handle.getConnectionHook() != null) {
handle.getConnectionHook().onCheckIn(handle);
}

if (!this.poolShuttingDown && this.releaseHelperThreadsConfigured) {
if (!handle.getOriginatingPartition().getConnectionsPendingRelease().tryTransfer(handle)) {
handle.getOriginatingPartition().getConnectionsPendingRelease().put(handle);
}
} else {
internalReleaseConnection(handle);
} 
}

protected void internalReleaseConnection(ConnectionHandle connectionHandle) throws SQLException {
connectionHandle.clearStatementCaches(false);

if (connectionHandle.getReplayLog() != null) {
connectionHandle.getReplayLog().clear();
connectionHandle.recoveryResult.getReplaceTarget().clear();
} 

if (connectionHandle.isExpired() || (!this.poolShuttingDown && connectionHandle.isPossiblyBroken() && !isConnectionHandleAlive(connectionHandle))) {

ConnectionPartition connectionPartition = connectionHandle.getOriginatingPartition();
maybeSignalForMoreConnections(connectionPartition);

postDestroyConnection(connectionHandle);
connectionHandle.clearStatementCaches(true);

return;
} 

connectionHandle.setConnectionLastUsedInMs(System.currentTimeMillis());
if (!this.poolShuttingDown) {

putConnectionBackInPartition(connectionHandle);
} else {
connectionHandle.internalClose();
} 
}

protected void putConnectionBackInPartition(ConnectionHandle connectionHandle) throws SQLException {
TransferQueue<ConnectionHandle> queue = connectionHandle.getOriginatingPartition().getFreeConnections();

if (!queue.tryTransfer(connectionHandle) && 
!queue.offer(connectionHandle)) {
connectionHandle.internalClose();
}
}

public boolean isConnectionHandleAlive(ConnectionHandle connection) {
Statement stmt = null;
boolean result = false;
boolean logicallyClosed = connection.logicallyClosed;
try {
if (logicallyClosed) {
connection.logicallyClosed = false;
}
String testStatement = this.config.getConnectionTestStatement();
ResultSet rs = null;

if (testStatement == null) {

rs = connection.getMetaData().getTables(null, null, "BONECPKEEPALIVE", METADATATABLE);
} else {
stmt = connection.createStatement();
stmt.execute(testStatement);
} 

if (rs != null) {
rs.close();
}

result = true;
} catch (SQLException e) {

result = false;
} finally {
connection.logicallyClosed = logicallyClosed;
connection.setConnectionLastResetInMs(System.currentTimeMillis());
result = closeStatement(stmt, result);
} 
return result;
}

private boolean closeStatement(Statement stmt, boolean result) {
if (stmt != null) {
try {
stmt.close();
} catch (SQLException e) {
return false;
} 
}
return result;
}

public int getTotalLeased() {
int total = 0;
for (int i = 0; i < this.partitionCount; i++) {
total += this.partitions[i].getCreatedConnections() - this.partitions[i].getAvailableConnections();
}
return total;
}

public int getTotalFree() {
int total = 0;
for (int i = 0; i < this.partitionCount; i++) {
total += this.partitions[i].getAvailableConnections();
}
return total;
}

public int getTotalCreatedConnections() {
int total = 0;
for (int i = 0; i < this.partitionCount; i++) {
total += this.partitions[i].getCreatedConnections();
}
return total;
}

public BoneCPConfig getConfig() {
return this.config;
}

protected ExecutorService getReleaseHelper() {
return this.releaseHelper;
}

protected void setReleaseHelper(ExecutorService releaseHelper) {
this.releaseHelper = releaseHelper;
}

protected Map<Connection, Reference<ConnectionHandle>> getFinalizableRefs() {
return this.finalizableRefs;
}

protected FinalizableReferenceQueue getFinalizableRefQueue() {
return this.finalizableRefQueue;
}

protected ExecutorService getStatementCloseHelperExecutor() {
return this.statementCloseHelperExecutor;
}

protected void setStatementCloseHelperExecutor(ExecutorService statementCloseHelper) {
this.statementCloseHelperExecutor = statementCloseHelper;
}

protected boolean isReleaseHelperThreadsConfigured() {
return this.releaseHelperThreadsConfigured;
}

protected boolean isStatementReleaseHelperThreadsConfigured() {
return this.statementReleaseHelperThreadsConfigured;
}

protected LinkedTransferQueue<StatementHandle> getStatementsPendingRelease() {
return this.statementsPendingRelease;
}

public Statistics getStatistics() {
return this.statistics;
}
}

