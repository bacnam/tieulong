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

public class ForceLogin extends BaseHandler {
    @Override
    public void handle(WebSocketRequest request, String message) throws IOException {
        ForceLogin.Request req = new Gson().fromJson(message, ForceLogin.Request.class);
        int loginkey = ServerConfig.getLoginKey();
        if (loginkey != 0 && req.loginkey == loginkey) {
            Player p = PlayerMgr.getInstance().getPlayer(req.pid);
            if (p == null) {
                request.error(ErrorCode.Player_NotFound, "指定玩家" + req.pid + "不存在");
            } else {
                ClientSession session = (ClientSession)request.getSession();
                session.setValid(true);
                PlayerMgr.getInstance().connectPlayer(session, p);
                session.setOpenId(p.getOpenId());
                session.setPlayerSid(p.getPlayerBO().getSid());
                request.response();
            }
        } else {
            request.error(ErrorCode.InvalidParam, "loginkey 错误");
        }
    }

    private static class Request {
        public int loginkey;
        public long pid;
    }
}
