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
/*     */ public class DroiyanTreasureBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "treasure_id", comment = "获得密保")
/*     */   private int treasure_id;
/*     */   @DataBaseField(type = "int(11)", fieldname = "gain_time", comment = "发生时间")
/*     */   private int gain_time;
/*     */   @DataBaseField(type = "int(11)", fieldname = "expire_time", comment = "过期时间")
/*     */   private int expire_time;
/*     */   
/*     */   public DroiyanTreasureBO() {
/*  26 */     this.id = 0L;
/*  27 */     this.pid = 0L;
/*  28 */     this.treasure_id = 0;
/*  29 */     this.gain_time = 0;
/*  30 */     this.expire_time = 0;
/*     */   }
/*     */   
/*     */   public DroiyanTreasureBO(ResultSet rs) throws Exception {
/*  34 */     this.id = rs.getLong(1);
/*  35 */     this.pid = rs.getLong(2);
/*  36 */     this.treasure_id = rs.getInt(3);
/*  37 */     this.gain_time = rs.getInt(4);
/*  38 */     this.expire_time = rs.getInt(5);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<DroiyanTreasureBO> list) throws Exception {
/*  44 */     list.add(new DroiyanTreasureBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  49 */     return getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  54 */     return "`id`, `pid`, `treasure_id`, `gain_time`, `expire_time`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  59 */     return "`droiyan_treasure`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  64 */     StringBuilder strBuf = new StringBuilder();
/*  65 */     strBuf.append("'").append(this.id).append("', ");
/*  66 */     strBuf.append("'").append(this.pid).append("', ");
/*  67 */     strBuf.append("'").append(this.treasure_id).append("', ");
/*  68 */     strBuf.append("'").append(this.gain_time).append("', ");
/*  69 */     strBuf.append("'").append(this.expire_time).append("', ");
/*  70 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  71 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  76 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  77 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  82 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/*  87 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/*  91 */     return this.pid;
/*     */   } public void setPid(long pid) {
/*  93 */     if (pid == this.pid)
/*     */       return; 
/*  95 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/*  98 */     if (pid == this.pid)
/*     */       return; 
/* 100 */     this.pid = pid;
/* 101 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public int getTreasureId() {
/* 105 */     return this.treasure_id;
/*     */   } public void setTreasureId(int treasure_id) {
/* 107 */     if (treasure_id == this.treasure_id)
/*     */       return; 
/* 109 */     this.treasure_id = treasure_id;
/*     */   }
/*     */   public void saveTreasureId(int treasure_id) {
/* 112 */     if (treasure_id == this.treasure_id)
/*     */       return; 
/* 114 */     this.treasure_id = treasure_id;
/* 115 */     saveField("treasure_id", Integer.valueOf(treasure_id));
/*     */   }
/*     */   
/*     */   public int getGainTime() {
/* 119 */     return this.gain_time;
/*     */   } public void setGainTime(int gain_time) {
/* 121 */     if (gain_time == this.gain_time)
/*     */       return; 
/* 123 */     this.gain_time = gain_time;
/*     */   }
/*     */   public void saveGainTime(int gain_time) {
/* 126 */     if (gain_time == this.gain_time)
/*     */       return; 
/* 128 */     this.gain_time = gain_time;
/* 129 */     saveField("gain_time", Integer.valueOf(gain_time));
/*     */   }
/*     */   
/*     */   public int getExpireTime() {
/* 133 */     return this.expire_time;
/*     */   } public void setExpireTime(int expire_time) {
/* 135 */     if (expire_time == this.expire_time)
/*     */       return; 
/* 137 */     this.expire_time = expire_time;
/*     */   }
/*     */   public void saveExpireTime(int expire_time) {
/* 140 */     if (expire_time == this.expire_time)
/*     */       return; 
/* 142 */     this.expire_time = expire_time;
/* 143 */     saveField("expire_time", Integer.valueOf(expire_time));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 150 */     StringBuilder sBuilder = new StringBuilder();
/* 151 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 152 */     sBuilder.append(" `treasure_id` = '").append(this.treasure_id).append("',");
/* 153 */     sBuilder.append(" `gain_time` = '").append(this.gain_time).append("',");
/* 154 */     sBuilder.append(" `expire_time` = '").append(this.expire_time).append("',");
/* 155 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 156 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 160 */     String sql = "CREATE TABLE IF NOT EXISTS `droiyan_treasure` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`treasure_id` int(11) NOT NULL DEFAULT '0' COMMENT '获得密保',`gain_time` int(11) NOT NULL DEFAULT '0' COMMENT '发生时间',`expire_time` int(11) NOT NULL DEFAULT '0' COMMENT '过期时间',PRIMARY KEY (`id`)) COMMENT='决战记录'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 167 */       ServerConfig.getInitialID() + 1L);
/* 168 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/DroiyanTreasureBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */