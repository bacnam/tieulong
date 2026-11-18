package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WorldBossChallengeBO extends BaseBO {
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "id",
        comment = "数据库表唯一键值"
    )
    private long id;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "pid",
        comment = "玩家pid"
    )
    private long pid;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "teamLevel",
        comment = "所属玩家等级"
    )
    private long teamLevel;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "challengeTimes",
        comment = "挑战次数"
    )
    private long challengeTimes;
    @DataBaseField(
        type = "bigint(20)",
        size = 10,
        fieldname = "inspiringTimes",
        comment = "鼓舞次数"
    )
    private List<Long> inspiringTimes;
    @DataBaseField(
        type = "bigint(20)",
        size = 10,
        fieldname = "totalDamage",
        comment = "BOSS伤害"
    )
    private List<Long> totalDamage;
    @DataBaseField(
        type = "bigint(20)",
        size = 10,
        fieldname = "damageRank",
        comment = "BOSS伤害"
    )
    private List<Long> damageRank;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "beginFightTime",
        comment = "玩家开始挑战boss时间,用于计算玩家战斗时间"
    )
    private long beginFightTime;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "leaveFightTime",
        comment = "玩家退出boss时间,用于计算玩家挑战CD时间"
    )
    private long leaveFightTime;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "attackTimes",
        comment = "今日攻击次数"
    )
    private long attackTimes;
    @DataBaseField(
        type = "int(11)",
        fieldname = "resurrection",
        comment = "复活次数"
    )
    private int resurrection;
    @DataBaseField(
        type = "tinyint(1)",
        fieldname = "autoChallenge",
        comment = "定时自动挑战世界BOSS"
    )
    private boolean autoChallenge;

    public WorldBossChallengeBO() {
        this.id = 0L;
        this.pid = 0L;
        this.teamLevel = 0L;
        this.challengeTimes = 0L;
        this.inspiringTimes = new ArrayList<>(10);

        for (int i = 0; i < 10; i++) {
            this.inspiringTimes.add(0L);
        }

        this.totalDamage = new ArrayList<>(10);

        for (int i = 0; i < 10; i++) {
            this.totalDamage.add(0L);
        }

        this.damageRank = new ArrayList<>(10);

        for (int i = 0; i < 10; i++) {
            this.damageRank.add(0L);
        }

        this.beginFightTime = 0L;
        this.leaveFightTime = 0L;
        this.attackTimes = 0L;
        this.resurrection = 0;
        this.autoChallenge = false;
    }

    public WorldBossChallengeBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.teamLevel = rs.getLong(3);
        this.challengeTimes = rs.getLong(4);
        this.inspiringTimes = new ArrayList<>(10);

        for (int i = 0; i < 10; i++) {
            this.inspiringTimes.add(rs.getLong(i + 5));
        }

        this.totalDamage = new ArrayList<>(10);

        for (int i = 0; i < 10; i++) {
            this.totalDamage.add(rs.getLong(i + 15));
        }

        this.damageRank = new ArrayList<>(10);

        for (int i = 0; i < 10; i++) {
            this.damageRank.add(rs.getLong(i + 25));
        }

        this.beginFightTime = rs.getLong(35);
        this.leaveFightTime = rs.getLong(36);
        this.attackTimes = rs.getLong(37);
        this.resurrection = rs.getInt(38);
        this.autoChallenge = rs.getBoolean(39);
    }

    @Override
    public void getFromResultSet(ResultSet rs, List list) throws Exception {
        list.add(new WorldBossChallengeBO(rs));
    }

    @Override
    public long getAsynTaskTag() {
        return this.getPid();
    }

    @Override
    public String getItemsName() {
        return "`id`, `pid`, `teamLevel`, `challengeTimes`, `inspiringTimes_0`, `inspiringTimes_1`, `inspiringTimes_2`, `inspiringTimes_3`, `inspiringTimes_4`, `inspiringTimes_5`, `inspiringTimes_6`, `inspiringTimes_7`, `inspiringTimes_8`, `inspiringTimes_9`, `totalDamage_0`, `totalDamage_1`, `totalDamage_2`, `totalDamage_3`, `totalDamage_4`, `totalDamage_5`, `totalDamage_6`, `totalDamage_7`, `totalDamage_8`, `totalDamage_9`, `damageRank_0`, `damageRank_1`, `damageRank_2`, `damageRank_3`, `damageRank_4`, `damageRank_5`, `damageRank_6`, `damageRank_7`, `damageRank_8`, `damageRank_9`, `beginFightTime`, `leaveFightTime`, `attackTimes`, `resurrection`, `autoChallenge`";
    }

    @Override
    public String getTableName() {
        return "`worldBossChallenge`";
    }

    @Override
    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.teamLevel).append("', ");
        strBuf.append("'").append(this.challengeTimes).append("', ");

        for (int i = 0; i < this.inspiringTimes.size(); i++) {
            strBuf.append("'").append(this.inspiringTimes.get(i)).append("', ");
        }

        for (int i = 0; i < this.totalDamage.size(); i++) {
            strBuf.append("'").append(this.totalDamage.get(i)).append("', ");
        }

        for (int i = 0; i < this.damageRank.size(); i++) {
            strBuf.append("'").append(this.damageRank.get(i)).append("', ");
        }

        strBuf.append("'").append(this.beginFightTime).append("', ");
        strBuf.append("'").append(this.leaveFightTime).append("', ");
        strBuf.append("'").append(this.attackTimes).append("', ");
        strBuf.append("'").append(this.resurrection).append("', ");
        strBuf.append("'").append(this.autoChallenge ? 1 : 0).append("', ");
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

    public long getTeamLevel() {
        return this.teamLevel;
    }

    public void setTeamLevel(long teamLevel) {
        if (teamLevel != this.teamLevel) {
            this.teamLevel = teamLevel;
        }
    }

    public void saveTeamLevel(long teamLevel) {
        if (teamLevel != this.teamLevel) {
            this.teamLevel = teamLevel;
            this.saveField("teamLevel", teamLevel);
        }
    }

    public long getChallengeTimes() {
        return this.challengeTimes;
    }

    public void setChallengeTimes(long challengeTimes) {
        if (challengeTimes != this.challengeTimes) {
            this.challengeTimes = challengeTimes;
        }
    }

    public void saveChallengeTimes(long challengeTimes) {
        if (challengeTimes != this.challengeTimes) {
            this.challengeTimes = challengeTimes;
            this.saveField("challengeTimes", challengeTimes);
        }
    }

    public int getInspiringTimesSize() {
        return this.inspiringTimes.size();
    }

    public List<Long> getInspiringTimesAll() {
        return new ArrayList<>(this.inspiringTimes);
    }

    public void setInspiringTimesAll(long value) {
        for (int i = 0; i < this.inspiringTimes.size(); i++) {
            this.inspiringTimes.set(i, value);
        }
    }

    public void saveInspiringTimesAll(long value) {
        this.setInspiringTimesAll(value);
        this.saveAll();
    }

    public long getInspiringTimes(int index) {
        return this.inspiringTimes.get(index);
    }

    public void setInspiringTimes(int index, long value) {
        if (value != this.inspiringTimes.get(index)) {
            this.inspiringTimes.set(index, value);
        }
    }

    public void saveInspiringTimes(int index, long value) {
        if (value != this.inspiringTimes.get(index)) {
            this.inspiringTimes.set(index, value);
            this.saveField("inspiringTimes_" + index, this.inspiringTimes.get(index));
        }
    }

    public int getTotalDamageSize() {
        return this.totalDamage.size();
    }

    public List<Long> getTotalDamageAll() {
        return new ArrayList<>(this.totalDamage);
    }

    public void setTotalDamageAll(long value) {
        for (int i = 0; i < this.totalDamage.size(); i++) {
            this.totalDamage.set(i, value);
        }
    }

    public void saveTotalDamageAll(long value) {
        this.setTotalDamageAll(value);
        this.saveAll();
    }

    public long getTotalDamage(int index) {
        return this.totalDamage.get(index);
    }

    public void setTotalDamage(int index, long value) {
        if (value != this.totalDamage.get(index)) {
            this.totalDamage.set(index, value);
        }
    }

    public void saveTotalDamage(int index, long value) {
        if (value != this.totalDamage.get(index)) {
            this.totalDamage.set(index, value);
            this.saveField("totalDamage_" + index, this.totalDamage.get(index));
        }
    }

    public int getDamageRankSize() {
        return this.damageRank.size();
    }

    public List<Long> getDamageRankAll() {
        return new ArrayList<>(this.damageRank);
    }

    public void setDamageRankAll(long value) {
        for (int i = 0; i < this.damageRank.size(); i++) {
            this.damageRank.set(i, value);
        }
    }

    public void saveDamageRankAll(long value) {
        this.setDamageRankAll(value);
        this.saveAll();
    }

    public long getDamageRank(int index) {
        return this.damageRank.get(index);
    }

    public void setDamageRank(int index, long value) {
        if (value != this.damageRank.get(index)) {
            this.damageRank.set(index, value);
        }
    }

    public void saveDamageRank(int index, long value) {
        if (value != this.damageRank.get(index)) {
            this.damageRank.set(index, value);
            this.saveField("damageRank_" + index, this.damageRank.get(index));
        }
    }

    public long getBeginFightTime() {
        return this.beginFightTime;
    }

    public void setBeginFightTime(long beginFightTime) {
        if (beginFightTime != this.beginFightTime) {
            this.beginFightTime = beginFightTime;
        }
    }

    public void saveBeginFightTime(long beginFightTime) {
        if (beginFightTime != this.beginFightTime) {
            this.beginFightTime = beginFightTime;
            this.saveField("beginFightTime", beginFightTime);
        }
    }

    public long getLeaveFightTime() {
        return this.leaveFightTime;
    }

    public void setLeaveFightTime(long leaveFightTime) {
        if (leaveFightTime != this.leaveFightTime) {
            this.leaveFightTime = leaveFightTime;
        }
    }

    public void saveLeaveFightTime(long leaveFightTime) {
        if (leaveFightTime != this.leaveFightTime) {
            this.leaveFightTime = leaveFightTime;
            this.saveField("leaveFightTime", leaveFightTime);
        }
    }

    public long getAttackTimes() {
        return this.attackTimes;
    }

    public void setAttackTimes(long attackTimes) {
        if (attackTimes != this.attackTimes) {
            this.attackTimes = attackTimes;
        }
    }

    public void saveAttackTimes(long attackTimes) {
        if (attackTimes != this.attackTimes) {
            this.attackTimes = attackTimes;
            this.saveField("attackTimes", attackTimes);
        }
    }

    public int getResurrection() {
        return this.resurrection;
    }

    public void setResurrection(int resurrection) {
        if (resurrection != this.resurrection) {
            this.resurrection = resurrection;
        }
    }

    public void saveResurrection(int resurrection) {
        if (resurrection != this.resurrection) {
            this.resurrection = resurrection;
            this.saveField("resurrection", resurrection);
        }
    }

    public boolean getAutoChallenge() {
        return this.autoChallenge;
    }

    public void setAutoChallenge(boolean autoChallenge) {
        if (autoChallenge != this.autoChallenge) {
            this.autoChallenge = autoChallenge;
        }
    }

    public void saveAutoChallenge(boolean autoChallenge) {
        if (autoChallenge != this.autoChallenge) {
            this.autoChallenge = autoChallenge;
            this.saveField("autoChallenge", autoChallenge ? 1 : 0);
        }
    }

    @Override
    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `teamLevel` = '").append(this.teamLevel).append("',");
        sBuilder.append(" `challengeTimes` = '").append(this.challengeTimes).append("',");

        for (int i = 0; i < this.inspiringTimes.size(); i++) {
            sBuilder.append(" `inspiringTimes_").append(i).append("` = '").append(this.inspiringTimes.get(i)).append("',");
        }

        for (int i = 0; i < this.totalDamage.size(); i++) {
            sBuilder.append(" `totalDamage_").append(i).append("` = '").append(this.totalDamage.get(i)).append("',");
        }

        for (int i = 0; i < this.damageRank.size(); i++) {
            sBuilder.append(" `damageRank_").append(i).append("` = '").append(this.damageRank.get(i)).append("',");
        }

        sBuilder.append(" `beginFightTime` = '").append(this.beginFightTime).append("',");
        sBuilder.append(" `leaveFightTime` = '").append(this.leaveFightTime).append("',");
        sBuilder.append(" `attackTimes` = '").append(this.attackTimes).append("',");
        sBuilder.append(" `resurrection` = '").append(this.resurrection).append("',");
        sBuilder.append(" `autoChallenge` = '").append(this.autoChallenge ? 1 : 0).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }

    public static String getSql_TableCreate() {
        return "CREATE TABLE IF NOT EXISTS `worldBossChallenge` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家pid',`teamLevel` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属玩家等级',`challengeTimes` bigint(20) NOT NULL DEFAULT '0' COMMENT '挑战次数',`inspiringTimes_0` bigint(20) NOT NULL DEFAULT '0' COMMENT '鼓舞次数',`inspiringTimes_1` bigint(20) NOT NULL DEFAULT '0' COMMENT '鼓舞次数',`inspiringTimes_2` bigint(20) NOT NULL DEFAULT '0' COMMENT '鼓舞次数',`inspiringTimes_3` bigint(20) NOT NULL DEFAULT '0' COMMENT '鼓舞次数',`inspiringTimes_4` bigint(20) NOT NULL DEFAULT '0' COMMENT '鼓舞次数',`inspiringTimes_5` bigint(20) NOT NULL DEFAULT '0' COMMENT '鼓舞次数',`inspiringTimes_6` bigint(20) NOT NULL DEFAULT '0' COMMENT '鼓舞次数',`inspiringTimes_7` bigint(20) NOT NULL DEFAULT '0' COMMENT '鼓舞次数',`inspiringTimes_8` bigint(20) NOT NULL DEFAULT '0' COMMENT '鼓舞次数',`inspiringTimes_9` bigint(20) NOT NULL DEFAULT '0' COMMENT '鼓舞次数',`totalDamage_0` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_1` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_2` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_3` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_4` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_5` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_6` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_7` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_8` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`totalDamage_9` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_0` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_1` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_2` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_3` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_4` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_5` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_6` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_7` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_8` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`damageRank_9` bigint(20) NOT NULL DEFAULT '0' COMMENT 'BOSS伤害',`beginFightTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家开始挑战boss时间,用于计算玩家战斗时间',`leaveFightTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家退出boss时间,用于计算玩家挑战CD时间',`attackTimes` bigint(20) NOT NULL DEFAULT '0' COMMENT '今日攻击次数',`resurrection` int(11) NOT NULL DEFAULT '0' COMMENT '复活次数',`autoChallenge` tinyint(1) NOT NULL DEFAULT '0' COMMENT '定时自动挑战世界BOSS',PRIMARY KEY (`id`)) COMMENT='玩家挑战世界Boss信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT="
            + (ServerConfig.getInitialID() + 1L);
    }
}
