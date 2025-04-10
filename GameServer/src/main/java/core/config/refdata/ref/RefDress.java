package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.Attribute;
import com.zhonglian.server.common.enums.DressType;
import java.util.List;

public class RefDress
extends RefBaseGame
{
@RefField(iskey = true)
public int id;
public DressType Type;
public int CharId;
public String Name;
public int TimeLimit;
public int Material;
public int Count;
public List<Attribute> AttrTypeList;
public List<Integer> AttrValueList;

public boolean Assert() {
if (!RefAssert.listSize(this.AttrTypeList, this.AttrValueList, new List[0])) {
return false;
}
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}
}

