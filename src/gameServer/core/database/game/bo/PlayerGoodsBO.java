package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PlayerGoodsBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家ID")
private long pid;
@DataBaseField(type = "bigint(20)", fieldname = "goods_id", comment = "商品资源ID(一般商品:RefGoodsInfo表ID,限时商品:LimitedGoods表主键,公会商品:GuildGoods表主键)")
private long goods_id;
@DataBaseField(type = "int(11)", fieldname = "store_id", comment = "商店ID")
private int store_id;
@DataBaseField(type = "int(11)", fieldname = "store_type", comment = "商店类型")
private int store_type;
@DataBaseField(type = "varchar(64)", fieldname = "cost_uniform_id", comment = "消费代币UniformId")
private String cost_uniform_id;
@DataBaseField(type = "varchar(64)", fieldname = "price", comment = "商品原价")
private String price;
@DataBaseField(type = "varchar(64)", fieldname = "discount", comment = "折后价格")
private String discount;
@DataBaseField(type = "int(11)", fieldname = "amount", comment = "单次购买数量")
private int amount;
@DataBaseField(type = "int(11)", fieldname = "buy_times", comment = "已购买次数")
private int buy_times;
@DataBaseField(type = "int(11)", fieldname = "total_buy_times", comment = "总购买次数")
private int total_buy_times;
@DataBaseField(type = "tinyint(1)", fieldname = "soldout", comment = "是否已卖出")
private boolean soldout;
@DataBaseField(type = "int(11)", fieldname = "createTime", comment = "创建时间")
private int createTime;
@DataBaseField(type = "int(11)", fieldname = "level", comment = "关卡ID")
private int level;

public PlayerGoodsBO() {
this.id = 0L;
this.pid = 0L;
this.goods_id = 0L;
this.store_id = 0;
this.store_type = 0;
this.cost_uniform_id = "";
this.price = "";
this.discount = "";
this.amount = 0;
this.buy_times = 0;
this.total_buy_times = 0;
this.soldout = false;
this.createTime = 0;
this.level = 0;
}

public PlayerGoodsBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.pid = rs.getLong(2);
this.goods_id = rs.getLong(3);
this.store_id = rs.getInt(4);
this.store_type = rs.getInt(5);
this.cost_uniform_id = rs.getString(6);
this.price = rs.getString(7);
this.discount = rs.getString(8);
this.amount = rs.getInt(9);
this.buy_times = rs.getInt(10);
this.total_buy_times = rs.getInt(11);
this.soldout = rs.getBoolean(12);
this.createTime = rs.getInt(13);
this.level = rs.getInt(14);
}

public void getFromResultSet(ResultSet rs, List<PlayerGoodsBO> list) throws Exception {
list.add(new PlayerGoodsBO(rs));
}

public long getAsynTaskTag() {
return getPid();
}

public String getItemsName() {
return "`id`, `pid`, `goods_id`, `store_id`, `store_type`, `cost_uniform_id`, `price`, `discount`, `amount`, `buy_times`, `total_buy_times`, `soldout`, `createTime`, `level`";
}

public String getTableName() {
return "`player_goods`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.pid).append("', ");
strBuf.append("'").append(this.goods_id).append("', ");
strBuf.append("'").append(this.store_id).append("', ");
strBuf.append("'").append(this.store_type).append("', ");
strBuf.append("'").append((this.cost_uniform_id == null) ? null : this.cost_uniform_id.replace("'", "''")).append("', ");
strBuf.append("'").append((this.price == null) ? null : this.price.replace("'", "''")).append("', ");
strBuf.append("'").append((this.discount == null) ? null : this.discount.replace("'", "''")).append("', ");
strBuf.append("'").append(this.amount).append("', ");
strBuf.append("'").append(this.buy_times).append("', ");
strBuf.append("'").append(this.total_buy_times).append("', ");
strBuf.append("'").append(this.soldout ? 1 : 0).append("', ");
strBuf.append("'").append(this.createTime).append("', ");
strBuf.append("'").append(this.level).append("', ");
strBuf.deleteCharAt(strBuf.length() - 2);
return strBuf.toString();
}

public ArrayList<byte[]> getInsertValueBytes() {
ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
return ret;
}

public void setId(long iID) {
this.id = iID;
}

public long getId() {
return this.id;
}

public long getPid() {
return this.pid;
} public void setPid(long pid) {
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

public long getGoodsId() {
return this.goods_id;
} public void setGoodsId(long goods_id) {
if (goods_id == this.goods_id)
return; 
this.goods_id = goods_id;
}
public void saveGoodsId(long goods_id) {
if (goods_id == this.goods_id)
return; 
this.goods_id = goods_id;
saveField("goods_id", Long.valueOf(goods_id));
}

public int getStoreId() {
return this.store_id;
} public void setStoreId(int store_id) {
if (store_id == this.store_id)
return; 
this.store_id = store_id;
}
public void saveStoreId(int store_id) {
if (store_id == this.store_id)
return; 
this.store_id = store_id;
saveField("store_id", Integer.valueOf(store_id));
}

public int getStoreType() {
return this.store_type;
} public void setStoreType(int store_type) {
if (store_type == this.store_type)
return; 
this.store_type = store_type;
}
public void saveStoreType(int store_type) {
if (store_type == this.store_type)
return; 
this.store_type = store_type;
saveField("store_type", Integer.valueOf(store_type));
}

public String getCostUniformId() {
return this.cost_uniform_id;
} public void setCostUniformId(String cost_uniform_id) {
if (cost_uniform_id.equals(this.cost_uniform_id))
return; 
this.cost_uniform_id = cost_uniform_id;
}
public void saveCostUniformId(String cost_uniform_id) {
if (cost_uniform_id.equals(this.cost_uniform_id))
return; 
this.cost_uniform_id = cost_uniform_id;
saveField("cost_uniform_id", cost_uniform_id);
}

public String getPrice() {
return this.price;
} public void setPrice(String price) {
if (price.equals(this.price))
return; 
this.price = price;
}
public void savePrice(String price) {
if (price.equals(this.price))
return; 
this.price = price;
saveField("price", price);
}

public String getDiscount() {
return this.discount;
} public void setDiscount(String discount) {
if (discount.equals(this.discount))
return; 
this.discount = discount;
}
public void saveDiscount(String discount) {
if (discount.equals(this.discount))
return; 
this.discount = discount;
saveField("discount", discount);
}

public int getAmount() {
return this.amount;
} public void setAmount(int amount) {
if (amount == this.amount)
return; 
this.amount = amount;
}
public void saveAmount(int amount) {
if (amount == this.amount)
return; 
this.amount = amount;
saveField("amount", Integer.valueOf(amount));
}

public int getBuyTimes() {
return this.buy_times;
} public void setBuyTimes(int buy_times) {
if (buy_times == this.buy_times)
return; 
this.buy_times = buy_times;
}
public void saveBuyTimes(int buy_times) {
if (buy_times == this.buy_times)
return; 
this.buy_times = buy_times;
saveField("buy_times", Integer.valueOf(buy_times));
}

public int getTotalBuyTimes() {
return this.total_buy_times;
} public void setTotalBuyTimes(int total_buy_times) {
if (total_buy_times == this.total_buy_times)
return; 
this.total_buy_times = total_buy_times;
}
public void saveTotalBuyTimes(int total_buy_times) {
if (total_buy_times == this.total_buy_times)
return; 
this.total_buy_times = total_buy_times;
saveField("total_buy_times", Integer.valueOf(total_buy_times));
}

public boolean getSoldout() {
return this.soldout;
} public void setSoldout(boolean soldout) {
if (soldout == this.soldout)
return; 
this.soldout = soldout;
}
public void saveSoldout(boolean soldout) {
if (soldout == this.soldout)
return; 
this.soldout = soldout;
saveField("soldout", Integer.valueOf(soldout ? 1 : 0));
}

public int getCreateTime() {
return this.createTime;
} public void setCreateTime(int createTime) {
if (createTime == this.createTime)
return; 
this.createTime = createTime;
}
public void saveCreateTime(int createTime) {
if (createTime == this.createTime)
return; 
this.createTime = createTime;
saveField("createTime", Integer.valueOf(createTime));
}

public int getLevel() {
return this.level;
} public void setLevel(int level) {
if (level == this.level)
return; 
this.level = level;
}
public void saveLevel(int level) {
if (level == this.level)
return; 
this.level = level;
saveField("level", Integer.valueOf(level));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `pid` = '").append(this.pid).append("',");
sBuilder.append(" `goods_id` = '").append(this.goods_id).append("',");
sBuilder.append(" `store_id` = '").append(this.store_id).append("',");
sBuilder.append(" `store_type` = '").append(this.store_type).append("',");
sBuilder.append(" `cost_uniform_id` = '").append((this.cost_uniform_id == null) ? null : this.cost_uniform_id.replace("'", "''")).append("',");
sBuilder.append(" `price` = '").append((this.price == null) ? null : this.price.replace("'", "''")).append("',");
sBuilder.append(" `discount` = '").append((this.discount == null) ? null : this.discount.replace("'", "''")).append("',");
sBuilder.append(" `amount` = '").append(this.amount).append("',");
sBuilder.append(" `buy_times` = '").append(this.buy_times).append("',");
sBuilder.append(" `total_buy_times` = '").append(this.total_buy_times).append("',");
sBuilder.append(" `soldout` = '").append(this.soldout ? 1 : 0).append("',");
sBuilder.append(" `createTime` = '").append(this.createTime).append("',");
sBuilder.append(" `level` = '").append(this.level).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `player_goods` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家ID',`goods_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '商品资源ID(一般商品:RefGoodsInfo表ID,限时商品:LimitedGoods表主键,公会商品:GuildGoods表主键)',`store_id` int(11) NOT NULL DEFAULT '0' COMMENT '商店ID',`store_type` int(11) NOT NULL DEFAULT '0' COMMENT '商店类型',`cost_uniform_id` varchar(64) NOT NULL DEFAULT '' COMMENT '消费代币UniformId',`price` varchar(64) NOT NULL DEFAULT '' COMMENT '商品原价',`discount` varchar(64) NOT NULL DEFAULT '' COMMENT '折后价格',`amount` int(11) NOT NULL DEFAULT '0' COMMENT '单次购买数量',`buy_times` int(11) NOT NULL DEFAULT '0' COMMENT '已购买次数',`total_buy_times` int(11) NOT NULL DEFAULT '0' COMMENT '总购买次数',`soldout` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已卖出',`createTime` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',`level` int(11) NOT NULL DEFAULT '0' COMMENT '关卡ID',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='玩家商品信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

