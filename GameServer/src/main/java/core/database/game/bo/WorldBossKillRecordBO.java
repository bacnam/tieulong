package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WorldBossKillRecordBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "bossId", comment = "BossId[对应于RefWorldBoss表ID]")
    private long bossId;
    @DataBaseField(type = "bigint(20)", fieldname = "bossLevel", comment = "Boss等级")
    private long bossLevel;
    @DataBaseField(type = "bigint(20)", fieldname = "killerPid", comment = "击杀者pid")
    private long killerPid;
    @DataBaseField(type = "varchar(500)", fieldname = "killerName", comment = "击杀者名称")
    private String killerName;
    @DataBaseField(type = "int(11)", fieldname = "killerLevel", comment = "击杀者等级")
    private int killerLevel;
    @DataBaseField(type = "int(11)", fieldname = "killerIcon", comment = "击杀者头像")
    private int killerIcon;
    @DataBaseField(type = "int(11)", fieldname = "deathTime", comment = "Boss死亡时间")
    private int deathTime;
    @DataBaseField(type = "bigint(20)", fieldname = "fightTime", comment = "战斗时间")
    private long fightTime;
    @DataBaseField(type = "varchar(500)", fieldname = "rewardItemId", comment = "奖励物品信息")
    private String rewardItemId;
    @DataBaseField(type = "varchar(500)", fieldname = "rewardItemCount", comment = "奖励物品数量")
    private String rewardItemCount;

    public WorldBossKillRecordBO() {
        this.id = 0L;
        this.bossId = 0L;
        this.bossLevel = 0L;
        this.killerPid = 0L;
        this.killerName = "";
        this.killerLevel = 0;
        this.killerIcon = 0;
        this.deathTime = 0;
        this.fightTime = 0L;
        this.rewardItemId = "";
        this.rewardItemCount = "";
    }

    public WorldBossKillRecordBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.bossId = rs.getLong(2);
        this.bossLevel = rs.getLong(3);
        this.killerPid = rs.getLong(4);
        this.killerName = rs.getString(5);
        this.killerLevel = rs.getInt(6);
        this.killerIcon = rs.getInt(7);
        this.deathTime = rs.getInt(8);
        this.fightTime = rs.getLong(9);
        this.rewardItemId = rs.getString(10);
        this.rewardItemCount = rs.getString(11);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `worldBossKillRecord` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`bossId` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BossId[对应于RefWorldBoss表ID]',`bossLevel` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Boss等级',`killerPid` bigint(20) NOT NULL DEFAULT '0' COMMENT '击杀者pid',`killerName` varchar(500) NOT NULL DEFAULT '' COMMENT '击杀者名称',`killerLevel` int(11) NOT NULL DEFAULT '0' COMMENT '击杀者等级',`killerIcon` int(11) NOT NULL DEFAULT '0' COMMENT '击杀者头像',`deathTime` int(11) NOT NULL DEFAULT '0' COMMENT 'Boss死亡时间',`fightTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '战斗时间',`rewardItemId` varchar(500) NOT NULL DEFAULT '' COMMENT '奖励物品信息',`rewardItemCount` varchar(500) NOT NULL DEFAULT '' COMMENT '奖励物品数量',PRIMARY KEY (`id`)) COMMENT='世界boss击杀记录'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<WorldBossKillRecordBO> list) throws Exception {
        list.add(new WorldBossKillRecordBO(rs));
    }

    public long getAsynTaskTag() {
        return 0L;
    }

    public String getItemsName() {
        return "`id`, `bossId`, `bossLevel`, `killerPid`, `killerName`, `killerLevel`, `killerIcon`, `deathTime`, `fightTime`, `rewardItemId`, `rewardItemCount`";
    }

    public String getTableName() {
        return "`worldBossKillRecord`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.bossId).append("', ");
        strBuf.append("'").append(this.bossLevel).append("', ");
        strBuf.append("'").append(this.killerPid).append("', ");
        strBuf.append("'").append((this.killerName == null) ? null : this.killerName.replace("'", "''")).append("', ");
        strBuf.append("'").append(this.killerLevel).append("', ");
        strBuf.append("'").append(this.killerIcon).append("', ");
        strBuf.append("'").append(this.deathTime).append("', ");
        strBuf.append("'").append(this.fightTime).append("', ");
        strBuf.append("'").append((this.rewardItemId == null) ? null : this.rewardItemId.replace("'", "''")).append("', ");
        strBuf.append("'").append((this.rewardItemCount == null) ? null : this.rewardItemCount.replace("'", "''")).append("', ");
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

    public long getBossId() {
        return this.bossId;
    }

    public void setBossId(long bossId) {
        if (bossId == this.bossId)
            return;
        this.bossId = bossId;
    }

    public void saveBossId(long bossId) {
        if (bossId == this.bossId)
            return;
        this.bossId = bossId;
        saveField("bossId", Long.valueOf(bossId));
    }

    public long getBossLevel() {
        return this.bossLevel;
    }

    public void setBossLevel(long bossLevel) {
        if (bossLevel == this.bossLevel)
            return;
        this.bossLevel = bossLevel;
    }

    public void saveBossLevel(long bossLevel) {
        if (bossLevel == this.bossLevel)
            return;
        this.bossLevel = bossLevel;
        saveField("bossLevel", Long.valueOf(bossLevel));
    }

    public long getKillerPid() {
        return this.killerPid;
    }

    public void setKillerPid(long killerPid) {
        if (killerPid == this.killerPid)
            return;
        this.killerPid = killerPid;
    }

    public void saveKillerPid(long killerPid) {
        if (killerPid == this.killerPid)
            return;
        this.killerPid = killerPid;
        saveField("killerPid", Long.valueOf(killerPid));
    }

    public String getKillerName() {
        return this.killerName;
    }

    public void setKillerName(String killerName) {
        if (killerName.equals(this.killerName))
            return;
        this.killerName = killerName;
    }

    public void saveKillerName(String killerName) {
        if (killerName.equals(this.killerName))
            return;
        this.killerName = killerName;
        saveField("killerName", killerName);
    }

    public int getKillerLevel() {
        return this.killerLevel;
    }

    public void setKillerLevel(int killerLevel) {
        if (killerLevel == this.killerLevel)
            return;
        this.killerLevel = killerLevel;
    }

    public void saveKillerLevel(int killerLevel) {
        if (killerLevel == this.killerLevel)
            return;
        this.killerLevel = killerLevel;
        saveField("killerLevel", Integer.valueOf(killerLevel));
    }

    public int getKillerIcon() {
        return this.killerIcon;
    }

    public void setKillerIcon(int killerIcon) {
        if (killerIcon == this.killerIcon)
            return;
        this.killerIcon = killerIcon;
    }

    public void saveKillerIcon(int killerIcon) {
        if (killerIcon == this.killerIcon)
            return;
        this.killerIcon = killerIcon;
        saveField("killerIcon", Integer.valueOf(killerIcon));
    }

    public int getDeathTime() {
        return this.deathTime;
    }

    public void setDeathTime(int deathTime) {
        if (deathTime == this.deathTime)
            return;
        this.deathTime = deathTime;
    }

    public void saveDeathTime(int deathTime) {
        if (deathTime == this.deathTime)
            return;
        this.deathTime = deathTime;
        saveField("deathTime", Integer.valueOf(deathTime));
    }

    public long getFightTime() {
        return this.fightTime;
    }

    public void setFightTime(long fightTime) {
        if (fightTime == this.fightTime)
            return;
        this.fightTime = fightTime;
    }

    public void saveFightTime(long fightTime) {
        if (fightTime == this.fightTime)
            return;
        this.fightTime = fightTime;
        saveField("fightTime", Long.valueOf(fightTime));
    }

    public String getRewardItemId() {
        return this.rewardItemId;
    }

    public void setRewardItemId(String rewardItemId) {
        if (rewardItemId.equals(this.rewardItemId))
            return;
        this.rewardItemId = rewardItemId;
    }

    public void saveRewardItemId(String rewardItemId) {
        if (rewardItemId.equals(this.rewardItemId))
            return;
        this.rewardItemId = rewardItemId;
        saveField("rewardItemId", rewardItemId);
    }

    public String getRewardItemCount() {
        return this.rewardItemCount;
    }

    public void setRewardItemCount(String rewardItemCount) {
        if (rewardItemCount.equals(this.rewardItemCount))
            return;
        this.rewardItemCount = rewardItemCount;
    }

    public void saveRewardItemCount(String rewardItemCount) {
        if (rewardItemCount.equals(this.rewardItemCount))
            return;
        this.rewardItemCount = rewardItemCount;
        saveField("rewardItemCount", rewardItemCount);
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `bossId` = '").append(this.bossId).append("',");
        sBuilder.append(" `bossLevel` = '").append(this.bossLevel).append("',");
        sBuilder.append(" `killerPid` = '").append(this.killerPid).append("',");
        sBuilder.append(" `killerName` = '").append((this.killerName == null) ? null : this.killerName.replace("'", "''")).append("',");
        sBuilder.append(" `killerLevel` = '").append(this.killerLevel).append("',");
        sBuilder.append(" `killerIcon` = '").append(this.killerIcon).append("',");
        sBuilder.append(" `deathTime` = '").append(this.deathTime).append("',");
        sBuilder.append(" `fightTime` = '").append(this.fightTime).append("',");
        sBuilder.append(" `rewardItemId` = '").append((this.rewardItemId == null) ? null : this.rewardItemId.replace("'", "''")).append("',");
        sBuilder.append(" `rewardItemCount` = '").append((this.rewardItemCount == null) ? null : this.rewardItemCount.replace("'", "''")).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

