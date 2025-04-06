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
/*     */ public class PlayerRechargeInfoBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家角色id")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "monthCardRemains", comment = "月卡剩余天数")
/*     */   private int monthCardRemains;
/*     */   @DataBaseField(type = "int(11)", fieldname = "permantCardRemains", comment = "年卡剩余天数")
/*     */   private int permantCardRemains;
/*     */   
/*     */   public PlayerRechargeInfoBO() {
/*  24 */     this.id = 0L;
/*  25 */     this.pid = 0L;
/*  26 */     this.monthCardRemains = 0;
/*  27 */     this.permantCardRemains = 0;
/*     */   }
/*     */   
/*     */   public PlayerRechargeInfoBO(ResultSet rs) throws Exception {
/*  31 */     this.id = rs.getLong(1);
/*  32 */     this.pid = rs.getLong(2);
/*  33 */     this.monthCardRemains = rs.getInt(3);
/*  34 */     this.permantCardRemains = rs.getInt(4);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<PlayerRechargeInfoBO> list) throws Exception {
/*  40 */     list.add(new PlayerRechargeInfoBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  45 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  50 */     return "`id`, `pid`, `monthCardRemains`, `permantCardRemains`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  55 */     return "`playerRechargeInfo`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  60 */     StringBuilder strBuf = new StringBuilder();
/*  61 */     strBuf.append("'").append(this.id).append("', ");
/*  62 */     strBuf.append("'").append(this.pid).append("', ");
/*  63 */     strBuf.append("'").append(this.monthCardRemains).append("', ");
/*  64 */     strBuf.append("'").append(this.permantCardRemains).append("', ");
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
/*     */   public int getMonthCardRemains() {
/* 100 */     return this.monthCardRemains;
/*     */   } public void setMonthCardRemains(int monthCardRemains) {
/* 102 */     if (monthCardRemains == this.monthCardRemains)
/*     */       return; 
/* 104 */     this.monthCardRemains = monthCardRemains;
/*     */   }
/*     */   public void saveMonthCardRemains(int monthCardRemains) {
/* 107 */     if (monthCardRemains == this.monthCardRemains)
/*     */       return; 
/* 109 */     this.monthCardRemains = monthCardRemains;
/* 110 */     saveField("monthCardRemains", Integer.valueOf(monthCardRemains));
/*     */   }
/*     */   
/*     */   public int getPermantCardRemains() {
/* 114 */     return this.permantCardRemains;
/*     */   } public void setPermantCardRemains(int permantCardRemains) {
/* 116 */     if (permantCardRemains == this.permantCardRemains)
/*     */       return; 
/* 118 */     this.permantCardRemains = permantCardRemains;
/*     */   }
/*     */   public void savePermantCardRemains(int permantCardRemains) {
/* 121 */     if (permantCardRemains == this.permantCardRemains)
/*     */       return; 
/* 123 */     this.permantCardRemains = permantCardRemains;
/* 124 */     saveField("permantCardRemains", Integer.valueOf(permantCardRemains));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 131 */     StringBuilder sBuilder = new StringBuilder();
/* 132 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 133 */     sBuilder.append(" `monthCardRemains` = '").append(this.monthCardRemains).append("',");
/* 134 */     sBuilder.append(" `permantCardRemains` = '").append(this.permantCardRemains).append("',");
/* 135 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 136 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 140 */     String sql = "CREATE TABLE IF NOT EXISTS `playerRechargeInfo` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家角色id',`monthCardRemains` int(11) NOT NULL DEFAULT '0' COMMENT '月卡剩余天数',`permantCardRemains` int(11) NOT NULL DEFAULT '0' COMMENT '年卡剩余天数',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='充值信息汇总'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/PlayerRechargeInfoBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */