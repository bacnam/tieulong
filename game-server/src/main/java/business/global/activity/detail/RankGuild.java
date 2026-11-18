package business.global.activity.detail;

import business.global.activity.RankActivity;
import business.global.gmmail.MailCenter;
import business.global.guild.Guild;
import business.global.guild.GuildMgr;
import business.global.rank.RankManager;
import business.global.rank.Record;
import business.player.Player;
import business.player.feature.guild.GuildMemberFeature;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.enums.ConstEnum.VIPGiftType;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;

public class RankGuild extends RankActivity {
    public RankGuild(ActivityBO bo) {
        super(bo);
    }

    @Override
    public void onEnd() {
        for (Record record : RankManager.getInstance().getRankList(RankType.Guild, RankManager.getInstance().getRankSize(RankType.Guild))) {
            if (record != null) {
                int rank = record.getRank();

                for (RankActivity.RankAward ref : this.rankrewardList) {
                    if (ref.rankrange.within(rank)) {
                        Guild guild = GuildMgr.getInstance().getGuild(record.getPid());
                        if (guild != null) {
                            for (long pid : guild.getMembers()) {
                                MailCenter.getInstance().sendMail(pid, this.ref.MailSender, this.ref.MailTitle, this.ref.MailContent, ref.reward);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public ActivityType getType() {
        return ActivityType.GuildRank;
    }

    @Override
    public VIPGiftType getAwardType() {
        return VIPGiftType.GuildRank;
    }

    @Override
    public ActivityRecordBO createPlayerActRecord(Player player) {
        ActivityRecordBO bo = new ActivityRecordBO();
        bo.setPid(player.getPid());
        bo.setAid(this.bo.getId());
        bo.setActivity(this.getType().toString());
        Guild guild = player.getFeature(GuildMemberFeature.class).getGuild();
        if (guild != null) {
            int power = guild.getPower();
            bo.setExtInt(0, power);
        }

        bo.insert();
        return bo;
    }
}
