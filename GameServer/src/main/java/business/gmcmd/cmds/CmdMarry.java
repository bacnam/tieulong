package business.gmcmd.cmds;

import business.gmcmd.annotation.Command;
import business.gmcmd.annotation.Commander;
import business.player.Player;
import business.player.feature.marry.MarryFeature;

@Commander(name = "marry", comment = "婚恋相关命令")
public class CmdMarry
{
@Command(comment = "刷新婚恋")
public String signrefresh(Player player) {
((MarryFeature)player.getFeature(MarryFeature.class)).dailyRefresh();
return "ok";
}
}

