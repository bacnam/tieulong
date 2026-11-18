package business.global.guild.rank.catagere;

import business.global.guild.GuildRank;
import business.global.guild.GuildRanks;
import business.player.RobotManager;
import com.zhonglian.server.common.enums.GuildRankType;

@GuildRanks({GuildRankType.GuildBoss})
public class GuildNormalRank extends GuildRank {
    public GuildNormalRank(GuildRankType type) {
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
        return RobotManager.getInstance().isRobot(ownerid);
    }
}
