/*     */ package com.zhonglian.server.logger.flow.db;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import BaseTask.SyncTask.SyncTaskManager;
/*     */ import com.zhonglian.server.common.db.DBCons;
/*     */ import com.zhonglian.server.common.db.SQLUtil;
/*     */ import com.zhonglian.server.common.db.annotation.DataBaseField;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DBFlowMgr
/*     */ {
/*     */   private static DBFlowMgr instance;
/*     */   
/*     */   public static DBFlowMgr getInstance() {
/*  29 */     if (instance == null) {
/*  30 */       instance = new DBFlowMgr();
/*     */     }
/*  32 */     return instance;
/*     */   }
/*     */   
/*  35 */   private Map<String, List<DBFlowBase>> logFlows = new HashMap<>();
/*     */   
/*     */   public void add(DBFlowBase DBLoggerBase) {
/*  38 */     List<DBFlowBase> list = this.logFlows.get(DBLoggerBase.getTableName());
/*  39 */     if (list == null) {
/*  40 */       list = new ArrayList<>();
/*  41 */       this.logFlows.put(DBLoggerBase.getTableName(), list);
/*     */     } 
/*  43 */     synchronized (list) {
/*  44 */       list.add(DBLoggerBase);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Connection getConnection() {
/*  49 */     return DBCons.getLogDBFactory().getConnection();
/*     */   }
/*     */   
/*     */   public void logAll() {
/*  53 */     Connection conn = null;
/*     */     
/*     */     try {
/*  56 */       conn = getConnection();
/*  57 */       conn.setAutoCommit(false);
/*     */       
/*  59 */       PreparedStatement pstmt = null;
/*  60 */       List<List<DBFlowBase>> loggers = new ArrayList<>(this.logFlows.values());
/*  61 */       for (List<DBFlowBase> list : loggers) {
/*  62 */         if (list.size() <= 0) {
/*     */           continue;
/*     */         }
/*     */         try {
/*  66 */           DBFlowBase template = list.get(0);
/*  67 */           String insetSql = template.getInsertSql();
/*  68 */           pstmt = conn.prepareStatement(insetSql);
/*  69 */           synchronized (list) {
/*  70 */             pstmt.getMaxRows();
/*  71 */             for (DBFlowBase flow : list) {
/*  72 */               flow.addToBatch(pstmt);
/*  73 */               pstmt.addBatch();
/*     */             } 
/*  75 */             list.clear();
/*     */           } 
/*  77 */           pstmt.executeBatch();
/*  78 */           conn.commit();
/*     */         }
/*  80 */         catch (Exception e) {
/*  81 */           CommLog.error("[DBFlowLoggerMgr]batch save [{}] log error message:{}", new Object[] { "", e.getMessage(), e }); continue;
/*     */         } finally {
/*  83 */           SQLUtil.close(pstmt);
/*     */         } 
/*     */       } 
/*  86 */       conn.close();
/*  87 */     } catch (Exception e) {
/*  88 */       CommLog.error("[DBFlowLoggerMgr]batch save log error message:{}", e.getMessage(), e);
/*     */     } finally {
/*  90 */       SQLUtil.close(conn);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateDB(String packagePath) {
/*     */     try {
/*  98 */       Exception exception2, exception1 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 179 */     catch (Exception e) {
/* 180 */       CommLog.error("升级数据库失败，原因" + e.getMessage(), e);
/* 181 */       System.exit(-1);
/*     */     } 
/* 183 */     CommLog.warn("==========数据库版本检测完毕==============");
/*     */   }
/*     */   
/*     */   private String getFieldInfo(DataBaseField dbinfo) {
/* 187 */     Object defaultValue = "";
/* 188 */     String type = dbinfo.type();
/* 189 */     if (type.startsWith("bytes") || type.startsWith("blob") || type.startsWith("text"))
/* 190 */       return String.format("%s NOT NULL COMMENT '%s'", new Object[] { type, dbinfo.comment() }); 
/* 191 */     if (type.startsWith("timestamp"))
/* 192 */       return String.format("%s NULL DEFAULT NULL COMMENT '%s'", new Object[] { type, dbinfo.comment() }); 
/* 193 */     if (type.startsWith("int") || type.startsWith("tinyint") || type.startsWith("bigint")) {
/* 194 */       defaultValue = Integer.valueOf(0);
/* 195 */     } else if (type.startsWith("varchar")) {
/* 196 */       defaultValue = "";
/*     */     } else {
/* 198 */       defaultValue = "";
/*     */     } 
/* 200 */     return String.format("%s NOT NULL DEFAULT '%s' COMMENT '%s'", new Object[] { type, defaultValue, dbinfo.comment() });
/*     */   }
/*     */   
/*     */   private boolean checkFieldToUpdate(String fieldInCode, String fieldInDB) {
/* 204 */     if (fieldInCode.equals(fieldInDB)) {
/* 205 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 209 */     List<String> intfields = Arrays.asList(new String[] { "tinyint(1)", "tinyint(2)", "tinyint(4)", "smallint(6)", "mediumint(9)", "int(11)", "bigint(20)" });
/* 210 */     int idxIntCode = intfields.indexOf(fieldInCode);
/* 211 */     int idxIntDB = intfields.indexOf(fieldInDB);
/* 212 */     if (idxIntCode >= 0 && idxIntDB >= 0) {
/* 213 */       return (idxIntCode > idxIntDB);
/*     */     }
/*     */ 
/*     */     
/* 217 */     String sCodeTxtType = fieldInCode.startsWith("varchar") ? "varchar" : fieldInCode;
/* 218 */     String sDBTxtType = fieldInDB.startsWith("varchar") ? "varchar" : fieldInDB;
/*     */     
/* 220 */     if (sDBTxtType.equals("varchar") && sCodeTxtType.equals("varchar")) {
/* 221 */       return (Integer.valueOf(fieldInCode.split("\\W", 3)[1]).intValue() > Integer.valueOf(fieldInDB.split("\\W", 3)[1]).intValue());
/*     */     }
/* 223 */     List<String> textFields = Arrays.asList(new String[] { "varchar", "text", "mediumtext", "longtext" });
/* 224 */     int idxTxtCode = textFields.indexOf(sCodeTxtType);
/* 225 */     int idxTxtDB = textFields.indexOf(sDBTxtType);
/* 226 */     if (idxTxtCode >= 0 && idxTxtDB >= 0) {
/* 227 */       return (idxTxtCode > idxTxtDB);
/*     */     }
/* 229 */     return false;
/*     */   }
/*     */   
/*     */   public void start() {
/* 233 */     SyncTaskManager.schedule(200, () -> {
/*     */           logAll();
/*     */           return true;
/*     */         });
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/logger/flow/db/DBFlowMgr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */