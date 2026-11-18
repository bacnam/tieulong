package business.global.refresh;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.AllPeopleReward;
import business.global.activity.detail.Lottery;
import business.global.activity.detail.SignInSeven;
import business.global.arena.ArenaManager;
import business.global.guild.GuildMgr;
import business.global.guild.GuildWarMgr;
import business.global.rank.RankManager;
import business.global.worldboss.WorldBossMgr;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.mgr.daily.BaseDailyRefreshEvent;
import com.zhonglian.server.common.mgr.daily.IDailyRefreshRef;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefWorldBoss;

public class WorldDailyRefresh extends BaseDailyRefreshEvent {
    public WorldDailyRefresh(IDailyRefreshRef ref, WorldDailyRefreshContainer container) {
        super(ref, container);
    }

    @Override
    public void doEvent(int times) {
        switch (this.ref.getEventTypes()) {
            case Day_h0:
                ArenaManager.getInstance().settle();
                RankManager.getInstance().settle(RankType.Droiyan, true);
                GuildMgr.getInstance().dailyEvent();
                ActivityMgr.getActivity(Lottery.class).dailyRefresh();
                GuildWarMgr.getInstance().dailyRefresh();
                ActivityMgr.getActivity(SignInSeven.class).handDailyRefresh();
                break;
            case Day_h4:
                for (RefWorldBoss ref : RefDataMgr.getAll(RefWorldBoss.class).values()) {
                    WorldBossMgr.getInstance().dailyRefreshWorldBoss(ref.id);
                }
            case Day_h8:
            case Day_h9:
            case Day_h10:
            case Day_h20:
            case Day_h14:
            case Week_d1h4:
            case Every_m5:
            case Every_m15:
            case Every_m30:
            case Every_h1:
            default:
                break;
            case Day_h11:
                GuildMgr.getInstance().longnvEvent();
                break;
            case Day_h12:
                WorldBossMgr.getInstance().autoFight(1);
                break;
            case Day_h13:
                WorldBossMgr.getInstance().sendRankReward(1, false);
                break;
            case Day_h15:
                WorldBossMgr.getInstance().autoFight(2);
                break;
            case Day_h16:
                WorldBossMgr.getInstance().sendRankReward(2, false);
                break;
            case Day_h18:
                WorldBossMgr.getInstance().autoFight(3);
                break;
            case Day_h19:
                WorldBossMgr.getInstance().sendRankReward(3, false);
                GuildWarMgr.getInstance().guildWarEvent();
                break;
            case Day_h21:
                WorldBossMgr.getInstance().autoFight(4);
                break;
            case Day_h22:
                WorldBossMgr.getInstance().sendRankReward(4, false);
                break;
            case Week_d1h0:
                ActivityMgr.getActivity(AllPeopleReward.class).weekEvent();
        }
    }
}
