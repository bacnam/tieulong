package core.config.refdata.ref;

import BaseCommon.CommLog;
import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import com.zhonglian.server.common.enums.EquipPos;
import core.config.refdata.RefDataMgr;
import java.util.List;

public class RefRobotCharacter extends RefBaseGame {
    @RefField(
        iskey = true
    )
    public int id;
    public int CharId;
    public NumberRange Wing;
    public List<Integer> Strengthen;
    public List<Integer> Equip;

    @Override
    public boolean Assert() {
        if (this.Strengthen.size() >= EquipPos.values().length) {
            CommLog.error("角色的强化部位只有{}个，实际配置{}个", EquipPos.values().length - 1, this.Strengthen.size());
            return false;
        } else if (!RefAssert.inRef(this.Equip, RefEquip.class, 0)) {
            return false;
        } else if (!RefAssert.inRef(this.CharId, RefCharacter.class)) {
            return false;
        } else if (this.Wing.getTop() >= this.Wing.getLow() && this.Wing.getTop() < RefDataMgr.size(RefWing.class)) {
            if (this.Equip.size() >= EquipPos.values().length) {
                CommLog.error("角色的装备部位只有{}个，实际配置{}个", EquipPos.values().length - 1, this.Equip.size());
                return false;
            } else {
                boolean rtn = true;

                for (int i = 0; i < this.Equip.size(); i++) {
                    if (this.Equip.get(i) != 0) {
                        RefEquip equip = RefDataMgr.get(RefEquip.class, this.Equip.get(i));
                        EquipPos pos = EquipPos.values()[i + 1];
                        if (!equip.couldEquipOn(pos)) {
                            CommLog.error("装备{}不能装备在第{}个位置{}上", equip.id, i, pos);
                            rtn = false;
                        }

                        if (equip.CharID != this.CharId) {
                            CommLog.error("装备{}不能装备在角色{}上", equip.id, this.CharId);
                            rtn = false;
                        }
                    }
                }

                return rtn;
            }
        } else {
            CommLog.error("角色的翅膀等级上下限配置错误，或者超出最大翅膀等级");
            return false;
        }
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}
