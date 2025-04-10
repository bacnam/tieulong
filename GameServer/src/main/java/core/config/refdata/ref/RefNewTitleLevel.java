package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.Attribute;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefNewTitleLevel
extends RefBaseGame
{
@RefField(iskey = true)
public int id;
public int Level;
public int TitleId;
public String Name;
public int Quality;
public List<Integer> UniformIdList;
public List<Integer> UniformCountList;
public List<Attribute> AttrTypeList;
public List<Integer> AttrValueList;
@RefField(iskey = false)
private static Map<Integer, Map<Integer, RefNewTitleLevel>> titleMap = new HashMap<>();

public boolean Assert() {
if (!RefAssert.listSize(this.AttrTypeList, this.AttrValueList, new List[0])) {
return false;
}
Map<Integer, RefNewTitleLevel> map = titleMap.get(Integer.valueOf(this.TitleId));
if (map == null) {
map = new HashMap<>();
titleMap.put(Integer.valueOf(this.TitleId), map);
} 
map.put(Integer.valueOf(this.Level), this);
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}

public static Map<Integer, RefNewTitleLevel> getTitleByType(int TitleId) {
return titleMap.get(Integer.valueOf(TitleId));
}
}

