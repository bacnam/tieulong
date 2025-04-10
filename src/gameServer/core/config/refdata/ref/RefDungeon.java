package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;

public class RefDungeon
extends RefBaseGame
{
@RefField(iskey = true)
public int id;
public int Exp;
public int Gold;
public int Drop;
public int BossDrop;
public int BossID;

public boolean Assert() {
if (!RefAssert.inRef(Integer.valueOf(this.Drop), RefReward.class, new Object[0])) {
return false;
}
if (!RefAssert.inRef(Integer.valueOf(this.BossDrop), RefReward.class, new Object[0])) {
return false;
}
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}
}

