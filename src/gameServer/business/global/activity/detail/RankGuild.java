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
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.RankType;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;
import java.util.Iterator;

public class RankGuild
extends RankActivity
{
public RankGuild(ActivityBO bo) {
super(bo);
}

public void onEnd() {
for (Record record : RankManager.getInstance().getRankList(RankType.Guild, RankManager.getInstance().getRankSize(RankType.Guild))) {
if (record == null)
continue; 
int rank = record.getRank();
for (RankActivity.RankAward ref : this.rankrewardList) {
if (!ref.rankrange.within(rank))
continue; 
Guild guild = GuildMgr.getInstance().getGuild(record.getPid());
if (guild != null) {
for (Iterator<Long> iterator = guild.getMembers().iterator(); iterator.hasNext(); ) { long pid = ((Long)iterator.next()).longValue();
MailCenter.getInstance().sendMail(pid, this.ref.MailSender, this.ref.MailTitle, this.ref.MailContent, ref.reward, new String[0]); }

}
} 
} 
}

public ActivityType getType() {
return ActivityType.GuildRank;
}

public ConstEnum.VIPGiftType getAwardType() {
return ConstEnum.VIPGiftType.GuildRank;
}

public ActivityRecordBO createPlayerActRecord(Player player) {
ActivityRecordBO bo = new ActivityRecordBO();
bo.setPid(player.getPid());
bo.setAid(this.bo.getId());
bo.setActivity(getType().toString());
Guild guild = ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getGuild();
if (guild != null) {
int power = guild.getPower();
bo.setExtInt(0, power);
} 
bo.insert();
return bo;
}
}

