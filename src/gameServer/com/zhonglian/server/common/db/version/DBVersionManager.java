/*     */ package com.zhonglian.server.common.db.version;
/*     */ 
/*     */ import BaseCommon.CommClass;
/*     */ import BaseCommon.CommLog;
/*     */ import com.zhonglian.server.common.db.BaseBO;
/*     */ import com.zhonglian.server.common.db.Conditions;
/*     */ import com.zhonglian.server.common.db.IDBConnectionFactory;
/*     */ import com.zhonglian.server.common.db.SQLExecutor;
/*     */ import com.zhonglian.server.common.db.annotation.DataBaseField;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DBVersionManager
/*     */   extends AbstractDBVersionManager
/*     */ {
/*     */   private boolean m_bInitializeOk = false;
/*  34 */   private String m_newestVersion = "0.0.0";
/*     */   
/*  36 */   private DBVersionInfoBO m_versionBO = new DBVersionInfoBO();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean initCurrentVersion() {
/*  48 */     if (this.m_bInitializeOk) {
/*  49 */       return true;
/*     */     }
/*     */     
/*  52 */     if (getConn() == null) {
/*  53 */       _onError("initialize: db connection info has not been assigned!!!");
/*  54 */       return false;
/*     */     } 
/*     */     
/*  57 */     List<DBVersionInfoBO> resultList = null;
/*  58 */     String sql = String.format("select * from %s", new Object[] { this.m_versionBO.getTableName() });
/*     */     try {
/*  60 */       resultList = SQLExecutor.executeQuery(sql, getConn(), this.m_versionBO);
/*  61 */     } catch (Throwable throwable) {
/*     */ 
/*     */     
/*     */     } finally {
/*  65 */       if (resultList != null && resultList.size() > 0) {
/*  66 */         this.m_versionBO = resultList.get(0);
/*     */       } else {
/*     */         try {
/*  69 */           SQLExecutor.execute(this.m_versionBO.createTableSql(), getConn());
/*  70 */         } catch (Throwable e) {
/*  71 */           _onError("initialize: create table failed!!!");
/*  72 */           return false;
/*     */         } 
/*     */         
/*  75 */         if (!SQLExecutor.InsertBO(getConn(), this.m_versionBO)) {
/*  76 */           _onError("initialize: insert bo failed!!!");
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  81 */     _clearLastError();
/*  82 */     this.m_bInitializeOk = true;
/*  83 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _onError(String errorInfo) {
/*  88 */     _setLastError(errorInfo);
/*  89 */     CommLog.error(errorInfo);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void _onMsg(String msg) {
/*  94 */     CommLog.info(msg);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCurVersion() {
/*  99 */     return this.m_versionBO.getVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean _setCurVersion(String version) {
/* 104 */     Conditions cnd = new Conditions();
/* 105 */     cnd.setTablesName(this.m_versionBO.getTableName());
/* 106 */     cnd.addAndEquals("ID", Long.valueOf(this.m_versionBO.getId()));
/*     */     
/* 108 */     int count = SQLExecutor.updateByCondition(getConn(), cnd, "Version='" + version + "'");
/* 109 */     return (1 == count);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNewestVersion() {
/* 114 */     return this.m_newestVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNewestVersion(String ver) {
/* 123 */     this.m_newestVersion = ver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean runAutoVersionUpdate(String packagePath) {
/* 134 */     regAllUpdate(packagePath);
/* 135 */     return run().booleanValue();
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
/*     */   public boolean checkAndUpdateVersion(String packagePath) {
/*     */     try {
/* 148 */       Exception exception2, exception1 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 225 */     catch (Exception e) {
/* 226 */       CommLog.error("升级数据库失败，请确认数据库连接配置，原因" + e.getMessage(), e);
/* 227 */       System.exit(-1);
/*     */     } 
/* 229 */     CommLog.warn("==========数据库版本检测完毕==============");
/* 230 */     return true;
/*     */   }
/*     */   
/*     */   private String getFieldInfo(DataBaseField dbinfo) {
/* 234 */     Object defaultValue = "";
/* 235 */     String type = dbinfo.type();
/* 236 */     if (type.startsWith("bytes") || type.startsWith("blob") || type.startsWith("text"))
/* 237 */       return String.format("%s NOT NULL COMMENT '%s'", new Object[] { type, dbinfo.comment() }); 
/* 238 */     if (type.startsWith("timestamp"))
/* 239 */       return String.format("%s NULL DEFAULT NULL COMMENT '%s'", new Object[] { type, dbinfo.comment() }); 
/* 240 */     if (type.startsWith("int") || type.startsWith("tinyint") || type.startsWith("bigint")) {
/* 241 */       defaultValue = Integer.valueOf(0);
/* 242 */     } else if (type.startsWith("varchar")) {
/* 243 */       defaultValue = "";
/*     */     } else {
/* 245 */       defaultValue = "";
/*     */     } 
/* 247 */     return String.format("%s NOT NULL DEFAULT '%s' COMMENT '%s'", new Object[] { type, defaultValue, dbinfo.comment() });
/*     */   }
/*     */   
/*     */   private boolean checkFieldToUpdate(String fieldInCode, String fieldInDB) {
/* 251 */     if (fieldInCode.equals(fieldInDB)) {
/* 252 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 256 */     List<String> intfields = Arrays.asList(new String[] { "tinyint(1)", "tinyint(2)", "tinyint(4)", "smallint(6)", "mediumint(9)", "int(11)", "bigint(20)" });
/* 257 */     int idxIntCode = intfields.indexOf(fieldInCode);
/* 258 */     int idxIntDB = intfields.indexOf(fieldInDB);
/* 259 */     if (idxIntCode >= 0 && idxIntDB >= 0) {
/* 260 */       return (idxIntCode > idxIntDB);
/*     */     }
/*     */ 
/*     */     
/* 264 */     String sCodeTxtType = fieldInCode.startsWith("varchar") ? "varchar" : fieldInCode;
/* 265 */     String sDBTxtType = fieldInDB.startsWith("varchar") ? "varchar" : fieldInDB;
/*     */     
/* 267 */     if (sDBTxtType.equals("varchar") && sCodeTxtType.equals("varchar")) {
/* 268 */       return (Integer.valueOf(fieldInCode.split("\\W", 3)[1]).intValue() > Integer.valueOf(fieldInDB.split("\\W", 3)[1]).intValue());
/*     */     }
/* 270 */     List<String> textFields = Arrays.asList(new String[] { "varchar", "text", "mediumtext", "longtext" });
/* 271 */     int idxTxtCode = textFields.indexOf(sCodeTxtType);
/* 272 */     int idxTxtDB = textFields.indexOf(sDBTxtType);
/* 273 */     if (idxTxtCode >= 0 && idxTxtDB >= 0) {
/* 274 */       return (idxTxtCode > idxTxtDB);
/*     */     }
/* 276 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean _createDBTable() {
/* 287 */     return super._createDBTable();
/*     */   }
/*     */   
/*     */   protected void regAllCreate(String path) {
/* 291 */     List<Class<?>> dealers = CommClass.getAllClassByInterface(AbstractCreateDBTable.class, path);
/*     */     
/* 293 */     for (Class<?> cs : dealers) {
/* 294 */       AbstractCreateDBTable dealer = null;
/*     */       try {
/* 296 */         dealer = CommClass.forName(cs.getName()).newInstance();
/* 297 */       } catch (InstantiationException|IllegalAccessException|ClassNotFoundException instantiationException) {}
/*     */ 
/*     */ 
/*     */       
/* 301 */       if (dealer == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 305 */       regVersionCreate(dealer);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void regAllUpdate(String path) {
/* 310 */     List<Class<?>> dealers = CommClass.getAllClassByInterface(IUpdateDBVersion.class, path);
/*     */     
/* 312 */     for (Class<?> cs : dealers) {
/* 313 */       IUpdateDBVersion dealer = null;
/*     */       try {
/* 315 */         dealer = CommClass.forName(cs.getName()).newInstance();
/* 316 */       } catch (InstantiationException|IllegalAccessException|ClassNotFoundException instantiationException) {}
/*     */ 
/*     */       
/* 319 */       if (dealer == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 323 */       regVersionUpdate(dealer);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends BaseBO> boolean addTable(Class<T> clz) {
/*     */     String tableName, sql;
/*     */     try {
/* 334 */       BaseBO baseBO = (BaseBO)clz.newInstance();
/* 335 */       tableName = (String)clz.getMethod("getTableName", new Class[0]).invoke(baseBO, new Object[0]);
/* 336 */       sql = (String)clz.getMethod("getSql_TableCreate", new Class[0]).invoke(null, new Object[0]);
/* 337 */     } catch (Throwable ex) {
/*     */       
/* 339 */       return false;
/*     */     } 
/*     */     
/* 342 */     IDBConnectionFactory conn = getConn();
/* 343 */     if (!SQLExecutor.execute(sql, conn)) {
/* 344 */       CommLog.error(String.valueOf(tableName) + " Create Fail!");
/* 345 */       return false;
/*     */     } 
/*     */     
/* 348 */     return true;
/*     */   }
/*     */   
/*     */   public boolean addIndex(String tableName, String index) {
/* 352 */     IDBConnectionFactory conn = getConn();
/*     */ 
/*     */     
/* 355 */     String sql = String.format("ALTER TABLE `%s` ADD INDEX `%s` (`%s`) USING BTREE ", new Object[] { tableName, index, index });
/* 356 */     if (!SQLExecutor.execute(sql, conn)) {
/* 357 */       CommLog.error("addIndex {}.{} Fail!{}sql:{}", new Object[] { tableName, index, System.lineSeparator(), sql });
/* 358 */       return false;
/*     */     } 
/*     */     
/* 361 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/db/version/DBVersionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */