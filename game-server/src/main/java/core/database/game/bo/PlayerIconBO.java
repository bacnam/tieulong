package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PlayerIconBO extends BaseBO {
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "id",
        comment = "数据库表唯一键值"
    )
    private long id;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "pid",
        comment = "*所属玩家ID"
    )
    private long pid;
    @DataBaseField(
        type = "int(11)",
        fieldname = "refCard",
        comment = "卡牌ID"
    )
    private int refCard;
    @DataBaseField(
        type = "int(11)",
        fieldname = "type",
        comment = "2-英雄头像,3-觉醒头像"
    )
    private int type;

    public PlayerIconBO() {
        this.id = 0L;
        this.pid = 0L;
        this.refCard = 0;
        this.type = 0;
    }

    public PlayerIconBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.refCard = rs.getInt(3);
        this.type = rs.getInt(4);
    }

    @Override
    public void getFromResultSet(ResultSet rs, List list) throws Exception {
        list.add(new PlayerIconBO(rs));
    }

    @Override
    public long getAsynTaskTag() {
        return this.getPid();
    }

    @Override
    public String getItemsName() {
        return "`id`, `pid`, `refCard`, `type`";
    }

    @Override
    public String getTableName() {
        return "`playerIcon`";
    }

    @Override
    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.refCard).append("', ");
        strBuf.append("'").append(this.type).append("', ");
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

    public int getRefCard() {
        return this.refCard;
    }

    public void setRefCard(int refCard) {
        if (refCard != this.refCard) {
            this.refCard = refCard;
        }
    }

    public void saveRefCard(int refCard) {
        if (refCard != this.refCard) {
            this.refCard = refCard;
            this.saveField("refCard", refCard);
        }
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        if (type != this.type) {
            this.type = type;
        }
    }

    public void saveType(int type) {
        if (type != this.type) {
            this.type = type;
            this.saveField("type", type);
        }
    }

    @Override
    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `refCard` = '").append(this.refCard).append("',");
        sBuilder.append(" `type` = '").append(this.type).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }

    public static String getSql_TableCreate() {
        return "CREATE TABLE IF NOT EXISTS `playerIcon` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '*所属玩家ID',`refCard` int(11) NOT NULL DEFAULT '0' COMMENT '卡牌ID',`type` int(11) NOT NULL DEFAULT '0' COMMENT '2-英雄头像,3-觉醒头像',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='玩家头像表,按表的id字段排序'  DEFAULT CHARSET=utf8 AUTO_INCREMENT="
            + (ServerConfig.getInitialID() + 1L);
    }
}
