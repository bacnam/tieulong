/*     */ package com.mchange.v2.c3p0;
/*     */ 
/*     */ import com.mchange.v2.beans.BeansUtils;
/*     */ import com.mchange.v2.c3p0.impl.C3P0ImplUtils;
/*     */ import com.mchange.v2.c3p0.impl.C3P0JavaBeanObjectFactory;
/*     */ import com.mchange.v2.c3p0.impl.IdentityTokenResolvable;
/*     */ import com.mchange.v2.c3p0.impl.IdentityTokenized;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import com.mchange.v2.naming.JavaBeanReferenceMaker;
/*     */ import java.beans.PropertyVetoException;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Hashtable;
/*     */ import java.util.logging.Logger;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.Referenceable;
/*     */ import javax.sql.ConnectionPoolDataSource;
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
/*     */ public final class JndiRefConnectionPoolDataSource
/*     */   extends IdentityTokenResolvable
/*     */   implements ConnectionPoolDataSource, Serializable, Referenceable
/*     */ {
/*  64 */   static final MLogger logger = MLog.getLogger(JndiRefConnectionPoolDataSource.class);
/*     */   
/*  66 */   static final Collection IGNORE_PROPS = Arrays.asList(new String[] { "reference", "pooledConnection" });
/*     */   
/*     */   JndiRefForwardingDataSource jrfds;
/*     */   
/*     */   WrapperConnectionPoolDataSource wcpds;
/*     */   String identityToken;
/*     */   
/*     */   public JndiRefConnectionPoolDataSource() {
/*  74 */     this(true);
/*     */   }
/*     */   
/*     */   public JndiRefConnectionPoolDataSource(boolean autoregister) {
/*  78 */     this.jrfds = new JndiRefForwardingDataSource();
/*  79 */     this.wcpds = new WrapperConnectionPoolDataSource();
/*  80 */     this.wcpds.setNestedDataSource(this.jrfds);
/*     */     
/*  82 */     if (autoregister) {
/*     */       
/*  84 */       this.identityToken = C3P0ImplUtils.allocateIdentityToken(this);
/*  85 */       C3P0Registry.reregister((IdentityTokenized)this);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isJndiLookupCaching() {
/*  90 */     return this.jrfds.isCaching();
/*     */   }
/*     */   public void setJndiLookupCaching(boolean caching) {
/*  93 */     this.jrfds.setCaching(caching);
/*     */   }
/*     */   public Hashtable getJndiEnv() {
/*  96 */     return this.jrfds.getJndiEnv();
/*     */   }
/*     */   public void setJndiEnv(Hashtable jndiEnv) {
/*  99 */     this.jrfds.setJndiEnv(jndiEnv);
/*     */   }
/*     */   public Object getJndiName() {
/* 102 */     return this.jrfds.getJndiName();
/*     */   }
/*     */   public void setJndiName(Object jndiName) throws PropertyVetoException {
/* 105 */     this.jrfds.setJndiName(jndiName);
/*     */   }
/*     */   public int getAcquireIncrement() {
/* 108 */     return this.wcpds.getAcquireIncrement();
/*     */   }
/*     */   public void setAcquireIncrement(int acquireIncrement) {
/* 111 */     this.wcpds.setAcquireIncrement(acquireIncrement);
/*     */   }
/*     */   public int getAcquireRetryAttempts() {
/* 114 */     return this.wcpds.getAcquireRetryAttempts();
/*     */   }
/*     */   public void setAcquireRetryAttempts(int ara) {
/* 117 */     this.wcpds.setAcquireRetryAttempts(ara);
/*     */   }
/*     */   public int getAcquireRetryDelay() {
/* 120 */     return this.wcpds.getAcquireRetryDelay();
/*     */   }
/*     */   public void setAcquireRetryDelay(int ard) {
/* 123 */     this.wcpds.setAcquireRetryDelay(ard);
/*     */   }
/*     */   public boolean isAutoCommitOnClose() {
/* 126 */     return this.wcpds.isAutoCommitOnClose();
/*     */   }
/*     */   public void setAutoCommitOnClose(boolean autoCommitOnClose) {
/* 129 */     this.wcpds.setAutoCommitOnClose(autoCommitOnClose);
/*     */   }
/*     */   public void setAutomaticTestTable(String att) {
/* 132 */     this.wcpds.setAutomaticTestTable(att);
/*     */   }
/*     */   public String getAutomaticTestTable() {
/* 135 */     return this.wcpds.getAutomaticTestTable();
/*     */   }
/*     */   public void setBreakAfterAcquireFailure(boolean baaf) {
/* 138 */     this.wcpds.setBreakAfterAcquireFailure(baaf);
/*     */   }
/*     */   public boolean isBreakAfterAcquireFailure() {
/* 141 */     return this.wcpds.isBreakAfterAcquireFailure();
/*     */   }
/*     */   public void setCheckoutTimeout(int ct) {
/* 144 */     this.wcpds.setCheckoutTimeout(ct);
/*     */   }
/*     */   public int getCheckoutTimeout() {
/* 147 */     return this.wcpds.getCheckoutTimeout();
/*     */   }
/*     */   public String getConnectionTesterClassName() {
/* 150 */     return this.wcpds.getConnectionTesterClassName();
/*     */   }
/*     */   public void setConnectionTesterClassName(String connectionTesterClassName) throws PropertyVetoException {
/* 153 */     this.wcpds.setConnectionTesterClassName(connectionTesterClassName);
/*     */   }
/*     */   public boolean isForceIgnoreUnresolvedTransactions() {
/* 156 */     return this.wcpds.isForceIgnoreUnresolvedTransactions();
/*     */   }
/*     */   public void setForceIgnoreUnresolvedTransactions(boolean forceIgnoreUnresolvedTransactions) {
/* 159 */     this.wcpds.setForceIgnoreUnresolvedTransactions(forceIgnoreUnresolvedTransactions);
/*     */   }
/*     */   public String getIdentityToken() {
/* 162 */     return this.identityToken;
/*     */   }
/*     */   public void setIdentityToken(String identityToken) {
/* 165 */     this.identityToken = identityToken;
/*     */   }
/*     */   public void setIdleConnectionTestPeriod(int idleConnectionTestPeriod) {
/* 168 */     this.wcpds.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
/*     */   }
/*     */   public int getIdleConnectionTestPeriod() {
/* 171 */     return this.wcpds.getIdleConnectionTestPeriod();
/*     */   }
/*     */   public int getInitialPoolSize() {
/* 174 */     return this.wcpds.getInitialPoolSize();
/*     */   }
/*     */   public void setInitialPoolSize(int initialPoolSize) {
/* 177 */     this.wcpds.setInitialPoolSize(initialPoolSize);
/*     */   }
/*     */   public int getMaxIdleTime() {
/* 180 */     return this.wcpds.getMaxIdleTime();
/*     */   }
/*     */   public void setMaxIdleTime(int maxIdleTime) {
/* 183 */     this.wcpds.setMaxIdleTime(maxIdleTime);
/*     */   }
/*     */   public int getMaxPoolSize() {
/* 186 */     return this.wcpds.getMaxPoolSize();
/*     */   }
/*     */   public void setMaxPoolSize(int maxPoolSize) {
/* 189 */     this.wcpds.setMaxPoolSize(maxPoolSize);
/*     */   }
/*     */   public int getMaxStatements() {
/* 192 */     return this.wcpds.getMaxStatements();
/*     */   }
/*     */   public void setMaxStatements(int maxStatements) {
/* 195 */     this.wcpds.setMaxStatements(maxStatements);
/*     */   }
/*     */   public int getMaxStatementsPerConnection() {
/* 198 */     return this.wcpds.getMaxStatementsPerConnection();
/*     */   }
/*     */   public void setMaxStatementsPerConnection(int mspc) {
/* 201 */     this.wcpds.setMaxStatementsPerConnection(mspc);
/*     */   }
/*     */   public int getMinPoolSize() {
/* 204 */     return this.wcpds.getMinPoolSize();
/*     */   }
/*     */   public void setMinPoolSize(int minPoolSize) {
/* 207 */     this.wcpds.setMinPoolSize(minPoolSize);
/*     */   }
/*     */   public String getPreferredTestQuery() {
/* 210 */     return this.wcpds.getPreferredTestQuery();
/*     */   }
/*     */   public void setPreferredTestQuery(String ptq) {
/* 213 */     this.wcpds.setPreferredTestQuery(ptq);
/*     */   }
/*     */   public int getPropertyCycle() {
/* 216 */     return this.wcpds.getPropertyCycle();
/*     */   }
/*     */   public void setPropertyCycle(int propertyCycle) {
/* 219 */     this.wcpds.setPropertyCycle(propertyCycle);
/*     */   }
/*     */   public boolean isTestConnectionOnCheckin() {
/* 222 */     return this.wcpds.isTestConnectionOnCheckin();
/*     */   }
/*     */   public void setTestConnectionOnCheckin(boolean testConnectionOnCheckin) {
/* 225 */     this.wcpds.setTestConnectionOnCheckin(testConnectionOnCheckin);
/*     */   }
/*     */   public boolean isTestConnectionOnCheckout() {
/* 228 */     return this.wcpds.isTestConnectionOnCheckout();
/*     */   }
/*     */   public void setTestConnectionOnCheckout(boolean testConnectionOnCheckout) {
/* 231 */     this.wcpds.setTestConnectionOnCheckout(testConnectionOnCheckout);
/*     */   }
/*     */   public boolean isUsesTraditionalReflectiveProxies() {
/* 234 */     return this.wcpds.isUsesTraditionalReflectiveProxies();
/*     */   }
/*     */   public void setUsesTraditionalReflectiveProxies(boolean utrp) {
/* 237 */     this.wcpds.setUsesTraditionalReflectiveProxies(utrp);
/*     */   }
/*     */   public String getFactoryClassLocation() {
/* 240 */     return this.jrfds.getFactoryClassLocation();
/*     */   }
/*     */   
/*     */   public void setFactoryClassLocation(String factoryClassLocation) {
/* 244 */     this.jrfds.setFactoryClassLocation(factoryClassLocation);
/* 245 */     this.wcpds.setFactoryClassLocation(factoryClassLocation);
/*     */   }
/*     */   
/* 248 */   static final JavaBeanReferenceMaker referenceMaker = new JavaBeanReferenceMaker();
/*     */ 
/*     */   
/*     */   static {
/* 252 */     referenceMaker.setFactoryClassName(C3P0JavaBeanObjectFactory.class.getName());
/* 253 */     referenceMaker.addReferenceProperty("acquireIncrement");
/* 254 */     referenceMaker.addReferenceProperty("acquireRetryAttempts");
/* 255 */     referenceMaker.addReferenceProperty("acquireRetryDelay");
/* 256 */     referenceMaker.addReferenceProperty("autoCommitOnClose");
/* 257 */     referenceMaker.addReferenceProperty("automaticTestTable");
/* 258 */     referenceMaker.addReferenceProperty("checkoutTimeout");
/* 259 */     referenceMaker.addReferenceProperty("connectionTesterClassName");
/* 260 */     referenceMaker.addReferenceProperty("factoryClassLocation");
/* 261 */     referenceMaker.addReferenceProperty("forceIgnoreUnresolvedTransactions");
/* 262 */     referenceMaker.addReferenceProperty("idleConnectionTestPeriod");
/* 263 */     referenceMaker.addReferenceProperty("identityToken");
/* 264 */     referenceMaker.addReferenceProperty("initialPoolSize");
/* 265 */     referenceMaker.addReferenceProperty("jndiEnv");
/* 266 */     referenceMaker.addReferenceProperty("jndiLookupCaching");
/* 267 */     referenceMaker.addReferenceProperty("jndiName");
/* 268 */     referenceMaker.addReferenceProperty("maxIdleTime");
/* 269 */     referenceMaker.addReferenceProperty("maxPoolSize");
/* 270 */     referenceMaker.addReferenceProperty("maxStatements");
/* 271 */     referenceMaker.addReferenceProperty("maxStatementsPerConnection");
/* 272 */     referenceMaker.addReferenceProperty("minPoolSize");
/* 273 */     referenceMaker.addReferenceProperty("preferredTestQuery");
/* 274 */     referenceMaker.addReferenceProperty("propertyCycle");
/* 275 */     referenceMaker.addReferenceProperty("testConnectionOnCheckin");
/* 276 */     referenceMaker.addReferenceProperty("testConnectionOnCheckout");
/* 277 */     referenceMaker.addReferenceProperty("usesTraditionalReflectiveProxies");
/*     */   }
/*     */   
/*     */   public Reference getReference() throws NamingException {
/* 281 */     return referenceMaker.createReference(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public PooledConnection getPooledConnection() throws SQLException {
/* 286 */     return this.wcpds.getPooledConnection();
/*     */   }
/*     */   
/*     */   public PooledConnection getPooledConnection(String user, String password) throws SQLException {
/* 290 */     return this.wcpds.getPooledConnection(user, password);
/*     */   }
/*     */   
/*     */   public PrintWriter getLogWriter() throws SQLException {
/* 294 */     return this.wcpds.getLogWriter();
/*     */   }
/*     */   
/*     */   public void setLogWriter(PrintWriter out) throws SQLException {
/* 298 */     this.wcpds.setLogWriter(out);
/*     */   }
/*     */   
/*     */   public void setLoginTimeout(int seconds) throws SQLException {
/* 302 */     this.wcpds.setLoginTimeout(seconds);
/*     */   }
/*     */   
/*     */   public int getLoginTimeout() throws SQLException {
/* 306 */     return this.wcpds.getLoginTimeout();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 310 */     StringBuffer sb = new StringBuffer(512);
/* 311 */     sb.append(super.toString());
/* 312 */     sb.append(" ["); try {
/* 313 */       BeansUtils.appendPropNamesAndValues(sb, this, IGNORE_PROPS);
/* 314 */     } catch (Exception e) {
/*     */ 
/*     */       
/* 317 */       if (logger.isLoggable(MLevel.FINE))
/* 318 */         logger.log(MLevel.FINE, "An exception occurred while extracting property names and values for toString()", e); 
/* 319 */       sb.append(e.toString());
/*     */     } 
/* 321 */     sb.append("]");
/* 322 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getParentLogger() throws SQLFeatureNotSupportedException {
/* 328 */     throw new SQLFeatureNotSupportedException("javax.sql.DataSource.getParentLogger() is not currently supported by " + getClass().getName());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/JndiRefConnectionPoolDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */