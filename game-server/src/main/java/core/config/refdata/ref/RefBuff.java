package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.Attribute;
import java.util.List;

public class RefBuff extends RefBaseGame {
    @RefField(
        iskey = true
    )
    public int id;
    public List<Attribute> AttrTypeList;
    public List<Double> AttrValueList;
    public List<Double> AttrIncList;
    public List<Boolean> AttrFixedList;
    public int Time;
    public int CD;
    public int HPCondition;

    @Override
    public boolean Assert() {
        return RefAssert.listSize(this.AttrTypeList, this.AttrValueList, this.AttrIncList, this.AttrFixedList);
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}
