package com.mchange.v2.c3p0.mbean;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import java.beans.PropertyVetoException;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;

public class C3P0PooledDataSource
implements C3P0PooledDataSourceMBean
{
private static final MLogger logger = MLog.getLogger(C3P0PooledDataSource.class);

String jndiName;

ComboPooledDataSource combods = new ComboPooledDataSource();

private void rebind() throws NamingException {
rebind(null);
}

private void rebind(String unbindName) throws NamingException {
InitialContext ictx = new InitialContext();
if (unbindName != null) {
ictx.unbind(unbindName);
}
if (this.jndiName != null) {

Name name = ictx.getNameParser(this.jndiName).parse(this.jndiName);
Context ctx = ictx;
for (int i = 0, max = name.size() - 1; i < max; i++) {

try {
ctx = ctx.createSubcontext(name.get(i));
} catch (NameAlreadyBoundException ignore) {
ctx = (Context)ctx.lookup(name.get(i));
} 
} 
ictx.rebind(this.jndiName, this.combods);
} 
}

public void setJndiName(String jndiName) throws NamingException {
String unbindName = this.jndiName;
this.jndiName = jndiName;
rebind(unbindName);
}

public String getJndiName() {
return this.jndiName;
}

public String getDescription() {
return this.combods.getDescription();
}

public void setDescription(String description) throws NamingException {
this.combods.setDescription(description);
rebind();
}

public String getDriverClass() {
return this.combods.getDriverClass();
}

public void setDriverClass(String driverClass) throws PropertyVetoException, NamingException {
this.combods.setDriverClass(driverClass);
rebind();
}

public String getJdbcUrl() {
return this.combods.getJdbcUrl();
}

public void setJdbcUrl(String jdbcUrl) throws NamingException {
this.combods.setJdbcUrl(jdbcUrl);
rebind();
}

public String getUser() {
return this.combods.getUser();
}

public void setUser(String user) throws NamingException {
this.combods.setUser(user);
rebind();
}

public String getPassword() {
return this.combods.getPassword();
}

public void setPassword(String password) throws NamingException {
this.combods.setPassword(password);
rebind();
}

public int getCheckoutTimeout() {
return this.combods.getCheckoutTimeout();
}

public void setCheckoutTimeout(int checkoutTimeout) throws NamingException {
this.combods.setCheckoutTimeout(checkoutTimeout);
rebind();
}

public int getAcquireIncrement() {
return this.combods.getAcquireIncrement();
}

public void setAcquireIncrement(int acquireIncrement) throws NamingException {
this.combods.setAcquireIncrement(acquireIncrement);
rebind();
}

public int getAcquireRetryAttempts() {
return this.combods.getAcquireRetryAttempts();
}

public void setAcquireRetryAttempts(int acquireRetryAttempts) throws NamingException {
this.combods.setAcquireRetryAttempts(acquireRetryAttempts);
rebind();
}

public int getAcquireRetryDelay() {
return this.combods.getAcquireRetryDelay();
}

public void setAcquireRetryDelay(int acquireRetryDelay) throws NamingException {
this.combods.setAcquireRetryDelay(acquireRetryDelay);
rebind();
}

public boolean isAutoCommitOnClose() {
return this.combods.isAutoCommitOnClose();
}

public void setAutoCommitOnClose(boolean autoCommitOnClose) throws NamingException {
this.combods.setAutoCommitOnClose(autoCommitOnClose);
rebind();
}

public String getConnectionTesterClassName() {
return this.combods.getConnectionTesterClassName();
}

public void setConnectionTesterClassName(String connectionTesterClassName) throws PropertyVetoException, NamingException {
this.combods.setConnectionTesterClassName(connectionTesterClassName);
rebind();
}

public String getAutomaticTestTable() {
return this.combods.getAutomaticTestTable();
}

public void setAutomaticTestTable(String automaticTestTable) throws NamingException {
this.combods.setAutomaticTestTable(automaticTestTable);
rebind();
}

public boolean isForceIgnoreUnresolvedTransactions() {
return this.combods.isForceIgnoreUnresolvedTransactions();
}

public void setForceIgnoreUnresolvedTransactions(boolean forceIgnoreUnresolvedTransactions) throws NamingException {
this.combods.setForceIgnoreUnresolvedTransactions(forceIgnoreUnresolvedTransactions);
rebind();
}

public int getIdleConnectionTestPeriod() {
return this.combods.getIdleConnectionTestPeriod();
}

public void setIdleConnectionTestPeriod(int idleConnectionTestPeriod) throws NamingException {
this.combods.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
rebind();
}

public int getInitialPoolSize() {
return this.combods.getInitialPoolSize();
}

public void setInitialPoolSize(int initialPoolSize) throws NamingException {
this.combods.setInitialPoolSize(initialPoolSize);
rebind();
}

public int getMaxIdleTime() {
return this.combods.getMaxIdleTime();
}

public void setMaxIdleTime(int maxIdleTime) throws NamingException {
this.combods.setMaxIdleTime(maxIdleTime);
rebind();
}

public int getMaxPoolSize() {
return this.combods.getMaxPoolSize();
}

public void setMaxPoolSize(int maxPoolSize) throws NamingException {
this.combods.setMaxPoolSize(maxPoolSize);
rebind();
}

public int getMaxStatements() {
return this.combods.getMaxStatements();
}

public void setMaxStatements(int maxStatements) throws NamingException {
this.combods.setMaxStatements(maxStatements);
rebind();
}

public int getMaxStatementsPerConnection() {
return this.combods.getMaxStatementsPerConnection();
}

public void setMaxStatementsPerConnection(int maxStatementsPerConnection) throws NamingException {
this.combods.setMaxStatementsPerConnection(maxStatementsPerConnection);
rebind();
}

public int getMinPoolSize() {
return this.combods.getMinPoolSize();
}

public void setMinPoolSize(int minPoolSize) throws NamingException {
this.combods.setMinPoolSize(minPoolSize);
rebind();
}

public int getPropertyCycle() {
return this.combods.getPropertyCycle();
}

public void setPropertyCycle(int propertyCycle) throws NamingException {
this.combods.setPropertyCycle(propertyCycle);
rebind();
}

public boolean isBreakAfterAcquireFailure() {
return this.combods.isBreakAfterAcquireFailure();
}

public void setBreakAfterAcquireFailure(boolean breakAfterAcquireFailure) throws NamingException {
this.combods.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
rebind();
}

public boolean isTestConnectionOnCheckout() {
return this.combods.isTestConnectionOnCheckout();
}

public void setTestConnectionOnCheckout(boolean testConnectionOnCheckout) throws NamingException {
this.combods.setTestConnectionOnCheckout(testConnectionOnCheckout);
rebind();
}

public boolean isTestConnectionOnCheckin() {
return this.combods.isTestConnectionOnCheckin();
}

public void setTestConnectionOnCheckin(boolean testConnectionOnCheckin) throws NamingException {
this.combods.setTestConnectionOnCheckin(testConnectionOnCheckin);
rebind();
}

public boolean isUsesTraditionalReflectiveProxies() {
return this.combods.isUsesTraditionalReflectiveProxies();
}

public void setUsesTraditionalReflectiveProxies(boolean usesTraditionalReflectiveProxies) throws NamingException {
this.combods.setUsesTraditionalReflectiveProxies(usesTraditionalReflectiveProxies);
rebind();
}

public String getPreferredTestQuery() {
return this.combods.getPreferredTestQuery();
}

public void setPreferredTestQuery(String preferredTestQuery) throws NamingException {
this.combods.setPreferredTestQuery(preferredTestQuery);
rebind();
}

public String getDataSourceName() {
return this.combods.getDataSourceName();
}

public void setDataSourceName(String name) throws NamingException {
this.combods.setDataSourceName(name);
rebind();
}

public int getNumHelperThreads() {
return this.combods.getNumHelperThreads();
}

public void setNumHelperThreads(int numHelperThreads) throws NamingException {
this.combods.setNumHelperThreads(numHelperThreads);
rebind();
}

public String getFactoryClassLocation() {
return this.combods.getFactoryClassLocation();
}

public void setFactoryClassLocation(String factoryClassLocation) throws NamingException {
this.combods.setFactoryClassLocation(factoryClassLocation);
rebind();
}

public int getNumUserPools() throws SQLException {
return this.combods.getNumUserPools();
}
public int getNumConnectionsDefaultUser() throws SQLException {
return this.combods.getNumConnectionsDefaultUser();
}
public int getNumIdleConnectionsDefaultUser() throws SQLException {
return this.combods.getNumIdleConnectionsDefaultUser();
}
public int getNumBusyConnectionsDefaultUser() throws SQLException {
return this.combods.getNumBusyConnectionsDefaultUser();
}
public int getNumUnclosedOrphanedConnectionsDefaultUser() throws SQLException {
return this.combods.getNumUnclosedOrphanedConnectionsDefaultUser();
}
public int getNumConnections(String username, String password) throws SQLException {
return this.combods.getNumConnections(username, password);
}
public int getNumIdleConnections(String username, String password) throws SQLException {
return this.combods.getNumIdleConnections(username, password);
}
public int getNumBusyConnections(String username, String password) throws SQLException {
return this.combods.getNumBusyConnections(username, password);
}
public int getNumUnclosedOrphanedConnections(String username, String password) throws SQLException {
return this.combods.getNumUnclosedOrphanedConnections(username, password);
}
public int getNumConnectionsAllUsers() throws SQLException {
return this.combods.getNumConnectionsAllUsers();
}
public int getNumIdleConnectionsAllUsers() throws SQLException {
return this.combods.getNumIdleConnectionsAllUsers();
}
public int getNumBusyConnectionsAllUsers() throws SQLException {
return this.combods.getNumBusyConnectionsAllUsers();
}
public int getNumUnclosedOrphanedConnectionsAllUsers() throws SQLException {
return this.combods.getNumUnclosedOrphanedConnectionsAllUsers();
}

public void softResetDefaultUser() throws SQLException {
this.combods.softResetDefaultUser();
}
public void softReset(String username, String password) throws SQLException {
this.combods.softReset(username, password);
}
public void softResetAllUsers() throws SQLException {
this.combods.softResetAllUsers();
}
public void hardReset() throws SQLException {
this.combods.hardReset();
}
public void close() throws SQLException {
this.combods.close();
}

public void create() throws Exception {}

public void start() throws Exception {
logger.log(MLevel.INFO, "Bound C3P0 PooledDataSource to name ''{0}''. Starting...", this.jndiName);
this.combods.getNumBusyConnectionsDefaultUser();
}

public void stop() {}

public void destroy() {}
}

