package core.network.client2game.handler.pvp;

import business.global.battle.SimulatBattle;
import business.player.Player;
import business.player.PlayerMgr;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.TeamInfo;
import java.io.IOException;

public class Simulate
extends PlayerHandler
{
private static class Request
{
long targetPid;
}

private static class Response
{
long fightid;
TeamInfo team;
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request begin = (Request)(new Gson()).fromJson(message, Request.class);
Player target = PlayerMgr.getInstance().getPlayer(begin.targetPid);
if (target == null) {
throw new WSException(ErrorCode.Player_NotFound, "玩家%s不存在", new Object[] { Long.valueOf(begin.targetPid) });
}

SimulatBattle battle = new SimulatBattle(player, target);
battle.fight();
request.response(battle.proto());
}
}

