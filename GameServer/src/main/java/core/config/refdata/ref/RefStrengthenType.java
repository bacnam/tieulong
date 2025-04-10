package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.Attribute;
import com.zhonglian.server.common.enums.EquipPos;
import java.util.List;

public class RefStrengthenType
extends RefBaseGame
{
@RefField(iskey = true)
public EquipPos id;
public Attribute StrengthenType;
public Attribute GemType;
public List<Attribute> ArtificeType;

public boolean Assert() {
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}
}

