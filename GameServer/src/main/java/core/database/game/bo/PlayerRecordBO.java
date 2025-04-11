package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PlayerRecordBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "角色ID")
    private long pid;
    @DataBaseField(type = "int(11)", size = 60, fieldname = "value", comment = "各项记录值")
    private List<Integer> value;
    @DataBaseField(type = "int(11)", size = 30, fieldname = "useItemTimes", comment = "使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]")
    private List<Integer> useItemTimes;
    @DataBaseField(type = "int(11)", fieldname = "lastUseBoxTime", comment = "最近开启宝箱时间")
    private int lastUseBoxTime;

    public PlayerRecordBO() {
        this.id = 0L;
        this.pid = 0L;
        this.value = new ArrayList<>(60);
        int i;
        for (i = 0; i < 60; i++) {
            this.value.add(Integer.valueOf(0));
        }
        this.useItemTimes = new ArrayList<>(30);
        for (i = 0; i < 30; i++) {
            this.useItemTimes.add(Integer.valueOf(0));
        }
        this.lastUseBoxTime = 0;
    }

    public PlayerRecordBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.value = new ArrayList<>(60);
        int i;
        for (i = 0; i < 60; i++) {
            this.value.add(Integer.valueOf(rs.getInt(i + 3)));
        }
        this.useItemTimes = new ArrayList<>(30);
        for (i = 0; i < 30; i++) {
            this.useItemTimes.add(Integer.valueOf(rs.getInt(i + 63)));
        }
        this.lastUseBoxTime = rs.getInt(93);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `playerRecord` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '角色ID',`value_0` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_1` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_2` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_3` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_4` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_5` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_6` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_7` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_8` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_9` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_10` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_11` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_12` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_13` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_14` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_15` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_16` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_17` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_18` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_19` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_20` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_21` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_22` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_23` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_24` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_25` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_26` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_27` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_28` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_29` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_30` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_31` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_32` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_33` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_34` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_35` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_36` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_37` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_38` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_39` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_40` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_41` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_42` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_43` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_44` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_45` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_46` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_47` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_48` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_49` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_50` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_51` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_52` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_53` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_54` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_55` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_56` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_57` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_58` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`value_59` int(11) NOT NULL DEFAULT '0' COMMENT '各项记录值',`useItemTimes_0` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_1` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_2` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_3` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_4` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_5` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_6` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_7` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_8` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_9` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_10` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_11` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_12` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_13` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_14` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_15` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_16` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_17` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_18` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_19` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_20` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_21` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_22` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_23` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_24` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_25` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_26` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_27` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_28` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`useItemTimes_29` int(11) NOT NULL DEFAULT '0' COMMENT '使用物品次数[根据物品使用类型来区分，主要用于开宝箱时根据不同的开启次数来取随机值]',`lastUseBoxTime` int(11) NOT NULL DEFAULT '0' COMMENT '最近开启宝箱时间',UNIQUE INDEX `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='每日刷新'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<PlayerRecordBO> list) throws Exception {
        list.add(new PlayerRecordBO(rs));
    }

    public long getAsynTaskTag() {
        return getPid();
    }

    public String getItemsName() {
        return "`id`, `pid`, `value_0`, `value_1`, `value_2`, `value_3`, `value_4`, `value_5`, `value_6`, `value_7`, `value_8`, `value_9`, `value_10`, `value_11`, `value_12`, `value_13`, `value_14`, `value_15`, `value_16`, `value_17`, `value_18`, `value_19`, `value_20`, `value_21`, `value_22`, `value_23`, `value_24`, `value_25`, `value_26`, `value_27`, `value_28`, `value_29`, `value_30`, `value_31`, `value_32`, `value_33`, `value_34`, `value_35`, `value_36`, `value_37`, `value_38`, `value_39`, `value_40`, `value_41`, `value_42`, `value_43`, `value_44`, `value_45`, `value_46`, `value_47`, `value_48`, `value_49`, `value_50`, `value_51`, `value_52`, `value_53`, `value_54`, `value_55`, `value_56`, `value_57`, `value_58`, `value_59`, `useItemTimes_0`, `useItemTimes_1`, `useItemTimes_2`, `useItemTimes_3`, `useItemTimes_4`, `useItemTimes_5`, `useItemTimes_6`, `useItemTimes_7`, `useItemTimes_8`, `useItemTimes_9`, `useItemTimes_10`, `useItemTimes_11`, `useItemTimes_12`, `useItemTimes_13`, `useItemTimes_14`, `useItemTimes_15`, `useItemTimes_16`, `useItemTimes_17`, `useItemTimes_18`, `useItemTimes_19`, `useItemTimes_20`, `useItemTimes_21`, `useItemTimes_22`, `useItemTimes_23`, `useItemTimes_24`, `useItemTimes_25`, `useItemTimes_26`, `useItemTimes_27`, `useItemTimes_28`, `useItemTimes_29`, `lastUseBoxTime`";
    }

    public String getTableName() {
        return "`playerRecord`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        int i;
        for (i = 0; i < this.value.size(); i++) {
            strBuf.append("'").append(this.value.get(i)).append("', ");
        }
        for (i = 0; i < this.useItemTimes.size(); i++) {
            strBuf.append("'").append(this.useItemTimes.get(i)).append("', ");
        }
        strBuf.append("'").append(this.lastUseBoxTime).append("', ");
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

    public int getValueSize() {
        return this.value.size();
    }

    public List<Integer> getValueAll() {
        return new ArrayList<>(this.value);
    }

    public void setValueAll(int value) {
        for (int i = 0; i < this.value.size(); ) {
            this.value.set(i, Integer.valueOf(value));
            i++;
        }
    }

    public void saveValueAll(int value) {
        setValueAll(value);
        saveAll();
    }

    public int getValue(int index) {
        return ((Integer) this.value.get(index)).intValue();
    }

    public void setValue(int index, int value) {
        if (value == ((Integer) this.value.get(index)).intValue())
            return;
        this.value.set(index, Integer.valueOf(value));
    }

    public void saveValue(int index, int value) {
        if (value == ((Integer) this.value.get(index)).intValue())
            return;
        this.value.set(index, Integer.valueOf(value));
        saveField("value_" + index, this.value.get(index));
    }

    public int getUseItemTimesSize() {
        return this.useItemTimes.size();
    }

    public List<Integer> getUseItemTimesAll() {
        return new ArrayList<>(this.useItemTimes);
    }

    public void setUseItemTimesAll(int value) {
        for (int i = 0; i < this.useItemTimes.size(); ) {
            this.useItemTimes.set(i, Integer.valueOf(value));
            i++;
        }
    }

    public void saveUseItemTimesAll(int value) {
        setUseItemTimesAll(value);
        saveAll();
    }

    public int getUseItemTimes(int index) {
        return ((Integer) this.useItemTimes.get(index)).intValue();
    }

    public void setUseItemTimes(int index, int value) {
        if (value == ((Integer) this.useItemTimes.get(index)).intValue())
            return;
        this.useItemTimes.set(index, Integer.valueOf(value));
    }

    public void saveUseItemTimes(int index, int value) {
        if (value == ((Integer) this.useItemTimes.get(index)).intValue())
            return;
        this.useItemTimes.set(index, Integer.valueOf(value));
        saveField("useItemTimes_" + index, this.useItemTimes.get(index));
    }

    public int getLastUseBoxTime() {
        return this.lastUseBoxTime;
    }

    public void setLastUseBoxTime(int lastUseBoxTime) {
        if (lastUseBoxTime == this.lastUseBoxTime)
            return;
        this.lastUseBoxTime = lastUseBoxTime;
    }

    public void saveLastUseBoxTime(int lastUseBoxTime) {
        if (lastUseBoxTime == this.lastUseBoxTime)
            return;
        this.lastUseBoxTime = lastUseBoxTime;
        saveField("lastUseBoxTime", Integer.valueOf(lastUseBoxTime));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        int i;
        for (i = 0; i < this.value.size(); i++) {
            sBuilder.append(" `value_").append(i).append("` = '").append(this.value.get(i)).append("',");
        }
        for (i = 0; i < this.useItemTimes.size(); i++) {
            sBuilder.append(" `useItemTimes_").append(i).append("` = '").append(this.useItemTimes.get(i)).append("',");
        }
        sBuilder.append(" `lastUseBoxTime` = '").append(this.lastUseBoxTime).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

