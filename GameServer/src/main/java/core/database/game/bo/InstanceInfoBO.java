package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InstanceInfoBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "所属玩家ID")
    private long pid;
    @DataBaseField(type = "int(11)", size = 10, fieldname = "instanceMaxLevel", comment = "最高副本关卡等级")
    private List<Integer> instanceMaxLevel;
    @DataBaseField(type = "int(11)", size = 10, fieldname = "challengTimes", comment = "挑战次数")
    private List<Integer> challengTimes;

    public InstanceInfoBO() {
        this.id = 0L;
        this.pid = 0L;
        this.instanceMaxLevel = new ArrayList<>(10);
        int i;
        for (i = 0; i < 10; i++) {
            this.instanceMaxLevel.add(Integer.valueOf(0));
        }
        this.challengTimes = new ArrayList<>(10);
        for (i = 0; i < 10; i++) {
            this.challengTimes.add(Integer.valueOf(0));
        }
    }

    public InstanceInfoBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.instanceMaxLevel = new ArrayList<>(10);
        int i;
        for (i = 0; i < 10; i++) {
            this.instanceMaxLevel.add(Integer.valueOf(rs.getInt(i + 3)));
        }
        this.challengTimes = new ArrayList<>(10);
        for (i = 0; i < 10; i++) {
            this.challengTimes.add(Integer.valueOf(rs.getInt(i + 13)));
        }
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `instance_info` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属玩家ID',`instanceMaxLevel_0` int(11) NOT NULL DEFAULT '0' COMMENT '最高副本关卡等级',`instanceMaxLevel_1` int(11) NOT NULL DEFAULT '0' COMMENT '最高副本关卡等级',`instanceMaxLevel_2` int(11) NOT NULL DEFAULT '0' COMMENT '最高副本关卡等级',`instanceMaxLevel_3` int(11) NOT NULL DEFAULT '0' COMMENT '最高副本关卡等级',`instanceMaxLevel_4` int(11) NOT NULL DEFAULT '0' COMMENT '最高副本关卡等级',`instanceMaxLevel_5` int(11) NOT NULL DEFAULT '0' COMMENT '最高副本关卡等级',`instanceMaxLevel_6` int(11) NOT NULL DEFAULT '0' COMMENT '最高副本关卡等级',`instanceMaxLevel_7` int(11) NOT NULL DEFAULT '0' COMMENT '最高副本关卡等级',`instanceMaxLevel_8` int(11) NOT NULL DEFAULT '0' COMMENT '最高副本关卡等级',`instanceMaxLevel_9` int(11) NOT NULL DEFAULT '0' COMMENT '最高副本关卡等级',`challengTimes_0` int(11) NOT NULL DEFAULT '0' COMMENT '挑战次数',`challengTimes_1` int(11) NOT NULL DEFAULT '0' COMMENT '挑战次数',`challengTimes_2` int(11) NOT NULL DEFAULT '0' COMMENT '挑战次数',`challengTimes_3` int(11) NOT NULL DEFAULT '0' COMMENT '挑战次数',`challengTimes_4` int(11) NOT NULL DEFAULT '0' COMMENT '挑战次数',`challengTimes_5` int(11) NOT NULL DEFAULT '0' COMMENT '挑战次数',`challengTimes_6` int(11) NOT NULL DEFAULT '0' COMMENT '挑战次数',`challengTimes_7` int(11) NOT NULL DEFAULT '0' COMMENT '挑战次数',`challengTimes_8` int(11) NOT NULL DEFAULT '0' COMMENT '挑战次数',`challengTimes_9` int(11) NOT NULL DEFAULT '0' COMMENT '挑战次数',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='玩家装备副本信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<InstanceInfoBO> list) throws Exception {
        list.add(new InstanceInfoBO(rs));
    }

    public long getAsynTaskTag() {
        return getPid();
    }

    public String getItemsName() {
        return "`id`, `pid`, `instanceMaxLevel_0`, `instanceMaxLevel_1`, `instanceMaxLevel_2`, `instanceMaxLevel_3`, `instanceMaxLevel_4`, `instanceMaxLevel_5`, `instanceMaxLevel_6`, `instanceMaxLevel_7`, `instanceMaxLevel_8`, `instanceMaxLevel_9`, `challengTimes_0`, `challengTimes_1`, `challengTimes_2`, `challengTimes_3`, `challengTimes_4`, `challengTimes_5`, `challengTimes_6`, `challengTimes_7`, `challengTimes_8`, `challengTimes_9`";
    }

    public String getTableName() {
        return "`instance_info`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        int i;
        for (i = 0; i < this.instanceMaxLevel.size(); i++) {
            strBuf.append("'").append(this.instanceMaxLevel.get(i)).append("', ");
        }
        for (i = 0; i < this.challengTimes.size(); i++) {
            strBuf.append("'").append(this.challengTimes.get(i)).append("', ");
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

    public int getInstanceMaxLevelSize() {
        return this.instanceMaxLevel.size();
    }

    public List<Integer> getInstanceMaxLevelAll() {
        return new ArrayList<>(this.instanceMaxLevel);
    }

    public void setInstanceMaxLevelAll(int value) {
        for (int i = 0; i < this.instanceMaxLevel.size(); ) {
            this.instanceMaxLevel.set(i, Integer.valueOf(value));
            i++;
        }
    }

    public void saveInstanceMaxLevelAll(int value) {
        setInstanceMaxLevelAll(value);
        saveAll();
    }

    public int getInstanceMaxLevel(int index) {
        return ((Integer) this.instanceMaxLevel.get(index)).intValue();
    }

    public void setInstanceMaxLevel(int index, int value) {
        if (value == ((Integer) this.instanceMaxLevel.get(index)).intValue())
            return;
        this.instanceMaxLevel.set(index, Integer.valueOf(value));
    }

    public void saveInstanceMaxLevel(int index, int value) {
        if (value == ((Integer) this.instanceMaxLevel.get(index)).intValue())
            return;
        this.instanceMaxLevel.set(index, Integer.valueOf(value));
        saveField("instanceMaxLevel_" + index, this.instanceMaxLevel.get(index));
    }

    public int getChallengTimesSize() {
        return this.challengTimes.size();
    }

    public List<Integer> getChallengTimesAll() {
        return new ArrayList<>(this.challengTimes);
    }

    public void setChallengTimesAll(int value) {
        for (int i = 0; i < this.challengTimes.size(); ) {
            this.challengTimes.set(i, Integer.valueOf(value));
            i++;
        }
    }

    public void saveChallengTimesAll(int value) {
        setChallengTimesAll(value);
        saveAll();
    }

    public int getChallengTimes(int index) {
        return ((Integer) this.challengTimes.get(index)).intValue();
    }

    public void setChallengTimes(int index, int value) {
        if (value == ((Integer) this.challengTimes.get(index)).intValue())
            return;
        this.challengTimes.set(index, Integer.valueOf(value));
    }

    public void saveChallengTimes(int index, int value) {
        if (value == ((Integer) this.challengTimes.get(index)).intValue())
            return;
        this.challengTimes.set(index, Integer.valueOf(value));
        saveField("challengTimes_" + index, this.challengTimes.get(index));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        int i;
        for (i = 0; i < this.instanceMaxLevel.size(); i++) {
            sBuilder.append(" `instanceMaxLevel_").append(i).append("` = '").append(this.instanceMaxLevel.get(i)).append("',");
        }
        for (i = 0; i < this.challengTimes.size(); i++) {
            sBuilder.append(" `challengTimes_").append(i).append("` = '").append(this.challengTimes.get(i)).append("',");
        }
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

