/*     */ package com.mchange.v2.c3p0.jboss;
/*     */ 
/*     */ import com.mchange.v2.c3p0.ComboPooledDataSource;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import java.beans.PropertyVetoException;
/*     */ import java.sql.SQLException;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NameAlreadyBoundException;
/*     */ import javax.naming.NamingException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class C3P0PooledDataSource
/*     */   implements C3P0PooledDataSourceMBean
/*     */ {
/*  54 */   private static final MLogger logger = MLog.getLogger(C3P0PooledDataSource.class);
/*     */   
/*     */   String jndiName;
/*     */   
/*  58 */   ComboPooledDataSource combods = new ComboPooledDataSource();
/*     */   
/*     */   private void rebind() throws NamingException {
/*  61 */     rebind(null);
/*     */   }
/*     */   
/*     */   private void rebind(String unbindName) throws NamingException {
/*  65 */     InitialContext ictx = new InitialContext();
/*  66 */     if (unbindName != null) {
/*  67 */       ictx.unbind(unbindName);
/*     */     }
/*  69 */     if (this.jndiName != null) {
/*     */ 
/*     */ 
/*     */       
/*  73 */       Name name = ictx.getNameParser(this.jndiName).parse(this.jndiName);
/*  74 */       Context ctx = ictx;
/*  75 */       for (int i = 0, max = name.size() - 1; i < max; i++) {
/*     */         
/*     */         try {
/*  78 */           ctx = ctx.createSubcontext(name.get(i));
/*  79 */         } catch (NameAlreadyBoundException ignore) {
/*  80 */           ctx = (Context)ctx.lookup(name.get(i));
/*     */         } 
/*     */       } 
/*  83 */       ictx.rebind(this.jndiName, this.combods);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJndiName(String jndiName) throws NamingException {
/*  92 */     String unbindName = this.jndiName;
/*  93 */     this.jndiName = jndiName;
/*  94 */     rebind(unbindName);
/*     */   }
/*     */   
/*     */   public String getJndiName() {
/*  98 */     return this.jndiName;
/*     */   }
/*     */   
/*     */   public String getDescription() {
/* 102 */     return this.combods.getDescription();
/*     */   }
/*     */   
/*     */   public void setDescription(String description) throws NamingException {
/* 106 */     this.combods.setDescription(description);
/* 107 */     rebind();
/*     */   }
/*     */   
/*     */   public String getDriverClass() {
/* 111 */     return this.combods.getDriverClass();
/*     */   }
/*     */   
/*     */   public void setDriverClass(String driverClass) throws PropertyVetoException, NamingException {
/* 115 */     this.combods.setDriverClass(driverClass);
/* 116 */     rebind();
/*     */   }
/*     */   
/*     */   public String getJdbcUrl() {
/* 120 */     return this.combods.getJdbcUrl();
/*     */   }
/*     */   
/*     */   public void setJdbcUrl(String jdbcUrl) throws NamingException {
/* 124 */     this.combods.setJdbcUrl(jdbcUrl);
/* 125 */     rebind();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUser() {
/* 130 */     return this.combods.getUser();
/*     */   }
/*     */   
/*     */   public void setUser(String user) throws NamingException {
/* 134 */     this.combods.setUser(user);
/* 135 */     rebind();
/*     */   }
/*     */   
/*     */   public String getPassword() {
/* 139 */     return this.combods.getPassword();
/*     */   }
/*     */   
/*     */   public void setPassword(String password) throws NamingException {
/* 143 */     this.combods.setPassword(password);
/* 144 */     rebind();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCheckoutTimeout() {
/* 149 */     return this.combods.getCheckoutTimeout();
/*     */   }
/*     */   
/*     */   public void setCheckoutTimeout(int checkoutTimeout) throws NamingException {
/* 153 */     this.combods.setCheckoutTimeout(checkoutTimeout);
/* 154 */     rebind();
/*     */   }
/*     */   
/*     */   public int getAcquireIncrement() {
/* 158 */     return this.combods.getAcquireIncrement();
/*     */   }
/*     */   
/*     */   public void setAcquireIncrement(int acquireIncrement) throws NamingException {
/* 162 */     this.combods.setAcquireIncrement(acquireIncrement);
/* 163 */     rebind();
/*     */   }
/*     */   
/*     */   public int getAcquireRetryAttempts() {
/* 167 */     return this.combods.getAcquireRetryAttempts();
/*     */   }
/*     */   
/*     */   public void setAcquireRetryAttempts(int acquireRetryAttempts) throws NamingException {
/* 171 */     this.combods.setAcquireRetryAttempts(acquireRetryAttempts);
/* 172 */     rebind();
/*     */   }
/*     */   
/*     */   public int getAcquireRetryDelay() {
/* 176 */     return this.combods.getAcquireRetryDelay();
/*     */   }
/*     */   
/*     */   public void setAcquireRetryDelay(int acquireRetryDelay) throws NamingException {
/* 180 */     this.combods.setAcquireRetryDelay(acquireRetryDelay);
/* 181 */     rebind();
/*     */   }
/*     */   
/*     */   public boolean isAutoCommitOnClose() {
/* 185 */     return this.combods.isAutoCommitOnClose();
/*     */   }
/*     */   
/*     */   public void setAutoCommitOnClose(boolean autoCommitOnClose) throws NamingException {
/* 189 */     this.combods.setAutoCommitOnClose(autoCommitOnClose);
/* 190 */     rebind();
/*     */   }
/*     */   
/*     */   public String getConnectionTesterClassName() {
/* 194 */     return this.combods.getConnectionTesterClassName();
/*     */   }
/*     */   
/*     */   public void setConnectionTesterClassName(String connectionTesterClassName) throws PropertyVetoException, NamingException {
/* 198 */     this.combods.setConnectionTesterClassName(connectionTesterClassName);
/* 199 */     rebind();
/*     */   }
/*     */   
/*     */   public String getAutomaticTestTable() {
/* 203 */     return this.combods.getAutomaticTestTable();
/*     */   }
/*     */   
/*     */   public void setAutomaticTestTable(String automaticTestTable) throws NamingException {
/* 207 */     this.combods.setAutomaticTestTable(automaticTestTable);
/* 208 */     rebind();
/*     */   }
/*     */   
/*     */   public boolean isForceIgnoreUnresolvedTransactions() {
/* 212 */     return this.combods.isForceIgnoreUnresolvedTransactions();
/*     */   }
/*     */   
/*     */   public void setForceIgnoreUnresolvedTransactions(boolean forceIgnoreUnresolvedTransactions) throws NamingException {
/* 216 */     this.combods.setForceIgnoreUnresolvedTransactions(forceIgnoreUnresolvedTransactions);
/* 217 */     rebind();
/*     */   }
/*     */   
/*     */   public int getIdleConnectionTestPeriod() {
/* 221 */     return this.combods.getIdleConnectionTestPeriod();
/*     */   }
/*     */   
/*     */   public void setIdleConnectionTestPeriod(int idleConnectionTestPeriod) throws NamingException {
/* 225 */     this.combods.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
/* 226 */     rebind();
/*     */   }
/*     */   
/*     */   public int getInitialPoolSize() {
/* 230 */     return this.combods.getInitialPoolSize();
/*     */   }
/*     */   
/*     */   public void setInitialPoolSize(int initialPoolSize) throws NamingException {
/* 234 */     this.combods.setInitialPoolSize(initialPoolSize);
/* 235 */     rebind();
/*     */   }
/*     */   
/*     */   public int getMaxIdleTime() {
/* 239 */     return this.combods.getMaxIdleTime();
/*     */   }
/*     */   
/*     */   public void setMaxIdleTime(int maxIdleTime) throws NamingException {
/* 243 */     this.combods.setMaxIdleTime(maxIdleTime);
/* 244 */     rebind();
/*     */   }
/*     */   
/*     */   public int getMaxPoolSize() {
/* 248 */     return this.combods.getMaxPoolSize();
/*     */   }
/*     */   
/*     */   public void setMaxPoolSize(int maxPoolSize) throws NamingException {
/* 252 */     this.combods.setMaxPoolSize(maxPoolSize);
/* 253 */     rebind();
/*     */   }
/*     */   
/*     */   public int getMaxStatements() {
/* 257 */     return this.combods.getMaxStatements();
/*     */   }
/*     */   
/*     */   public void setMaxStatements(int maxStatements) throws NamingException {
/* 261 */     this.combods.setMaxStatements(maxStatements);
/* 262 */     rebind();
/*     */   }
/*     */   
/*     */   public int getMaxStatementsPerConnection() {
/* 266 */     return this.combods.getMaxStatementsPerConnection();
/*     */   }
/*     */   
/*     */   public void setMaxStatementsPerConnection(int maxStatementsPerConnection) throws NamingException {
/* 270 */     this.combods.setMaxStatementsPerConnection(maxStatementsPerConnection);
/* 271 */     rebind();
/*     */   }
/*     */   
/*     */   public int getMinPoolSize() {
/* 275 */     return this.combods.getMinPoolSize();
/*     */   }
/*     */   
/*     */   public void setMinPoolSize(int minPoolSize) throws NamingException {
/* 279 */     this.combods.setMinPoolSize(minPoolSize);
/* 280 */     rebind();
/*     */   }
/*     */   
/*     */   public int getPropertyCycle() {
/* 284 */     return this.combods.getPropertyCycle();
/*     */   }
/*     */   
/*     */   public void setPropertyCycle(int propertyCycle) throws NamingException {
/* 288 */     this.combods.setPropertyCycle(propertyCycle);
/* 289 */     rebind();
/*     */   }
/*     */   
/*     */   public boolean isBreakAfterAcquireFailure() {
/* 293 */     return this.combods.isBreakAfterAcquireFailure();
/*     */   }
/*     */   
/*     */   public void setBreakAfterAcquireFailure(boolean breakAfterAcquireFailure) throws NamingException {
/* 297 */     this.combods.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
/* 298 */     rebind();
/*     */   }
/*     */   
/*     */   public boolean isTestConnectionOnCheckout() {
/* 302 */     return this.combods.isTestConnectionOnCheckout();
/*     */   }
/*     */   
/*     */   public void setTestConnectionOnCheckout(boolean testConnectionOnCheckout) throws NamingException {
/* 306 */     this.combods.setTestConnectionOnCheckout(testConnectionOnCheckout);
/* 307 */     rebind();
/*     */   }
/*     */   
/*     */   public boolean isTestConnectionOnCheckin() {
/* 311 */     return this.combods.isTestConnectionOnCheckin();
/*     */   }
/*     */   
/*     */   public void setTestConnectionOnCheckin(boolean testConnectionOnCheckin) throws NamingException {
/* 315 */     this.combods.setTestConnectionOnCheckin(testConnectionOnCheckin);
/* 316 */     rebind();
/*     */   }
/*     */   
/*     */   public boolean isUsesTraditionalReflectiveProxies() {
/* 320 */     return this.combods.isUsesTraditionalReflectiveProxies();
/*     */   }
/*     */   
/*     */   public void setUsesTraditionalReflectiveProxies(boolean usesTraditionalReflectiveProxies) throws NamingException {
/* 324 */     this.combods.setUsesTraditionalReflectiveProxies(usesTraditionalReflectiveProxies);
/* 325 */     rebind();
/*     */   }
/*     */   
/*     */   public String getPreferredTestQuery() {
/* 329 */     return this.combods.getPreferredTestQuery();
/*     */   }
/*     */   
/*     */   public void setPreferredTestQuery(String preferredTestQuery) throws NamingException {
/* 333 */     this.combods.setPreferredTestQuery(preferredTestQuery);
/* 334 */     rebind();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDataSourceName() {
/* 339 */     return this.combods.getDataSourceName();
/*     */   }
/*     */   
/*     */   public void setDataSourceName(String name) throws NamingException {
/* 343 */     this.combods.setDataSourceName(name);
/* 344 */     rebind();
/*     */   }
/*     */   
/*     */   public int getNumHelperThreads() {
/* 348 */     return this.combods.getNumHelperThreads();
/*     */   }
/*     */   
/*     */   public void setNumHelperThreads(int numHelperThreads) throws NamingException {
/* 352 */     this.combods.setNumHelperThreads(numHelperThreads);
/* 353 */     rebind();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFactoryClassLocation() {
/* 358 */     return this.combods.getFactoryClassLocation();
/*     */   }
/*     */   
/*     */   public void setFactoryClassLocation(String factoryClassLocation) throws NamingException {
/* 362 */     this.combods.setFactoryClassLocation(factoryClassLocation);
/* 363 */     rebind();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumUserPools() throws SQLException {
/* 369 */     return this.combods.getNumUserPools();
/*     */   }
/*     */   public int getNumConnectionsDefaultUser() throws SQLException {
/* 372 */     return this.combods.getNumConnectionsDefaultUser();
/*     */   }
/*     */   public int getNumIdleConnectionsDefaultUser() throws SQLException {
/* 375 */     return this.combods.getNumIdleConnectionsDefaultUser();
/*     */   }
/*     */   public int getNumBusyConnectionsDefaultUser() throws SQLException {
/* 378 */     return this.combods.getNumBusyConnectionsDefaultUser();
/*     */   }
/*     */   public int getNumUnclosedOrphanedConnectionsDefaultUser() throws SQLException {
/* 381 */     return this.combods.getNumUnclosedOrphanedConnectionsDefaultUser();
/*     */   }
/*     */   public int getNumConnections(String username, String password) throws SQLException {
/* 384 */     return this.combods.getNumConnections(username, password);
/*     */   }
/*     */   public int getNumIdleConnections(String username, String password) throws SQLException {
/* 387 */     return this.combods.getNumIdleConnections(username, password);
/*     */   }
/*     */   public int getNumBusyConnections(String username, String password) throws SQLException {
/* 390 */     return this.combods.getNumBusyConnections(username, password);
/*     */   }
/*     */   public int getNumUnclosedOrphanedConnections(String username, String password) throws SQLException {
/* 393 */     return this.combods.getNumUnclosedOrphanedConnections(username, password);
/*     */   }
/*     */   public int getNumConnectionsAllUsers() throws SQLException {
/* 396 */     return this.combods.getNumConnectionsAllUsers();
/*     */   }
/*     */   public int getNumIdleConnectionsAllUsers() throws SQLException {
/* 399 */     return this.combods.getNumIdleConnectionsAllUsers();
/*     */   }
/*     */   public int getNumBusyConnectionsAllUsers() throws SQLException {
/* 402 */     return this.combods.getNumBusyConnectionsAllUsers();
/*     */   }
/*     */   public int getNumUnclosedOrphanedConnectionsAllUsers() throws SQLException {
/* 405 */     return this.combods.getNumUnclosedOrphanedConnectionsAllUsers();
/*     */   }
/*     */   
/*     */   public void softResetDefaultUser() throws SQLException {
/* 409 */     this.combods.softResetDefaultUser();
/*     */   }
/*     */   public void softReset(String username, String password) throws SQLException {
/* 412 */     this.combods.softReset(username, password);
/*     */   }
/*     */   public void softResetAllUsers() throws SQLException {
/* 415 */     this.combods.softResetAllUsers();
/*     */   }
/*     */   public void hardReset() throws SQLException {
/* 418 */     this.combods.hardReset();
/*     */   }
/*     */   public void close() throws SQLException {
/* 421 */     this.combods.close();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void create() throws Exception {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() throws Exception {
/* 431 */     logger.log(MLevel.INFO, "Bound C3P0 PooledDataSource to name ''{0}''. Starting...", this.jndiName);
/* 432 */     this.combods.getNumBusyConnectionsDefaultUser();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/*     */     try {
/* 443 */       this.combods.close();
/* 444 */       logger.log(MLevel.INFO, "Destroyed C3P0 PooledDataSource with name ''{0}''.", this.jndiName);
/*     */     }
/* 446 */     catch (Exception e) {
/*     */       
/* 448 */       logger.log(MLevel.INFO, "Failed to destroy C3P0 PooledDataSource.", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getConnectionCustomizerClassName() {
/* 453 */     return this.combods.getConnectionCustomizerClassName();
/*     */   }
/*     */   public float getEffectivePropertyCycle(String username, String password) throws SQLException {
/* 456 */     return this.combods.getEffectivePropertyCycle(username, password);
/*     */   }
/*     */   public float getEffectivePropertyCycleDefaultUser() throws SQLException {
/* 459 */     return this.combods.getEffectivePropertyCycleDefaultUser();
/*     */   }
/*     */   public int getMaxAdministrativeTaskTime() {
/* 462 */     return this.combods.getMaxAdministrativeTaskTime();
/*     */   }
/*     */   public int getMaxConnectionAge() {
/* 465 */     return this.combods.getMaxConnectionAge();
/*     */   }
/*     */   public int getMaxIdleTimeExcessConnections() {
/* 468 */     return this.combods.getMaxIdleTimeExcessConnections();
/*     */   }
/*     */   public int getUnreturnedConnectionTimeout() {
/* 471 */     return this.combods.getUnreturnedConnectionTimeout();
/*     */   }
/*     */   public boolean isDebugUnreturnedConnectionStackTraces() {
/* 474 */     return this.combods.isDebugUnreturnedConnectionStackTraces();
/*     */   }
/*     */   
/*     */   public void setConnectionCustomizerClassName(String connectionCustomizerClassName) throws NamingException {
/* 478 */     this.combods.setConnectionCustomizerClassName(connectionCustomizerClassName);
/* 479 */     rebind();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDebugUnreturnedConnectionStackTraces(boolean debugUnreturnedConnectionStackTraces) throws NamingException {
/* 484 */     this.combods.setDebugUnreturnedConnectionStackTraces(debugUnreturnedConnectionStackTraces);
/* 485 */     rebind();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxAdministrativeTaskTime(int maxAdministrativeTaskTime) throws NamingException {
/* 490 */     this.combods.setMaxAdministrativeTaskTime(maxAdministrativeTaskTime);
/* 491 */     rebind();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxConnectionAge(int maxConnectionAge) throws NamingException {
/* 496 */     this.combods.setMaxConnectionAge(maxConnectionAge);
/* 497 */     rebind();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxIdleTimeExcessConnections(int maxIdleTimeExcessConnections) throws NamingException {
/* 502 */     this.combods.setMaxIdleTimeExcessConnections(maxIdleTimeExcessConnections);
/* 503 */     rebind();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUnreturnedConnectionTimeout(int unreturnedConnectionTimeout) throws NamingException {
/* 508 */     this.combods.setUnreturnedConnectionTimeout(unreturnedConnectionTimeout);
/* 509 */     rebind();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/jboss/C3P0PooledDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */