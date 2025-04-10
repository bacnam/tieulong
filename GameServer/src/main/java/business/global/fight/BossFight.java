package business.global.fight;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.RankDungeon;
import business.global.arena.ArenaConfig;
import business.global.rank.RankManager;
import business.player.Player;
import business.player.feature.PlayerItem;
import business.player.feature.achievement.AchievementFeature;
import business.player.feature.character.CharFeature;
import business.player.feature.pve.DungeonFeature;
import business.player.item.Reward;
import business.player.item.UniformItem;
import com.zhonglian.server.common.enums.Achievement;
import com.zhonglian.server.common.enums.Attribute;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.logger.flow.ItemFlow;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefBuff;
import core.config.refdata.ref.RefDungeon;
import core.config.refdata.ref.RefDungeonRebirth;
import core.config.refdata.ref.RefMonster;
import core.config.refdata.ref.RefReward;
import core.config.refdata.ref.RefSkill;
import java.util.HashMap;
import java.util.Map;

public class BossFight
extends Fight<Reward>
{
private Player player;
private Map<Integer, Fighter> attackers;
private int level;

public BossFight(Player player, int level) {
this.player = player;
if (level <= 0) {
this.level = player.getPlayerBO().getDungeonLv();
} else {
this.level = level;
} 
this.attackers = ((CharFeature)player.getFeature(CharFeature.class)).getFighters();
}

protected Reward onLost() {
return null;
}

protected Reward onWin() {
DungeonFeature dungeon = (DungeonFeature)this.player.getFeature(DungeonFeature.class);

Reward reward = new Reward();
for (int i = dungeon.getLevel(); i < this.level + 1; i++) {

RefDungeon ref = (RefDungeon)RefDataMgr.get(RefDungeon.class, Integer.valueOf(i));
reward.combine(((RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(ref.BossDrop))).genReward());

dungeon.nextDungeon();
} 
Reward newreward = new Reward();
for (UniformItem item : reward.merge()) {
newreward.add(item);
}
((PlayerItem)this.player.getFeature(PlayerItem.class)).gain(newreward, ItemFlow.Dungeon_BossWin);
dungeon.DungeonUp();

RankManager.getInstance().update(RankType.Dungeon, this.player.getPid(), this.player.getPlayerBO().getDungeonLv());

((AchievementFeature)this.player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.DungeonLevel, Integer.valueOf(this.player.getPlayerBO().getDungeonLv()));
((AchievementFeature)this.player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.DungeonLevel_M1, Integer.valueOf(this.player.getPlayerBO().getDungeonLv()));
((AchievementFeature)this.player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.DungeonLevel_M2, Integer.valueOf(this.player.getPlayerBO().getDungeonLv()));
((AchievementFeature)this.player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.DungeonLevel_M3, Integer.valueOf(this.player.getPlayerBO().getDungeonLv()));
((AchievementFeature)this.player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.DungeonLevel_M4, Integer.valueOf(this.player.getPlayerBO().getDungeonLv()));
((AchievementFeature)this.player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.DungeonLevel_M5, Integer.valueOf(this.player.getPlayerBO().getDungeonLv()));
((AchievementFeature)this.player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.DungeonLevel_M6, Integer.valueOf(this.player.getPlayerBO().getDungeonLv()));
((AchievementFeature)this.player.getFeature(AchievementFeature.class)).updateMax(Achievement.AchievementType.DungeonLevel_M7, Integer.valueOf(this.player.getPlayerBO().getDungeonLv()));

((RankDungeon)ActivityMgr.getActivity(RankDungeon.class)).UpdateMaxRequire_cost(this.player, this.player.getPlayerBO().getDungeonLv());
return newreward;
}

protected Map<Integer, Fighter> getDeffenders() {
Map<Integer, Fighter> rtn = new HashMap<>();

RefDungeon ref = (RefDungeon)RefDataMgr.get(RefDungeon.class, Integer.valueOf(this.level));
RefMonster refMonster = (RefMonster)RefDataMgr.get(RefMonster.class, Integer.valueOf(ref.BossID));

Fighter fighter = new Fighter();
fighter.id = ref.BossID;
int i;
for (i = 0; i < refMonster.SkillList.size(); i++) {
RefSkill skill = (RefSkill)RefDataMgr.get(RefSkill.class, refMonster.SkillList.get(i));
fighter.skills.put(Integer.valueOf(skill.id), skill);
fighter.skillsLv.put(Integer.valueOf(skill.id), Integer.valueOf(0));
} 
for (i = 0; i < refMonster.BuffList.size(); i++) {
RefBuff refBuff = (RefBuff)RefDataMgr.get(RefBuff.class, refMonster.BuffList.get(i));
Buff buff = new Buff(refBuff, 0);
buff.time = (refBuff.Time * 1000);
buff.cd = (refBuff.CD * 1000);
fighter.buffs.put(Integer.valueOf(buff.id), buff);
} 

fighter.putAttr(Attribute.MaxHP, refMonster.MaxHP);
fighter.putAttr(Attribute.ATK, refMonster.ATK);
fighter.putAttr(Attribute.DEF, refMonster.DEF);
fighter.putAttr(Attribute.RGS, refMonster.RGS);
fighter.putAttr(Attribute.Hit, refMonster.Hit);
fighter.putAttr(Attribute.Dodge, refMonster.Dodge);
fighter.putAttr(Attribute.Critical, refMonster.Critical);
fighter.putAttr(Attribute.Tenacity, refMonster.Tenacity);

fighter.hp = fighter.attr(Attribute.MaxHP);
rtn.put(Integer.valueOf(ref.BossID), fighter);
return rtn;
}

protected Map<Integer, Fighter> getAttackers() {
return this.attackers;
}

public int fightTime() {
return ArenaConfig.fightTime() + 90;
}

public void initAttr() {
RefDungeonRebirth ref = ((DungeonFeature)this.player.getFeature(DungeonFeature.class)).getRebirthRef(this.level);
if (ref != null) {
for (int i = 0; i < ref.AttrList.size(); i++) {
Attribute attr = ref.AttrList.get(i);
int value = ((Integer)ref.AttrValue.get(i)).intValue();
for (Fighter fight : this.attackers.values()) {
double finalvalue = ((Double)fight.attrs.get(attr)).doubleValue() * (100 + value) / 100.0D;
fight.putAttr(attr, finalvalue);
} 
} 
((DungeonFeature)this.player.getFeature(DungeonFeature.class)).removeRebirthRef(this.level);
} 
}
}

