package business.global.rank.catagere;

import business.global.rank.Ranks;
import com.zhonglian.server.common.enums.RankType;

@Ranks({RankType.WorldBoss1, RankType.WorldBoss2, RankType.WorldBoss3, RankType.WorldBoss4})
public class WorldBossRank
extends NormalRank
{
public WorldBossRank(RankType type) {
super(type);
}

protected boolean filter(long ownerid) {
return false;
}
}

