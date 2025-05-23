package com.mchange.v1.db.sql;

import com.mchange.io.UnsupportedVersionException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.naming.spi.ObjectFactory;
import javax.sql.DataSource;

public class DriverManagerDataSource
implements DataSource, Serializable, Referenceable
{
static final String REF_FACTORY_NAME = DmdsObjectFactory.class.getName();
static final String REF_JDBC_URL = "jdbcUrl";
static final String REF_DFLT_USER = "dfltUser";
static final String REF_DFLT_PWD = "dfltPassword";
String jdbcUrl;
String dfltUser;
String dfltPassword;
static final long serialVersionUID = 1L;
private static final short VERSION = 1;

public DriverManagerDataSource(String paramString1, String paramString2, String paramString3) {
this.jdbcUrl = paramString1;
this.dfltUser = paramString2;
this.dfltPassword = paramString3;
}

public DriverManagerDataSource(String paramString) {
this(paramString, null, null);
}

public Connection getConnection() throws SQLException {
return DriverManager.getConnection(this.jdbcUrl, createProps(null, null));
}

public Connection getConnection(String paramString1, String paramString2) throws SQLException {
return DriverManager.getConnection(this.jdbcUrl, createProps(paramString1, paramString2));
}

public PrintWriter getLogWriter() throws SQLException {
return DriverManager.getLogWriter();
}
public void setLogWriter(PrintWriter paramPrintWriter) throws SQLException {
DriverManager.setLogWriter(paramPrintWriter);
}
public int getLoginTimeout() throws SQLException {
return DriverManager.getLoginTimeout();
}
public void setLoginTimeout(int paramInt) throws SQLException {
DriverManager.setLoginTimeout(paramInt);
}

public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
return false;
}
public <T> T unwrap(Class<T> paramClass) throws SQLException {
throw new SQLException(getClass().getName() + " is not a wrapper for an object implementing any interface.");
}

public Reference getReference() throws NamingException {
Reference reference = new Reference(getClass().getName(), REF_FACTORY_NAME, null);

reference.add(new StringRefAddr("jdbcUrl", this.jdbcUrl));
reference.add(new StringRefAddr("dfltUser", this.dfltUser));
reference.add(new StringRefAddr("dfltPassword", this.dfltPassword));

return reference;
}

public Logger getParentLogger() throws SQLFeatureNotSupportedException {
throw new SQLFeatureNotSupportedException("javax.sql.DataSource.getParentLogger() is not currently supported by " + getClass().getName());
}

private Properties createProps(String paramString1, String paramString2) {
Properties properties = new Properties();
if (paramString1 != null) {

properties.put("user", paramString1);
properties.put("password", paramString2);
}
else if (this.dfltUser != null) {

properties.put("user", this.dfltUser);
properties.put("password", this.dfltPassword);
} 
return properties;
}

private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
paramObjectOutputStream.writeShort(1);
paramObjectOutputStream.writeUTF(this.jdbcUrl);
paramObjectOutputStream.writeUTF(this.dfltUser);
paramObjectOutputStream.writeUTF(this.dfltPassword);
}

private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
short s = paramObjectInputStream.readShort();
switch (s) {

case 1:
this.jdbcUrl = paramObjectInputStream.readUTF();
this.dfltUser = paramObjectInputStream.readUTF();
this.dfltPassword = paramObjectInputStream.readUTF();
return;
} 
throw new UnsupportedVersionException(this, s);
}

public static class DmdsObjectFactory
implements ObjectFactory
{
public Object getObjectInstance(Object param1Object, Name param1Name, Context param1Context, Hashtable param1Hashtable) throws Exception {
String str = DriverManagerDataSource.class.getName(); Reference reference;
if (param1Object instanceof Reference && (reference = (Reference)param1Object).getClassName().equals(str))
{

return new DriverManagerDataSource((String)reference.get("jdbcUrl").getContent(), (String)reference.get("dfltUser").getContent(), (String)reference.get("dfltPassword").getContent());
}

return null;
}
}
}

