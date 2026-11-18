package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DailyactiveBO extends BaseBO {
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "id",
        comment = "数据库表唯一键值"
    )
    private long id;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "pid",
        comment = "*玩家ID"
    )
    private long pid;
    @DataBaseField(
        type = "int(11)",
        fieldname = "value",
        comment = "当前活跃值"
    )
    private int value;
    @DataBaseField(
        type = "int(11)",
        fieldname = "teamLevel",
        comment = "当天刷新时的玩家等级"
    )
    private int teamLevel;
    @DataBaseField(
        type = "varchar(500)",
        fieldname = "fetchedTaskIndex",
        comment = "已经领取奖励的序号"
    )
    private String fetchedTaskIndex;

    public DailyactiveBO() {
        this.id = 0L;
        this.pid = 0L;
        this.value = 0;
        this.teamLevel = 0;
        this.fetchedTaskIndex = "";
    }

    public DailyactiveBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.value = rs.getInt(3);
        this.teamLevel = rs.getInt(4);
        this.fetchedTaskIndex = rs.getString(5);
    }

    @Override
    public void getFromResultSet(ResultSet rs, List list) throws Exception {
        list.add(new DailyactiveBO(rs));
    }

    @Override
    public long getAsynTaskTag() {
        return this.getPid();
    }

    @Override
    public String getItemsName() {
        return "`id`, `pid`, `value`, `teamLevel`, `fetchedTaskIndex`";
    }

    @Override
    public String getTableName() {
        return "`dailyactive`";
    }

    @Override
    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.value).append("', ");
        strBuf.append("'").append(this.teamLevel).append("', ");
        strBuf.append("'").append(this.fetchedTaskIndex == null ? null : this.fetchedTaskIndex.replace("'", "''")).append("', ");
        strBuf.deleteCharAt(strBuf.length() - 2);
        return strBuf.toString();
    }

    @Override
    public ArrayList<byte[]> getInsertValueBytes() {
        return new ArrayList<>();
    }

    @Override
    public void setId(long iID) {
        this.id = iID;
    }

    @Override
    public long getId() {
        return this.id;
    }

    public long getPid() {
        return this.pid;
    }

    public void setPid(long pid) {
        if (pid != this.pid) {
            this.pid = pid;
        }
    }

    public void savePid(long pid) {
        if (pid != this.pid) {
            this.pid = pid;
            this.saveField("pid", pid);
        }
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        if (value != this.value) {
            this.value = value;
        }
    }

    public void saveValue(int value) {
        if (value != this.value) {
            this.value = value;
            this.saveField("value", value);
        }
    }

    public int getTeamLevel() {
        return this.teamLevel;
    }

    public void setTeamLevel(int teamLevel) {
        if (teamLevel != this.teamLevel) {
            this.teamLevel = teamLevel;
        }
    }

    public void saveTeamLevel(int teamLevel) {
        if (teamLevel != this.teamLevel) {
            this.teamLevel = teamLevel;
            this.saveField("teamLevel", teamLevel);
        }
    }

    public String getFetchedTaskIndex() {
        return this.fetchedTaskIndex;
    }

    public void setFetchedTaskIndex(String fetchedTaskIndex) {
        if (!fetchedTaskIndex.equals(this.fetchedTaskIndex)) {
            this.fetchedTaskIndex = fetchedTaskIndex;
        }
    }

    public void saveFetchedTaskIndex(String fetchedTaskIndex) {
        if (!fetchedTaskIndex.equals(this.fetchedTaskIndex)) {
            this.fetchedTaskIndex = fetchedTaskIndex;
            this.saveField("fetchedTaskIndex", fetchedTaskIndex);
        }
    }

    @Override
    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `value` = '").append(this.value).append("',");
        sBuilder.append(" `teamLevel` = '").append(this.teamLevel).append("',");
        sBuilder.append(" `fetchedTaskIndex` = '").append(this.fetchedTaskIndex == null ? null : this.fetchedTaskIndex.replace("'", "''")).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }

    public static String getSql_TableCreate() {
        return "CREATE TABLE IF NOT EXISTS `dailyactive` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '*玩家ID',`value` int(11) NOT NULL DEFAULT '0' COMMENT '当前活跃值',`teamLevel` int(11) NOT NULL DEFAULT '0' COMMENT '当天刷新时的玩家等级',`fetchedTaskIndex` varchar(500) NOT NULL DEFAULT '' COMMENT '已经领取奖励的序号',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='任务活跃度信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT="
            + (ServerConfig.getInitialID() + 1L);
    }
}
