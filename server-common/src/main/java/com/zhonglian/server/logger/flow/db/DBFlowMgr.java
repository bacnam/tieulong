package com.zhonglian.server.logger.flow.db;

import BaseCommon.CommClass;
import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTaskManager;
import com.zhonglian.server.common.db.DBCons;
import com.zhonglian.server.common.db.SQLExecutor;
import com.zhonglian.server.common.db.SQLUtil;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DBFlowMgr {
    private static DBFlowMgr instance;
    private Map<String, List<DBFlowBase>> logFlows = new HashMap<>();

    private DBFlowMgr() {
    }

    public static DBFlowMgr getInstance() {
        if (instance == null) {
            instance = new DBFlowMgr();
        }

        return instance;
    }

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
            conn = this.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement pstmt = null;

            for (List<DBFlowBase> list : new ArrayList<>(this.logFlows.values())) {
                if (list.size() > 0) {
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
                    } catch (Exception var24) {
                        CommLog.error("[DBFlowLoggerMgr]batch save [{}] log error message:{}", "", var24.getMessage(), var24);
                    } finally {
                        SQLUtil.close(pstmt);
                    }
                }
            }

            conn.close();
        } catch (Exception var26) {
            CommLog.error("[DBFlowLoggerMgr]batch save log error message:{}", var26.getMessage(), var26);
        } finally {
            SQLUtil.close(conn);
        }
    }

    public void updateDB(String packagePath) {
        try {
            try (Connection conn = this.getConnection(); Statement stmt = conn.createStatement()) {
                CommLog.warn("==========开始检测[日志库]版本==============");
                List<String> tables = new ArrayList<>();
                ResultSet rs = stmt.executeQuery("show TABLES;");

                while (rs.next()) {
                    tables.add(rs.getString(1).toLowerCase());
                }

                List<Class<DBFlowBase>> tableToAdd = new ArrayList<>();
                List<String> fieldsToAdd = new ArrayList<>();
                List<String> fieldsToUpdate = new ArrayList<>();

                for (Class<?> cs : CommClass.getAllClassByInterface(DBFlowBase.class, packagePath)) {
                    DBFlowBase bo = (DBFlowBase)CommClass.forName(cs.getName()).newInstance();
                    String tableName = bo.getTableName().replace("`", "");
                    if (!tables.contains(tableName.toLowerCase())) {
                        tableToAdd.add((Class<DBFlowBase>)bo.getClass());
                    } else {
                        String sql = "desc " + bo.getTableName() + ";";
                        rs = stmt.executeQuery(sql);
                        Map<String, String> fieldsInDB = new HashMap<>();

                        while (rs.next()) {
                            String field = rs.getString(1);
                            String type = rs.getString(2);
                            fieldsInDB.put(field, type);
                        }

                        Map<String, DataBaseField> fieldsInCode = new HashMap<>();

                        Field[] fieldInfo;
                        for (Field curField : fieldInfo = cs.getDeclaredFields()) {
                            DataBaseField dbinfo = curField.getAnnotation(DataBaseField.class);
                            if (dbinfo != null) {
                                if (dbinfo.size() == 0) {
                                    fieldsInCode.put(curField.getName(), dbinfo);
                                } else {
                                    for (int i = 0; i < dbinfo.size(); i++) {
                                        fieldsInCode.put(curField.getName() + "_" + i, dbinfo);
                                    }
                                }
                            }
                        }

                        for (Entry<String, DataBaseField> fieldInCode : fieldsInCode.entrySet()) {
                            String fieldName = fieldInCode.getKey();
                            DataBaseField fieldInfox = fieldInCode.getValue();
                            if (!fieldsInDB.containsKey(fieldName)) {
                                fieldsToAdd.add(String.format("ALTER TABLE `%s` ADD `%s` %s;", tableName, fieldName, this.getFieldInfo(fieldInfox)));
                            } else if (this.checkFieldToUpdate(fieldInfox.type(), fieldsInDB.get(fieldName))) {
                                fieldsToUpdate.add(
                                    String.format("ALTER TABLE `%s` MODIFY COLUMN `%s`  %s ;", tableName, fieldName, this.getFieldInfo(fieldInfox))
                                );
                            }
                        }
                    }
                }

                for (Class<DBFlowBase> table : tableToAdd) {
                    String tableName = table.getSimpleName().replace("Flow", "");
                    CommLog.info("添加新增数据库表 ：{}", tableName);
                    String createSql = (String)table.getMethod("getCreateTableSQL").invoke(null);
                    if (!SQLExecutor.execute(createSql, DBCons.getLogDBFactory())) {
                        CommLog.error(tableName + " Create Fail!");
                    }
                }

                if (!fieldsToAdd.isEmpty()) {
                    for (String fieldSql : fieldsToAdd) {
                        CommLog.info("添加字段, SQL：" + fieldSql);
                        conn.createStatement().execute(fieldSql);
                    }
                }

                if (!fieldsToUpdate.isEmpty()) {
                    for (String fieldSql : fieldsToUpdate) {
                        CommLog.info("升级字段, SQL：" + fieldSql);
                        conn.createStatement().execute(fieldSql);
                    }
                }
            }
        } catch (Exception var40) {
            CommLog.error("升级数据库失败，原因" + var40.getMessage(), (Throwable)var40);
            System.exit(-1);
        }

        CommLog.warn("==========数据库版本检测完毕==============");
    }

    private String getFieldInfo(DataBaseField dbinfo) {
        Object defaultValue = "";
        String type = dbinfo.type();
        if (type.startsWith("bytes") || type.startsWith("blob") || type.startsWith("text")) {
            return String.format("%s NOT NULL COMMENT '%s'", type, dbinfo.comment());
        } else if (type.startsWith("timestamp")) {
            return String.format("%s NULL DEFAULT NULL COMMENT '%s'", type, dbinfo.comment());
        } else {
            if (type.startsWith("int") || type.startsWith("tinyint") || type.startsWith("bigint")) {
                defaultValue = 0;
            } else if (type.startsWith("varchar")) {
                defaultValue = "";
            } else {
                defaultValue = "";
            }

            return String.format("%s NOT NULL DEFAULT '%s' COMMENT '%s'", type, defaultValue, dbinfo.comment());
        }
    }

    private boolean checkFieldToUpdate(String fieldInCode, String fieldInDB) {
        if (fieldInCode.equals(fieldInDB)) {
            return false;
        } else {
            List<String> intfields = Arrays.asList("tinyint(1)", "tinyint(2)", "tinyint(4)", "smallint(6)", "mediumint(9)", "int(11)", "bigint(20)");
            int idxIntCode = intfields.indexOf(fieldInCode);
            int idxIntDB = intfields.indexOf(fieldInDB);
            if (idxIntCode < 0 || idxIntDB < 0) {
                String sCodeTxtType = fieldInCode.startsWith("varchar") ? "varchar" : fieldInCode;
                String sDBTxtType = fieldInDB.startsWith("varchar") ? "varchar" : fieldInDB;
                if (sDBTxtType.equals("varchar") && sCodeTxtType.equals("varchar")) {
                    return Integer.valueOf(fieldInCode.split("\\W", 3)[1]) > Integer.valueOf(fieldInDB.split("\\W", 3)[1]);
                } else {
                    List<String> textFields = Arrays.asList("varchar", "text", "mediumtext", "longtext");
                    int idxTxtCode = textFields.indexOf(sCodeTxtType);
                    int idxTxtDB = textFields.indexOf(sDBTxtType);
                    return idxTxtCode < 0 || idxTxtDB < 0 ? false : idxTxtCode > idxTxtDB;
                }
            } else {
                return idxIntCode > idxIntDB;
            }
        }
    }

    public void start() {
        SyncTaskManager.schedule(200, () -> {
            this.logAll();
            return true;
        });
    }
}
