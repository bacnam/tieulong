/*     */ package com.jolbox.bonecp;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.jolbox.bonecp.hooks.AcquireFailConfig;
/*     */ import com.jolbox.bonecp.hooks.ConnectionHook;
/*     */ import com.jolbox.bonecp.proxy.CallableStatementProxy;
/*     */ import com.jolbox.bonecp.proxy.ConnectionProxy;
/*     */ import com.jolbox.bonecp.proxy.PreparedStatementProxy;
/*     */ import com.jolbox.bonecp.proxy.StatementProxy;
/*     */ import com.jolbox.bonecp.proxy.TransactionRecoveryResult;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.sql.CallableStatement;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MemorizeTransactionProxy
/*     */   implements InvocationHandler
/*     */ {
/*     */   private Object target;
/*     */   private WeakReference<ConnectionHandle> connectionHandle;
/*  63 */   private static final ImmutableSet<String> clearLogConditions = ImmutableSet.of("rollback", "commit", "close");
/*     */   
/*  65 */   private static final Logger logger = LoggerFactory.getLogger(MemorizeTransactionProxy.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemorizeTransactionProxy() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Connection memorize(Connection target, ConnectionHandle connectionHandle) {
/*  81 */     return (Connection)Proxy.newProxyInstance(ConnectionProxy.class.getClassLoader(), new Class[] { ConnectionProxy.class }, new MemorizeTransactionProxy(target, connectionHandle));
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
/*     */   protected static Statement memorize(Statement target, ConnectionHandle connectionHandle) {
/*  93 */     return (Statement)Proxy.newProxyInstance(StatementProxy.class.getClassLoader(), new Class[] { StatementProxy.class }, new MemorizeTransactionProxy(target, connectionHandle));
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
/*     */   protected static PreparedStatement memorize(PreparedStatement target, ConnectionHandle connectionHandle) {
/* 105 */     return (PreparedStatement)Proxy.newProxyInstance(PreparedStatementProxy.class.getClassLoader(), new Class[] { PreparedStatementProxy.class }, new MemorizeTransactionProxy(target, connectionHandle));
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
/*     */   protected static CallableStatement memorize(CallableStatement target, ConnectionHandle connectionHandle) {
/* 118 */     return (CallableStatement)Proxy.newProxyInstance(CallableStatementProxy.class.getClassLoader(), new Class[] { CallableStatementProxy.class }, new MemorizeTransactionProxy(target, connectionHandle));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MemorizeTransactionProxy(Object target, ConnectionHandle connectionHandle) {
/* 129 */     this.target = target;
/* 130 */     this.connectionHandle = new WeakReference<ConnectionHandle>(connectionHandle);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 136 */     Object result = null;
/* 137 */     ConnectionHandle con = this.connectionHandle.get();
/* 138 */     if (con != null) {
/*     */       
/* 140 */       if (method.getName().equals("getProxyTarget")) {
/* 141 */         return this.target;
/*     */       }
/*     */       
/* 144 */       if (con.isInReplayMode()) {
/*     */         try {
/* 146 */           return method.invoke(this.target, args);
/* 147 */         } catch (InvocationTargetException t) {
/* 148 */           throw t.getCause();
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 154 */       if (con.recoveryResult != null) {
/* 155 */         Object remap = con.recoveryResult.getReplaceTarget().get(this.target);
/* 156 */         if (remap != null) {
/* 157 */           this.target = remap;
/*     */         }
/* 159 */         remap = con.recoveryResult.getReplaceTarget().get(con);
/* 160 */         if (remap != null) {
/* 161 */           con = (ConnectionHandle)remap;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 166 */       if (!con.isInReplayMode() && !method.getName().equals("hashCode") && !method.getName().equals("equals") && !method.getName().equals("toString"))
/*     */       {
/* 168 */         con.getReplayLog().add(new ReplayLog(this.target, method, args));
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 176 */         result = runWithPossibleProxySwap(method, this.target, args);
/*     */ 
/*     */         
/* 179 */         if (!con.isInReplayMode() && this.target instanceof Connection && clearLogConditions.contains(method.getName())) {
/* 180 */           con.getReplayLog().clear();
/*     */ 
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 186 */       catch (Throwable t) {
/*     */         
/* 188 */         List<ReplayLog> oldReplayLog = con.getReplayLog();
/* 189 */         con.setInReplayMode(true);
/*     */ 
/*     */         
/* 192 */         if (t instanceof SQLException || (t.getCause() != null && t.getCause() instanceof SQLException)) {
/* 193 */           con.markPossiblyBroken((SQLException)t.getCause());
/*     */         }
/*     */         
/* 196 */         if (!con.isPossiblyBroken()) {
/* 197 */           con.setInReplayMode(false);
/* 198 */           con.getReplayLog().clear();
/*     */         } else {
/* 200 */           logger.error("Connection failed. Attempting to recover transaction on Thread #" + Thread.currentThread().getId());
/*     */           
/*     */           try {
/* 203 */             con.recoveryResult = attemptRecovery(oldReplayLog);
/* 204 */             con.setReplayLog(oldReplayLog);
/* 205 */             con.setInReplayMode(false);
/* 206 */             logger.error("Recovery succeeded on Thread #" + Thread.currentThread().getId());
/* 207 */             con.possiblyBroken = false;
/*     */ 
/*     */             
/* 210 */             return con.recoveryResult.getResult();
/* 211 */           } catch (Throwable t2) {
/* 212 */             con.setInReplayMode(false);
/* 213 */             con.getReplayLog().clear();
/* 214 */             throw new SQLException("Could not recover transaction. Original exception follows." + t.getCause());
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 221 */         throw t.getCause();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 226 */     return result;
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
/*     */   private Object runWithPossibleProxySwap(Method method, Object target, Object[] args) throws IllegalAccessException, InvocationTargetException {
/*     */     Object result;
/* 241 */     if (method.getName().equals("createStatement")) {
/* 242 */       result = memorize((Statement)method.invoke(target, args), this.connectionHandle.get());
/*     */     }
/* 244 */     else if (method.getName().equals("prepareStatement")) {
/* 245 */       result = memorize((PreparedStatement)method.invoke(target, args), this.connectionHandle.get());
/*     */     }
/* 247 */     else if (method.getName().equals("prepareCall")) {
/* 248 */       result = memorize((CallableStatement)method.invoke(target, args), this.connectionHandle.get());
/*     */     } else {
/* 250 */       result = method.invoke(target, args);
/* 251 */     }  return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TransactionRecoveryResult attemptRecovery(List<ReplayLog> oldReplayLog) throws SQLException {
/* 261 */     boolean tryAgain = false;
/* 262 */     Throwable failedThrowable = null;
/*     */     
/* 264 */     ConnectionHandle con = this.connectionHandle.get();
/* 265 */     TransactionRecoveryResult recoveryResult = con.recoveryResult;
/* 266 */     ConnectionHook connectionHook = con.getPool().getConfig().getConnectionHook();
/*     */     
/* 268 */     int acquireRetryAttempts = con.getPool().getConfig().getAcquireRetryAttempts();
/* 269 */     long acquireRetryDelay = con.getPool().getConfig().getAcquireRetryDelayInMs();
/* 270 */     AcquireFailConfig acquireConfig = new AcquireFailConfig();
/* 271 */     acquireConfig.setAcquireRetryAttempts(new AtomicInteger(acquireRetryAttempts));
/* 272 */     acquireConfig.setAcquireRetryDelayInMs(acquireRetryDelay);
/* 273 */     acquireConfig.setLogMessage("Failed to replay transaction");
/*     */     
/* 275 */     Map<Object, Object> replaceTarget = new HashMap<Object, Object>();
/*     */     while (true) {
/* 277 */       replaceTarget.clear();
/*     */       
/* 279 */       for (Map.Entry<Object, Object> entry : (Iterable<Map.Entry<Object, Object>>)recoveryResult.getReplaceTarget().entrySet()) {
/* 280 */         replaceTarget.put(entry.getKey(), entry.getValue());
/*     */       }
/*     */       
/* 283 */       List<PreparedStatement> prepStatementTarget = new ArrayList<PreparedStatement>();
/* 284 */       List<CallableStatement> callableStatementTarget = new ArrayList<CallableStatement>();
/* 285 */       List<Statement> statementTarget = new ArrayList<Statement>();
/* 286 */       Object result = null;
/* 287 */       tryAgain = false;
/*     */       
/* 289 */       con.setInReplayMode(true);
/*     */       try {
/* 291 */         con.clearStatementCaches(true);
/* 292 */         con.getInternalConnection().close();
/* 293 */       } catch (Throwable t) {}
/*     */ 
/*     */       
/* 296 */       con.setInternalConnection(memorize(con.obtainInternalConnection(), con));
/* 297 */       con.getOriginatingPartition().trackConnectionFinalizer(con);
/*     */       
/* 299 */       Iterator<ReplayLog> i$ = oldReplayLog.iterator(); while (true) { if (i$.hasNext()) { ReplayLog replay = i$.next();
/*     */ 
/*     */           
/* 302 */           if (replay.getTarget() instanceof Connection) {
/* 303 */             replaceTarget.put(replay.getTarget(), con.getInternalConnection());
/* 304 */           } else if (replay.getTarget() instanceof CallableStatement) {
/* 305 */             if (replaceTarget.get(replay.getTarget()) == null) {
/* 306 */               replaceTarget.put(replay.getTarget(), callableStatementTarget.remove(0));
/*     */             }
/* 308 */           } else if (replay.getTarget() instanceof PreparedStatement) {
/* 309 */             if (replaceTarget.get(replay.getTarget()) == null) {
/* 310 */               replaceTarget.put(replay.getTarget(), prepStatementTarget.remove(0));
/*     */             }
/* 312 */           } else if (replay.getTarget() instanceof Statement && 
/* 313 */             replaceTarget.get(replay.getTarget()) == null) {
/* 314 */             replaceTarget.put(replay.getTarget(), statementTarget.remove(0));
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 322 */             result = runWithPossibleProxySwap(replay.getMethod(), replaceTarget.get(replay.getTarget()), replay.getArgs());
/*     */ 
/*     */             
/* 325 */             recoveryResult.setResult(result);
/*     */ 
/*     */             
/* 328 */             if (result instanceof CallableStatement) {
/* 329 */               callableStatementTarget.add((CallableStatement)result); continue;
/* 330 */             }  if (result instanceof PreparedStatement) {
/* 331 */               prepStatementTarget.add((PreparedStatement)result); continue;
/* 332 */             }  if (result instanceof Statement)
/* 333 */               statementTarget.add((Statement)result); 
/*     */             continue;
/* 335 */           } catch (Throwable t) {
/*     */ 
/*     */             
/* 338 */             if (connectionHook != null) {
/* 339 */               tryAgain = connectionHook.onAcquireFail(t, acquireConfig);
/*     */             } else {
/*     */               
/* 342 */               logger.error("Failed to replay transaction. Sleeping for " + acquireRetryDelay + "ms and trying again. Attempts left: " + acquireRetryAttempts + ". Exception: " + t.getCause());
/*     */               
/*     */               try {
/* 345 */                 Thread.sleep(acquireRetryDelay);
/* 346 */                 if (acquireRetryAttempts > 0) {
/* 347 */                   tryAgain = (--acquireRetryAttempts != 0);
/*     */                 }
/* 349 */               } catch (InterruptedException e) {
/* 350 */                 tryAgain = false;
/*     */               } 
/*     */             } 
/* 353 */             if (!tryAgain)
/* 354 */               failedThrowable = t; 
/*     */           }  }
/*     */         else
/*     */         { break; }
/*     */         
/* 359 */         if (!tryAgain) break;  }  if (!tryAgain)
/*     */         break; 
/*     */     } 
/* 362 */     for (Map.Entry<Object, Object> entry : replaceTarget.entrySet()) {
/* 363 */       recoveryResult.getReplaceTarget().put(entry.getKey(), entry.getValue());
/*     */     }
/*     */     
/* 366 */     for (ReplayLog replay : oldReplayLog) {
/* 367 */       replay.setTarget(replaceTarget.get(replay.getTarget()));
/*     */     }
/*     */     
/* 370 */     if (failedThrowable != null)
/*     */     {
/* 372 */       throw new SQLException(failedThrowable.getMessage(), failedThrowable);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 380 */     return recoveryResult;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/MemorizeTransactionProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */