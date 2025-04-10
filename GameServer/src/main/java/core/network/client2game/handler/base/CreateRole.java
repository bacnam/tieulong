package core.network.client2game.handler.base;

import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerBase;
import com.google.gson.Gson;
import com.zhonglian.server.common.mgr.sensitive.SensitiveWordMgr;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.ClientSession;
import core.network.client2game.handler.BaseHandler;
import java.io.IOException;

public class CreateRole
extends BaseHandler
{
public static class RoleCreate
{
String name;
int selected;
}

public void handle(WebSocketRequest request, String message) throws IOException {
RoleCreate req = (RoleCreate)(new Gson()).fromJson(message, RoleCreate.class);

ClientSession session = (ClientSession)request.getSession();

Player player = PlayerMgr.getInstance().getPlayer(session.getOpenId(), session.getPlayerSid());
if (player == null) {
request.error(ErrorCode.Player_NotFound, "玩家未创号", new Object[0]);
return;
} 
if (SensitiveWordMgr.getInstance().isContainsSensitiveWord(req.name)) {
request.error(ErrorCode.InvalidParam, "玩家名字包含敏感词", new Object[0]);

return;
} 
String name = "s" + session.getPlayerSid() + "." + req.name;
ErrorCode rslt = PlayerMgr.getInstance().createRole(player, name, req.selected);
if (rslt == ErrorCode.Player_AlreadyExist) {
request.error(rslt, "玩家重名了，请换一个名字", new Object[0]); return;
} 
if (rslt != ErrorCode.Success) {
request.error(rslt, "创号时发生错误, 详细错误信息看服务端控制台or游戏服日志", new Object[0]);

return;
} 
request.response(((PlayerBase)session.getPlayer().getFeature(PlayerBase.class)).fullInfo());
}
}

