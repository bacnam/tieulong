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
/*     */ public class GuildBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "int(11)", fieldname = "level", comment = "帮会等级")
/*     */   private int level;
/*     */   @DataBaseField(type = "int(11)", fieldname = "exp", comment = "帮会经验")
/*     */   private int exp;
/*     */   @DataBaseField(type = "int(11)", fieldname = "historyExp", comment = "历史帮会经验")
/*     */   private int historyExp;
/*     */   @DataBaseField(type = "varchar(50)", fieldname = "name", comment = "名称")
/*     */   private String name;
/*     */   @DataBaseField(type = "int(11)", fieldname = "icon", comment = "头像")
/*     */   private int icon;
/*     */   @DataBaseField(type = "int(11)", fieldname = "border", comment = "边框")
/*     */   private int border;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "notice", comment = "公告")
/*     */   private String notice;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "manifesto", comment = "宣言")
/*     */   private String manifesto;
/*     */   @DataBaseField(type = "int(11)", fieldname = "joinState", comment = "加入状态")
/*     */   private int joinState;
/*     */   @DataBaseField(type = "int(11)", fieldname = "createTime", comment = "创建时间")
/*     */   private int createTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "lastLoginTime", comment = "公会玩家最近一次登录时间")
/*     */   private int lastLoginTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "levelLimit", comment = "加入等级限制")
/*     */   private int levelLimit;
/*     */   @DataBaseField(type = "int(11)", fieldname = "maxFightPower", comment = "历史最大战斗力")
/*     */   private int maxFightPower;
/*     */   @DataBaseField(type = "int(11)", fieldname = "donate", comment = "历史最大贡献度")
/*     */   private int donate;
/*     */   @DataBaseField(type = "int(11)", fieldname = "guildbossLevel", comment = "最大通关帮派副本等级")
/*     */   private int guildbossLevel;
/*     */   @DataBaseField(type = "int(11)", fieldname = "guildbossOpenNum", comment = "每日帮派副本开启数")
/*     */   private int guildbossOpenNum;
/*     */   @DataBaseField(type = "int(11)", fieldname = "lnlevel", comment = "龙女等级")
/*     */   private int lnlevel;
/*     */   @DataBaseField(type = "int(11)", fieldname = "lnexp", comment = "龙女经验")
/*     */   private int lnexp;
/*     */   @DataBaseField(type = "int(11)", fieldname = "lnwarLevel", comment = "龙女战力档次")
/*     */   private int lnwarLevel;
/*     */   
/*     */   public GuildBO() {
/*  56 */     this.id = 0L;
/*  57 */     this.level = 0;
/*  58 */     this.exp = 0;
/*  59 */     this.historyExp = 0;
/*  60 */     this.name = "";
/*  61 */     this.icon = 0;
/*  62 */     this.border = 0;
/*  63 */     this.notice = "";
/*  64 */     this.manifesto = "";
/*  65 */     this.joinState = 0;
/*  66 */     this.createTime = 0;
/*  67 */     this.lastLoginTime = 0;
/*  68 */     this.levelLimit = 0;
/*  69 */     this.maxFightPower = 0;
/*  70 */     this.donate = 0;
/*  71 */     this.guildbossLevel = 0;
/*  72 */     this.guildbossOpenNum = 0;
/*  73 */     this.lnlevel = 0;
/*  74 */     this.lnexp = 0;
/*  75 */     this.lnwarLevel = 0;
/*     */   }
/*     */   
/*     */   public GuildBO(ResultSet rs) throws Exception {
/*  79 */     this.id = rs.getLong(1);
/*  80 */     this.level = rs.getInt(2);
/*  81 */     this.exp = rs.getInt(3);
/*  82 */     this.historyExp = rs.getInt(4);
/*  83 */     this.name = rs.getString(5);
/*  84 */     this.icon = rs.getInt(6);
/*  85 */     this.border = rs.getInt(7);
/*  86 */     this.notice = rs.getString(8);
/*  87 */     this.manifesto = rs.getString(9);
/*  88 */     this.joinState = rs.getInt(10);
/*  89 */     this.createTime = rs.getInt(11);
/*  90 */     this.lastLoginTime = rs.getInt(12);
/*  91 */     this.levelLimit = rs.getInt(13);
/*  92 */     this.maxFightPower = rs.getInt(14);
/*  93 */     this.donate = rs.getInt(15);
/*  94 */     this.guildbossLevel = rs.getInt(16);
/*  95 */     this.guildbossOpenNum = rs.getInt(17);
/*  96 */     this.lnlevel = rs.getInt(18);
/*  97 */     this.lnexp = rs.getInt(19);
/*  98 */     this.lnwarLevel = rs.getInt(20);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<GuildBO> list) throws Exception {
/* 104 */     list.add(new GuildBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/* 109 */     return getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/* 114 */     return "`id`, `level`, `exp`, `historyExp`, `name`, `icon`, `border`, `notice`, `manifesto`, `joinState`, `createTime`, `lastLoginTime`, `levelLimit`, `maxFightPower`, `donate`, `guildbossLevel`, `guildbossOpenNum`, `lnlevel`, `lnexp`, `lnwarLevel`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/* 119 */     return "`guild`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/* 124 */     StringBuilder strBuf = new StringBuilder();
/* 125 */     strBuf.append("'").append(this.id).append("', ");
/* 126 */     strBuf.append("'").append(this.level).append("', ");
/* 127 */     strBuf.append("'").append(this.exp).append("', ");
/* 128 */     strBuf.append("'").append(this.historyExp).append("', ");
/* 129 */     strBuf.append("'").append((this.name == null) ? null : this.name.replace("'", "''")).append("', ");
/* 130 */     strBuf.append("'").append(this.icon).append("', ");
/* 131 */     strBuf.append("'").append(this.border).append("', ");
/* 132 */     strBuf.append("'").append((this.notice == null) ? null : this.notice.replace("'", "''")).append("', ");
/* 133 */     strBuf.append("'").append((this.manifesto == null) ? null : this.manifesto.replace("'", "''")).append("', ");
/* 134 */     strBuf.append("'").append(this.joinState).append("', ");
/* 135 */     strBuf.append("'").append(this.createTime).append("', ");
/* 136 */     strBuf.append("'").append(this.lastLoginTime).append("', ");
/* 137 */     strBuf.append("'").append(this.levelLimit).append("', ");
/* 138 */     strBuf.append("'").append(this.maxFightPower).append("', ");
/* 139 */     strBuf.append("'").append(this.donate).append("', ");
/* 140 */     strBuf.append("'").append(this.guildbossLevel).append("', ");
/* 141 */     strBuf.append("'").append(this.guildbossOpenNum).append("', ");
/* 142 */     strBuf.append("'").append(this.lnlevel).append("', ");
/* 143 */     strBuf.append("'").append(this.lnexp).append("', ");
/* 144 */     strBuf.append("'").append(this.lnwarLevel).append("', ");
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
/*     */   public int getLevel() {
/* 166 */     return this.level;
/*     */   } public void setLevel(int level) {
/* 168 */     if (level == this.level)
/*     */       return; 
/* 170 */     this.level = level;
/*     */   }
/*     */   public void saveLevel(int level) {
/* 173 */     if (level == this.level)
/*     */       return; 
/* 175 */     this.level = level;
/* 176 */     saveField("level", Integer.valueOf(level));
/*     */   }
/*     */   
/*     */   public int getExp() {
/* 180 */     return this.exp;
/*     */   } public void setExp(int exp) {
/* 182 */     if (exp == this.exp)
/*     */       return; 
/* 184 */     this.exp = exp;
/*     */   }
/*     */   public void saveExp(int exp) {
/* 187 */     if (exp == this.exp)
/*     */       return; 
/* 189 */     this.exp = exp;
/* 190 */     saveField("exp", Integer.valueOf(exp));
/*     */   }
/*     */   
/*     */   public int getHistoryExp() {
/* 194 */     return this.historyExp;
/*     */   } public void setHistoryExp(int historyExp) {
/* 196 */     if (historyExp == this.historyExp)
/*     */       return; 
/* 198 */     this.historyExp = historyExp;
/*     */   }
/*     */   public void saveHistoryExp(int historyExp) {
/* 201 */     if (historyExp == this.historyExp)
/*     */       return; 
/* 203 */     this.historyExp = historyExp;
/* 204 */     saveField("historyExp", Integer.valueOf(historyExp));
/*     */   }
/*     */   
/*     */   public String getName() {
/* 208 */     return this.name;
/*     */   } public void setName(String name) {
/* 210 */     if (name.equals(this.name))
/*     */       return; 
/* 212 */     this.name = name;
/*     */   }
/*     */   public void saveName(String name) {
/* 215 */     if (name.equals(this.name))
/*     */       return; 
/* 217 */     this.name = name;
/* 218 */     saveField("name", name);
/*     */   }
/*     */   
/*     */   public int getIcon() {
/* 222 */     return this.icon;
/*     */   } public void setIcon(int icon) {
/* 224 */     if (icon == this.icon)
/*     */       return; 
/* 226 */     this.icon = icon;
/*     */   }
/*     */   public void saveIcon(int icon) {
/* 229 */     if (icon == this.icon)
/*     */       return; 
/* 231 */     this.icon = icon;
/* 232 */     saveField("icon", Integer.valueOf(icon));
/*     */   }
/*     */   
/*     */   public int getBorder() {
/* 236 */     return this.border;
/*     */   } public void setBorder(int border) {
/* 238 */     if (border == this.border)
/*     */       return; 
/* 240 */     this.border = border;
/*     */   }
/*     */   public void saveBorder(int border) {
/* 243 */     if (border == this.border)
/*     */       return; 
/* 245 */     this.border = border;
/* 246 */     saveField("border", Integer.valueOf(border));
/*     */   }
/*     */   
/*     */   public String getNotice() {
/* 250 */     return this.notice;
/*     */   } public void setNotice(String notice) {
/* 252 */     if (notice.equals(this.notice))
/*     */       return; 
/* 254 */     this.notice = notice;
/*     */   }
/*     */   public void saveNotice(String notice) {
/* 257 */     if (notice.equals(this.notice))
/*     */       return; 
/* 259 */     this.notice = notice;
/* 260 */     saveField("notice", notice);
/*     */   }
/*     */   
/*     */   public String getManifesto() {
/* 264 */     return this.manifesto;
/*     */   } public void setManifesto(String manifesto) {
/* 266 */     if (manifesto.equals(this.manifesto))
/*     */       return; 
/* 268 */     this.manifesto = manifesto;
/*     */   }
/*     */   public void saveManifesto(String manifesto) {
/* 271 */     if (manifesto.equals(this.manifesto))
/*     */       return; 
/* 273 */     this.manifesto = manifesto;
/* 274 */     saveField("manifesto", manifesto);
/*     */   }
/*     */   
/*     */   public int getJoinState() {
/* 278 */     return this.joinState;
/*     */   } public void setJoinState(int joinState) {
/* 280 */     if (joinState == this.joinState)
/*     */       return; 
/* 282 */     this.joinState = joinState;
/*     */   }
/*     */   public void saveJoinState(int joinState) {
/* 285 */     if (joinState == this.joinState)
/*     */       return; 
/* 287 */     this.joinState = joinState;
/* 288 */     saveField("joinState", Integer.valueOf(joinState));
/*     */   }
/*     */   
/*     */   public int getCreateTime() {
/* 292 */     return this.createTime;
/*     */   } public void setCreateTime(int createTime) {
/* 294 */     if (createTime == this.createTime)
/*     */       return; 
/* 296 */     this.createTime = createTime;
/*     */   }
/*     */   public void saveCreateTime(int createTime) {
/* 299 */     if (createTime == this.createTime)
/*     */       return; 
/* 301 */     this.createTime = createTime;
/* 302 */     saveField("createTime", Integer.valueOf(createTime));
/*     */   }
/*     */   
/*     */   public int getLastLoginTime() {
/* 306 */     return this.lastLoginTime;
/*     */   } public void setLastLoginTime(int lastLoginTime) {
/* 308 */     if (lastLoginTime == this.lastLoginTime)
/*     */       return; 
/* 310 */     this.lastLoginTime = lastLoginTime;
/*     */   }
/*     */   public void saveLastLoginTime(int lastLoginTime) {
/* 313 */     if (lastLoginTime == this.lastLoginTime)
/*     */       return; 
/* 315 */     this.lastLoginTime = lastLoginTime;
/* 316 */     saveField("lastLoginTime", Integer.valueOf(lastLoginTime));
/*     */   }
/*     */   
/*     */   public int getLevelLimit() {
/* 320 */     return this.levelLimit;
/*     */   } public void setLevelLimit(int levelLimit) {
/* 322 */     if (levelLimit == this.levelLimit)
/*     */       return; 
/* 324 */     this.levelLimit = levelLimit;
/*     */   }
/*     */   public void saveLevelLimit(int levelLimit) {
/* 327 */     if (levelLimit == this.levelLimit)
/*     */       return; 
/* 329 */     this.levelLimit = levelLimit;
/* 330 */     saveField("levelLimit", Integer.valueOf(levelLimit));
/*     */   }
/*     */   
/*     */   public int getMaxFightPower() {
/* 334 */     return this.maxFightPower;
/*     */   } public void setMaxFightPower(int maxFightPower) {
/* 336 */     if (maxFightPower == this.maxFightPower)
/*     */       return; 
/* 338 */     this.maxFightPower = maxFightPower;
/*     */   }
/*     */   public void saveMaxFightPower(int maxFightPower) {
/* 341 */     if (maxFightPower == this.maxFightPower)
/*     */       return; 
/* 343 */     this.maxFightPower = maxFightPower;
/* 344 */     saveField("maxFightPower", Integer.valueOf(maxFightPower));
/*     */   }
/*     */   
/*     */   public int getDonate() {
/* 348 */     return this.donate;
/*     */   } public void setDonate(int donate) {
/* 350 */     if (donate == this.donate)
/*     */       return; 
/* 352 */     this.donate = donate;
/*     */   }
/*     */   public void saveDonate(int donate) {
/* 355 */     if (donate == this.donate)
/*     */       return; 
/* 357 */     this.donate = donate;
/* 358 */     saveField("donate", Integer.valueOf(donate));
/*     */   }
/*     */   
/*     */   public int getGuildbossLevel() {
/* 362 */     return this.guildbossLevel;
/*     */   } public void setGuildbossLevel(int guildbossLevel) {
/* 364 */     if (guildbossLevel == this.guildbossLevel)
/*     */       return; 
/* 366 */     this.guildbossLevel = guildbossLevel;
/*     */   }
/*     */   public void saveGuildbossLevel(int guildbossLevel) {
/* 369 */     if (guildbossLevel == this.guildbossLevel)
/*     */       return; 
/* 371 */     this.guildbossLevel = guildbossLevel;
/* 372 */     saveField("guildbossLevel", Integer.valueOf(guildbossLevel));
/*     */   }
/*     */   
/*     */   public int getGuildbossOpenNum() {
/* 376 */     return this.guildbossOpenNum;
/*     */   } public void setGuildbossOpenNum(int guildbossOpenNum) {
/* 378 */     if (guildbossOpenNum == this.guildbossOpenNum)
/*     */       return; 
/* 380 */     this.guildbossOpenNum = guildbossOpenNum;
/*     */   }
/*     */   public void saveGuildbossOpenNum(int guildbossOpenNum) {
/* 383 */     if (guildbossOpenNum == this.guildbossOpenNum)
/*     */       return; 
/* 385 */     this.guildbossOpenNum = guildbossOpenNum;
/* 386 */     saveField("guildbossOpenNum", Integer.valueOf(guildbossOpenNum));
/*     */   }
/*     */   
/*     */   public int getLnlevel() {
/* 390 */     return this.lnlevel;
/*     */   } public void setLnlevel(int lnlevel) {
/* 392 */     if (lnlevel == this.lnlevel)
/*     */       return; 
/* 394 */     this.lnlevel = lnlevel;
/*     */   }
/*     */   public void saveLnlevel(int lnlevel) {
/* 397 */     if (lnlevel == this.lnlevel)
/*     */       return; 
/* 399 */     this.lnlevel = lnlevel;
/* 400 */     saveField("lnlevel", Integer.valueOf(lnlevel));
/*     */   }
/*     */   
/*     */   public int getLnexp() {
/* 404 */     return this.lnexp;
/*     */   } public void setLnexp(int lnexp) {
/* 406 */     if (lnexp == this.lnexp)
/*     */       return; 
/* 408 */     this.lnexp = lnexp;
/*     */   }
/*     */   public void saveLnexp(int lnexp) {
/* 411 */     if (lnexp == this.lnexp)
/*     */       return; 
/* 413 */     this.lnexp = lnexp;
/* 414 */     saveField("lnexp", Integer.valueOf(lnexp));
/*     */   }
/*     */   
/*     */   public int getLnwarLevel() {
/* 418 */     return this.lnwarLevel;
/*     */   } public void setLnwarLevel(int lnwarLevel) {
/* 420 */     if (lnwarLevel == this.lnwarLevel)
/*     */       return; 
/* 422 */     this.lnwarLevel = lnwarLevel;
/*     */   }
/*     */   public void saveLnwarLevel(int lnwarLevel) {
/* 425 */     if (lnwarLevel == this.lnwarLevel)
/*     */       return; 
/* 427 */     this.lnwarLevel = lnwarLevel;
/* 428 */     saveField("lnwarLevel", Integer.valueOf(lnwarLevel));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 435 */     StringBuilder sBuilder = new StringBuilder();
/* 436 */     sBuilder.append(" `level` = '").append(this.level).append("',");
/* 437 */     sBuilder.append(" `exp` = '").append(this.exp).append("',");
/* 438 */     sBuilder.append(" `historyExp` = '").append(this.historyExp).append("',");
/* 439 */     sBuilder.append(" `name` = '").append((this.name == null) ? null : this.name.replace("'", "''")).append("',");
/* 440 */     sBuilder.append(" `icon` = '").append(this.icon).append("',");
/* 441 */     sBuilder.append(" `border` = '").append(this.border).append("',");
/* 442 */     sBuilder.append(" `notice` = '").append((this.notice == null) ? null : this.notice.replace("'", "''")).append("',");
/* 443 */     sBuilder.append(" `manifesto` = '").append((this.manifesto == null) ? null : this.manifesto.replace("'", "''")).append("',");
/* 444 */     sBuilder.append(" `joinState` = '").append(this.joinState).append("',");
/* 445 */     sBuilder.append(" `createTime` = '").append(this.createTime).append("',");
/* 446 */     sBuilder.append(" `lastLoginTime` = '").append(this.lastLoginTime).append("',");
/* 447 */     sBuilder.append(" `levelLimit` = '").append(this.levelLimit).append("',");
/* 448 */     sBuilder.append(" `maxFightPower` = '").append(this.maxFightPower).append("',");
/* 449 */     sBuilder.append(" `donate` = '").append(this.donate).append("',");
/* 450 */     sBuilder.append(" `guildbossLevel` = '").append(this.guildbossLevel).append("',");
/* 451 */     sBuilder.append(" `guildbossOpenNum` = '").append(this.guildbossOpenNum).append("',");
/* 452 */     sBuilder.append(" `lnlevel` = '").append(this.lnlevel).append("',");
/* 453 */     sBuilder.append(" `lnexp` = '").append(this.lnexp).append("',");
/* 454 */     sBuilder.append(" `lnwarLevel` = '").append(this.lnwarLevel).append("',");
/* 455 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 456 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 460 */     String sql = "CREATE TABLE IF NOT EXISTS `guild` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`level` int(11) NOT NULL DEFAULT '0' COMMENT '帮会等级',`exp` int(11) NOT NULL DEFAULT '0' COMMENT '帮会经验',`historyExp` int(11) NOT NULL DEFAULT '0' COMMENT '历史帮会经验',`name` varchar(50) NOT NULL DEFAULT '' COMMENT '名称',`icon` int(11) NOT NULL DEFAULT '0' COMMENT '头像',`border` int(11) NOT NULL DEFAULT '0' COMMENT '边框',`notice` varchar(500) NOT NULL DEFAULT '' COMMENT '公告',`manifesto` varchar(500) NOT NULL DEFAULT '' COMMENT '宣言',`joinState` int(11) NOT NULL DEFAULT '0' COMMENT '加入状态',`createTime` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',`lastLoginTime` int(11) NOT NULL DEFAULT '0' COMMENT '公会玩家最近一次登录时间',`levelLimit` int(11) NOT NULL DEFAULT '0' COMMENT '加入等级限制',`maxFightPower` int(11) NOT NULL DEFAULT '0' COMMENT '历史最大战斗力',`donate` int(11) NOT NULL DEFAULT '0' COMMENT '历史最大贡献度',`guildbossLevel` int(11) NOT NULL DEFAULT '0' COMMENT '最大通关帮派副本等级',`guildbossOpenNum` int(11) NOT NULL DEFAULT '0' COMMENT '每日帮派副本开启数',`lnlevel` int(11) NOT NULL DEFAULT '0' COMMENT '龙女等级',`lnexp` int(11) NOT NULL DEFAULT '0' COMMENT '龙女经验',`lnwarLevel` int(11) NOT NULL DEFAULT '0' COMMENT '龙女战力档次',UNIQUE INDEX `name` (`name`),PRIMARY KEY (`id`)) COMMENT='公会信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/*     */       
/* 483 */       ServerConfig.getInitialID() + 1L);
/* 484 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/GuildBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */