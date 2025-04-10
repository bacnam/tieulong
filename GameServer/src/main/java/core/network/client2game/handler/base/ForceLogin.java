package core.network.client2game.handler.base;

import business.player.Player;
import business.player.PlayerMgr;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.ClientSession;
import core.network.client2game.handler.BaseHandler;
import core.server.ServerConfig;
import java.io.IOException;

public class ForceLogin
extends BaseHandler
{
private static class Request
{
public int loginkey;
public long pid;
}

public void handle(WebSocketRequest request, String message) throws IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
int loginkey = ServerConfig.getLoginKey();
if (loginkey == 0 || req.loginkey != loginkey) {
request.error(ErrorCode.InvalidParam, "loginkey 错误", new Object[0]);
return;
} 
Player p = PlayerMgr.getInstance().getPlayer(req.pid);
if (p == null) {
request.error(ErrorCode.Player_NotFound, "指定玩家" + req.pid + "不存在", new Object[0]);

return;
} 
ClientSession session = (ClientSession)request.getSession();
session.setValid(true);

PlayerMgr.getInstance().connectPlayer(session, p);
session.setOpenId(p.getOpenId());
session.setPlayerSid(p.getPlayerBO().getSid());
request.response();
}
}

