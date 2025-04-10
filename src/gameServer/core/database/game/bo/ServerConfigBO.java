package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ServerConfigBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "varchar(500)", fieldname = "key", comment = "键")
private String key;
@DataBaseField(type = "varchar(500)", fieldname = "value", comment = "值")
private String value;

public ServerConfigBO() {
this.id = 0L;
this.key = "";
this.value = "";
}

public ServerConfigBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.key = rs.getString(2);
this.value = rs.getString(3);
}

public void getFromResultSet(ResultSet rs, List<ServerConfigBO> list) throws Exception {
list.add(new ServerConfigBO(rs));
}

public long getAsynTaskTag() {
return 0L;
}

public String getItemsName() {
return "`id`, `key`, `value`";
}

public String getTableName() {
return "`serverConfig`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append((this.key == null) ? null : this.key.replace("'", "''")).append("', ");
strBuf.append("'").append((this.value == null) ? null : this.value.replace("'", "''")).append("', ");
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

public String getKey() {
return this.key;
} public void setKey(String key) {
if (key.equals(this.key))
return; 
this.key = key;
}
public void saveKey(String key) {
if (key.equals(this.key))
return; 
this.key = key;
saveField("key", key);
}

public String getValue() {
return this.value;
} public void setValue(String value) {
if (value.equals(this.value))
return; 
this.value = value;
}
public void saveValue(String value) {
if (value.equals(this.value))
return; 
this.value = value;
saveField("value", value);
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `key` = '").append((this.key == null) ? null : this.key.replace("'", "''")).append("',");
sBuilder.append(" `value` = '").append((this.value == null) ? null : this.value.replace("'", "''")).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `serverConfig` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`key` varchar(500) NOT NULL DEFAULT '' COMMENT '键',`value` varchar(500) NOT NULL DEFAULT '' COMMENT '值',PRIMARY KEY (`id`)) COMMENT='服务器动态配置'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

