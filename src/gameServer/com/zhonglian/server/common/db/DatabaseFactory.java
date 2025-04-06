/*     */ package com.zhonglian.server.common.db;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import com.jolbox.bonecp.BoneCPDataSource;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import java.io.PrintWriter;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DatabaseFactory
/*     */   implements IDBConnectionFactory
/*     */ {
/*  22 */   private BoneCPDataSource _source = null;
/*     */   
/*     */   private static final String db_driver = "com.mysql.jdbc.Driver";
/*     */   private String _url;
/*     */   
/*     */   private void setDataSourceParams() {
/*  28 */     int maxconnPerPartition = Integer.getInteger("boneCP.maxconnPerPartition", 20).intValue();
/*  29 */     int minconnPerPartition = Integer.getInteger("boneCP.minconnPerPartition", 10).intValue();
/*  30 */     this._source.setMaxConnectionsPerPartition(maxconnPerPartition);
/*  31 */     this._source.setMinConnectionsPerPartition(minconnPerPartition);
/*  32 */     this._source.setIdleConnectionTestPeriodInMinutes(2L);
/*  33 */     this._source.setIdleMaxAgeInMinutes(6L);
/*  34 */     this._source.setAcquireIncrement(5);
/*  35 */     this._source.setReleaseHelperThreads(3);
/*     */   }
/*     */   
/*     */   public DatabaseFactory(String url, String user, String password) {
/*  39 */     CommLog.info("数据库连接地址：{}", url);
/*  40 */     this._url = url;
/*  41 */     this._source = new BoneCPDataSource();
/*  42 */     this._source.setDriverClass("com.mysql.jdbc.Driver");
/*  43 */     this._source.setJdbcUrl(url);
/*  44 */     this._source.setUsername(user);
/*  45 */     this._source.setPassword(password);
/*  46 */     setDataSourceParams();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCatalog() {
/*  51 */     Connection conn = null;
/*     */     try {
/*  53 */       conn = this._source.getConnection();
/*  54 */       return conn.getCatalog();
/*  55 */     } catch (SQLException e) {
/*  56 */       CommLog.error(e.getMessage(), e);
/*     */     } finally {
/*  58 */       if (conn != null) {
/*     */         try {
/*  60 */           conn.close();
/*  61 */         } catch (SQLException ex) {
/*  62 */           CommLog.error(ex.getMessage(), ex);
/*     */         } 
/*     */       }
/*     */     } 
/*  66 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public PrintWriter getLogWriter() throws SQLException {
/*  71 */     return this._source.getLogWriter();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLogWriter(PrintWriter out) throws SQLException {
/*  76 */     this._source.setLogWriter(out);
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() {
/*     */     try {
/*  82 */       this._source.close();
/*  83 */       this._source.setLogWriter(null);
/*  84 */     } catch (Throwable ex) {
/*  85 */       CommLog.error(null, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Connection getConnection() {
/*     */     try {
/*  92 */       long ms = CommTime.nowMS();
/*  93 */       Connection conn = this._source.getConnection();
/*  94 */       long usedMs = CommTime.nowMS() - ms;
/*  95 */       if (usedMs > 100L) {
/*  96 */         CommLog.warn("DatabaseFactory getConnection() used {} ms, url {}", Long.valueOf(usedMs), this._url);
/*     */       }
/*  98 */       return conn;
/*  99 */     } catch (SQLException ex) {
/* 100 */       CommLog.error(null, ex);
/*     */       
/* 102 */       return null;
/*     */     } 
/*     */   }
/*     */   public int getUsedConnectCount() {
/* 106 */     return this._source.getTotalLeased();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/db/DatabaseFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */