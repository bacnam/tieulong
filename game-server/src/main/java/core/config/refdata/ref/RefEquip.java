package core.config.refdata.ref;

import business.player.item.ItemUtils;
import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.Attribute;
import com.zhonglian.server.common.enums.EquipPos;
import com.zhonglian.server.common.enums.EquipType;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.Quality;
import core.config.refdata.RefDataMgr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefEquip extends RefBaseGame {
    @RefField(
        iskey = true
    )
    public int id;
    public String Name;
    public int CharID;
    public EquipType Type;
    public int Level;
    public List<Attribute> AttrTypeList;
    public List<Integer> AttrValueList;
    @RefField(
        isfield = false
    )
    private Quality quality;
    @RefField(
        isfield = false
    )
    private List<EquipPos> equipPos;
    @RefField(
        isfield = false
    )
    public static Map<String, RefEquip> redEquip = new HashMap<>();

    public boolean couldEquipOn(EquipPos p) {
        return this.validEquipPos().contains(p);
    }

    public Quality getQuality() {
        if (this.quality == null) {
            int uniformid = ItemUtils.getUniformId(PrizeType.Equip, this.id);
            int q = RefDataMgr.get(RefUniformItem.class, uniformid).Quality;
            this.quality = Quality.values()[q];
        }

        return this.quality;
    }

    @Override
    public boolean Assert() {
        if (!RefAssert.inRef(this.CharID, RefCharacter.class)) {
            return false;
        } else if (!RefAssert.listSize(this.AttrTypeList, this.AttrValueList)) {
            return false;
        } else {
            if (this.getQuality() == Quality.Red) {
                String key = this.CharID + this.Type.toString() + this.Level;
                redEquip.put(key, this);
            }

            return true;
        }
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }

    public List<EquipPos> validEquipPos() {
        if (this.equipPos == null) {
            this.equipPos = new ArrayList<>();

            EquipPos[] var4;
            for (EquipPos pos : var4 = EquipPos.values()) {
                if (pos.toString().startsWith(this.Type.name())) {
                    this.equipPos.add(pos);
                }
            }
        }

        return this.equipPos;
    }
}
