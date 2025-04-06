/*      */ package core.database.game.bo;
/*      */ 
/*      */ import com.zhonglian.server.common.db.BaseBO;
/*      */ import com.zhonglian.server.common.db.annotation.DataBaseField;
/*      */ import core.server.ServerConfig;
/*      */ import java.sql.ResultSet;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ 
/*      */ 
/*      */ public class PlayerBO
/*      */   extends BaseBO
/*      */ {
/*      */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*      */   private long id;
/*      */   @DataBaseField(type = "varchar(50)", fieldname = "open_id", comment = "玩家账号ID")
/*      */   private String open_id;
/*      */   @DataBaseField(type = "int(11)", fieldname = "sid", comment = "服务器id")
/*      */   private int sid;
/*      */   @DataBaseField(type = "varchar(50)", fieldname = "name", comment = "玩家名称/昵称")
/*      */   private String name;
/*      */   @DataBaseField(type = "int(11)", fieldname = "icon", comment = "头像")
/*      */   private int icon;
/*      */   @DataBaseField(type = "int(11)", fieldname = "maxFightPower", comment = "最大战斗力")
/*      */   private int maxFightPower;
/*      */   @DataBaseField(type = "int(11)", fieldname = "gmLevel", comment = "gm权限")
/*      */   private int gmLevel;
/*      */   @DataBaseField(type = "int(11)", fieldname = "cheatTimes", comment = "作弊次数")
/*      */   private int cheatTimes;
/*      */   @DataBaseField(type = "int(11)", fieldname = "banned_ChatExpiredTime", comment = "禁言过期时间")
/*      */   private int banned_ChatExpiredTime;
/*      */   @DataBaseField(type = "int(11)", fieldname = "banned_LoginExpiredTime", comment = "禁登过期时间")
/*      */   private int banned_LoginExpiredTime;
/*      */   @DataBaseField(type = "int(11)", fieldname = "bannedTimes", comment = "封号次数")
/*      */   private int bannedTimes;
/*      */   @DataBaseField(type = "int(11)", fieldname = "lastLogin", comment = "最近一次登陆時間")
/*      */   private int lastLogin;
/*      */   @DataBaseField(type = "int(11)", fieldname = "lastLogout", comment = "最近一次登出時間")
/*      */   private int lastLogout;
/*      */   @DataBaseField(type = "int(11)", fieldname = "online_update", comment = "在线更新时间")
/*      */   private int online_update;
/*      */   @DataBaseField(type = "int(11)", fieldname = "totalRecharge", comment = "累计充值, 统计玩家充值RMB")
/*      */   private int totalRecharge;
/*      */   @DataBaseField(type = "int(11)", fieldname = "vipExp", comment = "vip经验")
/*      */   private int vipExp;
/*      */   @DataBaseField(type = "int(11)", fieldname = "vipLevel", comment = "vip等级")
/*      */   private int vipLevel;
/*      */   @DataBaseField(type = "int(11)", fieldname = "lv", comment = "等级")
/*      */   private int lv;
/*      */   @DataBaseField(type = "int(11)", fieldname = "exp", comment = "队伍经验")
/*      */   private int exp;
/*      */   @DataBaseField(type = "int(11)", fieldname = "crystal", comment = "水晶数量")
/*      */   private int crystal;
/*      */   @DataBaseField(type = "int(11)", fieldname = "gold", comment = "金币")
/*      */   private int gold;
/*      */   @DataBaseField(type = "int(11)", fieldname = "dungeon_lv", comment = "副本等级")
/*      */   private int dungeon_lv;
/*      */   @DataBaseField(type = "int(11)", fieldname = "dungeon_time", comment = "上次打副本时间")
/*      */   private int dungeon_time;
/*      */   @DataBaseField(type = "int(11)", fieldname = "ext_package", comment = "额外背包容量")
/*      */   private int ext_package;
/*      */   @DataBaseField(type = "int(11)", fieldname = "createTime", comment = "账号创建时间,单位秒")
/*      */   private int createTime;
/*      */   @DataBaseField(type = "bigint(20)", fieldname = "GmMailCheckId", comment = "Gm邮件领取编号")
/*      */   private long GmMailCheckId;
/*      */   @DataBaseField(type = "int(11)", fieldname = "strengthen_material", comment = "强化材料")
/*      */   private int strengthen_material;
/*      */   @DataBaseField(type = "int(11)", fieldname = "gem_material", comment = "宝石材料")
/*      */   private int gem_material;
/*      */   @DataBaseField(type = "int(11)", fieldname = "star_material", comment = "升星材料")
/*      */   private int star_material;
/*      */   @DataBaseField(type = "int(11)", fieldname = "mer_material", comment = "经脉材料")
/*      */   private int mer_material;
/*      */   @DataBaseField(type = "int(11)", fieldname = "wing_material", comment = "翅膀材料")
/*      */   private int wing_material;
/*      */   @DataBaseField(type = "int(11)", fieldname = "arena_token", comment = "竞技场代币")
/*      */   private int arena_token;
/*      */   @DataBaseField(type = "int(11)", fieldname = "equip_instance_material", comment = "装备副本代币")
/*      */   private int equip_instance_material;
/*      */   @DataBaseField(type = "int(11)", fieldname = "gem_instance_material", comment = "宝石副本代币")
/*      */   private int gem_instance_material;
/*      */   @DataBaseField(type = "int(11)", fieldname = "meridian_instance_material", comment = "经脉副本代币")
/*      */   private int meridian_instance_material;
/*      */   @DataBaseField(type = "int(11)", fieldname = "red_piece", comment = "红装碎片")
/*      */   private int red_piece;
/*      */   @DataBaseField(type = "int(11)", fieldname = "artifice_material", comment = "炼化石")
/*      */   private int artifice_material;
/*      */   @DataBaseField(type = "int(11)", fieldname = "lottery", comment = "奖券")
/*      */   private int lottery;
/*      */   @DataBaseField(type = "int(11)", fieldname = "warspirit_talent_material", comment = "战灵天赋材料")
/*      */   private int warspirit_talent_material;
/*      */   @DataBaseField(type = "int(11)", fieldname = "warspirit_lv_material", comment = "战灵等级材料")
/*      */   private int warspirit_lv_material;
/*      */   @DataBaseField(type = "int(11)", fieldname = "warspirit_lv", comment = "战灵等级")
/*      */   private int warspirit_lv;
/*      */   @DataBaseField(type = "int(11)", fieldname = "warspirit_exp", comment = "战灵经验")
/*      */   private int warspirit_exp;
/*      */   @DataBaseField(type = "int(11)", fieldname = "warspirit_talent", comment = "战灵天赋")
/*      */   private int warspirit_talent;
/*      */   @DataBaseField(type = "int(11)", fieldname = "dress_material", comment = "时装材料")
/*      */   private int dress_material;
/*      */   @DataBaseField(type = "int(11)", fieldname = "exp_material", comment = "经验材料")
/*      */   private int exp_material;
/*      */   
/*      */   public PlayerBO() {
/*  106 */     this.id = 0L;
/*  107 */     this.open_id = "";
/*  108 */     this.sid = 0;
/*  109 */     this.name = "";
/*  110 */     this.icon = 0;
/*  111 */     this.maxFightPower = 0;
/*  112 */     this.gmLevel = 0;
/*  113 */     this.cheatTimes = 0;
/*  114 */     this.banned_ChatExpiredTime = 0;
/*  115 */     this.banned_LoginExpiredTime = 0;
/*  116 */     this.bannedTimes = 0;
/*  117 */     this.lastLogin = 0;
/*  118 */     this.lastLogout = 0;
/*  119 */     this.online_update = 0;
/*  120 */     this.totalRecharge = 0;
/*  121 */     this.vipExp = 0;
/*  122 */     this.vipLevel = 0;
/*  123 */     this.lv = 0;
/*  124 */     this.exp = 0;
/*  125 */     this.crystal = 0;
/*  126 */     this.gold = 0;
/*  127 */     this.dungeon_lv = 0;
/*  128 */     this.dungeon_time = 0;
/*  129 */     this.ext_package = 0;
/*  130 */     this.createTime = 0;
/*  131 */     this.GmMailCheckId = 0L;
/*  132 */     this.strengthen_material = 0;
/*  133 */     this.gem_material = 0;
/*  134 */     this.star_material = 0;
/*  135 */     this.mer_material = 0;
/*  136 */     this.wing_material = 0;
/*  137 */     this.arena_token = 0;
/*  138 */     this.equip_instance_material = 0;
/*  139 */     this.gem_instance_material = 0;
/*  140 */     this.meridian_instance_material = 0;
/*  141 */     this.red_piece = 0;
/*  142 */     this.artifice_material = 0;
/*  143 */     this.lottery = 0;
/*  144 */     this.warspirit_talent_material = 0;
/*  145 */     this.warspirit_lv_material = 0;
/*  146 */     this.warspirit_lv = 0;
/*  147 */     this.warspirit_exp = 0;
/*  148 */     this.warspirit_talent = 0;
/*  149 */     this.dress_material = 0;
/*  150 */     this.exp_material = 0;
/*      */   }
/*      */   
/*      */   public PlayerBO(ResultSet rs) throws Exception {
/*  154 */     this.id = rs.getLong(1);
/*  155 */     this.open_id = rs.getString(2);
/*  156 */     this.sid = rs.getInt(3);
/*  157 */     this.name = rs.getString(4);
/*  158 */     this.icon = rs.getInt(5);
/*  159 */     this.maxFightPower = rs.getInt(6);
/*  160 */     this.gmLevel = rs.getInt(7);
/*  161 */     this.cheatTimes = rs.getInt(8);
/*  162 */     this.banned_ChatExpiredTime = rs.getInt(9);
/*  163 */     this.banned_LoginExpiredTime = rs.getInt(10);
/*  164 */     this.bannedTimes = rs.getInt(11);
/*  165 */     this.lastLogin = rs.getInt(12);
/*  166 */     this.lastLogout = rs.getInt(13);
/*  167 */     this.online_update = rs.getInt(14);
/*  168 */     this.totalRecharge = rs.getInt(15);
/*  169 */     this.vipExp = rs.getInt(16);
/*  170 */     this.vipLevel = rs.getInt(17);
/*  171 */     this.lv = rs.getInt(18);
/*  172 */     this.exp = rs.getInt(19);
/*  173 */     this.crystal = rs.getInt(20);
/*  174 */     this.gold = rs.getInt(21);
/*  175 */     this.dungeon_lv = rs.getInt(22);
/*  176 */     this.dungeon_time = rs.getInt(23);
/*  177 */     this.ext_package = rs.getInt(24);
/*  178 */     this.createTime = rs.getInt(25);
/*  179 */     this.GmMailCheckId = rs.getLong(26);
/*  180 */     this.strengthen_material = rs.getInt(27);
/*  181 */     this.gem_material = rs.getInt(28);
/*  182 */     this.star_material = rs.getInt(29);
/*  183 */     this.mer_material = rs.getInt(30);
/*  184 */     this.wing_material = rs.getInt(31);
/*  185 */     this.arena_token = rs.getInt(32);
/*  186 */     this.equip_instance_material = rs.getInt(33);
/*  187 */     this.gem_instance_material = rs.getInt(34);
/*  188 */     this.meridian_instance_material = rs.getInt(35);
/*  189 */     this.red_piece = rs.getInt(36);
/*  190 */     this.artifice_material = rs.getInt(37);
/*  191 */     this.lottery = rs.getInt(38);
/*  192 */     this.warspirit_talent_material = rs.getInt(39);
/*  193 */     this.warspirit_lv_material = rs.getInt(40);
/*  194 */     this.warspirit_lv = rs.getInt(41);
/*  195 */     this.warspirit_exp = rs.getInt(42);
/*  196 */     this.warspirit_talent = rs.getInt(43);
/*  197 */     this.dress_material = rs.getInt(44);
/*  198 */     this.exp_material = rs.getInt(45);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void getFromResultSet(ResultSet rs, List<PlayerBO> list) throws Exception {
/*  204 */     list.add(new PlayerBO(rs));
/*      */   }
/*      */ 
/*      */   
/*      */   public long getAsynTaskTag() {
/*  209 */     return getId();
/*      */   }
/*      */ 
/*      */   
/*      */   public String getItemsName() {
/*  214 */     return "`id`, `open_id`, `sid`, `name`, `icon`, `maxFightPower`, `gmLevel`, `cheatTimes`, `banned_ChatExpiredTime`, `banned_LoginExpiredTime`, `bannedTimes`, `lastLogin`, `lastLogout`, `online_update`, `totalRecharge`, `vipExp`, `vipLevel`, `lv`, `exp`, `crystal`, `gold`, `dungeon_lv`, `dungeon_time`, `ext_package`, `createTime`, `GmMailCheckId`, `strengthen_material`, `gem_material`, `star_material`, `mer_material`, `wing_material`, `arena_token`, `equip_instance_material`, `gem_instance_material`, `meridian_instance_material`, `red_piece`, `artifice_material`, `lottery`, `warspirit_talent_material`, `warspirit_lv_material`, `warspirit_lv`, `warspirit_exp`, `warspirit_talent`, `dress_material`, `exp_material`";
/*      */   }
/*      */ 
/*      */   
/*      */   public String getTableName() {
/*  219 */     return "`player`";
/*      */   }
/*      */ 
/*      */   
/*      */   public String getItemsValue() {
/*  224 */     StringBuilder strBuf = new StringBuilder();
/*  225 */     strBuf.append("'").append(this.id).append("', ");
/*  226 */     strBuf.append("'").append((this.open_id == null) ? null : this.open_id.replace("'", "''")).append("', ");
/*  227 */     strBuf.append("'").append(this.sid).append("', ");
/*  228 */     strBuf.append("'").append((this.name == null) ? null : this.name.replace("'", "''")).append("', ");
/*  229 */     strBuf.append("'").append(this.icon).append("', ");
/*  230 */     strBuf.append("'").append(this.maxFightPower).append("', ");
/*  231 */     strBuf.append("'").append(this.gmLevel).append("', ");
/*  232 */     strBuf.append("'").append(this.cheatTimes).append("', ");
/*  233 */     strBuf.append("'").append(this.banned_ChatExpiredTime).append("', ");
/*  234 */     strBuf.append("'").append(this.banned_LoginExpiredTime).append("', ");
/*  235 */     strBuf.append("'").append(this.bannedTimes).append("', ");
/*  236 */     strBuf.append("'").append(this.lastLogin).append("', ");
/*  237 */     strBuf.append("'").append(this.lastLogout).append("', ");
/*  238 */     strBuf.append("'").append(this.online_update).append("', ");
/*  239 */     strBuf.append("'").append(this.totalRecharge).append("', ");
/*  240 */     strBuf.append("'").append(this.vipExp).append("', ");
/*  241 */     strBuf.append("'").append(this.vipLevel).append("', ");
/*  242 */     strBuf.append("'").append(this.lv).append("', ");
/*  243 */     strBuf.append("'").append(this.exp).append("', ");
/*  244 */     strBuf.append("'").append(this.crystal).append("', ");
/*  245 */     strBuf.append("'").append(this.gold).append("', ");
/*  246 */     strBuf.append("'").append(this.dungeon_lv).append("', ");
/*  247 */     strBuf.append("'").append(this.dungeon_time).append("', ");
/*  248 */     strBuf.append("'").append(this.ext_package).append("', ");
/*  249 */     strBuf.append("'").append(this.createTime).append("', ");
/*  250 */     strBuf.append("'").append(this.GmMailCheckId).append("', ");
/*  251 */     strBuf.append("'").append(this.strengthen_material).append("', ");
/*  252 */     strBuf.append("'").append(this.gem_material).append("', ");
/*  253 */     strBuf.append("'").append(this.star_material).append("', ");
/*  254 */     strBuf.append("'").append(this.mer_material).append("', ");
/*  255 */     strBuf.append("'").append(this.wing_material).append("', ");
/*  256 */     strBuf.append("'").append(this.arena_token).append("', ");
/*  257 */     strBuf.append("'").append(this.equip_instance_material).append("', ");
/*  258 */     strBuf.append("'").append(this.gem_instance_material).append("', ");
/*  259 */     strBuf.append("'").append(this.meridian_instance_material).append("', ");
/*  260 */     strBuf.append("'").append(this.red_piece).append("', ");
/*  261 */     strBuf.append("'").append(this.artifice_material).append("', ");
/*  262 */     strBuf.append("'").append(this.lottery).append("', ");
/*  263 */     strBuf.append("'").append(this.warspirit_talent_material).append("', ");
/*  264 */     strBuf.append("'").append(this.warspirit_lv_material).append("', ");
/*  265 */     strBuf.append("'").append(this.warspirit_lv).append("', ");
/*  266 */     strBuf.append("'").append(this.warspirit_exp).append("', ");
/*  267 */     strBuf.append("'").append(this.warspirit_talent).append("', ");
/*  268 */     strBuf.append("'").append(this.dress_material).append("', ");
/*  269 */     strBuf.append("'").append(this.exp_material).append("', ");
/*  270 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  271 */     return strBuf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public ArrayList<byte[]> getInsertValueBytes() {
/*  276 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  277 */     return ret;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setId(long iID) {
/*  282 */     this.id = iID;
/*      */   }
/*      */ 
/*      */   
/*      */   public long getId() {
/*  287 */     return this.id;
/*      */   }
/*      */   
/*      */   public String getOpenId() {
/*  291 */     return this.open_id;
/*      */   } public void setOpenId(String open_id) {
/*  293 */     if (open_id.equals(this.open_id))
/*      */       return; 
/*  295 */     this.open_id = open_id;
/*      */   }
/*      */   public void saveOpenId(String open_id) {
/*  298 */     if (open_id.equals(this.open_id))
/*      */       return; 
/*  300 */     this.open_id = open_id;
/*  301 */     saveField("open_id", open_id);
/*      */   }
/*      */   
/*      */   public int getSid() {
/*  305 */     return this.sid;
/*      */   } public void setSid(int sid) {
/*  307 */     if (sid == this.sid)
/*      */       return; 
/*  309 */     this.sid = sid;
/*      */   }
/*      */   public void saveSid(int sid) {
/*  312 */     if (sid == this.sid)
/*      */       return; 
/*  314 */     this.sid = sid;
/*  315 */     saveField("sid", Integer.valueOf(sid));
/*      */   }
/*      */   
/*      */   public String getName() {
/*  319 */     return this.name;
/*      */   } public void setName(String name) {
/*  321 */     if (name.equals(this.name))
/*      */       return; 
/*  323 */     this.name = name;
/*      */   }
/*      */   public void saveName(String name) {
/*  326 */     if (name.equals(this.name))
/*      */       return; 
/*  328 */     this.name = name;
/*  329 */     saveField("name", name);
/*      */   }
/*      */   
/*      */   public int getIcon() {
/*  333 */     return this.icon;
/*      */   } public void setIcon(int icon) {
/*  335 */     if (icon == this.icon)
/*      */       return; 
/*  337 */     this.icon = icon;
/*      */   }
/*      */   public void saveIcon(int icon) {
/*  340 */     if (icon == this.icon)
/*      */       return; 
/*  342 */     this.icon = icon;
/*  343 */     saveField("icon", Integer.valueOf(icon));
/*      */   }
/*      */   
/*      */   public int getMaxFightPower() {
/*  347 */     return this.maxFightPower;
/*      */   } public void setMaxFightPower(int maxFightPower) {
/*  349 */     if (maxFightPower == this.maxFightPower)
/*      */       return; 
/*  351 */     this.maxFightPower = maxFightPower;
/*      */   }
/*      */   public void saveMaxFightPower(int maxFightPower) {
/*  354 */     if (maxFightPower == this.maxFightPower)
/*      */       return; 
/*  356 */     this.maxFightPower = maxFightPower;
/*  357 */     saveField("maxFightPower", Integer.valueOf(maxFightPower));
/*      */   }
/*      */   
/*      */   public int getGmLevel() {
/*  361 */     return this.gmLevel;
/*      */   } public void setGmLevel(int gmLevel) {
/*  363 */     if (gmLevel == this.gmLevel)
/*      */       return; 
/*  365 */     this.gmLevel = gmLevel;
/*      */   }
/*      */   public void saveGmLevel(int gmLevel) {
/*  368 */     if (gmLevel == this.gmLevel)
/*      */       return; 
/*  370 */     this.gmLevel = gmLevel;
/*  371 */     saveField("gmLevel", Integer.valueOf(gmLevel));
/*      */   }
/*      */   
/*      */   public int getCheatTimes() {
/*  375 */     return this.cheatTimes;
/*      */   } public void setCheatTimes(int cheatTimes) {
/*  377 */     if (cheatTimes == this.cheatTimes)
/*      */       return; 
/*  379 */     this.cheatTimes = cheatTimes;
/*      */   }
/*      */   public void saveCheatTimes(int cheatTimes) {
/*  382 */     if (cheatTimes == this.cheatTimes)
/*      */       return; 
/*  384 */     this.cheatTimes = cheatTimes;
/*  385 */     saveField("cheatTimes", Integer.valueOf(cheatTimes));
/*      */   }
/*      */   
/*      */   public int getBannedChatExpiredTime() {
/*  389 */     return this.banned_ChatExpiredTime;
/*      */   } public void setBannedChatExpiredTime(int banned_ChatExpiredTime) {
/*  391 */     if (banned_ChatExpiredTime == this.banned_ChatExpiredTime)
/*      */       return; 
/*  393 */     this.banned_ChatExpiredTime = banned_ChatExpiredTime;
/*      */   }
/*      */   public void saveBannedChatExpiredTime(int banned_ChatExpiredTime) {
/*  396 */     if (banned_ChatExpiredTime == this.banned_ChatExpiredTime)
/*      */       return; 
/*  398 */     this.banned_ChatExpiredTime = banned_ChatExpiredTime;
/*  399 */     saveField("banned_ChatExpiredTime", Integer.valueOf(banned_ChatExpiredTime));
/*      */   }
/*      */   
/*      */   public int getBannedLoginExpiredTime() {
/*  403 */     return this.banned_LoginExpiredTime;
/*      */   } public void setBannedLoginExpiredTime(int banned_LoginExpiredTime) {
/*  405 */     if (banned_LoginExpiredTime == this.banned_LoginExpiredTime)
/*      */       return; 
/*  407 */     this.banned_LoginExpiredTime = banned_LoginExpiredTime;
/*      */   }
/*      */   public void saveBannedLoginExpiredTime(int banned_LoginExpiredTime) {
/*  410 */     if (banned_LoginExpiredTime == this.banned_LoginExpiredTime)
/*      */       return; 
/*  412 */     this.banned_LoginExpiredTime = banned_LoginExpiredTime;
/*  413 */     saveField("banned_LoginExpiredTime", Integer.valueOf(banned_LoginExpiredTime));
/*      */   }
/*      */   
/*      */   public int getBannedTimes() {
/*  417 */     return this.bannedTimes;
/*      */   } public void setBannedTimes(int bannedTimes) {
/*  419 */     if (bannedTimes == this.bannedTimes)
/*      */       return; 
/*  421 */     this.bannedTimes = bannedTimes;
/*      */   }
/*      */   public void saveBannedTimes(int bannedTimes) {
/*  424 */     if (bannedTimes == this.bannedTimes)
/*      */       return; 
/*  426 */     this.bannedTimes = bannedTimes;
/*  427 */     saveField("bannedTimes", Integer.valueOf(bannedTimes));
/*      */   }
/*      */   
/*      */   public int getLastLogin() {
/*  431 */     return this.lastLogin;
/*      */   } public void setLastLogin(int lastLogin) {
/*  433 */     if (lastLogin == this.lastLogin)
/*      */       return; 
/*  435 */     this.lastLogin = lastLogin;
/*      */   }
/*      */   public void saveLastLogin(int lastLogin) {
/*  438 */     if (lastLogin == this.lastLogin)
/*      */       return; 
/*  440 */     this.lastLogin = lastLogin;
/*  441 */     saveField("lastLogin", Integer.valueOf(lastLogin));
/*      */   }
/*      */   
/*      */   public int getLastLogout() {
/*  445 */     return this.lastLogout;
/*      */   } public void setLastLogout(int lastLogout) {
/*  447 */     if (lastLogout == this.lastLogout)
/*      */       return; 
/*  449 */     this.lastLogout = lastLogout;
/*      */   }
/*      */   public void saveLastLogout(int lastLogout) {
/*  452 */     if (lastLogout == this.lastLogout)
/*      */       return; 
/*  454 */     this.lastLogout = lastLogout;
/*  455 */     saveField("lastLogout", Integer.valueOf(lastLogout));
/*      */   }
/*      */   
/*      */   public int getOnlineUpdate() {
/*  459 */     return this.online_update;
/*      */   } public void setOnlineUpdate(int online_update) {
/*  461 */     if (online_update == this.online_update)
/*      */       return; 
/*  463 */     this.online_update = online_update;
/*      */   }
/*      */   public void saveOnlineUpdate(int online_update) {
/*  466 */     if (online_update == this.online_update)
/*      */       return; 
/*  468 */     this.online_update = online_update;
/*  469 */     saveField("online_update", Integer.valueOf(online_update));
/*      */   }
/*      */   
/*      */   public int getTotalRecharge() {
/*  473 */     return this.totalRecharge;
/*      */   } public void setTotalRecharge(int totalRecharge) {
/*  475 */     if (totalRecharge == this.totalRecharge)
/*      */       return; 
/*  477 */     this.totalRecharge = totalRecharge;
/*      */   }
/*      */   public void saveTotalRecharge(int totalRecharge) {
/*  480 */     if (totalRecharge == this.totalRecharge)
/*      */       return; 
/*  482 */     this.totalRecharge = totalRecharge;
/*  483 */     saveField("totalRecharge", Integer.valueOf(totalRecharge));
/*      */   }
/*      */   
/*      */   public int getVipExp() {
/*  487 */     return this.vipExp;
/*      */   } public void setVipExp(int vipExp) {
/*  489 */     if (vipExp == this.vipExp)
/*      */       return; 
/*  491 */     this.vipExp = vipExp;
/*      */   }
/*      */   public void saveVipExp(int vipExp) {
/*  494 */     if (vipExp == this.vipExp)
/*      */       return; 
/*  496 */     this.vipExp = vipExp;
/*  497 */     saveField("vipExp", Integer.valueOf(vipExp));
/*      */   }
/*      */   
/*      */   public int getVipLevel() {
/*  501 */     return this.vipLevel;
/*      */   } public void setVipLevel(int vipLevel) {
/*  503 */     if (vipLevel == this.vipLevel)
/*      */       return; 
/*  505 */     this.vipLevel = vipLevel;
/*      */   }
/*      */   public void saveVipLevel(int vipLevel) {
/*  508 */     if (vipLevel == this.vipLevel)
/*      */       return; 
/*  510 */     this.vipLevel = vipLevel;
/*  511 */     saveField("vipLevel", Integer.valueOf(vipLevel));
/*      */   }
/*      */   
/*      */   public int getLv() {
/*  515 */     return this.lv;
/*      */   } public void setLv(int lv) {
/*  517 */     if (lv == this.lv)
/*      */       return; 
/*  519 */     this.lv = lv;
/*      */   }
/*      */   public void saveLv(int lv) {
/*  522 */     if (lv == this.lv)
/*      */       return; 
/*  524 */     this.lv = lv;
/*  525 */     saveField("lv", Integer.valueOf(lv));
/*      */   }
/*      */   
/*      */   public int getExp() {
/*  529 */     return this.exp;
/*      */   } public void setExp(int exp) {
/*  531 */     if (exp == this.exp)
/*      */       return; 
/*  533 */     this.exp = exp;
/*      */   }
/*      */   public void saveExp(int exp) {
/*  536 */     if (exp == this.exp)
/*      */       return; 
/*  538 */     this.exp = exp;
/*  539 */     saveField("exp", Integer.valueOf(exp));
/*      */   }
/*      */   
/*      */   public int getCrystal() {
/*  543 */     return this.crystal;
/*      */   } public void setCrystal(int crystal) {
/*  545 */     if (crystal == this.crystal)
/*      */       return; 
/*  547 */     this.crystal = crystal;
/*      */   }
/*      */   public void saveCrystal(int crystal) {
/*  550 */     if (crystal == this.crystal)
/*      */       return; 
/*  552 */     this.crystal = crystal;
/*  553 */     saveField("crystal", Integer.valueOf(crystal));
/*      */   }
/*      */   
/*      */   public int getGold() {
/*  557 */     return this.gold;
/*      */   } public void setGold(int gold) {
/*  559 */     if (gold == this.gold)
/*      */       return; 
/*  561 */     this.gold = gold;
/*      */   }
/*      */   public void saveGold(int gold) {
/*  564 */     if (gold == this.gold)
/*      */       return; 
/*  566 */     this.gold = gold;
/*  567 */     saveField("gold", Integer.valueOf(gold));
/*      */   }
/*      */   
/*      */   public int getDungeonLv() {
/*  571 */     return this.dungeon_lv;
/*      */   } public void setDungeonLv(int dungeon_lv) {
/*  573 */     if (dungeon_lv == this.dungeon_lv)
/*      */       return; 
/*  575 */     this.dungeon_lv = dungeon_lv;
/*      */   }
/*      */   public void saveDungeonLv(int dungeon_lv) {
/*  578 */     if (dungeon_lv == this.dungeon_lv)
/*      */       return; 
/*  580 */     this.dungeon_lv = dungeon_lv;
/*  581 */     saveField("dungeon_lv", Integer.valueOf(dungeon_lv));
/*      */   }
/*      */   
/*      */   public int getDungeonTime() {
/*  585 */     return this.dungeon_time;
/*      */   } public void setDungeonTime(int dungeon_time) {
/*  587 */     if (dungeon_time == this.dungeon_time)
/*      */       return; 
/*  589 */     this.dungeon_time = dungeon_time;
/*      */   }
/*      */   public void saveDungeonTime(int dungeon_time) {
/*  592 */     if (dungeon_time == this.dungeon_time)
/*      */       return; 
/*  594 */     this.dungeon_time = dungeon_time;
/*  595 */     saveField("dungeon_time", Integer.valueOf(dungeon_time));
/*      */   }
/*      */   
/*      */   public int getExtPackage() {
/*  599 */     return this.ext_package;
/*      */   } public void setExtPackage(int ext_package) {
/*  601 */     if (ext_package == this.ext_package)
/*      */       return; 
/*  603 */     this.ext_package = ext_package;
/*      */   }
/*      */   public void saveExtPackage(int ext_package) {
/*  606 */     if (ext_package == this.ext_package)
/*      */       return; 
/*  608 */     this.ext_package = ext_package;
/*  609 */     saveField("ext_package", Integer.valueOf(ext_package));
/*      */   }
/*      */   
/*      */   public int getCreateTime() {
/*  613 */     return this.createTime;
/*      */   } public void setCreateTime(int createTime) {
/*  615 */     if (createTime == this.createTime)
/*      */       return; 
/*  617 */     this.createTime = createTime;
/*      */   }
/*      */   public void saveCreateTime(int createTime) {
/*  620 */     if (createTime == this.createTime)
/*      */       return; 
/*  622 */     this.createTime = createTime;
/*  623 */     saveField("createTime", Integer.valueOf(createTime));
/*      */   }
/*      */   
/*      */   public long getGmMailCheckId() {
/*  627 */     return this.GmMailCheckId;
/*      */   } public void setGmMailCheckId(long GmMailCheckId) {
/*  629 */     if (GmMailCheckId == this.GmMailCheckId)
/*      */       return; 
/*  631 */     this.GmMailCheckId = GmMailCheckId;
/*      */   }
/*      */   public void saveGmMailCheckId(long GmMailCheckId) {
/*  634 */     if (GmMailCheckId == this.GmMailCheckId)
/*      */       return; 
/*  636 */     this.GmMailCheckId = GmMailCheckId;
/*  637 */     saveField("GmMailCheckId", Long.valueOf(GmMailCheckId));
/*      */   }
/*      */   
/*      */   public int getStrengthenMaterial() {
/*  641 */     return this.strengthen_material;
/*      */   } public void setStrengthenMaterial(int strengthen_material) {
/*  643 */     if (strengthen_material == this.strengthen_material)
/*      */       return; 
/*  645 */     this.strengthen_material = strengthen_material;
/*      */   }
/*      */   public void saveStrengthenMaterial(int strengthen_material) {
/*  648 */     if (strengthen_material == this.strengthen_material)
/*      */       return; 
/*  650 */     this.strengthen_material = strengthen_material;
/*  651 */     saveField("strengthen_material", Integer.valueOf(strengthen_material));
/*      */   }
/*      */   
/*      */   public int getGemMaterial() {
/*  655 */     return this.gem_material;
/*      */   } public void setGemMaterial(int gem_material) {
/*  657 */     if (gem_material == this.gem_material)
/*      */       return; 
/*  659 */     this.gem_material = gem_material;
/*      */   }
/*      */   public void saveGemMaterial(int gem_material) {
/*  662 */     if (gem_material == this.gem_material)
/*      */       return; 
/*  664 */     this.gem_material = gem_material;
/*  665 */     saveField("gem_material", Integer.valueOf(gem_material));
/*      */   }
/*      */   
/*      */   public int getStarMaterial() {
/*  669 */     return this.star_material;
/*      */   } public void setStarMaterial(int star_material) {
/*  671 */     if (star_material == this.star_material)
/*      */       return; 
/*  673 */     this.star_material = star_material;
/*      */   }
/*      */   public void saveStarMaterial(int star_material) {
/*  676 */     if (star_material == this.star_material)
/*      */       return; 
/*  678 */     this.star_material = star_material;
/*  679 */     saveField("star_material", Integer.valueOf(star_material));
/*      */   }
/*      */   
/*      */   public int getMerMaterial() {
/*  683 */     return this.mer_material;
/*      */   } public void setMerMaterial(int mer_material) {
/*  685 */     if (mer_material == this.mer_material)
/*      */       return; 
/*  687 */     this.mer_material = mer_material;
/*      */   }
/*      */   public void saveMerMaterial(int mer_material) {
/*  690 */     if (mer_material == this.mer_material)
/*      */       return; 
/*  692 */     this.mer_material = mer_material;
/*  693 */     saveField("mer_material", Integer.valueOf(mer_material));
/*      */   }
/*      */   
/*      */   public int getWingMaterial() {
/*  697 */     return this.wing_material;
/*      */   } public void setWingMaterial(int wing_material) {
/*  699 */     if (wing_material == this.wing_material)
/*      */       return; 
/*  701 */     this.wing_material = wing_material;
/*      */   }
/*      */   public void saveWingMaterial(int wing_material) {
/*  704 */     if (wing_material == this.wing_material)
/*      */       return; 
/*  706 */     this.wing_material = wing_material;
/*  707 */     saveField("wing_material", Integer.valueOf(wing_material));
/*      */   }
/*      */   
/*      */   public int getArenaToken() {
/*  711 */     return this.arena_token;
/*      */   } public void setArenaToken(int arena_token) {
/*  713 */     if (arena_token == this.arena_token)
/*      */       return; 
/*  715 */     this.arena_token = arena_token;
/*      */   }
/*      */   public void saveArenaToken(int arena_token) {
/*  718 */     if (arena_token == this.arena_token)
/*      */       return; 
/*  720 */     this.arena_token = arena_token;
/*  721 */     saveField("arena_token", Integer.valueOf(arena_token));
/*      */   }
/*      */   
/*      */   public int getEquipInstanceMaterial() {
/*  725 */     return this.equip_instance_material;
/*      */   } public void setEquipInstanceMaterial(int equip_instance_material) {
/*  727 */     if (equip_instance_material == this.equip_instance_material)
/*      */       return; 
/*  729 */     this.equip_instance_material = equip_instance_material;
/*      */   }
/*      */   public void saveEquipInstanceMaterial(int equip_instance_material) {
/*  732 */     if (equip_instance_material == this.equip_instance_material)
/*      */       return; 
/*  734 */     this.equip_instance_material = equip_instance_material;
/*  735 */     saveField("equip_instance_material", Integer.valueOf(equip_instance_material));
/*      */   }
/*      */   
/*      */   public int getGemInstanceMaterial() {
/*  739 */     return this.gem_instance_material;
/*      */   } public void setGemInstanceMaterial(int gem_instance_material) {
/*  741 */     if (gem_instance_material == this.gem_instance_material)
/*      */       return; 
/*  743 */     this.gem_instance_material = gem_instance_material;
/*      */   }
/*      */   public void saveGemInstanceMaterial(int gem_instance_material) {
/*  746 */     if (gem_instance_material == this.gem_instance_material)
/*      */       return; 
/*  748 */     this.gem_instance_material = gem_instance_material;
/*  749 */     saveField("gem_instance_material", Integer.valueOf(gem_instance_material));
/*      */   }
/*      */   
/*      */   public int getMeridianInstanceMaterial() {
/*  753 */     return this.meridian_instance_material;
/*      */   } public void setMeridianInstanceMaterial(int meridian_instance_material) {
/*  755 */     if (meridian_instance_material == this.meridian_instance_material)
/*      */       return; 
/*  757 */     this.meridian_instance_material = meridian_instance_material;
/*      */   }
/*      */   public void saveMeridianInstanceMaterial(int meridian_instance_material) {
/*  760 */     if (meridian_instance_material == this.meridian_instance_material)
/*      */       return; 
/*  762 */     this.meridian_instance_material = meridian_instance_material;
/*  763 */     saveField("meridian_instance_material", Integer.valueOf(meridian_instance_material));
/*      */   }
/*      */   
/*      */   public int getRedPiece() {
/*  767 */     return this.red_piece;
/*      */   } public void setRedPiece(int red_piece) {
/*  769 */     if (red_piece == this.red_piece)
/*      */       return; 
/*  771 */     this.red_piece = red_piece;
/*      */   }
/*      */   public void saveRedPiece(int red_piece) {
/*  774 */     if (red_piece == this.red_piece)
/*      */       return; 
/*  776 */     this.red_piece = red_piece;
/*  777 */     saveField("red_piece", Integer.valueOf(red_piece));
/*      */   }
/*      */   
/*      */   public int getArtificeMaterial() {
/*  781 */     return this.artifice_material;
/*      */   } public void setArtificeMaterial(int artifice_material) {
/*  783 */     if (artifice_material == this.artifice_material)
/*      */       return; 
/*  785 */     this.artifice_material = artifice_material;
/*      */   }
/*      */   public void saveArtificeMaterial(int artifice_material) {
/*  788 */     if (artifice_material == this.artifice_material)
/*      */       return; 
/*  790 */     this.artifice_material = artifice_material;
/*  791 */     saveField("artifice_material", Integer.valueOf(artifice_material));
/*      */   }
/*      */   
/*      */   public int getLottery() {
/*  795 */     return this.lottery;
/*      */   } public void setLottery(int lottery) {
/*  797 */     if (lottery == this.lottery)
/*      */       return; 
/*  799 */     this.lottery = lottery;
/*      */   }
/*      */   public void saveLottery(int lottery) {
/*  802 */     if (lottery == this.lottery)
/*      */       return; 
/*  804 */     this.lottery = lottery;
/*  805 */     saveField("lottery", Integer.valueOf(lottery));
/*      */   }
/*      */   
/*      */   public int getWarspiritTalentMaterial() {
/*  809 */     return this.warspirit_talent_material;
/*      */   } public void setWarspiritTalentMaterial(int warspirit_talent_material) {
/*  811 */     if (warspirit_talent_material == this.warspirit_talent_material)
/*      */       return; 
/*  813 */     this.warspirit_talent_material = warspirit_talent_material;
/*      */   }
/*      */   public void saveWarspiritTalentMaterial(int warspirit_talent_material) {
/*  816 */     if (warspirit_talent_material == this.warspirit_talent_material)
/*      */       return; 
/*  818 */     this.warspirit_talent_material = warspirit_talent_material;
/*  819 */     saveField("warspirit_talent_material", Integer.valueOf(warspirit_talent_material));
/*      */   }
/*      */   
/*      */   public int getWarspiritLvMaterial() {
/*  823 */     return this.warspirit_lv_material;
/*      */   } public void setWarspiritLvMaterial(int warspirit_lv_material) {
/*  825 */     if (warspirit_lv_material == this.warspirit_lv_material)
/*      */       return; 
/*  827 */     this.warspirit_lv_material = warspirit_lv_material;
/*      */   }
/*      */   public void saveWarspiritLvMaterial(int warspirit_lv_material) {
/*  830 */     if (warspirit_lv_material == this.warspirit_lv_material)
/*      */       return; 
/*  832 */     this.warspirit_lv_material = warspirit_lv_material;
/*  833 */     saveField("warspirit_lv_material", Integer.valueOf(warspirit_lv_material));
/*      */   }
/*      */   
/*      */   public int getWarspiritLv() {
/*  837 */     return this.warspirit_lv;
/*      */   } public void setWarspiritLv(int warspirit_lv) {
/*  839 */     if (warspirit_lv == this.warspirit_lv)
/*      */       return; 
/*  841 */     this.warspirit_lv = warspirit_lv;
/*      */   }
/*      */   public void saveWarspiritLv(int warspirit_lv) {
/*  844 */     if (warspirit_lv == this.warspirit_lv)
/*      */       return; 
/*  846 */     this.warspirit_lv = warspirit_lv;
/*  847 */     saveField("warspirit_lv", Integer.valueOf(warspirit_lv));
/*      */   }
/*      */   
/*      */   public int getWarspiritExp() {
/*  851 */     return this.warspirit_exp;
/*      */   } public void setWarspiritExp(int warspirit_exp) {
/*  853 */     if (warspirit_exp == this.warspirit_exp)
/*      */       return; 
/*  855 */     this.warspirit_exp = warspirit_exp;
/*      */   }
/*      */   public void saveWarspiritExp(int warspirit_exp) {
/*  858 */     if (warspirit_exp == this.warspirit_exp)
/*      */       return; 
/*  860 */     this.warspirit_exp = warspirit_exp;
/*  861 */     saveField("warspirit_exp", Integer.valueOf(warspirit_exp));
/*      */   }
/*      */   
/*      */   public int getWarspiritTalent() {
/*  865 */     return this.warspirit_talent;
/*      */   } public void setWarspiritTalent(int warspirit_talent) {
/*  867 */     if (warspirit_talent == this.warspirit_talent)
/*      */       return; 
/*  869 */     this.warspirit_talent = warspirit_talent;
/*      */   }
/*      */   public void saveWarspiritTalent(int warspirit_talent) {
/*  872 */     if (warspirit_talent == this.warspirit_talent)
/*      */       return; 
/*  874 */     this.warspirit_talent = warspirit_talent;
/*  875 */     saveField("warspirit_talent", Integer.valueOf(warspirit_talent));
/*      */   }
/*      */   
/*      */   public int getDressMaterial() {
/*  879 */     return this.dress_material;
/*      */   } public void setDressMaterial(int dress_material) {
/*  881 */     if (dress_material == this.dress_material)
/*      */       return; 
/*  883 */     this.dress_material = dress_material;
/*      */   }
/*      */   public void saveDressMaterial(int dress_material) {
/*  886 */     if (dress_material == this.dress_material)
/*      */       return; 
/*  888 */     this.dress_material = dress_material;
/*  889 */     saveField("dress_material", Integer.valueOf(dress_material));
/*      */   }
/*      */   
/*      */   public int getExpMaterial() {
/*  893 */     return this.exp_material;
/*      */   } public void setExpMaterial(int exp_material) {
/*  895 */     if (exp_material == this.exp_material)
/*      */       return; 
/*  897 */     this.exp_material = exp_material;
/*      */   }
/*      */   public void saveExpMaterial(int exp_material) {
/*  900 */     if (exp_material == this.exp_material)
/*      */       return; 
/*  902 */     this.exp_material = exp_material;
/*  903 */     saveField("exp_material", Integer.valueOf(exp_material));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getUpdateKeyValue() {
/*  910 */     StringBuilder sBuilder = new StringBuilder();
/*  911 */     sBuilder.append(" `open_id` = '").append((this.open_id == null) ? null : this.open_id.replace("'", "''")).append("',");
/*  912 */     sBuilder.append(" `sid` = '").append(this.sid).append("',");
/*  913 */     sBuilder.append(" `name` = '").append((this.name == null) ? null : this.name.replace("'", "''")).append("',");
/*  914 */     sBuilder.append(" `icon` = '").append(this.icon).append("',");
/*  915 */     sBuilder.append(" `maxFightPower` = '").append(this.maxFightPower).append("',");
/*  916 */     sBuilder.append(" `gmLevel` = '").append(this.gmLevel).append("',");
/*  917 */     sBuilder.append(" `cheatTimes` = '").append(this.cheatTimes).append("',");
/*  918 */     sBuilder.append(" `banned_ChatExpiredTime` = '").append(this.banned_ChatExpiredTime).append("',");
/*  919 */     sBuilder.append(" `banned_LoginExpiredTime` = '").append(this.banned_LoginExpiredTime).append("',");
/*  920 */     sBuilder.append(" `bannedTimes` = '").append(this.bannedTimes).append("',");
/*  921 */     sBuilder.append(" `lastLogin` = '").append(this.lastLogin).append("',");
/*  922 */     sBuilder.append(" `lastLogout` = '").append(this.lastLogout).append("',");
/*  923 */     sBuilder.append(" `online_update` = '").append(this.online_update).append("',");
/*  924 */     sBuilder.append(" `totalRecharge` = '").append(this.totalRecharge).append("',");
/*  925 */     sBuilder.append(" `vipExp` = '").append(this.vipExp).append("',");
/*  926 */     sBuilder.append(" `vipLevel` = '").append(this.vipLevel).append("',");
/*  927 */     sBuilder.append(" `lv` = '").append(this.lv).append("',");
/*  928 */     sBuilder.append(" `exp` = '").append(this.exp).append("',");
/*  929 */     sBuilder.append(" `crystal` = '").append(this.crystal).append("',");
/*  930 */     sBuilder.append(" `gold` = '").append(this.gold).append("',");
/*  931 */     sBuilder.append(" `dungeon_lv` = '").append(this.dungeon_lv).append("',");
/*  932 */     sBuilder.append(" `dungeon_time` = '").append(this.dungeon_time).append("',");
/*  933 */     sBuilder.append(" `ext_package` = '").append(this.ext_package).append("',");
/*  934 */     sBuilder.append(" `createTime` = '").append(this.createTime).append("',");
/*  935 */     sBuilder.append(" `GmMailCheckId` = '").append(this.GmMailCheckId).append("',");
/*  936 */     sBuilder.append(" `strengthen_material` = '").append(this.strengthen_material).append("',");
/*  937 */     sBuilder.append(" `gem_material` = '").append(this.gem_material).append("',");
/*  938 */     sBuilder.append(" `star_material` = '").append(this.star_material).append("',");
/*  939 */     sBuilder.append(" `mer_material` = '").append(this.mer_material).append("',");
/*  940 */     sBuilder.append(" `wing_material` = '").append(this.wing_material).append("',");
/*  941 */     sBuilder.append(" `arena_token` = '").append(this.arena_token).append("',");
/*  942 */     sBuilder.append(" `equip_instance_material` = '").append(this.equip_instance_material).append("',");
/*  943 */     sBuilder.append(" `gem_instance_material` = '").append(this.gem_instance_material).append("',");
/*  944 */     sBuilder.append(" `meridian_instance_material` = '").append(this.meridian_instance_material).append("',");
/*  945 */     sBuilder.append(" `red_piece` = '").append(this.red_piece).append("',");
/*  946 */     sBuilder.append(" `artifice_material` = '").append(this.artifice_material).append("',");
/*  947 */     sBuilder.append(" `lottery` = '").append(this.lottery).append("',");
/*  948 */     sBuilder.append(" `warspirit_talent_material` = '").append(this.warspirit_talent_material).append("',");
/*  949 */     sBuilder.append(" `warspirit_lv_material` = '").append(this.warspirit_lv_material).append("',");
/*  950 */     sBuilder.append(" `warspirit_lv` = '").append(this.warspirit_lv).append("',");
/*  951 */     sBuilder.append(" `warspirit_exp` = '").append(this.warspirit_exp).append("',");
/*  952 */     sBuilder.append(" `warspirit_talent` = '").append(this.warspirit_talent).append("',");
/*  953 */     sBuilder.append(" `dress_material` = '").append(this.dress_material).append("',");
/*  954 */     sBuilder.append(" `exp_material` = '").append(this.exp_material).append("',");
/*  955 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/*  956 */     return sBuilder.toString();
/*      */   }
/*      */   
/*      */   public static String getSql_TableCreate() {
/*  960 */     String sql = "CREATE TABLE IF NOT EXISTS `player` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`open_id` varchar(50) NOT NULL DEFAULT '' COMMENT '玩家账号ID',`sid` int(11) NOT NULL DEFAULT '0' COMMENT '服务器id',`name` varchar(50) NOT NULL DEFAULT '' COMMENT '玩家名称/昵称',`icon` int(11) NOT NULL DEFAULT '0' COMMENT '头像',`maxFightPower` int(11) NOT NULL DEFAULT '0' COMMENT '最大战斗力',`gmLevel` int(11) NOT NULL DEFAULT '0' COMMENT 'gm权限',`cheatTimes` int(11) NOT NULL DEFAULT '0' COMMENT '作弊次数',`banned_ChatExpiredTime` int(11) NOT NULL DEFAULT '0' COMMENT '禁言过期时间',`banned_LoginExpiredTime` int(11) NOT NULL DEFAULT '0' COMMENT '禁登过期时间',`bannedTimes` int(11) NOT NULL DEFAULT '0' COMMENT '封号次数',`lastLogin` int(11) NOT NULL DEFAULT '0' COMMENT '最近一次登陆時間',`lastLogout` int(11) NOT NULL DEFAULT '0' COMMENT '最近一次登出時間',`online_update` int(11) NOT NULL DEFAULT '0' COMMENT '在线更新时间',`totalRecharge` int(11) NOT NULL DEFAULT '0' COMMENT '累计充值, 统计玩家充值RMB',`vipExp` int(11) NOT NULL DEFAULT '0' COMMENT 'vip经验',`vipLevel` int(11) NOT NULL DEFAULT '0' COMMENT 'vip等级',`lv` int(11) NOT NULL DEFAULT '0' COMMENT '等级',`exp` int(11) NOT NULL DEFAULT '0' COMMENT '队伍经验',`crystal` int(11) NOT NULL DEFAULT '0' COMMENT '水晶数量',`gold` int(11) NOT NULL DEFAULT '0' COMMENT '金币',`dungeon_lv` int(11) NOT NULL DEFAULT '0' COMMENT '副本等级',`dungeon_time` int(11) NOT NULL DEFAULT '0' COMMENT '上次打副本时间',`ext_package` int(11) NOT NULL DEFAULT '0' COMMENT '额外背包容量',`createTime` int(11) NOT NULL DEFAULT '0' COMMENT '账号创建时间,单位秒',`GmMailCheckId` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Gm邮件领取编号',`strengthen_material` int(11) NOT NULL DEFAULT '0' COMMENT '强化材料',`gem_material` int(11) NOT NULL DEFAULT '0' COMMENT '宝石材料',`star_material` int(11) NOT NULL DEFAULT '0' COMMENT '升星材料',`mer_material` int(11) NOT NULL DEFAULT '0' COMMENT '经脉材料',`wing_material` int(11) NOT NULL DEFAULT '0' COMMENT '翅膀材料',`arena_token` int(11) NOT NULL DEFAULT '0' COMMENT '竞技场代币',`equip_instance_material` int(11) NOT NULL DEFAULT '0' COMMENT '装备副本代币',`gem_instance_material` int(11) NOT NULL DEFAULT '0' COMMENT '宝石副本代币',`meridian_instance_material` int(11) NOT NULL DEFAULT '0' COMMENT '经脉副本代币',`red_piece` int(11) NOT NULL DEFAULT '0' COMMENT '红装碎片',`artifice_material` int(11) NOT NULL DEFAULT '0' COMMENT '炼化石',`lottery` int(11) NOT NULL DEFAULT '0' COMMENT '奖券',`warspirit_talent_material` int(11) NOT NULL DEFAULT '0' COMMENT '战灵天赋材料',`warspirit_lv_material` int(11) NOT NULL DEFAULT '0' COMMENT '战灵等级材料',`warspirit_lv` int(11) NOT NULL DEFAULT '0' COMMENT '战灵等级',`warspirit_exp` int(11) NOT NULL DEFAULT '0' COMMENT '战灵经验',`warspirit_talent` int(11) NOT NULL DEFAULT '0' COMMENT '战灵天赋',`dress_material` int(11) NOT NULL DEFAULT '0' COMMENT '时装材料',`exp_material` int(11) NOT NULL DEFAULT '0' COMMENT '经验材料',KEY `open_id` (`open_id`),UNIQUE INDEX `name` (`name`),PRIMARY KEY (`id`)) COMMENT='玩家信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1009 */       ServerConfig.getInitialID() + 1L);
/* 1010 */     return sql;
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/PlayerBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */