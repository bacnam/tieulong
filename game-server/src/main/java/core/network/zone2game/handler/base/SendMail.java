package core.network.zone2game.handler.base;

import business.global.gmmail.MailCenter;
import business.player.PlayerMgr;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.zone2game.handler.ZBaseHandler;

public class SendMail extends ZBaseHandler {
    @Override
    public void handle(WebSocketRequest request, String message) throws WSException {
        proto.common.Mail.SendMail req = new Gson().fromJson(message, proto.common.Mail.SendMail.class);
        if (PlayerMgr.getInstance().getPlayer(req.scid) == null) {
            throw new WSException(ErrorCode.Player_NotFound, "%s玩家不存在，无法发送邮件", req.scid);
        } else {
            MailCenter.getInstance().sendMail(req.scid, req.mailID, req.uniformIDList, req.uniformCountList, req.params);
            request.response();
        }
    }
}
