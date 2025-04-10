package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PlayerIconBorderBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "*所属玩家ID")
private long pid;
@DataBaseField(type = "int(11)", fieldname = "refID", comment = "边框ID")
private int refID;

public PlayerIconBorderBO() {
this.id = 0L;
this.pid = 0L;
this.refID = 0;
}

public PlayerIconBorderBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.pid = rs.getLong(2);
this.refID = rs.getInt(3);
}

public void getFromResultSet(ResultSet rs, List<PlayerIconBorderBO> list) throws Exception {
list.add(new PlayerIconBorderBO(rs));
}

public long getAsynTaskTag() {
return getPid();
}

public String getItemsName() {
return "`id`, `pid`, `refID`";
}

public String getTableName() {
return "`playerIconBorder`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.pid).append("', ");
strBuf.append("'").append(this.refID).append("', ");
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

public int getRefID() {
return this.refID;
} public void setRefID(int refID) {
if (refID == this.refID)
return; 
this.refID = refID;
}
public void saveRefID(int refID) {
if (refID == this.refID)
return; 
this.refID = refID;
saveField("refID", Integer.valueOf(refID));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `pid` = '").append(this.pid).append("',");
sBuilder.append(" `refID` = '").append(this.refID).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `playerIconBorder` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '*所属玩家ID',`refID` int(11) NOT NULL DEFAULT '0' COMMENT '边框ID',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='玩家头像边框'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

