package com.mchange.v2.c3p0.impl;

import com.mchange.v1.db.sql.ConnectionUtils;
import com.mchange.v1.db.sql.ResultSetUtils;
import com.mchange.v1.db.sql.StatementUtils;
import com.mchange.v1.lang.BooleanUtils;
import com.mchange.v2.async.AsynchronousRunner;
import com.mchange.v2.async.ThreadPoolAsynchronousRunner;
import com.mchange.v2.c3p0.C3P0Registry;
import com.mchange.v2.c3p0.ConnectionCustomizer;
import com.mchange.v2.c3p0.ConnectionTester;
import com.mchange.v2.c3p0.cfg.C3P0Config;
import com.mchange.v2.c3p0.cfg.C3P0ConfigUtils;
import com.mchange.v2.coalesce.CoalesceChecker;
import com.mchange.v2.coalesce.Coalescer;
import com.mchange.v2.coalesce.CoalescerFactory;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import com.mchange.v2.resourcepool.BasicResourcePoolFactory;
import com.mchange.v2.resourcepool.ResourcePoolFactory;
import com.mchange.v2.sql.SqlUtils;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

public final class C3P0PooledConnectionPoolManager
{
private static final MLogger logger = MLog.getLogger(C3P0PooledConnectionPoolManager.class);

private static final boolean POOL_EVENT_SUPPORT = false;

private static final CoalesceChecker COALESCE_CHECKER = IdentityTokenizedCoalesceChecker.INSTANCE;

static final Coalescer COALESCER = CoalescerFactory.createCoalescer(COALESCE_CHECKER, true, false);

static final int DFLT_NUM_TASK_THREADS_PER_DATA_SOURCE = 3;

ThreadPoolAsynchronousRunner taskRunner;

ThreadPoolAsynchronousRunner deferredStatementDestroyer;

Timer timer;

ResourcePoolFactory rpfact;

Map authsToPools;

final ConnectionPoolDataSource cpds;

final Map propNamesToReadMethods;
final Map flatPropertyOverrides;
final Map userOverrides;
final DbAuth defaultAuth;
final String parentDataSourceIdentityToken;
final String parentDataSourceName;
int num_task_threads = 3;

public int getThreadPoolSize() {
return this.taskRunner.getThreadCount();
}
public int getThreadPoolNumActiveThreads() {
return this.taskRunner.getActiveCount();
}
public int getThreadPoolNumIdleThreads() {
return this.taskRunner.getIdleCount();
}
public int getThreadPoolNumTasksPending() {
return this.taskRunner.getPendingTaskCount();
}
public String getThreadPoolStackTraces() {
return this.taskRunner.getStackTraces();
}
public String getThreadPoolStatus() {
return this.taskRunner.getStatus();
}
public int getStatementDestroyerNumThreads() {
return (this.deferredStatementDestroyer != null) ? this.deferredStatementDestroyer.getThreadCount() : -1;
}
public int getStatementDestroyerNumActiveThreads() {
return (this.deferredStatementDestroyer != null) ? this.deferredStatementDestroyer.getActiveCount() : -1;
}
public int getStatementDestroyerNumIdleThreads() {
return (this.deferredStatementDestroyer != null) ? this.deferredStatementDestroyer.getIdleCount() : -1;
}
public int getStatementDestroyerNumTasksPending() {
return (this.deferredStatementDestroyer != null) ? this.deferredStatementDestroyer.getPendingTaskCount() : -1;
}
public String getStatementDestroyerStackTraces() {
return (this.deferredStatementDestroyer != null) ? this.deferredStatementDestroyer.getStackTraces() : null;
}
public String getStatementDestroyerStatus() {
return (this.deferredStatementDestroyer != null) ? this.deferredStatementDestroyer.getStatus() : null;
}

private ThreadPoolAsynchronousRunner createTaskRunner(int num_threads, int matt, Timer timer, String threadLabel) {
ThreadPoolAsynchronousRunner out = null;
if (matt > 0) {

int matt_ms = matt * 1000;
out = new ThreadPoolAsynchronousRunner(num_threads, true, matt_ms, matt_ms * 3, matt_ms * 6, timer, threadLabel);

}
else {

out = new ThreadPoolAsynchronousRunner(num_threads, true, timer, threadLabel);
} 
return out;
}

private String idString() {
StringBuffer sb = new StringBuffer(512);
sb.append("C3P0PooledConnectionPoolManager");
sb.append('[');
sb.append("identityToken->");
sb.append(this.parentDataSourceIdentityToken);
if (this.parentDataSourceIdentityToken == null || !this.parentDataSourceIdentityToken.equals(this.parentDataSourceName)) {

sb.append(", dataSourceName->");
sb.append(this.parentDataSourceName);
} 
sb.append(']');
return sb.toString();
}

private void maybePrivilegedPoolsInit(boolean privilege_spawned_threads) {
if (privilege_spawned_threads) {

PrivilegedAction<Void> privilegedPoolsInit = new PrivilegedAction<Void>()
{
public Void run()
{
C3P0PooledConnectionPoolManager.this._poolsInit();
return null;
}
};
AccessController.doPrivileged(privilegedPoolsInit);
} else {

_poolsInit();
} 
}

private void poolsInit() {
final boolean privilege_spawned_threads = getPrivilegeSpawnedThreads();
String contextClassLoaderSource = getContextClassLoaderSource();
class ContextClassLoaderPoolsInitThread
extends Thread
{
ContextClassLoaderPoolsInitThread(ClassLoader ccl)
{
setContextClassLoader(ccl);
}
public void run() {
C3P0PooledConnectionPoolManager.this.maybePrivilegedPoolsInit(privilege_spawned_threads);
}
};

try {
if ("library".equalsIgnoreCase(contextClassLoaderSource))
{
Thread t = new ContextClassLoaderPoolsInitThread(getClass().getClassLoader());
t.start();
t.join();
}
else if ("none".equalsIgnoreCase(contextClassLoaderSource))
{
Thread t = new ContextClassLoaderPoolsInitThread(this, null, privilege_spawned_threads);
t.start();
t.join();
}
else
{
if (logger.isLoggable(MLevel.WARNING) && !"caller".equalsIgnoreCase(contextClassLoaderSource))
logger.log(MLevel.WARNING, "Unknown contextClassLoaderSource: " + contextClassLoaderSource + " -- should be 'caller', 'library', or 'none'. Using default value 'caller'."); 
maybePrivilegedPoolsInit(privilege_spawned_threads);
}

} catch (InterruptedException e) {

if (logger.isLoggable(MLevel.SEVERE)) {
logger.log(MLevel.SEVERE, "Unexpected interruption while trying to initialize DataSource Thread resources [ poolsInit() ].", e);
}
} 
}

private synchronized void _poolsInit() {
String idStr = idString();

this.timer = new Timer(idStr + "-AdminTaskTimer", true);

int matt = getMaxAdministrativeTaskTime();

this.taskRunner = createTaskRunner(this.num_task_threads, matt, this.timer, idStr + "-HelperThread");

int num_deferred_close_threads = getStatementCacheNumDeferredCloseThreads();

if (num_deferred_close_threads > 0) {
this.deferredStatementDestroyer = createTaskRunner(num_deferred_close_threads, matt, this.timer, idStr + "-DeferredStatementDestroyerThread");
} else {
this.deferredStatementDestroyer = null;
} 

this.rpfact = (ResourcePoolFactory)BasicResourcePoolFactory.createNoEventSupportInstance((AsynchronousRunner)this.taskRunner, this.timer);

this.authsToPools = new HashMap<Object, Object>();
}

private void poolsDestroy() {
poolsDestroy(true);
}

private synchronized void poolsDestroy(boolean close_outstanding_connections) {
for (Iterator<C3P0PooledConnectionPool> ii = this.authsToPools.values().iterator(); ii.hasNext();) {

try {
((C3P0PooledConnectionPool)ii.next()).close(close_outstanding_connections);
} catch (Exception e) {

logger.log(MLevel.WARNING, "An Exception occurred while trying to clean up a pool!", e);
} 
} 

this.taskRunner.close(true);

if (this.deferredStatementDestroyer != null) {
this.deferredStatementDestroyer.close(false);
}
this.timer.cancel();

this.taskRunner = null;
this.timer = null;
this.rpfact = null;
this.authsToPools = null;
}

public C3P0PooledConnectionPoolManager(ConnectionPoolDataSource cpds, Map flatPropertyOverrides, Map forceUserOverrides, int num_task_threads, String parentDataSourceIdentityToken, String parentDataSourceName) throws SQLException {
try {
this.cpds = cpds;
this.flatPropertyOverrides = flatPropertyOverrides;
this.num_task_threads = num_task_threads;
this.parentDataSourceIdentityToken = parentDataSourceIdentityToken;
this.parentDataSourceName = parentDataSourceName;

DbAuth auth = null;

if (flatPropertyOverrides != null) {

String overrideUser = (String)flatPropertyOverrides.get("overrideDefaultUser");
String overridePassword = (String)flatPropertyOverrides.get("overrideDefaultPassword");

if (overrideUser == null) {

overrideUser = (String)flatPropertyOverrides.get("user");
overridePassword = (String)flatPropertyOverrides.get("password");
} 

if (overrideUser != null) {
auth = new DbAuth(overrideUser, overridePassword);
}
} 
if (auth == null) {
auth = C3P0ImplUtils.findAuth(cpds);
}
this.defaultAuth = auth;

Map<Object, Object> tmp = new HashMap<Object, Object>();
BeanInfo bi = Introspector.getBeanInfo(cpds.getClass());
PropertyDescriptor[] pds = bi.getPropertyDescriptors();
PropertyDescriptor pd = null;
for (int i = 0, len = pds.length; i < len; i++) {

pd = pds[i];

String name = pd.getName();
Method m = pd.getReadMethod();

if (m != null)
tmp.put(name, m); 
} 
this.propNamesToReadMethods = tmp;

if (forceUserOverrides == null) {

Method uom = (Method)this.propNamesToReadMethods.get("userOverridesAsString");
if (uom != null) {

String uoas = (String)uom.invoke(cpds, null);

Map uo = C3P0ImplUtils.parseUserOverridesAsString(uoas);
this.userOverrides = uo;
} else {

this.userOverrides = Collections.EMPTY_MAP;
} 
} else {
this.userOverrides = forceUserOverrides;
} 
poolsInit();
}
catch (Exception e) {

logger.log(MLevel.FINE, null, e);

throw SqlUtils.toSQLException(e);
} 
}

public synchronized C3P0PooledConnectionPool getPool(String username, String password, boolean create) throws SQLException {
if (create) {
return getPool(username, password);
}

DbAuth checkAuth = new DbAuth(username, password);
C3P0PooledConnectionPool out = (C3P0PooledConnectionPool)this.authsToPools.get(checkAuth);
if (out == null) {
throw new SQLException("No pool has been initialized for databse user '" + username + "' with the specified password.");
}
return out;
}

public C3P0PooledConnectionPool getPool(String username, String password) throws SQLException {
return getPool(new DbAuth(username, password));
}

public synchronized C3P0PooledConnectionPool getPool(DbAuth auth) throws SQLException {
C3P0PooledConnectionPool out = (C3P0PooledConnectionPool)this.authsToPools.get(auth);
if (out == null) {

out = createPooledConnectionPool(auth);
this.authsToPools.put(auth, out);

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "Created new pool for auth, username (masked): '" + auth.getMaskedUserString() + "'."); 
} 
return out;
}

public synchronized Set getManagedAuths() {
return Collections.unmodifiableSet(this.authsToPools.keySet());
}
public synchronized int getNumManagedAuths() {
return this.authsToPools.size();
}

public C3P0PooledConnectionPool getPool() throws SQLException {
return getPool(this.defaultAuth);
}

public synchronized int getNumIdleConnectionsAllAuths() throws SQLException {
int out = 0;
for (Iterator<C3P0PooledConnectionPool> ii = this.authsToPools.values().iterator(); ii.hasNext();)
out += ((C3P0PooledConnectionPool)ii.next()).getNumIdleConnections(); 
return out;
}

public synchronized int getNumBusyConnectionsAllAuths() throws SQLException {
int out = 0;
for (Iterator<C3P0PooledConnectionPool> ii = this.authsToPools.values().iterator(); ii.hasNext();)
out += ((C3P0PooledConnectionPool)ii.next()).getNumBusyConnections(); 
return out;
}

public synchronized int getNumConnectionsAllAuths() throws SQLException {
int out = 0;
for (Iterator<C3P0PooledConnectionPool> ii = this.authsToPools.values().iterator(); ii.hasNext();)
out += ((C3P0PooledConnectionPool)ii.next()).getNumConnections(); 
return out;
}

public synchronized int getNumUnclosedOrphanedConnectionsAllAuths() throws SQLException {
int out = 0;
for (Iterator<C3P0PooledConnectionPool> ii = this.authsToPools.values().iterator(); ii.hasNext();)
out += ((C3P0PooledConnectionPool)ii.next()).getNumUnclosedOrphanedConnections(); 
return out;
}

public synchronized int getStatementCacheNumStatementsAllUsers() throws SQLException {
int out = 0;
for (Iterator<C3P0PooledConnectionPool> ii = this.authsToPools.values().iterator(); ii.hasNext();)
out += ((C3P0PooledConnectionPool)ii.next()).getStatementCacheNumStatements(); 
return out;
}

public synchronized int getStatementCacheNumCheckedOutStatementsAllUsers() throws SQLException {
int out = 0;
for (Iterator<C3P0PooledConnectionPool> ii = this.authsToPools.values().iterator(); ii.hasNext();)
out += ((C3P0PooledConnectionPool)ii.next()).getStatementCacheNumCheckedOut(); 
return out;
}

public synchronized int getStatementCacheNumConnectionsWithCachedStatementsAllUsers() throws SQLException {
int out = 0;
for (Iterator<C3P0PooledConnectionPool> ii = this.authsToPools.values().iterator(); ii.hasNext();)
out += ((C3P0PooledConnectionPool)ii.next()).getStatementCacheNumConnectionsWithCachedStatements(); 
return out;
}

public synchronized int getStatementDestroyerNumConnectionsInUseAllUsers() throws SQLException {
if (this.deferredStatementDestroyer != null) {

int out = 0;
for (Iterator<C3P0PooledConnectionPool> ii = this.authsToPools.values().iterator(); ii.hasNext();)
out += ((C3P0PooledConnectionPool)ii.next()).getStatementDestroyerNumConnectionsInUse(); 
return out;
} 

return -1;
}

public synchronized int getStatementDestroyerNumConnectionsWithDeferredDestroyStatementsAllUsers() throws SQLException {
if (this.deferredStatementDestroyer != null) {

int out = 0;
for (Iterator<C3P0PooledConnectionPool> ii = this.authsToPools.values().iterator(); ii.hasNext();)
out += ((C3P0PooledConnectionPool)ii.next()).getStatementDestroyerNumConnectionsWithDeferredDestroyStatements(); 
return out;
} 

return -1;
}

public synchronized int getStatementDestroyerNumDeferredDestroyStatementsAllUsers() throws SQLException {
if (this.deferredStatementDestroyer != null) {

int out = 0;
for (Iterator<C3P0PooledConnectionPool> ii = this.authsToPools.values().iterator(); ii.hasNext();)
out += ((C3P0PooledConnectionPool)ii.next()).getStatementDestroyerNumDeferredDestroyStatements(); 
return out;
} 

return -1;
}

public synchronized void softResetAllAuths() throws SQLException {
for (Iterator<C3P0PooledConnectionPool> ii = this.authsToPools.values().iterator(); ii.hasNext();)
((C3P0PooledConnectionPool)ii.next()).reset(); 
}

public void close() {
close(true);
}

public synchronized void close(boolean close_outstanding_connections) {
if (this.authsToPools != null) {
poolsDestroy(close_outstanding_connections);
}
}

protected synchronized void finalize() {
close();
}

private Object getObject(String propName, String userName) {
Object out = null;

if (userName != null) {
out = C3P0ConfigUtils.extractUserOverride(propName, userName, this.userOverrides);
}
if (out == null && this.flatPropertyOverrides != null) {
out = this.flatPropertyOverrides.get(propName);
}

if (out == null) {

try {

Method m = (Method)this.propNamesToReadMethods.get(propName);
if (m != null) {

Object readProp = m.invoke(this.cpds, null);
if (readProp != null) {
out = readProp.toString();
}
} 
} catch (Exception e) {

if (logger.isLoggable(MLevel.WARNING)) {
logger.log(MLevel.WARNING, "An exception occurred while trying to read property '" + propName + "' from ConnectionPoolDataSource: " + this.cpds + ". Default config value will be used.", e);
}
} 
}

if (out == null) {
out = C3P0Config.getUnspecifiedUserProperty(propName, null);
}
return out;
}

private String getString(String propName, String userName) {
Object o = getObject(propName, userName);
return (o == null) ? null : o.toString();
}

private int getInt(String propName, String userName) throws Exception {
Object o = getObject(propName, userName);
if (o instanceof Integer)
return ((Integer)o).intValue(); 
if (o instanceof String) {
return Integer.parseInt((String)o);
}
throw new Exception("Unexpected object found for putative int property '" + propName + "': " + o);
}

private boolean getBoolean(String propName, String userName) throws Exception {
Object o = getObject(propName, userName);
if (o instanceof Boolean)
return ((Boolean)o).booleanValue(); 
if (o instanceof String) {
return BooleanUtils.parseBoolean((String)o);
}
throw new Exception("Unexpected object found for putative boolean property '" + propName + "': " + o);
}

public String getAutomaticTestTable(String userName) {
return getString("automaticTestTable", userName);
}
public String getPreferredTestQuery(String userName) {
return getString("preferredTestQuery", userName);
}

private int getInitialPoolSize(String userName) {
try {
return getInt("initialPoolSize", userName);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "Could not fetch int property", e); 
return C3P0Defaults.initialPoolSize();
} 
}

public int getMinPoolSize(String userName) {
try {
return getInt("minPoolSize", userName);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "Could not fetch int property", e); 
return C3P0Defaults.minPoolSize();
} 
}

private int getMaxPoolSize(String userName) {
try {
return getInt("maxPoolSize", userName);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "Could not fetch int property", e); 
return C3P0Defaults.maxPoolSize();
} 
}

private int getMaxStatements(String userName) {
try {
return getInt("maxStatements", userName);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "Could not fetch int property", e); 
return C3P0Defaults.maxStatements();
} 
}

private int getMaxStatementsPerConnection(String userName) {
try {
return getInt("maxStatementsPerConnection", userName);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "Could not fetch int property", e); 
return C3P0Defaults.maxStatementsPerConnection();
} 
}

private int getAcquireIncrement(String userName) {
try {
return getInt("acquireIncrement", userName);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "Could not fetch int property", e); 
return C3P0Defaults.acquireIncrement();
} 
}

private int getAcquireRetryAttempts(String userName) {
try {
return getInt("acquireRetryAttempts", userName);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "Could not fetch int property", e); 
return C3P0Defaults.acquireRetryAttempts();
} 
}

private int getAcquireRetryDelay(String userName) {
try {
return getInt("acquireRetryDelay", userName);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "Could not fetch int property", e); 
return C3P0Defaults.acquireRetryDelay();
} 
}

private boolean getBreakAfterAcquireFailure(String userName) {
try {
return getBoolean("breakAfterAcquireFailure", userName);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "Could not fetch boolean property", e); 
return C3P0Defaults.breakAfterAcquireFailure();
} 
}

private int getCheckoutTimeout(String userName) {
try {
return getInt("checkoutTimeout", userName);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "Could not fetch int property", e); 
return C3P0Defaults.checkoutTimeout();
} 
}

private int getIdleConnectionTestPeriod(String userName) {
try {
return getInt("idleConnectionTestPeriod", userName);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "Could not fetch int property", e); 
return C3P0Defaults.idleConnectionTestPeriod();
} 
}

private int getMaxIdleTime(String userName) {
try {
return getInt("maxIdleTime", userName);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "Could not fetch int property", e); 
return C3P0Defaults.maxIdleTime();
} 
}

private int getUnreturnedConnectionTimeout(String userName) {
try {
return getInt("unreturnedConnectionTimeout", userName);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "Could not fetch int property", e); 
return C3P0Defaults.unreturnedConnectionTimeout();
} 
}

private boolean getTestConnectionOnCheckout(String userName) {
try {
return getBoolean("testConnectionOnCheckout", userName);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "Could not fetch boolean property", e); 
return C3P0Defaults.testConnectionOnCheckout();
} 
}

private boolean getTestConnectionOnCheckin(String userName) {
try {
return getBoolean("testConnectionOnCheckin", userName);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "Could not fetch boolean property", e); 
return C3P0Defaults.testConnectionOnCheckin();
} 
}

private boolean getDebugUnreturnedConnectionStackTraces(String userName) {
try {
return getBoolean("debugUnreturnedConnectionStackTraces", userName);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "Could not fetch boolean property", e); 
return C3P0Defaults.debugUnreturnedConnectionStackTraces();
} 
}

private String getConnectionTesterClassName(String userName) {
return getString("connectionTesterClassName", userName);
}
private ConnectionTester getConnectionTester(String userName) {
return C3P0Registry.getConnectionTester(getConnectionTesterClassName(userName));
}
private String getConnectionCustomizerClassName(String userName) {
return getString("connectionCustomizerClassName", userName);
}
private ConnectionCustomizer getConnectionCustomizer(String userName) throws SQLException {
return C3P0Registry.getConnectionCustomizer(getConnectionCustomizerClassName(userName));
}

private int getMaxIdleTimeExcessConnections(String userName) {
try {
return getInt("maxIdleTimeExcessConnections", userName);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "Could not fetch int property", e); 
return C3P0Defaults.maxIdleTimeExcessConnections();
} 
}

private int getMaxConnectionAge(String userName) {
try {
return getInt("maxConnectionAge", userName);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "Could not fetch int property", e); 
return C3P0Defaults.maxConnectionAge();
} 
}

private int getPropertyCycle(String userName) {
try {
return getInt("propertyCycle", userName);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "Could not fetch int property", e); 
return C3P0Defaults.propertyCycle();
} 
}

private String getContextClassLoaderSource() {
try {
return getString("contextClassLoaderSource", null);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "Could not fetch String property", e); 
return C3P0Defaults.contextClassLoaderSource();
} 
}

private boolean getPrivilegeSpawnedThreads() {
try {
return getBoolean("privilegeSpawnedThreads", null);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "Could not fetch boolean property", e); 
return C3P0Defaults.privilegeSpawnedThreads();
} 
}

private int getMaxAdministrativeTaskTime() {
try {
return getInt("maxAdministrativeTaskTime", null);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "Could not fetch int property", e); 
return C3P0Defaults.maxAdministrativeTaskTime();
} 
}

private int getStatementCacheNumDeferredCloseThreads() {
try {
return getInt("statementCacheNumDeferredCloseThreads", null);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINE))
logger.log(MLevel.FINE, "Could not fetch int property", e); 
return C3P0Defaults.statementCacheNumDeferredCloseThreads();
} 
}

private C3P0PooledConnectionPool createPooledConnectionPool(DbAuth auth) throws SQLException {
String realTestQuery, userName = auth.getUser();
String automaticTestTable = getAutomaticTestTable(userName);

if (automaticTestTable != null) {

realTestQuery = initializeAutomaticTestTable(automaticTestTable, auth);
if (getPreferredTestQuery(userName) != null)
{
if (logger.isLoggable(MLevel.WARNING))
{
logger.logp(MLevel.WARNING, C3P0PooledConnectionPoolManager.class.getName(), "createPooledConnectionPool", "[c3p0] Both automaticTestTable and preferredTestQuery have been set! Using automaticTestTable, and ignoring preferredTestQuery. Real test query is ''{0}''.", realTestQuery);

}

}

}
else {

if (!this.defaultAuth.equals(auth)) {
ensureFirstConnectionAcquisition(auth);
}
realTestQuery = getPreferredTestQuery(userName);
} 

C3P0PooledConnectionPool out = new C3P0PooledConnectionPool(this.cpds, auth, getMinPoolSize(userName), getMaxPoolSize(userName), getInitialPoolSize(userName), getAcquireIncrement(userName), getAcquireRetryAttempts(userName), getAcquireRetryDelay(userName), getBreakAfterAcquireFailure(userName), getCheckoutTimeout(userName), getIdleConnectionTestPeriod(userName), getMaxIdleTime(userName), getMaxIdleTimeExcessConnections(userName), getMaxConnectionAge(userName), getPropertyCycle(userName), getUnreturnedConnectionTimeout(userName), getDebugUnreturnedConnectionStackTraces(userName), getTestConnectionOnCheckout(userName), getTestConnectionOnCheckin(userName), getMaxStatements(userName), getMaxStatementsPerConnection(userName), getConnectionTester(userName), getConnectionCustomizer(userName), realTestQuery, this.rpfact, this.taskRunner, this.deferredStatementDestroyer, this.parentDataSourceIdentityToken);

return out;
}

private String initializeAutomaticTestTable(String automaticTestTable, DbAuth auth) throws SQLException {
PooledConnection throwawayPooledConnection = auth.equals(this.defaultAuth) ? this.cpds.getPooledConnection() : this.cpds.getPooledConnection(auth.getUser(), auth.getPassword());

Connection c = null;
PreparedStatement testStmt = null;
PreparedStatement createStmt = null;
ResultSet mdrs = null;
ResultSet rs = null;

try {
c = throwawayPooledConnection.getConnection();

DatabaseMetaData dmd = c.getMetaData();
String q = dmd.getIdentifierQuoteString();
String quotedTableName = q + automaticTestTable + q;
String out = "SELECT * FROM " + quotedTableName;
mdrs = dmd.getTables(null, null, automaticTestTable, new String[] { "TABLE" });
boolean exists = mdrs.next();

if (exists) {

testStmt = c.prepareStatement(out);
rs = testStmt.executeQuery();
boolean has_rows = rs.next();
if (has_rows) {
throw new SQLException("automatic test table '" + automaticTestTable + "' contains rows, and it should not! Please set this " + "parameter to the name of a table c3p0 can create on its own, " + "that is not used elsewhere in the database!");

}

}
else {

String createSql = "CREATE TABLE " + quotedTableName + " ( a CHAR(1) )";

try {
createStmt = c.prepareStatement(createSql);
createStmt.executeUpdate();
}
catch (SQLException e) {

if (logger.isLoggable(MLevel.WARNING)) {
logger.log(MLevel.WARNING, "An attempt to create an automatic test table failed. Create SQL: " + createSql, e);
}

throw e;
} 
} 
return out;
}
finally {

ResultSetUtils.attemptClose(mdrs);
ResultSetUtils.attemptClose(rs);
StatementUtils.attemptClose(testStmt);
StatementUtils.attemptClose(createStmt);
ConnectionUtils.attemptClose(c); try {
if (throwawayPooledConnection != null) throwawayPooledConnection.close(); 
} catch (Exception e) {

logger.log(MLevel.WARNING, "A PooledConnection failed to close.", e);
} 
} 
}

private void ensureFirstConnectionAcquisition(DbAuth auth) throws SQLException {
PooledConnection throwawayPooledConnection = auth.equals(this.defaultAuth) ? this.cpds.getPooledConnection() : this.cpds.getPooledConnection(auth.getUser(), auth.getPassword());

Connection c = null;

try {
c = throwawayPooledConnection.getConnection();
}
finally {

ConnectionUtils.attemptClose(c); try {
if (throwawayPooledConnection != null) throwawayPooledConnection.close(); 
} catch (Exception e) {

logger.log(MLevel.WARNING, "A PooledConnection failed to close.", e);
} 
} 
}
}

