/*     */ package com.mchange.v2.c3p0.impl;
/*     */ 
/*     */ import com.mchange.v1.db.sql.ResultSetUtils;
/*     */ import com.mchange.v1.db.sql.StatementUtils;
/*     */ import com.mchange.v2.c3p0.AbstractConnectionTester;
/*     */ import com.mchange.v2.c3p0.cfg.C3P0Config;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import java.lang.reflect.Field;
/*     */ import java.sql.Connection;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultConnectionTester
/*     */   extends AbstractConnectionTester
/*     */ {
/*  50 */   static final MLogger logger = MLog.getLogger(DefaultConnectionTester.class);
/*     */   
/*     */   static final int IS_VALID_TIMEOUT = 0;
/*     */   
/*     */   static final String CONNECTION_TESTING_URL = "http://www.mchange.com/projects/c3p0/#configuring_connection_testing";
/*  55 */   static final int HASH_CODE = DefaultConnectionTester.class.getName().hashCode();
/*     */ 
/*     */ 
/*     */   
/*     */   static final Set INVALID_DB_STATES;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   static final QuerylessTestRunner METADATA_TABLESEARCH = new QuerylessTestRunner()
/*     */     {
/*     */ 
/*     */ 
/*     */       
/*     */       public int activeCheckConnectionNoQuery(Connection c, Throwable[] rootCauseOutParamHolder)
/*     */       {
/*  71 */         ResultSet rs = null;
/*     */         
/*     */         try {
/*  74 */           rs = c.getMetaData().getTables(null, null, "PROBABLYNOT", new String[] { "TABLE" });
/*     */ 
/*     */ 
/*     */           
/*  78 */           return 0;
/*     */         }
/*  80 */         catch (SQLException e) {
/*     */           
/*  82 */           if (DefaultConnectionTester.logger.isLoggable(MLevel.FINE)) {
/*  83 */             DefaultConnectionTester.logger.log(MLevel.FINE, "Connection " + c + " failed default system-table Connection test with an Exception!", e);
/*     */           }
/*  85 */           if (rootCauseOutParamHolder != null) {
/*  86 */             rootCauseOutParamHolder[0] = e;
/*     */           }
/*  88 */           String state = e.getSQLState();
/*  89 */           if (DefaultConnectionTester.INVALID_DB_STATES.contains(state)) {
/*     */             
/*  91 */             if (DefaultConnectionTester.logger.isLoggable(MLevel.WARNING)) {
/*  92 */               DefaultConnectionTester.logger.log(MLevel.WARNING, "SQL State '" + state + "' of Exception which occurred during a Connection test (fallback DatabaseMetaData test) implies that the database is invalid, " + "and the pool should refill itself with fresh Connections.", e);
/*     */             }
/*     */ 
/*     */             
/*  96 */             return -8;
/*     */           } 
/*     */           
/*  99 */           return -1;
/*     */         }
/* 101 */         catch (Exception e) {
/*     */           
/* 103 */           if (DefaultConnectionTester.logger.isLoggable(MLevel.FINE)) {
/* 104 */             DefaultConnectionTester.logger.log(MLevel.FINE, "Connection " + c + " failed default system-table Connection test with an Exception!", e);
/*     */           }
/* 106 */           if (rootCauseOutParamHolder != null) {
/* 107 */             rootCauseOutParamHolder[0] = e;
/*     */           }
/* 109 */           return -1;
/*     */         }
/*     */         finally {
/*     */           
/* 113 */           ResultSetUtils.attemptClose(rs);
/*     */         } 
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 120 */   static final QuerylessTestRunner IS_VALID = new QuerylessTestRunner()
/*     */     {
/*     */       
/*     */       public int activeCheckConnectionNoQuery(Connection c, Throwable[] rootCauseOutParamHolder)
/*     */       {
/*     */         try {
/* 126 */           boolean okay = c.isValid(0);
/* 127 */           if (okay) {
/* 128 */             return 0;
/*     */           }
/*     */           
/* 131 */           if (rootCauseOutParamHolder != null)
/* 132 */             rootCauseOutParamHolder[0] = new SQLException("Connection.isValid(0) returned false."); 
/* 133 */           return -1;
/*     */         
/*     */         }
/* 136 */         catch (SQLException e) {
/*     */           
/* 138 */           if (rootCauseOutParamHolder != null) {
/* 139 */             rootCauseOutParamHolder[0] = e;
/*     */           }
/* 141 */           String state = e.getSQLState();
/* 142 */           if (DefaultConnectionTester.INVALID_DB_STATES.contains(state)) {
/*     */             
/* 144 */             if (DefaultConnectionTester.logger.isLoggable(MLevel.WARNING)) {
/* 145 */               DefaultConnectionTester.logger.log(MLevel.WARNING, "SQL State '" + state + "' of Exception which occurred during a Connection test (fallback DatabaseMetaData test) implies that the database is invalid, " + "and the pool should refill itself with fresh Connections.", e);
/*     */             }
/*     */ 
/*     */             
/* 149 */             return -8;
/*     */           } 
/*     */           
/* 152 */           return -1;
/*     */         }
/* 154 */         catch (Exception e) {
/*     */           
/* 156 */           if (rootCauseOutParamHolder != null) {
/* 157 */             rootCauseOutParamHolder[0] = e;
/*     */           }
/* 159 */           return -1;
/*     */         } 
/*     */       }
/*     */     };
/*     */   
/* 164 */   static final QuerylessTestRunner SWITCH = new QuerylessTestRunner()
/*     */     {
/*     */       public int activeCheckConnectionNoQuery(Connection c, Throwable[] rootCauseOutParamHolder)
/*     */       {
/*     */         int i;
/*     */         try {
/* 170 */           i = DefaultConnectionTester.IS_VALID.activeCheckConnectionNoQuery(c, rootCauseOutParamHolder);
/* 171 */         } catch (AbstractMethodError e) {
/* 172 */           i = DefaultConnectionTester.METADATA_TABLESEARCH.activeCheckConnectionNoQuery(c, rootCauseOutParamHolder);
/* 173 */         }  return i;
/*     */       }
/*     */     };
/*     */   
/* 177 */   static final QuerylessTestRunner THREAD_LOCAL = new ThreadLocalQuerylessTestRunner();
/*     */   
/*     */   private static final String PROP_KEY = "com.mchange.v2.c3p0.impl.DefaultConnectionTester.querylessTestRunner";
/*     */   
/*     */   private final QuerylessTestRunner querylessTestRunner;
/*     */   
/*     */   private static QuerylessTestRunner reflectTestRunner(String propval) {
/*     */     try {
/* 185 */       if (propval.indexOf('.') >= 0) {
/* 186 */         return (QuerylessTestRunner)Class.forName(propval).newInstance();
/*     */       }
/*     */       
/* 189 */       Field staticField = DefaultConnectionTester.class.getDeclaredField(propval);
/* 190 */       return (QuerylessTestRunner)staticField.get(null);
/*     */     
/*     */     }
/* 193 */     catch (Exception e) {
/*     */       
/* 195 */       if (logger.isLoggable(MLevel.WARNING))
/* 196 */         logger.log(MLevel.WARNING, "Specified QuerylessTestRunner '" + propval + "' could not be found or instantiated. Reverting to default 'SWITCH'", e); 
/* 197 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 204 */     Set<String> temp = new HashSet();
/* 205 */     temp.add("08001");
/* 206 */     temp.add("08007");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 213 */     INVALID_DB_STATES = Collections.unmodifiableSet(temp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultConnectionTester() {
/* 224 */     QuerylessTestRunner defaultQuerylessTestRunner = SWITCH;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 230 */     String prop = C3P0Config.getMultiPropertiesConfig().getProperty("com.mchange.v2.c3p0.impl.DefaultConnectionTester.querylessTestRunner");
/* 231 */     if (prop == null) {
/* 232 */       this.querylessTestRunner = defaultQuerylessTestRunner;
/*     */     } else {
/*     */       
/* 235 */       QuerylessTestRunner reflected = reflectTestRunner(prop.trim());
/* 236 */       this.querylessTestRunner = (reflected != null) ? reflected : defaultQuerylessTestRunner;
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
/*     */   public int activeCheckConnection(Connection c, String query, Throwable[] rootCauseOutParamHolder) {
/* 248 */     if (query == null) {
/* 249 */       return this.querylessTestRunner.activeCheckConnectionNoQuery(c, rootCauseOutParamHolder);
/*     */     }
/*     */     
/* 252 */     Statement stmt = null;
/* 253 */     ResultSet rs = null;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 259 */       stmt = c.createStatement();
/* 260 */       rs = stmt.executeQuery(query);
/*     */       
/* 262 */       return 0;
/*     */     }
/* 264 */     catch (SQLException e) {
/*     */       
/* 266 */       if (logger.isLoggable(MLevel.FINE)) {
/* 267 */         logger.log(MLevel.FINE, "Connection " + c + " failed Connection test with an Exception! [query=" + query + "]", e);
/*     */       }
/* 269 */       if (rootCauseOutParamHolder != null) {
/* 270 */         rootCauseOutParamHolder[0] = e;
/*     */       }
/* 272 */       String state = e.getSQLState();
/* 273 */       if (INVALID_DB_STATES.contains(state)) {
/*     */         
/* 275 */         if (logger.isLoggable(MLevel.WARNING)) {
/* 276 */           logger.log(MLevel.WARNING, "SQL State '" + state + "' of Exception which occurred during a Connection test (test with query '" + query + "') implies that the database is invalid, " + "and the pool should refill itself with fresh Connections.", e);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 281 */         return -8;
/*     */       } 
/*     */       
/* 284 */       return -1;
/*     */     }
/* 286 */     catch (Exception e) {
/*     */       
/* 288 */       if (logger.isLoggable(MLevel.FINE)) {
/* 289 */         logger.log(MLevel.FINE, "Connection " + c + " failed Connection test with an Exception!", e);
/*     */       }
/* 291 */       if (rootCauseOutParamHolder != null) {
/* 292 */         rootCauseOutParamHolder[0] = e;
/*     */       }
/* 294 */       return -1;
/*     */     }
/*     */     finally {
/*     */       
/* 298 */       ResultSetUtils.attemptClose(rs);
/* 299 */       StatementUtils.attemptClose(stmt);
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
/*     */   public int statusOnException(Connection c, Throwable t, String query, Throwable[] rootCauseOutParamHolder) {
/* 312 */     if (logger.isLoggable(MLevel.FINER)) {
/* 313 */       logger.log(MLevel.FINER, "Testing a Connection in response to an Exception:", t);
/*     */     }
/*     */     
/*     */     try {
/* 317 */       if (t instanceof SQLException) {
/*     */         
/* 319 */         String state = ((SQLException)t).getSQLState();
/* 320 */         if (INVALID_DB_STATES.contains(state)) {
/*     */           
/* 322 */           if (logger.isLoggable(MLevel.WARNING)) {
/* 323 */             logger.log(MLevel.WARNING, "SQL State '" + state + "' of Exception tested by statusOnException() implies that the database is invalid, " + "and the pool should refill itself with fresh Connections.", t);
/*     */           }
/*     */ 
/*     */           
/* 327 */           return -8;
/*     */         } 
/*     */         
/* 330 */         return activeCheckConnection(c, query, rootCauseOutParamHolder);
/*     */       } 
/*     */ 
/*     */       
/* 334 */       if (logger.isLoggable(MLevel.FINE))
/* 335 */         logger.log(MLevel.FINE, "Connection test failed because test-provoking Throwable is an unexpected, non-SQLException.", t); 
/* 336 */       if (rootCauseOutParamHolder != null)
/* 337 */         rootCauseOutParamHolder[0] = t; 
/* 338 */       return -1;
/*     */     
/*     */     }
/* 341 */     catch (Exception e) {
/*     */       
/* 343 */       if (logger.isLoggable(MLevel.FINE)) {
/* 344 */         logger.log(MLevel.FINE, "Connection " + c + " failed Connection test with an Exception!", e);
/*     */       }
/* 346 */       if (rootCauseOutParamHolder != null) {
/* 347 */         rootCauseOutParamHolder[0] = e;
/*     */       }
/* 349 */       return -1;
/*     */     } finally {}
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
/*     */   private static String queryInfo(String query) {
/* 362 */     return (query == null) ? "[using Connection.isValid(...) if supported, or else traditional default query]" : ("[query=" + query + "]");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 367 */     return (o != null && o.getClass() == DefaultConnectionTester.class);
/*     */   }
/*     */   public int hashCode() {
/* 370 */     return HASH_CODE;
/*     */   }
/*     */   
/*     */   public static interface QuerylessTestRunner {
/*     */     int activeCheckConnectionNoQuery(Connection param1Connection, Throwable[] param1ArrayOfThrowable);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/DefaultConnectionTester.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */