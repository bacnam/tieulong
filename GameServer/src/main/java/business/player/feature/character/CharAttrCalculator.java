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
import core.config.refdata.ref.*;
import core.database.game.bo.CharacterBO;
import core.database.game.bo.DressBO;
import core.database.game.bo.EquipBO;
import core.network.proto.TitleInfo;

import java.util.List;
import java.util.Map;

public class CharAttrCalculator
        extends AttrCalculator {
    private Character character;
    private CharacterBO bo;
    private Player player;
    private RefCharacter ref;

    public CharAttrCalculator(Character character) {
        this.character = character;
        this.player = this.character.player;
        this.bo = character.getBo();
    }

    public int getPower() {
        DressFeature feature = (DressFeature) this.player.getFeature(DressFeature.class);
        List<DressBO> list = feature.getAllDress();
        if (feature.checkDressList(list) != 0) {
            this.updated = false;
        }

        return super.getPower();
    }

    protected void onUpdated() {
        this.character.updateRank(this.power);
    }

    protected void doUpdate() {
        this.ref = (RefCharacter) RefDataMgr.get(RefCharacter.class, Integer.valueOf(this.character.getCharId()));

        this.allAttr.put(Attribute.MaxHP, Integer.valueOf(0));
        this.allAttr.put(Attribute.ATK, Integer.valueOf(0));
        this.allAttr.put(Attribute.DEF, Integer.valueOf(0));
        this.allAttr.put(Attribute.RGS, Integer.valueOf(0));
        this.allAttr.put(Attribute.Hit, Integer.valueOf(0));
        this.allAttr.put(Attribute.Dodge, Integer.valueOf(0));
        this.allAttr.put(Attribute.Critical, Integer.valueOf(0));
        this.allAttr.put(Attribute.Tenacity, Integer.valueOf(0));

        calcBase();
        calcChar();
        calcEquip();
        calcPos();
        calcGuild();
        calcDress();
        calcLingBao();
        calcNewTitle();

        this.power = PowerUtils.getPower(this.allAttr);

        List<Integer> skills = this.bo.getSkillAll();
        for (int i = 0; i < this.ref.SkillList.size(); i++) {
            RefSkill skill = (RefSkill) RefDataMgr.get(RefSkill.class, this.ref.SkillList.get(i));
            if (this.player.getLv() > skill.Require)
                this.power += skill.CE * (((Integer) skills.get(i)).intValue() + 1);
        }
    }

    private void calcBase() {
        RefAttribute refAttribute = (RefAttribute) RefDataMgr.get(RefAttribute.class, String.format("%d%03d", new Object[]{Integer.valueOf(this.ref.id), Integer.valueOf(this.player.getLv())}));
        addAttr(Attribute.MaxHP, refAttribute.MaxHP);
        addAttr(Attribute.ATK, refAttribute.ATK);
        addAttr(Attribute.DEF, refAttribute.DEF);
        addAttr(Attribute.RGS, refAttribute.RGS);
        addAttr(Attribute.Hit, refAttribute.Hit);
        addAttr(Attribute.Dodge, refAttribute.Dodge);
        addAttr(Attribute.Critical, refAttribute.Critical);
        addAttr(Attribute.Tenacity, refAttribute.Tenacity);
    }

    private void calcPos() {
        EquipPos[] equipPos = EquipPos.values();
        for (int i = 1; i < equipPos.length; i++) {
            int artlv = this.bo.getArtifice(i);
            RefStrengthenType refStrengthen = (RefStrengthenType) RefDataMgr.get(RefStrengthenType.class, equipPos[i]);
            if (artlv > 0) {
                RefArtifice ref = (RefArtifice) RefDataMgr.get(RefArtifice.class, Integer.valueOf(artlv));
                if (ref != null) {
                    for (int j = 0; j < ref.Level - 1; j++) {
                        RefArtifice arc = (RefArtifice) RefArtifice.maxLevelMap.get(Integer.valueOf(j + 1));
                        int k = arc.getValue(refStrengthen.ArtificeType.get(j));
                        addAttr(refStrengthen.ArtificeType.get(j), k);
                    }
                    int value = ref.getValue(refStrengthen.ArtificeType.get(ref.Level - 1));
                    addAttr(refStrengthen.ArtificeType.get(ref.Level - 1), value);
                }
            }

            int gemlv = this.bo.getGem(i);
            if (gemlv > 0) {
                int value = ((RefGem) RefDataMgr.get(RefGem.class, Integer.valueOf(gemlv))).getValue(refStrengthen.GemType);
                addAttr(refStrengthen.GemType, value);
            }
        }
    }

    private void calcEquip() {
        List<Equip> equips = this.character.getEquips();
        for (Equip equip : equips) {
            EquipBO equipBO = equip.getBo();
            int pos = equipBO.getPos();
            EquipPos pos2 = EquipPos.values()[pos];

            double starvalue = 0.0D;
            if (this.bo.getStar(pos) > 0) {
                starvalue = ((RefStarInfo) RefDataMgr.get(RefStarInfo.class, Integer.valueOf(this.bo.getStar(pos)))).Attribute / 100.0D;
            }
            int strenLv = this.bo.getStrengthen(pos);
            Attribute strenattr = ((RefStrengthenType) RefDataMgr.get(RefStrengthenType.class, pos2)).StrengthenType;

            RefEquip ref = equip.getRef();
            for (int i = 0; i < ref.AttrTypeList.size(); i++) {
                int basevalue = ((Integer) ref.AttrValueList.get(i)).intValue() + equipBO.getAttr(i);
                Attribute attrtype = ref.AttrTypeList.get(i);
                int attrvalue = basevalue + (int) (basevalue * starvalue);
                if (strenattr == attrtype && strenLv > 0) {
                    attrvalue = (int) (attrvalue + basevalue * ((RefStrengthenInfo) RefDataMgr.get(RefStrengthenInfo.class, Integer.valueOf(strenLv))).Attribute);
                }
                addAttr(attrtype, attrvalue);
            }
        }
    }

    private void calcChar() {
        if (this.bo.getRebirth() > 0) {
            RefRebirth refRebirth = (RefRebirth) RefDataMgr.get(RefRebirth.class, Integer.valueOf(this.bo.getRebirth()));
            addAttr(refRebirth.AttrTypeList, refRebirth.AttrValueList);
        }

        if (this.bo.getMeridian() > 0) {
            RefMeridian refMeridian = (RefMeridian) RefDataMgr.get(RefMeridian.class, Integer.valueOf(this.bo.getMeridian()));
            addAttr(refMeridian.AttrTypeList, refMeridian.AttrValueList);
        }

        if (this.bo.getWing() > 0) {
            RefWing refWing = (RefWing) RefDataMgr.get(RefWing.class, Integer.valueOf(this.bo.getWing()));
            addAttr(refWing.AttrTypeList, refWing.AttrValueList);
        }
    }

    private void calcGuild() {
        GuildMemberFeature feature = (GuildMemberFeature) this.player.getFeature(GuildMemberFeature.class);
        Guild guild = feature.getGuild();
        if (guild != null) {
            for (Guild.GuildSkill skill : feature.getGuildSkillList()) {
                int maxLevel = GuildConfig.getSkillMaxLevel(skill.getSkillid(), guild.getLevel());
                int level = Math.min(maxLevel, skill.getLevel());
                RefGuildSkill ref = (RefGuildSkill) RefDataMgr.get(RefGuildSkill.class, Integer.valueOf(skill.getSkillid()));
                addAttr(ref.Attribute, ref.BaseValue + ref.GrowthValue * (level - 1));
            }
        }
    }

    private void calcDress() {
        for (DressBO bo : this.character.getDresses()) {
            RefDress ref = (RefDress) RefDataMgr.get(RefDress.class, Integer.valueOf(bo.getDressId()));
            addAttr(ref.AttrTypeList, ref.AttrValueList);
        }
    }

    private void calcNewTitle() {
        NewTitleFeature feature = (NewTitleFeature) this.player.getFeature(NewTitleFeature.class);
        for (TitleInfo title : feature.getAllTitleInfo()) {
            Map<Integer, RefNewTitleLevel> refmap = RefNewTitleLevel.getTitleByType(title.getTitleId());
            RefNewTitleLevel ref = refmap.get(Integer.valueOf(title.getLevel()));
            addAttr(ref.AttrTypeList, ref.AttrValueList);
        }
    }

    private void calcLingBao() {
        LingBaoFeature feature = (LingBaoFeature) this.player.getFeature(LingBaoFeature.class);
        RefLingBao ref = (RefLingBao) RefDataMgr.get(RefLingBao.class, Integer.valueOf(feature.getLevel()));
        addAttr(ref.AttrTypeList, ref.AttrValueList);
    }
}

