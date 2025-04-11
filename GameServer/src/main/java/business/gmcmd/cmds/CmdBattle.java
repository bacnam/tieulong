package business.gmcmd.cmds;

import BaseCommon.CommLog;
import business.global.battle.SimulatBattle;
import business.global.battle.detail.WorldbossBattle;
import business.gmcmd.annotation.Command;
import business.gmcmd.annotation.Commander;
import business.player.Player;
import business.player.PlayerMgr;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefWorldBoss;

@Commander(name = "battle", comment = "模拟战斗")
public class CmdBattle {
    @Command(comment = "模拟战斗[玩家id]")
    public String fight(Player player, long t) {
        Player target = PlayerMgr.getInstance().getPlayer(t);
        if (target == null) {
            return String.format("玩家不存在", new Object[0]);
        }
        SimulatBattle battle = new SimulatBattle(player, target);
        battle.fight();

        return String.format("模拟结束", new Object[0]);
    }

    @Command(comment = "模拟战斗[怪物id]")
    public String worldboss(Player player, int bossId) {
        RefWorldBoss ref = (RefWorldBoss) RefDataMgr.get(RefWorldBoss.class, Integer.valueOf(bossId));
        WorldbossBattle battle = new WorldbossBattle(player, ref.BossId);
        battle.init(bossId);
        battle.fight();
        CommLog.error((new StringBuilder(String.valueOf(battle.getDamage()))).toString());
        return String.format("模拟结束", new Object[0]);
    }
}

