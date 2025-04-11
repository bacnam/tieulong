package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GuildMemberBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家pid")
    private long pid;
    @DataBaseField(type = "bigint(20)", fieldname = "guildId", comment = "帮会Id")
    private long guildId;
    @DataBaseField(type = "int(11)", fieldname = "donate", comment = "贡献值")
    private int donate;
    @DataBaseField(type = "int(11)", fieldname = "weekDonate", comment = "周贡献值")
    private int weekDonate;
    @DataBaseField(type = "int(11)", fieldname = "historyDonate", comment = "历史贡献值")
    private int historyDonate;
    @DataBaseField(type = "int(11)", fieldname = "job", comment = "帮会职务")
    private int job;
    @DataBaseField(type = "int(11)", fieldname = "lastLeaveTime", comment = "上一次退出时间")
    private int lastLeaveTime;
    @DataBaseField(type = "int(11)", fieldname = "joinTime", comment = "加入时间")
    private int joinTime;
    @DataBaseField(type = "int(11)", fieldname = "sacrificeDonate", comment = "当天献祭贡献值")
    private int sacrificeDonate;
    @DataBaseField(type = "int(11)", fieldname = "sacrificeStatus", comment = "祭天状态")
    private int sacrificeStatus;
    @DataBaseField(type = "int(11)", fieldname = "skillpoints", comment = "帮派技能点")
    private int skillpoints;
    @DataBaseField(type = "int(11)", fieldname = "guildwarInspire", comment = "帮派战鼓舞次数")
    private int guildwarInspire;
    @DataBaseField(type = "int(11)", fieldname = "longnvPickReward", comment = "守卫龙女领奖次数")
    private int longnvPickReward;

    public GuildMemberBO() {
        this.id = 0L;
        this.pid = 0L;
        this.guildId = 0L;
        this.donate = 0;
        this.weekDonate = 0;
        this.historyDonate = 0;
        this.job = 0;
        this.lastLeaveTime = 0;
        this.joinTime = 0;
        this.sacrificeDonate = 0;
        this.sacrificeStatus = 0;
        this.skillpoints = 0;
        this.guildwarInspire = 0;
        this.longnvPickReward = 0;
    }

    public GuildMemberBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.guildId = rs.getLong(3);
        this.donate = rs.getInt(4);
        this.weekDonate = rs.getInt(5);
        this.historyDonate = rs.getInt(6);
        this.job = rs.getInt(7);
        this.lastLeaveTime = rs.getInt(8);
        this.joinTime = rs.getInt(9);
        this.sacrificeDonate = rs.getInt(10);
        this.sacrificeStatus = rs.getInt(11);
        this.skillpoints = rs.getInt(12);
        this.guildwarInspire = rs.getInt(13);
        this.longnvPickReward = rs.getInt(14);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `guildMember` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家pid',`guildId` bigint(20) NOT NULL DEFAULT '0' COMMENT '帮会Id',`donate` int(11) NOT NULL DEFAULT '0' COMMENT '贡献值',`weekDonate` int(11) NOT NULL DEFAULT '0' COMMENT '周贡献值',`historyDonate` int(11) NOT NULL DEFAULT '0' COMMENT '历史贡献值',`job` int(11) NOT NULL DEFAULT '0' COMMENT '帮会职务',`lastLeaveTime` int(11) NOT NULL DEFAULT '0' COMMENT '上一次退出时间',`joinTime` int(11) NOT NULL DEFAULT '0' COMMENT '加入时间',`sacrificeDonate` int(11) NOT NULL DEFAULT '0' COMMENT '当天献祭贡献值',`sacrificeStatus` int(11) NOT NULL DEFAULT '0' COMMENT '祭天状态',`skillpoints` int(11) NOT NULL DEFAULT '0' COMMENT '帮派技能点',`guildwarInspire` int(11) NOT NULL DEFAULT '0' COMMENT '帮派战鼓舞次数',`longnvPickReward` int(11) NOT NULL DEFAULT '0' COMMENT '守卫龙女领奖次数',PRIMARY KEY (`id`)) COMMENT='帮会成员信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<GuildMemberBO> list) throws Exception {
        list.add(new GuildMemberBO(rs));
    }

    public long getAsynTaskTag() {
        return getId();
    }

    public String getItemsName() {
        return "`id`, `pid`, `guildId`, `donate`, `weekDonate`, `historyDonate`, `job`, `lastLeaveTime`, `joinTime`, `sacrificeDonate`, `sacrificeStatus`, `skillpoints`, `guildwarInspire`, `longnvPickReward`";
    }

    public String getTableName() {
        return "`guildMember`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.guildId).append("', ");
        strBuf.append("'").append(this.donate).append("', ");
        strBuf.append("'").append(this.weekDonate).append("', ");
        strBuf.append("'").append(this.historyDonate).append("', ");
        strBuf.append("'").append(this.job).append("', ");
        strBuf.append("'").append(this.lastLeaveTime).append("', ");
        strBuf.append("'").append(this.joinTime).append("', ");
        strBuf.append("'").append(this.sacrificeDonate).append("', ");
        strBuf.append("'").append(this.sacrificeStatus).append("', ");
        strBuf.append("'").append(this.skillpoints).append("', ");
        strBuf.append("'").append(this.guildwarInspire).append("', ");
        strBuf.append("'").append(this.longnvPickReward).append("', ");
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

    public int getDonate() {
        return this.donate;
    }

    public void setDonate(int donate) {
        if (donate == this.donate)
            return;
        this.donate = donate;
    }

    public void saveDonate(int donate) {
        if (donate == this.donate)
            return;
        this.donate = donate;
        saveField("donate", Integer.valueOf(donate));
    }

    public int getWeekDonate() {
        return this.weekDonate;
    }

    public void setWeekDonate(int weekDonate) {
        if (weekDonate == this.weekDonate)
            return;
        this.weekDonate = weekDonate;
    }

    public void saveWeekDonate(int weekDonate) {
        if (weekDonate == this.weekDonate)
            return;
        this.weekDonate = weekDonate;
        saveField("weekDonate", Integer.valueOf(weekDonate));
    }

    public int getHistoryDonate() {
        return this.historyDonate;
    }

    public void setHistoryDonate(int historyDonate) {
        if (historyDonate == this.historyDonate)
            return;
        this.historyDonate = historyDonate;
    }

    public void saveHistoryDonate(int historyDonate) {
        if (historyDonate == this.historyDonate)
            return;
        this.historyDonate = historyDonate;
        saveField("historyDonate", Integer.valueOf(historyDonate));
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

    public int getLastLeaveTime() {
        return this.lastLeaveTime;
    }

    public void setLastLeaveTime(int lastLeaveTime) {
        if (lastLeaveTime == this.lastLeaveTime)
            return;
        this.lastLeaveTime = lastLeaveTime;
    }

    public void saveLastLeaveTime(int lastLeaveTime) {
        if (lastLeaveTime == this.lastLeaveTime)
            return;
        this.lastLeaveTime = lastLeaveTime;
        saveField("lastLeaveTime", Integer.valueOf(lastLeaveTime));
    }

    public int getJoinTime() {
        return this.joinTime;
    }

    public void setJoinTime(int joinTime) {
        if (joinTime == this.joinTime)
            return;
        this.joinTime = joinTime;
    }

    public void saveJoinTime(int joinTime) {
        if (joinTime == this.joinTime)
            return;
        this.joinTime = joinTime;
        saveField("joinTime", Integer.valueOf(joinTime));
    }

    public int getSacrificeDonate() {
        return this.sacrificeDonate;
    }

    public void setSacrificeDonate(int sacrificeDonate) {
        if (sacrificeDonate == this.sacrificeDonate)
            return;
        this.sacrificeDonate = sacrificeDonate;
    }

    public void saveSacrificeDonate(int sacrificeDonate) {
        if (sacrificeDonate == this.sacrificeDonate)
            return;
        this.sacrificeDonate = sacrificeDonate;
        saveField("sacrificeDonate", Integer.valueOf(sacrificeDonate));
    }

    public int getSacrificeStatus() {
        return this.sacrificeStatus;
    }

    public void setSacrificeStatus(int sacrificeStatus) {
        if (sacrificeStatus == this.sacrificeStatus)
            return;
        this.sacrificeStatus = sacrificeStatus;
    }

    public void saveSacrificeStatus(int sacrificeStatus) {
        if (sacrificeStatus == this.sacrificeStatus)
            return;
        this.sacrificeStatus = sacrificeStatus;
        saveField("sacrificeStatus", Integer.valueOf(sacrificeStatus));
    }

    public int getSkillpoints() {
        return this.skillpoints;
    }

    public void setSkillpoints(int skillpoints) {
        if (skillpoints == this.skillpoints)
            return;
        this.skillpoints = skillpoints;
    }

    public void saveSkillpoints(int skillpoints) {
        if (skillpoints == this.skillpoints)
            return;
        this.skillpoints = skillpoints;
        saveField("skillpoints", Integer.valueOf(skillpoints));
    }

    public int getGuildwarInspire() {
        return this.guildwarInspire;
    }

    public void setGuildwarInspire(int guildwarInspire) {
        if (guildwarInspire == this.guildwarInspire)
            return;
        this.guildwarInspire = guildwarInspire;
    }

    public void saveGuildwarInspire(int guildwarInspire) {
        if (guildwarInspire == this.guildwarInspire)
            return;
        this.guildwarInspire = guildwarInspire;
        saveField("guildwarInspire", Integer.valueOf(guildwarInspire));
    }

    public int getLongnvPickReward() {
        return this.longnvPickReward;
    }

    public void setLongnvPickReward(int longnvPickReward) {
        if (longnvPickReward == this.longnvPickReward)
            return;
        this.longnvPickReward = longnvPickReward;
    }

    public void saveLongnvPickReward(int longnvPickReward) {
        if (longnvPickReward == this.longnvPickReward)
            return;
        this.longnvPickReward = longnvPickReward;
        saveField("longnvPickReward", Integer.valueOf(longnvPickReward));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `guildId` = '").append(this.guildId).append("',");
        sBuilder.append(" `donate` = '").append(this.donate).append("',");
        sBuilder.append(" `weekDonate` = '").append(this.weekDonate).append("',");
        sBuilder.append(" `historyDonate` = '").append(this.historyDonate).append("',");
        sBuilder.append(" `job` = '").append(this.job).append("',");
        sBuilder.append(" `lastLeaveTime` = '").append(this.lastLeaveTime).append("',");
        sBuilder.append(" `joinTime` = '").append(this.joinTime).append("',");
        sBuilder.append(" `sacrificeDonate` = '").append(this.sacrificeDonate).append("',");
        sBuilder.append(" `sacrificeStatus` = '").append(this.sacrificeStatus).append("',");
        sBuilder.append(" `skillpoints` = '").append(this.skillpoints).append("',");
        sBuilder.append(" `guildwarInspire` = '").append(this.guildwarInspire).append("',");
        sBuilder.append(" `longnvPickReward` = '").append(this.longnvPickReward).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

