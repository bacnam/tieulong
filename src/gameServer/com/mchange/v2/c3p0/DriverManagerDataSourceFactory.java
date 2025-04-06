/*     */ package com.mchange.v2.c3p0;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.Properties;
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
/*     */ public final class DriverManagerDataSourceFactory
/*     */ {
/*     */   public static DataSource create(String driverClass, String jdbcUrl, String dfltUser, String dfltPassword, String refFactoryLoc) throws SQLException {
/*  73 */     DriverManagerDataSource out = new DriverManagerDataSource();
/*  74 */     out.setDriverClass(driverClass);
/*  75 */     out.setJdbcUrl(jdbcUrl);
/*  76 */     out.setUser(dfltUser);
/*  77 */     out.setPassword(dfltPassword);
/*  78 */     out.setFactoryClassLocation(refFactoryLoc);
/*  79 */     return out;
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
/*     */   public static DataSource create(String driverClass, String jdbcUrl, Properties props, String refFactoryLoc) throws SQLException {
/*  99 */     DriverManagerDataSource out = new DriverManagerDataSource();
/* 100 */     out.setDriverClass(driverClass);
/* 101 */     out.setJdbcUrl(jdbcUrl);
/* 102 */     out.setProperties(props);
/* 103 */     out.setFactoryClassLocation(refFactoryLoc);
/* 104 */     return out;
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
/*     */   public static DataSource create(String driverClass, String jdbcUrl, String dfltUser, String dfltPassword) throws SQLException {
/* 121 */     return create(driverClass, jdbcUrl, dfltUser, dfltPassword, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataSource create(String driverClass, String jdbcUrl) throws SQLException {
/* 132 */     return create(driverClass, jdbcUrl, (String)null, (String)null);
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
/*     */   public static DataSource create(String jdbcUrl, String dfltUser, String dfltPassword) throws SQLException {
/* 148 */     return create((String)null, jdbcUrl, dfltUser, dfltPassword);
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
/*     */   public static DataSource create(String jdbcUrl) throws SQLException {
/* 162 */     return create((String)null, jdbcUrl, (String)null, (String)null);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/DriverManagerDataSourceFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */