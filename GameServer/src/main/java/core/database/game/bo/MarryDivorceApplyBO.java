package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MarryDivorceApplyBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
private long pid;
@DataBaseField(type = "bigint(20)", fieldname = "from_pid", comment = "来自玩家id")
private long from_pid;
@DataBaseField(type = "int(11)", fieldname = "apply_time", comment = "申请时间")
private int apply_time;

public MarryDivorceApplyBO() {
this.id = 0L;
this.pid = 0L;
this.from_pid = 0L;
this.apply_time = 0;
}

public MarryDivorceApplyBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.pid = rs.getLong(2);
this.from_pid = rs.getLong(3);
this.apply_time = rs.getInt(4);
}

public void getFromResultSet(ResultSet rs, List<MarryDivorceApplyBO> list) throws Exception {
list.add(new MarryDivorceApplyBO(rs));
}

public long getAsynTaskTag() {
return getId();
}

public String getItemsName() {
return "`id`, `pid`, `from_pid`, `apply_time`";
}

public String getTableName() {
return "`marry_divorce_apply`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.pid).append("', ");
strBuf.append("'").append(this.from_pid).append("', ");
strBuf.append("'").append(this.apply_time).append("', ");
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

public long getFromPid() {
return this.from_pid;
} public void setFromPid(long from_pid) {
if (from_pid == this.from_pid)
return; 
this.from_pid = from_pid;
}
public void saveFromPid(long from_pid) {
if (from_pid == this.from_pid)
return; 
this.from_pid = from_pid;
saveField("from_pid", Long.valueOf(from_pid));
}

public int getApplyTime() {
return this.apply_time;
} public void setApplyTime(int apply_time) {
if (apply_time == this.apply_time)
return; 
this.apply_time = apply_time;
}
public void saveApplyTime(int apply_time) {
if (apply_time == this.apply_time)
return; 
this.apply_time = apply_time;
saveField("apply_time", Integer.valueOf(apply_time));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `pid` = '").append(this.pid).append("',");
sBuilder.append(" `from_pid` = '").append(this.from_pid).append("',");
sBuilder.append(" `apply_time` = '").append(this.apply_time).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `marry_divorce_apply` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`from_pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '来自玩家id',`apply_time` int(11) NOT NULL DEFAULT '0' COMMENT '申请时间',PRIMARY KEY (`id`)) COMMENT='离婚申请'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

