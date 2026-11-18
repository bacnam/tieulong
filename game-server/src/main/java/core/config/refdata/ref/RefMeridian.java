package core.config.refdata.ref;

import business.player.feature.character.PowerUtils;
import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.Attribute;
import java.util.List;

public class RefMeridian extends RefBaseGame {
    @RefField(
        iskey = true
    )
    public int id;
    public int Num;
    public int Level;
    public String Name;
    public int Material;
    public List<Attribute> AttrTypeList;
    public List<Integer> AttrValueList;
    @RefField(
        isfield = false
    )
    public int Power = 0;

    @Override
    public boolean Assert() {
        if (!RefAssert.listSize(this.AttrTypeList, this.AttrValueList)) {
            return false;
        } else {
            this.Power = PowerUtils.getPower(this.AttrTypeList, this.AttrValueList);
            return true;
        }
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}
