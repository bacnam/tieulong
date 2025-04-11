package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GuildwarapplyBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "guildId", comment = "公会ID")
    private long guildId;
    @DataBaseField(type = "int(11)", fieldname = "centerId", comment = "据点id")
    private int centerId;
    @DataBaseField(type = "int(11)", fieldname = "applyTime", comment = "申请时间")
    private int applyTime;
    @DataBaseField(type = "int(11)", fieldname = "winCenterId", comment = "占领据点Id")
    private int winCenterId;

    public GuildwarapplyBO() {
        this.id = 0L;
        this.guildId = 0L;
        this.centerId = 0;
        this.applyTime = 0;
        this.winCenterId = 0;
    }

    public GuildwarapplyBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.guildId = rs.getLong(2);
        this.centerId = rs.getInt(3);
        this.applyTime = rs.getInt(4);
        this.winCenterId = rs.getInt(5);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `guildwarapply` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`guildId` bigint(20) NOT NULL DEFAULT '0' COMMENT '公会ID',`centerId` int(11) NOT NULL DEFAULT '0' COMMENT '据点id',`applyTime` int(11) NOT NULL DEFAULT '0' COMMENT '申请时间',`winCenterId` int(11) NOT NULL DEFAULT '0' COMMENT '占领据点Id',KEY `guildId` (`guildId`),PRIMARY KEY (`id`)) COMMENT='公会报名信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<GuildwarapplyBO> list) throws Exception {
        list.add(new GuildwarapplyBO(rs));
    }

    public long getAsynTaskTag() {
        return getGuildId();
    }

    public String getItemsName() {
        return "`id`, `guildId`, `centerId`, `applyTime`, `winCenterId`";
    }

    public String getTableName() {
        return "`guildwarapply`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.guildId).append("', ");
        strBuf.append("'").append(this.centerId).append("', ");
        strBuf.append("'").append(this.applyTime).append("', ");
        strBuf.append("'").append(this.winCenterId).append("', ");
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

    public int getCenterId() {
        return this.centerId;
    }

    public void setCenterId(int centerId) {
        if (centerId == this.centerId)
            return;
        this.centerId = centerId;
    }

    public void saveCenterId(int centerId) {
        if (centerId == this.centerId)
            return;
        this.centerId = centerId;
        saveField("centerId", Integer.valueOf(centerId));
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

    public int getWinCenterId() {
        return this.winCenterId;
    }

    public void setWinCenterId(int winCenterId) {
        if (winCenterId == this.winCenterId)
            return;
        this.winCenterId = winCenterId;
    }

    public void saveWinCenterId(int winCenterId) {
        if (winCenterId == this.winCenterId)
            return;
        this.winCenterId = winCenterId;
        saveField("winCenterId", Integer.valueOf(winCenterId));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `guildId` = '").append(this.guildId).append("',");
        sBuilder.append(" `centerId` = '").append(this.centerId).append("',");
        sBuilder.append(" `applyTime` = '").append(this.applyTime).append("',");
        sBuilder.append(" `winCenterId` = '").append(this.winCenterId).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

