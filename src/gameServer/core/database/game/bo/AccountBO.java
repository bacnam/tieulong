package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AccountBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "varchar(50)", fieldname = "open_id", comment = "平台用户openid")
private String open_id;
@DataBaseField(type = "varchar(128)", fieldname = "username", comment = "账号名")
private String username;
@DataBaseField(type = "varchar(32)", fieldname = "ip", comment = "当前登录IP")
private String ip;
@DataBaseField(type = "varchar(50)", fieldname = "mac", comment = "当前登录Mac地址")
private String mac;
@DataBaseField(type = "int(11)", fieldname = "loginTime", comment = "当前登录时间")
private int loginTime;
@DataBaseField(type = "varchar(32)", fieldname = "lastIp", comment = "上次登录IP")
private String lastIp;
@DataBaseField(type = "varchar(50)", fieldname = "lastMac", comment = "上次登录Mac")
private String lastMac;
@DataBaseField(type = "int(11)", fieldname = "lastLoginTime", comment = "上次登录日期")
private int lastLoginTime;
@DataBaseField(type = "int(11)", fieldname = "banExpiredTime", comment = "封号过期时间")
private int banExpiredTime;
@DataBaseField(type = "int(11)", fieldname = "lastServerId", comment = "最后登录的服务器ID")
private int lastServerId;
@DataBaseField(type = "varchar(30)", fieldname = "lastClientVersion", comment = "最后登录的客户端版本")
private String lastClientVersion;
@DataBaseField(type = "varchar(50)", fieldname = "adFrom", comment = "玩家渠道")
private String adFrom;
@DataBaseField(type = "varchar(50)", fieldname = "adFrom2", comment = "玩家二级渠道")
private String adFrom2;
@DataBaseField(type = "varchar(32)", fieldname = "regIp", comment = "注册IP")
private String regIp;
@DataBaseField(type = "varchar(50)", fieldname = "regMac", comment = "注册Mac地址")
private String regMac;
@DataBaseField(type = "int(11)", fieldname = "regTime", comment = "注册时间")
private int regTime;
@DataBaseField(type = "int(11)", fieldname = "createTime", comment = "创建时间")
private int createTime;
@DataBaseField(type = "varchar(500)", fieldname = "adid", comment = "设备ID")
private String adid;
@DataBaseField(type = "varchar(50)", fieldname = "nation", comment = "国家")
private String nation;

public AccountBO() {
this.id = 0L;
this.open_id = "";
this.username = "";
this.ip = "";
this.mac = "";
this.loginTime = 0;
this.lastIp = "";
this.lastMac = "";
this.lastLoginTime = 0;
this.banExpiredTime = 0;
this.lastServerId = 0;
this.lastClientVersion = "";
this.adFrom = "";
this.adFrom2 = "";
this.regIp = "";
this.regMac = "";
this.regTime = 0;
this.createTime = 0;
this.adid = "";
this.nation = "";
}

public AccountBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.open_id = rs.getString(2);
this.username = rs.getString(3);
this.ip = rs.getString(4);
this.mac = rs.getString(5);
this.loginTime = rs.getInt(6);
this.lastIp = rs.getString(7);
this.lastMac = rs.getString(8);
this.lastLoginTime = rs.getInt(9);
this.banExpiredTime = rs.getInt(10);
this.lastServerId = rs.getInt(11);
this.lastClientVersion = rs.getString(12);
this.adFrom = rs.getString(13);
this.adFrom2 = rs.getString(14);
this.regIp = rs.getString(15);
this.regMac = rs.getString(16);
this.regTime = rs.getInt(17);
this.createTime = rs.getInt(18);
this.adid = rs.getString(19);
this.nation = rs.getString(20);
}

public void getFromResultSet(ResultSet rs, List<AccountBO> list) throws Exception {
list.add(new AccountBO(rs));
}

public long getAsynTaskTag() {
return 0L;
}

public String getItemsName() {
return "`id`, `open_id`, `username`, `ip`, `mac`, `loginTime`, `lastIp`, `lastMac`, `lastLoginTime`, `banExpiredTime`, `lastServerId`, `lastClientVersion`, `adFrom`, `adFrom2`, `regIp`, `regMac`, `regTime`, `createTime`, `adid`, `nation`";
}

public String getTableName() {
return "`account`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append((this.open_id == null) ? null : this.open_id.replace("'", "''")).append("', ");
strBuf.append("'").append((this.username == null) ? null : this.username.replace("'", "''")).append("', ");
strBuf.append("'").append((this.ip == null) ? null : this.ip.replace("'", "''")).append("', ");
strBuf.append("'").append((this.mac == null) ? null : this.mac.replace("'", "''")).append("', ");
strBuf.append("'").append(this.loginTime).append("', ");
strBuf.append("'").append((this.lastIp == null) ? null : this.lastIp.replace("'", "''")).append("', ");
strBuf.append("'").append((this.lastMac == null) ? null : this.lastMac.replace("'", "''")).append("', ");
strBuf.append("'").append(this.lastLoginTime).append("', ");
strBuf.append("'").append(this.banExpiredTime).append("', ");
strBuf.append("'").append(this.lastServerId).append("', ");
strBuf.append("'").append((this.lastClientVersion == null) ? null : this.lastClientVersion.replace("'", "''")).append("', ");
strBuf.append("'").append((this.adFrom == null) ? null : this.adFrom.replace("'", "''")).append("', ");
strBuf.append("'").append((this.adFrom2 == null) ? null : this.adFrom2.replace("'", "''")).append("', ");
strBuf.append("'").append((this.regIp == null) ? null : this.regIp.replace("'", "''")).append("', ");
strBuf.append("'").append((this.regMac == null) ? null : this.regMac.replace("'", "''")).append("', ");
strBuf.append("'").append(this.regTime).append("', ");
strBuf.append("'").append(this.createTime).append("', ");
strBuf.append("'").append((this.adid == null) ? null : this.adid.replace("'", "''")).append("', ");
strBuf.append("'").append((this.nation == null) ? null : this.nation.replace("'", "''")).append("', ");
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

public String getOpenId() {
return this.open_id;
} public void setOpenId(String open_id) {
if (open_id.equals(this.open_id))
return; 
this.open_id = open_id;
}
public void saveOpenId(String open_id) {
if (open_id.equals(this.open_id))
return; 
this.open_id = open_id;
saveField("open_id", open_id);
}

public String getUsername() {
return this.username;
} public void setUsername(String username) {
if (username.equals(this.username))
return; 
this.username = username;
}
public void saveUsername(String username) {
if (username.equals(this.username))
return; 
this.username = username;
saveField("username", username);
}

public String getIp() {
return this.ip;
} public void setIp(String ip) {
if (ip.equals(this.ip))
return; 
this.ip = ip;
}
public void saveIp(String ip) {
if (ip.equals(this.ip))
return; 
this.ip = ip;
saveField("ip", ip);
}

public String getMac() {
return this.mac;
} public void setMac(String mac) {
if (mac.equals(this.mac))
return; 
this.mac = mac;
}
public void saveMac(String mac) {
if (mac.equals(this.mac))
return; 
this.mac = mac;
saveField("mac", mac);
}

public int getLoginTime() {
return this.loginTime;
} public void setLoginTime(int loginTime) {
if (loginTime == this.loginTime)
return; 
this.loginTime = loginTime;
}
public void saveLoginTime(int loginTime) {
if (loginTime == this.loginTime)
return; 
this.loginTime = loginTime;
saveField("loginTime", Integer.valueOf(loginTime));
}

public String getLastIp() {
return this.lastIp;
} public void setLastIp(String lastIp) {
if (lastIp.equals(this.lastIp))
return; 
this.lastIp = lastIp;
}
public void saveLastIp(String lastIp) {
if (lastIp.equals(this.lastIp))
return; 
this.lastIp = lastIp;
saveField("lastIp", lastIp);
}

public String getLastMac() {
return this.lastMac;
} public void setLastMac(String lastMac) {
if (lastMac.equals(this.lastMac))
return; 
this.lastMac = lastMac;
}
public void saveLastMac(String lastMac) {
if (lastMac.equals(this.lastMac))
return; 
this.lastMac = lastMac;
saveField("lastMac", lastMac);
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

public int getBanExpiredTime() {
return this.banExpiredTime;
} public void setBanExpiredTime(int banExpiredTime) {
if (banExpiredTime == this.banExpiredTime)
return; 
this.banExpiredTime = banExpiredTime;
}
public void saveBanExpiredTime(int banExpiredTime) {
if (banExpiredTime == this.banExpiredTime)
return; 
this.banExpiredTime = banExpiredTime;
saveField("banExpiredTime", Integer.valueOf(banExpiredTime));
}

public int getLastServerId() {
return this.lastServerId;
} public void setLastServerId(int lastServerId) {
if (lastServerId == this.lastServerId)
return; 
this.lastServerId = lastServerId;
}
public void saveLastServerId(int lastServerId) {
if (lastServerId == this.lastServerId)
return; 
this.lastServerId = lastServerId;
saveField("lastServerId", Integer.valueOf(lastServerId));
}

public String getLastClientVersion() {
return this.lastClientVersion;
} public void setLastClientVersion(String lastClientVersion) {
if (lastClientVersion.equals(this.lastClientVersion))
return; 
this.lastClientVersion = lastClientVersion;
}
public void saveLastClientVersion(String lastClientVersion) {
if (lastClientVersion.equals(this.lastClientVersion))
return; 
this.lastClientVersion = lastClientVersion;
saveField("lastClientVersion", lastClientVersion);
}

public String getAdFrom() {
return this.adFrom;
} public void setAdFrom(String adFrom) {
if (adFrom.equals(this.adFrom))
return; 
this.adFrom = adFrom;
}
public void saveAdFrom(String adFrom) {
if (adFrom.equals(this.adFrom))
return; 
this.adFrom = adFrom;
saveField("adFrom", adFrom);
}

public String getAdFrom2() {
return this.adFrom2;
} public void setAdFrom2(String adFrom2) {
if (adFrom2.equals(this.adFrom2))
return; 
this.adFrom2 = adFrom2;
}
public void saveAdFrom2(String adFrom2) {
if (adFrom2.equals(this.adFrom2))
return; 
this.adFrom2 = adFrom2;
saveField("adFrom2", adFrom2);
}

public String getRegIp() {
return this.regIp;
} public void setRegIp(String regIp) {
if (regIp.equals(this.regIp))
return; 
this.regIp = regIp;
}
public void saveRegIp(String regIp) {
if (regIp.equals(this.regIp))
return; 
this.regIp = regIp;
saveField("regIp", regIp);
}

public String getRegMac() {
return this.regMac;
} public void setRegMac(String regMac) {
if (regMac.equals(this.regMac))
return; 
this.regMac = regMac;
}
public void saveRegMac(String regMac) {
if (regMac.equals(this.regMac))
return; 
this.regMac = regMac;
saveField("regMac", regMac);
}

public int getRegTime() {
return this.regTime;
} public void setRegTime(int regTime) {
if (regTime == this.regTime)
return; 
this.regTime = regTime;
}
public void saveRegTime(int regTime) {
if (regTime == this.regTime)
return; 
this.regTime = regTime;
saveField("regTime", Integer.valueOf(regTime));
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

public String getAdid() {
return this.adid;
} public void setAdid(String adid) {
if (adid.equals(this.adid))
return; 
this.adid = adid;
}
public void saveAdid(String adid) {
if (adid.equals(this.adid))
return; 
this.adid = adid;
saveField("adid", adid);
}

public String getNation() {
return this.nation;
} public void setNation(String nation) {
if (nation.equals(this.nation))
return; 
this.nation = nation;
}
public void saveNation(String nation) {
if (nation.equals(this.nation))
return; 
this.nation = nation;
saveField("nation", nation);
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `open_id` = '").append((this.open_id == null) ? null : this.open_id.replace("'", "''")).append("',");
sBuilder.append(" `username` = '").append((this.username == null) ? null : this.username.replace("'", "''")).append("',");
sBuilder.append(" `ip` = '").append((this.ip == null) ? null : this.ip.replace("'", "''")).append("',");
sBuilder.append(" `mac` = '").append((this.mac == null) ? null : this.mac.replace("'", "''")).append("',");
sBuilder.append(" `loginTime` = '").append(this.loginTime).append("',");
sBuilder.append(" `lastIp` = '").append((this.lastIp == null) ? null : this.lastIp.replace("'", "''")).append("',");
sBuilder.append(" `lastMac` = '").append((this.lastMac == null) ? null : this.lastMac.replace("'", "''")).append("',");
sBuilder.append(" `lastLoginTime` = '").append(this.lastLoginTime).append("',");
sBuilder.append(" `banExpiredTime` = '").append(this.banExpiredTime).append("',");
sBuilder.append(" `lastServerId` = '").append(this.lastServerId).append("',");
sBuilder.append(" `lastClientVersion` = '").append((this.lastClientVersion == null) ? null : this.lastClientVersion.replace("'", "''")).append("',");
sBuilder.append(" `adFrom` = '").append((this.adFrom == null) ? null : this.adFrom.replace("'", "''")).append("',");
sBuilder.append(" `adFrom2` = '").append((this.adFrom2 == null) ? null : this.adFrom2.replace("'", "''")).append("',");
sBuilder.append(" `regIp` = '").append((this.regIp == null) ? null : this.regIp.replace("'", "''")).append("',");
sBuilder.append(" `regMac` = '").append((this.regMac == null) ? null : this.regMac.replace("'", "''")).append("',");
sBuilder.append(" `regTime` = '").append(this.regTime).append("',");
sBuilder.append(" `createTime` = '").append(this.createTime).append("',");
sBuilder.append(" `adid` = '").append((this.adid == null) ? null : this.adid.replace("'", "''")).append("',");
sBuilder.append(" `nation` = '").append((this.nation == null) ? null : this.nation.replace("'", "''")).append("',");
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `account` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`open_id` varchar(50) NOT NULL DEFAULT '' COMMENT '平台用户openid',`username` varchar(128) NOT NULL DEFAULT '' COMMENT '账号名',`ip` varchar(32) NOT NULL DEFAULT '' COMMENT '当前登录IP',`mac` varchar(50) NOT NULL DEFAULT '' COMMENT '当前登录Mac地址',`loginTime` int(11) NOT NULL DEFAULT '0' COMMENT '当前登录时间',`lastIp` varchar(32) NOT NULL DEFAULT '' COMMENT '上次登录IP',`lastMac` varchar(50) NOT NULL DEFAULT '' COMMENT '上次登录Mac',`lastLoginTime` int(11) NOT NULL DEFAULT '0' COMMENT '上次登录日期',`banExpiredTime` int(11) NOT NULL DEFAULT '0' COMMENT '封号过期时间',`lastServerId` int(11) NOT NULL DEFAULT '0' COMMENT '最后登录的服务器ID',`lastClientVersion` varchar(30) NOT NULL DEFAULT '' COMMENT '最后登录的客户端版本',`adFrom` varchar(50) NOT NULL DEFAULT '' COMMENT '玩家渠道',`adFrom2` varchar(50) NOT NULL DEFAULT '' COMMENT '玩家二级渠道',`regIp` varchar(32) NOT NULL DEFAULT '' COMMENT '注册IP',`regMac` varchar(50) NOT NULL DEFAULT '' COMMENT '注册Mac地址',`regTime` int(11) NOT NULL DEFAULT '0' COMMENT '注册时间',`createTime` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',`adid` varchar(500) NOT NULL DEFAULT '' COMMENT '设备ID',`nation` varchar(50) NOT NULL DEFAULT '' COMMENT '国家',PRIMARY KEY (`id`)) COMMENT='玩家账号信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

