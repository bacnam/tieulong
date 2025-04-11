package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import core.config.refdata.RefDataMgr;

public class RefDroiyanTreasure
        extends RefBaseGame {
    @RefField(iskey = true)
    public int id;
    public int LevelMax;
    public int Time;
    public int Rate;
    public int Reward;
    public int NormalWeight;
    public int RevengeWeight;
    public int Quality;

    public static RefDroiyanTreasure findTreature(int range) {
        RefContainer<RefDroiyanTreasure> all = RefDataMgr.getAll(RefDroiyanTreasure.class);
        RefDroiyanTreasure preOne = null;
        int total_range = 0;
        for (RefDroiyanTreasure ref : all.values()) {
            total_range += ref.Rate;
            if (range <= total_range) {
                preOne = ref;
                break;
            }
        }
        return preOne;
    }

    public boolean Assert() {
        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}

