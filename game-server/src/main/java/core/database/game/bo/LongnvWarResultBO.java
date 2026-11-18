package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LongnvWarResultBO extends BaseBO {
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "id",
        comment = "数据库表唯一键值"
    )
    private long id;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "guildId",
        comment = "公会ID"
    )
    private long guildId;
    @DataBaseField(
        type = "int(11)",
        fieldname = "level",
        comment = "龙女等级"
    )
    private int level;
    @DataBaseField(
        type = "int(11)",
        fieldname = "result",
        comment = "结果"
    )
    private int result;
    @DataBaseField(
        type = "int(11)",
        fieldname = "challengeTime",
        comment = "宣战时间"
    )
    private int challengeTime;
    @DataBaseField(
        type = "int(11)",
        fieldname = "fightTime",
        comment = "战斗时间"
    )
    private int fightTime;

    public LongnvWarResultBO() {
        this.id = 0L;
        this.guildId = 0L;
        this.level = 0;
        this.result = 0;
        this.challengeTime = 0;
        this.fightTime = 0;
    }

    public LongnvWarResultBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.guildId = rs.getLong(2);
        this.level = rs.getInt(3);
        this.result = rs.getInt(4);
        this.challengeTime = rs.getInt(5);
        this.fightTime = rs.getInt(6);
    }

    @Override
    public void getFromResultSet(ResultSet rs, List list) throws Exception {
        list.add(new LongnvWarResultBO(rs));
    }

    @Override
    public long getAsynTaskTag() {
        return 0L;
    }

    @Override
    public String getItemsName() {
        return "`id`, `guildId`, `level`, `result`, `challengeTime`, `fightTime`";
    }

    @Override
    public String getTableName() {
        return "`longnvWarResult`";
    }

    @Override
    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.guildId).append("', ");
        strBuf.append("'").append(this.level).append("', ");
        strBuf.append("'").append(this.result).append("', ");
        strBuf.append("'").append(this.challengeTime).append("', ");
        strBuf.append("'").append(this.fightTime).append("', ");
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

    public long getGuildId() {
        return this.guildId;
    }

    public void setGuildId(long guildId) {
        if (guildId != this.guildId) {
            this.guildId = guildId;
        }
    }

    public void saveGuildId(long guildId) {
        if (guildId != this.guildId) {
            this.guildId = guildId;
            this.saveField("guildId", guildId);
        }
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        if (level != this.level) {
            this.level = level;
        }
    }

    public void saveLevel(int level) {
        if (level != this.level) {
            this.level = level;
            this.saveField("level", level);
        }
    }

    public int getResult() {
        return this.result;
    }

    public void setResult(int result) {
        if (result != this.result) {
            this.result = result;
        }
    }

    public void saveResult(int result) {
        if (result != this.result) {
            this.result = result;
            this.saveField("result", result);
        }
    }

    public int getChallengeTime() {
        return this.challengeTime;
    }

    public void setChallengeTime(int challengeTime) {
        if (challengeTime != this.challengeTime) {
            this.challengeTime = challengeTime;
        }
    }

    public void saveChallengeTime(int challengeTime) {
        if (challengeTime != this.challengeTime) {
            this.challengeTime = challengeTime;
            this.saveField("challengeTime", challengeTime);
        }
    }

    public int getFightTime() {
        return this.fightTime;
    }

    public void setFightTime(int fightTime) {
        if (fightTime != this.fightTime) {
            this.fightTime = fightTime;
        }
    }

    public void saveFightTime(int fightTime) {
        if (fightTime != this.fightTime) {
            this.fightTime = fightTime;
            this.saveField("fightTime", fightTime);
        }
    }

    @Override
    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `guildId` = '").append(this.guildId).append("',");
        sBuilder.append(" `level` = '").append(this.level).append("',");
        sBuilder.append(" `result` = '").append(this.result).append("',");
        sBuilder.append(" `challengeTime` = '").append(this.challengeTime).append("',");
        sBuilder.append(" `fightTime` = '").append(this.fightTime).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }

    public static String getSql_TableCreate() {
        return "CREATE TABLE IF NOT EXISTS `longnvWarResult` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`guildId` bigint(20) NOT NULL DEFAULT '0' COMMENT '公会ID',`level` int(11) NOT NULL DEFAULT '0' COMMENT '龙女等级',`result` int(11) NOT NULL DEFAULT '0' COMMENT '结果',`challengeTime` int(11) NOT NULL DEFAULT '0' COMMENT '宣战时间',`fightTime` int(11) NOT NULL DEFAULT '0' COMMENT '战斗时间',PRIMARY KEY (`id`)) COMMENT='公会战'  DEFAULT CHARSET=utf8 AUTO_INCREMENT="
            + (ServerConfig.getInitialID() + 1L);
    }
}
