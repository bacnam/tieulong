package com.mchange.v1.db.sql;

import com.mchange.v1.util.BrokenObjectException;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionBundlePoolBean
implements ConnectionBundlePool
{
ConnectionBundlePool inner;

public void init(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt1, int paramInt2, int paramInt3) throws SQLException, ClassNotFoundException {
Class.forName(paramString1);
init(paramString2, paramString3, paramString4, paramInt1, paramInt2, paramInt3);
}

public void init(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2, int paramInt3) throws SQLException {
this.inner = new InnerPool(paramString1, paramString2, paramString3, paramInt1, paramInt2, paramInt3);
}

public ConnectionBundle checkoutBundle() throws SQLException, InterruptedException, BrokenObjectException {
return this.inner.checkoutBundle();
}
public void checkinBundle(ConnectionBundle paramConnectionBundle) throws SQLException, BrokenObjectException {
this.inner.checkinBundle(paramConnectionBundle);
}
public void close() throws SQLException {
this.inner.close();
}
protected void setConnectionOptions(Connection paramConnection) throws SQLException {
paramConnection.setAutoCommit(false);
}

class InnerPool
extends ConnectionBundlePoolImpl
{
InnerPool(String param1String1, String param1String2, String param1String3, int param1Int1, int param1Int2, int param1Int3) throws SQLException {
super(param1Int1, param1Int2, param1Int3);

init(param1String1, param1String2, param1String3);
}

protected void setConnectionOptions(Connection param1Connection) throws SQLException {
ConnectionBundlePoolBean.this.setConnectionOptions(param1Connection);
}
}
}

