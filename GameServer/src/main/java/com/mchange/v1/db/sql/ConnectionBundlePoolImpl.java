package com.mchange.v1.db.sql;

import com.mchange.v1.util.AbstractResourcePool;
import com.mchange.v1.util.BrokenObjectException;
import com.mchange.v1.util.UnexpectedException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class ConnectionBundlePoolImpl
extends AbstractResourcePool
implements ConnectionBundlePool
{
String jdbcUrl;
String username;
String pwd;

public ConnectionBundlePoolImpl(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2, int paramInt3) throws SQLException {
super(paramInt1, paramInt2, paramInt3);
init(paramString1, paramString2, paramString3);
}

protected ConnectionBundlePoolImpl(int paramInt1, int paramInt2, int paramInt3) {
super(paramInt1, paramInt2, paramInt3);
}

protected void init(String paramString1, String paramString2, String paramString3) throws SQLException {
this.jdbcUrl = paramString1;
this.username = paramString2;
this.pwd = paramString3; try {
init();
} catch (SQLException sQLException) {
throw sQLException;
} catch (Exception exception) {
throw new UnexpectedException(exception, "Unexpected exception while initializing ConnectionBundlePool");
} 
}

public ConnectionBundle checkoutBundle() throws SQLException, BrokenObjectException, InterruptedException {
try {
return (ConnectionBundle)checkoutResource();
} catch (BrokenObjectException brokenObjectException) {
throw brokenObjectException;
} catch (InterruptedException interruptedException) {
throw interruptedException;
} catch (SQLException sQLException) {
throw sQLException;
} catch (Exception exception) {
throw new UnexpectedException(exception, "Unexpected exception while checking out ConnectionBundle");
} 
}
public void checkinBundle(ConnectionBundle paramConnectionBundle) throws BrokenObjectException {
checkinResource(paramConnectionBundle);
}

public void close() throws SQLException {
try {
super.close();
} catch (SQLException sQLException) {
throw sQLException;
} catch (Exception exception) {
throw new UnexpectedException(exception, "Unexpected exception while closing pool.");
} 
}

protected Object acquireResource() throws Exception {
Connection connection = DriverManager.getConnection(this.jdbcUrl, this.username, this.pwd);
setConnectionOptions(connection);
return new ConnectionBundleImpl(connection);
}

protected void refurbishResource(Object paramObject) throws BrokenObjectException {
boolean bool;
try {
Connection connection = ((ConnectionBundle)paramObject).getConnection();
connection.rollback();
bool = connection.isClosed();
setConnectionOptions(connection);
}
catch (SQLException sQLException) {
bool = true;
}  if (bool) throw new BrokenObjectException(paramObject);

}

protected void destroyResource(Object paramObject) throws Exception {
((ConnectionBundle)paramObject).close();
}

protected abstract void setConnectionOptions(Connection paramConnection) throws SQLException;
}

