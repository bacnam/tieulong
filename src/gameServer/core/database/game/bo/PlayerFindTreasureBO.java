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
/*     */ public class PlayerFindTreasureBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家pid")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "total", comment = "累计寻宝次数")
/*     */   private int total;
/*     */   @DataBaseField(type = "int(11)", fieldname = "times", comment = "每日单抽寻宝次数")
/*     */   private int times;
/*     */   @DataBaseField(type = "int(11)", fieldname = "tentimes", comment = "每日十连抽寻宝次数")
/*     */   private int tentimes;
/*     */   @DataBaseField(type = "int(11)", fieldname = "warspiritTotal", comment = "战灵累计寻宝次数")
/*     */   private int warspiritTotal;
/*     */   
/*     */   public PlayerFindTreasureBO() {
/*  28 */     this.id = 0L;
/*  29 */     this.pid = 0L;
/*  30 */     this.total = 0;
/*  31 */     this.times = 0;
/*  32 */     this.tentimes = 0;
/*  33 */     this.warspiritTotal = 0;
/*     */   }
/*     */   
/*     */   public PlayerFindTreasureBO(ResultSet rs) throws Exception {
/*  37 */     this.id = rs.getLong(1);
/*  38 */     this.pid = rs.getLong(2);
/*  39 */     this.total = rs.getInt(3);
/*  40 */     this.times = rs.getInt(4);
/*  41 */     this.tentimes = rs.getInt(5);
/*  42 */     this.warspiritTotal = rs.getInt(6);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<PlayerFindTreasureBO> list) throws Exception {
/*  48 */     list.add(new PlayerFindTreasureBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  53 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  58 */     return "`id`, `pid`, `total`, `times`, `tentimes`, `warspiritTotal`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  63 */     return "`player_find_treasure`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  68 */     StringBuilder strBuf = new StringBuilder();
/*  69 */     strBuf.append("'").append(this.id).append("', ");
/*  70 */     strBuf.append("'").append(this.pid).append("', ");
/*  71 */     strBuf.append("'").append(this.total).append("', ");
/*  72 */     strBuf.append("'").append(this.times).append("', ");
/*  73 */     strBuf.append("'").append(this.tentimes).append("', ");
/*  74 */     strBuf.append("'").append(this.warspiritTotal).append("', ");
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
/*     */   public int getTotal() {
/* 110 */     return this.total;
/*     */   } public void setTotal(int total) {
/* 112 */     if (total == this.total)
/*     */       return; 
/* 114 */     this.total = total;
/*     */   }
/*     */   public void saveTotal(int total) {
/* 117 */     if (total == this.total)
/*     */       return; 
/* 119 */     this.total = total;
/* 120 */     saveField("total", Integer.valueOf(total));
/*     */   }
/*     */   
/*     */   public int getTimes() {
/* 124 */     return this.times;
/*     */   } public void setTimes(int times) {
/* 126 */     if (times == this.times)
/*     */       return; 
/* 128 */     this.times = times;
/*     */   }
/*     */   public void saveTimes(int times) {
/* 131 */     if (times == this.times)
/*     */       return; 
/* 133 */     this.times = times;
/* 134 */     saveField("times", Integer.valueOf(times));
/*     */   }
/*     */   
/*     */   public int getTentimes() {
/* 138 */     return this.tentimes;
/*     */   } public void setTentimes(int tentimes) {
/* 140 */     if (tentimes == this.tentimes)
/*     */       return; 
/* 142 */     this.tentimes = tentimes;
/*     */   }
/*     */   public void saveTentimes(int tentimes) {
/* 145 */     if (tentimes == this.tentimes)
/*     */       return; 
/* 147 */     this.tentimes = tentimes;
/* 148 */     saveField("tentimes", Integer.valueOf(tentimes));
/*     */   }
/*     */   
/*     */   public int getWarspiritTotal() {
/* 152 */     return this.warspiritTotal;
/*     */   } public void setWarspiritTotal(int warspiritTotal) {
/* 154 */     if (warspiritTotal == this.warspiritTotal)
/*     */       return; 
/* 156 */     this.warspiritTotal = warspiritTotal;
/*     */   }
/*     */   public void saveWarspiritTotal(int warspiritTotal) {
/* 159 */     if (warspiritTotal == this.warspiritTotal)
/*     */       return; 
/* 161 */     this.warspiritTotal = warspiritTotal;
/* 162 */     saveField("warspiritTotal", Integer.valueOf(warspiritTotal));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 169 */     StringBuilder sBuilder = new StringBuilder();
/* 170 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 171 */     sBuilder.append(" `total` = '").append(this.total).append("',");
/* 172 */     sBuilder.append(" `times` = '").append(this.times).append("',");
/* 173 */     sBuilder.append(" `tentimes` = '").append(this.tentimes).append("',");
/* 174 */     sBuilder.append(" `warspiritTotal` = '").append(this.warspiritTotal).append("',");
/* 175 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 176 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 180 */     String sql = "CREATE TABLE IF NOT EXISTS `player_find_treasure` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家pid',`total` int(11) NOT NULL DEFAULT '0' COMMENT '累计寻宝次数',`times` int(11) NOT NULL DEFAULT '0' COMMENT '每日单抽寻宝次数',`tentimes` int(11) NOT NULL DEFAULT '0' COMMENT '每日十连抽寻宝次数',`warspiritTotal` int(11) NOT NULL DEFAULT '0' COMMENT '战灵累计寻宝次数',PRIMARY KEY (`id`)) COMMENT='玩家寻宝信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/PlayerFindTreasureBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */