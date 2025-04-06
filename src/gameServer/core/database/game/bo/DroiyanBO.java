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
/*     */ public class DroiyanBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "point", comment = "决战积分")
/*     */   private int point;
/*     */   @DataBaseField(type = "int(11)", fieldname = "red", comment = "红名值")
/*     */   private int red;
/*     */   @DataBaseField(type = "int(11)", fieldname = "win_times", comment = "连胜次数")
/*     */   private int win_times;
/*     */   @DataBaseField(type = "bigint(20)", size = 3, fieldname = "fighters", comment = "路人")
/*     */   private List<Long> fighters;
/*     */   @DataBaseField(type = "int(11)", fieldname = "last_search_time", comment = "最后检索时间")
/*     */   private int last_search_time;
/*     */   
/*     */   public DroiyanBO() {
/*  30 */     this.id = 0L;
/*  31 */     this.pid = 0L;
/*  32 */     this.point = 0;
/*  33 */     this.red = 0;
/*  34 */     this.win_times = 0;
/*  35 */     this.fighters = new ArrayList<>(3);
/*  36 */     for (int i = 0; i < 3; i++) {
/*  37 */       this.fighters.add(Long.valueOf(0L));
/*     */     }
/*  39 */     this.last_search_time = 0;
/*     */   }
/*     */   
/*     */   public DroiyanBO(ResultSet rs) throws Exception {
/*  43 */     this.id = rs.getLong(1);
/*  44 */     this.pid = rs.getLong(2);
/*  45 */     this.point = rs.getInt(3);
/*  46 */     this.red = rs.getInt(4);
/*  47 */     this.win_times = rs.getInt(5);
/*  48 */     this.fighters = new ArrayList<>(3);
/*  49 */     for (int i = 0; i < 3; i++) {
/*  50 */       this.fighters.add(Long.valueOf(rs.getLong(i + 6)));
/*     */     }
/*  52 */     this.last_search_time = rs.getInt(9);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<DroiyanBO> list) throws Exception {
/*  58 */     list.add(new DroiyanBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  63 */     return getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  68 */     return "`id`, `pid`, `point`, `red`, `win_times`, `fighters_0`, `fighters_1`, `fighters_2`, `last_search_time`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  73 */     return "`droiyan`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  78 */     StringBuilder strBuf = new StringBuilder();
/*  79 */     strBuf.append("'").append(this.id).append("', ");
/*  80 */     strBuf.append("'").append(this.pid).append("', ");
/*  81 */     strBuf.append("'").append(this.point).append("', ");
/*  82 */     strBuf.append("'").append(this.red).append("', ");
/*  83 */     strBuf.append("'").append(this.win_times).append("', ");
/*  84 */     for (int i = 0; i < this.fighters.size(); i++) {
/*  85 */       strBuf.append("'").append(this.fighters.get(i)).append("', ");
/*     */     }
/*  87 */     strBuf.append("'").append(this.last_search_time).append("', ");
/*  88 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  89 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  94 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  95 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/* 100 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 105 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 109 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 111 */     if (pid == this.pid)
/*     */       return; 
/* 113 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 116 */     if (pid == this.pid)
/*     */       return; 
/* 118 */     this.pid = pid;
/* 119 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public int getPoint() {
/* 123 */     return this.point;
/*     */   } public void setPoint(int point) {
/* 125 */     if (point == this.point)
/*     */       return; 
/* 127 */     this.point = point;
/*     */   }
/*     */   public void savePoint(int point) {
/* 130 */     if (point == this.point)
/*     */       return; 
/* 132 */     this.point = point;
/* 133 */     saveField("point", Integer.valueOf(point));
/*     */   }
/*     */   
/*     */   public int getRed() {
/* 137 */     return this.red;
/*     */   } public void setRed(int red) {
/* 139 */     if (red == this.red)
/*     */       return; 
/* 141 */     this.red = red;
/*     */   }
/*     */   public void saveRed(int red) {
/* 144 */     if (red == this.red)
/*     */       return; 
/* 146 */     this.red = red;
/* 147 */     saveField("red", Integer.valueOf(red));
/*     */   }
/*     */   
/*     */   public int getWinTimes() {
/* 151 */     return this.win_times;
/*     */   } public void setWinTimes(int win_times) {
/* 153 */     if (win_times == this.win_times)
/*     */       return; 
/* 155 */     this.win_times = win_times;
/*     */   }
/*     */   public void saveWinTimes(int win_times) {
/* 158 */     if (win_times == this.win_times)
/*     */       return; 
/* 160 */     this.win_times = win_times;
/* 161 */     saveField("win_times", Integer.valueOf(win_times));
/*     */   }
/*     */   
/*     */   public int getFightersSize() {
/* 165 */     return this.fighters.size();
/* 166 */   } public List<Long> getFightersAll() { return new ArrayList<>(this.fighters); }
/* 167 */   public void setFightersAll(long value) { for (int i = 0; i < this.fighters.size(); ) { this.fighters.set(i, Long.valueOf(value)); i++; }
/* 168 */      } public void saveFightersAll(long value) { setFightersAll(value); saveAll(); } public long getFighters(int index) {
/* 169 */     return ((Long)this.fighters.get(index)).longValue();
/*     */   } public void setFighters(int index, long value) {
/* 171 */     if (value == ((Long)this.fighters.get(index)).longValue())
/*     */       return; 
/* 173 */     this.fighters.set(index, Long.valueOf(value));
/*     */   }
/*     */   public void saveFighters(int index, long value) {
/* 176 */     if (value == ((Long)this.fighters.get(index)).longValue())
/*     */       return; 
/* 178 */     this.fighters.set(index, Long.valueOf(value));
/* 179 */     saveField("fighters_" + index, this.fighters.get(index));
/*     */   }
/*     */   
/*     */   public int getLastSearchTime() {
/* 183 */     return this.last_search_time;
/*     */   } public void setLastSearchTime(int last_search_time) {
/* 185 */     if (last_search_time == this.last_search_time)
/*     */       return; 
/* 187 */     this.last_search_time = last_search_time;
/*     */   }
/*     */   public void saveLastSearchTime(int last_search_time) {
/* 190 */     if (last_search_time == this.last_search_time)
/*     */       return; 
/* 192 */     this.last_search_time = last_search_time;
/* 193 */     saveField("last_search_time", Integer.valueOf(last_search_time));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 200 */     StringBuilder sBuilder = new StringBuilder();
/* 201 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 202 */     sBuilder.append(" `point` = '").append(this.point).append("',");
/* 203 */     sBuilder.append(" `red` = '").append(this.red).append("',");
/* 204 */     sBuilder.append(" `win_times` = '").append(this.win_times).append("',");
/* 205 */     for (int i = 0; i < this.fighters.size(); i++) {
/* 206 */       sBuilder.append(" `fighters_").append(i).append("` = '").append(this.fighters.get(i)).append("',");
/*     */     }
/* 208 */     sBuilder.append(" `last_search_time` = '").append(this.last_search_time).append("',");
/* 209 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 210 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 214 */     String sql = "CREATE TABLE IF NOT EXISTS `droiyan` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`point` int(11) NOT NULL DEFAULT '0' COMMENT '决战积分',`red` int(11) NOT NULL DEFAULT '0' COMMENT '红名值',`win_times` int(11) NOT NULL DEFAULT '0' COMMENT '连胜次数',`fighters_0` bigint(20) NOT NULL DEFAULT '0' COMMENT '路人',`fighters_1` bigint(20) NOT NULL DEFAULT '0' COMMENT '路人',`fighters_2` bigint(20) NOT NULL DEFAULT '0' COMMENT '路人',`last_search_time` int(11) NOT NULL DEFAULT '0' COMMENT '最后检索时间',PRIMARY KEY (`id`)) COMMENT='决战系统'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 225 */       ServerConfig.getInitialID() + 1L);
/* 226 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/DroiyanBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */