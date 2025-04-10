package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UnlockRewardBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "所属玩家ID")
private long pid;
@DataBaseField(type = "varchar(500)", fieldname = "fetchRefId", comment = "已经领取奖励的refId")
private String fetchRefId;

public UnlockRewardBO() {
this.id = 0L;
this.pid = 0L;
this.fetchRefId = "";
}

public UnlockRewardBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.pid = rs.getLong(2);
this.fetchRefId = rs.getString(3);
}

public void getFromResultSet(ResultSet rs, List<UnlockRewardBO> list) throws Exception {
list.add(new UnlockRewardBO(rs));
}

public long getAsynTaskTag() {
return getPid();
}

public String getItemsName() {
return "`id`, `pid`, `fetchRefId`";
}

public String getTableName() {
return "`unlockReward`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.pid).append("', ");
strBuf.append("'").append((this.fetchRefId == null) ? null : this.fetchRefId.replace("'", "''")).append("', ");
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

public String getFetchRefId() {
return this.fetchRefId;
} public void setFetchRefId(String fetchRefId) {
if (fetchRefId.equals(this.fetchRefId))
return; 
this.fetchRefId = fetchRefId;
}
public void saveFetchRefId(String fetchRefId) {
if (fetchRefId.equals(this.fetchRefId))
return; 
this.fetchRefId = fetchRefId;
saveField("fetchRefId", fetchRefId);
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `pid` = '").append(this.pid).append("',");
sBuilder.append(" `fetchRefId` = '").append((this.fetchRefId == null) ? null : this.fetchRefId.replace("'", "''")).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `unlockReward` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属玩家ID',`fetchRefId` varchar(500) NOT NULL DEFAULT '' COMMENT '已经领取奖励的refId',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='试炼关卡相关信息记录'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

