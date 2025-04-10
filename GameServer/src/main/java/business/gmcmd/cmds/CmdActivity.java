package business.gmcmd.cmds;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.AccumRechargeDay;
import business.global.activity.detail.RankWing;
import business.gmcmd.annotation.Command;
import business.gmcmd.annotation.Commander;
import business.player.Player;

@Commander(name = "activity", comment = "商城相关命令")
public class CmdActivity
{
@Command(comment = "羽翼榜活动结束")
public String closewingrank(Player player) {
RankWing wingRank = (RankWing)ActivityMgr.getActivity(RankWing.class);
wingRank.onClosed();
return "ok";
}

@Command(comment = "连续充值")
public String accumday(Player player) {
((AccumRechargeDay)ActivityMgr.getActivity(AccumRechargeDay.class)).dailyRefresh(player);
return "ok";
}
}

