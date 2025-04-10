package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.CharacterType;
import java.util.List;

public class RefCharacter
extends RefBaseGame
{
@RefField(iskey = true)
public int id;
public CharacterType Type;
public String Name;
public int RNG;
public int SPD;
public List<Integer> SkillList;

public boolean Assert() {
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}
}

