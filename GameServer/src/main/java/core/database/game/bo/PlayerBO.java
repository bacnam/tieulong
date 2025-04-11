package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PlayerBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "varchar(50)", fieldname = "open_id", comment = "玩家账号ID")
    private String open_id;
    @DataBaseField(type = "int(11)", fieldname = "sid", comment = "服务器id")
    private int sid;
    @DataBaseField(type = "varchar(50)", fieldname = "name", comment = "玩家名称/昵称")
    private String name;
    @DataBaseField(type = "int(11)", fieldname = "icon", comment = "头像")
    private int icon;
    @DataBaseField(type = "int(11)", fieldname = "maxFightPower", comment = "最大战斗力")
    private int maxFightPower;
    @DataBaseField(type = "int(11)", fieldname = "gmLevel", comment = "gm权限")
    private int gmLevel;
    @DataBaseField(type = "int(11)", fieldname = "cheatTimes", comment = "作弊次数")
    private int cheatTimes;
    @DataBaseField(type = "int(11)", fieldname = "banned_ChatExpiredTime", comment = "禁言过期时间")
    private int banned_ChatExpiredTime;
    @DataBaseField(type = "int(11)", fieldname = "banned_LoginExpiredTime", comment = "禁登过期时间")
    private int banned_LoginExpiredTime;
    @DataBaseField(type = "int(11)", fieldname = "bannedTimes", comment = "封号次数")
    private int bannedTimes;
    @DataBaseField(type = "int(11)", fieldname = "lastLogin", comment = "最近一次登陆時間")
    private int lastLogin;
    @DataBaseField(type = "int(11)", fieldname = "lastLogout", comment = "最近一次登出時間")
    private int lastLogout;
    @DataBaseField(type = "int(11)", fieldname = "online_update", comment = "在线更新时间")
    private int online_update;
    @DataBaseField(type = "int(11)", fieldname = "totalRecharge", comment = "累计充值, 统计玩家充值RMB")
    private int totalRecharge;
    @DataBaseField(type = "int(11)", fieldname = "vipExp", comment = "vip经验")
    private int vipExp;
    @DataBaseField(type = "int(11)", fieldname = "vipLevel", comment = "vip等级")
    private int vipLevel;
    @DataBaseField(type = "int(11)", fieldname = "lv", comment = "等级")
    private int lv;
    @DataBaseField(type = "int(11)", fieldname = "exp", comment = "队伍经验")
    private int exp;
    @DataBaseField(type = "int(11)", fieldname = "crystal", comment = "水晶数量")
    private int crystal;
    @DataBaseField(type = "int(11)", fieldname = "gold", comment = "金币")
    private int gold;
    @DataBaseField(type = "int(11)", fieldname = "dungeon_lv", comment = "副本等级")
    private int dungeon_lv;
    @DataBaseField(type = "int(11)", fieldname = "dungeon_time", comment = "上次打副本时间")
    private int dungeon_time;
    @DataBaseField(type = "int(11)", fieldname = "ext_package", comment = "额外背包容量")
    private int ext_package;
    @DataBaseField(type = "int(11)", fieldname = "createTime", comment = "账号创建时间,单位秒")
    private int createTime;
    @DataBaseField(type = "bigint(20)", fieldname = "GmMailCheckId", comment = "Gm邮件领取编号")
    private long GmMailCheckId;
    @DataBaseField(type = "int(11)", fieldname = "strengthen_material", comment = "强化材料")
    private int strengthen_material;
    @DataBaseField(type = "int(11)", fieldname = "gem_material", comment = "宝石材料")
    private int gem_material;
    @DataBaseField(type = "int(11)", fieldname = "star_material", comment = "升星材料")
    private int star_material;
    @DataBaseField(type = "int(11)", fieldname = "mer_material", comment = "经脉材料")
    private int mer_material;
    @DataBaseField(type = "int(11)", fieldname = "wing_material", comment = "翅膀材料")
    private int wing_material;
    @DataBaseField(type = "int(11)", fieldname = "arena_token", comment = "竞技场代币")
    private int arena_token;
    @DataBaseField(type = "int(11)", fieldname = "equip_instance_material", comment = "装备副本代币")
    private int equip_instance_material;
    @DataBaseField(type = "int(11)", fieldname = "gem_instance_material", comment = "宝石副本代币")
    private int gem_instance_material;
    @DataBaseField(type = "int(11)", fieldname = "meridian_instance_material", comment = "经脉副本代币")
    private int meridian_instance_material;
    @DataBaseField(type = "int(11)", fieldname = "red_piece", comment = "红装碎片")
    private int red_piece;
    @DataBaseField(type = "int(11)", fieldname = "artifice_material", comment = "炼化石")
    private int artifice_material;
    @DataBaseField(type = "int(11)", fieldname = "lottery", comment = "奖券")
    private int lottery;
    @DataBaseField(type = "int(11)", fieldname = "warspirit_talent_material", comment = "战灵天赋材料")
    private int warspirit_talent_material;
    @DataBaseField(type = "int(11)", fieldname = "warspirit_lv_material", comment = "战灵等级材料")
    private int warspirit_lv_material;
    @DataBaseField(type = "int(11)", fieldname = "warspirit_lv", comment = "战灵等级")
    private int warspirit_lv;
    @DataBaseField(type = "int(11)", fieldname = "warspirit_exp", comment = "战灵经验")
    private int warspirit_exp;
    @DataBaseField(type = "int(11)", fieldname = "warspirit_talent", comment = "战灵天赋")
    private int warspirit_talent;
    @DataBaseField(type = "int(11)", fieldname = "dress_material", comment = "时装材料")
    private int dress_material;
    @DataBaseField(type = "int(11)", fieldname = "exp_material", comment = "经验材料")
    private int exp_material;

    public PlayerBO() {
        this.id = 0L;
        this.open_id = "";
        this.sid = 0;
        this.name = "";
        this.icon = 0;
        this.maxFightPower = 0;
        this.gmLevel = 0;
        this.cheatTimes = 0;
        this.banned_ChatExpiredTime = 0;
        this.banned_LoginExpiredTime = 0;
        this.bannedTimes = 0;
        this.lastLogin = 0;
        this.lastLogout = 0;
        this.online_update = 0;
        this.totalRecharge = 0;
        this.vipExp = 0;
        this.vipLevel = 0;
        this.lv = 0;
        this.exp = 0;
        this.crystal = 0;
        this.gold = 0;
        this.dungeon_lv = 0;
        this.dungeon_time = 0;
        this.ext_package = 0;
        this.createTime = 0;
        this.GmMailCheckId = 0L;
        this.strengthen_material = 0;
        this.gem_material = 0;
        this.star_material = 0;
        this.mer_material = 0;
        this.wing_material = 0;
        this.arena_token = 0;
        this.equip_instance_material = 0;
        this.gem_instance_material = 0;
        this.meridian_instance_material = 0;
        this.red_piece = 0;
        this.artifice_material = 0;
        this.lottery = 0;
        this.warspirit_talent_material = 0;
        this.warspirit_lv_material = 0;
        this.warspirit_lv = 0;
        this.warspirit_exp = 0;
        this.warspirit_talent = 0;
        this.dress_material = 0;
        this.exp_material = 0;
    }

    public PlayerBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.open_id = rs.getString(2);
        this.sid = rs.getInt(3);
        this.name = rs.getString(4);
        this.icon = rs.getInt(5);
        this.maxFightPower = rs.getInt(6);
        this.gmLevel = rs.getInt(7);
        this.cheatTimes = rs.getInt(8);
        this.banned_ChatExpiredTime = rs.getInt(9);
        this.banned_LoginExpiredTime = rs.getInt(10);
        this.bannedTimes = rs.getInt(11);
        this.lastLogin = rs.getInt(12);
        this.lastLogout = rs.getInt(13);
        this.online_update = rs.getInt(14);
        this.totalRecharge = rs.getInt(15);
        this.vipExp = rs.getInt(16);
        this.vipLevel = rs.getInt(17);
        this.lv = rs.getInt(18);
        this.exp = rs.getInt(19);
        this.crystal = rs.getInt(20);
        this.gold = rs.getInt(21);
        this.dungeon_lv = rs.getInt(22);
        this.dungeon_time = rs.getInt(23);
        this.ext_package = rs.getInt(24);
        this.createTime = rs.getInt(25);
        this.GmMailCheckId = rs.getLong(26);
        this.strengthen_material = rs.getInt(27);
        this.gem_material = rs.getInt(28);
        this.star_material = rs.getInt(29);
        this.mer_material = rs.getInt(30);
        this.wing_material = rs.getInt(31);
        this.arena_token = rs.getInt(32);
        this.equip_instance_material = rs.getInt(33);
        this.gem_instance_material = rs.getInt(34);
        this.meridian_instance_material = rs.getInt(35);
        this.red_piece = rs.getInt(36);
        this.artifice_material = rs.getInt(37);
        this.lottery = rs.getInt(38);
        this.warspirit_talent_material = rs.getInt(39);
        this.warspirit_lv_material = rs.getInt(40);
        this.warspirit_lv = rs.getInt(41);
        this.warspirit_exp = rs.getInt(42);
        this.warspirit_talent = rs.getInt(43);
        this.dress_material = rs.getInt(44);
        this.exp_material = rs.getInt(45);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `player` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`open_id` varchar(50) NOT NULL DEFAULT '' COMMENT '玩家账号ID',`sid` int(11) NOT NULL DEFAULT '0' COMMENT '服务器id',`name` varchar(50) NOT NULL DEFAULT '' COMMENT '玩家名称/昵称',`icon` int(11) NOT NULL DEFAULT '0' COMMENT '头像',`maxFightPower` int(11) NOT NULL DEFAULT '0' COMMENT '最大战斗力',`gmLevel` int(11) NOT NULL DEFAULT '0' COMMENT 'gm权限',`cheatTimes` int(11) NOT NULL DEFAULT '0' COMMENT '作弊次数',`banned_ChatExpiredTime` int(11) NOT NULL DEFAULT '0' COMMENT '禁言过期时间',`banned_LoginExpiredTime` int(11) NOT NULL DEFAULT '0' COMMENT '禁登过期时间',`bannedTimes` int(11) NOT NULL DEFAULT '0' COMMENT '封号次数',`lastLogin` int(11) NOT NULL DEFAULT '0' COMMENT '最近一次登陆時間',`lastLogout` int(11) NOT NULL DEFAULT '0' COMMENT '最近一次登出時間',`online_update` int(11) NOT NULL DEFAULT '0' COMMENT '在线更新时间',`totalRecharge` int(11) NOT NULL DEFAULT '0' COMMENT '累计充值, 统计玩家充值RMB',`vipExp` int(11) NOT NULL DEFAULT '0' COMMENT 'vip经验',`vipLevel` int(11) NOT NULL DEFAULT '0' COMMENT 'vip等级',`lv` int(11) NOT NULL DEFAULT '0' COMMENT '等级',`exp` int(11) NOT NULL DEFAULT '0' COMMENT '队伍经验',`crystal` int(11) NOT NULL DEFAULT '0' COMMENT '水晶数量',`gold` int(11) NOT NULL DEFAULT '0' COMMENT '金币',`dungeon_lv` int(11) NOT NULL DEFAULT '0' COMMENT '副本等级',`dungeon_time` int(11) NOT NULL DEFAULT '0' COMMENT '上次打副本时间',`ext_package` int(11) NOT NULL DEFAULT '0' COMMENT '额外背包容量',`createTime` int(11) NOT NULL DEFAULT '0' COMMENT '账号创建时间,单位秒',`GmMailCheckId` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Gm邮件领取编号',`strengthen_material` int(11) NOT NULL DEFAULT '0' COMMENT '强化材料',`gem_material` int(11) NOT NULL DEFAULT '0' COMMENT '宝石材料',`star_material` int(11) NOT NULL DEFAULT '0' COMMENT '升星材料',`mer_material` int(11) NOT NULL DEFAULT '0' COMMENT '经脉材料',`wing_material` int(11) NOT NULL DEFAULT '0' COMMENT '翅膀材料',`arena_token` int(11) NOT NULL DEFAULT '0' COMMENT '竞技场代币',`equip_instance_material` int(11) NOT NULL DEFAULT '0' COMMENT '装备副本代币',`gem_instance_material` int(11) NOT NULL DEFAULT '0' COMMENT '宝石副本代币',`meridian_instance_material` int(11) NOT NULL DEFAULT '0' COMMENT '经脉副本代币',`red_piece` int(11) NOT NULL DEFAULT '0' COMMENT '红装碎片',`artifice_material` int(11) NOT NULL DEFAULT '0' COMMENT '炼化石',`lottery` int(11) NOT NULL DEFAULT '0' COMMENT '奖券',`warspirit_talent_material` int(11) NOT NULL DEFAULT '0' COMMENT '战灵天赋材料',`warspirit_lv_material` int(11) NOT NULL DEFAULT '0' COMMENT '战灵等级材料',`warspirit_lv` int(11) NOT NULL DEFAULT '0' COMMENT '战灵等级',`warspirit_exp` int(11) NOT NULL DEFAULT '0' COMMENT '战灵经验',`warspirit_talent` int(11) NOT NULL DEFAULT '0' COMMENT '战灵天赋',`dress_material` int(11) NOT NULL DEFAULT '0' COMMENT '时装材料',`exp_material` int(11) NOT NULL DEFAULT '0' COMMENT '经验材料',KEY `open_id` (`open_id`),UNIQUE INDEX `name` (`name`),PRIMARY KEY (`id`)) COMMENT='玩家信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<PlayerBO> list) throws Exception {
        list.add(new PlayerBO(rs));
    }

    public long getAsynTaskTag() {
        return getId();
    }

    public String getItemsName() {
        return "`id`, `open_id`, `sid`, `name`, `icon`, `maxFightPower`, `gmLevel`, `cheatTimes`, `banned_ChatExpiredTime`, `banned_LoginExpiredTime`, `bannedTimes`, `lastLogin`, `lastLogout`, `online_update`, `totalRecharge`, `vipExp`, `vipLevel`, `lv`, `exp`, `crystal`, `gold`, `dungeon_lv`, `dungeon_time`, `ext_package`, `createTime`, `GmMailCheckId`, `strengthen_material`, `gem_material`, `star_material`, `mer_material`, `wing_material`, `arena_token`, `equip_instance_material`, `gem_instance_material`, `meridian_instance_material`, `red_piece`, `artifice_material`, `lottery`, `warspirit_talent_material`, `warspirit_lv_material`, `warspirit_lv`, `warspirit_exp`, `warspirit_talent`, `dress_material`, `exp_material`";
    }

    public String getTableName() {
        return "`player`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append((this.open_id == null) ? null : this.open_id.replace("'", "''")).append("', ");
        strBuf.append("'").append(this.sid).append("', ");
        strBuf.append("'").append((this.name == null) ? null : this.name.replace("'", "''")).append("', ");
        strBuf.append("'").append(this.icon).append("', ");
        strBuf.append("'").append(this.maxFightPower).append("', ");
        strBuf.append("'").append(this.gmLevel).append("', ");
        strBuf.append("'").append(this.cheatTimes).append("', ");
        strBuf.append("'").append(this.banned_ChatExpiredTime).append("', ");
        strBuf.append("'").append(this.banned_LoginExpiredTime).append("', ");
        strBuf.append("'").append(this.bannedTimes).append("', ");
        strBuf.append("'").append(this.lastLogin).append("', ");
        strBuf.append("'").append(this.lastLogout).append("', ");
        strBuf.append("'").append(this.online_update).append("', ");
        strBuf.append("'").append(this.totalRecharge).append("', ");
        strBuf.append("'").append(this.vipExp).append("', ");
        strBuf.append("'").append(this.vipLevel).append("', ");
        strBuf.append("'").append(this.lv).append("', ");
        strBuf.append("'").append(this.exp).append("', ");
        strBuf.append("'").append(this.crystal).append("', ");
        strBuf.append("'").append(this.gold).append("', ");
        strBuf.append("'").append(this.dungeon_lv).append("', ");
        strBuf.append("'").append(this.dungeon_time).append("', ");
        strBuf.append("'").append(this.ext_package).append("', ");
        strBuf.append("'").append(this.createTime).append("', ");
        strBuf.append("'").append(this.GmMailCheckId).append("', ");
        strBuf.append("'").append(this.strengthen_material).append("', ");
        strBuf.append("'").append(this.gem_material).append("', ");
        strBuf.append("'").append(this.star_material).append("', ");
        strBuf.append("'").append(this.mer_material).append("', ");
        strBuf.append("'").append(this.wing_material).append("', ");
        strBuf.append("'").append(this.arena_token).append("', ");
        strBuf.append("'").append(this.equip_instance_material).append("', ");
        strBuf.append("'").append(this.gem_instance_material).append("', ");
        strBuf.append("'").append(this.meridian_instance_material).append("', ");
        strBuf.append("'").append(this.red_piece).append("', ");
        strBuf.append("'").append(this.artifice_material).append("', ");
        strBuf.append("'").append(this.lottery).append("', ");
        strBuf.append("'").append(this.warspirit_talent_material).append("', ");
        strBuf.append("'").append(this.warspirit_lv_material).append("', ");
        strBuf.append("'").append(this.warspirit_lv).append("', ");
        strBuf.append("'").append(this.warspirit_exp).append("', ");
        strBuf.append("'").append(this.warspirit_talent).append("', ");
        strBuf.append("'").append(this.dress_material).append("', ");
        strBuf.append("'").append(this.exp_material).append("', ");
        strBuf.deleteCharAt(strBuf.length() - 2);
        return strBuf.toString();
    }

    public ArrayList<byte[]> getInsertValueBytes() {
        ArrayList<byte[]> ret = (ArrayList) new ArrayList<>();
        return ret;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long iID) {
        this.id = iID;
    }

    public String getOpenId() {
        return this.open_id;
    }

    public void setOpenId(String open_id) {
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

    public int getSid() {
        return this.sid;
    }

    public void setSid(int sid) {
        if (sid == this.sid)
            return;
        this.sid = sid;
    }

    public void saveSid(int sid) {
        if (sid == this.sid)
            return;
        this.sid = sid;
        saveField("sid", Integer.valueOf(sid));
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
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
    }

    public void setIcon(int icon) {
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

    public int getMaxFightPower() {
        return this.maxFightPower;
    }

    public void setMaxFightPower(int maxFightPower) {
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

    public int getGmLevel() {
        return this.gmLevel;
    }

    public void setGmLevel(int gmLevel) {
        if (gmLevel == this.gmLevel)
            return;
        this.gmLevel = gmLevel;
    }

    public void saveGmLevel(int gmLevel) {
        if (gmLevel == this.gmLevel)
            return;
        this.gmLevel = gmLevel;
        saveField("gmLevel", Integer.valueOf(gmLevel));
    }

    public int getCheatTimes() {
        return this.cheatTimes;
    }

    public void setCheatTimes(int cheatTimes) {
        if (cheatTimes == this.cheatTimes)
            return;
        this.cheatTimes = cheatTimes;
    }

    public void saveCheatTimes(int cheatTimes) {
        if (cheatTimes == this.cheatTimes)
            return;
        this.cheatTimes = cheatTimes;
        saveField("cheatTimes", Integer.valueOf(cheatTimes));
    }

    public int getBannedChatExpiredTime() {
        return this.banned_ChatExpiredTime;
    }

    public void setBannedChatExpiredTime(int banned_ChatExpiredTime) {
        if (banned_ChatExpiredTime == this.banned_ChatExpiredTime)
            return;
        this.banned_ChatExpiredTime = banned_ChatExpiredTime;
    }

    public void saveBannedChatExpiredTime(int banned_ChatExpiredTime) {
        if (banned_ChatExpiredTime == this.banned_ChatExpiredTime)
            return;
        this.banned_ChatExpiredTime = banned_ChatExpiredTime;
        saveField("banned_ChatExpiredTime", Integer.valueOf(banned_ChatExpiredTime));
    }

    public int getBannedLoginExpiredTime() {
        return this.banned_LoginExpiredTime;
    }

    public void setBannedLoginExpiredTime(int banned_LoginExpiredTime) {
        if (banned_LoginExpiredTime == this.banned_LoginExpiredTime)
            return;
        this.banned_LoginExpiredTime = banned_LoginExpiredTime;
    }

    public void saveBannedLoginExpiredTime(int banned_LoginExpiredTime) {
        if (banned_LoginExpiredTime == this.banned_LoginExpiredTime)
            return;
        this.banned_LoginExpiredTime = banned_LoginExpiredTime;
        saveField("banned_LoginExpiredTime", Integer.valueOf(banned_LoginExpiredTime));
    }

    public int getBannedTimes() {
        return this.bannedTimes;
    }

    public void setBannedTimes(int bannedTimes) {
        if (bannedTimes == this.bannedTimes)
            return;
        this.bannedTimes = bannedTimes;
    }

    public void saveBannedTimes(int bannedTimes) {
        if (bannedTimes == this.bannedTimes)
            return;
        this.bannedTimes = bannedTimes;
        saveField("bannedTimes", Integer.valueOf(bannedTimes));
    }

    public int getLastLogin() {
        return this.lastLogin;
    }

    public void setLastLogin(int lastLogin) {
        if (lastLogin == this.lastLogin)
            return;
        this.lastLogin = lastLogin;
    }

    public void saveLastLogin(int lastLogin) {
        if (lastLogin == this.lastLogin)
            return;
        this.lastLogin = lastLogin;
        saveField("lastLogin", Integer.valueOf(lastLogin));
    }

    public int getLastLogout() {
        return this.lastLogout;
    }

    public void setLastLogout(int lastLogout) {
        if (lastLogout == this.lastLogout)
            return;
        this.lastLogout = lastLogout;
    }

    public void saveLastLogout(int lastLogout) {
        if (lastLogout == this.lastLogout)
            return;
        this.lastLogout = lastLogout;
        saveField("lastLogout", Integer.valueOf(lastLogout));
    }

    public int getOnlineUpdate() {
        return this.online_update;
    }

    public void setOnlineUpdate(int online_update) {
        if (online_update == this.online_update)
            return;
        this.online_update = online_update;
    }

    public void saveOnlineUpdate(int online_update) {
        if (online_update == this.online_update)
            return;
        this.online_update = online_update;
        saveField("online_update", Integer.valueOf(online_update));
    }

    public int getTotalRecharge() {
        return this.totalRecharge;
    }

    public void setTotalRecharge(int totalRecharge) {
        if (totalRecharge == this.totalRecharge)
            return;
        this.totalRecharge = totalRecharge;
    }

    public void saveTotalRecharge(int totalRecharge) {
        if (totalRecharge == this.totalRecharge)
            return;
        this.totalRecharge = totalRecharge;
        saveField("totalRecharge", Integer.valueOf(totalRecharge));
    }

    public int getVipExp() {
        return this.vipExp;
    }

    public void setVipExp(int vipExp) {
        if (vipExp == this.vipExp)
            return;
        this.vipExp = vipExp;
    }

    public void saveVipExp(int vipExp) {
        if (vipExp == this.vipExp)
            return;
        this.vipExp = vipExp;
        saveField("vipExp", Integer.valueOf(vipExp));
    }

    public int getVipLevel() {
        return this.vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        if (vipLevel == this.vipLevel)
            return;
        this.vipLevel = vipLevel;
    }

    public void saveVipLevel(int vipLevel) {
        if (vipLevel == this.vipLevel)
            return;
        this.vipLevel = vipLevel;
        saveField("vipLevel", Integer.valueOf(vipLevel));
    }

    public int getLv() {
        return this.lv;
    }

    public void setLv(int lv) {
        if (lv == this.lv)
            return;
        this.lv = lv;
    }

    public void saveLv(int lv) {
        if (lv == this.lv)
            return;
        this.lv = lv;
        saveField("lv", Integer.valueOf(lv));
    }

    public int getExp() {
        return this.exp;
    }

    public void setExp(int exp) {
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

    public int getCrystal() {
        return this.crystal;
    }

    public void setCrystal(int crystal) {
        if (crystal == this.crystal)
            return;
        this.crystal = crystal;
    }

    public void saveCrystal(int crystal) {
        if (crystal == this.crystal)
            return;
        this.crystal = crystal;
        saveField("crystal", Integer.valueOf(crystal));
    }

    public int getGold() {
        return this.gold;
    }

    public void setGold(int gold) {
        if (gold == this.gold)
            return;
        this.gold = gold;
    }

    public void saveGold(int gold) {
        if (gold == this.gold)
            return;
        this.gold = gold;
        saveField("gold", Integer.valueOf(gold));
    }

    public int getDungeonLv() {
        return this.dungeon_lv;
    }

    public void setDungeonLv(int dungeon_lv) {
        if (dungeon_lv == this.dungeon_lv)
            return;
        this.dungeon_lv = dungeon_lv;
    }

    public void saveDungeonLv(int dungeon_lv) {
        if (dungeon_lv == this.dungeon_lv)
            return;
        this.dungeon_lv = dungeon_lv;
        saveField("dungeon_lv", Integer.valueOf(dungeon_lv));
    }

    public int getDungeonTime() {
        return this.dungeon_time;
    }

    public void setDungeonTime(int dungeon_time) {
        if (dungeon_time == this.dungeon_time)
            return;
        this.dungeon_time = dungeon_time;
    }

    public void saveDungeonTime(int dungeon_time) {
        if (dungeon_time == this.dungeon_time)
            return;
        this.dungeon_time = dungeon_time;
        saveField("dungeon_time", Integer.valueOf(dungeon_time));
    }

    public int getExtPackage() {
        return this.ext_package;
    }

    public void setExtPackage(int ext_package) {
        if (ext_package == this.ext_package)
            return;
        this.ext_package = ext_package;
    }

    public void saveExtPackage(int ext_package) {
        if (ext_package == this.ext_package)
            return;
        this.ext_package = ext_package;
        saveField("ext_package", Integer.valueOf(ext_package));
    }

    public int getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(int createTime) {
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

    public long getGmMailCheckId() {
        return this.GmMailCheckId;
    }

    public void setGmMailCheckId(long GmMailCheckId) {
        if (GmMailCheckId == this.GmMailCheckId)
            return;
        this.GmMailCheckId = GmMailCheckId;
    }

    public void saveGmMailCheckId(long GmMailCheckId) {
        if (GmMailCheckId == this.GmMailCheckId)
            return;
        this.GmMailCheckId = GmMailCheckId;
        saveField("GmMailCheckId", Long.valueOf(GmMailCheckId));
    }

    public int getStrengthenMaterial() {
        return this.strengthen_material;
    }

    public void setStrengthenMaterial(int strengthen_material) {
        if (strengthen_material == this.strengthen_material)
            return;
        this.strengthen_material = strengthen_material;
    }

    public void saveStrengthenMaterial(int strengthen_material) {
        if (strengthen_material == this.strengthen_material)
            return;
        this.strengthen_material = strengthen_material;
        saveField("strengthen_material", Integer.valueOf(strengthen_material));
    }

    public int getGemMaterial() {
        return this.gem_material;
    }

    public void setGemMaterial(int gem_material) {
        if (gem_material == this.gem_material)
            return;
        this.gem_material = gem_material;
    }

    public void saveGemMaterial(int gem_material) {
        if (gem_material == this.gem_material)
            return;
        this.gem_material = gem_material;
        saveField("gem_material", Integer.valueOf(gem_material));
    }

    public int getStarMaterial() {
        return this.star_material;
    }

    public void setStarMaterial(int star_material) {
        if (star_material == this.star_material)
            return;
        this.star_material = star_material;
    }

    public void saveStarMaterial(int star_material) {
        if (star_material == this.star_material)
            return;
        this.star_material = star_material;
        saveField("star_material", Integer.valueOf(star_material));
    }

    public int getMerMaterial() {
        return this.mer_material;
    }

    public void setMerMaterial(int mer_material) {
        if (mer_material == this.mer_material)
            return;
        this.mer_material = mer_material;
    }

    public void saveMerMaterial(int mer_material) {
        if (mer_material == this.mer_material)
            return;
        this.mer_material = mer_material;
        saveField("mer_material", Integer.valueOf(mer_material));
    }

    public int getWingMaterial() {
        return this.wing_material;
    }

    public void setWingMaterial(int wing_material) {
        if (wing_material == this.wing_material)
            return;
        this.wing_material = wing_material;
    }

    public void saveWingMaterial(int wing_material) {
        if (wing_material == this.wing_material)
            return;
        this.wing_material = wing_material;
        saveField("wing_material", Integer.valueOf(wing_material));
    }

    public int getArenaToken() {
        return this.arena_token;
    }

    public void setArenaToken(int arena_token) {
        if (arena_token == this.arena_token)
            return;
        this.arena_token = arena_token;
    }

    public void saveArenaToken(int arena_token) {
        if (arena_token == this.arena_token)
            return;
        this.arena_token = arena_token;
        saveField("arena_token", Integer.valueOf(arena_token));
    }

    public int getEquipInstanceMaterial() {
        return this.equip_instance_material;
    }

    public void setEquipInstanceMaterial(int equip_instance_material) {
        if (equip_instance_material == this.equip_instance_material)
            return;
        this.equip_instance_material = equip_instance_material;
    }

    public void saveEquipInstanceMaterial(int equip_instance_material) {
        if (equip_instance_material == this.equip_instance_material)
            return;
        this.equip_instance_material = equip_instance_material;
        saveField("equip_instance_material", Integer.valueOf(equip_instance_material));
    }

    public int getGemInstanceMaterial() {
        return this.gem_instance_material;
    }

    public void setGemInstanceMaterial(int gem_instance_material) {
        if (gem_instance_material == this.gem_instance_material)
            return;
        this.gem_instance_material = gem_instance_material;
    }

    public void saveGemInstanceMaterial(int gem_instance_material) {
        if (gem_instance_material == this.gem_instance_material)
            return;
        this.gem_instance_material = gem_instance_material;
        saveField("gem_instance_material", Integer.valueOf(gem_instance_material));
    }

    public int getMeridianInstanceMaterial() {
        return this.meridian_instance_material;
    }

    public void setMeridianInstanceMaterial(int meridian_instance_material) {
        if (meridian_instance_material == this.meridian_instance_material)
            return;
        this.meridian_instance_material = meridian_instance_material;
    }

    public void saveMeridianInstanceMaterial(int meridian_instance_material) {
        if (meridian_instance_material == this.meridian_instance_material)
            return;
        this.meridian_instance_material = meridian_instance_material;
        saveField("meridian_instance_material", Integer.valueOf(meridian_instance_material));
    }

    public int getRedPiece() {
        return this.red_piece;
    }

    public void setRedPiece(int red_piece) {
        if (red_piece == this.red_piece)
            return;
        this.red_piece = red_piece;
    }

    public void saveRedPiece(int red_piece) {
        if (red_piece == this.red_piece)
            return;
        this.red_piece = red_piece;
        saveField("red_piece", Integer.valueOf(red_piece));
    }

    public int getArtificeMaterial() {
        return this.artifice_material;
    }

    public void setArtificeMaterial(int artifice_material) {
        if (artifice_material == this.artifice_material)
            return;
        this.artifice_material = artifice_material;
    }

    public void saveArtificeMaterial(int artifice_material) {
        if (artifice_material == this.artifice_material)
            return;
        this.artifice_material = artifice_material;
        saveField("artifice_material", Integer.valueOf(artifice_material));
    }

    public int getLottery() {
        return this.lottery;
    }

    public void setLottery(int lottery) {
        if (lottery == this.lottery)
            return;
        this.lottery = lottery;
    }

    public void saveLottery(int lottery) {
        if (lottery == this.lottery)
            return;
        this.lottery = lottery;
        saveField("lottery", Integer.valueOf(lottery));
    }

    public int getWarspiritTalentMaterial() {
        return this.warspirit_talent_material;
    }

    public void setWarspiritTalentMaterial(int warspirit_talent_material) {
        if (warspirit_talent_material == this.warspirit_talent_material)
            return;
        this.warspirit_talent_material = warspirit_talent_material;
    }

    public void saveWarspiritTalentMaterial(int warspirit_talent_material) {
        if (warspirit_talent_material == this.warspirit_talent_material)
            return;
        this.warspirit_talent_material = warspirit_talent_material;
        saveField("warspirit_talent_material", Integer.valueOf(warspirit_talent_material));
    }

    public int getWarspiritLvMaterial() {
        return this.warspirit_lv_material;
    }

    public void setWarspiritLvMaterial(int warspirit_lv_material) {
        if (warspirit_lv_material == this.warspirit_lv_material)
            return;
        this.warspirit_lv_material = warspirit_lv_material;
    }

    public void saveWarspiritLvMaterial(int warspirit_lv_material) {
        if (warspirit_lv_material == this.warspirit_lv_material)
            return;
        this.warspirit_lv_material = warspirit_lv_material;
        saveField("warspirit_lv_material", Integer.valueOf(warspirit_lv_material));
    }

    public int getWarspiritLv() {
        return this.warspirit_lv;
    }

    public void setWarspiritLv(int warspirit_lv) {
        if (warspirit_lv == this.warspirit_lv)
            return;
        this.warspirit_lv = warspirit_lv;
    }

    public void saveWarspiritLv(int warspirit_lv) {
        if (warspirit_lv == this.warspirit_lv)
            return;
        this.warspirit_lv = warspirit_lv;
        saveField("warspirit_lv", Integer.valueOf(warspirit_lv));
    }

    public int getWarspiritExp() {
        return this.warspirit_exp;
    }

    public void setWarspiritExp(int warspirit_exp) {
        if (warspirit_exp == this.warspirit_exp)
            return;
        this.warspirit_exp = warspirit_exp;
    }

    public void saveWarspiritExp(int warspirit_exp) {
        if (warspirit_exp == this.warspirit_exp)
            return;
        this.warspirit_exp = warspirit_exp;
        saveField("warspirit_exp", Integer.valueOf(warspirit_exp));
    }

    public int getWarspiritTalent() {
        return this.warspirit_talent;
    }

    public void setWarspiritTalent(int warspirit_talent) {
        if (warspirit_talent == this.warspirit_talent)
            return;
        this.warspirit_talent = warspirit_talent;
    }

    public void saveWarspiritTalent(int warspirit_talent) {
        if (warspirit_talent == this.warspirit_talent)
            return;
        this.warspirit_talent = warspirit_talent;
        saveField("warspirit_talent", Integer.valueOf(warspirit_talent));
    }

    public int getDressMaterial() {
        return this.dress_material;
    }

    public void setDressMaterial(int dress_material) {
        if (dress_material == this.dress_material)
            return;
        this.dress_material = dress_material;
    }

    public void saveDressMaterial(int dress_material) {
        if (dress_material == this.dress_material)
            return;
        this.dress_material = dress_material;
        saveField("dress_material", Integer.valueOf(dress_material));
    }

    public int getExpMaterial() {
        return this.exp_material;
    }

    public void setExpMaterial(int exp_material) {
        if (exp_material == this.exp_material)
            return;
        this.exp_material = exp_material;
    }

    public void saveExpMaterial(int exp_material) {
        if (exp_material == this.exp_material)
            return;
        this.exp_material = exp_material;
        saveField("exp_material", Integer.valueOf(exp_material));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `open_id` = '").append((this.open_id == null) ? null : this.open_id.replace("'", "''")).append("',");
        sBuilder.append(" `sid` = '").append(this.sid).append("',");
        sBuilder.append(" `name` = '").append((this.name == null) ? null : this.name.replace("'", "''")).append("',");
        sBuilder.append(" `icon` = '").append(this.icon).append("',");
        sBuilder.append(" `maxFightPower` = '").append(this.maxFightPower).append("',");
        sBuilder.append(" `gmLevel` = '").append(this.gmLevel).append("',");
        sBuilder.append(" `cheatTimes` = '").append(this.cheatTimes).append("',");
        sBuilder.append(" `banned_ChatExpiredTime` = '").append(this.banned_ChatExpiredTime).append("',");
        sBuilder.append(" `banned_LoginExpiredTime` = '").append(this.banned_LoginExpiredTime).append("',");
        sBuilder.append(" `bannedTimes` = '").append(this.bannedTimes).append("',");
        sBuilder.append(" `lastLogin` = '").append(this.lastLogin).append("',");
        sBuilder.append(" `lastLogout` = '").append(this.lastLogout).append("',");
        sBuilder.append(" `online_update` = '").append(this.online_update).append("',");
        sBuilder.append(" `totalRecharge` = '").append(this.totalRecharge).append("',");
        sBuilder.append(" `vipExp` = '").append(this.vipExp).append("',");
        sBuilder.append(" `vipLevel` = '").append(this.vipLevel).append("',");
        sBuilder.append(" `lv` = '").append(this.lv).append("',");
        sBuilder.append(" `exp` = '").append(this.exp).append("',");
        sBuilder.append(" `crystal` = '").append(this.crystal).append("',");
        sBuilder.append(" `gold` = '").append(this.gold).append("',");
        sBuilder.append(" `dungeon_lv` = '").append(this.dungeon_lv).append("',");
        sBuilder.append(" `dungeon_time` = '").append(this.dungeon_time).append("',");
        sBuilder.append(" `ext_package` = '").append(this.ext_package).append("',");
        sBuilder.append(" `createTime` = '").append(this.createTime).append("',");
        sBuilder.append(" `GmMailCheckId` = '").append(this.GmMailCheckId).append("',");
        sBuilder.append(" `strengthen_material` = '").append(this.strengthen_material).append("',");
        sBuilder.append(" `gem_material` = '").append(this.gem_material).append("',");
        sBuilder.append(" `star_material` = '").append(this.star_material).append("',");
        sBuilder.append(" `mer_material` = '").append(this.mer_material).append("',");
        sBuilder.append(" `wing_material` = '").append(this.wing_material).append("',");
        sBuilder.append(" `arena_token` = '").append(this.arena_token).append("',");
        sBuilder.append(" `equip_instance_material` = '").append(this.equip_instance_material).append("',");
        sBuilder.append(" `gem_instance_material` = '").append(this.gem_instance_material).append("',");
        sBuilder.append(" `meridian_instance_material` = '").append(this.meridian_instance_material).append("',");
        sBuilder.append(" `red_piece` = '").append(this.red_piece).append("',");
        sBuilder.append(" `artifice_material` = '").append(this.artifice_material).append("',");
        sBuilder.append(" `lottery` = '").append(this.lottery).append("',");
        sBuilder.append(" `warspirit_talent_material` = '").append(this.warspirit_talent_material).append("',");
        sBuilder.append(" `warspirit_lv_material` = '").append(this.warspirit_lv_material).append("',");
        sBuilder.append(" `warspirit_lv` = '").append(this.warspirit_lv).append("',");
        sBuilder.append(" `warspirit_exp` = '").append(this.warspirit_exp).append("',");
        sBuilder.append(" `warspirit_talent` = '").append(this.warspirit_talent).append("',");
        sBuilder.append(" `dress_material` = '").append(this.dress_material).append("',");
        sBuilder.append(" `exp_material` = '").append(this.exp_material).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

