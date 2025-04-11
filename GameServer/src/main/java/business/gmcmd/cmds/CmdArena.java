package business.gmcmd.cmds;

import business.global.arena.ArenaConfig;
import business.global.arena.ArenaManager;
import business.global.arena.Competitor;
import business.gmcmd.annotation.Command;
import business.gmcmd.annotation.Commander;
import business.player.Player;
import business.player.feature.features.PlayerRecord;
import com.zhonglian.server.common.enums.ConstEnum;

@Commander(name = "arena", comment = "竞技场")
public class CmdArena {
    @Command(comment = "设置挑战次数")
    public String times(Player player, int times) {
        ((PlayerRecord) player.getFeature(PlayerRecord.class)).setValue(ConstEnum.DailyRefresh.ArenaChallenge, ArenaConfig.maxChallengeTimes() - times);
        return "ok";
    }

    @Command(comment = "设置挑战CD")
    public String fightCD(Player player, int cd) {
        ArenaManager.getInstance().getOrCreate(player.getPid()).setFightCD(cd);
        return "ok";
    }

    @Command(comment = "设置刷新CD")
    public String refreshCD(Player player, int cd) {
        ArenaManager.getInstance().getOrCreate(player.getPid()).setRefreshCD(cd);
        return "ok";
    }

    @Command(comment = "设置排名")
    public String rank(Player player, int rank) {
        Competitor competitor = ArenaManager.getInstance().getOrCreate(player.getPid());
        ArenaManager.getInstance().swithRank(competitor.getRank(), rank);
        return "ok";
    }

    @Command(comment = "设置排名")
    public String settle(Player player) {
        ArenaManager.getInstance().settle();
        return "ok";
    }
}

