package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GuildwarGuildResultBO extends BaseBO {
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "id",
        comment = "数据库表唯一键值"
    )
    private long id;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "atkGuildId",
        comment = "公会ID"
    )
    private long atkGuildId;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "defGuildId",
        comment = "公会ID"
    )
    private long defGuildId;
    @DataBaseField(
        type = "int(11)",
        fieldname = "result",
        comment = "结果"
    )
    private int result;
    @DataBaseField(
        type = "int(11)",
        fieldname = "centerId",
        comment = "据点id"
    )
    private int centerId;
    @DataBaseField(
        type = "int(11)",
        fieldname = "fightTime",
        comment = "战斗时间"
    )
    private int fightTime;

    public GuildwarGuildResultBO() {
        this.id = 0L;
        this.atkGuildId = 0L;
        this.defGuildId = 0L;
        this.result = 0;
        this.centerId = 0;
        this.fightTime = 0;
    }

    public GuildwarGuildResultBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.atkGuildId = rs.getLong(2);
        this.defGuildId = rs.getLong(3);
        this.result = rs.getInt(4);
        this.centerId = rs.getInt(5);
        this.fightTime = rs.getInt(6);
    }

    @Override
    public void getFromResultSet(ResultSet rs, List list) throws Exception {
        list.add(new GuildwarGuildResultBO(rs));
    }

    @Override
    public long getAsynTaskTag() {
        return 0L;
    }

    @Override
    public String getItemsName() {
        return "`id`, `atkGuildId`, `defGuildId`, `result`, `centerId`, `fightTime`";
    }

    @Override
    public String getTableName() {
        return "`guildwarGuildResult`";
    }

    @Override
    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.atkGuildId).append("', ");
        strBuf.append("'").append(this.defGuildId).append("', ");
        strBuf.append("'").append(this.result).append("', ");
        strBuf.append("'").append(this.centerId).append("', ");
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

    public long getAtkGuildId() {
        return this.atkGuildId;
    }

    public void setAtkGuildId(long atkGuildId) {
        if (atkGuildId != this.atkGuildId) {
            this.atkGuildId = atkGuildId;
        }
    }

    public void saveAtkGuildId(long atkGuildId) {
        if (atkGuildId != this.atkGuildId) {
            this.atkGuildId = atkGuildId;
            this.saveField("atkGuildId", atkGuildId);
        }
    }

    public long getDefGuildId() {
        return this.defGuildId;
    }

    public void setDefGuildId(long defGuildId) {
        if (defGuildId != this.defGuildId) {
            this.defGuildId = defGuildId;
        }
    }

    public void saveDefGuildId(long defGuildId) {
        if (defGuildId != this.defGuildId) {
            this.defGuildId = defGuildId;
            this.saveField("defGuildId", defGuildId);
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

    public int getCenterId() {
        return this.centerId;
    }

    public void setCenterId(int centerId) {
        if (centerId != this.centerId) {
            this.centerId = centerId;
        }
    }

    public void saveCenterId(int centerId) {
        if (centerId != this.centerId) {
            this.centerId = centerId;
            this.saveField("centerId", centerId);
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
        sBuilder.append(" `atkGuildId` = '").append(this.atkGuildId).append("',");
        sBuilder.append(" `defGuildId` = '").append(this.defGuildId).append("',");
        sBuilder.append(" `result` = '").append(this.result).append("',");
        sBuilder.append(" `centerId` = '").append(this.centerId).append("',");
        sBuilder.append(" `fightTime` = '").append(this.fightTime).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }

    public static String getSql_TableCreate() {
        return "CREATE TABLE IF NOT EXISTS `guildwarGuildResult` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`atkGuildId` bigint(20) NOT NULL DEFAULT '0' COMMENT '公会ID',`defGuildId` bigint(20) NOT NULL DEFAULT '0' COMMENT '公会ID',`result` int(11) NOT NULL DEFAULT '0' COMMENT '结果',`centerId` int(11) NOT NULL DEFAULT '0' COMMENT '据点id',`fightTime` int(11) NOT NULL DEFAULT '0' COMMENT '战斗时间',PRIMARY KEY (`id`)) COMMENT='公会战'  DEFAULT CHARSET=utf8 AUTO_INCREMENT="
            + (ServerConfig.getInitialID() + 1L);
    }
}
