package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;

public class RefPlayerName
extends RefBaseGame
{
@RefField(iskey = true)
public int id;
public String PrefixName;
public String MidName;
public String SuffixName;

public boolean Assert() {
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}
}

