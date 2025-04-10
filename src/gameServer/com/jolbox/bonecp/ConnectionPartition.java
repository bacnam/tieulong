package com.jolbox.bonecp;

import com.google.common.base.FinalizableReferenceQueue;
import com.google.common.base.FinalizableWeakReference;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import jsr166y.LinkedTransferQueue;
import jsr166y.TransferQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionPartition
implements Serializable
{
private static final long serialVersionUID = -7864443421028454573L;
static Logger logger = LoggerFactory.getLogger(ConnectionPartition.class);

private TransferQueue<ConnectionHandle> freeConnections;

private final int acquireIncrement;

private final int minConnections;

private final int maxConnections;

protected ReentrantReadWriteLock statsLock = new ReentrantReadWriteLock();

private int createdConnections = 0;

private final String url;

private final String username;

private final String password;

private volatile boolean unableToCreateMoreTransactions = false;

private LinkedTransferQueue<ConnectionHandle> connectionsPendingRelease;

private boolean disableTracking;

private BlockingQueue<Object> poolWatchThreadSignalQueue = new ArrayBlockingQueue(1);

private long queryExecuteTimeLimitInNanoSeconds;

private String poolName;

protected BoneCP pool;

protected BlockingQueue<Object> getPoolWatchThreadSignalQueue() {
return this.poolWatchThreadSignalQueue;
}

protected void updateCreatedConnections(int increment) {
try {
this.statsLock.writeLock().lock();
this.createdConnections += increment;
}
finally {

this.statsLock.writeLock().unlock();
} 
}

protected void addFreeConnection(ConnectionHandle connectionHandle) throws SQLException {
connectionHandle.setOriginatingPartition(this);

updateCreatedConnections(1);
if (!this.disableTracking) {
trackConnectionFinalizer(connectionHandle);
}

if (!this.freeConnections.offer(connectionHandle)) {

updateCreatedConnections(-1);

if (!this.disableTracking) {
this.pool.getFinalizableRefs().remove(connectionHandle.getInternalConnection());
}

connectionHandle.internalClose();
} 
}

protected void trackConnectionFinalizer(ConnectionHandle connectionHandle) {
if (!this.disableTracking) {

Connection con = connectionHandle.getInternalConnection();
if (con != null && con instanceof Proxy && Proxy.getInvocationHandler(con) instanceof MemorizeTransactionProxy) {

try {

con = (Connection)Proxy.getInvocationHandler(con).invoke(con, ConnectionHandle.class.getMethod("getProxyTarget", new Class[0]), null);
} catch (Throwable t) {
logger.error("Error while attempting to track internal db connection", t);
} 
}
final Connection internalDBConnection = con;
final BoneCP pool = connectionHandle.getPool();
connectionHandle.getPool().getFinalizableRefs().put(internalDBConnection, new FinalizableWeakReference<ConnectionHandle>(connectionHandle, connectionHandle.getPool().getFinalizableRefQueue())
{
public void finalizeReferent() {
try {
pool.getFinalizableRefs().remove(internalDBConnection);
if (internalDBConnection != null && !internalDBConnection.isClosed()) {

ConnectionPartition.logger.warn("BoneCP detected an unclosed connection " + ConnectionPartition.this.poolName + "and will now attempt to close it for you. " + "You should be closing this connection in your application - enable connectionWatch for additional debugging assistance.");

internalDBConnection.close();
ConnectionPartition.this.updateCreatedConnections(-1);
} 
} catch (Throwable t) {
ConnectionPartition.logger.error("Error while closing off internal db connection", t);
} 
}
});
} 
}

protected TransferQueue<ConnectionHandle> getFreeConnections() {
return this.freeConnections;
}

protected void setFreeConnections(TransferQueue<ConnectionHandle> freeConnections) {
this.freeConnections = freeConnections;
}

public ConnectionPartition(BoneCP pool) {
BoneCPConfig config = pool.getConfig();
this.minConnections = config.getMinConnectionsPerPartition();
this.maxConnections = config.getMaxConnectionsPerPartition();
this.acquireIncrement = config.getAcquireIncrement();
this.url = config.getJdbcUrl();
this.username = config.getUsername();
this.password = config.getPassword();
this.poolName = (config.getPoolName() != null) ? ("(in pool '" + config.getPoolName() + "') ") : "";
this.pool = pool;

this.connectionsPendingRelease = new LinkedTransferQueue();
this.disableTracking = config.isDisableConnectionTracking();
this.queryExecuteTimeLimitInNanoSeconds = TimeUnit.NANOSECONDS.convert(config.getQueryExecuteTimeLimitInMs(), TimeUnit.MILLISECONDS);

int helperThreads = config.getReleaseHelperThreads();
for (int i = 0; i < helperThreads; i++)
{
pool.getReleaseHelper().execute(new ConnectionReleaseHelperThread((BlockingQueue<ConnectionHandle>)this.connectionsPendingRelease, pool));
}
}

protected int getAcquireIncrement() {
return this.acquireIncrement;
}

protected int getMinConnections() {
return this.minConnections;
}

protected int getMaxConnections() {
return this.maxConnections;
}

protected int getCreatedConnections() {
try {
this.statsLock.readLock().lock();
return this.createdConnections;
} finally {
this.statsLock.readLock().unlock();
} 
}

protected String getUrl() {
return this.url;
}

protected String getUsername() {
return this.username;
}

protected String getPassword() {
return this.password;
}

protected boolean isUnableToCreateMoreTransactions() {
return this.unableToCreateMoreTransactions;
}

protected void setUnableToCreateMoreTransactions(boolean unableToCreateMoreTransactions) {
this.unableToCreateMoreTransactions = unableToCreateMoreTransactions;
}

protected LinkedTransferQueue<ConnectionHandle> getConnectionsPendingRelease() {
return this.connectionsPendingRelease;
}

protected int getAvailableConnections() {
return this.freeConnections.size();
}

public int getRemainingCapacity() {
return this.freeConnections.remainingCapacity();
}

protected long getQueryExecuteTimeLimitinNanoSeconds() {
return this.queryExecuteTimeLimitInNanoSeconds;
}
}

