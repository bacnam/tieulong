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
import com.zhonglian.server.common.enums.Attribute;
import com.zhonglian.server.common.enums.DressType;
import com.zhonglian.server.common.enums.EquipPos;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.enums.UnlockType;
import com.zhonglian.server.common.enums.Achievement.AchievementType;
import com.zhonglian.server.common.enums.ConstEnum.SexType;
import com.zhonglian.server.common.enums.ConstEnum.UniverseMessageType;
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
import core.network.proto.EquipMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharFeature extends Feature {
    private Map<Integer, Character> characters = new HashMap<>();
    private Integer power;

    public CharFeature(Player owner) {
        super(owner);
    }

    @Override
    public void loadDB() {
        for (CharacterBO bo : BM.getBM(CharacterBO.class).findAll("pid", this.player.getPid())) {
            this.characters.put(bo.getCharId(), new Character(this.player, bo));
        }

        for (Equip equip : this.player.getFeature(EquipFeature.class).getAllEquips()) {
            int charid = equip.getBo().getCharId();
            if (charid != 0) {
                equip.setOwner(this.getCharacter(charid), EquipPos.values()[equip.getBo().getPos()]);
            }
        }

        for (DressBO dress : this.player.getFeature(DressFeature.class).getAllDress()) {
            int charid = dress.getCharId();
            if (charid != 0) {
                this.getCharacter(charid).activeDress(DressType.values()[dress.getType()], dress);
            }
        }
    }

    public Character getCharacter(int character) {
        return this.characters.get(character);
    }

    public long unlockChar(int selected, ItemFlow playercreate) throws WSException {
        if (this.characters.size() != 0) {
            UnlockType type = UnlockType.valueOf("Character" + (this.characters.size() + 1));
            RefUnlockFunction.checkUnlock(this.player, type);
        }

        if (this.characters.containsKey(selected)) {
            throw new WSException(ErrorCode.Player_AlreadyExist, "该角色[%s]已解锁", selected);
        } else {
            CharacterBO bo = new CharacterBO();
            bo.setPid(this.player.getPid());
            bo.setCharId(selected);
            bo.insert();
            Character ch = new Character(this.player, bo);
            this.characters.put(ch.getCharId(), ch);
            if (this.characters.size() > 1) {
                NoticeMgr.getInstance().sendMarquee(UniverseMessageType.UnlockChar, this.player.getName());
            }

            this.updatePower();
            this.player.getFeature(AchievementFeature.class).updateInc(AchievementType.UnlockChar);
            return bo.getId();
        }
    }

    public Map<Integer, Character> getAll() {
        return this.characters;
    }

    public Map<Integer, Fighter> getFighters() {
        Map<Integer, Fighter> rtn = new HashMap<>();

        for (Character character : this.characters.values()) {
            int charid = character.getCharId();
            CharacterBO bo = character.getBo();
            RefCharacter refChar = RefDataMgr.get(RefCharacter.class, charid);
            Fighter fighter = new Fighter();
            fighter.id = charid;

            for (int i = 0; i < refChar.SkillList.size(); i++) {
                int skillid = refChar.SkillList.get(i);
                RefSkill skill = RefDataMgr.get(RefSkill.class, skillid);
                if (this.player.getLv() >= skill.Require) {
                    fighter.skills.put(skillid, skill);
                    fighter.skillsLv.put(skillid, bo.getSkill(i));
                }
            }

            CharAttrCalculator calculator = character.getAttr();
            fighter.putAllAttr(calculator.getAttrs());
            fighter.hp = fighter.attr(Attribute.MaxHP);
            rtn.put(character.getCharId(), fighter);
        }

        WarSpirit spirit = this.player.getFeature(WarSpiritFeature.class).getWarSpiritNow();
        if (spirit != null) {
            WarSpiritBO bo = spirit.getBo();
            RefWarSpirit refSpirit = RefDataMgr.get(RefWarSpirit.class, bo.getSpiritId());
            Fighter fighter = new Fighter();
            fighter.id = spirit.getSpiritId();
            fighter.isMain = false;

            for (int ix = 0; ix < refSpirit.SkillList.size(); ix++) {
                int skillid = refSpirit.SkillList.get(ix);
                RefSkill skill = RefDataMgr.get(RefSkill.class, skillid);
                fighter.skills.put(skillid, skill);
                if (skillid == 0) {
                    fighter.skillsLv.put(skillid, 0);
                } else {
                    fighter.skillsLv.put(skillid, bo.getSkill());
                }
            }

            SpiritAttrCalculator calculator = spirit.getAttr();
            fighter.putAllAttr(calculator.getAttrs());
            fighter.hp = fighter.attr(Attribute.MaxHP);
            rtn.put(spirit.getSpiritId(), fighter);
        }

        return rtn;
    }

    public List<core.network.proto.Character.CharInfo> getAllInfo() {
        List<core.network.proto.Character.CharInfo> list = new ArrayList<>();
        this.player.getFeature(EquipFeature.class).getAllEquips();
        this.characters.values().forEach(character -> {
            core.network.proto.Character.CharInfo ch = new core.network.proto.Character.CharInfo();
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
            list.add(ch);
        });
        return list;
    }

    public int simulate(Integer simulateId) {
        RefRobotCharacter ref = RefDataMgr.get(RefRobotCharacter.class, simulateId);
        Character character = this.characters.get(ref.CharId);
        if (character == null) {
            CharacterBO bo = new CharacterBO();
            bo.setPid(this.player.getPid());
            bo.setCharId(ref.CharId);
            bo.insert();
            character = new Character(this.player, bo);
            this.characters.put(ref.CharId, character);
        }

        CharacterBO bo = character.getBo();

        for (int i = 0; i < ref.Strengthen.size(); i++) {
            bo.setStrengthen(i + 1, ref.Strengthen.get(i));
        }

        bo.setWing(ref.Wing.random());
        bo.saveAll();

        for (int i = 0; i < ref.Equip.size(); i++) {
            int equipId = ref.Equip.get(i);
            if (equipId != 0) {
                RefEquip equipref = RefDataMgr.get(RefEquip.class, equipId);
                EquipBO equipBO = new EquipBO();
                equipBO.setPid(this.player.getPid());
                equipBO.setEquipId(equipref.id);
                equipBO.setCharId(bo.getCharId());
                equipBO.setPos(i + 1);

                for (int attridx = 0; attridx < equipref.AttrValueList.size(); attridx++) {
                    equipBO.setAttr(attridx, equipref.AttrValueList.get(attridx) * CommMath.randomInt(500, 1500) / 10000);
                }

                equipBO.setGainTime(CommTime.nowSecond());
                equipBO.insert();
                Equip equip = new Equip(this.player, equipBO);
                character.equip(equip, EquipPos.values()[i + 1]);
                equip.saveOwner(character, EquipPos.values()[i + 1]);
                this.player.getFeature(EquipFeature.class).gain(equip);
            }
        }

        bo.saveAll();
        return ref.CharId;
    }

    private int calculatePower() {
        int power = 0;

        for (Character character : this.characters.values()) {
            power += character.getPower();
        }

        power += this.player.getFeature(WarSpiritFeature.class).getPower();
        if (power > this.player.getPlayerBO().getMaxFightPower()) {
            this.player.getPlayerBO().saveMaxFightPower(power);
            this.player.notify2Zone();
        }

        return power;
    }

    public int updatePower() {
        Guild guild = this.player.getFeature(GuildMemberFeature.class).getGuild();
        if (guild != null) {
            if (this.power == null) {
                this.power = this.calculatePower();
                guild.updatePower();
            } else {
                int newpower = this.calculatePower();
                int change = newpower - this.power;
                this.power = newpower;
                guild.updatePower(change);
            }
        } else {
            this.power = this.calculatePower();
        }

        if (!RobotManager.getInstance().isRobot(this.getPid()) && !this.player.isBanned()) {
            RankManager.getInstance().update(RankType.Power, this.player.getPid(), this.power.intValue());
            this.player.getFeature(AchievementFeature.class).updateMax(AchievementType.PowerTotal, this.power);
            ActivityMgr.getActivity(RankPower.class).UpdateMaxRequire_cost(this.player, this.power);
            MarryFeature marry = this.player.getFeature(MarryFeature.class);
            if (marry.bo.getMarried() != 0) {
                Player man = null;
                Player lover = PlayerMgr.getInstance().getPlayer(marry.bo.getLoverPid());
                if (marry.bo.getSex() == SexType.Man.ordinal()) {
                    man = this.player;
                } else {
                    man = lover;
                }

                RankManager.getInstance().update(RankType.Lovers, man.getPid(), this.power + lover.getFeature(CharFeature.class).getPower());
            }
        }

        return this.power;
    }

    public int updateRobotPower() {
        return this.power = this.calculatePower();
    }

    public int getPower() {
        return this.power != null ? this.power : this.updatePower();
    }

    public int size() {
        return this.characters.size();
    }

    public double getMaxDamage(int bossid) {
        RefMonster refboss = RefDataMgr.get(RefMonster.class, bossid);
        double maxdamage = 0.0;
        Map<Integer, Fighter> fighters = this.getFighters();

        for (Fighter fighter : fighters.values()) {
            for (RefSkill refSkill : fighter.skills.values()) {
                if ("Damage".equals(refSkill.SettleType)) {
                    double def = 0.0;
                    if ("RGS".equals(refSkill.DefAttr)) {
                        def = refboss.RGS;
                    } else if ("DEF".equals(refSkill.DefAttr)) {
                        def = refboss.DEF;
                    }

                    double d = (fighter.attr(Attribute.ATK) - def)
                        * (refSkill.Attr + refSkill.AttrAdd * fighter.skillsLv.get(refSkill.id).intValue())
                        / 100.0
                        * RefDataMgr.getFactor("CriticalMultiple", 2000)
                        / 1000.0;
                    maxdamage = Math.max(maxdamage, d);
                }
            }
        }

        return maxdamage * 3.5;
    }

    public void updateCharPower() {
        for (Character character : this.characters.values()) {
            character.onAttrChanged();
        }

        this.updatePower();
    }
}
