package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;

public class RefStarInfo
extends RefBaseGame
{
@RefField(iskey = true)
public int id;
public int Attribute;
public int Gold;
public int Material;

public boolean Assert() {
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}
}

