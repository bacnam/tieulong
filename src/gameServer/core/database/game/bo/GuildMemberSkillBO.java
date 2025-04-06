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
/*     */ public class GuildMemberSkillBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家pid")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "skillId", comment = "帮会技能静态Id")
/*     */   private int skillId;
/*     */   @DataBaseField(type = "int(11)", fieldname = "level", comment = "帮会技能等级")
/*     */   private int level;
/*     */   @DataBaseField(type = "int(11)", fieldname = "createTime", comment = "创建时间")
/*     */   private int createTime;
/*     */   
/*     */   public GuildMemberSkillBO() {
/*  26 */     this.id = 0L;
/*  27 */     this.pid = 0L;
/*  28 */     this.skillId = 0;
/*  29 */     this.level = 0;
/*  30 */     this.createTime = 0;
/*     */   }
/*     */   
/*     */   public GuildMemberSkillBO(ResultSet rs) throws Exception {
/*  34 */     this.id = rs.getLong(1);
/*  35 */     this.pid = rs.getLong(2);
/*  36 */     this.skillId = rs.getInt(3);
/*  37 */     this.level = rs.getInt(4);
/*  38 */     this.createTime = rs.getInt(5);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<GuildMemberSkillBO> list) throws Exception {
/*  44 */     list.add(new GuildMemberSkillBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  49 */     return getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  54 */     return "`id`, `pid`, `skillId`, `level`, `createTime`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  59 */     return "`guildMemberSkill`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  64 */     StringBuilder strBuf = new StringBuilder();
/*  65 */     strBuf.append("'").append(this.id).append("', ");
/*  66 */     strBuf.append("'").append(this.pid).append("', ");
/*  67 */     strBuf.append("'").append(this.skillId).append("', ");
/*  68 */     strBuf.append("'").append(this.level).append("', ");
/*  69 */     strBuf.append("'").append(this.createTime).append("', ");
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
/*     */   public int getSkillId() {
/* 105 */     return this.skillId;
/*     */   } public void setSkillId(int skillId) {
/* 107 */     if (skillId == this.skillId)
/*     */       return; 
/* 109 */     this.skillId = skillId;
/*     */   }
/*     */   public void saveSkillId(int skillId) {
/* 112 */     if (skillId == this.skillId)
/*     */       return; 
/* 114 */     this.skillId = skillId;
/* 115 */     saveField("skillId", Integer.valueOf(skillId));
/*     */   }
/*     */   
/*     */   public int getLevel() {
/* 119 */     return this.level;
/*     */   } public void setLevel(int level) {
/* 121 */     if (level == this.level)
/*     */       return; 
/* 123 */     this.level = level;
/*     */   }
/*     */   public void saveLevel(int level) {
/* 126 */     if (level == this.level)
/*     */       return; 
/* 128 */     this.level = level;
/* 129 */     saveField("level", Integer.valueOf(level));
/*     */   }
/*     */   
/*     */   public int getCreateTime() {
/* 133 */     return this.createTime;
/*     */   } public void setCreateTime(int createTime) {
/* 135 */     if (createTime == this.createTime)
/*     */       return; 
/* 137 */     this.createTime = createTime;
/*     */   }
/*     */   public void saveCreateTime(int createTime) {
/* 140 */     if (createTime == this.createTime)
/*     */       return; 
/* 142 */     this.createTime = createTime;
/* 143 */     saveField("createTime", Integer.valueOf(createTime));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 150 */     StringBuilder sBuilder = new StringBuilder();
/* 151 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 152 */     sBuilder.append(" `skillId` = '").append(this.skillId).append("',");
/* 153 */     sBuilder.append(" `level` = '").append(this.level).append("',");
/* 154 */     sBuilder.append(" `createTime` = '").append(this.createTime).append("',");
/* 155 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 156 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 160 */     String sql = "CREATE TABLE IF NOT EXISTS `guildMemberSkill` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家pid',`skillId` int(11) NOT NULL DEFAULT '0' COMMENT '帮会技能静态Id',`level` int(11) NOT NULL DEFAULT '0' COMMENT '帮会技能等级',`createTime` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',PRIMARY KEY (`id`)) COMMENT='帮会成员技能信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 167 */       ServerConfig.getInitialID() + 1L);
/* 168 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/GuildMemberSkillBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */