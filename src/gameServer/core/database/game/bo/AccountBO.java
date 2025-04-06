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
/*     */ public class AccountBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "varchar(50)", fieldname = "open_id", comment = "平台用户openid")
/*     */   private String open_id;
/*     */   @DataBaseField(type = "varchar(128)", fieldname = "username", comment = "账号名")
/*     */   private String username;
/*     */   @DataBaseField(type = "varchar(32)", fieldname = "ip", comment = "当前登录IP")
/*     */   private String ip;
/*     */   @DataBaseField(type = "varchar(50)", fieldname = "mac", comment = "当前登录Mac地址")
/*     */   private String mac;
/*     */   @DataBaseField(type = "int(11)", fieldname = "loginTime", comment = "当前登录时间")
/*     */   private int loginTime;
/*     */   @DataBaseField(type = "varchar(32)", fieldname = "lastIp", comment = "上次登录IP")
/*     */   private String lastIp;
/*     */   @DataBaseField(type = "varchar(50)", fieldname = "lastMac", comment = "上次登录Mac")
/*     */   private String lastMac;
/*     */   @DataBaseField(type = "int(11)", fieldname = "lastLoginTime", comment = "上次登录日期")
/*     */   private int lastLoginTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "banExpiredTime", comment = "封号过期时间")
/*     */   private int banExpiredTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "lastServerId", comment = "最后登录的服务器ID")
/*     */   private int lastServerId;
/*     */   @DataBaseField(type = "varchar(30)", fieldname = "lastClientVersion", comment = "最后登录的客户端版本")
/*     */   private String lastClientVersion;
/*     */   @DataBaseField(type = "varchar(50)", fieldname = "adFrom", comment = "玩家渠道")
/*     */   private String adFrom;
/*     */   @DataBaseField(type = "varchar(50)", fieldname = "adFrom2", comment = "玩家二级渠道")
/*     */   private String adFrom2;
/*     */   @DataBaseField(type = "varchar(32)", fieldname = "regIp", comment = "注册IP")
/*     */   private String regIp;
/*     */   @DataBaseField(type = "varchar(50)", fieldname = "regMac", comment = "注册Mac地址")
/*     */   private String regMac;
/*     */   @DataBaseField(type = "int(11)", fieldname = "regTime", comment = "注册时间")
/*     */   private int regTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "createTime", comment = "创建时间")
/*     */   private int createTime;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "adid", comment = "设备ID")
/*     */   private String adid;
/*     */   @DataBaseField(type = "varchar(50)", fieldname = "nation", comment = "国家")
/*     */   private String nation;
/*     */   
/*     */   public AccountBO() {
/*  56 */     this.id = 0L;
/*  57 */     this.open_id = "";
/*  58 */     this.username = "";
/*  59 */     this.ip = "";
/*  60 */     this.mac = "";
/*  61 */     this.loginTime = 0;
/*  62 */     this.lastIp = "";
/*  63 */     this.lastMac = "";
/*  64 */     this.lastLoginTime = 0;
/*  65 */     this.banExpiredTime = 0;
/*  66 */     this.lastServerId = 0;
/*  67 */     this.lastClientVersion = "";
/*  68 */     this.adFrom = "";
/*  69 */     this.adFrom2 = "";
/*  70 */     this.regIp = "";
/*  71 */     this.regMac = "";
/*  72 */     this.regTime = 0;
/*  73 */     this.createTime = 0;
/*  74 */     this.adid = "";
/*  75 */     this.nation = "";
/*     */   }
/*     */   
/*     */   public AccountBO(ResultSet rs) throws Exception {
/*  79 */     this.id = rs.getLong(1);
/*  80 */     this.open_id = rs.getString(2);
/*  81 */     this.username = rs.getString(3);
/*  82 */     this.ip = rs.getString(4);
/*  83 */     this.mac = rs.getString(5);
/*  84 */     this.loginTime = rs.getInt(6);
/*  85 */     this.lastIp = rs.getString(7);
/*  86 */     this.lastMac = rs.getString(8);
/*  87 */     this.lastLoginTime = rs.getInt(9);
/*  88 */     this.banExpiredTime = rs.getInt(10);
/*  89 */     this.lastServerId = rs.getInt(11);
/*  90 */     this.lastClientVersion = rs.getString(12);
/*  91 */     this.adFrom = rs.getString(13);
/*  92 */     this.adFrom2 = rs.getString(14);
/*  93 */     this.regIp = rs.getString(15);
/*  94 */     this.regMac = rs.getString(16);
/*  95 */     this.regTime = rs.getInt(17);
/*  96 */     this.createTime = rs.getInt(18);
/*  97 */     this.adid = rs.getString(19);
/*  98 */     this.nation = rs.getString(20);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<AccountBO> list) throws Exception {
/* 104 */     list.add(new AccountBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/* 109 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/* 114 */     return "`id`, `open_id`, `username`, `ip`, `mac`, `loginTime`, `lastIp`, `lastMac`, `lastLoginTime`, `banExpiredTime`, `lastServerId`, `lastClientVersion`, `adFrom`, `adFrom2`, `regIp`, `regMac`, `regTime`, `createTime`, `adid`, `nation`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/* 119 */     return "`account`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/* 124 */     StringBuilder strBuf = new StringBuilder();
/* 125 */     strBuf.append("'").append(this.id).append("', ");
/* 126 */     strBuf.append("'").append((this.open_id == null) ? null : this.open_id.replace("'", "''")).append("', ");
/* 127 */     strBuf.append("'").append((this.username == null) ? null : this.username.replace("'", "''")).append("', ");
/* 128 */     strBuf.append("'").append((this.ip == null) ? null : this.ip.replace("'", "''")).append("', ");
/* 129 */     strBuf.append("'").append((this.mac == null) ? null : this.mac.replace("'", "''")).append("', ");
/* 130 */     strBuf.append("'").append(this.loginTime).append("', ");
/* 131 */     strBuf.append("'").append((this.lastIp == null) ? null : this.lastIp.replace("'", "''")).append("', ");
/* 132 */     strBuf.append("'").append((this.lastMac == null) ? null : this.lastMac.replace("'", "''")).append("', ");
/* 133 */     strBuf.append("'").append(this.lastLoginTime).append("', ");
/* 134 */     strBuf.append("'").append(this.banExpiredTime).append("', ");
/* 135 */     strBuf.append("'").append(this.lastServerId).append("', ");
/* 136 */     strBuf.append("'").append((this.lastClientVersion == null) ? null : this.lastClientVersion.replace("'", "''")).append("', ");
/* 137 */     strBuf.append("'").append((this.adFrom == null) ? null : this.adFrom.replace("'", "''")).append("', ");
/* 138 */     strBuf.append("'").append((this.adFrom2 == null) ? null : this.adFrom2.replace("'", "''")).append("', ");
/* 139 */     strBuf.append("'").append((this.regIp == null) ? null : this.regIp.replace("'", "''")).append("', ");
/* 140 */     strBuf.append("'").append((this.regMac == null) ? null : this.regMac.replace("'", "''")).append("', ");
/* 141 */     strBuf.append("'").append(this.regTime).append("', ");
/* 142 */     strBuf.append("'").append(this.createTime).append("', ");
/* 143 */     strBuf.append("'").append((this.adid == null) ? null : this.adid.replace("'", "''")).append("', ");
/* 144 */     strBuf.append("'").append((this.nation == null) ? null : this.nation.replace("'", "''")).append("', ");
/* 145 */     strBuf.deleteCharAt(strBuf.length() - 2);
/* 146 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/* 151 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/* 152 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/* 157 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 162 */     return this.id;
/*     */   }
/*     */   
/*     */   public String getOpenId() {
/* 166 */     return this.open_id;
/*     */   } public void setOpenId(String open_id) {
/* 168 */     if (open_id.equals(this.open_id))
/*     */       return; 
/* 170 */     this.open_id = open_id;
/*     */   }
/*     */   public void saveOpenId(String open_id) {
/* 173 */     if (open_id.equals(this.open_id))
/*     */       return; 
/* 175 */     this.open_id = open_id;
/* 176 */     saveField("open_id", open_id);
/*     */   }
/*     */   
/*     */   public String getUsername() {
/* 180 */     return this.username;
/*     */   } public void setUsername(String username) {
/* 182 */     if (username.equals(this.username))
/*     */       return; 
/* 184 */     this.username = username;
/*     */   }
/*     */   public void saveUsername(String username) {
/* 187 */     if (username.equals(this.username))
/*     */       return; 
/* 189 */     this.username = username;
/* 190 */     saveField("username", username);
/*     */   }
/*     */   
/*     */   public String getIp() {
/* 194 */     return this.ip;
/*     */   } public void setIp(String ip) {
/* 196 */     if (ip.equals(this.ip))
/*     */       return; 
/* 198 */     this.ip = ip;
/*     */   }
/*     */   public void saveIp(String ip) {
/* 201 */     if (ip.equals(this.ip))
/*     */       return; 
/* 203 */     this.ip = ip;
/* 204 */     saveField("ip", ip);
/*     */   }
/*     */   
/*     */   public String getMac() {
/* 208 */     return this.mac;
/*     */   } public void setMac(String mac) {
/* 210 */     if (mac.equals(this.mac))
/*     */       return; 
/* 212 */     this.mac = mac;
/*     */   }
/*     */   public void saveMac(String mac) {
/* 215 */     if (mac.equals(this.mac))
/*     */       return; 
/* 217 */     this.mac = mac;
/* 218 */     saveField("mac", mac);
/*     */   }
/*     */   
/*     */   public int getLoginTime() {
/* 222 */     return this.loginTime;
/*     */   } public void setLoginTime(int loginTime) {
/* 224 */     if (loginTime == this.loginTime)
/*     */       return; 
/* 226 */     this.loginTime = loginTime;
/*     */   }
/*     */   public void saveLoginTime(int loginTime) {
/* 229 */     if (loginTime == this.loginTime)
/*     */       return; 
/* 231 */     this.loginTime = loginTime;
/* 232 */     saveField("loginTime", Integer.valueOf(loginTime));
/*     */   }
/*     */   
/*     */   public String getLastIp() {
/* 236 */     return this.lastIp;
/*     */   } public void setLastIp(String lastIp) {
/* 238 */     if (lastIp.equals(this.lastIp))
/*     */       return; 
/* 240 */     this.lastIp = lastIp;
/*     */   }
/*     */   public void saveLastIp(String lastIp) {
/* 243 */     if (lastIp.equals(this.lastIp))
/*     */       return; 
/* 245 */     this.lastIp = lastIp;
/* 246 */     saveField("lastIp", lastIp);
/*     */   }
/*     */   
/*     */   public String getLastMac() {
/* 250 */     return this.lastMac;
/*     */   } public void setLastMac(String lastMac) {
/* 252 */     if (lastMac.equals(this.lastMac))
/*     */       return; 
/* 254 */     this.lastMac = lastMac;
/*     */   }
/*     */   public void saveLastMac(String lastMac) {
/* 257 */     if (lastMac.equals(this.lastMac))
/*     */       return; 
/* 259 */     this.lastMac = lastMac;
/* 260 */     saveField("lastMac", lastMac);
/*     */   }
/*     */   
/*     */   public int getLastLoginTime() {
/* 264 */     return this.lastLoginTime;
/*     */   } public void setLastLoginTime(int lastLoginTime) {
/* 266 */     if (lastLoginTime == this.lastLoginTime)
/*     */       return; 
/* 268 */     this.lastLoginTime = lastLoginTime;
/*     */   }
/*     */   public void saveLastLoginTime(int lastLoginTime) {
/* 271 */     if (lastLoginTime == this.lastLoginTime)
/*     */       return; 
/* 273 */     this.lastLoginTime = lastLoginTime;
/* 274 */     saveField("lastLoginTime", Integer.valueOf(lastLoginTime));
/*     */   }
/*     */   
/*     */   public int getBanExpiredTime() {
/* 278 */     return this.banExpiredTime;
/*     */   } public void setBanExpiredTime(int banExpiredTime) {
/* 280 */     if (banExpiredTime == this.banExpiredTime)
/*     */       return; 
/* 282 */     this.banExpiredTime = banExpiredTime;
/*     */   }
/*     */   public void saveBanExpiredTime(int banExpiredTime) {
/* 285 */     if (banExpiredTime == this.banExpiredTime)
/*     */       return; 
/* 287 */     this.banExpiredTime = banExpiredTime;
/* 288 */     saveField("banExpiredTime", Integer.valueOf(banExpiredTime));
/*     */   }
/*     */   
/*     */   public int getLastServerId() {
/* 292 */     return this.lastServerId;
/*     */   } public void setLastServerId(int lastServerId) {
/* 294 */     if (lastServerId == this.lastServerId)
/*     */       return; 
/* 296 */     this.lastServerId = lastServerId;
/*     */   }
/*     */   public void saveLastServerId(int lastServerId) {
/* 299 */     if (lastServerId == this.lastServerId)
/*     */       return; 
/* 301 */     this.lastServerId = lastServerId;
/* 302 */     saveField("lastServerId", Integer.valueOf(lastServerId));
/*     */   }
/*     */   
/*     */   public String getLastClientVersion() {
/* 306 */     return this.lastClientVersion;
/*     */   } public void setLastClientVersion(String lastClientVersion) {
/* 308 */     if (lastClientVersion.equals(this.lastClientVersion))
/*     */       return; 
/* 310 */     this.lastClientVersion = lastClientVersion;
/*     */   }
/*     */   public void saveLastClientVersion(String lastClientVersion) {
/* 313 */     if (lastClientVersion.equals(this.lastClientVersion))
/*     */       return; 
/* 315 */     this.lastClientVersion = lastClientVersion;
/* 316 */     saveField("lastClientVersion", lastClientVersion);
/*     */   }
/*     */   
/*     */   public String getAdFrom() {
/* 320 */     return this.adFrom;
/*     */   } public void setAdFrom(String adFrom) {
/* 322 */     if (adFrom.equals(this.adFrom))
/*     */       return; 
/* 324 */     this.adFrom = adFrom;
/*     */   }
/*     */   public void saveAdFrom(String adFrom) {
/* 327 */     if (adFrom.equals(this.adFrom))
/*     */       return; 
/* 329 */     this.adFrom = adFrom;
/* 330 */     saveField("adFrom", adFrom);
/*     */   }
/*     */   
/*     */   public String getAdFrom2() {
/* 334 */     return this.adFrom2;
/*     */   } public void setAdFrom2(String adFrom2) {
/* 336 */     if (adFrom2.equals(this.adFrom2))
/*     */       return; 
/* 338 */     this.adFrom2 = adFrom2;
/*     */   }
/*     */   public void saveAdFrom2(String adFrom2) {
/* 341 */     if (adFrom2.equals(this.adFrom2))
/*     */       return; 
/* 343 */     this.adFrom2 = adFrom2;
/* 344 */     saveField("adFrom2", adFrom2);
/*     */   }
/*     */   
/*     */   public String getRegIp() {
/* 348 */     return this.regIp;
/*     */   } public void setRegIp(String regIp) {
/* 350 */     if (regIp.equals(this.regIp))
/*     */       return; 
/* 352 */     this.regIp = regIp;
/*     */   }
/*     */   public void saveRegIp(String regIp) {
/* 355 */     if (regIp.equals(this.regIp))
/*     */       return; 
/* 357 */     this.regIp = regIp;
/* 358 */     saveField("regIp", regIp);
/*     */   }
/*     */   
/*     */   public String getRegMac() {
/* 362 */     return this.regMac;
/*     */   } public void setRegMac(String regMac) {
/* 364 */     if (regMac.equals(this.regMac))
/*     */       return; 
/* 366 */     this.regMac = regMac;
/*     */   }
/*     */   public void saveRegMac(String regMac) {
/* 369 */     if (regMac.equals(this.regMac))
/*     */       return; 
/* 371 */     this.regMac = regMac;
/* 372 */     saveField("regMac", regMac);
/*     */   }
/*     */   
/*     */   public int getRegTime() {
/* 376 */     return this.regTime;
/*     */   } public void setRegTime(int regTime) {
/* 378 */     if (regTime == this.regTime)
/*     */       return; 
/* 380 */     this.regTime = regTime;
/*     */   }
/*     */   public void saveRegTime(int regTime) {
/* 383 */     if (regTime == this.regTime)
/*     */       return; 
/* 385 */     this.regTime = regTime;
/* 386 */     saveField("regTime", Integer.valueOf(regTime));
/*     */   }
/*     */   
/*     */   public int getCreateTime() {
/* 390 */     return this.createTime;
/*     */   } public void setCreateTime(int createTime) {
/* 392 */     if (createTime == this.createTime)
/*     */       return; 
/* 394 */     this.createTime = createTime;
/*     */   }
/*     */   public void saveCreateTime(int createTime) {
/* 397 */     if (createTime == this.createTime)
/*     */       return; 
/* 399 */     this.createTime = createTime;
/* 400 */     saveField("createTime", Integer.valueOf(createTime));
/*     */   }
/*     */   
/*     */   public String getAdid() {
/* 404 */     return this.adid;
/*     */   } public void setAdid(String adid) {
/* 406 */     if (adid.equals(this.adid))
/*     */       return; 
/* 408 */     this.adid = adid;
/*     */   }
/*     */   public void saveAdid(String adid) {
/* 411 */     if (adid.equals(this.adid))
/*     */       return; 
/* 413 */     this.adid = adid;
/* 414 */     saveField("adid", adid);
/*     */   }
/*     */   
/*     */   public String getNation() {
/* 418 */     return this.nation;
/*     */   } public void setNation(String nation) {
/* 420 */     if (nation.equals(this.nation))
/*     */       return; 
/* 422 */     this.nation = nation;
/*     */   }
/*     */   public void saveNation(String nation) {
/* 425 */     if (nation.equals(this.nation))
/*     */       return; 
/* 427 */     this.nation = nation;
/* 428 */     saveField("nation", nation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 435 */     StringBuilder sBuilder = new StringBuilder();
/* 436 */     sBuilder.append(" `open_id` = '").append((this.open_id == null) ? null : this.open_id.replace("'", "''")).append("',");
/* 437 */     sBuilder.append(" `username` = '").append((this.username == null) ? null : this.username.replace("'", "''")).append("',");
/* 438 */     sBuilder.append(" `ip` = '").append((this.ip == null) ? null : this.ip.replace("'", "''")).append("',");
/* 439 */     sBuilder.append(" `mac` = '").append((this.mac == null) ? null : this.mac.replace("'", "''")).append("',");
/* 440 */     sBuilder.append(" `loginTime` = '").append(this.loginTime).append("',");
/* 441 */     sBuilder.append(" `lastIp` = '").append((this.lastIp == null) ? null : this.lastIp.replace("'", "''")).append("',");
/* 442 */     sBuilder.append(" `lastMac` = '").append((this.lastMac == null) ? null : this.lastMac.replace("'", "''")).append("',");
/* 443 */     sBuilder.append(" `lastLoginTime` = '").append(this.lastLoginTime).append("',");
/* 444 */     sBuilder.append(" `banExpiredTime` = '").append(this.banExpiredTime).append("',");
/* 445 */     sBuilder.append(" `lastServerId` = '").append(this.lastServerId).append("',");
/* 446 */     sBuilder.append(" `lastClientVersion` = '").append((this.lastClientVersion == null) ? null : this.lastClientVersion.replace("'", "''")).append("',");
/* 447 */     sBuilder.append(" `adFrom` = '").append((this.adFrom == null) ? null : this.adFrom.replace("'", "''")).append("',");
/* 448 */     sBuilder.append(" `adFrom2` = '").append((this.adFrom2 == null) ? null : this.adFrom2.replace("'", "''")).append("',");
/* 449 */     sBuilder.append(" `regIp` = '").append((this.regIp == null) ? null : this.regIp.replace("'", "''")).append("',");
/* 450 */     sBuilder.append(" `regMac` = '").append((this.regMac == null) ? null : this.regMac.replace("'", "''")).append("',");
/* 451 */     sBuilder.append(" `regTime` = '").append(this.regTime).append("',");
/* 452 */     sBuilder.append(" `createTime` = '").append(this.createTime).append("',");
/* 453 */     sBuilder.append(" `adid` = '").append((this.adid == null) ? null : this.adid.replace("'", "''")).append("',");
/* 454 */     sBuilder.append(" `nation` = '").append((this.nation == null) ? null : this.nation.replace("'", "''")).append("',");
/* 455 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 456 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 460 */     String sql = "CREATE TABLE IF NOT EXISTS `account` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`open_id` varchar(50) NOT NULL DEFAULT '' COMMENT '平台用户openid',`username` varchar(128) NOT NULL DEFAULT '' COMMENT '账号名',`ip` varchar(32) NOT NULL DEFAULT '' COMMENT '当前登录IP',`mac` varchar(50) NOT NULL DEFAULT '' COMMENT '当前登录Mac地址',`loginTime` int(11) NOT NULL DEFAULT '0' COMMENT '当前登录时间',`lastIp` varchar(32) NOT NULL DEFAULT '' COMMENT '上次登录IP',`lastMac` varchar(50) NOT NULL DEFAULT '' COMMENT '上次登录Mac',`lastLoginTime` int(11) NOT NULL DEFAULT '0' COMMENT '上次登录日期',`banExpiredTime` int(11) NOT NULL DEFAULT '0' COMMENT '封号过期时间',`lastServerId` int(11) NOT NULL DEFAULT '0' COMMENT '最后登录的服务器ID',`lastClientVersion` varchar(30) NOT NULL DEFAULT '' COMMENT '最后登录的客户端版本',`adFrom` varchar(50) NOT NULL DEFAULT '' COMMENT '玩家渠道',`adFrom2` varchar(50) NOT NULL DEFAULT '' COMMENT '玩家二级渠道',`regIp` varchar(32) NOT NULL DEFAULT '' COMMENT '注册IP',`regMac` varchar(50) NOT NULL DEFAULT '' COMMENT '注册Mac地址',`regTime` int(11) NOT NULL DEFAULT '0' COMMENT '注册时间',`createTime` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',`adid` varchar(500) NOT NULL DEFAULT '' COMMENT '设备ID',`nation` varchar(50) NOT NULL DEFAULT '' COMMENT '国家',PRIMARY KEY (`id`)) COMMENT='玩家账号信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 482 */       ServerConfig.getInitialID() + 1L);
/* 483 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/AccountBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */