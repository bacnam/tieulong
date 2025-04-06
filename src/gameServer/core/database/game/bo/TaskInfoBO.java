/*     */ package core.database.game.bo;
/*     */ 
/*     */ import com.zhonglian.server.common.db.BaseBO;
/*     */ import com.zhonglian.server.common.db.annotation.DataBaseField;
/*     */ import core.server.ServerConfig;
/*     */ import java.sql.ResultSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ public class TaskInfoBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "int(11)", fieldname = "taskId", comment = "任务ID")
/*     */   private int taskId;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "*玩家ID")
/*     */   private long pid;
/*     */   @DataBaseField(type = "varchar(30)", fieldname = "taskName", comment = "任务名称")
/*     */   private String taskName;
/*     */   @DataBaseField(type = "int(11)", fieldname = "completeCount", comment = "完成次数")
/*     */   private int completeCount;
/*     */   @DataBaseField(type = "int(11)", fieldname = "isCompleted", comment = "是否已完成(1是/0否)")
/*     */   private int isCompleted;
/*     */   
/*     */   public TaskInfoBO() {
/*  28 */     this.id = 0L;
/*  29 */     this.taskId = 0;
/*  30 */     this.pid = 0L;
/*  31 */     this.taskName = "";
/*  32 */     this.completeCount = 0;
/*  33 */     this.isCompleted = 0;
/*     */   }
/*     */   
/*     */   public TaskInfoBO(ResultSet rs) throws Exception {
/*  37 */     this.id = rs.getLong(1);
/*  38 */     this.taskId = rs.getInt(2);
/*  39 */     this.pid = rs.getLong(3);
/*  40 */     this.taskName = rs.getString(4);
/*  41 */     this.completeCount = rs.getInt(5);
/*  42 */     this.isCompleted = rs.getInt(6);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<TaskInfoBO> list) throws Exception {
/*  48 */     list.add(new TaskInfoBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  53 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  58 */     return "`id`, `taskId`, `pid`, `taskName`, `completeCount`, `isCompleted`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  63 */     return "`taskInfo`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  68 */     StringBuilder strBuf = new StringBuilder();
/*  69 */     strBuf.append("'").append(this.id).append("', ");
/*  70 */     strBuf.append("'").append(this.taskId).append("', ");
/*  71 */     strBuf.append("'").append(this.pid).append("', ");
/*  72 */     strBuf.append("'").append((this.taskName == null) ? null : this.taskName.replace("'", "''")).append("', ");
/*  73 */     strBuf.append("'").append(this.completeCount).append("', ");
/*  74 */     strBuf.append("'").append(this.isCompleted).append("', ");
/*  75 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  76 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  81 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  82 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  87 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/*  92 */     return this.id;
/*     */   }
/*     */   
/*     */   public int getTaskId() {
/*  96 */     return this.taskId;
/*     */   } public void setTaskId(int taskId) {
/*  98 */     if (taskId == this.taskId)
/*     */       return; 
/* 100 */     this.taskId = taskId;
/*     */   }
/*     */   public void saveTaskId(int taskId) {
/* 103 */     if (taskId == this.taskId)
/*     */       return; 
/* 105 */     this.taskId = taskId;
/* 106 */     saveField("taskId", Integer.valueOf(taskId));
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 110 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 112 */     if (pid == this.pid)
/*     */       return; 
/* 114 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 117 */     if (pid == this.pid)
/*     */       return; 
/* 119 */     this.pid = pid;
/* 120 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public String getTaskName() {
/* 124 */     return this.taskName;
/*     */   } public void setTaskName(String taskName) {
/* 126 */     if (taskName.equals(this.taskName))
/*     */       return; 
/* 128 */     this.taskName = taskName;
/*     */   }
/*     */   public void saveTaskName(String taskName) {
/* 131 */     if (taskName.equals(this.taskName))
/*     */       return; 
/* 133 */     this.taskName = taskName;
/* 134 */     saveField("taskName", taskName);
/*     */   }
/*     */   
/*     */   public int getCompleteCount() {
/* 138 */     return this.completeCount;
/*     */   } public void setCompleteCount(int completeCount) {
/* 140 */     if (completeCount == this.completeCount)
/*     */       return; 
/* 142 */     this.completeCount = completeCount;
/*     */   }
/*     */   public void saveCompleteCount(int completeCount) {
/* 145 */     if (completeCount == this.completeCount)
/*     */       return; 
/* 147 */     this.completeCount = completeCount;
/* 148 */     saveField("completeCount", Integer.valueOf(completeCount));
/*     */   }
/*     */   
/*     */   public int getIsCompleted() {
/* 152 */     return this.isCompleted;
/*     */   } public void setIsCompleted(int isCompleted) {
/* 154 */     if (isCompleted == this.isCompleted)
/*     */       return; 
/* 156 */     this.isCompleted = isCompleted;
/*     */   }
/*     */   public void saveIsCompleted(int isCompleted) {
/* 159 */     if (isCompleted == this.isCompleted)
/*     */       return; 
/* 161 */     this.isCompleted = isCompleted;
/* 162 */     saveField("isCompleted", Integer.valueOf(isCompleted));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 169 */     StringBuilder sBuilder = new StringBuilder();
/* 170 */     sBuilder.append(" `taskId` = '").append(this.taskId).append("',");
/* 171 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 172 */     sBuilder.append(" `taskName` = '").append((this.taskName == null) ? null : this.taskName.replace("'", "''")).append("',");
/* 173 */     sBuilder.append(" `completeCount` = '").append(this.completeCount).append("',");
/* 174 */     sBuilder.append(" `isCompleted` = '").append(this.isCompleted).append("',");
/* 175 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 176 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 180 */     String sql = "CREATE TABLE IF NOT EXISTS `taskInfo` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`taskId` int(11) NOT NULL DEFAULT '0' COMMENT '任务ID',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '*玩家ID',`taskName` varchar(30) NOT NULL DEFAULT '' COMMENT '任务名称',`completeCount` int(11) NOT NULL DEFAULT '0' COMMENT '完成次数',`isCompleted` int(11) NOT NULL DEFAULT '0' COMMENT '是否已完成(1是/0否)',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='任务信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 189 */       ServerConfig.getInitialID() + 1L);
/* 190 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/TaskInfoBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */