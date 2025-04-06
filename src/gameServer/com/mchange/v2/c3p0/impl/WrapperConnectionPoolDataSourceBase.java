/*     */ package com.mchange.v2.c3p0.impl;
/*     */ 
/*     */ import com.mchange.v2.c3p0.C3P0Registry;
/*     */ import com.mchange.v2.c3p0.ConnectionCustomizer;
/*     */ import com.mchange.v2.c3p0.cfg.C3P0Config;
/*     */ import com.mchange.v2.naming.JavaBeanReferenceMaker;
/*     */ import com.mchange.v2.naming.ReferenceIndirector;
/*     */ import com.mchange.v2.ser.IndirectlySerialized;
/*     */ import com.mchange.v2.ser.SerializableUtils;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.beans.PropertyVetoException;
/*     */ import java.beans.VetoableChangeListener;
/*     */ import java.beans.VetoableChangeSupport;
/*     */ import java.io.IOException;
/*     */ import java.io.NotSerializableException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.util.logging.Logger;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.Referenceable;
/*     */ import javax.sql.DataSource;
/*     */ import javax.sql.PooledConnection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class WrapperConnectionPoolDataSourceBase
/*     */   extends IdentityTokenResolvable
/*     */   implements Referenceable, Serializable
/*     */ {
/*  45 */   protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);
/*     */   
/*     */   protected PropertyChangeSupport getPropertyChangeSupport() {
/*  48 */     return this.pcs;
/*  49 */   } protected VetoableChangeSupport vcs = new VetoableChangeSupport(this);
/*     */   
/*     */   protected VetoableChangeSupport getVetoableChangeSupport() {
/*  52 */     return this.vcs;
/*  53 */   } private int acquireIncrement = C3P0Config.initializeIntPropertyVar("acquireIncrement", C3P0Defaults.acquireIncrement());
/*  54 */   private int acquireRetryAttempts = C3P0Config.initializeIntPropertyVar("acquireRetryAttempts", C3P0Defaults.acquireRetryAttempts());
/*  55 */   private int acquireRetryDelay = C3P0Config.initializeIntPropertyVar("acquireRetryDelay", C3P0Defaults.acquireRetryDelay());
/*  56 */   private boolean autoCommitOnClose = C3P0Config.initializeBooleanPropertyVar("autoCommitOnClose", C3P0Defaults.autoCommitOnClose());
/*  57 */   private String automaticTestTable = C3P0Config.initializeStringPropertyVar("automaticTestTable", C3P0Defaults.automaticTestTable());
/*  58 */   private boolean breakAfterAcquireFailure = C3P0Config.initializeBooleanPropertyVar("breakAfterAcquireFailure", C3P0Defaults.breakAfterAcquireFailure());
/*  59 */   private int checkoutTimeout = C3P0Config.initializeIntPropertyVar("checkoutTimeout", C3P0Defaults.checkoutTimeout());
/*  60 */   private String connectionCustomizerClassName = C3P0Config.initializeStringPropertyVar("connectionCustomizerClassName", C3P0Defaults.connectionCustomizerClassName());
/*  61 */   private String connectionTesterClassName = C3P0Config.initializeStringPropertyVar("connectionTesterClassName", C3P0Defaults.connectionTesterClassName());
/*  62 */   private String contextClassLoaderSource = C3P0Config.initializeStringPropertyVar("contextClassLoaderSource", C3P0Defaults.contextClassLoaderSource());
/*  63 */   private boolean debugUnreturnedConnectionStackTraces = C3P0Config.initializeBooleanPropertyVar("debugUnreturnedConnectionStackTraces", C3P0Defaults.debugUnreturnedConnectionStackTraces());
/*  64 */   private String factoryClassLocation = C3P0Config.initializeStringPropertyVar("factoryClassLocation", C3P0Defaults.factoryClassLocation());
/*  65 */   private boolean forceIgnoreUnresolvedTransactions = C3P0Config.initializeBooleanPropertyVar("forceIgnoreUnresolvedTransactions", C3P0Defaults.forceIgnoreUnresolvedTransactions());
/*     */   private volatile String identityToken;
/*  67 */   private int idleConnectionTestPeriod = C3P0Config.initializeIntPropertyVar("idleConnectionTestPeriod", C3P0Defaults.idleConnectionTestPeriod());
/*  68 */   private int initialPoolSize = C3P0Config.initializeIntPropertyVar("initialPoolSize", C3P0Defaults.initialPoolSize());
/*  69 */   private int maxAdministrativeTaskTime = C3P0Config.initializeIntPropertyVar("maxAdministrativeTaskTime", C3P0Defaults.maxAdministrativeTaskTime());
/*  70 */   private int maxConnectionAge = C3P0Config.initializeIntPropertyVar("maxConnectionAge", C3P0Defaults.maxConnectionAge());
/*  71 */   private int maxIdleTime = C3P0Config.initializeIntPropertyVar("maxIdleTime", C3P0Defaults.maxIdleTime());
/*  72 */   private int maxIdleTimeExcessConnections = C3P0Config.initializeIntPropertyVar("maxIdleTimeExcessConnections", C3P0Defaults.maxIdleTimeExcessConnections());
/*  73 */   private int maxPoolSize = C3P0Config.initializeIntPropertyVar("maxPoolSize", C3P0Defaults.maxPoolSize());
/*  74 */   private int maxStatements = C3P0Config.initializeIntPropertyVar("maxStatements", C3P0Defaults.maxStatements());
/*  75 */   private int maxStatementsPerConnection = C3P0Config.initializeIntPropertyVar("maxStatementsPerConnection", C3P0Defaults.maxStatementsPerConnection());
/*  76 */   private int minPoolSize = C3P0Config.initializeIntPropertyVar("minPoolSize", C3P0Defaults.minPoolSize());
/*     */   private DataSource nestedDataSource;
/*  78 */   private String overrideDefaultPassword = C3P0Config.initializeStringPropertyVar("overrideDefaultPassword", C3P0Defaults.overrideDefaultPassword());
/*  79 */   private String overrideDefaultUser = C3P0Config.initializeStringPropertyVar("overrideDefaultUser", C3P0Defaults.overrideDefaultUser());
/*  80 */   private String preferredTestQuery = C3P0Config.initializeStringPropertyVar("preferredTestQuery", C3P0Defaults.preferredTestQuery());
/*  81 */   private boolean privilegeSpawnedThreads = C3P0Config.initializeBooleanPropertyVar("privilegeSpawnedThreads", C3P0Defaults.privilegeSpawnedThreads());
/*  82 */   private int propertyCycle = C3P0Config.initializeIntPropertyVar("propertyCycle", C3P0Defaults.propertyCycle());
/*  83 */   private int statementCacheNumDeferredCloseThreads = C3P0Config.initializeIntPropertyVar("statementCacheNumDeferredCloseThreads", C3P0Defaults.statementCacheNumDeferredCloseThreads());
/*  84 */   private boolean testConnectionOnCheckin = C3P0Config.initializeBooleanPropertyVar("testConnectionOnCheckin", C3P0Defaults.testConnectionOnCheckin());
/*  85 */   private boolean testConnectionOnCheckout = C3P0Config.initializeBooleanPropertyVar("testConnectionOnCheckout", C3P0Defaults.testConnectionOnCheckout());
/*  86 */   private int unreturnedConnectionTimeout = C3P0Config.initializeIntPropertyVar("unreturnedConnectionTimeout", C3P0Defaults.unreturnedConnectionTimeout());
/*  87 */   private String userOverridesAsString = C3P0Config.initializeUserOverridesAsString();
/*  88 */   private boolean usesTraditionalReflectiveProxies = C3P0Config.initializeBooleanPropertyVar("usesTraditionalReflectiveProxies", C3P0Defaults.usesTraditionalReflectiveProxies()); private static final long serialVersionUID = 1L; private static final short VERSION = 1;
/*     */   
/*     */   public synchronized int getAcquireIncrement() {
/*  91 */     return this.acquireIncrement;
/*     */   }
/*     */   
/*     */   public synchronized void setAcquireIncrement(int acquireIncrement) {
/*  95 */     this.acquireIncrement = acquireIncrement;
/*     */   }
/*     */   
/*     */   public synchronized int getAcquireRetryAttempts() {
/*  99 */     return this.acquireRetryAttempts;
/*     */   }
/*     */   
/*     */   public synchronized void setAcquireRetryAttempts(int acquireRetryAttempts) {
/* 103 */     this.acquireRetryAttempts = acquireRetryAttempts;
/*     */   }
/*     */   
/*     */   public synchronized int getAcquireRetryDelay() {
/* 107 */     return this.acquireRetryDelay;
/*     */   }
/*     */   
/*     */   public synchronized void setAcquireRetryDelay(int acquireRetryDelay) {
/* 111 */     this.acquireRetryDelay = acquireRetryDelay;
/*     */   }
/*     */   
/*     */   public synchronized boolean isAutoCommitOnClose() {
/* 115 */     return this.autoCommitOnClose;
/*     */   }
/*     */   
/*     */   public synchronized void setAutoCommitOnClose(boolean autoCommitOnClose) {
/* 119 */     this.autoCommitOnClose = autoCommitOnClose;
/*     */   }
/*     */   
/*     */   public synchronized String getAutomaticTestTable() {
/* 123 */     return this.automaticTestTable;
/*     */   }
/*     */   
/*     */   public synchronized void setAutomaticTestTable(String automaticTestTable) {
/* 127 */     this.automaticTestTable = automaticTestTable;
/*     */   }
/*     */   
/*     */   public synchronized boolean isBreakAfterAcquireFailure() {
/* 131 */     return this.breakAfterAcquireFailure;
/*     */   }
/*     */   
/*     */   public synchronized void setBreakAfterAcquireFailure(boolean breakAfterAcquireFailure) {
/* 135 */     this.breakAfterAcquireFailure = breakAfterAcquireFailure;
/*     */   }
/*     */   
/*     */   public synchronized int getCheckoutTimeout() {
/* 139 */     return this.checkoutTimeout;
/*     */   }
/*     */   
/*     */   public synchronized void setCheckoutTimeout(int checkoutTimeout) {
/* 143 */     this.checkoutTimeout = checkoutTimeout;
/*     */   }
/*     */   
/*     */   public synchronized String getConnectionCustomizerClassName() {
/* 147 */     return this.connectionCustomizerClassName;
/*     */   }
/*     */   
/*     */   public synchronized void setConnectionCustomizerClassName(String connectionCustomizerClassName) {
/* 151 */     this.connectionCustomizerClassName = connectionCustomizerClassName;
/*     */   }
/*     */   
/*     */   public synchronized String getConnectionTesterClassName() {
/* 155 */     return this.connectionTesterClassName;
/*     */   }
/*     */   
/*     */   public synchronized void setConnectionTesterClassName(String connectionTesterClassName) throws PropertyVetoException {
/* 159 */     String oldVal = this.connectionTesterClassName;
/* 160 */     if (!eqOrBothNull(oldVal, connectionTesterClassName))
/* 161 */       this.vcs.fireVetoableChange("connectionTesterClassName", oldVal, connectionTesterClassName); 
/* 162 */     this.connectionTesterClassName = connectionTesterClassName;
/*     */   }
/*     */   
/*     */   public synchronized String getContextClassLoaderSource() {
/* 166 */     return this.contextClassLoaderSource;
/*     */   }
/*     */   
/*     */   public synchronized void setContextClassLoaderSource(String contextClassLoaderSource) {
/* 170 */     this.contextClassLoaderSource = contextClassLoaderSource;
/*     */   }
/*     */   
/*     */   public synchronized boolean isDebugUnreturnedConnectionStackTraces() {
/* 174 */     return this.debugUnreturnedConnectionStackTraces;
/*     */   }
/*     */   
/*     */   public synchronized void setDebugUnreturnedConnectionStackTraces(boolean debugUnreturnedConnectionStackTraces) {
/* 178 */     this.debugUnreturnedConnectionStackTraces = debugUnreturnedConnectionStackTraces;
/*     */   }
/*     */   
/*     */   public synchronized String getFactoryClassLocation() {
/* 182 */     return this.factoryClassLocation;
/*     */   }
/*     */   
/*     */   public synchronized void setFactoryClassLocation(String factoryClassLocation) {
/* 186 */     this.factoryClassLocation = factoryClassLocation;
/*     */   }
/*     */   
/*     */   public synchronized boolean isForceIgnoreUnresolvedTransactions() {
/* 190 */     return this.forceIgnoreUnresolvedTransactions;
/*     */   }
/*     */   
/*     */   public synchronized void setForceIgnoreUnresolvedTransactions(boolean forceIgnoreUnresolvedTransactions) {
/* 194 */     this.forceIgnoreUnresolvedTransactions = forceIgnoreUnresolvedTransactions;
/*     */   }
/*     */   
/*     */   public String getIdentityToken() {
/* 198 */     return this.identityToken;
/*     */   }
/*     */   
/*     */   public void setIdentityToken(String identityToken) {
/* 202 */     String oldVal = this.identityToken;
/* 203 */     this.identityToken = identityToken;
/* 204 */     if (!eqOrBothNull(oldVal, identityToken))
/* 205 */       this.pcs.firePropertyChange("identityToken", oldVal, identityToken); 
/*     */   }
/*     */   
/*     */   public synchronized int getIdleConnectionTestPeriod() {
/* 209 */     return this.idleConnectionTestPeriod;
/*     */   }
/*     */   
/*     */   public synchronized void setIdleConnectionTestPeriod(int idleConnectionTestPeriod) {
/* 213 */     this.idleConnectionTestPeriod = idleConnectionTestPeriod;
/*     */   }
/*     */   
/*     */   public synchronized int getInitialPoolSize() {
/* 217 */     return this.initialPoolSize;
/*     */   }
/*     */   
/*     */   public synchronized void setInitialPoolSize(int initialPoolSize) {
/* 221 */     this.initialPoolSize = initialPoolSize;
/*     */   }
/*     */   
/*     */   public synchronized int getMaxAdministrativeTaskTime() {
/* 225 */     return this.maxAdministrativeTaskTime;
/*     */   }
/*     */   
/*     */   public synchronized void setMaxAdministrativeTaskTime(int maxAdministrativeTaskTime) {
/* 229 */     this.maxAdministrativeTaskTime = maxAdministrativeTaskTime;
/*     */   }
/*     */   
/*     */   public synchronized int getMaxConnectionAge() {
/* 233 */     return this.maxConnectionAge;
/*     */   }
/*     */   
/*     */   public synchronized void setMaxConnectionAge(int maxConnectionAge) {
/* 237 */     this.maxConnectionAge = maxConnectionAge;
/*     */   }
/*     */   
/*     */   public synchronized int getMaxIdleTime() {
/* 241 */     return this.maxIdleTime;
/*     */   }
/*     */   
/*     */   public synchronized void setMaxIdleTime(int maxIdleTime) {
/* 245 */     this.maxIdleTime = maxIdleTime;
/*     */   }
/*     */   
/*     */   public synchronized int getMaxIdleTimeExcessConnections() {
/* 249 */     return this.maxIdleTimeExcessConnections;
/*     */   }
/*     */   
/*     */   public synchronized void setMaxIdleTimeExcessConnections(int maxIdleTimeExcessConnections) {
/* 253 */     this.maxIdleTimeExcessConnections = maxIdleTimeExcessConnections;
/*     */   }
/*     */   
/*     */   public synchronized int getMaxPoolSize() {
/* 257 */     return this.maxPoolSize;
/*     */   }
/*     */   
/*     */   public synchronized void setMaxPoolSize(int maxPoolSize) {
/* 261 */     this.maxPoolSize = maxPoolSize;
/*     */   }
/*     */   
/*     */   public synchronized int getMaxStatements() {
/* 265 */     return this.maxStatements;
/*     */   }
/*     */   
/*     */   public synchronized void setMaxStatements(int maxStatements) {
/* 269 */     this.maxStatements = maxStatements;
/*     */   }
/*     */   
/*     */   public synchronized int getMaxStatementsPerConnection() {
/* 273 */     return this.maxStatementsPerConnection;
/*     */   }
/*     */   
/*     */   public synchronized void setMaxStatementsPerConnection(int maxStatementsPerConnection) {
/* 277 */     this.maxStatementsPerConnection = maxStatementsPerConnection;
/*     */   }
/*     */   
/*     */   public synchronized int getMinPoolSize() {
/* 281 */     return this.minPoolSize;
/*     */   }
/*     */   
/*     */   public synchronized void setMinPoolSize(int minPoolSize) {
/* 285 */     this.minPoolSize = minPoolSize;
/*     */   }
/*     */   
/*     */   public synchronized DataSource getNestedDataSource() {
/* 289 */     return this.nestedDataSource;
/*     */   }
/*     */   
/*     */   public synchronized void setNestedDataSource(DataSource nestedDataSource) {
/* 293 */     DataSource oldVal = this.nestedDataSource;
/* 294 */     this.nestedDataSource = nestedDataSource;
/* 295 */     if (!eqOrBothNull(oldVal, nestedDataSource))
/* 296 */       this.pcs.firePropertyChange("nestedDataSource", oldVal, nestedDataSource); 
/*     */   }
/*     */   
/*     */   public synchronized String getOverrideDefaultPassword() {
/* 300 */     return this.overrideDefaultPassword;
/*     */   }
/*     */   
/*     */   public synchronized void setOverrideDefaultPassword(String overrideDefaultPassword) {
/* 304 */     this.overrideDefaultPassword = overrideDefaultPassword;
/*     */   }
/*     */   
/*     */   public synchronized String getOverrideDefaultUser() {
/* 308 */     return this.overrideDefaultUser;
/*     */   }
/*     */   
/*     */   public synchronized void setOverrideDefaultUser(String overrideDefaultUser) {
/* 312 */     this.overrideDefaultUser = overrideDefaultUser;
/*     */   }
/*     */   
/*     */   public synchronized String getPreferredTestQuery() {
/* 316 */     return this.preferredTestQuery;
/*     */   }
/*     */   
/*     */   public synchronized void setPreferredTestQuery(String preferredTestQuery) {
/* 320 */     this.preferredTestQuery = preferredTestQuery;
/*     */   }
/*     */   
/*     */   public synchronized boolean isPrivilegeSpawnedThreads() {
/* 324 */     return this.privilegeSpawnedThreads;
/*     */   }
/*     */   
/*     */   public synchronized void setPrivilegeSpawnedThreads(boolean privilegeSpawnedThreads) {
/* 328 */     this.privilegeSpawnedThreads = privilegeSpawnedThreads;
/*     */   }
/*     */   
/*     */   public synchronized int getPropertyCycle() {
/* 332 */     return this.propertyCycle;
/*     */   }
/*     */   
/*     */   public synchronized void setPropertyCycle(int propertyCycle) {
/* 336 */     this.propertyCycle = propertyCycle;
/*     */   }
/*     */   
/*     */   public synchronized int getStatementCacheNumDeferredCloseThreads() {
/* 340 */     return this.statementCacheNumDeferredCloseThreads;
/*     */   }
/*     */   
/*     */   public synchronized void setStatementCacheNumDeferredCloseThreads(int statementCacheNumDeferredCloseThreads) {
/* 344 */     this.statementCacheNumDeferredCloseThreads = statementCacheNumDeferredCloseThreads;
/*     */   }
/*     */   
/*     */   public synchronized boolean isTestConnectionOnCheckin() {
/* 348 */     return this.testConnectionOnCheckin;
/*     */   }
/*     */   
/*     */   public synchronized void setTestConnectionOnCheckin(boolean testConnectionOnCheckin) {
/* 352 */     this.testConnectionOnCheckin = testConnectionOnCheckin;
/*     */   }
/*     */   
/*     */   public synchronized boolean isTestConnectionOnCheckout() {
/* 356 */     return this.testConnectionOnCheckout;
/*     */   }
/*     */   
/*     */   public synchronized void setTestConnectionOnCheckout(boolean testConnectionOnCheckout) {
/* 360 */     this.testConnectionOnCheckout = testConnectionOnCheckout;
/*     */   }
/*     */   
/*     */   public synchronized int getUnreturnedConnectionTimeout() {
/* 364 */     return this.unreturnedConnectionTimeout;
/*     */   }
/*     */   
/*     */   public synchronized void setUnreturnedConnectionTimeout(int unreturnedConnectionTimeout) {
/* 368 */     this.unreturnedConnectionTimeout = unreturnedConnectionTimeout;
/*     */   }
/*     */   
/*     */   public synchronized String getUserOverridesAsString() {
/* 372 */     return this.userOverridesAsString;
/*     */   }
/*     */   
/*     */   public synchronized void setUserOverridesAsString(String userOverridesAsString) throws PropertyVetoException {
/* 376 */     String oldVal = this.userOverridesAsString;
/* 377 */     if (!eqOrBothNull(oldVal, userOverridesAsString))
/* 378 */       this.vcs.fireVetoableChange("userOverridesAsString", oldVal, userOverridesAsString); 
/* 379 */     this.userOverridesAsString = userOverridesAsString;
/*     */   }
/*     */   
/*     */   public synchronized boolean isUsesTraditionalReflectiveProxies() {
/* 383 */     return this.usesTraditionalReflectiveProxies;
/*     */   }
/*     */   
/*     */   public synchronized void setUsesTraditionalReflectiveProxies(boolean usesTraditionalReflectiveProxies) {
/* 387 */     this.usesTraditionalReflectiveProxies = usesTraditionalReflectiveProxies;
/*     */   }
/*     */   
/*     */   public void addPropertyChangeListener(PropertyChangeListener pcl) {
/* 391 */     this.pcs.addPropertyChangeListener(pcl);
/*     */   }
/*     */   public void addPropertyChangeListener(String propName, PropertyChangeListener pcl) {
/* 394 */     this.pcs.addPropertyChangeListener(propName, pcl);
/*     */   }
/*     */   public void removePropertyChangeListener(PropertyChangeListener pcl) {
/* 397 */     this.pcs.removePropertyChangeListener(pcl);
/*     */   }
/*     */   public void removePropertyChangeListener(String propName, PropertyChangeListener pcl) {
/* 400 */     this.pcs.removePropertyChangeListener(propName, pcl);
/*     */   }
/*     */   public PropertyChangeListener[] getPropertyChangeListeners() {
/* 403 */     return this.pcs.getPropertyChangeListeners();
/*     */   }
/*     */   public void addVetoableChangeListener(VetoableChangeListener vcl) {
/* 406 */     this.vcs.addVetoableChangeListener(vcl);
/*     */   }
/*     */   public void removeVetoableChangeListener(VetoableChangeListener vcl) {
/* 409 */     this.vcs.removeVetoableChangeListener(vcl);
/*     */   }
/*     */   public VetoableChangeListener[] getVetoableChangeListeners() {
/* 412 */     return this.vcs.getVetoableChangeListeners();
/*     */   }
/*     */   private boolean eqOrBothNull(Object a, Object b) {
/* 415 */     return (a == b || (a != null && a.equals(b)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream oos) throws IOException {
/* 425 */     oos.writeShort(1);
/* 426 */     oos.writeInt(this.acquireIncrement);
/* 427 */     oos.writeInt(this.acquireRetryAttempts);
/* 428 */     oos.writeInt(this.acquireRetryDelay);
/* 429 */     oos.writeBoolean(this.autoCommitOnClose);
/* 430 */     oos.writeObject(this.automaticTestTable);
/* 431 */     oos.writeBoolean(this.breakAfterAcquireFailure);
/* 432 */     oos.writeInt(this.checkoutTimeout);
/* 433 */     oos.writeObject(this.connectionCustomizerClassName);
/* 434 */     oos.writeObject(this.connectionTesterClassName);
/* 435 */     oos.writeObject(this.contextClassLoaderSource);
/* 436 */     oos.writeBoolean(this.debugUnreturnedConnectionStackTraces);
/* 437 */     oos.writeObject(this.factoryClassLocation);
/* 438 */     oos.writeBoolean(this.forceIgnoreUnresolvedTransactions);
/* 439 */     oos.writeObject(this.identityToken);
/* 440 */     oos.writeInt(this.idleConnectionTestPeriod);
/* 441 */     oos.writeInt(this.initialPoolSize);
/* 442 */     oos.writeInt(this.maxAdministrativeTaskTime);
/* 443 */     oos.writeInt(this.maxConnectionAge);
/* 444 */     oos.writeInt(this.maxIdleTime);
/* 445 */     oos.writeInt(this.maxIdleTimeExcessConnections);
/* 446 */     oos.writeInt(this.maxPoolSize);
/* 447 */     oos.writeInt(this.maxStatements);
/* 448 */     oos.writeInt(this.maxStatementsPerConnection);
/* 449 */     oos.writeInt(this.minPoolSize);
/*     */ 
/*     */     
/*     */     try {
/* 453 */       SerializableUtils.toByteArray(this.nestedDataSource);
/* 454 */       oos.writeObject(this.nestedDataSource);
/*     */     }
/* 456 */     catch (NotSerializableException nse) {
/*     */ 
/*     */       
/*     */       try {
/* 460 */         ReferenceIndirector referenceIndirector = new ReferenceIndirector();
/* 461 */         oos.writeObject(referenceIndirector.indirectForm(this.nestedDataSource));
/*     */       }
/* 463 */       catch (IOException indirectionIOException) {
/* 464 */         throw indirectionIOException;
/* 465 */       } catch (Exception indirectionOtherException) {
/* 466 */         throw new IOException("Problem indirectly serializing nestedDataSource: " + indirectionOtherException.toString());
/*     */       } 
/* 468 */     }  oos.writeObject(this.overrideDefaultPassword);
/* 469 */     oos.writeObject(this.overrideDefaultUser);
/* 470 */     oos.writeObject(this.preferredTestQuery);
/* 471 */     oos.writeBoolean(this.privilegeSpawnedThreads);
/* 472 */     oos.writeInt(this.propertyCycle);
/* 473 */     oos.writeInt(this.statementCacheNumDeferredCloseThreads);
/* 474 */     oos.writeBoolean(this.testConnectionOnCheckin);
/* 475 */     oos.writeBoolean(this.testConnectionOnCheckout);
/* 476 */     oos.writeInt(this.unreturnedConnectionTimeout);
/* 477 */     oos.writeObject(this.userOverridesAsString);
/* 478 */     oos.writeBoolean(this.usesTraditionalReflectiveProxies);
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/*     */     Object o;
/* 483 */     short version = ois.readShort();
/* 484 */     switch (version) {
/*     */       
/*     */       case 1:
/* 487 */         this.acquireIncrement = ois.readInt();
/* 488 */         this.acquireRetryAttempts = ois.readInt();
/* 489 */         this.acquireRetryDelay = ois.readInt();
/* 490 */         this.autoCommitOnClose = ois.readBoolean();
/* 491 */         this.automaticTestTable = (String)ois.readObject();
/* 492 */         this.breakAfterAcquireFailure = ois.readBoolean();
/* 493 */         this.checkoutTimeout = ois.readInt();
/* 494 */         this.connectionCustomizerClassName = (String)ois.readObject();
/* 495 */         this.connectionTesterClassName = (String)ois.readObject();
/* 496 */         this.contextClassLoaderSource = (String)ois.readObject();
/* 497 */         this.debugUnreturnedConnectionStackTraces = ois.readBoolean();
/* 498 */         this.factoryClassLocation = (String)ois.readObject();
/* 499 */         this.forceIgnoreUnresolvedTransactions = ois.readBoolean();
/* 500 */         this.identityToken = (String)ois.readObject();
/* 501 */         this.idleConnectionTestPeriod = ois.readInt();
/* 502 */         this.initialPoolSize = ois.readInt();
/* 503 */         this.maxAdministrativeTaskTime = ois.readInt();
/* 504 */         this.maxConnectionAge = ois.readInt();
/* 505 */         this.maxIdleTime = ois.readInt();
/* 506 */         this.maxIdleTimeExcessConnections = ois.readInt();
/* 507 */         this.maxPoolSize = ois.readInt();
/* 508 */         this.maxStatements = ois.readInt();
/* 509 */         this.maxStatementsPerConnection = ois.readInt();
/* 510 */         this.minPoolSize = ois.readInt();
/*     */ 
/*     */         
/* 513 */         o = ois.readObject();
/* 514 */         if (o instanceof IndirectlySerialized) o = ((IndirectlySerialized)o).getObject(); 
/* 515 */         this.nestedDataSource = (DataSource)o;
/*     */         
/* 517 */         this.overrideDefaultPassword = (String)ois.readObject();
/* 518 */         this.overrideDefaultUser = (String)ois.readObject();
/* 519 */         this.preferredTestQuery = (String)ois.readObject();
/* 520 */         this.privilegeSpawnedThreads = ois.readBoolean();
/* 521 */         this.propertyCycle = ois.readInt();
/* 522 */         this.statementCacheNumDeferredCloseThreads = ois.readInt();
/* 523 */         this.testConnectionOnCheckin = ois.readBoolean();
/* 524 */         this.testConnectionOnCheckout = ois.readBoolean();
/* 525 */         this.unreturnedConnectionTimeout = ois.readInt();
/* 526 */         this.userOverridesAsString = (String)ois.readObject();
/* 527 */         this.usesTraditionalReflectiveProxies = ois.readBoolean();
/* 528 */         this.pcs = new PropertyChangeSupport(this);
/* 529 */         this.vcs = new VetoableChangeSupport(this);
/*     */         return;
/*     */     } 
/* 532 */     throw new IOException("Unsupported Serialized Version: " + version);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getParentLogger() throws SQLFeatureNotSupportedException {
/* 538 */     return C3P0ImplUtils.PARENT_LOGGER;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 542 */     StringBuffer sb = new StringBuffer();
/* 543 */     sb.append(super.toString());
/* 544 */     sb.append(" [ ");
/* 545 */     sb.append("acquireIncrement -> " + this.acquireIncrement);
/* 546 */     sb.append(", ");
/* 547 */     sb.append("acquireRetryAttempts -> " + this.acquireRetryAttempts);
/* 548 */     sb.append(", ");
/* 549 */     sb.append("acquireRetryDelay -> " + this.acquireRetryDelay);
/* 550 */     sb.append(", ");
/* 551 */     sb.append("autoCommitOnClose -> " + this.autoCommitOnClose);
/* 552 */     sb.append(", ");
/* 553 */     sb.append("automaticTestTable -> " + this.automaticTestTable);
/* 554 */     sb.append(", ");
/* 555 */     sb.append("breakAfterAcquireFailure -> " + this.breakAfterAcquireFailure);
/* 556 */     sb.append(", ");
/* 557 */     sb.append("checkoutTimeout -> " + this.checkoutTimeout);
/* 558 */     sb.append(", ");
/* 559 */     sb.append("connectionCustomizerClassName -> " + this.connectionCustomizerClassName);
/* 560 */     sb.append(", ");
/* 561 */     sb.append("connectionTesterClassName -> " + this.connectionTesterClassName);
/* 562 */     sb.append(", ");
/* 563 */     sb.append("contextClassLoaderSource -> " + this.contextClassLoaderSource);
/* 564 */     sb.append(", ");
/* 565 */     sb.append("debugUnreturnedConnectionStackTraces -> " + this.debugUnreturnedConnectionStackTraces);
/* 566 */     sb.append(", ");
/* 567 */     sb.append("factoryClassLocation -> " + this.factoryClassLocation);
/* 568 */     sb.append(", ");
/* 569 */     sb.append("forceIgnoreUnresolvedTransactions -> " + this.forceIgnoreUnresolvedTransactions);
/* 570 */     sb.append(", ");
/* 571 */     sb.append("identityToken -> " + this.identityToken);
/* 572 */     sb.append(", ");
/* 573 */     sb.append("idleConnectionTestPeriod -> " + this.idleConnectionTestPeriod);
/* 574 */     sb.append(", ");
/* 575 */     sb.append("initialPoolSize -> " + this.initialPoolSize);
/* 576 */     sb.append(", ");
/* 577 */     sb.append("maxAdministrativeTaskTime -> " + this.maxAdministrativeTaskTime);
/* 578 */     sb.append(", ");
/* 579 */     sb.append("maxConnectionAge -> " + this.maxConnectionAge);
/* 580 */     sb.append(", ");
/* 581 */     sb.append("maxIdleTime -> " + this.maxIdleTime);
/* 582 */     sb.append(", ");
/* 583 */     sb.append("maxIdleTimeExcessConnections -> " + this.maxIdleTimeExcessConnections);
/* 584 */     sb.append(", ");
/* 585 */     sb.append("maxPoolSize -> " + this.maxPoolSize);
/* 586 */     sb.append(", ");
/* 587 */     sb.append("maxStatements -> " + this.maxStatements);
/* 588 */     sb.append(", ");
/* 589 */     sb.append("maxStatementsPerConnection -> " + this.maxStatementsPerConnection);
/* 590 */     sb.append(", ");
/* 591 */     sb.append("minPoolSize -> " + this.minPoolSize);
/* 592 */     sb.append(", ");
/* 593 */     sb.append("nestedDataSource -> " + this.nestedDataSource);
/* 594 */     sb.append(", ");
/* 595 */     sb.append("preferredTestQuery -> " + this.preferredTestQuery);
/* 596 */     sb.append(", ");
/* 597 */     sb.append("privilegeSpawnedThreads -> " + this.privilegeSpawnedThreads);
/* 598 */     sb.append(", ");
/* 599 */     sb.append("propertyCycle -> " + this.propertyCycle);
/* 600 */     sb.append(", ");
/* 601 */     sb.append("statementCacheNumDeferredCloseThreads -> " + this.statementCacheNumDeferredCloseThreads);
/* 602 */     sb.append(", ");
/* 603 */     sb.append("testConnectionOnCheckin -> " + this.testConnectionOnCheckin);
/* 604 */     sb.append(", ");
/* 605 */     sb.append("testConnectionOnCheckout -> " + this.testConnectionOnCheckout);
/* 606 */     sb.append(", ");
/* 607 */     sb.append("unreturnedConnectionTimeout -> " + this.unreturnedConnectionTimeout);
/* 608 */     sb.append(", ");
/* 609 */     sb.append("usesTraditionalReflectiveProxies -> " + this.usesTraditionalReflectiveProxies);
/*     */     
/* 611 */     String extraToStringInfo = extraToStringInfo();
/* 612 */     if (extraToStringInfo != null)
/* 613 */       sb.append(extraToStringInfo); 
/* 614 */     sb.append(" ]");
/* 615 */     return sb.toString();
/*     */   }
/*     */   
/*     */   protected String extraToStringInfo() {
/* 619 */     return null;
/*     */   }
/* 621 */   static final JavaBeanReferenceMaker referenceMaker = new JavaBeanReferenceMaker();
/*     */ 
/*     */   
/*     */   static {
/* 625 */     referenceMaker.setFactoryClassName("com.mchange.v2.c3p0.impl.C3P0JavaBeanObjectFactory");
/* 626 */     referenceMaker.addReferenceProperty("acquireIncrement");
/* 627 */     referenceMaker.addReferenceProperty("acquireRetryAttempts");
/* 628 */     referenceMaker.addReferenceProperty("acquireRetryDelay");
/* 629 */     referenceMaker.addReferenceProperty("autoCommitOnClose");
/* 630 */     referenceMaker.addReferenceProperty("automaticTestTable");
/* 631 */     referenceMaker.addReferenceProperty("breakAfterAcquireFailure");
/* 632 */     referenceMaker.addReferenceProperty("checkoutTimeout");
/* 633 */     referenceMaker.addReferenceProperty("connectionCustomizerClassName");
/* 634 */     referenceMaker.addReferenceProperty("connectionTesterClassName");
/* 635 */     referenceMaker.addReferenceProperty("contextClassLoaderSource");
/* 636 */     referenceMaker.addReferenceProperty("debugUnreturnedConnectionStackTraces");
/* 637 */     referenceMaker.addReferenceProperty("factoryClassLocation");
/* 638 */     referenceMaker.addReferenceProperty("forceIgnoreUnresolvedTransactions");
/* 639 */     referenceMaker.addReferenceProperty("identityToken");
/* 640 */     referenceMaker.addReferenceProperty("idleConnectionTestPeriod");
/* 641 */     referenceMaker.addReferenceProperty("initialPoolSize");
/* 642 */     referenceMaker.addReferenceProperty("maxAdministrativeTaskTime");
/* 643 */     referenceMaker.addReferenceProperty("maxConnectionAge");
/* 644 */     referenceMaker.addReferenceProperty("maxIdleTime");
/* 645 */     referenceMaker.addReferenceProperty("maxIdleTimeExcessConnections");
/* 646 */     referenceMaker.addReferenceProperty("maxPoolSize");
/* 647 */     referenceMaker.addReferenceProperty("maxStatements");
/* 648 */     referenceMaker.addReferenceProperty("maxStatementsPerConnection");
/* 649 */     referenceMaker.addReferenceProperty("minPoolSize");
/* 650 */     referenceMaker.addReferenceProperty("nestedDataSource");
/* 651 */     referenceMaker.addReferenceProperty("overrideDefaultPassword");
/* 652 */     referenceMaker.addReferenceProperty("overrideDefaultUser");
/* 653 */     referenceMaker.addReferenceProperty("preferredTestQuery");
/* 654 */     referenceMaker.addReferenceProperty("privilegeSpawnedThreads");
/* 655 */     referenceMaker.addReferenceProperty("propertyCycle");
/* 656 */     referenceMaker.addReferenceProperty("statementCacheNumDeferredCloseThreads");
/* 657 */     referenceMaker.addReferenceProperty("testConnectionOnCheckin");
/* 658 */     referenceMaker.addReferenceProperty("testConnectionOnCheckout");
/* 659 */     referenceMaker.addReferenceProperty("unreturnedConnectionTimeout");
/* 660 */     referenceMaker.addReferenceProperty("userOverridesAsString");
/* 661 */     referenceMaker.addReferenceProperty("usesTraditionalReflectiveProxies");
/*     */   }
/*     */ 
/*     */   
/*     */   public Reference getReference() throws NamingException {
/* 666 */     return referenceMaker.createReference(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WrapperConnectionPoolDataSourceBase(boolean autoregister) {
/* 674 */     if (autoregister) {
/*     */       
/* 676 */       this.identityToken = C3P0ImplUtils.allocateIdentityToken(this);
/* 677 */       C3P0Registry.reregister(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   private WrapperConnectionPoolDataSourceBase() {}
/*     */   
/*     */   protected abstract PooledConnection getPooledConnection(ConnectionCustomizer paramConnectionCustomizer, String paramString) throws SQLException;
/*     */   
/*     */   protected abstract PooledConnection getPooledConnection(String paramString1, String paramString2, ConnectionCustomizer paramConnectionCustomizer, String paramString3) throws SQLException;
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/WrapperConnectionPoolDataSourceBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */