package core.config.refdata.ref;

import business.player.item.Reward;
import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import com.zhonglian.server.common.enums.ConstEnum;

import java.util.ArrayList;
import java.util.List;

public class RefUseItem
        extends RefBaseGame {
    @RefField(iskey = true)
    public int ID;
    public ConstEnum.BoxMatcherType MatcherType;
    public int Times;
    public ArrayList<Integer> Probabilities;
    public ArrayList<Integer> Items;
    public ArrayList<NumberRange> NumberRange;

    public Reward reward() {
        Reward reward = new Reward();
        return reward;
    }

    public boolean Assert() {
        if (!RefAssert.inRef(this.Items, RefUniformItem.class, new Object[0])) {
            return false;
        }
        if (!RefAssert.listSize(this.Probabilities, this.Items, new List[]{this.NumberRange})) {
            return false;
        }
        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}

