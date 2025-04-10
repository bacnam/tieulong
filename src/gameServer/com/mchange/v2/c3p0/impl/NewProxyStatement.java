package com.mchange.v2.c3p0.impl;

import com.mchange.v2.c3p0.C3P0ProxyStatement;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import com.mchange.v2.sql.SqlUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;

public final class NewProxyStatement
implements Statement, C3P0ProxyStatement, ProxyResultSetDetachable
{
protected Statement inner;

private void __setInner(Statement inner) {
this.inner = inner;
}

NewProxyStatement(Statement inner) {
__setInner(inner);
}

public final void close() throws SQLException {
try {
maybeDirtyTransaction();

if (!isDetached())
{
synchronized (this.myProxyResultSets) {

for (Iterator<ResultSet> ii = this.myProxyResultSets.iterator(); ii.hasNext(); ) {

ResultSet closeMe = ii.next();
ii.remove();
try {
closeMe.close();
} catch (SQLException e) {

if (logger.isLoggable(MLevel.WARNING))
logger.log(MLevel.WARNING, "Exception on close of apparently orphaned ResultSet.", e); 
} 
if (logger.isLoggable(MLevel.FINE)) {
logger.log(MLevel.FINE, this + " closed orphaned ResultSet: " + closeMe);
}
} 
} 
if (this.is_cached) {
this.parentPooledConnection.checkinStatement(this.inner);
} else {

this.parentPooledConnection.markInactiveUncachedStatement(this.inner); try {
this.inner.close();
} catch (Exception e) {

if (logger.isLoggable(MLevel.WARNING))
logger.log(MLevel.WARNING, "Exception on close of inner statement.", e); 
SQLException sqle = SqlUtils.toSQLException(e);
throw sqle;
} 
} 

detach();
this.inner = null;
this.creatorProxy = null;
}

} catch (NullPointerException exc) {

if (isDetached()) {

if (logger.isLoggable(MLevel.FINE))
{
logger.log(MLevel.FINE, this + ": close() called more than once.");
}
} else {
throw exc;
} 
} catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final Connection getConnection() throws SQLException {
try {
maybeDirtyTransaction();

if (!isDetached()) {
return this.creatorProxy;
}
throw new SQLException("You cannot operate on a closed Statement!");
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final SQLWarning getWarnings() throws SQLException {
try {
maybeDirtyTransaction();

return this.inner.getWarnings();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final void clearWarnings() throws SQLException {
try {
maybeDirtyTransaction();

this.inner.clearWarnings();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final boolean isClosed() throws SQLException {
try {
maybeDirtyTransaction();

return isDetached();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final boolean execute(String a) throws SQLException {
try {
maybeDirtyTransaction();

return this.inner.execute(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final boolean execute(String a, String[] b) throws SQLException {
try {
maybeDirtyTransaction();

return this.inner.execute(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final boolean execute(String a, int[] b) throws SQLException {
try {
maybeDirtyTransaction();

return this.inner.execute(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final boolean execute(String a, int b) throws SQLException {
try {
maybeDirtyTransaction();

return this.inner.execute(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final ResultSet executeQuery(String a) throws SQLException {
try {
maybeDirtyTransaction();

ResultSet innerResultSet = this.inner.executeQuery(a);
if (innerResultSet == null) return null; 
this.parentPooledConnection.markActiveResultSetForStatement(this.inner, innerResultSet);
NewProxyResultSet out = new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
synchronized (this.myProxyResultSets) { this.myProxyResultSets.add(out); }
return out;
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final int executeUpdate(String a, int b) throws SQLException {
try {
maybeDirtyTransaction();

return this.inner.executeUpdate(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final int executeUpdate(String a, int[] b) throws SQLException {
try {
maybeDirtyTransaction();

return this.inner.executeUpdate(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final int executeUpdate(String a, String[] b) throws SQLException {
try {
maybeDirtyTransaction();

return this.inner.executeUpdate(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final int executeUpdate(String a) throws SQLException {
try {
maybeDirtyTransaction();

return this.inner.executeUpdate(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final int getMaxFieldSize() throws SQLException {
try {
maybeDirtyTransaction();

return this.inner.getMaxFieldSize();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final void setMaxFieldSize(int a) throws SQLException {
try {
maybeDirtyTransaction();

this.inner.setMaxFieldSize(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final int getMaxRows() throws SQLException {
try {
maybeDirtyTransaction();

return this.inner.getMaxRows();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final void setMaxRows(int a) throws SQLException {
try {
maybeDirtyTransaction();

this.inner.setMaxRows(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final void setEscapeProcessing(boolean a) throws SQLException {
try {
maybeDirtyTransaction();

this.inner.setEscapeProcessing(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final int getQueryTimeout() throws SQLException {
try {
maybeDirtyTransaction();

return this.inner.getQueryTimeout();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final void setQueryTimeout(int a) throws SQLException {
try {
maybeDirtyTransaction();

this.inner.setQueryTimeout(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final void cancel() throws SQLException {
try {
maybeDirtyTransaction();

this.inner.cancel();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final void setCursorName(String a) throws SQLException {
try {
maybeDirtyTransaction();

this.inner.setCursorName(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final ResultSet getResultSet() throws SQLException {
try {
maybeDirtyTransaction();

ResultSet innerResultSet = this.inner.getResultSet();
if (innerResultSet == null) return null; 
this.parentPooledConnection.markActiveResultSetForStatement(this.inner, innerResultSet);
NewProxyResultSet out = new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
synchronized (this.myProxyResultSets) { this.myProxyResultSets.add(out); }
return out;
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final int getUpdateCount() throws SQLException {
try {
maybeDirtyTransaction();

return this.inner.getUpdateCount();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final boolean getMoreResults(int a) throws SQLException {
try {
maybeDirtyTransaction();

return this.inner.getMoreResults(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final boolean getMoreResults() throws SQLException {
try {
maybeDirtyTransaction();

return this.inner.getMoreResults();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final void setFetchDirection(int a) throws SQLException {
try {
maybeDirtyTransaction();

this.inner.setFetchDirection(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final int getFetchDirection() throws SQLException {
try {
maybeDirtyTransaction();

return this.inner.getFetchDirection();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final void setFetchSize(int a) throws SQLException {
try {
maybeDirtyTransaction();

this.inner.setFetchSize(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final int getFetchSize() throws SQLException {
try {
maybeDirtyTransaction();

return this.inner.getFetchSize();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final int getResultSetConcurrency() throws SQLException {
try {
maybeDirtyTransaction();

return this.inner.getResultSetConcurrency();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final int getResultSetType() throws SQLException {
try {
maybeDirtyTransaction();

return this.inner.getResultSetType();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final void addBatch(String a) throws SQLException {
try {
maybeDirtyTransaction();

this.inner.addBatch(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final void clearBatch() throws SQLException {
try {
maybeDirtyTransaction();

this.inner.clearBatch();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final int[] executeBatch() throws SQLException {
try {
maybeDirtyTransaction();

return this.inner.executeBatch();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final ResultSet getGeneratedKeys() throws SQLException {
try {
maybeDirtyTransaction();

ResultSet innerResultSet = this.inner.getGeneratedKeys();
if (innerResultSet == null) return null; 
this.parentPooledConnection.markActiveResultSetForStatement(this.inner, innerResultSet);
NewProxyResultSet out = new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
synchronized (this.myProxyResultSets) { this.myProxyResultSets.add(out); }
return out;
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final int getResultSetHoldability() throws SQLException {
try {
maybeDirtyTransaction();

return this.inner.getResultSetHoldability();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final void setPoolable(boolean a) throws SQLException {
try {
maybeDirtyTransaction();

this.inner.setPoolable(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final boolean isPoolable() throws SQLException {
try {
maybeDirtyTransaction();

return this.inner.isPoolable();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final void closeOnCompletion() throws SQLException {
try {
maybeDirtyTransaction();

this.inner.closeOnCompletion();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final boolean isCloseOnCompletion() throws SQLException {
try {
maybeDirtyTransaction();

return this.inner.isCloseOnCompletion();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
}
throw exc;
}
catch (Exception exc) {

if (!isDetached())
{
throw this.parentPooledConnection.handleThrowable(exc);
}
throw SqlUtils.toSQLException(exc);
} 
}

public final Object unwrap(Class a) throws SQLException {
if (isWrapperFor(a)) return this.inner; 
throw new SQLException(this + " is not a wrapper for " + a.getName());
}

public final boolean isWrapperFor(Class<Statement> a) throws SQLException {
return (Statement.class == a || a.isAssignableFrom(this.inner.getClass()));
}

private static final MLogger logger = MLog.getLogger("com.mchange.v2.c3p0.impl.NewProxyStatement");

volatile NewPooledConnection parentPooledConnection;

ConnectionEventListener cel = new ConnectionEventListener()
{
public void connectionErrorOccurred(ConnectionEvent evt) {}

public void connectionClosed(ConnectionEvent evt) {
NewProxyStatement.this.detach();
}
};
boolean is_cached;
void attach(NewPooledConnection parentPooledConnection) {
this.parentPooledConnection = parentPooledConnection;
parentPooledConnection.addConnectionEventListener(this.cel);
}
NewProxyConnection creatorProxy;

private void detach() {
this.parentPooledConnection.removeConnectionEventListener(this.cel);
this.parentPooledConnection = null;
}

NewProxyStatement(Statement inner, NewPooledConnection parentPooledConnection) {
this(inner);
attach(parentPooledConnection);
}

boolean isDetached() {
return (this.parentPooledConnection == null);
}

HashSet myProxyResultSets = new HashSet();

public void detachProxyResultSet(ResultSet prs) {
synchronized (this.myProxyResultSets) { this.myProxyResultSets.remove(prs); }

}

NewProxyStatement(Statement inner, NewPooledConnection parentPooledConnection, boolean cached, NewProxyConnection cProxy) {
this(inner, parentPooledConnection);
this.is_cached = cached;
this.creatorProxy = cProxy;
}

public Object rawStatementOperation(Method m, Object target, Object[] args) throws IllegalAccessException, InvocationTargetException, SQLException {
maybeDirtyTransaction();

if (target == C3P0ProxyStatement.RAW_STATEMENT) target = this.inner; 
for (int i = 0, len = args.length; i < len; i++) {
if (args[i] == C3P0ProxyStatement.RAW_STATEMENT) args[i] = this.inner; 
}  Object out = m.invoke(target, args);
if (out instanceof ResultSet) {

ResultSet innerResultSet = (ResultSet)out;
this.parentPooledConnection.markActiveResultSetForStatement(this.inner, innerResultSet);
out = new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
} 

return out;
}

void maybeDirtyTransaction() {
if (this.creatorProxy != null) this.creatorProxy.maybeDirtyTransaction(); 
}
}

