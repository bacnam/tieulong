package business.player.feature.features;

import BaseCommon.CommLog;
import business.global.activity.ActivityMgr;
import business.global.activity.detail.AccumRechargeDay;
import business.global.activity.detail.ActivityInstance;
import business.global.activity.detail.DailyRecharge;
import business.global.activity.detail.DailySign;
import business.global.activity.detail.FortuneWheel;
import business.global.activity.detail.NewFirstReward;
import business.global.activity.detail.SignInOpenServer;
import business.global.activity.detail.SignInPrize;
import business.global.guild.Guild;
import business.player.Player;
import business.player.feature.achievement.AchievementFeature;
import business.player.feature.guild.GuildMemberFeature;
import business.player.feature.marry.MarryFeature;
import business.player.feature.player.TitleFeature;
import business.player.feature.pve.InstanceFeature;
import business.player.feature.pvp.DroiyanFeature;
import business.player.feature.pvp.StealGoldFeature;
import business.player.feature.pvp.WorshipFeature;
import business.player.feature.store.StoreFeature;
import business.player.feature.store.StoreRecord;
import business.player.feature.task.TaskActivityFeature;
import business.player.feature.treasure.FindTreasureFeature;
import business.player.feature.worldboss.WorldBossFeature;
import com.zhonglian.server.common.mgr.daily.BaseDailyRefreshEvent;
import com.zhonglian.server.common.mgr.daily.IDailyRefreshRef;

public class PlayerDailyRefresh
extends BaseDailyRefreshEvent
{
public static abstract class IPlayerDailyRefresh
{
public abstract void doEvent(Player param1Player, IDailyRefreshRef param1IDailyRefreshRef, int param1Int);
}

public PlayerDailyRefresh(IDailyRefreshRef ref, DailyRefreshFeature container) {
super(ref, container);
}

public void doEvent(int times) {
DailyRefreshFeature containerP = (DailyRefreshFeature)this.dailyRefresh;
Player player = containerP.getPlayer();
player.lockIns();
try {
doEvent(player, times);
} catch (Exception e) {
CommLog.error("玩家{}执行刷新发生异常", Long.valueOf(player.getPid()), e);
} 
player.unlockIns();
} private void doEvent(Player player, int times) {
GuildMemberFeature guildMember;
Guild guild;
switch (this.ref.getEventTypes()) {

case null:
((StoreRecord)player.getFeature(StoreRecord.class)).dailyRefresh();

((PlayerRecord)player.getFeature(PlayerRecord.class)).clearValue0();

((AchievementFeature)player.getFeature(AchievementFeature.class)).dailyRefresh();

((TitleFeature)player.getFeature(TitleFeature.class)).dailyRefresh();

((TaskActivityFeature)player.getFeature(TaskActivityFeature.class)).dailyRefresh();

((DailySign)ActivityMgr.getActivity(DailySign.class)).handDailyRefresh(player, times);

((SignInOpenServer)ActivityMgr.getActivity(SignInOpenServer.class)).handDailyRefresh(player);

((SignInPrize)ActivityMgr.getActivity(SignInPrize.class)).handDailyRefresh(player);

((ActivityInstance)ActivityMgr.getActivity(ActivityInstance.class)).handDailyRefresh(player);

((RechargeFeature)player.getFeature(RechargeFeature.class)).desRebateRemains(times);

((WorshipFeature)player.getFeature(WorshipFeature.class)).getBO().saveWorshipTimesAll(0);

((DailyRecharge)ActivityMgr.getActivity(DailyRecharge.class)).dailyRechargeRefresh(player);

((DroiyanFeature)player.getFeature(DroiyanFeature.class)).getBo().savePoint(0);

((DroiyanFeature)player.getFeature(DroiyanFeature.class)).getBo().saveWinTimes(0);

((WorldBossFeature)player.getFeature(WorldBossFeature.class)).dailyRefresh();

((InstanceFeature)player.getFeature(InstanceFeature.class)).dailyRefresh();

((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).dailyEvent();

((StealGoldFeature)player.getFeature(StealGoldFeature.class)).dailyRefresh();

((NewFirstReward)ActivityMgr.getActivity(NewFirstReward.class)).dailyRefesh(player, times);

((FindTreasureFeature)player.getFeature(FindTreasureFeature.class)).getOrCreate().saveTimes(0);
((FindTreasureFeature)player.getFeature(FindTreasureFeature.class)).getOrCreate().saveTentimes(0);

((MarryFeature)player.getFeature(MarryFeature.class)).dailyRefresh();

((TitleFeature)player.getFeature(TitleFeature.class)).checkAllTitle();

((AccumRechargeDay)ActivityMgr.getActivity(AccumRechargeDay.class)).dailyRefresh(player);

((FortuneWheel)ActivityMgr.getActivity(FortuneWheel.class)).dailyRefresh(player);
break;

case Every_h2:
guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
guild = guildMember.getGuild();
if (guild != null && guild.isInOpenHour()) {
guildMember.refreshChallengeTimes();
}
break;
case Every_h3:
((StoreFeature)player.getFeature(StoreFeature.class)).doAutoRefresh();
break;
} 
}
}

