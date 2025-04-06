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
/*     */ public class GuildApplyBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "所属玩家ID")
/*     */   private long pid;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "guildId", comment = "所属玩家ID")
/*     */   private long guildId;
/*     */   @DataBaseField(type = "int(11)", fieldname = "applyTime", comment = "申请时间")
/*     */   private int applyTime;
/*     */   
/*     */   public GuildApplyBO() {
/*  24 */     this.id = 0L;
/*  25 */     this.pid = 0L;
/*  26 */     this.guildId = 0L;
/*  27 */     this.applyTime = 0;
/*     */   }
/*     */   
/*     */   public GuildApplyBO(ResultSet rs) throws Exception {
/*  31 */     this.id = rs.getLong(1);
/*  32 */     this.pid = rs.getLong(2);
/*  33 */     this.guildId = rs.getLong(3);
/*  34 */     this.applyTime = rs.getInt(4);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<GuildApplyBO> list) throws Exception {
/*  40 */     list.add(new GuildApplyBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  45 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  50 */     return "`id`, `pid`, `guildId`, `applyTime`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  55 */     return "`guildApply`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  60 */     StringBuilder strBuf = new StringBuilder();
/*  61 */     strBuf.append("'").append(this.id).append("', ");
/*  62 */     strBuf.append("'").append(this.pid).append("', ");
/*  63 */     strBuf.append("'").append(this.guildId).append("', ");
/*  64 */     strBuf.append("'").append(this.applyTime).append("', ");
/*  65 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  66 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  71 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  72 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  77 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/*  82 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/*  86 */     return this.pid;
/*     */   } public void setPid(long pid) {
/*  88 */     if (pid == this.pid)
/*     */       return; 
/*  90 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/*  93 */     if (pid == this.pid)
/*     */       return; 
/*  95 */     this.pid = pid;
/*  96 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public long getGuildId() {
/* 100 */     return this.guildId;
/*     */   } public void setGuildId(long guildId) {
/* 102 */     if (guildId == this.guildId)
/*     */       return; 
/* 104 */     this.guildId = guildId;
/*     */   }
/*     */   public void saveGuildId(long guildId) {
/* 107 */     if (guildId == this.guildId)
/*     */       return; 
/* 109 */     this.guildId = guildId;
/* 110 */     saveField("guildId", Long.valueOf(guildId));
/*     */   }
/*     */   
/*     */   public int getApplyTime() {
/* 114 */     return this.applyTime;
/*     */   } public void setApplyTime(int applyTime) {
/* 116 */     if (applyTime == this.applyTime)
/*     */       return; 
/* 118 */     this.applyTime = applyTime;
/*     */   }
/*     */   public void saveApplyTime(int applyTime) {
/* 121 */     if (applyTime == this.applyTime)
/*     */       return; 
/* 123 */     this.applyTime = applyTime;
/* 124 */     saveField("applyTime", Integer.valueOf(applyTime));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 131 */     StringBuilder sBuilder = new StringBuilder();
/* 132 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 133 */     sBuilder.append(" `guildId` = '").append(this.guildId).append("',");
/* 134 */     sBuilder.append(" `applyTime` = '").append(this.applyTime).append("',");
/* 135 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 136 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 140 */     String sql = "CREATE TABLE IF NOT EXISTS `guildApply` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属玩家ID',`guildId` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属玩家ID',`applyTime` int(11) NOT NULL DEFAULT '0' COMMENT '申请时间',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='公会申请列表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 147 */       ServerConfig.getInitialID() + 1L);
/* 148 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/GuildApplyBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */