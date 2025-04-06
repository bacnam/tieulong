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
/*     */ public class WarSpiritTreasureRecordBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "spirit_id", comment = "战灵id")
/*     */   private int spirit_id;
/*     */   @DataBaseField(type = "int(11)", fieldname = "time", comment = "获得时间")
/*     */   private int time;
/*     */   
/*     */   public WarSpiritTreasureRecordBO() {
/*  24 */     this.id = 0L;
/*  25 */     this.pid = 0L;
/*  26 */     this.spirit_id = 0;
/*  27 */     this.time = 0;
/*     */   }
/*     */   
/*     */   public WarSpiritTreasureRecordBO(ResultSet rs) throws Exception {
/*  31 */     this.id = rs.getLong(1);
/*  32 */     this.pid = rs.getLong(2);
/*  33 */     this.spirit_id = rs.getInt(3);
/*  34 */     this.time = rs.getInt(4);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<WarSpiritTreasureRecordBO> list) throws Exception {
/*  40 */     list.add(new WarSpiritTreasureRecordBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  45 */     return getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  50 */     return "`id`, `pid`, `spirit_id`, `time`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  55 */     return "`warSpiritTreasureRecord`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  60 */     StringBuilder strBuf = new StringBuilder();
/*  61 */     strBuf.append("'").append(this.id).append("', ");
/*  62 */     strBuf.append("'").append(this.pid).append("', ");
/*  63 */     strBuf.append("'").append(this.spirit_id).append("', ");
/*  64 */     strBuf.append("'").append(this.time).append("', ");
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
/*     */   public int getSpiritId() {
/* 100 */     return this.spirit_id;
/*     */   } public void setSpiritId(int spirit_id) {
/* 102 */     if (spirit_id == this.spirit_id)
/*     */       return; 
/* 104 */     this.spirit_id = spirit_id;
/*     */   }
/*     */   public void saveSpiritId(int spirit_id) {
/* 107 */     if (spirit_id == this.spirit_id)
/*     */       return; 
/* 109 */     this.spirit_id = spirit_id;
/* 110 */     saveField("spirit_id", Integer.valueOf(spirit_id));
/*     */   }
/*     */   
/*     */   public int getTime() {
/* 114 */     return this.time;
/*     */   } public void setTime(int time) {
/* 116 */     if (time == this.time)
/*     */       return; 
/* 118 */     this.time = time;
/*     */   }
/*     */   public void saveTime(int time) {
/* 121 */     if (time == this.time)
/*     */       return; 
/* 123 */     this.time = time;
/* 124 */     saveField("time", Integer.valueOf(time));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 131 */     StringBuilder sBuilder = new StringBuilder();
/* 132 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 133 */     sBuilder.append(" `spirit_id` = '").append(this.spirit_id).append("',");
/* 134 */     sBuilder.append(" `time` = '").append(this.time).append("',");
/* 135 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 136 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 140 */     String sql = "CREATE TABLE IF NOT EXISTS `warSpiritTreasureRecord` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`spirit_id` int(11) NOT NULL DEFAULT '0' COMMENT '战灵id',`time` int(11) NOT NULL DEFAULT '0' COMMENT '获得时间',PRIMARY KEY (`id`)) COMMENT='战灵抽奖表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 146 */       ServerConfig.getInitialID() + 1L);
/* 147 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/WarSpiritTreasureRecordBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */