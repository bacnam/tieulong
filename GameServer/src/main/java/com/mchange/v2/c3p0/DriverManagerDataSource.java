package com.mchange.v2.c3p0;

import com.mchange.v2.c3p0.cfg.C3P0Config;
import com.mchange.v2.c3p0.impl.DriverManagerDataSourceBase;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import com.mchange.v2.sql.SqlUtils;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;

public final class DriverManagerDataSource
extends DriverManagerDataSourceBase
implements DataSource
{
static final MLogger logger = MLog.getLogger(DriverManagerDataSource.class);

Driver driver;

static {
try {
Class.forName("java.sql.DriverManager");
} catch (Exception e) {

String msg = "Could not load the DriverManager class?!?";
if (logger.isLoggable(MLevel.SEVERE))
logger.log(MLevel.SEVERE, msg); 
throw new InternalError(msg);
} 
}

boolean driver_class_loaded = false;

private static final long serialVersionUID = 1L;
private static final short VERSION = 1;

public DriverManagerDataSource() {
this(true);
}

public DriverManagerDataSource(boolean autoregister) {
super(autoregister);

setUpPropertyListeners();

String user = C3P0Config.initializeStringPropertyVar("user", null);
String password = C3P0Config.initializeStringPropertyVar("password", null);

if (user != null) {
setUser(user);
}
if (password != null) {
setPassword(password);
}
}

private void setUpPropertyListeners() {
PropertyChangeListener driverClassListener = new PropertyChangeListener()
{
public void propertyChange(PropertyChangeEvent evt)
{
if ("driverClass".equals(evt.getPropertyName())) {

DriverManagerDataSource.this.setDriverClassLoaded(false);

if (DriverManagerDataSource.this.driverClass != null && DriverManagerDataSource.this.driverClass.trim().length() == 0)
DriverManagerDataSource.this.driverClass = null; 
} 
}
};
addPropertyChangeListener(driverClassListener);
}

private synchronized boolean isDriverClassLoaded() {
return this.driver_class_loaded;
}

private synchronized void setDriverClassLoaded(boolean dcl) {
this.driver_class_loaded = dcl;
if (!this.driver_class_loaded) clearDriver();

}

private synchronized void ensureDriverLoaded() throws SQLException {
try {
if (!isDriverClassLoaded())
{
if (this.driverClass != null)
Class.forName(this.driverClass); 
setDriverClassLoaded(true);
}

} catch (ClassNotFoundException e) {

if (logger.isLoggable(MLevel.WARNING)) {
logger.log(MLevel.WARNING, "Could not load driverClass " + this.driverClass, e);
}
} 
}

public Connection getConnection() throws SQLException {
ensureDriverLoaded();

Connection out = driver().connect(this.jdbcUrl, this.properties);
if (out == null) {
throw new SQLException("Apparently, jdbc URL '" + this.jdbcUrl + "' is not valid for the underlying " + "driver [" + driver() + "].");
}
return out;
}

public Connection getConnection(String username, String password) throws SQLException {
ensureDriverLoaded();

Connection out = driver().connect(this.jdbcUrl, overrideProps(username, password));
if (out == null) {
throw new SQLException("Apparently, jdbc URL '" + this.jdbcUrl + "' is not valid for the underlying " + "driver [" + driver() + "].");
}
return out;
}

public PrintWriter getLogWriter() throws SQLException {
return DriverManager.getLogWriter();
}
public void setLogWriter(PrintWriter out) throws SQLException {
DriverManager.setLogWriter(out);
}
public int getLoginTimeout() throws SQLException {
return DriverManager.getLoginTimeout();
}
public void setLoginTimeout(int seconds) throws SQLException {
DriverManager.setLoginTimeout(seconds);
}

public synchronized void setJdbcUrl(String jdbcUrl) {
super.setJdbcUrl(jdbcUrl);
clearDriver();
}

public synchronized void setUser(String user) {
String oldUser = getUser();
if (!eqOrBothNull(user, oldUser)) {

if (user != null) {
this.properties.put("user", user);
} else {
this.properties.remove("user");
} 
this.pcs.firePropertyChange("user", oldUser, user);
} 
}

public synchronized String getUser() {
return this.properties.getProperty("user");
}

public synchronized void setPassword(String password) {
String oldPass = getPassword();
if (!eqOrBothNull(password, oldPass)) {

if (password != null) {
this.properties.put("password", password);
} else {
this.properties.remove("password");
} 
this.pcs.firePropertyChange("password", oldPass, password);
} 
}

public synchronized String getPassword() {
return this.properties.getProperty("password");
}

private final Properties overrideProps(String user, String password) {
Properties overriding = (Properties)this.properties.clone();

if (user != null) {
overriding.put("user", user);
} else {
overriding.remove("user");
} 
if (password != null) {
overriding.put("password", password);
} else {
overriding.remove("password");
} 
return overriding;
}

private synchronized Driver driver() throws SQLException {
if (this.driver == null)
{
if (this.driverClass != null && this.forceUseNamedDriverClass) {

if (logger.isLoggable(MLevel.FINER)) {
logger.finer("Circumventing DriverManager and instantiating driver class '" + this.driverClass + "' directly. (forceUseNamedDriverClass = " + this.forceUseNamedDriverClass + ")");
}
try {
this.driver = (Driver)Class.forName(this.driverClass).newInstance();
} catch (Exception e) {
SqlUtils.toSQLException("Cannot instantiate specified JDBC driver. Exception while initializing named, forced-to-use driver class'" + this.driverClass + "'", e);
} 
} else {
this.driver = DriverManager.getDriver(this.jdbcUrl);
}  } 
return this.driver;
}

private synchronized void clearDriver() {
this.driver = null;
}
private static boolean eqOrBothNull(Object a, Object b) {
return (a == b || (a != null && a.equals(b)));
}

private void writeObject(ObjectOutputStream oos) throws IOException {
oos.writeShort(1);
}

private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
short version = ois.readShort();
switch (version) {

case 1:
setUpPropertyListeners();
return;
} 
throw new IOException("Unsupported Serialized Version: " + version);
}

public boolean isWrapperFor(Class<?> iface) throws SQLException {
return false;
}

public <T> T unwrap(Class<T> iface) throws SQLException {
throw new SQLException(this + " is not a Wrapper for " + iface.getName());
}
}

