package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RechargeResetBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "varchar(100)", fieldname = "goodsid", comment = "商品id")
    private String goodsid;
    @DataBaseField(type = "varchar(500)", fieldname = "resetSign", comment = "重置签名")
    private String resetSign;

    public RechargeResetBO() {
        this.id = 0L;
        this.goodsid = "";
        this.resetSign = "";
    }

    public RechargeResetBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.goodsid = rs.getString(2);
        this.resetSign = rs.getString(3);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `rechargeReset` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`goodsid` varchar(100) NOT NULL DEFAULT '' COMMENT '商品id',`resetSign` varchar(500) NOT NULL DEFAULT '' COMMENT '重置签名',KEY `goodsid` (`goodsid`),PRIMARY KEY (`id`)) COMMENT='玩家大富翁信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<RechargeResetBO> list) throws Exception {
        list.add(new RechargeResetBO(rs));
    }

    public long getAsynTaskTag() {
        return 0L;
    }

    public String getItemsName() {
        return "`id`, `goodsid`, `resetSign`";
    }

    public String getTableName() {
        return "`rechargeReset`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append((this.goodsid == null) ? null : this.goodsid.replace("'", "''")).append("', ");
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

    public String getGoodsid() {
        return this.goodsid;
    }

    public void setGoodsid(String goodsid) {
        if (goodsid.equals(this.goodsid))
            return;
        this.goodsid = goodsid;
    }

    public void saveGoodsid(String goodsid) {
        if (goodsid.equals(this.goodsid))
            return;
        this.goodsid = goodsid;
        saveField("goodsid", goodsid);
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
        sBuilder.append(" `goodsid` = '").append((this.goodsid == null) ? null : this.goodsid.replace("'", "''")).append("',");
        sBuilder.append(" `resetSign` = '").append((this.resetSign == null) ? null : this.resetSign.replace("'", "''")).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

