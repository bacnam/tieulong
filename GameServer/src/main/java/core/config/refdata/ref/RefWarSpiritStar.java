package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.Attribute;
import core.config.refdata.RefDataMgr;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefWarSpiritStar
extends RefBaseGame
{
@RefField(iskey = true)
public int id;
public int WarspiritId;
public int Level;
public int WarspiritNum;
public int ExtraId;
public int ExtraCount;
public List<Attribute> AttrTypeList;
public List<Integer> AttrValueList;
public int SkillLevel;
public int NeedLv;
public static Map<Integer, Map<Integer, RefWarSpiritStar>> spiritMap = new HashMap<>();

public boolean Assert() {
if (!RefAssert.listSize(this.AttrTypeList, this.AttrValueList, new List[0])) {
return false;
}

return true;
}

public boolean AssertAll(RefContainer<?> all) {
for (RefWarSpirit refspirit : RefDataMgr.getAll(RefWarSpirit.class).values()) {
spiritMap.put(Integer.valueOf(refspirit.id), new HashMap<>());
}

for (RefWarSpiritStar ref : all.values()) {
((Map<Integer, RefWarSpiritStar>)spiritMap.get(Integer.valueOf(ref.WarspiritId))).put(Integer.valueOf(ref.Level), ref);
}
return true;
}
}

