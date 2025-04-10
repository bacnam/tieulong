package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;

public class RefMarryLevel
extends RefBaseGame
{
@RefField(iskey = true)
public int id;
public int NeedExp;
public int RewardId;

public boolean Assert() {
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}
}

