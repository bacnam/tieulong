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
/*     */ public class PlayerIconBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "*所属玩家ID")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "refCard", comment = "卡牌ID")
/*     */   private int refCard;
/*     */   @DataBaseField(type = "int(11)", fieldname = "type", comment = "2-英雄头像,3-觉醒头像")
/*     */   private int type;
/*     */   
/*     */   public PlayerIconBO() {
/*  24 */     this.id = 0L;
/*  25 */     this.pid = 0L;
/*  26 */     this.refCard = 0;
/*  27 */     this.type = 0;
/*     */   }
/*     */   
/*     */   public PlayerIconBO(ResultSet rs) throws Exception {
/*  31 */     this.id = rs.getLong(1);
/*  32 */     this.pid = rs.getLong(2);
/*  33 */     this.refCard = rs.getInt(3);
/*  34 */     this.type = rs.getInt(4);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<PlayerIconBO> list) throws Exception {
/*  40 */     list.add(new PlayerIconBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  45 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  50 */     return "`id`, `pid`, `refCard`, `type`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  55 */     return "`playerIcon`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  60 */     StringBuilder strBuf = new StringBuilder();
/*  61 */     strBuf.append("'").append(this.id).append("', ");
/*  62 */     strBuf.append("'").append(this.pid).append("', ");
/*  63 */     strBuf.append("'").append(this.refCard).append("', ");
/*  64 */     strBuf.append("'").append(this.type).append("', ");
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
/*     */   public int getRefCard() {
/* 100 */     return this.refCard;
/*     */   } public void setRefCard(int refCard) {
/* 102 */     if (refCard == this.refCard)
/*     */       return; 
/* 104 */     this.refCard = refCard;
/*     */   }
/*     */   public void saveRefCard(int refCard) {
/* 107 */     if (refCard == this.refCard)
/*     */       return; 
/* 109 */     this.refCard = refCard;
/* 110 */     saveField("refCard", Integer.valueOf(refCard));
/*     */   }
/*     */   
/*     */   public int getType() {
/* 114 */     return this.type;
/*     */   } public void setType(int type) {
/* 116 */     if (type == this.type)
/*     */       return; 
/* 118 */     this.type = type;
/*     */   }
/*     */   public void saveType(int type) {
/* 121 */     if (type == this.type)
/*     */       return; 
/* 123 */     this.type = type;
/* 124 */     saveField("type", Integer.valueOf(type));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 131 */     StringBuilder sBuilder = new StringBuilder();
/* 132 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 133 */     sBuilder.append(" `refCard` = '").append(this.refCard).append("',");
/* 134 */     sBuilder.append(" `type` = '").append(this.type).append("',");
/* 135 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 136 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 140 */     String sql = "CREATE TABLE IF NOT EXISTS `playerIcon` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '*所属玩家ID',`refCard` int(11) NOT NULL DEFAULT '0' COMMENT '卡牌ID',`type` int(11) NOT NULL DEFAULT '0' COMMENT '2-英雄头像,3-觉醒头像',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='玩家头像表,按表的id字段排序'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 147 */       ServerConfig.getInitialID() + 1L);
/* 148 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/PlayerIconBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */