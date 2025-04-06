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
/*     */ public class GuildTaskLogBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
/*     */   private long pid;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "taskId", comment = "任务的sid")
/*     */   private long taskId;
/*     */   @DataBaseField(type = "int(11)", fieldname = "logTime", comment = "日志时间")
/*     */   private int logTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "action", comment = "任务动作")
/*     */   private int action;
/*     */   @DataBaseField(type = "int(11)", fieldname = "itemId", comment = "物品id")
/*     */   private int itemId;
/*     */   @DataBaseField(type = "int(11)", fieldname = "itemCount", comment = "物品数量")
/*     */   private int itemCount;
/*     */   @DataBaseField(type = "tinyint(1)", fieldname = "isDouble", comment = "双倍")
/*     */   private boolean isDouble;
/*     */   
/*     */   public GuildTaskLogBO() {
/*  32 */     this.id = 0L;
/*  33 */     this.pid = 0L;
/*  34 */     this.taskId = 0L;
/*  35 */     this.logTime = 0;
/*  36 */     this.action = 0;
/*  37 */     this.itemId = 0;
/*  38 */     this.itemCount = 0;
/*  39 */     this.isDouble = false;
/*     */   }
/*     */   
/*     */   public GuildTaskLogBO(ResultSet rs) throws Exception {
/*  43 */     this.id = rs.getLong(1);
/*  44 */     this.pid = rs.getLong(2);
/*  45 */     this.taskId = rs.getLong(3);
/*  46 */     this.logTime = rs.getInt(4);
/*  47 */     this.action = rs.getInt(5);
/*  48 */     this.itemId = rs.getInt(6);
/*  49 */     this.itemCount = rs.getInt(7);
/*  50 */     this.isDouble = rs.getBoolean(8);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<GuildTaskLogBO> list) throws Exception {
/*  56 */     list.add(new GuildTaskLogBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  61 */     return getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  66 */     return "`id`, `pid`, `taskId`, `logTime`, `action`, `itemId`, `itemCount`, `isDouble`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  71 */     return "`guildTaskLog`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  76 */     StringBuilder strBuf = new StringBuilder();
/*  77 */     strBuf.append("'").append(this.id).append("', ");
/*  78 */     strBuf.append("'").append(this.pid).append("', ");
/*  79 */     strBuf.append("'").append(this.taskId).append("', ");
/*  80 */     strBuf.append("'").append(this.logTime).append("', ");
/*  81 */     strBuf.append("'").append(this.action).append("', ");
/*  82 */     strBuf.append("'").append(this.itemId).append("', ");
/*  83 */     strBuf.append("'").append(this.itemCount).append("', ");
/*  84 */     strBuf.append("'").append(this.isDouble ? 1 : 0).append("', ");
/*  85 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  86 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  91 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  92 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  97 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 102 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 106 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 108 */     if (pid == this.pid)
/*     */       return; 
/* 110 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 113 */     if (pid == this.pid)
/*     */       return; 
/* 115 */     this.pid = pid;
/* 116 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public long getTaskId() {
/* 120 */     return this.taskId;
/*     */   } public void setTaskId(long taskId) {
/* 122 */     if (taskId == this.taskId)
/*     */       return; 
/* 124 */     this.taskId = taskId;
/*     */   }
/*     */   public void saveTaskId(long taskId) {
/* 127 */     if (taskId == this.taskId)
/*     */       return; 
/* 129 */     this.taskId = taskId;
/* 130 */     saveField("taskId", Long.valueOf(taskId));
/*     */   }
/*     */   
/*     */   public int getLogTime() {
/* 134 */     return this.logTime;
/*     */   } public void setLogTime(int logTime) {
/* 136 */     if (logTime == this.logTime)
/*     */       return; 
/* 138 */     this.logTime = logTime;
/*     */   }
/*     */   public void saveLogTime(int logTime) {
/* 141 */     if (logTime == this.logTime)
/*     */       return; 
/* 143 */     this.logTime = logTime;
/* 144 */     saveField("logTime", Integer.valueOf(logTime));
/*     */   }
/*     */   
/*     */   public int getAction() {
/* 148 */     return this.action;
/*     */   } public void setAction(int action) {
/* 150 */     if (action == this.action)
/*     */       return; 
/* 152 */     this.action = action;
/*     */   }
/*     */   public void saveAction(int action) {
/* 155 */     if (action == this.action)
/*     */       return; 
/* 157 */     this.action = action;
/* 158 */     saveField("action", Integer.valueOf(action));
/*     */   }
/*     */   
/*     */   public int getItemId() {
/* 162 */     return this.itemId;
/*     */   } public void setItemId(int itemId) {
/* 164 */     if (itemId == this.itemId)
/*     */       return; 
/* 166 */     this.itemId = itemId;
/*     */   }
/*     */   public void saveItemId(int itemId) {
/* 169 */     if (itemId == this.itemId)
/*     */       return; 
/* 171 */     this.itemId = itemId;
/* 172 */     saveField("itemId", Integer.valueOf(itemId));
/*     */   }
/*     */   
/*     */   public int getItemCount() {
/* 176 */     return this.itemCount;
/*     */   } public void setItemCount(int itemCount) {
/* 178 */     if (itemCount == this.itemCount)
/*     */       return; 
/* 180 */     this.itemCount = itemCount;
/*     */   }
/*     */   public void saveItemCount(int itemCount) {
/* 183 */     if (itemCount == this.itemCount)
/*     */       return; 
/* 185 */     this.itemCount = itemCount;
/* 186 */     saveField("itemCount", Integer.valueOf(itemCount));
/*     */   }
/*     */   
/*     */   public boolean getIsDouble() {
/* 190 */     return this.isDouble;
/*     */   } public void setIsDouble(boolean isDouble) {
/* 192 */     if (isDouble == this.isDouble)
/*     */       return; 
/* 194 */     this.isDouble = isDouble;
/*     */   }
/*     */   public void saveIsDouble(boolean isDouble) {
/* 197 */     if (isDouble == this.isDouble)
/*     */       return; 
/* 199 */     this.isDouble = isDouble;
/* 200 */     saveField("isDouble", Integer.valueOf(isDouble ? 1 : 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 207 */     StringBuilder sBuilder = new StringBuilder();
/* 208 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 209 */     sBuilder.append(" `taskId` = '").append(this.taskId).append("',");
/* 210 */     sBuilder.append(" `logTime` = '").append(this.logTime).append("',");
/* 211 */     sBuilder.append(" `action` = '").append(this.action).append("',");
/* 212 */     sBuilder.append(" `itemId` = '").append(this.itemId).append("',");
/* 213 */     sBuilder.append(" `itemCount` = '").append(this.itemCount).append("',");
/* 214 */     sBuilder.append(" `isDouble` = '").append(this.isDouble ? 1 : 0).append("',");
/* 215 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 216 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 220 */     String sql = "CREATE TABLE IF NOT EXISTS `guildTaskLog` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`taskId` bigint(20) NOT NULL DEFAULT '0' COMMENT '任务的sid',`logTime` int(11) NOT NULL DEFAULT '0' COMMENT '日志时间',`action` int(11) NOT NULL DEFAULT '0' COMMENT '任务动作',`itemId` int(11) NOT NULL DEFAULT '0' COMMENT '物品id',`itemCount` int(11) NOT NULL DEFAULT '0' COMMENT '物品数量',`isDouble` tinyint(1) NOT NULL DEFAULT '0' COMMENT '双倍',PRIMARY KEY (`id`)) COMMENT='竞技场信息表,记录每个玩家竞技场相关信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 230 */       ServerConfig.getInitialID() + 1L);
/* 231 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/GuildTaskLogBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */