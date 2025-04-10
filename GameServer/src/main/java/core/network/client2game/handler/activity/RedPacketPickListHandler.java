package core.network.client2game.handler.activity;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.RedPacket;
import business.global.redpacket.RedPacketMgr;
import business.player.Player;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.RedPacketInfo;
import core.network.proto.RedPacketPickInfo;
import java.io.IOException;
import java.util.List;

public class RedPacketPickListHandler
extends PlayerHandler
{
class Request
{
long id;
}

private static class Response {
RedPacketInfo info;
List<RedPacketPickInfo> pickList;

public Response(RedPacketInfo info, List<RedPacketPickInfo> pickList) {
this.info = info;
this.pickList = pickList;
}
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
RedPacket packet = (RedPacket)ActivityMgr.getActivity(RedPacket.class);
if (packet.getStatus() == ActivityStatus.Close) {
throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", new Object[] { packet.getType() });
}
RedPacketInfo info = RedPacketMgr.getInstance().getPacket(req.id, player);
List<RedPacketPickInfo> pickList = packet.getPickList(req.id);
request.response(new Response(info, pickList));
}
}

