package com.zhonglian.server.common.db.version;

import BaseCommon.CommClass;
import BaseCommon.CommLog;
import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.Conditions;
import com.zhonglian.server.common.db.IDBConnectionFactory;
import com.zhonglian.server.common.db.SQLExecutor;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class DBVersionManager extends AbstractDBVersionManager {
    private boolean m_bInitializeOk = false;
    private String m_newestVersion = "0.0.0";
    private DBVersionInfoBO m_versionBO = new DBVersionInfoBO();

    @Override
    protected boolean initCurrentVersion() {
        if (this.m_bInitializeOk) {
            return true;
        } else if (this.getConn() == null) {
            this._onError("initialize: db connection info has not been assigned!!!");
            return false;
        } else {
            List<DBVersionInfoBO> resultList = null;
            String sql = String.format("select * from %s", this.m_versionBO.getTableName());

            try {
                resultList = SQLExecutor.executeQuery(sql, this.getConn(), this.m_versionBO);
            } catch (Throwable var12) {
            } finally {
                if (resultList != null && resultList.size() > 0) {
                    this.m_versionBO = resultList.get(0);
                } else {
                    try {
                        SQLExecutor.execute(this.m_versionBO.createTableSql(), this.getConn());
                    } catch (Throwable var11) {
                        this._onError("initialize: create table failed!!!");
                        return false;
                    }

                    if (!SQLExecutor.InsertBO(this.getConn(), this.m_versionBO)) {
                        this._onError("initialize: insert bo failed!!!");
                    }
                }
            }

            this._clearLastError();
            this.m_bInitializeOk = true;
            return true;
        }
    }

    @Override
    protected void _onError(String errorInfo) {
        this._setLastError(errorInfo);
        CommLog.error(errorInfo);
    }

    @Override
    protected void _onMsg(String msg) {
        CommLog.info(msg);
    }

    @Override
    public String getCurVersion() {
        return this.m_versionBO.getVersion();
    }

    @Override
    protected boolean _setCurVersion(String version) {
        Conditions cnd = new Conditions();
        cnd.setTablesName(this.m_versionBO.getTableName());
        cnd.addAndEquals("ID", this.m_versionBO.getId());
        int count = SQLExecutor.updateByCondition(this.getConn(), cnd, "Version='" + version + "'");
        return 1 == count;
    }

    @Override
    public String getNewestVersion() {
        return this.m_newestVersion;
    }

    public void setNewestVersion(String ver) {
        this.m_newestVersion = ver;
    }

    public boolean runAutoVersionUpdate(String packagePath) {
        this.regAllUpdate(packagePath);
        return this.run();
    }

    public boolean checkAndUpdateVersion(String packagePath) {
        try {
            try (Connection conn = this.getConn().getConnection(); Statement stmt = conn.createStatement()) {
                CommLog.warn("==========开始检测数据库版本==============");
                List<String> tables = new ArrayList<>();
                ResultSet rs = stmt.executeQuery("show TABLES;");

                while (rs.next()) {
                    tables.add(rs.getString(1).toLowerCase());
                }

                List<Class<BaseBO>> tableToAdd = new ArrayList<>();
                List<String> fieldsToAdd = new ArrayList<>();
                List<String> fieldsToUpdate = new ArrayList<>();

                for (Class<?> cs : CommClass.getAllClassByInterface(BaseBO.class, packagePath)) {
                    BaseBO bo = (BaseBO)CommClass.forName(cs.getName()).newInstance();
                    String tableName = bo.getTableName().replace("`", "");
                    if (!tables.contains(tableName.toLowerCase())) {
                        tableToAdd.add((Class<BaseBO>)bo.getClass());
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

                for (Class<BaseBO> table : tableToAdd) {
                    CommLog.info("添加新增数据库表 ：{}", table.getSimpleName().replace("BO", ""));
                    this.addTable(table);
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
            CommLog.error("升级数据库失败，请确认数据库连接配置，原因" + var40.getMessage(), (Throwable)var40);
            System.exit(-1);
        }

        CommLog.warn("==========数据库版本检测完毕==============");
        return true;
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

    @Override
    public boolean _createDBTable() {
        return super._createDBTable();
    }

    protected void regAllCreate(String path) {
        for (Class<?> cs : CommClass.getAllClassByInterface(AbstractCreateDBTable.class, path)) {
            AbstractCreateDBTable dealer = null;

            try {
                dealer = (AbstractCreateDBTable)CommClass.forName(cs.getName()).newInstance();
            } catch (IllegalAccessException | ClassNotFoundException | InstantiationException var7) {
            }

            if (dealer != null) {
                this.regVersionCreate(dealer);
            }
        }
    }

    protected void regAllUpdate(String path) {
        for (Class<?> cs : CommClass.getAllClassByInterface(IUpdateDBVersion.class, path)) {
            IUpdateDBVersion dealer = null;

            try {
                dealer = (IUpdateDBVersion)CommClass.forName(cs.getName()).newInstance();
            } catch (IllegalAccessException | ClassNotFoundException | InstantiationException var7) {
            }

            if (dealer != null) {
                this.regVersionUpdate(dealer);
            }
        }
    }

    public <T extends BaseBO> boolean addTable(Class<T> clz) {
        String tableName;
        String sql;
        try {
            T bo = (T)clz.newInstance();
            tableName = (String)clz.getMethod("getTableName").invoke(bo);
            sql = (String)clz.getMethod("getSql_TableCreate").invoke(null);
        } catch (Throwable var5) {
            return false;
        }

        IDBConnectionFactory conn = this.getConn();
        if (!SQLExecutor.execute(sql, conn)) {
            CommLog.error(tableName + " Create Fail!");
            return false;
        } else {
            return true;
        }
    }

    public boolean addIndex(String tableName, String index) {
        IDBConnectionFactory conn = this.getConn();
        String sql = String.format("ALTER TABLE `%s` ADD INDEX `%s` (`%s`) USING BTREE ", tableName, index, index);
        if (!SQLExecutor.execute(sql, conn)) {
            CommLog.error("addIndex {}.{} Fail!{}sql:{}", tableName, index, System.lineSeparator(), sql);
            return false;
        } else {
            return true;
        }
    }
}
