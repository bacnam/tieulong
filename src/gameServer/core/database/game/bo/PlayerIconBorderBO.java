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
/*     */ public class PlayerIconBorderBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "*所属玩家ID")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "refID", comment = "边框ID")
/*     */   private int refID;
/*     */   
/*     */   public PlayerIconBorderBO() {
/*  22 */     this.id = 0L;
/*  23 */     this.pid = 0L;
/*  24 */     this.refID = 0;
/*     */   }
/*     */   
/*     */   public PlayerIconBorderBO(ResultSet rs) throws Exception {
/*  28 */     this.id = rs.getLong(1);
/*  29 */     this.pid = rs.getLong(2);
/*  30 */     this.refID = rs.getInt(3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<PlayerIconBorderBO> list) throws Exception {
/*  36 */     list.add(new PlayerIconBorderBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  41 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  46 */     return "`id`, `pid`, `refID`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  51 */     return "`playerIconBorder`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  56 */     StringBuilder strBuf = new StringBuilder();
/*  57 */     strBuf.append("'").append(this.id).append("', ");
/*  58 */     strBuf.append("'").append(this.pid).append("', ");
/*  59 */     strBuf.append("'").append(this.refID).append("', ");
/*  60 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  61 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  66 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  67 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  72 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/*  77 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/*  81 */     return this.pid;
/*     */   } public void setPid(long pid) {
/*  83 */     if (pid == this.pid)
/*     */       return; 
/*  85 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/*  88 */     if (pid == this.pid)
/*     */       return; 
/*  90 */     this.pid = pid;
/*  91 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public int getRefID() {
/*  95 */     return this.refID;
/*     */   } public void setRefID(int refID) {
/*  97 */     if (refID == this.refID)
/*     */       return; 
/*  99 */     this.refID = refID;
/*     */   }
/*     */   public void saveRefID(int refID) {
/* 102 */     if (refID == this.refID)
/*     */       return; 
/* 104 */     this.refID = refID;
/* 105 */     saveField("refID", Integer.valueOf(refID));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 112 */     StringBuilder sBuilder = new StringBuilder();
/* 113 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 114 */     sBuilder.append(" `refID` = '").append(this.refID).append("',");
/* 115 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 116 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 120 */     String sql = "CREATE TABLE IF NOT EXISTS `playerIconBorder` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '*所属玩家ID',`refID` int(11) NOT NULL DEFAULT '0' COMMENT '边框ID',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='玩家头像边框'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 126 */       ServerConfig.getInitialID() + 1L);
/* 127 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/PlayerIconBorderBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */