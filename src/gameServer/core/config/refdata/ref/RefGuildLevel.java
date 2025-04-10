package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;

public class RefGuildLevel
extends RefBaseGame
{
@RefField(iskey = true)
public int id;
public int UpgradeValue;
public int MaxMemberAmount;

public boolean Assert() {
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}
}

