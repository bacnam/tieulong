package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ItemBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "所属玩家ID")
private long pid;
@DataBaseField(type = "int(11)", fieldname = "itemId", comment = "道具ID")
private int itemId;
@DataBaseField(type = "int(11)", fieldname = "count", comment = "道具数量")
private int count;
@DataBaseField(type = "int(11)", fieldname = "gainTime", comment = "获得时间,单位秒")
private int gainTime;

public ItemBO() {
this.id = 0L;
this.pid = 0L;
this.itemId = 0;
this.count = 0;
this.gainTime = 0;
}

public ItemBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.pid = rs.getLong(2);
this.itemId = rs.getInt(3);
this.count = rs.getInt(4);
this.gainTime = rs.getInt(5);
}

public void getFromResultSet(ResultSet rs, List<ItemBO> list) throws Exception {
list.add(new ItemBO(rs));
}

public long getAsynTaskTag() {
return getPid();
}

public String getItemsName() {
return "`id`, `pid`, `itemId`, `count`, `gainTime`";
}

public String getTableName() {
return "`item`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.pid).append("', ");
strBuf.append("'").append(this.itemId).append("', ");
strBuf.append("'").append(this.count).append("', ");
strBuf.append("'").append(this.gainTime).append("', ");
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

public int getItemId() {
return this.itemId;
} public void setItemId(int itemId) {
if (itemId == this.itemId)
return; 
this.itemId = itemId;
}
public void saveItemId(int itemId) {
if (itemId == this.itemId)
return; 
this.itemId = itemId;
saveField("itemId", Integer.valueOf(itemId));
}

public int getCount() {
return this.count;
} public void setCount(int count) {
if (count == this.count)
return; 
this.count = count;
}
public void saveCount(int count) {
if (count == this.count)
return; 
this.count = count;
saveField("count", Integer.valueOf(count));
}

public int getGainTime() {
return this.gainTime;
} public void setGainTime(int gainTime) {
if (gainTime == this.gainTime)
return; 
this.gainTime = gainTime;
}
public void saveGainTime(int gainTime) {
if (gainTime == this.gainTime)
return; 
this.gainTime = gainTime;
saveField("gainTime", Integer.valueOf(gainTime));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `pid` = '").append(this.pid).append("',");
sBuilder.append(" `itemId` = '").append(this.itemId).append("',");
sBuilder.append(" `count` = '").append(this.count).append("',");
sBuilder.append(" `gainTime` = '").append(this.gainTime).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `item` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属玩家ID',`itemId` int(11) NOT NULL DEFAULT '0' COMMENT '道具ID',`count` int(11) NOT NULL DEFAULT '0' COMMENT '道具数量',`gainTime` int(11) NOT NULL DEFAULT '0' COMMENT '获得时间,单位秒',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='道具表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

