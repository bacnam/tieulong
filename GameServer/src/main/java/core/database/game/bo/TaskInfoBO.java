package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TaskInfoBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "int(11)", fieldname = "taskId", comment = "任务ID")
private int taskId;
@DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "*玩家ID")
private long pid;
@DataBaseField(type = "varchar(30)", fieldname = "taskName", comment = "任务名称")
private String taskName;
@DataBaseField(type = "int(11)", fieldname = "completeCount", comment = "完成次数")
private int completeCount;
@DataBaseField(type = "int(11)", fieldname = "isCompleted", comment = "是否已完成(1是/0否)")
private int isCompleted;

public TaskInfoBO() {
this.id = 0L;
this.taskId = 0;
this.pid = 0L;
this.taskName = "";
this.completeCount = 0;
this.isCompleted = 0;
}

public TaskInfoBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.taskId = rs.getInt(2);
this.pid = rs.getLong(3);
this.taskName = rs.getString(4);
this.completeCount = rs.getInt(5);
this.isCompleted = rs.getInt(6);
}

public void getFromResultSet(ResultSet rs, List<TaskInfoBO> list) throws Exception {
list.add(new TaskInfoBO(rs));
}

public long getAsynTaskTag() {
return getPid();
}

public String getItemsName() {
return "`id`, `taskId`, `pid`, `taskName`, `completeCount`, `isCompleted`";
}

public String getTableName() {
return "`taskInfo`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.taskId).append("', ");
strBuf.append("'").append(this.pid).append("', ");
strBuf.append("'").append((this.taskName == null) ? null : this.taskName.replace("'", "''")).append("', ");
strBuf.append("'").append(this.completeCount).append("', ");
strBuf.append("'").append(this.isCompleted).append("', ");
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

public int getTaskId() {
return this.taskId;
} public void setTaskId(int taskId) {
if (taskId == this.taskId)
return; 
this.taskId = taskId;
}
public void saveTaskId(int taskId) {
if (taskId == this.taskId)
return; 
this.taskId = taskId;
saveField("taskId", Integer.valueOf(taskId));
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

public String getTaskName() {
return this.taskName;
} public void setTaskName(String taskName) {
if (taskName.equals(this.taskName))
return; 
this.taskName = taskName;
}
public void saveTaskName(String taskName) {
if (taskName.equals(this.taskName))
return; 
this.taskName = taskName;
saveField("taskName", taskName);
}

public int getCompleteCount() {
return this.completeCount;
} public void setCompleteCount(int completeCount) {
if (completeCount == this.completeCount)
return; 
this.completeCount = completeCount;
}
public void saveCompleteCount(int completeCount) {
if (completeCount == this.completeCount)
return; 
this.completeCount = completeCount;
saveField("completeCount", Integer.valueOf(completeCount));
}

public int getIsCompleted() {
return this.isCompleted;
} public void setIsCompleted(int isCompleted) {
if (isCompleted == this.isCompleted)
return; 
this.isCompleted = isCompleted;
}
public void saveIsCompleted(int isCompleted) {
if (isCompleted == this.isCompleted)
return; 
this.isCompleted = isCompleted;
saveField("isCompleted", Integer.valueOf(isCompleted));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `taskId` = '").append(this.taskId).append("',");
sBuilder.append(" `pid` = '").append(this.pid).append("',");
sBuilder.append(" `taskName` = '").append((this.taskName == null) ? null : this.taskName.replace("'", "''")).append("',");
sBuilder.append(" `completeCount` = '").append(this.completeCount).append("',");
sBuilder.append(" `isCompleted` = '").append(this.isCompleted).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `taskInfo` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`taskId` int(11) NOT NULL DEFAULT '0' COMMENT '任务ID',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '*玩家ID',`taskName` varchar(30) NOT NULL DEFAULT '' COMMENT '任务名称',`completeCount` int(11) NOT NULL DEFAULT '0' COMMENT '完成次数',`isCompleted` int(11) NOT NULL DEFAULT '0' COMMENT '是否已完成(1是/0否)',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='任务信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

