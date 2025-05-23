package core.config.refdata.ref;

import business.player.feature.character.PowerUtils;
import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.Attribute;
import java.util.List;

public class RefWing
extends RefBaseGame
{
@RefField(iskey = true)
public int id;
public int Level;
public int Star;
public String Name;
public int Exp;
public List<Attribute> AttrTypeList;
public List<Integer> AttrValueList;
public int Gold;
public int GoldExp;
public float GoldCrit;
public int Material;
public int MaterialExp;
public float MaterialCrit;
@RefField(isfield = false)
public int Power = 0;

public boolean Assert() {
if (!RefAssert.listSize(this.AttrTypeList, this.AttrValueList, new List[0])) {
return false;
}
this.Power = PowerUtils.getPower(this.AttrTypeList, this.AttrValueList);
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}
}

