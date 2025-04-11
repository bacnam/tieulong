package business.gmcmd.cmds;

import business.gmcmd.annotation.Command;
import business.gmcmd.annotation.Commander;
import business.player.Player;
import business.player.item.Reward;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefReward;

@Commander(name = "test", comment = "测试相关命令")
public class CmdTest {
    @Command(comment = "测试奖励生成")
    public String reward(Player player, int id) {
        RefReward ref = (RefReward) RefDataMgr.get(RefReward.class, Integer.valueOf(id));
        Reward reward = ref.genReward();
        return reward.toString();
    }
}

