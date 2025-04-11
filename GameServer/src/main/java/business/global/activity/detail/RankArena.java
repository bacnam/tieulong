package business.global.activity.detail;

import business.global.activity.RankActivity;
import business.global.arena.ArenaManager;
import business.global.gmmail.MailCenter;
import business.global.rank.RankManager;
import business.global.rank.Record;
import business.player.Player;
import business.player.RobotManager;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.RankType;
import core.config.refdata.ref.RefOpenServerRankReward;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;

public class RankArena
        extends RankActivity {
    public RankArena(ActivityBO bo) {
        super(bo);
    }

    public void onEnd() {
        for (Record record : RankManager.getInstance().getRankList(RankType.Arena, RankManager.getInstance().getRankSize(RankType.Arena))) {
            if (record == null)
                continue;
            if (RobotManager.getInstance().isRobot(record.getPid()))
                continue;
            int rank = record.getRank();
            for (RefOpenServerRankReward ref : RefOpenServerRankReward.RankRewardByType.get(ConstEnum.RankRewardType.ArenaRank)) {
                if (!ref.RankRange.within(rank))
                    continue;
                MailCenter.getInstance().sendMail(record.getPid(), ref.MailId, new String[]{(new StringBuilder(String.valueOf(rank))).toString()});
            }
        }
    }

    public ActivityType getType() {
        return ActivityType.ArenaRank;
    }

    public ActivityRecordBO createPlayerActRecord(Player player) {
        ActivityRecordBO bo = new ActivityRecordBO();
        bo.setPid(player.getPid());
        bo.setAid(this.bo.getId());
        bo.setActivity(getType().toString());
        int rank = ArenaManager.getInstance().getOrCreate(player.getPid()).getRank();
        bo.setExtInt(0, rank);
        bo.insert();
        return bo;
    }
}

