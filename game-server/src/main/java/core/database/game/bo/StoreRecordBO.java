package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StoreRecordBO extends BaseBO {
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "id",
        comment = "数据库表唯一键值"
    )
    private long id;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "pid",
        comment = "玩家ID"
    )
    private long pid;
    @DataBaseField(
        type = "int(11)",
        size = 25,
        fieldname = "lastRefreshTime",
        comment = "上次刷新时间,单位秒"
    )
    private List<Integer> lastRefreshTime;
    @DataBaseField(
        type = "int(11)",
        size = 25,
        fieldname = "freeRefreshTimes",
        comment = "当前免费刷新次数(两小时累积一次最大为10)"
    )
    private List<Integer> freeRefreshTimes;
    @DataBaseField(
        type = "int(11)",
        size = 25,
        fieldname = "paidRefreshTimes",
        comment = "当前付费刷新次数(次数越多花费越高最大配置)"
    )
    private List<Integer> paidRefreshTimes;
    @DataBaseField(
        type = "int(11)",
        size = 25,
        fieldname = "buyTimes",
        comment = "当天购买次数"
    )
    private List<Integer> buyTimes;
    @DataBaseField(
        type = "int(11)",
        size = 25,
        fieldname = "flushIcon",
        comment = "是否提示小红点信息"
    )
    private List<Integer> flushIcon;

    public StoreRecordBO() {
        this.id = 0L;
        this.pid = 0L;
        this.lastRefreshTime = new ArrayList<>(25);

        for (int i = 0; i < 25; i++) {
            this.lastRefreshTime.add(0);
        }

        this.freeRefreshTimes = new ArrayList<>(25);

        for (int i = 0; i < 25; i++) {
            this.freeRefreshTimes.add(0);
        }

        this.paidRefreshTimes = new ArrayList<>(25);

        for (int i = 0; i < 25; i++) {
            this.paidRefreshTimes.add(0);
        }

        this.buyTimes = new ArrayList<>(25);

        for (int i = 0; i < 25; i++) {
            this.buyTimes.add(0);
        }

        this.flushIcon = new ArrayList<>(25);

        for (int i = 0; i < 25; i++) {
            this.flushIcon.add(0);
        }
    }

    public StoreRecordBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.lastRefreshTime = new ArrayList<>(25);

        for (int i = 0; i < 25; i++) {
            this.lastRefreshTime.add(rs.getInt(i + 3));
        }

        this.freeRefreshTimes = new ArrayList<>(25);

        for (int i = 0; i < 25; i++) {
            this.freeRefreshTimes.add(rs.getInt(i + 28));
        }

        this.paidRefreshTimes = new ArrayList<>(25);

        for (int i = 0; i < 25; i++) {
            this.paidRefreshTimes.add(rs.getInt(i + 53));
        }

        this.buyTimes = new ArrayList<>(25);

        for (int i = 0; i < 25; i++) {
            this.buyTimes.add(rs.getInt(i + 78));
        }

        this.flushIcon = new ArrayList<>(25);

        for (int i = 0; i < 25; i++) {
            this.flushIcon.add(rs.getInt(i + 103));
        }
    }

    @Override
    public void getFromResultSet(ResultSet rs, List list) throws Exception {
        list.add(new StoreRecordBO(rs));
    }

    @Override
    public long getAsynTaskTag() {
        return this.getPid();
    }

    @Override
    public String getItemsName() {
        return "`id`, `pid`, `lastRefreshTime_0`, `lastRefreshTime_1`, `lastRefreshTime_2`, `lastRefreshTime_3`, `lastRefreshTime_4`, `lastRefreshTime_5`, `lastRefreshTime_6`, `lastRefreshTime_7`, `lastRefreshTime_8`, `lastRefreshTime_9`, `lastRefreshTime_10`, `lastRefreshTime_11`, `lastRefreshTime_12`, `lastRefreshTime_13`, `lastRefreshTime_14`, `lastRefreshTime_15`, `lastRefreshTime_16`, `lastRefreshTime_17`, `lastRefreshTime_18`, `lastRefreshTime_19`, `lastRefreshTime_20`, `lastRefreshTime_21`, `lastRefreshTime_22`, `lastRefreshTime_23`, `lastRefreshTime_24`, `freeRefreshTimes_0`, `freeRefreshTimes_1`, `freeRefreshTimes_2`, `freeRefreshTimes_3`, `freeRefreshTimes_4`, `freeRefreshTimes_5`, `freeRefreshTimes_6`, `freeRefreshTimes_7`, `freeRefreshTimes_8`, `freeRefreshTimes_9`, `freeRefreshTimes_10`, `freeRefreshTimes_11`, `freeRefreshTimes_12`, `freeRefreshTimes_13`, `freeRefreshTimes_14`, `freeRefreshTimes_15`, `freeRefreshTimes_16`, `freeRefreshTimes_17`, `freeRefreshTimes_18`, `freeRefreshTimes_19`, `freeRefreshTimes_20`, `freeRefreshTimes_21`, `freeRefreshTimes_22`, `freeRefreshTimes_23`, `freeRefreshTimes_24`, `paidRefreshTimes_0`, `paidRefreshTimes_1`, `paidRefreshTimes_2`, `paidRefreshTimes_3`, `paidRefreshTimes_4`, `paidRefreshTimes_5`, `paidRefreshTimes_6`, `paidRefreshTimes_7`, `paidRefreshTimes_8`, `paidRefreshTimes_9`, `paidRefreshTimes_10`, `paidRefreshTimes_11`, `paidRefreshTimes_12`, `paidRefreshTimes_13`, `paidRefreshTimes_14`, `paidRefreshTimes_15`, `paidRefreshTimes_16`, `paidRefreshTimes_17`, `paidRefreshTimes_18`, `paidRefreshTimes_19`, `paidRefreshTimes_20`, `paidRefreshTimes_21`, `paidRefreshTimes_22`, `paidRefreshTimes_23`, `paidRefreshTimes_24`, `buyTimes_0`, `buyTimes_1`, `buyTimes_2`, `buyTimes_3`, `buyTimes_4`, `buyTimes_5`, `buyTimes_6`, `buyTimes_7`, `buyTimes_8`, `buyTimes_9`, `buyTimes_10`, `buyTimes_11`, `buyTimes_12`, `buyTimes_13`, `buyTimes_14`, `buyTimes_15`, `buyTimes_16`, `buyTimes_17`, `buyTimes_18`, `buyTimes_19`, `buyTimes_20`, `buyTimes_21`, `buyTimes_22`, `buyTimes_23`, `buyTimes_24`, `flushIcon_0`, `flushIcon_1`, `flushIcon_2`, `flushIcon_3`, `flushIcon_4`, `flushIcon_5`, `flushIcon_6`, `flushIcon_7`, `flushIcon_8`, `flushIcon_9`, `flushIcon_10`, `flushIcon_11`, `flushIcon_12`, `flushIcon_13`, `flushIcon_14`, `flushIcon_15`, `flushIcon_16`, `flushIcon_17`, `flushIcon_18`, `flushIcon_19`, `flushIcon_20`, `flushIcon_21`, `flushIcon_22`, `flushIcon_23`, `flushIcon_24`";
    }

    @Override
    public String getTableName() {
        return "`storeRecord`";
    }

    @Override
    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");

        for (int i = 0; i < this.lastRefreshTime.size(); i++) {
            strBuf.append("'").append(this.lastRefreshTime.get(i)).append("', ");
        }

        for (int i = 0; i < this.freeRefreshTimes.size(); i++) {
            strBuf.append("'").append(this.freeRefreshTimes.get(i)).append("', ");
        }

        for (int i = 0; i < this.paidRefreshTimes.size(); i++) {
            strBuf.append("'").append(this.paidRefreshTimes.get(i)).append("', ");
        }

        for (int i = 0; i < this.buyTimes.size(); i++) {
            strBuf.append("'").append(this.buyTimes.get(i)).append("', ");
        }

        for (int i = 0; i < this.flushIcon.size(); i++) {
            strBuf.append("'").append(this.flushIcon.get(i)).append("', ");
        }

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

    public int getLastRefreshTimeSize() {
        return this.lastRefreshTime.size();
    }

    public List<Integer> getLastRefreshTimeAll() {
        return new ArrayList<>(this.lastRefreshTime);
    }

    public void setLastRefreshTimeAll(int value) {
        for (int i = 0; i < this.lastRefreshTime.size(); i++) {
            this.lastRefreshTime.set(i, value);
        }
    }

    public void saveLastRefreshTimeAll(int value) {
        this.setLastRefreshTimeAll(value);
        this.saveAll();
    }

    public int getLastRefreshTime(int index) {
        return this.lastRefreshTime.get(index);
    }

    public void setLastRefreshTime(int index, int value) {
        if (value != this.lastRefreshTime.get(index)) {
            this.lastRefreshTime.set(index, value);
        }
    }

    public void saveLastRefreshTime(int index, int value) {
        if (value != this.lastRefreshTime.get(index)) {
            this.lastRefreshTime.set(index, value);
            this.saveField("lastRefreshTime_" + index, this.lastRefreshTime.get(index));
        }
    }

    public int getFreeRefreshTimesSize() {
        return this.freeRefreshTimes.size();
    }

    public List<Integer> getFreeRefreshTimesAll() {
        return new ArrayList<>(this.freeRefreshTimes);
    }

    public void setFreeRefreshTimesAll(int value) {
        for (int i = 0; i < this.freeRefreshTimes.size(); i++) {
            this.freeRefreshTimes.set(i, value);
        }
    }

    public void saveFreeRefreshTimesAll(int value) {
        this.setFreeRefreshTimesAll(value);
        this.saveAll();
    }

    public int getFreeRefreshTimes(int index) {
        return this.freeRefreshTimes.get(index);
    }

    public void setFreeRefreshTimes(int index, int value) {
        if (value != this.freeRefreshTimes.get(index)) {
            this.freeRefreshTimes.set(index, value);
        }
    }

    public void saveFreeRefreshTimes(int index, int value) {
        if (value != this.freeRefreshTimes.get(index)) {
            this.freeRefreshTimes.set(index, value);
            this.saveField("freeRefreshTimes_" + index, this.freeRefreshTimes.get(index));
        }
    }

    public int getPaidRefreshTimesSize() {
        return this.paidRefreshTimes.size();
    }

    public List<Integer> getPaidRefreshTimesAll() {
        return new ArrayList<>(this.paidRefreshTimes);
    }

    public void setPaidRefreshTimesAll(int value) {
        for (int i = 0; i < this.paidRefreshTimes.size(); i++) {
            this.paidRefreshTimes.set(i, value);
        }
    }

    public void savePaidRefreshTimesAll(int value) {
        this.setPaidRefreshTimesAll(value);
        this.saveAll();
    }

    public int getPaidRefreshTimes(int index) {
        return this.paidRefreshTimes.get(index);
    }

    public void setPaidRefreshTimes(int index, int value) {
        if (value != this.paidRefreshTimes.get(index)) {
            this.paidRefreshTimes.set(index, value);
        }
    }

    public void savePaidRefreshTimes(int index, int value) {
        if (value != this.paidRefreshTimes.get(index)) {
            this.paidRefreshTimes.set(index, value);
            this.saveField("paidRefreshTimes_" + index, this.paidRefreshTimes.get(index));
        }
    }

    public int getBuyTimesSize() {
        return this.buyTimes.size();
    }

    public List<Integer> getBuyTimesAll() {
        return new ArrayList<>(this.buyTimes);
    }

    public void setBuyTimesAll(int value) {
        for (int i = 0; i < this.buyTimes.size(); i++) {
            this.buyTimes.set(i, value);
        }
    }

    public void saveBuyTimesAll(int value) {
        this.setBuyTimesAll(value);
        this.saveAll();
    }

    public int getBuyTimes(int index) {
        return this.buyTimes.get(index);
    }

    public void setBuyTimes(int index, int value) {
        if (value != this.buyTimes.get(index)) {
            this.buyTimes.set(index, value);
        }
    }

    public void saveBuyTimes(int index, int value) {
        if (value != this.buyTimes.get(index)) {
            this.buyTimes.set(index, value);
            this.saveField("buyTimes_" + index, this.buyTimes.get(index));
        }
    }

    public int getFlushIconSize() {
        return this.flushIcon.size();
    }

    public List<Integer> getFlushIconAll() {
        return new ArrayList<>(this.flushIcon);
    }

    public void setFlushIconAll(int value) {
        for (int i = 0; i < this.flushIcon.size(); i++) {
            this.flushIcon.set(i, value);
        }
    }

    public void saveFlushIconAll(int value) {
        this.setFlushIconAll(value);
        this.saveAll();
    }

    public int getFlushIcon(int index) {
        return this.flushIcon.get(index);
    }

    public void setFlushIcon(int index, int value) {
        if (value != this.flushIcon.get(index)) {
            this.flushIcon.set(index, value);
        }
    }

    public void saveFlushIcon(int index, int value) {
        if (value != this.flushIcon.get(index)) {
            this.flushIcon.set(index, value);
            this.saveField("flushIcon_" + index, this.flushIcon.get(index));
        }
    }

    @Override
    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");

        for (int i = 0; i < this.lastRefreshTime.size(); i++) {
            sBuilder.append(" `lastRefreshTime_").append(i).append("` = '").append(this.lastRefreshTime.get(i)).append("',");
        }

        for (int i = 0; i < this.freeRefreshTimes.size(); i++) {
            sBuilder.append(" `freeRefreshTimes_").append(i).append("` = '").append(this.freeRefreshTimes.get(i)).append("',");
        }

        for (int i = 0; i < this.paidRefreshTimes.size(); i++) {
            sBuilder.append(" `paidRefreshTimes_").append(i).append("` = '").append(this.paidRefreshTimes.get(i)).append("',");
        }

        for (int i = 0; i < this.buyTimes.size(); i++) {
            sBuilder.append(" `buyTimes_").append(i).append("` = '").append(this.buyTimes.get(i)).append("',");
        }

        for (int i = 0; i < this.flushIcon.size(); i++) {
            sBuilder.append(" `flushIcon_").append(i).append("` = '").append(this.flushIcon.get(i)).append("',");
        }

        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }

    public static String getSql_TableCreate() {
        return "CREATE TABLE IF NOT EXISTS `storeRecord` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家ID',`lastRefreshTime_0` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_1` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_2` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_3` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_4` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_5` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_6` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_7` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_8` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_9` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_10` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_11` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_12` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_13` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_14` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_15` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_16` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_17` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_18` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_19` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_20` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_21` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_22` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_23` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_24` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`freeRefreshTimes_0` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_1` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_2` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_3` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_4` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_5` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_6` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_7` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_8` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_9` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_10` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_11` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_12` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_13` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_14` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_15` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_16` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_17` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_18` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_19` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_20` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_21` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_22` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_23` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_24` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`paidRefreshTimes_0` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_1` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_2` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_3` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_4` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_5` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_6` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_7` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_8` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_9` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_10` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_11` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_12` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_13` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_14` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_15` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_16` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_17` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_18` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_19` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_20` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_21` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_22` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_23` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_24` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`buyTimes_0` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_1` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_2` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_3` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_4` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_5` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_6` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_7` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_8` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_9` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_10` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_11` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_12` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_13` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_14` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_15` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_16` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_17` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_18` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_19` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_20` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_21` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_22` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_23` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_24` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`flushIcon_0` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_1` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_2` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_3` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_4` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_5` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_6` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_7` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_8` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_9` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_10` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_11` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_12` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_13` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_14` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_15` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_16` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_17` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_18` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_19` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_20` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_21` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_22` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_23` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_24` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',UNIQUE INDEX `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='玩家个人商店刷新记录表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT="
            + (ServerConfig.getInitialID() + 1L);
    }
}
