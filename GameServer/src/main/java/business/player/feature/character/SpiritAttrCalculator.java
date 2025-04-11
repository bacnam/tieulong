package business.player.feature.character;

import business.player.Player;
import com.zhonglian.server.common.enums.Attribute;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.*;
import core.database.game.bo.WarSpiritBO;

import java.util.Map;

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

    protected void doUpdate() {
        this.allAttr.put(Attribute.MaxHP, Integer.valueOf(0));
        this.allAttr.put(Attribute.ATK, Integer.valueOf(0));
        this.allAttr.put(Attribute.DEF, Integer.valueOf(0));
        this.allAttr.put(Attribute.RGS, Integer.valueOf(0));
        this.allAttr.put(Attribute.Hit, Integer.valueOf(0));
        this.allAttr.put(Attribute.Dodge, Integer.valueOf(0));
        this.allAttr.put(Attribute.Critical, Integer.valueOf(0));
        this.allAttr.put(Attribute.Tenacity, Integer.valueOf(0));

        int lv = this.player.getPlayerBO().getWarspiritLv();
        RefWarSpiritLv refLv = (RefWarSpiritLv) RefDataMgr.get(RefWarSpiritLv.class, Integer.valueOf(lv));
        addAttr(refLv.AttrTypeList, refLv.AttrValueList);

        int telent = this.player.getPlayerBO().getWarspiritTalent();
        RefWarSpiritTalent refTalent = (RefWarSpiritTalent) RefDataMgr.get(RefWarSpiritTalent.class, Integer.valueOf(telent));
        addAttr(refTalent.AttrTypeList, refTalent.AttrValueList);

        calcStar();

        this.power = PowerUtils.getPower(this.allAttr);

        RefWarSpirit ref = (RefWarSpirit) RefDataMgr.get(RefWarSpirit.class, Integer.valueOf(this.bo.getSpiritId()));
        for (int i = 0; i < ref.SkillList.size(); i++) {
            int skilllv = 0;
            if (((Integer) ref.SkillList.get(i)).intValue() != 0) {
                skilllv = this.bo.getSkill();
            }
            RefSkill skill = (RefSkill) RefDataMgr.get(RefSkill.class, ref.SkillList.get(i));
            this.power += skill.CE * (skilllv + 1);
        }
    }

    private void calcStar() {
        RefWarSpiritStar ref = (RefWarSpiritStar) ((Map) RefWarSpiritStar.spiritMap.get(Integer.valueOf(this.bo.getSpiritId()))).get(Integer.valueOf(this.bo.getStar()));
        addAttr(ref.AttrTypeList, ref.AttrValueList);
    }
}

