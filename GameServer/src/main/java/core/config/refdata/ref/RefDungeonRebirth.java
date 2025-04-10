package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.Attribute;
import java.util.List;

public class RefDungeonRebirth
extends RefBaseGame
{
@RefField(iskey = true)
public int id;
public List<Attribute> AttrList;
public List<Integer> AttrValue;
public int Cost;

public boolean Assert() {
if (!RefAssert.listSize(this.AttrList, this.AttrValue, new List[0])) {
return false;
}
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}
}

