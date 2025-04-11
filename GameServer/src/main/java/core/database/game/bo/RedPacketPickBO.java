package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RedPacketPickBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
    private long pid;
    @DataBaseField(type = "bigint(20)", fieldname = "packet_id", comment = "红包id")
    private long packet_id;
    @DataBaseField(type = "int(11)", fieldname = "money", comment = "领取金额")
    private int money;
    @DataBaseField(type = "int(11)", fieldname = "time", comment = "领取时间")
    private int time;

    public RedPacketPickBO() {
        this.id = 0L;
        this.pid = 0L;
        this.packet_id = 0L;
        this.money = 0;
        this.time = 0;
    }

    public RedPacketPickBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.packet_id = rs.getLong(3);
        this.money = rs.getInt(4);
        this.time = rs.getInt(5);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `red_packet_pick` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`packet_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '红包id',`money` int(11) NOT NULL DEFAULT '0' COMMENT '领取金额',`time` int(11) NOT NULL DEFAULT '0' COMMENT '领取时间',PRIMARY KEY (`id`)) COMMENT='红包领取表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<RedPacketPickBO> list) throws Exception {
        list.add(new RedPacketPickBO(rs));
    }

    public long getAsynTaskTag() {
        return getId();
    }

    public String getItemsName() {
        return "`id`, `pid`, `packet_id`, `money`, `time`";
    }

    public String getTableName() {
        return "`red_packet_pick`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.packet_id).append("', ");
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

    public long getPacketId() {
        return this.packet_id;
    }

    public void setPacketId(long packet_id) {
        if (packet_id == this.packet_id)
            return;
        this.packet_id = packet_id;
    }

    public void savePacketId(long packet_id) {
        if (packet_id == this.packet_id)
            return;
        this.packet_id = packet_id;
        saveField("packet_id", Long.valueOf(packet_id));
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
        sBuilder.append(" `packet_id` = '").append(this.packet_id).append("',");
        sBuilder.append(" `money` = '").append(this.money).append("',");
        sBuilder.append(" `time` = '").append(this.time).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

