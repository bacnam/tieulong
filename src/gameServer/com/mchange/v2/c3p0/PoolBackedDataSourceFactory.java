/*     */ package com.mchange.v2.c3p0;
/*     */ 
/*     */ import com.mchange.v2.sql.SqlUtils;
/*     */ import java.sql.SQLException;
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
/*     */ public final class PoolBackedDataSourceFactory
/*     */ {
/*     */   public static DataSource createReferenceable(DataSource unpooledDataSource, int minPoolSize, int maxPoolSize, int acquireIncrement, int maxIdleTime, int maxStatements, String factoryLocation) throws SQLException {
/*     */     try {
/*  87 */       WrapperConnectionPoolDataSource cpds = new WrapperConnectionPoolDataSource();
/*  88 */       cpds.setNestedDataSource(unpooledDataSource);
/*  89 */       cpds.setMinPoolSize(minPoolSize);
/*  90 */       cpds.setMaxPoolSize(maxPoolSize);
/*  91 */       cpds.setAcquireIncrement(acquireIncrement);
/*  92 */       cpds.setMaxIdleTime(maxIdleTime);
/*  93 */       cpds.setMaxStatements(maxStatements);
/*  94 */       cpds.setFactoryClassLocation(factoryLocation);
/*     */ 
/*     */       
/*  97 */       PoolBackedDataSource out = new PoolBackedDataSource();
/*  98 */       out.setConnectionPoolDataSource(cpds);
/*  99 */       return out;
/*     */     }
/* 101 */     catch (Exception e) {
/* 102 */       throw SqlUtils.toSQLException(e);
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
/*     */   public static DataSource createReferenceable(DataSource unpooledDataSource, String factoryLocation) throws SQLException {
/*     */     try {
/* 126 */       WrapperConnectionPoolDataSource cpds = new WrapperConnectionPoolDataSource();
/* 127 */       cpds.setNestedDataSource(unpooledDataSource);
/* 128 */       cpds.setFactoryClassLocation(factoryLocation);
/*     */       
/* 130 */       PoolBackedDataSource out = new PoolBackedDataSource();
/* 131 */       out.setConnectionPoolDataSource(cpds);
/* 132 */       return out;
/*     */     }
/* 134 */     catch (Exception e) {
/* 135 */       throw SqlUtils.toSQLException(e);
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
/*     */   public static DataSource createReferenceable(String jdbcDriverClass, String jdbcUrl, String user, String password, int minPoolSize, int maxPoolSize, int acquireIncrement, int maxIdleTime, int maxStatements, String factoryLocation) throws SQLException {
/* 175 */     DataSource nested = DriverManagerDataSourceFactory.create(jdbcDriverClass, jdbcUrl, user, password);
/*     */ 
/*     */ 
/*     */     
/* 179 */     return createReferenceable(nested, minPoolSize, maxPoolSize, acquireIncrement, maxIdleTime, maxStatements, factoryLocation);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataSource createReferenceable(String jdbcDriverClass, String jdbcUrl, String user, String password, String factoryLocation) throws SQLException {
/* 210 */     DataSource nested = DriverManagerDataSourceFactory.create(jdbcDriverClass, jdbcUrl, user, password);
/*     */ 
/*     */ 
/*     */     
/* 214 */     return createReferenceable(nested, factoryLocation);
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
/*     */   public static DataSource createSerializable(DataSource unpooledDataSource, int minPoolSize, int maxPoolSize, int acquireIncrement, int maxIdleTime, int maxStatements) throws SQLException {
/*     */     try {
/* 251 */       WrapperConnectionPoolDataSource cpds = new WrapperConnectionPoolDataSource();
/* 252 */       cpds.setNestedDataSource(unpooledDataSource);
/* 253 */       cpds.setMinPoolSize(minPoolSize);
/* 254 */       cpds.setMaxPoolSize(maxPoolSize);
/* 255 */       cpds.setAcquireIncrement(acquireIncrement);
/* 256 */       cpds.setMaxIdleTime(maxIdleTime);
/* 257 */       cpds.setMaxStatements(maxStatements);
/*     */       
/* 259 */       PoolBackedDataSource out = new PoolBackedDataSource();
/* 260 */       out.setConnectionPoolDataSource(cpds);
/* 261 */       return out;
/*     */     }
/* 263 */     catch (Exception e) {
/* 264 */       throw SqlUtils.toSQLException(e);
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
/*     */   public static DataSource createSerializable(DataSource unpooledDataSource) throws SQLException {
/*     */     try {
/* 283 */       WrapperConnectionPoolDataSource cpds = new WrapperConnectionPoolDataSource();
/* 284 */       cpds.setNestedDataSource(unpooledDataSource);
/*     */       
/* 286 */       PoolBackedDataSource out = new PoolBackedDataSource();
/* 287 */       out.setConnectionPoolDataSource(cpds);
/* 288 */       return out;
/*     */     }
/* 290 */     catch (Exception e) {
/* 291 */       throw SqlUtils.toSQLException(e);
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
/*     */   public static DataSource createSerializable(String jdbcDriverClass, String jdbcUrl, String user, String password, int minPoolSize, int maxPoolSize, int acquireIncrement, int maxIdleTime, int maxStatements) throws SQLException {
/* 329 */     DataSource nested = DriverManagerDataSourceFactory.create(jdbcDriverClass, jdbcUrl, user, password);
/*     */ 
/*     */ 
/*     */     
/* 333 */     return createSerializable(nested, minPoolSize, maxPoolSize, acquireIncrement, maxIdleTime, maxStatements);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataSource createSerializable(String jdbcDriverClass, String jdbcUrl, String user, String password) throws SQLException {
/* 359 */     DataSource nested = DriverManagerDataSourceFactory.create(jdbcDriverClass, jdbcUrl, user, password);
/*     */ 
/*     */ 
/*     */     
/* 363 */     return createSerializable(nested);
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
/*     */   public static DataSource create(DataSource unpooledDataSource, int minPoolSize, int maxPoolSize, int acquireIncrement, int maxIdleTime, int maxStatements, String factoryLocation) throws SQLException {
/* 397 */     return createReferenceable(unpooledDataSource, minPoolSize, maxPoolSize, acquireIncrement, maxIdleTime, maxStatements, factoryLocation);
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
/*     */   public static DataSource create(DataSource unpooledDataSource, int minPoolSize, int maxPoolSize, int acquireIncrement, int maxIdleTime, int maxStatements) throws SQLException {
/* 432 */     return createReferenceable(unpooledDataSource, minPoolSize, maxPoolSize, acquireIncrement, maxIdleTime, maxStatements, null);
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
/*     */   public static DataSource create(DataSource unpooledDataSource) throws SQLException {
/* 449 */     return createSerializable(unpooledDataSource);
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
/*     */   public static DataSource create(String jdbcDriverClass, String jdbcUrl, String user, String password, int minPoolSize, int maxPoolSize, int acquireIncrement, int maxIdleTime, int maxStatements, String factoryLocation) throws SQLException {
/* 487 */     return createReferenceable(jdbcDriverClass, jdbcUrl, user, password, minPoolSize, maxPoolSize, acquireIncrement, maxIdleTime, maxStatements, factoryLocation);
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
/*     */   public static DataSource create(String jdbcDriverClass, String jdbcUrl, String user, String password, int minPoolSize, int maxPoolSize, int acquireIncrement, int maxIdleTime, int maxStatements) throws SQLException {
/* 530 */     return createReferenceable(jdbcDriverClass, jdbcUrl, user, password, minPoolSize, maxPoolSize, acquireIncrement, maxIdleTime, maxStatements, null);
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
/*     */   public static DataSource create(String jdbcUrl, String user, String password, int minPoolSize, int maxPoolSize, int acquireIncrement, int maxIdleTime, int maxStatements, String factoryLocation) throws SQLException {
/* 578 */     return create(null, jdbcUrl, user, password, minPoolSize, maxPoolSize, acquireIncrement, maxIdleTime, maxStatements, factoryLocation);
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
/*     */   public static DataSource create(String jdbcUrl, String user, String password, int minPoolSize, int maxPoolSize, int acquireIncrement, int maxIdleTime, int maxStatements) throws SQLException {
/* 622 */     return create(null, jdbcUrl, user, password, minPoolSize, maxPoolSize, acquireIncrement, maxIdleTime, maxStatements, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataSource create(String jdbcDriverClass, String jdbcUrl, String user, String password) throws SQLException {
/* 648 */     return createSerializable(jdbcDriverClass, jdbcUrl, user, password);
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
/*     */   public static DataSource create(String jdbcUrl, String user, String password) throws SQLException {
/* 670 */     return create(null, jdbcUrl, user, password);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/PoolBackedDataSourceFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */