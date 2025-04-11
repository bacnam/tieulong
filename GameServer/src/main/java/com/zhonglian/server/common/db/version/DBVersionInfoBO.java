package com.zhonglian.server.common.db.version;

import com.zhonglian.server.common.db.IBaseBO;
import com.zhonglian.server.common.db.IDBConnectionFactory;

import java.sql.ResultSet;
import java.util.List;

public class DBVersionInfoBO
        implements IBaseBO {
    private long m_id;
    private String m_version;
    private String m_info;

    public DBVersionInfoBO() {
        this.m_id = 0L;
        this.m_version = "0.0.0";
        this.m_info = "";
    }

    public DBVersionInfoBO(DBVersionInfoBO _bo) {
        this.m_id = _bo.m_id;
        this.m_version = _bo.m_version;
        this.m_info = _bo.m_info;
    }

    public DBVersionInfoBO(int id, String version, String info) {
        this.m_id = id;
        this.m_version = version;
        this.m_info = info;
    }

    public void getFromResultSet(ResultSet rs, List<DBVersionInfoBO> list) throws Exception {
        DBVersionInfoBO bo = new DBVersionInfoBO();

        bo.m_id = rs.getInt(1);
        bo.m_version = rs.getString(2);
        bo.m_info = rs.getString(3);

        list.add(bo);
    }

    public String createTableSql() {
        return String.format("CREATE TABLE %s (%s int(11) NOT NULL, %s varchar(64) NOT NULL, %s varchar(64), PRIMARY KEY (%s));", new Object[]{getTableName(), "ID",
                "Version", "Info", "ID"});
    }

    public String getItemsName() {
        return "`ID`, `Version`, `Info`";
    }

    public String getTableName() {
        return "`TableVersionInfo`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();

        strBuf.append(this.m_id).append(",'");
        strBuf.append(this.m_version).append("','");
        strBuf.append(this.m_info).append("'");

        return strBuf.toString();
    }

    public long getId() {
        return this.m_id;
    }

    public void setId(long iID) {
        this.m_id = iID;
    }

    public String getVersion() {
        return this.m_version;
    }

    public void setVersion(String version) {
        this.m_version = version;
    }

    public String getInfo() {
        return this.m_info;
    }

    public void setInfo(String info) {
        this.m_info = info;
    }

    public IDBConnectionFactory getConnectionFactory() {
        return null;
    }
}

