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
/*     */ public class ActivityBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "activity", comment = "活动类型")
/*     */   private String activity;
/*     */   @DataBaseField(type = "tinyint(1)", fieldname = "isActive", comment = "是否启用")
/*     */   private boolean isActive;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "lastStatus", comment = "上次检测的时候的状态")
/*     */   private String lastStatus;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "gm_id", comment = "GM后台对应的id")
/*     */   private String gm_id;
/*     */   @DataBaseField(type = "int(11)", fieldname = "beginTime", comment = "活动开启时间")
/*     */   private int beginTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "endTime", comment = "活动结束时间-可能客户端还需要显示")
/*     */   private int endTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "closeTime", comment = "活动关闭时间-活动彻底关闭，客户端不再显示")
/*     */   private int closeTime;
/*     */   @DataBaseField(type = "text(500)", fieldname = "json", comment = "活动具体配置")
/*     */   private String json;
/*     */   @DataBaseField(type = "int(11)", fieldname = "joinIn", comment = "活动参与人数")
/*     */   private int joinIn;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "winnerPid", comment = "活动赢家pid")
/*     */   private long winnerPid;
/*     */   @DataBaseField(type = "int(11)", size = 10, fieldname = "extInt", comment = "活动int型扩展字段信息")
/*     */   private List<Integer> extInt;
/*     */   @DataBaseField(type = "varchar(500)", size = 10, fieldname = "extStr", comment = "活动string型扩展字段信息")
/*     */   private List<String> extStr;
/*     */   
/*     */   public ActivityBO() {
/*  42 */     this.id = 0L;
/*  43 */     this.activity = "";
/*  44 */     this.isActive = false;
/*  45 */     this.lastStatus = "";
/*  46 */     this.gm_id = "";
/*  47 */     this.beginTime = 0;
/*  48 */     this.endTime = 0;
/*  49 */     this.closeTime = 0;
/*  50 */     this.json = "";
/*  51 */     this.joinIn = 0;
/*  52 */     this.winnerPid = 0L;
/*  53 */     this.extInt = new ArrayList<>(10); int i;
/*  54 */     for (i = 0; i < 10; i++) {
/*  55 */       this.extInt.add(Integer.valueOf(0));
/*     */     }
/*  57 */     this.extStr = new ArrayList<>(10);
/*  58 */     for (i = 0; i < 10; i++) {
/*  59 */       this.extStr.add("");
/*     */     }
/*     */   }
/*     */   
/*     */   public ActivityBO(ResultSet rs) throws Exception {
/*  64 */     this.id = rs.getLong(1);
/*  65 */     this.activity = rs.getString(2);
/*  66 */     this.isActive = rs.getBoolean(3);
/*  67 */     this.lastStatus = rs.getString(4);
/*  68 */     this.gm_id = rs.getString(5);
/*  69 */     this.beginTime = rs.getInt(6);
/*  70 */     this.endTime = rs.getInt(7);
/*  71 */     this.closeTime = rs.getInt(8);
/*  72 */     this.json = rs.getString(9);
/*  73 */     this.joinIn = rs.getInt(10);
/*  74 */     this.winnerPid = rs.getLong(11);
/*  75 */     this.extInt = new ArrayList<>(10); int i;
/*  76 */     for (i = 0; i < 10; i++) {
/*  77 */       this.extInt.add(Integer.valueOf(rs.getInt(i + 12)));
/*     */     }
/*  79 */     this.extStr = new ArrayList<>(10);
/*  80 */     for (i = 0; i < 10; i++) {
/*  81 */       this.extStr.add(rs.getString(i + 22));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<ActivityBO> list) throws Exception {
/*  88 */     list.add(new ActivityBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  93 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  98 */     return "`id`, `activity`, `isActive`, `lastStatus`, `gm_id`, `beginTime`, `endTime`, `closeTime`, `json`, `joinIn`, `winnerPid`, `extInt_0`, `extInt_1`, `extInt_2`, `extInt_3`, `extInt_4`, `extInt_5`, `extInt_6`, `extInt_7`, `extInt_8`, `extInt_9`, `extStr_0`, `extStr_1`, `extStr_2`, `extStr_3`, `extStr_4`, `extStr_5`, `extStr_6`, `extStr_7`, `extStr_8`, `extStr_9`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/* 103 */     return "`activity`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/* 108 */     StringBuilder strBuf = new StringBuilder();
/* 109 */     strBuf.append("'").append(this.id).append("', ");
/* 110 */     strBuf.append("'").append((this.activity == null) ? null : this.activity.replace("'", "''")).append("', ");
/* 111 */     strBuf.append("'").append(this.isActive ? 1 : 0).append("', ");
/* 112 */     strBuf.append("'").append((this.lastStatus == null) ? null : this.lastStatus.replace("'", "''")).append("', ");
/* 113 */     strBuf.append("'").append((this.gm_id == null) ? null : this.gm_id.replace("'", "''")).append("', ");
/* 114 */     strBuf.append("'").append(this.beginTime).append("', ");
/* 115 */     strBuf.append("'").append(this.endTime).append("', ");
/* 116 */     strBuf.append("'").append(this.closeTime).append("', ");
/* 117 */     strBuf.append("'").append((this.json == null) ? null : this.json.replace("'", "''")).append("', ");
/* 118 */     strBuf.append("'").append(this.joinIn).append("', ");
/* 119 */     strBuf.append("'").append(this.winnerPid).append("', "); int i;
/* 120 */     for (i = 0; i < this.extInt.size(); i++) {
/* 121 */       strBuf.append("'").append(this.extInt.get(i)).append("', ");
/*     */     }
/* 123 */     for (i = 0; i < this.extStr.size(); i++) {
/* 124 */       strBuf.append("'").append((this.extStr.get(i) == null) ? null : ((String)this.extStr.get(i)).replace("'", "''")).append("', ");
/*     */     }
/* 126 */     strBuf.deleteCharAt(strBuf.length() - 2);
/* 127 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/* 132 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/* 133 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/* 138 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 143 */     return this.id;
/*     */   }
/*     */   
/*     */   public String getActivity() {
/* 147 */     return this.activity;
/*     */   } public void setActivity(String activity) {
/* 149 */     if (activity.equals(this.activity))
/*     */       return; 
/* 151 */     this.activity = activity;
/*     */   }
/*     */   public void saveActivity(String activity) {
/* 154 */     if (activity.equals(this.activity))
/*     */       return; 
/* 156 */     this.activity = activity;
/* 157 */     saveField("activity", activity);
/*     */   }
/*     */   
/*     */   public boolean getIsActive() {
/* 161 */     return this.isActive;
/*     */   } public void setIsActive(boolean isActive) {
/* 163 */     if (isActive == this.isActive)
/*     */       return; 
/* 165 */     this.isActive = isActive;
/*     */   }
/*     */   public void saveIsActive(boolean isActive) {
/* 168 */     if (isActive == this.isActive)
/*     */       return; 
/* 170 */     this.isActive = isActive;
/* 171 */     saveField("isActive", Integer.valueOf(isActive ? 1 : 0));
/*     */   }
/*     */   
/*     */   public String getLastStatus() {
/* 175 */     return this.lastStatus;
/*     */   } public void setLastStatus(String lastStatus) {
/* 177 */     if (lastStatus.equals(this.lastStatus))
/*     */       return; 
/* 179 */     this.lastStatus = lastStatus;
/*     */   }
/*     */   public void saveLastStatus(String lastStatus) {
/* 182 */     if (lastStatus.equals(this.lastStatus))
/*     */       return; 
/* 184 */     this.lastStatus = lastStatus;
/* 185 */     saveField("lastStatus", lastStatus);
/*     */   }
/*     */   
/*     */   public String getGmId() {
/* 189 */     return this.gm_id;
/*     */   } public void setGmId(String gm_id) {
/* 191 */     if (gm_id.equals(this.gm_id))
/*     */       return; 
/* 193 */     this.gm_id = gm_id;
/*     */   }
/*     */   public void saveGmId(String gm_id) {
/* 196 */     if (gm_id.equals(this.gm_id))
/*     */       return; 
/* 198 */     this.gm_id = gm_id;
/* 199 */     saveField("gm_id", gm_id);
/*     */   }
/*     */   
/*     */   public int getBeginTime() {
/* 203 */     return this.beginTime;
/*     */   } public void setBeginTime(int beginTime) {
/* 205 */     if (beginTime == this.beginTime)
/*     */       return; 
/* 207 */     this.beginTime = beginTime;
/*     */   }
/*     */   public void saveBeginTime(int beginTime) {
/* 210 */     if (beginTime == this.beginTime)
/*     */       return; 
/* 212 */     this.beginTime = beginTime;
/* 213 */     saveField("beginTime", Integer.valueOf(beginTime));
/*     */   }
/*     */   
/*     */   public int getEndTime() {
/* 217 */     return this.endTime;
/*     */   } public void setEndTime(int endTime) {
/* 219 */     if (endTime == this.endTime)
/*     */       return; 
/* 221 */     this.endTime = endTime;
/*     */   }
/*     */   public void saveEndTime(int endTime) {
/* 224 */     if (endTime == this.endTime)
/*     */       return; 
/* 226 */     this.endTime = endTime;
/* 227 */     saveField("endTime", Integer.valueOf(endTime));
/*     */   }
/*     */   
/*     */   public int getCloseTime() {
/* 231 */     return this.closeTime;
/*     */   } public void setCloseTime(int closeTime) {
/* 233 */     if (closeTime == this.closeTime)
/*     */       return; 
/* 235 */     this.closeTime = closeTime;
/*     */   }
/*     */   public void saveCloseTime(int closeTime) {
/* 238 */     if (closeTime == this.closeTime)
/*     */       return; 
/* 240 */     this.closeTime = closeTime;
/* 241 */     saveField("closeTime", Integer.valueOf(closeTime));
/*     */   }
/*     */   
/*     */   public String getJson() {
/* 245 */     return this.json;
/*     */   } public void setJson(String json) {
/* 247 */     if (json.equals(this.json))
/*     */       return; 
/* 249 */     this.json = json;
/*     */   }
/*     */   public void saveJson(String json) {
/* 252 */     if (json.equals(this.json))
/*     */       return; 
/* 254 */     this.json = json;
/* 255 */     saveField("json", json);
/*     */   }
/*     */   
/*     */   public int getJoinIn() {
/* 259 */     return this.joinIn;
/*     */   } public void setJoinIn(int joinIn) {
/* 261 */     if (joinIn == this.joinIn)
/*     */       return; 
/* 263 */     this.joinIn = joinIn;
/*     */   }
/*     */   public void saveJoinIn(int joinIn) {
/* 266 */     if (joinIn == this.joinIn)
/*     */       return; 
/* 268 */     this.joinIn = joinIn;
/* 269 */     saveField("joinIn", Integer.valueOf(joinIn));
/*     */   }
/*     */   
/*     */   public long getWinnerPid() {
/* 273 */     return this.winnerPid;
/*     */   } public void setWinnerPid(long winnerPid) {
/* 275 */     if (winnerPid == this.winnerPid)
/*     */       return; 
/* 277 */     this.winnerPid = winnerPid;
/*     */   }
/*     */   public void saveWinnerPid(long winnerPid) {
/* 280 */     if (winnerPid == this.winnerPid)
/*     */       return; 
/* 282 */     this.winnerPid = winnerPid;
/* 283 */     saveField("winnerPid", Long.valueOf(winnerPid));
/*     */   }
/*     */   
/*     */   public int getExtIntSize() {
/* 287 */     return this.extInt.size();
/* 288 */   } public List<Integer> getExtIntAll() { return new ArrayList<>(this.extInt); }
/* 289 */   public void setExtIntAll(int value) { for (int i = 0; i < this.extInt.size(); ) { this.extInt.set(i, Integer.valueOf(value)); i++; }
/* 290 */      } public void saveExtIntAll(int value) { setExtIntAll(value); saveAll(); } public int getExtInt(int index) {
/* 291 */     return ((Integer)this.extInt.get(index)).intValue();
/*     */   } public void setExtInt(int index, int value) {
/* 293 */     if (value == ((Integer)this.extInt.get(index)).intValue())
/*     */       return; 
/* 295 */     this.extInt.set(index, Integer.valueOf(value));
/*     */   }
/*     */   public void saveExtInt(int index, int value) {
/* 298 */     if (value == ((Integer)this.extInt.get(index)).intValue())
/*     */       return; 
/* 300 */     this.extInt.set(index, Integer.valueOf(value));
/* 301 */     saveField("extInt_" + index, this.extInt.get(index));
/*     */   }
/*     */   
/*     */   public int getExtStrSize() {
/* 305 */     return this.extStr.size();
/* 306 */   } public List<String> getExtStrAll() { return new ArrayList<>(this.extStr); }
/* 307 */   public void setExtStrAll(String value) { for (int i = 0; i < this.extStr.size(); ) { this.extStr.set(i, value); i++; }
/* 308 */      } public void saveExtStrAll(String value) { setExtStrAll(value); saveAll(); } public String getExtStr(int index) {
/* 309 */     return this.extStr.get(index);
/*     */   } public void setExtStr(int index, String value) {
/* 311 */     if (value.equals(this.extStr.get(index)))
/*     */       return; 
/* 313 */     this.extStr.set(index, value);
/*     */   }
/*     */   public void saveExtStr(int index, String value) {
/* 316 */     if (value.equals(this.extStr.get(index)))
/*     */       return; 
/* 318 */     this.extStr.set(index, value);
/* 319 */     saveField("extStr_" + index, this.extStr.get(index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 326 */     StringBuilder sBuilder = new StringBuilder();
/* 327 */     sBuilder.append(" `activity` = '").append((this.activity == null) ? null : this.activity.replace("'", "''")).append("',");
/* 328 */     sBuilder.append(" `isActive` = '").append(this.isActive ? 1 : 0).append("',");
/* 329 */     sBuilder.append(" `lastStatus` = '").append((this.lastStatus == null) ? null : this.lastStatus.replace("'", "''")).append("',");
/* 330 */     sBuilder.append(" `gm_id` = '").append((this.gm_id == null) ? null : this.gm_id.replace("'", "''")).append("',");
/* 331 */     sBuilder.append(" `beginTime` = '").append(this.beginTime).append("',");
/* 332 */     sBuilder.append(" `endTime` = '").append(this.endTime).append("',");
/* 333 */     sBuilder.append(" `closeTime` = '").append(this.closeTime).append("',");
/* 334 */     sBuilder.append(" `json` = '").append((this.json == null) ? null : this.json.replace("'", "''")).append("',");
/* 335 */     sBuilder.append(" `joinIn` = '").append(this.joinIn).append("',");
/* 336 */     sBuilder.append(" `winnerPid` = '").append(this.winnerPid).append("',"); int i;
/* 337 */     for (i = 0; i < this.extInt.size(); i++) {
/* 338 */       sBuilder.append(" `extInt_").append(i).append("` = '").append(this.extInt.get(i)).append("',");
/*     */     }
/* 340 */     for (i = 0; i < this.extStr.size(); i++) {
/* 341 */       sBuilder.append(" `extStr_").append(i).append("` = '").append((this.extStr == null) ? null : ((String)this.extStr.get(i)).replace("'", "''")).append("',");
/*     */     }
/* 343 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 344 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 348 */     String sql = "CREATE TABLE IF NOT EXISTS `activity` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`activity` varchar(500) NOT NULL DEFAULT '' COMMENT '活动类型',`isActive` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否启用',`lastStatus` varchar(500) NOT NULL DEFAULT '' COMMENT '上次检测的时候的状态',`gm_id` varchar(500) NOT NULL DEFAULT '' COMMENT 'GM后台对应的id',`beginTime` int(11) NOT NULL DEFAULT '0' COMMENT '活动开启时间',`endTime` int(11) NOT NULL DEFAULT '0' COMMENT '活动结束时间-可能客户端还需要显示',`closeTime` int(11) NOT NULL DEFAULT '0' COMMENT '活动关闭时间-活动彻底关闭，客户端不再显示',`json` text NULL COMMENT '活动具体配置',`joinIn` int(11) NOT NULL DEFAULT '0' COMMENT '活动参与人数',`winnerPid` bigint(20) NOT NULL DEFAULT '0' COMMENT '活动赢家pid',`extInt_0` int(11) NOT NULL DEFAULT '0' COMMENT '活动int型扩展字段信息',`extInt_1` int(11) NOT NULL DEFAULT '0' COMMENT '活动int型扩展字段信息',`extInt_2` int(11) NOT NULL DEFAULT '0' COMMENT '活动int型扩展字段信息',`extInt_3` int(11) NOT NULL DEFAULT '0' COMMENT '活动int型扩展字段信息',`extInt_4` int(11) NOT NULL DEFAULT '0' COMMENT '活动int型扩展字段信息',`extInt_5` int(11) NOT NULL DEFAULT '0' COMMENT '活动int型扩展字段信息',`extInt_6` int(11) NOT NULL DEFAULT '0' COMMENT '活动int型扩展字段信息',`extInt_7` int(11) NOT NULL DEFAULT '0' COMMENT '活动int型扩展字段信息',`extInt_8` int(11) NOT NULL DEFAULT '0' COMMENT '活动int型扩展字段信息',`extInt_9` int(11) NOT NULL DEFAULT '0' COMMENT '活动int型扩展字段信息',`extStr_0` varchar(500) NOT NULL DEFAULT '' COMMENT '活动string型扩展字段信息',`extStr_1` varchar(500) NOT NULL DEFAULT '' COMMENT '活动string型扩展字段信息',`extStr_2` varchar(500) NOT NULL DEFAULT '' COMMENT '活动string型扩展字段信息',`extStr_3` varchar(500) NOT NULL DEFAULT '' COMMENT '活动string型扩展字段信息',`extStr_4` varchar(500) NOT NULL DEFAULT '' COMMENT '活动string型扩展字段信息',`extStr_5` varchar(500) NOT NULL DEFAULT '' COMMENT '活动string型扩展字段信息',`extStr_6` varchar(500) NOT NULL DEFAULT '' COMMENT '活动string型扩展字段信息',`extStr_7` varchar(500) NOT NULL DEFAULT '' COMMENT '活动string型扩展字段信息',`extStr_8` varchar(500) NOT NULL DEFAULT '' COMMENT '活动string型扩展字段信息',`extStr_9` varchar(500) NOT NULL DEFAULT '' COMMENT '活动string型扩展字段信息',PRIMARY KEY (`id`)) COMMENT='活动信息表，记录每个活动基本信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 381 */       ServerConfig.getInitialID() + 1L);
/* 382 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/ActivityBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */