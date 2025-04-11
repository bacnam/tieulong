package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClientdataBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家ID")
    private long pid;
    @DataBaseField(type = "text(20000)", size = 8, fieldname = "vstr", comment = "自定义字段")
    private List<String> vstr;

    public ClientdataBO() {
        this.id = 0L;
        this.pid = 0L;
        this.vstr = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            this.vstr.add("");
        }
    }

    public ClientdataBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.vstr = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            this.vstr.add(rs.getString(i + 3));
        }
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `clientdata` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家ID',`vstr_0` text NULL COMMENT '自定义字段',`vstr_1` text NULL COMMENT '自定义字段',`vstr_2` text NULL COMMENT '自定义字段',`vstr_3` text NULL COMMENT '自定义字段',`vstr_4` text NULL COMMENT '自定义字段',`vstr_5` text NULL COMMENT '自定义字段',`vstr_6` text NULL COMMENT '自定义字段',`vstr_7` text NULL COMMENT '自定义字段',UNIQUE INDEX `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='用户自定义数段表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<ClientdataBO> list) throws Exception {
        list.add(new ClientdataBO(rs));
    }

    public long getAsynTaskTag() {
        return getPid();
    }

    public String getItemsName() {
        return "`id`, `pid`, `vstr_0`, `vstr_1`, `vstr_2`, `vstr_3`, `vstr_4`, `vstr_5`, `vstr_6`, `vstr_7`";
    }

    public String getTableName() {
        return "`clientdata`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        for (int i = 0; i < this.vstr.size(); i++) {
            strBuf.append("'").append((this.vstr.get(i) == null) ? null : ((String) this.vstr.get(i)).replace("'", "''")).append("', ");
        }
        strBuf.deleteCharAt(strBuf.length() - 2);
        return strBuf.toString();
    }

    public ArrayList<byte[]> getInsertValueBytes() {
        ArrayList<byte[]> ret = (ArrayList) new ArrayList<>();
        return ret;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long iID) {
        this.id = iID;
    }

    public long getPid() {
        return this.pid;
    }

    public void setPid(long pid) {
        if (pid == this.pid)
            return;
        this.pid = pid;
    }

    public void savePid(long pid) {
        if (pid == this.pid)
            return;
        this.pid = pid;
        saveField("pid", Long.valueOf(pid));
    }

    public int getVstrSize() {
        return this.vstr.size();
    }

    public List<String> getVstrAll() {
        return new ArrayList<>(this.vstr);
    }

    public void setVstrAll(String value) {
        for (int i = 0; i < this.vstr.size(); ) {
            this.vstr.set(i, value);
            i++;
        }
    }

    public void saveVstrAll(String value) {
        setVstrAll(value);
        saveAll();
    }

    public String getVstr(int index) {
        return this.vstr.get(index);
    }

    public void setVstr(int index, String value) {
        if (value.equals(this.vstr.get(index)))
            return;
        this.vstr.set(index, value);
    }

    public void saveVstr(int index, String value) {
        if (value.equals(this.vstr.get(index)))
            return;
        this.vstr.set(index, value);
        saveField("vstr_" + index, this.vstr.get(index));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        for (int i = 0; i < this.vstr.size(); i++) {
            sBuilder.append(" `vstr_").append(i).append("` = '").append((this.vstr == null) ? null : ((String) this.vstr.get(i)).replace("'", "''")).append("',");
        }
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

