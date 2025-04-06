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
/*     */ public class GuildwarapplyBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "guildId", comment = "公会ID")
/*     */   private long guildId;
/*     */   @DataBaseField(type = "int(11)", fieldname = "centerId", comment = "据点id")
/*     */   private int centerId;
/*     */   @DataBaseField(type = "int(11)", fieldname = "applyTime", comment = "申请时间")
/*     */   private int applyTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "winCenterId", comment = "占领据点Id")
/*     */   private int winCenterId;
/*     */   
/*     */   public GuildwarapplyBO() {
/*  26 */     this.id = 0L;
/*  27 */     this.guildId = 0L;
/*  28 */     this.centerId = 0;
/*  29 */     this.applyTime = 0;
/*  30 */     this.winCenterId = 0;
/*     */   }
/*     */   
/*     */   public GuildwarapplyBO(ResultSet rs) throws Exception {
/*  34 */     this.id = rs.getLong(1);
/*  35 */     this.guildId = rs.getLong(2);
/*  36 */     this.centerId = rs.getInt(3);
/*  37 */     this.applyTime = rs.getInt(4);
/*  38 */     this.winCenterId = rs.getInt(5);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<GuildwarapplyBO> list) throws Exception {
/*  44 */     list.add(new GuildwarapplyBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  49 */     return getGuildId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  54 */     return "`id`, `guildId`, `centerId`, `applyTime`, `winCenterId`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  59 */     return "`guildwarapply`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  64 */     StringBuilder strBuf = new StringBuilder();
/*  65 */     strBuf.append("'").append(this.id).append("', ");
/*  66 */     strBuf.append("'").append(this.guildId).append("', ");
/*  67 */     strBuf.append("'").append(this.centerId).append("', ");
/*  68 */     strBuf.append("'").append(this.applyTime).append("', ");
/*  69 */     strBuf.append("'").append(this.winCenterId).append("', ");
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
/*     */   public long getGuildId() {
/*  91 */     return this.guildId;
/*     */   } public void setGuildId(long guildId) {
/*  93 */     if (guildId == this.guildId)
/*     */       return; 
/*  95 */     this.guildId = guildId;
/*     */   }
/*     */   public void saveGuildId(long guildId) {
/*  98 */     if (guildId == this.guildId)
/*     */       return; 
/* 100 */     this.guildId = guildId;
/* 101 */     saveField("guildId", Long.valueOf(guildId));
/*     */   }
/*     */   
/*     */   public int getCenterId() {
/* 105 */     return this.centerId;
/*     */   } public void setCenterId(int centerId) {
/* 107 */     if (centerId == this.centerId)
/*     */       return; 
/* 109 */     this.centerId = centerId;
/*     */   }
/*     */   public void saveCenterId(int centerId) {
/* 112 */     if (centerId == this.centerId)
/*     */       return; 
/* 114 */     this.centerId = centerId;
/* 115 */     saveField("centerId", Integer.valueOf(centerId));
/*     */   }
/*     */   
/*     */   public int getApplyTime() {
/* 119 */     return this.applyTime;
/*     */   } public void setApplyTime(int applyTime) {
/* 121 */     if (applyTime == this.applyTime)
/*     */       return; 
/* 123 */     this.applyTime = applyTime;
/*     */   }
/*     */   public void saveApplyTime(int applyTime) {
/* 126 */     if (applyTime == this.applyTime)
/*     */       return; 
/* 128 */     this.applyTime = applyTime;
/* 129 */     saveField("applyTime", Integer.valueOf(applyTime));
/*     */   }
/*     */   
/*     */   public int getWinCenterId() {
/* 133 */     return this.winCenterId;
/*     */   } public void setWinCenterId(int winCenterId) {
/* 135 */     if (winCenterId == this.winCenterId)
/*     */       return; 
/* 137 */     this.winCenterId = winCenterId;
/*     */   }
/*     */   public void saveWinCenterId(int winCenterId) {
/* 140 */     if (winCenterId == this.winCenterId)
/*     */       return; 
/* 142 */     this.winCenterId = winCenterId;
/* 143 */     saveField("winCenterId", Integer.valueOf(winCenterId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 150 */     StringBuilder sBuilder = new StringBuilder();
/* 151 */     sBuilder.append(" `guildId` = '").append(this.guildId).append("',");
/* 152 */     sBuilder.append(" `centerId` = '").append(this.centerId).append("',");
/* 153 */     sBuilder.append(" `applyTime` = '").append(this.applyTime).append("',");
/* 154 */     sBuilder.append(" `winCenterId` = '").append(this.winCenterId).append("',");
/* 155 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 156 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 160 */     String sql = "CREATE TABLE IF NOT EXISTS `guildwarapply` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`guildId` bigint(20) NOT NULL DEFAULT '0' COMMENT '公会ID',`centerId` int(11) NOT NULL DEFAULT '0' COMMENT '据点id',`applyTime` int(11) NOT NULL DEFAULT '0' COMMENT '申请时间',`winCenterId` int(11) NOT NULL DEFAULT '0' COMMENT '占领据点Id',KEY `guildId` (`guildId`),PRIMARY KEY (`id`)) COMMENT='公会报名信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/GuildwarapplyBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */