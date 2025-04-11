package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GuildwarpuppetBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家ID")
    private long pid;
    @DataBaseField(type = "bigint(20)", fieldname = "guildId", comment = "公会ID")
    private long guildId;
    @DataBaseField(type = "int(11)", fieldname = "puppetId", comment = "傀儡ID")
    private int puppetId;
    @DataBaseField(type = "int(11)", fieldname = "applyTime", comment = "申请时间")
    private int applyTime;

    public GuildwarpuppetBO() {
        this.id = 0L;
        this.pid = 0L;
        this.guildId = 0L;
        this.puppetId = 0;
        this.applyTime = 0;
    }

    public GuildwarpuppetBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.guildId = rs.getLong(3);
        this.puppetId = rs.getInt(4);
        this.applyTime = rs.getInt(5);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `guildwarpuppet` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家ID',`guildId` bigint(20) NOT NULL DEFAULT '0' COMMENT '公会ID',`puppetId` int(11) NOT NULL DEFAULT '0' COMMENT '傀儡ID',`applyTime` int(11) NOT NULL DEFAULT '0' COMMENT '申请时间',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='玩家傀儡信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<GuildwarpuppetBO> list) throws Exception {
        list.add(new GuildwarpuppetBO(rs));
    }

    public long getAsynTaskTag() {
        return 0L;
    }

    public String getItemsName() {
        return "`id`, `pid`, `guildId`, `puppetId`, `applyTime`";
    }

    public String getTableName() {
        return "`guildwarpuppet`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.guildId).append("', ");
        strBuf.append("'").append(this.puppetId).append("', ");
        strBuf.append("'").append(this.applyTime).append("', ");
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

    public int getPuppetId() {
        return this.puppetId;
    }

    public void setPuppetId(int puppetId) {
        if (puppetId == this.puppetId)
            return;
        this.puppetId = puppetId;
    }

    public void savePuppetId(int puppetId) {
        if (puppetId == this.puppetId)
            return;
        this.puppetId = puppetId;
        saveField("puppetId", Integer.valueOf(puppetId));
    }

    public int getApplyTime() {
        return this.applyTime;
    }

    public void setApplyTime(int applyTime) {
        if (applyTime == this.applyTime)
            return;
        this.applyTime = applyTime;
    }

    public void saveApplyTime(int applyTime) {
        if (applyTime == this.applyTime)
            return;
        this.applyTime = applyTime;
        saveField("applyTime", Integer.valueOf(applyTime));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `guildId` = '").append(this.guildId).append("',");
        sBuilder.append(" `puppetId` = '").append(this.puppetId).append("',");
        sBuilder.append(" `applyTime` = '").append(this.applyTime).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

