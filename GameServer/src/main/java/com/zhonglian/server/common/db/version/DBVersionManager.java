package com.zhonglian.server.common.db.version;

import BaseCommon.CommClass;
import BaseCommon.CommLog;
import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.Conditions;
import com.zhonglian.server.common.db.IDBConnectionFactory;
import com.zhonglian.server.common.db.SQLExecutor;
import com.zhonglian.server.common.db.annotation.DataBaseField;

import java.util.Arrays;
import java.util.List;

public abstract class DBVersionManager
        extends AbstractDBVersionManager {
    private boolean m_bInitializeOk = false;
    private String m_newestVersion = "0.0.0";

    private DBVersionInfoBO m_versionBO = new DBVersionInfoBO();

    protected boolean initCurrentVersion() {
        if (this.m_bInitializeOk) {
            return true;
        }

        if (getConn() == null) {
            _onError("initialize: db connection info has not been assigned!!!");
            return false;
        }

        List<DBVersionInfoBO> resultList = null;
        String sql = String.format("select * from %s", new Object[]{this.m_versionBO.getTableName()});
        try {
            resultList = SQLExecutor.executeQuery(sql, getConn(), this.m_versionBO);
        } catch (Throwable throwable) {

        } finally {
            if (resultList != null && resultList.size() > 0) {
                this.m_versionBO = resultList.get(0);
            } else {
                try {
                    SQLExecutor.execute(this.m_versionBO.createTableSql(), getConn());
                } catch (Throwable e) {
                    _onError("initialize: create table failed!!!");
                    return false;
                }

                if (!SQLExecutor.InsertBO(getConn(), this.m_versionBO)) {
                    _onError("initialize: insert bo failed!!!");
                }
            }
        }

        _clearLastError();
        this.m_bInitializeOk = true;
        return true;
    }

    protected void _onError(String errorInfo) {
        _setLastError(errorInfo);
        CommLog.error(errorInfo);
    }

    protected void _onMsg(String msg) {
        CommLog.info(msg);
    }

    public String getCurVersion() {
        return this.m_versionBO.getVersion();
    }

    protected boolean _setCurVersion(String version) {
        Conditions cnd = new Conditions();
        cnd.setTablesName(this.m_versionBO.getTableName());
        cnd.addAndEquals("ID", Long.valueOf(this.m_versionBO.getId()));

        int count = SQLExecutor.updateByCondition(getConn(), cnd, "Version='" + version + "'");
        return (1 == count);
    }

    public String getNewestVersion() {
        return this.m_newestVersion;
    }

    public void setNewestVersion(String ver) {
        this.m_newestVersion = ver;
    }

    public boolean runAutoVersionUpdate(String packagePath) {
        regAllUpdate(packagePath);
        return run().booleanValue();
    }

    public boolean checkAndUpdateVersion(String packagePath) {
        try {
            Exception exception2, exception1 = null;

        } catch (Exception e) {
            CommLog.error("升级数据库失败，请确认数据库连接配置，原因" + e.getMessage(), e);
            System.exit(-1);
        }
        CommLog.warn("==========数据库版本检测完毕==============");
        return true;
    }

    private String getFieldInfo(DataBaseField dbinfo) {
        Object defaultValue = "";
        String type = dbinfo.type();
        if (type.startsWith("bytes") || type.startsWith("blob") || type.startsWith("text"))
            return String.format("%s NOT NULL COMMENT '%s'", new Object[]{type, dbinfo.comment()});
        if (type.startsWith("timestamp"))
            return String.format("%s NULL DEFAULT NULL COMMENT '%s'", new Object[]{type, dbinfo.comment()});
        if (type.startsWith("int") || type.startsWith("tinyint") || type.startsWith("bigint")) {
            defaultValue = Integer.valueOf(0);
        } else if (type.startsWith("varchar")) {
            defaultValue = "";
        } else {
            defaultValue = "";
        }
        return String.format("%s NOT NULL DEFAULT '%s' COMMENT '%s'", new Object[]{type, defaultValue, dbinfo.comment()});
    }

    private boolean checkFieldToUpdate(String fieldInCode, String fieldInDB) {
        if (fieldInCode.equals(fieldInDB)) {
            return false;
        }

        List<String> intfields = Arrays.asList(new String[]{"tinyint(1)", "tinyint(2)", "tinyint(4)", "smallint(6)", "mediumint(9)", "int(11)", "bigint(20)"});
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
        List<String> textFields = Arrays.asList(new String[]{"varchar", "text", "mediumtext", "longtext"});
        int idxTxtCode = textFields.indexOf(sCodeTxtType);
        int idxTxtDB = textFields.indexOf(sDBTxtType);
        if (idxTxtCode >= 0 && idxTxtDB >= 0) {
            return (idxTxtCode > idxTxtDB);
        }
        return false;
    }

    public boolean _createDBTable() {
        return super._createDBTable();
    }

    protected void regAllCreate(String path) {
        List<Class<?>> dealers = CommClass.getAllClassByInterface(AbstractCreateDBTable.class, path);

        for (Class<?> cs : dealers) {
            AbstractCreateDBTable dealer = null;
            try {
                dealer = CommClass.forName(cs.getName()).newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException instantiationException) {
            }

            if (dealer == null) {
                continue;
            }

            regVersionCreate(dealer);
        }
    }

    protected void regAllUpdate(String path) {
        List<Class<?>> dealers = CommClass.getAllClassByInterface(IUpdateDBVersion.class, path);

        for (Class<?> cs : dealers) {
            IUpdateDBVersion dealer = null;
            try {
                dealer = CommClass.forName(cs.getName()).newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException instantiationException) {
            }

            if (dealer == null) {
                continue;
            }

            regVersionUpdate(dealer);
        }
    }

    public <T extends BaseBO> boolean addTable(Class<T> clz) {
        String tableName, sql;
        try {
            BaseBO baseBO = (BaseBO) clz.newInstance();
            tableName = (String) clz.getMethod("getTableName", new Class[0]).invoke(baseBO, new Object[0]);
            sql = (String) clz.getMethod("getSql_TableCreate", new Class[0]).invoke(null, new Object[0]);
        } catch (Throwable ex) {

            return false;
        }

        IDBConnectionFactory conn = getConn();
        if (!SQLExecutor.execute(sql, conn)) {
            CommLog.error(String.valueOf(tableName) + " Create Fail!");
            return false;
        }

        return true;
    }

    public boolean addIndex(String tableName, String index) {
        IDBConnectionFactory conn = getConn();

        String sql = String.format("ALTER TABLE `%s` ADD INDEX `%s` (`%s`) USING BTREE ", new Object[]{tableName, index, index});
        if (!SQLExecutor.execute(sql, conn)) {
            CommLog.error("addIndex {}.{} Fail!{}sql:{}", new Object[]{tableName, index, System.lineSeparator(), sql});
            return false;
        }

        return true;
    }
}

