package business.global.activity.detail;

import business.global.activity.RankActivity;
import business.global.gmmail.MailCenter;
import business.global.rank.RankManager;
import business.global.rank.Record;
import business.player.Player;
import business.player.feature.pvp.DroiyanFeature;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.enums.ConstEnum.RankRewardType;
import core.config.refdata.ref.RefOpenServerRankReward;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;

public class RankGumu extends RankActivity {
    public RankGumu(ActivityBO bo) {
        super(bo);
    }

    @Override
    public void onEnd() {
        for (Record record : RankManager.getInstance().getRankList(RankType.GuMuPower, RankManager.getInstance().getRankSize(RankType.GuMuPower))) {
            if (record != null) {
                int rank = record.getRank();

                for (RefOpenServerRankReward ref : RefOpenServerRankReward.RankRewardByType.get(RankRewardType.GumuRank)) {
                    if (ref.RankRange.within(rank)) {
                        MailCenter.getInstance().sendMail(record.getPid(), ref.MailId, String.valueOf(rank));
                    }
                }
            }
        }
    }

    @Override
    public ActivityType getType() {
        return ActivityType.GumuRank;
    }

    @Override
    public ActivityRecordBO createPlayerActRecord(Player player) {
        ActivityRecordBO bo = new ActivityRecordBO();
        bo.setPid(player.getPid());
        bo.setAid(this.bo.getId());
        bo.setActivity(this.getType().toString());
        int rank = player.getFeature(DroiyanFeature.class).getBo().getPoint();
        bo.setExtInt(0, rank);
        bo.insert();
        return bo;
    }
}
