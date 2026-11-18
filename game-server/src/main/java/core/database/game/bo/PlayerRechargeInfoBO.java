package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PlayerRechargeInfoBO extends BaseBO {
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "id",
        comment = "数据库表唯一键值"
    )
    private long id;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "pid",
        comment = "玩家角色id"
    )
    private long pid;
    @DataBaseField(
        type = "int(11)",
        fieldname = "monthCardRemains",
        comment = "月卡剩余天数"
    )
    private int monthCardRemains;
    @DataBaseField(
        type = "int(11)",
        fieldname = "permantCardRemains",
        comment = "年卡剩余天数"
    )
    private int permantCardRemains;

    public PlayerRechargeInfoBO() {
        this.id = 0L;
        this.pid = 0L;
        this.monthCardRemains = 0;
        this.permantCardRemains = 0;
    }

    public PlayerRechargeInfoBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.monthCardRemains = rs.getInt(3);
        this.permantCardRemains = rs.getInt(4);
    }

    @Override
    public void getFromResultSet(ResultSet rs, List list) throws Exception {
        list.add(new PlayerRechargeInfoBO(rs));
    }

    @Override
    public long getAsynTaskTag() {
        return 0L;
    }

    @Override
    public String getItemsName() {
        return "`id`, `pid`, `monthCardRemains`, `permantCardRemains`";
    }

    @Override
    public String getTableName() {
        return "`playerRechargeInfo`";
    }

    @Override
    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.monthCardRemains).append("', ");
        strBuf.append("'").append(this.permantCardRemains).append("', ");
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

    public int getMonthCardRemains() {
        return this.monthCardRemains;
    }

    public void setMonthCardRemains(int monthCardRemains) {
        if (monthCardRemains != this.monthCardRemains) {
            this.monthCardRemains = monthCardRemains;
        }
    }

    public void saveMonthCardRemains(int monthCardRemains) {
        if (monthCardRemains != this.monthCardRemains) {
            this.monthCardRemains = monthCardRemains;
            this.saveField("monthCardRemains", monthCardRemains);
        }
    }

    public int getPermantCardRemains() {
        return this.permantCardRemains;
    }

    public void setPermantCardRemains(int permantCardRemains) {
        if (permantCardRemains != this.permantCardRemains) {
            this.permantCardRemains = permantCardRemains;
        }
    }

    public void savePermantCardRemains(int permantCardRemains) {
        if (permantCardRemains != this.permantCardRemains) {
            this.permantCardRemains = permantCardRemains;
            this.saveField("permantCardRemains", permantCardRemains);
        }
    }

    @Override
    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `monthCardRemains` = '").append(this.monthCardRemains).append("',");
        sBuilder.append(" `permantCardRemains` = '").append(this.permantCardRemains).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }

    public static String getSql_TableCreate() {
        return "CREATE TABLE IF NOT EXISTS `playerRechargeInfo` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家角色id',`monthCardRemains` int(11) NOT NULL DEFAULT '0' COMMENT '月卡剩余天数',`permantCardRemains` int(11) NOT NULL DEFAULT '0' COMMENT '年卡剩余天数',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='充值信息汇总'  DEFAULT CHARSET=utf8 AUTO_INCREMENT="
            + (ServerConfig.getInitialID() + 1L);
    }
}
