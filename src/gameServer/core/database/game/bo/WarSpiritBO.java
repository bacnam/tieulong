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
/*     */ public class WarSpiritBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "spirit_id", comment = "战灵id")
/*     */   private int spirit_id;
/*     */   @DataBaseField(type = "int(11)", fieldname = "skill", comment = "技能等级")
/*     */   private int skill;
/*     */   @DataBaseField(type = "int(11)", fieldname = "star", comment = "星级")
/*     */   private int star;
/*     */   @DataBaseField(type = "tinyint(1)", fieldname = "is_selected", comment = "是否出战")
/*     */   private boolean is_selected;
/*     */   
/*     */   public WarSpiritBO() {
/*  28 */     this.id = 0L;
/*  29 */     this.pid = 0L;
/*  30 */     this.spirit_id = 0;
/*  31 */     this.skill = 0;
/*  32 */     this.star = 0;
/*  33 */     this.is_selected = false;
/*     */   }
/*     */   
/*     */   public WarSpiritBO(ResultSet rs) throws Exception {
/*  37 */     this.id = rs.getLong(1);
/*  38 */     this.pid = rs.getLong(2);
/*  39 */     this.spirit_id = rs.getInt(3);
/*  40 */     this.skill = rs.getInt(4);
/*  41 */     this.star = rs.getInt(5);
/*  42 */     this.is_selected = rs.getBoolean(6);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<WarSpiritBO> list) throws Exception {
/*  48 */     list.add(new WarSpiritBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  53 */     return getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  58 */     return "`id`, `pid`, `spirit_id`, `skill`, `star`, `is_selected`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  63 */     return "`warSpirit`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  68 */     StringBuilder strBuf = new StringBuilder();
/*  69 */     strBuf.append("'").append(this.id).append("', ");
/*  70 */     strBuf.append("'").append(this.pid).append("', ");
/*  71 */     strBuf.append("'").append(this.spirit_id).append("', ");
/*  72 */     strBuf.append("'").append(this.skill).append("', ");
/*  73 */     strBuf.append("'").append(this.star).append("', ");
/*  74 */     strBuf.append("'").append(this.is_selected ? 1 : 0).append("', ");
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
/*     */   public long getPid() {
/*  96 */     return this.pid;
/*     */   } public void setPid(long pid) {
/*  98 */     if (pid == this.pid)
/*     */       return; 
/* 100 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 103 */     if (pid == this.pid)
/*     */       return; 
/* 105 */     this.pid = pid;
/* 106 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public int getSpiritId() {
/* 110 */     return this.spirit_id;
/*     */   } public void setSpiritId(int spirit_id) {
/* 112 */     if (spirit_id == this.spirit_id)
/*     */       return; 
/* 114 */     this.spirit_id = spirit_id;
/*     */   }
/*     */   public void saveSpiritId(int spirit_id) {
/* 117 */     if (spirit_id == this.spirit_id)
/*     */       return; 
/* 119 */     this.spirit_id = spirit_id;
/* 120 */     saveField("spirit_id", Integer.valueOf(spirit_id));
/*     */   }
/*     */   
/*     */   public int getSkill() {
/* 124 */     return this.skill;
/*     */   } public void setSkill(int skill) {
/* 126 */     if (skill == this.skill)
/*     */       return; 
/* 128 */     this.skill = skill;
/*     */   }
/*     */   public void saveSkill(int skill) {
/* 131 */     if (skill == this.skill)
/*     */       return; 
/* 133 */     this.skill = skill;
/* 134 */     saveField("skill", Integer.valueOf(skill));
/*     */   }
/*     */   
/*     */   public int getStar() {
/* 138 */     return this.star;
/*     */   } public void setStar(int star) {
/* 140 */     if (star == this.star)
/*     */       return; 
/* 142 */     this.star = star;
/*     */   }
/*     */   public void saveStar(int star) {
/* 145 */     if (star == this.star)
/*     */       return; 
/* 147 */     this.star = star;
/* 148 */     saveField("star", Integer.valueOf(star));
/*     */   }
/*     */   
/*     */   public boolean getIsSelected() {
/* 152 */     return this.is_selected;
/*     */   } public void setIsSelected(boolean is_selected) {
/* 154 */     if (is_selected == this.is_selected)
/*     */       return; 
/* 156 */     this.is_selected = is_selected;
/*     */   }
/*     */   public void saveIsSelected(boolean is_selected) {
/* 159 */     if (is_selected == this.is_selected)
/*     */       return; 
/* 161 */     this.is_selected = is_selected;
/* 162 */     saveField("is_selected", Integer.valueOf(is_selected ? 1 : 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 169 */     StringBuilder sBuilder = new StringBuilder();
/* 170 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 171 */     sBuilder.append(" `spirit_id` = '").append(this.spirit_id).append("',");
/* 172 */     sBuilder.append(" `skill` = '").append(this.skill).append("',");
/* 173 */     sBuilder.append(" `star` = '").append(this.star).append("',");
/* 174 */     sBuilder.append(" `is_selected` = '").append(this.is_selected ? 1 : 0).append("',");
/* 175 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 176 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 180 */     String sql = "CREATE TABLE IF NOT EXISTS `warSpirit` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`spirit_id` int(11) NOT NULL DEFAULT '0' COMMENT '战灵id',`skill` int(11) NOT NULL DEFAULT '0' COMMENT '技能等级',`star` int(11) NOT NULL DEFAULT '0' COMMENT '星级',`is_selected` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否出战',PRIMARY KEY (`id`)) COMMENT='战灵信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 188 */       ServerConfig.getInitialID() + 1L);
/* 189 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/WarSpiritBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */