package business.global.activity.detail;

import business.global.activity.RankActivity;
import business.global.gmmail.MailCenter;
import business.global.rank.RankManager;
import business.global.rank.Record;
import business.player.Player;
import business.player.feature.pvp.DroiyanFeature;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.RankType;
import core.config.refdata.ref.RefOpenServerRankReward;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;

public class RankTianLong
        extends RankActivity {
    public RankTianLong(ActivityBO bo) {
        super(bo);
    }

    public void onEnd() {
        for (Record record : RankManager.getInstance().getRankList(RankType.TianLongPower, RankManager.getInstance().getRankSize(RankType.TianLongPower))) {
            if (record == null)
                continue;
            int rank = record.getRank();
            for (RefOpenServerRankReward ref : RefOpenServerRankReward.RankRewardByType.get(ConstEnum.RankRewardType.TianLongRank)) {
                if (!ref.RankRange.within(rank))
                    continue;
                MailCenter.getInstance().sendMail(record.getPid(), ref.MailId, new String[]{(new StringBuilder(String.valueOf(rank))).toString()});
            }
        }
    }

    public ActivityType getType() {
        return ActivityType.TianLongRank;
    }

    public ActivityRecordBO createPlayerActRecord(Player player) {
        ActivityRecordBO bo = new ActivityRecordBO();
        bo.setPid(player.getPid());
        bo.setAid(this.bo.getId());
        bo.setActivity(getType().toString());
        int rank = ((DroiyanFeature) player.getFeature(DroiyanFeature.class)).getBo().getPoint();
        bo.setExtInt(0, rank);
        bo.insert();
        return bo;
    }
}

