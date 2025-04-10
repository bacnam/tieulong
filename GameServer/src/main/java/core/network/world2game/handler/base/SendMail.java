package core.network.world2game.handler.base;

import business.global.gmmail.MailCenter;
import business.player.PlayerMgr;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.world2game.handler.WBaseHandler;
import proto.common.Mail;

public class SendMail
extends WBaseHandler
{
public void handle(WebSocketRequest request, String message) throws WSException {
Mail.SendMail req = (Mail.SendMail)(new Gson()).fromJson(message, Mail.SendMail.class);

if (PlayerMgr.getInstance().getPlayer(req.scid) == null) {
throw new WSException(ErrorCode.Player_NotFound, "%s玩家不存在，无法发送邮件", new Object[] { Long.valueOf(req.scid) });
}
MailCenter.getInstance().sendMail(req.scid, req.mailID, req.uniformIDList, req.uniformCountList, req.params);

request.response();
}
}

