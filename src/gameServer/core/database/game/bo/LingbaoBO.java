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
/*     */ public class LingbaoBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "level", comment = "灵宝等级")
/*     */   private int level;
/*     */   @DataBaseField(type = "int(11)", fieldname = "exp", comment = "灵宝经验")
/*     */   private int exp;
/*     */   
/*     */   public LingbaoBO() {
/*  24 */     this.id = 0L;
/*  25 */     this.pid = 0L;
/*  26 */     this.level = 0;
/*  27 */     this.exp = 0;
/*     */   }
/*     */   
/*     */   public LingbaoBO(ResultSet rs) throws Exception {
/*  31 */     this.id = rs.getLong(1);
/*  32 */     this.pid = rs.getLong(2);
/*  33 */     this.level = rs.getInt(3);
/*  34 */     this.exp = rs.getInt(4);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<LingbaoBO> list) throws Exception {
/*  40 */     list.add(new LingbaoBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  45 */     return getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  50 */     return "`id`, `pid`, `level`, `exp`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  55 */     return "`lingbao`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  60 */     StringBuilder strBuf = new StringBuilder();
/*  61 */     strBuf.append("'").append(this.id).append("', ");
/*  62 */     strBuf.append("'").append(this.pid).append("', ");
/*  63 */     strBuf.append("'").append(this.level).append("', ");
/*  64 */     strBuf.append("'").append(this.exp).append("', ");
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
/*     */   public int getLevel() {
/* 100 */     return this.level;
/*     */   } public void setLevel(int level) {
/* 102 */     if (level == this.level)
/*     */       return; 
/* 104 */     this.level = level;
/*     */   }
/*     */   public void saveLevel(int level) {
/* 107 */     if (level == this.level)
/*     */       return; 
/* 109 */     this.level = level;
/* 110 */     saveField("level", Integer.valueOf(level));
/*     */   }
/*     */   
/*     */   public int getExp() {
/* 114 */     return this.exp;
/*     */   } public void setExp(int exp) {
/* 116 */     if (exp == this.exp)
/*     */       return; 
/* 118 */     this.exp = exp;
/*     */   }
/*     */   public void saveExp(int exp) {
/* 121 */     if (exp == this.exp)
/*     */       return; 
/* 123 */     this.exp = exp;
/* 124 */     saveField("exp", Integer.valueOf(exp));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 131 */     StringBuilder sBuilder = new StringBuilder();
/* 132 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 133 */     sBuilder.append(" `level` = '").append(this.level).append("',");
/* 134 */     sBuilder.append(" `exp` = '").append(this.exp).append("',");
/* 135 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 136 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 140 */     String sql = "CREATE TABLE IF NOT EXISTS `lingbao` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`level` int(11) NOT NULL DEFAULT '0' COMMENT '灵宝等级',`exp` int(11) NOT NULL DEFAULT '0' COMMENT '灵宝经验',PRIMARY KEY (`id`)) COMMENT='灵宝表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 146 */       ServerConfig.getInitialID() + 1L);
/* 147 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/LingbaoBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */