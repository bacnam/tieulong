package business.player.feature.character;

import business.player.Player;
import com.zhonglian.server.common.enums.Attribute;
import com.zhonglian.server.common.enums.EquipPos;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefEquip;
import core.database.game.bo.EquipBO;
import java.util.HashMap;
import java.util.Map;

public class Equip {
    private EquipBO bo;
    private RefEquip ref;
    private Character owner;
    private Player player;
    private Integer basepower;

    Equip(Player player, EquipBO bo) {
        this.player = player;
        this.bo = bo;
        this.ref = RefDataMgr.get(RefEquip.class, bo.getEquipId());
    }

    public Player getPlayer() {
        return this.player;
    }

    public EquipBO getBo() {
        return this.bo;
    }

    public void setOwner(Character character, EquipPos pos) {
        this.owner = character;
        if (character != null) {
            character.equip(this, pos);
        }
    }

    public Character getOwner() {
        return this.owner;
    }

    public EquipPos getPos() {
        return this.owner == null ? EquipPos.None : EquipPos.values()[this.bo.getPos()];
    }

    public int getEquipId() {
        return this.bo.getEquipId();
    }

    public long getSid() {
        return this.bo.getId();
    }

    public int getLevel() {
        return this.ref.Level;
    }

    public RefEquip getRef() {
        return this.ref;
    }

    public int getBasePower() {
        return this.basepower != null ? this.basepower : this.updateBasePower();
    }

    private int updateBasePower() {
        Map<Attribute, Integer> attrs = new HashMap<>();

        for (int i = 0; i < this.ref.AttrTypeList.size(); i++) {
            attrs.put(this.ref.AttrTypeList.get(i), this.ref.AttrValueList.get(i) + this.bo.getAttr(i));
        }

        return this.basepower = PowerUtils.getPower(attrs);
    }

    public void onAttrChanged() {
        this.owner.onAttrChanged();
    }

    public void saveOwner(Character character, EquipPos pos) {
        this.owner = character;
        this.bo.setCharId(character == null ? 0 : character.getCharId());
        this.bo.setPos(pos.ordinal());
        this.bo.saveAll();
    }
}
