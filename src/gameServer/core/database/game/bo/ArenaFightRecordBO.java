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
/*     */ public class ArenaFightRecordBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "atkPid", comment = "挑战者玩家ID")
/*     */   private long atkPid;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "defPid", comment = "防御者玩家ID")
/*     */   private long defPid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "atkRank", comment = "挑战者玩家排名")
/*     */   private int atkRank;
/*     */   @DataBaseField(type = "int(11)", fieldname = "defRank", comment = "防御者玩家排名")
/*     */   private int defRank;
/*     */   @DataBaseField(type = "int(11)", fieldname = "beginTime", comment = "战斗开始时间")
/*     */   private int beginTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "endTime", comment = "战斗结束时间")
/*     */   private int endTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "result", comment = "挑战结果,FightResult")
/*     */   private int result;
/*     */   
/*     */   public ArenaFightRecordBO() {
/*  32 */     this.id = 0L;
/*  33 */     this.atkPid = 0L;
/*  34 */     this.defPid = 0L;
/*  35 */     this.atkRank = 0;
/*  36 */     this.defRank = 0;
/*  37 */     this.beginTime = 0;
/*  38 */     this.endTime = 0;
/*  39 */     this.result = 0;
/*     */   }
/*     */   
/*     */   public ArenaFightRecordBO(ResultSet rs) throws Exception {
/*  43 */     this.id = rs.getLong(1);
/*  44 */     this.atkPid = rs.getLong(2);
/*  45 */     this.defPid = rs.getLong(3);
/*  46 */     this.atkRank = rs.getInt(4);
/*  47 */     this.defRank = rs.getInt(5);
/*  48 */     this.beginTime = rs.getInt(6);
/*  49 */     this.endTime = rs.getInt(7);
/*  50 */     this.result = rs.getInt(8);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<ArenaFightRecordBO> list) throws Exception {
/*  56 */     list.add(new ArenaFightRecordBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  61 */     return getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  66 */     return "`id`, `atkPid`, `defPid`, `atkRank`, `defRank`, `beginTime`, `endTime`, `result`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  71 */     return "`arena_fight_record`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  76 */     StringBuilder strBuf = new StringBuilder();
/*  77 */     strBuf.append("'").append(this.id).append("', ");
/*  78 */     strBuf.append("'").append(this.atkPid).append("', ");
/*  79 */     strBuf.append("'").append(this.defPid).append("', ");
/*  80 */     strBuf.append("'").append(this.atkRank).append("', ");
/*  81 */     strBuf.append("'").append(this.defRank).append("', ");
/*  82 */     strBuf.append("'").append(this.beginTime).append("', ");
/*  83 */     strBuf.append("'").append(this.endTime).append("', ");
/*  84 */     strBuf.append("'").append(this.result).append("', ");
/*  85 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  86 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  91 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  92 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  97 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 102 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getAtkPid() {
/* 106 */     return this.atkPid;
/*     */   } public void setAtkPid(long atkPid) {
/* 108 */     if (atkPid == this.atkPid)
/*     */       return; 
/* 110 */     this.atkPid = atkPid;
/*     */   }
/*     */   public void saveAtkPid(long atkPid) {
/* 113 */     if (atkPid == this.atkPid)
/*     */       return; 
/* 115 */     this.atkPid = atkPid;
/* 116 */     saveField("atkPid", Long.valueOf(atkPid));
/*     */   }
/*     */   
/*     */   public long getDefPid() {
/* 120 */     return this.defPid;
/*     */   } public void setDefPid(long defPid) {
/* 122 */     if (defPid == this.defPid)
/*     */       return; 
/* 124 */     this.defPid = defPid;
/*     */   }
/*     */   public void saveDefPid(long defPid) {
/* 127 */     if (defPid == this.defPid)
/*     */       return; 
/* 129 */     this.defPid = defPid;
/* 130 */     saveField("defPid", Long.valueOf(defPid));
/*     */   }
/*     */   
/*     */   public int getAtkRank() {
/* 134 */     return this.atkRank;
/*     */   } public void setAtkRank(int atkRank) {
/* 136 */     if (atkRank == this.atkRank)
/*     */       return; 
/* 138 */     this.atkRank = atkRank;
/*     */   }
/*     */   public void saveAtkRank(int atkRank) {
/* 141 */     if (atkRank == this.atkRank)
/*     */       return; 
/* 143 */     this.atkRank = atkRank;
/* 144 */     saveField("atkRank", Integer.valueOf(atkRank));
/*     */   }
/*     */   
/*     */   public int getDefRank() {
/* 148 */     return this.defRank;
/*     */   } public void setDefRank(int defRank) {
/* 150 */     if (defRank == this.defRank)
/*     */       return; 
/* 152 */     this.defRank = defRank;
/*     */   }
/*     */   public void saveDefRank(int defRank) {
/* 155 */     if (defRank == this.defRank)
/*     */       return; 
/* 157 */     this.defRank = defRank;
/* 158 */     saveField("defRank", Integer.valueOf(defRank));
/*     */   }
/*     */   
/*     */   public int getBeginTime() {
/* 162 */     return this.beginTime;
/*     */   } public void setBeginTime(int beginTime) {
/* 164 */     if (beginTime == this.beginTime)
/*     */       return; 
/* 166 */     this.beginTime = beginTime;
/*     */   }
/*     */   public void saveBeginTime(int beginTime) {
/* 169 */     if (beginTime == this.beginTime)
/*     */       return; 
/* 171 */     this.beginTime = beginTime;
/* 172 */     saveField("beginTime", Integer.valueOf(beginTime));
/*     */   }
/*     */   
/*     */   public int getEndTime() {
/* 176 */     return this.endTime;
/*     */   } public void setEndTime(int endTime) {
/* 178 */     if (endTime == this.endTime)
/*     */       return; 
/* 180 */     this.endTime = endTime;
/*     */   }
/*     */   public void saveEndTime(int endTime) {
/* 183 */     if (endTime == this.endTime)
/*     */       return; 
/* 185 */     this.endTime = endTime;
/* 186 */     saveField("endTime", Integer.valueOf(endTime));
/*     */   }
/*     */   
/*     */   public int getResult() {
/* 190 */     return this.result;
/*     */   } public void setResult(int result) {
/* 192 */     if (result == this.result)
/*     */       return; 
/* 194 */     this.result = result;
/*     */   }
/*     */   public void saveResult(int result) {
/* 197 */     if (result == this.result)
/*     */       return; 
/* 199 */     this.result = result;
/* 200 */     saveField("result", Integer.valueOf(result));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 207 */     StringBuilder sBuilder = new StringBuilder();
/* 208 */     sBuilder.append(" `atkPid` = '").append(this.atkPid).append("',");
/* 209 */     sBuilder.append(" `defPid` = '").append(this.defPid).append("',");
/* 210 */     sBuilder.append(" `atkRank` = '").append(this.atkRank).append("',");
/* 211 */     sBuilder.append(" `defRank` = '").append(this.defRank).append("',");
/* 212 */     sBuilder.append(" `beginTime` = '").append(this.beginTime).append("',");
/* 213 */     sBuilder.append(" `endTime` = '").append(this.endTime).append("',");
/* 214 */     sBuilder.append(" `result` = '").append(this.result).append("',");
/* 215 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 216 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 220 */     String sql = "CREATE TABLE IF NOT EXISTS `arena_fight_record` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`atkPid` bigint(20) NOT NULL DEFAULT '0' COMMENT '挑战者玩家ID',`defPid` bigint(20) NOT NULL DEFAULT '0' COMMENT '防御者玩家ID',`atkRank` int(11) NOT NULL DEFAULT '0' COMMENT '挑战者玩家排名',`defRank` int(11) NOT NULL DEFAULT '0' COMMENT '防御者玩家排名',`beginTime` int(11) NOT NULL DEFAULT '0' COMMENT '战斗开始时间',`endTime` int(11) NOT NULL DEFAULT '0' COMMENT '战斗结束时间',`result` int(11) NOT NULL DEFAULT '0' COMMENT '挑战结果,FightResult',KEY `atkPid` (`atkPid`),KEY `defPid` (`defPid`),PRIMARY KEY (`id`)) COMMENT='战报记录表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 232 */       ServerConfig.getInitialID() + 1L);
/* 233 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/ArenaFightRecordBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */