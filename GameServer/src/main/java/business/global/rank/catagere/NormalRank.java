package business.global.rank.catagere;

import business.global.rank.Rank;
import business.global.rank.Ranks;
import business.player.RobotManager;
import com.zhonglian.server.common.enums.RankType;

@Ranks({RankType.Droiyan, RankType.Level, RankType.WingLevel, RankType.Dungeon, RankType.Power, RankType.TianLongPower, RankType.GuMuPower, RankType.XiaoYaoPower, RankType.DrawPoint, RankType.Lovers, RankType.Artifice})
public class NormalRank
        extends Rank {
    public NormalRank(RankType type) {
        super(type, (left, right) -> (left.getValue() != right.getValue()) ? ((right.getValue() > left.getValue()) ? 1 : -1) : ((left.getExt1() != right.getExt1()) ? ((right.getExt1() > left.getExt1()) ? 1 : -1) : (left.getUpdateTime() - right.getUpdateTime())));
    }

    protected boolean filter(long ownerid) {
        return RobotManager.getInstance().isRobot(ownerid);
    }
}

