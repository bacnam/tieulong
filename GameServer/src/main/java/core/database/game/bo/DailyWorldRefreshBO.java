package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DailyWorldRefreshBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "int(11)", fieldname = "serverStart", comment = "服务器首次启动")
    private int serverStart;
    @DataBaseField(type = "int(11)", size = 40, fieldname = "indexLastTime", comment = "上次刷新时间")
    private List<Integer> indexLastTime;

    public DailyWorldRefreshBO() {
        this.id = 0L;
        this.serverStart = 0;
        this.indexLastTime = new ArrayList<>(40);
        for (int i = 0; i < 40; i++) {
            this.indexLastTime.add(Integer.valueOf(0));
        }
    }

    public DailyWorldRefreshBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.serverStart = rs.getInt(2);
        this.indexLastTime = new ArrayList<>(40);
        for (int i = 0; i < 40; i++) {
            this.indexLastTime.add(Integer.valueOf(rs.getInt(i + 3)));
        }
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `dailyWorldRefresh` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`serverStart` int(11) NOT NULL DEFAULT '0' COMMENT '服务器首次启动',`indexLastTime_0` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_1` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_2` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_3` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_4` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_5` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_6` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_7` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_8` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_9` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_10` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_11` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_12` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_13` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_14` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_15` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_16` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_17` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_18` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_19` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_20` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_21` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_22` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_23` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_24` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_25` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_26` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_27` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_28` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_29` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_30` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_31` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_32` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_33` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_34` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_35` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_36` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_37` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_38` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_39` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',PRIMARY KEY (`id`)) COMMENT='每日刷新'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<DailyWorldRefreshBO> list) throws Exception {
        list.add(new DailyWorldRefreshBO(rs));
    }

    public long getAsynTaskTag() {
        return 0L;
    }

    public String getItemsName() {
        return "`id`, `serverStart`, `indexLastTime_0`, `indexLastTime_1`, `indexLastTime_2`, `indexLastTime_3`, `indexLastTime_4`, `indexLastTime_5`, `indexLastTime_6`, `indexLastTime_7`, `indexLastTime_8`, `indexLastTime_9`, `indexLastTime_10`, `indexLastTime_11`, `indexLastTime_12`, `indexLastTime_13`, `indexLastTime_14`, `indexLastTime_15`, `indexLastTime_16`, `indexLastTime_17`, `indexLastTime_18`, `indexLastTime_19`, `indexLastTime_20`, `indexLastTime_21`, `indexLastTime_22`, `indexLastTime_23`, `indexLastTime_24`, `indexLastTime_25`, `indexLastTime_26`, `indexLastTime_27`, `indexLastTime_28`, `indexLastTime_29`, `indexLastTime_30`, `indexLastTime_31`, `indexLastTime_32`, `indexLastTime_33`, `indexLastTime_34`, `indexLastTime_35`, `indexLastTime_36`, `indexLastTime_37`, `indexLastTime_38`, `indexLastTime_39`";
    }

    public String getTableName() {
        return "`dailyWorldRefresh`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.serverStart).append("', ");
        for (int i = 0; i < this.indexLastTime.size(); i++) {
            strBuf.append("'").append(this.indexLastTime.get(i)).append("', ");
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

    public int getServerStart() {
        return this.serverStart;
    }

    public void setServerStart(int serverStart) {
        if (serverStart == this.serverStart)
            return;
        this.serverStart = serverStart;
    }

    public void saveServerStart(int serverStart) {
        if (serverStart == this.serverStart)
            return;
        this.serverStart = serverStart;
        saveField("serverStart", Integer.valueOf(serverStart));
    }

    public int getIndexLastTimeSize() {
        return this.indexLastTime.size();
    }

    public List<Integer> getIndexLastTimeAll() {
        return new ArrayList<>(this.indexLastTime);
    }

    public void setIndexLastTimeAll(int value) {
        for (int i = 0; i < this.indexLastTime.size(); ) {
            this.indexLastTime.set(i, Integer.valueOf(value));
            i++;
        }
    }

    public void saveIndexLastTimeAll(int value) {
        setIndexLastTimeAll(value);
        saveAll();
    }

    public int getIndexLastTime(int index) {
        return ((Integer) this.indexLastTime.get(index)).intValue();
    }

    public void setIndexLastTime(int index, int value) {
        if (value == ((Integer) this.indexLastTime.get(index)).intValue())
            return;
        this.indexLastTime.set(index, Integer.valueOf(value));
    }

    public void saveIndexLastTime(int index, int value) {
        if (value == ((Integer) this.indexLastTime.get(index)).intValue())
            return;
        this.indexLastTime.set(index, Integer.valueOf(value));
        saveField("indexLastTime_" + index, this.indexLastTime.get(index));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `serverStart` = '").append(this.serverStart).append("',");
        for (int i = 0; i < this.indexLastTime.size(); i++) {
            sBuilder.append(" `indexLastTime_").append(i).append("` = '").append(this.indexLastTime.get(i)).append("',");
        }
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

