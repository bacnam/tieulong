package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ArenaCompetitorBO extends BaseBO {
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "id",
        comment = "数据库表唯一键值"
    )
    private long id;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "pid",
        comment = "所属玩家ID"
    )
    private long pid;
    @DataBaseField(
        type = "int(11)",
        fieldname = "rank",
        comment = "排名"
    )
    private int rank;
    @DataBaseField(
        type = "int(11)",
        fieldname = "lastRankTime",
        comment = "最近一次排名变更时间"
    )
    private int lastRankTime;
    @DataBaseField(
        type = "int(11)",
        fieldname = "lastFightTime",
        comment = "上次战斗事件,单位秒,用于冷却"
    )
    private int lastFightTime;
    @DataBaseField(
        type = "int(11)",
        fieldname = "highestRank",
        comment = "历史最高排名"
    )
    private int highestRank;
    @DataBaseField(
        type = "int(11)",
        fieldname = "winning",
        comment = "连胜"
    )
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

    @Override
    public void getFromResultSet(ResultSet rs, List list) throws Exception {
        list.add(new ArenaCompetitorBO(rs));
    }

    @Override
    public long getAsynTaskTag() {
        return this.getPid();
    }

    @Override
    public String getItemsName() {
        return "`id`, `pid`, `rank`, `lastRankTime`, `lastFightTime`, `highestRank`, `winning`";
    }

    @Override
    public String getTableName() {
        return "`arena_competitor`";
    }

    @Override
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

    public int getRank() {
        return this.rank;
    }

    public void setRank(int rank) {
        if (rank != this.rank) {
            this.rank = rank;
        }
    }

    public void saveRank(int rank) {
        if (rank != this.rank) {
            this.rank = rank;
            this.saveField("rank", rank);
        }
    }

    public int getLastRankTime() {
        return this.lastRankTime;
    }

    public void setLastRankTime(int lastRankTime) {
        if (lastRankTime != this.lastRankTime) {
            this.lastRankTime = lastRankTime;
        }
    }

    public void saveLastRankTime(int lastRankTime) {
        if (lastRankTime != this.lastRankTime) {
            this.lastRankTime = lastRankTime;
            this.saveField("lastRankTime", lastRankTime);
        }
    }

    public int getLastFightTime() {
        return this.lastFightTime;
    }

    public void setLastFightTime(int lastFightTime) {
        if (lastFightTime != this.lastFightTime) {
            this.lastFightTime = lastFightTime;
        }
    }

    public void saveLastFightTime(int lastFightTime) {
        if (lastFightTime != this.lastFightTime) {
            this.lastFightTime = lastFightTime;
            this.saveField("lastFightTime", lastFightTime);
        }
    }

    public int getHighestRank() {
        return this.highestRank;
    }

    public void setHighestRank(int highestRank) {
        if (highestRank != this.highestRank) {
            this.highestRank = highestRank;
        }
    }

    public void saveHighestRank(int highestRank) {
        if (highestRank != this.highestRank) {
            this.highestRank = highestRank;
            this.saveField("highestRank", highestRank);
        }
    }

    public int getWinning() {
        return this.winning;
    }

    public void setWinning(int winning) {
        if (winning != this.winning) {
            this.winning = winning;
        }
    }

    public void saveWinning(int winning) {
        if (winning != this.winning) {
            this.winning = winning;
            this.saveField("winning", winning);
        }
    }

    @Override
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

    public static String getSql_TableCreate() {
        return "CREATE TABLE IF NOT EXISTS `arena_competitor` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属玩家ID',`rank` int(11) NOT NULL DEFAULT '0' COMMENT '排名',`lastRankTime` int(11) NOT NULL DEFAULT '0' COMMENT '最近一次排名变更时间',`lastFightTime` int(11) NOT NULL DEFAULT '0' COMMENT '上次战斗事件,单位秒,用于冷却',`highestRank` int(11) NOT NULL DEFAULT '0' COMMENT '历史最高排名',`winning` int(11) NOT NULL DEFAULT '0' COMMENT '连胜',PRIMARY KEY (`id`)) COMMENT='玩家信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT="
            + (ServerConfig.getInitialID() + 1L);
    }
}
