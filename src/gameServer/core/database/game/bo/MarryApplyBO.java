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
/*     */ public class MarryApplyBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
/*     */   private long pid;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "from_pid", comment = "来自玩家id")
/*     */   private long from_pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "apply_time", comment = "申请时间")
/*     */   private int apply_time;
/*     */   
/*     */   public MarryApplyBO() {
/*  24 */     this.id = 0L;
/*  25 */     this.pid = 0L;
/*  26 */     this.from_pid = 0L;
/*  27 */     this.apply_time = 0;
/*     */   }
/*     */   
/*     */   public MarryApplyBO(ResultSet rs) throws Exception {
/*  31 */     this.id = rs.getLong(1);
/*  32 */     this.pid = rs.getLong(2);
/*  33 */     this.from_pid = rs.getLong(3);
/*  34 */     this.apply_time = rs.getInt(4);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<MarryApplyBO> list) throws Exception {
/*  40 */     list.add(new MarryApplyBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  45 */     return getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  50 */     return "`id`, `pid`, `from_pid`, `apply_time`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  55 */     return "`marry_apply`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  60 */     StringBuilder strBuf = new StringBuilder();
/*  61 */     strBuf.append("'").append(this.id).append("', ");
/*  62 */     strBuf.append("'").append(this.pid).append("', ");
/*  63 */     strBuf.append("'").append(this.from_pid).append("', ");
/*  64 */     strBuf.append("'").append(this.apply_time).append("', ");
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
/*     */   public long getFromPid() {
/* 100 */     return this.from_pid;
/*     */   } public void setFromPid(long from_pid) {
/* 102 */     if (from_pid == this.from_pid)
/*     */       return; 
/* 104 */     this.from_pid = from_pid;
/*     */   }
/*     */   public void saveFromPid(long from_pid) {
/* 107 */     if (from_pid == this.from_pid)
/*     */       return; 
/* 109 */     this.from_pid = from_pid;
/* 110 */     saveField("from_pid", Long.valueOf(from_pid));
/*     */   }
/*     */   
/*     */   public int getApplyTime() {
/* 114 */     return this.apply_time;
/*     */   } public void setApplyTime(int apply_time) {
/* 116 */     if (apply_time == this.apply_time)
/*     */       return; 
/* 118 */     this.apply_time = apply_time;
/*     */   }
/*     */   public void saveApplyTime(int apply_time) {
/* 121 */     if (apply_time == this.apply_time)
/*     */       return; 
/* 123 */     this.apply_time = apply_time;
/* 124 */     saveField("apply_time", Integer.valueOf(apply_time));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 131 */     StringBuilder sBuilder = new StringBuilder();
/* 132 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 133 */     sBuilder.append(" `from_pid` = '").append(this.from_pid).append("',");
/* 134 */     sBuilder.append(" `apply_time` = '").append(this.apply_time).append("',");
/* 135 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 136 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 140 */     String sql = "CREATE TABLE IF NOT EXISTS `marry_apply` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`from_pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '来自玩家id',`apply_time` int(11) NOT NULL DEFAULT '0' COMMENT '申请时间',PRIMARY KEY (`id`)) COMMENT='婚恋系统'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 146 */       ServerConfig.getInitialID() + 1L);
/* 147 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/MarryApplyBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */