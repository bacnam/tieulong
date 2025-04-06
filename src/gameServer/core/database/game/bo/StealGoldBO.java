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
/*     */ public class StealGoldBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "times", comment = "偷取次数")
/*     */   private int times;
/*     */   @DataBaseField(type = "bigint(20)", size = 4, fieldname = "fighters", comment = "路人")
/*     */   private List<Long> fighters;
/*     */   
/*     */   public StealGoldBO() {
/*  24 */     this.id = 0L;
/*  25 */     this.pid = 0L;
/*  26 */     this.times = 0;
/*  27 */     this.fighters = new ArrayList<>(4);
/*  28 */     for (int i = 0; i < 4; i++) {
/*  29 */       this.fighters.add(Long.valueOf(0L));
/*     */     }
/*     */   }
/*     */   
/*     */   public StealGoldBO(ResultSet rs) throws Exception {
/*  34 */     this.id = rs.getLong(1);
/*  35 */     this.pid = rs.getLong(2);
/*  36 */     this.times = rs.getInt(3);
/*  37 */     this.fighters = new ArrayList<>(4);
/*  38 */     for (int i = 0; i < 4; i++) {
/*  39 */       this.fighters.add(Long.valueOf(rs.getLong(i + 4)));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<StealGoldBO> list) throws Exception {
/*  46 */     list.add(new StealGoldBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  51 */     return getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  56 */     return "`id`, `pid`, `times`, `fighters_0`, `fighters_1`, `fighters_2`, `fighters_3`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  61 */     return "`steal_gold`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  66 */     StringBuilder strBuf = new StringBuilder();
/*  67 */     strBuf.append("'").append(this.id).append("', ");
/*  68 */     strBuf.append("'").append(this.pid).append("', ");
/*  69 */     strBuf.append("'").append(this.times).append("', ");
/*  70 */     for (int i = 0; i < this.fighters.size(); i++) {
/*  71 */       strBuf.append("'").append(this.fighters.get(i)).append("', ");
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
/*     */   public int getTimes() {
/* 108 */     return this.times;
/*     */   } public void setTimes(int times) {
/* 110 */     if (times == this.times)
/*     */       return; 
/* 112 */     this.times = times;
/*     */   }
/*     */   public void saveTimes(int times) {
/* 115 */     if (times == this.times)
/*     */       return; 
/* 117 */     this.times = times;
/* 118 */     saveField("times", Integer.valueOf(times));
/*     */   }
/*     */   
/*     */   public int getFightersSize() {
/* 122 */     return this.fighters.size();
/* 123 */   } public List<Long> getFightersAll() { return new ArrayList<>(this.fighters); }
/* 124 */   public void setFightersAll(long value) { for (int i = 0; i < this.fighters.size(); ) { this.fighters.set(i, Long.valueOf(value)); i++; }
/* 125 */      } public void saveFightersAll(long value) { setFightersAll(value); saveAll(); } public long getFighters(int index) {
/* 126 */     return ((Long)this.fighters.get(index)).longValue();
/*     */   } public void setFighters(int index, long value) {
/* 128 */     if (value == ((Long)this.fighters.get(index)).longValue())
/*     */       return; 
/* 130 */     this.fighters.set(index, Long.valueOf(value));
/*     */   }
/*     */   public void saveFighters(int index, long value) {
/* 133 */     if (value == ((Long)this.fighters.get(index)).longValue())
/*     */       return; 
/* 135 */     this.fighters.set(index, Long.valueOf(value));
/* 136 */     saveField("fighters_" + index, this.fighters.get(index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 143 */     StringBuilder sBuilder = new StringBuilder();
/* 144 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 145 */     sBuilder.append(" `times` = '").append(this.times).append("',");
/* 146 */     for (int i = 0; i < this.fighters.size(); i++) {
/* 147 */       sBuilder.append(" `fighters_").append(i).append("` = '").append(this.fighters.get(i)).append("',");
/*     */     }
/* 149 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 150 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 154 */     String sql = "CREATE TABLE IF NOT EXISTS `steal_gold` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`times` int(11) NOT NULL DEFAULT '0' COMMENT '偷取次数',`fighters_0` bigint(20) NOT NULL DEFAULT '0' COMMENT '路人',`fighters_1` bigint(20) NOT NULL DEFAULT '0' COMMENT '路人',`fighters_2` bigint(20) NOT NULL DEFAULT '0' COMMENT '路人',`fighters_3` bigint(20) NOT NULL DEFAULT '0' COMMENT '路人',PRIMARY KEY (`id`)) COMMENT='探金手系统'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 163 */       ServerConfig.getInitialID() + 1L);
/* 164 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/StealGoldBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */