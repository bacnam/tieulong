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
/*     */ public class DailyactiveBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "*玩家ID")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "value", comment = "当前活跃值")
/*     */   private int value;
/*     */   @DataBaseField(type = "int(11)", fieldname = "teamLevel", comment = "当天刷新时的玩家等级")
/*     */   private int teamLevel;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "fetchedTaskIndex", comment = "已经领取奖励的序号")
/*     */   private String fetchedTaskIndex;
/*     */   
/*     */   public DailyactiveBO() {
/*  26 */     this.id = 0L;
/*  27 */     this.pid = 0L;
/*  28 */     this.value = 0;
/*  29 */     this.teamLevel = 0;
/*  30 */     this.fetchedTaskIndex = "";
/*     */   }
/*     */   
/*     */   public DailyactiveBO(ResultSet rs) throws Exception {
/*  34 */     this.id = rs.getLong(1);
/*  35 */     this.pid = rs.getLong(2);
/*  36 */     this.value = rs.getInt(3);
/*  37 */     this.teamLevel = rs.getInt(4);
/*  38 */     this.fetchedTaskIndex = rs.getString(5);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<DailyactiveBO> list) throws Exception {
/*  44 */     list.add(new DailyactiveBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  49 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  54 */     return "`id`, `pid`, `value`, `teamLevel`, `fetchedTaskIndex`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  59 */     return "`dailyactive`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  64 */     StringBuilder strBuf = new StringBuilder();
/*  65 */     strBuf.append("'").append(this.id).append("', ");
/*  66 */     strBuf.append("'").append(this.pid).append("', ");
/*  67 */     strBuf.append("'").append(this.value).append("', ");
/*  68 */     strBuf.append("'").append(this.teamLevel).append("', ");
/*  69 */     strBuf.append("'").append((this.fetchedTaskIndex == null) ? null : this.fetchedTaskIndex.replace("'", "''")).append("', ");
/*  70 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  71 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  76 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  77 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  82 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/*  87 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/*  91 */     return this.pid;
/*     */   } public void setPid(long pid) {
/*  93 */     if (pid == this.pid)
/*     */       return; 
/*  95 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/*  98 */     if (pid == this.pid)
/*     */       return; 
/* 100 */     this.pid = pid;
/* 101 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public int getValue() {
/* 105 */     return this.value;
/*     */   } public void setValue(int value) {
/* 107 */     if (value == this.value)
/*     */       return; 
/* 109 */     this.value = value;
/*     */   }
/*     */   public void saveValue(int value) {
/* 112 */     if (value == this.value)
/*     */       return; 
/* 114 */     this.value = value;
/* 115 */     saveField("value", Integer.valueOf(value));
/*     */   }
/*     */   
/*     */   public int getTeamLevel() {
/* 119 */     return this.teamLevel;
/*     */   } public void setTeamLevel(int teamLevel) {
/* 121 */     if (teamLevel == this.teamLevel)
/*     */       return; 
/* 123 */     this.teamLevel = teamLevel;
/*     */   }
/*     */   public void saveTeamLevel(int teamLevel) {
/* 126 */     if (teamLevel == this.teamLevel)
/*     */       return; 
/* 128 */     this.teamLevel = teamLevel;
/* 129 */     saveField("teamLevel", Integer.valueOf(teamLevel));
/*     */   }
/*     */   
/*     */   public String getFetchedTaskIndex() {
/* 133 */     return this.fetchedTaskIndex;
/*     */   } public void setFetchedTaskIndex(String fetchedTaskIndex) {
/* 135 */     if (fetchedTaskIndex.equals(this.fetchedTaskIndex))
/*     */       return; 
/* 137 */     this.fetchedTaskIndex = fetchedTaskIndex;
/*     */   }
/*     */   public void saveFetchedTaskIndex(String fetchedTaskIndex) {
/* 140 */     if (fetchedTaskIndex.equals(this.fetchedTaskIndex))
/*     */       return; 
/* 142 */     this.fetchedTaskIndex = fetchedTaskIndex;
/* 143 */     saveField("fetchedTaskIndex", fetchedTaskIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 150 */     StringBuilder sBuilder = new StringBuilder();
/* 151 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 152 */     sBuilder.append(" `value` = '").append(this.value).append("',");
/* 153 */     sBuilder.append(" `teamLevel` = '").append(this.teamLevel).append("',");
/* 154 */     sBuilder.append(" `fetchedTaskIndex` = '").append((this.fetchedTaskIndex == null) ? null : this.fetchedTaskIndex.replace("'", "''")).append("',");
/* 155 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 156 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 160 */     String sql = "CREATE TABLE IF NOT EXISTS `dailyactive` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '*玩家ID',`value` int(11) NOT NULL DEFAULT '0' COMMENT '当前活跃值',`teamLevel` int(11) NOT NULL DEFAULT '0' COMMENT '当天刷新时的玩家等级',`fetchedTaskIndex` varchar(500) NOT NULL DEFAULT '' COMMENT '已经领取奖励的序号',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='任务活跃度信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 168 */       ServerConfig.getInitialID() + 1L);
/* 169 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/DailyactiveBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */