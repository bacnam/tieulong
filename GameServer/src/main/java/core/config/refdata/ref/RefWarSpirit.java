package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import java.util.List;

public class RefWarSpirit
extends RefBaseGame
{
@RefField(iskey = true)
public int id;
public String Name;
public int RNG;
public int SPD;
public int ActiveId;
public int ActiveCount;
public int StarMaterial;
public List<Integer> SkillList;

public boolean Assert() {
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}
}

