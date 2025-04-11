package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DroiyanRecordBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
    private long pid;
    @DataBaseField(type = "bigint(20)", fieldname = "target", comment = "被攻击玩家")
    private long target;
    @DataBaseField(type = "varchar(50)", fieldname = "target_name", comment = "被攻击玩家名称")
    private String target_name;
    @DataBaseField(type = "int(11)", fieldname = "result", comment = "挑战结果")
    private int result;
    @DataBaseField(type = "int(11)", fieldname = "time", comment = "发生时间")
    private int time;
    @DataBaseField(type = "int(11)", fieldname = "point", comment = "获得积分")
    private int point;
    @DataBaseField(type = "int(11)", fieldname = "gold", comment = "获得金币")
    private int gold;
    @DataBaseField(type = "int(11)", fieldname = "exp", comment = "获得经验")
    private int exp;
    @DataBaseField(type = "int(11)", fieldname = "rob", comment = "抢劫获得物品")
    private int rob;
    @DataBaseField(type = "int(11)", fieldname = "treasure", comment = "寻宝获得物品")
    private int treasure;
    @DataBaseField(type = "tinyint(1)", fieldname = "revenged", comment = "已经复仇")
    private boolean revenged;

    public DroiyanRecordBO() {
        this.id = 0L;
        this.pid = 0L;
        this.target = 0L;
        this.target_name = "";
        this.result = 0;
        this.time = 0;
        this.point = 0;
        this.gold = 0;
        this.exp = 0;
        this.rob = 0;
        this.treasure = 0;
        this.revenged = false;
    }

    public DroiyanRecordBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.target = rs.getLong(3);
        this.target_name = rs.getString(4);
        this.result = rs.getInt(5);
        this.time = rs.getInt(6);
        this.point = rs.getInt(7);
        this.gold = rs.getInt(8);
        this.exp = rs.getInt(9);
        this.rob = rs.getInt(10);
        this.treasure = rs.getInt(11);
        this.revenged = rs.getBoolean(12);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `droiyan_record` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`target` bigint(20) NOT NULL DEFAULT '0' COMMENT '被攻击玩家',`target_name` varchar(50) NOT NULL DEFAULT '' COMMENT '被攻击玩家名称',`result` int(11) NOT NULL DEFAULT '0' COMMENT '挑战结果',`time` int(11) NOT NULL DEFAULT '0' COMMENT '发生时间',`point` int(11) NOT NULL DEFAULT '0' COMMENT '获得积分',`gold` int(11) NOT NULL DEFAULT '0' COMMENT '获得金币',`exp` int(11) NOT NULL DEFAULT '0' COMMENT '获得经验',`rob` int(11) NOT NULL DEFAULT '0' COMMENT '抢劫获得物品',`treasure` int(11) NOT NULL DEFAULT '0' COMMENT '寻宝获得物品',`revenged` tinyint(1) NOT NULL DEFAULT '0' COMMENT '已经复仇',PRIMARY KEY (`id`)) COMMENT='决战记录'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<DroiyanRecordBO> list) throws Exception {
        list.add(new DroiyanRecordBO(rs));
    }

    public long getAsynTaskTag() {
        return getId();
    }

    public String getItemsName() {
        return "`id`, `pid`, `target`, `target_name`, `result`, `time`, `point`, `gold`, `exp`, `rob`, `treasure`, `revenged`";
    }

    public String getTableName() {
        return "`droiyan_record`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.target).append("', ");
        strBuf.append("'").append((this.target_name == null) ? null : this.target_name.replace("'", "''")).append("', ");
        strBuf.append("'").append(this.result).append("', ");
        strBuf.append("'").append(this.time).append("', ");
        strBuf.append("'").append(this.point).append("', ");
        strBuf.append("'").append(this.gold).append("', ");
        strBuf.append("'").append(this.exp).append("', ");
        strBuf.append("'").append(this.rob).append("', ");
        strBuf.append("'").append(this.treasure).append("', ");
        strBuf.append("'").append(this.revenged ? 1 : 0).append("', ");
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

    public long getTarget() {
        return this.target;
    }

    public void setTarget(long target) {
        if (target == this.target)
            return;
        this.target = target;
    }

    public void saveTarget(long target) {
        if (target == this.target)
            return;
        this.target = target;
        saveField("target", Long.valueOf(target));
    }

    public String getTargetName() {
        return this.target_name;
    }

    public void setTargetName(String target_name) {
        if (target_name.equals(this.target_name))
            return;
        this.target_name = target_name;
    }

    public void saveTargetName(String target_name) {
        if (target_name.equals(this.target_name))
            return;
        this.target_name = target_name;
        saveField("target_name", target_name);
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

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        if (time == this.time)
            return;
        this.time = time;
    }

    public void saveTime(int time) {
        if (time == this.time)
            return;
        this.time = time;
        saveField("time", Integer.valueOf(time));
    }

    public int getPoint() {
        return this.point;
    }

    public void setPoint(int point) {
        if (point == this.point)
            return;
        this.point = point;
    }

    public void savePoint(int point) {
        if (point == this.point)
            return;
        this.point = point;
        saveField("point", Integer.valueOf(point));
    }

    public int getGold() {
        return this.gold;
    }

    public void setGold(int gold) {
        if (gold == this.gold)
            return;
        this.gold = gold;
    }

    public void saveGold(int gold) {
        if (gold == this.gold)
            return;
        this.gold = gold;
        saveField("gold", Integer.valueOf(gold));
    }

    public int getExp() {
        return this.exp;
    }

    public void setExp(int exp) {
        if (exp == this.exp)
            return;
        this.exp = exp;
    }

    public void saveExp(int exp) {
        if (exp == this.exp)
            return;
        this.exp = exp;
        saveField("exp", Integer.valueOf(exp));
    }

    public int getRob() {
        return this.rob;
    }

    public void setRob(int rob) {
        if (rob == this.rob)
            return;
        this.rob = rob;
    }

    public void saveRob(int rob) {
        if (rob == this.rob)
            return;
        this.rob = rob;
        saveField("rob", Integer.valueOf(rob));
    }

    public int getTreasure() {
        return this.treasure;
    }

    public void setTreasure(int treasure) {
        if (treasure == this.treasure)
            return;
        this.treasure = treasure;
    }

    public void saveTreasure(int treasure) {
        if (treasure == this.treasure)
            return;
        this.treasure = treasure;
        saveField("treasure", Integer.valueOf(treasure));
    }

    public boolean getRevenged() {
        return this.revenged;
    }

    public void setRevenged(boolean revenged) {
        if (revenged == this.revenged)
            return;
        this.revenged = revenged;
    }

    public void saveRevenged(boolean revenged) {
        if (revenged == this.revenged)
            return;
        this.revenged = revenged;
        saveField("revenged", Integer.valueOf(revenged ? 1 : 0));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `target` = '").append(this.target).append("',");
        sBuilder.append(" `target_name` = '").append((this.target_name == null) ? null : this.target_name.replace("'", "''")).append("',");
        sBuilder.append(" `result` = '").append(this.result).append("',");
        sBuilder.append(" `time` = '").append(this.time).append("',");
        sBuilder.append(" `point` = '").append(this.point).append("',");
        sBuilder.append(" `gold` = '").append(this.gold).append("',");
        sBuilder.append(" `exp` = '").append(this.exp).append("',");
        sBuilder.append(" `rob` = '").append(this.rob).append("',");
        sBuilder.append(" `treasure` = '").append(this.treasure).append("',");
        sBuilder.append(" `revenged` = '").append(this.revenged ? 1 : 0).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

