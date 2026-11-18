package core.network.client2game.handler.guildwar;

import business.global.guild.GuildWarMgr;
import business.player.Player;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class GuildWarRebirth extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        GuildWarRebirth.Request req = new Gson().fromJson(message, GuildWarRebirth.Request.class);
        request.response(GuildWarMgr.getInstance().rebirth(player, req.centerId));
    }

    public static class Request {
        int centerId;
    }
}
