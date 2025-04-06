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
/*     */ public class RechargeResetBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "varchar(100)", fieldname = "goodsid", comment = "商品id")
/*     */   private String goodsid;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "resetSign", comment = "重置签名")
/*     */   private String resetSign;
/*     */   
/*     */   public RechargeResetBO() {
/*  22 */     this.id = 0L;
/*  23 */     this.goodsid = "";
/*  24 */     this.resetSign = "";
/*     */   }
/*     */   
/*     */   public RechargeResetBO(ResultSet rs) throws Exception {
/*  28 */     this.id = rs.getLong(1);
/*  29 */     this.goodsid = rs.getString(2);
/*  30 */     this.resetSign = rs.getString(3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<RechargeResetBO> list) throws Exception {
/*  36 */     list.add(new RechargeResetBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  41 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  46 */     return "`id`, `goodsid`, `resetSign`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  51 */     return "`rechargeReset`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  56 */     StringBuilder strBuf = new StringBuilder();
/*  57 */     strBuf.append("'").append(this.id).append("', ");
/*  58 */     strBuf.append("'").append((this.goodsid == null) ? null : this.goodsid.replace("'", "''")).append("', ");
/*  59 */     strBuf.append("'").append((this.resetSign == null) ? null : this.resetSign.replace("'", "''")).append("', ");
/*  60 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  61 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  66 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  67 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  72 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/*  77 */     return this.id;
/*     */   }
/*     */   
/*     */   public String getGoodsid() {
/*  81 */     return this.goodsid;
/*     */   } public void setGoodsid(String goodsid) {
/*  83 */     if (goodsid.equals(this.goodsid))
/*     */       return; 
/*  85 */     this.goodsid = goodsid;
/*     */   }
/*     */   public void saveGoodsid(String goodsid) {
/*  88 */     if (goodsid.equals(this.goodsid))
/*     */       return; 
/*  90 */     this.goodsid = goodsid;
/*  91 */     saveField("goodsid", goodsid);
/*     */   }
/*     */   
/*     */   public String getResetSign() {
/*  95 */     return this.resetSign;
/*     */   } public void setResetSign(String resetSign) {
/*  97 */     if (resetSign.equals(this.resetSign))
/*     */       return; 
/*  99 */     this.resetSign = resetSign;
/*     */   }
/*     */   public void saveResetSign(String resetSign) {
/* 102 */     if (resetSign.equals(this.resetSign))
/*     */       return; 
/* 104 */     this.resetSign = resetSign;
/* 105 */     saveField("resetSign", resetSign);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 112 */     StringBuilder sBuilder = new StringBuilder();
/* 113 */     sBuilder.append(" `goodsid` = '").append((this.goodsid == null) ? null : this.goodsid.replace("'", "''")).append("',");
/* 114 */     sBuilder.append(" `resetSign` = '").append((this.resetSign == null) ? null : this.resetSign.replace("'", "''")).append("',");
/* 115 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 116 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 120 */     String sql = "CREATE TABLE IF NOT EXISTS `rechargeReset` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`goodsid` varchar(100) NOT NULL DEFAULT '' COMMENT '商品id',`resetSign` varchar(500) NOT NULL DEFAULT '' COMMENT '重置签名',KEY `goodsid` (`goodsid`),PRIMARY KEY (`id`)) COMMENT='玩家大富翁信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 126 */       ServerConfig.getInitialID() + 1L);
/* 127 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/RechargeResetBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */