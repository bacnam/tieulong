/*     */ package com.mchange.v2.c3p0;
/*     */ 
/*     */ import com.mchange.v1.db.sql.ConnectionUtils;
/*     */ import com.mchange.v2.c3p0.cfg.C3P0Config;
/*     */ import com.mchange.v2.c3p0.cfg.C3P0ConfigUtils;
/*     */ import com.mchange.v2.c3p0.impl.C3P0ImplUtils;
/*     */ import com.mchange.v2.c3p0.impl.C3P0PooledConnection;
/*     */ import com.mchange.v2.c3p0.impl.NewPooledConnection;
/*     */ import com.mchange.v2.c3p0.impl.WrapperConnectionPoolDataSourceBase;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyVetoException;
/*     */ import java.beans.VetoableChangeListener;
/*     */ import java.io.PrintWriter;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Map;
/*     */ import javax.sql.ConnectionPoolDataSource;
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
/*     */ public final class WrapperConnectionPoolDataSource
/*     */   extends WrapperConnectionPoolDataSourceBase
/*     */   implements ConnectionPoolDataSource
/*     */ {
/*  59 */   static final MLogger logger = MLog.getLogger(WrapperConnectionPoolDataSource.class);
/*     */ 
/*     */   
/*  62 */   ConnectionTester connectionTester = C3P0Registry.getDefaultConnectionTester();
/*     */   
/*     */   Map userOverrides;
/*     */   
/*     */   public WrapperConnectionPoolDataSource(boolean autoregister) {
/*  67 */     super(autoregister);
/*     */     
/*  69 */     setUpPropertyListeners();
/*     */ 
/*     */     
/*     */     try {
/*  73 */       this.userOverrides = C3P0ImplUtils.parseUserOverridesAsString(getUserOverridesAsString());
/*  74 */     } catch (Exception e) {
/*     */       
/*  76 */       if (logger.isLoggable(MLevel.WARNING))
/*  77 */         logger.log(MLevel.WARNING, "Failed to parse stringified userOverrides. " + getUserOverridesAsString(), e); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public WrapperConnectionPoolDataSource() {
/*  82 */     this(true);
/*     */   }
/*     */   
/*     */   private void setUpPropertyListeners() {
/*  86 */     VetoableChangeListener setConnectionTesterListener = new VetoableChangeListener()
/*     */       {
/*     */         
/*     */         public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException
/*     */         {
/*  91 */           String propName = evt.getPropertyName();
/*  92 */           Object val = evt.getNewValue();
/*     */           
/*  94 */           if ("connectionTesterClassName".equals(propName)) {
/*     */             
/*     */             try {
/*  97 */               WrapperConnectionPoolDataSource.this.recreateConnectionTester((String)val);
/*  98 */             } catch (Exception e) {
/*     */ 
/*     */               
/* 101 */               if (WrapperConnectionPoolDataSource.logger.isLoggable(MLevel.WARNING)) {
/* 102 */                 WrapperConnectionPoolDataSource.logger.log(MLevel.WARNING, "Failed to create ConnectionTester of class " + val, e);
/*     */               }
/* 104 */               throw new PropertyVetoException("Could not instantiate connection tester class with name '" + val + "'.", evt);
/*     */             }
/*     */           
/* 107 */           } else if ("userOverridesAsString".equals(propName)) {
/*     */             
/*     */             try {
/* 110 */               WrapperConnectionPoolDataSource.this.userOverrides = C3P0ImplUtils.parseUserOverridesAsString((String)val);
/* 111 */             } catch (Exception e) {
/*     */               
/* 113 */               if (WrapperConnectionPoolDataSource.logger.isLoggable(MLevel.WARNING)) {
/* 114 */                 WrapperConnectionPoolDataSource.logger.log(MLevel.WARNING, "Failed to parse stringified userOverrides. " + val, e);
/*     */               }
/* 116 */               throw new PropertyVetoException("Failed to parse stringified userOverrides. " + val, evt);
/*     */             } 
/*     */           } 
/*     */         }
/*     */       };
/* 121 */     addVetoableChangeListener(setConnectionTesterListener);
/*     */   }
/*     */ 
/*     */   
/*     */   public WrapperConnectionPoolDataSource(String configName) {
/* 126 */     this();
/*     */ 
/*     */     
/*     */     try {
/* 130 */       if (configName != null) {
/* 131 */         C3P0Config.bindNamedConfigToBean(this, configName, true);
/*     */       }
/* 133 */     } catch (Exception e) {
/*     */       
/* 135 */       if (logger.isLoggable(MLevel.WARNING)) {
/* 136 */         logger.log(MLevel.WARNING, "Error binding WrapperConnectionPoolDataSource to named-config '" + configName + "'. Some default-config values may be used.", e);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PooledConnection getPooledConnection() throws SQLException {
/* 147 */     return getPooledConnection((ConnectionCustomizer)null, (String)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PooledConnection getPooledConnection(ConnectionCustomizer cc, String pdsIdt) throws SQLException {
/* 155 */     DataSource nds = getNestedDataSource();
/* 156 */     if (nds == null)
/* 157 */       throw new SQLException("No standard DataSource has been set beneath this wrapper! [ nestedDataSource == null ]"); 
/* 158 */     Connection conn = null;
/*     */     
/*     */     try {
/* 161 */       conn = nds.getConnection();
/* 162 */       if (conn == null) {
/* 163 */         throw new SQLException("An (unpooled) DataSource returned null from its getConnection() method! DataSource: " + getNestedDataSource());
/*     */       }
/* 165 */       if (isUsesTraditionalReflectiveProxies(getUser()))
/*     */       {
/*     */         
/* 168 */         return (PooledConnection)new C3P0PooledConnection(conn, this.connectionTester, isAutoCommitOnClose(getUser()), isForceIgnoreUnresolvedTransactions(getUser()), cc, pdsIdt);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 177 */       return (PooledConnection)new NewPooledConnection(conn, this.connectionTester, isAutoCommitOnClose(getUser()), isForceIgnoreUnresolvedTransactions(getUser()), getPreferredTestQuery(getUser()), cc, pdsIdt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 186 */     catch (SQLException e) {
/*     */ 
/*     */ 
/*     */       
/* 190 */       ConnectionUtils.attemptClose(conn);
/*     */       
/* 192 */       throw e;
/*     */     }
/* 194 */     catch (RuntimeException re) {
/*     */ 
/*     */ 
/*     */       
/* 198 */       ConnectionUtils.attemptClose(conn);
/*     */       
/* 200 */       throw re;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public PooledConnection getPooledConnection(String user, String password) throws SQLException {
/* 206 */     return getPooledConnection(user, password, (ConnectionCustomizer)null, (String)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PooledConnection getPooledConnection(String user, String password, ConnectionCustomizer cc, String pdsIdt) throws SQLException {
/* 214 */     DataSource nds = getNestedDataSource();
/* 215 */     if (nds == null)
/* 216 */       throw new SQLException("No standard DataSource has been set beneath this wrapper! [ nestedDataSource == null ]"); 
/* 217 */     Connection conn = null;
/*     */     
/*     */     try {
/* 220 */       conn = nds.getConnection(user, password);
/* 221 */       if (conn == null) {
/* 222 */         throw new SQLException("An (unpooled) DataSource returned null from its getConnection() method! DataSource: " + getNestedDataSource());
/*     */       }
/* 224 */       if (isUsesTraditionalReflectiveProxies(user))
/*     */       {
/*     */         
/* 227 */         return (PooledConnection)new C3P0PooledConnection(conn, this.connectionTester, isAutoCommitOnClose(user), isForceIgnoreUnresolvedTransactions(user), cc, pdsIdt);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 236 */       return (PooledConnection)new NewPooledConnection(conn, this.connectionTester, isAutoCommitOnClose(user), isForceIgnoreUnresolvedTransactions(user), getPreferredTestQuery(user), cc, pdsIdt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 245 */     catch (SQLException e) {
/*     */ 
/*     */ 
/*     */       
/* 249 */       ConnectionUtils.attemptClose(conn);
/*     */       
/* 251 */       throw e;
/*     */     }
/* 253 */     catch (RuntimeException re) {
/*     */ 
/*     */ 
/*     */       
/* 257 */       ConnectionUtils.attemptClose(conn);
/*     */       
/* 259 */       throw re;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isAutoCommitOnClose(String userName) {
/* 265 */     if (userName == null) {
/* 266 */       return isAutoCommitOnClose();
/*     */     }
/* 268 */     Boolean override = C3P0ConfigUtils.extractBooleanOverride("autoCommitOnClose", userName, this.userOverrides);
/* 269 */     return (override == null) ? isAutoCommitOnClose() : override.booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isForceIgnoreUnresolvedTransactions(String userName) {
/* 274 */     if (userName == null) {
/* 275 */       return isForceIgnoreUnresolvedTransactions();
/*     */     }
/* 277 */     Boolean override = C3P0ConfigUtils.extractBooleanOverride("forceIgnoreUnresolvedTransactions", userName, this.userOverrides);
/* 278 */     return (override == null) ? isForceIgnoreUnresolvedTransactions() : override.booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isUsesTraditionalReflectiveProxies(String userName) {
/* 283 */     if (userName == null) {
/* 284 */       return isUsesTraditionalReflectiveProxies();
/*     */     }
/* 286 */     Boolean override = C3P0ConfigUtils.extractBooleanOverride("usesTraditionalReflectiveProxies", userName, this.userOverrides);
/* 287 */     return (override == null) ? isUsesTraditionalReflectiveProxies() : override.booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   private String getPreferredTestQuery(String userName) {
/* 292 */     if (userName == null) {
/* 293 */       return getPreferredTestQuery();
/*     */     }
/* 295 */     String override = (String)C3P0ConfigUtils.extractUserOverride("preferredTestQuery", userName, this.userOverrides);
/* 296 */     return (override == null) ? getPreferredTestQuery() : override;
/*     */   }
/*     */ 
/*     */   
/*     */   public PrintWriter getLogWriter() throws SQLException {
/* 301 */     return getNestedDataSource().getLogWriter();
/*     */   }
/*     */   
/*     */   public void setLogWriter(PrintWriter out) throws SQLException {
/* 305 */     getNestedDataSource().setLogWriter(out);
/*     */   }
/*     */   
/*     */   public void setLoginTimeout(int seconds) throws SQLException {
/* 309 */     getNestedDataSource().setLoginTimeout(seconds);
/*     */   }
/*     */   
/*     */   public int getLoginTimeout() throws SQLException {
/* 313 */     return getNestedDataSource().getLoginTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUser() {
/*     */     try {
/* 319 */       return C3P0ImplUtils.findAuth(getNestedDataSource()).getUser();
/* 320 */     } catch (SQLException e) {
/*     */ 
/*     */       
/* 323 */       if (logger.isLoggable(MLevel.WARNING)) {
/* 324 */         logger.log(MLevel.WARNING, "An Exception occurred while trying to find the 'user' property from our nested DataSource. Defaulting to no specified username.", e);
/*     */       }
/*     */       
/* 327 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getPassword() {
/*     */     try {
/* 333 */       return C3P0ImplUtils.findAuth(getNestedDataSource()).getPassword();
/* 334 */     } catch (SQLException e) {
/*     */ 
/*     */       
/* 337 */       if (logger.isLoggable(MLevel.WARNING)) {
/* 338 */         logger.log(MLevel.WARNING, "An Exception occurred while trying to find the 'password' property from our nested DataSource. Defaulting to no specified password.", e);
/*     */       }
/* 340 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Map getUserOverrides() {
/* 345 */     return this.userOverrides;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 349 */     StringBuffer sb = new StringBuffer();
/* 350 */     sb.append(super.toString());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 355 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String extraToStringInfo() {
/* 360 */     if (this.userOverrides != null) {
/* 361 */       return "; userOverrides: " + this.userOverrides.toString();
/*     */     }
/* 363 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized void recreateConnectionTester(String className) throws Exception {
/* 369 */     if (className != null) {
/*     */       
/* 371 */       ConnectionTester ct = (ConnectionTester)Class.forName(className).newInstance();
/* 372 */       this.connectionTester = ct;
/*     */     } else {
/*     */       
/* 375 */       this.connectionTester = C3P0Registry.getDefaultConnectionTester();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/WrapperConnectionPoolDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */