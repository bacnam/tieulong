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
/*     */ public class GuildwarpuppetBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家ID")
/*     */   private long pid;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "guildId", comment = "公会ID")
/*     */   private long guildId;
/*     */   @DataBaseField(type = "int(11)", fieldname = "puppetId", comment = "傀儡ID")
/*     */   private int puppetId;
/*     */   @DataBaseField(type = "int(11)", fieldname = "applyTime", comment = "申请时间")
/*     */   private int applyTime;
/*     */   
/*     */   public GuildwarpuppetBO() {
/*  26 */     this.id = 0L;
/*  27 */     this.pid = 0L;
/*  28 */     this.guildId = 0L;
/*  29 */     this.puppetId = 0;
/*  30 */     this.applyTime = 0;
/*     */   }
/*     */   
/*     */   public GuildwarpuppetBO(ResultSet rs) throws Exception {
/*  34 */     this.id = rs.getLong(1);
/*  35 */     this.pid = rs.getLong(2);
/*  36 */     this.guildId = rs.getLong(3);
/*  37 */     this.puppetId = rs.getInt(4);
/*  38 */     this.applyTime = rs.getInt(5);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<GuildwarpuppetBO> list) throws Exception {
/*  44 */     list.add(new GuildwarpuppetBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  49 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  54 */     return "`id`, `pid`, `guildId`, `puppetId`, `applyTime`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  59 */     return "`guildwarpuppet`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  64 */     StringBuilder strBuf = new StringBuilder();
/*  65 */     strBuf.append("'").append(this.id).append("', ");
/*  66 */     strBuf.append("'").append(this.pid).append("', ");
/*  67 */     strBuf.append("'").append(this.guildId).append("', ");
/*  68 */     strBuf.append("'").append(this.puppetId).append("', ");
/*  69 */     strBuf.append("'").append(this.applyTime).append("', ");
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
/*     */   public long getGuildId() {
/* 105 */     return this.guildId;
/*     */   } public void setGuildId(long guildId) {
/* 107 */     if (guildId == this.guildId)
/*     */       return; 
/* 109 */     this.guildId = guildId;
/*     */   }
/*     */   public void saveGuildId(long guildId) {
/* 112 */     if (guildId == this.guildId)
/*     */       return; 
/* 114 */     this.guildId = guildId;
/* 115 */     saveField("guildId", Long.valueOf(guildId));
/*     */   }
/*     */   
/*     */   public int getPuppetId() {
/* 119 */     return this.puppetId;
/*     */   } public void setPuppetId(int puppetId) {
/* 121 */     if (puppetId == this.puppetId)
/*     */       return; 
/* 123 */     this.puppetId = puppetId;
/*     */   }
/*     */   public void savePuppetId(int puppetId) {
/* 126 */     if (puppetId == this.puppetId)
/*     */       return; 
/* 128 */     this.puppetId = puppetId;
/* 129 */     saveField("puppetId", Integer.valueOf(puppetId));
/*     */   }
/*     */   
/*     */   public int getApplyTime() {
/* 133 */     return this.applyTime;
/*     */   } public void setApplyTime(int applyTime) {
/* 135 */     if (applyTime == this.applyTime)
/*     */       return; 
/* 137 */     this.applyTime = applyTime;
/*     */   }
/*     */   public void saveApplyTime(int applyTime) {
/* 140 */     if (applyTime == this.applyTime)
/*     */       return; 
/* 142 */     this.applyTime = applyTime;
/* 143 */     saveField("applyTime", Integer.valueOf(applyTime));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 150 */     StringBuilder sBuilder = new StringBuilder();
/* 151 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 152 */     sBuilder.append(" `guildId` = '").append(this.guildId).append("',");
/* 153 */     sBuilder.append(" `puppetId` = '").append(this.puppetId).append("',");
/* 154 */     sBuilder.append(" `applyTime` = '").append(this.applyTime).append("',");
/* 155 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 156 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 160 */     String sql = "CREATE TABLE IF NOT EXISTS `guildwarpuppet` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家ID',`guildId` bigint(20) NOT NULL DEFAULT '0' COMMENT '公会ID',`puppetId` int(11) NOT NULL DEFAULT '0' COMMENT '傀儡ID',`applyTime` int(11) NOT NULL DEFAULT '0' COMMENT '申请时间',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='玩家傀儡信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/GuildwarpuppetBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */