package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RankRecordBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "int(11)", fieldname = "type", comment = "排行榜类型")
private int type;
@DataBaseField(type = "bigint(20)", fieldname = "owner", comment = "所属者id")
private long owner;
@DataBaseField(type = "bigint(20)", fieldname = "value", comment = "值")
private long value;
@DataBaseField(type = "int(11)", fieldname = "updateTime", comment = "更新时间")
private int updateTime;
@DataBaseField(type = "bigint(20)", fieldname = "ext1", comment = "扩展字段1")
private long ext1;
@DataBaseField(type = "bigint(20)", fieldname = "ext2", comment = "扩展字段2")
private long ext2;
@DataBaseField(type = "bigint(20)", fieldname = "ext3", comment = "扩展字段3")
private long ext3;
@DataBaseField(type = "varchar(200)", fieldname = "ext4", comment = "扩展字段4")
private String ext4;
@DataBaseField(type = "varchar(200)", fieldname = "ext5", comment = "扩展字段5")
private String ext5;

public RankRecordBO() {
this.id = 0L;
this.type = 0;
this.owner = 0L;
this.value = 0L;
this.updateTime = 0;
this.ext1 = 0L;
this.ext2 = 0L;
this.ext3 = 0L;
this.ext4 = "";
this.ext5 = "";
}

public RankRecordBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.type = rs.getInt(2);
this.owner = rs.getLong(3);
this.value = rs.getLong(4);
this.updateTime = rs.getInt(5);
this.ext1 = rs.getLong(6);
this.ext2 = rs.getLong(7);
this.ext3 = rs.getLong(8);
this.ext4 = rs.getString(9);
this.ext5 = rs.getString(10);
}

public void getFromResultSet(ResultSet rs, List<RankRecordBO> list) throws Exception {
list.add(new RankRecordBO(rs));
}

public long getAsynTaskTag() {
return 0L;
}

public String getItemsName() {
return "`id`, `type`, `owner`, `value`, `updateTime`, `ext1`, `ext2`, `ext3`, `ext4`, `ext5`";
}

public String getTableName() {
return "`rank_record`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.type).append("', ");
strBuf.append("'").append(this.owner).append("', ");
strBuf.append("'").append(this.value).append("', ");
strBuf.append("'").append(this.updateTime).append("', ");
strBuf.append("'").append(this.ext1).append("', ");
strBuf.append("'").append(this.ext2).append("', ");
strBuf.append("'").append(this.ext3).append("', ");
strBuf.append("'").append((this.ext4 == null) ? null : this.ext4.replace("'", "''")).append("', ");
strBuf.append("'").append((this.ext5 == null) ? null : this.ext5.replace("'", "''")).append("', ");
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

public int getType() {
return this.type;
} public void setType(int type) {
if (type == this.type)
return; 
this.type = type;
}
public void saveType(int type) {
if (type == this.type)
return; 
this.type = type;
saveField("type", Integer.valueOf(type));
}

public long getOwner() {
return this.owner;
} public void setOwner(long owner) {
if (owner == this.owner)
return; 
this.owner = owner;
}
public void saveOwner(long owner) {
if (owner == this.owner)
return; 
this.owner = owner;
saveField("owner", Long.valueOf(owner));
}

public long getValue() {
return this.value;
} public void setValue(long value) {
if (value == this.value)
return; 
this.value = value;
}
public void saveValue(long value) {
if (value == this.value)
return; 
this.value = value;
saveField("value", Long.valueOf(value));
}

public int getUpdateTime() {
return this.updateTime;
} public void setUpdateTime(int updateTime) {
if (updateTime == this.updateTime)
return; 
this.updateTime = updateTime;
}
public void saveUpdateTime(int updateTime) {
if (updateTime == this.updateTime)
return; 
this.updateTime = updateTime;
saveField("updateTime", Integer.valueOf(updateTime));
}

public long getExt1() {
return this.ext1;
} public void setExt1(long ext1) {
if (ext1 == this.ext1)
return; 
this.ext1 = ext1;
}
public void saveExt1(long ext1) {
if (ext1 == this.ext1)
return; 
this.ext1 = ext1;
saveField("ext1", Long.valueOf(ext1));
}

public long getExt2() {
return this.ext2;
} public void setExt2(long ext2) {
if (ext2 == this.ext2)
return; 
this.ext2 = ext2;
}
public void saveExt2(long ext2) {
if (ext2 == this.ext2)
return; 
this.ext2 = ext2;
saveField("ext2", Long.valueOf(ext2));
}

public long getExt3() {
return this.ext3;
} public void setExt3(long ext3) {
if (ext3 == this.ext3)
return; 
this.ext3 = ext3;
}
public void saveExt3(long ext3) {
if (ext3 == this.ext3)
return; 
this.ext3 = ext3;
saveField("ext3", Long.valueOf(ext3));
}

public String getExt4() {
return this.ext4;
} public void setExt4(String ext4) {
if (ext4.equals(this.ext4))
return; 
this.ext4 = ext4;
}
public void saveExt4(String ext4) {
if (ext4.equals(this.ext4))
return; 
this.ext4 = ext4;
saveField("ext4", ext4);
}

public String getExt5() {
return this.ext5;
} public void setExt5(String ext5) {
if (ext5.equals(this.ext5))
return; 
this.ext5 = ext5;
}
public void saveExt5(String ext5) {
if (ext5.equals(this.ext5))
return; 
this.ext5 = ext5;
saveField("ext5", ext5);
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `type` = '").append(this.type).append("',");
sBuilder.append(" `owner` = '").append(this.owner).append("',");
sBuilder.append(" `value` = '").append(this.value).append("',");
sBuilder.append(" `updateTime` = '").append(this.updateTime).append("',");
sBuilder.append(" `ext1` = '").append(this.ext1).append("',");
sBuilder.append(" `ext2` = '").append(this.ext2).append("',");
sBuilder.append(" `ext3` = '").append(this.ext3).append("',");
sBuilder.append(" `ext4` = '").append((this.ext4 == null) ? null : this.ext4.replace("'", "''")).append("',");
sBuilder.append(" `ext5` = '").append((this.ext5 == null) ? null : this.ext5.replace("'", "''")).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `rank_record` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`type` int(11) NOT NULL DEFAULT '0' COMMENT '排行榜类型',`owner` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属者id',`value` bigint(20) NOT NULL DEFAULT '0' COMMENT '值',`updateTime` int(11) NOT NULL DEFAULT '0' COMMENT '更新时间',`ext1` bigint(20) NOT NULL DEFAULT '0' COMMENT '扩展字段1',`ext2` bigint(20) NOT NULL DEFAULT '0' COMMENT '扩展字段2',`ext3` bigint(20) NOT NULL DEFAULT '0' COMMENT '扩展字段3',`ext4` varchar(200) NOT NULL DEFAULT '' COMMENT '扩展字段4',`ext5` varchar(200) NOT NULL DEFAULT '' COMMENT '扩展字段5',PRIMARY KEY (`id`)) COMMENT='排行表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

