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
/*     */ public class ArenaCompetitorBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "所属玩家ID")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "rank", comment = "排名")
/*     */   private int rank;
/*     */   @DataBaseField(type = "int(11)", fieldname = "lastRankTime", comment = "最近一次排名变更时间")
/*     */   private int lastRankTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "lastFightTime", comment = "上次战斗事件,单位秒,用于冷却")
/*     */   private int lastFightTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "highestRank", comment = "历史最高排名")
/*     */   private int highestRank;
/*     */   @DataBaseField(type = "int(11)", fieldname = "winning", comment = "连胜")
/*     */   private int winning;
/*     */   
/*     */   public ArenaCompetitorBO() {
/*  30 */     this.id = 0L;
/*  31 */     this.pid = 0L;
/*  32 */     this.rank = 0;
/*  33 */     this.lastRankTime = 0;
/*  34 */     this.lastFightTime = 0;
/*  35 */     this.highestRank = 0;
/*  36 */     this.winning = 0;
/*     */   }
/*     */   
/*     */   public ArenaCompetitorBO(ResultSet rs) throws Exception {
/*  40 */     this.id = rs.getLong(1);
/*  41 */     this.pid = rs.getLong(2);
/*  42 */     this.rank = rs.getInt(3);
/*  43 */     this.lastRankTime = rs.getInt(4);
/*  44 */     this.lastFightTime = rs.getInt(5);
/*  45 */     this.highestRank = rs.getInt(6);
/*  46 */     this.winning = rs.getInt(7);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<ArenaCompetitorBO> list) throws Exception {
/*  52 */     list.add(new ArenaCompetitorBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  57 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  62 */     return "`id`, `pid`, `rank`, `lastRankTime`, `lastFightTime`, `highestRank`, `winning`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  67 */     return "`arena_competitor`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  72 */     StringBuilder strBuf = new StringBuilder();
/*  73 */     strBuf.append("'").append(this.id).append("', ");
/*  74 */     strBuf.append("'").append(this.pid).append("', ");
/*  75 */     strBuf.append("'").append(this.rank).append("', ");
/*  76 */     strBuf.append("'").append(this.lastRankTime).append("', ");
/*  77 */     strBuf.append("'").append(this.lastFightTime).append("', ");
/*  78 */     strBuf.append("'").append(this.highestRank).append("', ");
/*  79 */     strBuf.append("'").append(this.winning).append("', ");
/*  80 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  81 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  86 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  87 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  92 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/*  97 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 101 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 103 */     if (pid == this.pid)
/*     */       return; 
/* 105 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 108 */     if (pid == this.pid)
/*     */       return; 
/* 110 */     this.pid = pid;
/* 111 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public int getRank() {
/* 115 */     return this.rank;
/*     */   } public void setRank(int rank) {
/* 117 */     if (rank == this.rank)
/*     */       return; 
/* 119 */     this.rank = rank;
/*     */   }
/*     */   public void saveRank(int rank) {
/* 122 */     if (rank == this.rank)
/*     */       return; 
/* 124 */     this.rank = rank;
/* 125 */     saveField("rank", Integer.valueOf(rank));
/*     */   }
/*     */   
/*     */   public int getLastRankTime() {
/* 129 */     return this.lastRankTime;
/*     */   } public void setLastRankTime(int lastRankTime) {
/* 131 */     if (lastRankTime == this.lastRankTime)
/*     */       return; 
/* 133 */     this.lastRankTime = lastRankTime;
/*     */   }
/*     */   public void saveLastRankTime(int lastRankTime) {
/* 136 */     if (lastRankTime == this.lastRankTime)
/*     */       return; 
/* 138 */     this.lastRankTime = lastRankTime;
/* 139 */     saveField("lastRankTime", Integer.valueOf(lastRankTime));
/*     */   }
/*     */   
/*     */   public int getLastFightTime() {
/* 143 */     return this.lastFightTime;
/*     */   } public void setLastFightTime(int lastFightTime) {
/* 145 */     if (lastFightTime == this.lastFightTime)
/*     */       return; 
/* 147 */     this.lastFightTime = lastFightTime;
/*     */   }
/*     */   public void saveLastFightTime(int lastFightTime) {
/* 150 */     if (lastFightTime == this.lastFightTime)
/*     */       return; 
/* 152 */     this.lastFightTime = lastFightTime;
/* 153 */     saveField("lastFightTime", Integer.valueOf(lastFightTime));
/*     */   }
/*     */   
/*     */   public int getHighestRank() {
/* 157 */     return this.highestRank;
/*     */   } public void setHighestRank(int highestRank) {
/* 159 */     if (highestRank == this.highestRank)
/*     */       return; 
/* 161 */     this.highestRank = highestRank;
/*     */   }
/*     */   public void saveHighestRank(int highestRank) {
/* 164 */     if (highestRank == this.highestRank)
/*     */       return; 
/* 166 */     this.highestRank = highestRank;
/* 167 */     saveField("highestRank", Integer.valueOf(highestRank));
/*     */   }
/*     */   
/*     */   public int getWinning() {
/* 171 */     return this.winning;
/*     */   } public void setWinning(int winning) {
/* 173 */     if (winning == this.winning)
/*     */       return; 
/* 175 */     this.winning = winning;
/*     */   }
/*     */   public void saveWinning(int winning) {
/* 178 */     if (winning == this.winning)
/*     */       return; 
/* 180 */     this.winning = winning;
/* 181 */     saveField("winning", Integer.valueOf(winning));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 188 */     StringBuilder sBuilder = new StringBuilder();
/* 189 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 190 */     sBuilder.append(" `rank` = '").append(this.rank).append("',");
/* 191 */     sBuilder.append(" `lastRankTime` = '").append(this.lastRankTime).append("',");
/* 192 */     sBuilder.append(" `lastFightTime` = '").append(this.lastFightTime).append("',");
/* 193 */     sBuilder.append(" `highestRank` = '").append(this.highestRank).append("',");
/* 194 */     sBuilder.append(" `winning` = '").append(this.winning).append("',");
/* 195 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 196 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 200 */     String sql = "CREATE TABLE IF NOT EXISTS `arena_competitor` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属玩家ID',`rank` int(11) NOT NULL DEFAULT '0' COMMENT '排名',`lastRankTime` int(11) NOT NULL DEFAULT '0' COMMENT '最近一次排名变更时间',`lastFightTime` int(11) NOT NULL DEFAULT '0' COMMENT '上次战斗事件,单位秒,用于冷却',`highestRank` int(11) NOT NULL DEFAULT '0' COMMENT '历史最高排名',`winning` int(11) NOT NULL DEFAULT '0' COMMENT '连胜',PRIMARY KEY (`id`)) COMMENT='玩家信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/ArenaCompetitorBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */