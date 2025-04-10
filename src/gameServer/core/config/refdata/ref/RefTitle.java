package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import com.zhonglian.server.common.enums.Attribute;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.Title;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefTitle
extends RefBaseGame
{
@RefField(iskey = true)
public int id;
public Title Type;
public String Name;
public NumberRange NumRange;
public int Quality;
public int TimeLimit;
public List<Attribute> AttrTypeList;
public List<Integer> AttrValueList;
public int MailId;
public ConstEnum.AchieveReset Reset;
@RefField(iskey = false)
private static Map<Title, RefTitle> titleMap = new HashMap<>();

public boolean Assert() {
if (!RefAssert.listSize(this.AttrTypeList, this.AttrValueList, new List[0])) {
return false;
}
titleMap.put(this.Type, this);
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}

public static RefTitle getTitleByType(Title Type) {
return titleMap.get(Type);
}
}

