package core.network.client2game.handler.guildwar;

import business.global.guild.GuildWarMgr;
import business.player.Player;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.GuildWarFightProtol;

import java.io.IOException;

public class GuildWarFightInfo
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        GuildWarFightProtol info = GuildWarMgr.getInstance().getFightInfo(req.centerId, player);
        request.response(info);
    }

    public static class Request {
        int centerId;
    }
}

