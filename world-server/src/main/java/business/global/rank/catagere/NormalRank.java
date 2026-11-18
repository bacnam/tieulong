package business.global.rank.catagere;

import business.global.rank.Rank;
import business.global.rank.Ranks;
import com.zhonglian.server.common.enums.RankType;

@Ranks({RankType.WorldTreasure, RankType.WorldRecharge, RankType.WorldConsume})
public class NormalRank extends Rank {
    public NormalRank(RankType type) {
        super(type, (left, right) -> {
            if (left.getValue() != right.getValue()) {
                return right.getValue() > left.getValue() ? 1 : -1;
            } else if (left.getExt1() != right.getExt1()) {
                return right.getExt1() > left.getExt1() ? 1 : -1;
            } else {
                return left.getUpdateTime() - right.getUpdateTime();
            }
        });
    }

    @Override
    protected boolean filter(long ownerid) {
        return false;
    }
}
