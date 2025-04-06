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
/*     */ public class ActivityRecordBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "所属玩家pid")
/*     */   private long pid;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "aid", comment = "对应活动主键Id")
/*     */   private long aid;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "activity", comment = "所属活动")
/*     */   private String activity;
/*     */   @DataBaseField(type = "int(11)", size = 5, fieldname = "extInt", comment = "int型扩展字段")
/*     */   private List<Integer> extInt;
/*     */   @DataBaseField(type = "varchar(500)", size = 4, fieldname = "extStr", comment = "string型扩展字段")
/*     */   private List<String> extStr;
/*     */   
/*     */   public ActivityRecordBO() {
/*  28 */     this.id = 0L;
/*  29 */     this.pid = 0L;
/*  30 */     this.aid = 0L;
/*  31 */     this.activity = "";
/*  32 */     this.extInt = new ArrayList<>(5); int i;
/*  33 */     for (i = 0; i < 5; i++) {
/*  34 */       this.extInt.add(Integer.valueOf(0));
/*     */     }
/*  36 */     this.extStr = new ArrayList<>(4);
/*  37 */     for (i = 0; i < 4; i++) {
/*  38 */       this.extStr.add("");
/*     */     }
/*     */   }
/*     */   
/*     */   public ActivityRecordBO(ResultSet rs) throws Exception {
/*  43 */     this.id = rs.getLong(1);
/*  44 */     this.pid = rs.getLong(2);
/*  45 */     this.aid = rs.getLong(3);
/*  46 */     this.activity = rs.getString(4);
/*  47 */     this.extInt = new ArrayList<>(5); int i;
/*  48 */     for (i = 0; i < 5; i++) {
/*  49 */       this.extInt.add(Integer.valueOf(rs.getInt(i + 5)));
/*     */     }
/*  51 */     this.extStr = new ArrayList<>(4);
/*  52 */     for (i = 0; i < 4; i++) {
/*  53 */       this.extStr.add(rs.getString(i + 10));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<ActivityRecordBO> list) throws Exception {
/*  60 */     list.add(new ActivityRecordBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  65 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  70 */     return "`id`, `pid`, `aid`, `activity`, `extInt_0`, `extInt_1`, `extInt_2`, `extInt_3`, `extInt_4`, `extStr_0`, `extStr_1`, `extStr_2`, `extStr_3`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  75 */     return "`activityRecord`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  80 */     StringBuilder strBuf = new StringBuilder();
/*  81 */     strBuf.append("'").append(this.id).append("', ");
/*  82 */     strBuf.append("'").append(this.pid).append("', ");
/*  83 */     strBuf.append("'").append(this.aid).append("', ");
/*  84 */     strBuf.append("'").append((this.activity == null) ? null : this.activity.replace("'", "''")).append("', "); int i;
/*  85 */     for (i = 0; i < this.extInt.size(); i++) {
/*  86 */       strBuf.append("'").append(this.extInt.get(i)).append("', ");
/*     */     }
/*  88 */     for (i = 0; i < this.extStr.size(); i++) {
/*  89 */       strBuf.append("'").append((this.extStr.get(i) == null) ? null : ((String)this.extStr.get(i)).replace("'", "''")).append("', ");
/*     */     }
/*  91 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  92 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  97 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  98 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/* 103 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 108 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 112 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 114 */     if (pid == this.pid)
/*     */       return; 
/* 116 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 119 */     if (pid == this.pid)
/*     */       return; 
/* 121 */     this.pid = pid;
/* 122 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public long getAid() {
/* 126 */     return this.aid;
/*     */   } public void setAid(long aid) {
/* 128 */     if (aid == this.aid)
/*     */       return; 
/* 130 */     this.aid = aid;
/*     */   }
/*     */   public void saveAid(long aid) {
/* 133 */     if (aid == this.aid)
/*     */       return; 
/* 135 */     this.aid = aid;
/* 136 */     saveField("aid", Long.valueOf(aid));
/*     */   }
/*     */   
/*     */   public String getActivity() {
/* 140 */     return this.activity;
/*     */   } public void setActivity(String activity) {
/* 142 */     if (activity.equals(this.activity))
/*     */       return; 
/* 144 */     this.activity = activity;
/*     */   }
/*     */   public void saveActivity(String activity) {
/* 147 */     if (activity.equals(this.activity))
/*     */       return; 
/* 149 */     this.activity = activity;
/* 150 */     saveField("activity", activity);
/*     */   }
/*     */   
/*     */   public int getExtIntSize() {
/* 154 */     return this.extInt.size();
/* 155 */   } public List<Integer> getExtIntAll() { return new ArrayList<>(this.extInt); }
/* 156 */   public void setExtIntAll(int value) { for (int i = 0; i < this.extInt.size(); ) { this.extInt.set(i, Integer.valueOf(value)); i++; }
/* 157 */      } public void saveExtIntAll(int value) { setExtIntAll(value); saveAll(); } public int getExtInt(int index) {
/* 158 */     return ((Integer)this.extInt.get(index)).intValue();
/*     */   } public void setExtInt(int index, int value) {
/* 160 */     if (value == ((Integer)this.extInt.get(index)).intValue())
/*     */       return; 
/* 162 */     this.extInt.set(index, Integer.valueOf(value));
/*     */   }
/*     */   public void saveExtInt(int index, int value) {
/* 165 */     if (value == ((Integer)this.extInt.get(index)).intValue())
/*     */       return; 
/* 167 */     this.extInt.set(index, Integer.valueOf(value));
/* 168 */     saveField("extInt_" + index, this.extInt.get(index));
/*     */   }
/*     */   
/*     */   public int getExtStrSize() {
/* 172 */     return this.extStr.size();
/* 173 */   } public List<String> getExtStrAll() { return new ArrayList<>(this.extStr); }
/* 174 */   public void setExtStrAll(String value) { for (int i = 0; i < this.extStr.size(); ) { this.extStr.set(i, value); i++; }
/* 175 */      } public void saveExtStrAll(String value) { setExtStrAll(value); saveAll(); } public String getExtStr(int index) {
/* 176 */     return this.extStr.get(index);
/*     */   } public void setExtStr(int index, String value) {
/* 178 */     if (value.equals(this.extStr.get(index)))
/*     */       return; 
/* 180 */     this.extStr.set(index, value);
/*     */   }
/*     */   public void saveExtStr(int index, String value) {
/* 183 */     if (value.equals(this.extStr.get(index)))
/*     */       return; 
/* 185 */     this.extStr.set(index, value);
/* 186 */     saveField("extStr_" + index, this.extStr.get(index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 193 */     StringBuilder sBuilder = new StringBuilder();
/* 194 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 195 */     sBuilder.append(" `aid` = '").append(this.aid).append("',");
/* 196 */     sBuilder.append(" `activity` = '").append((this.activity == null) ? null : this.activity.replace("'", "''")).append("',"); int i;
/* 197 */     for (i = 0; i < this.extInt.size(); i++) {
/* 198 */       sBuilder.append(" `extInt_").append(i).append("` = '").append(this.extInt.get(i)).append("',");
/*     */     }
/* 200 */     for (i = 0; i < this.extStr.size(); i++) {
/* 201 */       sBuilder.append(" `extStr_").append(i).append("` = '").append((this.extStr == null) ? null : ((String)this.extStr.get(i)).replace("'", "''")).append("',");
/*     */     }
/* 203 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 204 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 208 */     String sql = "CREATE TABLE IF NOT EXISTS `activityRecord` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属玩家pid',`aid` bigint(20) NOT NULL DEFAULT '0' COMMENT '对应活动主键Id',`activity` varchar(500) NOT NULL DEFAULT '' COMMENT '所属活动',`extInt_0` int(11) NOT NULL DEFAULT '0' COMMENT 'int型扩展字段',`extInt_1` int(11) NOT NULL DEFAULT '0' COMMENT 'int型扩展字段',`extInt_2` int(11) NOT NULL DEFAULT '0' COMMENT 'int型扩展字段',`extInt_3` int(11) NOT NULL DEFAULT '0' COMMENT 'int型扩展字段',`extInt_4` int(11) NOT NULL DEFAULT '0' COMMENT 'int型扩展字段',`extStr_0` varchar(500) NOT NULL DEFAULT '' COMMENT 'string型扩展字段',`extStr_1` varchar(500) NOT NULL DEFAULT '' COMMENT 'string型扩展字段',`extStr_2` varchar(500) NOT NULL DEFAULT '' COMMENT 'string型扩展字段',`extStr_3` varchar(500) NOT NULL DEFAULT '' COMMENT 'string型扩展字段',PRIMARY KEY (`id`)) COMMENT='活动参与记录信息，记录每个玩家参加活动的参与记录'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 223 */       ServerConfig.getInitialID() + 1L);
/* 224 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/ActivityRecordBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */