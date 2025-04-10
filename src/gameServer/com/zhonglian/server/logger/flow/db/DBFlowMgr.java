package com.zhonglian.server.logger.flow.db;

import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTaskManager;
import com.zhonglian.server.common.db.DBCons;
import com.zhonglian.server.common.db.SQLUtil;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBFlowMgr
{
private static DBFlowMgr instance;

public static DBFlowMgr getInstance() {
if (instance == null) {
instance = new DBFlowMgr();
}
return instance;
}

private Map<String, List<DBFlowBase>> logFlows = new HashMap<>();

public void add(DBFlowBase DBLoggerBase) {
List<DBFlowBase> list = this.logFlows.get(DBLoggerBase.getTableName());
if (list == null) {
list = new ArrayList<>();
this.logFlows.put(DBLoggerBase.getTableName(), list);
} 
synchronized (list) {
list.add(DBLoggerBase);
} 
}

private Connection getConnection() {
return DBCons.getLogDBFactory().getConnection();
}

public void logAll() {
Connection conn = null;

try {
conn = getConnection();
conn.setAutoCommit(false);

PreparedStatement pstmt = null;
List<List<DBFlowBase>> loggers = new ArrayList<>(this.logFlows.values());
for (List<DBFlowBase> list : loggers) {
if (list.size() <= 0) {
continue;
}
try {
DBFlowBase template = list.get(0);
String insetSql = template.getInsertSql();
pstmt = conn.prepareStatement(insetSql);
synchronized (list) {
pstmt.getMaxRows();
for (DBFlowBase flow : list) {
flow.addToBatch(pstmt);
pstmt.addBatch();
} 
list.clear();
} 
pstmt.executeBatch();
conn.commit();
}
catch (Exception e) {
CommLog.error("[DBFlowLoggerMgr]batch save [{}] log error message:{}", new Object[] { "", e.getMessage(), e }); continue;
} finally {
SQLUtil.close(pstmt);
} 
} 
conn.close();
} catch (Exception e) {
CommLog.error("[DBFlowLoggerMgr]batch save log error message:{}", e.getMessage(), e);
} finally {
SQLUtil.close(conn);
} 
}

public void updateDB(String packagePath) {
try {
Exception exception2, exception1 = null;

}
catch (Exception e) {
CommLog.error("升级数据库失败，原因" + e.getMessage(), e);
System.exit(-1);
} 
CommLog.warn("==========数据库版本检测完毕==============");
}

private String getFieldInfo(DataBaseField dbinfo) {
Object defaultValue = "";
String type = dbinfo.type();
if (type.startsWith("bytes") || type.startsWith("blob") || type.startsWith("text"))
return String.format("%s NOT NULL COMMENT '%s'", new Object[] { type, dbinfo.comment() }); 
if (type.startsWith("timestamp"))
return String.format("%s NULL DEFAULT NULL COMMENT '%s'", new Object[] { type, dbinfo.comment() }); 
if (type.startsWith("int") || type.startsWith("tinyint") || type.startsWith("bigint")) {
defaultValue = Integer.valueOf(0);
} else if (type.startsWith("varchar")) {
defaultValue = "";
} else {
defaultValue = "";
} 
return String.format("%s NOT NULL DEFAULT '%s' COMMENT '%s'", new Object[] { type, defaultValue, dbinfo.comment() });
}

private boolean checkFieldToUpdate(String fieldInCode, String fieldInDB) {
if (fieldInCode.equals(fieldInDB)) {
return false;
}

List<String> intfields = Arrays.asList(new String[] { "tinyint(1)", "tinyint(2)", "tinyint(4)", "smallint(6)", "mediumint(9)", "int(11)", "bigint(20)" });
int idxIntCode = intfields.indexOf(fieldInCode);
int idxIntDB = intfields.indexOf(fieldInDB);
if (idxIntCode >= 0 && idxIntDB >= 0) {
return (idxIntCode > idxIntDB);
}

String sCodeTxtType = fieldInCode.startsWith("varchar") ? "varchar" : fieldInCode;
String sDBTxtType = fieldInDB.startsWith("varchar") ? "varchar" : fieldInDB;

if (sDBTxtType.equals("varchar") && sCodeTxtType.equals("varchar")) {
return (Integer.valueOf(fieldInCode.split("\\W", 3)[1]).intValue() > Integer.valueOf(fieldInDB.split("\\W", 3)[1]).intValue());
}
List<String> textFields = Arrays.asList(new String[] { "varchar", "text", "mediumtext", "longtext" });
int idxTxtCode = textFields.indexOf(sCodeTxtType);
int idxTxtDB = textFields.indexOf(sDBTxtType);
if (idxTxtCode >= 0 && idxTxtDB >= 0) {
return (idxTxtCode > idxTxtDB);
}
return false;
}

public void start() {
SyncTaskManager.schedule(200, () -> {
logAll();
return true;
});
}
}

