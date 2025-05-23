package com.mchange.v2.c3p0.impl;

import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import com.mchange.v2.sql.SqlUtils;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;

public final class NewProxyResultSet
implements ResultSet
{
protected ResultSet inner;

private void __setInner(ResultSet inner) {
this.inner = inner;
}

NewProxyResultSet(ResultSet inner) {
__setInner(inner);
}

public final Object getObject(String a, Map<String, Class<?>> b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getObject(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Object getObject(int a, Map<String, Class<?>> b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getObject(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Object getObject(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getObject(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Object getObject(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getObject(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Object getObject(int a, Class<?> b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getObject(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Object getObject(String a, Class<?> b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getObject(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final boolean getBoolean(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getBoolean(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final boolean getBoolean(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getBoolean(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final byte getByte(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getByte(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final byte getByte(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getByte(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final short getShort(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getShort(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final short getShort(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getShort(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final int getInt(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getInt(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final int getInt(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getInt(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final long getLong(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getLong(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final long getLong(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getLong(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final float getFloat(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getFloat(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final float getFloat(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getFloat(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final double getDouble(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getDouble(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final double getDouble(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getDouble(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final byte[] getBytes(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getBytes(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final byte[] getBytes(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getBytes(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Array getArray(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getArray(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Array getArray(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getArray(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final boolean next() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.next();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final URL getURL(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getURL(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final URL getURL(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getURL(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void close() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

if (!isDetached())
{
if (this.creator instanceof Statement)
{ this.parentPooledConnection.markInactiveResultSetForStatement((Statement)this.creator, this.inner); }
else if (this.creator instanceof java.sql.DatabaseMetaData)
{ this.parentPooledConnection.markInactiveMetaDataResultSet(this.inner); }
else if (this.creator instanceof java.sql.Connection)
{ this.parentPooledConnection.markInactiveRawConnectionResultSet(this.inner); }
else { throw new InternalError("Must be Statement or DatabaseMetaData -- Bad Creator: " + this.creator); }
if (this.creatorProxy instanceof ProxyResultSetDetachable) ((ProxyResultSetDetachable)this.creatorProxy).detachProxyResultSet(this); 
detach();
this.inner.close();
this.inner = null;
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

public final int getType() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getType();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final boolean previous() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.previous();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Ref getRef(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getRef(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Ref getRef(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getRef(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final String getString(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getString(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final String getString(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getString(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Time getTime(int a, Calendar b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getTime(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Time getTime(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getTime(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Time getTime(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getTime(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Time getTime(String a, Calendar b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getTime(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Date getDate(String a, Calendar b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getDate(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Date getDate(int a, Calendar b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getDate(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Date getDate(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getDate(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Date getDate(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getDate(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final boolean first() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.first();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final boolean last() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.last();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final ResultSetMetaData getMetaData() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getMetaData();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getWarnings();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.clearWarnings();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return isDetached();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Statement getStatement() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

if (this.creator instanceof Statement)
return (Statement)this.creatorProxy; 
if (this.creator instanceof java.sql.DatabaseMetaData)
return null; 
throw new InternalError("Must be Statement or DatabaseMetaData -- Bad Creator: " + this.creator);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final InputStream getUnicodeStream(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getUnicodeStream(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final InputStream getUnicodeStream(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getUnicodeStream(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final String getCursorName() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getCursorName();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final int findColumn(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.findColumn(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final boolean isBeforeFirst() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.isBeforeFirst();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final boolean isAfterLast() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.isAfterLast();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final boolean isFirst() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.isFirst();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final boolean isLast() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.isLast();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void beforeFirst() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.beforeFirst();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void afterLast() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.afterLast();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final int getRow() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getRow();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final boolean absolute(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.absolute(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final boolean relative(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.relative(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final int getConcurrency() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getConcurrency();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final boolean rowUpdated() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.rowUpdated();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final boolean rowInserted() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.rowInserted();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final boolean rowDeleted() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.rowDeleted();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateNull(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateNull(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateNull(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateNull(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateBoolean(int a, boolean b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateBoolean(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateBoolean(String a, boolean b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateBoolean(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateByte(String a, byte b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateByte(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateByte(int a, byte b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateByte(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateShort(String a, short b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateShort(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateShort(int a, short b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateShort(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateInt(String a, int b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateInt(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateInt(int a, int b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateInt(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateLong(int a, long b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateLong(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateLong(String a, long b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateLong(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateFloat(int a, float b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateFloat(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateFloat(String a, float b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateFloat(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateDouble(String a, double b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateDouble(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateDouble(int a, double b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateDouble(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateBigDecimal(String a, BigDecimal b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateBigDecimal(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateBigDecimal(int a, BigDecimal b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateBigDecimal(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateString(int a, String b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateString(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateString(String a, String b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateString(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateBytes(int a, byte[] b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateBytes(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateBytes(String a, byte[] b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateBytes(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateDate(String a, Date b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateDate(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateDate(int a, Date b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateDate(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateTime(String a, Time b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateTime(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateTime(int a, Time b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateTime(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateTimestamp(String a, Timestamp b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateTimestamp(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateTimestamp(int a, Timestamp b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateTimestamp(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateAsciiStream(String a, InputStream b, long c) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateAsciiStream(a, b, c);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateAsciiStream(int a, InputStream b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateAsciiStream(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateAsciiStream(String a, InputStream b, int c) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateAsciiStream(a, b, c);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateAsciiStream(int a, InputStream b, long c) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateAsciiStream(a, b, c);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateAsciiStream(int a, InputStream b, int c) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateAsciiStream(a, b, c);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateAsciiStream(String a, InputStream b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateAsciiStream(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateBinaryStream(String a, InputStream b, int c) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateBinaryStream(a, b, c);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateBinaryStream(int a, InputStream b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateBinaryStream(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateBinaryStream(String a, InputStream b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateBinaryStream(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateBinaryStream(int a, InputStream b, int c) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateBinaryStream(a, b, c);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateBinaryStream(int a, InputStream b, long c) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateBinaryStream(a, b, c);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateBinaryStream(String a, InputStream b, long c) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateBinaryStream(a, b, c);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateCharacterStream(String a, Reader b, long c) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateCharacterStream(a, b, c);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateCharacterStream(int a, Reader b, long c) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateCharacterStream(a, b, c);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateCharacterStream(int a, Reader b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateCharacterStream(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateCharacterStream(String a, Reader b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateCharacterStream(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateCharacterStream(String a, Reader b, int c) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateCharacterStream(a, b, c);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateCharacterStream(int a, Reader b, int c) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateCharacterStream(a, b, c);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateObject(int a, Object b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateObject(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateObject(int a, Object b, int c) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateObject(a, b, c);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateObject(String a, Object b, int c) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateObject(a, b, c);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateObject(String a, Object b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateObject(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void insertRow() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.insertRow();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateRow() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateRow();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void deleteRow() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.deleteRow();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void refreshRow() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.refreshRow();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void cancelRowUpdates() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.cancelRowUpdates();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void moveToInsertRow() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.moveToInsertRow();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void moveToCurrentRow() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.moveToCurrentRow();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateRef(String a, Ref b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateRef(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateRef(int a, Ref b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateRef(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateBlob(int a, Blob b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateBlob(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateBlob(String a, Blob b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateBlob(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateBlob(String a, InputStream b, long c) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateBlob(a, b, c);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateBlob(String a, InputStream b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateBlob(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateBlob(int a, InputStream b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateBlob(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateBlob(int a, InputStream b, long c) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateBlob(a, b, c);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateClob(int a, Reader b, long c) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateClob(a, b, c);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateClob(int a, Clob b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateClob(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateClob(String a, Reader b, long c) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateClob(a, b, c);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateClob(String a, Clob b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateClob(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateClob(int a, Reader b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateClob(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateClob(String a, Reader b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateClob(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateArray(String a, Array b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateArray(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateArray(int a, Array b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateArray(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateRowId(int a, RowId b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateRowId(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateRowId(String a, RowId b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateRowId(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateNString(String a, String b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateNString(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateNString(int a, String b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateNString(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateNClob(String a, NClob b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateNClob(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateNClob(int a, NClob b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateNClob(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateNClob(int a, Reader b, long c) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateNClob(a, b, c);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateNClob(int a, Reader b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateNClob(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateNClob(String a, Reader b, long c) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateNClob(a, b, c);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateNClob(String a, Reader b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateNClob(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateSQLXML(int a, SQLXML b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateSQLXML(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateSQLXML(String a, SQLXML b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateSQLXML(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateNCharacterStream(int a, Reader b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateNCharacterStream(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateNCharacterStream(String a, Reader b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateNCharacterStream(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateNCharacterStream(String a, Reader b, long c) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateNCharacterStream(a, b, c);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final void updateNCharacterStream(int a, Reader b, long c) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.updateNCharacterStream(a, b, c);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Timestamp getTimestamp(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getTimestamp(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Timestamp getTimestamp(String a, Calendar b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getTimestamp(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Timestamp getTimestamp(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getTimestamp(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Timestamp getTimestamp(int a, Calendar b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getTimestamp(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.setFetchDirection(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getFetchDirection();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

this.inner.setFetchSize(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getFetchSize();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final int getHoldability() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getHoldability();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final boolean wasNull() throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.wasNull();
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final BigDecimal getBigDecimal(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getBigDecimal(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final BigDecimal getBigDecimal(String a, int b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getBigDecimal(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final BigDecimal getBigDecimal(int a, int b) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getBigDecimal(a, b);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final BigDecimal getBigDecimal(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getBigDecimal(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Blob getBlob(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getBlob(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Blob getBlob(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getBlob(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Clob getClob(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getClob(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Clob getClob(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getClob(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final RowId getRowId(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getRowId(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final RowId getRowId(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getRowId(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final NClob getNClob(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getNClob(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final NClob getNClob(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getNClob(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final SQLXML getSQLXML(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getSQLXML(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final SQLXML getSQLXML(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getSQLXML(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final String getNString(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getNString(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final String getNString(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getNString(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Reader getNCharacterStream(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getNCharacterStream(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Reader getNCharacterStream(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getNCharacterStream(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Reader getCharacterStream(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getCharacterStream(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final Reader getCharacterStream(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getCharacterStream(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final InputStream getAsciiStream(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getAsciiStream(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final InputStream getAsciiStream(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getAsciiStream(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final InputStream getBinaryStream(int a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getBinaryStream(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final InputStream getBinaryStream(String a) throws SQLException {
try {
if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();

return this.inner.getBinaryStream(a);
}
catch (NullPointerException exc) {

if (isDetached())
{
throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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

public final boolean isWrapperFor(Class<ResultSet> a) throws SQLException {
return (ResultSet.class == a || a.isAssignableFrom(this.inner.getClass()));
}

private static final MLogger logger = MLog.getLogger("com.mchange.v2.c3p0.impl.NewProxyResultSet");

volatile NewPooledConnection parentPooledConnection;

ConnectionEventListener cel = new ConnectionEventListener()
{
public void connectionErrorOccurred(ConnectionEvent evt) {}

public void connectionClosed(ConnectionEvent evt) {
NewProxyResultSet.this.detach();
}
};
Object creator;
void attach(NewPooledConnection parentPooledConnection) {
this.parentPooledConnection = parentPooledConnection;
parentPooledConnection.addConnectionEventListener(this.cel);
}
Object creatorProxy; NewProxyConnection proxyConn;

private void detach() {
this.parentPooledConnection.removeConnectionEventListener(this.cel);
this.parentPooledConnection = null;
}

NewProxyResultSet(ResultSet inner, NewPooledConnection parentPooledConnection) {
this(inner);
attach(parentPooledConnection);
}

boolean isDetached() {
return (this.parentPooledConnection == null);
}

NewProxyResultSet(ResultSet inner, NewPooledConnection parentPooledConnection, Object c, Object cProxy) {
this(inner, parentPooledConnection);
this.creator = c;
this.creatorProxy = cProxy;
if (this.creatorProxy instanceof NewProxyConnection) this.proxyConn = (NewProxyConnection)cProxy; 
}
}

