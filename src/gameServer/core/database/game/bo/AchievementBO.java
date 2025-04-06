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
/*     */ public class AchievementBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "*玩家ID")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "achieveId", comment = "成就ID")
/*     */   private int achieveId;
/*     */   @DataBaseField(type = "int(11)", fieldname = "completeCount", comment = "完成次数")
/*     */   private int completeCount;
/*     */   @DataBaseField(type = "int(11)", fieldname = "achieveCount", comment = "达成成就数量(系列成就)")
/*     */   private int achieveCount;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "gainPrizeList", comment = "领取列表")
/*     */   private String gainPrizeList;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "argument", comment = "附加参数")
/*     */   private long argument;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "argument2", comment = "附加参数2")
/*     */   private long argument2;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "argument3", comment = "附加参数3")
/*     */   private long argument3;
/*     */   
/*     */   public AchievementBO() {
/*  34 */     this.id = 0L;
/*  35 */     this.pid = 0L;
/*  36 */     this.achieveId = 0;
/*  37 */     this.completeCount = 0;
/*  38 */     this.achieveCount = 0;
/*  39 */     this.gainPrizeList = "";
/*  40 */     this.argument = 0L;
/*  41 */     this.argument2 = 0L;
/*  42 */     this.argument3 = 0L;
/*     */   }
/*     */   
/*     */   public AchievementBO(ResultSet rs) throws Exception {
/*  46 */     this.id = rs.getLong(1);
/*  47 */     this.pid = rs.getLong(2);
/*  48 */     this.achieveId = rs.getInt(3);
/*  49 */     this.completeCount = rs.getInt(4);
/*  50 */     this.achieveCount = rs.getInt(5);
/*  51 */     this.gainPrizeList = rs.getString(6);
/*  52 */     this.argument = rs.getLong(7);
/*  53 */     this.argument2 = rs.getLong(8);
/*  54 */     this.argument3 = rs.getLong(9);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<AchievementBO> list) throws Exception {
/*  60 */     list.add(new AchievementBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  65 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  70 */     return "`id`, `pid`, `achieveId`, `completeCount`, `achieveCount`, `gainPrizeList`, `argument`, `argument2`, `argument3`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  75 */     return "`achievement`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  80 */     StringBuilder strBuf = new StringBuilder();
/*  81 */     strBuf.append("'").append(this.id).append("', ");
/*  82 */     strBuf.append("'").append(this.pid).append("', ");
/*  83 */     strBuf.append("'").append(this.achieveId).append("', ");
/*  84 */     strBuf.append("'").append(this.completeCount).append("', ");
/*  85 */     strBuf.append("'").append(this.achieveCount).append("', ");
/*  86 */     strBuf.append("'").append((this.gainPrizeList == null) ? null : this.gainPrizeList.replace("'", "''")).append("', ");
/*  87 */     strBuf.append("'").append(this.argument).append("', ");
/*  88 */     strBuf.append("'").append(this.argument2).append("', ");
/*  89 */     strBuf.append("'").append(this.argument3).append("', ");
/*  90 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  91 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  96 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  97 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/* 102 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 107 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 111 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 113 */     if (pid == this.pid)
/*     */       return; 
/* 115 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 118 */     if (pid == this.pid)
/*     */       return; 
/* 120 */     this.pid = pid;
/* 121 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public int getAchieveId() {
/* 125 */     return this.achieveId;
/*     */   } public void setAchieveId(int achieveId) {
/* 127 */     if (achieveId == this.achieveId)
/*     */       return; 
/* 129 */     this.achieveId = achieveId;
/*     */   }
/*     */   public void saveAchieveId(int achieveId) {
/* 132 */     if (achieveId == this.achieveId)
/*     */       return; 
/* 134 */     this.achieveId = achieveId;
/* 135 */     saveField("achieveId", Integer.valueOf(achieveId));
/*     */   }
/*     */   
/*     */   public int getCompleteCount() {
/* 139 */     return this.completeCount;
/*     */   } public void setCompleteCount(int completeCount) {
/* 141 */     if (completeCount == this.completeCount)
/*     */       return; 
/* 143 */     this.completeCount = completeCount;
/*     */   }
/*     */   public void saveCompleteCount(int completeCount) {
/* 146 */     if (completeCount == this.completeCount)
/*     */       return; 
/* 148 */     this.completeCount = completeCount;
/* 149 */     saveField("completeCount", Integer.valueOf(completeCount));
/*     */   }
/*     */   
/*     */   public int getAchieveCount() {
/* 153 */     return this.achieveCount;
/*     */   } public void setAchieveCount(int achieveCount) {
/* 155 */     if (achieveCount == this.achieveCount)
/*     */       return; 
/* 157 */     this.achieveCount = achieveCount;
/*     */   }
/*     */   public void saveAchieveCount(int achieveCount) {
/* 160 */     if (achieveCount == this.achieveCount)
/*     */       return; 
/* 162 */     this.achieveCount = achieveCount;
/* 163 */     saveField("achieveCount", Integer.valueOf(achieveCount));
/*     */   }
/*     */   
/*     */   public String getGainPrizeList() {
/* 167 */     return this.gainPrizeList;
/*     */   } public void setGainPrizeList(String gainPrizeList) {
/* 169 */     if (gainPrizeList.equals(this.gainPrizeList))
/*     */       return; 
/* 171 */     this.gainPrizeList = gainPrizeList;
/*     */   }
/*     */   public void saveGainPrizeList(String gainPrizeList) {
/* 174 */     if (gainPrizeList.equals(this.gainPrizeList))
/*     */       return; 
/* 176 */     this.gainPrizeList = gainPrizeList;
/* 177 */     saveField("gainPrizeList", gainPrizeList);
/*     */   }
/*     */   
/*     */   public long getArgument() {
/* 181 */     return this.argument;
/*     */   } public void setArgument(long argument) {
/* 183 */     if (argument == this.argument)
/*     */       return; 
/* 185 */     this.argument = argument;
/*     */   }
/*     */   public void saveArgument(long argument) {
/* 188 */     if (argument == this.argument)
/*     */       return; 
/* 190 */     this.argument = argument;
/* 191 */     saveField("argument", Long.valueOf(argument));
/*     */   }
/*     */   
/*     */   public long getArgument2() {
/* 195 */     return this.argument2;
/*     */   } public void setArgument2(long argument2) {
/* 197 */     if (argument2 == this.argument2)
/*     */       return; 
/* 199 */     this.argument2 = argument2;
/*     */   }
/*     */   public void saveArgument2(long argument2) {
/* 202 */     if (argument2 == this.argument2)
/*     */       return; 
/* 204 */     this.argument2 = argument2;
/* 205 */     saveField("argument2", Long.valueOf(argument2));
/*     */   }
/*     */   
/*     */   public long getArgument3() {
/* 209 */     return this.argument3;
/*     */   } public void setArgument3(long argument3) {
/* 211 */     if (argument3 == this.argument3)
/*     */       return; 
/* 213 */     this.argument3 = argument3;
/*     */   }
/*     */   public void saveArgument3(long argument3) {
/* 216 */     if (argument3 == this.argument3)
/*     */       return; 
/* 218 */     this.argument3 = argument3;
/* 219 */     saveField("argument3", Long.valueOf(argument3));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 226 */     StringBuilder sBuilder = new StringBuilder();
/* 227 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 228 */     sBuilder.append(" `achieveId` = '").append(this.achieveId).append("',");
/* 229 */     sBuilder.append(" `completeCount` = '").append(this.completeCount).append("',");
/* 230 */     sBuilder.append(" `achieveCount` = '").append(this.achieveCount).append("',");
/* 231 */     sBuilder.append(" `gainPrizeList` = '").append((this.gainPrizeList == null) ? null : this.gainPrizeList.replace("'", "''")).append("',");
/* 232 */     sBuilder.append(" `argument` = '").append(this.argument).append("',");
/* 233 */     sBuilder.append(" `argument2` = '").append(this.argument2).append("',");
/* 234 */     sBuilder.append(" `argument3` = '").append(this.argument3).append("',");
/* 235 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 236 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 240 */     String sql = "CREATE TABLE IF NOT EXISTS `achievement` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '*玩家ID',`achieveId` int(11) NOT NULL DEFAULT '0' COMMENT '成就ID',`completeCount` int(11) NOT NULL DEFAULT '0' COMMENT '完成次数',`achieveCount` int(11) NOT NULL DEFAULT '0' COMMENT '达成成就数量(系列成就)',`gainPrizeList` varchar(500) NOT NULL DEFAULT '' COMMENT '领取列表',`argument` bigint(20) NOT NULL DEFAULT '0' COMMENT '附加参数',`argument2` bigint(20) NOT NULL DEFAULT '0' COMMENT '附加参数2',`argument3` bigint(20) NOT NULL DEFAULT '0' COMMENT '附加参数3',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='成就信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 252 */       ServerConfig.getInitialID() + 1L);
/* 253 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/AchievementBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */