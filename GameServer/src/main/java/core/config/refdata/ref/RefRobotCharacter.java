package core.config.refdata.ref;

import BaseCommon.CommLog;
import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import com.zhonglian.server.common.enums.EquipPos;
import core.config.refdata.RefDataMgr;

import java.util.List;

public class RefRobotCharacter
        extends RefBaseGame {
    @RefField(iskey = true)
    public int id;
    public int CharId;
    public NumberRange Wing;
    public List<Integer> Strengthen;
    public List<Integer> Equip;

    public boolean Assert() {
        if (this.Strengthen.size() >= (EquipPos.values()).length) {
            CommLog.error("角色的强化部位只有{}个，实际配置{}个", Integer.valueOf((EquipPos.values()).length - 1), Integer.valueOf(this.Strengthen.size()));
            return false;
        }
        if (!RefAssert.inRef(this.Equip, RefEquip.class, new Object[]{Integer.valueOf(0)})) {
            return false;
        }
        if (!RefAssert.inRef(Integer.valueOf(this.CharId), RefCharacter.class, new Object[0])) {
            return false;
        }
        if (this.Wing.getTop() < this.Wing.getLow() || this.Wing.getTop() >= RefDataMgr.size(RefWing.class)) {
            CommLog.error("角色的翅膀等级上下限配置错误，或者超出最大翅膀等级");
            return false;
        }
        if (this.Equip.size() >= (EquipPos.values()).length) {
            CommLog.error("角色的装备部位只有{}个，实际配置{}个", Integer.valueOf((EquipPos.values()).length - 1), Integer.valueOf(this.Equip.size()));
            return false;
        }
        boolean rtn = true;
        for (int i = 0; i < this.Equip.size(); i++) {
            if (((Integer) this.Equip.get(i)).intValue() != 0) {

                RefEquip equip = (RefEquip) RefDataMgr.get(RefEquip.class, this.Equip.get(i));
                EquipPos pos = EquipPos.values()[i + 1];
                if (!equip.couldEquipOn(pos)) {
                    CommLog.error("装备{}不能装备在第{}个位置{}上", new Object[]{Integer.valueOf(equip.id), Integer.valueOf(i), pos});
                    rtn = false;
                }
                if (equip.CharID != this.CharId) {
                    CommLog.error("装备{}不能装备在角色{}上", Integer.valueOf(equip.id), Integer.valueOf(this.CharId));
                    rtn = false;
                }
            }
        }
        return rtn;
    }

    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}

