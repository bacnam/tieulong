package core.network.world2game.handler.base;

import business.global.guild.Guild;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.character.CharFeature;
import business.player.feature.guild.GuildMemberFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.Config;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.world2game.handler.WBaseHandler;

public class GetPlayerInfo
        extends WBaseHandler {
    public void handle(WebSocketRequest request, String message) throws WSException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);

        Player.PlayerInfo info = new Player.PlayerInfo();
        Player player = PlayerMgr.getInstance().getPlayer(req.pid);
        if (player != null) {
            info.pid = player.getPid();
            info.icon = player.getPlayerBO().getIcon();
            info.name = player.getName();
            info.lv = player.getLv();
            info.vipLv = player.getVipLevel();
            Guild guild = ((GuildMemberFeature) player.getFeature(GuildMemberFeature.class)).getGuild();
            if (guild != null) {
                info.guildID = guild.getGuildId();
                info.guildName = guild.getName();
            }
            info.serverId = Config.ServerID();
            info.maxPower = ((CharFeature) player.getFeature(CharFeature.class)).getPower();
        }
        request.response(info);
    }

    private static class Request {
        long pid;
    }
}

