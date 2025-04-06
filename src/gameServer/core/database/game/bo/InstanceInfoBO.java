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
/*     */ public class InstanceInfoBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "所属玩家ID")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", size = 10, fieldname = "instanceMaxLevel", comment = "最高副本关卡等级")
/*     */   private List<Integer> instanceMaxLevel;
/*     */   @DataBaseField(type = "int(11)", size = 10, fieldname = "challengTimes", comment = "挑战次数")
/*     */   private List<Integer> challengTimes;
/*     */   
/*     */   public InstanceInfoBO() {
/*  24 */     this.id = 0L;
/*  25 */     this.pid = 0L;
/*  26 */     this.instanceMaxLevel = new ArrayList<>(10); int i;
/*  27 */     for (i = 0; i < 10; i++) {
/*  28 */       this.instanceMaxLevel.add(Integer.valueOf(0));
/*     */     }
/*  30 */     this.challengTimes = new ArrayList<>(10);
/*  31 */     for (i = 0; i < 10; i++) {
/*  32 */       this.challengTimes.add(Integer.valueOf(0));
/*     */     }
/*     */   }
/*     */   
/*     */   public InstanceInfoBO(ResultSet rs) throws Exception {
/*  37 */     this.id = rs.getLong(1);
/*  38 */     this.pid = rs.getLong(2);
/*  39 */     this.instanceMaxLevel = new ArrayList<>(10); int i;
/*  40 */     for (i = 0; i < 10; i++) {
/*  41 */       this.instanceMaxLevel.add(Integer.valueOf(rs.getInt(i + 3)));
/*     */     }
/*  43 */     this.challengTimes = new ArrayList<>(10);
/*  44 */     for (i = 0; i < 10; i++) {
/*  45 */       this.challengTimes.add(Integer.valueOf(rs.getInt(i + 13)));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<InstanceInfoBO> list) throws Exception {
/*  52 */     list.add(new InstanceInfoBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  57 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  62 */     return "`id`, `pid`, `instanceMaxLevel_0`, `instanceMaxLevel_1`, `instanceMaxLevel_2`, `instanceMaxLevel_3`, `instanceMaxLevel_4`, `instanceMaxLevel_5`, `instanceMaxLevel_6`, `instanceMaxLevel_7`, `instanceMaxLevel_8`, `instanceMaxLevel_9`, `challengTimes_0`, `challengTimes_1`, `challengTimes_2`, `challengTimes_3`, `challengTimes_4`, `challengTimes_5`, `challengTimes_6`, `challengTimes_7`, `challengTimes_8`, `challengTimes_9`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  67 */     return "`instance_info`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  72 */     StringBuilder strBuf = new StringBuilder();
/*  73 */     strBuf.append("'").append(this.id).append("', ");
/*  74 */     strBuf.append("'").append(this.pid).append("', "); int i;
/*  75 */     for (i = 0; i < this.instanceMaxLevel.size(); i++) {
/*  76 */       strBuf.append("'").append(this.instanceMaxLevel.get(i)).append("', ");
/*     */     }
/*  78 */     for (i = 0; i < this.challengTimes.size(); i++) {
/*  79 */       strBuf.append("'").append(this.challengTimes.get(i)).append("', ");
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
/*     */   public int getInstanceMaxLevelSize() {
/* 116 */     return this.instanceMaxLevel.size();
/* 117 */   } public List<Integer> getInstanceMaxLevelAll() { return new ArrayList<>(this.instanceMaxLevel); }
/* 118 */   public void setInstanceMaxLevelAll(int value) { for (int i = 0; i < this.instanceMaxLevel.size(); ) { this.instanceMaxLevel.set(i, Integer.valueOf(value)); i++; }
/* 119 */      } public void saveInstanceMaxLevelAll(int value) { setInstanceMaxLevelAll(value); saveAll(); } public int getInstanceMaxLevel(int index) {
/* 120 */     return ((Integer)this.instanceMaxLevel.get(index)).intValue();
/*     */   } public void setInstanceMaxLevel(int index, int value) {
/* 122 */     if (value == ((Integer)this.instanceMaxLevel.get(index)).intValue())
/*     */       return; 
/* 124 */     this.instanceMaxLevel.set(index, Integer.valueOf(value));
/*     */   }
/*     */   public void saveInstanceMaxLevel(int index, int value) {
/* 127 */     if (value == ((Integer)this.instanceMaxLevel.get(index)).intValue())
/*     */       return; 
/* 129 */     this.instanceMaxLevel.set(index, Integer.valueOf(value));
/* 130 */     saveField("instanceMaxLevel_" + index, this.instanceMaxLevel.get(index));
/*     */   }
/*     */   
/*     */   public int getChallengTimesSize() {
/* 134 */     return this.challengTimes.size();
/* 135 */   } public List<Integer> getChallengTimesAll() { return new ArrayList<>(this.challengTimes); }
/* 136 */   public void setChallengTimesAll(int value) { for (int i = 0; i < this.challengTimes.size(); ) { this.challengTimes.set(i, Integer.valueOf(value)); i++; }
/* 137 */      } public void saveChallengTimesAll(int value) { setChallengTimesAll(value); saveAll(); } public int getChallengTimes(int index) {
/* 138 */     return ((Integer)this.challengTimes.get(index)).intValue();
/*     */   } public void setChallengTimes(int index, int value) {
/* 140 */     if (value == ((Integer)this.challengTimes.get(index)).intValue())
/*     */       return; 
/* 142 */     this.challengTimes.set(index, Integer.valueOf(value));
/*     */   }
/*     */   public void saveChallengTimes(int index, int value) {
/* 145 */     if (value == ((Integer)this.challengTimes.get(index)).intValue())
/*     */       return; 
/* 147 */     this.challengTimes.set(index, Integer.valueOf(value));
/* 148 */     saveField("challengTimes_" + index, this.challengTimes.get(index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 155 */     StringBuilder sBuilder = new StringBuilder();
/* 156 */     sBuilder.append(" `pid` = '").append(this.pid).append("',"); int i;
/* 157 */     for (i = 0; i < this.instanceMaxLevel.size(); i++) {
/* 158 */       sBuilder.append(" `instanceMaxLevel_").append(i).append("` = '").append(this.instanceMaxLevel.get(i)).append("',");
/*     */     }
/* 160 */     for (i = 0; i < this.challengTimes.size(); i++) {
/* 161 */       sBuilder.append(" `challengTimes_").append(i).append("` = '").append(this.challengTimes.get(i)).append("',");
/*     */     }
/* 163 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 164 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 168 */     String sql = "CREATE TABLE IF NOT EXISTS `instance_info` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属玩家ID',`instanceMaxLevel_0` int(11) NOT NULL DEFAULT '0' COMMENT '最高副本关卡等级',`instanceMaxLevel_1` int(11) NOT NULL DEFAULT '0' COMMENT '最高副本关卡等级',`instanceMaxLevel_2` int(11) NOT NULL DEFAULT '0' COMMENT '最高副本关卡等级',`instanceMaxLevel_3` int(11) NOT NULL DEFAULT '0' COMMENT '最高副本关卡等级',`instanceMaxLevel_4` int(11) NOT NULL DEFAULT '0' COMMENT '最高副本关卡等级',`instanceMaxLevel_5` int(11) NOT NULL DEFAULT '0' COMMENT '最高副本关卡等级',`instanceMaxLevel_6` int(11) NOT NULL DEFAULT '0' COMMENT '最高副本关卡等级',`instanceMaxLevel_7` int(11) NOT NULL DEFAULT '0' COMMENT '最高副本关卡等级',`instanceMaxLevel_8` int(11) NOT NULL DEFAULT '0' COMMENT '最高副本关卡等级',`instanceMaxLevel_9` int(11) NOT NULL DEFAULT '0' COMMENT '最高副本关卡等级',`challengTimes_0` int(11) NOT NULL DEFAULT '0' COMMENT '挑战次数',`challengTimes_1` int(11) NOT NULL DEFAULT '0' COMMENT '挑战次数',`challengTimes_2` int(11) NOT NULL DEFAULT '0' COMMENT '挑战次数',`challengTimes_3` int(11) NOT NULL DEFAULT '0' COMMENT '挑战次数',`challengTimes_4` int(11) NOT NULL DEFAULT '0' COMMENT '挑战次数',`challengTimes_5` int(11) NOT NULL DEFAULT '0' COMMENT '挑战次数',`challengTimes_6` int(11) NOT NULL DEFAULT '0' COMMENT '挑战次数',`challengTimes_7` int(11) NOT NULL DEFAULT '0' COMMENT '挑战次数',`challengTimes_8` int(11) NOT NULL DEFAULT '0' COMMENT '挑战次数',`challengTimes_9` int(11) NOT NULL DEFAULT '0' COMMENT '挑战次数',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='玩家装备副本信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 193 */       ServerConfig.getInitialID() + 1L);
/* 194 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/InstanceInfoBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */