/*     */ package com.mchange.v2.c3p0;
/*     */ 
/*     */ import com.mchange.v2.beans.BeansUtils;
/*     */ import com.mchange.v2.c3p0.impl.AbstractPoolBackedDataSource;
/*     */ import com.mchange.v2.lang.ObjectUtils;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyVetoException;
/*     */ import java.beans.VetoableChangeListener;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import javax.naming.Referenceable;
/*     */ import javax.sql.DataSource;
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
/*     */ 
/*     */ 
/*     */ public abstract class AbstractComboPooledDataSource
/*     */   extends AbstractPoolBackedDataSource
/*     */   implements PooledDataSource, Serializable, Referenceable
/*     */ {
/*  57 */   static final MLogger logger = MLog.getLogger(AbstractComboPooledDataSource.class);
/*     */   
/*  59 */   static final Set TO_STRING_IGNORE_PROPS = new HashSet(Arrays.asList((Object[])new String[] { "connection", "lastAcquisitionFailureDefaultUser", "lastCheckinFailureDefaultUser", "lastCheckoutFailureDefaultUser", "lastConnectionTestFailureDefaultUser", "lastIdleTestFailureDefaultUser", "logWriter", "loginTimeout", "numBusyConnections", "numBusyConnectionsAllUsers", "numBusyConnectionsDefaultUser", "numConnections", "numConnectionsAllUsers", "numConnectionsDefaultUser", "numFailedCheckinsDefaultUser", "numFailedCheckoutsDefaultUser", "numFailedIdleTestsDefaultUser", "numIdleConnections", "numIdleConnectionsAllUsers", "numThreadsAwaitingCheckoutDefaultUser", "numIdleConnectionsDefaultUser", "numUnclosedOrphanedConnections", "numUnclosedOrphanedConnectionsAllUsers", "numUnclosedOrphanedConnectionsDefaultUser", "numUserPools", "effectivePropertyCycleDefaultUser", "parentLogger", "startTimeMillisDefaultUser", "statementCacheNumCheckedOutDefaultUser", "statementCacheNumCheckedOutStatementsAllUsers", "statementCacheNumConnectionsWithCachedStatementsAllUsers", "statementCacheNumConnectionsWithCachedStatementsDefaultUser", "statementCacheNumStatementsAllUsers", "statementCacheNumStatementsDefaultUser", "statementDestroyerNumConnectionsInUseAllUsers", "statementDestroyerNumConnectionsWithDeferredDestroyStatementsAllUsers", "statementDestroyerNumDeferredDestroyStatementsAllUsers", "statementDestroyerNumConnectionsInUseDefaultUser", "statementDestroyerNumConnectionsWithDeferredDestroyStatementsDefaultUser", "statementDestroyerNumDeferredDestroyStatementsDefaultUser", "statementDestroyerNumThreads", "statementDestroyerNumActiveThreads", "statementDestroyerNumIdleThreads", "statementDestroyerNumTasksPending", "threadPoolSize", "threadPoolNumActiveThreads", "threadPoolNumIdleThreads", "threadPoolNumTasksPending", "threadPoolStackTraces", "threadPoolStatus", "overrideDefaultUser", "overrideDefaultPassword", "password", "reference", "upTimeMillisDefaultUser", "user", "userOverridesAsString", "allUsers", "connectionPoolDataSource", "propertyChangeListeners", "vetoableChangeListeners" }));
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
/*     */   transient DriverManagerDataSource dmds;
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
/*     */   transient WrapperConnectionPoolDataSource wcpds;
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
/*     */   private static final long serialVersionUID = 1L;
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
/*     */   private static final short VERSION = 1;
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
/*     */   private static boolean diff(int a, int b) {
/* 125 */     return (a != b);
/* 126 */   } private static boolean diff(boolean a, boolean b) { return (a != b); } private static boolean diff(Object a, Object b) {
/* 127 */     return !ObjectUtils.eqOrBothNull(a, b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractComboPooledDataSource() {
/* 138 */     this(true);
/*     */   }
/*     */   
/*     */   public AbstractComboPooledDataSource(boolean autoregister) {
/* 142 */     super(autoregister);
/*     */ 
/*     */ 
/*     */     
/* 146 */     this.dmds = new DriverManagerDataSource();
/* 147 */     this.wcpds = new WrapperConnectionPoolDataSource();
/*     */     
/* 149 */     this.wcpds.setNestedDataSource(this.dmds);
/*     */     
/*     */     try {
/* 152 */       setConnectionPoolDataSource(this.wcpds);
/* 153 */     } catch (PropertyVetoException e) {
/*     */       
/* 155 */       logger.log(MLevel.WARNING, "Hunh??? This can't happen. We haven't set up any listeners to veto the property change yet!", e);
/* 156 */       throw new RuntimeException("Hunh??? This can't happen. We haven't set up any listeners to veto the property change yet! " + e);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 161 */     setUpPropertyEvents();
/*     */   }
/*     */ 
/*     */   
/*     */   private void setUpPropertyEvents() {
/* 166 */     VetoableChangeListener wcpdsConsistencyEnforcer = new VetoableChangeListener()
/*     */       {
/*     */         
/*     */         public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException
/*     */         {
/* 171 */           String propName = evt.getPropertyName();
/* 172 */           Object val = evt.getNewValue();
/*     */           
/* 174 */           if ("connectionPoolDataSource".equals(propName))
/*     */           {
/* 176 */             if (val instanceof WrapperConnectionPoolDataSource) {
/*     */               
/* 178 */               DataSource nested = ((WrapperConnectionPoolDataSource)val).getNestedDataSource();
/* 179 */               if (!(nested instanceof DriverManagerDataSource)) {
/* 180 */                 throw new PropertyVetoException(getClass().getName() + " requires that its unpooled DataSource " + " be set at all times, and that it be a" + " com.mchange.v2.c3p0.DriverManagerDataSource. Bad: " + nested, evt);
/*     */               }
/*     */             }
/*     */             else {
/*     */               
/* 185 */               throw new PropertyVetoException(getClass().getName() + " requires that its ConnectionPoolDataSource " + " be set at all times, and that it be a" + " com.mchange.v2.c3p0.WrapperConnectionPoolDataSource. Bad: " + val, evt);
/*     */             } 
/*     */           }
/*     */         }
/*     */       };
/*     */     
/* 191 */     addVetoableChangeListener(wcpdsConsistencyEnforcer);
/*     */     
/* 193 */     PropertyChangeListener wcpdsStateUpdater = new PropertyChangeListener()
/*     */       {
/*     */         public void propertyChange(PropertyChangeEvent evt)
/*     */         {
/* 197 */           String propName = evt.getPropertyName();
/* 198 */           Object val = evt.getNewValue();
/*     */           
/* 200 */           if ("connectionPoolDataSource".equals(propName))
/* 201 */             AbstractComboPooledDataSource.this.updateLocalVarsFromCpdsProp(); 
/*     */         }
/*     */       };
/* 204 */     addPropertyChangeListener(wcpdsStateUpdater);
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateLocalVarsFromCpdsProp() {
/* 209 */     this.wcpds = (WrapperConnectionPoolDataSource)getConnectionPoolDataSource();
/* 210 */     this.dmds = (DriverManagerDataSource)this.wcpds.getNestedDataSource();
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractComboPooledDataSource(String configName) {
/* 215 */     this();
/* 216 */     initializeNamedConfig(configName, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 228 */     return this.dmds.getDescription();
/*     */   }
/*     */   public void setDescription(String description) {
/* 231 */     this.dmds.setDescription(description);
/*     */   }
/*     */   public String getDriverClass() {
/* 234 */     return this.dmds.getDriverClass();
/*     */   }
/*     */   
/*     */   public void setDriverClass(String driverClass) throws PropertyVetoException {
/* 238 */     this.dmds.setDriverClass(driverClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isForceUseNamedDriverClass() {
/* 243 */     return this.dmds.isForceUseNamedDriverClass();
/*     */   }
/*     */   
/*     */   public void setForceUseNamedDriverClass(boolean forceUseNamedDriverClass) {
/* 247 */     this.dmds.setForceUseNamedDriverClass(forceUseNamedDriverClass);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getJdbcUrl() {
/* 253 */     return this.dmds.getJdbcUrl();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setJdbcUrl(String jdbcUrl) {
/* 258 */     if (diff(this.dmds.getJdbcUrl(), jdbcUrl)) {
/*     */       
/* 260 */       this.dmds.setJdbcUrl(jdbcUrl);
/* 261 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Properties getProperties() {
/* 271 */     return this.dmds.getProperties();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setProperties(Properties properties) {
/* 276 */     if (diff(this.dmds.getProperties(), properties)) {
/*     */ 
/*     */       
/* 279 */       this.dmds.setProperties(properties);
/* 280 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUser() {
/* 286 */     return this.dmds.getUser();
/*     */   }
/*     */   
/*     */   public void setUser(String user) {
/* 290 */     if (diff(this.dmds.getUser(), user)) {
/*     */       
/* 292 */       this.dmds.setUser(user);
/* 293 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getPassword() {
/* 298 */     return this.dmds.getPassword();
/*     */   }
/*     */   
/*     */   public void setPassword(String password) {
/* 302 */     if (diff(this.dmds.getPassword(), password)) {
/*     */       
/* 304 */       this.dmds.setPassword(password);
/* 305 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCheckoutTimeout() {
/* 311 */     return this.wcpds.getCheckoutTimeout();
/*     */   }
/*     */   
/*     */   public void setCheckoutTimeout(int checkoutTimeout) {
/* 315 */     if (diff(this.wcpds.getCheckoutTimeout(), checkoutTimeout)) {
/*     */       
/* 317 */       this.wcpds.setCheckoutTimeout(checkoutTimeout);
/* 318 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getAcquireIncrement() {
/* 323 */     return this.wcpds.getAcquireIncrement();
/*     */   }
/*     */   
/*     */   public void setAcquireIncrement(int acquireIncrement) {
/* 327 */     if (diff(this.wcpds.getAcquireIncrement(), acquireIncrement)) {
/*     */       
/* 329 */       this.wcpds.setAcquireIncrement(acquireIncrement);
/* 330 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getAcquireRetryAttempts() {
/* 335 */     return this.wcpds.getAcquireRetryAttempts();
/*     */   }
/*     */   
/*     */   public void setAcquireRetryAttempts(int acquireRetryAttempts) {
/* 339 */     if (diff(this.wcpds.getAcquireRetryAttempts(), acquireRetryAttempts)) {
/*     */       
/* 341 */       this.wcpds.setAcquireRetryAttempts(acquireRetryAttempts);
/* 342 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getAcquireRetryDelay() {
/* 347 */     return this.wcpds.getAcquireRetryDelay();
/*     */   }
/*     */   
/*     */   public void setAcquireRetryDelay(int acquireRetryDelay) {
/* 351 */     if (diff(this.wcpds.getAcquireRetryDelay(), acquireRetryDelay)) {
/*     */       
/* 353 */       this.wcpds.setAcquireRetryDelay(acquireRetryDelay);
/* 354 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isAutoCommitOnClose() {
/* 359 */     return this.wcpds.isAutoCommitOnClose();
/*     */   }
/*     */   
/*     */   public void setAutoCommitOnClose(boolean autoCommitOnClose) {
/* 363 */     if (diff(this.wcpds.isAutoCommitOnClose(), autoCommitOnClose)) {
/*     */       
/* 365 */       this.wcpds.setAutoCommitOnClose(autoCommitOnClose);
/* 366 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getContextClassLoaderSource() {
/* 371 */     return this.wcpds.getContextClassLoaderSource();
/*     */   }
/*     */   
/*     */   public void setContextClassLoaderSource(String contextClassLoaderSource) throws PropertyVetoException {
/* 375 */     if (diff(this.wcpds.getContextClassLoaderSource(), contextClassLoaderSource)) {
/*     */       
/* 377 */       this.wcpds.setContextClassLoaderSource(contextClassLoaderSource);
/* 378 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getConnectionTesterClassName() {
/* 383 */     return this.wcpds.getConnectionTesterClassName();
/*     */   }
/*     */   
/*     */   public void setConnectionTesterClassName(String connectionTesterClassName) throws PropertyVetoException {
/* 387 */     if (diff(this.wcpds.getConnectionTesterClassName(), connectionTesterClassName)) {
/*     */       
/* 389 */       this.wcpds.setConnectionTesterClassName(connectionTesterClassName);
/* 390 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getAutomaticTestTable() {
/* 395 */     return this.wcpds.getAutomaticTestTable();
/*     */   }
/*     */   
/*     */   public void setAutomaticTestTable(String automaticTestTable) {
/* 399 */     if (diff(this.wcpds.getAutomaticTestTable(), automaticTestTable)) {
/*     */       
/* 401 */       this.wcpds.setAutomaticTestTable(automaticTestTable);
/* 402 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isForceIgnoreUnresolvedTransactions() {
/* 407 */     return this.wcpds.isForceIgnoreUnresolvedTransactions();
/*     */   }
/*     */   
/*     */   public void setForceIgnoreUnresolvedTransactions(boolean forceIgnoreUnresolvedTransactions) {
/* 411 */     if (diff(this.wcpds.isForceIgnoreUnresolvedTransactions(), forceIgnoreUnresolvedTransactions)) {
/*     */       
/* 413 */       this.wcpds.setForceIgnoreUnresolvedTransactions(forceIgnoreUnresolvedTransactions);
/* 414 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isPrivilegeSpawnedThreads() {
/* 419 */     return this.wcpds.isPrivilegeSpawnedThreads();
/*     */   }
/*     */   
/*     */   public void setPrivilegeSpawnedThreads(boolean privilegeSpawnedThreads) {
/* 423 */     if (diff(this.wcpds.isPrivilegeSpawnedThreads(), privilegeSpawnedThreads)) {
/*     */       
/* 425 */       this.wcpds.setPrivilegeSpawnedThreads(privilegeSpawnedThreads);
/* 426 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getIdleConnectionTestPeriod() {
/* 431 */     return this.wcpds.getIdleConnectionTestPeriod();
/*     */   }
/*     */   
/*     */   public void setIdleConnectionTestPeriod(int idleConnectionTestPeriod) {
/* 435 */     if (diff(this.wcpds.getIdleConnectionTestPeriod(), idleConnectionTestPeriod)) {
/*     */       
/* 437 */       this.wcpds.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
/* 438 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getInitialPoolSize() {
/* 443 */     return this.wcpds.getInitialPoolSize();
/*     */   }
/*     */   
/*     */   public void setInitialPoolSize(int initialPoolSize) {
/* 447 */     if (diff(this.wcpds.getInitialPoolSize(), initialPoolSize)) {
/*     */       
/* 449 */       this.wcpds.setInitialPoolSize(initialPoolSize);
/* 450 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getMaxIdleTime() {
/* 455 */     return this.wcpds.getMaxIdleTime();
/*     */   }
/*     */   
/*     */   public void setMaxIdleTime(int maxIdleTime) {
/* 459 */     if (diff(this.wcpds.getMaxIdleTime(), maxIdleTime)) {
/*     */       
/* 461 */       this.wcpds.setMaxIdleTime(maxIdleTime);
/* 462 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getMaxPoolSize() {
/* 467 */     return this.wcpds.getMaxPoolSize();
/*     */   }
/*     */   
/*     */   public void setMaxPoolSize(int maxPoolSize) {
/* 471 */     if (diff(this.wcpds.getMaxPoolSize(), maxPoolSize)) {
/*     */       
/* 473 */       this.wcpds.setMaxPoolSize(maxPoolSize);
/* 474 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getMaxStatements() {
/* 479 */     return this.wcpds.getMaxStatements();
/*     */   }
/*     */   
/*     */   public void setMaxStatements(int maxStatements) {
/* 483 */     if (diff(this.wcpds.getMaxStatements(), maxStatements)) {
/*     */       
/* 485 */       this.wcpds.setMaxStatements(maxStatements);
/* 486 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getMaxStatementsPerConnection() {
/* 491 */     return this.wcpds.getMaxStatementsPerConnection();
/*     */   }
/*     */   
/*     */   public void setMaxStatementsPerConnection(int maxStatementsPerConnection) {
/* 495 */     if (diff(this.wcpds.getMaxStatementsPerConnection(), maxStatementsPerConnection)) {
/*     */       
/* 497 */       this.wcpds.setMaxStatementsPerConnection(maxStatementsPerConnection);
/* 498 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getMinPoolSize() {
/* 503 */     return this.wcpds.getMinPoolSize();
/*     */   }
/*     */   
/*     */   public void setMinPoolSize(int minPoolSize) {
/* 507 */     if (diff(this.wcpds.getMinPoolSize(), minPoolSize)) {
/*     */       
/* 509 */       this.wcpds.setMinPoolSize(minPoolSize);
/* 510 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getOverrideDefaultUser() {
/* 515 */     return this.wcpds.getOverrideDefaultUser();
/*     */   }
/*     */   
/*     */   public void setOverrideDefaultUser(String overrideDefaultUser) {
/* 519 */     if (diff(this.wcpds.getOverrideDefaultUser(), overrideDefaultUser)) {
/*     */       
/* 521 */       this.wcpds.setOverrideDefaultUser(overrideDefaultUser);
/* 522 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getOverrideDefaultPassword() {
/* 527 */     return this.wcpds.getOverrideDefaultPassword();
/*     */   }
/*     */   
/*     */   public void setOverrideDefaultPassword(String overrideDefaultPassword) {
/* 531 */     if (diff(this.wcpds.getOverrideDefaultPassword(), overrideDefaultPassword)) {
/*     */       
/* 533 */       this.wcpds.setOverrideDefaultPassword(overrideDefaultPassword);
/* 534 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getPropertyCycle() {
/* 539 */     return this.wcpds.getPropertyCycle();
/*     */   }
/*     */   
/*     */   public void setPropertyCycle(int propertyCycle) {
/* 543 */     if (diff(this.wcpds.getPropertyCycle(), propertyCycle)) {
/*     */       
/* 545 */       this.wcpds.setPropertyCycle(propertyCycle);
/* 546 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isBreakAfterAcquireFailure() {
/* 551 */     return this.wcpds.isBreakAfterAcquireFailure();
/*     */   }
/*     */   
/*     */   public void setBreakAfterAcquireFailure(boolean breakAfterAcquireFailure) {
/* 555 */     if (diff(this.wcpds.isBreakAfterAcquireFailure(), breakAfterAcquireFailure)) {
/*     */       
/* 557 */       this.wcpds.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
/* 558 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isTestConnectionOnCheckout() {
/* 563 */     return this.wcpds.isTestConnectionOnCheckout();
/*     */   }
/*     */   
/*     */   public void setTestConnectionOnCheckout(boolean testConnectionOnCheckout) {
/* 567 */     if (diff(this.wcpds.isTestConnectionOnCheckout(), testConnectionOnCheckout)) {
/*     */       
/* 569 */       this.wcpds.setTestConnectionOnCheckout(testConnectionOnCheckout);
/* 570 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isTestConnectionOnCheckin() {
/* 575 */     return this.wcpds.isTestConnectionOnCheckin();
/*     */   }
/*     */   
/*     */   public void setTestConnectionOnCheckin(boolean testConnectionOnCheckin) {
/* 579 */     if (diff(this.wcpds.isTestConnectionOnCheckin(), testConnectionOnCheckin)) {
/*     */       
/* 581 */       this.wcpds.setTestConnectionOnCheckin(testConnectionOnCheckin);
/* 582 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isUsesTraditionalReflectiveProxies() {
/* 587 */     return this.wcpds.isUsesTraditionalReflectiveProxies();
/*     */   }
/*     */   
/*     */   public void setUsesTraditionalReflectiveProxies(boolean usesTraditionalReflectiveProxies) {
/* 591 */     if (diff(this.wcpds.isUsesTraditionalReflectiveProxies(), usesTraditionalReflectiveProxies)) {
/*     */       
/* 593 */       this.wcpds.setUsesTraditionalReflectiveProxies(usesTraditionalReflectiveProxies);
/* 594 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getPreferredTestQuery() {
/* 599 */     return this.wcpds.getPreferredTestQuery();
/*     */   }
/*     */   
/*     */   public void setPreferredTestQuery(String preferredTestQuery) {
/* 603 */     if (diff(this.wcpds.getPreferredTestQuery(), preferredTestQuery)) {
/*     */       
/* 605 */       this.wcpds.setPreferredTestQuery(preferredTestQuery);
/* 606 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getMaxAdministrativeTaskTime() {
/* 611 */     return this.wcpds.getMaxAdministrativeTaskTime();
/*     */   }
/*     */   
/*     */   public void setMaxAdministrativeTaskTime(int maxAdministrativeTaskTime) {
/* 615 */     if (diff(this.wcpds.getMaxAdministrativeTaskTime(), maxAdministrativeTaskTime)) {
/*     */       
/* 617 */       this.wcpds.setMaxAdministrativeTaskTime(maxAdministrativeTaskTime);
/* 618 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getMaxIdleTimeExcessConnections() {
/* 623 */     return this.wcpds.getMaxIdleTimeExcessConnections();
/*     */   }
/*     */   
/*     */   public void setMaxIdleTimeExcessConnections(int maxIdleTimeExcessConnections) {
/* 627 */     if (diff(this.wcpds.getMaxIdleTimeExcessConnections(), maxIdleTimeExcessConnections)) {
/*     */       
/* 629 */       this.wcpds.setMaxIdleTimeExcessConnections(maxIdleTimeExcessConnections);
/* 630 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getMaxConnectionAge() {
/* 635 */     return this.wcpds.getMaxConnectionAge();
/*     */   }
/*     */   
/*     */   public void setMaxConnectionAge(int maxConnectionAge) {
/* 639 */     if (diff(this.wcpds.getMaxConnectionAge(), maxConnectionAge)) {
/*     */       
/* 641 */       this.wcpds.setMaxConnectionAge(maxConnectionAge);
/* 642 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getConnectionCustomizerClassName() {
/* 647 */     return this.wcpds.getConnectionCustomizerClassName();
/*     */   }
/*     */   
/*     */   public void setConnectionCustomizerClassName(String connectionCustomizerClassName) {
/* 651 */     if (diff(this.wcpds.getConnectionCustomizerClassName(), connectionCustomizerClassName)) {
/*     */       
/* 653 */       this.wcpds.setConnectionCustomizerClassName(connectionCustomizerClassName);
/* 654 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getUnreturnedConnectionTimeout() {
/* 659 */     return this.wcpds.getUnreturnedConnectionTimeout();
/*     */   }
/*     */   
/*     */   public void setUnreturnedConnectionTimeout(int unreturnedConnectionTimeout) {
/* 663 */     if (diff(this.wcpds.getUnreturnedConnectionTimeout(), unreturnedConnectionTimeout)) {
/*     */       
/* 665 */       this.wcpds.setUnreturnedConnectionTimeout(unreturnedConnectionTimeout);
/* 666 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getUserOverridesAsString() {
/* 671 */     return this.wcpds.getUserOverridesAsString();
/*     */   }
/*     */   
/*     */   public void setUserOverridesAsString(String uoas) throws PropertyVetoException {
/* 675 */     if (diff(this.wcpds.getUserOverridesAsString(), uoas)) {
/*     */       
/* 677 */       this.wcpds.setUserOverridesAsString(uoas);
/* 678 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Map getUserOverrides() {
/* 683 */     return this.wcpds.getUserOverrides();
/*     */   }
/*     */   public boolean isDebugUnreturnedConnectionStackTraces() {
/* 686 */     return this.wcpds.isDebugUnreturnedConnectionStackTraces();
/*     */   }
/*     */   
/*     */   public void setDebugUnreturnedConnectionStackTraces(boolean debugUnreturnedConnectionStackTraces) {
/* 690 */     if (diff(this.wcpds.isDebugUnreturnedConnectionStackTraces(), debugUnreturnedConnectionStackTraces)) {
/*     */       
/* 692 */       this.wcpds.setDebugUnreturnedConnectionStackTraces(debugUnreturnedConnectionStackTraces);
/* 693 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getStatementCacheNumDeferredCloseThreads() {
/* 698 */     return this.wcpds.getStatementCacheNumDeferredCloseThreads();
/*     */   }
/*     */   
/*     */   public void setStatementCacheNumDeferredCloseThreads(int statementCacheNumDeferredCloseThreads) {
/* 702 */     if (diff(this.wcpds.getStatementCacheNumDeferredCloseThreads(), statementCacheNumDeferredCloseThreads)) {
/*     */       
/* 704 */       this.wcpds.setStatementCacheNumDeferredCloseThreads(statementCacheNumDeferredCloseThreads);
/* 705 */       resetPoolManager(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFactoryClassLocation() {
/* 711 */     return super.getFactoryClassLocation();
/*     */   }
/*     */   
/*     */   public void setFactoryClassLocation(String factoryClassLocation) {
/* 715 */     if (diff(this.dmds.getFactoryClassLocation(), factoryClassLocation) || diff(this.wcpds.getFactoryClassLocation(), factoryClassLocation) || diff(super.getFactoryClassLocation(), factoryClassLocation)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 721 */       this.dmds.setFactoryClassLocation(factoryClassLocation);
/* 722 */       this.wcpds.setFactoryClassLocation(factoryClassLocation);
/* 723 */       super.setFactoryClassLocation(factoryClassLocation);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 731 */     StringBuffer sb = new StringBuffer(512);
/* 732 */     sb.append(getClass().getName());
/* 733 */     sb.append(" [ "); try {
/* 734 */       BeansUtils.appendPropNamesAndValues(sb, this, TO_STRING_IGNORE_PROPS);
/* 735 */     } catch (Exception e) {
/*     */       
/* 737 */       sb.append(e.toString());
/*     */     } 
/*     */     
/* 740 */     sb.append(" ]");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 746 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream oos) throws IOException {
/* 755 */     oos.writeShort(1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 760 */     short version = ois.readShort();
/* 761 */     switch (version) {
/*     */       
/*     */       case 1:
/* 764 */         updateLocalVarsFromCpdsProp();
/* 765 */         setUpPropertyEvents();
/*     */         return;
/*     */     } 
/* 768 */     throw new IOException("Unsupported Serialized Version: " + version);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWrapperFor(Class<?> iface) throws SQLException {
/* 775 */     return (iface == DataSource.class || iface.isAssignableFrom(DataSource.class));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T unwrap(Class<T> iface) throws SQLException {
/* 780 */     if (isWrapperFor(iface)) {
/* 781 */       return (T)this.dmds;
/*     */     }
/* 783 */     throw new SQLException(this + " is not a Wrapper for " + iface.getName());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/AbstractComboPooledDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */