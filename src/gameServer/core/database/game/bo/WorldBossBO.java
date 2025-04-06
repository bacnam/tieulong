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
/*     */ public class WorldBossBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "bossId", comment = "Boss关卡ID(WorldBoss静态表ID)")
/*     */   private long bossId;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "bossHp", comment = "Boss当前生命(复活时刷新)")
/*     */   private long bossHp;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "bossMaxHp", comment = "Boss最大生命(复活时刷新)")
/*     */   private long bossMaxHp;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "bossLevel", comment = "Boss当前等级")
/*     */   private long bossLevel;
/*     */   @DataBaseField(type = "tinyint(1)", fieldname = "isDead", comment = "Boss是否已死亡")
/*     */   private boolean isDead;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "deadTime", comment = "Boss死亡时间")
/*     */   private long deadTime;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "reviveTime", comment = "Boss复活时间")
/*     */   private long reviveTime;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "lastKillCid", comment = "上次击杀者pid")
/*     */   private long lastKillCid;
/*     */   
/*     */   public WorldBossBO() {
/*  34 */     this.id = 0L;
/*  35 */     this.bossId = 0L;
/*  36 */     this.bossHp = 0L;
/*  37 */     this.bossMaxHp = 0L;
/*  38 */     this.bossLevel = 0L;
/*  39 */     this.isDead = false;
/*  40 */     this.deadTime = 0L;
/*  41 */     this.reviveTime = 0L;
/*  42 */     this.lastKillCid = 0L;
/*     */   }
/*     */   
/*     */   public WorldBossBO(ResultSet rs) throws Exception {
/*  46 */     this.id = rs.getLong(1);
/*  47 */     this.bossId = rs.getLong(2);
/*  48 */     this.bossHp = rs.getLong(3);
/*  49 */     this.bossMaxHp = rs.getLong(4);
/*  50 */     this.bossLevel = rs.getLong(5);
/*  51 */     this.isDead = rs.getBoolean(6);
/*  52 */     this.deadTime = rs.getLong(7);
/*  53 */     this.reviveTime = rs.getLong(8);
/*  54 */     this.lastKillCid = rs.getLong(9);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<WorldBossBO> list) throws Exception {
/*  60 */     list.add(new WorldBossBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  65 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  70 */     return "`id`, `bossId`, `bossHp`, `bossMaxHp`, `bossLevel`, `isDead`, `deadTime`, `reviveTime`, `lastKillCid`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  75 */     return "`worldBoss`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  80 */     StringBuilder strBuf = new StringBuilder();
/*  81 */     strBuf.append("'").append(this.id).append("', ");
/*  82 */     strBuf.append("'").append(this.bossId).append("', ");
/*  83 */     strBuf.append("'").append(this.bossHp).append("', ");
/*  84 */     strBuf.append("'").append(this.bossMaxHp).append("', ");
/*  85 */     strBuf.append("'").append(this.bossLevel).append("', ");
/*  86 */     strBuf.append("'").append(this.isDead ? 1 : 0).append("', ");
/*  87 */     strBuf.append("'").append(this.deadTime).append("', ");
/*  88 */     strBuf.append("'").append(this.reviveTime).append("', ");
/*  89 */     strBuf.append("'").append(this.lastKillCid).append("', ");
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
/*     */   public long getBossId() {
/* 111 */     return this.bossId;
/*     */   } public void setBossId(long bossId) {
/* 113 */     if (bossId == this.bossId)
/*     */       return; 
/* 115 */     this.bossId = bossId;
/*     */   }
/*     */   public void saveBossId(long bossId) {
/* 118 */     if (bossId == this.bossId)
/*     */       return; 
/* 120 */     this.bossId = bossId;
/* 121 */     saveField("bossId", Long.valueOf(bossId));
/*     */   }
/*     */   
/*     */   public long getBossHp() {
/* 125 */     return this.bossHp;
/*     */   } public void setBossHp(long bossHp) {
/* 127 */     if (bossHp == this.bossHp)
/*     */       return; 
/* 129 */     this.bossHp = bossHp;
/*     */   }
/*     */   public void saveBossHp(long bossHp) {
/* 132 */     if (bossHp == this.bossHp)
/*     */       return; 
/* 134 */     this.bossHp = bossHp;
/* 135 */     saveField("bossHp", Long.valueOf(bossHp));
/*     */   }
/*     */   
/*     */   public long getBossMaxHp() {
/* 139 */     return this.bossMaxHp;
/*     */   } public void setBossMaxHp(long bossMaxHp) {
/* 141 */     if (bossMaxHp == this.bossMaxHp)
/*     */       return; 
/* 143 */     this.bossMaxHp = bossMaxHp;
/*     */   }
/*     */   public void saveBossMaxHp(long bossMaxHp) {
/* 146 */     if (bossMaxHp == this.bossMaxHp)
/*     */       return; 
/* 148 */     this.bossMaxHp = bossMaxHp;
/* 149 */     saveField("bossMaxHp", Long.valueOf(bossMaxHp));
/*     */   }
/*     */   
/*     */   public long getBossLevel() {
/* 153 */     return this.bossLevel;
/*     */   } public void setBossLevel(long bossLevel) {
/* 155 */     if (bossLevel == this.bossLevel)
/*     */       return; 
/* 157 */     this.bossLevel = bossLevel;
/*     */   }
/*     */   public void saveBossLevel(long bossLevel) {
/* 160 */     if (bossLevel == this.bossLevel)
/*     */       return; 
/* 162 */     this.bossLevel = bossLevel;
/* 163 */     saveField("bossLevel", Long.valueOf(bossLevel));
/*     */   }
/*     */   
/*     */   public boolean getIsDead() {
/* 167 */     return this.isDead;
/*     */   } public void setIsDead(boolean isDead) {
/* 169 */     if (isDead == this.isDead)
/*     */       return; 
/* 171 */     this.isDead = isDead;
/*     */   }
/*     */   public void saveIsDead(boolean isDead) {
/* 174 */     if (isDead == this.isDead)
/*     */       return; 
/* 176 */     this.isDead = isDead;
/* 177 */     saveField("isDead", Integer.valueOf(isDead ? 1 : 0));
/*     */   }
/*     */   
/*     */   public long getDeadTime() {
/* 181 */     return this.deadTime;
/*     */   } public void setDeadTime(long deadTime) {
/* 183 */     if (deadTime == this.deadTime)
/*     */       return; 
/* 185 */     this.deadTime = deadTime;
/*     */   }
/*     */   public void saveDeadTime(long deadTime) {
/* 188 */     if (deadTime == this.deadTime)
/*     */       return; 
/* 190 */     this.deadTime = deadTime;
/* 191 */     saveField("deadTime", Long.valueOf(deadTime));
/*     */   }
/*     */   
/*     */   public long getReviveTime() {
/* 195 */     return this.reviveTime;
/*     */   } public void setReviveTime(long reviveTime) {
/* 197 */     if (reviveTime == this.reviveTime)
/*     */       return; 
/* 199 */     this.reviveTime = reviveTime;
/*     */   }
/*     */   public void saveReviveTime(long reviveTime) {
/* 202 */     if (reviveTime == this.reviveTime)
/*     */       return; 
/* 204 */     this.reviveTime = reviveTime;
/* 205 */     saveField("reviveTime", Long.valueOf(reviveTime));
/*     */   }
/*     */   
/*     */   public long getLastKillCid() {
/* 209 */     return this.lastKillCid;
/*     */   } public void setLastKillCid(long lastKillCid) {
/* 211 */     if (lastKillCid == this.lastKillCid)
/*     */       return; 
/* 213 */     this.lastKillCid = lastKillCid;
/*     */   }
/*     */   public void saveLastKillCid(long lastKillCid) {
/* 216 */     if (lastKillCid == this.lastKillCid)
/*     */       return; 
/* 218 */     this.lastKillCid = lastKillCid;
/* 219 */     saveField("lastKillCid", Long.valueOf(lastKillCid));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 226 */     StringBuilder sBuilder = new StringBuilder();
/* 227 */     sBuilder.append(" `bossId` = '").append(this.bossId).append("',");
/* 228 */     sBuilder.append(" `bossHp` = '").append(this.bossHp).append("',");
/* 229 */     sBuilder.append(" `bossMaxHp` = '").append(this.bossMaxHp).append("',");
/* 230 */     sBuilder.append(" `bossLevel` = '").append(this.bossLevel).append("',");
/* 231 */     sBuilder.append(" `isDead` = '").append(this.isDead ? 1 : 0).append("',");
/* 232 */     sBuilder.append(" `deadTime` = '").append(this.deadTime).append("',");
/* 233 */     sBuilder.append(" `reviveTime` = '").append(this.reviveTime).append("',");
/* 234 */     sBuilder.append(" `lastKillCid` = '").append(this.lastKillCid).append("',");
/* 235 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 236 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 240 */     String sql = "CREATE TABLE IF NOT EXISTS `worldBoss` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`bossId` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Boss关卡ID(WorldBoss静态表ID)',`bossHp` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Boss当前生命(复活时刷新)',`bossMaxHp` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Boss最大生命(复活时刷新)',`bossLevel` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Boss当前等级',`isDead` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Boss是否已死亡',`deadTime` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Boss死亡时间',`reviveTime` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Boss复活时间',`lastKillCid` bigint(20) NOT NULL DEFAULT '0' COMMENT '上次击杀者pid',PRIMARY KEY (`id`)) COMMENT='世界boss信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 251 */       ServerConfig.getInitialID() + 1L);
/* 252 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/WorldBossBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */