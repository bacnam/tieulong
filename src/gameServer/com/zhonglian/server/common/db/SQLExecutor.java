/*     */ package com.zhonglian.server.common.db;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ public class SQLExecutor
/*     */ {
/*  15 */   private static final byte[] emptyBinary = new byte[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean InsertBO(IDBConnectionFactory con_pool, IBaseBO bo) {
/*  25 */     return InsertBO(con_pool, bo, null);
/*     */   }
/*     */   
/*     */   public static boolean InsertBO(IDBConnectionFactory con_pool, IBaseBO bo, ArrayList<byte[]> _binaryList) {
/*  29 */     Connection conn = null;
/*  30 */     PreparedStatement stmt = null;
/*  31 */     String SQL = null;
/*  32 */     long id = 0L;
/*  33 */     int dealCount = 0;
/*     */     try {
/*     */       Exception exception2;
/*  36 */       conn = con_pool.getConnection();
/*  37 */       SQL = "insert into " + bo.getTableName() + "(" + bo.getItemsName() + ") values (" + bo.getItemsValue() + ");";
/*     */       
/*  39 */       stmt = conn.prepareStatement(SQL, 1);
/*     */       
/*  41 */       if (_binaryList != null) {
/*  42 */         for (int i = 0; i < _binaryList.size(); i++) {
/*  43 */           byte[] binaryData = _binaryList.get(i);
/*  44 */           if (binaryData == null) {
/*  45 */             binaryData = emptyBinary;
/*     */           }
/*  47 */           stmt.setBytes(i + 1, binaryData);
/*     */         } 
/*     */       }
/*     */       
/*  51 */       dealCount = stmt.executeUpdate();
/*     */       
/*  53 */       Exception exception1 = null;
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*  58 */     catch (SQLException ex) {
/*  59 */       CommLog.error("SQLExecutor.insertBO('{}') error:{}", new Object[] { SQL, ex.getMessage(), ex });
/*     */     } finally {
/*  61 */       SQLUtil.close(null, stmt, conn);
/*     */     } 
/*     */     
/*  64 */     if (id > 0L) {
/*  65 */       bo.setId(id);
/*     */     }
/*     */     
/*  68 */     return (dealCount > 0);
/*     */   }
/*     */   
/*     */   public static int ReplaceBO(IDBConnectionFactory con_pool, IBaseBO bo) {
/*  72 */     Connection conn = null;
/*  73 */     Statement stmt = null;
/*  74 */     String SQL = null;
/*  75 */     int resultCount = 0;
/*     */     
/*     */     try {
/*  78 */       conn = con_pool.getConnection();
/*  79 */       stmt = conn.createStatement();
/*     */       
/*  81 */       SQL = GetReplaceSql(bo);
/*     */       
/*  83 */       resultCount = stmt.executeUpdate(SQL);
/*  84 */     } catch (SQLException ex) {
/*  85 */       CommLog.error("SQLExecutor.ReplaceBO('{}') error:{}", SQL, ex.getMessage());
/*  86 */       return -1;
/*     */     } finally {
/*  88 */       SQLUtil.close(null, stmt, conn);
/*     */     } 
/*     */     
/*  91 */     return resultCount;
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
/*     */   public static <T extends IBaseBO> List<T> getListByCondition(IDBConnectionFactory con_pool, Conditions condition, T vo) {
/* 107 */     List<T> res = new ArrayList<>();
/* 108 */     Connection conn = null;
/* 109 */     Statement stmt = null;
/* 110 */     ResultSet rs = null;
/* 111 */     String SQL = null;
/*     */     
/*     */     try {
/* 114 */       conn = con_pool.getConnection();
/* 115 */       stmt = conn.createStatement();
/*     */       
/* 117 */       condition.setTablesName(vo.getTableName());
/* 118 */       condition.setItemsName(vo.getItemsName());
/* 119 */       SQL = condition.getSQL();
/*     */       
/* 121 */       rs = stmt.executeQuery(SQL);
/* 122 */       while (rs.next()) {
/* 123 */         vo.getFromResultSet(rs, res);
/*     */       }
/* 125 */     } catch (Throwable ex) {
/* 126 */       CommLog.error("SQLExecutor.getListByCondition('{}') error:{}", SQL, ex.getMessage());
/* 127 */       return res;
/*     */     } finally {
/* 129 */       SQLUtil.close(rs, stmt, conn);
/*     */     } 
/*     */     
/* 132 */     return res;
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
/*     */   public static int updateByCondition(IDBConnectionFactory con_pool, Conditions condition, String updateString) {
/* 144 */     return updateByCondition(con_pool, condition, updateString, null);
/*     */   }
/*     */   
/*     */   public static int updateByCondition(IDBConnectionFactory con_pool, Conditions condition, String updateString, ArrayList<byte[]> _binaryList) {
/* 148 */     Connection conn = null;
/* 149 */     PreparedStatement stmt = null;
/* 150 */     String SQL = null;
/*     */     
/* 152 */     int resultCount = 0;
/*     */     
/*     */     try {
/* 155 */       conn = con_pool.getConnection();
/*     */       
/* 157 */       SQL = GetUpdateSql(condition, updateString);
/* 158 */       String conditionString = condition.getCondition();
/* 159 */       if (conditionString.trim().length() > 0) {
/* 160 */         SQL = String.valueOf(SQL) + " where " + conditionString;
/*     */       }
/* 162 */       SQL = String.valueOf(SQL) + ";";
/*     */       
/* 164 */       stmt = conn.prepareStatement(SQL);
/*     */       
/* 166 */       if (_binaryList != null) {
/* 167 */         for (int i = 0; i < _binaryList.size(); i++) {
/* 168 */           byte[] binaryData = _binaryList.get(i);
/* 169 */           if (binaryData == null) {
/* 170 */             binaryData = emptyBinary;
/*     */           }
/*     */           
/* 173 */           stmt.setBytes(i + 1, binaryData);
/*     */         } 
/*     */       }
/*     */       
/* 177 */       resultCount = stmt.executeUpdate();
/* 178 */     } catch (SQLException ex) {
/* 179 */       CommLog.error("SQLExecutor.updateByCondition('{}') error:{}", new Object[] { SQL, ex.getMessage(), ex });
/* 180 */       return -1;
/*     */     } finally {
/* 182 */       SQLUtil.close(null, stmt, conn);
/*     */     } 
/*     */     
/* 185 */     return resultCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int delByCondition(IDBConnectionFactory con_pool, Conditions condition) {
/* 196 */     Connection conn = null;
/* 197 */     Statement stmt = null;
/* 198 */     String SQL = null;
/*     */     
/* 200 */     int resultCount = 0;
/*     */     
/*     */     try {
/* 203 */       conn = con_pool.getConnection();
/* 204 */       stmt = conn.createStatement();
/*     */       
/* 206 */       SQL = "delete " + condition.getConditionAndTables() + ";";
/*     */       
/* 208 */       resultCount = stmt.executeUpdate(SQL);
/* 209 */     } catch (SQLException ex) {
/* 210 */       CommLog.error("SQLExecutor.delByCondition('{}') error:{}", new Object[] { SQL, ex.getMessage(), ex });
/* 211 */       return -1;
/*     */     } finally {
/* 213 */       SQLUtil.close(null, stmt, conn);
/*     */     } 
/*     */     
/* 216 */     return resultCount;
/*     */   }
/*     */   
/*     */   public static String GetUpdateSql(Conditions condition, String updateString) {
/* 220 */     return "update " + condition.getTablesName() + " set " + updateString;
/*     */   }
/*     */   
/*     */   public static <T extends IBaseBO> String GetInsertSql(List<T> bos) {
/* 224 */     if (bos.size() == 0) {
/* 225 */       return "";
/*     */     }
/* 227 */     StringBuilder sBuilder = new StringBuilder();
/* 228 */     IBaseBO bo = (IBaseBO)bos.get(0);
/* 229 */     sBuilder.append(String.format("insert into %s (%s) values (%s)", new Object[] { bo.getTableName(), bo.getItemsName(), bo.getItemsValue() }));
/*     */ 
/*     */     
/* 232 */     for (int index = 1; index < bos.size(); index++) {
/* 233 */       sBuilder.append(String.format(", (%s)", new Object[] { ((IBaseBO)bos.get(index)).getItemsValue() }));
/*     */     } 
/* 235 */     sBuilder.append(";");
/* 236 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String GetReplaceSql(IBaseBO bo) {
/* 240 */     return "replace into " + bo.getTableName() + "(" + bo.getItemsName() + ") values (" + bo.getItemsValue() + ");";
/*     */   }
/*     */   
/*     */   static void LogSQLError(Throwable e, String sql) {
/* 244 */     CommLog.error("SQLExecutor-SQL Error: [ '{}' ] {}", sql, e.getMessage());
/*     */   }
/*     */   
/*     */   public static boolean execute(String sql, IDBConnectionFactory con_pool) {
/* 248 */     Connection conn = null;
/* 249 */     Statement stmt = null;
/*     */     try {
/* 251 */       conn = con_pool.getConnection();
/* 252 */       stmt = conn.createStatement();
/* 253 */       stmt.execute(sql);
/* 254 */     } catch (SQLException e) {
/* 255 */       LogSQLError(e, sql);
/* 256 */       return false;
/*     */     } finally {
/* 258 */       SQLUtil.close(null, stmt, conn);
/*     */     } 
/* 260 */     return true;
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
/*     */   public static int executeUpdate(String sql, IDBConnectionFactory con_pool) {
/* 272 */     int dealCount = 0;
/* 273 */     Connection conn = null;
/* 274 */     Statement stmt = null;
/*     */     try {
/* 276 */       conn = con_pool.getConnection();
/* 277 */       stmt = conn.createStatement();
/* 278 */       dealCount = stmt.executeUpdate(sql);
/* 279 */     } catch (SQLException e) {
/* 280 */       LogSQLError(e, sql);
/* 281 */       return -1;
/*     */     } finally {
/* 283 */       SQLUtil.close(null, stmt, conn);
/*     */     } 
/* 285 */     return dealCount;
/*     */   }
/*     */   
/*     */   public static int executeInsert(String sql, IDBConnectionFactory con_pool) {
/* 289 */     int lastID = 0;
/*     */     
/* 291 */     Connection conn = null;
/* 292 */     PreparedStatement stmt = null; try {
/*     */       Exception exception2;
/* 294 */       conn = con_pool.getConnection();
/* 295 */       stmt = conn.prepareStatement(sql, 1);
/* 296 */       stmt.executeUpdate();
/*     */       
/* 298 */       Exception exception1 = null;
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 303 */     catch (SQLException e) {
/* 304 */       LogSQLError(e, sql);
/* 305 */       return -1;
/*     */     } finally {
/* 307 */       SQLUtil.close(null, stmt, conn);
/*     */     } 
/*     */     
/* 310 */     return lastID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Integer> getIDList(String sql, IDBConnectionFactory con_pool) {
/* 321 */     List<Integer> lstID = new ArrayList<>();
/*     */     
/* 323 */     Connection conn = null;
/* 324 */     Statement stmt = null;
/*     */     try {
/*     */       Exception exception2;
/* 327 */       conn = con_pool.getConnection();
/* 328 */       stmt = conn.createStatement();
/* 329 */       Exception exception1 = null;
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 334 */     catch (SQLException e) {
/* 335 */       LogSQLError(e, sql);
/* 336 */       return null;
/*     */     } finally {
/* 338 */       SQLUtil.close(null, stmt, conn);
/*     */     } 
/*     */     
/* 341 */     return lstID;
/*     */   }
/*     */   
/*     */   public static <T extends IBaseBO> List<T> executeQuery(String sql, IDBConnectionFactory con_pool, T bo) {
/* 345 */     List<T> listResult = new ArrayList<>();
/*     */     
/* 347 */     Statement stmt = null;
/* 348 */     ResultSet rs = null;
/* 349 */     Connection conn = null;
/*     */     
/*     */     try {
/* 352 */       conn = con_pool.getConnection();
/* 353 */       stmt = conn.createStatement();
/* 354 */       rs = stmt.executeQuery(sql);
/*     */       
/* 356 */       while (rs.next()) {
/* 357 */         bo.getFromResultSet(rs, listResult);
/*     */       }
/* 359 */     } catch (Throwable e) {
/* 360 */       LogSQLError(e, sql);
/* 361 */       return null;
/*     */     } finally {
/* 363 */       SQLUtil.close(rs, stmt, conn);
/*     */     } 
/*     */     
/* 366 */     return listResult;
/*     */   }
/*     */   
/*     */   public static int executeQueryCount(String sql, IDBConnectionFactory con_pool) {
/* 370 */     Statement stmt = null;
/* 371 */     ResultSet rs = null;
/* 372 */     Connection conn = null;
/*     */     
/* 374 */     int nRet = 0;
/*     */     try {
/* 376 */       conn = con_pool.getConnection();
/* 377 */       stmt = conn.createStatement();
/* 378 */       rs = stmt.executeQuery(sql);
/*     */       
/* 380 */       if (rs != null && rs.next()) {
/* 381 */         nRet = rs.getInt(1);
/*     */       }
/* 383 */     } catch (SQLException e) {
/* 384 */       LogSQLError(e, sql);
/*     */     } finally {
/* 386 */       SQLUtil.close(rs, stmt, conn);
/*     */     } 
/*     */     
/* 389 */     return nRet;
/*     */   }
/*     */   
/*     */   public static void setUTF8(IDBConnectionFactory con_pool) {
/* 393 */     String sql = "SET NAMES 'utf8';";
/* 394 */     Connection conn = null;
/* 395 */     Statement stmt = null;
/*     */     try {
/* 397 */       conn = con_pool.getConnection();
/* 398 */       stmt = conn.createStatement();
/* 399 */       stmt.executeUpdate(sql);
/* 400 */     } catch (SQLException e) {
/* 401 */       LogSQLError(e, sql);
/*     */     } finally {
/* 403 */       SQLUtil.close(null, stmt, conn);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/db/SQLExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */