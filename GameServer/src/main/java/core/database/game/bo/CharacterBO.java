package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CharacterBO
extends BaseBO
{
@DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
private long id;
@DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
private long pid;
@DataBaseField(type = "int(11)", fieldname = "char_id", comment = "角色id")
private int char_id;
@DataBaseField(type = "int(11)", size = 9, fieldname = "strengthen", comment = "强化等级")
private List<Integer> strengthen;
@DataBaseField(type = "int(11)", size = 9, fieldname = "gem", comment = "宝石")
private List<Integer> gem;
@DataBaseField(type = "int(11)", size = 9, fieldname = "star", comment = "星级")
private List<Integer> star;
@DataBaseField(type = "int(11)", fieldname = "meridian", comment = "经脉")
private int meridian;
@DataBaseField(type = "int(11)", fieldname = "wing", comment = "翅膀")
private int wing;
@DataBaseField(type = "int(11)", fieldname = "wingExp", comment = "翅膀当前经验")
private int wingExp;
@DataBaseField(type = "int(11)", size = 5, fieldname = "skill", comment = "技能等级")
private List<Integer> skill;
@DataBaseField(type = "int(11)", fieldname = "rebirth", comment = "重生等级")
private int rebirth;
@DataBaseField(type = "int(11)", fieldname = "rebirthExp", comment = "重生经验")
private int rebirthExp;
@DataBaseField(type = "int(11)", size = 9, fieldname = "artifice", comment = "炼化当前等级")
private List<Integer> artifice;
@DataBaseField(type = "int(11)", size = 9, fieldname = "artificeMax", comment = "炼化最大等级")
private List<Integer> artificeMax;
@DataBaseField(type = "int(11)", size = 9, fieldname = "artificeTimes", comment = "炼化次数")
private List<Integer> artificeTimes;

public CharacterBO() {
this.id = 0L;
this.pid = 0L;
this.char_id = 0;
this.strengthen = new ArrayList<>(9); int i;
for (i = 0; i < 9; i++) {
this.strengthen.add(Integer.valueOf(0));
}
this.gem = new ArrayList<>(9);
for (i = 0; i < 9; i++) {
this.gem.add(Integer.valueOf(0));
}
this.star = new ArrayList<>(9);
for (i = 0; i < 9; i++) {
this.star.add(Integer.valueOf(0));
}
this.meridian = 0;
this.wing = 0;
this.wingExp = 0;
this.skill = new ArrayList<>(5);
for (i = 0; i < 5; i++) {
this.skill.add(Integer.valueOf(0));
}
this.rebirth = 0;
this.rebirthExp = 0;
this.artifice = new ArrayList<>(9);
for (i = 0; i < 9; i++) {
this.artifice.add(Integer.valueOf(0));
}
this.artificeMax = new ArrayList<>(9);
for (i = 0; i < 9; i++) {
this.artificeMax.add(Integer.valueOf(0));
}
this.artificeTimes = new ArrayList<>(9);
for (i = 0; i < 9; i++) {
this.artificeTimes.add(Integer.valueOf(0));
}
}

public CharacterBO(ResultSet rs) throws Exception {
this.id = rs.getLong(1);
this.pid = rs.getLong(2);
this.char_id = rs.getInt(3);
this.strengthen = new ArrayList<>(9); int i;
for (i = 0; i < 9; i++) {
this.strengthen.add(Integer.valueOf(rs.getInt(i + 4)));
}
this.gem = new ArrayList<>(9);
for (i = 0; i < 9; i++) {
this.gem.add(Integer.valueOf(rs.getInt(i + 13)));
}
this.star = new ArrayList<>(9);
for (i = 0; i < 9; i++) {
this.star.add(Integer.valueOf(rs.getInt(i + 22)));
}
this.meridian = rs.getInt(31);
this.wing = rs.getInt(32);
this.wingExp = rs.getInt(33);
this.skill = new ArrayList<>(5);
for (i = 0; i < 5; i++) {
this.skill.add(Integer.valueOf(rs.getInt(i + 34)));
}
this.rebirth = rs.getInt(39);
this.rebirthExp = rs.getInt(40);
this.artifice = new ArrayList<>(9);
for (i = 0; i < 9; i++) {
this.artifice.add(Integer.valueOf(rs.getInt(i + 41)));
}
this.artificeMax = new ArrayList<>(9);
for (i = 0; i < 9; i++) {
this.artificeMax.add(Integer.valueOf(rs.getInt(i + 50)));
}
this.artificeTimes = new ArrayList<>(9);
for (i = 0; i < 9; i++) {
this.artificeTimes.add(Integer.valueOf(rs.getInt(i + 59)));
}
}

public void getFromResultSet(ResultSet rs, List<CharacterBO> list) throws Exception {
list.add(new CharacterBO(rs));
}

public long getAsynTaskTag() {
return getId();
}

public String getItemsName() {
return "`id`, `pid`, `char_id`, `strengthen_0`, `strengthen_1`, `strengthen_2`, `strengthen_3`, `strengthen_4`, `strengthen_5`, `strengthen_6`, `strengthen_7`, `strengthen_8`, `gem_0`, `gem_1`, `gem_2`, `gem_3`, `gem_4`, `gem_5`, `gem_6`, `gem_7`, `gem_8`, `star_0`, `star_1`, `star_2`, `star_3`, `star_4`, `star_5`, `star_6`, `star_7`, `star_8`, `meridian`, `wing`, `wingExp`, `skill_0`, `skill_1`, `skill_2`, `skill_3`, `skill_4`, `rebirth`, `rebirthExp`, `artifice_0`, `artifice_1`, `artifice_2`, `artifice_3`, `artifice_4`, `artifice_5`, `artifice_6`, `artifice_7`, `artifice_8`, `artificeMax_0`, `artificeMax_1`, `artificeMax_2`, `artificeMax_3`, `artificeMax_4`, `artificeMax_5`, `artificeMax_6`, `artificeMax_7`, `artificeMax_8`, `artificeTimes_0`, `artificeTimes_1`, `artificeTimes_2`, `artificeTimes_3`, `artificeTimes_4`, `artificeTimes_5`, `artificeTimes_6`, `artificeTimes_7`, `artificeTimes_8`";
}

public String getTableName() {
return "`character`";
}

public String getItemsValue() {
StringBuilder strBuf = new StringBuilder();
strBuf.append("'").append(this.id).append("', ");
strBuf.append("'").append(this.pid).append("', ");
strBuf.append("'").append(this.char_id).append("', "); int i;
for (i = 0; i < this.strengthen.size(); i++) {
strBuf.append("'").append(this.strengthen.get(i)).append("', ");
}
for (i = 0; i < this.gem.size(); i++) {
strBuf.append("'").append(this.gem.get(i)).append("', ");
}
for (i = 0; i < this.star.size(); i++) {
strBuf.append("'").append(this.star.get(i)).append("', ");
}
strBuf.append("'").append(this.meridian).append("', ");
strBuf.append("'").append(this.wing).append("', ");
strBuf.append("'").append(this.wingExp).append("', ");
for (i = 0; i < this.skill.size(); i++) {
strBuf.append("'").append(this.skill.get(i)).append("', ");
}
strBuf.append("'").append(this.rebirth).append("', ");
strBuf.append("'").append(this.rebirthExp).append("', ");
for (i = 0; i < this.artifice.size(); i++) {
strBuf.append("'").append(this.artifice.get(i)).append("', ");
}
for (i = 0; i < this.artificeMax.size(); i++) {
strBuf.append("'").append(this.artificeMax.get(i)).append("', ");
}
for (i = 0; i < this.artificeTimes.size(); i++) {
strBuf.append("'").append(this.artificeTimes.get(i)).append("', ");
}
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

public long getPid() {
return this.pid;
} public void setPid(long pid) {
if (pid == this.pid)
return; 
this.pid = pid;
}
public void savePid(long pid) {
if (pid == this.pid)
return; 
this.pid = pid;
saveField("pid", Long.valueOf(pid));
}

public int getCharId() {
return this.char_id;
} public void setCharId(int char_id) {
if (char_id == this.char_id)
return; 
this.char_id = char_id;
}
public void saveCharId(int char_id) {
if (char_id == this.char_id)
return; 
this.char_id = char_id;
saveField("char_id", Integer.valueOf(char_id));
}

public int getStrengthenSize() {
return this.strengthen.size();
} public List<Integer> getStrengthenAll() { return new ArrayList<>(this.strengthen); }
public void setStrengthenAll(int value) { for (int i = 0; i < this.strengthen.size(); ) { this.strengthen.set(i, Integer.valueOf(value)); i++; }
} public void saveStrengthenAll(int value) { setStrengthenAll(value); saveAll(); } public int getStrengthen(int index) {
return ((Integer)this.strengthen.get(index)).intValue();
} public void setStrengthen(int index, int value) {
if (value == ((Integer)this.strengthen.get(index)).intValue())
return; 
this.strengthen.set(index, Integer.valueOf(value));
}
public void saveStrengthen(int index, int value) {
if (value == ((Integer)this.strengthen.get(index)).intValue())
return; 
this.strengthen.set(index, Integer.valueOf(value));
saveField("strengthen_" + index, this.strengthen.get(index));
}

public int getGemSize() {
return this.gem.size();
} public List<Integer> getGemAll() { return new ArrayList<>(this.gem); }
public void setGemAll(int value) { for (int i = 0; i < this.gem.size(); ) { this.gem.set(i, Integer.valueOf(value)); i++; }
} public void saveGemAll(int value) { setGemAll(value); saveAll(); } public int getGem(int index) {
return ((Integer)this.gem.get(index)).intValue();
} public void setGem(int index, int value) {
if (value == ((Integer)this.gem.get(index)).intValue())
return; 
this.gem.set(index, Integer.valueOf(value));
}
public void saveGem(int index, int value) {
if (value == ((Integer)this.gem.get(index)).intValue())
return; 
this.gem.set(index, Integer.valueOf(value));
saveField("gem_" + index, this.gem.get(index));
}

public int getStarSize() {
return this.star.size();
} public List<Integer> getStarAll() { return new ArrayList<>(this.star); }
public void setStarAll(int value) { for (int i = 0; i < this.star.size(); ) { this.star.set(i, Integer.valueOf(value)); i++; }
} public void saveStarAll(int value) { setStarAll(value); saveAll(); } public int getStar(int index) {
return ((Integer)this.star.get(index)).intValue();
} public void setStar(int index, int value) {
if (value == ((Integer)this.star.get(index)).intValue())
return; 
this.star.set(index, Integer.valueOf(value));
}
public void saveStar(int index, int value) {
if (value == ((Integer)this.star.get(index)).intValue())
return; 
this.star.set(index, Integer.valueOf(value));
saveField("star_" + index, this.star.get(index));
}

public int getMeridian() {
return this.meridian;
} public void setMeridian(int meridian) {
if (meridian == this.meridian)
return; 
this.meridian = meridian;
}
public void saveMeridian(int meridian) {
if (meridian == this.meridian)
return; 
this.meridian = meridian;
saveField("meridian", Integer.valueOf(meridian));
}

public int getWing() {
return this.wing;
} public void setWing(int wing) {
if (wing == this.wing)
return; 
this.wing = wing;
}
public void saveWing(int wing) {
if (wing == this.wing)
return; 
this.wing = wing;
saveField("wing", Integer.valueOf(wing));
}

public int getWingExp() {
return this.wingExp;
} public void setWingExp(int wingExp) {
if (wingExp == this.wingExp)
return; 
this.wingExp = wingExp;
}
public void saveWingExp(int wingExp) {
if (wingExp == this.wingExp)
return; 
this.wingExp = wingExp;
saveField("wingExp", Integer.valueOf(wingExp));
}

public int getSkillSize() {
return this.skill.size();
} public List<Integer> getSkillAll() { return new ArrayList<>(this.skill); }
public void setSkillAll(int value) { for (int i = 0; i < this.skill.size(); ) { this.skill.set(i, Integer.valueOf(value)); i++; }
} public void saveSkillAll(int value) { setSkillAll(value); saveAll(); } public int getSkill(int index) {
return ((Integer)this.skill.get(index)).intValue();
} public void setSkill(int index, int value) {
if (value == ((Integer)this.skill.get(index)).intValue())
return; 
this.skill.set(index, Integer.valueOf(value));
}
public void saveSkill(int index, int value) {
if (value == ((Integer)this.skill.get(index)).intValue())
return; 
this.skill.set(index, Integer.valueOf(value));
saveField("skill_" + index, this.skill.get(index));
}

public int getRebirth() {
return this.rebirth;
} public void setRebirth(int rebirth) {
if (rebirth == this.rebirth)
return; 
this.rebirth = rebirth;
}
public void saveRebirth(int rebirth) {
if (rebirth == this.rebirth)
return; 
this.rebirth = rebirth;
saveField("rebirth", Integer.valueOf(rebirth));
}

public int getRebirthExp() {
return this.rebirthExp;
} public void setRebirthExp(int rebirthExp) {
if (rebirthExp == this.rebirthExp)
return; 
this.rebirthExp = rebirthExp;
}
public void saveRebirthExp(int rebirthExp) {
if (rebirthExp == this.rebirthExp)
return; 
this.rebirthExp = rebirthExp;
saveField("rebirthExp", Integer.valueOf(rebirthExp));
}

public int getArtificeSize() {
return this.artifice.size();
} public List<Integer> getArtificeAll() { return new ArrayList<>(this.artifice); }
public void setArtificeAll(int value) { for (int i = 0; i < this.artifice.size(); ) { this.artifice.set(i, Integer.valueOf(value)); i++; }
} public void saveArtificeAll(int value) { setArtificeAll(value); saveAll(); } public int getArtifice(int index) {
return ((Integer)this.artifice.get(index)).intValue();
} public void setArtifice(int index, int value) {
if (value == ((Integer)this.artifice.get(index)).intValue())
return; 
this.artifice.set(index, Integer.valueOf(value));
}
public void saveArtifice(int index, int value) {
if (value == ((Integer)this.artifice.get(index)).intValue())
return; 
this.artifice.set(index, Integer.valueOf(value));
saveField("artifice_" + index, this.artifice.get(index));
}

public int getArtificeMaxSize() {
return this.artificeMax.size();
} public List<Integer> getArtificeMaxAll() { return new ArrayList<>(this.artificeMax); }
public void setArtificeMaxAll(int value) { for (int i = 0; i < this.artificeMax.size(); ) { this.artificeMax.set(i, Integer.valueOf(value)); i++; }
} public void saveArtificeMaxAll(int value) { setArtificeMaxAll(value); saveAll(); } public int getArtificeMax(int index) {
return ((Integer)this.artificeMax.get(index)).intValue();
} public void setArtificeMax(int index, int value) {
if (value == ((Integer)this.artificeMax.get(index)).intValue())
return; 
this.artificeMax.set(index, Integer.valueOf(value));
}
public void saveArtificeMax(int index, int value) {
if (value == ((Integer)this.artificeMax.get(index)).intValue())
return; 
this.artificeMax.set(index, Integer.valueOf(value));
saveField("artificeMax_" + index, this.artificeMax.get(index));
}

public int getArtificeTimesSize() {
return this.artificeTimes.size();
} public List<Integer> getArtificeTimesAll() { return new ArrayList<>(this.artificeTimes); }
public void setArtificeTimesAll(int value) { for (int i = 0; i < this.artificeTimes.size(); ) { this.artificeTimes.set(i, Integer.valueOf(value)); i++; }
} public void saveArtificeTimesAll(int value) { setArtificeTimesAll(value); saveAll(); } public int getArtificeTimes(int index) {
return ((Integer)this.artificeTimes.get(index)).intValue();
} public void setArtificeTimes(int index, int value) {
if (value == ((Integer)this.artificeTimes.get(index)).intValue())
return; 
this.artificeTimes.set(index, Integer.valueOf(value));
}
public void saveArtificeTimes(int index, int value) {
if (value == ((Integer)this.artificeTimes.get(index)).intValue())
return; 
this.artificeTimes.set(index, Integer.valueOf(value));
saveField("artificeTimes_" + index, this.artificeTimes.get(index));
}

protected String getUpdateKeyValue() {
StringBuilder sBuilder = new StringBuilder();
sBuilder.append(" `pid` = '").append(this.pid).append("',");
sBuilder.append(" `char_id` = '").append(this.char_id).append("',"); int i;
for (i = 0; i < this.strengthen.size(); i++) {
sBuilder.append(" `strengthen_").append(i).append("` = '").append(this.strengthen.get(i)).append("',");
}
for (i = 0; i < this.gem.size(); i++) {
sBuilder.append(" `gem_").append(i).append("` = '").append(this.gem.get(i)).append("',");
}
for (i = 0; i < this.star.size(); i++) {
sBuilder.append(" `star_").append(i).append("` = '").append(this.star.get(i)).append("',");
}
sBuilder.append(" `meridian` = '").append(this.meridian).append("',");
sBuilder.append(" `wing` = '").append(this.wing).append("',");
sBuilder.append(" `wingExp` = '").append(this.wingExp).append("',");
for (i = 0; i < this.skill.size(); i++) {
sBuilder.append(" `skill_").append(i).append("` = '").append(this.skill.get(i)).append("',");
}
sBuilder.append(" `rebirth` = '").append(this.rebirth).append("',");
sBuilder.append(" `rebirthExp` = '").append(this.rebirthExp).append("',");
for (i = 0; i < this.artifice.size(); i++) {
sBuilder.append(" `artifice_").append(i).append("` = '").append(this.artifice.get(i)).append("',");
}
for (i = 0; i < this.artificeMax.size(); i++) {
sBuilder.append(" `artificeMax_").append(i).append("` = '").append(this.artificeMax.get(i)).append("',");
}
for (i = 0; i < this.artificeTimes.size(); i++) {
sBuilder.append(" `artificeTimes_").append(i).append("` = '").append(this.artificeTimes.get(i)).append("',");
}
sBuilder.deleteCharAt(sBuilder.length() - 1);
return sBuilder.toString();
}

public static String getSql_TableCreate() {
String sql = "CREATE TABLE IF NOT EXISTS `character` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`char_id` int(11) NOT NULL DEFAULT '0' COMMENT '角色id',`strengthen_0` int(11) NOT NULL DEFAULT '0' COMMENT '强化等级',`strengthen_1` int(11) NOT NULL DEFAULT '0' COMMENT '强化等级',`strengthen_2` int(11) NOT NULL DEFAULT '0' COMMENT '强化等级',`strengthen_3` int(11) NOT NULL DEFAULT '0' COMMENT '强化等级',`strengthen_4` int(11) NOT NULL DEFAULT '0' COMMENT '强化等级',`strengthen_5` int(11) NOT NULL DEFAULT '0' COMMENT '强化等级',`strengthen_6` int(11) NOT NULL DEFAULT '0' COMMENT '强化等级',`strengthen_7` int(11) NOT NULL DEFAULT '0' COMMENT '强化等级',`strengthen_8` int(11) NOT NULL DEFAULT '0' COMMENT '强化等级',`gem_0` int(11) NOT NULL DEFAULT '0' COMMENT '宝石',`gem_1` int(11) NOT NULL DEFAULT '0' COMMENT '宝石',`gem_2` int(11) NOT NULL DEFAULT '0' COMMENT '宝石',`gem_3` int(11) NOT NULL DEFAULT '0' COMMENT '宝石',`gem_4` int(11) NOT NULL DEFAULT '0' COMMENT '宝石',`gem_5` int(11) NOT NULL DEFAULT '0' COMMENT '宝石',`gem_6` int(11) NOT NULL DEFAULT '0' COMMENT '宝石',`gem_7` int(11) NOT NULL DEFAULT '0' COMMENT '宝石',`gem_8` int(11) NOT NULL DEFAULT '0' COMMENT '宝石',`star_0` int(11) NOT NULL DEFAULT '0' COMMENT '星级',`star_1` int(11) NOT NULL DEFAULT '0' COMMENT '星级',`star_2` int(11) NOT NULL DEFAULT '0' COMMENT '星级',`star_3` int(11) NOT NULL DEFAULT '0' COMMENT '星级',`star_4` int(11) NOT NULL DEFAULT '0' COMMENT '星级',`star_5` int(11) NOT NULL DEFAULT '0' COMMENT '星级',`star_6` int(11) NOT NULL DEFAULT '0' COMMENT '星级',`star_7` int(11) NOT NULL DEFAULT '0' COMMENT '星级',`star_8` int(11) NOT NULL DEFAULT '0' COMMENT '星级',`meridian` int(11) NOT NULL DEFAULT '0' COMMENT '经脉',`wing` int(11) NOT NULL DEFAULT '0' COMMENT '翅膀',`wingExp` int(11) NOT NULL DEFAULT '0' COMMENT '翅膀当前经验',`skill_0` int(11) NOT NULL DEFAULT '0' COMMENT '技能等级',`skill_1` int(11) NOT NULL DEFAULT '0' COMMENT '技能等级',`skill_2` int(11) NOT NULL DEFAULT '0' COMMENT '技能等级',`skill_3` int(11) NOT NULL DEFAULT '0' COMMENT '技能等级',`skill_4` int(11) NOT NULL DEFAULT '0' COMMENT '技能等级',`rebirth` int(11) NOT NULL DEFAULT '0' COMMENT '重生等级',`rebirthExp` int(11) NOT NULL DEFAULT '0' COMMENT '重生经验',`artifice_0` int(11) NOT NULL DEFAULT '0' COMMENT '炼化当前等级',`artifice_1` int(11) NOT NULL DEFAULT '0' COMMENT '炼化当前等级',`artifice_2` int(11) NOT NULL DEFAULT '0' COMMENT '炼化当前等级',`artifice_3` int(11) NOT NULL DEFAULT '0' COMMENT '炼化当前等级',`artifice_4` int(11) NOT NULL DEFAULT '0' COMMENT '炼化当前等级',`artifice_5` int(11) NOT NULL DEFAULT '0' COMMENT '炼化当前等级',`artifice_6` int(11) NOT NULL DEFAULT '0' COMMENT '炼化当前等级',`artifice_7` int(11) NOT NULL DEFAULT '0' COMMENT '炼化当前等级',`artifice_8` int(11) NOT NULL DEFAULT '0' COMMENT '炼化当前等级',`artificeMax_0` int(11) NOT NULL DEFAULT '0' COMMENT '炼化最大等级',`artificeMax_1` int(11) NOT NULL DEFAULT '0' COMMENT '炼化最大等级',`artificeMax_2` int(11) NOT NULL DEFAULT '0' COMMENT '炼化最大等级',`artificeMax_3` int(11) NOT NULL DEFAULT '0' COMMENT '炼化最大等级',`artificeMax_4` int(11) NOT NULL DEFAULT '0' COMMENT '炼化最大等级',`artificeMax_5` int(11) NOT NULL DEFAULT '0' COMMENT '炼化最大等级',`artificeMax_6` int(11) NOT NULL DEFAULT '0' COMMENT '炼化最大等级',`artificeMax_7` int(11) NOT NULL DEFAULT '0' COMMENT '炼化最大等级',`artificeMax_8` int(11) NOT NULL DEFAULT '0' COMMENT '炼化最大等级',`artificeTimes_0` int(11) NOT NULL DEFAULT '0' COMMENT '炼化次数',`artificeTimes_1` int(11) NOT NULL DEFAULT '0' COMMENT '炼化次数',`artificeTimes_2` int(11) NOT NULL DEFAULT '0' COMMENT '炼化次数',`artificeTimes_3` int(11) NOT NULL DEFAULT '0' COMMENT '炼化次数',`artificeTimes_4` int(11) NOT NULL DEFAULT '0' COMMENT '炼化次数',`artificeTimes_5` int(11) NOT NULL DEFAULT '0' COMMENT '炼化次数',`artificeTimes_6` int(11) NOT NULL DEFAULT '0' COMMENT '炼化次数',`artificeTimes_7` int(11) NOT NULL DEFAULT '0' COMMENT '炼化次数',`artificeTimes_8` int(11) NOT NULL DEFAULT '0' COMMENT '炼化次数',PRIMARY KEY (`id`)) COMMENT='玩家信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

ServerConfig.getInitialID() + 1L);
return sql;
}
}

