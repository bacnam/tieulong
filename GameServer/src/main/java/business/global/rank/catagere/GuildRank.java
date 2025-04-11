package business.global.rank.catagere;

import business.global.rank.Ranks;
import com.zhonglian.server.common.enums.RankType;

@Ranks({RankType.Guild})
public class GuildRank
        extends NormalRank {
    public GuildRank(RankType type) {
        super(type);
    }

    protected boolean filter(long ownerid) {
        return false;
    }
}

