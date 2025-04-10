package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.utils.Maps;
import java.util.Map;

public class RefSkipDungeon
extends RefBaseGame
{
@RefField(iskey = true)
public int id;
public int Level;
public int Cost;
public static Map<Integer, RefSkipDungeon> LevelMap = Maps.newConcurrentHashMap();

public boolean Assert() {
LevelMap.put(Integer.valueOf(this.Level), this);

return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}
}

