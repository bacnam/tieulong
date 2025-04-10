package business.gmcmd.cmds;

import business.gmcmd.annotation.Command;
import business.gmcmd.annotation.Commander;
import business.player.Player;
import business.player.feature.pve.InstanceFeature;

@Commander(name = "instance", comment = "副本相关命令")
public class CmdInstance
{
@Command(comment = "刷新所有副本挑战信息")
public String refresh(Player player) {
((InstanceFeature)player.getFeature(InstanceFeature.class)).dailyRefresh();
return String.format("刷新玩家副本挑战信息成功", new Object[0]);
}

@Command(comment = "设置副本挑战次数")
public String set(Player player, int times) {
((InstanceFeature)player.getFeature(InstanceFeature.class)).getOrCreate().saveChallengTimesAll(times);
return String.format("ok", new Object[0]);
}
}

