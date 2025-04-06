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
/*     */ public class StoreRecordBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家ID")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", size = 25, fieldname = "lastRefreshTime", comment = "上次刷新时间,单位秒")
/*     */   private List<Integer> lastRefreshTime;
/*     */   @DataBaseField(type = "int(11)", size = 25, fieldname = "freeRefreshTimes", comment = "当前免费刷新次数(两小时累积一次最大为10)")
/*     */   private List<Integer> freeRefreshTimes;
/*     */   @DataBaseField(type = "int(11)", size = 25, fieldname = "paidRefreshTimes", comment = "当前付费刷新次数(次数越多花费越高最大配置)")
/*     */   private List<Integer> paidRefreshTimes;
/*     */   @DataBaseField(type = "int(11)", size = 25, fieldname = "buyTimes", comment = "当天购买次数")
/*     */   private List<Integer> buyTimes;
/*     */   @DataBaseField(type = "int(11)", size = 25, fieldname = "flushIcon", comment = "是否提示小红点信息")
/*     */   private List<Integer> flushIcon;
/*     */   
/*     */   public StoreRecordBO() {
/*  30 */     this.id = 0L;
/*  31 */     this.pid = 0L;
/*  32 */     this.lastRefreshTime = new ArrayList<>(25); int i;
/*  33 */     for (i = 0; i < 25; i++) {
/*  34 */       this.lastRefreshTime.add(Integer.valueOf(0));
/*     */     }
/*  36 */     this.freeRefreshTimes = new ArrayList<>(25);
/*  37 */     for (i = 0; i < 25; i++) {
/*  38 */       this.freeRefreshTimes.add(Integer.valueOf(0));
/*     */     }
/*  40 */     this.paidRefreshTimes = new ArrayList<>(25);
/*  41 */     for (i = 0; i < 25; i++) {
/*  42 */       this.paidRefreshTimes.add(Integer.valueOf(0));
/*     */     }
/*  44 */     this.buyTimes = new ArrayList<>(25);
/*  45 */     for (i = 0; i < 25; i++) {
/*  46 */       this.buyTimes.add(Integer.valueOf(0));
/*     */     }
/*  48 */     this.flushIcon = new ArrayList<>(25);
/*  49 */     for (i = 0; i < 25; i++) {
/*  50 */       this.flushIcon.add(Integer.valueOf(0));
/*     */     }
/*     */   }
/*     */   
/*     */   public StoreRecordBO(ResultSet rs) throws Exception {
/*  55 */     this.id = rs.getLong(1);
/*  56 */     this.pid = rs.getLong(2);
/*  57 */     this.lastRefreshTime = new ArrayList<>(25); int i;
/*  58 */     for (i = 0; i < 25; i++) {
/*  59 */       this.lastRefreshTime.add(Integer.valueOf(rs.getInt(i + 3)));
/*     */     }
/*  61 */     this.freeRefreshTimes = new ArrayList<>(25);
/*  62 */     for (i = 0; i < 25; i++) {
/*  63 */       this.freeRefreshTimes.add(Integer.valueOf(rs.getInt(i + 28)));
/*     */     }
/*  65 */     this.paidRefreshTimes = new ArrayList<>(25);
/*  66 */     for (i = 0; i < 25; i++) {
/*  67 */       this.paidRefreshTimes.add(Integer.valueOf(rs.getInt(i + 53)));
/*     */     }
/*  69 */     this.buyTimes = new ArrayList<>(25);
/*  70 */     for (i = 0; i < 25; i++) {
/*  71 */       this.buyTimes.add(Integer.valueOf(rs.getInt(i + 78)));
/*     */     }
/*  73 */     this.flushIcon = new ArrayList<>(25);
/*  74 */     for (i = 0; i < 25; i++) {
/*  75 */       this.flushIcon.add(Integer.valueOf(rs.getInt(i + 103)));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<StoreRecordBO> list) throws Exception {
/*  82 */     list.add(new StoreRecordBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  87 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  92 */     return "`id`, `pid`, `lastRefreshTime_0`, `lastRefreshTime_1`, `lastRefreshTime_2`, `lastRefreshTime_3`, `lastRefreshTime_4`, `lastRefreshTime_5`, `lastRefreshTime_6`, `lastRefreshTime_7`, `lastRefreshTime_8`, `lastRefreshTime_9`, `lastRefreshTime_10`, `lastRefreshTime_11`, `lastRefreshTime_12`, `lastRefreshTime_13`, `lastRefreshTime_14`, `lastRefreshTime_15`, `lastRefreshTime_16`, `lastRefreshTime_17`, `lastRefreshTime_18`, `lastRefreshTime_19`, `lastRefreshTime_20`, `lastRefreshTime_21`, `lastRefreshTime_22`, `lastRefreshTime_23`, `lastRefreshTime_24`, `freeRefreshTimes_0`, `freeRefreshTimes_1`, `freeRefreshTimes_2`, `freeRefreshTimes_3`, `freeRefreshTimes_4`, `freeRefreshTimes_5`, `freeRefreshTimes_6`, `freeRefreshTimes_7`, `freeRefreshTimes_8`, `freeRefreshTimes_9`, `freeRefreshTimes_10`, `freeRefreshTimes_11`, `freeRefreshTimes_12`, `freeRefreshTimes_13`, `freeRefreshTimes_14`, `freeRefreshTimes_15`, `freeRefreshTimes_16`, `freeRefreshTimes_17`, `freeRefreshTimes_18`, `freeRefreshTimes_19`, `freeRefreshTimes_20`, `freeRefreshTimes_21`, `freeRefreshTimes_22`, `freeRefreshTimes_23`, `freeRefreshTimes_24`, `paidRefreshTimes_0`, `paidRefreshTimes_1`, `paidRefreshTimes_2`, `paidRefreshTimes_3`, `paidRefreshTimes_4`, `paidRefreshTimes_5`, `paidRefreshTimes_6`, `paidRefreshTimes_7`, `paidRefreshTimes_8`, `paidRefreshTimes_9`, `paidRefreshTimes_10`, `paidRefreshTimes_11`, `paidRefreshTimes_12`, `paidRefreshTimes_13`, `paidRefreshTimes_14`, `paidRefreshTimes_15`, `paidRefreshTimes_16`, `paidRefreshTimes_17`, `paidRefreshTimes_18`, `paidRefreshTimes_19`, `paidRefreshTimes_20`, `paidRefreshTimes_21`, `paidRefreshTimes_22`, `paidRefreshTimes_23`, `paidRefreshTimes_24`, `buyTimes_0`, `buyTimes_1`, `buyTimes_2`, `buyTimes_3`, `buyTimes_4`, `buyTimes_5`, `buyTimes_6`, `buyTimes_7`, `buyTimes_8`, `buyTimes_9`, `buyTimes_10`, `buyTimes_11`, `buyTimes_12`, `buyTimes_13`, `buyTimes_14`, `buyTimes_15`, `buyTimes_16`, `buyTimes_17`, `buyTimes_18`, `buyTimes_19`, `buyTimes_20`, `buyTimes_21`, `buyTimes_22`, `buyTimes_23`, `buyTimes_24`, `flushIcon_0`, `flushIcon_1`, `flushIcon_2`, `flushIcon_3`, `flushIcon_4`, `flushIcon_5`, `flushIcon_6`, `flushIcon_7`, `flushIcon_8`, `flushIcon_9`, `flushIcon_10`, `flushIcon_11`, `flushIcon_12`, `flushIcon_13`, `flushIcon_14`, `flushIcon_15`, `flushIcon_16`, `flushIcon_17`, `flushIcon_18`, `flushIcon_19`, `flushIcon_20`, `flushIcon_21`, `flushIcon_22`, `flushIcon_23`, `flushIcon_24`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  97 */     return "`storeRecord`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/* 102 */     StringBuilder strBuf = new StringBuilder();
/* 103 */     strBuf.append("'").append(this.id).append("', ");
/* 104 */     strBuf.append("'").append(this.pid).append("', "); int i;
/* 105 */     for (i = 0; i < this.lastRefreshTime.size(); i++) {
/* 106 */       strBuf.append("'").append(this.lastRefreshTime.get(i)).append("', ");
/*     */     }
/* 108 */     for (i = 0; i < this.freeRefreshTimes.size(); i++) {
/* 109 */       strBuf.append("'").append(this.freeRefreshTimes.get(i)).append("', ");
/*     */     }
/* 111 */     for (i = 0; i < this.paidRefreshTimes.size(); i++) {
/* 112 */       strBuf.append("'").append(this.paidRefreshTimes.get(i)).append("', ");
/*     */     }
/* 114 */     for (i = 0; i < this.buyTimes.size(); i++) {
/* 115 */       strBuf.append("'").append(this.buyTimes.get(i)).append("', ");
/*     */     }
/* 117 */     for (i = 0; i < this.flushIcon.size(); i++) {
/* 118 */       strBuf.append("'").append(this.flushIcon.get(i)).append("', ");
/*     */     }
/* 120 */     strBuf.deleteCharAt(strBuf.length() - 2);
/* 121 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/* 126 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/* 127 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/* 132 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 137 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 141 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 143 */     if (pid == this.pid)
/*     */       return; 
/* 145 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 148 */     if (pid == this.pid)
/*     */       return; 
/* 150 */     this.pid = pid;
/* 151 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public int getLastRefreshTimeSize() {
/* 155 */     return this.lastRefreshTime.size();
/* 156 */   } public List<Integer> getLastRefreshTimeAll() { return new ArrayList<>(this.lastRefreshTime); }
/* 157 */   public void setLastRefreshTimeAll(int value) { for (int i = 0; i < this.lastRefreshTime.size(); ) { this.lastRefreshTime.set(i, Integer.valueOf(value)); i++; }
/* 158 */      } public void saveLastRefreshTimeAll(int value) { setLastRefreshTimeAll(value); saveAll(); } public int getLastRefreshTime(int index) {
/* 159 */     return ((Integer)this.lastRefreshTime.get(index)).intValue();
/*     */   } public void setLastRefreshTime(int index, int value) {
/* 161 */     if (value == ((Integer)this.lastRefreshTime.get(index)).intValue())
/*     */       return; 
/* 163 */     this.lastRefreshTime.set(index, Integer.valueOf(value));
/*     */   }
/*     */   public void saveLastRefreshTime(int index, int value) {
/* 166 */     if (value == ((Integer)this.lastRefreshTime.get(index)).intValue())
/*     */       return; 
/* 168 */     this.lastRefreshTime.set(index, Integer.valueOf(value));
/* 169 */     saveField("lastRefreshTime_" + index, this.lastRefreshTime.get(index));
/*     */   }
/*     */   
/*     */   public int getFreeRefreshTimesSize() {
/* 173 */     return this.freeRefreshTimes.size();
/* 174 */   } public List<Integer> getFreeRefreshTimesAll() { return new ArrayList<>(this.freeRefreshTimes); }
/* 175 */   public void setFreeRefreshTimesAll(int value) { for (int i = 0; i < this.freeRefreshTimes.size(); ) { this.freeRefreshTimes.set(i, Integer.valueOf(value)); i++; }
/* 176 */      } public void saveFreeRefreshTimesAll(int value) { setFreeRefreshTimesAll(value); saveAll(); } public int getFreeRefreshTimes(int index) {
/* 177 */     return ((Integer)this.freeRefreshTimes.get(index)).intValue();
/*     */   } public void setFreeRefreshTimes(int index, int value) {
/* 179 */     if (value == ((Integer)this.freeRefreshTimes.get(index)).intValue())
/*     */       return; 
/* 181 */     this.freeRefreshTimes.set(index, Integer.valueOf(value));
/*     */   }
/*     */   public void saveFreeRefreshTimes(int index, int value) {
/* 184 */     if (value == ((Integer)this.freeRefreshTimes.get(index)).intValue())
/*     */       return; 
/* 186 */     this.freeRefreshTimes.set(index, Integer.valueOf(value));
/* 187 */     saveField("freeRefreshTimes_" + index, this.freeRefreshTimes.get(index));
/*     */   }
/*     */   
/*     */   public int getPaidRefreshTimesSize() {
/* 191 */     return this.paidRefreshTimes.size();
/* 192 */   } public List<Integer> getPaidRefreshTimesAll() { return new ArrayList<>(this.paidRefreshTimes); }
/* 193 */   public void setPaidRefreshTimesAll(int value) { for (int i = 0; i < this.paidRefreshTimes.size(); ) { this.paidRefreshTimes.set(i, Integer.valueOf(value)); i++; }
/* 194 */      } public void savePaidRefreshTimesAll(int value) { setPaidRefreshTimesAll(value); saveAll(); } public int getPaidRefreshTimes(int index) {
/* 195 */     return ((Integer)this.paidRefreshTimes.get(index)).intValue();
/*     */   } public void setPaidRefreshTimes(int index, int value) {
/* 197 */     if (value == ((Integer)this.paidRefreshTimes.get(index)).intValue())
/*     */       return; 
/* 199 */     this.paidRefreshTimes.set(index, Integer.valueOf(value));
/*     */   }
/*     */   public void savePaidRefreshTimes(int index, int value) {
/* 202 */     if (value == ((Integer)this.paidRefreshTimes.get(index)).intValue())
/*     */       return; 
/* 204 */     this.paidRefreshTimes.set(index, Integer.valueOf(value));
/* 205 */     saveField("paidRefreshTimes_" + index, this.paidRefreshTimes.get(index));
/*     */   }
/*     */   
/*     */   public int getBuyTimesSize() {
/* 209 */     return this.buyTimes.size();
/* 210 */   } public List<Integer> getBuyTimesAll() { return new ArrayList<>(this.buyTimes); }
/* 211 */   public void setBuyTimesAll(int value) { for (int i = 0; i < this.buyTimes.size(); ) { this.buyTimes.set(i, Integer.valueOf(value)); i++; }
/* 212 */      } public void saveBuyTimesAll(int value) { setBuyTimesAll(value); saveAll(); } public int getBuyTimes(int index) {
/* 213 */     return ((Integer)this.buyTimes.get(index)).intValue();
/*     */   } public void setBuyTimes(int index, int value) {
/* 215 */     if (value == ((Integer)this.buyTimes.get(index)).intValue())
/*     */       return; 
/* 217 */     this.buyTimes.set(index, Integer.valueOf(value));
/*     */   }
/*     */   public void saveBuyTimes(int index, int value) {
/* 220 */     if (value == ((Integer)this.buyTimes.get(index)).intValue())
/*     */       return; 
/* 222 */     this.buyTimes.set(index, Integer.valueOf(value));
/* 223 */     saveField("buyTimes_" + index, this.buyTimes.get(index));
/*     */   }
/*     */   
/*     */   public int getFlushIconSize() {
/* 227 */     return this.flushIcon.size();
/* 228 */   } public List<Integer> getFlushIconAll() { return new ArrayList<>(this.flushIcon); }
/* 229 */   public void setFlushIconAll(int value) { for (int i = 0; i < this.flushIcon.size(); ) { this.flushIcon.set(i, Integer.valueOf(value)); i++; }
/* 230 */      } public void saveFlushIconAll(int value) { setFlushIconAll(value); saveAll(); } public int getFlushIcon(int index) {
/* 231 */     return ((Integer)this.flushIcon.get(index)).intValue();
/*     */   } public void setFlushIcon(int index, int value) {
/* 233 */     if (value == ((Integer)this.flushIcon.get(index)).intValue())
/*     */       return; 
/* 235 */     this.flushIcon.set(index, Integer.valueOf(value));
/*     */   }
/*     */   public void saveFlushIcon(int index, int value) {
/* 238 */     if (value == ((Integer)this.flushIcon.get(index)).intValue())
/*     */       return; 
/* 240 */     this.flushIcon.set(index, Integer.valueOf(value));
/* 241 */     saveField("flushIcon_" + index, this.flushIcon.get(index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 248 */     StringBuilder sBuilder = new StringBuilder();
/* 249 */     sBuilder.append(" `pid` = '").append(this.pid).append("',"); int i;
/* 250 */     for (i = 0; i < this.lastRefreshTime.size(); i++) {
/* 251 */       sBuilder.append(" `lastRefreshTime_").append(i).append("` = '").append(this.lastRefreshTime.get(i)).append("',");
/*     */     }
/* 253 */     for (i = 0; i < this.freeRefreshTimes.size(); i++) {
/* 254 */       sBuilder.append(" `freeRefreshTimes_").append(i).append("` = '").append(this.freeRefreshTimes.get(i)).append("',");
/*     */     }
/* 256 */     for (i = 0; i < this.paidRefreshTimes.size(); i++) {
/* 257 */       sBuilder.append(" `paidRefreshTimes_").append(i).append("` = '").append(this.paidRefreshTimes.get(i)).append("',");
/*     */     }
/* 259 */     for (i = 0; i < this.buyTimes.size(); i++) {
/* 260 */       sBuilder.append(" `buyTimes_").append(i).append("` = '").append(this.buyTimes.get(i)).append("',");
/*     */     }
/* 262 */     for (i = 0; i < this.flushIcon.size(); i++) {
/* 263 */       sBuilder.append(" `flushIcon_").append(i).append("` = '").append(this.flushIcon.get(i)).append("',");
/*     */     }
/* 265 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 266 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 270 */     String sql = "CREATE TABLE IF NOT EXISTS `storeRecord` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家ID',`lastRefreshTime_0` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_1` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_2` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_3` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_4` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_5` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_6` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_7` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_8` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_9` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_10` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_11` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_12` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_13` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_14` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_15` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_16` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_17` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_18` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_19` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_20` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_21` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_22` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_23` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`lastRefreshTime_24` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间,单位秒',`freeRefreshTimes_0` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_1` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_2` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_3` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_4` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_5` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_6` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_7` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_8` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_9` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_10` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_11` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_12` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_13` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_14` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_15` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_16` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_17` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_18` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_19` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_20` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_21` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_22` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_23` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`freeRefreshTimes_24` int(11) NOT NULL DEFAULT '0' COMMENT '当前免费刷新次数(两小时累积一次最大为10)',`paidRefreshTimes_0` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_1` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_2` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_3` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_4` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_5` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_6` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_7` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_8` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_9` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_10` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_11` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_12` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_13` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_14` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_15` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_16` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_17` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_18` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_19` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_20` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_21` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_22` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_23` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`paidRefreshTimes_24` int(11) NOT NULL DEFAULT '0' COMMENT '当前付费刷新次数(次数越多花费越高最大配置)',`buyTimes_0` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_1` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_2` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_3` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_4` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_5` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_6` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_7` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_8` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_9` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_10` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_11` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_12` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_13` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_14` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_15` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_16` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_17` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_18` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_19` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_20` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_21` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_22` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_23` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`buyTimes_24` int(11) NOT NULL DEFAULT '0' COMMENT '当天购买次数',`flushIcon_0` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_1` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_2` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_3` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_4` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_5` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_6` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_7` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_8` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_9` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_10` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_11` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_12` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_13` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_14` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_15` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_16` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_17` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_18` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_19` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_20` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_21` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_22` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_23` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',`flushIcon_24` int(11) NOT NULL DEFAULT '0' COMMENT '是否提示小红点信息',UNIQUE INDEX `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='玩家个人商店刷新记录表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 400 */       ServerConfig.getInitialID() + 1L);
/* 401 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/StoreRecordBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */