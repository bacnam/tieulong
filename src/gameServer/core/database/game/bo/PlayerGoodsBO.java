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
/*     */ public class PlayerGoodsBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家ID")
/*     */   private long pid;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "goods_id", comment = "商品资源ID(一般商品:RefGoodsInfo表ID,限时商品:LimitedGoods表主键,公会商品:GuildGoods表主键)")
/*     */   private long goods_id;
/*     */   @DataBaseField(type = "int(11)", fieldname = "store_id", comment = "商店ID")
/*     */   private int store_id;
/*     */   @DataBaseField(type = "int(11)", fieldname = "store_type", comment = "商店类型")
/*     */   private int store_type;
/*     */   @DataBaseField(type = "varchar(64)", fieldname = "cost_uniform_id", comment = "消费代币UniformId")
/*     */   private String cost_uniform_id;
/*     */   @DataBaseField(type = "varchar(64)", fieldname = "price", comment = "商品原价")
/*     */   private String price;
/*     */   @DataBaseField(type = "varchar(64)", fieldname = "discount", comment = "折后价格")
/*     */   private String discount;
/*     */   @DataBaseField(type = "int(11)", fieldname = "amount", comment = "单次购买数量")
/*     */   private int amount;
/*     */   @DataBaseField(type = "int(11)", fieldname = "buy_times", comment = "已购买次数")
/*     */   private int buy_times;
/*     */   @DataBaseField(type = "int(11)", fieldname = "total_buy_times", comment = "总购买次数")
/*     */   private int total_buy_times;
/*     */   @DataBaseField(type = "tinyint(1)", fieldname = "soldout", comment = "是否已卖出")
/*     */   private boolean soldout;
/*     */   @DataBaseField(type = "int(11)", fieldname = "createTime", comment = "创建时间")
/*     */   private int createTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "level", comment = "关卡ID")
/*     */   private int level;
/*     */   
/*     */   public PlayerGoodsBO() {
/*  44 */     this.id = 0L;
/*  45 */     this.pid = 0L;
/*  46 */     this.goods_id = 0L;
/*  47 */     this.store_id = 0;
/*  48 */     this.store_type = 0;
/*  49 */     this.cost_uniform_id = "";
/*  50 */     this.price = "";
/*  51 */     this.discount = "";
/*  52 */     this.amount = 0;
/*  53 */     this.buy_times = 0;
/*  54 */     this.total_buy_times = 0;
/*  55 */     this.soldout = false;
/*  56 */     this.createTime = 0;
/*  57 */     this.level = 0;
/*     */   }
/*     */   
/*     */   public PlayerGoodsBO(ResultSet rs) throws Exception {
/*  61 */     this.id = rs.getLong(1);
/*  62 */     this.pid = rs.getLong(2);
/*  63 */     this.goods_id = rs.getLong(3);
/*  64 */     this.store_id = rs.getInt(4);
/*  65 */     this.store_type = rs.getInt(5);
/*  66 */     this.cost_uniform_id = rs.getString(6);
/*  67 */     this.price = rs.getString(7);
/*  68 */     this.discount = rs.getString(8);
/*  69 */     this.amount = rs.getInt(9);
/*  70 */     this.buy_times = rs.getInt(10);
/*  71 */     this.total_buy_times = rs.getInt(11);
/*  72 */     this.soldout = rs.getBoolean(12);
/*  73 */     this.createTime = rs.getInt(13);
/*  74 */     this.level = rs.getInt(14);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<PlayerGoodsBO> list) throws Exception {
/*  80 */     list.add(new PlayerGoodsBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  85 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  90 */     return "`id`, `pid`, `goods_id`, `store_id`, `store_type`, `cost_uniform_id`, `price`, `discount`, `amount`, `buy_times`, `total_buy_times`, `soldout`, `createTime`, `level`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  95 */     return "`player_goods`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/* 100 */     StringBuilder strBuf = new StringBuilder();
/* 101 */     strBuf.append("'").append(this.id).append("', ");
/* 102 */     strBuf.append("'").append(this.pid).append("', ");
/* 103 */     strBuf.append("'").append(this.goods_id).append("', ");
/* 104 */     strBuf.append("'").append(this.store_id).append("', ");
/* 105 */     strBuf.append("'").append(this.store_type).append("', ");
/* 106 */     strBuf.append("'").append((this.cost_uniform_id == null) ? null : this.cost_uniform_id.replace("'", "''")).append("', ");
/* 107 */     strBuf.append("'").append((this.price == null) ? null : this.price.replace("'", "''")).append("', ");
/* 108 */     strBuf.append("'").append((this.discount == null) ? null : this.discount.replace("'", "''")).append("', ");
/* 109 */     strBuf.append("'").append(this.amount).append("', ");
/* 110 */     strBuf.append("'").append(this.buy_times).append("', ");
/* 111 */     strBuf.append("'").append(this.total_buy_times).append("', ");
/* 112 */     strBuf.append("'").append(this.soldout ? 1 : 0).append("', ");
/* 113 */     strBuf.append("'").append(this.createTime).append("', ");
/* 114 */     strBuf.append("'").append(this.level).append("', ");
/* 115 */     strBuf.deleteCharAt(strBuf.length() - 2);
/* 116 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/* 121 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/* 122 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/* 127 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 132 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 136 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 138 */     if (pid == this.pid)
/*     */       return; 
/* 140 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 143 */     if (pid == this.pid)
/*     */       return; 
/* 145 */     this.pid = pid;
/* 146 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public long getGoodsId() {
/* 150 */     return this.goods_id;
/*     */   } public void setGoodsId(long goods_id) {
/* 152 */     if (goods_id == this.goods_id)
/*     */       return; 
/* 154 */     this.goods_id = goods_id;
/*     */   }
/*     */   public void saveGoodsId(long goods_id) {
/* 157 */     if (goods_id == this.goods_id)
/*     */       return; 
/* 159 */     this.goods_id = goods_id;
/* 160 */     saveField("goods_id", Long.valueOf(goods_id));
/*     */   }
/*     */   
/*     */   public int getStoreId() {
/* 164 */     return this.store_id;
/*     */   } public void setStoreId(int store_id) {
/* 166 */     if (store_id == this.store_id)
/*     */       return; 
/* 168 */     this.store_id = store_id;
/*     */   }
/*     */   public void saveStoreId(int store_id) {
/* 171 */     if (store_id == this.store_id)
/*     */       return; 
/* 173 */     this.store_id = store_id;
/* 174 */     saveField("store_id", Integer.valueOf(store_id));
/*     */   }
/*     */   
/*     */   public int getStoreType() {
/* 178 */     return this.store_type;
/*     */   } public void setStoreType(int store_type) {
/* 180 */     if (store_type == this.store_type)
/*     */       return; 
/* 182 */     this.store_type = store_type;
/*     */   }
/*     */   public void saveStoreType(int store_type) {
/* 185 */     if (store_type == this.store_type)
/*     */       return; 
/* 187 */     this.store_type = store_type;
/* 188 */     saveField("store_type", Integer.valueOf(store_type));
/*     */   }
/*     */   
/*     */   public String getCostUniformId() {
/* 192 */     return this.cost_uniform_id;
/*     */   } public void setCostUniformId(String cost_uniform_id) {
/* 194 */     if (cost_uniform_id.equals(this.cost_uniform_id))
/*     */       return; 
/* 196 */     this.cost_uniform_id = cost_uniform_id;
/*     */   }
/*     */   public void saveCostUniformId(String cost_uniform_id) {
/* 199 */     if (cost_uniform_id.equals(this.cost_uniform_id))
/*     */       return; 
/* 201 */     this.cost_uniform_id = cost_uniform_id;
/* 202 */     saveField("cost_uniform_id", cost_uniform_id);
/*     */   }
/*     */   
/*     */   public String getPrice() {
/* 206 */     return this.price;
/*     */   } public void setPrice(String price) {
/* 208 */     if (price.equals(this.price))
/*     */       return; 
/* 210 */     this.price = price;
/*     */   }
/*     */   public void savePrice(String price) {
/* 213 */     if (price.equals(this.price))
/*     */       return; 
/* 215 */     this.price = price;
/* 216 */     saveField("price", price);
/*     */   }
/*     */   
/*     */   public String getDiscount() {
/* 220 */     return this.discount;
/*     */   } public void setDiscount(String discount) {
/* 222 */     if (discount.equals(this.discount))
/*     */       return; 
/* 224 */     this.discount = discount;
/*     */   }
/*     */   public void saveDiscount(String discount) {
/* 227 */     if (discount.equals(this.discount))
/*     */       return; 
/* 229 */     this.discount = discount;
/* 230 */     saveField("discount", discount);
/*     */   }
/*     */   
/*     */   public int getAmount() {
/* 234 */     return this.amount;
/*     */   } public void setAmount(int amount) {
/* 236 */     if (amount == this.amount)
/*     */       return; 
/* 238 */     this.amount = amount;
/*     */   }
/*     */   public void saveAmount(int amount) {
/* 241 */     if (amount == this.amount)
/*     */       return; 
/* 243 */     this.amount = amount;
/* 244 */     saveField("amount", Integer.valueOf(amount));
/*     */   }
/*     */   
/*     */   public int getBuyTimes() {
/* 248 */     return this.buy_times;
/*     */   } public void setBuyTimes(int buy_times) {
/* 250 */     if (buy_times == this.buy_times)
/*     */       return; 
/* 252 */     this.buy_times = buy_times;
/*     */   }
/*     */   public void saveBuyTimes(int buy_times) {
/* 255 */     if (buy_times == this.buy_times)
/*     */       return; 
/* 257 */     this.buy_times = buy_times;
/* 258 */     saveField("buy_times", Integer.valueOf(buy_times));
/*     */   }
/*     */   
/*     */   public int getTotalBuyTimes() {
/* 262 */     return this.total_buy_times;
/*     */   } public void setTotalBuyTimes(int total_buy_times) {
/* 264 */     if (total_buy_times == this.total_buy_times)
/*     */       return; 
/* 266 */     this.total_buy_times = total_buy_times;
/*     */   }
/*     */   public void saveTotalBuyTimes(int total_buy_times) {
/* 269 */     if (total_buy_times == this.total_buy_times)
/*     */       return; 
/* 271 */     this.total_buy_times = total_buy_times;
/* 272 */     saveField("total_buy_times", Integer.valueOf(total_buy_times));
/*     */   }
/*     */   
/*     */   public boolean getSoldout() {
/* 276 */     return this.soldout;
/*     */   } public void setSoldout(boolean soldout) {
/* 278 */     if (soldout == this.soldout)
/*     */       return; 
/* 280 */     this.soldout = soldout;
/*     */   }
/*     */   public void saveSoldout(boolean soldout) {
/* 283 */     if (soldout == this.soldout)
/*     */       return; 
/* 285 */     this.soldout = soldout;
/* 286 */     saveField("soldout", Integer.valueOf(soldout ? 1 : 0));
/*     */   }
/*     */   
/*     */   public int getCreateTime() {
/* 290 */     return this.createTime;
/*     */   } public void setCreateTime(int createTime) {
/* 292 */     if (createTime == this.createTime)
/*     */       return; 
/* 294 */     this.createTime = createTime;
/*     */   }
/*     */   public void saveCreateTime(int createTime) {
/* 297 */     if (createTime == this.createTime)
/*     */       return; 
/* 299 */     this.createTime = createTime;
/* 300 */     saveField("createTime", Integer.valueOf(createTime));
/*     */   }
/*     */   
/*     */   public int getLevel() {
/* 304 */     return this.level;
/*     */   } public void setLevel(int level) {
/* 306 */     if (level == this.level)
/*     */       return; 
/* 308 */     this.level = level;
/*     */   }
/*     */   public void saveLevel(int level) {
/* 311 */     if (level == this.level)
/*     */       return; 
/* 313 */     this.level = level;
/* 314 */     saveField("level", Integer.valueOf(level));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 321 */     StringBuilder sBuilder = new StringBuilder();
/* 322 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 323 */     sBuilder.append(" `goods_id` = '").append(this.goods_id).append("',");
/* 324 */     sBuilder.append(" `store_id` = '").append(this.store_id).append("',");
/* 325 */     sBuilder.append(" `store_type` = '").append(this.store_type).append("',");
/* 326 */     sBuilder.append(" `cost_uniform_id` = '").append((this.cost_uniform_id == null) ? null : this.cost_uniform_id.replace("'", "''")).append("',");
/* 327 */     sBuilder.append(" `price` = '").append((this.price == null) ? null : this.price.replace("'", "''")).append("',");
/* 328 */     sBuilder.append(" `discount` = '").append((this.discount == null) ? null : this.discount.replace("'", "''")).append("',");
/* 329 */     sBuilder.append(" `amount` = '").append(this.amount).append("',");
/* 330 */     sBuilder.append(" `buy_times` = '").append(this.buy_times).append("',");
/* 331 */     sBuilder.append(" `total_buy_times` = '").append(this.total_buy_times).append("',");
/* 332 */     sBuilder.append(" `soldout` = '").append(this.soldout ? 1 : 0).append("',");
/* 333 */     sBuilder.append(" `createTime` = '").append(this.createTime).append("',");
/* 334 */     sBuilder.append(" `level` = '").append(this.level).append("',");
/* 335 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 336 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 340 */     String sql = "CREATE TABLE IF NOT EXISTS `player_goods` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家ID',`goods_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '商品资源ID(一般商品:RefGoodsInfo表ID,限时商品:LimitedGoods表主键,公会商品:GuildGoods表主键)',`store_id` int(11) NOT NULL DEFAULT '0' COMMENT '商店ID',`store_type` int(11) NOT NULL DEFAULT '0' COMMENT '商店类型',`cost_uniform_id` varchar(64) NOT NULL DEFAULT '' COMMENT '消费代币UniformId',`price` varchar(64) NOT NULL DEFAULT '' COMMENT '商品原价',`discount` varchar(64) NOT NULL DEFAULT '' COMMENT '折后价格',`amount` int(11) NOT NULL DEFAULT '0' COMMENT '单次购买数量',`buy_times` int(11) NOT NULL DEFAULT '0' COMMENT '已购买次数',`total_buy_times` int(11) NOT NULL DEFAULT '0' COMMENT '总购买次数',`soldout` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已卖出',`createTime` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',`level` int(11) NOT NULL DEFAULT '0' COMMENT '关卡ID',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='玩家商品信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 357 */       ServerConfig.getInitialID() + 1L);
/* 358 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/PlayerGoodsBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */