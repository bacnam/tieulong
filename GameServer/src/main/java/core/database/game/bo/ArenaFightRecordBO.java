package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ArenaFightRecordBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "atkPid", comment = "挑战者玩家ID")
    private long atkPid;
    @DataBaseField(type = "bigint(20)", fieldname = "defPid", comment = "防御者玩家ID")
    private long defPid;
    @DataBaseField(type = "int(11)", fieldname = "atkRank", comment = "挑战者玩家排名")
    private int atkRank;
    @DataBaseField(type = "int(11)", fieldname = "defRank", comment = "防御者玩家排名")
    private int defRank;
    @DataBaseField(type = "int(11)", fieldname = "beginTime", comment = "战斗开始时间")
    private int beginTime;
    @DataBaseField(type = "int(11)", fieldname = "endTime", comment = "战斗结束时间")
    private int endTime;
    @DataBaseField(type = "int(11)", fieldname = "result", comment = "挑战结果,FightResult")
    private int result;

    public ArenaFightRecordBO() {
        this.id = 0L;
        this.atkPid = 0L;
        this.defPid = 0L;
        this.atkRank = 0;
        this.defRank = 0;
        this.beginTime = 0;
        this.endTime = 0;
        this.result = 0;
    }

    public ArenaFightRecordBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.atkPid = rs.getLong(2);
        this.defPid = rs.getLong(3);
        this.atkRank = rs.getInt(4);
        this.defRank = rs.getInt(5);
        this.beginTime = rs.getInt(6);
        this.endTime = rs.getInt(7);
        this.result = rs.getInt(8);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `arena_fight_record` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`atkPid` bigint(20) NOT NULL DEFAULT '0' COMMENT '挑战者玩家ID',`defPid` bigint(20) NOT NULL DEFAULT '0' COMMENT '防御者玩家ID',`atkRank` int(11) NOT NULL DEFAULT '0' COMMENT '挑战者玩家排名',`defRank` int(11) NOT NULL DEFAULT '0' COMMENT '防御者玩家排名',`beginTime` int(11) NOT NULL DEFAULT '0' COMMENT '战斗开始时间',`endTime` int(11) NOT NULL DEFAULT '0' COMMENT '战斗结束时间',`result` int(11) NOT NULL DEFAULT '0' COMMENT '挑战结果,FightResult',KEY `atkPid` (`atkPid`),KEY `defPid` (`defPid`),PRIMARY KEY (`id`)) COMMENT='战报记录表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<ArenaFightRecordBO> list) throws Exception {
        list.add(new ArenaFightRecordBO(rs));
    }

    public long getAsynTaskTag() {
        return getId();
    }

    public String getItemsName() {
        return "`id`, `atkPid`, `defPid`, `atkRank`, `defRank`, `beginTime`, `endTime`, `result`";
    }

    public String getTableName() {
        return "`arena_fight_record`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.atkPid).append("', ");
        strBuf.append("'").append(this.defPid).append("', ");
        strBuf.append("'").append(this.atkRank).append("', ");
        strBuf.append("'").append(this.defRank).append("', ");
        strBuf.append("'").append(this.beginTime).append("', ");
        strBuf.append("'").append(this.endTime).append("', ");
        strBuf.append("'").append(this.result).append("', ");
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

    public long getAtkPid() {
        return this.atkPid;
    }

    public void setAtkPid(long atkPid) {
        if (atkPid == this.atkPid)
            return;
        this.atkPid = atkPid;
    }

    public void saveAtkPid(long atkPid) {
        if (atkPid == this.atkPid)
            return;
        this.atkPid = atkPid;
        saveField("atkPid", Long.valueOf(atkPid));
    }

    public long getDefPid() {
        return this.defPid;
    }

    public void setDefPid(long defPid) {
        if (defPid == this.defPid)
            return;
        this.defPid = defPid;
    }

    public void saveDefPid(long defPid) {
        if (defPid == this.defPid)
            return;
        this.defPid = defPid;
        saveField("defPid", Long.valueOf(defPid));
    }

    public int getAtkRank() {
        return this.atkRank;
    }

    public void setAtkRank(int atkRank) {
        if (atkRank == this.atkRank)
            return;
        this.atkRank = atkRank;
    }

    public void saveAtkRank(int atkRank) {
        if (atkRank == this.atkRank)
            return;
        this.atkRank = atkRank;
        saveField("atkRank", Integer.valueOf(atkRank));
    }

    public int getDefRank() {
        return this.defRank;
    }

    public void setDefRank(int defRank) {
        if (defRank == this.defRank)
            return;
        this.defRank = defRank;
    }

    public void saveDefRank(int defRank) {
        if (defRank == this.defRank)
            return;
        this.defRank = defRank;
        saveField("defRank", Integer.valueOf(defRank));
    }

    public int getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(int beginTime) {
        if (beginTime == this.beginTime)
            return;
        this.beginTime = beginTime;
    }

    public void saveBeginTime(int beginTime) {
        if (beginTime == this.beginTime)
            return;
        this.beginTime = beginTime;
        saveField("beginTime", Integer.valueOf(beginTime));
    }

    public int getEndTime() {
        return this.endTime;
    }

    public void setEndTime(int endTime) {
        if (endTime == this.endTime)
            return;
        this.endTime = endTime;
    }

    public void saveEndTime(int endTime) {
        if (endTime == this.endTime)
            return;
        this.endTime = endTime;
        saveField("endTime", Integer.valueOf(endTime));
    }

    public int getResult() {
        return this.result;
    }

    public void setResult(int result) {
        if (result == this.result)
            return;
        this.result = result;
    }

    public void saveResult(int result) {
        if (result == this.result)
            return;
        this.result = result;
        saveField("result", Integer.valueOf(result));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `atkPid` = '").append(this.atkPid).append("',");
        sBuilder.append(" `defPid` = '").append(this.defPid).append("',");
        sBuilder.append(" `atkRank` = '").append(this.atkRank).append("',");
        sBuilder.append(" `defRank` = '").append(this.defRank).append("',");
        sBuilder.append(" `beginTime` = '").append(this.beginTime).append("',");
        sBuilder.append(" `endTime` = '").append(this.endTime).append("',");
        sBuilder.append(" `result` = '").append(this.result).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

