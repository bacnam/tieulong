package business.global.activity.detail;

import business.global.activity.RankActivity;
import business.global.gmmail.MailCenter;
import business.global.rank.RankManager;
import business.global.rank.Record;
import business.player.Player;
import business.player.feature.character.CharFeature;
import business.player.feature.character.Character;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.RankType;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;

public class RankWing
extends RankActivity
{
public RankWing(ActivityBO bo) {
super(bo);
}

public void onEnd() {
for (Record record : RankManager.getInstance().getRankList(RankType.WingLevel, RankManager.getInstance().getRankSize(RankType.WingLevel))) {
if (record == null)
continue; 
int rank = record.getRank();
for (RankActivity.RankAward ref : this.rankrewardList) {
if (!ref.rankrange.within(rank))
continue; 
MailCenter.getInstance().sendMail(record.getPid(), this.ref.MailSender, this.ref.MailTitle, this.ref.MailContent, ref.reward, new String[0]);
} 
} 
}

public ActivityType getType() {
return ActivityType.WingRank;
}

public ConstEnum.VIPGiftType getAwardType() {
return ConstEnum.VIPGiftType.WingRank;
}

public ActivityRecordBO createPlayerActRecord(Player player) {
ActivityRecordBO bo = new ActivityRecordBO();
bo.setPid(player.getPid());
bo.setAid(this.bo.getId());
bo.setActivity(getType().toString());
int wingLevel = 0;
for (Character charac : ((CharFeature)player.getFeature(CharFeature.class)).getAll().values()) {
wingLevel += charac.getBo().getWing();
}
bo.setExtInt(0, wingLevel);
bo.insert();
return bo;
}
}

