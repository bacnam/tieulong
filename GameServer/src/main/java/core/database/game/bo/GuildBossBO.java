package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GuildBossBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "guildId", comment = "帮派Id")
    private long guildId;
    @DataBaseField(type = "int(11)", fieldname = "bossId", comment = "Boss关卡ID(GuildBoss静态表ID)")
    private int bossId;
    @DataBaseField(type = "bigint(20)", fieldname = "bossHp", comment = "Boss当前生命(复活时刷新)")
    private long bossHp;
    @DataBaseField(type = "bigint(20)", fieldname = "bossMaxHp", comment = "Boss最大生命(复活时刷新)")
    private long bossMaxHp;
    @DataBaseField(type = "tinyint(1)", fieldname = "isDead", comment = "Boss是否已死亡")
    private boolean isDead;
    @DataBaseField(type = "tinyint(1)", fieldname = "isOpen", comment = "Boss是否已开启")
    private boolean isOpen;
    @DataBaseField(type = "bigint(20)", fieldname = "deadTime", comment = "Boss死亡时间")
    private long deadTime;
    @DataBaseField(type = "bigint(20)", fieldname = "reviveTime", comment = "Boss复活时间")
    private long reviveTime;
    @DataBaseField(type = "bigint(20)", fieldname = "lastKillCid", comment = "上次击杀者pid")
    private long lastKillCid;

    public GuildBossBO() {
        this.id = 0L;
        this.guildId = 0L;
        this.bossId = 0;
        this.bossHp = 0L;
        this.bossMaxHp = 0L;
        this.isDead = false;
        this.isOpen = false;
        this.deadTime = 0L;
        this.reviveTime = 0L;
        this.lastKillCid = 0L;
    }

    public GuildBossBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.guildId = rs.getLong(2);
        this.bossId = rs.getInt(3);
        this.bossHp = rs.getLong(4);
        this.bossMaxHp = rs.getLong(5);
        this.isDead = rs.getBoolean(6);
        this.isOpen = rs.getBoolean(7);
        this.deadTime = rs.getLong(8);
        this.reviveTime = rs.getLong(9);
        this.lastKillCid = rs.getLong(10);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `guildBoss` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`guildId` bigint(20) NOT NULL DEFAULT '0' COMMENT '帮派Id',`bossId` int(11) NOT NULL DEFAULT '0' COMMENT 'Boss关卡ID(GuildBoss静态表ID)',`bossHp` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Boss当前生命(复活时刷新)',`bossMaxHp` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Boss最大生命(复活时刷新)',`isDead` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Boss是否已死亡',`isOpen` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Boss是否已开启',`deadTime` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Boss死亡时间',`reviveTime` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Boss复活时间',`lastKillCid` bigint(20) NOT NULL DEFAULT '0' COMMENT '上次击杀者pid',PRIMARY KEY (`id`)) COMMENT='帮派boss信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<GuildBossBO> list) throws Exception {
        list.add(new GuildBossBO(rs));
    }

    public long getAsynTaskTag() {
        return 0L;
    }

    public String getItemsName() {
        return "`id`, `guildId`, `bossId`, `bossHp`, `bossMaxHp`, `isDead`, `isOpen`, `deadTime`, `reviveTime`, `lastKillCid`";
    }

    public String getTableName() {
        return "`guildBoss`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.guildId).append("', ");
        strBuf.append("'").append(this.bossId).append("', ");
        strBuf.append("'").append(this.bossHp).append("', ");
        strBuf.append("'").append(this.bossMaxHp).append("', ");
        strBuf.append("'").append(this.isDead ? 1 : 0).append("', ");
        strBuf.append("'").append(this.isOpen ? 1 : 0).append("', ");
        strBuf.append("'").append(this.deadTime).append("', ");
        strBuf.append("'").append(this.reviveTime).append("', ");
        strBuf.append("'").append(this.lastKillCid).append("', ");
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

    public int getBossId() {
        return this.bossId;
    }

    public void setBossId(int bossId) {
        if (bossId == this.bossId)
            return;
        this.bossId = bossId;
    }

    public void saveBossId(int bossId) {
        if (bossId == this.bossId)
            return;
        this.bossId = bossId;
        saveField("bossId", Integer.valueOf(bossId));
    }

    public long getBossHp() {
        return this.bossHp;
    }

    public void setBossHp(long bossHp) {
        if (bossHp == this.bossHp)
            return;
        this.bossHp = bossHp;
    }

    public void saveBossHp(long bossHp) {
        if (bossHp == this.bossHp)
            return;
        this.bossHp = bossHp;
        saveField("bossHp", Long.valueOf(bossHp));
    }

    public long getBossMaxHp() {
        return this.bossMaxHp;
    }

    public void setBossMaxHp(long bossMaxHp) {
        if (bossMaxHp == this.bossMaxHp)
            return;
        this.bossMaxHp = bossMaxHp;
    }

    public void saveBossMaxHp(long bossMaxHp) {
        if (bossMaxHp == this.bossMaxHp)
            return;
        this.bossMaxHp = bossMaxHp;
        saveField("bossMaxHp", Long.valueOf(bossMaxHp));
    }

    public boolean getIsDead() {
        return this.isDead;
    }

    public void setIsDead(boolean isDead) {
        if (isDead == this.isDead)
            return;
        this.isDead = isDead;
    }

    public void saveIsDead(boolean isDead) {
        if (isDead == this.isDead)
            return;
        this.isDead = isDead;
        saveField("isDead", Integer.valueOf(isDead ? 1 : 0));
    }

    public boolean getIsOpen() {
        return this.isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        if (isOpen == this.isOpen)
            return;
        this.isOpen = isOpen;
    }

    public void saveIsOpen(boolean isOpen) {
        if (isOpen == this.isOpen)
            return;
        this.isOpen = isOpen;
        saveField("isOpen", Integer.valueOf(isOpen ? 1 : 0));
    }

    public long getDeadTime() {
        return this.deadTime;
    }

    public void setDeadTime(long deadTime) {
        if (deadTime == this.deadTime)
            return;
        this.deadTime = deadTime;
    }

    public void saveDeadTime(long deadTime) {
        if (deadTime == this.deadTime)
            return;
        this.deadTime = deadTime;
        saveField("deadTime", Long.valueOf(deadTime));
    }

    public long getReviveTime() {
        return this.reviveTime;
    }

    public void setReviveTime(long reviveTime) {
        if (reviveTime == this.reviveTime)
            return;
        this.reviveTime = reviveTime;
    }

    public void saveReviveTime(long reviveTime) {
        if (reviveTime == this.reviveTime)
            return;
        this.reviveTime = reviveTime;
        saveField("reviveTime", Long.valueOf(reviveTime));
    }

    public long getLastKillCid() {
        return this.lastKillCid;
    }

    public void setLastKillCid(long lastKillCid) {
        if (lastKillCid == this.lastKillCid)
            return;
        this.lastKillCid = lastKillCid;
    }

    public void saveLastKillCid(long lastKillCid) {
        if (lastKillCid == this.lastKillCid)
            return;
        this.lastKillCid = lastKillCid;
        saveField("lastKillCid", Long.valueOf(lastKillCid));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `guildId` = '").append(this.guildId).append("',");
        sBuilder.append(" `bossId` = '").append(this.bossId).append("',");
        sBuilder.append(" `bossHp` = '").append(this.bossHp).append("',");
        sBuilder.append(" `bossMaxHp` = '").append(this.bossMaxHp).append("',");
        sBuilder.append(" `isDead` = '").append(this.isDead ? 1 : 0).append("',");
        sBuilder.append(" `isOpen` = '").append(this.isOpen ? 1 : 0).append("',");
        sBuilder.append(" `deadTime` = '").append(this.deadTime).append("',");
        sBuilder.append(" `reviveTime` = '").append(this.reviveTime).append("',");
        sBuilder.append(" `lastKillCid` = '").append(this.lastKillCid).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

