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
/*     */ public class RichRiskExchangeBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家pid")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "x", comment = "坐标的X")
/*     */   private int x;
/*     */   @DataBaseField(type = "int(11)", fieldname = "y", comment = "坐标的Y")
/*     */   private int y;
/*     */   @DataBaseField(type = "int(11)", fieldname = "exchangeId", comment = "兑换Id")
/*     */   private int exchangeId;
/*     */   @DataBaseField(type = "int(11)", fieldname = "conditionId", comment = "条件物品Id")
/*     */   private int conditionId;
/*     */   @DataBaseField(type = "int(11)", fieldname = "conditionAmount", comment = "条件物品数量")
/*     */   private int conditionAmount;
/*     */   @DataBaseField(type = "int(11)", fieldname = "goalId", comment = "兑换物Id")
/*     */   private int goalId;
/*     */   @DataBaseField(type = "int(11)", fieldname = "goalAmount", comment = "兑换物数量")
/*     */   private int goalAmount;
/*     */   @DataBaseField(type = "int(11)", fieldname = "score", comment = "获得积分")
/*     */   private int score;
/*     */   @DataBaseField(type = "int(11)", fieldname = "timesLimit", comment = "次数限制")
/*     */   private int timesLimit;
/*     */   
/*     */   public RichRiskExchangeBO() {
/*  38 */     this.id = 0L;
/*  39 */     this.pid = 0L;
/*  40 */     this.x = 0;
/*  41 */     this.y = 0;
/*  42 */     this.exchangeId = 0;
/*  43 */     this.conditionId = 0;
/*  44 */     this.conditionAmount = 0;
/*  45 */     this.goalId = 0;
/*  46 */     this.goalAmount = 0;
/*  47 */     this.score = 0;
/*  48 */     this.timesLimit = 0;
/*     */   }
/*     */   
/*     */   public RichRiskExchangeBO(ResultSet rs) throws Exception {
/*  52 */     this.id = rs.getLong(1);
/*  53 */     this.pid = rs.getLong(2);
/*  54 */     this.x = rs.getInt(3);
/*  55 */     this.y = rs.getInt(4);
/*  56 */     this.exchangeId = rs.getInt(5);
/*  57 */     this.conditionId = rs.getInt(6);
/*  58 */     this.conditionAmount = rs.getInt(7);
/*  59 */     this.goalId = rs.getInt(8);
/*  60 */     this.goalAmount = rs.getInt(9);
/*  61 */     this.score = rs.getInt(10);
/*  62 */     this.timesLimit = rs.getInt(11);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<RichRiskExchangeBO> list) throws Exception {
/*  68 */     list.add(new RichRiskExchangeBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  73 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  78 */     return "`id`, `pid`, `x`, `y`, `exchangeId`, `conditionId`, `conditionAmount`, `goalId`, `goalAmount`, `score`, `timesLimit`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  83 */     return "`richRiskExchange`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  88 */     StringBuilder strBuf = new StringBuilder();
/*  89 */     strBuf.append("'").append(this.id).append("', ");
/*  90 */     strBuf.append("'").append(this.pid).append("', ");
/*  91 */     strBuf.append("'").append(this.x).append("', ");
/*  92 */     strBuf.append("'").append(this.y).append("', ");
/*  93 */     strBuf.append("'").append(this.exchangeId).append("', ");
/*  94 */     strBuf.append("'").append(this.conditionId).append("', ");
/*  95 */     strBuf.append("'").append(this.conditionAmount).append("', ");
/*  96 */     strBuf.append("'").append(this.goalId).append("', ");
/*  97 */     strBuf.append("'").append(this.goalAmount).append("', ");
/*  98 */     strBuf.append("'").append(this.score).append("', ");
/*  99 */     strBuf.append("'").append(this.timesLimit).append("', ");
/* 100 */     strBuf.deleteCharAt(strBuf.length() - 2);
/* 101 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/* 106 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/* 107 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/* 112 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 117 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 121 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 123 */     if (pid == this.pid)
/*     */       return; 
/* 125 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 128 */     if (pid == this.pid)
/*     */       return; 
/* 130 */     this.pid = pid;
/* 131 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public int getX() {
/* 135 */     return this.x;
/*     */   } public void setX(int x) {
/* 137 */     if (x == this.x)
/*     */       return; 
/* 139 */     this.x = x;
/*     */   }
/*     */   public void saveX(int x) {
/* 142 */     if (x == this.x)
/*     */       return; 
/* 144 */     this.x = x;
/* 145 */     saveField("x", Integer.valueOf(x));
/*     */   }
/*     */   
/*     */   public int getY() {
/* 149 */     return this.y;
/*     */   } public void setY(int y) {
/* 151 */     if (y == this.y)
/*     */       return; 
/* 153 */     this.y = y;
/*     */   }
/*     */   public void saveY(int y) {
/* 156 */     if (y == this.y)
/*     */       return; 
/* 158 */     this.y = y;
/* 159 */     saveField("y", Integer.valueOf(y));
/*     */   }
/*     */   
/*     */   public int getExchangeId() {
/* 163 */     return this.exchangeId;
/*     */   } public void setExchangeId(int exchangeId) {
/* 165 */     if (exchangeId == this.exchangeId)
/*     */       return; 
/* 167 */     this.exchangeId = exchangeId;
/*     */   }
/*     */   public void saveExchangeId(int exchangeId) {
/* 170 */     if (exchangeId == this.exchangeId)
/*     */       return; 
/* 172 */     this.exchangeId = exchangeId;
/* 173 */     saveField("exchangeId", Integer.valueOf(exchangeId));
/*     */   }
/*     */   
/*     */   public int getConditionId() {
/* 177 */     return this.conditionId;
/*     */   } public void setConditionId(int conditionId) {
/* 179 */     if (conditionId == this.conditionId)
/*     */       return; 
/* 181 */     this.conditionId = conditionId;
/*     */   }
/*     */   public void saveConditionId(int conditionId) {
/* 184 */     if (conditionId == this.conditionId)
/*     */       return; 
/* 186 */     this.conditionId = conditionId;
/* 187 */     saveField("conditionId", Integer.valueOf(conditionId));
/*     */   }
/*     */   
/*     */   public int getConditionAmount() {
/* 191 */     return this.conditionAmount;
/*     */   } public void setConditionAmount(int conditionAmount) {
/* 193 */     if (conditionAmount == this.conditionAmount)
/*     */       return; 
/* 195 */     this.conditionAmount = conditionAmount;
/*     */   }
/*     */   public void saveConditionAmount(int conditionAmount) {
/* 198 */     if (conditionAmount == this.conditionAmount)
/*     */       return; 
/* 200 */     this.conditionAmount = conditionAmount;
/* 201 */     saveField("conditionAmount", Integer.valueOf(conditionAmount));
/*     */   }
/*     */   
/*     */   public int getGoalId() {
/* 205 */     return this.goalId;
/*     */   } public void setGoalId(int goalId) {
/* 207 */     if (goalId == this.goalId)
/*     */       return; 
/* 209 */     this.goalId = goalId;
/*     */   }
/*     */   public void saveGoalId(int goalId) {
/* 212 */     if (goalId == this.goalId)
/*     */       return; 
/* 214 */     this.goalId = goalId;
/* 215 */     saveField("goalId", Integer.valueOf(goalId));
/*     */   }
/*     */   
/*     */   public int getGoalAmount() {
/* 219 */     return this.goalAmount;
/*     */   } public void setGoalAmount(int goalAmount) {
/* 221 */     if (goalAmount == this.goalAmount)
/*     */       return; 
/* 223 */     this.goalAmount = goalAmount;
/*     */   }
/*     */   public void saveGoalAmount(int goalAmount) {
/* 226 */     if (goalAmount == this.goalAmount)
/*     */       return; 
/* 228 */     this.goalAmount = goalAmount;
/* 229 */     saveField("goalAmount", Integer.valueOf(goalAmount));
/*     */   }
/*     */   
/*     */   public int getScore() {
/* 233 */     return this.score;
/*     */   } public void setScore(int score) {
/* 235 */     if (score == this.score)
/*     */       return; 
/* 237 */     this.score = score;
/*     */   }
/*     */   public void saveScore(int score) {
/* 240 */     if (score == this.score)
/*     */       return; 
/* 242 */     this.score = score;
/* 243 */     saveField("score", Integer.valueOf(score));
/*     */   }
/*     */   
/*     */   public int getTimesLimit() {
/* 247 */     return this.timesLimit;
/*     */   } public void setTimesLimit(int timesLimit) {
/* 249 */     if (timesLimit == this.timesLimit)
/*     */       return; 
/* 251 */     this.timesLimit = timesLimit;
/*     */   }
/*     */   public void saveTimesLimit(int timesLimit) {
/* 254 */     if (timesLimit == this.timesLimit)
/*     */       return; 
/* 256 */     this.timesLimit = timesLimit;
/* 257 */     saveField("timesLimit", Integer.valueOf(timesLimit));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 264 */     StringBuilder sBuilder = new StringBuilder();
/* 265 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 266 */     sBuilder.append(" `x` = '").append(this.x).append("',");
/* 267 */     sBuilder.append(" `y` = '").append(this.y).append("',");
/* 268 */     sBuilder.append(" `exchangeId` = '").append(this.exchangeId).append("',");
/* 269 */     sBuilder.append(" `conditionId` = '").append(this.conditionId).append("',");
/* 270 */     sBuilder.append(" `conditionAmount` = '").append(this.conditionAmount).append("',");
/* 271 */     sBuilder.append(" `goalId` = '").append(this.goalId).append("',");
/* 272 */     sBuilder.append(" `goalAmount` = '").append(this.goalAmount).append("',");
/* 273 */     sBuilder.append(" `score` = '").append(this.score).append("',");
/* 274 */     sBuilder.append(" `timesLimit` = '").append(this.timesLimit).append("',");
/* 275 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 276 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 280 */     String sql = "CREATE TABLE IF NOT EXISTS `richRiskExchange` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家pid',`x` int(11) NOT NULL DEFAULT '0' COMMENT '坐标的X',`y` int(11) NOT NULL DEFAULT '0' COMMENT '坐标的Y',`exchangeId` int(11) NOT NULL DEFAULT '0' COMMENT '兑换Id',`conditionId` int(11) NOT NULL DEFAULT '0' COMMENT '条件物品Id',`conditionAmount` int(11) NOT NULL DEFAULT '0' COMMENT '条件物品数量',`goalId` int(11) NOT NULL DEFAULT '0' COMMENT '兑换物Id',`goalAmount` int(11) NOT NULL DEFAULT '0' COMMENT '兑换物数量',`score` int(11) NOT NULL DEFAULT '0' COMMENT '获得积分',`timesLimit` int(11) NOT NULL DEFAULT '0' COMMENT '次数限制',PRIMARY KEY (`id`)) COMMENT='大富翁商品兑换表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 293 */       ServerConfig.getInitialID() + 1L);
/* 294 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/RichRiskExchangeBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */