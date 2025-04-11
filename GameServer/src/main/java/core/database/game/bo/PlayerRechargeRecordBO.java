package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PlayerRechargeRecordBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家角色id")
    private long pid;
    @DataBaseField(type = "varchar(500)", fieldname = "goodsID", comment = "商品id")
    private String goodsID;
    @DataBaseField(type = "int(11)", fieldname = "lastBuyTime", comment = "最近一次购买时间")
    private int lastBuyTime;
    @DataBaseField(type = "int(11)", fieldname = "buyCount", comment = "购买次数")
    private int buyCount;
    @DataBaseField(type = "varchar(500)", fieldname = "resetSign", comment = "重置标识")
    private String resetSign;

    public PlayerRechargeRecordBO() {
        this.id = 0L;
        this.pid = 0L;
        this.goodsID = "";
        this.lastBuyTime = 0;
        this.buyCount = 0;
        this.resetSign = "";
    }

    public PlayerRechargeRecordBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.goodsID = rs.getString(3);
        this.lastBuyTime = rs.getInt(4);
        this.buyCount = rs.getInt(5);
        this.resetSign = rs.getString(6);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `playerRechargeRecord` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家角色id',`goodsID` varchar(500) NOT NULL DEFAULT '' COMMENT '商品id',`lastBuyTime` int(11) NOT NULL DEFAULT '0' COMMENT '最近一次购买时间',`buyCount` int(11) NOT NULL DEFAULT '0' COMMENT '购买次数',`resetSign` varchar(500) NOT NULL DEFAULT '' COMMENT '重置标识',PRIMARY KEY (`id`)) COMMENT='玩家地宫记录'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<PlayerRechargeRecordBO> list) throws Exception {
        list.add(new PlayerRechargeRecordBO(rs));
    }

    public long getAsynTaskTag() {
        return 0L;
    }

    public String getItemsName() {
        return "`id`, `pid`, `goodsID`, `lastBuyTime`, `buyCount`, `resetSign`";
    }

    public String getTableName() {
        return "`playerRechargeRecord`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append((this.goodsID == null) ? null : this.goodsID.replace("'", "''")).append("', ");
        strBuf.append("'").append(this.lastBuyTime).append("', ");
        strBuf.append("'").append(this.buyCount).append("', ");
        strBuf.append("'").append((this.resetSign == null) ? null : this.resetSign.replace("'", "''")).append("', ");
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

    public String getGoodsID() {
        return this.goodsID;
    }

    public void setGoodsID(String goodsID) {
        if (goodsID.equals(this.goodsID))
            return;
        this.goodsID = goodsID;
    }

    public void saveGoodsID(String goodsID) {
        if (goodsID.equals(this.goodsID))
            return;
        this.goodsID = goodsID;
        saveField("goodsID", goodsID);
    }

    public int getLastBuyTime() {
        return this.lastBuyTime;
    }

    public void setLastBuyTime(int lastBuyTime) {
        if (lastBuyTime == this.lastBuyTime)
            return;
        this.lastBuyTime = lastBuyTime;
    }

    public void saveLastBuyTime(int lastBuyTime) {
        if (lastBuyTime == this.lastBuyTime)
            return;
        this.lastBuyTime = lastBuyTime;
        saveField("lastBuyTime", Integer.valueOf(lastBuyTime));
    }

    public int getBuyCount() {
        return this.buyCount;
    }

    public void setBuyCount(int buyCount) {
        if (buyCount == this.buyCount)
            return;
        this.buyCount = buyCount;
    }

    public void saveBuyCount(int buyCount) {
        if (buyCount == this.buyCount)
            return;
        this.buyCount = buyCount;
        saveField("buyCount", Integer.valueOf(buyCount));
    }

    public String getResetSign() {
        return this.resetSign;
    }

    public void setResetSign(String resetSign) {
        if (resetSign.equals(this.resetSign))
            return;
        this.resetSign = resetSign;
    }

    public void saveResetSign(String resetSign) {
        if (resetSign.equals(this.resetSign))
            return;
        this.resetSign = resetSign;
        saveField("resetSign", resetSign);
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `goodsID` = '").append((this.goodsID == null) ? null : this.goodsID.replace("'", "''")).append("',");
        sBuilder.append(" `lastBuyTime` = '").append(this.lastBuyTime).append("',");
        sBuilder.append(" `buyCount` = '").append(this.buyCount).append("',");
        sBuilder.append(" `resetSign` = '").append((this.resetSign == null) ? null : this.resetSign.replace("'", "''")).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

