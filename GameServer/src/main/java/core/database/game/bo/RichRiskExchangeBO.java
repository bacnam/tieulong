package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RichRiskExchangeBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家pid")
    private long pid;
    @DataBaseField(type = "int(11)", fieldname = "x", comment = "坐标的X")
    private int x;
    @DataBaseField(type = "int(11)", fieldname = "y", comment = "坐标的Y")
    private int y;
    @DataBaseField(type = "int(11)", fieldname = "exchangeId", comment = "兑换Id")
    private int exchangeId;
    @DataBaseField(type = "int(11)", fieldname = "conditionId", comment = "条件物品Id")
    private int conditionId;
    @DataBaseField(type = "int(11)", fieldname = "conditionAmount", comment = "条件物品数量")
    private int conditionAmount;
    @DataBaseField(type = "int(11)", fieldname = "goalId", comment = "兑换物Id")
    private int goalId;
    @DataBaseField(type = "int(11)", fieldname = "goalAmount", comment = "兑换物数量")
    private int goalAmount;
    @DataBaseField(type = "int(11)", fieldname = "score", comment = "获得积分")
    private int score;
    @DataBaseField(type = "int(11)", fieldname = "timesLimit", comment = "次数限制")
    private int timesLimit;

    public RichRiskExchangeBO() {
        this.id = 0L;
        this.pid = 0L;
        this.x = 0;
        this.y = 0;
        this.exchangeId = 0;
        this.conditionId = 0;
        this.conditionAmount = 0;
        this.goalId = 0;
        this.goalAmount = 0;
        this.score = 0;
        this.timesLimit = 0;
    }

    public RichRiskExchangeBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.x = rs.getInt(3);
        this.y = rs.getInt(4);
        this.exchangeId = rs.getInt(5);
        this.conditionId = rs.getInt(6);
        this.conditionAmount = rs.getInt(7);
        this.goalId = rs.getInt(8);
        this.goalAmount = rs.getInt(9);
        this.score = rs.getInt(10);
        this.timesLimit = rs.getInt(11);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `richRiskExchange` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家pid',`x` int(11) NOT NULL DEFAULT '0' COMMENT '坐标的X',`y` int(11) NOT NULL DEFAULT '0' COMMENT '坐标的Y',`exchangeId` int(11) NOT NULL DEFAULT '0' COMMENT '兑换Id',`conditionId` int(11) NOT NULL DEFAULT '0' COMMENT '条件物品Id',`conditionAmount` int(11) NOT NULL DEFAULT '0' COMMENT '条件物品数量',`goalId` int(11) NOT NULL DEFAULT '0' COMMENT '兑换物Id',`goalAmount` int(11) NOT NULL DEFAULT '0' COMMENT '兑换物数量',`score` int(11) NOT NULL DEFAULT '0' COMMENT '获得积分',`timesLimit` int(11) NOT NULL DEFAULT '0' COMMENT '次数限制',PRIMARY KEY (`id`)) COMMENT='大富翁商品兑换表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<RichRiskExchangeBO> list) throws Exception {
        list.add(new RichRiskExchangeBO(rs));
    }

    public long getAsynTaskTag() {
        return 0L;
    }

    public String getItemsName() {
        return "`id`, `pid`, `x`, `y`, `exchangeId`, `conditionId`, `conditionAmount`, `goalId`, `goalAmount`, `score`, `timesLimit`";
    }

    public String getTableName() {
        return "`richRiskExchange`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.x).append("', ");
        strBuf.append("'").append(this.y).append("', ");
        strBuf.append("'").append(this.exchangeId).append("', ");
        strBuf.append("'").append(this.conditionId).append("', ");
        strBuf.append("'").append(this.conditionAmount).append("', ");
        strBuf.append("'").append(this.goalId).append("', ");
        strBuf.append("'").append(this.goalAmount).append("', ");
        strBuf.append("'").append(this.score).append("', ");
        strBuf.append("'").append(this.timesLimit).append("', ");
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

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        if (x == this.x)
            return;
        this.x = x;
    }

    public void saveX(int x) {
        if (x == this.x)
            return;
        this.x = x;
        saveField("x", Integer.valueOf(x));
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        if (y == this.y)
            return;
        this.y = y;
    }

    public void saveY(int y) {
        if (y == this.y)
            return;
        this.y = y;
        saveField("y", Integer.valueOf(y));
    }

    public int getExchangeId() {
        return this.exchangeId;
    }

    public void setExchangeId(int exchangeId) {
        if (exchangeId == this.exchangeId)
            return;
        this.exchangeId = exchangeId;
    }

    public void saveExchangeId(int exchangeId) {
        if (exchangeId == this.exchangeId)
            return;
        this.exchangeId = exchangeId;
        saveField("exchangeId", Integer.valueOf(exchangeId));
    }

    public int getConditionId() {
        return this.conditionId;
    }

    public void setConditionId(int conditionId) {
        if (conditionId == this.conditionId)
            return;
        this.conditionId = conditionId;
    }

    public void saveConditionId(int conditionId) {
        if (conditionId == this.conditionId)
            return;
        this.conditionId = conditionId;
        saveField("conditionId", Integer.valueOf(conditionId));
    }

    public int getConditionAmount() {
        return this.conditionAmount;
    }

    public void setConditionAmount(int conditionAmount) {
        if (conditionAmount == this.conditionAmount)
            return;
        this.conditionAmount = conditionAmount;
    }

    public void saveConditionAmount(int conditionAmount) {
        if (conditionAmount == this.conditionAmount)
            return;
        this.conditionAmount = conditionAmount;
        saveField("conditionAmount", Integer.valueOf(conditionAmount));
    }

    public int getGoalId() {
        return this.goalId;
    }

    public void setGoalId(int goalId) {
        if (goalId == this.goalId)
            return;
        this.goalId = goalId;
    }

    public void saveGoalId(int goalId) {
        if (goalId == this.goalId)
            return;
        this.goalId = goalId;
        saveField("goalId", Integer.valueOf(goalId));
    }

    public int getGoalAmount() {
        return this.goalAmount;
    }

    public void setGoalAmount(int goalAmount) {
        if (goalAmount == this.goalAmount)
            return;
        this.goalAmount = goalAmount;
    }

    public void saveGoalAmount(int goalAmount) {
        if (goalAmount == this.goalAmount)
            return;
        this.goalAmount = goalAmount;
        saveField("goalAmount", Integer.valueOf(goalAmount));
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        if (score == this.score)
            return;
        this.score = score;
    }

    public void saveScore(int score) {
        if (score == this.score)
            return;
        this.score = score;
        saveField("score", Integer.valueOf(score));
    }

    public int getTimesLimit() {
        return this.timesLimit;
    }

    public void setTimesLimit(int timesLimit) {
        if (timesLimit == this.timesLimit)
            return;
        this.timesLimit = timesLimit;
    }

    public void saveTimesLimit(int timesLimit) {
        if (timesLimit == this.timesLimit)
            return;
        this.timesLimit = timesLimit;
        saveField("timesLimit", Integer.valueOf(timesLimit));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `x` = '").append(this.x).append("',");
        sBuilder.append(" `y` = '").append(this.y).append("',");
        sBuilder.append(" `exchangeId` = '").append(this.exchangeId).append("',");
        sBuilder.append(" `conditionId` = '").append(this.conditionId).append("',");
        sBuilder.append(" `conditionAmount` = '").append(this.conditionAmount).append("',");
        sBuilder.append(" `goalId` = '").append(this.goalId).append("',");
        sBuilder.append(" `goalAmount` = '").append(this.goalAmount).append("',");
        sBuilder.append(" `score` = '").append(this.score).append("',");
        sBuilder.append(" `timesLimit` = '").append(this.timesLimit).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

