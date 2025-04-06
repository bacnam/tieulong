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
/*     */ public class LongnvResultBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "atkpid", comment = "攻击者ID")
/*     */   private long atkpid;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "defpid", comment = "防御者ID")
/*     */   private long defpid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "result", comment = "结果")
/*     */   private int result;
/*     */   @DataBaseField(type = "int(11)", fieldname = "fightTime", comment = "战斗时间")
/*     */   private int fightTime;
/*     */   
/*     */   public LongnvResultBO() {
/*  26 */     this.id = 0L;
/*  27 */     this.atkpid = 0L;
/*  28 */     this.defpid = 0L;
/*  29 */     this.result = 0;
/*  30 */     this.fightTime = 0;
/*     */   }
/*     */   
/*     */   public LongnvResultBO(ResultSet rs) throws Exception {
/*  34 */     this.id = rs.getLong(1);
/*  35 */     this.atkpid = rs.getLong(2);
/*  36 */     this.defpid = rs.getLong(3);
/*  37 */     this.result = rs.getInt(4);
/*  38 */     this.fightTime = rs.getInt(5);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<LongnvResultBO> list) throws Exception {
/*  44 */     list.add(new LongnvResultBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  49 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  54 */     return "`id`, `atkpid`, `defpid`, `result`, `fightTime`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  59 */     return "`longnvResult`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  64 */     StringBuilder strBuf = new StringBuilder();
/*  65 */     strBuf.append("'").append(this.id).append("', ");
/*  66 */     strBuf.append("'").append(this.atkpid).append("', ");
/*  67 */     strBuf.append("'").append(this.defpid).append("', ");
/*  68 */     strBuf.append("'").append(this.result).append("', ");
/*  69 */     strBuf.append("'").append(this.fightTime).append("', ");
/*  70 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  71 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  76 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  77 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  82 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/*  87 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getAtkpid() {
/*  91 */     return this.atkpid;
/*     */   } public void setAtkpid(long atkpid) {
/*  93 */     if (atkpid == this.atkpid)
/*     */       return; 
/*  95 */     this.atkpid = atkpid;
/*     */   }
/*     */   public void saveAtkpid(long atkpid) {
/*  98 */     if (atkpid == this.atkpid)
/*     */       return; 
/* 100 */     this.atkpid = atkpid;
/* 101 */     saveField("atkpid", Long.valueOf(atkpid));
/*     */   }
/*     */   
/*     */   public long getDefpid() {
/* 105 */     return this.defpid;
/*     */   } public void setDefpid(long defpid) {
/* 107 */     if (defpid == this.defpid)
/*     */       return; 
/* 109 */     this.defpid = defpid;
/*     */   }
/*     */   public void saveDefpid(long defpid) {
/* 112 */     if (defpid == this.defpid)
/*     */       return; 
/* 114 */     this.defpid = defpid;
/* 115 */     saveField("defpid", Long.valueOf(defpid));
/*     */   }
/*     */   
/*     */   public int getResult() {
/* 119 */     return this.result;
/*     */   } public void setResult(int result) {
/* 121 */     if (result == this.result)
/*     */       return; 
/* 123 */     this.result = result;
/*     */   }
/*     */   public void saveResult(int result) {
/* 126 */     if (result == this.result)
/*     */       return; 
/* 128 */     this.result = result;
/* 129 */     saveField("result", Integer.valueOf(result));
/*     */   }
/*     */   
/*     */   public int getFightTime() {
/* 133 */     return this.fightTime;
/*     */   } public void setFightTime(int fightTime) {
/* 135 */     if (fightTime == this.fightTime)
/*     */       return; 
/* 137 */     this.fightTime = fightTime;
/*     */   }
/*     */   public void saveFightTime(int fightTime) {
/* 140 */     if (fightTime == this.fightTime)
/*     */       return; 
/* 142 */     this.fightTime = fightTime;
/* 143 */     saveField("fightTime", Integer.valueOf(fightTime));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 150 */     StringBuilder sBuilder = new StringBuilder();
/* 151 */     sBuilder.append(" `atkpid` = '").append(this.atkpid).append("',");
/* 152 */     sBuilder.append(" `defpid` = '").append(this.defpid).append("',");
/* 153 */     sBuilder.append(" `result` = '").append(this.result).append("',");
/* 154 */     sBuilder.append(" `fightTime` = '").append(this.fightTime).append("',");
/* 155 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 156 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 160 */     String sql = "CREATE TABLE IF NOT EXISTS `longnvResult` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`atkpid` bigint(20) NOT NULL DEFAULT '0' COMMENT '攻击者ID',`defpid` bigint(20) NOT NULL DEFAULT '0' COMMENT '防御者ID',`result` int(11) NOT NULL DEFAULT '0' COMMENT '结果',`fightTime` int(11) NOT NULL DEFAULT '0' COMMENT '战斗时间',PRIMARY KEY (`id`)) COMMENT='公会战'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 167 */       ServerConfig.getInitialID() + 1L);
/* 168 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/LongnvResultBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */