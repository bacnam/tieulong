package business.global.guild.rank.catagere;

import business.global.guild.GuildRank;
import business.global.guild.GuildRanks;
import business.player.RobotManager;
import com.zhonglian.server.common.enums.GuildRankType;
import core.database.game.bo.GuildRankRecordBO;

@GuildRanks({GuildRankType.GuildBoss})
public class GuildNormalRank
extends GuildRank
{
public GuildNormalRank(GuildRankType type) {
super(type, (left, right) -> (left.getValue() != right.getValue()) ? ((right.getValue() > left.getValue()) ? 1 : -1) : ((left.getExt1() != right.getExt1()) ? ((right.getExt1() > left.getExt1()) ? 1 : -1) : (left.getUpdateTime() - right.getUpdateTime())));
}

protected boolean filter(long ownerid) {
return RobotManager.getInstance().isRobot(ownerid);
}
}

