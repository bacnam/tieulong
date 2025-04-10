package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ExceptionInterceptor;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.Util;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;

public class MysqlPooledConnection
implements PooledConnection
{
private static final Constructor<?> JDBC_4_POOLED_CONNECTION_WRAPPER_CTOR;
public static final int CONNECTION_ERROR_EVENT = 1;
public static final int CONNECTION_CLOSED_EVENT = 2;
private Map<ConnectionEventListener, ConnectionEventListener> connectionEventListeners;
private Connection logicalHandle;
private Connection physicalConn;
private ExceptionInterceptor exceptionInterceptor;

static {
if (Util.isJdbc4()) {
try {
JDBC_4_POOLED_CONNECTION_WRAPPER_CTOR = Class.forName("com.mysql.jdbc.jdbc2.optional.JDBC4MysqlPooledConnection").getConstructor(new Class[] { Connection.class });

}
catch (SecurityException e) {
throw new RuntimeException(e);
} catch (NoSuchMethodException e) {
throw new RuntimeException(e);
} catch (ClassNotFoundException e) {
throw new RuntimeException(e);
} 
} else {
JDBC_4_POOLED_CONNECTION_WRAPPER_CTOR = null;
} 
}

protected static MysqlPooledConnection getInstance(Connection connection) throws SQLException {
if (!Util.isJdbc4()) {
return new MysqlPooledConnection(connection);
}

return (MysqlPooledConnection)Util.handleNewInstance(JDBC_4_POOLED_CONNECTION_WRAPPER_CTOR, new Object[] { connection }, connection.getExceptionInterceptor());
}

public MysqlPooledConnection(Connection connection) {
this.logicalHandle = null;
this.physicalConn = connection;
this.connectionEventListeners = new HashMap<ConnectionEventListener, ConnectionEventListener>();
this.exceptionInterceptor = this.physicalConn.getExceptionInterceptor();
}

public synchronized void addConnectionEventListener(ConnectionEventListener connectioneventlistener) {
if (this.connectionEventListeners != null) {
this.connectionEventListeners.put(connectioneventlistener, connectioneventlistener);
}
}

public synchronized void removeConnectionEventListener(ConnectionEventListener connectioneventlistener) {
if (this.connectionEventListeners != null) {
this.connectionEventListeners.remove(connectioneventlistener);
}
}

public synchronized Connection getConnection() throws SQLException {
return getConnection(true, false);
}

protected synchronized Connection getConnection(boolean resetServerState, boolean forXa) throws SQLException {
if (this.physicalConn == null) {

SQLException sqlException = SQLError.createSQLException("Physical Connection doesn't exist", this.exceptionInterceptor);

callConnectionEventListeners(1, sqlException);

throw sqlException;
} 

try {
if (this.logicalHandle != null) {
((ConnectionWrapper)this.logicalHandle).close(false);
}

if (resetServerState) {
this.physicalConn.resetServerState();
}

this.logicalHandle = (Connection)ConnectionWrapper.getInstance(this, this.physicalConn, forXa);

}
catch (SQLException sqlException) {
callConnectionEventListeners(1, sqlException);

throw sqlException;
} 

return this.logicalHandle;
}

public synchronized void close() throws SQLException {
if (this.physicalConn != null) {
this.physicalConn.close();

this.physicalConn = null;
} 

if (this.connectionEventListeners != null) {
this.connectionEventListeners.clear();

this.connectionEventListeners = null;
} 
}

protected synchronized void callConnectionEventListeners(int eventType, SQLException sqlException) {
if (this.connectionEventListeners == null) {
return;
}

Iterator<Map.Entry<ConnectionEventListener, ConnectionEventListener>> iterator = this.connectionEventListeners.entrySet().iterator();

ConnectionEvent connectionevent = new ConnectionEvent(this, sqlException);

while (iterator.hasNext()) {

ConnectionEventListener connectioneventlistener = (ConnectionEventListener)((Map.Entry)iterator.next()).getValue();

if (eventType == 2) {
connectioneventlistener.connectionClosed(connectionevent); continue;
}  if (eventType == 1) {
connectioneventlistener.connectionErrorOccurred(connectionevent);
}
} 
}

protected ExceptionInterceptor getExceptionInterceptor() {
return this.exceptionInterceptor;
}
}

