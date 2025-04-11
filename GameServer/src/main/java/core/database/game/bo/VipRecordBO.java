package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VipRecordBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家ID")
    private long pid;
    @DataBaseField(type = "int(11)", fieldname = "totalRecharge", comment = "累计充值,用于计算vip等级")
    private int totalRecharge;
    @DataBaseField(type = "int(11)", size = 17, fieldname = "lastFetchPrivateTime", comment = "特权礼包时间[秒],包含0级")
    private List<Integer> lastFetchPrivateTime;

    public VipRecordBO() {
        this.id = 0L;
        this.pid = 0L;
        this.totalRecharge = 0;
        this.lastFetchPrivateTime = new ArrayList<>(17);
        for (int i = 0; i < 17; i++) {
            this.lastFetchPrivateTime.add(Integer.valueOf(0));
        }
    }

    public VipRecordBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.totalRecharge = rs.getInt(3);
        this.lastFetchPrivateTime = new ArrayList<>(17);
        for (int i = 0; i < 17; i++) {
            this.lastFetchPrivateTime.add(Integer.valueOf(rs.getInt(i + 4)));
        }
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `vipRecord` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家ID',`totalRecharge` int(11) NOT NULL DEFAULT '0' COMMENT '累计充值,用于计算vip等级',`lastFetchPrivateTime_0` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_1` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_2` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_3` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_4` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_5` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_6` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_7` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_8` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_9` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_10` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_11` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_12` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_13` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_14` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_15` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_16` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',UNIQUE INDEX `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='VIP信息相关记录表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<VipRecordBO> list) throws Exception {
        list.add(new VipRecordBO(rs));
    }

    public long getAsynTaskTag() {
        return getPid();
    }

    public String getItemsName() {
        return "`id`, `pid`, `totalRecharge`, `lastFetchPrivateTime_0`, `lastFetchPrivateTime_1`, `lastFetchPrivateTime_2`, `lastFetchPrivateTime_3`, `lastFetchPrivateTime_4`, `lastFetchPrivateTime_5`, `lastFetchPrivateTime_6`, `lastFetchPrivateTime_7`, `lastFetchPrivateTime_8`, `lastFetchPrivateTime_9`, `lastFetchPrivateTime_10`, `lastFetchPrivateTime_11`, `lastFetchPrivateTime_12`, `lastFetchPrivateTime_13`, `lastFetchPrivateTime_14`, `lastFetchPrivateTime_15`, `lastFetchPrivateTime_16`";
    }

    public String getTableName() {
        return "`vipRecord`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.totalRecharge).append("', ");
        for (int i = 0; i < this.lastFetchPrivateTime.size(); i++) {
            strBuf.append("'").append(this.lastFetchPrivateTime.get(i)).append("', ");
        }
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

    public int getTotalRecharge() {
        return this.totalRecharge;
    }

    public void setTotalRecharge(int totalRecharge) {
        if (totalRecharge == this.totalRecharge)
            return;
        this.totalRecharge = totalRecharge;
    }

    public void saveTotalRecharge(int totalRecharge) {
        if (totalRecharge == this.totalRecharge)
            return;
        this.totalRecharge = totalRecharge;
        saveField("totalRecharge", Integer.valueOf(totalRecharge));
    }

    public int getLastFetchPrivateTimeSize() {
        return this.lastFetchPrivateTime.size();
    }

    public List<Integer> getLastFetchPrivateTimeAll() {
        return new ArrayList<>(this.lastFetchPrivateTime);
    }

    public void setLastFetchPrivateTimeAll(int value) {
        for (int i = 0; i < this.lastFetchPrivateTime.size(); ) {
            this.lastFetchPrivateTime.set(i, Integer.valueOf(value));
            i++;
        }
    }

    public void saveLastFetchPrivateTimeAll(int value) {
        setLastFetchPrivateTimeAll(value);
        saveAll();
    }

    public int getLastFetchPrivateTime(int index) {
        return ((Integer) this.lastFetchPrivateTime.get(index)).intValue();
    }

    public void setLastFetchPrivateTime(int index, int value) {
        if (value == ((Integer) this.lastFetchPrivateTime.get(index)).intValue())
            return;
        this.lastFetchPrivateTime.set(index, Integer.valueOf(value));
    }

    public void saveLastFetchPrivateTime(int index, int value) {
        if (value == ((Integer) this.lastFetchPrivateTime.get(index)).intValue())
            return;
        this.lastFetchPrivateTime.set(index, Integer.valueOf(value));
        saveField("lastFetchPrivateTime_" + index, this.lastFetchPrivateTime.get(index));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `totalRecharge` = '").append(this.totalRecharge).append("',");
        for (int i = 0; i < this.lastFetchPrivateTime.size(); i++) {
            sBuilder.append(" `lastFetchPrivateTime_").append(i).append("` = '").append(this.lastFetchPrivateTime.get(i)).append("',");
        }
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

