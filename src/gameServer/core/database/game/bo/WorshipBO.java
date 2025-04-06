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
/*     */ public class WorshipBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "所属玩家ID")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", size = 20, fieldname = "worshipTimes", comment = "膜拜次数")
/*     */   private List<Integer> worshipTimes;
/*     */   @DataBaseField(type = "int(11)", size = 20, fieldname = "beWorshipTimes", comment = "被膜拜次数")
/*     */   private List<Integer> beWorshipTimes;
/*     */   
/*     */   public WorshipBO() {
/*  24 */     this.id = 0L;
/*  25 */     this.pid = 0L;
/*  26 */     this.worshipTimes = new ArrayList<>(20); int i;
/*  27 */     for (i = 0; i < 20; i++) {
/*  28 */       this.worshipTimes.add(Integer.valueOf(0));
/*     */     }
/*  30 */     this.beWorshipTimes = new ArrayList<>(20);
/*  31 */     for (i = 0; i < 20; i++) {
/*  32 */       this.beWorshipTimes.add(Integer.valueOf(0));
/*     */     }
/*     */   }
/*     */   
/*     */   public WorshipBO(ResultSet rs) throws Exception {
/*  37 */     this.id = rs.getLong(1);
/*  38 */     this.pid = rs.getLong(2);
/*  39 */     this.worshipTimes = new ArrayList<>(20); int i;
/*  40 */     for (i = 0; i < 20; i++) {
/*  41 */       this.worshipTimes.add(Integer.valueOf(rs.getInt(i + 3)));
/*     */     }
/*  43 */     this.beWorshipTimes = new ArrayList<>(20);
/*  44 */     for (i = 0; i < 20; i++) {
/*  45 */       this.beWorshipTimes.add(Integer.valueOf(rs.getInt(i + 23)));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<WorshipBO> list) throws Exception {
/*  52 */     list.add(new WorshipBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  57 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  62 */     return "`id`, `pid`, `worshipTimes_0`, `worshipTimes_1`, `worshipTimes_2`, `worshipTimes_3`, `worshipTimes_4`, `worshipTimes_5`, `worshipTimes_6`, `worshipTimes_7`, `worshipTimes_8`, `worshipTimes_9`, `worshipTimes_10`, `worshipTimes_11`, `worshipTimes_12`, `worshipTimes_13`, `worshipTimes_14`, `worshipTimes_15`, `worshipTimes_16`, `worshipTimes_17`, `worshipTimes_18`, `worshipTimes_19`, `beWorshipTimes_0`, `beWorshipTimes_1`, `beWorshipTimes_2`, `beWorshipTimes_3`, `beWorshipTimes_4`, `beWorshipTimes_5`, `beWorshipTimes_6`, `beWorshipTimes_7`, `beWorshipTimes_8`, `beWorshipTimes_9`, `beWorshipTimes_10`, `beWorshipTimes_11`, `beWorshipTimes_12`, `beWorshipTimes_13`, `beWorshipTimes_14`, `beWorshipTimes_15`, `beWorshipTimes_16`, `beWorshipTimes_17`, `beWorshipTimes_18`, `beWorshipTimes_19`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  67 */     return "`worship`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  72 */     StringBuilder strBuf = new StringBuilder();
/*  73 */     strBuf.append("'").append(this.id).append("', ");
/*  74 */     strBuf.append("'").append(this.pid).append("', "); int i;
/*  75 */     for (i = 0; i < this.worshipTimes.size(); i++) {
/*  76 */       strBuf.append("'").append(this.worshipTimes.get(i)).append("', ");
/*     */     }
/*  78 */     for (i = 0; i < this.beWorshipTimes.size(); i++) {
/*  79 */       strBuf.append("'").append(this.beWorshipTimes.get(i)).append("', ");
/*     */     }
/*  81 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  82 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  87 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  88 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  93 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/*  98 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 102 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 104 */     if (pid == this.pid)
/*     */       return; 
/* 106 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 109 */     if (pid == this.pid)
/*     */       return; 
/* 111 */     this.pid = pid;
/* 112 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public int getWorshipTimesSize() {
/* 116 */     return this.worshipTimes.size();
/* 117 */   } public List<Integer> getWorshipTimesAll() { return new ArrayList<>(this.worshipTimes); }
/* 118 */   public void setWorshipTimesAll(int value) { for (int i = 0; i < this.worshipTimes.size(); ) { this.worshipTimes.set(i, Integer.valueOf(value)); i++; }
/* 119 */      } public void saveWorshipTimesAll(int value) { setWorshipTimesAll(value); saveAll(); } public int getWorshipTimes(int index) {
/* 120 */     return ((Integer)this.worshipTimes.get(index)).intValue();
/*     */   } public void setWorshipTimes(int index, int value) {
/* 122 */     if (value == ((Integer)this.worshipTimes.get(index)).intValue())
/*     */       return; 
/* 124 */     this.worshipTimes.set(index, Integer.valueOf(value));
/*     */   }
/*     */   public void saveWorshipTimes(int index, int value) {
/* 127 */     if (value == ((Integer)this.worshipTimes.get(index)).intValue())
/*     */       return; 
/* 129 */     this.worshipTimes.set(index, Integer.valueOf(value));
/* 130 */     saveField("worshipTimes_" + index, this.worshipTimes.get(index));
/*     */   }
/*     */   
/*     */   public int getBeWorshipTimesSize() {
/* 134 */     return this.beWorshipTimes.size();
/* 135 */   } public List<Integer> getBeWorshipTimesAll() { return new ArrayList<>(this.beWorshipTimes); }
/* 136 */   public void setBeWorshipTimesAll(int value) { for (int i = 0; i < this.beWorshipTimes.size(); ) { this.beWorshipTimes.set(i, Integer.valueOf(value)); i++; }
/* 137 */      } public void saveBeWorshipTimesAll(int value) { setBeWorshipTimesAll(value); saveAll(); } public int getBeWorshipTimes(int index) {
/* 138 */     return ((Integer)this.beWorshipTimes.get(index)).intValue();
/*     */   } public void setBeWorshipTimes(int index, int value) {
/* 140 */     if (value == ((Integer)this.beWorshipTimes.get(index)).intValue())
/*     */       return; 
/* 142 */     this.beWorshipTimes.set(index, Integer.valueOf(value));
/*     */   }
/*     */   public void saveBeWorshipTimes(int index, int value) {
/* 145 */     if (value == ((Integer)this.beWorshipTimes.get(index)).intValue())
/*     */       return; 
/* 147 */     this.beWorshipTimes.set(index, Integer.valueOf(value));
/* 148 */     saveField("beWorshipTimes_" + index, this.beWorshipTimes.get(index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 155 */     StringBuilder sBuilder = new StringBuilder();
/* 156 */     sBuilder.append(" `pid` = '").append(this.pid).append("',"); int i;
/* 157 */     for (i = 0; i < this.worshipTimes.size(); i++) {
/* 158 */       sBuilder.append(" `worshipTimes_").append(i).append("` = '").append(this.worshipTimes.get(i)).append("',");
/*     */     }
/* 160 */     for (i = 0; i < this.beWorshipTimes.size(); i++) {
/* 161 */       sBuilder.append(" `beWorshipTimes_").append(i).append("` = '").append(this.beWorshipTimes.get(i)).append("',");
/*     */     }
/* 163 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 164 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 168 */     String sql = "CREATE TABLE IF NOT EXISTS `worship` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属玩家ID',`worshipTimes_0` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_1` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_2` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_3` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_4` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_5` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_6` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_7` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_8` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_9` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_10` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_11` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_12` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_13` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_14` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_15` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_16` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_17` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_18` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`worshipTimes_19` int(11) NOT NULL DEFAULT '0' COMMENT '膜拜次数',`beWorshipTimes_0` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_1` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_2` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_3` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_4` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_5` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_6` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_7` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_8` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_9` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_10` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_11` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_12` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_13` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_14` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_15` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_16` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_17` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_18` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',`beWorshipTimes_19` int(11) NOT NULL DEFAULT '0' COMMENT '被膜拜次数',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='玩家膜拜信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 213 */       ServerConfig.getInitialID() + 1L);
/* 214 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/WorshipBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */