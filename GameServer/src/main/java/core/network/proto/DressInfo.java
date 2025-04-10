package core.network.proto;

import com.zhonglian.server.common.utils.CommTime;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefDress;
import core.database.game.bo.DressBO;

public class DressInfo
{
DressBO dressbo;
int dressLeftTime;

public DressInfo(DressBO bo) {
RefDress ref = (RefDress)RefDataMgr.get(RefDress.class, Integer.valueOf(bo.getDressId()));
this.dressbo = bo;
if (ref.TimeLimit > 0 && bo.getEquipTime() != 0) {
this.dressLeftTime = Math.max(0, ref.TimeLimit - CommTime.nowSecond() - bo.getEquipTime());
} else {
this.dressLeftTime = -1;
} 
}
}

