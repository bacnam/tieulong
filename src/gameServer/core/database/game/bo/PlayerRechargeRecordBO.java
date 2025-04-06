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
/*     */ public class PlayerRechargeRecordBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家角色id")
/*     */   private long pid;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "goodsID", comment = "商品id")
/*     */   private String goodsID;
/*     */   @DataBaseField(type = "int(11)", fieldname = "lastBuyTime", comment = "最近一次购买时间")
/*     */   private int lastBuyTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "buyCount", comment = "购买次数")
/*     */   private int buyCount;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "resetSign", comment = "重置标识")
/*     */   private String resetSign;
/*     */   
/*     */   public PlayerRechargeRecordBO() {
/*  28 */     this.id = 0L;
/*  29 */     this.pid = 0L;
/*  30 */     this.goodsID = "";
/*  31 */     this.lastBuyTime = 0;
/*  32 */     this.buyCount = 0;
/*  33 */     this.resetSign = "";
/*     */   }
/*     */   
/*     */   public PlayerRechargeRecordBO(ResultSet rs) throws Exception {
/*  37 */     this.id = rs.getLong(1);
/*  38 */     this.pid = rs.getLong(2);
/*  39 */     this.goodsID = rs.getString(3);
/*  40 */     this.lastBuyTime = rs.getInt(4);
/*  41 */     this.buyCount = rs.getInt(5);
/*  42 */     this.resetSign = rs.getString(6);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<PlayerRechargeRecordBO> list) throws Exception {
/*  48 */     list.add(new PlayerRechargeRecordBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  53 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  58 */     return "`id`, `pid`, `goodsID`, `lastBuyTime`, `buyCount`, `resetSign`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  63 */     return "`playerRechargeRecord`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  68 */     StringBuilder strBuf = new StringBuilder();
/*  69 */     strBuf.append("'").append(this.id).append("', ");
/*  70 */     strBuf.append("'").append(this.pid).append("', ");
/*  71 */     strBuf.append("'").append((this.goodsID == null) ? null : this.goodsID.replace("'", "''")).append("', ");
/*  72 */     strBuf.append("'").append(this.lastBuyTime).append("', ");
/*  73 */     strBuf.append("'").append(this.buyCount).append("', ");
/*  74 */     strBuf.append("'").append((this.resetSign == null) ? null : this.resetSign.replace("'", "''")).append("', ");
/*  75 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  76 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  81 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  82 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  87 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/*  92 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/*  96 */     return this.pid;
/*     */   } public void setPid(long pid) {
/*  98 */     if (pid == this.pid)
/*     */       return; 
/* 100 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 103 */     if (pid == this.pid)
/*     */       return; 
/* 105 */     this.pid = pid;
/* 106 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public String getGoodsID() {
/* 110 */     return this.goodsID;
/*     */   } public void setGoodsID(String goodsID) {
/* 112 */     if (goodsID.equals(this.goodsID))
/*     */       return; 
/* 114 */     this.goodsID = goodsID;
/*     */   }
/*     */   public void saveGoodsID(String goodsID) {
/* 117 */     if (goodsID.equals(this.goodsID))
/*     */       return; 
/* 119 */     this.goodsID = goodsID;
/* 120 */     saveField("goodsID", goodsID);
/*     */   }
/*     */   
/*     */   public int getLastBuyTime() {
/* 124 */     return this.lastBuyTime;
/*     */   } public void setLastBuyTime(int lastBuyTime) {
/* 126 */     if (lastBuyTime == this.lastBuyTime)
/*     */       return; 
/* 128 */     this.lastBuyTime = lastBuyTime;
/*     */   }
/*     */   public void saveLastBuyTime(int lastBuyTime) {
/* 131 */     if (lastBuyTime == this.lastBuyTime)
/*     */       return; 
/* 133 */     this.lastBuyTime = lastBuyTime;
/* 134 */     saveField("lastBuyTime", Integer.valueOf(lastBuyTime));
/*     */   }
/*     */   
/*     */   public int getBuyCount() {
/* 138 */     return this.buyCount;
/*     */   } public void setBuyCount(int buyCount) {
/* 140 */     if (buyCount == this.buyCount)
/*     */       return; 
/* 142 */     this.buyCount = buyCount;
/*     */   }
/*     */   public void saveBuyCount(int buyCount) {
/* 145 */     if (buyCount == this.buyCount)
/*     */       return; 
/* 147 */     this.buyCount = buyCount;
/* 148 */     saveField("buyCount", Integer.valueOf(buyCount));
/*     */   }
/*     */   
/*     */   public String getResetSign() {
/* 152 */     return this.resetSign;
/*     */   } public void setResetSign(String resetSign) {
/* 154 */     if (resetSign.equals(this.resetSign))
/*     */       return; 
/* 156 */     this.resetSign = resetSign;
/*     */   }
/*     */   public void saveResetSign(String resetSign) {
/* 159 */     if (resetSign.equals(this.resetSign))
/*     */       return; 
/* 161 */     this.resetSign = resetSign;
/* 162 */     saveField("resetSign", resetSign);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 169 */     StringBuilder sBuilder = new StringBuilder();
/* 170 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 171 */     sBuilder.append(" `goodsID` = '").append((this.goodsID == null) ? null : this.goodsID.replace("'", "''")).append("',");
/* 172 */     sBuilder.append(" `lastBuyTime` = '").append(this.lastBuyTime).append("',");
/* 173 */     sBuilder.append(" `buyCount` = '").append(this.buyCount).append("',");
/* 174 */     sBuilder.append(" `resetSign` = '").append((this.resetSign == null) ? null : this.resetSign.replace("'", "''")).append("',");
/* 175 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 176 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 180 */     String sql = "CREATE TABLE IF NOT EXISTS `playerRechargeRecord` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家角色id',`goodsID` varchar(500) NOT NULL DEFAULT '' COMMENT '商品id',`lastBuyTime` int(11) NOT NULL DEFAULT '0' COMMENT '最近一次购买时间',`buyCount` int(11) NOT NULL DEFAULT '0' COMMENT '购买次数',`resetSign` varchar(500) NOT NULL DEFAULT '' COMMENT '重置标识',PRIMARY KEY (`id`)) COMMENT='玩家地宫记录'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 188 */       ServerConfig.getInitialID() + 1L);
/* 189 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/PlayerRechargeRecordBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */