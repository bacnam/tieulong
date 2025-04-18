package com.mchange.v2.c3p0.impl;

import com.mchange.v2.c3p0.C3P0Registry;
import com.mchange.v2.c3p0.ConnectionCustomizer;
import com.mchange.v2.c3p0.cfg.C3P0Config;
import com.mchange.v2.naming.JavaBeanReferenceMaker;
import com.mchange.v2.naming.ReferenceIndirector;
import com.mchange.v2.ser.IndirectlySerialized;
import com.mchange.v2.ser.SerializableUtils;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.sql.DataSource;
import javax.sql.PooledConnection;

public abstract class WrapperConnectionPoolDataSourceBase
extends IdentityTokenResolvable
implements Referenceable, Serializable
{
protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

protected PropertyChangeSupport getPropertyChangeSupport() {
return this.pcs;
} protected VetoableChangeSupport vcs = new VetoableChangeSupport(this);

protected VetoableChangeSupport getVetoableChangeSupport() {
return this.vcs;
} private int acquireIncrement = C3P0Config.initializeIntPropertyVar("acquireIncrement", C3P0Defaults.acquireIncrement());
private int acquireRetryAttempts = C3P0Config.initializeIntPropertyVar("acquireRetryAttempts", C3P0Defaults.acquireRetryAttempts());
private int acquireRetryDelay = C3P0Config.initializeIntPropertyVar("acquireRetryDelay", C3P0Defaults.acquireRetryDelay());
private boolean autoCommitOnClose = C3P0Config.initializeBooleanPropertyVar("autoCommitOnClose", C3P0Defaults.autoCommitOnClose());
private String automaticTestTable = C3P0Config.initializeStringPropertyVar("automaticTestTable", C3P0Defaults.automaticTestTable());
private boolean breakAfterAcquireFailure = C3P0Config.initializeBooleanPropertyVar("breakAfterAcquireFailure", C3P0Defaults.breakAfterAcquireFailure());
private int checkoutTimeout = C3P0Config.initializeIntPropertyVar("checkoutTimeout", C3P0Defaults.checkoutTimeout());
private String connectionCustomizerClassName = C3P0Config.initializeStringPropertyVar("connectionCustomizerClassName", C3P0Defaults.connectionCustomizerClassName());
private String connectionTesterClassName = C3P0Config.initializeStringPropertyVar("connectionTesterClassName", C3P0Defaults.connectionTesterClassName());
private String contextClassLoaderSource = C3P0Config.initializeStringPropertyVar("contextClassLoaderSource", C3P0Defaults.contextClassLoaderSource());
private boolean debugUnreturnedConnectionStackTraces = C3P0Config.initializeBooleanPropertyVar("debugUnreturnedConnectionStackTraces", C3P0Defaults.debugUnreturnedConnectionStackTraces());
private String factoryClassLocation = C3P0Config.initializeStringPropertyVar("factoryClassLocation", C3P0Defaults.factoryClassLocation());
private boolean forceIgnoreUnresolvedTransactions = C3P0Config.initializeBooleanPropertyVar("forceIgnoreUnresolvedTransactions", C3P0Defaults.forceIgnoreUnresolvedTransactions());
private volatile String identityToken;
private int idleConnectionTestPeriod = C3P0Config.initializeIntPropertyVar("idleConnectionTestPeriod", C3P0Defaults.idleConnectionTestPeriod());
private int initialPoolSize = C3P0Config.initializeIntPropertyVar("initialPoolSize", C3P0Defaults.initialPoolSize());
private int maxAdministrativeTaskTime = C3P0Config.initializeIntPropertyVar("maxAdministrativeTaskTime", C3P0Defaults.maxAdministrativeTaskTime());
private int maxConnectionAge = C3P0Config.initializeIntPropertyVar("maxConnectionAge", C3P0Defaults.maxConnectionAge());
private int maxIdleTime = C3P0Config.initializeIntPropertyVar("maxIdleTime", C3P0Defaults.maxIdleTime());
private int maxIdleTimeExcessConnections = C3P0Config.initializeIntPropertyVar("maxIdleTimeExcessConnections", C3P0Defaults.maxIdleTimeExcessConnections());
private int maxPoolSize = C3P0Config.initializeIntPropertyVar("maxPoolSize", C3P0Defaults.maxPoolSize());
private int maxStatements = C3P0Config.initializeIntPropertyVar("maxStatements", C3P0Defaults.maxStatements());
private int maxStatementsPerConnection = C3P0Config.initializeIntPropertyVar("maxStatementsPerConnection", C3P0Defaults.maxStatementsPerConnection());
private int minPoolSize = C3P0Config.initializeIntPropertyVar("minPoolSize", C3P0Defaults.minPoolSize());
private DataSource nestedDataSource;
private String overrideDefaultPassword = C3P0Config.initializeStringPropertyVar("overrideDefaultPassword", C3P0Defaults.overrideDefaultPassword());
private String overrideDefaultUser = C3P0Config.initializeStringPropertyVar("overrideDefaultUser", C3P0Defaults.overrideDefaultUser());
private String preferredTestQuery = C3P0Config.initializeStringPropertyVar("preferredTestQuery", C3P0Defaults.preferredTestQuery());
private boolean privilegeSpawnedThreads = C3P0Config.initializeBooleanPropertyVar("privilegeSpawnedThreads", C3P0Defaults.privilegeSpawnedThreads());
private int propertyCycle = C3P0Config.initializeIntPropertyVar("propertyCycle", C3P0Defaults.propertyCycle());
private int statementCacheNumDeferredCloseThreads = C3P0Config.initializeIntPropertyVar("statementCacheNumDeferredCloseThreads", C3P0Defaults.statementCacheNumDeferredCloseThreads());
private boolean testConnectionOnCheckin = C3P0Config.initializeBooleanPropertyVar("testConnectionOnCheckin", C3P0Defaults.testConnectionOnCheckin());
private boolean testConnectionOnCheckout = C3P0Config.initializeBooleanPropertyVar("testConnectionOnCheckout", C3P0Defaults.testConnectionOnCheckout());
private int unreturnedConnectionTimeout = C3P0Config.initializeIntPropertyVar("unreturnedConnectionTimeout", C3P0Defaults.unreturnedConnectionTimeout());
private String userOverridesAsString = C3P0Config.initializeUserOverridesAsString();
private boolean usesTraditionalReflectiveProxies = C3P0Config.initializeBooleanPropertyVar("usesTraditionalReflectiveProxies", C3P0Defaults.usesTraditionalReflectiveProxies()); private static final long serialVersionUID = 1L; private static final short VERSION = 1;

public synchronized int getAcquireIncrement() {
return this.acquireIncrement;
}

public synchronized void setAcquireIncrement(int acquireIncrement) {
this.acquireIncrement = acquireIncrement;
}

public synchronized int getAcquireRetryAttempts() {
return this.acquireRetryAttempts;
}

public synchronized void setAcquireRetryAttempts(int acquireRetryAttempts) {
this.acquireRetryAttempts = acquireRetryAttempts;
}

public synchronized int getAcquireRetryDelay() {
return this.acquireRetryDelay;
}

public synchronized void setAcquireRetryDelay(int acquireRetryDelay) {
this.acquireRetryDelay = acquireRetryDelay;
}

public synchronized boolean isAutoCommitOnClose() {
return this.autoCommitOnClose;
}

public synchronized void setAutoCommitOnClose(boolean autoCommitOnClose) {
this.autoCommitOnClose = autoCommitOnClose;
}

public synchronized String getAutomaticTestTable() {
return this.automaticTestTable;
}

public synchronized void setAutomaticTestTable(String automaticTestTable) {
this.automaticTestTable = automaticTestTable;
}

public synchronized boolean isBreakAfterAcquireFailure() {
return this.breakAfterAcquireFailure;
}

public synchronized void setBreakAfterAcquireFailure(boolean breakAfterAcquireFailure) {
this.breakAfterAcquireFailure = breakAfterAcquireFailure;
}

public synchronized int getCheckoutTimeout() {
return this.checkoutTimeout;
}

public synchronized void setCheckoutTimeout(int checkoutTimeout) {
this.checkoutTimeout = checkoutTimeout;
}

public synchronized String getConnectionCustomizerClassName() {
return this.connectionCustomizerClassName;
}

public synchronized void setConnectionCustomizerClassName(String connectionCustomizerClassName) {
this.connectionCustomizerClassName = connectionCustomizerClassName;
}

public synchronized String getConnectionTesterClassName() {
return this.connectionTesterClassName;
}

public synchronized void setConnectionTesterClassName(String connectionTesterClassName) throws PropertyVetoException {
String oldVal = this.connectionTesterClassName;
if (!eqOrBothNull(oldVal, connectionTesterClassName))
this.vcs.fireVetoableChange("connectionTesterClassName", oldVal, connectionTesterClassName); 
this.connectionTesterClassName = connectionTesterClassName;
}

public synchronized String getContextClassLoaderSource() {
return this.contextClassLoaderSource;
}

public synchronized void setContextClassLoaderSource(String contextClassLoaderSource) {
this.contextClassLoaderSource = contextClassLoaderSource;
}

public synchronized boolean isDebugUnreturnedConnectionStackTraces() {
return this.debugUnreturnedConnectionStackTraces;
}

public synchronized void setDebugUnreturnedConnectionStackTraces(boolean debugUnreturnedConnectionStackTraces) {
this.debugUnreturnedConnectionStackTraces = debugUnreturnedConnectionStackTraces;
}

public synchronized String getFactoryClassLocation() {
return this.factoryClassLocation;
}

public synchronized void setFactoryClassLocation(String factoryClassLocation) {
this.factoryClassLocation = factoryClassLocation;
}

public synchronized boolean isForceIgnoreUnresolvedTransactions() {
return this.forceIgnoreUnresolvedTransactions;
}

public synchronized void setForceIgnoreUnresolvedTransactions(boolean forceIgnoreUnresolvedTransactions) {
this.forceIgnoreUnresolvedTransactions = forceIgnoreUnresolvedTransactions;
}

public String getIdentityToken() {
return this.identityToken;
}

public void setIdentityToken(String identityToken) {
String oldVal = this.identityToken;
this.identityToken = identityToken;
if (!eqOrBothNull(oldVal, identityToken))
this.pcs.firePropertyChange("identityToken", oldVal, identityToken); 
}

public synchronized int getIdleConnectionTestPeriod() {
return this.idleConnectionTestPeriod;
}

public synchronized void setIdleConnectionTestPeriod(int idleConnectionTestPeriod) {
this.idleConnectionTestPeriod = idleConnectionTestPeriod;
}

public synchronized int getInitialPoolSize() {
return this.initialPoolSize;
}

public synchronized void setInitialPoolSize(int initialPoolSize) {
this.initialPoolSize = initialPoolSize;
}

public synchronized int getMaxAdministrativeTaskTime() {
return this.maxAdministrativeTaskTime;
}

public synchronized void setMaxAdministrativeTaskTime(int maxAdministrativeTaskTime) {
this.maxAdministrativeTaskTime = maxAdministrativeTaskTime;
}

public synchronized int getMaxConnectionAge() {
return this.maxConnectionAge;
}

public synchronized void setMaxConnectionAge(int maxConnectionAge) {
this.maxConnectionAge = maxConnectionAge;
}

public synchronized int getMaxIdleTime() {
return this.maxIdleTime;
}

public synchronized void setMaxIdleTime(int maxIdleTime) {
this.maxIdleTime = maxIdleTime;
}

public synchronized int getMaxIdleTimeExcessConnections() {
return this.maxIdleTimeExcessConnections;
}

public synchronized void setMaxIdleTimeExcessConnections(int maxIdleTimeExcessConnections) {
this.maxIdleTimeExcessConnections = maxIdleTimeExcessConnections;
}

public synchronized int getMaxPoolSize() {
return this.maxPoolSize;
}

public synchronized void setMaxPoolSize(int maxPoolSize) {
this.maxPoolSize = maxPoolSize;
}

public synchronized int getMaxStatements() {
return this.maxStatements;
}

public synchronized void setMaxStatements(int maxStatements) {
this.maxStatements = maxStatements;
}

public synchronized int getMaxStatementsPerConnection() {
return this.maxStatementsPerConnection;
}

public synchronized void setMaxStatementsPerConnection(int maxStatementsPerConnection) {
this.maxStatementsPerConnection = maxStatementsPerConnection;
}

public synchronized int getMinPoolSize() {
return this.minPoolSize;
}

public synchronized void setMinPoolSize(int minPoolSize) {
this.minPoolSize = minPoolSize;
}

public synchronized DataSource getNestedDataSource() {
return this.nestedDataSource;
}

public synchronized void setNestedDataSource(DataSource nestedDataSource) {
DataSource oldVal = this.nestedDataSource;
this.nestedDataSource = nestedDataSource;
if (!eqOrBothNull(oldVal, nestedDataSource))
this.pcs.firePropertyChange("nestedDataSource", oldVal, nestedDataSource); 
}

public synchronized String getOverrideDefaultPassword() {
return this.overrideDefaultPassword;
}

public synchronized void setOverrideDefaultPassword(String overrideDefaultPassword) {
this.overrideDefaultPassword = overrideDefaultPassword;
}

public synchronized String getOverrideDefaultUser() {
return this.overrideDefaultUser;
}

public synchronized void setOverrideDefaultUser(String overrideDefaultUser) {
this.overrideDefaultUser = overrideDefaultUser;
}

public synchronized String getPreferredTestQuery() {
return this.preferredTestQuery;
}

public synchronized void setPreferredTestQuery(String preferredTestQuery) {
this.preferredTestQuery = preferredTestQuery;
}

public synchronized boolean isPrivilegeSpawnedThreads() {
return this.privilegeSpawnedThreads;
}

public synchronized void setPrivilegeSpawnedThreads(boolean privilegeSpawnedThreads) {
this.privilegeSpawnedThreads = privilegeSpawnedThreads;
}

public synchronized int getPropertyCycle() {
return this.propertyCycle;
}

public synchronized void setPropertyCycle(int propertyCycle) {
this.propertyCycle = propertyCycle;
}

public synchronized int getStatementCacheNumDeferredCloseThreads() {
return this.statementCacheNumDeferredCloseThreads;
}

public synchronized void setStatementCacheNumDeferredCloseThreads(int statementCacheNumDeferredCloseThreads) {
this.statementCacheNumDeferredCloseThreads = statementCacheNumDeferredCloseThreads;
}

public synchronized boolean isTestConnectionOnCheckin() {
return this.testConnectionOnCheckin;
}

public synchronized void setTestConnectionOnCheckin(boolean testConnectionOnCheckin) {
this.testConnectionOnCheckin = testConnectionOnCheckin;
}

public synchronized boolean isTestConnectionOnCheckout() {
return this.testConnectionOnCheckout;
}

public synchronized void setTestConnectionOnCheckout(boolean testConnectionOnCheckout) {
this.testConnectionOnCheckout = testConnectionOnCheckout;
}

public synchronized int getUnreturnedConnectionTimeout() {
return this.unreturnedConnectionTimeout;
}

public synchronized void setUnreturnedConnectionTimeout(int unreturnedConnectionTimeout) {
this.unreturnedConnectionTimeout = unreturnedConnectionTimeout;
}

public synchronized String getUserOverridesAsString() {
return this.userOverridesAsString;
}

public synchronized void setUserOverridesAsString(String userOverridesAsString) throws PropertyVetoException {
String oldVal = this.userOverridesAsString;
if (!eqOrBothNull(oldVal, userOverridesAsString))
this.vcs.fireVetoableChange("userOverridesAsString", oldVal, userOverridesAsString); 
this.userOverridesAsString = userOverridesAsString;
}

public synchronized boolean isUsesTraditionalReflectiveProxies() {
return this.usesTraditionalReflectiveProxies;
}

public synchronized void setUsesTraditionalReflectiveProxies(boolean usesTraditionalReflectiveProxies) {
this.usesTraditionalReflectiveProxies = usesTraditionalReflectiveProxies;
}

public void addPropertyChangeListener(PropertyChangeListener pcl) {
this.pcs.addPropertyChangeListener(pcl);
}
public void addPropertyChangeListener(String propName, PropertyChangeListener pcl) {
this.pcs.addPropertyChangeListener(propName, pcl);
}
public void removePropertyChangeListener(PropertyChangeListener pcl) {
this.pcs.removePropertyChangeListener(pcl);
}
public void removePropertyChangeListener(String propName, PropertyChangeListener pcl) {
this.pcs.removePropertyChangeListener(propName, pcl);
}
public PropertyChangeListener[] getPropertyChangeListeners() {
return this.pcs.getPropertyChangeListeners();
}
public void addVetoableChangeListener(VetoableChangeListener vcl) {
this.vcs.addVetoableChangeListener(vcl);
}
public void removeVetoableChangeListener(VetoableChangeListener vcl) {
this.vcs.removeVetoableChangeListener(vcl);
}
public VetoableChangeListener[] getVetoableChangeListeners() {
return this.vcs.getVetoableChangeListeners();
}
private boolean eqOrBothNull(Object a, Object b) {
return (a == b || (a != null && a.equals(b)));
}

private void writeObject(ObjectOutputStream oos) throws IOException {
oos.writeShort(1);
oos.writeInt(this.acquireIncrement);
oos.writeInt(this.acquireRetryAttempts);
oos.writeInt(this.acquireRetryDelay);
oos.writeBoolean(this.autoCommitOnClose);
oos.writeObject(this.automaticTestTable);
oos.writeBoolean(this.breakAfterAcquireFailure);
oos.writeInt(this.checkoutTimeout);
oos.writeObject(this.connectionCustomizerClassName);
oos.writeObject(this.connectionTesterClassName);
oos.writeObject(this.contextClassLoaderSource);
oos.writeBoolean(this.debugUnreturnedConnectionStackTraces);
oos.writeObject(this.factoryClassLocation);
oos.writeBoolean(this.forceIgnoreUnresolvedTransactions);
oos.writeObject(this.identityToken);
oos.writeInt(this.idleConnectionTestPeriod);
oos.writeInt(this.initialPoolSize);
oos.writeInt(this.maxAdministrativeTaskTime);
oos.writeInt(this.maxConnectionAge);
oos.writeInt(this.maxIdleTime);
oos.writeInt(this.maxIdleTimeExcessConnections);
oos.writeInt(this.maxPoolSize);
oos.writeInt(this.maxStatements);
oos.writeInt(this.maxStatementsPerConnection);
oos.writeInt(this.minPoolSize);

try {
SerializableUtils.toByteArray(this.nestedDataSource);
oos.writeObject(this.nestedDataSource);
}
catch (NotSerializableException nse) {

try {
ReferenceIndirector referenceIndirector = new ReferenceIndirector();
oos.writeObject(referenceIndirector.indirectForm(this.nestedDataSource));
}
catch (IOException indirectionIOException) {
throw indirectionIOException;
} catch (Exception indirectionOtherException) {
throw new IOException("Problem indirectly serializing nestedDataSource: " + indirectionOtherException.toString());
} 
}  oos.writeObject(this.overrideDefaultPassword);
oos.writeObject(this.overrideDefaultUser);
oos.writeObject(this.preferredTestQuery);
oos.writeBoolean(this.privilegeSpawnedThreads);
oos.writeInt(this.propertyCycle);
oos.writeInt(this.statementCacheNumDeferredCloseThreads);
oos.writeBoolean(this.testConnectionOnCheckin);
oos.writeBoolean(this.testConnectionOnCheckout);
oos.writeInt(this.unreturnedConnectionTimeout);
oos.writeObject(this.userOverridesAsString);
oos.writeBoolean(this.usesTraditionalReflectiveProxies);
}

private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
Object o;
short version = ois.readShort();
switch (version) {

case 1:
this.acquireIncrement = ois.readInt();
this.acquireRetryAttempts = ois.readInt();
this.acquireRetryDelay = ois.readInt();
this.autoCommitOnClose = ois.readBoolean();
this.automaticTestTable = (String)ois.readObject();
this.breakAfterAcquireFailure = ois.readBoolean();
this.checkoutTimeout = ois.readInt();
this.connectionCustomizerClassName = (String)ois.readObject();
this.connectionTesterClassName = (String)ois.readObject();
this.contextClassLoaderSource = (String)ois.readObject();
this.debugUnreturnedConnectionStackTraces = ois.readBoolean();
this.factoryClassLocation = (String)ois.readObject();
this.forceIgnoreUnresolvedTransactions = ois.readBoolean();
this.identityToken = (String)ois.readObject();
this.idleConnectionTestPeriod = ois.readInt();
this.initialPoolSize = ois.readInt();
this.maxAdministrativeTaskTime = ois.readInt();
this.maxConnectionAge = ois.readInt();
this.maxIdleTime = ois.readInt();
this.maxIdleTimeExcessConnections = ois.readInt();
this.maxPoolSize = ois.readInt();
this.maxStatements = ois.readInt();
this.maxStatementsPerConnection = ois.readInt();
this.minPoolSize = ois.readInt();

o = ois.readObject();
if (o instanceof IndirectlySerialized) o = ((IndirectlySerialized)o).getObject(); 
this.nestedDataSource = (DataSource)o;

this.overrideDefaultPassword = (String)ois.readObject();
this.overrideDefaultUser = (String)ois.readObject();
this.preferredTestQuery = (String)ois.readObject();
this.privilegeSpawnedThreads = ois.readBoolean();
this.propertyCycle = ois.readInt();
this.statementCacheNumDeferredCloseThreads = ois.readInt();
this.testConnectionOnCheckin = ois.readBoolean();
this.testConnectionOnCheckout = ois.readBoolean();
this.unreturnedConnectionTimeout = ois.readInt();
this.userOverridesAsString = (String)ois.readObject();
this.usesTraditionalReflectiveProxies = ois.readBoolean();
this.pcs = new PropertyChangeSupport(this);
this.vcs = new VetoableChangeSupport(this);
return;
} 
throw new IOException("Unsupported Serialized Version: " + version);
}

public Logger getParentLogger() throws SQLFeatureNotSupportedException {
return C3P0ImplUtils.PARENT_LOGGER;
}

public String toString() {
StringBuffer sb = new StringBuffer();
sb.append(super.toString());
sb.append(" [ ");
sb.append("acquireIncrement -> " + this.acquireIncrement);
sb.append(", ");
sb.append("acquireRetryAttempts -> " + this.acquireRetryAttempts);
sb.append(", ");
sb.append("acquireRetryDelay -> " + this.acquireRetryDelay);
sb.append(", ");
sb.append("autoCommitOnClose -> " + this.autoCommitOnClose);
sb.append(", ");
sb.append("automaticTestTable -> " + this.automaticTestTable);
sb.append(", ");
sb.append("breakAfterAcquireFailure -> " + this.breakAfterAcquireFailure);
sb.append(", ");
sb.append("checkoutTimeout -> " + this.checkoutTimeout);
sb.append(", ");
sb.append("connectionCustomizerClassName -> " + this.connectionCustomizerClassName);
sb.append(", ");
sb.append("connectionTesterClassName -> " + this.connectionTesterClassName);
sb.append(", ");
sb.append("contextClassLoaderSource -> " + this.contextClassLoaderSource);
sb.append(", ");
sb.append("debugUnreturnedConnectionStackTraces -> " + this.debugUnreturnedConnectionStackTraces);
sb.append(", ");
sb.append("factoryClassLocation -> " + this.factoryClassLocation);
sb.append(", ");
sb.append("forceIgnoreUnresolvedTransactions -> " + this.forceIgnoreUnresolvedTransactions);
sb.append(", ");
sb.append("identityToken -> " + this.identityToken);
sb.append(", ");
sb.append("idleConnectionTestPeriod -> " + this.idleConnectionTestPeriod);
sb.append(", ");
sb.append("initialPoolSize -> " + this.initialPoolSize);
sb.append(", ");
sb.append("maxAdministrativeTaskTime -> " + this.maxAdministrativeTaskTime);
sb.append(", ");
sb.append("maxConnectionAge -> " + this.maxConnectionAge);
sb.append(", ");
sb.append("maxIdleTime -> " + this.maxIdleTime);
sb.append(", ");
sb.append("maxIdleTimeExcessConnections -> " + this.maxIdleTimeExcessConnections);
sb.append(", ");
sb.append("maxPoolSize -> " + this.maxPoolSize);
sb.append(", ");
sb.append("maxStatements -> " + this.maxStatements);
sb.append(", ");
sb.append("maxStatementsPerConnection -> " + this.maxStatementsPerConnection);
sb.append(", ");
sb.append("minPoolSize -> " + this.minPoolSize);
sb.append(", ");
sb.append("nestedDataSource -> " + this.nestedDataSource);
sb.append(", ");
sb.append("preferredTestQuery -> " + this.preferredTestQuery);
sb.append(", ");
sb.append("privilegeSpawnedThreads -> " + this.privilegeSpawnedThreads);
sb.append(", ");
sb.append("propertyCycle -> " + this.propertyCycle);
sb.append(", ");
sb.append("statementCacheNumDeferredCloseThreads -> " + this.statementCacheNumDeferredCloseThreads);
sb.append(", ");
sb.append("testConnectionOnCheckin -> " + this.testConnectionOnCheckin);
sb.append(", ");
sb.append("testConnectionOnCheckout -> " + this.testConnectionOnCheckout);
sb.append(", ");
sb.append("unreturnedConnectionTimeout -> " + this.unreturnedConnectionTimeout);
sb.append(", ");
sb.append("usesTraditionalReflectiveProxies -> " + this.usesTraditionalReflectiveProxies);

String extraToStringInfo = extraToStringInfo();
if (extraToStringInfo != null)
sb.append(extraToStringInfo); 
sb.append(" ]");
return sb.toString();
}

protected String extraToStringInfo() {
return null;
}
static final JavaBeanReferenceMaker referenceMaker = new JavaBeanReferenceMaker();

static {
referenceMaker.setFactoryClassName("com.mchange.v2.c3p0.impl.C3P0JavaBeanObjectFactory");
referenceMaker.addReferenceProperty("acquireIncrement");
referenceMaker.addReferenceProperty("acquireRetryAttempts");
referenceMaker.addReferenceProperty("acquireRetryDelay");
referenceMaker.addReferenceProperty("autoCommitOnClose");
referenceMaker.addReferenceProperty("automaticTestTable");
referenceMaker.addReferenceProperty("breakAfterAcquireFailure");
referenceMaker.addReferenceProperty("checkoutTimeout");
referenceMaker.addReferenceProperty("connectionCustomizerClassName");
referenceMaker.addReferenceProperty("connectionTesterClassName");
referenceMaker.addReferenceProperty("contextClassLoaderSource");
referenceMaker.addReferenceProperty("debugUnreturnedConnectionStackTraces");
referenceMaker.addReferenceProperty("factoryClassLocation");
referenceMaker.addReferenceProperty("forceIgnoreUnresolvedTransactions");
referenceMaker.addReferenceProperty("identityToken");
referenceMaker.addReferenceProperty("idleConnectionTestPeriod");
referenceMaker.addReferenceProperty("initialPoolSize");
referenceMaker.addReferenceProperty("maxAdministrativeTaskTime");
referenceMaker.addReferenceProperty("maxConnectionAge");
referenceMaker.addReferenceProperty("maxIdleTime");
referenceMaker.addReferenceProperty("maxIdleTimeExcessConnections");
referenceMaker.addReferenceProperty("maxPoolSize");
referenceMaker.addReferenceProperty("maxStatements");
referenceMaker.addReferenceProperty("maxStatementsPerConnection");
referenceMaker.addReferenceProperty("minPoolSize");
referenceMaker.addReferenceProperty("nestedDataSource");
referenceMaker.addReferenceProperty("overrideDefaultPassword");
referenceMaker.addReferenceProperty("overrideDefaultUser");
referenceMaker.addReferenceProperty("preferredTestQuery");
referenceMaker.addReferenceProperty("privilegeSpawnedThreads");
referenceMaker.addReferenceProperty("propertyCycle");
referenceMaker.addReferenceProperty("statementCacheNumDeferredCloseThreads");
referenceMaker.addReferenceProperty("testConnectionOnCheckin");
referenceMaker.addReferenceProperty("testConnectionOnCheckout");
referenceMaker.addReferenceProperty("unreturnedConnectionTimeout");
referenceMaker.addReferenceProperty("userOverridesAsString");
referenceMaker.addReferenceProperty("usesTraditionalReflectiveProxies");
}

public Reference getReference() throws NamingException {
return referenceMaker.createReference(this);
}

public WrapperConnectionPoolDataSourceBase(boolean autoregister) {
if (autoregister) {

this.identityToken = C3P0ImplUtils.allocateIdentityToken(this);
C3P0Registry.reregister(this);
} 
}

private WrapperConnectionPoolDataSourceBase() {}

protected abstract PooledConnection getPooledConnection(ConnectionCustomizer paramConnectionCustomizer, String paramString) throws SQLException;

protected abstract PooledConnection getPooledConnection(String paramString1, String paramString2, ConnectionCustomizer paramConnectionCustomizer, String paramString3) throws SQLException;
}

