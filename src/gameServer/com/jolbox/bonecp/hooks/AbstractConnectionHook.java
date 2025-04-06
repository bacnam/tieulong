/*     */ package com.jolbox.bonecp.hooks;
/*     */ 
/*     */ import com.jolbox.bonecp.ConnectionHandle;
/*     */ import com.jolbox.bonecp.PoolUtil;
/*     */ import com.jolbox.bonecp.StatementHandle;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractConnectionHook
/*     */   implements ConnectionHook
/*     */ {
/*  36 */   private static final Logger logger = LoggerFactory.getLogger(AbstractConnectionHook.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onAcquire(ConnectionHandle connection) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onCheckIn(ConnectionHandle connection) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onCheckOut(ConnectionHandle connection) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDestroy(ConnectionHandle connection) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean onAcquireFail(Throwable t, AcquireFailConfig acquireConfig) {
/*  75 */     boolean tryAgain = false;
/*  76 */     String log = acquireConfig.getLogMessage();
/*  77 */     logger.error(log + " Sleeping for " + acquireConfig.getAcquireRetryDelayInMs() + "ms and trying again. Attempts left: " + acquireConfig.getAcquireRetryAttempts() + ". Exception: " + t.getCause());
/*     */     
/*     */     try {
/*  80 */       Thread.sleep(acquireConfig.getAcquireRetryDelayInMs());
/*  81 */       if (acquireConfig.getAcquireRetryAttempts().get() > 0) {
/*  82 */         tryAgain = (acquireConfig.getAcquireRetryAttempts().decrementAndGet() > 0);
/*     */       }
/*  84 */     } catch (Exception e) {
/*  85 */       tryAgain = false;
/*     */     } 
/*     */     
/*  88 */     return tryAgain;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onQueryExecuteTimeLimitExceeded(ConnectionHandle handle, Statement statement, String sql, Map<Object, Object> logParams, long timeElapsedInNs) {
/*  94 */     onQueryExecuteTimeLimitExceeded(handle, statement, sql, logParams);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void onQueryExecuteTimeLimitExceeded(ConnectionHandle handle, Statement statement, String sql, Map<Object, Object> logParams) {
/* 105 */     onQueryExecuteTimeLimitExceeded(sql, logParams);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void onQueryExecuteTimeLimitExceeded(String sql, Map<Object, Object> logParams) {
/* 110 */     StringBuilder sb = new StringBuilder("Query execute time limit exceeded. Query: ");
/* 111 */     sb.append(PoolUtil.fillLogParams(sql, logParams));
/* 112 */     logger.warn(sb.toString());
/*     */   }
/*     */   
/*     */   public boolean onConnectionException(ConnectionHandle connection, String state, Throwable t) {
/* 116 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onBeforeStatementExecute(ConnectionHandle conn, StatementHandle statement, String sql, Map<Object, Object> params) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onAfterStatementExecute(ConnectionHandle conn, StatementHandle statement, String sql, Map<Object, Object> params) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public ConnectionState onMarkPossiblyBroken(ConnectionHandle connection, String state, SQLException e) {
/* 131 */     return ConnectionState.NOP;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/hooks/AbstractConnectionHook.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */