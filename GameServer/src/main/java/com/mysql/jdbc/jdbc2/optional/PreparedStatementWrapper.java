package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.ResultSetInternalMethods;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.Util;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class PreparedStatementWrapper
extends StatementWrapper
implements PreparedStatement
{
private static final Constructor<?> JDBC_4_PREPARED_STATEMENT_WRAPPER_CTOR;

static {
if (Util.isJdbc4()) {
try {
JDBC_4_PREPARED_STATEMENT_WRAPPER_CTOR = Class.forName("com.mysql.jdbc.jdbc2.optional.JDBC4PreparedStatementWrapper").getConstructor(new Class[] { ConnectionWrapper.class, MysqlPooledConnection.class, PreparedStatement.class });

}
catch (SecurityException e) {
throw new RuntimeException(e);
} catch (NoSuchMethodException e) {
throw new RuntimeException(e);
} catch (ClassNotFoundException e) {
throw new RuntimeException(e);
} 
} else {
JDBC_4_PREPARED_STATEMENT_WRAPPER_CTOR = null;
} 
}

protected static PreparedStatementWrapper getInstance(ConnectionWrapper c, MysqlPooledConnection conn, PreparedStatement toWrap) throws SQLException {
if (!Util.isJdbc4()) {
return new PreparedStatementWrapper(c, conn, toWrap);
}

return (PreparedStatementWrapper)Util.handleNewInstance(JDBC_4_PREPARED_STATEMENT_WRAPPER_CTOR, new Object[] { c, conn, toWrap }, conn.getExceptionInterceptor());
}

PreparedStatementWrapper(ConnectionWrapper c, MysqlPooledConnection conn, PreparedStatement toWrap) {
super(c, conn, toWrap);
}

public void setArray(int parameterIndex, Array x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setArray(parameterIndex, x);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
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

public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setBigDecimal(parameterIndex, x);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
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

public void setBlob(int parameterIndex, Blob x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setBlob(parameterIndex, x);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setBoolean(int parameterIndex, boolean x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setBoolean(parameterIndex, x);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setByte(int parameterIndex, byte x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setByte(parameterIndex, x);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setBytes(int parameterIndex, byte[] x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setBytes(parameterIndex, x);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
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

public void setClob(int parameterIndex, Clob x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setClob(parameterIndex, x);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setDate(int parameterIndex, Date x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setDate(parameterIndex, x);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setDate(parameterIndex, x, cal);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setDouble(int parameterIndex, double x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setDouble(parameterIndex, x);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setFloat(int parameterIndex, float x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setFloat(parameterIndex, x);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setInt(int parameterIndex, int x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setInt(parameterIndex, x);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setLong(int parameterIndex, long x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setLong(parameterIndex, x);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public ResultSetMetaData getMetaData() throws SQLException {
try {
if (this.wrappedStmt != null) {
return ((PreparedStatement)this.wrappedStmt).getMetaData();
}

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);

return null;
} 
}

public void setNull(int parameterIndex, int sqlType) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setNull(parameterIndex, sqlType);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setNull(parameterIndex, sqlType, typeName);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setObject(int parameterIndex, Object x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setObject(parameterIndex, x);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setObject(parameterIndex, x, targetSqlType);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setObject(parameterIndex, x, targetSqlType, scale);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public ParameterMetaData getParameterMetaData() throws SQLException {
try {
if (this.wrappedStmt != null) {
return ((PreparedStatement)this.wrappedStmt).getParameterMetaData();
}

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);

return null;
} 
}

public void setRef(int parameterIndex, Ref x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setRef(parameterIndex, x);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setShort(int parameterIndex, short x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setShort(parameterIndex, x);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setString(int parameterIndex, String x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setString(parameterIndex, x);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setTime(int parameterIndex, Time x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setTime(parameterIndex, x);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setTime(parameterIndex, x, cal);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setTimestamp(parameterIndex, x);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setTimestamp(parameterIndex, x, cal);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setURL(int parameterIndex, URL x) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setURL(parameterIndex, x);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).setUnicodeStream(parameterIndex, x, length);
} else {

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void addBatch() throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).addBatch();
} else {
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public void clearParameters() throws SQLException {
try {
if (this.wrappedStmt != null) {
((PreparedStatement)this.wrappedStmt).clearParameters();
} else {
throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
}

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);
} 
}

public boolean execute() throws SQLException {
try {
if (this.wrappedStmt != null) {
return ((PreparedStatement)this.wrappedStmt).execute();
}

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);

return false;
} 
}

public ResultSet executeQuery() throws SQLException {
try {
if (this.wrappedStmt != null) {
ResultSet rs = ((PreparedStatement)this.wrappedStmt).executeQuery();

((ResultSetInternalMethods)rs).setWrapperStatement(this);

return rs;
} 

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);

return null;
} 
}

public int executeUpdate() throws SQLException {
try {
if (this.wrappedStmt != null) {
return ((PreparedStatement)this.wrappedStmt).executeUpdate();
}

throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);

}
catch (SQLException sqlEx) {
checkAndFireConnectionError(sqlEx);

return -1;
} 
}
}

