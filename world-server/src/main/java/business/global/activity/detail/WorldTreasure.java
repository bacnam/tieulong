package business.global.activity.detail;

import business.global.activity.RankActivity;
import business.global.mail.MailCenter;
import business.global.rank.RankManager;
import business.global.rank.Record;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.enums.RankType;
import core.database.world.bo.ActivityBO;
import proto.gameworld.RankAward;

public class WorldTreasure extends RankActivity {
    public WorldTreasure(ActivityBO bo) {
        super(bo);
    }

    @Override
    public RankType getRankType() {
        return RankType.WorldTreasure;
    }

    @Override
    public ActivityType getType() {
        return ActivityType.WorldTreasure;
    }

    @Override
    public void onEnd() {
        for (Record record : RankManager.getInstance().getRankList(RankType.WorldTreasure, RankManager.getInstance().getRankSize(RankType.WorldTreasure))) {
            if (record != null) {
                int rank = record.getRank();

                for (RankAward ref : this.rankrewardList) {
                    if (ref.rankrange.within(rank)) {
                        MailCenter.getInstance()
                            .sendMail(record.getPid(), record.getServerId(), this.mailId, ref.uniformItemIds(), ref.uniformItemCounts(), "" + rank);
                    }
                }
            }
        }
    }
}
