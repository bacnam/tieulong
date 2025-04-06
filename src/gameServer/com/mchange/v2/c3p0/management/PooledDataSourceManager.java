/*     */ package com.mchange.v2.c3p0.management;
/*     */ 
/*     */ import com.mchange.v2.c3p0.PooledDataSource;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PooledDataSourceManager
/*     */   implements PooledDataSourceManagerMBean
/*     */ {
/*     */   PooledDataSource pds;
/*     */   
/*     */   public PooledDataSourceManager(PooledDataSource pds) {
/*  47 */     this.pds = pds;
/*     */   }
/*     */   public String getIdentityToken() {
/*  50 */     return this.pds.getIdentityToken();
/*     */   }
/*     */   public String getDataSourceName() {
/*  53 */     return this.pds.getDataSourceName();
/*     */   }
/*     */   public void setDataSourceName(String dataSourceName) {
/*  56 */     this.pds.setDataSourceName(dataSourceName);
/*     */   }
/*     */   public int getNumConnectionsDefaultUser() throws SQLException {
/*  59 */     return this.pds.getNumConnectionsDefaultUser();
/*     */   }
/*     */   public int getNumIdleConnectionsDefaultUser() throws SQLException {
/*  62 */     return this.pds.getNumIdleConnectionsDefaultUser();
/*     */   }
/*     */   public int getNumBusyConnectionsDefaultUser() throws SQLException {
/*  65 */     return this.pds.getNumBusyConnectionsDefaultUser();
/*     */   }
/*     */   public int getNumUnclosedOrphanedConnectionsDefaultUser() throws SQLException {
/*  68 */     return this.pds.getNumUnclosedOrphanedConnectionsDefaultUser();
/*     */   }
/*     */   public float getEffectivePropertyCycleDefaultUser() throws SQLException {
/*  71 */     return this.pds.getEffectivePropertyCycleDefaultUser();
/*     */   }
/*     */   public int getThreadPoolSize() throws SQLException {
/*  74 */     return this.pds.getThreadPoolSize();
/*     */   }
/*     */   public int getThreadPoolNumActiveThreads() throws SQLException {
/*  77 */     return this.pds.getThreadPoolNumActiveThreads();
/*     */   }
/*     */   public int getThreadPoolNumIdleThreads() throws SQLException {
/*  80 */     return this.pds.getThreadPoolNumIdleThreads();
/*     */   }
/*     */   public int getThreadPoolNumTasksPending() throws SQLException {
/*  83 */     return this.pds.getThreadPoolNumTasksPending();
/*     */   }
/*     */   public String sampleThreadPoolStackTraces() throws SQLException {
/*  86 */     return this.pds.sampleThreadPoolStackTraces();
/*     */   }
/*     */   public String sampleThreadPoolStatus() throws SQLException {
/*  89 */     return this.pds.sampleThreadPoolStatus();
/*     */   }
/*     */   public void softResetDefaultUser() throws SQLException {
/*  92 */     this.pds.softResetDefaultUser();
/*     */   }
/*     */   public int getNumConnections(String username, String password) throws SQLException {
/*  95 */     return this.pds.getNumConnections(username, password);
/*     */   }
/*     */   public int getNumIdleConnections(String username, String password) throws SQLException {
/*  98 */     return this.pds.getNumIdleConnections(username, password);
/*     */   }
/*     */   public int getNumBusyConnections(String username, String password) throws SQLException {
/* 101 */     return this.pds.getNumBusyConnections(username, password);
/*     */   }
/*     */   public int getNumUnclosedOrphanedConnections(String username, String password) throws SQLException {
/* 104 */     return this.pds.getNumUnclosedOrphanedConnections(username, password);
/*     */   }
/*     */   public float getEffectivePropertyCycle(String username, String password) throws SQLException {
/* 107 */     return this.pds.getEffectivePropertyCycle(username, password);
/*     */   }
/*     */   public void softReset(String username, String password) throws SQLException {
/* 110 */     this.pds.softReset(username, password);
/*     */   }
/*     */   public int getNumBusyConnectionsAllUsers() throws SQLException {
/* 113 */     return this.pds.getNumBusyConnectionsAllUsers();
/*     */   }
/*     */   public int getNumIdleConnectionsAllUsers() throws SQLException {
/* 116 */     return this.pds.getNumIdleConnectionsAllUsers();
/*     */   }
/*     */   public int getNumConnectionsAllUsers() throws SQLException {
/* 119 */     return this.pds.getNumConnectionsAllUsers();
/*     */   }
/*     */   public int getNumUnclosedOrphanedConnectionsAllUsers() throws SQLException {
/* 122 */     return this.pds.getNumUnclosedOrphanedConnectionsAllUsers();
/*     */   }
/*     */   public void softResetAllUsers() throws SQLException {
/* 125 */     this.pds.softResetAllUsers();
/*     */   }
/*     */   public int getNumUserPools() throws SQLException {
/* 128 */     return this.pds.getNumUserPools();
/*     */   }
/*     */   public Collection getAllUsers() throws SQLException {
/* 131 */     return this.pds.getAllUsers();
/*     */   }
/*     */   public void hardReset() throws SQLException {
/* 134 */     this.pds.hardReset();
/*     */   }
/*     */   public void close() throws SQLException {
/* 137 */     this.pds.close();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/management/PooledDataSourceManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */