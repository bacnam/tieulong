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
/*     */ public class NewtitleBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "title_id", comment = "称号id")
/*     */   private int title_id;
/*     */   @DataBaseField(type = "int(11)", fieldname = "level", comment = "等级")
/*     */   private int level;
/*     */   @DataBaseField(type = "tinyint(1)", fieldname = "is_using", comment = "是否正在使用")
/*     */   private boolean is_using;
/*     */   @DataBaseField(type = "int(11)", fieldname = "active_time", comment = "激活时间")
/*     */   private int active_time;
/*     */   
/*     */   public NewtitleBO() {
/*  28 */     this.id = 0L;
/*  29 */     this.pid = 0L;
/*  30 */     this.title_id = 0;
/*  31 */     this.level = 0;
/*  32 */     this.is_using = false;
/*  33 */     this.active_time = 0;
/*     */   }
/*     */   
/*     */   public NewtitleBO(ResultSet rs) throws Exception {
/*  37 */     this.id = rs.getLong(1);
/*  38 */     this.pid = rs.getLong(2);
/*  39 */     this.title_id = rs.getInt(3);
/*  40 */     this.level = rs.getInt(4);
/*  41 */     this.is_using = rs.getBoolean(5);
/*  42 */     this.active_time = rs.getInt(6);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<NewtitleBO> list) throws Exception {
/*  48 */     list.add(new NewtitleBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  53 */     return getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  58 */     return "`id`, `pid`, `title_id`, `level`, `is_using`, `active_time`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  63 */     return "`newtitle`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  68 */     StringBuilder strBuf = new StringBuilder();
/*  69 */     strBuf.append("'").append(this.id).append("', ");
/*  70 */     strBuf.append("'").append(this.pid).append("', ");
/*  71 */     strBuf.append("'").append(this.title_id).append("', ");
/*  72 */     strBuf.append("'").append(this.level).append("', ");
/*  73 */     strBuf.append("'").append(this.is_using ? 1 : 0).append("', ");
/*  74 */     strBuf.append("'").append(this.active_time).append("', ");
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
/*     */   public int getTitleId() {
/* 110 */     return this.title_id;
/*     */   } public void setTitleId(int title_id) {
/* 112 */     if (title_id == this.title_id)
/*     */       return; 
/* 114 */     this.title_id = title_id;
/*     */   }
/*     */   public void saveTitleId(int title_id) {
/* 117 */     if (title_id == this.title_id)
/*     */       return; 
/* 119 */     this.title_id = title_id;
/* 120 */     saveField("title_id", Integer.valueOf(title_id));
/*     */   }
/*     */   
/*     */   public int getLevel() {
/* 124 */     return this.level;
/*     */   } public void setLevel(int level) {
/* 126 */     if (level == this.level)
/*     */       return; 
/* 128 */     this.level = level;
/*     */   }
/*     */   public void saveLevel(int level) {
/* 131 */     if (level == this.level)
/*     */       return; 
/* 133 */     this.level = level;
/* 134 */     saveField("level", Integer.valueOf(level));
/*     */   }
/*     */   
/*     */   public boolean getIsUsing() {
/* 138 */     return this.is_using;
/*     */   } public void setIsUsing(boolean is_using) {
/* 140 */     if (is_using == this.is_using)
/*     */       return; 
/* 142 */     this.is_using = is_using;
/*     */   }
/*     */   public void saveIsUsing(boolean is_using) {
/* 145 */     if (is_using == this.is_using)
/*     */       return; 
/* 147 */     this.is_using = is_using;
/* 148 */     saveField("is_using", Integer.valueOf(is_using ? 1 : 0));
/*     */   }
/*     */   
/*     */   public int getActiveTime() {
/* 152 */     return this.active_time;
/*     */   } public void setActiveTime(int active_time) {
/* 154 */     if (active_time == this.active_time)
/*     */       return; 
/* 156 */     this.active_time = active_time;
/*     */   }
/*     */   public void saveActiveTime(int active_time) {
/* 159 */     if (active_time == this.active_time)
/*     */       return; 
/* 161 */     this.active_time = active_time;
/* 162 */     saveField("active_time", Integer.valueOf(active_time));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 169 */     StringBuilder sBuilder = new StringBuilder();
/* 170 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 171 */     sBuilder.append(" `title_id` = '").append(this.title_id).append("',");
/* 172 */     sBuilder.append(" `level` = '").append(this.level).append("',");
/* 173 */     sBuilder.append(" `is_using` = '").append(this.is_using ? 1 : 0).append("',");
/* 174 */     sBuilder.append(" `active_time` = '").append(this.active_time).append("',");
/* 175 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 176 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 180 */     String sql = "CREATE TABLE IF NOT EXISTS `newtitle` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`title_id` int(11) NOT NULL DEFAULT '0' COMMENT '称号id',`level` int(11) NOT NULL DEFAULT '0' COMMENT '等级',`is_using` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否正在使用',`active_time` int(11) NOT NULL DEFAULT '0' COMMENT '激活时间',PRIMARY KEY (`id`)) COMMENT='玩家信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/NewtitleBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */