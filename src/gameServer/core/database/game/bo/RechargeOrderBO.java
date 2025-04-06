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
/*     */ public class RechargeOrderBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家角色id")
/*     */   private long pid;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "open_id", comment = "平台服务器上生成的openid")
/*     */   private String open_id;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "productid", comment = "商品id")
/*     */   private String productid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "quantity", comment = "商品id")
/*     */   private int quantity;
/*     */   @DataBaseField(type = "int(11)", fieldname = "orderTime", comment = "到游戏服务器上时间")
/*     */   private int orderTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "deliverTime", comment = "到游戏服务器上时间")
/*     */   private int deliverTime;
/*     */   @DataBaseField(type = "varchar(100)", fieldname = "cporderid", comment = "cp定单号,php正式的，唯一")
/*     */   private String cporderid;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "adfromOrderid", comment = "渠道支付定单号")
/*     */   private String adfromOrderid;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "serverID", comment = "服务器ID")
/*     */   private String serverID;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "carrier", comment = "运营商")
/*     */   private String carrier;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "platform", comment = "平台ios，andriod，越狱")
/*     */   private String platform;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "adfrom", comment = "pp，360，qq 主来源1")
/*     */   private String adfrom;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "adfrom2", comment = "二级来源")
/*     */   private String adfrom2;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "gameid", comment = "梦加内部游戏id")
/*     */   private String gameid;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "appID", comment = "appID")
/*     */   private String appID;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "status", comment = "订单状态-已付款，已领取，已取消")
/*     */   private String status;
/*     */   
/*     */   public RechargeOrderBO() {
/*  50 */     this.id = 0L;
/*  51 */     this.pid = 0L;
/*  52 */     this.open_id = "";
/*  53 */     this.productid = "";
/*  54 */     this.quantity = 0;
/*  55 */     this.orderTime = 0;
/*  56 */     this.deliverTime = 0;
/*  57 */     this.cporderid = "";
/*  58 */     this.adfromOrderid = "";
/*  59 */     this.serverID = "";
/*  60 */     this.carrier = "";
/*  61 */     this.platform = "";
/*  62 */     this.adfrom = "";
/*  63 */     this.adfrom2 = "";
/*  64 */     this.gameid = "";
/*  65 */     this.appID = "";
/*  66 */     this.status = "";
/*     */   }
/*     */   
/*     */   public RechargeOrderBO(ResultSet rs) throws Exception {
/*  70 */     this.id = rs.getLong(1);
/*  71 */     this.pid = rs.getLong(2);
/*  72 */     this.open_id = rs.getString(3);
/*  73 */     this.productid = rs.getString(4);
/*  74 */     this.quantity = rs.getInt(5);
/*  75 */     this.orderTime = rs.getInt(6);
/*  76 */     this.deliverTime = rs.getInt(7);
/*  77 */     this.cporderid = rs.getString(8);
/*  78 */     this.adfromOrderid = rs.getString(9);
/*  79 */     this.serverID = rs.getString(10);
/*  80 */     this.carrier = rs.getString(11);
/*  81 */     this.platform = rs.getString(12);
/*  82 */     this.adfrom = rs.getString(13);
/*  83 */     this.adfrom2 = rs.getString(14);
/*  84 */     this.gameid = rs.getString(15);
/*  85 */     this.appID = rs.getString(16);
/*  86 */     this.status = rs.getString(17);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<RechargeOrderBO> list) throws Exception {
/*  92 */     list.add(new RechargeOrderBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  97 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/* 102 */     return "`id`, `pid`, `open_id`, `productid`, `quantity`, `orderTime`, `deliverTime`, `cporderid`, `adfromOrderid`, `serverID`, `carrier`, `platform`, `adfrom`, `adfrom2`, `gameid`, `appID`, `status`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/* 107 */     return "`rechargeOrder`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/* 112 */     StringBuilder strBuf = new StringBuilder();
/* 113 */     strBuf.append("'").append(this.id).append("', ");
/* 114 */     strBuf.append("'").append(this.pid).append("', ");
/* 115 */     strBuf.append("'").append((this.open_id == null) ? null : this.open_id.replace("'", "''")).append("', ");
/* 116 */     strBuf.append("'").append((this.productid == null) ? null : this.productid.replace("'", "''")).append("', ");
/* 117 */     strBuf.append("'").append(this.quantity).append("', ");
/* 118 */     strBuf.append("'").append(this.orderTime).append("', ");
/* 119 */     strBuf.append("'").append(this.deliverTime).append("', ");
/* 120 */     strBuf.append("'").append((this.cporderid == null) ? null : this.cporderid.replace("'", "''")).append("', ");
/* 121 */     strBuf.append("'").append((this.adfromOrderid == null) ? null : this.adfromOrderid.replace("'", "''")).append("', ");
/* 122 */     strBuf.append("'").append((this.serverID == null) ? null : this.serverID.replace("'", "''")).append("', ");
/* 123 */     strBuf.append("'").append((this.carrier == null) ? null : this.carrier.replace("'", "''")).append("', ");
/* 124 */     strBuf.append("'").append((this.platform == null) ? null : this.platform.replace("'", "''")).append("', ");
/* 125 */     strBuf.append("'").append((this.adfrom == null) ? null : this.adfrom.replace("'", "''")).append("', ");
/* 126 */     strBuf.append("'").append((this.adfrom2 == null) ? null : this.adfrom2.replace("'", "''")).append("', ");
/* 127 */     strBuf.append("'").append((this.gameid == null) ? null : this.gameid.replace("'", "''")).append("', ");
/* 128 */     strBuf.append("'").append((this.appID == null) ? null : this.appID.replace("'", "''")).append("', ");
/* 129 */     strBuf.append("'").append((this.status == null) ? null : this.status.replace("'", "''")).append("', ");
/* 130 */     strBuf.deleteCharAt(strBuf.length() - 2);
/* 131 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/* 136 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/* 137 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/* 142 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 147 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 151 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 153 */     if (pid == this.pid)
/*     */       return; 
/* 155 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 158 */     if (pid == this.pid)
/*     */       return; 
/* 160 */     this.pid = pid;
/* 161 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public String getOpenId() {
/* 165 */     return this.open_id;
/*     */   } public void setOpenId(String open_id) {
/* 167 */     if (open_id.equals(this.open_id))
/*     */       return; 
/* 169 */     this.open_id = open_id;
/*     */   }
/*     */   public void saveOpenId(String open_id) {
/* 172 */     if (open_id.equals(this.open_id))
/*     */       return; 
/* 174 */     this.open_id = open_id;
/* 175 */     saveField("open_id", open_id);
/*     */   }
/*     */   
/*     */   public String getProductid() {
/* 179 */     return this.productid;
/*     */   } public void setProductid(String productid) {
/* 181 */     if (productid.equals(this.productid))
/*     */       return; 
/* 183 */     this.productid = productid;
/*     */   }
/*     */   public void saveProductid(String productid) {
/* 186 */     if (productid.equals(this.productid))
/*     */       return; 
/* 188 */     this.productid = productid;
/* 189 */     saveField("productid", productid);
/*     */   }
/*     */   
/*     */   public int getQuantity() {
/* 193 */     return this.quantity;
/*     */   } public void setQuantity(int quantity) {
/* 195 */     if (quantity == this.quantity)
/*     */       return; 
/* 197 */     this.quantity = quantity;
/*     */   }
/*     */   public void saveQuantity(int quantity) {
/* 200 */     if (quantity == this.quantity)
/*     */       return; 
/* 202 */     this.quantity = quantity;
/* 203 */     saveField("quantity", Integer.valueOf(quantity));
/*     */   }
/*     */   
/*     */   public int getOrderTime() {
/* 207 */     return this.orderTime;
/*     */   } public void setOrderTime(int orderTime) {
/* 209 */     if (orderTime == this.orderTime)
/*     */       return; 
/* 211 */     this.orderTime = orderTime;
/*     */   }
/*     */   public void saveOrderTime(int orderTime) {
/* 214 */     if (orderTime == this.orderTime)
/*     */       return; 
/* 216 */     this.orderTime = orderTime;
/* 217 */     saveField("orderTime", Integer.valueOf(orderTime));
/*     */   }
/*     */   
/*     */   public int getDeliverTime() {
/* 221 */     return this.deliverTime;
/*     */   } public void setDeliverTime(int deliverTime) {
/* 223 */     if (deliverTime == this.deliverTime)
/*     */       return; 
/* 225 */     this.deliverTime = deliverTime;
/*     */   }
/*     */   public void saveDeliverTime(int deliverTime) {
/* 228 */     if (deliverTime == this.deliverTime)
/*     */       return; 
/* 230 */     this.deliverTime = deliverTime;
/* 231 */     saveField("deliverTime", Integer.valueOf(deliverTime));
/*     */   }
/*     */   
/*     */   public String getCporderid() {
/* 235 */     return this.cporderid;
/*     */   } public void setCporderid(String cporderid) {
/* 237 */     if (cporderid.equals(this.cporderid))
/*     */       return; 
/* 239 */     this.cporderid = cporderid;
/*     */   }
/*     */   public void saveCporderid(String cporderid) {
/* 242 */     if (cporderid.equals(this.cporderid))
/*     */       return; 
/* 244 */     this.cporderid = cporderid;
/* 245 */     saveField("cporderid", cporderid);
/*     */   }
/*     */   
/*     */   public String getAdfromOrderid() {
/* 249 */     return this.adfromOrderid;
/*     */   } public void setAdfromOrderid(String adfromOrderid) {
/* 251 */     if (adfromOrderid.equals(this.adfromOrderid))
/*     */       return; 
/* 253 */     this.adfromOrderid = adfromOrderid;
/*     */   }
/*     */   public void saveAdfromOrderid(String adfromOrderid) {
/* 256 */     if (adfromOrderid.equals(this.adfromOrderid))
/*     */       return; 
/* 258 */     this.adfromOrderid = adfromOrderid;
/* 259 */     saveField("adfromOrderid", adfromOrderid);
/*     */   }
/*     */   
/*     */   public String getServerID() {
/* 263 */     return this.serverID;
/*     */   } public void setServerID(String serverID) {
/* 265 */     if (serverID.equals(this.serverID))
/*     */       return; 
/* 267 */     this.serverID = serverID;
/*     */   }
/*     */   public void saveServerID(String serverID) {
/* 270 */     if (serverID.equals(this.serverID))
/*     */       return; 
/* 272 */     this.serverID = serverID;
/* 273 */     saveField("serverID", serverID);
/*     */   }
/*     */   
/*     */   public String getCarrier() {
/* 277 */     return this.carrier;
/*     */   } public void setCarrier(String carrier) {
/* 279 */     if (carrier.equals(this.carrier))
/*     */       return; 
/* 281 */     this.carrier = carrier;
/*     */   }
/*     */   public void saveCarrier(String carrier) {
/* 284 */     if (carrier.equals(this.carrier))
/*     */       return; 
/* 286 */     this.carrier = carrier;
/* 287 */     saveField("carrier", carrier);
/*     */   }
/*     */   
/*     */   public String getPlatform() {
/* 291 */     return this.platform;
/*     */   } public void setPlatform(String platform) {
/* 293 */     if (platform.equals(this.platform))
/*     */       return; 
/* 295 */     this.platform = platform;
/*     */   }
/*     */   public void savePlatform(String platform) {
/* 298 */     if (platform.equals(this.platform))
/*     */       return; 
/* 300 */     this.platform = platform;
/* 301 */     saveField("platform", platform);
/*     */   }
/*     */   
/*     */   public String getAdfrom() {
/* 305 */     return this.adfrom;
/*     */   } public void setAdfrom(String adfrom) {
/* 307 */     if (adfrom.equals(this.adfrom))
/*     */       return; 
/* 309 */     this.adfrom = adfrom;
/*     */   }
/*     */   public void saveAdfrom(String adfrom) {
/* 312 */     if (adfrom.equals(this.adfrom))
/*     */       return; 
/* 314 */     this.adfrom = adfrom;
/* 315 */     saveField("adfrom", adfrom);
/*     */   }
/*     */   
/*     */   public String getAdfrom2() {
/* 319 */     return this.adfrom2;
/*     */   } public void setAdfrom2(String adfrom2) {
/* 321 */     if (adfrom2.equals(this.adfrom2))
/*     */       return; 
/* 323 */     this.adfrom2 = adfrom2;
/*     */   }
/*     */   public void saveAdfrom2(String adfrom2) {
/* 326 */     if (adfrom2.equals(this.adfrom2))
/*     */       return; 
/* 328 */     this.adfrom2 = adfrom2;
/* 329 */     saveField("adfrom2", adfrom2);
/*     */   }
/*     */   
/*     */   public String getGameid() {
/* 333 */     return this.gameid;
/*     */   } public void setGameid(String gameid) {
/* 335 */     if (gameid.equals(this.gameid))
/*     */       return; 
/* 337 */     this.gameid = gameid;
/*     */   }
/*     */   public void saveGameid(String gameid) {
/* 340 */     if (gameid.equals(this.gameid))
/*     */       return; 
/* 342 */     this.gameid = gameid;
/* 343 */     saveField("gameid", gameid);
/*     */   }
/*     */   
/*     */   public String getAppID() {
/* 347 */     return this.appID;
/*     */   } public void setAppID(String appID) {
/* 349 */     if (appID.equals(this.appID))
/*     */       return; 
/* 351 */     this.appID = appID;
/*     */   }
/*     */   public void saveAppID(String appID) {
/* 354 */     if (appID.equals(this.appID))
/*     */       return; 
/* 356 */     this.appID = appID;
/* 357 */     saveField("appID", appID);
/*     */   }
/*     */   
/*     */   public String getStatus() {
/* 361 */     return this.status;
/*     */   } public void setStatus(String status) {
/* 363 */     if (status.equals(this.status))
/*     */       return; 
/* 365 */     this.status = status;
/*     */   }
/*     */   public void saveStatus(String status) {
/* 368 */     if (status.equals(this.status))
/*     */       return; 
/* 370 */     this.status = status;
/* 371 */     saveField("status", status);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 378 */     StringBuilder sBuilder = new StringBuilder();
/* 379 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 380 */     sBuilder.append(" `open_id` = '").append((this.open_id == null) ? null : this.open_id.replace("'", "''")).append("',");
/* 381 */     sBuilder.append(" `productid` = '").append((this.productid == null) ? null : this.productid.replace("'", "''")).append("',");
/* 382 */     sBuilder.append(" `quantity` = '").append(this.quantity).append("',");
/* 383 */     sBuilder.append(" `orderTime` = '").append(this.orderTime).append("',");
/* 384 */     sBuilder.append(" `deliverTime` = '").append(this.deliverTime).append("',");
/* 385 */     sBuilder.append(" `cporderid` = '").append((this.cporderid == null) ? null : this.cporderid.replace("'", "''")).append("',");
/* 386 */     sBuilder.append(" `adfromOrderid` = '").append((this.adfromOrderid == null) ? null : this.adfromOrderid.replace("'", "''")).append("',");
/* 387 */     sBuilder.append(" `serverID` = '").append((this.serverID == null) ? null : this.serverID.replace("'", "''")).append("',");
/* 388 */     sBuilder.append(" `carrier` = '").append((this.carrier == null) ? null : this.carrier.replace("'", "''")).append("',");
/* 389 */     sBuilder.append(" `platform` = '").append((this.platform == null) ? null : this.platform.replace("'", "''")).append("',");
/* 390 */     sBuilder.append(" `adfrom` = '").append((this.adfrom == null) ? null : this.adfrom.replace("'", "''")).append("',");
/* 391 */     sBuilder.append(" `adfrom2` = '").append((this.adfrom2 == null) ? null : this.adfrom2.replace("'", "''")).append("',");
/* 392 */     sBuilder.append(" `gameid` = '").append((this.gameid == null) ? null : this.gameid.replace("'", "''")).append("',");
/* 393 */     sBuilder.append(" `appID` = '").append((this.appID == null) ? null : this.appID.replace("'", "''")).append("',");
/* 394 */     sBuilder.append(" `status` = '").append((this.status == null) ? null : this.status.replace("'", "''")).append("',");
/* 395 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 396 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 400 */     String sql = "CREATE TABLE IF NOT EXISTS `rechargeOrder` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家角色id',`open_id` varchar(500) NOT NULL DEFAULT '' COMMENT '平台服务器上生成的openid',`productid` varchar(500) NOT NULL DEFAULT '' COMMENT '商品id',`quantity` int(11) NOT NULL DEFAULT '0' COMMENT '商品id',`orderTime` int(11) NOT NULL DEFAULT '0' COMMENT '到游戏服务器上时间',`deliverTime` int(11) NOT NULL DEFAULT '0' COMMENT '到游戏服务器上时间',`cporderid` varchar(100) NOT NULL DEFAULT '' COMMENT 'cp定单号,php正式的，唯一',`adfromOrderid` varchar(500) NOT NULL DEFAULT '' COMMENT '渠道支付定单号',`serverID` varchar(500) NOT NULL DEFAULT '' COMMENT '服务器ID',`carrier` varchar(500) NOT NULL DEFAULT '' COMMENT '运营商',`platform` varchar(500) NOT NULL DEFAULT '' COMMENT '平台ios，andriod，越狱',`adfrom` varchar(500) NOT NULL DEFAULT '' COMMENT 'pp，360，qq 主来源1',`adfrom2` varchar(500) NOT NULL DEFAULT '' COMMENT '二级来源',`gameid` varchar(500) NOT NULL DEFAULT '' COMMENT '梦加内部游戏id',`appID` varchar(500) NOT NULL DEFAULT '' COMMENT 'appID',`status` varchar(500) NOT NULL DEFAULT '' COMMENT '订单状态-已付款，已领取，已取消',KEY `cporderid` (`cporderid`),PRIMARY KEY (`id`)) COMMENT='玩家地宫记录'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 420 */       ServerConfig.getInitialID() + 1L);
/* 421 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/RechargeOrderBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */