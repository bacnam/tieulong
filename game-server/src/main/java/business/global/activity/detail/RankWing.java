package business.global.activity.detail;

import business.global.activity.RankActivity;
import business.global.gmmail.MailCenter;
import business.global.rank.RankManager;
import business.global.rank.Record;
import business.player.Player;
import business.player.feature.character.CharFeature;
import business.player.feature.character.Character;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.enums.ConstEnum.VIPGiftType;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;

public class RankWing extends RankActivity {
    public RankWing(ActivityBO bo) {
        super(bo);
    }

    @Override
    public void onEnd() {
        for (Record record : RankManager.getInstance().getRankList(RankType.WingLevel, RankManager.getInstance().getRankSize(RankType.WingLevel))) {
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
        return ActivityType.WingRank;
    }

    @Override
    public VIPGiftType getAwardType() {
        return VIPGiftType.WingRank;
    }

    @Override
    public ActivityRecordBO createPlayerActRecord(Player player) {
        ActivityRecordBO bo = new ActivityRecordBO();
        bo.setPid(player.getPid());
        bo.setAid(this.bo.getId());
        bo.setActivity(this.getType().toString());
        int wingLevel = 0;

        for (Character charac : player.getFeature(CharFeature.class).getAll().values()) {
            wingLevel += charac.getBo().getWing();
        }

        bo.setExtInt(0, wingLevel);
        bo.insert();
        return bo;
    }
}
