package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GuildTaskLogBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
    private long pid;
    @DataBaseField(type = "bigint(20)", fieldname = "taskId", comment = "任务的sid")
    private long taskId;
    @DataBaseField(type = "int(11)", fieldname = "logTime", comment = "日志时间")
    private int logTime;
    @DataBaseField(type = "int(11)", fieldname = "action", comment = "任务动作")
    private int action;
    @DataBaseField(type = "int(11)", fieldname = "itemId", comment = "物品id")
    private int itemId;
    @DataBaseField(type = "int(11)", fieldname = "itemCount", comment = "物品数量")
    private int itemCount;
    @DataBaseField(type = "tinyint(1)", fieldname = "isDouble", comment = "双倍")
    private boolean isDouble;

    public GuildTaskLogBO() {
        this.id = 0L;
        this.pid = 0L;
        this.taskId = 0L;
        this.logTime = 0;
        this.action = 0;
        this.itemId = 0;
        this.itemCount = 0;
        this.isDouble = false;
    }

    public GuildTaskLogBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.taskId = rs.getLong(3);
        this.logTime = rs.getInt(4);
        this.action = rs.getInt(5);
        this.itemId = rs.getInt(6);
        this.itemCount = rs.getInt(7);
        this.isDouble = rs.getBoolean(8);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `guildTaskLog` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`taskId` bigint(20) NOT NULL DEFAULT '0' COMMENT '任务的sid',`logTime` int(11) NOT NULL DEFAULT '0' COMMENT '日志时间',`action` int(11) NOT NULL DEFAULT '0' COMMENT '任务动作',`itemId` int(11) NOT NULL DEFAULT '0' COMMENT '物品id',`itemCount` int(11) NOT NULL DEFAULT '0' COMMENT '物品数量',`isDouble` tinyint(1) NOT NULL DEFAULT '0' COMMENT '双倍',PRIMARY KEY (`id`)) COMMENT='竞技场信息表,记录每个玩家竞技场相关信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<GuildTaskLogBO> list) throws Exception {
        list.add(new GuildTaskLogBO(rs));
    }

    public long getAsynTaskTag() {
        return getId();
    }

    public String getItemsName() {
        return "`id`, `pid`, `taskId`, `logTime`, `action`, `itemId`, `itemCount`, `isDouble`";
    }

    public String getTableName() {
        return "`guildTaskLog`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.taskId).append("', ");
        strBuf.append("'").append(this.logTime).append("', ");
        strBuf.append("'").append(this.action).append("', ");
        strBuf.append("'").append(this.itemId).append("', ");
        strBuf.append("'").append(this.itemCount).append("', ");
        strBuf.append("'").append(this.isDouble ? 1 : 0).append("', ");
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

    public long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(long taskId) {
        if (taskId == this.taskId)
            return;
        this.taskId = taskId;
    }

    public void saveTaskId(long taskId) {
        if (taskId == this.taskId)
            return;
        this.taskId = taskId;
        saveField("taskId", Long.valueOf(taskId));
    }

    public int getLogTime() {
        return this.logTime;
    }

    public void setLogTime(int logTime) {
        if (logTime == this.logTime)
            return;
        this.logTime = logTime;
    }

    public void saveLogTime(int logTime) {
        if (logTime == this.logTime)
            return;
        this.logTime = logTime;
        saveField("logTime", Integer.valueOf(logTime));
    }

    public int getAction() {
        return this.action;
    }

    public void setAction(int action) {
        if (action == this.action)
            return;
        this.action = action;
    }

    public void saveAction(int action) {
        if (action == this.action)
            return;
        this.action = action;
        saveField("action", Integer.valueOf(action));
    }

    public int getItemId() {
        return this.itemId;
    }

    public void setItemId(int itemId) {
        if (itemId == this.itemId)
            return;
        this.itemId = itemId;
    }

    public void saveItemId(int itemId) {
        if (itemId == this.itemId)
            return;
        this.itemId = itemId;
        saveField("itemId", Integer.valueOf(itemId));
    }

    public int getItemCount() {
        return this.itemCount;
    }

    public void setItemCount(int itemCount) {
        if (itemCount == this.itemCount)
            return;
        this.itemCount = itemCount;
    }

    public void saveItemCount(int itemCount) {
        if (itemCount == this.itemCount)
            return;
        this.itemCount = itemCount;
        saveField("itemCount", Integer.valueOf(itemCount));
    }

    public boolean getIsDouble() {
        return this.isDouble;
    }

    public void setIsDouble(boolean isDouble) {
        if (isDouble == this.isDouble)
            return;
        this.isDouble = isDouble;
    }

    public void saveIsDouble(boolean isDouble) {
        if (isDouble == this.isDouble)
            return;
        this.isDouble = isDouble;
        saveField("isDouble", Integer.valueOf(isDouble ? 1 : 0));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `taskId` = '").append(this.taskId).append("',");
        sBuilder.append(" `logTime` = '").append(this.logTime).append("',");
        sBuilder.append(" `action` = '").append(this.action).append("',");
        sBuilder.append(" `itemId` = '").append(this.itemId).append("',");
        sBuilder.append(" `itemCount` = '").append(this.itemCount).append("',");
        sBuilder.append(" `isDouble` = '").append(this.isDouble ? 1 : 0).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

