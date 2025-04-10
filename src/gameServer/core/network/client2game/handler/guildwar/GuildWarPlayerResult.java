package core.network.client2game.handler.guildwar;

import business.global.guild.GuildWarMgr;
import business.player.Player;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class GuildWarPlayerResult
extends PlayerHandler
{
public static class Request
{
long sid;
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
request.response((GuildWarMgr.getInstance()).fightBattle.get(Long.valueOf(req.sid)));
}
}

