package business.player.feature.character;

import business.global.guild.Guild;
import business.global.guild.GuildConfig;
import business.player.Player;
import business.player.feature.guild.GuildMemberFeature;
import business.player.feature.player.LingBaoFeature;
import business.player.feature.player.NewTitleFeature;
import com.zhonglian.server.common.enums.Attribute;
import com.zhonglian.server.common.enums.EquipPos;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefArtifice;
import core.config.refdata.ref.RefAttribute;
import core.config.refdata.ref.RefCharacter;
import core.config.refdata.ref.RefDress;
import core.config.refdata.ref.RefEquip;
import core.config.refdata.ref.RefGem;
import core.config.refdata.ref.RefGuildSkill;
import core.config.refdata.ref.RefLingBao;
import core.config.refdata.ref.RefMeridian;
import core.config.refdata.ref.RefNewTitleLevel;
import core.config.refdata.ref.RefRebirth;
import core.config.refdata.ref.RefSkill;
import core.config.refdata.ref.RefStarInfo;
import core.config.refdata.ref.RefStrengthenInfo;
import core.config.refdata.ref.RefStrengthenType;
import core.config.refdata.ref.RefWing;
import core.database.game.bo.CharacterBO;
import core.database.game.bo.DressBO;
import core.database.game.bo.EquipBO;
import core.network.proto.TitleInfo;
import java.util.List;
import java.util.Map;

public class CharAttrCalculator extends AttrCalculator {
    private Character character;
    private CharacterBO bo;
    private Player player;
    private RefCharacter ref;

    public CharAttrCalculator(Character character) {
        this.character = character;
        this.player = this.character.player;
        this.bo = character.getBo();
    }

    @Override
    public int getPower() {
        DressFeature feature = this.player.getFeature(DressFeature.class);
        List<DressBO> list = feature.getAllDress();
        if (feature.checkDressList(list) != 0) {
            this.updated = false;
        }

        return super.getPower();
    }

    protected void onUpdated() {
        this.character.updateRank(this.power);
    }

    @Override
    protected void doUpdate() {
        this.ref = RefDataMgr.get(RefCharacter.class, this.character.getCharId());
        this.allAttr.put(Attribute.MaxHP, 0);
        this.allAttr.put(Attribute.ATK, 0);
        this.allAttr.put(Attribute.DEF, 0);
        this.allAttr.put(Attribute.RGS, 0);
        this.allAttr.put(Attribute.Hit, 0);
        this.allAttr.put(Attribute.Dodge, 0);
        this.allAttr.put(Attribute.Critical, 0);
        this.allAttr.put(Attribute.Tenacity, 0);
        this.calcBase();
        this.calcChar();
        this.calcEquip();
        this.calcPos();
        this.calcGuild();
        this.calcDress();
        this.calcLingBao();
        this.calcNewTitle();
        this.power = PowerUtils.getPower(this.allAttr);
        List<Integer> skills = this.bo.getSkillAll();

        for (int i = 0; i < this.ref.SkillList.size(); i++) {
            RefSkill skill = RefDataMgr.get(RefSkill.class, this.ref.SkillList.get(i));
            if (this.player.getLv() > skill.Require) {
                this.power = this.power + skill.CE * (skills.get(i) + 1);
            }
        }
    }

    private void calcBase() {
        RefAttribute refAttribute = RefDataMgr.get(RefAttribute.class, String.format("%d%03d", this.ref.id, this.player.getLv()));
        this.addAttr(Attribute.MaxHP, refAttribute.MaxHP);
        this.addAttr(Attribute.ATK, refAttribute.ATK);
        this.addAttr(Attribute.DEF, refAttribute.DEF);
        this.addAttr(Attribute.RGS, refAttribute.RGS);
        this.addAttr(Attribute.Hit, refAttribute.Hit);
        this.addAttr(Attribute.Dodge, refAttribute.Dodge);
        this.addAttr(Attribute.Critical, refAttribute.Critical);
        this.addAttr(Attribute.Tenacity, refAttribute.Tenacity);
    }

    private void calcPos() {
        EquipPos[] equipPos = EquipPos.values();

        for (int i = 1; i < equipPos.length; i++) {
            int artlv = this.bo.getArtifice(i);
            RefStrengthenType refStrengthen = RefDataMgr.get(RefStrengthenType.class, equipPos[i]);
            if (artlv > 0) {
                RefArtifice ref = RefDataMgr.get(RefArtifice.class, artlv);
                if (ref != null) {
                    for (int j = 0; j < ref.Level - 1; j++) {
                        RefArtifice arc = RefArtifice.maxLevelMap.get(j + 1);
                        int value = arc.getValue(refStrengthen.ArtificeType.get(j));
                        this.addAttr(refStrengthen.ArtificeType.get(j), value);
                    }

                    int value = ref.getValue(refStrengthen.ArtificeType.get(ref.Level - 1));
                    this.addAttr(refStrengthen.ArtificeType.get(ref.Level - 1), value);
                }
            }

            int gemlv = this.bo.getGem(i);
            if (gemlv > 0) {
                int value = RefDataMgr.get(RefGem.class, gemlv).getValue(refStrengthen.GemType);
                this.addAttr(refStrengthen.GemType, value);
            }
        }
    }

    private void calcEquip() {
        for (Equip equip : this.character.getEquips()) {
            EquipBO equipBO = equip.getBo();
            int pos = equipBO.getPos();
            EquipPos pos2 = EquipPos.values()[pos];
            double starvalue = 0.0;
            if (this.bo.getStar(pos) > 0) {
                starvalue = RefDataMgr.get(RefStarInfo.class, this.bo.getStar(pos)).Attribute / 100.0;
            }

            int strenLv = this.bo.getStrengthen(pos);
            Attribute strenattr = RefDataMgr.get(RefStrengthenType.class, pos2).StrengthenType;
            RefEquip ref = equip.getRef();

            for (int i = 0; i < ref.AttrTypeList.size(); i++) {
                int basevalue = ref.AttrValueList.get(i) + equipBO.getAttr(i);
                Attribute attrtype = ref.AttrTypeList.get(i);
                int attrvalue = basevalue + (int)(basevalue * starvalue);
                if (strenattr == attrtype && strenLv > 0) {
                    attrvalue = (int)(attrvalue + basevalue * RefDataMgr.get(RefStrengthenInfo.class, strenLv).Attribute);
                }

                this.addAttr(attrtype, attrvalue);
            }
        }
    }

    private void calcChar() {
        if (this.bo.getRebirth() > 0) {
            RefRebirth refRebirth = RefDataMgr.get(RefRebirth.class, this.bo.getRebirth());
            this.addAttr(refRebirth.AttrTypeList, refRebirth.AttrValueList);
        }

        if (this.bo.getMeridian() > 0) {
            RefMeridian refMeridian = RefDataMgr.get(RefMeridian.class, this.bo.getMeridian());
            this.addAttr(refMeridian.AttrTypeList, refMeridian.AttrValueList);
        }

        if (this.bo.getWing() > 0) {
            RefWing refWing = RefDataMgr.get(RefWing.class, this.bo.getWing());
            this.addAttr(refWing.AttrTypeList, refWing.AttrValueList);
        }
    }

    private void calcGuild() {
        GuildMemberFeature feature = this.player.getFeature(GuildMemberFeature.class);
        Guild guild = feature.getGuild();
        if (guild != null) {
            for (core.network.proto.Guild.GuildSkill skill : feature.getGuildSkillList()) {
                int maxLevel = GuildConfig.getSkillMaxLevel(skill.getSkillid(), guild.getLevel());
                int level = Math.min(maxLevel, skill.getLevel());
                RefGuildSkill ref = RefDataMgr.get(RefGuildSkill.class, skill.getSkillid());
                this.addAttr(ref.Attribute, ref.BaseValue + ref.GrowthValue * (level - 1));
            }
        }
    }

    private void calcDress() {
        for (DressBO bo : this.character.getDresses()) {
            RefDress ref = RefDataMgr.get(RefDress.class, bo.getDressId());
            this.addAttr(ref.AttrTypeList, ref.AttrValueList);
        }
    }

    private void calcNewTitle() {
        NewTitleFeature feature = this.player.getFeature(NewTitleFeature.class);

        for (TitleInfo title : feature.getAllTitleInfo()) {
            Map<Integer, RefNewTitleLevel> refmap = RefNewTitleLevel.getTitleByType(title.getTitleId());
            RefNewTitleLevel ref = refmap.get(title.getLevel());
            this.addAttr(ref.AttrTypeList, ref.AttrValueList);
        }
    }

    private void calcLingBao() {
        LingBaoFeature feature = this.player.getFeature(LingBaoFeature.class);
        RefLingBao ref = RefDataMgr.get(RefLingBao.class, feature.getLevel());
        this.addAttr(ref.AttrTypeList, ref.AttrValueList);
    }
}
