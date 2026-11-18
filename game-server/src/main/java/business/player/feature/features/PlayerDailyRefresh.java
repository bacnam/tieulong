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

public class PlayerDailyRefresh extends BaseDailyRefreshEvent {
    public PlayerDailyRefresh(IDailyRefreshRef ref, DailyRefreshFeature container) {
        super(ref, container);
    }

    @Override
    public void doEvent(int times) {
        DailyRefreshFeature containerP = (DailyRefreshFeature)this.dailyRefresh;
        Player player = containerP.getPlayer();
        player.lockIns();

        try {
            this.doEvent(player, times);
        } catch (Exception var5) {
            CommLog.error("玩家{}执行刷新发生异常", player.getPid(), var5);
        }

        player.unlockIns();
    }

    private void doEvent(Player player, int times) {
        switch (this.ref.getEventTypes()) {
            case Day_h0:
                player.getFeature(StoreRecord.class).dailyRefresh();
                player.getFeature(PlayerRecord.class).clearValue0();
                player.getFeature(AchievementFeature.class).dailyRefresh();
                player.getFeature(TitleFeature.class).dailyRefresh();
                player.getFeature(TaskActivityFeature.class).dailyRefresh();
                ActivityMgr.getActivity(DailySign.class).handDailyRefresh(player, times);
                ActivityMgr.getActivity(SignInOpenServer.class).handDailyRefresh(player);
                ActivityMgr.getActivity(SignInPrize.class).handDailyRefresh(player);
                ActivityMgr.getActivity(ActivityInstance.class).handDailyRefresh(player);
                player.getFeature(RechargeFeature.class).desRebateRemains(times);
                player.getFeature(WorshipFeature.class).getBO().saveWorshipTimesAll(0);
                ActivityMgr.getActivity(DailyRecharge.class).dailyRechargeRefresh(player);
                player.getFeature(DroiyanFeature.class).getBo().savePoint(0);
                player.getFeature(DroiyanFeature.class).getBo().saveWinTimes(0);
                player.getFeature(WorldBossFeature.class).dailyRefresh();
                player.getFeature(InstanceFeature.class).dailyRefresh();
                player.getFeature(GuildMemberFeature.class).dailyEvent();
                player.getFeature(StealGoldFeature.class).dailyRefresh();
                ActivityMgr.getActivity(NewFirstReward.class).dailyRefesh(player, times);
                player.getFeature(FindTreasureFeature.class).getOrCreate().saveTimes(0);
                player.getFeature(FindTreasureFeature.class).getOrCreate().saveTentimes(0);
                player.getFeature(MarryFeature.class).dailyRefresh();
                player.getFeature(TitleFeature.class).checkAllTitle();
                ActivityMgr.getActivity(AccumRechargeDay.class).dailyRefresh(player);
                ActivityMgr.getActivity(FortuneWheel.class).dailyRefresh(player);
            case Day_h4:
            case Day_h8:
            case Day_h9:
            case Day_h10:
            case Day_h11:
            case Day_h12:
            case Day_h13:
            case Day_h15:
            case Day_h16:
            case Day_h18:
            case Day_h19:
            case Day_h20:
            case Day_h21:
            case Day_h22:
            case Day_h14:
            case Week_d1h4:
            case Week_d1h0:
            case Every_m5:
            case Every_m15:
            case Every_m30:
            case Every_h1:
            case Every_h4:
            default:
                break;
            case Every_h2:
                GuildMemberFeature guildMember = player.getFeature(GuildMemberFeature.class);
                Guild guild = guildMember.getGuild();
                if (guild != null && guild.isInOpenHour()) {
                    guildMember.refreshChallengeTimes();
                }
                break;
            case Every_h3:
                player.getFeature(StoreFeature.class).doAutoRefresh();
        }
    }

    public abstract static class IPlayerDailyRefresh {
        public abstract void doEvent(Player var1, IDailyRefreshRef var2, int var3);
    }
}
