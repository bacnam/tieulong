package business.gmcmd.cmds;

import business.global.chat.ChatMgr;
import business.gmcmd.annotation.Command;
import business.gmcmd.annotation.Commander;
import business.player.Player;
import core.network.game2world.WorldConnector;

@Commander(name = "world", comment = "跨服相关命令")
public class CmdWorld {
    @Command(comment = "测试跨服请求")
    public String test(Player player) {
        WorldConnector.request("wforward.client.Test", "", null);

        return "ok";
    }

    @Command(comment = "测试聊天")
    public String chat(Player player) {
        ChatMgr.getInstance().init();
        return "ok";
    }

    @Command(comment = "测试聊天2")
    public String chat2(Player player) {
        return "ok";
    }
}

