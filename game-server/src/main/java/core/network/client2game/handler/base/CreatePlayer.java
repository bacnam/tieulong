package core.network.client2game.handler.base;

import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerBase;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.ClientSession;
import core.network.client2game.handler.BaseHandler;
import java.io.IOException;

public class CreatePlayer extends BaseHandler {
    @Override
    public void handle(WebSocketRequest request, String message) throws IOException {
        ClientSession session = (ClientSession)request.getSession();
        Player player = PlayerMgr.getInstance().getPlayer(session.getOpenId(), session.getPlayerSid());
        if (player != null) {
            request.error(ErrorCode.Player_AlreadyExist, "玩家已经有账号了，禁止重复创号");
        } else {
            ErrorCode rslt = PlayerMgr.getInstance().createPlayerFirst(session, session.getOpenId(), session.getPlayerSid());
            if (rslt != ErrorCode.Success) {
                request.error(rslt, "创号时发生错误, 详细错误信息看服务端控制台or游戏服日志");
            } else {
                request.response(session.getPlayer().getFeature(PlayerBase.class).fullInfo());
            }
        }
    }
}
