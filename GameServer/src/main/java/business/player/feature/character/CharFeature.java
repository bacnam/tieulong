package business.player.feature.character;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.RankPower;
import business.global.fight.Fighter;
import business.global.guild.Guild;
import business.global.notice.NoticeMgr;
import business.global.rank.RankManager;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.RobotManager;
import business.player.feature.Feature;
import business.player.feature.achievement.AchievementFeature;
import business.player.feature.guild.GuildMemberFeature;
import business.player.feature.marry.MarryFeature;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.Achievement;
import com.zhonglian.server.common.enums.Attribute;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.DressType;
import com.zhonglian.server.common.enums.EquipPos;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.enums.UnlockType;
import com.zhonglian.server.common.utils.CommMath;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefCharacter;
import core.config.refdata.ref.RefEquip;
import core.config.refdata.ref.RefMonster;
import core.config.refdata.ref.RefRobotCharacter;
import core.config.refdata.ref.RefSkill;
import core.config.refdata.ref.RefUnlockFunction;
import core.config.refdata.ref.RefWarSpirit;
import core.database.game.bo.CharacterBO;
import core.database.game.bo.DressBO;
import core.database.game.bo.EquipBO;
import core.database.game.bo.WarSpiritBO;
import core.network.proto.Character;
import core.network.proto.EquipMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharFeature extends Feature {
private Map<Integer, Character> characters;
private Integer power;

public CharFeature(Player owner) {
super(owner);

this.characters = new HashMap<>();
}

public void loadDB() {
List<CharacterBO> bos = BM.getBM(CharacterBO.class).findAll("pid", Long.valueOf(this.player.getPid()));
for (CharacterBO bo : bos) {
this.characters.put(Integer.valueOf(bo.getCharId()), new Character(this.player, bo));
}

List<Equip> equips = ((EquipFeature)this.player.getFeature(EquipFeature.class)).getAllEquips();
for (Equip equip : equips) {
int charid = equip.getBo().getCharId();
if (charid != 0) {
equip.setOwner(getCharacter(charid), EquipPos.values()[equip.getBo().getPos()]);
}
} 

List<DressBO> dresses = ((DressFeature)this.player.getFeature(DressFeature.class)).getAllDress();
for (DressBO dress : dresses) {
int charid = dress.getCharId();
if (charid != 0) {
getCharacter(charid).activeDress(DressType.values()[dress.getType()], dress);
}
} 
}

public Character getCharacter(int character) {
return this.characters.get(Integer.valueOf(character));
}

public long unlockChar(int selected, ItemFlow playercreate) throws WSException {
if (this.characters.size() != 0) {
UnlockType type = UnlockType.valueOf("Character" + (this.characters.size() + 1));
RefUnlockFunction.checkUnlock(this.player, type);
} 
if (this.characters.containsKey(Integer.valueOf(selected))) {
throw new WSException(ErrorCode.Player_AlreadyExist, "该角色[%s]已解锁", new Object[] { Integer.valueOf(selected) });
}
CharacterBO bo = new CharacterBO();
bo.setPid(this.player.getPid());
bo.setCharId(selected);
bo.insert();
Character ch = new Character(this.player, bo);
this.characters.put(Integer.valueOf(ch.getCharId()), ch);
if (this.characters.size() > 1)
NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.UnlockChar, new String[] { this.player.getName() }); 
updatePower();

((AchievementFeature)this.player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.UnlockChar);
return bo.getId();
}

public Map<Integer, Character> getAll() {
return this.characters;
}

public Map<Integer, Fighter> getFighters() {
Map<Integer, Fighter> rtn = new HashMap<>();
for (Character character : this.characters.values()) {
int charid = character.getCharId();
CharacterBO bo = character.getBo();
RefCharacter refChar = (RefCharacter)RefDataMgr.get(RefCharacter.class, Integer.valueOf(charid));

Fighter fighter = new Fighter();
fighter.id = charid;
for (int i = 0; i < refChar.SkillList.size(); i++) {
int skillid = ((Integer)refChar.SkillList.get(i)).intValue();
RefSkill skill = (RefSkill)RefDataMgr.get(RefSkill.class, Integer.valueOf(skillid));
if (this.player.getLv() >= skill.Require) {

fighter.skills.put(Integer.valueOf(skillid), skill);
fighter.skillsLv.put(Integer.valueOf(skillid), Integer.valueOf(bo.getSkill(i)));
} 
}  CharAttrCalculator calculator = character.getAttr();
fighter.putAllAttr(calculator.getAttrs());

fighter.hp = fighter.attr(Attribute.MaxHP);

rtn.put(Integer.valueOf(character.getCharId()), fighter);
} 
WarSpirit spirit = ((WarSpiritFeature)this.player.getFeature(WarSpiritFeature.class)).getWarSpiritNow();
if (spirit != null) {
WarSpiritBO bo = spirit.getBo();
RefWarSpirit refSpirit = (RefWarSpirit)RefDataMgr.get(RefWarSpirit.class, Integer.valueOf(bo.getSpiritId()));

Fighter fighter = new Fighter();
fighter.id = spirit.getSpiritId();
fighter.isMain = false;
for (int i = 0; i < refSpirit.SkillList.size(); i++) {
int skillid = ((Integer)refSpirit.SkillList.get(i)).intValue();
RefSkill skill = (RefSkill)RefDataMgr.get(RefSkill.class, Integer.valueOf(skillid));
fighter.skills.put(Integer.valueOf(skillid), skill);
if (skillid == 0) {
fighter.skillsLv.put(Integer.valueOf(skillid), Integer.valueOf(0));
} else {
fighter.skillsLv.put(Integer.valueOf(skillid), Integer.valueOf(bo.getSkill()));
} 
} 
SpiritAttrCalculator calculator = spirit.getAttr();
fighter.putAllAttr(calculator.getAttrs());

fighter.hp = fighter.attr(Attribute.MaxHP);
rtn.put(Integer.valueOf(spirit.getSpiritId()), fighter);
} 
return rtn;
}

public List<Character.CharInfo> getAllInfo() {
List<Character.CharInfo> list = new ArrayList<>();
((EquipFeature)this.player.getFeature(EquipFeature.class)).getAllEquips();
this.characters.values().forEach(character -> {
Character.CharInfo ch = new Character.CharInfo();

CharacterBO bo = character.getBo();
ch.sid = bo.getId();
ch.level = this.player.getLv();
ch.charId = character.getCharId();
ch.strengthen.addAll(bo.getStrengthenAll());
ch.gem.addAll(bo.getGemAll());
ch.star.addAll(bo.getStarAll());
ch.meridian = bo.getMeridian();
ch.wing = bo.getWing();
ch.wingExp = bo.getWingExp();
ch.skill.addAll(bo.getSkillAll());
ch.rebirth = bo.getRebirth();
ch.rebirthExp = bo.getRebirthExp();
ch.artifice.addAll(bo.getArtificeAll());
ch.artificeMax.addAll(bo.getArtificeMaxAll());
for (Equip equip : character.getEquips()) {
ch.equips.add(new EquipMessage(equip.getBo()));
}
ch.dresses = character.getAllDressInfo();
paramList.add(ch);
});
return list;
}

public int simulate(Integer simulateId) {
RefRobotCharacter ref = (RefRobotCharacter)RefDataMgr.get(RefRobotCharacter.class, simulateId);
Character character = this.characters.get(Integer.valueOf(ref.CharId));
if (character == null) {
CharacterBO characterBO = new CharacterBO();
characterBO.setPid(this.player.getPid());
characterBO.setCharId(ref.CharId);
characterBO.insert();
character = new Character(this.player, characterBO);
this.characters.put(Integer.valueOf(ref.CharId), character);
} 
CharacterBO bo = character.getBo(); int i;
for (i = 0; i < ref.Strengthen.size(); i++) {
bo.setStrengthen(i + 1, ((Integer)ref.Strengthen.get(i)).intValue());
}
bo.setWing(ref.Wing.random());
bo.saveAll();

for (i = 0; i < ref.Equip.size(); i++) {
int equipId = ((Integer)ref.Equip.get(i)).intValue();
if (equipId != 0) {

RefEquip equipref = (RefEquip)RefDataMgr.get(RefEquip.class, Integer.valueOf(equipId));
EquipBO equipBO = new EquipBO();
equipBO.setPid(this.player.getPid());
equipBO.setEquipId(equipref.id);
equipBO.setCharId(bo.getCharId());
equipBO.setPos(i + 1);
for (int attridx = 0; attridx < equipref.AttrValueList.size(); attridx++) {
equipBO.setAttr(attridx, ((Integer)equipref.AttrValueList.get(attridx)).intValue() * CommMath.randomInt(500, 1500) / 10000);
}
equipBO.setGainTime(CommTime.nowSecond());
equipBO.insert();
Equip equip = new Equip(this.player, equipBO);
character.equip(equip, EquipPos.values()[i + 1]);
equip.saveOwner(character, EquipPos.values()[i + 1]);
((EquipFeature)this.player.getFeature(EquipFeature.class)).gain(equip);
} 
}  bo.saveAll();
return ref.CharId;
}

private int calculatePower() {
int power = 0;

for (Character character : this.characters.values()) {
power += character.getPower();
}

power += ((WarSpiritFeature)this.player.getFeature(WarSpiritFeature.class)).getPower();

if (power > this.player.getPlayerBO().getMaxFightPower()) {
this.player.getPlayerBO().saveMaxFightPower(power);
this.player.notify2Zone();
} 
return power;
}

public int updatePower() {
Guild guild = ((GuildMemberFeature)this.player.getFeature(GuildMemberFeature.class)).getGuild();
if (guild != null) {
if (this.power == null) {
this.power = Integer.valueOf(calculatePower());
guild.updatePower();
} else {
int newpower = calculatePower();
int change = newpower - this.power.intValue();
this.power = Integer.valueOf(newpower);
guild.updatePower(change);
} 
} else {
this.power = Integer.valueOf(calculatePower());
} 

if (!RobotManager.getInstance().isRobot(getPid()) && !this.player.isBanned()) {

RankManager.getInstance().update(RankType.Power, this.player.getPid(), this.power.intValue());

((AchievementFeature)this.player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.PowerTotal, this.power);

((RankPower)ActivityMgr.getActivity(RankPower.class)).UpdateMaxRequire_cost(this.player, this.power.intValue());

MarryFeature marry = (MarryFeature)this.player.getFeature(MarryFeature.class);
if (marry.bo.getMarried() != 0) {
Player man = null;
Player lover = PlayerMgr.getInstance().getPlayer(marry.bo.getLoverPid());
if (marry.bo.getSex() == ConstEnum.SexType.Man.ordinal()) {
man = this.player;
} else {
man = lover;
} 
RankManager.getInstance().update(RankType.Lovers, man.getPid(), (this.power.intValue() + ((CharFeature)lover.getFeature(CharFeature.class)).getPower()));
} 
} 
return this.power.intValue();
}

public int updateRobotPower() {
return (this.power = Integer.valueOf(calculatePower())).intValue();
}

public int getPower() {
if (this.power != null) {
return this.power.intValue();
}
return updatePower();
}

public int size() {
return this.characters.size();
}

public double getMaxDamage(int bossid) {
RefMonster refboss = (RefMonster)RefDataMgr.get(RefMonster.class, Integer.valueOf(bossid));
double maxdamage = 0.0D;
Map<Integer, Fighter> fighters = getFighters();
for (Fighter fighter : fighters.values()) {
for (RefSkill refSkill : fighter.skills.values()) {
if (!"Damage".equals(refSkill.SettleType)) {
continue;
}
double def = 0.0D;
if ("RGS".equals(refSkill.DefAttr)) {
def = refboss.RGS;
} else if ("DEF".equals(refSkill.DefAttr)) {
def = refboss.DEF;
} 

double d = (fighter.attr(Attribute.ATK) - def) * (
refSkill.Attr + refSkill.AttrAdd * ((Integer)fighter.skillsLv.get(Integer.valueOf(refSkill.id))).intValue()) / 100.0D * 
RefDataMgr.getFactor("CriticalMultiple", 2000) / 1000.0D;
maxdamage = Math.max(maxdamage, d);
} 
} 
return maxdamage * 3.5D;
}

public void updateCharPower() {
for (Character character : this.characters.values()) {
character.onAttrChanged();
}
updatePower();
}
}

