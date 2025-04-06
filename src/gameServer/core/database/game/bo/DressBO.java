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
/*     */ public class DressBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "dress_id", comment = "时装id")
/*     */   private int dress_id;
/*     */   @DataBaseField(type = "int(11)", fieldname = "type", comment = "时装类型")
/*     */   private int type;
/*     */   @DataBaseField(type = "int(11)", fieldname = "char_id", comment = "装备的角色")
/*     */   private int char_id;
/*     */   @DataBaseField(type = "int(11)", fieldname = "active_time", comment = "激活时间")
/*     */   private int active_time;
/*     */   @DataBaseField(type = "int(11)", fieldname = "equip_time", comment = "穿戴时间")
/*     */   private int equip_time;
/*     */   
/*     */   public DressBO() {
/*  30 */     this.id = 0L;
/*  31 */     this.pid = 0L;
/*  32 */     this.dress_id = 0;
/*  33 */     this.type = 0;
/*  34 */     this.char_id = 0;
/*  35 */     this.active_time = 0;
/*  36 */     this.equip_time = 0;
/*     */   }
/*     */   
/*     */   public DressBO(ResultSet rs) throws Exception {
/*  40 */     this.id = rs.getLong(1);
/*  41 */     this.pid = rs.getLong(2);
/*  42 */     this.dress_id = rs.getInt(3);
/*  43 */     this.type = rs.getInt(4);
/*  44 */     this.char_id = rs.getInt(5);
/*  45 */     this.active_time = rs.getInt(6);
/*  46 */     this.equip_time = rs.getInt(7);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<DressBO> list) throws Exception {
/*  52 */     list.add(new DressBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  57 */     return getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  62 */     return "`id`, `pid`, `dress_id`, `type`, `char_id`, `active_time`, `equip_time`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  67 */     return "`dress`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  72 */     StringBuilder strBuf = new StringBuilder();
/*  73 */     strBuf.append("'").append(this.id).append("', ");
/*  74 */     strBuf.append("'").append(this.pid).append("', ");
/*  75 */     strBuf.append("'").append(this.dress_id).append("', ");
/*  76 */     strBuf.append("'").append(this.type).append("', ");
/*  77 */     strBuf.append("'").append(this.char_id).append("', ");
/*  78 */     strBuf.append("'").append(this.active_time).append("', ");
/*  79 */     strBuf.append("'").append(this.equip_time).append("', ");
/*  80 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  81 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  86 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  87 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  92 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/*  97 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 101 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 103 */     if (pid == this.pid)
/*     */       return; 
/* 105 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 108 */     if (pid == this.pid)
/*     */       return; 
/* 110 */     this.pid = pid;
/* 111 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public int getDressId() {
/* 115 */     return this.dress_id;
/*     */   } public void setDressId(int dress_id) {
/* 117 */     if (dress_id == this.dress_id)
/*     */       return; 
/* 119 */     this.dress_id = dress_id;
/*     */   }
/*     */   public void saveDressId(int dress_id) {
/* 122 */     if (dress_id == this.dress_id)
/*     */       return; 
/* 124 */     this.dress_id = dress_id;
/* 125 */     saveField("dress_id", Integer.valueOf(dress_id));
/*     */   }
/*     */   
/*     */   public int getType() {
/* 129 */     return this.type;
/*     */   } public void setType(int type) {
/* 131 */     if (type == this.type)
/*     */       return; 
/* 133 */     this.type = type;
/*     */   }
/*     */   public void saveType(int type) {
/* 136 */     if (type == this.type)
/*     */       return; 
/* 138 */     this.type = type;
/* 139 */     saveField("type", Integer.valueOf(type));
/*     */   }
/*     */   
/*     */   public int getCharId() {
/* 143 */     return this.char_id;
/*     */   } public void setCharId(int char_id) {
/* 145 */     if (char_id == this.char_id)
/*     */       return; 
/* 147 */     this.char_id = char_id;
/*     */   }
/*     */   public void saveCharId(int char_id) {
/* 150 */     if (char_id == this.char_id)
/*     */       return; 
/* 152 */     this.char_id = char_id;
/* 153 */     saveField("char_id", Integer.valueOf(char_id));
/*     */   }
/*     */   
/*     */   public int getActiveTime() {
/* 157 */     return this.active_time;
/*     */   } public void setActiveTime(int active_time) {
/* 159 */     if (active_time == this.active_time)
/*     */       return; 
/* 161 */     this.active_time = active_time;
/*     */   }
/*     */   public void saveActiveTime(int active_time) {
/* 164 */     if (active_time == this.active_time)
/*     */       return; 
/* 166 */     this.active_time = active_time;
/* 167 */     saveField("active_time", Integer.valueOf(active_time));
/*     */   }
/*     */   
/*     */   public int getEquipTime() {
/* 171 */     return this.equip_time;
/*     */   } public void setEquipTime(int equip_time) {
/* 173 */     if (equip_time == this.equip_time)
/*     */       return; 
/* 175 */     this.equip_time = equip_time;
/*     */   }
/*     */   public void saveEquipTime(int equip_time) {
/* 178 */     if (equip_time == this.equip_time)
/*     */       return; 
/* 180 */     this.equip_time = equip_time;
/* 181 */     saveField("equip_time", Integer.valueOf(equip_time));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 188 */     StringBuilder sBuilder = new StringBuilder();
/* 189 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 190 */     sBuilder.append(" `dress_id` = '").append(this.dress_id).append("',");
/* 191 */     sBuilder.append(" `type` = '").append(this.type).append("',");
/* 192 */     sBuilder.append(" `char_id` = '").append(this.char_id).append("',");
/* 193 */     sBuilder.append(" `active_time` = '").append(this.active_time).append("',");
/* 194 */     sBuilder.append(" `equip_time` = '").append(this.equip_time).append("',");
/* 195 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 196 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 200 */     String sql = "CREATE TABLE IF NOT EXISTS `dress` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`dress_id` int(11) NOT NULL DEFAULT '0' COMMENT '时装id',`type` int(11) NOT NULL DEFAULT '0' COMMENT '时装类型',`char_id` int(11) NOT NULL DEFAULT '0' COMMENT '装备的角色',`active_time` int(11) NOT NULL DEFAULT '0' COMMENT '激活时间',`equip_time` int(11) NOT NULL DEFAULT '0' COMMENT '穿戴时间',PRIMARY KEY (`id`)) COMMENT='玩家信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 209 */       ServerConfig.getInitialID() + 1L);
/* 210 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/DressBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */