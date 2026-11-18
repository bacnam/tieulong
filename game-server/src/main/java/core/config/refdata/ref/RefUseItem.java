package core.config.refdata.ref;

import business.player.item.Reward;
import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import com.zhonglian.server.common.enums.ConstEnum.BoxMatcherType;
import java.util.ArrayList;

public class RefUseItem extends RefBaseGame {
    @RefField(
        iskey = true
    )
    public int ID;
    public BoxMatcherType MatcherType;
    public int Times;
    public ArrayList<Integer> Probabilities;
    public ArrayList<Integer> Items;
    public ArrayList<NumberRange> NumberRange;

    public Reward reward() {
        return new Reward();
    }

    @Override
    public boolean Assert() {
        return !RefAssert.inRef(this.Items, RefUniformItem.class) ? false : RefAssert.listSize(this.Probabilities, this.Items, this.NumberRange);
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}
