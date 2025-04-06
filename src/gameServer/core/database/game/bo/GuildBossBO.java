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
/*     */ public class GuildBossBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "guildId", comment = "帮派Id")
/*     */   private long guildId;
/*     */   @DataBaseField(type = "int(11)", fieldname = "bossId", comment = "Boss关卡ID(GuildBoss静态表ID)")
/*     */   private int bossId;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "bossHp", comment = "Boss当前生命(复活时刷新)")
/*     */   private long bossHp;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "bossMaxHp", comment = "Boss最大生命(复活时刷新)")
/*     */   private long bossMaxHp;
/*     */   @DataBaseField(type = "tinyint(1)", fieldname = "isDead", comment = "Boss是否已死亡")
/*     */   private boolean isDead;
/*     */   @DataBaseField(type = "tinyint(1)", fieldname = "isOpen", comment = "Boss是否已开启")
/*     */   private boolean isOpen;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "deadTime", comment = "Boss死亡时间")
/*     */   private long deadTime;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "reviveTime", comment = "Boss复活时间")
/*     */   private long reviveTime;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "lastKillCid", comment = "上次击杀者pid")
/*     */   private long lastKillCid;
/*     */   
/*     */   public GuildBossBO() {
/*  36 */     this.id = 0L;
/*  37 */     this.guildId = 0L;
/*  38 */     this.bossId = 0;
/*  39 */     this.bossHp = 0L;
/*  40 */     this.bossMaxHp = 0L;
/*  41 */     this.isDead = false;
/*  42 */     this.isOpen = false;
/*  43 */     this.deadTime = 0L;
/*  44 */     this.reviveTime = 0L;
/*  45 */     this.lastKillCid = 0L;
/*     */   }
/*     */   
/*     */   public GuildBossBO(ResultSet rs) throws Exception {
/*  49 */     this.id = rs.getLong(1);
/*  50 */     this.guildId = rs.getLong(2);
/*  51 */     this.bossId = rs.getInt(3);
/*  52 */     this.bossHp = rs.getLong(4);
/*  53 */     this.bossMaxHp = rs.getLong(5);
/*  54 */     this.isDead = rs.getBoolean(6);
/*  55 */     this.isOpen = rs.getBoolean(7);
/*  56 */     this.deadTime = rs.getLong(8);
/*  57 */     this.reviveTime = rs.getLong(9);
/*  58 */     this.lastKillCid = rs.getLong(10);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<GuildBossBO> list) throws Exception {
/*  64 */     list.add(new GuildBossBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  69 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  74 */     return "`id`, `guildId`, `bossId`, `bossHp`, `bossMaxHp`, `isDead`, `isOpen`, `deadTime`, `reviveTime`, `lastKillCid`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  79 */     return "`guildBoss`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  84 */     StringBuilder strBuf = new StringBuilder();
/*  85 */     strBuf.append("'").append(this.id).append("', ");
/*  86 */     strBuf.append("'").append(this.guildId).append("', ");
/*  87 */     strBuf.append("'").append(this.bossId).append("', ");
/*  88 */     strBuf.append("'").append(this.bossHp).append("', ");
/*  89 */     strBuf.append("'").append(this.bossMaxHp).append("', ");
/*  90 */     strBuf.append("'").append(this.isDead ? 1 : 0).append("', ");
/*  91 */     strBuf.append("'").append(this.isOpen ? 1 : 0).append("', ");
/*  92 */     strBuf.append("'").append(this.deadTime).append("', ");
/*  93 */     strBuf.append("'").append(this.reviveTime).append("', ");
/*  94 */     strBuf.append("'").append(this.lastKillCid).append("', ");
/*  95 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  96 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/* 101 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/* 102 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/* 107 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 112 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getGuildId() {
/* 116 */     return this.guildId;
/*     */   } public void setGuildId(long guildId) {
/* 118 */     if (guildId == this.guildId)
/*     */       return; 
/* 120 */     this.guildId = guildId;
/*     */   }
/*     */   public void saveGuildId(long guildId) {
/* 123 */     if (guildId == this.guildId)
/*     */       return; 
/* 125 */     this.guildId = guildId;
/* 126 */     saveField("guildId", Long.valueOf(guildId));
/*     */   }
/*     */   
/*     */   public int getBossId() {
/* 130 */     return this.bossId;
/*     */   } public void setBossId(int bossId) {
/* 132 */     if (bossId == this.bossId)
/*     */       return; 
/* 134 */     this.bossId = bossId;
/*     */   }
/*     */   public void saveBossId(int bossId) {
/* 137 */     if (bossId == this.bossId)
/*     */       return; 
/* 139 */     this.bossId = bossId;
/* 140 */     saveField("bossId", Integer.valueOf(bossId));
/*     */   }
/*     */   
/*     */   public long getBossHp() {
/* 144 */     return this.bossHp;
/*     */   } public void setBossHp(long bossHp) {
/* 146 */     if (bossHp == this.bossHp)
/*     */       return; 
/* 148 */     this.bossHp = bossHp;
/*     */   }
/*     */   public void saveBossHp(long bossHp) {
/* 151 */     if (bossHp == this.bossHp)
/*     */       return; 
/* 153 */     this.bossHp = bossHp;
/* 154 */     saveField("bossHp", Long.valueOf(bossHp));
/*     */   }
/*     */   
/*     */   public long getBossMaxHp() {
/* 158 */     return this.bossMaxHp;
/*     */   } public void setBossMaxHp(long bossMaxHp) {
/* 160 */     if (bossMaxHp == this.bossMaxHp)
/*     */       return; 
/* 162 */     this.bossMaxHp = bossMaxHp;
/*     */   }
/*     */   public void saveBossMaxHp(long bossMaxHp) {
/* 165 */     if (bossMaxHp == this.bossMaxHp)
/*     */       return; 
/* 167 */     this.bossMaxHp = bossMaxHp;
/* 168 */     saveField("bossMaxHp", Long.valueOf(bossMaxHp));
/*     */   }
/*     */   
/*     */   public boolean getIsDead() {
/* 172 */     return this.isDead;
/*     */   } public void setIsDead(boolean isDead) {
/* 174 */     if (isDead == this.isDead)
/*     */       return; 
/* 176 */     this.isDead = isDead;
/*     */   }
/*     */   public void saveIsDead(boolean isDead) {
/* 179 */     if (isDead == this.isDead)
/*     */       return; 
/* 181 */     this.isDead = isDead;
/* 182 */     saveField("isDead", Integer.valueOf(isDead ? 1 : 0));
/*     */   }
/*     */   
/*     */   public boolean getIsOpen() {
/* 186 */     return this.isOpen;
/*     */   } public void setIsOpen(boolean isOpen) {
/* 188 */     if (isOpen == this.isOpen)
/*     */       return; 
/* 190 */     this.isOpen = isOpen;
/*     */   }
/*     */   public void saveIsOpen(boolean isOpen) {
/* 193 */     if (isOpen == this.isOpen)
/*     */       return; 
/* 195 */     this.isOpen = isOpen;
/* 196 */     saveField("isOpen", Integer.valueOf(isOpen ? 1 : 0));
/*     */   }
/*     */   
/*     */   public long getDeadTime() {
/* 200 */     return this.deadTime;
/*     */   } public void setDeadTime(long deadTime) {
/* 202 */     if (deadTime == this.deadTime)
/*     */       return; 
/* 204 */     this.deadTime = deadTime;
/*     */   }
/*     */   public void saveDeadTime(long deadTime) {
/* 207 */     if (deadTime == this.deadTime)
/*     */       return; 
/* 209 */     this.deadTime = deadTime;
/* 210 */     saveField("deadTime", Long.valueOf(deadTime));
/*     */   }
/*     */   
/*     */   public long getReviveTime() {
/* 214 */     return this.reviveTime;
/*     */   } public void setReviveTime(long reviveTime) {
/* 216 */     if (reviveTime == this.reviveTime)
/*     */       return; 
/* 218 */     this.reviveTime = reviveTime;
/*     */   }
/*     */   public void saveReviveTime(long reviveTime) {
/* 221 */     if (reviveTime == this.reviveTime)
/*     */       return; 
/* 223 */     this.reviveTime = reviveTime;
/* 224 */     saveField("reviveTime", Long.valueOf(reviveTime));
/*     */   }
/*     */   
/*     */   public long getLastKillCid() {
/* 228 */     return this.lastKillCid;
/*     */   } public void setLastKillCid(long lastKillCid) {
/* 230 */     if (lastKillCid == this.lastKillCid)
/*     */       return; 
/* 232 */     this.lastKillCid = lastKillCid;
/*     */   }
/*     */   public void saveLastKillCid(long lastKillCid) {
/* 235 */     if (lastKillCid == this.lastKillCid)
/*     */       return; 
/* 237 */     this.lastKillCid = lastKillCid;
/* 238 */     saveField("lastKillCid", Long.valueOf(lastKillCid));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 245 */     StringBuilder sBuilder = new StringBuilder();
/* 246 */     sBuilder.append(" `guildId` = '").append(this.guildId).append("',");
/* 247 */     sBuilder.append(" `bossId` = '").append(this.bossId).append("',");
/* 248 */     sBuilder.append(" `bossHp` = '").append(this.bossHp).append("',");
/* 249 */     sBuilder.append(" `bossMaxHp` = '").append(this.bossMaxHp).append("',");
/* 250 */     sBuilder.append(" `isDead` = '").append(this.isDead ? 1 : 0).append("',");
/* 251 */     sBuilder.append(" `isOpen` = '").append(this.isOpen ? 1 : 0).append("',");
/* 252 */     sBuilder.append(" `deadTime` = '").append(this.deadTime).append("',");
/* 253 */     sBuilder.append(" `reviveTime` = '").append(this.reviveTime).append("',");
/* 254 */     sBuilder.append(" `lastKillCid` = '").append(this.lastKillCid).append("',");
/* 255 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 256 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 260 */     String sql = "CREATE TABLE IF NOT EXISTS `guildBoss` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`guildId` bigint(20) NOT NULL DEFAULT '0' COMMENT '帮派Id',`bossId` int(11) NOT NULL DEFAULT '0' COMMENT 'Boss关卡ID(GuildBoss静态表ID)',`bossHp` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Boss当前生命(复活时刷新)',`bossMaxHp` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Boss最大生命(复活时刷新)',`isDead` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Boss是否已死亡',`isOpen` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Boss是否已开启',`deadTime` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Boss死亡时间',`reviveTime` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Boss复活时间',`lastKillCid` bigint(20) NOT NULL DEFAULT '0' COMMENT '上次击杀者pid',PRIMARY KEY (`id`)) COMMENT='帮派boss信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 272 */       ServerConfig.getInitialID() + 1L);
/* 273 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/GuildBossBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */