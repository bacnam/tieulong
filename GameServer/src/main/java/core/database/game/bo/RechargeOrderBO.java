package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RechargeOrderBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家角色id")
    private long pid;
    @DataBaseField(type = "varchar(500)", fieldname = "open_id", comment = "平台服务器上生成的openid")
    private String open_id;
    @DataBaseField(type = "varchar(500)", fieldname = "productid", comment = "商品id")
    private String productid;
    @DataBaseField(type = "int(11)", fieldname = "quantity", comment = "商品id")
    private int quantity;
    @DataBaseField(type = "int(11)", fieldname = "orderTime", comment = "到游戏服务器上时间")
    private int orderTime;
    @DataBaseField(type = "int(11)", fieldname = "deliverTime", comment = "到游戏服务器上时间")
    private int deliverTime;
    @DataBaseField(type = "varchar(100)", fieldname = "cporderid", comment = "cp定单号,php正式的，唯一")
    private String cporderid;
    @DataBaseField(type = "varchar(500)", fieldname = "adfromOrderid", comment = "渠道支付定单号")
    private String adfromOrderid;
    @DataBaseField(type = "varchar(500)", fieldname = "serverID", comment = "服务器ID")
    private String serverID;
    @DataBaseField(type = "varchar(500)", fieldname = "carrier", comment = "运营商")
    private String carrier;
    @DataBaseField(type = "varchar(500)", fieldname = "platform", comment = "平台ios，andriod，越狱")
    private String platform;
    @DataBaseField(type = "varchar(500)", fieldname = "adfrom", comment = "pp，360，qq 主来源1")
    private String adfrom;
    @DataBaseField(type = "varchar(500)", fieldname = "adfrom2", comment = "二级来源")
    private String adfrom2;
    @DataBaseField(type = "varchar(500)", fieldname = "gameid", comment = "梦加内部游戏id")
    private String gameid;
    @DataBaseField(type = "varchar(500)", fieldname = "appID", comment = "appID")
    private String appID;
    @DataBaseField(type = "varchar(500)", fieldname = "status", comment = "订单状态-已付款，已领取，已取消")
    private String status;

    public RechargeOrderBO() {
        this.id = 0L;
        this.pid = 0L;
        this.open_id = "";
        this.productid = "";
        this.quantity = 0;
        this.orderTime = 0;
        this.deliverTime = 0;
        this.cporderid = "";
        this.adfromOrderid = "";
        this.serverID = "";
        this.carrier = "";
        this.platform = "";
        this.adfrom = "";
        this.adfrom2 = "";
        this.gameid = "";
        this.appID = "";
        this.status = "";
    }

    public RechargeOrderBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.open_id = rs.getString(3);
        this.productid = rs.getString(4);
        this.quantity = rs.getInt(5);
        this.orderTime = rs.getInt(6);
        this.deliverTime = rs.getInt(7);
        this.cporderid = rs.getString(8);
        this.adfromOrderid = rs.getString(9);
        this.serverID = rs.getString(10);
        this.carrier = rs.getString(11);
        this.platform = rs.getString(12);
        this.adfrom = rs.getString(13);
        this.adfrom2 = rs.getString(14);
        this.gameid = rs.getString(15);
        this.appID = rs.getString(16);
        this.status = rs.getString(17);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `rechargeOrder` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家角色id',`open_id` varchar(500) NOT NULL DEFAULT '' COMMENT '平台服务器上生成的openid',`productid` varchar(500) NOT NULL DEFAULT '' COMMENT '商品id',`quantity` int(11) NOT NULL DEFAULT '0' COMMENT '商品id',`orderTime` int(11) NOT NULL DEFAULT '0' COMMENT '到游戏服务器上时间',`deliverTime` int(11) NOT NULL DEFAULT '0' COMMENT '到游戏服务器上时间',`cporderid` varchar(100) NOT NULL DEFAULT '' COMMENT 'cp定单号,php正式的，唯一',`adfromOrderid` varchar(500) NOT NULL DEFAULT '' COMMENT '渠道支付定单号',`serverID` varchar(500) NOT NULL DEFAULT '' COMMENT '服务器ID',`carrier` varchar(500) NOT NULL DEFAULT '' COMMENT '运营商',`platform` varchar(500) NOT NULL DEFAULT '' COMMENT '平台ios，andriod，越狱',`adfrom` varchar(500) NOT NULL DEFAULT '' COMMENT 'pp，360，qq 主来源1',`adfrom2` varchar(500) NOT NULL DEFAULT '' COMMENT '二级来源',`gameid` varchar(500) NOT NULL DEFAULT '' COMMENT '梦加内部游戏id',`appID` varchar(500) NOT NULL DEFAULT '' COMMENT 'appID',`status` varchar(500) NOT NULL DEFAULT '' COMMENT '订单状态-已付款，已领取，已取消',KEY `cporderid` (`cporderid`),PRIMARY KEY (`id`)) COMMENT='玩家地宫记录'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<RechargeOrderBO> list) throws Exception {
        list.add(new RechargeOrderBO(rs));
    }

    public long getAsynTaskTag() {
        return 0L;
    }

    public String getItemsName() {
        return "`id`, `pid`, `open_id`, `productid`, `quantity`, `orderTime`, `deliverTime`, `cporderid`, `adfromOrderid`, `serverID`, `carrier`, `platform`, `adfrom`, `adfrom2`, `gameid`, `appID`, `status`";
    }

    public String getTableName() {
        return "`rechargeOrder`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append((this.open_id == null) ? null : this.open_id.replace("'", "''")).append("', ");
        strBuf.append("'").append((this.productid == null) ? null : this.productid.replace("'", "''")).append("', ");
        strBuf.append("'").append(this.quantity).append("', ");
        strBuf.append("'").append(this.orderTime).append("', ");
        strBuf.append("'").append(this.deliverTime).append("', ");
        strBuf.append("'").append((this.cporderid == null) ? null : this.cporderid.replace("'", "''")).append("', ");
        strBuf.append("'").append((this.adfromOrderid == null) ? null : this.adfromOrderid.replace("'", "''")).append("', ");
        strBuf.append("'").append((this.serverID == null) ? null : this.serverID.replace("'", "''")).append("', ");
        strBuf.append("'").append((this.carrier == null) ? null : this.carrier.replace("'", "''")).append("', ");
        strBuf.append("'").append((this.platform == null) ? null : this.platform.replace("'", "''")).append("', ");
        strBuf.append("'").append((this.adfrom == null) ? null : this.adfrom.replace("'", "''")).append("', ");
        strBuf.append("'").append((this.adfrom2 == null) ? null : this.adfrom2.replace("'", "''")).append("', ");
        strBuf.append("'").append((this.gameid == null) ? null : this.gameid.replace("'", "''")).append("', ");
        strBuf.append("'").append((this.appID == null) ? null : this.appID.replace("'", "''")).append("', ");
        strBuf.append("'").append((this.status == null) ? null : this.status.replace("'", "''")).append("', ");
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

    public String getOpenId() {
        return this.open_id;
    }

    public void setOpenId(String open_id) {
        if (open_id.equals(this.open_id))
            return;
        this.open_id = open_id;
    }

    public void saveOpenId(String open_id) {
        if (open_id.equals(this.open_id))
            return;
        this.open_id = open_id;
        saveField("open_id", open_id);
    }

    public String getProductid() {
        return this.productid;
    }

    public void setProductid(String productid) {
        if (productid.equals(this.productid))
            return;
        this.productid = productid;
    }

    public void saveProductid(String productid) {
        if (productid.equals(this.productid))
            return;
        this.productid = productid;
        saveField("productid", productid);
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity == this.quantity)
            return;
        this.quantity = quantity;
    }

    public void saveQuantity(int quantity) {
        if (quantity == this.quantity)
            return;
        this.quantity = quantity;
        saveField("quantity", Integer.valueOf(quantity));
    }

    public int getOrderTime() {
        return this.orderTime;
    }

    public void setOrderTime(int orderTime) {
        if (orderTime == this.orderTime)
            return;
        this.orderTime = orderTime;
    }

    public void saveOrderTime(int orderTime) {
        if (orderTime == this.orderTime)
            return;
        this.orderTime = orderTime;
        saveField("orderTime", Integer.valueOf(orderTime));
    }

    public int getDeliverTime() {
        return this.deliverTime;
    }

    public void setDeliverTime(int deliverTime) {
        if (deliverTime == this.deliverTime)
            return;
        this.deliverTime = deliverTime;
    }

    public void saveDeliverTime(int deliverTime) {
        if (deliverTime == this.deliverTime)
            return;
        this.deliverTime = deliverTime;
        saveField("deliverTime", Integer.valueOf(deliverTime));
    }

    public String getCporderid() {
        return this.cporderid;
    }

    public void setCporderid(String cporderid) {
        if (cporderid.equals(this.cporderid))
            return;
        this.cporderid = cporderid;
    }

    public void saveCporderid(String cporderid) {
        if (cporderid.equals(this.cporderid))
            return;
        this.cporderid = cporderid;
        saveField("cporderid", cporderid);
    }

    public String getAdfromOrderid() {
        return this.adfromOrderid;
    }

    public void setAdfromOrderid(String adfromOrderid) {
        if (adfromOrderid.equals(this.adfromOrderid))
            return;
        this.adfromOrderid = adfromOrderid;
    }

    public void saveAdfromOrderid(String adfromOrderid) {
        if (adfromOrderid.equals(this.adfromOrderid))
            return;
        this.adfromOrderid = adfromOrderid;
        saveField("adfromOrderid", adfromOrderid);
    }

    public String getServerID() {
        return this.serverID;
    }

    public void setServerID(String serverID) {
        if (serverID.equals(this.serverID))
            return;
        this.serverID = serverID;
    }

    public void saveServerID(String serverID) {
        if (serverID.equals(this.serverID))
            return;
        this.serverID = serverID;
        saveField("serverID", serverID);
    }

    public String getCarrier() {
        return this.carrier;
    }

    public void setCarrier(String carrier) {
        if (carrier.equals(this.carrier))
            return;
        this.carrier = carrier;
    }

    public void saveCarrier(String carrier) {
        if (carrier.equals(this.carrier))
            return;
        this.carrier = carrier;
        saveField("carrier", carrier);
    }

    public String getPlatform() {
        return this.platform;
    }

    public void setPlatform(String platform) {
        if (platform.equals(this.platform))
            return;
        this.platform = platform;
    }

    public void savePlatform(String platform) {
        if (platform.equals(this.platform))
            return;
        this.platform = platform;
        saveField("platform", platform);
    }

    public String getAdfrom() {
        return this.adfrom;
    }

    public void setAdfrom(String adfrom) {
        if (adfrom.equals(this.adfrom))
            return;
        this.adfrom = adfrom;
    }

    public void saveAdfrom(String adfrom) {
        if (adfrom.equals(this.adfrom))
            return;
        this.adfrom = adfrom;
        saveField("adfrom", adfrom);
    }

    public String getAdfrom2() {
        return this.adfrom2;
    }

    public void setAdfrom2(String adfrom2) {
        if (adfrom2.equals(this.adfrom2))
            return;
        this.adfrom2 = adfrom2;
    }

    public void saveAdfrom2(String adfrom2) {
        if (adfrom2.equals(this.adfrom2))
            return;
        this.adfrom2 = adfrom2;
        saveField("adfrom2", adfrom2);
    }

    public String getGameid() {
        return this.gameid;
    }

    public void setGameid(String gameid) {
        if (gameid.equals(this.gameid))
            return;
        this.gameid = gameid;
    }

    public void saveGameid(String gameid) {
        if (gameid.equals(this.gameid))
            return;
        this.gameid = gameid;
        saveField("gameid", gameid);
    }

    public String getAppID() {
        return this.appID;
    }

    public void setAppID(String appID) {
        if (appID.equals(this.appID))
            return;
        this.appID = appID;
    }

    public void saveAppID(String appID) {
        if (appID.equals(this.appID))
            return;
        this.appID = appID;
        saveField("appID", appID);
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        if (status.equals(this.status))
            return;
        this.status = status;
    }

    public void saveStatus(String status) {
        if (status.equals(this.status))
            return;
        this.status = status;
        saveField("status", status);
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `open_id` = '").append((this.open_id == null) ? null : this.open_id.replace("'", "''")).append("',");
        sBuilder.append(" `productid` = '").append((this.productid == null) ? null : this.productid.replace("'", "''")).append("',");
        sBuilder.append(" `quantity` = '").append(this.quantity).append("',");
        sBuilder.append(" `orderTime` = '").append(this.orderTime).append("',");
        sBuilder.append(" `deliverTime` = '").append(this.deliverTime).append("',");
        sBuilder.append(" `cporderid` = '").append((this.cporderid == null) ? null : this.cporderid.replace("'", "''")).append("',");
        sBuilder.append(" `adfromOrderid` = '").append((this.adfromOrderid == null) ? null : this.adfromOrderid.replace("'", "''")).append("',");
        sBuilder.append(" `serverID` = '").append((this.serverID == null) ? null : this.serverID.replace("'", "''")).append("',");
        sBuilder.append(" `carrier` = '").append((this.carrier == null) ? null : this.carrier.replace("'", "''")).append("',");
        sBuilder.append(" `platform` = '").append((this.platform == null) ? null : this.platform.replace("'", "''")).append("',");
        sBuilder.append(" `adfrom` = '").append((this.adfrom == null) ? null : this.adfrom.replace("'", "''")).append("',");
        sBuilder.append(" `adfrom2` = '").append((this.adfrom2 == null) ? null : this.adfrom2.replace("'", "''")).append("',");
        sBuilder.append(" `gameid` = '").append((this.gameid == null) ? null : this.gameid.replace("'", "''")).append("',");
        sBuilder.append(" `appID` = '").append((this.appID == null) ? null : this.appID.replace("'", "''")).append("',");
        sBuilder.append(" `status` = '").append((this.status == null) ? null : this.status.replace("'", "''")).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

