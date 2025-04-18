package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.SQLError;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Proxy;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.util.HashMap;

public class JDBC4CallableStatementWrapper
extends CallableStatementWrapper
{
public JDBC4CallableStatementWrapper(ConnectionWrapper c, MysqlPooledConnection conn, CallableStatement toWrap) {
super(c, conn, toWrap);
}

public void close() throws SQLException {
try {
super.close();
} finally {
this.unwrappedInterfaces = null;
} 
}

public boolean isClosed() throws SQLException {
try {
if (this.wrappedStmt != null) {
return this.wrappedStmt.isClosed();
}
throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);

return false;
} 
}
public void setPoolable(boolean poolable) throws SQLException {
try {
if (this.wrappedStmt != null) {
this.wrappedStmt.setPoolable(poolable);
} else {
throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
}

} catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public boolean isPoolable() throws SQLException {
try {
if (this.wrappedStmt != null) {
return this.wrappedStmt.isPoolable();
}
throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);

return false;
} 
}
public void setRowId(int parameterIndex, RowId x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setRowId(parameterIndex, x);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setNClob(int parameterIndex, NClob value) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setNClob(parameterIndex, value);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setSQLXML(parameterIndex, xmlObject);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setNString(int parameterIndex, String value) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setNString(parameterIndex, value);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setNCharacterStream(parameterIndex, value, length);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setClob(parameterIndex, reader, length);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setBlob(parameterIndex, inputStream, length);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setNClob(parameterIndex, reader, length);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setAsciiStream(parameterIndex, x, length);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setBinaryStream(parameterIndex, x, length);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setCharacterStream(parameterIndex, reader, length);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setAsciiStream(parameterIndex, x);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setBinaryStream(parameterIndex, x);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setCharacterStream(parameterIndex, reader);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setNCharacterStream(parameterIndex, value);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setClob(int parameterIndex, Reader reader) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setClob(parameterIndex, reader);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setBlob(parameterIndex, inputStream);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setNClob(int parameterIndex, Reader reader) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setNClob(parameterIndex, reader);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public boolean isWrapperFor(Class<?> iface) throws SQLException {
boolean isInstance = iface.isInstance(this);

if (isInstance) {
return true;
}

String interfaceClassName = iface.getName();

return (interfaceClassName.equals("com.mysql.jdbc.Statement") || interfaceClassName.equals("java.sql.Statement") || interfaceClassName.equals("java.sql.PreparedStatement") || interfaceClassName.equals("java.sql.Wrapper"));
}

public synchronized <T> T unwrap(Class<T> iface) throws SQLException {
try {
if ("java.sql.Statement".equals(iface.getName()) || "java.sql.PreparedStatement".equals(iface.getName()) || "java.sql.Wrapper.class".equals(iface.getName()))
{

return iface.cast(this);
}

if (this.unwrappedInterfaces == null) {
this.unwrappedInterfaces = new HashMap<Object, Object>();
}

Object cachedUnwrapped = this.unwrappedInterfaces.get(iface);

if (cachedUnwrapped == null) {
if (cachedUnwrapped == null) {
cachedUnwrapped = Proxy.newProxyInstance(this.wrappedStmt.getClass().getClassLoader(), new Class[] { iface }, new WrapperBase.ConnectionErrorFiringInvocationHandler(this, this.wrappedStmt));

this.unwrappedInterfaces.put(iface, cachedUnwrapped);
} 
this.unwrappedInterfaces.put(iface, cachedUnwrapped);
} 

return iface.cast(cachedUnwrapped);
} catch (ClassCastException cce) {
throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), "S1009", this.exceptionInterceptor);
} 
}

public void setRowId(String parameterName, RowId x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((CallableStatement)this.wrappedStmt).setRowId(parameterName, x);
} else {
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
try {
if (this.wrappedStmt != null) {
((CallableStatement)this.wrappedStmt).setSQLXML(parameterName, xmlObject);
} else {
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public SQLXML getSQLXML(int parameterIndex) throws SQLException {
try {
if (this.wrappedStmt != null) {
return ((CallableStatement)this.wrappedStmt).getSQLXML(parameterIndex);
}
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);

return null;
} 
}

public SQLXML getSQLXML(String parameterName) throws SQLException {
try {
if (this.wrappedStmt != null) {
return ((CallableStatement)this.wrappedStmt).getSQLXML(parameterName);
}
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);

return null;
} 
}
public RowId getRowId(String parameterName) throws SQLException {
try {
if (this.wrappedStmt != null) {
return ((CallableStatement)this.wrappedStmt).getRowId(parameterName);
}
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);

return null;
} 
}
public void setNClob(String parameterName, NClob value) throws SQLException {
try {
if (this.wrappedStmt != null) {
((CallableStatement)this.wrappedStmt).setNClob(parameterName, value);
} else {
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setNClob(String parameterName, Reader reader) throws SQLException {
try {
if (this.wrappedStmt != null) {
((CallableStatement)this.wrappedStmt).setNClob(parameterName, reader);
} else {
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
try {
if (this.wrappedStmt != null) {
((CallableStatement)this.wrappedStmt).setNClob(parameterName, reader, length);
} else {
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setNString(String parameterName, String value) throws SQLException {
try {
if (this.wrappedStmt != null) {
((CallableStatement)this.wrappedStmt).setNString(parameterName, value);
} else {
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public Reader getCharacterStream(int parameterIndex) throws SQLException {
try {
if (this.wrappedStmt != null) {
return ((CallableStatement)this.wrappedStmt).getCharacterStream(parameterIndex);
}
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);

return null;
} 
}

public Reader getCharacterStream(String parameterName) throws SQLException {
try {
if (this.wrappedStmt != null) {
return ((CallableStatement)this.wrappedStmt).getCharacterStream(parameterName);
}
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);

return null;
} 
}

public Reader getNCharacterStream(int parameterIndex) throws SQLException {
try {
if (this.wrappedStmt != null) {
return ((CallableStatement)this.wrappedStmt).getNCharacterStream(parameterIndex);
}
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);

return null;
} 
}

public Reader getNCharacterStream(String parameterName) throws SQLException {
try {
if (this.wrappedStmt != null) {
return ((CallableStatement)this.wrappedStmt).getNCharacterStream(parameterName);
}
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);

return null;
} 
}

public NClob getNClob(String parameterName) throws SQLException {
try {
if (this.wrappedStmt != null) {
return ((CallableStatement)this.wrappedStmt).getNClob(parameterName);
}
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);

return null;
} 
}

public String getNString(String parameterName) throws SQLException {
try {
if (this.wrappedStmt != null) {
return ((CallableStatement)this.wrappedStmt).getNString(parameterName);
}
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);

return null;
} 
}
public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((CallableStatement)this.wrappedStmt).setAsciiStream(parameterName, x);
} else {
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
try {
if (this.wrappedStmt != null) {
((CallableStatement)this.wrappedStmt).setAsciiStream(parameterName, x, length);
} else {
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((CallableStatement)this.wrappedStmt).setBinaryStream(parameterName, x);
} else {
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
try {
if (this.wrappedStmt != null) {
((CallableStatement)this.wrappedStmt).setBinaryStream(parameterName, x, length);
} else {
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setBlob(String parameterName, InputStream x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((CallableStatement)this.wrappedStmt).setBlob(parameterName, x);
} else {
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setBlob(String parameterName, InputStream x, long length) throws SQLException {
try {
if (this.wrappedStmt != null) {
((CallableStatement)this.wrappedStmt).setBlob(parameterName, x, length);
} else {
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setBlob(String parameterName, Blob x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((CallableStatement)this.wrappedStmt).setBlob(parameterName, x);
} else {
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
try {
if (this.wrappedStmt != null) {
((CallableStatement)this.wrappedStmt).setCharacterStream(parameterName, reader);
} else {
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
try {
if (this.wrappedStmt != null) {
((CallableStatement)this.wrappedStmt).setCharacterStream(parameterName, reader, length);
} else {
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setClob(String parameterName, Clob x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((CallableStatement)this.wrappedStmt).setClob(parameterName, x);
} else {
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setClob(String parameterName, Reader reader) throws SQLException {
try {
if (this.wrappedStmt != null) {
((CallableStatement)this.wrappedStmt).setClob(parameterName, reader);
} else {
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setClob(String parameterName, Reader reader, long length) throws SQLException {
try {
if (this.wrappedStmt != null) {
((CallableStatement)this.wrappedStmt).setClob(parameterName, reader, length);
} else {
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setNCharacterStream(String parameterName, Reader reader) throws SQLException {
try {
if (this.wrappedStmt != null) {
((CallableStatement)this.wrappedStmt).setNCharacterStream(parameterName, reader);
} else {
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setNCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
try {
if (this.wrappedStmt != null) {
((CallableStatement)this.wrappedStmt).setNCharacterStream(parameterName, reader, length);
} else {
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public NClob getNClob(int parameterIndex) throws SQLException {
try {
if (this.wrappedStmt != null) {
return ((CallableStatement)this.wrappedStmt).getNClob(parameterIndex);
}
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);

return null;
} 
}
public String getNString(int parameterIndex) throws SQLException {
try {
if (this.wrappedStmt != null) {
return ((CallableStatement)this.wrappedStmt).getNString(parameterIndex);
}
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);

return null;
} 
}
public RowId getRowId(int parameterIndex) throws SQLException {
try {
if (this.wrappedStmt != null) {
return ((CallableStatement)this.wrappedStmt).getRowId(parameterIndex);
}
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);

return null;
} 
}
}

