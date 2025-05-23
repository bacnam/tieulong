package com.mchange.v2.c3p0.impl;

import com.mchange.v2.c3p0.ConnectionCustomizer;
import com.mchange.v2.c3p0.ConnectionTester;
import com.mchange.v2.c3p0.FullQueryConnectionTester;
import com.mchange.v2.c3p0.stmt.GooGooStatementCache;
import com.mchange.v2.c3p0.util.ConnectionEventSupport;
import com.mchange.v2.c3p0.util.StatementEventSupport;
import com.mchange.v2.lang.ObjectUtils;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import com.mchange.v2.sql.SqlUtils;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.sql.ConnectionEventListener;
import javax.sql.StatementEventListener;

public final class NewPooledConnection
extends AbstractC3P0PooledConnection
{
private static final MLogger logger = MLog.getLogger(NewPooledConnection.class);

private static final SQLException NORMAL_CLOSE_PLACEHOLDER = new SQLException("This pooled Connection was explicitly close()ed by a client, not invalidated due to an error.");

static Set holdabilityBugKeys = null;

final Connection physicalConnection;

final ConnectionTester connectionTester;

final boolean autoCommitOnClose;

final boolean forceIgnoreUnresolvedTransactions;
final String preferredTestQuery;
final boolean supports_setHoldability;
final boolean supports_setReadOnly;
final boolean supports_setTypeMap;
final int dflt_txn_isolation;
final String dflt_catalog;
final int dflt_holdability;
final boolean dflt_readOnly;
final Map dflt_typeMap;
final ConnectionEventSupport ces;
final StatementEventSupport ses;
GooGooStatementCache scache = null;
Throwable invalidatingException = null;
int connection_status = 0;
Set uncachedActiveStatements = new HashSet();
Map resultSetsForStatements = new HashMap<Object, Object>();
Set metaDataResultSets = new HashSet();
Set rawConnectionResultSets = null;

boolean connection_error_signaled = false;

volatile NewProxyConnection exposedProxy = null;

volatile boolean isolation_lvl_nondefault = false;

volatile boolean catalog_nondefault = false;

volatile boolean holdability_nondefault = false;

volatile boolean readOnly_nondefault = false;

volatile boolean typeMap_nondefault = false;

public NewPooledConnection(Connection con, ConnectionTester connectionTester, boolean autoCommitOnClose, boolean forceIgnoreUnresolvedTransactions, String preferredTestQuery, ConnectionCustomizer cc, String pdsIdt) throws SQLException {
try {
if (cc != null) {
cc.onAcquire(con, pdsIdt);
}
} catch (Exception e) {
throw SqlUtils.toSQLException(e);
} 
this.physicalConnection = con;
this.connectionTester = connectionTester;
this.autoCommitOnClose = autoCommitOnClose;
this.forceIgnoreUnresolvedTransactions = forceIgnoreUnresolvedTransactions;
this.preferredTestQuery = preferredTestQuery;
this.supports_setHoldability = C3P0ImplUtils.supportsMethod(con, "setHoldability", new Class[] { int.class });
this.supports_setReadOnly = C3P0ImplUtils.supportsMethod(con, "setReadOnly", new Class[] { boolean.class });
this.supports_setTypeMap = C3P0ImplUtils.supportsMethod(con, "setTypeMap", new Class[] { Map.class });
this.dflt_txn_isolation = con.getTransactionIsolation();
this.dflt_catalog = con.getCatalog();
this.dflt_holdability = this.supports_setHoldability ? carefulCheckHoldability(con) : 2;
this.dflt_readOnly = this.supports_setReadOnly ? carefulCheckReadOnly(con) : false;
this.dflt_typeMap = (this.supports_setTypeMap && carefulCheckTypeMap(con) == null) ? null : Collections.EMPTY_MAP;
this.ces = new ConnectionEventSupport(this);
this.ses = new StatementEventSupport(this);
}

private static int carefulCheckHoldability(Connection con) {
try {
return con.getHoldability();
} catch (Exception e) {

return 2;
}
catch (Error e) {

synchronized (NewPooledConnection.class) {

if (holdabilityBugKeys == null)
holdabilityBugKeys = new HashSet(); 
String hbk = holdabilityBugKey(con, e);
if (!holdabilityBugKeys.contains(hbk)) {

if (logger.isLoggable(MLevel.WARNING)) {
logger.log(MLevel.WARNING, con + " threw an Error when we tried to check its default " + "holdability. This is probably due to a bug in your JDBC driver that c3p0 can harmlessly " + "work around (reported for some DB2 drivers). Please verify that the error stack trace is consistent" + "with the getHoldability() method not being properly implemented, and is not due to some deeper problem. " + "This message will not be repeated for Connections of type " + con.getClass().getName() + " that " + "provoke errors of type " + e.getClass().getName() + " when getHoldability() is called.", e);
}

holdabilityBugKeys.add(hbk);
} 
} 
return 2;
} 
}

private static String holdabilityBugKey(Connection con, Error err) {
return con.getClass().getName() + '|' + err.getClass().getName();
}
private static boolean carefulCheckReadOnly(Connection con) {
try {
return con.isReadOnly();
} catch (Exception e) {

return false;
} 
}

private static Map carefulCheckTypeMap(Connection con) {
try {
return con.getTypeMap();
} catch (Exception e) {

return null;
} 
}

public synchronized Connection getConnection() throws SQLException {
try {
if (this.exposedProxy == null) {

this.exposedProxy = new NewProxyConnection(this.physicalConnection, this);

}
else if (logger.isLoggable(MLevel.WARNING)) {
logger.log(MLevel.WARNING, "c3p0 -- Uh oh... getConnection() was called on a PooledConnection when it had already provided a client with a Connection that has not yet been closed. This probably indicates a bug in the connection pool!!!");
} 

return this.exposedProxy;
}
catch (Exception e) {

SQLException sqle = handleThrowable(e);
throw sqle;
} 
}

public synchronized int getConnectionStatus() {
return this.connection_status;
}

public synchronized void closeAll() throws SQLException {
try {
closeAllCachedStatements();
}
catch (Exception e) {

SQLException sqle = handleThrowable(e);
throw sqle;
} 
}

synchronized void closeMaybeCheckedOut(boolean checked_out) throws SQLException {
close(null, checked_out);
}
public synchronized void close() throws SQLException {
close(null);
}
public void addConnectionEventListener(ConnectionEventListener cel) {
this.ces.addConnectionEventListener(cel);
}
public void removeConnectionEventListener(ConnectionEventListener cel) {
this.ces.removeConnectionEventListener(cel);
}
public void printConnectionListeners() {
this.ces.printListeners();
}

public void addStatementEventListener(StatementEventListener sel) {
if (logger.isLoggable(MLevel.INFO)) {
logger.info("Per the JDBC4 spec, " + getClass().getName() + " accepts StatementListeners, but for now there is no circumstance under which they are notified!");
}

this.ses.addStatementEventListener(sel);
}

public void removeStatementEventListener(StatementEventListener sel) {
this.ses.removeStatementEventListener(sel);
}

public void printStatementListeners() {
this.ses.printListeners();
}

public synchronized void initStatementCache(GooGooStatementCache scache) {
this.scache = scache;
}
public synchronized GooGooStatementCache getStatementCache() {
return this.scache;
}

void markNewTxnIsolation(int lvl) {
this.isolation_lvl_nondefault = (lvl != this.dflt_txn_isolation);
}

void markNewCatalog(String catalog) {
this.catalog_nondefault = ObjectUtils.eqOrBothNull(catalog, this.dflt_catalog);
}

void markNewHoldability(int holdability) {
this.holdability_nondefault = (holdability != this.dflt_holdability);
}

void markNewReadOnly(boolean readOnly) {
this.readOnly_nondefault = (readOnly != this.dflt_readOnly);
}

void markNewTypeMap(Map typeMap) {
this.typeMap_nondefault = (typeMap != this.dflt_typeMap);
}

synchronized Object checkoutStatement(Method stmtProducingMethod, Object[] args) throws SQLException {
return this.scache.checkoutStatement(this.physicalConnection, stmtProducingMethod, args);
}

synchronized void checkinStatement(Statement stmt) throws SQLException {
cleanupStatementResultSets(stmt);
this.scache.checkinStatement(stmt);
}

synchronized void markActiveUncachedStatement(Statement stmt) {
this.uncachedActiveStatements.add(stmt);
}

synchronized void markInactiveUncachedStatement(Statement stmt) {
cleanupStatementResultSets(stmt);
this.uncachedActiveStatements.remove(stmt);
}

synchronized void markActiveResultSetForStatement(Statement stmt, ResultSet rs) {
Set<ResultSet> rss = resultSets(stmt, true);
rss.add(rs);
}

synchronized void markInactiveResultSetForStatement(Statement stmt, ResultSet rs) {
Set rss = resultSets(stmt, false);
if (rss == null) {

if (logger.isLoggable(MLevel.FINE)) {
logger.fine("ResultSet " + rs + " was apparently closed after the Statement that created it had already been closed.");
}
} else if (!rss.remove(rs)) {
throw new InternalError("Marking a ResultSet inactive that we did not know was opened!");
} 
}

synchronized void markActiveRawConnectionResultSet(ResultSet rs) {
if (this.rawConnectionResultSets == null)
this.rawConnectionResultSets = new HashSet(); 
this.rawConnectionResultSets.add(rs);
}

synchronized void markInactiveRawConnectionResultSet(ResultSet rs) {
if (!this.rawConnectionResultSets.remove(rs))
throw new InternalError("Marking a raw Connection ResultSet inactive that we did not know was opened!"); 
}

synchronized void markActiveMetaDataResultSet(ResultSet rs) {
this.metaDataResultSets.add(rs);
}
synchronized void markInactiveMetaDataResultSet(ResultSet rs) {
this.metaDataResultSets.remove(rs);
}

void markClosedProxyConnection(NewProxyConnection npc, boolean txn_known_resolved) {
SQLException trouble = null;

try {
synchronized (this)
{

try {
if (npc != this.exposedProxy) {
throw new InternalError("C3P0 Error: An exposed proxy asked a PooledConnection that was not its parents to clean up its resources!");
}

this.exposedProxy = null;

List<Throwable> closeExceptions = new LinkedList();
cleanupResultSets(closeExceptions);
cleanupUncachedStatements(closeExceptions);
checkinAllCachedStatements(closeExceptions);
if (closeExceptions.size() > 0) {

if (logger.isLoggable(MLevel.INFO))
logger.info("[c3p0] The following Exceptions occurred while trying to clean up a Connection's stranded resources:"); 
for (Iterator<Throwable> ii = closeExceptions.iterator(); ii.hasNext(); ) {

Throwable t = ii.next();

if (logger.isLoggable(MLevel.INFO))
logger.log(MLevel.INFO, "[c3p0 -- conection resource close Exception]", t); 
} 
} 
reset(txn_known_resolved);

if (closeExceptions.size() > 0) {
trouble = SqlUtils.toSQLException(closeExceptions.get(0));
}
} catch (SQLException e) {

if (logger.isLoggable(MLevel.FINE)) {
logger.log(MLevel.FINE, "An exception occurred while reseting a closed Connection. Invalidating Connection.", e);
}
updateConnectionStatus(-1);

trouble = e;

}

}

}
finally {

if (trouble != null)
{

fireConnectionErrorOccurred(trouble);
}

fireConnectionClosed();
} 
}

private void reset(boolean txn_known_resolved) throws SQLException {
C3P0ImplUtils.resetTxnState(this.physicalConnection, this.forceIgnoreUnresolvedTransactions, this.autoCommitOnClose, txn_known_resolved);
if (this.isolation_lvl_nondefault) {

this.physicalConnection.setTransactionIsolation(this.dflt_txn_isolation);
this.isolation_lvl_nondefault = false;
} 

if (this.catalog_nondefault) {

this.physicalConnection.setCatalog(this.dflt_catalog);
this.catalog_nondefault = false;
} 
if (this.holdability_nondefault) {

this.physicalConnection.setHoldability(this.dflt_holdability);
this.holdability_nondefault = false;
} 
if (this.readOnly_nondefault) {

this.physicalConnection.setReadOnly(this.dflt_readOnly);
this.readOnly_nondefault = false;
} 
if (this.typeMap_nondefault) {

this.physicalConnection.setTypeMap(this.dflt_typeMap);
this.typeMap_nondefault = false;
} 
}

synchronized boolean isStatementCaching() {
return (this.scache != null);
}

SQLException handleThrowable(Throwable t) {
boolean fire_cxn_error = false;
SQLException sqle = null;

try {
synchronized (this) {
int status;
if (logger.isLoggable(MLevel.FINER)) {
logger.log(MLevel.FINER, this + " handling a throwable.", t);
}
sqle = SqlUtils.toSQLException(t);

if (this.connectionTester instanceof FullQueryConnectionTester) {
status = ((FullQueryConnectionTester)this.connectionTester).statusOnException(this.physicalConnection, sqle, this.preferredTestQuery);
} else {
status = this.connectionTester.statusOnException(this.physicalConnection, sqle);
} 
updateConnectionStatus(status);
if (status != 0)
{

if (logger.isLoggable(MLevel.FINE)) {
logger.log(MLevel.FINE, this + " invalidated by Exception.", t);
}

if (!this.connection_error_signaled) {
fire_cxn_error = true;

}
else if (logger.isLoggable(MLevel.WARNING)) {

logger.log(MLevel.WARNING, "[c3p0] A PooledConnection that has already signalled a Connection error is still in use!");
logger.log(MLevel.WARNING, "[c3p0] Another error has occurred [ " + t + " ] which will not be reported to listeners!", t);
}

}

} 
} finally {

if (fire_cxn_error) {

fireConnectionErrorOccurred(sqle);
this.connection_error_signaled = true;
} 
} 
return sqle;
}

private void fireConnectionClosed() {
assert !Thread.holdsLock(this);
this.ces.fireConnectionClosed();
}

private void fireConnectionErrorOccurred(SQLException error) {
assert !Thread.holdsLock(this);
this.ces.fireConnectionErrorOccurred(error);
}

private void close(Throwable cause) throws SQLException {
close(cause, false);
}

private void close(Throwable cause, boolean forced) throws SQLException {
assert Thread.holdsLock(this);

if (this.invalidatingException == null) {

List<SQLException> closeExceptions = new LinkedList();

cleanupResultSets(closeExceptions);

cleanupUncachedStatements(closeExceptions);

try {
closeAllCachedStatements();
} catch (SQLException e) {
closeExceptions.add(e);
} 
if (forced) {

try {
C3P0ImplUtils.resetTxnState(this.physicalConnection, this.forceIgnoreUnresolvedTransactions, this.autoCommitOnClose, false);
} catch (Exception e) {

if (logger.isLoggable(MLevel.FINER)) {
logger.log(MLevel.FINER, "Failed to reset the transaction state of  " + this.physicalConnection + "just prior to close(). " + "Only relevant at all if this was a Connection being forced close()ed midtransaction.", e);
}
} 
}

try {
this.physicalConnection.close();
} catch (SQLException e) {

if (logger.isLoggable(MLevel.FINER)) {
logger.log(MLevel.FINER, "Failed to close physical Connection: " + this.physicalConnection, e);
}
closeExceptions.add(e);
} 

if (this.connection_status == 0)
this.connection_status = -1; 
if (cause == null) {

this.invalidatingException = NORMAL_CLOSE_PLACEHOLDER;

if (logger.isLoggable(MLevel.FINEST)) {
logger.log(MLevel.FINEST, this + " closed by a client.", new Exception("DEBUG -- CLOSE BY CLIENT STACK TRACE"));
}
logCloseExceptions(null, closeExceptions);

if (closeExceptions.size() > 0) {
throw new SQLException("Some resources failed to close properly while closing " + this);
}
} else {

this.invalidatingException = cause;

logCloseExceptions(cause, closeExceptions);
} 
} 
}

private void cleanupResultSets(List closeExceptions) {
cleanupAllStatementResultSets(closeExceptions);
cleanupUnclosedResultSetsSet(this.metaDataResultSets, closeExceptions);
if (this.rawConnectionResultSets != null) {
cleanupUnclosedResultSetsSet(this.rawConnectionResultSets, closeExceptions);
}
}

private void cleanupUnclosedResultSetsSet(Set rsSet, List<SQLException> closeExceptions) {
for (Iterator<ResultSet> ii = rsSet.iterator(); ii.hasNext(); ) {

ResultSet rs = ii.next();
try {
rs.close();
} catch (SQLException e) {
closeExceptions.add(e);
} 
ii.remove();
} 
}

private void cleanupStatementResultSets(Statement stmt) {
Set rss = resultSets(stmt, false);
if (rss != null)
{
for (Iterator<ResultSet> ii = rss.iterator(); ii.hasNext();) {

try {
((ResultSet)ii.next()).close();
} catch (Exception e) {

if (logger.isLoggable(MLevel.INFO))
logger.log(MLevel.INFO, "ResultSet close() failed.", e); 
} 
} 
}
this.resultSetsForStatements.remove(stmt);
}

private void cleanupAllStatementResultSets(List<SQLException> closeExceptions) {
for (Iterator ii = this.resultSetsForStatements.keySet().iterator(); ii.hasNext(); ) {

Object stmt = ii.next();
Set rss = (Set)this.resultSetsForStatements.get(stmt);
for (Iterator<ResultSet> jj = rss.iterator(); jj.hasNext(); ) {

ResultSet rs = jj.next();
try {
rs.close();
} catch (SQLException e) {

closeExceptions.add(e);

if (logger.isLoggable(MLevel.FINER)) {
logger.log(MLevel.FINER, "An Exception occurred while trying to cleanup the following ResultSet: " + rs, e);
}
} 
} 
} 

this.resultSetsForStatements.clear();
}

private void cleanupUncachedStatements(List<SQLException> closeExceptions) {
for (Iterator<Statement> ii = this.uncachedActiveStatements.iterator(); ii.hasNext(); ) {

Statement stmt = ii.next();
try {
stmt.close();
} catch (SQLException e) {

closeExceptions.add(e);

if (logger.isLoggable(MLevel.FINER)) {
logger.log(MLevel.FINER, "An Exception occurred while trying to cleanup the following uncached Statement: " + stmt, e);
}
} 

ii.remove();
} 
}

private void checkinAllCachedStatements(List<SQLException> closeExceptions) {
try {
if (this.scache != null) {
this.scache.checkinAll(this.physicalConnection);
}
} catch (SQLException e) {
closeExceptions.add(e);
} 
}

private void closeAllCachedStatements() throws SQLException {
if (this.scache != null) {
this.scache.closeAll(this.physicalConnection);
}
}

private void updateConnectionStatus(int status) {
switch (this.connection_status) {
case -8:
return;

case -1:
if (status == -8) {
this.connection_status = status;
}
case 0:
if (status != 0) {
this.connection_status = status;
}
} 
throw new InternalError(this + " -- Illegal Connection Status: " + this.connection_status);
}

private Set resultSets(Statement stmt, boolean create) {
Set out = (Set)this.resultSetsForStatements.get(stmt);
if (out == null && create) {

out = new HashSet();
this.resultSetsForStatements.put(stmt, out);
} 
return out;
}

Connection getPhysicalConnection() {
return this.physicalConnection;
}

private static void logCloseExceptions(Throwable cause, Collection exceptions) {
if (logger.isLoggable(MLevel.INFO)) {

if (cause != null)
{

logger.log(MLevel.INFO, "[c3p0] A PooledConnection died due to the following error!", cause);
}
if (exceptions != null && exceptions.size() > 0) {

if (cause == null) {
logger.info("[c3p0] Exceptions occurred while trying to close a PooledConnection's resources normally.");
} else {

logger.info("[c3p0] Exceptions occurred while trying to close a Broken PooledConnection.");
} 
for (Iterator<Throwable> ii = exceptions.iterator(); ii.hasNext(); ) {

Throwable t = ii.next();

logger.log(MLevel.INFO, "[c3p0] NewPooledConnection close Exception.", t);
} 
} 
} 
}
}

