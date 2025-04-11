package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GuildBoardBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "留言玩家id")
    private long pid;
    @DataBaseField(type = "varchar(500)", fieldname = "name", comment = "玩家名称")
    private String name;
    @DataBaseField(type = "int(11)", fieldname = "job", comment = "玩家职位")
    private int job;
    @DataBaseField(type = "bigint(20)", fieldname = "guildId", comment = "帮会ID")
    private long guildId;
    @DataBaseField(type = "varchar(500)", fieldname = "message", comment = "留言内容")
    private String message;
    @DataBaseField(type = "int(11)", fieldname = "messageTime", comment = "留言时间")
    private int messageTime;
    @DataBaseField(type = "tinyint(1)", fieldname = "top", comment = "置顶")
    private boolean top;

    public GuildBoardBO() {
        this.id = 0L;
        this.pid = 0L;
        this.name = "";
        this.job = 0;
        this.guildId = 0L;
        this.message = "";
        this.messageTime = 0;
        this.top = false;
    }

    public GuildBoardBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.name = rs.getString(3);
        this.job = rs.getInt(4);
        this.guildId = rs.getLong(5);
        this.message = rs.getString(6);
        this.messageTime = rs.getInt(7);
        this.top = rs.getBoolean(8);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `guildBoard` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '留言玩家id',`name` varchar(500) NOT NULL DEFAULT '' COMMENT '玩家名称',`job` int(11) NOT NULL DEFAULT '0' COMMENT '玩家职位',`guildId` bigint(20) NOT NULL DEFAULT '0' COMMENT '帮会ID',`message` varchar(500) NOT NULL DEFAULT '' COMMENT '留言内容',`messageTime` int(11) NOT NULL DEFAULT '0' COMMENT '留言时间',`top` tinyint(1) NOT NULL DEFAULT '0' COMMENT '置顶',PRIMARY KEY (`id`)) COMMENT='竞技场信息表,记录每个玩家竞技场相关信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<GuildBoardBO> list) throws Exception {
        list.add(new GuildBoardBO(rs));
    }

    public long getAsynTaskTag() {
        return getPid();
    }

    public String getItemsName() {
        return "`id`, `pid`, `name`, `job`, `guildId`, `message`, `messageTime`, `top`";
    }

    public String getTableName() {
        return "`guildBoard`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append((this.name == null) ? null : this.name.replace("'", "''")).append("', ");
        strBuf.append("'").append(this.job).append("', ");
        strBuf.append("'").append(this.guildId).append("', ");
        strBuf.append("'").append((this.message == null) ? null : this.message.replace("'", "''")).append("', ");
        strBuf.append("'").append(this.messageTime).append("', ");
        strBuf.append("'").append(this.top ? 1 : 0).append("', ");
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name.equals(this.name))
            return;
        this.name = name;
    }

    public void saveName(String name) {
        if (name.equals(this.name))
            return;
        this.name = name;
        saveField("name", name);
    }

    public int getJob() {
        return this.job;
    }

    public void setJob(int job) {
        if (job == this.job)
            return;
        this.job = job;
    }

    public void saveJob(int job) {
        if (job == this.job)
            return;
        this.job = job;
        saveField("job", Integer.valueOf(job));
    }

    public long getGuildId() {
        return this.guildId;
    }

    public void setGuildId(long guildId) {
        if (guildId == this.guildId)
            return;
        this.guildId = guildId;
    }

    public void saveGuildId(long guildId) {
        if (guildId == this.guildId)
            return;
        this.guildId = guildId;
        saveField("guildId", Long.valueOf(guildId));
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        if (message.equals(this.message))
            return;
        this.message = message;
    }

    public void saveMessage(String message) {
        if (message.equals(this.message))
            return;
        this.message = message;
        saveField("message", message);
    }

    public int getMessageTime() {
        return this.messageTime;
    }

    public void setMessageTime(int messageTime) {
        if (messageTime == this.messageTime)
            return;
        this.messageTime = messageTime;
    }

    public void saveMessageTime(int messageTime) {
        if (messageTime == this.messageTime)
            return;
        this.messageTime = messageTime;
        saveField("messageTime", Integer.valueOf(messageTime));
    }

    public boolean getTop() {
        return this.top;
    }

    public void setTop(boolean top) {
        if (top == this.top)
            return;
        this.top = top;
    }

    public void saveTop(boolean top) {
        if (top == this.top)
            return;
        this.top = top;
        saveField("top", Integer.valueOf(top ? 1 : 0));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `name` = '").append((this.name == null) ? null : this.name.replace("'", "''")).append("',");
        sBuilder.append(" `job` = '").append(this.job).append("',");
        sBuilder.append(" `guildId` = '").append(this.guildId).append("',");
        sBuilder.append(" `message` = '").append((this.message == null) ? null : this.message.replace("'", "''")).append("',");
        sBuilder.append(" `messageTime` = '").append(this.messageTime).append("',");
        sBuilder.append(" `top` = '").append(this.top ? 1 : 0).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

