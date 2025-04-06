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
/*     */ public class TitleBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "title_id", comment = "称号id")
/*     */   private int title_id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "value", comment = "当前数值")
/*     */   private long value;
/*     */   @DataBaseField(type = "tinyint(1)", fieldname = "is_using", comment = "是否正在使用")
/*     */   private boolean is_using;
/*     */   @DataBaseField(type = "tinyint(1)", fieldname = "is_active", comment = "是否激活")
/*     */   private boolean is_active;
/*     */   @DataBaseField(type = "int(11)", fieldname = "active_time", comment = "激活时间")
/*     */   private int active_time;
/*     */   @DataBaseField(type = "tinyint(1)", fieldname = "is_reward", comment = "是否获得奖励")
/*     */   private boolean is_reward;
/*     */   
/*     */   public TitleBO() {
/*  32 */     this.id = 0L;
/*  33 */     this.pid = 0L;
/*  34 */     this.title_id = 0;
/*  35 */     this.value = 0L;
/*  36 */     this.is_using = false;
/*  37 */     this.is_active = false;
/*  38 */     this.active_time = 0;
/*  39 */     this.is_reward = false;
/*     */   }
/*     */   
/*     */   public TitleBO(ResultSet rs) throws Exception {
/*  43 */     this.id = rs.getLong(1);
/*  44 */     this.pid = rs.getLong(2);
/*  45 */     this.title_id = rs.getInt(3);
/*  46 */     this.value = rs.getLong(4);
/*  47 */     this.is_using = rs.getBoolean(5);
/*  48 */     this.is_active = rs.getBoolean(6);
/*  49 */     this.active_time = rs.getInt(7);
/*  50 */     this.is_reward = rs.getBoolean(8);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<TitleBO> list) throws Exception {
/*  56 */     list.add(new TitleBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  61 */     return getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  66 */     return "`id`, `pid`, `title_id`, `value`, `is_using`, `is_active`, `active_time`, `is_reward`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  71 */     return "`title`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  76 */     StringBuilder strBuf = new StringBuilder();
/*  77 */     strBuf.append("'").append(this.id).append("', ");
/*  78 */     strBuf.append("'").append(this.pid).append("', ");
/*  79 */     strBuf.append("'").append(this.title_id).append("', ");
/*  80 */     strBuf.append("'").append(this.value).append("', ");
/*  81 */     strBuf.append("'").append(this.is_using ? 1 : 0).append("', ");
/*  82 */     strBuf.append("'").append(this.is_active ? 1 : 0).append("', ");
/*  83 */     strBuf.append("'").append(this.active_time).append("', ");
/*  84 */     strBuf.append("'").append(this.is_reward ? 1 : 0).append("', ");
/*  85 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  86 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  91 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  92 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  97 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 102 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 106 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 108 */     if (pid == this.pid)
/*     */       return; 
/* 110 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 113 */     if (pid == this.pid)
/*     */       return; 
/* 115 */     this.pid = pid;
/* 116 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public int getTitleId() {
/* 120 */     return this.title_id;
/*     */   } public void setTitleId(int title_id) {
/* 122 */     if (title_id == this.title_id)
/*     */       return; 
/* 124 */     this.title_id = title_id;
/*     */   }
/*     */   public void saveTitleId(int title_id) {
/* 127 */     if (title_id == this.title_id)
/*     */       return; 
/* 129 */     this.title_id = title_id;
/* 130 */     saveField("title_id", Integer.valueOf(title_id));
/*     */   }
/*     */   
/*     */   public long getValue() {
/* 134 */     return this.value;
/*     */   } public void setValue(long value) {
/* 136 */     if (value == this.value)
/*     */       return; 
/* 138 */     this.value = value;
/*     */   }
/*     */   public void saveValue(long value) {
/* 141 */     if (value == this.value)
/*     */       return; 
/* 143 */     this.value = value;
/* 144 */     saveField("value", Long.valueOf(value));
/*     */   }
/*     */   
/*     */   public boolean getIsUsing() {
/* 148 */     return this.is_using;
/*     */   } public void setIsUsing(boolean is_using) {
/* 150 */     if (is_using == this.is_using)
/*     */       return; 
/* 152 */     this.is_using = is_using;
/*     */   }
/*     */   public void saveIsUsing(boolean is_using) {
/* 155 */     if (is_using == this.is_using)
/*     */       return; 
/* 157 */     this.is_using = is_using;
/* 158 */     saveField("is_using", Integer.valueOf(is_using ? 1 : 0));
/*     */   }
/*     */   
/*     */   public boolean getIsActive() {
/* 162 */     return this.is_active;
/*     */   } public void setIsActive(boolean is_active) {
/* 164 */     if (is_active == this.is_active)
/*     */       return; 
/* 166 */     this.is_active = is_active;
/*     */   }
/*     */   public void saveIsActive(boolean is_active) {
/* 169 */     if (is_active == this.is_active)
/*     */       return; 
/* 171 */     this.is_active = is_active;
/* 172 */     saveField("is_active", Integer.valueOf(is_active ? 1 : 0));
/*     */   }
/*     */   
/*     */   public int getActiveTime() {
/* 176 */     return this.active_time;
/*     */   } public void setActiveTime(int active_time) {
/* 178 */     if (active_time == this.active_time)
/*     */       return; 
/* 180 */     this.active_time = active_time;
/*     */   }
/*     */   public void saveActiveTime(int active_time) {
/* 183 */     if (active_time == this.active_time)
/*     */       return; 
/* 185 */     this.active_time = active_time;
/* 186 */     saveField("active_time", Integer.valueOf(active_time));
/*     */   }
/*     */   
/*     */   public boolean getIsReward() {
/* 190 */     return this.is_reward;
/*     */   } public void setIsReward(boolean is_reward) {
/* 192 */     if (is_reward == this.is_reward)
/*     */       return; 
/* 194 */     this.is_reward = is_reward;
/*     */   }
/*     */   public void saveIsReward(boolean is_reward) {
/* 197 */     if (is_reward == this.is_reward)
/*     */       return; 
/* 199 */     this.is_reward = is_reward;
/* 200 */     saveField("is_reward", Integer.valueOf(is_reward ? 1 : 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 207 */     StringBuilder sBuilder = new StringBuilder();
/* 208 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 209 */     sBuilder.append(" `title_id` = '").append(this.title_id).append("',");
/* 210 */     sBuilder.append(" `value` = '").append(this.value).append("',");
/* 211 */     sBuilder.append(" `is_using` = '").append(this.is_using ? 1 : 0).append("',");
/* 212 */     sBuilder.append(" `is_active` = '").append(this.is_active ? 1 : 0).append("',");
/* 213 */     sBuilder.append(" `active_time` = '").append(this.active_time).append("',");
/* 214 */     sBuilder.append(" `is_reward` = '").append(this.is_reward ? 1 : 0).append("',");
/* 215 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 216 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 220 */     String sql = "CREATE TABLE IF NOT EXISTS `title` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`title_id` int(11) NOT NULL DEFAULT '0' COMMENT '称号id',`value` bigint(20) NOT NULL DEFAULT '0' COMMENT '当前数值',`is_using` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否正在使用',`is_active` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否激活',`active_time` int(11) NOT NULL DEFAULT '0' COMMENT '激活时间',`is_reward` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否获得奖励',PRIMARY KEY (`id`)) COMMENT='玩家信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 230 */       ServerConfig.getInitialID() + 1L);
/* 231 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/TitleBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */