package business.player.feature.character;

import business.player.Player;
import com.zhonglian.server.common.enums.Attribute;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefSkill;
import core.config.refdata.ref.RefWarSpirit;
import core.config.refdata.ref.RefWarSpiritLv;
import core.config.refdata.ref.RefWarSpiritStar;
import core.config.refdata.ref.RefWarSpiritTalent;
import core.database.game.bo.WarSpiritBO;

public class SpiritAttrCalculator extends AttrCalculator {
    private WarSpirit spirit;
    private WarSpiritBO bo;
    private Player player;

    public SpiritAttrCalculator(WarSpirit spirit) {
        this.spirit = spirit;
        this.player = this.spirit.player;
        this.spirit = spirit;
        this.bo = spirit.getBo();
    }

    @Override
    protected void doUpdate() {
        this.allAttr.put(Attribute.MaxHP, 0);
        this.allAttr.put(Attribute.ATK, 0);
        this.allAttr.put(Attribute.DEF, 0);
        this.allAttr.put(Attribute.RGS, 0);
        this.allAttr.put(Attribute.Hit, 0);
        this.allAttr.put(Attribute.Dodge, 0);
        this.allAttr.put(Attribute.Critical, 0);
        this.allAttr.put(Attribute.Tenacity, 0);
        int lv = this.player.getPlayerBO().getWarspiritLv();
        RefWarSpiritLv refLv = RefDataMgr.get(RefWarSpiritLv.class, lv);
        this.addAttr(refLv.AttrTypeList, refLv.AttrValueList);
        int telent = this.player.getPlayerBO().getWarspiritTalent();
        RefWarSpiritTalent refTalent = RefDataMgr.get(RefWarSpiritTalent.class, telent);
        this.addAttr(refTalent.AttrTypeList, refTalent.AttrValueList);
        this.calcStar();
        this.power = PowerUtils.getPower(this.allAttr);
        RefWarSpirit ref = RefDataMgr.get(RefWarSpirit.class, this.bo.getSpiritId());

        for (int i = 0; i < ref.SkillList.size(); i++) {
            int skilllv = 0;
            if (ref.SkillList.get(i) != 0) {
                skilllv = this.bo.getSkill();
            }

            RefSkill skill = RefDataMgr.get(RefSkill.class, ref.SkillList.get(i));
            this.power = this.power + skill.CE * (skilllv + 1);
        }
    }

    private void calcStar() {
        RefWarSpiritStar ref = RefWarSpiritStar.spiritMap.get(this.bo.getSpiritId()).get(this.bo.getStar());
        this.addAttr(ref.AttrTypeList, ref.AttrValueList);
    }
}
