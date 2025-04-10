package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GuildBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "int(11)", fieldname = "level", comment = "帮会等级")
private int level;
@DataBaseField(type = "int(11)", fieldname = "exp", comment = "帮会经验")
private int exp;
@DataBaseField(type = "int(11)", fieldname = "historyExp", comment = "历史帮会经验")
private int historyExp;
@DataBaseField(type = "varchar(50)", fieldname = "name", comment = "名称")
private String name;
@DataBaseField(type = "int(11)", fieldname = "icon", comment = "头像")
private int icon;
@DataBaseField(type = "int(11)", fieldname = "border", comment = "边框")
private int border;
@DataBaseField(type = "varchar(500)", fieldname = "notice", comment = "公告")
private String notice;
@DataBaseField(type = "varchar(500)", fieldname = "manifesto", comment = "宣言")
private String manifesto;
@DataBaseField(type = "int(11)", fieldname = "joinState", comment = "加入状态")
private int joinState;
@DataBaseField(type = "int(11)", fieldname = "createTime", comment = "创建时间")
private int createTime;
@DataBaseField(type = "int(11)", fieldname = "lastLoginTime", comment = "公会玩家最近一次登录时间")
private int lastLoginTime;
@DataBaseField(type = "int(11)", fieldname = "levelLimit", comment = "加入等级限制")
private int levelLimit;
@DataBaseField(type = "int(11)", fieldname = "maxFightPower", comment = "历史最大战斗力")
private int maxFightPower;
@DataBaseField(type = "int(11)", fieldname = "donate", comment = "历史最大贡献度")
private int donate;
@DataBaseField(type = "int(11)", fieldname = "guildbossLevel", comment = "最大通关帮派副本等级")
private int guildbossLevel;
@DataBaseField(type = "int(11)", fieldname = "guildbossOpenNum", comment = "每日帮派副本开启数")
private int guildbossOpenNum;
@DataBaseField(type = "int(11)", fieldname = "lnlevel", comment = "龙女等级")
private int lnlevel;
@DataBaseField(type = "int(11)", fieldname = "lnexp", comment = "龙女经验")
private int lnexp;
@DataBaseField(type = "int(11)", fieldname = "lnwarLevel", comment = "龙女战力档次")
private int lnwarLevel;

public GuildBO() {
this.id = 0L;
this.level = 0;
this.exp = 0;
this.historyExp = 0;
this.name = "";
this.icon = 0;
this.border = 0;
this.notice = "";
this.manifesto = "";
this.joinState = 0;
this.createTime = 0;
this.lastLoginTime = 0;
this.levelLimit = 0;
this.maxFightPower = 0;
this.donate = 0;
this.guildbossLevel = 0;
this.guildbossOpenNum = 0;
this.lnlevel = 0;
this.lnexp = 0;
this.lnwarLevel = 0;
}

public GuildBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.level = rs.getInt(2);
this.exp = rs.getInt(3);
this.historyExp = rs.getInt(4);
this.name = rs.getString(5);
this.icon = rs.getInt(6);
this.border = rs.getInt(7);
this.notice = rs.getString(8);
this.manifesto = rs.getString(9);
this.joinState = rs.getInt(10);
this.createTime = rs.getInt(11);
this.lastLoginTime = rs.getInt(12);
this.levelLimit = rs.getInt(13);
this.maxFightPower = rs.getInt(14);
this.donate = rs.getInt(15);
this.guildbossLevel = rs.getInt(16);
this.guildbossOpenNum = rs.getInt(17);
this.lnlevel = rs.getInt(18);
this.lnexp = rs.getInt(19);
this.lnwarLevel = rs.getInt(20);
}

public void getFromResultSet(ResultSet rs, List<GuildBO> list) throws Exception {
list.add(new GuildBO(rs));
}

public long getAsynTaskTag() {
return getId();
}

public String getItemsName() {
return "`id`, `level`, `exp`, `historyExp`, `name`, `icon`, `border`, `notice`, `manifesto`, `joinState`, `createTime`, `lastLoginTime`, `levelLimit`, `maxFightPower`, `donate`, `guildbossLevel`, `guildbossOpenNum`, `lnlevel`, `lnexp`, `lnwarLevel`";
}

public String getTableName() {
return "`guild`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.level).append("', ");
strBuf.append("'").append(this.exp).append("', ");
strBuf.append("'").append(this.historyExp).append("', ");
strBuf.append("'").append((this.name == null) ? null : this.name.replace("'", "''")).append("', ");
strBuf.append("'").append(this.icon).append("', ");
strBuf.append("'").append(this.border).append("', ");
strBuf.append("'").append((this.notice == null) ? null : this.notice.replace("'", "''")).append("', ");
strBuf.append("'").append((this.manifesto == null) ? null : this.manifesto.replace("'", "''")).append("', ");
strBuf.append("'").append(this.joinState).append("', ");
strBuf.append("'").append(this.createTime).append("', ");
strBuf.append("'").append(this.lastLoginTime).append("', ");
strBuf.append("'").append(this.levelLimit).append("', ");
strBuf.append("'").append(this.maxFightPower).append("', ");
strBuf.append("'").append(this.donate).append("', ");
strBuf.append("'").append(this.guildbossLevel).append("', ");
strBuf.append("'").append(this.guildbossOpenNum).append("', ");
strBuf.append("'").append(this.lnlevel).append("', ");
strBuf.append("'").append(this.lnexp).append("', ");
strBuf.append("'").append(this.lnwarLevel).append("', ");
strBuf.deleteCharAt(strBuf.length() - 2);
return strBuf.toString();
}

public ArrayList<byte[]> getInsertValueBytes() {
ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
return ret;
}

public void setId(long iID) {
this.id = iID;
}

public long getId() {
return this.id;
}

public int getLevel() {
return this.level;
} public void setLevel(int level) {
if (level == this.level)
return; 
this.level = level;
}
public void saveLevel(int level) {
if (level == this.level)
return; 
this.level = level;
saveField("level", Integer.valueOf(level));
}

public int getExp() {
return this.exp;
} public void setExp(int exp) {
if (exp == this.exp)
return; 
this.exp = exp;
}
public void saveExp(int exp) {
if (exp == this.exp)
return; 
this.exp = exp;
saveField("exp", Integer.valueOf(exp));
}

public int getHistoryExp() {
return this.historyExp;
} public void setHistoryExp(int historyExp) {
if (historyExp == this.historyExp)
return; 
this.historyExp = historyExp;
}
public void saveHistoryExp(int historyExp) {
if (historyExp == this.historyExp)
return; 
this.historyExp = historyExp;
saveField("historyExp", Integer.valueOf(historyExp));
}

public String getName() {
return this.name;
} public void setName(String name) {
if (name.equals(this.name))
return; 
this.name = name;
}
public void saveName(String name) {
if (name.equals(this.name))
return; 
this.name = name;
saveField("name", name);
}

public int getIcon() {
return this.icon;
} public void setIcon(int icon) {
if (icon == this.icon)
return; 
this.icon = icon;
}
public void saveIcon(int icon) {
if (icon == this.icon)
return; 
this.icon = icon;
saveField("icon", Integer.valueOf(icon));
}

public int getBorder() {
return this.border;
} public void setBorder(int border) {
if (border == this.border)
return; 
this.border = border;
}
public void saveBorder(int border) {
if (border == this.border)
return; 
this.border = border;
saveField("border", Integer.valueOf(border));
}

public String getNotice() {
return this.notice;
} public void setNotice(String notice) {
if (notice.equals(this.notice))
return; 
this.notice = notice;
}
public void saveNotice(String notice) {
if (notice.equals(this.notice))
return; 
this.notice = notice;
saveField("notice", notice);
}

public String getManifesto() {
return this.manifesto;
} public void setManifesto(String manifesto) {
if (manifesto.equals(this.manifesto))
return; 
this.manifesto = manifesto;
}
public void saveManifesto(String manifesto) {
if (manifesto.equals(this.manifesto))
return; 
this.manifesto = manifesto;
saveField("manifesto", manifesto);
}

public int getJoinState() {
return this.joinState;
} public void setJoinState(int joinState) {
if (joinState == this.joinState)
return; 
this.joinState = joinState;
}
public void saveJoinState(int joinState) {
if (joinState == this.joinState)
return; 
this.joinState = joinState;
saveField("joinState", Integer.valueOf(joinState));
}

public int getCreateTime() {
return this.createTime;
} public void setCreateTime(int createTime) {
if (createTime == this.createTime)
return; 
this.createTime = createTime;
}
public void saveCreateTime(int createTime) {
if (createTime == this.createTime)
return; 
this.createTime = createTime;
saveField("createTime", Integer.valueOf(createTime));
}

public int getLastLoginTime() {
return this.lastLoginTime;
} public void setLastLoginTime(int lastLoginTime) {
if (lastLoginTime == this.lastLoginTime)
return; 
this.lastLoginTime = lastLoginTime;
}
public void saveLastLoginTime(int lastLoginTime) {
if (lastLoginTime == this.lastLoginTime)
return; 
this.lastLoginTime = lastLoginTime;
saveField("lastLoginTime", Integer.valueOf(lastLoginTime));
}

public int getLevelLimit() {
return this.levelLimit;
} public void setLevelLimit(int levelLimit) {
if (levelLimit == this.levelLimit)
return; 
this.levelLimit = levelLimit;
}
public void saveLevelLimit(int levelLimit) {
if (levelLimit == this.levelLimit)
return; 
this.levelLimit = levelLimit;
saveField("levelLimit", Integer.valueOf(levelLimit));
}

public int getMaxFightPower() {
return this.maxFightPower;
} public void setMaxFightPower(int maxFightPower) {
if (maxFightPower == this.maxFightPower)
return; 
this.maxFightPower = maxFightPower;
}
public void saveMaxFightPower(int maxFightPower) {
if (maxFightPower == this.maxFightPower)
return; 
this.maxFightPower = maxFightPower;
saveField("maxFightPower", Integer.valueOf(maxFightPower));
}

public int getDonate() {
return this.donate;
} public void setDonate(int donate) {
if (donate == this.donate)
return; 
this.donate = donate;
}
public void saveDonate(int donate) {
if (donate == this.donate)
return; 
this.donate = donate;
saveField("donate", Integer.valueOf(donate));
}

public int getGuildbossLevel() {
return this.guildbossLevel;
} public void setGuildbossLevel(int guildbossLevel) {
if (guildbossLevel == this.guildbossLevel)
return; 
this.guildbossLevel = guildbossLevel;
}
public void saveGuildbossLevel(int guildbossLevel) {
if (guildbossLevel == this.guildbossLevel)
return; 
this.guildbossLevel = guildbossLevel;
saveField("guildbossLevel", Integer.valueOf(guildbossLevel));
}

public int getGuildbossOpenNum() {
return this.guildbossOpenNum;
} public void setGuildbossOpenNum(int guildbossOpenNum) {
if (guildbossOpenNum == this.guildbossOpenNum)
return; 
this.guildbossOpenNum = guildbossOpenNum;
}
public void saveGuildbossOpenNum(int guildbossOpenNum) {
if (guildbossOpenNum == this.guildbossOpenNum)
return; 
this.guildbossOpenNum = guildbossOpenNum;
saveField("guildbossOpenNum", Integer.valueOf(guildbossOpenNum));
}

public int getLnlevel() {
return this.lnlevel;
} public void setLnlevel(int lnlevel) {
if (lnlevel == this.lnlevel)
return; 
this.lnlevel = lnlevel;
}
public void saveLnlevel(int lnlevel) {
if (lnlevel == this.lnlevel)
return; 
this.lnlevel = lnlevel;
saveField("lnlevel", Integer.valueOf(lnlevel));
}

public int getLnexp() {
return this.lnexp;
} public void setLnexp(int lnexp) {
if (lnexp == this.lnexp)
return; 
this.lnexp = lnexp;
}
public void saveLnexp(int lnexp) {
if (lnexp == this.lnexp)
return; 
this.lnexp = lnexp;
saveField("lnexp", Integer.valueOf(lnexp));
}

public int getLnwarLevel() {
return this.lnwarLevel;
} public void setLnwarLevel(int lnwarLevel) {
if (lnwarLevel == this.lnwarLevel)
return; 
this.lnwarLevel = lnwarLevel;
}
public void saveLnwarLevel(int lnwarLevel) {
if (lnwarLevel == this.lnwarLevel)
return; 
this.lnwarLevel = lnwarLevel;
saveField("lnwarLevel", Integer.valueOf(lnwarLevel));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `level` = '").append(this.level).append("',");
sBuilder.append(" `exp` = '").append(this.exp).append("',");
sBuilder.append(" `historyExp` = '").append(this.historyExp).append("',");
sBuilder.append(" `name` = '").append((this.name == null) ? null : this.name.replace("'", "''")).append("',");
sBuilder.append(" `icon` = '").append(this.icon).append("',");
sBuilder.append(" `border` = '").append(this.border).append("',");
sBuilder.append(" `notice` = '").append((this.notice == null) ? null : this.notice.replace("'", "''")).append("',");
sBuilder.append(" `manifesto` = '").append((this.manifesto == null) ? null : this.manifesto.replace("'", "''")).append("',");
sBuilder.append(" `joinState` = '").append(this.joinState).append("',");
sBuilder.append(" `createTime` = '").append(this.createTime).append("',");
sBuilder.append(" `lastLoginTime` = '").append(this.lastLoginTime).append("',");
sBuilder.append(" `levelLimit` = '").append(this.levelLimit).append("',");
sBuilder.append(" `maxFightPower` = '").append(this.maxFightPower).append("',");
sBuilder.append(" `donate` = '").append(this.donate).append("',");
sBuilder.append(" `guildbossLevel` = '").append(this.guildbossLevel).append("',");
sBuilder.append(" `guildbossOpenNum` = '").append(this.guildbossOpenNum).append("',");
sBuilder.append(" `lnlevel` = '").append(this.lnlevel).append("',");
sBuilder.append(" `lnexp` = '").append(this.lnexp).append("',");
sBuilder.append(" `lnwarLevel` = '").append(this.lnwarLevel).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `guild` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`level` int(11) NOT NULL DEFAULT '0' COMMENT '帮会等级',`exp` int(11) NOT NULL DEFAULT '0' COMMENT '帮会经验',`historyExp` int(11) NOT NULL DEFAULT '0' COMMENT '历史帮会经验',`name` varchar(50) NOT NULL DEFAULT '' COMMENT '名称',`icon` int(11) NOT NULL DEFAULT '0' COMMENT '头像',`border` int(11) NOT NULL DEFAULT '0' COMMENT '边框',`notice` varchar(500) NOT NULL DEFAULT '' COMMENT '公告',`manifesto` varchar(500) NOT NULL DEFAULT '' COMMENT '宣言',`joinState` int(11) NOT NULL DEFAULT '0' COMMENT '加入状态',`createTime` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',`lastLoginTime` int(11) NOT NULL DEFAULT '0' COMMENT '公会玩家最近一次登录时间',`levelLimit` int(11) NOT NULL DEFAULT '0' COMMENT '加入等级限制',`maxFightPower` int(11) NOT NULL DEFAULT '0' COMMENT '历史最大战斗力',`donate` int(11) NOT NULL DEFAULT '0' COMMENT '历史最大贡献度',`guildbossLevel` int(11) NOT NULL DEFAULT '0' COMMENT '最大通关帮派副本等级',`guildbossOpenNum` int(11) NOT NULL DEFAULT '0' COMMENT '每日帮派副本开启数',`lnlevel` int(11) NOT NULL DEFAULT '0' COMMENT '龙女等级',`lnexp` int(11) NOT NULL DEFAULT '0' COMMENT '龙女经验',`lnwarLevel` int(11) NOT NULL DEFAULT '0' COMMENT '龙女战力档次',UNIQUE INDEX `name` (`name`),PRIMARY KEY (`id`)) COMMENT='公会信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

