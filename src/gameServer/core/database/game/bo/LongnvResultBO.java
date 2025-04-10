package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LongnvResultBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "atkpid", comment = "攻击者ID")
private long atkpid;
@DataBaseField(type = "bigint(20)", fieldname = "defpid", comment = "防御者ID")
private long defpid;
@DataBaseField(type = "int(11)", fieldname = "result", comment = "结果")
private int result;
@DataBaseField(type = "int(11)", fieldname = "fightTime", comment = "战斗时间")
private int fightTime;

public LongnvResultBO() {
this.id = 0L;
this.atkpid = 0L;
this.defpid = 0L;
this.result = 0;
this.fightTime = 0;
}

public LongnvResultBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.atkpid = rs.getLong(2);
this.defpid = rs.getLong(3);
this.result = rs.getInt(4);
this.fightTime = rs.getInt(5);
}

public void getFromResultSet(ResultSet rs, List<LongnvResultBO> list) throws Exception {
list.add(new LongnvResultBO(rs));
}

public long getAsynTaskTag() {
return 0L;
}

public String getItemsName() {
return "`id`, `atkpid`, `defpid`, `result`, `fightTime`";
}

public String getTableName() {
return "`longnvResult`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.atkpid).append("', ");
strBuf.append("'").append(this.defpid).append("', ");
strBuf.append("'").append(this.result).append("', ");
strBuf.append("'").append(this.fightTime).append("', ");
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

public long getAtkpid() {
return this.atkpid;
} public void setAtkpid(long atkpid) {
if (atkpid == this.atkpid)
return; 
this.atkpid = atkpid;
}
public void saveAtkpid(long atkpid) {
if (atkpid == this.atkpid)
return; 
this.atkpid = atkpid;
saveField("atkpid", Long.valueOf(atkpid));
}

public long getDefpid() {
return this.defpid;
} public void setDefpid(long defpid) {
if (defpid == this.defpid)
return; 
this.defpid = defpid;
}
public void saveDefpid(long defpid) {
if (defpid == this.defpid)
return; 
this.defpid = defpid;
saveField("defpid", Long.valueOf(defpid));
}

public int getResult() {
return this.result;
} public void setResult(int result) {
if (result == this.result)
return; 
this.result = result;
}
public void saveResult(int result) {
if (result == this.result)
return; 
this.result = result;
saveField("result", Integer.valueOf(result));
}

public int getFightTime() {
return this.fightTime;
} public void setFightTime(int fightTime) {
if (fightTime == this.fightTime)
return; 
this.fightTime = fightTime;
}
public void saveFightTime(int fightTime) {
if (fightTime == this.fightTime)
return; 
this.fightTime = fightTime;
saveField("fightTime", Integer.valueOf(fightTime));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `atkpid` = '").append(this.atkpid).append("',");
sBuilder.append(" `defpid` = '").append(this.defpid).append("',");
sBuilder.append(" `result` = '").append(this.result).append("',");
sBuilder.append(" `fightTime` = '").append(this.fightTime).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `longnvResult` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`atkpid` bigint(20) NOT NULL DEFAULT '0' COMMENT '攻击者ID',`defpid` bigint(20) NOT NULL DEFAULT '0' COMMENT '防御者ID',`result` int(11) NOT NULL DEFAULT '0' COMMENT '结果',`fightTime` int(11) NOT NULL DEFAULT '0' COMMENT '战斗时间',PRIMARY KEY (`id`)) COMMENT='公会战'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

