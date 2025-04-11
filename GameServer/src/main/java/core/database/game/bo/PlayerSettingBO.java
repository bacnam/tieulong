package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PlayerSettingBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "*所属玩家ID")
    private long pid;
    @DataBaseField(type = "int(11)", fieldname = "type", comment = "设置类型,对应枚举类型ProtoEnum.SettingType")
    private int type;
    @DataBaseField(type = "int(11)", fieldname = "value", comment = "设置对应值")
    private int value;
    @DataBaseField(type = "int(11)", fieldname = "lastChangeTime", comment = "最近一次修改时间,单位秒")
    private int lastChangeTime;

    public PlayerSettingBO() {
        this.id = 0L;
        this.pid = 0L;
        this.type = 0;
        this.value = 0;
        this.lastChangeTime = 0;
    }

    public PlayerSettingBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.type = rs.getInt(3);
        this.value = rs.getInt(4);
        this.lastChangeTime = rs.getInt(5);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `playerSetting` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '*所属玩家ID',`type` int(11) NOT NULL DEFAULT '0' COMMENT '设置类型,对应枚举类型ProtoEnum.SettingType',`value` int(11) NOT NULL DEFAULT '0' COMMENT '设置对应值',`lastChangeTime` int(11) NOT NULL DEFAULT '0' COMMENT '最近一次修改时间,单位秒',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='玩家设置表,按表的id字段排序'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<PlayerSettingBO> list) throws Exception {
        list.add(new PlayerSettingBO(rs));
    }

    public long getAsynTaskTag() {
        return 0L;
    }

    public String getItemsName() {
        return "`id`, `pid`, `type`, `value`, `lastChangeTime`";
    }

    public String getTableName() {
        return "`playerSetting`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.type).append("', ");
        strBuf.append("'").append(this.value).append("', ");
        strBuf.append("'").append(this.lastChangeTime).append("', ");
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

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        if (type == this.type)
            return;
        this.type = type;
    }

    public void saveType(int type) {
        if (type == this.type)
            return;
        this.type = type;
        saveField("type", Integer.valueOf(type));
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        if (value == this.value)
            return;
        this.value = value;
    }

    public void saveValue(int value) {
        if (value == this.value)
            return;
        this.value = value;
        saveField("value", Integer.valueOf(value));
    }

    public int getLastChangeTime() {
        return this.lastChangeTime;
    }

    public void setLastChangeTime(int lastChangeTime) {
        if (lastChangeTime == this.lastChangeTime)
            return;
        this.lastChangeTime = lastChangeTime;
    }

    public void saveLastChangeTime(int lastChangeTime) {
        if (lastChangeTime == this.lastChangeTime)
            return;
        this.lastChangeTime = lastChangeTime;
        saveField("lastChangeTime", Integer.valueOf(lastChangeTime));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `type` = '").append(this.type).append("',");
        sBuilder.append(" `value` = '").append(this.value).append("',");
        sBuilder.append(" `lastChangeTime` = '").append(this.lastChangeTime).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

