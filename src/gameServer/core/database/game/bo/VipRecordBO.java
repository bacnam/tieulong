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
/*     */ public class VipRecordBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家ID")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "totalRecharge", comment = "累计充值,用于计算vip等级")
/*     */   private int totalRecharge;
/*     */   @DataBaseField(type = "int(11)", size = 17, fieldname = "lastFetchPrivateTime", comment = "特权礼包时间[秒],包含0级")
/*     */   private List<Integer> lastFetchPrivateTime;
/*     */   
/*     */   public VipRecordBO() {
/*  24 */     this.id = 0L;
/*  25 */     this.pid = 0L;
/*  26 */     this.totalRecharge = 0;
/*  27 */     this.lastFetchPrivateTime = new ArrayList<>(17);
/*  28 */     for (int i = 0; i < 17; i++) {
/*  29 */       this.lastFetchPrivateTime.add(Integer.valueOf(0));
/*     */     }
/*     */   }
/*     */   
/*     */   public VipRecordBO(ResultSet rs) throws Exception {
/*  34 */     this.id = rs.getLong(1);
/*  35 */     this.pid = rs.getLong(2);
/*  36 */     this.totalRecharge = rs.getInt(3);
/*  37 */     this.lastFetchPrivateTime = new ArrayList<>(17);
/*  38 */     for (int i = 0; i < 17; i++) {
/*  39 */       this.lastFetchPrivateTime.add(Integer.valueOf(rs.getInt(i + 4)));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<VipRecordBO> list) throws Exception {
/*  46 */     list.add(new VipRecordBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  51 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  56 */     return "`id`, `pid`, `totalRecharge`, `lastFetchPrivateTime_0`, `lastFetchPrivateTime_1`, `lastFetchPrivateTime_2`, `lastFetchPrivateTime_3`, `lastFetchPrivateTime_4`, `lastFetchPrivateTime_5`, `lastFetchPrivateTime_6`, `lastFetchPrivateTime_7`, `lastFetchPrivateTime_8`, `lastFetchPrivateTime_9`, `lastFetchPrivateTime_10`, `lastFetchPrivateTime_11`, `lastFetchPrivateTime_12`, `lastFetchPrivateTime_13`, `lastFetchPrivateTime_14`, `lastFetchPrivateTime_15`, `lastFetchPrivateTime_16`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  61 */     return "`vipRecord`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  66 */     StringBuilder strBuf = new StringBuilder();
/*  67 */     strBuf.append("'").append(this.id).append("', ");
/*  68 */     strBuf.append("'").append(this.pid).append("', ");
/*  69 */     strBuf.append("'").append(this.totalRecharge).append("', ");
/*  70 */     for (int i = 0; i < this.lastFetchPrivateTime.size(); i++) {
/*  71 */       strBuf.append("'").append(this.lastFetchPrivateTime.get(i)).append("', ");
/*     */     }
/*  73 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  74 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  79 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  80 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  85 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/*  90 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/*  94 */     return this.pid;
/*     */   } public void setPid(long pid) {
/*  96 */     if (pid == this.pid)
/*     */       return; 
/*  98 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 101 */     if (pid == this.pid)
/*     */       return; 
/* 103 */     this.pid = pid;
/* 104 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public int getTotalRecharge() {
/* 108 */     return this.totalRecharge;
/*     */   } public void setTotalRecharge(int totalRecharge) {
/* 110 */     if (totalRecharge == this.totalRecharge)
/*     */       return; 
/* 112 */     this.totalRecharge = totalRecharge;
/*     */   }
/*     */   public void saveTotalRecharge(int totalRecharge) {
/* 115 */     if (totalRecharge == this.totalRecharge)
/*     */       return; 
/* 117 */     this.totalRecharge = totalRecharge;
/* 118 */     saveField("totalRecharge", Integer.valueOf(totalRecharge));
/*     */   }
/*     */   
/*     */   public int getLastFetchPrivateTimeSize() {
/* 122 */     return this.lastFetchPrivateTime.size();
/* 123 */   } public List<Integer> getLastFetchPrivateTimeAll() { return new ArrayList<>(this.lastFetchPrivateTime); }
/* 124 */   public void setLastFetchPrivateTimeAll(int value) { for (int i = 0; i < this.lastFetchPrivateTime.size(); ) { this.lastFetchPrivateTime.set(i, Integer.valueOf(value)); i++; }
/* 125 */      } public void saveLastFetchPrivateTimeAll(int value) { setLastFetchPrivateTimeAll(value); saveAll(); } public int getLastFetchPrivateTime(int index) {
/* 126 */     return ((Integer)this.lastFetchPrivateTime.get(index)).intValue();
/*     */   } public void setLastFetchPrivateTime(int index, int value) {
/* 128 */     if (value == ((Integer)this.lastFetchPrivateTime.get(index)).intValue())
/*     */       return; 
/* 130 */     this.lastFetchPrivateTime.set(index, Integer.valueOf(value));
/*     */   }
/*     */   public void saveLastFetchPrivateTime(int index, int value) {
/* 133 */     if (value == ((Integer)this.lastFetchPrivateTime.get(index)).intValue())
/*     */       return; 
/* 135 */     this.lastFetchPrivateTime.set(index, Integer.valueOf(value));
/* 136 */     saveField("lastFetchPrivateTime_" + index, this.lastFetchPrivateTime.get(index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 143 */     StringBuilder sBuilder = new StringBuilder();
/* 144 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 145 */     sBuilder.append(" `totalRecharge` = '").append(this.totalRecharge).append("',");
/* 146 */     for (int i = 0; i < this.lastFetchPrivateTime.size(); i++) {
/* 147 */       sBuilder.append(" `lastFetchPrivateTime_").append(i).append("` = '").append(this.lastFetchPrivateTime.get(i)).append("',");
/*     */     }
/* 149 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 150 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 154 */     String sql = "CREATE TABLE IF NOT EXISTS `vipRecord` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家ID',`totalRecharge` int(11) NOT NULL DEFAULT '0' COMMENT '累计充值,用于计算vip等级',`lastFetchPrivateTime_0` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_1` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_2` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_3` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_4` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_5` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_6` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_7` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_8` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_9` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_10` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_11` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_12` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_13` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_14` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_15` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',`lastFetchPrivateTime_16` int(11) NOT NULL DEFAULT '0' COMMENT '特权礼包时间[秒],包含0级',UNIQUE INDEX `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='VIP信息相关记录表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 177 */       ServerConfig.getInitialID() + 1L);
/* 178 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/VipRecordBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */