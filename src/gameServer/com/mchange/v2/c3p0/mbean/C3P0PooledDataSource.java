/*     */ package com.mchange.v2.c3p0.mbean;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class C3P0PooledDataSource
/*     */   implements C3P0PooledDataSourceMBean
/*     */ {
/*  57 */   private static final MLogger logger = MLog.getLogger(C3P0PooledDataSource.class);
/*     */   
/*     */   String jndiName;
/*     */   
/*  61 */   ComboPooledDataSource combods = new ComboPooledDataSource();
/*     */   
/*     */   private void rebind() throws NamingException {
/*  64 */     rebind(null);
/*     */   }
/*     */   
/*     */   private void rebind(String unbindName) throws NamingException {
/*  68 */     InitialContext ictx = new InitialContext();
/*  69 */     if (unbindName != null) {
/*  70 */       ictx.unbind(unbindName);
/*     */     }
/*  72 */     if (this.jndiName != null) {
/*     */ 
/*     */ 
/*     */       
/*  76 */       Name name = ictx.getNameParser(this.jndiName).parse(this.jndiName);
/*  77 */       Context ctx = ictx;
/*  78 */       for (int i = 0, max = name.size() - 1; i < max; i++) {
/*     */         
/*     */         try {
/*  81 */           ctx = ctx.createSubcontext(name.get(i));
/*  82 */         } catch (NameAlreadyBoundException ignore) {
/*  83 */           ctx = (Context)ctx.lookup(name.get(i));
/*     */         } 
/*     */       } 
/*  86 */       ictx.rebind(this.jndiName, this.combods);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJndiName(String jndiName) throws NamingException {
/*  95 */     String unbindName = this.jndiName;
/*  96 */     this.jndiName = jndiName;
/*  97 */     rebind(unbindName);
/*     */   }
/*     */   
/*     */   public String getJndiName() {
/* 101 */     return this.jndiName;
/*     */   }
/*     */   
/*     */   public String getDescription() {
/* 105 */     return this.combods.getDescription();
/*     */   }
/*     */   
/*     */   public void setDescription(String description) throws NamingException {
/* 109 */     this.combods.setDescription(description);
/* 110 */     rebind();
/*     */   }
/*     */   
/*     */   public String getDriverClass() {
/* 114 */     return this.combods.getDriverClass();
/*     */   }
/*     */   
/*     */   public void setDriverClass(String driverClass) throws PropertyVetoException, NamingException {
/* 118 */     this.combods.setDriverClass(driverClass);
/* 119 */     rebind();
/*     */   }
/*     */   
/*     */   public String getJdbcUrl() {
/* 123 */     return this.combods.getJdbcUrl();
/*     */   }
/*     */   
/*     */   public void setJdbcUrl(String jdbcUrl) throws NamingException {
/* 127 */     this.combods.setJdbcUrl(jdbcUrl);
/* 128 */     rebind();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUser() {
/* 133 */     return this.combods.getUser();
/*     */   }
/*     */   
/*     */   public void setUser(String user) throws NamingException {
/* 137 */     this.combods.setUser(user);
/* 138 */     rebind();
/*     */   }
/*     */   
/*     */   public String getPassword() {
/* 142 */     return this.combods.getPassword();
/*     */   }
/*     */   
/*     */   public void setPassword(String password) throws NamingException {
/* 146 */     this.combods.setPassword(password);
/* 147 */     rebind();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCheckoutTimeout() {
/* 152 */     return this.combods.getCheckoutTimeout();
/*     */   }
/*     */   
/*     */   public void setCheckoutTimeout(int checkoutTimeout) throws NamingException {
/* 156 */     this.combods.setCheckoutTimeout(checkoutTimeout);
/* 157 */     rebind();
/*     */   }
/*     */   
/*     */   public int getAcquireIncrement() {
/* 161 */     return this.combods.getAcquireIncrement();
/*     */   }
/*     */   
/*     */   public void setAcquireIncrement(int acquireIncrement) throws NamingException {
/* 165 */     this.combods.setAcquireIncrement(acquireIncrement);
/* 166 */     rebind();
/*     */   }
/*     */   
/*     */   public int getAcquireRetryAttempts() {
/* 170 */     return this.combods.getAcquireRetryAttempts();
/*     */   }
/*     */   
/*     */   public void setAcquireRetryAttempts(int acquireRetryAttempts) throws NamingException {
/* 174 */     this.combods.setAcquireRetryAttempts(acquireRetryAttempts);
/* 175 */     rebind();
/*     */   }
/*     */   
/*     */   public int getAcquireRetryDelay() {
/* 179 */     return this.combods.getAcquireRetryDelay();
/*     */   }
/*     */   
/*     */   public void setAcquireRetryDelay(int acquireRetryDelay) throws NamingException {
/* 183 */     this.combods.setAcquireRetryDelay(acquireRetryDelay);
/* 184 */     rebind();
/*     */   }
/*     */   
/*     */   public boolean isAutoCommitOnClose() {
/* 188 */     return this.combods.isAutoCommitOnClose();
/*     */   }
/*     */   
/*     */   public void setAutoCommitOnClose(boolean autoCommitOnClose) throws NamingException {
/* 192 */     this.combods.setAutoCommitOnClose(autoCommitOnClose);
/* 193 */     rebind();
/*     */   }
/*     */   
/*     */   public String getConnectionTesterClassName() {
/* 197 */     return this.combods.getConnectionTesterClassName();
/*     */   }
/*     */   
/*     */   public void setConnectionTesterClassName(String connectionTesterClassName) throws PropertyVetoException, NamingException {
/* 201 */     this.combods.setConnectionTesterClassName(connectionTesterClassName);
/* 202 */     rebind();
/*     */   }
/*     */   
/*     */   public String getAutomaticTestTable() {
/* 206 */     return this.combods.getAutomaticTestTable();
/*     */   }
/*     */   
/*     */   public void setAutomaticTestTable(String automaticTestTable) throws NamingException {
/* 210 */     this.combods.setAutomaticTestTable(automaticTestTable);
/* 211 */     rebind();
/*     */   }
/*     */   
/*     */   public boolean isForceIgnoreUnresolvedTransactions() {
/* 215 */     return this.combods.isForceIgnoreUnresolvedTransactions();
/*     */   }
/*     */   
/*     */   public void setForceIgnoreUnresolvedTransactions(boolean forceIgnoreUnresolvedTransactions) throws NamingException {
/* 219 */     this.combods.setForceIgnoreUnresolvedTransactions(forceIgnoreUnresolvedTransactions);
/* 220 */     rebind();
/*     */   }
/*     */   
/*     */   public int getIdleConnectionTestPeriod() {
/* 224 */     return this.combods.getIdleConnectionTestPeriod();
/*     */   }
/*     */   
/*     */   public void setIdleConnectionTestPeriod(int idleConnectionTestPeriod) throws NamingException {
/* 228 */     this.combods.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
/* 229 */     rebind();
/*     */   }
/*     */   
/*     */   public int getInitialPoolSize() {
/* 233 */     return this.combods.getInitialPoolSize();
/*     */   }
/*     */   
/*     */   public void setInitialPoolSize(int initialPoolSize) throws NamingException {
/* 237 */     this.combods.setInitialPoolSize(initialPoolSize);
/* 238 */     rebind();
/*     */   }
/*     */   
/*     */   public int getMaxIdleTime() {
/* 242 */     return this.combods.getMaxIdleTime();
/*     */   }
/*     */   
/*     */   public void setMaxIdleTime(int maxIdleTime) throws NamingException {
/* 246 */     this.combods.setMaxIdleTime(maxIdleTime);
/* 247 */     rebind();
/*     */   }
/*     */   
/*     */   public int getMaxPoolSize() {
/* 251 */     return this.combods.getMaxPoolSize();
/*     */   }
/*     */   
/*     */   public void setMaxPoolSize(int maxPoolSize) throws NamingException {
/* 255 */     this.combods.setMaxPoolSize(maxPoolSize);
/* 256 */     rebind();
/*     */   }
/*     */   
/*     */   public int getMaxStatements() {
/* 260 */     return this.combods.getMaxStatements();
/*     */   }
/*     */   
/*     */   public void setMaxStatements(int maxStatements) throws NamingException {
/* 264 */     this.combods.setMaxStatements(maxStatements);
/* 265 */     rebind();
/*     */   }
/*     */   
/*     */   public int getMaxStatementsPerConnection() {
/* 269 */     return this.combods.getMaxStatementsPerConnection();
/*     */   }
/*     */   
/*     */   public void setMaxStatementsPerConnection(int maxStatementsPerConnection) throws NamingException {
/* 273 */     this.combods.setMaxStatementsPerConnection(maxStatementsPerConnection);
/* 274 */     rebind();
/*     */   }
/*     */   
/*     */   public int getMinPoolSize() {
/* 278 */     return this.combods.getMinPoolSize();
/*     */   }
/*     */   
/*     */   public void setMinPoolSize(int minPoolSize) throws NamingException {
/* 282 */     this.combods.setMinPoolSize(minPoolSize);
/* 283 */     rebind();
/*     */   }
/*     */   
/*     */   public int getPropertyCycle() {
/* 287 */     return this.combods.getPropertyCycle();
/*     */   }
/*     */   
/*     */   public void setPropertyCycle(int propertyCycle) throws NamingException {
/* 291 */     this.combods.setPropertyCycle(propertyCycle);
/* 292 */     rebind();
/*     */   }
/*     */   
/*     */   public boolean isBreakAfterAcquireFailure() {
/* 296 */     return this.combods.isBreakAfterAcquireFailure();
/*     */   }
/*     */   
/*     */   public void setBreakAfterAcquireFailure(boolean breakAfterAcquireFailure) throws NamingException {
/* 300 */     this.combods.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
/* 301 */     rebind();
/*     */   }
/*     */   
/*     */   public boolean isTestConnectionOnCheckout() {
/* 305 */     return this.combods.isTestConnectionOnCheckout();
/*     */   }
/*     */   
/*     */   public void setTestConnectionOnCheckout(boolean testConnectionOnCheckout) throws NamingException {
/* 309 */     this.combods.setTestConnectionOnCheckout(testConnectionOnCheckout);
/* 310 */     rebind();
/*     */   }
/*     */   
/*     */   public boolean isTestConnectionOnCheckin() {
/* 314 */     return this.combods.isTestConnectionOnCheckin();
/*     */   }
/*     */   
/*     */   public void setTestConnectionOnCheckin(boolean testConnectionOnCheckin) throws NamingException {
/* 318 */     this.combods.setTestConnectionOnCheckin(testConnectionOnCheckin);
/* 319 */     rebind();
/*     */   }
/*     */   
/*     */   public boolean isUsesTraditionalReflectiveProxies() {
/* 323 */     return this.combods.isUsesTraditionalReflectiveProxies();
/*     */   }
/*     */   
/*     */   public void setUsesTraditionalReflectiveProxies(boolean usesTraditionalReflectiveProxies) throws NamingException {
/* 327 */     this.combods.setUsesTraditionalReflectiveProxies(usesTraditionalReflectiveProxies);
/* 328 */     rebind();
/*     */   }
/*     */   
/*     */   public String getPreferredTestQuery() {
/* 332 */     return this.combods.getPreferredTestQuery();
/*     */   }
/*     */   
/*     */   public void setPreferredTestQuery(String preferredTestQuery) throws NamingException {
/* 336 */     this.combods.setPreferredTestQuery(preferredTestQuery);
/* 337 */     rebind();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDataSourceName() {
/* 342 */     return this.combods.getDataSourceName();
/*     */   }
/*     */   
/*     */   public void setDataSourceName(String name) throws NamingException {
/* 346 */     this.combods.setDataSourceName(name);
/* 347 */     rebind();
/*     */   }
/*     */   
/*     */   public int getNumHelperThreads() {
/* 351 */     return this.combods.getNumHelperThreads();
/*     */   }
/*     */   
/*     */   public void setNumHelperThreads(int numHelperThreads) throws NamingException {
/* 355 */     this.combods.setNumHelperThreads(numHelperThreads);
/* 356 */     rebind();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFactoryClassLocation() {
/* 361 */     return this.combods.getFactoryClassLocation();
/*     */   }
/*     */   
/*     */   public void setFactoryClassLocation(String factoryClassLocation) throws NamingException {
/* 365 */     this.combods.setFactoryClassLocation(factoryClassLocation);
/* 366 */     rebind();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumUserPools() throws SQLException {
/* 372 */     return this.combods.getNumUserPools();
/*     */   }
/*     */   public int getNumConnectionsDefaultUser() throws SQLException {
/* 375 */     return this.combods.getNumConnectionsDefaultUser();
/*     */   }
/*     */   public int getNumIdleConnectionsDefaultUser() throws SQLException {
/* 378 */     return this.combods.getNumIdleConnectionsDefaultUser();
/*     */   }
/*     */   public int getNumBusyConnectionsDefaultUser() throws SQLException {
/* 381 */     return this.combods.getNumBusyConnectionsDefaultUser();
/*     */   }
/*     */   public int getNumUnclosedOrphanedConnectionsDefaultUser() throws SQLException {
/* 384 */     return this.combods.getNumUnclosedOrphanedConnectionsDefaultUser();
/*     */   }
/*     */   public int getNumConnections(String username, String password) throws SQLException {
/* 387 */     return this.combods.getNumConnections(username, password);
/*     */   }
/*     */   public int getNumIdleConnections(String username, String password) throws SQLException {
/* 390 */     return this.combods.getNumIdleConnections(username, password);
/*     */   }
/*     */   public int getNumBusyConnections(String username, String password) throws SQLException {
/* 393 */     return this.combods.getNumBusyConnections(username, password);
/*     */   }
/*     */   public int getNumUnclosedOrphanedConnections(String username, String password) throws SQLException {
/* 396 */     return this.combods.getNumUnclosedOrphanedConnections(username, password);
/*     */   }
/*     */   public int getNumConnectionsAllUsers() throws SQLException {
/* 399 */     return this.combods.getNumConnectionsAllUsers();
/*     */   }
/*     */   public int getNumIdleConnectionsAllUsers() throws SQLException {
/* 402 */     return this.combods.getNumIdleConnectionsAllUsers();
/*     */   }
/*     */   public int getNumBusyConnectionsAllUsers() throws SQLException {
/* 405 */     return this.combods.getNumBusyConnectionsAllUsers();
/*     */   }
/*     */   public int getNumUnclosedOrphanedConnectionsAllUsers() throws SQLException {
/* 408 */     return this.combods.getNumUnclosedOrphanedConnectionsAllUsers();
/*     */   }
/*     */   
/*     */   public void softResetDefaultUser() throws SQLException {
/* 412 */     this.combods.softResetDefaultUser();
/*     */   }
/*     */   public void softReset(String username, String password) throws SQLException {
/* 415 */     this.combods.softReset(username, password);
/*     */   }
/*     */   public void softResetAllUsers() throws SQLException {
/* 418 */     this.combods.softResetAllUsers();
/*     */   }
/*     */   public void hardReset() throws SQLException {
/* 421 */     this.combods.hardReset();
/*     */   }
/*     */   public void close() throws SQLException {
/* 424 */     this.combods.close();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void create() throws Exception {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() throws Exception {
/* 434 */     logger.log(MLevel.INFO, "Bound C3P0 PooledDataSource to name ''{0}''. Starting...", this.jndiName);
/* 435 */     this.combods.getNumBusyConnectionsDefaultUser();
/*     */   }
/*     */   
/*     */   public void stop() {}
/*     */   
/*     */   public void destroy() {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/mbean/C3P0PooledDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */