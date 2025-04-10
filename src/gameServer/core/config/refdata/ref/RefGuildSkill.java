package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.Attribute;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.utils.Maps;
import java.util.Map;

public class RefGuildSkill
extends RefBaseGame
{
@RefField(iskey = true)
public int id;
public Attribute Attribute;
public String Description;
public int BaseValue;
public int GrowthValue;
public int UnlockLevel;
@RefField(isfield = false)
public static Map<ConstEnum.BuffType, RefGuildSkill> buffMap = Maps.newMap();

public boolean Assert() {
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}

public static RefGuildSkill getGuildSkillRef(ConstEnum.BuffType buffType) {
return buffMap.get(buffType);
}

public int getSkillValue(int level) {
if (level == 0) {
return 0;
}
return this.BaseValue + this.GrowthValue * (level - 1);
}
}

