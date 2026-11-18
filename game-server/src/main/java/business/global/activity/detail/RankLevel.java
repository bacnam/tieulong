package business.global.activity.detail;

import business.global.activity.RankActivity;
import business.global.gmmail.MailCenter;
import business.global.rank.RankManager;
import business.global.rank.Record;
import business.player.Player;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.enums.ConstEnum.VIPGiftType;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;

public class RankLevel extends RankActivity {
    public RankLevel(ActivityBO bo) {
        super(bo);
    }

    @Override
    public void onEnd() {
        for (Record record : RankManager.getInstance().getRankList(RankType.Level, RankManager.getInstance().getRankSize(RankType.Level))) {
            if (record != null) {
                int rank = record.getRank();

                for (RankActivity.RankAward ref : this.rankrewardList) {
                    if (ref.rankrange.within(rank)) {
                        MailCenter.getInstance().sendMail(record.getPid(), this.ref.MailSender, this.ref.MailTitle, this.ref.MailContent, ref.reward);
                    }
                }
            }
        }
    }

    @Override
    public ActivityType getType() {
        return ActivityType.LevelRank;
    }

    @Override
    public VIPGiftType getAwardType() {
        return VIPGiftType.LevelRank;
    }

    @Override
    public ActivityRecordBO createPlayerActRecord(Player player) {
        ActivityRecordBO bo = new ActivityRecordBO();
        bo.setPid(player.getPid());
        bo.setAid(this.bo.getId());
        bo.setActivity(this.getType().toString());
        int rank = player.getLv();
        bo.setExtInt(0, rank);
        bo.insert();
        return bo;
    }
}
