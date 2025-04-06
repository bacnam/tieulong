/*     */ package com.mchange.v2.c3p0;
/*     */ 
/*     */ import com.mchange.v2.beans.BeansUtils;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import com.mchange.v2.sql.SqlUtils;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import javax.sql.ConnectionPoolDataSource;
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
/*     */ public final class DataSources
/*     */ {
/*  72 */   static final MLogger logger = MLog.getLogger(DataSources.class);
/*     */ 
/*     */ 
/*     */   
/*     */   static final Set WRAPPER_CXN_POOL_DATA_SOURCE_OVERWRITE_PROPS;
/*     */ 
/*     */   
/*     */   static final Set POOL_BACKED_DATA_SOURCE_OVERWRITE_PROPS;
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  84 */     String[] props = { "checkoutTimeout", "acquireIncrement", "acquireRetryAttempts", "acquireRetryDelay", "autoCommitOnClose", "connectionTesterClassName", "forceIgnoreUnresolvedTransactions", "idleConnectionTestPeriod", "initialPoolSize", "maxIdleTime", "maxPoolSize", "maxStatements", "maxStatementsPerConnection", "minPoolSize", "propertyCycle", "breakAfterAcquireFailure", "testConnectionOnCheckout", "testConnectionOnCheckin", "usesTraditionalReflectiveProxies", "preferredTestQuery", "automaticTestTable", "factoryClassLocation" };
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
/* 110 */     WRAPPER_CXN_POOL_DATA_SOURCE_OVERWRITE_PROPS = Collections.unmodifiableSet(new HashSet(Arrays.asList((Object[])props)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 117 */     props = new String[] { "numHelperThreads", "factoryClassLocation" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     POOL_BACKED_DATA_SOURCE_OVERWRITE_PROPS = Collections.unmodifiableSet(new HashSet(Arrays.asList((Object[])props)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataSource unpooledDataSource() throws SQLException {
/* 132 */     DriverManagerDataSource out = new DriverManagerDataSource();
/* 133 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   public static DataSource unpooledDataSource(String jdbcUrl) throws SQLException {
/* 138 */     DriverManagerDataSource out = new DriverManagerDataSource();
/* 139 */     out.setJdbcUrl(jdbcUrl);
/* 140 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataSource unpooledDataSource(String jdbcUrl, String user, String password) throws SQLException {
/* 148 */     Properties props = new Properties();
/* 149 */     props.put("user", user);
/* 150 */     props.put("password", password);
/* 151 */     return unpooledDataSource(jdbcUrl, props);
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
/*     */ 
/*     */   
/*     */   public static DataSource unpooledDataSource(String jdbcUrl, Properties driverProps) throws SQLException {
/* 165 */     DriverManagerDataSource out = new DriverManagerDataSource();
/* 166 */     out.setJdbcUrl(jdbcUrl);
/* 167 */     out.setProperties(driverProps);
/* 168 */     return out;
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
/*     */   public static DataSource pooledDataSource(DataSource unpooledDataSource) throws SQLException {
/* 180 */     return pooledDataSource(unpooledDataSource, null, (Map)null);
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
/*     */ 
/*     */   
/*     */   public static DataSource pooledDataSource(DataSource unpooledDataSource, int statement_cache_size) throws SQLException {
/* 194 */     Map<Object, Object> overrideProps = new HashMap<Object, Object>();
/* 195 */     overrideProps.put("maxStatements", new Integer(statement_cache_size));
/* 196 */     return pooledDataSource(unpooledDataSource, null, overrideProps);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataSource pooledDataSource(DataSource unpooledDataSource, PoolConfig pcfg) throws SQLException {
/*     */     try {
/* 212 */       WrapperConnectionPoolDataSource wcpds = new WrapperConnectionPoolDataSource();
/* 213 */       wcpds.setNestedDataSource(unpooledDataSource);
/*     */ 
/*     */       
/* 216 */       BeansUtils.overwriteSpecificAccessibleProperties(pcfg, wcpds, WRAPPER_CXN_POOL_DATA_SOURCE_OVERWRITE_PROPS);
/*     */       
/* 218 */       PoolBackedDataSource nascent_pbds = new PoolBackedDataSource();
/* 219 */       nascent_pbds.setConnectionPoolDataSource(wcpds);
/* 220 */       BeansUtils.overwriteSpecificAccessibleProperties(pcfg, nascent_pbds, POOL_BACKED_DATA_SOURCE_OVERWRITE_PROPS);
/*     */       
/* 222 */       return nascent_pbds;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 230 */     catch (Exception e) {
/*     */ 
/*     */       
/* 233 */       SQLException sqle = SqlUtils.toSQLException("Exception configuring pool-backed DataSource: " + e, e);
/* 234 */       if (logger.isLoggable(MLevel.FINE) && e != sqle)
/* 235 */         logger.log(MLevel.FINE, "Converted exception to throwable SQLException", e); 
/* 236 */       throw sqle;
/*     */     } 
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
/*     */   public static DataSource pooledDataSource(DataSource unpooledDataSource, String configName) throws SQLException {
/* 259 */     return pooledDataSource(unpooledDataSource, configName, null);
/*     */   }
/*     */   public static DataSource pooledDataSource(DataSource unpooledDataSource, Map overrideProps) throws SQLException {
/* 262 */     return pooledDataSource(unpooledDataSource, null, overrideProps);
/*     */   }
/*     */ 
/*     */   
/*     */   public static DataSource pooledDataSource(DataSource unpooledDataSource, String configName, Map overrideProps) throws SQLException {
/*     */     try {
/* 268 */       WrapperConnectionPoolDataSource wcpds = new WrapperConnectionPoolDataSource(configName);
/* 269 */       wcpds.setNestedDataSource(unpooledDataSource);
/* 270 */       if (overrideProps != null) {
/* 271 */         BeansUtils.overwriteAccessiblePropertiesFromMap(overrideProps, wcpds, false, null, true, MLevel.WARNING, MLevel.WARNING, false);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 280 */       PoolBackedDataSource nascent_pbds = new PoolBackedDataSource(configName);
/* 281 */       nascent_pbds.setConnectionPoolDataSource(wcpds);
/* 282 */       if (overrideProps != null) {
/* 283 */         BeansUtils.overwriteAccessiblePropertiesFromMap(overrideProps, nascent_pbds, false, null, true, MLevel.WARNING, MLevel.WARNING, false);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 292 */       return nascent_pbds;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 300 */     catch (Exception e) {
/*     */ 
/*     */       
/* 303 */       SQLException sqle = SqlUtils.toSQLException("Exception configuring pool-backed DataSource: " + e, e);
/* 304 */       if (logger.isLoggable(MLevel.FINE) && e != sqle)
/* 305 */         logger.log(MLevel.FINE, "Converted exception to throwable SQLException", e); 
/* 306 */       throw sqle;
/*     */     } 
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
/*     */ 
/*     */   
/*     */   public static DataSource pooledDataSource(DataSource unpooledDataSource, Properties props) throws SQLException {
/* 321 */     Properties peeledProps = new Properties();
/* 322 */     for (Enumeration<?> e = props.propertyNames(); e.hasMoreElements(); ) {
/*     */       
/* 324 */       String propKey = (String)e.nextElement();
/* 325 */       String propVal = props.getProperty(propKey);
/* 326 */       String peeledKey = propKey.startsWith("c3p0.") ? propKey.substring(5) : propKey;
/* 327 */       peeledProps.put(peeledKey, propVal);
/*     */     } 
/* 329 */     return pooledDataSource(unpooledDataSource, null, peeledProps);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void destroy(DataSource pooledDataSource) throws SQLException {
/* 348 */     destroy(pooledDataSource, false);
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
/*     */   
/*     */   public static void forceDestroy(DataSource pooledDataSource) throws SQLException {
/* 361 */     destroy(pooledDataSource, true);
/*     */   }
/*     */   
/*     */   private static void destroy(DataSource pooledDataSource, boolean force) throws SQLException {
/* 365 */     if (pooledDataSource instanceof PoolBackedDataSource) {
/*     */       
/* 367 */       ConnectionPoolDataSource cpds = ((PoolBackedDataSource)pooledDataSource).getConnectionPoolDataSource();
/* 368 */       if (cpds instanceof WrapperConnectionPoolDataSource)
/* 369 */         destroy(((WrapperConnectionPoolDataSource)cpds).getNestedDataSource(), force); 
/*     */     } 
/* 371 */     if (pooledDataSource instanceof PooledDataSource)
/* 372 */       ((PooledDataSource)pooledDataSource).close(force); 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/DataSources.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */