package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ArenaCompetitorBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "所属玩家ID")
    private long pid;
    @DataBaseField(type = "int(11)", fieldname = "rank", comment = "排名")
    private int rank;
    @DataBaseField(type = "int(11)", fieldname = "lastRankTime", comment = "最近一次排名变更时间")
    private int lastRankTime;
    @DataBaseField(type = "int(11)", fieldname = "lastFightTime", comment = "上次战斗事件,单位秒,用于冷却")
    private int lastFightTime;
    @DataBaseField(type = "int(11)", fieldname = "highestRank", comment = "历史最高排名")
    private int highestRank;
    @DataBaseField(type = "int(11)", fieldname = "winning", comment = "连胜")
    private int winning;

    public ArenaCompetitorBO() {
        this.id = 0L;
        this.pid = 0L;
        this.rank = 0;
        this.lastRankTime = 0;
        this.lastFightTime = 0;
        this.highestRank = 0;
        this.winning = 0;
    }

    public ArenaCompetitorBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.rank = rs.getInt(3);
        this.lastRankTime = rs.getInt(4);
        this.lastFightTime = rs.getInt(5);
        this.highestRank = rs.getInt(6);
        this.winning = rs.getInt(7);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `arena_competitor` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属玩家ID',`rank` int(11) NOT NULL DEFAULT '0' COMMENT '排名',`lastRankTime` int(11) NOT NULL DEFAULT '0' COMMENT '最近一次排名变更时间',`lastFightTime` int(11) NOT NULL DEFAULT '0' COMMENT '上次战斗事件,单位秒,用于冷却',`highestRank` int(11) NOT NULL DEFAULT '0' COMMENT '历史最高排名',`winning` int(11) NOT NULL DEFAULT '0' COMMENT '连胜',PRIMARY KEY (`id`)) COMMENT='玩家信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<ArenaCompetitorBO> list) throws Exception {
        list.add(new ArenaCompetitorBO(rs));
    }

    public long getAsynTaskTag() {
        return getPid();
    }

    public String getItemsName() {
        return "`id`, `pid`, `rank`, `lastRankTime`, `lastFightTime`, `highestRank`, `winning`";
    }

    public String getTableName() {
        return "`arena_competitor`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.rank).append("', ");
        strBuf.append("'").append(this.lastRankTime).append("', ");
        strBuf.append("'").append(this.lastFightTime).append("', ");
        strBuf.append("'").append(this.highestRank).append("', ");
        strBuf.append("'").append(this.winning).append("', ");
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

    public int getRank() {
        return this.rank;
    }

    public void setRank(int rank) {
        if (rank == this.rank)
            return;
        this.rank = rank;
    }

    public void saveRank(int rank) {
        if (rank == this.rank)
            return;
        this.rank = rank;
        saveField("rank", Integer.valueOf(rank));
    }

    public int getLastRankTime() {
        return this.lastRankTime;
    }

    public void setLastRankTime(int lastRankTime) {
        if (lastRankTime == this.lastRankTime)
            return;
        this.lastRankTime = lastRankTime;
    }

    public void saveLastRankTime(int lastRankTime) {
        if (lastRankTime == this.lastRankTime)
            return;
        this.lastRankTime = lastRankTime;
        saveField("lastRankTime", Integer.valueOf(lastRankTime));
    }

    public int getLastFightTime() {
        return this.lastFightTime;
    }

    public void setLastFightTime(int lastFightTime) {
        if (lastFightTime == this.lastFightTime)
            return;
        this.lastFightTime = lastFightTime;
    }

    public void saveLastFightTime(int lastFightTime) {
        if (lastFightTime == this.lastFightTime)
            return;
        this.lastFightTime = lastFightTime;
        saveField("lastFightTime", Integer.valueOf(lastFightTime));
    }

    public int getHighestRank() {
        return this.highestRank;
    }

    public void setHighestRank(int highestRank) {
        if (highestRank == this.highestRank)
            return;
        this.highestRank = highestRank;
    }

    public void saveHighestRank(int highestRank) {
        if (highestRank == this.highestRank)
            return;
        this.highestRank = highestRank;
        saveField("highestRank", Integer.valueOf(highestRank));
    }

    public int getWinning() {
        return this.winning;
    }

    public void setWinning(int winning) {
        if (winning == this.winning)
            return;
        this.winning = winning;
    }

    public void saveWinning(int winning) {
        if (winning == this.winning)
            return;
        this.winning = winning;
        saveField("winning", Integer.valueOf(winning));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `rank` = '").append(this.rank).append("',");
        sBuilder.append(" `lastRankTime` = '").append(this.lastRankTime).append("',");
        sBuilder.append(" `lastFightTime` = '").append(this.lastFightTime).append("',");
        sBuilder.append(" `highestRank` = '").append(this.highestRank).append("',");
        sBuilder.append(" `winning` = '").append(this.winning).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

