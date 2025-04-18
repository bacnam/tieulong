package com.mchange.v2.c3p0.impl;

import com.mchange.lang.ThrowableUtils;
import com.mchange.v2.c3p0.C3P0Registry;
import com.mchange.v2.c3p0.PooledDataSource;
import com.mchange.v2.c3p0.cfg.C3P0Config;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

public abstract class AbstractPoolBackedDataSource
extends PoolBackedDataSourceBase
implements PooledDataSource
{
static final MLogger logger = MLog.getLogger(AbstractPoolBackedDataSource.class);

static final String NO_CPDS_ERR_MSG = "Attempted to use an uninitialized PoolBackedDataSource. Please call setConnectionPoolDataSource( ... ) to initialize.";

transient C3P0PooledConnectionPoolManager poolManager;

transient boolean is_closed = false;

private static final long serialVersionUID = 1L;

private static final short VERSION = 1;

protected AbstractPoolBackedDataSource(boolean autoregister) {
super(autoregister);
setUpPropertyEvents();
}

private void setUpPropertyEvents() {
PropertyChangeListener l = new PropertyChangeListener()
{
public void propertyChange(PropertyChangeEvent evt)
{
AbstractPoolBackedDataSource.this.resetPoolManager(false); }
};
addPropertyChangeListener(l);
}

protected void initializeNamedConfig(String configName, boolean shouldBindUserOverridesAsString) {
try {
if (configName != null) {

C3P0Config.bindNamedConfigToBean(this, configName, shouldBindUserOverridesAsString);
if (getDataSourceName().equals(getIdentityToken())) {
setDataSourceName(configName);
}
} 
} catch (Exception e) {

if (logger.isLoggable(MLevel.WARNING)) {
logger.log(MLevel.WARNING, "Error binding PoolBackedDataSource to named-config '" + configName + "'. Some default-config values may be used.", e);
}
} 
}

public String getDataSourceName() {
String out = super.getDataSourceName();
if (out == null)
out = getIdentityToken(); 
return out;
}

public Connection getConnection() throws SQLException {
PooledConnection pc = getPoolManager().getPool().checkoutPooledConnection();
return pc.getConnection();
}

public Connection getConnection(String username, String password) throws SQLException {
PooledConnection pc = getPoolManager().getPool(username, password).checkoutPooledConnection();
return pc.getConnection();
}

public PrintWriter getLogWriter() throws SQLException {
return assertCpds().getLogWriter();
}
public void setLogWriter(PrintWriter out) throws SQLException {
assertCpds().setLogWriter(out);
}
public int getLoginTimeout() throws SQLException {
return assertCpds().getLoginTimeout();
}
public void setLoginTimeout(int seconds) throws SQLException {
assertCpds().setLoginTimeout(seconds);
}

public int getNumConnections() throws SQLException {
return getPoolManager().getPool().getNumConnections();
}
public int getNumIdleConnections() throws SQLException {
return getPoolManager().getPool().getNumIdleConnections();
}
public int getNumBusyConnections() throws SQLException {
return getPoolManager().getPool().getNumBusyConnections();
}
public int getNumUnclosedOrphanedConnections() throws SQLException {
return getPoolManager().getPool().getNumUnclosedOrphanedConnections();
}
public int getNumConnectionsDefaultUser() throws SQLException {
return getNumConnections();
}
public int getNumIdleConnectionsDefaultUser() throws SQLException {
return getNumIdleConnections();
}
public int getNumBusyConnectionsDefaultUser() throws SQLException {
return getNumBusyConnections();
}
public int getNumUnclosedOrphanedConnectionsDefaultUser() throws SQLException {
return getNumUnclosedOrphanedConnections();
}
public int getStatementCacheNumStatementsDefaultUser() throws SQLException {
return getPoolManager().getPool().getStatementCacheNumStatements();
}
public int getStatementCacheNumCheckedOutDefaultUser() throws SQLException {
return getPoolManager().getPool().getStatementCacheNumCheckedOut();
}
public int getStatementCacheNumConnectionsWithCachedStatementsDefaultUser() throws SQLException {
return getPoolManager().getPool().getStatementCacheNumConnectionsWithCachedStatements();
}
public float getEffectivePropertyCycleDefaultUser() throws SQLException {
return getPoolManager().getPool().getEffectivePropertyCycle();
}
public long getStartTimeMillisDefaultUser() throws SQLException {
return getPoolManager().getPool().getStartTime();
}
public long getUpTimeMillisDefaultUser() throws SQLException {
return getPoolManager().getPool().getUpTime();
}
public long getNumFailedCheckinsDefaultUser() throws SQLException {
return getPoolManager().getPool().getNumFailedCheckins();
}
public long getNumFailedCheckoutsDefaultUser() throws SQLException {
return getPoolManager().getPool().getNumFailedCheckouts();
}
public long getNumFailedIdleTestsDefaultUser() throws SQLException {
return getPoolManager().getPool().getNumFailedIdleTests();
}
public int getNumThreadsAwaitingCheckoutDefaultUser() throws SQLException {
return getPoolManager().getPool().getNumThreadsAwaitingCheckout();
}
public int getThreadPoolSize() throws SQLException {
return getPoolManager().getThreadPoolSize();
}
public int getThreadPoolNumActiveThreads() throws SQLException {
return getPoolManager().getThreadPoolNumActiveThreads();
}
public int getThreadPoolNumIdleThreads() throws SQLException {
return getPoolManager().getThreadPoolNumIdleThreads();
}
public int getThreadPoolNumTasksPending() throws SQLException {
return getPoolManager().getThreadPoolNumTasksPending();
}
public String sampleThreadPoolStackTraces() throws SQLException {
return getPoolManager().getThreadPoolStackTraces();
}
public String sampleThreadPoolStatus() throws SQLException {
return getPoolManager().getThreadPoolStatus();
}
public String sampleStatementCacheStatusDefaultUser() throws SQLException {
return getPoolManager().getPool().dumpStatementCacheStatus();
}
public String sampleStatementCacheStatus(String username, String password) throws SQLException {
return assertAuthPool(username, password).dumpStatementCacheStatus();
}
public Throwable getLastAcquisitionFailureDefaultUser() throws SQLException {
return getPoolManager().getPool().getLastAcquisitionFailure();
}
public Throwable getLastCheckinFailureDefaultUser() throws SQLException {
return getPoolManager().getPool().getLastCheckinFailure();
}
public Throwable getLastCheckoutFailureDefaultUser() throws SQLException {
return getPoolManager().getPool().getLastCheckoutFailure();
}
public Throwable getLastIdleTestFailureDefaultUser() throws SQLException {
return getPoolManager().getPool().getLastIdleTestFailure();
}
public Throwable getLastConnectionTestFailureDefaultUser() throws SQLException {
return getPoolManager().getPool().getLastConnectionTestFailure();
}
public Throwable getLastAcquisitionFailure(String username, String password) throws SQLException {
return assertAuthPool(username, password).getLastAcquisitionFailure();
}
public Throwable getLastCheckinFailure(String username, String password) throws SQLException {
return assertAuthPool(username, password).getLastCheckinFailure();
}
public Throwable getLastCheckoutFailure(String username, String password) throws SQLException {
return assertAuthPool(username, password).getLastCheckoutFailure();
}
public Throwable getLastIdleTestFailure(String username, String password) throws SQLException {
return assertAuthPool(username, password).getLastIdleTestFailure();
}
public Throwable getLastConnectionTestFailure(String username, String password) throws SQLException {
return assertAuthPool(username, password).getLastConnectionTestFailure();
}
public int getNumThreadsAwaitingCheckout(String username, String password) throws SQLException {
return assertAuthPool(username, password).getNumThreadsAwaitingCheckout();
}

public String sampleLastAcquisitionFailureStackTraceDefaultUser() throws SQLException {
Throwable t = getLastAcquisitionFailureDefaultUser();
return (t == null) ? null : ThrowableUtils.extractStackTrace(t);
}

public String sampleLastCheckinFailureStackTraceDefaultUser() throws SQLException {
Throwable t = getLastCheckinFailureDefaultUser();
return (t == null) ? null : ThrowableUtils.extractStackTrace(t);
}

public String sampleLastCheckoutFailureStackTraceDefaultUser() throws SQLException {
Throwable t = getLastCheckoutFailureDefaultUser();
return (t == null) ? null : ThrowableUtils.extractStackTrace(t);
}

public String sampleLastIdleTestFailureStackTraceDefaultUser() throws SQLException {
Throwable t = getLastIdleTestFailureDefaultUser();
return (t == null) ? null : ThrowableUtils.extractStackTrace(t);
}

public String sampleLastConnectionTestFailureStackTraceDefaultUser() throws SQLException {
Throwable t = getLastConnectionTestFailureDefaultUser();
return (t == null) ? null : ThrowableUtils.extractStackTrace(t);
}

public String sampleLastAcquisitionFailureStackTrace(String username, String password) throws SQLException {
Throwable t = getLastAcquisitionFailure(username, password);
return (t == null) ? null : ThrowableUtils.extractStackTrace(t);
}

public String sampleLastCheckinFailureStackTrace(String username, String password) throws SQLException {
Throwable t = getLastCheckinFailure(username, password);
return (t == null) ? null : ThrowableUtils.extractStackTrace(t);
}

public String sampleLastCheckoutFailureStackTrace(String username, String password) throws SQLException {
Throwable t = getLastCheckoutFailure(username, password);
return (t == null) ? null : ThrowableUtils.extractStackTrace(t);
}

public String sampleLastIdleTestFailureStackTrace(String username, String password) throws SQLException {
Throwable t = getLastIdleTestFailure(username, password);
return (t == null) ? null : ThrowableUtils.extractStackTrace(t);
}

public String sampleLastConnectionTestFailureStackTrace(String username, String password) throws SQLException {
Throwable t = getLastConnectionTestFailure(username, password);
return (t == null) ? null : ThrowableUtils.extractStackTrace(t);
}

public void softResetDefaultUser() throws SQLException {
getPoolManager().getPool().reset();
}
public int getNumConnections(String username, String password) throws SQLException {
return assertAuthPool(username, password).getNumConnections();
}
public int getNumIdleConnections(String username, String password) throws SQLException {
return assertAuthPool(username, password).getNumIdleConnections();
}
public int getNumBusyConnections(String username, String password) throws SQLException {
return assertAuthPool(username, password).getNumBusyConnections();
}
public int getNumUnclosedOrphanedConnections(String username, String password) throws SQLException {
return assertAuthPool(username, password).getNumUnclosedOrphanedConnections();
}
public int getStatementCacheNumStatements(String username, String password) throws SQLException {
return assertAuthPool(username, password).getStatementCacheNumStatements();
}
public int getStatementCacheNumCheckedOut(String username, String password) throws SQLException {
return assertAuthPool(username, password).getStatementCacheNumCheckedOut();
}
public int getStatementCacheNumConnectionsWithCachedStatements(String username, String password) throws SQLException {
return assertAuthPool(username, password).getStatementCacheNumConnectionsWithCachedStatements();
}
public float getEffectivePropertyCycle(String username, String password) throws SQLException {
return assertAuthPool(username, password).getEffectivePropertyCycle();
}
public long getStartTimeMillis(String username, String password) throws SQLException {
return assertAuthPool(username, password).getStartTime();
}
public long getUpTimeMillis(String username, String password) throws SQLException {
return assertAuthPool(username, password).getUpTime();
}
public long getNumFailedCheckins(String username, String password) throws SQLException {
return assertAuthPool(username, password).getNumFailedCheckins();
}
public long getNumFailedCheckouts(String username, String password) throws SQLException {
return assertAuthPool(username, password).getNumFailedCheckouts();
}
public long getNumFailedIdleTests(String username, String password) throws SQLException {
return assertAuthPool(username, password).getNumFailedIdleTests();
}
public void softReset(String username, String password) throws SQLException {
assertAuthPool(username, password).reset();
}
public int getNumBusyConnectionsAllUsers() throws SQLException {
return getPoolManager().getNumBusyConnectionsAllAuths();
}
public int getNumIdleConnectionsAllUsers() throws SQLException {
return getPoolManager().getNumIdleConnectionsAllAuths();
}
public int getNumConnectionsAllUsers() throws SQLException {
return getPoolManager().getNumConnectionsAllAuths();
}
public int getNumUnclosedOrphanedConnectionsAllUsers() throws SQLException {
return getPoolManager().getNumUnclosedOrphanedConnectionsAllAuths();
}
public int getStatementCacheNumStatementsAllUsers() throws SQLException {
return getPoolManager().getStatementCacheNumStatementsAllUsers();
}
public int getStatementCacheNumCheckedOutStatementsAllUsers() throws SQLException {
return getPoolManager().getStatementCacheNumCheckedOutStatementsAllUsers();
}
public int getStatementCacheNumConnectionsWithCachedStatementsAllUsers() throws SQLException {
return getPoolManager().getStatementCacheNumConnectionsWithCachedStatementsAllUsers();
}

public int getStatementDestroyerNumConnectionsInUseAllUsers() throws SQLException {
return getPoolManager().getStatementDestroyerNumConnectionsInUseAllUsers();
}
public int getStatementDestroyerNumConnectionsWithDeferredDestroyStatementsAllUsers() throws SQLException {
return getPoolManager().getStatementDestroyerNumConnectionsWithDeferredDestroyStatementsAllUsers();
}
public int getStatementDestroyerNumDeferredDestroyStatementsAllUsers() throws SQLException {
return getPoolManager().getStatementDestroyerNumDeferredDestroyStatementsAllUsers();
}
public int getStatementDestroyerNumConnectionsInUseDefaultUser() throws SQLException {
return getPoolManager().getPool().getStatementDestroyerNumConnectionsInUse();
}
public int getStatementDestroyerNumConnectionsWithDeferredDestroyStatementsDefaultUser() throws SQLException {
return getPoolManager().getPool().getStatementDestroyerNumConnectionsWithDeferredDestroyStatements();
}
public int getStatementDestroyerNumDeferredDestroyStatementsDefaultUser() throws SQLException {
return getPoolManager().getPool().getStatementDestroyerNumDeferredDestroyStatements();
}
public int getStatementDestroyerNumThreads() throws SQLException {
return getPoolManager().getStatementDestroyerNumThreads();
}
public int getStatementDestroyerNumActiveThreads() throws SQLException {
return getPoolManager().getStatementDestroyerNumActiveThreads();
}
public int getStatementDestroyerNumIdleThreads() throws SQLException {
return getPoolManager().getStatementDestroyerNumIdleThreads();
}
public int getStatementDestroyerNumTasksPending() throws SQLException {
return getPoolManager().getStatementDestroyerNumTasksPending();
}
public int getStatementDestroyerNumConnectionsInUse(String username, String password) throws SQLException {
return assertAuthPool(username, password).getStatementDestroyerNumConnectionsInUse();
}
public int getStatementDestroyerNumConnectionsWithDeferredDestroyStatements(String username, String password) throws SQLException {
return assertAuthPool(username, password).getStatementDestroyerNumConnectionsWithDeferredDestroyStatements();
}
public int getStatementDestroyerNumDeferredDestroyStatements(String username, String password) throws SQLException {
return assertAuthPool(username, password).getStatementDestroyerNumDeferredDestroyStatements();
}
public String sampleStatementDestroyerStackTraces() throws SQLException {
return getPoolManager().getStatementDestroyerStackTraces();
}
public String sampleStatementDestroyerStatus() throws SQLException {
return getPoolManager().getStatementDestroyerStatus();
}

public void softResetAllUsers() throws SQLException {
getPoolManager().softResetAllAuths();
}
public int getNumUserPools() throws SQLException {
return getPoolManager().getNumManagedAuths();
}

public Collection getAllUsers() throws SQLException {
LinkedList<String> out = new LinkedList();
Set auths = getPoolManager().getManagedAuths();
for (Iterator<DbAuth> ii = auths.iterator(); ii.hasNext();)
out.add(((DbAuth)ii.next()).getUser()); 
return Collections.unmodifiableList(out);
}

public synchronized void hardReset() {
resetPoolManager();
}

public synchronized void close() {
resetPoolManager();
this.is_closed = true;

C3P0Registry.markClosed(this);

if (logger.isLoggable(MLevel.FINEST))
{
logger.log(MLevel.FINEST, getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " has been closed. ", new Exception("DEBUG STACK TRACE for PoolBackedDataSource.close()."));
}
}

public void close(boolean force_destroy) {
close();
}

public synchronized void resetPoolManager() {
resetPoolManager(true);
}

public synchronized void resetPoolManager(boolean close_checked_out_connections) {
if (this.poolManager != null) {

this.poolManager.close(close_checked_out_connections);
this.poolManager = null;
} 
}

private synchronized ConnectionPoolDataSource assertCpds() throws SQLException {
if (this.is_closed) {
throw new SQLException(this + " has been closed() -- you can no longer use it.");
}
ConnectionPoolDataSource out = getConnectionPoolDataSource();
if (out == null)
throw new SQLException("Attempted to use an uninitialized PoolBackedDataSource. Please call setConnectionPoolDataSource( ... ) to initialize."); 
return out;
}

private synchronized C3P0PooledConnectionPoolManager getPoolManager() throws SQLException {
if (this.poolManager == null) {

ConnectionPoolDataSource cpds = assertCpds();
this.poolManager = new C3P0PooledConnectionPoolManager(cpds, null, null, getNumHelperThreads(), getIdentityToken(), getDataSourceName());
if (logger.isLoggable(MLevel.INFO))
logger.info("Initializing c3p0 pool... " + toString()); 
} 
return this.poolManager;
}

private C3P0PooledConnectionPool assertAuthPool(String username, String password) throws SQLException {
C3P0PooledConnectionPool authPool = getPoolManager().getPool(username, password, false);
if (authPool == null) {
throw new SQLException("No pool has been yet been established for Connections authenticated by user '" + username + "' with the password provided. [Use getConnection( username, password ) " + "to initialize such a pool.]");
}

return authPool;
}

private void writeObject(ObjectOutputStream oos) throws IOException {
oos.writeShort(1);
}

private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
short version = ois.readShort();
switch (version) {

case 1:
setUpPropertyEvents();
return;
} 
throw new IOException("Unsupported Serialized Version: " + version);
}

public boolean isWrapperFor(Class<?> iface) throws SQLException {
return false;
}

public <T> T unwrap(Class<T> iface) throws SQLException {
throw new SQLException(this + " is not a Wrapper for " + iface.getName());
}
}

