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
import com.zhonglian.server.common.enums.Attribute;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.enums.Achievement.AchievementType;
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

public class BossFight extends Fight<Reward> {
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

        this.attackers = player.getFeature(CharFeature.class).getFighters();
    }

    protected Reward onLost() {
        return null;
    }

    protected Reward onWin() {
        DungeonFeature dungeon = this.player.getFeature(DungeonFeature.class);
        Reward reward = new Reward();

        for (int i = dungeon.getLevel(); i < this.level + 1; i++) {
            RefDungeon ref = RefDataMgr.get(RefDungeon.class, i);
            reward.combine(RefDataMgr.get(RefReward.class, ref.BossDrop).genReward());
            dungeon.nextDungeon();
        }

        Reward newreward = new Reward();

        for (UniformItem item : reward.merge()) {
            newreward.add(item);
        }

        this.player.getFeature(PlayerItem.class).gain(newreward, ItemFlow.Dungeon_BossWin);
        dungeon.DungeonUp();
        RankManager.getInstance().update(RankType.Dungeon, this.player.getPid(), this.player.getPlayerBO().getDungeonLv());
        this.player.getFeature(AchievementFeature.class).updateMax(AchievementType.DungeonLevel, this.player.getPlayerBO().getDungeonLv());
        this.player.getFeature(AchievementFeature.class).updateMax(AchievementType.DungeonLevel_M1, this.player.getPlayerBO().getDungeonLv());
        this.player.getFeature(AchievementFeature.class).updateMax(AchievementType.DungeonLevel_M2, this.player.getPlayerBO().getDungeonLv());
        this.player.getFeature(AchievementFeature.class).updateMax(AchievementType.DungeonLevel_M3, this.player.getPlayerBO().getDungeonLv());
        this.player.getFeature(AchievementFeature.class).updateMax(AchievementType.DungeonLevel_M4, this.player.getPlayerBO().getDungeonLv());
        this.player.getFeature(AchievementFeature.class).updateMax(AchievementType.DungeonLevel_M5, this.player.getPlayerBO().getDungeonLv());
        this.player.getFeature(AchievementFeature.class).updateMax(AchievementType.DungeonLevel_M6, this.player.getPlayerBO().getDungeonLv());
        this.player.getFeature(AchievementFeature.class).updateMax(AchievementType.DungeonLevel_M7, this.player.getPlayerBO().getDungeonLv());
        ActivityMgr.getActivity(RankDungeon.class).UpdateMaxRequire_cost(this.player, this.player.getPlayerBO().getDungeonLv());
        return newreward;
    }

    @Override
    protected Map<Integer, Fighter> getDeffenders() {
        Map<Integer, Fighter> rtn = new HashMap<>();
        RefDungeon ref = RefDataMgr.get(RefDungeon.class, this.level);
        RefMonster refMonster = RefDataMgr.get(RefMonster.class, ref.BossID);
        Fighter fighter = new Fighter();
        fighter.id = ref.BossID;

        for (int i = 0; i < refMonster.SkillList.size(); i++) {
            RefSkill skill = RefDataMgr.get(RefSkill.class, refMonster.SkillList.get(i));
            fighter.skills.put(skill.id, skill);
            fighter.skillsLv.put(skill.id, 0);
        }

        for (int i = 0; i < refMonster.BuffList.size(); i++) {
            RefBuff refBuff = RefDataMgr.get(RefBuff.class, refMonster.BuffList.get(i));
            Buff buff = new Buff(refBuff, 0);
            buff.time = refBuff.Time * 1000;
            buff.cd = refBuff.CD * 1000;
            fighter.buffs.put(buff.id, buff);
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
        rtn.put(ref.BossID, fighter);
        return rtn;
    }

    @Override
    protected Map<Integer, Fighter> getAttackers() {
        return this.attackers;
    }

    @Override
    public int fightTime() {
        return ArenaConfig.fightTime() + 90;
    }

    public void initAttr() {
        RefDungeonRebirth ref = this.player.getFeature(DungeonFeature.class).getRebirthRef(this.level);
        if (ref != null) {
            for (int i = 0; i < ref.AttrList.size(); i++) {
                Attribute attr = ref.AttrList.get(i);
                int value = ref.AttrValue.get(i);

                for (Fighter fight : this.attackers.values()) {
                    double finalvalue = fight.attrs.get(attr) * (100 + value) / 100.0;
                    fight.putAttr(attr, finalvalue);
                }
            }

            this.player.getFeature(DungeonFeature.class).removeRebirthRef(this.level);
        }
    }
}
