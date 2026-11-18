package core.config.refdata.ref;

import BaseCommon.CommLog;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.PrizeType;

public class RefUniformItem extends RefBaseWorld {
    @RefField(
        iskey = true
    )
    public int id;
    public PrizeType Type;
    public int ItemID;
    public String Name;
    public int Price;
    public int RecoveryReward;
    public int Quality;

    @Override
    public boolean Assert() {
        if (this.Type == PrizeType.Equip && this.Price != 0) {
            CommLog.error(String.format("UniformID=%s的类型为装备，不能配置出售价格，如需价格请配置在对应的Equip表中", this.id));
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}
