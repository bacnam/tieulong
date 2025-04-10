package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ConfRewardBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "int(11)", fieldname = "rewardID", comment = "礼包ID")
private int rewardID;
@DataBaseField(type = "int(11)", fieldname = "price", comment = "礼包价格")
private int price;
@DataBaseField(type = "varchar(64)", fieldname = "iconID", comment = "礼包图标")
private String iconID;
@DataBaseField(type = "varchar(64)", fieldname = "name", comment = "礼包名称")
private String name;
@DataBaseField(type = "varchar(128)", fieldname = "itemID", comment = "物品ID")
private String itemID;
@DataBaseField(type = "varchar(128)", fieldname = "itemCount", comment = "物品数量")
private String itemCount;
@DataBaseField(type = "varchar(512)", fieldname = "rewardDescribe", comment = "礼包描述")
private String rewardDescribe;
@DataBaseField(type = "int(11)", fieldname = "createTime", comment = "创建时间")
private int createTime;

public ConfRewardBO() {
this.id = 0L;
this.rewardID = 0;
this.price = 0;
this.iconID = "";
this.name = "";
this.itemID = "";
this.itemCount = "";
this.rewardDescribe = "";
this.createTime = 0;
}

public ConfRewardBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.rewardID = rs.getInt(2);
this.price = rs.getInt(3);
this.iconID = rs.getString(4);
this.name = rs.getString(5);
this.itemID = rs.getString(6);
this.itemCount = rs.getString(7);
this.rewardDescribe = rs.getString(8);
this.createTime = rs.getInt(9);
}

public void getFromResultSet(ResultSet rs, List<ConfRewardBO> list) throws Exception {
list.add(new ConfRewardBO(rs));
}

public long getAsynTaskTag() {
return 0L;
}

public String getItemsName() {
return "`id`, `rewardID`, `price`, `iconID`, `name`, `itemID`, `itemCount`, `rewardDescribe`, `createTime`";
}

public String getTableName() {
return "`confReward`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.rewardID).append("', ");
strBuf.append("'").append(this.price).append("', ");
strBuf.append("'").append((this.iconID == null) ? null : this.iconID.replace("'", "''")).append("', ");
strBuf.append("'").append((this.name == null) ? null : this.name.replace("'", "''")).append("', ");
strBuf.append("'").append((this.itemID == null) ? null : this.itemID.replace("'", "''")).append("', ");
strBuf.append("'").append((this.itemCount == null) ? null : this.itemCount.replace("'", "''")).append("', ");
strBuf.append("'").append((this.rewardDescribe == null) ? null : this.rewardDescribe.replace("'", "''")).append("', ");
strBuf.append("'").append(this.createTime).append("', ");
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

public int getRewardID() {
return this.rewardID;
} public void setRewardID(int rewardID) {
if (rewardID == this.rewardID)
return; 
this.rewardID = rewardID;
}
public void saveRewardID(int rewardID) {
if (rewardID == this.rewardID)
return; 
this.rewardID = rewardID;
saveField("rewardID", Integer.valueOf(rewardID));
}

public int getPrice() {
return this.price;
} public void setPrice(int price) {
if (price == this.price)
return; 
this.price = price;
}
public void savePrice(int price) {
if (price == this.price)
return; 
this.price = price;
saveField("price", Integer.valueOf(price));
}

public String getIconID() {
return this.iconID;
} public void setIconID(String iconID) {
if (iconID.equals(this.iconID))
return; 
this.iconID = iconID;
}
public void saveIconID(String iconID) {
if (iconID.equals(this.iconID))
return; 
this.iconID = iconID;
saveField("iconID", iconID);
}

public String getName() {
return this.name;
} public void setName(String name) {
if (name.equals(this.name))
return; 
this.name = name;
}
public void saveName(String name) {
if (name.equals(this.name))
return; 
this.name = name;
saveField("name", name);
}

public String getItemID() {
return this.itemID;
} public void setItemID(String itemID) {
if (itemID.equals(this.itemID))
return; 
this.itemID = itemID;
}
public void saveItemID(String itemID) {
if (itemID.equals(this.itemID))
return; 
this.itemID = itemID;
saveField("itemID", itemID);
}

public String getItemCount() {
return this.itemCount;
} public void setItemCount(String itemCount) {
if (itemCount.equals(this.itemCount))
return; 
this.itemCount = itemCount;
}
public void saveItemCount(String itemCount) {
if (itemCount.equals(this.itemCount))
return; 
this.itemCount = itemCount;
saveField("itemCount", itemCount);
}

public String getRewardDescribe() {
return this.rewardDescribe;
} public void setRewardDescribe(String rewardDescribe) {
if (rewardDescribe.equals(this.rewardDescribe))
return; 
this.rewardDescribe = rewardDescribe;
}
public void saveRewardDescribe(String rewardDescribe) {
if (rewardDescribe.equals(this.rewardDescribe))
return; 
this.rewardDescribe = rewardDescribe;
saveField("rewardDescribe", rewardDescribe);
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

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `rewardID` = '").append(this.rewardID).append("',");
sBuilder.append(" `price` = '").append(this.price).append("',");
sBuilder.append(" `iconID` = '").append((this.iconID == null) ? null : this.iconID.replace("'", "''")).append("',");
sBuilder.append(" `name` = '").append((this.name == null) ? null : this.name.replace("'", "''")).append("',");
sBuilder.append(" `itemID` = '").append((this.itemID == null) ? null : this.itemID.replace("'", "''")).append("',");
sBuilder.append(" `itemCount` = '").append((this.itemCount == null) ? null : this.itemCount.replace("'", "''")).append("',");
sBuilder.append(" `rewardDescribe` = '").append((this.rewardDescribe == null) ? null : this.rewardDescribe.replace("'", "''")).append("',");
sBuilder.append(" `createTime` = '").append(this.createTime).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `confReward` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`rewardID` int(11) NOT NULL DEFAULT '0' COMMENT '礼包ID',`price` int(11) NOT NULL DEFAULT '0' COMMENT '礼包价格',`iconID` varchar(64) NOT NULL DEFAULT '' COMMENT '礼包图标',`name` varchar(64) NOT NULL DEFAULT '' COMMENT '礼包名称',`itemID` varchar(128) NOT NULL DEFAULT '' COMMENT '物品ID',`itemCount` varchar(128) NOT NULL DEFAULT '' COMMENT '物品数量',`rewardDescribe` varchar(512) NOT NULL DEFAULT '' COMMENT '礼包描述',`createTime` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',PRIMARY KEY (`id`)) COMMENT='后台配置礼包'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

