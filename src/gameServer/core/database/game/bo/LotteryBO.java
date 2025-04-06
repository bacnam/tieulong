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
/*     */ public class LotteryBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "所属玩家pid")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "num", comment = "期数")
/*     */   private int num;
/*     */   @DataBaseField(type = "int(11)", fieldname = "number", comment = "号码")
/*     */   private int number;
/*     */   @DataBaseField(type = "int(11)", fieldname = "type", comment = "种类")
/*     */   private int type;
/*     */   @DataBaseField(type = "int(11)", fieldname = "buyday", comment = "购买日期")
/*     */   private int buyday;
/*     */   @DataBaseField(type = "int(11)", fieldname = "rewardday", comment = "中奖日期")
/*     */   private int rewardday;
/*     */   
/*     */   public LotteryBO() {
/*  30 */     this.id = 0L;
/*  31 */     this.pid = 0L;
/*  32 */     this.num = 0;
/*  33 */     this.number = 0;
/*  34 */     this.type = 0;
/*  35 */     this.buyday = 0;
/*  36 */     this.rewardday = 0;
/*     */   }
/*     */   
/*     */   public LotteryBO(ResultSet rs) throws Exception {
/*  40 */     this.id = rs.getLong(1);
/*  41 */     this.pid = rs.getLong(2);
/*  42 */     this.num = rs.getInt(3);
/*  43 */     this.number = rs.getInt(4);
/*  44 */     this.type = rs.getInt(5);
/*  45 */     this.buyday = rs.getInt(6);
/*  46 */     this.rewardday = rs.getInt(7);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<LotteryBO> list) throws Exception {
/*  52 */     list.add(new LotteryBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  57 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  62 */     return "`id`, `pid`, `num`, `number`, `type`, `buyday`, `rewardday`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  67 */     return "`lottery`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  72 */     StringBuilder strBuf = new StringBuilder();
/*  73 */     strBuf.append("'").append(this.id).append("', ");
/*  74 */     strBuf.append("'").append(this.pid).append("', ");
/*  75 */     strBuf.append("'").append(this.num).append("', ");
/*  76 */     strBuf.append("'").append(this.number).append("', ");
/*  77 */     strBuf.append("'").append(this.type).append("', ");
/*  78 */     strBuf.append("'").append(this.buyday).append("', ");
/*  79 */     strBuf.append("'").append(this.rewardday).append("', ");
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
/*     */   public int getNum() {
/* 115 */     return this.num;
/*     */   } public void setNum(int num) {
/* 117 */     if (num == this.num)
/*     */       return; 
/* 119 */     this.num = num;
/*     */   }
/*     */   public void saveNum(int num) {
/* 122 */     if (num == this.num)
/*     */       return; 
/* 124 */     this.num = num;
/* 125 */     saveField("num", Integer.valueOf(num));
/*     */   }
/*     */   
/*     */   public int getNumber() {
/* 129 */     return this.number;
/*     */   } public void setNumber(int number) {
/* 131 */     if (number == this.number)
/*     */       return; 
/* 133 */     this.number = number;
/*     */   }
/*     */   public void saveNumber(int number) {
/* 136 */     if (number == this.number)
/*     */       return; 
/* 138 */     this.number = number;
/* 139 */     saveField("number", Integer.valueOf(number));
/*     */   }
/*     */   
/*     */   public int getType() {
/* 143 */     return this.type;
/*     */   } public void setType(int type) {
/* 145 */     if (type == this.type)
/*     */       return; 
/* 147 */     this.type = type;
/*     */   }
/*     */   public void saveType(int type) {
/* 150 */     if (type == this.type)
/*     */       return; 
/* 152 */     this.type = type;
/* 153 */     saveField("type", Integer.valueOf(type));
/*     */   }
/*     */   
/*     */   public int getBuyday() {
/* 157 */     return this.buyday;
/*     */   } public void setBuyday(int buyday) {
/* 159 */     if (buyday == this.buyday)
/*     */       return; 
/* 161 */     this.buyday = buyday;
/*     */   }
/*     */   public void saveBuyday(int buyday) {
/* 164 */     if (buyday == this.buyday)
/*     */       return; 
/* 166 */     this.buyday = buyday;
/* 167 */     saveField("buyday", Integer.valueOf(buyday));
/*     */   }
/*     */   
/*     */   public int getRewardday() {
/* 171 */     return this.rewardday;
/*     */   } public void setRewardday(int rewardday) {
/* 173 */     if (rewardday == this.rewardday)
/*     */       return; 
/* 175 */     this.rewardday = rewardday;
/*     */   }
/*     */   public void saveRewardday(int rewardday) {
/* 178 */     if (rewardday == this.rewardday)
/*     */       return; 
/* 180 */     this.rewardday = rewardday;
/* 181 */     saveField("rewardday", Integer.valueOf(rewardday));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 188 */     StringBuilder sBuilder = new StringBuilder();
/* 189 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 190 */     sBuilder.append(" `num` = '").append(this.num).append("',");
/* 191 */     sBuilder.append(" `number` = '").append(this.number).append("',");
/* 192 */     sBuilder.append(" `type` = '").append(this.type).append("',");
/* 193 */     sBuilder.append(" `buyday` = '").append(this.buyday).append("',");
/* 194 */     sBuilder.append(" `rewardday` = '").append(this.rewardday).append("',");
/* 195 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 196 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 200 */     String sql = "CREATE TABLE IF NOT EXISTS `lottery` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属玩家pid',`num` int(11) NOT NULL DEFAULT '0' COMMENT '期数',`number` int(11) NOT NULL DEFAULT '0' COMMENT '号码',`type` int(11) NOT NULL DEFAULT '0' COMMENT '种类',`buyday` int(11) NOT NULL DEFAULT '0' COMMENT '购买日期',`rewardday` int(11) NOT NULL DEFAULT '0' COMMENT '中奖日期',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='一元购表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 210 */       ServerConfig.getInitialID() + 1L);
/* 211 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/LotteryBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */