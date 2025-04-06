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
/*     */ public class DailyPlayerRefreshBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "角色ID")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", size = 70, fieldname = "indexLastTime", comment = "上次刷新时间")
/*     */   private List<Integer> indexLastTime;
/*     */   
/*     */   public DailyPlayerRefreshBO() {
/*  22 */     this.id = 0L;
/*  23 */     this.pid = 0L;
/*  24 */     this.indexLastTime = new ArrayList<>(70);
/*  25 */     for (int i = 0; i < 70; i++) {
/*  26 */       this.indexLastTime.add(Integer.valueOf(0));
/*     */     }
/*     */   }
/*     */   
/*     */   public DailyPlayerRefreshBO(ResultSet rs) throws Exception {
/*  31 */     this.id = rs.getLong(1);
/*  32 */     this.pid = rs.getLong(2);
/*  33 */     this.indexLastTime = new ArrayList<>(70);
/*  34 */     for (int i = 0; i < 70; i++) {
/*  35 */       this.indexLastTime.add(Integer.valueOf(rs.getInt(i + 3)));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<DailyPlayerRefreshBO> list) throws Exception {
/*  42 */     list.add(new DailyPlayerRefreshBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  47 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  52 */     return "`id`, `pid`, `indexLastTime_0`, `indexLastTime_1`, `indexLastTime_2`, `indexLastTime_3`, `indexLastTime_4`, `indexLastTime_5`, `indexLastTime_6`, `indexLastTime_7`, `indexLastTime_8`, `indexLastTime_9`, `indexLastTime_10`, `indexLastTime_11`, `indexLastTime_12`, `indexLastTime_13`, `indexLastTime_14`, `indexLastTime_15`, `indexLastTime_16`, `indexLastTime_17`, `indexLastTime_18`, `indexLastTime_19`, `indexLastTime_20`, `indexLastTime_21`, `indexLastTime_22`, `indexLastTime_23`, `indexLastTime_24`, `indexLastTime_25`, `indexLastTime_26`, `indexLastTime_27`, `indexLastTime_28`, `indexLastTime_29`, `indexLastTime_30`, `indexLastTime_31`, `indexLastTime_32`, `indexLastTime_33`, `indexLastTime_34`, `indexLastTime_35`, `indexLastTime_36`, `indexLastTime_37`, `indexLastTime_38`, `indexLastTime_39`, `indexLastTime_40`, `indexLastTime_41`, `indexLastTime_42`, `indexLastTime_43`, `indexLastTime_44`, `indexLastTime_45`, `indexLastTime_46`, `indexLastTime_47`, `indexLastTime_48`, `indexLastTime_49`, `indexLastTime_50`, `indexLastTime_51`, `indexLastTime_52`, `indexLastTime_53`, `indexLastTime_54`, `indexLastTime_55`, `indexLastTime_56`, `indexLastTime_57`, `indexLastTime_58`, `indexLastTime_59`, `indexLastTime_60`, `indexLastTime_61`, `indexLastTime_62`, `indexLastTime_63`, `indexLastTime_64`, `indexLastTime_65`, `indexLastTime_66`, `indexLastTime_67`, `indexLastTime_68`, `indexLastTime_69`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  57 */     return "`dailyPlayerRefresh`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  62 */     StringBuilder strBuf = new StringBuilder();
/*  63 */     strBuf.append("'").append(this.id).append("', ");
/*  64 */     strBuf.append("'").append(this.pid).append("', ");
/*  65 */     for (int i = 0; i < this.indexLastTime.size(); i++) {
/*  66 */       strBuf.append("'").append(this.indexLastTime.get(i)).append("', ");
/*     */     }
/*  68 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  69 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  74 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  75 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  80 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/*  85 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/*  89 */     return this.pid;
/*     */   } public void setPid(long pid) {
/*  91 */     if (pid == this.pid)
/*     */       return; 
/*  93 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/*  96 */     if (pid == this.pid)
/*     */       return; 
/*  98 */     this.pid = pid;
/*  99 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public int getIndexLastTimeSize() {
/* 103 */     return this.indexLastTime.size();
/* 104 */   } public List<Integer> getIndexLastTimeAll() { return new ArrayList<>(this.indexLastTime); }
/* 105 */   public void setIndexLastTimeAll(int value) { for (int i = 0; i < this.indexLastTime.size(); ) { this.indexLastTime.set(i, Integer.valueOf(value)); i++; }
/* 106 */      } public void saveIndexLastTimeAll(int value) { setIndexLastTimeAll(value); saveAll(); } public int getIndexLastTime(int index) {
/* 107 */     return ((Integer)this.indexLastTime.get(index)).intValue();
/*     */   } public void setIndexLastTime(int index, int value) {
/* 109 */     if (value == ((Integer)this.indexLastTime.get(index)).intValue())
/*     */       return; 
/* 111 */     this.indexLastTime.set(index, Integer.valueOf(value));
/*     */   }
/*     */   public void saveIndexLastTime(int index, int value) {
/* 114 */     if (value == ((Integer)this.indexLastTime.get(index)).intValue())
/*     */       return; 
/* 116 */     this.indexLastTime.set(index, Integer.valueOf(value));
/* 117 */     saveField("indexLastTime_" + index, this.indexLastTime.get(index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 124 */     StringBuilder sBuilder = new StringBuilder();
/* 125 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 126 */     for (int i = 0; i < this.indexLastTime.size(); i++) {
/* 127 */       sBuilder.append(" `indexLastTime_").append(i).append("` = '").append(this.indexLastTime.get(i)).append("',");
/*     */     }
/* 129 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 130 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 134 */     String sql = "CREATE TABLE IF NOT EXISTS `dailyPlayerRefresh` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '角色ID',`indexLastTime_0` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_1` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_2` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_3` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_4` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_5` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_6` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_7` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_8` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_9` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_10` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_11` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_12` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_13` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_14` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_15` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_16` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_17` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_18` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_19` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_20` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_21` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_22` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_23` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_24` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_25` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_26` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_27` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_28` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_29` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_30` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_31` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_32` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_33` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_34` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_35` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_36` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_37` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_38` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_39` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_40` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_41` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_42` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_43` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_44` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_45` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_46` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_47` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_48` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_49` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_50` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_51` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_52` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_53` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_54` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_55` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_56` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_57` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_58` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_59` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_60` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_61` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_62` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_63` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_64` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_65` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_66` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_67` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_68` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',`indexLastTime_69` int(11) NOT NULL DEFAULT '0' COMMENT '上次刷新时间',UNIQUE INDEX `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='每日刷新'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 209 */       ServerConfig.getInitialID() + 1L);
/* 210 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/DailyPlayerRefreshBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */