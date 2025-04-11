package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StealGoldNewsBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
    private long pid;
    @DataBaseField(type = "bigint(20)", fieldname = "atkid", comment = "偷取者id")
    private long atkid;
    @DataBaseField(type = "int(11)", fieldname = "money", comment = "偷取数量")
    private int money;
    @DataBaseField(type = "int(11)", fieldname = "time", comment = "时间")
    private int time;

    public StealGoldNewsBO() {
        this.id = 0L;
        this.pid = 0L;
        this.atkid = 0L;
        this.money = 0;
        this.time = 0;
    }

    public StealGoldNewsBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.atkid = rs.getLong(3);
        this.money = rs.getInt(4);
        this.time = rs.getInt(5);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `steal_gold_news` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`atkid` bigint(20) NOT NULL DEFAULT '0' COMMENT '偷取者id',`money` int(11) NOT NULL DEFAULT '0' COMMENT '偷取数量',`time` int(11) NOT NULL DEFAULT '0' COMMENT '时间',PRIMARY KEY (`id`)) COMMENT='探金手传闻系统'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<StealGoldNewsBO> list) throws Exception {
        list.add(new StealGoldNewsBO(rs));
    }

    public long getAsynTaskTag() {
        return getId();
    }

    public String getItemsName() {
        return "`id`, `pid`, `atkid`, `money`, `time`";
    }

    public String getTableName() {
        return "`steal_gold_news`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.atkid).append("', ");
        strBuf.append("'").append(this.money).append("', ");
        strBuf.append("'").append(this.time).append("', ");
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

    public long getAtkid() {
        return this.atkid;
    }

    public void setAtkid(long atkid) {
        if (atkid == this.atkid)
            return;
        this.atkid = atkid;
    }

    public void saveAtkid(long atkid) {
        if (atkid == this.atkid)
            return;
        this.atkid = atkid;
        saveField("atkid", Long.valueOf(atkid));
    }

    public int getMoney() {
        return this.money;
    }

    public void setMoney(int money) {
        if (money == this.money)
            return;
        this.money = money;
    }

    public void saveMoney(int money) {
        if (money == this.money)
            return;
        this.money = money;
        saveField("money", Integer.valueOf(money));
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

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `atkid` = '").append(this.atkid).append("',");
        sBuilder.append(" `money` = '").append(this.money).append("',");
        sBuilder.append(" `time` = '").append(this.time).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

